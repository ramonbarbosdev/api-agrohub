package com.api_agrohub.domain.empresa.model;


import java.time.LocalDateTime;

import com.api_agrohub.domain.usuario.model.Usuario;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
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

@Entity
@Table(name = "usuario_empresa")
public class UsuarioEmpresa {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_usuarioempresa")
    @SequenceGenerator(name = "seq_usuarioempresa", sequenceName = "seq_usuarioempresa", allocationSize = 1)
    private Long id_usuarioempresa;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_usuario", insertable = false, updatable = false)
    @JsonIgnore
    private Usuario usuario;

    @Column(name = "id_usuario")
    private Long id_usuario;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_empresa", insertable = false, updatable = false)
    @JsonIgnore
    private Empresa empresa;

    @Column(name = "id_empresa")
    private Long id_empresa;

    @Column(name = "dt_cadastro", nullable = false, updatable = false)
    private LocalDateTime dt_cadastro;

    @PrePersist
    protected void onCreate() {
        this.dt_cadastro = LocalDateTime.now();
    }

    public Long getId_usuarioempresa() {
        return id_usuarioempresa;
    }

    public void setId_usuarioempresa(Long id_usuarioempresa) {
        this.id_usuarioempresa = id_usuarioempresa;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    public Long getId_usuario() {
        return id_usuario;
    }

    public void setId_usuario(Long id_usuario) {
        this.id_usuario = id_usuario;
    }

    public Empresa getEmpresa() {
        return empresa;
    }

    public void setEmpresa(Empresa empresa) {
        this.empresa = empresa;
    }

    public Long getId_empresa() {
        return id_empresa;
    }

    public void setId_empresa(Long id_empresa) {
        this.id_empresa = id_empresa;
    }

    public LocalDateTime getDt_cadastro() {
        return dt_cadastro;
    }

    public void setDt_cadastro(LocalDateTime dt_cadastro) {
        this.dt_cadastro = dt_cadastro;
    }

}
