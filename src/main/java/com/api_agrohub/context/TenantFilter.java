package com.api_agrohub.context;

import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import com.api_agrohub.domain.empresa.repository.TenantRepository;

import java.io.IOException;

// @Order(1)
// @Component
public class TenantFilter implements Filter {

    @Autowired
    private TenantRepository repository;

   @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
        throws IOException, ServletException {

        // HttpServletRequest req = (HttpServletRequest) request;
        // String host = req.getServerName(); // agrox.seusistema.com

        // String subdomain = host.split("\\.")[0]; // agrox

        // // Se for ambiente dev (localhost:8080)
        // if ("localhost".equals(subdomain)) {
        //     TenantContext.setCurrentTenant("DEV");
        // } 
        // // Se for admin (painel do desenvolvedor)
        // else if ("admin".equals(subdomain)) {
        //     TenantContext.setCurrentTenant("ADMIN_PANEL");
        // } 
        // else {
        //     String tenantId = repository
        //         .findIdTenantBySubdomain(subdomain)
        //         .orElseThrow(() -> new RuntimeException("Tenant não encontrado!"));

        //     TenantContext.setCurrentTenant(tenantId);
        // }

        // try {
        //     chain.doFilter(request, response);
        // } finally {
        //     TenantContext.clear();
        // }
    }
}
