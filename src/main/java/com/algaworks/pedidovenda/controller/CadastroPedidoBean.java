package com.algaworks.pedidovenda.controller;

import java.io.Serializable;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.enterprise.event.Observes;
import javax.enterprise.inject.Produces;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;

import org.apache.commons.lang3.StringUtils;
import org.primefaces.event.CellEditEvent;

import com.algaworks.pedidovenda.model.Cliente;
import com.algaworks.pedidovenda.model.FormaPagamento;
import com.algaworks.pedidovenda.model.ItemPedido;
import com.algaworks.pedidovenda.model.Pagamento;
import com.algaworks.pedidovenda.model.Parcelamento;
import com.algaworks.pedidovenda.model.Pedido;
import com.algaworks.pedidovenda.model.Produto;
import com.algaworks.pedidovenda.model.Usuario;
import com.algaworks.pedidovenda.repository.Clientes;
import com.algaworks.pedidovenda.repository.Produtos;
import com.algaworks.pedidovenda.repository.Usuarios;
import com.algaworks.pedidovenda.service.CadastroPedidoService;
import com.algaworks.pedidovenda.service.NegocioException;
import com.algaworks.pedidovenda.service.ParcelamentoService;
import com.algaworks.pedidovenda.util.DateFormat;
import com.algaworks.pedidovenda.util.jsf.FacesUtil;
import com.algaworks.pedidovenda.validation.SKU;

@Named
@ViewScoped
public class CadastroPedidoBean implements Serializable {

	private static final long serialVersionUID = 1L;

	private Parcelamento parcelamento;

	@Inject
	private Usuarios usuarios;

	@Inject
	private Clientes clientes;

	@Inject
	private Produtos produtos;

	@Inject
	private CadastroPedidoService cadastroPedidoService;

	private ArrayList<Parcelamento> parcelasPedido;

	private String sku;

	@Produces
	@PedidoEdicao
	private Pedido pedido;

	private List<Usuario> vendedores;

	private Produto produtoLinhaEditavel;

	@Inject
	private ParcelamentoService cadastroParcelamentoService;

	public void pedidoAlterado(@Observes PedidoAlteradoEvent event) {
		this.pedido = event.getPedido(); // O pedido recebe o pedidoevent da
											// classe PedidoAlteradoEvent
	}

	public CadastroPedidoBean() {
		limpar();
	}

	public void inicializar() {
		if (FacesUtil.isNotPostBack()) {
			this.vendedores = this.usuarios.vendedores();

			this.pedido.adicionarItemVazio();

			this.recalcularPedido();
		}
	}

	private void limpar() {
		pedido = new Pedido();
		parcelamento = new Parcelamento();
	}

	public void salvar() throws ParseException {
		this.pedido.removerItemVazio();
		try {
			this.pedido = this.cadastroPedidoService.salvar(this.pedido);
			FacesUtil.addInfoMessage("Pedido salvo com sucesso!");
		} finally {
			this.pedido.adicionarItemVazio();
		}
	}

	public void recalcularPedido() {
		if (this.pedido != null) {
			this.pedido.recalcularValorTotal();
		}
	}

	public void carregarProdutoPorSku() {
		if (StringUtils.isNotEmpty(this.sku)) {
			this.produtoLinhaEditavel = this.produtos.porSku(this.sku);
			this.carregarProdutoLinhaEditavel();
		}
	}

	public void carregarProdutoLinhaEditavel() {
		ItemPedido item = this.pedido.getItens().get(0);

		if (this.produtoLinhaEditavel != null) {
			if (this.existeItemComProduto(this.produtoLinhaEditavel)) {
				FacesUtil
						.addErrorMessage("Já existe um item no pedido com o produto informado.");
			} else {
				item.setProduto(this.produtoLinhaEditavel);
				item.setValorUnitario(this.produtoLinhaEditavel
						.getValorUnitario());
				item.setValorCusto(this.produtoLinhaEditavel.getValorcusto());
				this.pedido.adicionarItemVazio();
				this.produtoLinhaEditavel = null;
				this.sku = null;

				this.pedido.recalcularValorTotal();
			}
		}
	}

	private boolean existeItemComProduto(Produto produto) {
		boolean existeItem = false;
		// Intera em todos os itens do pedido
		for (ItemPedido item : this.getPedido().getItens()) {
			// se o produto que recebemos como parametro for igual ao produto do
			// item
			if (produto.equals(item.getProduto())) {
				existeItem = true; // true
				break;// e para
			}
		}

		return existeItem;
	}

	public ArrayList<Parcelamento> Parcelamento(BigDecimal valorTotal,
			Integer parcelamentos) throws ParseException {

		ArrayList<Parcelamento> parcelamentoList = new ArrayList<>();
		Calendar cFirst = Calendar.getInstance();
		SimpleDateFormat dataFormadata = new SimpleDateFormat(
				"yyyy-MM-dd HH:mm:ss.S");
		cFirst.setTime(this.parcelamento.getDate());
		double valorParc = 0.0;
		valorParc = valorTotal.doubleValue() / parcelamentos.doubleValue();

		for (int i = 1; i <= parcelamentos; i++) {
			Parcelamento parcela = new Parcelamento();
			parcela.setNum_parc(i);
			String cFirstString = dataFormadata.format(cFirst.getTime());
			Date Date = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.S")
					.parse(cFirstString);
			parcela.setDate(Date);
			parcela.setValores(new BigDecimal(valorParc).setScale(2,
					RoundingMode.CEILING));
			if (this.pedido.isCartaoCredito()) {
				parcela.setPagamento(Pagamento.PAGA);
			}
			parcela.setPedido(this.pedido);
			parcelamentoList.add(parcela);
			cFirst.add(Calendar.MONTH, 1);

		}

		return parcelamentoList;
	}

	public void calculaParcelas() throws ParseException {

		if (this.pedido.isParcelavel() && this.pedido.isNulo()) {
			this.parcelasPedido = new ArrayList<Parcelamento>();
			this.pedido.getParcelamentos().clear();
			this.parcelasPedido = this.Parcelamento(
					this.pedido.getValorSubtotal(), this.pedido.getParcelas());
			this.pedido.getParcelamentos().addAll(this.parcelasPedido);
		} else {
			FacesUtil
					.addErrorMessage("Somente para Promissória e Cartão de Crédito."
							+ " Pedido também deve possuir pelo menos um item");

		}

	}

	public List<Produto> completarProduto(String nome) {
		return this.produtos.porNome(nome);
	}

	public void atualizarQuantidade(ItemPedido item, int linha) {
		if (item.getQuantidade() < 1) {
			// Nao posso remover a primeira linha
			if (linha == 0) {
				item.setQuantidade(1);
			} else {
				this.getPedido().getItens().remove(linha);
			}
		}

		this.pedido.recalcularValorTotal();
	}

	public void atualizarParcela(Parcelamento parcelamento, int linha) {
		Integer i = Integer.valueOf(parcelamento.getValores().toString());
		Integer auxiliar = i;
		// Logica setar Pago ou em Débito
		if (i < 1) {
			parcelamento.setPagamento(Pagamento.PAGA);
		}
		if (i > 1) {
			parcelamento.setPagamento(Pagamento.PARCIAL);
		}

	}

	public void onCellEdit(CellEditEvent event) {
		Object oldValue = event.getOldValue();
		Object newValue = event.getNewValue();

		if (newValue != null && !newValue.equals(oldValue)) {
			FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_INFO,
					"Status da parcela foi alterado", "Old: " + oldValue
							+ ", New:" + newValue);
			FacesContext.getCurrentInstance().addMessage(null, msg);
		}
	}

	public void quitarPedido() {
		this.pedido.setDataQuita(new Date());
		List<Parcelamento> parcelamentosEditar = this.pedido.getParcelamentos();

		for (Parcelamento parcelamento : parcelamentosEditar) {
			if(parcelamento.isEmDebito()){
				parcelamento.setPagamento(Pagamento.QUITADO);	
			}
			
		}
	}

	public FormaPagamento[] getFormasPagamento() {
		return FormaPagamento.values();
	}

	public Pagamento[] getEstadoPagamento() {
		return Pagamento.values();
	}

	public List<Cliente> completarCliente(String nome) {
		return this.clientes.porNome(nome);
	}

	public Pedido getPedido() {
		return pedido;
	}

	public void setPedido(Pedido pedido) {
		this.pedido = pedido;
	}

	public List<Usuario> getVendedores() {
		return vendedores;
	}

	public boolean isEditando() {
		return this.pedido.getId() != null;
	}

	public Produto getProdutoLinhaEditavel() {
		return produtoLinhaEditavel;
	}

	public void setProdutoLinhaEditavel(Produto produtoLinhaEditavel) {
		this.produtoLinhaEditavel = produtoLinhaEditavel;
	}

	@SKU
	public String getSku() {
		return sku;
	}

	public void setSku(String sku) {
		this.sku = sku;
	}

	public Parcelamento getParcelamento() {
		return parcelamento;
	}

	public void setParcelamento(Parcelamento parcelamento) {
		this.parcelamento = parcelamento;
	}

	public ArrayList<Parcelamento> getParcelasPedido() {
		return parcelasPedido;
	}

	public void setParcelasPedido(ArrayList<Parcelamento> parcelasPedido) {
		this.parcelasPedido = parcelasPedido;
	}

}