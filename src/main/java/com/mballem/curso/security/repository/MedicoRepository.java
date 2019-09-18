package com.mballem.curso.security.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.mballem.curso.security.domain.Medico;

@Repository
public interface MedicoRepository extends JpaRepository<Medico, Long>{

	@Query("SELECT m FROM Medico m WHERE m.usuario.id = :id")
	Optional<Medico> findByUsuarioId(@Param("id") Long id);
}
