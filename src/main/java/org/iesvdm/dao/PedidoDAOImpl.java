package org.iesvdm.dao;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.Types;
import java.util.List;
import java.util.Optional;

import org.iesvdm.modelo.Pedido;
import org.iesvdm.modelo.PedidoDetalle;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Repository
public class PedidoDAOImpl implements PedidoDAO {

	private final JdbcTemplate jdbcTemplate;

	public PedidoDAOImpl(JdbcTemplate jdbcTemplate) {
		this.jdbcTemplate = jdbcTemplate;
	}

	@Override
	public synchronized void create(Pedido pedido) {
		String sqlInsert = """
				INSERT INTO pedido (total, fecha, id_cliente, id_comercial)
				VALUES (?, ?, ?, ?)
				""";

		KeyHolder keyHolder = new GeneratedKeyHolder();
		int rows = jdbcTemplate.update(connection -> {
			PreparedStatement ps = connection.prepareStatement(sqlInsert, new String[] { "id" });
			int index = 1;
			ps.setBigDecimal(index++, pedido.getTotal());
			setFecha(ps, index++, pedido);
			ps.setInt(index++, pedido.getClienteId());
			ps.setInt(index, pedido.getComercialId());
			return ps;
		}, keyHolder);

		pedido.setId(keyHolder.getKey().intValue());
		log.info("Insertados {} registros.", rows);
	}

	@Override
	public List<Pedido> getAll() {
		List<Pedido> pedidos = jdbcTemplate.query(
				"SELECT id, total, fecha, id_cliente, id_comercial FROM pedido",
				(rs, rowNum) -> mapPedido(rs));

		log.info("Devueltos {} registros.", pedidos.size());
		return pedidos;
	}

	@Override
	public Optional<Pedido> find(int id) {
		List<Pedido> pedidos = jdbcTemplate.query(
				"SELECT id, total, fecha, id_cliente, id_comercial FROM pedido WHERE id = ?",
				(rs, rowNum) -> mapPedido(rs), id);

		if (pedidos.isEmpty()) {
			log.info("Pedido no encontrado.");
			return Optional.empty();
		}
		return Optional.of(pedidos.get(0));
	}

	@Override
	public List<PedidoDetalle> getAllDetalles() {
		List<PedidoDetalle> pedidos = jdbcTemplate.query("""
				SELECT p.id, p.total, p.fecha,
				       CONCAT_WS(' ', c.nombre, c.apellido1, c.apellido2) AS cliente_nombre,
				       CONCAT_WS(' ', co.nombre, co.apellido1, co.apellido2) AS comercial_nombre
				FROM pedido p
				JOIN cliente c ON c.id = p.id_cliente
				JOIN comercial co ON co.id = p.id_comercial
				""", (rs, rowNum) -> mapPedidoDetalle(rs));

		log.info("Devueltos {} detalles de pedidos.", pedidos.size());
		return pedidos;
	}

	@Override
	public Optional<PedidoDetalle> findDetalle(int id) {
		List<PedidoDetalle> pedidos = jdbcTemplate.query("""
				SELECT p.id, p.total, p.fecha,
				       CONCAT_WS(' ', c.nombre, c.apellido1, c.apellido2) AS cliente_nombre,
				       CONCAT_WS(' ', co.nombre, co.apellido1, co.apellido2) AS comercial_nombre
				FROM pedido p
				JOIN cliente c ON c.id = p.id_cliente
				JOIN comercial co ON co.id = p.id_comercial
				WHERE p.id = ?
				""", (rs, rowNum) -> mapPedidoDetalle(rs), id);

		if (pedidos.isEmpty()) {
			log.info("Detalle de pedido no encontrado.");
			return Optional.empty();
		}
		return Optional.of(pedidos.get(0));
	}

	@Override
	public void update(Pedido pedido) {
		int rows = jdbcTemplate.update(connection -> {
			PreparedStatement ps = connection.prepareStatement("""
					UPDATE pedido SET
						total = ?,
						fecha = ?,
						id_cliente = ?,
						id_comercial = ?
					WHERE id = ?
					""");
			int index = 1;
			ps.setBigDecimal(index++, pedido.getTotal());
			setFecha(ps, index++, pedido);
			ps.setInt(index++, pedido.getClienteId());
			ps.setInt(index++, pedido.getComercialId());
			ps.setInt(index, pedido.getId());
			return ps;
		});

		log.info("Update de Pedido con {} registros actualizados.", rows);
	}

	@Override
	public void delete(int id) {
		int rows = jdbcTemplate.update("DELETE FROM pedido WHERE id = ?", id);
		log.info("Delete de Pedido con {} registros eliminados.", rows);
	}

	private Pedido mapPedido(java.sql.ResultSet resultSet) throws java.sql.SQLException {
		Date fecha = resultSet.getDate("fecha");
		return new Pedido(
				resultSet.getInt("id"),
				resultSet.getBigDecimal("total"),
				fecha == null ? null : fecha.toLocalDate(),
				resultSet.getInt("id_cliente"),
				resultSet.getInt("id_comercial"));
	}

	private PedidoDetalle mapPedidoDetalle(java.sql.ResultSet resultSet) throws java.sql.SQLException {
		Date fecha = resultSet.getDate("fecha");
		return new PedidoDetalle(
				resultSet.getInt("id"),
				resultSet.getBigDecimal("total"),
				fecha == null ? null : fecha.toLocalDate(),
				resultSet.getString("cliente_nombre"),
				resultSet.getString("comercial_nombre"));
	}

	private void setFecha(PreparedStatement preparedStatement, int index, Pedido pedido)
			throws java.sql.SQLException {
		if (pedido.getFecha() == null) {
			preparedStatement.setNull(index, Types.DATE);
		} else {
			preparedStatement.setDate(index, Date.valueOf(pedido.getFecha()));
		}
	}
}