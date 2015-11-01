package com.algaworks.pedidovenda.service;

import java.io.Serializable;

import javax.inject.Inject;

import com.algaworks.pedidovenda.model.Grupo;
import com.algaworks.pedidovenda.model.Produto;
import com.algaworks.pedidovenda.repository.Grupos;
import com.algaworks.pedidovenda.repository.Produtos;
import com.algaworks.pedidovenda.util.jpa.Transactional;

//Responsavel pelas regras de negocio de cadastro de produtos
//Bean CDI

//IMplementa serezlizable por ser ViewEscoped
public class CadastroGrupoService implements Serializable{

	
	private static final long serialVersionUID = 1L;
	
	@Inject
	private Grupos grupos;
	
	@Transactional
	public Grupo salvar(Grupo grupo){
	
		return grupos.guardar(grupo);
	}

}
