package com.api_agrohub.domain.usuario.model;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

import jakarta.persistence.CollectionTable;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
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

@Entity
@Table(name = "rota_permission")
@SequenceGenerator(name = "seq_rotapermission", sequenceName = "seq_rotapermission", allocationSize = 1, initialValue = 1)
public class RotaPermission {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id_rotapermission;

    @NotBlank(message = "A rota é obrigatoria!")
    private String path; 

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "rota_permission_methods", joinColumns = @JoinColumn(name = "id_rotapermission"))
    @Column(name = "method")
    private Set<String> methods = new HashSet<>(); 

    private boolean fl_permitido; 

    @ManyToOne()
    @JoinColumn(name = "id_role", insertable = false, updatable = false)
    private Role role;

    @Column(name = "id_role")
    private Long id_role;

    @Column(name = "dt_cadastro", nullable = false, updatable = false)
    private LocalDateTime dt_cadastro;

    @PrePersist
    protected void onCreate() {
        this.dt_cadastro = LocalDateTime.now();
    }

    public Long getId_rotapermission() {
        return id_rotapermission;
    }

    public void setId_rotapermission(Long id_rotapermission) {
        this.id_rotapermission = id_rotapermission;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public Set<String> getMethods() {
        return methods;
    }

    public void setMethods(Set<String> methods) {
        this.methods = methods;
    }

    public boolean isFl_permitido() {
        return fl_permitido;
    }

    public void setFl_permitido(boolean fl_permitido) {
        this.fl_permitido = fl_permitido;
    }


    public Long getId_role() {
        return id_role;
    }

    public void setId_role(Long id_role) {
        this.id_role = id_role;
    }

    public LocalDateTime getDt_cadastro() {
        return dt_cadastro;
    }

    public void setDt_cadastro(LocalDateTime dt_cadastro) {
        this.dt_cadastro = dt_cadastro;
    }


}
