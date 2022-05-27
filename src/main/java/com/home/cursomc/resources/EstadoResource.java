package com.home.cursomc.resources;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.home.cursomc.dto.CidadeDto;
import com.home.cursomc.dto.EstadoDto;
import com.home.cursomc.services.CidadeService;
import com.home.cursomc.services.EstadoService;

@RestController
@RequestMapping(value="/estados")
public class EstadoResource {

	@Autowired
	EstadoService service;
	
	@Autowired
	CidadeService cidadeService;
	
	@GetMapping
	public ResponseEntity<List<EstadoDto>> findAll() {
		List<EstadoDto> list = service.findAll().stream().map(EstadoDto::new).collect(Collectors.toList());
		return ResponseEntity.ok().body(list);
	}
	
	@GetMapping("/{estado_id}/cidades")
	public ResponseEntity<List<CidadeDto>> findAll(@PathVariable Integer estado_id) {
		List<CidadeDto> list = cidadeService.findByEstado(estado_id).stream().map(CidadeDto::new).collect(Collectors.toList());
		
		return ResponseEntity.ok().body(list);
	}
}
