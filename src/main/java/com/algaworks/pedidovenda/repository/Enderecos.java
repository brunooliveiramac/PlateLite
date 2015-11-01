package com.algaworks.pedidovenda.repository;


import com.algaworks.pedidovenda.model.Categoria;
import com.algaworks.pedidovenda.model.Cliente;
import com.algaworks.pedidovenda.model.Endereco;

import java.io.Serializable;
import java.util.List;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;



public class Enderecos implements Serializable {
    
    private static final long serialVersionUID = 1L;
    @Inject
    private EntityManager manager;

	public Endereco guardar(Endereco endereco){

		return	endereco = manager.merge(endereco);
		}
    
    
    public Endereco porId(Long id) {
        try {
            return manager.createQuery("from Endereco where id = :id", Endereco.class).setParameter("id", id).getSingleResult();
        } catch (NoResultException e) {
            return null;
        }
    }
    
    public Cliente porIdCliente(Long id) {
		return manager.find(Cliente.class, id);
	}
    public List<Cliente> idCliente(){
    	return manager.createQuery("select id from Cliente", Cliente.class).getResultList();
    	
    }
    
    
    public List<Endereco> enderecosPorId(Cliente id){
		return manager.createQuery("from Endereco where cliente_id = :idCliente",
				Endereco.class)
				.setParameter("idCliente", id)
				.getResultList();
	}
    
    
  
    
   
}