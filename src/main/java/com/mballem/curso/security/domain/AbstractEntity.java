package com.mballem.curso.security.domain;

import java.io.Serializable;

import javax.persistence.*;

/**
 * Superclasse de todas as classes de entidade. 
 * Seu uso traz uma economia de linhas de códigos nas entidades já que o id e métodos comuns serão herdados dessa classe..
 * 
 * */
@SuppressWarnings("serial")
@MappedSuperclass //Designa uma classe cujas informações de mapeamento são aplicadas às entidades que herdam dela. Uma superclasse mapeada não possui uma tabela separada definida para ela.
public abstract class AbstractEntity implements Serializable  {

	@Id @GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	public AbstractEntity() {
		super();
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}
	
	public boolean hasNotId() {
		return id == null;
	}

	public boolean hasId() {
		return id != null;
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		AbstractEntity other = (AbstractEntity) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return String.format("Entidade %s id: %s", this.getClass().getName(), getId());
	}	
}
