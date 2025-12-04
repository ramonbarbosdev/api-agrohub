package com.api_agrohub.interceptor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import com.api_agrohub.context.TenantContext;
import com.api_agrohub.context.TenantSessionFilter;
import com.api_agrohub.domain.empresa.repository.TenantRepository;
import com.api_agrohub.security.JWTTokenAutenticacaoService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class TenantInterceptor implements HandlerInterceptor {

    @Autowired
    private JWTTokenAutenticacaoService jwtService;

    @Autowired
    private TenantRepository tenantRepository;

    @Autowired
    private TenantSessionFilter tenantSessionFilter;

    private static final String[] PUBLIC_PATHS = {
            "/api-agrohub/swagger-ui",
            "/api-agrohub/v3/api-docs",
            "/api-agrohub/swagger-resources",
            "/api-agrohub/webjars",
            "/api-agrohub/auth",
            "/api-agrohub/login"
    };

    private boolean isPublic(HttpServletRequest request) {
        String path = request.getRequestURI();
        for (String p : PUBLIC_PATHS) {
            if (path.startsWith(p))
                return true;
        }
        return false;
    }

    private String resolverTenantDoDominio(HttpServletRequest request) {

        String host = request.getServerName();
        String subdomain = host.split("\\.")[0];

        if ("localhost".equalsIgnoreCase(subdomain)) {
            return "DEV";
        }
        if ("admin".equalsIgnoreCase(subdomain)) {
            return "ADMIN_PANEL";
        }

        return tenantRepository.findTenantIdBySubdomain(subdomain)
                .orElseThrow(() -> new RuntimeException("❌ Tenant não encontrado para domínio: " + subdomain));
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
            throws Exception {

        if (isPublic(request)) {
            return true;
        }

        String tenantId = resolverTenantDoDominio(request);
        TenantContext.setTenantId(tenantId);

        // ativa filtro do Hibernate para esta requisição
        tenantSessionFilter.enableFilter();

        // valida token se existir
        String authHeader = request.getHeader("Authorization");
        if (authHeader != null && !authHeader.isBlank()) {
            String tenantToken = jwtService.extractTenantId(authHeader);

            if (!tenantId.equalsIgnoreCase(tenantToken)) {
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                response.getWriter().write("{\"error\":\"Token não pertence a este tenant\"}");
                return false;
            }
        }

        return true;
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler,
            Exception ex) {
        TenantContext.clear();
    }
}
