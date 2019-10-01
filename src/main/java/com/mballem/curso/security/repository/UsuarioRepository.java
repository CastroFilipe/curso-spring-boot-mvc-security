package com.mballem.curso.security.repository;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.mballem.curso.security.domain.Usuario;

@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, Long>{

	@Query("SELECT u From Usuario u WHERE u.email LIKE :email")
	Usuario findByEmail(@Param("email") String email);

	//Busca por email ou perfil.
	@Query("SELECT DISTINCT u From Usuario u INNER JOIN u.perfis p WHERE u.email LIKE :search% OR p.desc LIKE :search%")
	Page<Usuario> findByEmailOrPerfil(@Param("search") String search, Pageable pageable);

	@Query("SELECT DISTINCT u FROM Usuario u INNER JOIN u.perfis p WHERE u.id = :usuarioId AND p.id IN :perfisId")
	Optional<Usuario> findByIdAndPerfis(Long usuarioId, Long[] perfisId);

	@Query("SELECT DISTINCT u FROM Usuario u WHERE u.email LIKE :email AND u.ativo = true")
	Optional<Usuario> findByEmailAndAtivo(String email);
}
