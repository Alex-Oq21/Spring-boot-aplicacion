package com.alexis.aplicacion.Exception;

public class UsernameOrIdNotfound  extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = -1504180028580337796L;
	public UsernameOrIdNotfound() {
		super("Usuario o ID no encontrado");
	}
	public UsernameOrIdNotfound(String message) {
		super(message);
	}
}
