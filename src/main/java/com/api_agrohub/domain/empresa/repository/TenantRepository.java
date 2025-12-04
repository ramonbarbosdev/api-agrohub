package com.api_agrohub.domain.empresa.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.api_agrohub.domain.empresa.model.Empresa;
import com.api_agrohub.domain.empresa.model.Tenant;

import jakarta.transaction.Transactional;

@Repository
@Transactional
public interface TenantRepository extends JpaRepository<Tenant, String> {

        @Query("SELECT t.idTenant FROM Tenant t WHERE t.subdomain = :subdomain")
        Optional<String> findTenantIdBySubdomain(String subdomain);
}
