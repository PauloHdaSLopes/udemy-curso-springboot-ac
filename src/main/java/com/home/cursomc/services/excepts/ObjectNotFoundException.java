package com.home.cursomc.services.excepts;

public class ObjectNotFoundException extends RuntimeException{

	public ObjectNotFoundException(String msg) {
		super(msg);
	}

	private static final long serialVersionUID = 1L;
	
}
