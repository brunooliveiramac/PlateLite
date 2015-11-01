package com.algaworks.pedidovenda.controller;

import javax.inject.Inject;
import javax.persistence.EntityManager;

import com.algaworks.pedidovenda.model.Cliente;
import com.algaworks.pedidovenda.model.Cliente;
import com.algaworks.pedidovenda.repository.TiposPessoas;
import com.algaworks.pedidovenda.service.CadastroClienteService;
import com.algaworks.pedidovenda.util.jpa.JPAUtil;

public class Teste {
			
	public static void main(String[] args) {
		Cliente cliente = new Cliente();
		cliente.setDocumentoReceitaFederal("123123123123");
		cliente.setEmail("AAAA");
		cliente.setNome("Teste");
		
		EntityManager manager = new JPAUtil().getEntityManager();
		manager.getTransaction().begin();
		
		manager.merge(cliente);
	}
}
