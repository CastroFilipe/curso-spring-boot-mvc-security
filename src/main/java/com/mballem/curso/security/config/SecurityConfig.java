package com.mballem.curso.security.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import com.mballem.curso.security.service.UsuarioService;

/**
 * Arquivo de configuração que sobreescreve a configuração default do spring security.
 * 
 * */
//Anotação usada para informar ao Springframework que essa é uma classe de configuração do springsecurity
@EnableWebSecurity
//WebSecurityConfigurerAdapter possui métodos de configurações prontos e que serão sobrescritos de acordo com a necessidade.
public class SecurityConfig extends WebSecurityConfigurerAdapter{

	@Autowired
	private UsuarioService usuarioService;
	
	@Override
	protected void configure(HttpSecurity http) throws Exception {
		
		http.authorizeRequests()//autoriza requisições...
		.antMatchers("/webjars/**","/css/**","/image/**", "/js/**").permitAll()
		.antMatchers("/", "/home").permitAll()
		.anyRequest().authenticated() //Solicita autenticação para todos os outros recursos
		.and()
			.formLogin()//editar as propriedades da tela de login
			.loginPage("/login")//Método usado para indicar qual a URI de acesso ao login. (form action="/login")
			.defaultSuccessUrl("/", true)//endpoint para o qual será redirecionado em caso de sucesso no login
			.failureUrl("/login-error")//endpoint para o qual será redirecionado em caso de falha no login
			.permitAll()//O processo de login é finalizado com o permitAll() para que ele seja público. Todos devem ter acesso aos endipoints /login e /login-error
		
		.and()
			.logout()//define o comportamento de logout
			.logoutSuccessUrl("/");//endpoint para o qual será redirecionado quando fizer logout
		
		//http.csrf().disable();//usar apenas em aplicações RESTFUL
	}

	/*
	 * Método que faz a configuração para o uso de criptografia nas senhas.
	 * As senhas no banco de dados serão salvas de forma criptografada, esse método será utilizado de forma automática pelo springsecurity sempre 
	 * que o mesmo precisar checar credenciais e validar senhas na classe UserService.
	 * */
	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
		auth
		.userDetailsService(usuarioService)//O parametro é uma classe que implementa UserDetailsService
		.passwordEncoder(new BCryptPasswordEncoder());//informa o tipo de criptografia que será usada pela aplicação no momento de checar as credencias de login(senha)
	}
	
	

}
