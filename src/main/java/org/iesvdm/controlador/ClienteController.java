package org.iesvdm.controlador;

import java.util.List;

import org.iesvdm.modelo.Cliente;
import org.iesvdm.servicios.ClienteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.view.RedirectView;

@RequestMapping("/clientes")
@Controller
public class ClienteController {

    @Autowired 
    private ClienteService clienteService;
    
    @GetMapping
    public String listar(Model model) {
        List<Cliente> clientes = clienteService.listAll();
        model.addAttribute("clientes", clientes);
        return "clientes";
    }

    @GetMapping("/{id}")
    public String detalle(Model model, @PathVariable int id) {
        model.addAttribute("cliente", clienteService.one(id));
        return "detalle-cliente";
    }

    @GetMapping("/crear")
    public String crear(Model model) {
        model.addAttribute("cliente", new Cliente());
        return "crear-cliente";
    }

    @PostMapping("/crear")
    public RedirectView submitCrear(@ModelAttribute("cliente") Cliente cliente) {
        clienteService.newCliente(cliente);
        return new RedirectView("/clientes");
    }

    @GetMapping("/editar/{id}")
    public String editar(Model model, @PathVariable int id) {
        model.addAttribute("cliente", clienteService.one(id));
        return "editar-cliente";
    }

    @PostMapping("/editar/{id}")
    public RedirectView submitEditar(@ModelAttribute("cliente") Cliente cliente) {
        clienteService.replaceCliente(cliente);
        return new RedirectView("/clientes");
    }

    @PostMapping("/borrar/{id}")
    public RedirectView submitBorrar(@PathVariable int id, RedirectAttributes redirectAttributes) {
        try {
            clienteService.deleteCliente(id);
        } catch (DataIntegrityViolationException exception) {
            redirectAttributes.addFlashAttribute("error",
                    "No se puede borrar el cliente porque tiene pedidos asociados.");
        }
        return new RedirectView("/clientes");
    }
}
