package org.iesvdm.servicios;

import java.util.List;

import org.iesvdm.dao.PedidoDAO;
import org.iesvdm.modelo.Pedido;
import org.iesvdm.modelo.PedidoDetalle;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PedidoService {

	@Autowired
	private PedidoDAO pedidoDAO;

	public List<Pedido> listAll() {
		return pedidoDAO.getAll();
	}

	public Pedido one(int id) {
		return pedidoDAO.find(id).orElse(null);
	}

	public List<PedidoDetalle> listAllDetalles() {
		return pedidoDAO.getAllDetalles();
	}

	public PedidoDetalle oneDetalle(int id) {
		return pedidoDAO.findDetalle(id).orElse(null);
	}

	public void newPedido(Pedido pedido) {
		pedidoDAO.create(pedido);
	}

	public void replacePedido(Pedido pedido) {
		pedidoDAO.update(pedido);
	}

	public void deletePedido(int id) {
		pedidoDAO.delete(id);
	}
}