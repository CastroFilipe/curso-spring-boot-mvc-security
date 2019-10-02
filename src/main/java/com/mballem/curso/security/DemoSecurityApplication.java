package com.mballem.curso.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.mballem.curso.security.service.EmailService;

@SpringBootApplication
public class DemoSecurityApplication implements CommandLineRunner{

	public static void main(String[] args) {
		SpringApplication.run(DemoSecurityApplication.class, args);
		System.out.println("Usuário previamente cadastrado no banco de dados com perfil 1 ADMIN:");
		System.out.println("admin@clinica.com.br senha: 123456");
	}

	@Autowired
	EmailService emailService;
	
	@Override
	public void run(String... args) throws Exception {
		
		//para teste de envio de email
		System.out.println("enviando email...");
		
		emailService.enviarPedidoDeConfirmacaoCadastro("filipe.couto.10@gmail.com", "codFake123");
		
		System.out.println("email ENVIADO");
	}
}
