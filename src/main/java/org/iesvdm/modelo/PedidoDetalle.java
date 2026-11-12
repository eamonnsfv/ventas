package org.iesvdm.modelo;

import java.math.BigDecimal;
import java.time.LocalDate;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class PedidoDetalle {
	private int id;
	private BigDecimal total;
	private LocalDate fecha;
	private int clienteId;
	private int comercialId;
	private String clienteNombre;
	private String comercialNombre;

	public PedidoDetalle(int id, BigDecimal total, LocalDate fecha, String clienteNombre, String comercialNombre) {
		this(id, total, fecha, 0, 0, clienteNombre, comercialNombre);
	}

	public PedidoDetalle(int id, BigDecimal total, LocalDate fecha, int clienteId, int comercialId,
			String clienteNombre, String comercialNombre) {
		this.id = id;
		this.total = total;
		this.fecha = fecha;
		this.clienteId = clienteId;
		this.comercialId = comercialId;
		this.clienteNombre = clienteNombre;
		this.comercialNombre = comercialNombre;
	}
}