package com.mballem.curso.security.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.mballem.curso.security.domain.Perfil;
import com.mballem.curso.security.domain.Usuario;
import com.mballem.curso.security.repository.UsuarioRepository;

/*
 * A interface UserDetailsService fornece o método loadUserByUsername() necessário para fazer os testes nas credenciais (user e password).
 * Processo de login: Na submissão do formulario para login o SpringSecurity identificará a tentativa de login. Pegará as credenciais(user e senha) e
 * fará a busca em uma classe que implementa UserDetailsService e que possua o método loadUserByUsername(String username). O user será passado como
 * argumento username. Na implementação do método, uma busca no banco de dados será feita para retornar o Usuario correspondente. Algumas informações 
 * serão passadas ao SpringSecurity através do objeto User retornado. Assim o springSecurity fará os testes na senha e se o usuário possuí privilégios
 * para acessar determinada parte na aplicação.
 * */
@Service
public class UsuarioService implements UserDetailsService {

	@Autowired
	UsuarioRepository usuarioRepository;

	@Transactional(readOnly = true)
	public Usuario buscarPorEmail(String email) {
		return usuarioRepository.findByEmail(email);
	}

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		
		//faz a consulta no banco de dados pelo username
		Usuario usuario = buscarPorEmail(username);
		
		//parametros: Email, senha, perfis
		return new User(// User é uma classe do Spring que implementa UserDetails
				usuario.getEmail(), 
				usuario.getSenha(),
				AuthorityUtils.createAuthorityList(convertPerfisToString(usuario.getPerfis()))
		);
	}

	// Método auxiliar que converte uma Lista de Perfis e um array de String.
	private String[] convertPerfisToString(List<Perfil> perfis) {
		String[] authorities = new String[perfis.size()];

		// adiciona a descrição de cada perfil presente na lista perfis
		for (int i = 0; i < perfis.size(); i++) {
			authorities[i] = perfis.get(i).getDesc();
		}

		return authorities;
	}
}
