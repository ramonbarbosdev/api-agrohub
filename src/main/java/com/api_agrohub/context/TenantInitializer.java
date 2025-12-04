package com.api_agrohub.context;

import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.api_agrohub.domain.empresa.model.Tenant;
import com.api_agrohub.domain.empresa.repository.TenantRepository;
import com.api_agrohub.util.TenantUtil;

@Configuration
public class TenantInitializer {
    @Bean
    public ApplicationRunner initTenant(TenantRepository tenantRepository) {
        return args -> {
            if (tenantRepository.count() == 0) {
                Tenant tenant = new Tenant();
                tenant.setIdTenant(TenantUtil.generateTenantId());
                tenant.setNomeTenant("Tenant ROOT");
                tenant.setSubdomain("localhost");
                // tenant.setTheme("{\"primary\":\"#4CAF50\"}");
                // tenant.setFeatures("{\"admin\": true, \"visita\": true}");
                tenant.setAtivo(true);

                tenantRepository.save(tenant);

                System.out.println("🎯 Tenant ROOT criado automaticamente!");
            }
        };
    }
}
