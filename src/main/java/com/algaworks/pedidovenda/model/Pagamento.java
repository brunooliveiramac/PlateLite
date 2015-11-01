package com.algaworks.pedidovenda.model;

public enum Pagamento {

	PAGA("Pago"), 
	EM_DEBITO("Em debito"),
	PARCIAL("Parcial"),
	QUITADO("Quitado");

	
	private String descricao;
	
	Pagamento(String descricao) {
		this.descricao = descricao;
	}

	public String getDescricao() {
		return descricao;
	}
	
}