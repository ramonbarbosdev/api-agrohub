package com.api_agrohub.domain.empresa.model;


import java.time.LocalDateTime;

import com.api_agrohub.domain.empresa.model.PlanoAssinatura;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;

@Entity
@Table(name = "plano_assinatura")
public class PlanoAssinatura {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_planoassinatura")
    @SequenceGenerator(name = "seq_planoassinatura", sequenceName = "seq_planoassinatura", allocationSize = 1)
    private Long id_planoassinatura; 

    @NotBlank(message = "O nome é obrigatorio!")
    private String nm_planoassinatura;

    private Double vl_mensal;

    private int nu_limitemensagens;

    private int nu_limiteatendentes;

    @Column(name = "dt_cadastro", nullable = false, updatable = false)
    private LocalDateTime dt_cadastro;

    @PrePersist
    protected void onCreate() {
        this.dt_cadastro = LocalDateTime.now();
    }

    public Long getId_planoassinatura() {
        return id_planoassinatura;
    }

    public void setId_planoassinatura(Long id_planoassinatura) {
        this.id_planoassinatura = id_planoassinatura;
    }



    public Double getVl_mensal() {
        return vl_mensal;
    }

    public void setVl_mensal(Double vl_mensal) {
        this.vl_mensal = vl_mensal;
    }

    public int getNu_limitemensagens() {
        return nu_limitemensagens;
    }

    public void setNu_limitemensagens(int nu_limitemensagens) {
        this.nu_limitemensagens = nu_limitemensagens;
    }

    public int getNu_limiteatendentes() {
        return nu_limiteatendentes;
    }

    public void setNu_limiteatendentes(int nu_limiteatendentes) {
        this.nu_limiteatendentes = nu_limiteatendentes;
    }

    public LocalDateTime getDt_cadastro() {
        return dt_cadastro;
    }

    public void setDt_cadastro(LocalDateTime dt_cadastro) {
        this.dt_cadastro = dt_cadastro;
    }

    public String getNm_planoassinatura() {
        return nm_planoassinatura;
    }

    public void setNm_planoassinatura(String nm_planoassinatura) {
        this.nm_planoassinatura = nm_planoassinatura;
    }

    

}
