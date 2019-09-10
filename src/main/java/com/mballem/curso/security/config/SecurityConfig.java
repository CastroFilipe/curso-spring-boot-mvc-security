package com.mballem.curso.security.config;

import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

/**
 * Arquivo de configuração que sobreescreve a configuração default do spring security.
 * 
 * */
//Anotação usada para informar ao Springframework que essa é uma classe de configuração do springsecurity
@EnableWebSecurity
//WebSecurityConfigurerAdapter possui métodos de configurações prontos e que serão sobrescritos de acordo com a necessidade.
public class SecurityConfig extends WebSecurityConfigurerAdapter{

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		//Solicita autenticação para todos os recursos
		http.authorizeRequests()
		.antMatchers("/webjars/**","/css/**","/image/**", "/js/**").permitAll()
		.antMatchers("/", "/home").permitAll()
		.anyRequest().authenticated();
	}

}
