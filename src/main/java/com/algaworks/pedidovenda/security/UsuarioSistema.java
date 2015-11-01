package com.algaworks.pedidovenda.security;

import java.util.Collection;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;

import com.algaworks.pedidovenda.model.Usuario;

//Classe que representa o usuario logado no sistema
public class UsuarioSistema extends User {

	
	private static final long serialVersionUID = 1L;
	
	//Vai representar um usuario
	
	private Usuario usuario;
	
	public UsuarioSistema(Usuario usuario, Collection<? extends GrantedAuthority> authorities) {
		super(usuario.getEmail(), usuario.getSenha(), authorities);
		this.usuario = usuario;

	}

	public Usuario getUsuario() {
		return usuario;
	}

}
