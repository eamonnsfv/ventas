package org.iesvdm.dto;

import java.math.BigDecimal;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ClienteTotalPedidosDTO {
	private int clienteId;
	private String clienteNombre;
	private BigDecimal totalPedidos;
}
