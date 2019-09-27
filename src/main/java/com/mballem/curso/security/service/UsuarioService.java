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
import com.mballem.curso.security.domain.PerfilTipo;
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

	/**
	 * Método que busca um usuario pelo email. O username de um Usuario é o email
	 * */
	@Transactional(readOnly = true)
	public Usuario buscarPorEmail(String email) {
		return usuarioRepository.findByEmail(email);
	}

	/**
	 * Método presente na interface UserDetailsService. O método será chamado, de forma automatica, pelo SpringSecurity quando uma tentativa de login na aplicação 
	 * for realizada. Dentro do método o parametro username(que é o email do usuário que estará tentando fazer o login) será utilizado para fazer uma busca no banco
	 * de dados. Se o Usuario não for encontrado, será lançada a exceção UsernameNotFoundException e a tentativa de login será negada; caso contrário será retornado 
	 * o objeto User, um objeto que implementa a interface UserDetails. O objeto User conterá as informações necessárias para que o Spring Secutiry valide a senha do
	 * usuário e autorize a tentativa de login.
	 * 
	 * @param username o nome de usuário digitado pelo usuário no formulário de login
	 * */
	@Transactional(readOnly = true)//necessário para evitar exceção LazyInitializationException devido ao método getPerfis()
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		
		//faz a consulta no banco de dados pelo username
		Usuario usuario = buscarPorEmail(username);
		
		//
		return new User(// User é uma classe do Spring que implementa UserDetails. parametros do construtor: Email, senha, array de perfis
				usuario.getEmail(), 
				usuario.getSenha(),//senha criptografada vinda do usuario salvo no banco
				AuthorityUtils.createAuthorityList(convertPerfisToString(usuario.getPerfis()))
		);
	}

	/** 
	 * Método auxiliar que converte uma Lista de Perfis em um array de String.
	 * 
	 * @param perfis uma Lista de Objetos Perfil
	 * @return um array com a descrição de cada perfil presente na lista
	 * */
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

	/**
	 * Método que busca um usuario por id
	 * */
	@Transactional(readOnly = true)
	public Usuario buscarPorId(Long id) {

		return usuarioRepository.findById(id).get();
	}

	/**
	 * Método que busca um Usuario por ir e Perfis
	 * 
	 * @param usuarioId id do Usuario
	 * @param perfisId ids dos perfis que o Usuario possui.
	 * @throws UsernameNotFoundException se o usuário não for encontrado
	 * */
	@Transactional(readOnly = true)
	public Usuario buscarPorIdEPerfis(Long usuarioId, Long[] perfisId) {
		
		return usuarioRepository.findByIdAndPerfis(usuarioId, perfisId).orElseThrow(() -> new UsernameNotFoundException("Usuário não encontrado"));
	}

	/**
	 * Método que verifica se as senhas são iguais
	 * 
	 * */
	public static boolean isSenhaCorreta(String senhaDigitada, String senhaArmazenada) {
		//Método que compara duas senhas, mesmo que uma delas esteja criptografada
		return new BCryptPasswordEncoder().matches(senhaDigitada, senhaArmazenada);
	}

	@Transactional(readOnly = false)
	public void alterarSenha(Usuario usuario, String senha) {
		usuario.setSenha(new BCryptPasswordEncoder().encode(senha));
		usuarioRepository.save(usuario);
	}

	@Transactional(readOnly = false)
	public void salvarCadastroPaciente(Usuario usuario) {
		String crypt = new BCryptPasswordEncoder().encode(usuario.getSenha());
		usuario.setSenha(crypt);
		usuario.addPerfil(PerfilTipo.PACIENTE);
		usuarioRepository.save(usuario);
	}

}
