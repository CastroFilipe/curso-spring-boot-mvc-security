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
	 * Classe do pacote thymeleaf. Engine template que permite usar o template email/confirmacao.html nas mensagens enviadas
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
		
		//classe context do pacote thymeleaf. usada para substituir os valores titulo, texto e linkConfirmacao no template thymeleaf email/confirmacao
		Context context = new Context();
		context.setVariable("titulo", "Bem vindo a clínica Spring");
		context.setVariable("texto", "clique no link abaixo para confirmar");
		context.setVariable("linkConfirmacao", "http://localhost:8090/u/confirmacao/cadastro?codigo="+codigo);//envia o link onde o usuário deverá clicar para confirmar o cadastro. 
		
		//os valores presentes na variável context serão usados no template email/confirmacao.html e todo o código html será atribuído a variável html
		String html = template.process("email/confirmacao", context);
		//System.out.println(html);
		
		//preparando o email
		helper.setTo(destino);//destinatário
		helper.setText(html, true);//conteúdo do email
		helper.setSubject("Confirmação de cadastro");//título do email
		helper.setFrom("nao-responder@clinica.com.br");//Email que é exibido como Remetente. Porém não é o email que de fato envia a mensagem.(funciona apenas em alguns servidores de email)
		
		//adiciona o logo da clínica no template thymeleaf email/confirmacao.html
		helper.addInline("logo", new ClassPathResource("static/image/spring-security.png"));//obs: deixar essa linha como última das configurações helper.
		
		mailSender.send(message);//enviar o email
	}
	
	/**
	 * Método para redefinição de senha.
	 * 
	 * @param destino o email do Usuario que solicitou a redefinição de senha
	 * @param verificador código criado de forma randomica pela aplicação e que será usado como código de verificação.
	 * */
	public void enviarPedidoRedefinicaoSenha(String destino, String verificador) throws MessagingException {
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = 
        		new MimeMessageHelper(message, MimeMessageHelper.MULTIPART_MODE_MIXED_RELATED, "UTF-8");
        
        Context context = new Context();
        context.setVariable("titulo", "Redefinição de Senha");
        context.setVariable("texto", "Para redefinir sua senha use o código de verficação " +
                "quando exigido no formulário." );
        context.setVariable("verificador", verificador);//código que vai na página html enviada por email para a redefinição de senha
        
        String html = template.process("email/confirmacao", context);        
        helper.setTo(destino);
        helper.setText(html, true);
        helper.setSubject("Redefinição de Senha");
        helper.setFrom("no-replay@clinica.com.br");

        helper.addInline("logo", new ClassPathResource("/static/image/spring-security.png"));  
       
        mailSender.send(message);		
	}
}
