package com.algaworks.pedidovenda.model;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;
import javax.validation.constraints.NotNull;


@Entity
@Table(name = "parcelamento")
public class Parcelamento implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue
	private Long id;
	
	@Column(name = "num_parc", nullable = true)
	private Integer num_parc;
	
	@Column(name = "valores", precision = 11, scale = 2, nullable = true)
	private BigDecimal valores;
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "data", nullable = true)
	private Date date = new Date();
	
	@ManyToOne
	@JoinColumn(name = "pedido_id", nullable = false)
	private Pedido pedido;

	@NotNull
	@Enumerated(EnumType.STRING)
	@Column(nullable = false, length = 20)
	private Pagamento pagamento = Pagamento.EM_DEBITO;
	
	
	
	

	
	
	


	public Pedido getPedido() {
		return pedido;
	}

	public void setPedido(Pedido pedido) {
		this.pedido = pedido;
	}


	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public BigDecimal getValores() {
		return valores;
	}

	public void setValores(BigDecimal valores) {
		this.valores = valores;
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}
	
	
	public Pagamento getPagamento() {
		return pagamento;
	}

	public void setPagamento(Pagamento pagamento) {
		this.pagamento = pagamento;
	}

	public Integer getNum_parc() {
		return num_parc;
	}

	public void setNum_parc(Integer num_parc) {
		this.num_parc = num_parc;
	}
	
	

	public void recalcularValorTotalParcela(BigDecimal valor) {
		BigDecimal total = BigDecimal.ZERO;
		total = this.getValores().subtract(valor);
		
		this.setValores(total);
		
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
		Parcelamento other = (Parcelamento) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}
	
	
	@Transient
	public boolean isEmDebito(){
		return Pagamento.EM_DEBITO.equals(this.getPagamento());
	}
	
	@Transient
	public boolean isPago(){
		return Pagamento.PAGA.equals(this.getPagamento());
	}

	


}
