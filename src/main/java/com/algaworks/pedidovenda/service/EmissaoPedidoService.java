package com.algaworks.pedidovenda.service;

import java.io.Serializable;

import javax.inject.Inject;

import com.algaworks.pedidovenda.controller.PedidoEdicao;
import com.algaworks.pedidovenda.model.Pedido;
import com.algaworks.pedidovenda.model.StatusPedido;
import com.algaworks.pedidovenda.repository.Pedidos;
import com.algaworks.pedidovenda.util.jpa.Transactional;

//Classe bean CDI
public class EmissaoPedidoService implements Serializable {

	private static final long serialVersionUID = 1L;
	
	@Inject //injeta Bean de serviço
	private CadastroPedidoService cadastroPedidoService;
	
	@Inject
	Pedidos pedidos;
	
	@Inject
	private EstoqueService estoqueService;
	
	@Transactional
	public Pedido emitir(Pedido pedido){
		pedido = this.cadastroPedidoService.salvar(pedido);
		
		if(pedido.isNaoEmissivel()){ 
			throw new NegocioException("Pedido nao pode ser emitido com status "
					+ pedido.getStatus().getDescricao() + ".");
		}
		  
		this.estoqueService.baixarItensEstoque(pedido);
		
		pedido.setStatus(StatusPedido.EMITIDO);
		
		pedido = this.pedidos.guardar(pedido);
		
		return pedido;
	}
}
