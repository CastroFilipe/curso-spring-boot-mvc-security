package com.mballem.curso.security.domain;

import java.util.List;

import javax.persistence.*;

/*
 * O atributo indexes fará o mapeamento de algum indice presente no banco de dados.
 * No banco de dados utilizado por essa aplicação foi criado um indice 'idx_especialidade_titulo' referente a coluna titulo da tabela Especialidade.
 * O atributo indexes fará o mapeamento desse indice presente no banco de dados. Seu uso é recomendado quando um dos atributos será utilizado
 * com frequencia em consultas no banco de dados. Nessa aplicação uma Especialidade será selecionado ou por Id ou pelo titulo
 * */
@SuppressWarnings("serial")
@Entity
@Table(name = "especialidades", indexes = {@Index(name = "idx_especialidade_titulo", columnList = "titulo")})
public class Especialidade extends AbstractEntity {
	
	@Column(name = "titulo", unique = true, nullable = false)
	private String titulo;
	
	@Column(name = "descricao", columnDefinition = "TEXT")
	private String descricao;
	
	@ManyToMany
	@JoinTable(
			name = "medicos_tem_especialidades",
			joinColumns = @JoinColumn(name = "id_especialidade", referencedColumnName = "id"),
			inverseJoinColumns = @JoinColumn(name = "id_medico", referencedColumnName = "id")
    )
	private List<Medico> medicos;	

	public String getTitulo() {
		return titulo;
	}

	public void setTitulo(String titulo) {
		this.titulo = titulo;
	}	

	public String getDescricao() {
		return descricao;
	}

	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}

	public List<Medico> getMedicos() {
		return medicos;
	}

	public void setMedico(List<Medico> medicos) {
		this.medicos = medicos;
	}
}
