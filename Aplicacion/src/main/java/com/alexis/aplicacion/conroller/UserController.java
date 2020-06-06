package com.alexis.aplicacion.conroller;

import java.util.stream.Collectors;

import javax.validation.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import com.alexis.aplicacion.dto.ChangePasswordForm;
import com.alexis.aplicacion.entity.Uuser;
import com.alexis.aplicacion.repository.RoleRepository;
import com.alexis.aplicacion.service.UserService;

@Controller
public class UserController {
	@Autowired
	UserService userService;
	
	@Autowired
	RoleRepository roleRepository;
	
	@GetMapping("/")
	public String index() {
		return "index";
	}
	@GetMapping("/userForm")
	public String userForm(Model model) {
		model.addAttribute("userForm", new Uuser());
		model.addAttribute("userList", userService.getAllUusers());
		model.addAttribute("roles", roleRepository.findAll());
		model.addAttribute("listTab", "active");
		return"user-form/user-view";
	}
	@PostMapping("/userForm")    
	public String createUser(@Valid @ModelAttribute("userForm")Uuser uuser, BindingResult result, ModelMap model ) {
		if (result.hasErrors()) {
			model.addAttribute("userForm", uuser);
			model.addAttribute("formTab", "active");
		}else {
			try {
				userService.createUuser(uuser);
				model.addAttribute("userForm", new Uuser());
				model.addAttribute("listTab", "active");
			} catch (Exception e) {
				model.addAttribute("formErrorMessage ", e.getMessage());
				model.addAttribute("userForm", uuser);
				model.addAttribute("formTab", "active");	
				model.addAttribute("userList", userService.getAllUusers());
				model.addAttribute("roles", roleRepository.findAll());
				
			}
		}
		model.addAttribute("userList", userService.getAllUusers());
		model.addAttribute("roles", roleRepository.findAll());
		return "user-form/user-view";
	}
	@GetMapping("/editUser/{id}")
	public String editUser(Model model, @PathVariable(name="id")Long id ) throws Exception{
		Uuser userToEdit = userService.getUuserById(id);
		model.addAttribute("userForm", userToEdit);
		model.addAttribute("userList", userService.getAllUusers());
		model.addAttribute("roles", roleRepository.findAll());
		model.addAttribute("formTab", "active");
		model.addAttribute("editMode", "true");
		model.addAttribute("passwordForm", new ChangePasswordForm(id));
		return"user-form/user-view";

	}
	@PostMapping("/editUser")
	public String postEditUserForm(@Valid @ModelAttribute("userForm")Uuser uuser, BindingResult result, ModelMap model) {
		if (result.hasErrors()) {
			model.addAttribute("userForm", uuser);
			model.addAttribute("formTab", "active");
			model.addAttribute("editMode", "true");
			model.addAttribute("passwordForm", new ChangePasswordForm(uuser.getId()));

		}else {
			try {
				userService.updateUuser(uuser);
				model.addAttribute("userForm", new Uuser());
				model.addAttribute("listTab", "active");
			} catch (Exception e) {
				model.addAttribute("formErrorMessage ", e.getMessage());
				model.addAttribute("userForm", uuser);
				model.addAttribute("formTab", "active");	
				model.addAttribute("userList", userService.getAllUusers());
				model.addAttribute("roles", roleRepository.findAll());
				model.addAttribute("editMode", "true");
				model.addAttribute("passwordForm", new ChangePasswordForm(uuser.getId()));
				
				
			}
		}
		model.addAttribute("userList", userService.getAllUusers());
		model.addAttribute("roles", roleRepository.findAll());
		return "user-form/user-view";
	}
	@GetMapping("/userForm/cancel")
	public String cancelEditUser(ModelMap model) {
		return "redirect:/userForm";
	}
	@GetMapping("/deleteUser/{id}")
	public String deleteUser(Model model, @PathVariable(name="id")Long id) {
		try {
			userService.deleteUuser(id);
		}catch(Exception e) {
			model.addAttribute("ListErrorMessage", e.getMessage());
		}
		return userForm(model);
	}
	@PostMapping("/editUser/changePassword")
	public ResponseEntity<String> postEditUserChangePassword(@Valid @RequestBody ChangePasswordForm form, Errors errors) {
		try {
			if (errors.hasErrors()) {
				 String result = errors.getAllErrors()
	                        .stream().map(x -> x.getDefaultMessage())
	                        .collect(Collectors.joining(""));

	            throw new Exception(result);	
			  }
			userService.changePassword(form);
	}catch(Exception e) {
		return ResponseEntity.badRequest().body(e.getMessage());
	}
		return ResponseEntity.ok("Succes");
  }
}