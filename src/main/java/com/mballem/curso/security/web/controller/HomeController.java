package com.mballem.curso.security.web.controller;

import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {

	// abrir pagina home
	@GetMapping({"/", "/home"})
	public String home() {
		return "home";
	}
	
	// Método para abrir pagina login
	@GetMapping({"/login"})
	public String login() {
		return "login";
	}
	
	// Método em caso de login inválido
	@GetMapping({"/login-error"})
	public String loginError(ModelMap model) {
		model.addAttribute("alerta", "erro");
		model.addAttribute("titulo", "Credenciais inválidas!");
		model.addAttribute("texto", "Login ou senha incorretos, tente novamente.");
		model.addAttribute("subtexto", "Acesso permitido apenas para cadastros já ativados");
		
		return "login";
	}
	
	/*
	 * Método que será chamado pelo Spring secutiy sempre que uma exceção de acesso negado AccessDeniedException for lançada.
	 * Os endpoints e recursos permitidos para cada perfil foram configurados na classe SecurityConfig. 
	 * O spring security chega a este método pois foi definido .accessDeniedPage("/acesso-negado") na classe SecurityConfig
	 * 	*/
	@GetMapping({"/acesso-negado"})
	public String acessoNegado(ModelMap model, HttpServletResponse resp) {
		model.addAttribute("status", resp.getStatus());//status, error e message são váriaveis definidas, via thymeleaf, na página templates/usuario/error.html
		model.addAttribute("error", "Acesso negado");
		model.addAttribute("message", "Sem permissão para acessar a área");
		
		return "error";//abrirá a página de erro.html
	}	
}
