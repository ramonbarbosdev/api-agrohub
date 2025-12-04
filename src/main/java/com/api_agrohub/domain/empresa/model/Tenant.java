package com.api_agrohub.domain.empresa.model;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

import com.api_agrohub.util.TenantUtil;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;

import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "tenant")
public class Tenant {

    @Id
    @Column(name = "id_tenant", length = 50)
    private String idTenant;

    @NotBlank(message = "O nome é obrigatorio!")
    @Column(name = "nm_tenant")
    private String nomeTenant;

    @NotBlank(message = "O subdominio é obrigatorio!")
    @Column(name = "nm_subdominio")
    private String subdomain;

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(name = "ds_theme")
    private Map<String, Object> theme = new HashMap<>();

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(name = "ds_features")
    private Map<String, Object> features = new HashMap<>();

    @Column(name = "fl_ativo")
    private boolean ativo = true;

    @Column(name = "dt_cadastro", nullable = false, updatable = false)
    private LocalDateTime dataCadastro;

    @PrePersist
    protected void onCreate() {
        this.dataCadastro = LocalDateTime.now();

        if (this.idTenant == null) {
            this.idTenant = TenantUtil.generateTenantId();
        }
    }

}
