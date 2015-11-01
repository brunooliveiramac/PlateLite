package com.algaworks.pedidovenda.repository;

import java.io.Serializable;
import java.util.List;

import javax.inject.Inject;
import javax.persistence.EntityManager;

import org.apache.commons.lang3.StringUtils;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;

import com.algaworks.pedidovenda.model.Categoria;
import com.algaworks.pedidovenda.model.Cliente;
import com.algaworks.pedidovenda.model.Subcategoria;
import com.algaworks.pedidovenda.repository.filter.CategoriaFilter;
import com.algaworks.pedidovenda.repository.filter.ClienteFilter;
import com.algaworks.pedidovenda.service.NegocioException;
import com.algaworks.pedidovenda.util.jpa.Transactional;

public class Categorias implements Serializable{

	
	private static final long serialVersionUID = 1L;
	
	@Inject
	private EntityManager manager;
	
	
	public Categoria guarda(Categoria categoria){
	return categoria = manager.merge(categoria);
	
	}
	
	public Categoria porId(Long id) {
		return manager.find(Categoria.class, id);
	}
	
	//Busca categoria
	public List<Categoria> raiz(){
		return manager.createQuery("from Categoria", Categoria.class).getResultList();
	}
	
	//Busca categoria filha
	public List<Subcategoria> subcategoriasDe(Categoria categoriaPai){
		return manager.createQuery("from Subcategoria where categoria_id = :raiz",
				Subcategoria.class)
				.setParameter("raiz", categoriaPai)
				.getResultList();
	}
	
	@SuppressWarnings("unchecked")
	public List<Categoria> filtrados(CategoriaFilter filtro) {

		Session session = manager.unwrap(Session.class);
		
		Criteria criteria = session.createCriteria(Categoria.class);
		if(StringUtils.isNotBlank(filtro.getNome())){
			criteria.add(Restrictions.like("descricao", filtro.getNome(),MatchMode.ANYWHERE));
		}
		
		return criteria.addOrder(Order.asc("descricao")).list();

	}

	public List<Categoria> porNome(String nome) {
		return this.manager.createQuery("from Categoria " +
				"where upper(descricao) like :descricao", Categoria.class)
				.setParameter("descricao", nome.toUpperCase() + "%")
				.getResultList();		
	}

	@Transactional
	public void remover(Categoria categoriaSelecionada) {
		try {
			categoriaSelecionada = porId(categoriaSelecionada.getId());
			manager.remove(categoriaSelecionada);
			manager.flush();
		} catch (Exception e) {
			throw new NegocioException("Categoria possui subcategorias.");
		}
	}


	
}
