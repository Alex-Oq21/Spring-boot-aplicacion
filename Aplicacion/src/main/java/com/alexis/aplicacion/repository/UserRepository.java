package com.alexis.aplicacion.repository;

import java.util.Optional;


import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.alexis.aplicacion.entity.Uuser;

@Repository
public interface UserRepository extends CrudRepository<Uuser, Long> {
	
	public Optional<Uuser> findByUsername(String username);
}
