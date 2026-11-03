package org.iesvdm.controlador;

import java.util.List;

import org.iesvdm.modelo.Comercial;
import org.iesvdm.servicios.ComercialService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.view.RedirectView;

@Controller
public class ComercialController {

	@Autowired
	private ComercialService comercialService;

	@GetMapping("/comerciales")
	public String listar(Model model) {
		List<Comercial> comerciales = comercialService.listAll();
		model.addAttribute("comerciales", comerciales);
		return "comerciales";
	}

	@GetMapping("/comerciales/{id}")
	public String detalle(Model model, @PathVariable int id) {
		model.addAttribute("comercial", comercialService.one(id));
		return "detalle-comercial";
	}

	@GetMapping("/comerciales/crear")
	public String crear(Model model) {
		model.addAttribute("comercial", new Comercial());
		return "crear-comercial";
	}

	@PostMapping("/comerciales/crear")
	public RedirectView submitCrear(@ModelAttribute("comercial") Comercial comercial) {
		comercialService.newComercial(comercial);
		return new RedirectView("/comerciales");
	}

	@GetMapping("/comerciales/editar/{id}")
	public String editar(Model model, @PathVariable int id) {
		model.addAttribute("comercial", comercialService.one(id));
		return "editar-comercial";
	}

	@PostMapping("/comerciales/editar/{id}")
	public RedirectView submitEditar(@ModelAttribute("comercial") Comercial comercial) {
		comercialService.replaceComercial(comercial);
		return new RedirectView("/comerciales");
	}

	@PostMapping("/comerciales/borrar/{id}")
	public RedirectView submitBorrar(@PathVariable int id) {
		comercialService.deleteComercial(id);
		return new RedirectView("/comerciales");
	}
}
