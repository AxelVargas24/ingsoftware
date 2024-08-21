package com.ingenieriaweb.bdringo.service;

import java.util.List;
import java.util.Optional;

import com.ingenieriaweb.bdringo.model.Orden;
import com.ingenieriaweb.bdringo.model.Usuario;

public interface IOrdenService {
	List<Orden> findAll();
	Optional<Orden>findById(Integer id);
	Orden save (Orden orden);
	String generarNumeroOrden();
	List<Orden> findByUsuario(Usuario usuario);
}
