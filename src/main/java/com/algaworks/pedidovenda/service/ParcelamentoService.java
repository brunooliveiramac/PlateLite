package com.algaworks.pedidovenda.service;

import java.io.Serializable;

import javax.inject.Inject;
import javax.persistence.EntityManager;

import com.algaworks.pedidovenda.model.Parcelamento;
import com.algaworks.pedidovenda.repository.Parcelamentos;
import com.algaworks.pedidovenda.util.jpa.Transactional;

public class ParcelamentoService implements Serializable{


	private static final long serialVersionUID = 1L;
	
	@Inject
	private Parcelamentos parcelamentos;
	
	@Transactional
	public Parcelamento save(Parcelamento parcelamento) {
		return parcelamento = this.parcelamentos.guardar(parcelamento);
			
	}


}
