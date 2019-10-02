package com.mballem.curso.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;

@SpringBootApplication
public class DemoSecurityApplication implements CommandLineRunner{

	public static void main(String[] args) {
		SpringApplication.run(DemoSecurityApplication.class, args);
		System.out.println("Usuário previamente cadastrado no banco de dados com perfil 1 ADMIN:");
		System.out.println("admin@clinica.com.br senha: 123456");
	}

	@Autowired
	JavaMailSender sender;
	
	@Override
	public void run(String... args) throws Exception {
		
		//para teste de envio de email
		System.out.println("enviando email...");
		SimpleMailMessage simple = new SimpleMailMessage();
		simple.setTo("filipe.couto.10@gmail.com");
		simple.setText("Prezado, envio este email como Teste");//corpo do email
		simple.setSubject("URGENTE- teste de envio de email");//Título
		sender.send(simple);
		System.out.println("email ENVIADO");
	}
}
