package com.algaworks.pedidovenda.repository;

import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.NoResultException;
import javax.persistence.Persistence;
import javax.persistence.PersistenceException;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.hibernate.transform.Transformers;
import org.hibernate.type.StandardBasicTypes;
import org.hibernate.type.Type;

import com.algaworks.pedidovenda.model.ItemPedido;
import com.algaworks.pedidovenda.model.Produto;
import com.algaworks.pedidovenda.model.vo.DataValor;
import com.algaworks.pedidovenda.repository.filter.ProdutoFilter;
import com.algaworks.pedidovenda.service.NegocioException;
import com.algaworks.pedidovenda.util.jpa.Transactional;

public class Produtos implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Inject
	private EntityManager manager;

	public Produto guardar(Produto produto) {
		try {
			produto = manager.merge(produto);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return produto;
	}

	public Produto porSKU(String sku) {
		// TODO Auto-generated method stub
		try {
			return manager
					.createQuery("from Produto where upper(sku) = :sku",
							Produto.class)
					.setParameter("sku", sku.toUpperCase()).getSingleResult();
		} catch (NoResultException e) {
			return null;
		}

	}

	@Transactional
	public void remover(Produto produto) {
		try {
			produto = porId(produto.getId());
			manager.remove(produto);
			// Completa a excução da exclusão - Se o produto estiver sendo usado
			// por outra tabela
			// cancela a exclusão e envia um erro para o usuário
			// Chave estrangeira
			manager.flush();
		} catch (PersistenceException e) {
			throw new NegocioException("Produto não pode ser excluído.");
		}

	}

	@SuppressWarnings("unchecked")
	public List<Produto> filtrados(ProdutoFilter filtro) {
		// Criteria do Hibernate
		Session session = manager.unwrap(Session.class);
		// Criando critério
		Criteria criteria = session.createCriteria(Produto.class)
				.createAlias("categoria", "c")
				.createAlias("subcategoria", "sub");
		if (StringUtils.isNotBlank(filtro.getSku())) {
			// SKU do campo igaul SKU do banco "eq" = "equals"
			criteria.add(Restrictions.eq("sku", filtro.getSku()));
		}

		if (StringUtils.isNotBlank(filtro.getNome())) {
			criteria.add(Restrictions.like("nome", filtro.getNome(),
					MatchMode.ANYWHERE));
		}
		if (StringUtils.isNotBlank(filtro.getCategoria())) {
			// acessamos o nome da categoria associado a subcategoria pelo alias "c"
			// criado anteriormente
			criteria.add(Restrictions.ilike("c.descricao", filtro.getCategoria(),
					MatchMode.ANYWHERE));
		}
		
		if (StringUtils.isNotBlank(filtro.getSubcategoria())) {
			// acessamos o nome da categoria associado a subcategoria pelo alias "c"
			// criado anteriormente
			criteria.add(Restrictions.ilike("sub.descricao", filtro.getSubcategoria(),
					MatchMode.ANYWHERE));
		}

		return criteria.addOrder(Order.asc("nome")).list();
	}

	public Produto porId(Long id) {
		return manager.find(Produto.class, id);
	}

	public List<Produto> porNome(String nome) {// JPQL
		return this.manager
				.createQuery("from Produto where upper(nome) like :nome",
						Produto.class)
				.setParameter("nome", nome.toUpperCase() + "%").getResultList();
	}

	public Produto porSku(String sku) {
		try {
			return manager
					.createQuery("from Produto where upper(sku) = :sku",
							Produto.class)
					.setParameter("sku", sku.toUpperCase()).getSingleResult();
		} catch (NoResultException e) {
			return null;
		}
	}



	@SuppressWarnings("unchecked")
	public static Map<Date, BigDecimal> valorTotalCusto(Integer numeroDias,
			Session session) {

		Calendar dataInicial = Calendar.getInstance();
		dataInicial = DateUtils.truncate(dataInicial, Calendar.DAY_OF_MONTH);
		dataInicial.add(Calendar.DAY_OF_MONTH, numeroDias * -1);

		Map<Date, BigDecimal> resultado = criarMapaVazioFluxo(numeroDias,
				dataInicial);

		Criteria criteria = session.createCriteria(Produto.class);
		criteria.setProjection(
				Projections
						.projectionList()
						.add(Projections.sqlGroupProjection(
								"date(data) as data", "date(data)",
								new String[] { "data" },
								new Type[] { StandardBasicTypes.DATE }))
						.add(Projections.sum("valorcusto").as("valor"))).add(
				Restrictions.ge("data", dataInicial.getTime()));
		List<DataValor> valorTotal = criteria.setResultTransformer(
				Transformers.aliasToBean(DataValor.class)).list();
		// criteria.list();

		return resultado;
	}

	public BigDecimal leftEstoque(String data) {
		BigDecimal value = BigDecimal.ZERO;

		try {
			Connection con = ConectaMySql.obtemConexao();
			PreparedStatement statement = con
					.prepareStatement("SELECT  sum(i.valor_custo * i.quantidade) FROM item_pedido i INNER JOIN pedido p ON p.id = i.pedido_id WHERE p.data_criacao BETWEEN  ?  and  ? ");
			statement.setString(1, data);
			statement.setString(2, data);
			ResultSet result = statement.executeQuery();
			result.next();
			String sum = result.getString(1);
			System.out.println(sum);
			value = new BigDecimal(sum);
			con.close();
		} catch (Exception exc) {
			System.out.println(exc.getMessage());
		}

		return value;
	}
	
	public BigDecimal inEstoque(String data) {
		BigDecimal value = BigDecimal.ZERO;

		try {
			Connection con = ConectaMySql.obtemConexao();
			PreparedStatement statement = con
					.prepareStatement("SELECT  sum(i.valor_custo * i.quantidade_estoque) FROM produto i WHERE i.data_criacao_prod BETWEEN  ?  and  ? ");
			statement.setString(1, data);
			statement.setString(2, data);
			ResultSet result = statement.executeQuery();
			result.next();
			String sum = result.getString(1);
			System.out.println(sum);
			value = new BigDecimal(sum);
			con.close();
		} catch (Exception exc) {
			System.out.println(exc.getMessage());
		}

		return value;
	}

	private static Map<Date, BigDecimal> criarMapaVazioFluxo(
			Integer numeroDias, Calendar dataInicial) {
		dataInicial = (Calendar) dataInicial.clone();
		Map<Date, BigDecimal> mapInicial = new TreeMap<>();
		mapInicial.put(dataInicial.getTime(), BigDecimal.ZERO);
		dataInicial.add(Calendar.DAY_OF_MONTH, numeroDias);

		return mapInicial;
	}
	
	public BigDecimal valorCustoLeftEstoque(){
		Session session = manager.unwrap(Session.class);
		Criteria criteria = session.createCriteria(ItemPedido.class);
		
		
		
		return null;
	}

}
