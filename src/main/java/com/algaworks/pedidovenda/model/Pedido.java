package com.algaworks.pedidovenda.model;

import java.io.Serializable;
import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.inject.Inject;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;
import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.NotBlank;

import com.algaworks.pedidovenda.service.CadastroPedidoService;
import com.algaworks.pedidovenda.service.ParcelamentoService;
import com.algaworks.pedidovenda.util.jpa.Transactional;

@Entity
@Table(name = "pedido")
public class Pedido implements Serializable {

	private static final long serialVersionUID = 1L;

	private Long id;
	private Date dataCriacao;
	private String observacao;
	private BigDecimal valorDesconto = BigDecimal.ZERO;
	private BigDecimal valorTotal = BigDecimal.ZERO;
	private StatusPedido status = StatusPedido.ORCAMENTO;
	private FormaPagamento formaPagamento;
	private Usuario vendedor;
	private Cliente cliente;
	private Integer repetir;
	private List<ItemPedido> itens = new ArrayList<>();
	private List<Parcelamento> parcelamentos = new ArrayList<>();
	private Date dataQuita;

	@Id
	@GeneratedValue
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	@Transient
	public boolean isNovo() {
		return getId() == null;
	}

	@Transient
	public boolean isExistente() {
		return !isNovo();
	}

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "data_criacao", nullable = false)
	public Date getDataCriacao() {
		return dataCriacao;
	}

	public void setDataCriacao(Date dataCriacao) {
		this.dataCriacao = dataCriacao;
	}

	@Column(columnDefinition = "text")
	public String getObservacao() {
		return observacao;
	}

	public void setObservacao(String observacao) {
		this.observacao = observacao;
	}

	@NotNull
	@Column(name = "valor_desconto", nullable = false, precision = 10, scale = 2)
	public BigDecimal getValorDesconto() {
		return valorDesconto;
	}

	public void setValorDesconto(BigDecimal valorDesconto) {
		this.valorDesconto = valorDesconto;
	}

	@NotNull
	@Column(name = "valor_total", nullable = false, precision = 10, scale = 2)
	public BigDecimal getValorTotal() {
		return valorTotal;
	}

	public void setValorTotal(BigDecimal valorTotal) {
		this.valorTotal = valorTotal;
	}

	@NotNull
	@Enumerated(EnumType.STRING)
	@Column(nullable = false, length = 20)
	public StatusPedido getStatus() {
		return status;
	}

	public void setStatus(StatusPedido status) {
		this.status = status;
	}

	@NotNull
	@Enumerated(EnumType.STRING)
	@Column(name = "forma_pagamento", nullable = false, length = 20)
	public FormaPagamento getFormaPagamento() {
		return formaPagamento;
	}

	public void setFormaPagamento(FormaPagamento formaPagamento) {
		this.formaPagamento = formaPagamento;
	}

	@NotNull
	@ManyToOne
	@JoinColumn(name = "vendedor_id", nullable = false)
	public Usuario getVendedor() {
		return vendedor;
	}

	public void setVendedor(Usuario vendedor) {
		this.vendedor = vendedor;
	}

	@NotNull
	@ManyToOne
	@JoinColumn(name = "cliente_id", nullable = false)
	public Cliente getCliente() {
		return cliente;
	}

	public void setCliente(Cliente cliente) {
		this.cliente = cliente;
	}

	@OneToMany(mappedBy = "pedido", cascade = CascadeType.ALL, fetch = FetchType.LAZY, orphanRemoval = true)
	public List<ItemPedido> getItens() {
		return itens;
	}

	public void setItens(List<ItemPedido> itens) {
		this.itens = itens;
	}

	
	@Column(name = "repetir", nullable = true)
	public Integer getRepetir() {
		return repetir;
	}

	public void setRepetir(Integer repetir) {
		this.repetir = repetir;
	}

	@OneToMany(mappedBy = "pedido", cascade = CascadeType.ALL, fetch = FetchType.LAZY, orphanRemoval = true)
	public List<Parcelamento> getParcelamentos() {
		return parcelamentos;
	}

	public void setParcelamentos(List<Parcelamento> parcelamentos) {
		this.parcelamentos = parcelamentos;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "data", nullable = true)
	public Date getDataQuita() {
		return dataQuita;
	}

	public void setDataQuita(Date dataQuita) {
		this.dataQuita = dataQuita;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Pedido other = (Pedido) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}

	@Transient
	public BigDecimal getValorSubtotal() {
		BigDecimal total = BigDecimal.ZERO;
		// somar itens do pedido
		for (ItemPedido item : this.getItens()) {// Soma os pedidos adicionados
			if (item.getProduto() != null && item.getProduto().getId() != null) {
				total = total.add(item.getValorTotal());
			}
		}
		return total;
	}

	@Transient
	public Integer getParcelas() {
		return this.getRepetir();
	}

	public void recalcularValorTotal() {
		BigDecimal total = BigDecimal.ZERO;

		total = total.subtract(this.getValorDesconto());

		// somar itens do pedido
		for (ItemPedido item : this.getItens()) {// Soma os pedidos adicionados
			if (item.getProduto() != null && item.getProduto().getId() != null) {
				total = total.add(item.getValorTotal());
			}
		}
		this.setValorTotal(total);
	}

	// Adiciona item vazio na capa de pedido dentro do item
	public void adicionarItemVazio() {
		if (this.isOrcamento()) { // Se for orçamento adiciona
			Produto produto = new Produto();

			ItemPedido item = new ItemPedido();
			item.setProduto(produto);
			item.setPedido(this);

			this.getItens().add(0, item);// Adiciona o item na posição zero,
											// sempre
		}

	}

	@Transient
	// Nao é mapeamento JPA (Get)
	public boolean isOrcamento() {
		return StatusPedido.ORCAMENTO.equals(this.getStatus());
	}

	public void removerItemVazio() {
		ItemPedido primeiroItem = this.getItens().get(0);

		if (primeiroItem != null && primeiroItem.getProduto().getId() == null) {
			this.getItens().remove(0);
		}

	}

	@Transient
	public Integer getNumParc() {
		return this.getRepetir();
	}

	@Transient
	public boolean isValorTotalNegativo() {
		return this.getValorTotal().compareTo(BigDecimal.ZERO) < 0;
	}

	@Transient
	public boolean isEmitido() {
		return StatusPedido.EMITIDO.equals(this.getStatus());
	}

	@Transient
	public boolean isPromissoria() {
		return FormaPagamento.PROMISSORIA.equals(this.getFormaPagamento());
	}

	@Transient
	public boolean isParcelavel() {
		return this.isPromissoria() || this.isCartaoCredito();
	}

	@Transient
	public boolean isCartaoCredito() {
		return FormaPagamento.CARTAO_CREDITO.equals(this.getFormaPagamento());
	}

	@Transient
	public boolean isNaoEmissivel() {
		return !this.isEmissivel();
	}

	@Transient
	public boolean isEmissivel() {
		return this.isExistente() && this.isOrcamento();
	}

	@Transient
	public boolean isNaoCancelavel() {
		return !isCancelavel();

	}

	@Transient
	public boolean isCancelavel() {
		return this.isExistente() && !this.isCancelado();
	}

	@Transient
	public boolean isQuitado() {
		return this.getDataQuita() != null;
	}

	@Transient
	public boolean isQuitadobotao() {
		return this.isOrcamento() || this.isQuitado();
	}

	@Transient
	private boolean isCancelado() {
		return StatusPedido.CANCELADO.equals(this.getStatus());
	}

	@Transient
	public boolean isNaoAlteravel() {
		return !isAlteravel();
	}

	@Transient
	private boolean isAlteravel() {
		return this.isOrcamento();
	}

	@Transient
	private boolean isCredito() {
		return FormaPagamento.CARTAO_CREDITO.equals(this.getStatus());
	}

	@Transient
	public boolean isNaoEnviavelPorEmail() {
		return this.isNovo() || this.isCancelado();
	}

	@Transient
	public boolean isNulo() {
		return this.getItens() != null;
	}

}
