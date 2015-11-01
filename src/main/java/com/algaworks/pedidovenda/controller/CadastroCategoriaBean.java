package com.algaworks.pedidovenda.controller;

import java.io.Serializable;
import java.util.List;

import javax.faces.bean.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import com.algaworks.pedidovenda.model.Categoria;
import com.algaworks.pedidovenda.model.Cliente;
import com.algaworks.pedidovenda.repository.Categorias;
import com.algaworks.pedidovenda.service.CadastroCategoriaService;
import com.algaworks.pedidovenda.util.jsf.FacesUtil;

@Named
@ViewScoped
public class CadastroCategoriaBean implements Serializable {

	private static final long serialVersionUID = 1L;


	public CadastroCategoriaBean() {
			categoria = new Categoria();
	}
	
	@Inject
	private Categorias categorias;
	
	@Inject
	private Categoria categoria;
	
	@Inject
	private CadastroCategoriaService cadastroCategoriaService;
	
	
	public void salva(){
		this.categoria = cadastroCategoriaService.salva(this.categoria);
		categoria = new Categoria();
		FacesUtil.addInfoMessage("Categoria salva com sucesso.");

	}
	
	public List<Categoria> completarCategoria(String nome) {
		return this.categorias.porNome(nome);
	}


	public Categoria getCategoria() {
		return categoria;
	}


	public void setCategoria(Categoria categoria) {
		this.categoria = categoria;
	}
	
	
	public boolean isEditando(){
		return this.categoria.getId() != null;
	}

}
