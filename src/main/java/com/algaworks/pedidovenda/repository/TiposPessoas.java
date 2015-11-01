package com.algaworks.pedidovenda.repository;

import java.io.Serializable;

import javax.inject.Inject;
import javax.persistence.EntityManager;

import com.algaworks.pedidovenda.model.TipoPessoa;

public class TiposPessoas implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	
	@Inject
	private EntityManager manager;
	
	public TipoPessoa porId(Long id) {
		return manager.find(TipoPessoa.class, id);
	}
}
