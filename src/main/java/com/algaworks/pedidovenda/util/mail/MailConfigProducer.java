package com.algaworks.pedidovenda.util.mail;

import java.io.IOException;
import java.util.Properties;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Produces;

import com.outjected.email.api.SessionConfig;
import com.outjected.email.impl.SimpleMailConfig;
//Produtor do SessionConfig
public class MailConfigProducer {

	@Produces
	@ApplicationScoped
	public SessionConfig getMailConfig() throws IOException {
		Properties props = new Properties();
		//Acessa o mail properties com as propriedas para o envio do email
		//Carrega o arquivo de ppropriedades
		props.load(getClass().getResourceAsStream("/mail.properties"));
		
		SimpleMailConfig config = new SimpleMailConfig();
		//Configura sessão para enviar Email
		config.setServerHost("smtp.mandrillapp.com");
		config.setServerPort(587);
		config.setEnableSsl(false);
		config.setEnableTls(true);
		config.setAuth(true);
		config.setUsername("brunooliveira.mac@gmail.com");
		config.setPassword("62719402");

		
		return config;
	}
	
}