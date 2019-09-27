package com.mballem.curso.security.domain;

import java.time.LocalTime;

import javax.persistence.*;

/*
 * O atributo indexes fará o mapeamento de algum indice presente no banco de dados.
 * No banco de dados utilizado por essa aplicação foi criado um indice 'idx_hora_minuto' referente a coluna 'hora_minuto' da tabela Hora.
 * O atributo indexes fará o mapeamento desse indice presente no banco de dados. Seu uso é recomendado quando um dos atributos será utilizado
 * com frequencia em consultas no banco de dados.
 */
@SuppressWarnings("serial")
@Entity
@Table(name = "horas", indexes = {@Index(name = "idx_hora_minuto", columnList = "hora_minuto")})
public class Horario extends AbstractEntity {
	
	@Column(name = "hora_minuto", unique = true, nullable = false)
	private LocalTime horaMinuto;

	public LocalTime getHoraMinuto() {
		return horaMinuto;
	}

	public void setHoraMinuto(LocalTime horaMinuto) {
		this.horaMinuto = horaMinuto;
	}
}
