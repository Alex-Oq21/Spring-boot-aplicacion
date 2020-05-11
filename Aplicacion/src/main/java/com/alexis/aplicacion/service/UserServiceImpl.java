package com.alexis.aplicacion.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alexis.aplicacion.entity.Uuser;
import com.alexis.aplicacion.repository.UserRepository;
@Service
public class UserServiceImpl implements UserService {
	@Autowired
	UserRepository repository;
	
	@Override
	public Iterable<Uuser> getAllUusers() {
		// TODO Auto-generated method stub
		return repository.findAll();
	}

}
