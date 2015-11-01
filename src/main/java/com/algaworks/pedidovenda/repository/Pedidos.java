package com.algaworks.pedidovenda.repository;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.PersistenceException;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projection;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.hibernate.transform.Transformers;
import org.hibernate.type.StandardBasicTypes;
import org.hibernate.type.Type;

import com.algaworks.pedidovenda.model.Pedido;
import com.algaworks.pedidovenda.model.Produto;
import com.algaworks.pedidovenda.model.Usuario;
import com.algaworks.pedidovenda.model.vo.DataValor;
import com.algaworks.pedidovenda.repository.filter.PedidoFilter;
import com.algaworks.pedidovenda.service.NegocioException;
import com.algaworks.pedidovenda.util.jpa.Transactional;

public class Pedidos implements Serializable {

	private static final long serialVersionUID = 1L;

	@Inject
	private EntityManager manager;

	@SuppressWarnings({ "unchecked" })
	public Map<Date, BigDecimal> valoresTotaisPorData(Integer numeroDeDias,
			Usuario criadoPor) {
		Session session = manager.unwrap(Session.class);

		numeroDeDias -= 1;
		Calendar dataInicial = Calendar.getInstance();
		dataInicial = DateUtils.truncate(dataInicial, Calendar.DAY_OF_MONTH); // trunca
																				// a
																				// DATA,
																				// somente
																				// o
																				// mês
		dataInicial.add(Calendar.DAY_OF_MONTH, numeroDeDias * -1);

		Map<Date, BigDecimal> resultado = criarMapaVazio(numeroDeDias,
				dataInicial);

		Criteria criteria = session.createCriteria(Pedido.class);
		// select date(data_criacao) as data, sum(valor_total) as valor from
		// pedido
		// where dataCriacao >= :dataInicial and vendedor_id = :criadoPor group
		// by date(data_criacao)

		// Projetando
		criteria.setProjection(
				Projections
						.projectionList()
						.add(Projections.sqlGroupProjection(
								"date(data_criacao) as data",
								"date(data_criacao)", new String[] { "data" },
								new Type[] { StandardBasicTypes.DATE }))

						.add(Projections.sum("valorTotal").as("valor")))

		.add(Restrictions.ge("dataCriacao", dataInicial.getTime()));
		// retorna uma lis de arryas de data e valor
		if (criadoPor != null) {
			criteria.add(Restrictions.eq("vendedor", criadoPor));
		}

		// Tranforma o resultado da busca em obj DataValor
		List<DataValor> valoresPorData = criteria.setResultTransformer(
				Transformers.aliasToBean(DataValor.class)).list();

		for (DataValor dataValor : valoresPorData) {
			resultado.put(dataValor.getData(), dataValor.getValor());
		}

		return resultado;
	}

	private Map<Date, BigDecimal> criarMapaVazio(Integer numeroDeDias,
			Calendar dataInicial) {
		dataInicial = (Calendar) dataInicial.clone();// Para nao alterar a
														// instancia
		Map<Date, BigDecimal> mapaInicial = new TreeMap<>();

		for (int i = 0; i <= numeroDeDias; i++) {
			mapaInicial.put(dataInicial.getTime(), BigDecimal.ZERO);
			dataInicial.add(Calendar.DAY_OF_MONTH, 1);
		}

		return mapaInicial;
	}

	@SuppressWarnings("unchecked")
	public List<Pedido> filtrados(PedidoFilter filtro) {
		Session session = this.manager.unwrap(Session.class);

		Criteria criteria = session.createCriteria(Pedido.class)
		// fazemos uma associação (join) com cliente e nomeamos como "c"
				.createAlias("cliente", "c")
				// fazemos uma associação (join) com cliente e nomeamos como "c"
				.createAlias("vendedor", "v");

		if (filtro.getNumeroDe() != null) {
			// id deve ser maior ou igual (ge = greater or equals) a
			// filtro.numeroDe
			criteria.add(Restrictions.ge("id", filtro.getNumeroDe()));
		}

		if (filtro.getNumeroAte() != null) {
			// id deve ser MENOR ou igual (le = lower or equals) a
			// filtro.numeroDe
			criteria.add(Restrictions.le("id", filtro.getNumeroAte()));
		}

		if (filtro.getDataCriacaoDe() != null) {
			// id deve ser maior ou igual (ge = greater or equals) a
			// filtro.numeroDe
			criteria.add(Restrictions.ge("dataCriacao",
					filtro.getDataCriacaoDe()));
		}

		if (filtro.getDataCriacaoAte() != null) {
			// id deve ser MENOR ou igual (le = lower or equals) a
			// filtro.numeroDe
			criteria.add(Restrictions.le("dataCriacao",
					filtro.getDataCriacaoAte()));
		}

		if (StringUtils.isNotBlank(filtro.getNomeCliente())) {
			// acessamos o nome do cliente associado ao pedido pelo alias "c"
			// criado anteriormente
			criteria.add(Restrictions.ilike("c.nome", filtro.getNomeCliente(),
					MatchMode.ANYWHERE));
		}

		if (StringUtils.isNotBlank(filtro.getNomeVendedor())) {
			// acessamos o nome do cliente associado ao pedido pelo alias "c"
			// criado anteriormente
			criteria.add(Restrictions.ilike("v.nome", filtro.getNomeCliente(),
					MatchMode.ANYWHERE));
		}

		if (filtro.getStatuses() != null && filtro.getStatuses().length > 0) {
			// adicionamos uma restrição "in", passando um array de constantes
			// da enum StatusPedido
			criteria.add(Restrictions.in("status", filtro.getStatuses()));
		}

		return criteria.addOrder(Order.asc("id")).list();

	}
	
	@Transactional
	public void remover(Pedido produto) {
		try {
			produto = porId(produto.getId());
			manager.remove(produto);
			// Completa a excução da exclusão - Se o produto estiver sendo usado
			// por outra tabela
			// cancela a exclusão e envia um erro para o usuário
			// Chave estrangeira
			manager.flush();
		} catch (PersistenceException e) {
			throw new NegocioException("Pedido não pode ser excluído.");
		}

	}

	

	public Pedido guardar(Pedido pedido) {
		return this.manager.merge(pedido);
	}

	public Pedido porId(Long id) {
		return this.manager.find(Pedido.class, id);
	}
}
