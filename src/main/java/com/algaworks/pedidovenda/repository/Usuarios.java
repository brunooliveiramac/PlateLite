package com.algaworks.pedidovenda.repository;

import java.io.Serializable;
import java.util.List;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceException;

import org.apache.commons.lang3.StringUtils;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;

import com.algaworks.pedidovenda.model.Produto;
import com.algaworks.pedidovenda.model.Usuario;
import com.algaworks.pedidovenda.repository.filter.ProdutoFilter;
import com.algaworks.pedidovenda.repository.filter.UsuarioFilter;
import com.algaworks.pedidovenda.service.NegocioException;
import com.algaworks.pedidovenda.util.jpa.Transactional;
	
public class Usuarios implements Serializable {

	
	private static final long serialVersionUID = 1L;
	
	@Inject
	private EntityManager manager;
	
	

	public Usuario guardar(Usuario usuario) {
		return	usuario = manager.merge(usuario);
	}
	

	public Usuario porId(Long id) {
		return manager.find(Usuario.class, id);
	}


	@SuppressWarnings("unchecked")
	public List<Usuario> filtrados(UsuarioFilter filtro) {

		Session session = manager.unwrap(Session.class);
		
		Criteria criteria = session.createCriteria(Usuario.class);
		if(StringUtils.isNotBlank(filtro.getNome())){
			criteria.add(Restrictions.like("nome", filtro.getNome(),MatchMode.ANYWHERE));
		}
		
		return criteria.addOrder(Order.asc("nome")).list();

	}
	
	@Transactional
	public void remover(Usuario usuario){
		try{
			usuario = porId(usuario.getId());
			manager.remove(usuario);
			//Completa a excução da exclusão - Se o produto estiver sendo usado por outra tabela
			//cancela a exclusão e envia um erro para o usuário
			//Chave estrangeira
			manager.flush();
		}catch(PersistenceException e){
			throw new NegocioException("Cliente não pode ser excluído.");
		}
		
	}
	
	public List<Usuario> vendedores() {
		// TODO filtrar apenas vendedores (por um grupo específico)
		return this.manager.createQuery("from Usuario", Usuario.class)
				.getResultList();
	}


	public Usuario porEmail(String email) {
		Usuario usuario = null;
		
		try{
		usuario = this.manager.createQuery("from Usuario where lower(email) = :email", Usuario.class)
				.setParameter("email", email.toLowerCase()).getSingleResult(); // Single para retornar apenas um usuario
		
		}catch(NoResultException e){
			e.printStackTrace();
		}
		return usuario;
		
		
	}
	
	
	

}
