package com.home.cursomc.resources;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.home.cursomc.domain.Produto;
import com.home.cursomc.dto.ProdutoDto;
import com.home.cursomc.resources.utils.URL;
import com.home.cursomc.services.ProdutoService;

import java.util.List;

@RestController
@RequestMapping(value="/produtos")
public class ProdutoResource {

	@Autowired
	private ProdutoService service;
	
//	@GetMapping
//	public ResponseEntity<List<ProdutoDto>>categorias findAll() {
//		List<Produto> list = service.findAll();
//		
//		List<ProdutoDto> listDto = list.stream()
//										.map(ProdutoDto::new)
//										.collect(Collectors.toList());
//		
//		return ResponseEntity.ok().body(listDto); 
//	}
	
	@GetMapping()
	public ResponseEntity<Page<ProdutoDto>> findPage(@RequestParam(value="nome", defaultValue="0") String nome,
													 @RequestParam(value="categorias", defaultValue="0") String categorias,
													 @RequestParam(value="page", defaultValue="0") Integer page, 
													 @RequestParam(value="linesPerPage", defaultValue="24") Integer linesPerPage, 
													 @RequestParam(value="orderBy", defaultValue="nome") String orderBy,
													 @RequestParam(value="direction", defaultValue="ASC") String direction) {
		List<Integer> ids = URL.decodeIntList(categorias);
		nome = URL.decodeParam(nome);
		
		Page<Produto> list = service.search(nome, ids, page, linesPerPage, orderBy, direction);
		
		Page<ProdutoDto> listDto = list.map(ProdutoDto::new);
		
		return ResponseEntity.ok().body(listDto); 
	}
	
	@RequestMapping(value="/{id}", method=RequestMethod.GET)
	public ResponseEntity<Produto> find(@PathVariable Integer id) {
		Produto obj = service.find(id);

		return ResponseEntity.ok().body(obj);
	}
	
//	@PostMapping()
//	public ResponseEntity<Void> create(@Valid @RequestBody ProdutoDto objDto) {
//		Produto obj = service.fromDto(objDto);
//		obj = service.create(obj);
//		URI uri = ServletUriComponentsBuilder
//						.fromCurrentRequest()
//						.path("/{id}")
//						.buildAndExpand(obj.getId())
//						.toUri();
//		return ResponseEntity.created(uri).build();
//	}
	
//	@PutMapping(value= "/{id}")
//	public ResponseEntity<Void> update(@PathVariable Integer id,@Valid @RequestBody ProdutoDto dto) {
//		Produto obj = service.fromDto(dto);
//		obj.setId(id);
//		obj = service.update(obj);
//		return ResponseEntity.noContent().build();
//	}
	
//	@DeleteMapping("/{id}")
//	public ResponseEntity<Void> delete(@PathVariable Integer id) {
//		service.delete(id);
//		return ResponseEntity.noContent().build();
//	}
}
