package com.api_agrohub.domain.usuario.model;

import java.util.ArrayList;
import java.util.List;

import org.springframework.security.core.GrantedAuthority;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;

@Entity
@Table(name = "role")
@SequenceGenerator(name = "seq_role", sequenceName = "seq_role", allocationSize = 1, initialValue = 1)
public class Role implements GrantedAuthority {
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_role")
	@SequenceGenerator(
        name = "seq_role",     
        sequenceName = "seq_role",
        allocationSize = 1
    )
	private Long id;

	@NotBlank(message = "O nome é obrigatorio!")
	private String nomeRole; // Permissão

	@OneToMany(mappedBy = "id_role", cascade = CascadeType.MERGE, fetch = FetchType.EAGER)
	public List<RotaPermission> itensRotaPermission = new ArrayList<RotaPermission>();


	@Override
	public String getAuthority() { /* Retorna o nome no papel, acesso ou autorização */
		// TODO Auto-generated method stub
		return this.nomeRole;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getNomeRole() {
		return nomeRole;
	}

	public void setNomeRole(String nomeRole) {
		this.nomeRole = nomeRole;
	}

	public List<RotaPermission> getItensRotaPermission() {
		return itensRotaPermission;
	}
	public void setItensRotaPermission(List<RotaPermission> itensRotaPermission) {
		this.itensRotaPermission = itensRotaPermission;
	}

}
