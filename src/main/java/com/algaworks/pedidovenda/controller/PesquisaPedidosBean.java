package com.algaworks.pedidovenda.controller;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.faces.bean.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import com.algaworks.pedidovenda.model.Pedido;
import com.algaworks.pedidovenda.model.StatusPedido;
import com.algaworks.pedidovenda.repository.Pedidos;
import com.algaworks.pedidovenda.repository.filter.PedidoFilter;

@Named
@ViewScoped
public class PesquisaPedidosBean implements Serializable {

	private static final long serialVersionUID = 1L;
	
	@Inject
	private Pedidos pedidos;
	
	private Pedido pedidoSelecionado;
	
	private PedidoFilter filtro;
	private List<Pedido> pedidosFiltrados;
	
	public PesquisaPedidosBean() {
		filtro = new PedidoFilter();
		pedidosFiltrados = new ArrayList<>();
	}
	
	public void excluir(){
		pedidos.remover(pedidoSelecionado);
		pedidosFiltrados.remove(pedidoSelecionado);
	}

	public void pesquisar() {
		pedidosFiltrados = pedidos.filtrados(filtro);
	}
	
	public StatusPedido[] getStatuses() {
		return StatusPedido.values();
	}
	
	public List<Pedido> getPedidosFiltrados() {
		return pedidosFiltrados;
	}

	public PedidoFilter getFiltro() {
		return filtro;
	}

	public Pedido getPedidoSelecionado() {
		return pedidoSelecionado;
	}

	public void setPedidoSelecionado(Pedido pedidoSelecionado) {
		this.pedidoSelecionado = pedidoSelecionado;
	}
	
	
	
}
