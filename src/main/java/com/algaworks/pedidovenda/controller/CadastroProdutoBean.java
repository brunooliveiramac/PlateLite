package com.algaworks.pedidovenda.controller;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.validation.constraints.NotNull;

import com.algaworks.pedidovenda.model.Categoria;
import com.algaworks.pedidovenda.model.Produto;
import com.algaworks.pedidovenda.model.Subcategoria;
import com.algaworks.pedidovenda.repository.Categorias;
import com.algaworks.pedidovenda.service.CadastroProdutoService;
import com.algaworks.pedidovenda.util.jsf.FacesUtil;

@Named
@ViewScoped
public class CadastroProdutoBean implements Serializable {

	private static final long serialVersionUID = 1L;


	@Inject
	private Categorias categorias;
	
	@Inject
	private CadastroProdutoService cadastroProdutoService;
	
	
	
	private Produto produto;
	
	private Categoria categoriaPai;
	
	
	private List<Categoria> categoriasRaizes;
	
	private List<Subcategoria> subcategorias;
	
	public List<Subcategoria> getSubcategorias() {
		return subcategorias;
	}

	@NotNull
	public Categoria getCategoriaPai() {
		return categoriaPai;
	}

	public void setCategoriaPai(Categoria categoriaPai) {
		this.categoriaPai = categoriaPai;
	}

	public CadastroProdutoBean() {
		limpar();
	}
	
	public void inicializar() {
		System.out.println("Inicializando...");
		//Para não fazer a consulta novamente
		if(FacesUtil.isNotPostBack()){
			categoriasRaizes = categorias.raiz();
				//Carrega as subcategorias p/ editar
				if(categoriaPai != null){
					carregarSubcategorias();
				}
		}
		
	
	}
	
	public void carregarSubcategorias(){
		subcategorias = categorias.subcategoriasDe(categoriaPai);
	}
	
	private void limpar(){
		produto = new Produto();
		categoriaPai = null;
		subcategorias = new  ArrayList<>();
	}
	
	
	public void salvar(){
		this.produto.setCategoria(this.categoriaPai);
		this.produto = cadastroProdutoService.salvar(this.produto);
		limpar();
		FacesUtil.addInfoMessage("Produto salvo com sucesso!");
	}





	public Produto getProduto() {
		return produto;
	}

	public void setProduto(Produto produto) {
		this.produto = produto;
		
		if(this.produto != null){
			this.categoriaPai = this.produto.getCategoria();
		}
	}

	public List<Categoria> getCategoriasRaizes() {
		return categoriasRaizes;
	}
	
		
	public boolean isEditando(){
		return this.produto.getId() != null;
	}
	
	
}