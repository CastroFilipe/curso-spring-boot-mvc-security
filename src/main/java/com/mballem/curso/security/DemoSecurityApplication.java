package com.mballem.curso.security;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class DemoSecurityApplication {

	public static void main(String[] args) {
		SpringApplication.run(DemoSecurityApplication.class, args);
		System.out.println("Usuário previamente cadastrado no banco de dados com perfil 1 ADMIN:");
		System.out.println("admin@clinica.com.br senha: 123456");
	}
}
