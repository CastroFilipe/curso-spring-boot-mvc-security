package com.mballem.curso.security.service;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring5.SpringTemplateEngine;

@Service
public class EmailService {
	
	/*
	 * Usada para o envio do email para o servidor de email.
	 */
	@Autowired
	private JavaMailSender mailSender;

	/*
	 * Engine template. permite usar o template email/confirmacao.html nas mensagens enviadas
	 * */
	@Autowired
	private SpringTemplateEngine template;
	
	/**
	 * Envia um email na tentativa de cadastro de um Usuário.
	 * 
	 * @param destino o email cadastrado pelo usuario e que receberá o código de confirmação do cadastro.
	 * @param codigo o codigo de confirmação usado para validar o cadastro.
	 * @throws MessagingException 
	 * */
	public void enviarPedidoDeConfirmacaoCadastro(String destino, String codigo) throws MessagingException {
		MimeMessage message = mailSender.createMimeMessage();
		MimeMessageHelper helper = new MimeMessageHelper(message, MimeMessageHelper.MULTIPART_MODE_MIXED_RELATED, "UTF-8");
		
		//usado para substituir os valores titulo, texto e linkConfirmacao no template thymeleaf email/confirmacao
		Context context = new Context();
		context.setVariable("titulo", "Bem vindo a clínica Spring");
		context.setVariable("texto", "clique aqui para confirmar");
		context.setVariable("linkConfirmacao", "http://localhost:8090/u/confirmacao/cadastro?codigo="+codigo);//envia o link onde o usuário deverá clicar para confirmar o cadastro
		
		String html = template.process("email/confirmacao", context);
		
		//preparando o email
		helper.setTo(destino);//destinatário
		helper.setText(html, true);//conteúdo do email
		helper.setSubject("Confirmação de cadastro");//título do email
		helper.setFrom("nao-responder@clinica.com.br");//Email que é exibido como Remetente. Porém não é o email que de fato envia a mensagem.(funciona apenas em alguns servidores de email)
		
		//adiciona o logo da clínica no template thymeleaf email/confirmacao.html
		helper.addInline("logo", new ClassPathResource("static/image/spring-security.png"));//obs: deixar essa linha como última das configurações helper.
		
		mailSender.send(message);
	}
}
