package com.alexis.aplicacion.service;

import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.alexis.aplicacion.entity.Uuser;
import com.alexis.aplicacion.repository.UserRepository;
@Service
public  class UserServiceImpl implements UserService {
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
		if(uuser.getConfirmPassword() == null || uuser.getConfirmPassword().isEmpty()) {
			throw new Exception ("El campo confirmar contraseña es obligatorio");
		}
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
	@Override
	public Uuser getUuserById(Long id) throws Exception {
		
		return repository.findById(id).orElseThrow(() -> new Exception("El usuario que intenta editar no existe"));
	}
	@Override
	public Uuser updateUuser(Uuser fromUuser) throws Exception {
		Uuser  toUuser = getUuserById(fromUuser.getId());
 	    mapUuser(fromUuser, toUuser);
 	    return repository.save(toUuser);
		
	}
	protected void mapUuser(Uuser from, Uuser to) {
		to.setUsername(from.getUsername());
		to.setFirstName(from.getFirstName());
		to.setLastName(from.getLastName());
		to.setEmail(from.getEmail());
		to.setRoles(from.getRoles());
	}
}
