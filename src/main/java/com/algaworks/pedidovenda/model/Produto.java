package com.algaworks.pedidovenda.model;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.hibernate.validator.constraints.NotBlank;

import com.algaworks.pedidovenda.service.NegocioException;
import com.algaworks.pedidovenda.validation.SKU;

@Entity
@Table(name="produto")
public class Produto implements Serializable {

	private static final long serialVersionUID = 1L;

	private Long id;
	private String nome;
	private String sku;
	private BigDecimal valorUnitario;
	private Integer quantidadeEstoque;
	private Categoria categoria;
	private Subcategoria subcategoria;
	private BigDecimal valorcusto;
	private Date data;
	




	@Id
	@GeneratedValue
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	@NotBlank
	@Size(max = 80)
	@Column(nullable = false, length = 80)
	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	@NotBlank @SKU
	@Column(nullable = false, length = 20, unique = true)
	public String getSku() {
		return sku;
	}

	public void setSku(String sku) {
		this.sku = sku == null ? null : sku.toUpperCase();
	}

	@NotNull(message = "é obrigatório")
	@Column(name="valor_unitario", nullable = false, precision = 10, scale = 2)
	public BigDecimal getValorUnitario() {
		return valorUnitario;
	}

	public void setValorUnitario(BigDecimal valorUnitario) {
		this.valorUnitario = valorUnitario;
	}

	@NotNull @Min(0) @Max(value = 9999, message = "tem um valor muito alto")
	@Column(name="quantidade_estoque", nullable = false, length = 5)
	public Integer getQuantidadeEstoque() {
		return quantidadeEstoque;
	}

	public void setQuantidadeEstoque(Integer quantidadeEstoque) {
		this.quantidadeEstoque = quantidadeEstoque;
	}

	
	@ManyToOne
	@JoinColumn(name = "categoria_id", nullable = false)
	public Categoria getCategoria() {
		return categoria;
	}

	public void setCategoria(Categoria categoria) {
		this.categoria = categoria;
	}
	

	@ManyToOne
	@JoinColumn(name = "subcategoria_id", nullable = false)
	public Subcategoria getSubcategoria() {
		return subcategoria;
	}
	
	
	public void setSubcategoria(Subcategoria subcategoria) {
		this.subcategoria = subcategoria;
	}
	
	
	@NotNull(message = "é obrigatório")
	@Column(name="valor_custo", nullable = false, precision = 10, scale = 2)
	public BigDecimal getValorcusto() {
		return valorcusto;
	}

	public void setValorcusto(BigDecimal valorcusto) {
		this.valorcusto = valorcusto;
	}
	
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "data_criacao_prod", nullable = false)
	public Date getData() {
		return data;
	}

	public void setData(Date data) {
		this.data = data;
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
		Produto other = (Produto) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}

	public void baixarEstoque(Integer quantidade) {
		int novaQuantidade = this.getQuantidadeEstoque() - quantidade;
		
		if (novaQuantidade < 0) {
			throw new NegocioException("Não há disponibilidade no estoque de "
					+ quantidade + " itens do produto " + this.getSku() + ".");
		}
		
		this.setQuantidadeEstoque(novaQuantidade);
	}

	public void adicionarEstoque(Integer quantidade) { //cancelamento
		this.setQuantidadeEstoque(getQuantidadeEstoque() + quantidade);
		
	}

}