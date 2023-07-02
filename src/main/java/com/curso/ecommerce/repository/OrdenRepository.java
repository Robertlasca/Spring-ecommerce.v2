package com.curso.ecommerce.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.curso.ecommerce.entity.Orden;
import com.curso.ecommerce.entity.Usuario;
@Repository
public interface OrdenRepository extends JpaRepository<Orden,Integer>{

	List<Orden> findByUsuario(Usuario usuario);
}