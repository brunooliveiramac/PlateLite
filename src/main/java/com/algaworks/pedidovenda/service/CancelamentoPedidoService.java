package com.algaworks.pedidovenda.service;

import java.io.Serializable;

import javax.inject.Inject;

import com.algaworks.pedidovenda.model.Pedido;
import com.algaworks.pedidovenda.model.StatusPedido;
import com.algaworks.pedidovenda.repository.Pedidos;
import com.algaworks.pedidovenda.util.jpa.Transactional;

public class CancelamentoPedidoService implements Serializable {
		
	@Inject
	private Pedidos pedidos;
	
	@Inject
	private EstoqueService estoqueService;

	
	
	private static final long serialVersionUID = 1L;

	@Transactional
	public Pedido cancelar(Pedido pedido) {
		
		pedido = this.pedidos.porId(pedido.getId());
		
		if(pedido.isNaoCancelavel()){
			throw new  NegocioException("Pedido nao pode ser cancelado no status "
					+ pedido.getStatus().getDescricao());
		}
		if(pedido.isEmitido()){
			this.estoqueService.retornarItensEstoque(pedido); //Contrario da baixa
		}
		
		pedido.setStatus(StatusPedido.CANCELADO);
		pedido = this.pedidos.guardar(pedido);
		
		
		return pedido;
	}

}
