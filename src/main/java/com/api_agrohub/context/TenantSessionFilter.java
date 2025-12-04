package com.api_agrohub.context;

import org.hibernate.Session;
import org.springframework.stereotype.Component;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;

@Component
public class TenantSessionFilter {

    @PersistenceContext
    private EntityManager entityManager;

    public void enableFilter() {
        String tenantId = TenantContext.getTenantId();
        if (tenantId != null) {
            Session session = entityManager.unwrap(Session.class);
            session.enableFilter("tenantFilter")
                    .setParameter("tenantId", tenantId);
        }
    }
}