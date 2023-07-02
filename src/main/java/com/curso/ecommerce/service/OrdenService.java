package com.curso.ecommerce.service;

import java.util.List;
import java.util.Optional;

import com.curso.ecommerce.entity.Orden;
import com.curso.ecommerce.entity.Usuario;

public interface OrdenService {
	List <Orden> findAll();
	Orden save(Orden orden);
	Optional <Orden> findById(Integer id);
	String generarNumeroOrden();
	List<Orden> findByUsuario(Usuario usuario);

}