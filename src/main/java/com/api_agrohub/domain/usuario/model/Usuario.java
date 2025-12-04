package com.api_agrohub.domain.usuario.model;

import java.io.Serial;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;

import org.hibernate.annotations.Filter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.api_agrohub.domain.empresa.model.UsuarioEmpresa;
import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.OneToMany;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.JoinColumns;
import jakarta.persistence.ForeignKey;

@Entity
@Filter(name = "tenantFilter", condition = "id_tenant = :tenantId")
public class Usuario implements UserDetails {

	/**
	 * 
	 */
	@Serial
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_usuario")
	@SequenceGenerator(name = "seq_usuario", sequenceName = "seq_usuario", allocationSize = 1)
	private Long id;

	@Column(unique = true)
	private String login;

	private String senha;

	private String nome;

	private String token = "";

	private String img;

	@Column(name = "id_tenant", nullable = false)
	private String id_tenant;

	@ManyToMany(fetch = FetchType.EAGER, cascade = CascadeType.MERGE)
	@JoinTable(name = "usuarios_role", joinColumns = @JoinColumn(name = "usuario_id", referencedColumnName = "id", foreignKey = @ForeignKey(name = "fk_usuario_role_usuario")), inverseJoinColumns = @JoinColumn(name = "role_id", referencedColumnName = "id", foreignKey = @ForeignKey(name = "fk_usuario_role_role"))
	// Sem uniqueConstraints aqui se você quer permitir duplicatas (não recomendado)
	)
	private Set<Role> roles = new HashSet<>();

	@OneToMany(mappedBy = "id_usuario", cascade = CascadeType.MERGE, fetch = FetchType.EAGER, orphanRemoval = false)
	public List<UsuarioEmpresa> itensUsuarioEmpresa = new ArrayList<UsuarioEmpresa>();

	public String getImg() {
		return img;
	}

	public void setImg(String img) {
		this.img = img;
	}

	public Set<Role> getRoles() {
		return roles;
	}

	public void setRoles(Set<Role> roles) {
		this.roles = roles;
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getLogin() {
		return login;
	}

	public void setLogin(String login) {
		this.login = login;
	}

	public String getSenha() {
		return senha;
	}

	public void setSenha(String senha) {
		this.senha = senha;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public String getId_tenant() {
		return id_tenant;
	}

	public void setId_tenant(String id_tenant) {
		this.id_tenant = id_tenant;
	}

	public List<UsuarioEmpresa> getItensUsuarioEmpresa() {
		return itensUsuarioEmpresa;
	}

	public void setItensUsuarioEmpresa(List<UsuarioEmpresa> itensUsuarioEmpresa) {
		this.itensUsuarioEmpresa = itensUsuarioEmpresa;
	}

	@Override
	public int hashCode() {
		return Objects.hash(id);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Usuario other = (Usuario) obj;
		return Objects.equals(id, other.id);
	}

	@JsonIgnore
	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {

		return roles;
	}

	@JsonIgnore
	@Override
	public String getPassword() {

		return this.senha;
	}

	@JsonIgnore
	@Override
	public String getUsername() {

		return this.login;
	}

	@JsonIgnore
	@Override
	public boolean isAccountNonExpired() {

		// return UserDetails.super.isAccountNonExpired();
		return true;
	}

	@JsonIgnore
	@Override
	public boolean isAccountNonLocked() {

		// return UserDetails.super.isAccountNonLocked();
		return true;
	}

	@JsonIgnore
	@Override
	public boolean isCredentialsNonExpired() {

		// return UserDetails.super.isCredentialsNonExpired();
		return true;
	}

	@JsonIgnore
	@Override
	public boolean isEnabled() {

		// return UserDetails.super.isEnabled();
		return true;
	}

}
