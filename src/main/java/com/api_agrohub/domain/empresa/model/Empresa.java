package com.api_agrohub.domain.empresa.model;


import java.beans.Transient;
import java.time.LocalDateTime;

import org.hibernate.annotations.Filter;
import org.hibernate.annotations.FilterDef;
import org.hibernate.annotations.ParamDef;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "empresa")
@Filter(name = "tenantFilter", condition = "id_tenant = :tenantId")
public class Empresa {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_empresa")
    @SequenceGenerator(name = "seq_empresa", sequenceName = "seq_empresa", allocationSize = 1)
    private Long id_empresa;

    @Column(name = "id_tenant", nullable = false)
    private String id_tenant;

    @NotBlank(message = "O nome é obrigatorio!")
    private String cd_empresa;

    @NotBlank(message = "O nome é obrigatorio!")
    private String nm_empresa;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_planoassinatura", insertable = false, updatable = false)
    @JsonIgnore
    private PlanoAssinatura planoassinatura;

    @Column(name = "id_planoassinatura")
    private Long id_planoassinatura;

    private String ds_email;
    
    private String nu_telefone;

    private String accessToken;

    private String webhookUrl;

    private boolean fl_ativo = true;

    @Column(name = "dt_cadastro", nullable = false, updatable = false)
    private LocalDateTime dt_cadastro;

    @PrePersist
    protected void onCreate() {
        this.dt_cadastro = LocalDateTime.now();
    }

 
    @JsonProperty("nm_planoassinatura")
    public String getNm_planoassinatura() {

        if (planoassinatura != null) {
            return planoassinatura.getNm_planoassinatura();
        }
        return null;    
    }


}
