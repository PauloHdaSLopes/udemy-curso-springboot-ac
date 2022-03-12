package com.home.cursomc.services;

import java.util.Random;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.home.cursomc.domain.Cliente;
import com.home.cursomc.repositories.ClienteRepository;
import com.home.cursomc.services.excepts.ObjectNotFoundException;

@Service
public class AuthService {

	@Autowired
	private ClienteRepository repo;
	@Autowired
	private BCryptPasswordEncoder bCrypt;
	@Autowired
	private EmailService emailService;
	
	private Random rnd = new Random();
	
	public void sendNewPassword(String email) {
		Cliente cliente = repo.findByEmail(email);
		if(cliente == null) {
			throw new ObjectNotFoundException("Email não encontrado");
		}
		
		String newPass = newPassword();
		cliente.setSenha(bCrypt.encode(newPass));
		
		repo.save(cliente);
		emailService.sendNewPasswordEmail(cliente, newPass);
	}


	private String newPassword() {
		char[] vet = new char[10];
		for (int i = 0; i < vet.length; i++) {
			vet[i] = randomChar();
		}
		return new String(vet);
	}


	private char randomChar() {
		int opt = rnd.nextInt(3);
		if(opt == 0) { // gera digito
			return (char) (rnd.nextInt(10) + 48);
		} else if (opt == 1) { // gera letra maiuscula
			return (char) (rnd.nextInt(26) + 65);
		} else { // gera minuscula
			return (char) (rnd.nextInt(26) + 97);
		}
	}
}
