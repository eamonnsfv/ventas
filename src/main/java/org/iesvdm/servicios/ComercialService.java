package org.iesvdm.servicios;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.iesvdm.dao.ComercialDAO;
import org.iesvdm.dao.PedidoDAO;
import org.iesvdm.dto.ClienteTotalPedidosDTO;
import org.iesvdm.dto.PedidoEstadisticasDTO;
import org.iesvdm.modelo.Comercial;
import org.iesvdm.modelo.PedidoDetalle;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ComercialService {

	private final ComercialDAO comercialDAO;
	private final PedidoDAO pedidoDAO;

	@Autowired
	public ComercialService(ComercialDAO comercialDAO, PedidoDAO pedidoDAO) {
		this.comercialDAO = comercialDAO;
		this.pedidoDAO = pedidoDAO;
	}

	public List<Comercial> listAll() {
		return comercialDAO.getAll();
	}

	public Comercial one(int id) {
		return comercialDAO.find(id).orElse(null);
	}

	public List<PedidoDetalle> listPedidosByComercial(int comercialId) {
		return pedidoDAO.findByComercialId(comercialId);
	}

	public List<ClienteTotalPedidosDTO> clientesOrdenadosPorTotalPedidos(List<PedidoDetalle> pedidos) {
		Map<Integer, ClienteTotalPedidosDTO> totalesPorCliente = new HashMap<>();
		for (PedidoDetalle pedido : pedidos) {
			ClienteTotalPedidosDTO cliente = totalesPorCliente.computeIfAbsent(
					pedido.getClienteId(),
					id -> new ClienteTotalPedidosDTO(id, pedido.getClienteNombre(), BigDecimal.ZERO));
			BigDecimal totalPedido = pedido.getTotal() == null ? BigDecimal.ZERO : pedido.getTotal();
			cliente.setTotalPedidos(cliente.getTotalPedidos().add(totalPedido));
		}

		List<ClienteTotalPedidosDTO> clientes = new ArrayList<>(totalesPorCliente.values());
		clientes.sort(Comparator.comparing(ClienteTotalPedidosDTO::getTotalPedidos)
				.reversed()
				.thenComparingInt(ClienteTotalPedidosDTO::getClienteId));
		return clientes;
	}

	public PedidoEstadisticasDTO estadisticasPedidos(List<PedidoDetalle> pedidos) {
		BigDecimal totalPedidos = pedidos.stream()
				.map(PedidoDetalle::getTotal)
				.filter(total -> total != null)
				.reduce(BigDecimal.ZERO, BigDecimal::add);
		BigDecimal mediaPedidos = pedidos.isEmpty()
				? BigDecimal.ZERO
				: totalPedidos.divide(BigDecimal.valueOf(pedidos.size()), 2, RoundingMode.HALF_UP);
		BigDecimal maximoPedido = pedidos.stream()
				.map(PedidoDetalle::getTotal)
				.filter(total -> total != null)
				.max(BigDecimal::compareTo)
				.orElse(null);
		BigDecimal minimoPedido = pedidos.stream()
				.map(PedidoDetalle::getTotal)
				.filter(total -> total != null)
				.min(BigDecimal::compareTo)
				.orElse(null);

		return new PedidoEstadisticasDTO(totalPedidos, mediaPedidos, maximoPedido, minimoPedido);
	}

	public void newComercial(Comercial comercial) {
		comercialDAO.create(comercial);
	}

	public void replaceComercial(Comercial comercial) {
		comercialDAO.update(comercial);
	}

	public void deleteComercial(int id) {
		comercialDAO.delete(id);
	}
}
