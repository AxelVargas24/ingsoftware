package com.ingenieriaweb.bdringo.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.ingenieriaweb.bdringo.model.Producto;


@Repository
public interface IProductoRepository extends JpaRepository<Producto,Integer>{
	
}
