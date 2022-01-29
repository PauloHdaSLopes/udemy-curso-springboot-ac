package com.home.cursomc.services.validation;

import java.util.ArrayList;
import java.util.List;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import org.springframework.beans.factory.annotation.Autowired;

import com.home.cursomc.domain.Cliente;
import com.home.cursomc.domain.enums.TipoCliente;
import com.home.cursomc.dto.ClienteNewDto;
import com.home.cursomc.repositories.ClienteRepository;
import com.home.cursomc.resources.exceptions.FieldMessage;
import com.home.cursomc.services.validation.utils.BR;

public class ClienteInsertValidator implements ConstraintValidator<ClienteInsert, ClienteNewDto>{

	@Autowired
	private ClienteRepository repo;
	
	@Override
	public void initialize(ClienteInsert nn) {
		
	}
	
	@Override
	public boolean isValid(ClienteNewDto dto, ConstraintValidatorContext context) {
		List<FieldMessage> list = new ArrayList<>();
		
		if (dto.getTipo().equals(TipoCliente.PESSOAFISICA.getCod())
				&& !BR.isValidCpf(dto.getCpfOuCnpj())) {
			list.add(new FieldMessage("cpfOuCnpj", "CPF inválido"));
		}
		
		if (dto.getTipo().equals(TipoCliente.PESSOAJURIDICA.getCod())
				&& !BR.isValidCnpj(dto.getCpfOuCnpj())) {
			list.add(new FieldMessage("cpfOuCnpj", "CNPJ inválido"));
		}
		
		Cliente aux = repo.findByEmail(dto.getEmail());
		if (aux != null) {
			list.add(new FieldMessage("email", "Email já existente"));
		}
		
		for (FieldMessage e: list) {
			context.disableDefaultConstraintViolation();
			context.buildConstraintViolationWithTemplate(e.getMessage())
				.addPropertyNode(e.getFieldName())
				.addConstraintViolation();
		}
		return list.isEmpty();
	};
}
