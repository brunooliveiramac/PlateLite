package com.algaworks.pedidovenda.teste;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;

import com.algaworks.pedidovenda.model.Cliente;
import com.algaworks.pedidovenda.model.Endereco;
import com.algaworks.pedidovenda.model.TipoPessoa;

public class Teste {
	public static void main(String[] args) {
		//Fabricas
		EntityManagerFactory factory = Persistence.createEntityManagerFactory("PedidoPU");
		EntityManager manager = factory.createEntityManager();
		
		//Primeiro, criar transação
		EntityTransaction trx = manager.getTransaction();
		//iniciaTrasação
		trx.begin();
		
	
		
		trx.commit();
	}
}
