package com.mballem.curso.security.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import com.mballem.curso.security.domain.PerfilTipo;
import com.mballem.curso.security.service.UsuarioService;

/**
 * Arquivo de configuração que sobreescreve a configuração default do spring security.
 * Ao adicionar a dependencia do SpringSecurity propriedades padrões de segurança são implementadas no projeto. Essas propriedades podem ser sobrescritas na classe
 * SecurityConfig que extende WebSecurityConfigurerAdapter.
 * 
 * */
/*
 * @EnableWebSecurity Anotação usada para informar ao Springframework que essa é uma classe de configuração do springsecurity.
 * A super classe WebSecurityConfigurerAdapter possui métodos de configurações prontos e que serão sobrescritos de acordo com a necessidade.
 */
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter{

	//constantes usadas para facilitar.
	private static final String ADMIN = PerfilTipo.ADMIN.getDesc();
	private static final String MEDICO = PerfilTipo.MEDICO.getDesc();
	private static final String PACIENTE = PerfilTipo.PACIENTE.getDesc();
	
	@Autowired
	private UsuarioService usuarioService;
	
	/**
	 * Método configure com as configurações de segurança e acesso.
	 * No método configure() usamos a variável http, de HttpSecurity, para definir as regras de acessos. Entre essas regras devemos tornar 
	 * público o acesso aos arquivos estáticos da aplicação. Esses arquivos acabam sendo bloqueados pelo Spring Security quando iniciamos nossa
	 * própria configuração.
	 * */
	@Override
	protected void configure(HttpSecurity http) throws Exception {
		
		http.authorizeRequests()//autoriza as seguintes requisições...

		//acessos públicos liberados para todos
		.antMatchers("/webjars/**","/css/**","/image/**", "/js/**").permitAll()//libera os recursos de css, imagens e js para todos que visualizam as páginas.
		.antMatchers("/", "/home").permitAll()//libera o acesso público a página /home
		.antMatchers("/u/novo/cadastro", "/u/cadastro/realizado", "/u/cadastro/paciente/salvar").permitAll()//endipoints para o acesso a página e metodos de cadastro de novo usuario
		
		//acessos privados para perfil de ADMIN
		.antMatchers("/u/editar/senha", "/u/confirmar/senha").hasAnyAuthority(MEDICO,PACIENTE)
		.antMatchers("/u/**").hasAuthority(ADMIN)//libera o endpoint /u/** para usuários com o perfil de ADMIN. Assim as telas de alguns cadastros serão visíveis apenas para admins.
		
		//acessos privados para perfil MEDICO
		.antMatchers("/medicos/dados", "/medicos/editar", "/medicos/salvar").hasAnyAuthority(MEDICO, ADMIN)//libera os endpoints para MEDICO e ADMIN
		.antMatchers("/medicos/**").hasAuthority(MEDICO)//Acessos que serão exclusivos do perfil medico
		
		//acessos privados para perfil PACIENTE
		.antMatchers("/pacientes/**").hasAuthority(PACIENTE)
		
		//acessos privados para o endpoint /especialidades
		.antMatchers("/especialidades/titulo").hasAnyAuthority(MEDICO, ADMIN)
		.antMatchers("/especialidades/**").hasAuthority(ADMIN)
		
		.anyRequest().authenticated() //Esse método informa que qualquer solicitação a aplicação deve estar autenticada, a menos é claro, aquelas que foram liberadas como públicas.
		
		
		.and()//serve para concatenar instruções de diferentes tipos
			.formLogin()//O método indica que, a partir daqui, as configurações de login serão editadas
			.loginPage("/login")//Método usado para indicar qual a URI de acesso ao login. Essa URI deve ser a action do formulário de autenticação (form action="/login")
			.defaultSuccessUrl("/", true)//endpoint para o qual será redirecionado em caso de sucesso no login
			.failureUrl("/login-error")//endpoint para o qual será redirecionado em caso de falha no login
			.permitAll()//O processo de login é finalizado com o permitAll() para que ele seja público. Logo, mesmo usuários não logados poderão ver a tela de login. Todos devem ter acesso aos endipoints /login e /login-error
		
		.and()
			.logout()//define o comportamento de logout
			.logoutSuccessUrl("/")//endpoint para o qual será redirecionado quando fizer logout
		
		//instruções para captura de exceção quando houver acesso negado
		.and()
			.exceptionHandling()//captura exceções
			.accessDeniedPage("/acesso-negado")//endpoint do controler que fará o tratamento da exceção capturada do tipo AccessDeniedException
		;
		
		//http.csrf().disable();//usar apenas em aplicações RESTFUL
	}

	/**
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
