package org.iesvdm.controlador;

import java.util.List;

import org.iesvdm.modelo.Cliente;
import org.iesvdm.modelo.Comercial;
import org.iesvdm.modelo.Pedido;
import org.iesvdm.servicios.ClienteService;
import org.iesvdm.servicios.ComercialService;
import org.iesvdm.servicios.PedidoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.view.RedirectView;

@RequestMapping("/pedidos")
@Controller
public class PedidoController {

	@Autowired
	private PedidoService pedidoService;

	@Autowired
	private ClienteService clienteService;

	@Autowired
	private ComercialService comercialService;

	@GetMapping
	public String listar(Model model) {
		model.addAttribute("pedidos", pedidoService.listAllDetalles());
		return "pedidos";
	}

	@GetMapping("/{id}")
	public String detalle(Model model, @PathVariable int id) {
		model.addAttribute("pedido", pedidoService.oneDetalle(id));
		return "detalle-pedido";
	}

	@GetMapping("/crear")
	public String crear(Model model) {
		model.addAttribute("pedido", new Pedido());
		cargarRelaciones(model);
		return "crear-pedido";
	}

	@PostMapping("/crear")
	public String submitCrear(@ModelAttribute("pedido") Pedido pedido, Model model) {
		if (!relacionesValidas(pedido)) {
			cargarRelaciones(model);
			model.addAttribute("error", "El cliente y el comercial seleccionados no existen.");
			return "crear-pedido";
		}
		pedidoService.newPedido(pedido);
		return "redirect:/pedidos";
	}

	@GetMapping("/editar/{id}")
	public String editar(Model model, @PathVariable int id) {
		model.addAttribute("pedido", pedidoService.one(id));
		cargarRelaciones(model);
		return "editar-pedido";
	}

	@PostMapping("/editar/{id}")
	public String submitEditar(@ModelAttribute("pedido") Pedido pedido, Model model) {
		if (!relacionesValidas(pedido)) {
			cargarRelaciones(model);
			model.addAttribute("error", "El cliente y el comercial seleccionados no existen.");
			return "editar-pedido";
		}
		pedidoService.replacePedido(pedido);
		return "redirect:/pedidos";
	}

	@PostMapping("/borrar/{id}")
	public RedirectView submitBorrar(@PathVariable int id) {
		pedidoService.deletePedido(id);
		return new RedirectView("/pedidos");
	}

	private void cargarRelaciones(Model model) {
		List<Cliente> clientes = clienteService.listAll();
		List<Comercial> comerciales = comercialService.listAll();
		model.addAttribute("clientes", clientes);
		model.addAttribute("comerciales", comerciales);
	}

	private boolean relacionesValidas(Pedido pedido) {
		return clienteService.one(pedido.getClienteId()) != null
				&& comercialService.one(pedido.getComercialId()) != null;
	}
}