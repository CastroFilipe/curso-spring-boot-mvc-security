package com.mballem.curso.security.domain;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.*;

import com.fasterxml.jackson.annotation.JsonIgnore;
/*
 * O atributo indexes fará o mapeamento de algum indice presente no banco de dados.
 * No banco de dados utilizado por essa aplicação foi criado um indice 'idx_usuario_email' referente a coluna email da tabela Usuario.
 * O atributo indexes fará o mapeamento desse indice presente no banco de dados. Seu uso é recomendado quando um dos atributos será utilizado
 * com frequencia em consultas no banco de dados. Nessa aplicação um Usuario será selecionado ou por Id ou email
 * */
@SuppressWarnings("serial")
@Entity
@Table(name = "usuarios", indexes = {@Index(name = "idx_usuario_email", columnList = "email")})
public class Usuario extends AbstractEntity {	
	
	@Column(name = "email", unique = true, nullable = false)
	private String email;
	
	@JsonIgnore//evitará que a senha de um Usuario seja exibida no Json. Mesmo a senha sendo criptografada, não é uma boa pratica exibi-lá
	@Column(name = "senha", nullable = false)
	private String senha;
	
	@ManyToMany
	@JoinTable(
		name = "usuarios_tem_perfis", 
        joinColumns = { @JoinColumn(name = "usuario_id", referencedColumnName = "id") }, 
        inverseJoinColumns = { @JoinColumn(name = "perfil_id", referencedColumnName = "id") }
	)
	private List<Perfil> perfis;
	
	@Column(name = "ativo", nullable = false, columnDefinition = "TINYINT(1)")
	private boolean ativo;
	
	@Column(name = "codigo_verificador", length = 6)
	private String codigoVerificador;
	
	public Usuario() {
		super();
	}

	public Usuario(Long id) {
		super.setId(id);
	}

	// adiciona perfis a lista
	public void addPerfil(PerfilTipo tipo) {
		if (this.perfis == null) {
			this.perfis = new ArrayList<>();
		}
		this.perfis.add(new Perfil(tipo.getCod()));
	}

	public Usuario(String email) {
		this.email = email;
	}
	
	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getSenha() {
		return senha;
	}

	public void setSenha(String senha) {
		this.senha = senha;
	}

	public List<Perfil> getPerfis() {
		return perfis;
	}

	public void setPerfis(List<Perfil> perfis) {
		this.perfis = perfis;
	}

	public boolean isAtivo() {
		return ativo;
	}

	public void setAtivo(boolean ativo) {
		this.ativo = ativo;
	}	
	
	public String getCodigoVerificador() {
		return codigoVerificador;
	}

	public void setCodigoVerificador(String codigoVerificador) {
		this.codigoVerificador = codigoVerificador;
	}

}
