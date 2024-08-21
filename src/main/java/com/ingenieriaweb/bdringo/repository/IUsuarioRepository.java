package com.ingenieriaweb.bdringo.repository;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.ingenieriaweb.bdringo.model.Usuario;
import java.util.Optional;


@Repository
public interface IUsuarioRepository extends JpaRepository<Usuario,Integer> {
	Optional<Usuario> findByEmail(String email);
	Usuario findByUsername(String username);

}
