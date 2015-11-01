package com.algaworks.pedidovenda.service;

import java.io.Serializable;

import javax.inject.Inject;

import com.algaworks.pedidovenda.model.Produto;
import com.algaworks.pedidovenda.repository.Produtos;
import com.algaworks.pedidovenda.util.jpa.Transactional;

//Responsavel pelas regras de negocio de cadastro de produtos
//Bean CDI

//IMplementa serezlizable por ser ViewEscoped
public class CadastroProdutoService implements Serializable{

	
	private static final long serialVersionUID = 1L;
	
	@Inject
	private Produtos produtos;
	
	@Transactional
	public Produto salvar(Produto produto){
		//Regra de negocio SKU
		//Vai no repositorio de produtos e busca por SKU
		Produto produtoExistente = produtos.porSKU(produto.getSku());
		// && == E					   //Se nao for igual "salva" senão "edita"
		if(produtoExistente != null && !produtoExistente.equals(produto)){
			throw new NegocioException("Já existe SKU cadastrado!");
		}
		return produtos.guardar(produto);
	}

}
