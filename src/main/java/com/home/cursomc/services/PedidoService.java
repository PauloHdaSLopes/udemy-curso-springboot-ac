package com.home.cursomc.services;

import java.util.Date;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Service;

import com.home.cursomc.domain.Cliente;
import com.home.cursomc.domain.ItemPedido;
import com.home.cursomc.domain.PagamentoComBoleto;
import com.home.cursomc.domain.Pedido;
import com.home.cursomc.domain.enums.EstadoPagamento;
import com.home.cursomc.repositories.ItemPedidoRepository;
import com.home.cursomc.repositories.PagamentoRepository;
import com.home.cursomc.repositories.PedidoRepository;
import com.home.cursomc.security.UserSS;
import com.home.cursomc.services.excepts.AuthorizationException;
import com.home.cursomc.services.excepts.ObjectNotFoundException;

@Service
public class PedidoService {

	@Autowired
	private PedidoRepository repo;
	@Autowired
	private ItemPedidoRepository itemPedidoRepo;
	@Autowired
	private PagamentoRepository pagamentoRepo;
	@Autowired
	private ClienteService clienteService;
	@Autowired
	private BoletoService boletoService;
	@Autowired
	private ProdutoService produtoService;
	@Autowired
	private EmailService emailService;
	
	public Pedido find(Integer id) {
		Optional<Pedido> obj = repo.findById(id);
		return obj.orElseThrow(() -> new ObjectNotFoundException("Objeto não encontrado! Id: " + id + ", Tipo: " + Pedido.class.getName()));
	}
	
	public Pedido create(Pedido obj) {
		obj.setId(null);
		obj.setInstante(new Date());
		
		Cliente clienteFromRepo = clienteService.find(obj.getCliente().getId());
		obj.setCliente(clienteFromRepo);
		
		obj.getPagamento().setEstado(EstadoPagamento.PENDENTE);
		obj.getPagamento().setPedido(obj);
		if(obj.getPagamento() instanceof PagamentoComBoleto) {
			PagamentoComBoleto pagto = (PagamentoComBoleto) obj.getPagamento();
			boletoService.preencherPagametoComBoleto(pagto, obj.getInstante());
		}
		
		pagamentoRepo.save(obj.getPagamento());
		
		for(ItemPedido ip: obj.getItens()) {
			ip.setDesconto(0.0);
			ip.setProduto(produtoService.find(ip.getProduto().getId()));
			ip.setPreco(ip.getProduto().getPreco());
			ip.setPedido(obj);
		}
		
		itemPedidoRepo.saveAll(obj.getItens());
		emailService.sendOrderConfirmationEmail(obj);
		return obj;
	}
	
	public Page<Pedido> findPage(Integer page, Integer linesPerPage, String orderBy, String direction) { 
		UserSS user = UserService.authenticated();
		if(user == null) {
			throw new AuthorizationException("Acesso negado!");
		}
		PageRequest pageRequest = PageRequest.of(page, linesPerPage, Direction.valueOf(direction), orderBy);
		Cliente cliente = clienteService.find(user.getId());
		System.out.println("**CLIENTE*** " + cliente);
		return repo.findByCliente(cliente, pageRequest);
	}
}
