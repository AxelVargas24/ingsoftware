package com.ingenieriaweb.bdringo.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.ingenieriaweb.bdringo.model.Orden;
import com.ingenieriaweb.bdringo.model.Usuario;

@Repository
public interface IOrdenRepository extends JpaRepository<Orden,Integer>{
	List<Orden> findByUsuario(Usuario usuario);
	
}
