package com.alexis.aplicacion.conroller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import com.alexis.aplicacion.entity.Uuser;
import com.alexis.aplicacion.repository.RoleRepository;
import com.alexis.aplicacion.repository.UserRepository;
import com.alexis.aplicacion.service.UserService;

@Controller
public class UserController {
	@Autowired
	UserService userService;
	
	@Autowired
	RoleRepository roleRepository;
	
	@RequestMapping("/")
	public String index() {
		return "index";
	}
	@RequestMapping("/userForm")
	public String userForm(Model model) {
		model.addAttribute("userForm", new Uuser());
		model.addAttribute("userList", userService.getAllUusers());
		model.addAttribute("roles", roleRepository.findAll());
		model.addAttribute("listTap", "active");
		return"user-form/user-view";
	}
}
