package com.api_agrohub.domain.usuario.dto;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import com.api_agrohub.domain.empresa.model.UsuarioEmpresa;
import com.api_agrohub.domain.usuario.model.Usuario;

public class UsuarioDTO implements Serializable {

	private Long userId;
	private String userLogin;
	private String userNome;
	private String userSenha;
	private String userImg;
	private List<String> roles = new ArrayList<>();
	public List<UsuarioEmpresa> itensUsuarioEmpresa = new ArrayList<UsuarioEmpresa>();

    public UsuarioDTO(Usuario usuario) {
		this.userId = usuario.getId();
		this.userLogin = usuario.getLogin();
		this.userNome = usuario.getNome();
		this.userSenha = usuario.getSenha();
		this.userImg = usuario.getImg();
		this.itensUsuarioEmpresa = usuario.getItensUsuarioEmpresa();

		if (usuario.getRoles() != null && !usuario.getRoles().isEmpty()) {
			this.roles = usuario.getRoles().stream()
					.map(role -> role.getNomeRole())
					.collect(Collectors.toList());
		}
	}

	public List<String> getRoles() {
		return roles;
	}

	public void setRoles(List<String> roles) {
		this.roles = roles;
	}

	public String getUserImg() {
		return userImg;
	}

	public void setUserImg(String userImg) {
		this.userImg = userImg;
	}

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public String getUserLogin() {
		return userLogin;
	}

	public void setUserLogin(String userLogin) {
		this.userLogin = userLogin;
	}

	public String getUserNome() {
		return userNome;
	}

	public void setUserNome(String userNome) {
		this.userNome = userNome;
	}

	public String getUserSenha() {
		return userSenha;
	}

	public void setUserSenha(String userSenha) {
		this.userSenha = userSenha;
	}

	public List<UsuarioEmpresa> getItensUsuarioEmpresa() {
		return itensUsuarioEmpresa;
	}

	public void setItensUsuarioEmpresa(List<UsuarioEmpresa> itensUsuarioEmpresa) {
		this.itensUsuarioEmpresa = itensUsuarioEmpresa;
	}
}