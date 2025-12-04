package com.api_agrohub.context;

import com.api_agrohub.domain.sistema.model.MultiTenantEntity;

import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;

public class TenantListener {

    @PrePersist
    @PreUpdate
    public void setTenant(MultiTenantEntity entity) {
        entity.setId_tenant(TenantContext.getTenantId());
    }
}
