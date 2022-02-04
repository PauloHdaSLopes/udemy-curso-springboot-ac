package com.home.cursomc.services;

import org.springframework.mail.SimpleMailMessage;

import com.home.cursomc.domain.Pedido;

public interface EmailService {
	
	void sendOrderConfirmationEmail(Pedido obj);
	void sendEmail(SimpleMailMessage msg);
}
