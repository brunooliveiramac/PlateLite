package com.algaworks.pedidovenda.repository;

import java.io.Serializable;
import java.util.List;

import javax.inject.Inject;
import javax.persistence.EntityManager;

import com.algaworks.pedidovenda.model.Categoria;
import com.algaworks.pedidovenda.model.Grupo;
import com.algaworks.pedidovenda.model.Produto;

public class Grupos implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	@Inject
	private EntityManager manager;
	
	public Grupo porId(Long id) {
		return manager.find(Grupo.class, id);
	}
	
	//Busca grupo
	public List<Grupo> raiz(){
		return manager.createQuery("from Grupo", Grupo.class).getResultList();
	}
	
	
	public Grupo guardar(Grupo grupo) {
		return	grupo = manager.merge(grupo);
	}
	
}
