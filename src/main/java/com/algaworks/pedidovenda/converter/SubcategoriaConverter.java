package com.algaworks.pedidovenda.converter;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;

import com.algaworks.pedidovenda.model.Categoria;
import com.algaworks.pedidovenda.model.Cliente;
import com.algaworks.pedidovenda.model.Subcategoria;
import com.algaworks.pedidovenda.repository.Categorias;
import com.algaworks.pedidovenda.repository.Subcategorias;
import com.algaworks.pedidovenda.util.cdi.CDIServiceLocator;

@FacesConverter(forClass = Subcategoria.class)
public class SubcategoriaConverter implements Converter {

	//@Inject
	private Subcategorias subcategorias;
	
	public SubcategoriaConverter() {
		subcategorias = CDIServiceLocator.getBean(Subcategorias.class);
	}
	
	@Override
	public Object getAsObject(FacesContext context, UIComponent component, String value) {
		Subcategoria retorno = null;
		
		if (value != null) {
			Long id = new Long(value);
			retorno = subcategorias.porId(id);
		}
		
		return retorno;
	}

	@Override
	public String getAsString(FacesContext context, UIComponent component, Object value) {
		if(value != null) {           
			Subcategoria categoria = (Subcategoria) value;
            return categoria.getId() == null ? null : categoria.getId().toString();
        }
        return "";
	}

}