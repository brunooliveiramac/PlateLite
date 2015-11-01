package com.algaworks.pedidovenda.controller;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
















import javax.faces.bean.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import com.algaworks.pedidovenda.model.Categoria;
import com.algaworks.pedidovenda.model.Produto;
import com.algaworks.pedidovenda.model.Subcategoria;
import com.algaworks.pedidovenda.repository.Categorias;
import com.algaworks.pedidovenda.repository.Produtos;
import com.algaworks.pedidovenda.repository.Subcategorias;
import com.algaworks.pedidovenda.repository.filter.CategoriaFilter;
import com.algaworks.pedidovenda.repository.filter.ProdutoFilter;
import com.algaworks.pedidovenda.repository.filter.SubCategoriaFilter;
import com.algaworks.pedidovenda.util.jsf.FacesUtil;

@Named
@ViewScoped
public class PesquisaSubCategoriasBean implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private SubCategoriaFilter filtro;


	private List<Subcategoria> subcategoriasFiltrados;
	private Subcategoria subcategoriaSelecionada;
	



	
	public void excluir(){
		//Produto atribuido no xhtml
		subcategorias.remover(this.subcategoriaSelecionada);
		subcategoriasFiltrados.remove(subcategoriaSelecionada);
		
		FacesUtil.addInfoMessage("Subcategoria excluida com sucesso.");
	}
	

	@Inject
	private Subcategorias subcategorias;

	
	public void pesquisar(){
		subcategoriasFiltrados = subcategorias.filtrados(filtro);
	}
	
	


	public PesquisaSubCategoriasBean() {
		filtro = new SubCategoriaFilter();
	}


	

	public SubCategoriaFilter getFiltro() {
		return filtro;
	}




	public void setFiltro(SubCategoriaFilter filtro) {
		this.filtro = filtro;
	}







	public List<Subcategoria> getSubcategoriasFiltrados() {
		return subcategoriasFiltrados;
	}




	public void setSubcategoriasFiltrados(List<Subcategoria> subcategoriasFiltrados) {
		this.subcategoriasFiltrados = subcategoriasFiltrados;
	}




	public Subcategoria getSubcategoriaSelecionada() {
		return subcategoriaSelecionada;
	}




	public void setSubcategoriaSelecionada(Subcategoria subcategoriaSelecionada) {
		this.subcategoriaSelecionada = subcategoriaSelecionada;
	}















	
}