package com.algaworks.pedidovenda.controller;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.validation.constraints.NotNull;

import com.algaworks.pedidovenda.model.Categoria;
import com.algaworks.pedidovenda.model.Grupo;
import com.algaworks.pedidovenda.model.Produto;
import com.algaworks.pedidovenda.repository.Categorias;
import com.algaworks.pedidovenda.repository.Grupos;
import com.algaworks.pedidovenda.service.CadastroGrupoService;
import com.algaworks.pedidovenda.service.CadastroProdutoService;
import com.algaworks.pedidovenda.util.jsf.FacesUtil;

@Named
@ViewScoped
public class CadastroGrupoBean implements Serializable {

	private static final long serialVersionUID = 1L;


	@Inject
	private Grupos grupos;
	
	public Grupos getGrupos() {
		return grupos;
	}


	public void setGrupos(Grupos grupos) {
		this.grupos = grupos;
	}


	private Grupo grupo;

	
	@Inject
	private CadastroGrupoService cadastroGrupoService;
	

	
	public CadastroGrupoService getCadastroGrupoService() {
		return cadastroGrupoService;
	}


	public void setCadastroGrupoService(CadastroGrupoService cadastroGrupoService) {
		this.cadastroGrupoService = cadastroGrupoService;
	}


	public Grupo getGrupo() {
		return grupo;
	}


	public void setGrupo(Grupo grupo) {
		this.grupo = grupo;
	}

	public CadastroGrupoBean() {
		grupo = new Grupo();
	}
	
	
	public void salvar(){
		this.grupo = cadastroGrupoService.salvar(this.grupo);
		FacesUtil.addInfoMessage("Grupo salvo com sucesso!");
	}






		
	public boolean isEditando(){
		return this.grupo.getId() != null;
	}
	
	
	
}