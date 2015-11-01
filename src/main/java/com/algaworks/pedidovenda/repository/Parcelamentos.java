package com.algaworks.pedidovenda.repository;

import java.io.Serializable;

import javax.inject.Inject;
import javax.persistence.EntityManager;

import com.algaworks.pedidovenda.model.Parcelamento;

public class Parcelamentos implements Serializable{


	private static final long serialVersionUID = 1L;
	
	@Inject
	private EntityManager entityManager;
	
	public Parcelamento guardar(Parcelamento parcelamento) {
		return this.entityManager.merge(parcelamento);
	}
	
	

}
