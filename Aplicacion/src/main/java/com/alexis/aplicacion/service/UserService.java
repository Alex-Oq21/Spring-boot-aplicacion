package com.alexis.aplicacion.service;


import org.springframework.stereotype.Service;

import com.alexis.aplicacion.entity.Uuser;
@Service
public interface UserService {
	public Iterable<Uuser> getAllUusers();

	public Uuser createUser( Uuser uuser) throws Exception;
}
