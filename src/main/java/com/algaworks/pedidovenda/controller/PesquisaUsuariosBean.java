package com.algaworks.pedidovenda.controller;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;













import javax.faces.bean.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import com.algaworks.pedidovenda.model.Produto;
import com.algaworks.pedidovenda.model.Usuario;
import com.algaworks.pedidovenda.repository.Produtos;
import com.algaworks.pedidovenda.repository.Usuarios;
import com.algaworks.pedidovenda.repository.filter.ProdutoFilter;
import com.algaworks.pedidovenda.repository.filter.UsuarioFilter;
import com.algaworks.pedidovenda.util.jsf.FacesUtil;

@Named
@ViewScoped
public class PesquisaUsuariosBean implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private UsuarioFilter filtro;


	@Inject
	private Usuarios usuarios;

	private List<Usuario> usuariosFiltrados;
	
	private Usuario usuarioSelecionado;
	
	

	public UsuarioFilter getFiltro() {
		return filtro;
	}



	public void setFiltro(UsuarioFilter filtro) {
		this.filtro = filtro;
	}



	public List<Usuario> getUsuariosFiltrados() {
		return usuariosFiltrados;
	}



	public void setUsuariosFiltrados(List<Usuario> usuariosFiltrados) {
		this.usuariosFiltrados = usuariosFiltrados;
	}



	public Usuario getUsuarioSelecionado() {
		return usuarioSelecionado;
	}



	public void setUsuarioSelecionado(Usuario usuarioSelecionado) {
		this.usuarioSelecionado = usuarioSelecionado;
	}


	
	public void pesquisar(){
			usuariosFiltrados = usuarios.filtrados(filtro);
	}
	
	
	
	public PesquisaUsuariosBean() {
		filtro = new UsuarioFilter();
	}
	
	public void excluir(){
		//Produto atribuido no xhtml
		usuarios.remover(usuarioSelecionado);
		usuariosFiltrados.remove(usuarioSelecionado);
		
		FacesUtil.addInfoMessage("Usuario " + usuarioSelecionado.getNome() +" excluido com sucesso.");
	}
	

	
}