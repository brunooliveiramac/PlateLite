package com.algaworks.pedidovenda.controller;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;










import javax.faces.bean.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import com.algaworks.pedidovenda.model.Produto;
import com.algaworks.pedidovenda.repository.Produtos;
import com.algaworks.pedidovenda.repository.filter.ProdutoFilter;
import com.algaworks.pedidovenda.util.jsf.FacesUtil;

@Named
@ViewScoped
public class PesquisaProdutosBean implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private ProdutoFilter filtro;
	public ProdutoFilter getFiltro() {
		return filtro;
	}

	private List<Produto> produtosFiltrados;
	private Produto produtoSelecionado;
	
	public Produto getProdutoSelecionado() {
		return produtoSelecionado;
	}


	public void setProdutoSelecionado(Produto produtoSelecionado) {
		this.produtoSelecionado = produtoSelecionado;
	}
	
	public void excluir(){
		//Produto atribuido no xhtml
		produtos.remover(produtoSelecionado);
		produtosFiltrados.remove(produtoSelecionado);
		
		FacesUtil.addInfoMessage("Produto " + produtoSelecionado.getSku() +" excluido com sucesso.");
	}
	

	@Inject
	private Produtos produtos;

	
	public void pesquisar(){
			produtosFiltrados = produtos.filtrados(filtro);
	}
	
	
	public List<Produto> getProdutosFiltrados() {
		return produtosFiltrados;
	}

	public PesquisaProdutosBean() {
		filtro = new ProdutoFilter();
	}


	
}