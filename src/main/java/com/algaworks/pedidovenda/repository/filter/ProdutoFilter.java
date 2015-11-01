package com.algaworks.pedidovenda.repository.filter;

import java.io.Serializable;

import com.algaworks.pedidovenda.validation.SKU;

public class ProdutoFilter implements Serializable{

	private static final long serialVersionUID = 1L;
	
	
	private String sku;
	private String nome;
	private String categoria;
	private String subcategoria;
	
	@SKU
	public String getSku() {
		return sku;
	}
	public void setSku(String sku) {
		//Ternario
		//Se SKU for Nulo "então" atribuo Nulo "senão" toUpercsa (Maiusculo)
		this.sku = sku == null ? null : sku.toUpperCase();
	}
	public String getNome() {
		return nome;
	}
	public void setNome(String nome) {
		this.nome = nome;
	}
	public String getCategoria() {
		return categoria;
	}
	public void setCategoria(String categoria) {
		this.categoria = categoria;
	}
	public String getSubcategoria() {
		return subcategoria;
	}
	public void setSubcategoria(String subcategoria) {
		this.subcategoria = subcategoria;
	}
	
	
	
	
}
