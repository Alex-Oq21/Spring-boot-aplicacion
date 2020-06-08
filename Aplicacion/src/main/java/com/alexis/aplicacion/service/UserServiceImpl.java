package com.alexis.aplicacion.service;

import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.alexis.aplicacion.Exception.UsernameOrIdNotfound;
import com.alexis.aplicacion.dto.ChangePasswordForm;
import com.alexis.aplicacion.entity.Uuser;
import com.alexis.aplicacion.repository.UserRepository;
@Service
public  class UserServiceImpl implements UserService {
	@Autowired
	UserRepository repository;
	BCryptPasswordEncoder bCryptPasswordEncoder;
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
			String encodePassword = bCryptPasswordEncoder.encode(uuser.getPassword());
			uuser.setPassword(encodePassword);
			uuser = repository.save(uuser);
			
		}
		return uuser;
	}
	@Override
	public Uuser getUuserById(Long id) throws UsernameOrIdNotfound {
		
		return repository.findById(id).orElseThrow(() -> new UsernameOrIdNotfound("El usuario  no existe"));
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
	@PreAuthorize("hasAnyRole('ROLE_ADMIN')")
	public void deleteUuser(Long id) throws UsernameOrIdNotfound {
		Uuser uuser= getUuserById(id);
		repository.delete(uuser);
		
	}
	@Override
	public Uuser changePassword(ChangePasswordForm form) throws Exception {
		Uuser uuser = getUuserById(form.getId());
		if ( !isLoggedUserADMIN() &&! uuser.getPassword().equals(form.getCurrentPassword())) {
			throw new Exception("Contraseña Actual Invalida");
		}
		if (uuser.getPassword().equals(form.getNewPassword())) {
			throw new Exception("La nueva contraseña debe ser diferente a la actual");
		}
		if (! form.getNewPassword().equals(form.getConfirmPassword())) {
			throw new Exception("Las contraseñas no coinciden");
		}
			String encodePassword = bCryptPasswordEncoder.encode(form.getNewPassword());
			uuser.setPassword(encodePassword);
			return repository.save(uuser);
	}
	public boolean isLoggedUserADMIN(){
		 return loggedUserHasRole("ROLE_ADMIN");
		}

		public boolean loggedUserHasRole(String role) {
		 Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		 UserDetails loggedUser = null;
		 Object roles = null; 
		 if (principal instanceof UserDetails) {
		  loggedUser = (UserDetails) principal;
		 
		  roles = loggedUser.getAuthorities().stream()
		    .filter(x -> role.equals(x.getAuthority() ))      
		    .findFirst().orElse(null); //loggedUser = null;
		 }
		 return roles != null ?true :false;
}
}
