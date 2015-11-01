package com.algaworks.pedidovenda.service;

import java.io.Serializable;

import javax.inject.Inject;

import com.algaworks.pedidovenda.model.Categoria;
import com.algaworks.pedidovenda.model.Subcategoria;
import com.algaworks.pedidovenda.repository.Categorias;
import com.algaworks.pedidovenda.repository.Subcategorias;
import com.algaworks.pedidovenda.util.jpa.Transactional;

public class CadastroSubCategoriaService implements Serializable {
	
	
	private static final long serialVersionUID = 1L;
		
	
	
	
	@Inject
	private Subcategorias subcategorias;
	
	@Transactional
	public Subcategoria salva(Subcategoria subcategoria){
		return subcategorias.guarda(subcategoria);
	}

}
