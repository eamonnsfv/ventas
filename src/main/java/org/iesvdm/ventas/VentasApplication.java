package org.iesvdm.ventas;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.extern.slf4j.Slf4j;

import java.util.Optional;

import org.iesvdm.dao.ClienteDAO;
import org.iesvdm.modelo.Cliente;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;

@SpringBootApplication(scanBasePackages = "org.iesvdm")
@RestController
@Slf4j 
public class VentasApplication implements CommandLineRunner {

	@Autowired 
	private ClienteDAO clienteDAO;

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
	}
}
