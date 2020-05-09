package com.alexis.aplicacion.conroller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class UserController {
	@RequestMapping("/")
	public String index() {
		return "index";
	}
	@RequestMapping("/userForm")
	public String userForm() {
		return"user-form/user-view";
	}
}
