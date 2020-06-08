package com.alexis.aplicacion.service;



import com.alexis.aplicacion.Exception.UsernameOrIdNotfound;
import com.alexis.aplicacion.dto.ChangePasswordForm;
import com.alexis.aplicacion.entity.Uuser;

public interface UserService {
	public Iterable<Uuser> getAllUusers();

	public  Uuser createUuser( Uuser uuser) throws Exception;
	public Uuser getUuserById(Long id) throws Exception;
	public Uuser updateUuser(Uuser uuser)throws Exception;
	public void deleteUuser(Long id)throws UsernameOrIdNotfound;
	public Uuser changePassword(ChangePasswordForm form) throws Exception;

}
