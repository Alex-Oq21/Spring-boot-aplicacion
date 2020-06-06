package com.alexis.aplicacion.service;

import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alexis.aplicacion.dto.ChangePasswordForm;
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
	public Uuser createUuser(Uuser uuser)throws Exception {
			
		if (CheckusernameAvaliable(uuser) && CheckpasswordValid(uuser))  {
			uuser = repository.save(uuser);
			
		}
		return uuser;
	}
	@Override
	public Uuser getUuserById(Long id) throws Exception {
		
		return repository.findById(id).orElseThrow(() -> new Exception("El usuario  no existe"));
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
	@Override
	public void deleteUuser(Long id) throws Exception {
		Uuser uuser= getUuserById(id);
		repository.delete(uuser);
		
	}
	@Override
	public Uuser changePassword(ChangePasswordForm form) throws Exception {
		Uuser uuser = getUuserById(form.getId());
		if ( ! uuser.getPassword().equals(form.getCurrentPassword())) {
			throw new Exception("Contraseña Actual Invalido");
		}
		if (uuser.getPassword().equals(form.getNewPassword())) {
			throw new Exception("La nueva contraseña debe ser diferente a la actual");
		}
		if (! form.getNewPassword().equals(form.getConfirmPassword())) {
			throw new Exception("Las contraseñas no coinciden");
		}
			uuser.setPassword(form.getNewPassword());
		return repository.save(uuser);
	}
}
