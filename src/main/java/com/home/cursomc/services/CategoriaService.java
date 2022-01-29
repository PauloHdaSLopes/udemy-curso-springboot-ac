package com.home.cursomc.services;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Service;

import com.home.cursomc.domain.Categoria;
import com.home.cursomc.dto.CategoriaDto;
import com.home.cursomc.repositories.CategoriaRepository;
import com.home.cursomc.services.excepts.DataIntegrityException;
import com.home.cursomc.services.excepts.ObjectNotFoundException;

@Service
public class CategoriaService {

	@Autowired
	private CategoriaRepository repo;
	
	public Categoria find(Integer id) {
		Optional<Categoria> obj = repo.findById(id);
		return obj.orElseThrow(() -> new ObjectNotFoundException("Objeto não encontrado! Id: " + id + ", Tipo: " + Categoria.class.getName()));
	}

	public Categoria create(Categoria obj) {
		obj.setId(null);
		return repo.save(obj);
	}

	public Categoria update(Categoria obj) {
		Categoria categoriaFromRepo = find(obj.getId());
		updateData(categoriaFromRepo, obj);
		return repo.save(categoriaFromRepo);
	}

	public void delete(Integer id) {
		Categoria obj = find(id);
		try {
			repo.delete(obj);			
		} catch (DataIntegrityViolationException e) {
			throw new DataIntegrityException("Não é possivel excluir uma categoria que possua produtos!");
		}
	}

	public List<Categoria> findAll() {
		return repo.findAll();
	}
	
	public Page<Categoria> findPage(Integer page, Integer linesPerPage, String orderBy, String direction) {
		PageRequest pageRequest = PageRequest.of(page, linesPerPage, Direction.valueOf(direction), orderBy);
		return repo.findAll(pageRequest);
	}
	
	public Categoria fromDto(CategoriaDto dto) {
		return new Categoria(dto.getId(), dto.getNome());
	}
	
	private void updateData(Categoria categoriaFromRepo, Categoria obj) {
		categoriaFromRepo.setNome(obj.getNome());
	}
}
