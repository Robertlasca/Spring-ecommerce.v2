package com.curso.ecommerce.service;

import java.util.List;
import java.util.Optional;

import com.curso.ecommerce.entity.Usuario;


public interface IUsuarioService {

	Optional<Usuario> findById(Integer id);
	
	Usuario save(Usuario usuario);
	
	Optional<Usuario> findByEmail(String email);
	
	List<Usuario> findAll();
	
	
}