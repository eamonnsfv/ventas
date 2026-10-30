package org.iesvdm.dao;

import java.util.List;
import java.util.Optional;

import org.iesvdm.modelo.Comercial;
import org.springframework.stereotype.Repository;

@Repository
public interface ComercialDAO {
	void create(Comercial comercial);
	List<Comercial> getAll();
	Optional<Comercial> find(int id);
	void update(Comercial comercial);
	void delete(int id);
}