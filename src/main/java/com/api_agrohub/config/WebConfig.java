package com.api_agrohub.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import com.api_agrohub.interceptor.TenantInterceptor;


@Configuration
public class WebConfig implements WebMvcConfigurer {

    // @Autowired
    // private PermissaoInterceptor permissaoInterceptor;

    // @Override
    // public void addInterceptors(InterceptorRegistry registry) {
    // registry.addInterceptor(permissaoInterceptor)
    // .excludePathPatterns("/auth/**", "/swagger/**", "/v3/**");// todas as rotas
    // }
    @Autowired
    private TenantInterceptor tenantInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(tenantInterceptor)
        .excludePathPatterns("/empresa/**", "/auth/**");
    }

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/uploads/**")
                .addResourceLocations("file:uploads/");
    }
}