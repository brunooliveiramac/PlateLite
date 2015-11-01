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
import com.algaworks.pedidovenda.model.Pedido;
import com.algaworks.pedidovenda.model.Subcategoria;
import com.algaworks.pedidovenda.repository.filter.CategoriaFilter;
import com.algaworks.pedidovenda.repository.filter.ClienteFilter;
import com.algaworks.pedidovenda.repository.filter.SubCategoriaFilter;
import com.algaworks.pedidovenda.service.NegocioException;
import com.algaworks.pedidovenda.util.jpa.Transactional;

public class Subcategorias implements Serializable{

	
	private static final long serialVersionUID = 1L;
	
	@Inject
	private EntityManager manager;
	

	
	public Subcategoria porId(Long id) {
		return manager.find(Subcategoria.class, id);
	}
	
	
	public Subcategoria guarda(Subcategoria categoria){
	return categoria = manager.merge(categoria);
	
	}

	
	@Transactional
	public void remover(Subcategoria subcategoriaSelecionada) {
		try {
			subcategoriaSelecionada = porId(subcategoriaSelecionada.getId());
			manager.remove(subcategoriaSelecionada);
			manager.flush();
		} catch (Exception e) {
			throw new NegocioException("Subcategoria não pode ser excluida.");
		}
	}


	@SuppressWarnings("unchecked")
	public List<Subcategoria> filtrados(SubCategoriaFilter filtro) {
		Session session = this.manager.unwrap(Session.class);

		Criteria criteria = session.createCriteria(Subcategoria.class)
				.createAlias("categoria", "c");
		
		if (StringUtils.isNotBlank(filtro.getCategoria())) {
			// acessamos o nome da categoria associado a subcategoria pelo alias "c"
			// criado anteriormente
			criteria.add(Restrictions.ilike("c.descricao", filtro.getCategoria(),
					MatchMode.ANYWHERE));
		}
		
		if(StringUtils.isNotBlank(filtro.getNome())){
			criteria.add(Restrictions.like("descricao", filtro.getNome(),MatchMode.ANYWHERE));
		}
		
		
		return criteria.addOrder(Order.asc("descricao")).list();
	}




	
}
