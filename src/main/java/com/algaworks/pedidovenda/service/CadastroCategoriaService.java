package com.algaworks.pedidovenda.service;

import java.io.Serializable;

import javax.inject.Inject;

import com.algaworks.pedidovenda.model.Categoria;
import com.algaworks.pedidovenda.repository.Categorias;
import com.algaworks.pedidovenda.util.jpa.Transactional;

public class CadastroCategoriaService implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	
	@Inject
	private Categorias categorias;
	
	@Transactional
	public Categoria salva(Categoria categoria){
		return categorias.guarda(categoria);
	}

}
