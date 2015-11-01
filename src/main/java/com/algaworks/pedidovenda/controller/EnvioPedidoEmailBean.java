package com.algaworks.pedidovenda.controller;

import java.io.Serializable;
import java.util.Locale;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.inject.Named;

import org.apache.commons.mail.DefaultAuthenticator;
import org.apache.commons.mail.Email;
import org.apache.commons.mail.EmailException;
import org.apache.commons.mail.SimpleEmail;
import org.apache.velocity.tools.generic.NumberTool;
import org.omnifaces.util.Faces;

import com.algaworks.pedidovenda.model.Pedido;
import com.algaworks.pedidovenda.util.jsf.FacesUtil;
import com.algaworks.pedidovenda.util.mail.Mailer;
import com.outjected.email.api.MailMessage;
import com.outjected.email.impl.MailMessageImpl;
import com.outjected.email.impl.SimpleMailConfig;
import com.outjected.email.impl.templating.velocity.VelocityTemplate;

@Named
@RequestScoped
public class EnvioPedidoEmailBean implements Serializable{

	private static final long serialVersionUID = 1L;
	
	
	@Inject //Bean CDI	
	private Mailer mailer;
	
	@Inject
	@PedidoEdicao //Qualificador
	private Pedido pedido;
	
	public void enviarPedido() throws EmailException{
		
		SimpleMailConfig config = new SimpleMailConfig();
		config.setServerHost("smtp.mandrillapp.com");
		config.setServerPort(587);
		config.setEnableSsl(false);
		config.setEnableTls(true);
		config.setAuth(true);
		config.setUsername("lojaspresencamodas@gmail.com");
		config.setPassword("6Gzymq-VVOOKGSJ2cJATQA");

		MailMessage message = new MailMessageImpl(config);

		message.from("lojaspresencamodas@gmail.com")
			.to(this.pedido.getCliente().getEmail())
			.subject("Presença Modas")
			.bodyHtml(new VelocityTemplate(getClass().getResourceAsStream("/emails/pedido.template")))
			.put("pedido", this.pedido)
			.put("numberTool", new NumberTool())
			.put("locale", new Locale("pt", "BR"))
			.send();
		
		
		
/*		//Pega uma instacia
		MailMessage message = mailer.novaMensagem();
		//Destinatario
		message.to(this.pedido.getCliente().getEmail())
		.subject("Pedido "+this.pedido.getId())
		.bodyHtml("<strong>Valor Total: </strong>"+this.pedido.getValorTotal())
		.send();
	*/
		
/*		Email email = new SimpleEmail();
		email.setHostName("smtp.mandrillapp.com");
		email.setSmtpPort(587);
		email.setAuthenticator(new DefaultAuthenticator("brunooliveira.mac@gmail.com", "CsZJR-_JBKX4UJ23itNewA"));
		email.setSSLOnConnect(true);
		email.setFrom("brunooliveira.mac@gmail.com");
		email.setSubject("TestMail");
		email.setMsg("This is a test mail ... :-)");
		email.addTo("brunoli.macedo@gmail.com");
		email.send();*/
		
		FacesUtil.addInfoMessage("Email enviado com sucesso!");
	}

}
