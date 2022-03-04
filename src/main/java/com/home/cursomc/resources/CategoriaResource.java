package com.home.cursomc.resources;

import java.net.URI;
import java.util.List;
import java.util.stream.Collectors;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.home.cursomc.domain.Categoria;
import com.home.cursomc.dto.CategoriaDto;
import com.home.cursomc.services.CategoriaService;


@RestController
@RequestMapping(value="/categorias")
public class CategoriaResource {

	@Autowired
	private CategoriaService service;
	
	@GetMapping
	public ResponseEntity<List<CategoriaDto>> findAll() {
		List<Categoria> list = service.findAll();
		
		List<CategoriaDto> listDto = list.stream()
										.map(CategoriaDto::new)
										.collect(Collectors.toList());
		
		return ResponseEntity.ok().body(listDto); 
	}
	
	@GetMapping("/page")
	public ResponseEntity<Page<CategoriaDto>> findPage(@RequestParam(value="page", defaultValue="0") Integer page, 
														@RequestParam(value="linesPerPage", defaultValue="24") Integer linesPerPage, 
														@RequestParam(value="orderBy", defaultValue="nome") String orderBy,
														@RequestParam(value="direction", defaultValue="ASC") String direction) {
		Page<Categoria> list = service.findPage(page, linesPerPage, orderBy, direction);
		
		Page<CategoriaDto> listDto = list.map(CategoriaDto::new);
		
		return ResponseEntity.ok().body(listDto); 
	}
	
	@RequestMapping(value="/{id}", method=RequestMethod.GET)
	public ResponseEntity<Categoria> find(@PathVariable Integer id) {
		Categoria obj = service.find(id);

		return ResponseEntity.ok().body(obj);
	}
	
	@PreAuthorize("hasAnyRole('ADMIN')")
	@PostMapping()
	public ResponseEntity<Void> create(@Valid @RequestBody CategoriaDto objDto) {
		Categoria obj = service.fromDto(objDto);
		obj = service.create(obj);
		URI uri = ServletUriComponentsBuilder
						.fromCurrentRequest()
						.path("/{id}")
						.buildAndExpand(obj.getId())
						.toUri();
		return ResponseEntity.created(uri).build();
	}
	
	@PreAuthorize("hasAnyRole('ADMIN')")
	@PutMapping(value= "/{id}")
	public ResponseEntity<Void> update(@PathVariable Integer id,@Valid @RequestBody CategoriaDto dto) {
		Categoria obj = service.fromDto(dto);
		obj.setId(id);
		obj = service.update(obj);
		return ResponseEntity.noContent().build();
	}
	
	@PreAuthorize("hasAnyRole('ADMIN')")
	@DeleteMapping("/{id}")
	public ResponseEntity<Void> delete(@PathVariable Integer id) {
		service.delete(id);
		return ResponseEntity.noContent().build();
	}
}
