package com.algaworks.pedidovenda.controller;

import java.io.Serializable;
import java.util.List;

import javax.faces.bean.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import com.algaworks.pedidovenda.model.Categoria;
import com.algaworks.pedidovenda.model.Cliente;
import com.algaworks.pedidovenda.model.Subcategoria;
import com.algaworks.pedidovenda.repository.Categorias;
import com.algaworks.pedidovenda.service.CadastroCategoriaService;
import com.algaworks.pedidovenda.service.CadastroSubCategoriaService;
import com.algaworks.pedidovenda.util.jsf.FacesUtil;

@Named
@ViewScoped
public class CadastroSubcategoriaBean implements Serializable {

	private static final long serialVersionUID = 1L;


	public CadastroSubcategoriaBean() {
		subcategoria = new Subcategoria();
	}
	
	private Categoria categoriaPai;
	
	private List<Categoria> categorias;
	
	@Inject
	private Categorias categoriasRep;
	
	private Subcategoria subcategoria;
	
	@Inject
	private CadastroSubCategoriaService cadastroSubCategoriaService;
	
	public void inicializar() {
		categorias = categoriasRep.raiz();
		
	}
	
	
	public void salva(){
		this.subcategoria.setCategoria(categoriaPai);
		this.subcategoria = cadastroSubCategoriaService.salva(this.subcategoria);
		FacesUtil.addInfoMessage("Subcategoria Salva com Sucesso.");
		subcategoria = new Subcategoria();
	}


	public List<Categoria> getCategorias() {
		return categorias;
	}


	public void setCategorias(List<Categoria> categorias) {
		this.categorias = categorias;
	}


	public Categoria getCategoriaPai() {
		return categoriaPai;
	}


	public void setCategoriaPai(Categoria categoriaPai) {
		this.categoriaPai = categoriaPai;
	}


	public Subcategoria getSubcategoria() {
		return subcategoria;
	}


	public void setSubcategoria(Subcategoria subcategoria) {
		this.subcategoria = subcategoria;
		
		if(this.subcategoria != null){
			this.categoriaPai = this.subcategoria.getCategoria();
		}
	}
	
	
	
	
	public boolean isEditando(){
		return this.subcategoria.getId() != null;
	}



	

}
