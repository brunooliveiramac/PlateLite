package com.algaworks.pedidovenda.service;

import java.io.Serializable;

import javax.inject.Inject;

import com.algaworks.pedidovenda.model.Cliente;
import com.algaworks.pedidovenda.repository.Clientes;
import com.algaworks.pedidovenda.util.jpa.Transactional;

public class CadastroClienteService implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
		
	@Inject
	private Clientes clientes;
	
	//Regras de negocio
	@Transactional
	public Cliente salvar(Cliente cliente){
		Cliente clienteExisteEmail = clientes.porEmail(cliente.getEmail());
		if (clienteExisteEmail != null && !clienteExisteEmail.equals(cliente)) {
			throw new NegocioException("Já existe EMAIL cadastrado!");

		}
		return clientes.guardar(cliente);
	}
	
	
}
