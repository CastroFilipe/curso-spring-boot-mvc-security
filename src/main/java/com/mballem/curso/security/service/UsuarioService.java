package com.mballem.curso.security.service;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.mballem.curso.security.datatables.Datatables;
import com.mballem.curso.security.datatables.DatatablesColunas;
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
 * 
 * O Spring Security precisa apenas que criemos um método de consulta pelo login do usuário, sem que precisemos testar a senha. A senha será testada 
 * pelo próprio Spring Security
 * */
@Service
public class UsuarioService implements UserDetailsService {

	@Autowired
	UsuarioRepository usuarioRepository;
	
	@Autowired
	private Datatables datatables;

	@Transactional(readOnly = true)
	public Usuario buscarPorEmail(String email) {
		return usuarioRepository.findByEmail(email);
	}

	@Transactional(readOnly = true)//necessário para evitar exceção LazyInitializationException devido ao método getPerfis()
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

	@Transactional(readOnly = true)
	public Map<String, Object> buscarTodos(HttpServletRequest request) {
		datatables.setRequest(request);
		datatables.setColunas(DatatablesColunas.USUARIOS);
		
		Page<Usuario> page = datatables.getSearch().isEmpty()//testa se está vázio. Ou seja se o usuário digitou algo na caixa de pesquisa
				? usuarioRepository.findAll(datatables.getPageable()) //se vázio, busca todos com o objeto page default presente em datatables
				: usuarioRepository.findByEmailOrPerfil(datatables.getSearch(), datatables.getPageable());//se não buscará por email ou perfil, de acordo com o que o usuário digitou
		return datatables.getResponse(page);
	}

	/**
	 * Método que salva um novo usuário. Antes de salvar, a senha será criptografada.
	 * */
	@Transactional(readOnly = false)
	public void salvarUsuario(Usuario usuario) {
		//usa a criptografia Bcrypt para codificar a senha
		String crypt = new BCryptPasswordEncoder().encode(usuario.getSenha());
		usuario.setSenha(crypt);
		
		usuarioRepository.save(usuario);	
	}

}
