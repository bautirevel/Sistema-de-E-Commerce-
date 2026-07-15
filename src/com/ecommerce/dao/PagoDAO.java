package com.ecommerce.dao;

import com.ecommerce.model.Pago;
import java.util.List;
import java.util.Map;

public interface PagoDAO {
    Pago guardar(Pago pago);
    List<Pago> obtenerTodos();
    double recaudacionTotal();
    Map<String, Double> recaudacionPorMetodo();
}
