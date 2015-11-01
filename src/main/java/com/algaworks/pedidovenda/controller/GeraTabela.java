package com.algaworks.pedidovenda.controller;

import javax.persistence.EntityManager;



import com.algaworks.pedidovenda.util.jpa.EntityManagerProducer;
import com.algaworks.pedidovenda.util.jpa.JPAUtil;


public class GeraTabela {
		
			
			public static void main(String[] args){
					
				EntityManager manager = new JPAUtil().getEntityManager();
				manager.getTransaction().begin();
				
			}
			
		/*	<context-param>
			<param-name>javax.faces.INTERPRET_EMPTY_STRING_SUBMITTED_VALUES_AS_NULL</param-name>
			<param-value>true</param-value>
		</context-param>*/
}
