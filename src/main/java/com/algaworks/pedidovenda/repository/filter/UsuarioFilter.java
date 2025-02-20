package com.algaworks.pedidovenda.repository.filter;

import java.io.Serializable;

import com.algaworks.pedidovenda.validation.SKU;

public class UsuarioFilter implements Serializable{

	private static final long serialVersionUID = 1L;
	
	
	private String sku;
	private String nome;
	
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
	
	
}
