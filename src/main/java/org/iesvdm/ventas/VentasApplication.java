package org.iesvdm.ventas;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.extern.slf4j.Slf4j;

import java.util.Optional;

import org.iesvdm.dao.ClienteDAO;
import org.iesvdm.dao.ComercialDAO;
import org.iesvdm.modelo.Cliente;
import org.iesvdm.modelo.Comercial;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;

@SpringBootApplication(scanBasePackages = "org.iesvdm")
@RestController
@Slf4j 
public class VentasApplication implements CommandLineRunner {

	@Autowired 
	private ClienteDAO clienteDAO;

	@Autowired
	private ComercialDAO comercialDAO;

	public static void main(String[] args) {
		SpringApplication.run(VentasApplication.class, args);
	}

	@GetMapping("/")
	public String home() {
		return "Welcome to the Ventas Application!";
	}

	@Override 
	public void run (String... args) throws Exception {
		// Aquí puedes agregar código que se ejecute al iniciar la aplicación
		log.info("*********************************");
		log.info("* Prueba de arranque ClienteDAO *");
		log.info("*********************************");

		clienteDAO.getAll().forEach(cliente -> log.info("Cliente: {}", cliente));
		int id = 1; // Cambia este valor según el ID que quieras buscar
		Optional<Cliente> cliente = clienteDAO.find(id);

		if (cliente.isPresent()) {
			String nombreOld = cliente.get().getNombre();
			cliente.get().setNombre("Jose M");
			clienteDAO.update(cliente.get());
			log.info("Cliente con id {} actualizado de {} a {}", id, cliente.get());

			clienteDAO.getAll().forEach(c -> log.info("Cliente: {}", c));

			cliente.get().setNombre(nombreOld);
			clienteDAO.update(cliente.get());
			log.info("Cliente con id {} restaurado a {}", id, cliente.get());
		} else {	
			log.info("No encontradó cliente con id {}", id);
		}

		log.info("***********************************");
		log.info("* Prueba de arranque ComercialDAO *");
		log.info("***********************************");

		comercialDAO.getAll().forEach(comercial -> log.info("Comercial: {}", comercial));
		int comercialId = 1;
		Optional<Comercial> comercial = comercialDAO.find(comercialId);

		if (comercial.isPresent()) {
			String nombreOld = comercial.get().getNombre();
			comercial.get().setNombre("Jose M");
			comercialDAO.update(comercial.get());
			log.info("Comercial con id {} actualizado a {}", comercialId, comercial.get());

			comercial.get().setNombre(nombreOld);
			comercialDAO.update(comercial.get());
			log.info("Comercial con id {} restaurado a {}", comercialId, comercial.get());
		} else {
			log.info("No encontrado comercial con id {}", comercialId);
		}
	}
}
