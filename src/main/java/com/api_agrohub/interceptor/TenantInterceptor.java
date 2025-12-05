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
            "/agrohub/swagger-ui",
            "/agrohub/v3/api-docs",
            "/agrohub/swagger-resources",
            "/agrohub/webjars",
            "/agrohub/auth",
            // "/agrohub/login"
    };

    private boolean isPublic(HttpServletRequest request) {
        String path = request.getRequestURI();
        for (String p : PUBLIC_PATHS) {
            if (path.startsWith(p))
                return true;
        }
        return false;
    }

    private boolean isLocalhost(HttpServletRequest request) {
        String host = request.getServerName();
        return host.contains("localhost") || "127.0.0.1".equals(host);
    }

    private String resolverTenant(HttpServletRequest request) {

        String host = request.getServerName();

        String tenantId = TenantContext.getTenantId();

        if (isLocalhost(request)) {

            // 2) Se não tiver header, tenta pegar do JWT (talvez você já coloque o tenant
            // no token)
            String authHeader = request.getHeader("Authorization");
            if (authHeader != null && !authHeader.isBlank()) {
                String tenantToken = jwtService.extractTenantId(authHeader);
                if (tenantToken != null && !tenantToken.isBlank()) {
                    return tenantToken;
                }
            }

            // 3) Se ainda não tiver tenant (ex.: login, /public), retorna null
            return null;
        }

        String hostSemPorta = host.split(":")[0]; // remove :8080 se tiver
        String subdomain = hostSemPorta.split("\\.")[0];

        return tenantRepository.findTenantIdBySubdomain(subdomain)
                .orElseThrow(() -> new RuntimeException("❌ Tenant não encontrado para domínio: " + subdomain));
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
            throws Exception {

        String tenantId = resolverTenant(request);

        // Se conseguimos resolver tenant (DEV ou PROD), coloca no contexto
        if (tenantId != null && !tenantId.isBlank()) {
            TenantContext.setTenantId(tenantId);
        }

        // Rotas públicas (login, cadastro, /public, etc.)
        if (isPublic(request)) {
            return true;
        }

        // Daqui pra baixo: rota protegida → precisa de token
        String authHeader = request.getHeader("Authorization");
        if (authHeader == null || authHeader.isBlank()) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.getWriter().write("{\"error\":\"Token ausente\"}");
            return false;
        }

        String tenantToken = jwtService.extractTenantId(authHeader);

        // 🔹 DEV: se ainda não tinha tenant (ex: localhost), usa o do token
        if (isLocalhost(request)) {
            if (tenantId == null || tenantId.isBlank()) {
                TenantContext.setTenantId(tenantToken);
                return true;
            }
            // se já tinha (ex.: header X-Tenant-ID), garante que bate
            if (!tenantId.equalsIgnoreCase(tenantToken)) {
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                response.getWriter().write("{\"error\":\"Token não pertence a este tenant (DEV)\"}");
                return false;
            }
            return true;
        }

        // 🔹 PROD: precisa bater domain.tenant x token.tenant
        if (!tenantId.equalsIgnoreCase(tenantToken)) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.getWriter().write("{\"error\":\"Token não pertence a este tenant\"}");
            return false;
        }

        return true;
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler,
            Exception ex) {
        TenantContext.clear();
    }
}
