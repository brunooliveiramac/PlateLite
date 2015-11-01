package com.algaworks.pedidovenda.controller;

import java.io.Serializable;
import java.util.List;

import javax.faces.bean.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import com.algaworks.pedidovenda.model.Cliente;
import com.algaworks.pedidovenda.model.Endereco;
import com.algaworks.pedidovenda.model.TipoPessoa;
import com.algaworks.pedidovenda.repository.Enderecos;
import com.algaworks.pedidovenda.service.CadastroClienteService;
import com.algaworks.pedidovenda.util.jsf.FacesUtil;


@Named
@ViewScoped
public class CadastroClienteBean implements Serializable {

	private static final long serialVersionUID = 1L;
	
	@Inject
	private Cliente cliente;
	
	private Endereco endereco;
	
	private TipoPessoa tipopessoa;
	



	public void inicializar() {
		
		
			//endereco = new Endereco();
		
		
	}
	
	@Inject
	CadastroClienteService cadastroClienteService;
	
	
	
	public TipoPessoa getTipopessoa() {
		return tipopessoa;
	}


	public void setTipopessoa(TipoPessoa tipopessoa) {
		this.tipopessoa = tipopessoa;
	}


	

	public Endereco getEndereco() {
		return endereco;
	}


	public void setEndereco(Endereco endereco) {
		this.endereco = endereco;
	}


	public Cliente getCliente() {
		return cliente;
	}


	public void setCliente(Cliente cliente) {
		this.cliente = cliente;
	}


	public CadastroClienteBean() {
		cliente = new Cliente();
		endereco = new Endereco();
		}
	

	public void salvar() {
		this.cliente = cadastroClienteService.salvar(this.cliente);
		FacesUtil.addInfoMessage("Cliente salvo com sucesso!");
		cliente = new Cliente();


	}

	public TipoPessoa[] getTipoCliente(){
        return TipoPessoa.values();
    } 
	
	public boolean isEditando() {
		return this.cliente.getId() != null;
	}
	
	public void adicionarEndereco() {
		this.endereco.setCliente(this.cliente);
		this.cliente.getEnderecos().add(this.endereco);
		FacesUtil.addInfoMessage("Endereco adicionado.");
	
	}
	
	public void removerEndereco(){
		this.cliente.getEnderecos().remove(this.endereco);
		//this.cliente = cadastroClienteService.salvar(this.cliente);
	}
	
	public void editarEndereco() {
		this.cliente.getEnderecos()
		.set(this.cliente.getEnderecos().indexOf(endereco), endereco);
		}
	

}