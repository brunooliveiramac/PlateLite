package com.algaworks.pedidovenda.controller;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
















import javax.faces.bean.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import com.algaworks.pedidovenda.model.Cliente;
import com.algaworks.pedidovenda.model.Produto;
import com.algaworks.pedidovenda.model.Usuario;
import com.algaworks.pedidovenda.repository.Clientes;
import com.algaworks.pedidovenda.repository.Produtos;
import com.algaworks.pedidovenda.repository.Usuarios;
import com.algaworks.pedidovenda.repository.filter.ClienteFilter;
import com.algaworks.pedidovenda.repository.filter.ProdutoFilter;
import com.algaworks.pedidovenda.repository.filter.UsuarioFilter;
import com.algaworks.pedidovenda.util.jsf.FacesUtil;

@Named
@ViewScoped
public class PesquisaClientesBean implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private ClienteFilter filtro;


	@Inject
	private Clientes clientes;

	private List<Cliente> clientesFiltrados;
	
	private Cliente clienteSelecionado;
	
	
	
	public ClienteFilter getFiltro() {
		return filtro;
	}



	public void setFiltro(ClienteFilter filtro) {
		this.filtro = filtro;
	}







	public List<Cliente> getClientesFiltrados() {
		return clientesFiltrados;
	}



	public void setClientesFiltrados(List<Cliente> clientesFiltrados) {
		this.clientesFiltrados = clientesFiltrados;
	}



	public Cliente getClienteSelecionado() {
		return clienteSelecionado;
	}



	public void setClienteSelecionado(Cliente clienteSelecionado) {
		this.clienteSelecionado = clienteSelecionado;
	}



	public void pesquisar(){
		clientesFiltrados = clientes.filtrados(filtro);
	}
	
	
	
	public PesquisaClientesBean() {
		filtro = new ClienteFilter();
	}
	
	public void excluir(){
		//Cliente atribuido no xhtml
		clientes.remover(clienteSelecionado);
		clientesFiltrados.remove(clienteSelecionado);
		
		FacesUtil.addInfoMessage("Usuario " + clienteSelecionado.getNome() +" excluido com sucesso.");
	}



	public Clientes getClientes() {
		return clientes;
	}



	public void setClientes(Clientes clientes) {
		this.clientes = clientes;
	}
	

	
}