package com.alexis.aplicacion.service;

import java.util.Optional;
import javax.validation.Valid;
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
		return repository.findAll();
	}
	private boolean CheckusernameAvaliable(Uuser uuser) throws Exception {
		Optional<Uuser> userFound = repository.findByUsername(uuser.getUsername());
		if (userFound.isPresent()) {
			throw new Exception("Username no disponible");
		}
			return true;
	}
	private boolean CheckpasswordValid(Uuser uuser) throws Exception{
		if (! uuser.getPassword().equals(uuser.getConfirmPassword())) {
			throw new Exception("No coinciden las contraseñas");
		}
		return true;
	}
	@Override
	public Uuser createUser(Uuser uuser)throws Exception {
		if (CheckusernameAvaliable(uuser) && CheckpasswordValid(uuser))  {
			uuser = repository.save(uuser);
		}
		return uuser;
	}
}
