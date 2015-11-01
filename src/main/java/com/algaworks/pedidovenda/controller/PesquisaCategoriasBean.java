package com.algaworks.pedidovenda.controller;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;













import javax.faces.bean.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import com.algaworks.pedidovenda.model.Categoria;
import com.algaworks.pedidovenda.model.Produto;
import com.algaworks.pedidovenda.repository.Categorias;
import com.algaworks.pedidovenda.repository.Produtos;
import com.algaworks.pedidovenda.repository.filter.CategoriaFilter;
import com.algaworks.pedidovenda.repository.filter.ProdutoFilter;
import com.algaworks.pedidovenda.util.jsf.FacesUtil;

@Named
@ViewScoped
public class PesquisaCategoriasBean implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private CategoriaFilter filtro;


	private List<Categoria> categoriasFiltrados;
	private Categoria categoriaSelecionada;
	



	
	public void excluir(){
		//Produto atribuido no xhtml
		categorias.remover(this.categoriaSelecionada);
		categoriasFiltrados.remove(categoriaSelecionada);
		
		FacesUtil.addInfoMessage("Categoria excluida com sucesso.");
	}
	

	@Inject
	private Categorias categorias;

	
	public void pesquisar(){
		categoriasFiltrados = categorias.filtrados(filtro);
	}
	
	


	public PesquisaCategoriasBean() {
		filtro = new CategoriaFilter();
	}


	public CategoriaFilter getFiltro() {
		return filtro;
	}


	public void setFiltro(CategoriaFilter filtro) {
		this.filtro = filtro;
	}


	public List<Categoria> getCategoriasFiltrados() {
		return categoriasFiltrados;
	}


	public void setCategoriasFiltrados(List<Categoria> categoriasFiltrados) {
		this.categoriasFiltrados = categoriasFiltrados;
	}


	public Categoria getCategoriaSelecionada() {
		return categoriaSelecionada;
	}


	public void setCategoriaSelecionada(Categoria categoriaSelecionada) {
		this.categoriaSelecionada = categoriaSelecionada;
	}


	
}