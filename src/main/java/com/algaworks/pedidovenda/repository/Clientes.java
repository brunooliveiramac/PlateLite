package com.algaworks.pedidovenda.repository;

import java.io.Serializable;
import java.util.List;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceException;

import org.apache.commons.lang3.StringUtils;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;

import com.algaworks.pedidovenda.model.Cliente;
import com.algaworks.pedidovenda.model.Produto;
import com.algaworks.pedidovenda.model.Usuario;
import com.algaworks.pedidovenda.repository.filter.ClienteFilter;
import com.algaworks.pedidovenda.repository.filter.UsuarioFilter;
import com.algaworks.pedidovenda.service.NegocioException;
import com.algaworks.pedidovenda.util.jpa.Transactional;

public class Clientes implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	@Inject
	private EntityManager manager;
	
	public Cliente guardar(Cliente cliente){
		return	cliente = manager.merge(cliente);
		}
	
	public Cliente porId(Long id) {
		return manager.find(Cliente.class, id);
	}
	
	public Cliente porEmail(String email) {
		// TODO Auto-generated method stub
		try {
			return manager
					.createQuery("from Cliente where email = :email",
							Cliente.class)
					.setParameter("email", email).getSingleResult();
		} catch (NoResultException e) {
			return null;
		}

	}


	@SuppressWarnings("unchecked")
	public List<Cliente> filtrados(ClienteFilter filtro) {

		Session session = manager.unwrap(Session.class);
		
		Criteria criteria = session.createCriteria(Cliente.class);
		if(StringUtils.isNotBlank(filtro.getNome())){
			criteria.add(Restrictions.like("nome", filtro.getNome(),MatchMode.ANYWHERE));
		}
		
		return criteria.addOrder(Order.asc("nome")).list();

	}
	
	@Transactional
	public void remover(Cliente cliente){
		try{
			cliente = porId(cliente.getId());
			manager.remove(cliente);
			//Completa a excução da exclusão - Se o produto estiver sendo usado por outra tabela
			//cancela a exclusão e envia um erro para o usuário
			//Chave estrangeira
			manager.flush();
		}catch(PersistenceException e){
			throw new NegocioException("Usuario não pode ser excluído.");
		}
		
	}
	
	public List<Cliente> porNome(String nome) {
		return this.manager.createQuery("from Cliente " +
				"where upper(nome) like :nome", Cliente.class)
				.setParameter("nome", nome.toUpperCase() + "%")
				.getResultList();
	}
	
	
}
