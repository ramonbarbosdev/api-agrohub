package com.api_agrohub.domain.empresa.model;


import java.beans.Transient;
import java.time.LocalDateTime;

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

@JsonIgnoreProperties({ "hibernateLazyInitializer", "handler" })
@Entity
@Table(name = "empresa")
public class Empresa {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_empresa")
    @SequenceGenerator(name = "seq_empresa", sequenceName = "seq_empresa", allocationSize = 1)
    private Long id_empresa;

    @NotBlank(message = "O tenant é obrigatorio!")
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

    public Long getId_empresa() {
        return id_empresa;
    }

    public void setId_empresa(Long id_empresa) {
        this.id_empresa = id_empresa;
    }

    public String getCd_empresa() {
        return cd_empresa;
    }

    public void setCd_empresa(String cd_empresa) {
        this.cd_empresa = cd_empresa;
    }

    public String getId_tenant() {
        return id_tenant;
    }

    public void setId_tenant(String id_tenant) {
        this.id_tenant = id_tenant;
    }

    public String getNm_empresa() {
        return nm_empresa;
    }

    public void setNm_empresa(String nm_empresa) {
        this.nm_empresa = nm_empresa;
    }

    public String getDs_email() {
        return ds_email;
    }

    public void setDs_email(String ds_email) {
        this.ds_email = ds_email;
    }

    public boolean isFl_ativo() {
        return fl_ativo;
    }

    public void setFl_ativo(boolean fl_ativo) {
        this.fl_ativo = fl_ativo;
    }

    public LocalDateTime getDt_cadastro() {
        return dt_cadastro;
    }

    public void setDt_cadastro(LocalDateTime dt_cadastro) {
        this.dt_cadastro = dt_cadastro;
    }

    public PlanoAssinatura getPlanoassinatura() {
        return planoassinatura;
    }

    public void setPlanoassinatura(PlanoAssinatura planoassinatura) {
        this.planoassinatura = planoassinatura;
    }

    public Long getId_planoassinatura() {
        return id_planoassinatura;
    }

    public void setId_planoassinatura(Long id_planoassinatura) {
        this.id_planoassinatura = id_planoassinatura;
    }

    @JsonProperty("nm_planoassinatura")
    public String getNm_planoassinatura() {

        if (planoassinatura != null) {
            return planoassinatura.getNm_planoassinatura();
        }
        return null;    
    }

    public String getNu_telefone() {
        return nu_telefone;
    }

    public void setNu_telefone(String nu_telefone) {
        this.nu_telefone = nu_telefone;
    }

    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    public String getWebhookUrl() {
        return webhookUrl;
    }

    public void setWebhookUrl(String webhookUrl) {
        this.webhookUrl = webhookUrl;
    }

}
