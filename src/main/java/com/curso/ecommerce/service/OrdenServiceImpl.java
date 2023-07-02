package com.curso.ecommerce.service;

import java.util.List;
import java.util.Optional;
import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.curso.ecommerce.entity.Orden;
import com.curso.ecommerce.entity.Usuario;
import com.curso.ecommerce.repository.OrdenRepository;

@Service
public class OrdenServiceImpl implements OrdenService{

	@Autowired
	private OrdenRepository ordenRepository;
	
	@Override
	public Orden save(Orden orden) {
		return ordenRepository.save(orden);
		
	}

	@Override
	public List<Orden> findAll() {
		
		return ordenRepository.findAll();
	}
	
	public String generarNumeroOrden() {
		int numero=0;
		
		String numeroConcatenado="";
		
		List<Orden> ordenes=findAll();
		List<Integer> numeros= new ArrayList<Integer>();
		
		ordenes.stream().forEach(p -> numeros.add(Integer.parseInt(p.getNumero())));
		
		if(ordenes.isEmpty()) {
			numero=1;
		}else {
			numero=numeros.stream().max(Integer::compare).get();
			numero++;
		}
		
		if(numero<10) {
			numeroConcatenado="000000000"+String.valueOf(numero);
		}else if(numero <100) {
			numeroConcatenado="00000000"+String.valueOf(numero);
		}
		else if(numero <1000) {
			numeroConcatenado="0000000"+String.valueOf(numero);
		}else if(numero <10000) {
			numeroConcatenado="0000000"+String.valueOf(numero);
		}
		return numeroConcatenado;
	}

	@Override
	public List<Orden> findByUsuario(Usuario usuario) {
		
		return ordenRepository.findByUsuario(usuario);
	}

	@Override
	public Optional<Orden> findById(Integer id) {
		return ordenRepository.findById(id);
	}

	
}

