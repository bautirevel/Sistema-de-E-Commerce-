package com.ecommerce.dao;

import com.ecommerce.model.Reclamo;
import com.ecommerce.model.EstadoReclamo;
import java.util.List;

public interface ReclamoDAO {
    Reclamo guardar(Reclamo reclamo);
    List<Reclamo> obtenerTodos();
    void cambiarEstado(int numero, EstadoReclamo nuevoEstado);
    int contarPorEstado(EstadoReclamo estado);
}
