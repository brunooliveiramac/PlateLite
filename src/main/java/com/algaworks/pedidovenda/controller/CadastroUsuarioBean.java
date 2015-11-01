package com.algaworks.pedidovenda.controller;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.faces.bean.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import javax.swing.text.html.HTMLDocument.HTMLReader.IsindexAction;

import com.algaworks.pedidovenda.model.Cliente;
import com.algaworks.pedidovenda.model.Endereco;
import com.algaworks.pedidovenda.model.Grupo;
import com.algaworks.pedidovenda.model.Produto;
import com.algaworks.pedidovenda.model.Usuario;
import com.algaworks.pedidovenda.repository.Grupos;
import com.algaworks.pedidovenda.service.CadastroProdutoService;
import com.algaworks.pedidovenda.service.CadastroUsuarioService;
import com.algaworks.pedidovenda.util.jsf.FacesUtil;


@Named
@ViewScoped
public class CadastroUsuarioBean implements Serializable {

	private static final long serialVersionUID = 1L;
	
	@Inject
	private Usuario usuario;
	
	
	private List<Grupo> grupousuario;
	
	@Inject
	private Grupos grupos;
		
	private Grupo grupoSelecionado;
	

	@Inject
	private CadastroUsuarioService cadastroUsuarioService;
	

	public Grupo getGrupoSelecionado() {
		return grupoSelecionado;
	}


	public void setGrupoSelecionado(Grupo grupoSelecionado) {
		this.grupoSelecionado = grupoSelecionado;
	}


	public List<Grupo> getGrupousuario() {
		return grupousuario;
	}


	public void setGrupousuario(List<Grupo> grupousuario) {
		this.grupousuario = grupousuario;
	}


	

	public void inicializar() {
		System.out.println("Inicializando...");
		//Para não fazer a consulta novamente
		if(FacesUtil.isNotPostBack()){
			grupousuario = grupos.raiz();
				//Carrega as subcategorias p/ editar
				
		}
		
	
	}
	


	public Grupos getGrupos() {
		return grupos;
	}


	public void setGrupos(Grupos grupos) {
		this.grupos = grupos;
	}


	public CadastroUsuarioBean() {
		usuario = new Usuario();
		grupoSelecionado = null;
		}
	

	public Usuario getUsuario() {
		return usuario;
	}


	public void setUsuario(Usuario usuario) {
		this.usuario = usuario;
		
	
	}

	private void limpar(){
		usuario = new Usuario();
	}
	
	public void adicionarGrupo() {
		   this.usuario.getGrupos().add(this.grupoSelecionado);
			this.usuario = cadastroUsuarioService.salvar(this.usuario);

	}
	
	public void removerGrupo(){
		this.usuario.getGrupos().remove(this.grupoSelecionado);
		this.usuario = cadastroUsuarioService.salvar(this.usuario);

	}
	

	public void salvar(){
		
		//System.out.println("Grupo Selecionado:" + grupoSelecionado.getId());
		adicionarGrupo();
		this.usuario = cadastroUsuarioService.salvar(this.usuario);
		limpar();
		FacesUtil.addInfoMessage("Usuario salvo com sucesso!");
	}
	
	public boolean isEditando(){
		return this.usuario.getId() != null;
	}
	
	
	
	
	
	

}