package com.algaworks.pedidovenda.service;

import java.io.Serializable;

import javax.inject.Inject;

import com.algaworks.pedidovenda.model.Produto;
import com.algaworks.pedidovenda.model.Usuario;
import com.algaworks.pedidovenda.repository.Produtos;
import com.algaworks.pedidovenda.repository.Usuarios;
import com.algaworks.pedidovenda.util.jpa.Transactional;

//Responsavel pelas regras de negocio de cadastro de produtos
//Bean CDI

//IMplementa serezlizable por ser ViewEscoped
public class CadastroUsuarioService implements Serializable{

	
	private static final long serialVersionUID = 1L;
	
	@Inject
	private Usuarios usuarios;
	
	@Transactional
	public Usuario salvar(Usuario usuario){
		//Regra de negocio SKU
		//Vai no repositorio de produtos e busca por SKU
	/*    Usuario usuarioExistente = usuarios.porId(usuario.getId());
		// && == E					   //Se nao for igual "salva" senão "edita"
		if(usuarioExistente != null && !usuarioExistente.equals(usuario)){
			throw new NegocioException("Já existe usuario cadastrado!");
		}
	*/	return usuarios.guardar(usuario);
	}

}
