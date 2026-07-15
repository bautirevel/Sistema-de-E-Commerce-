package com.ecommerce.dao;

import com.ecommerce.model.Orden;
import com.ecommerce.model.EstadoOrden;
import com.ecommerce.exceptions.OrdenNoEncontradaException;
import java.util.List;
import java.util.Map;

public interface OrdenDAO {
    Orden guardar(Orden orden);
    Orden buscarPorId(int id) throws OrdenNoEncontradaException;
    List<Orden> obtenerTodas();
    List<Orden> obtenerPorCliente(String email);
    void actualizarEstado(int id, EstadoOrden nuevoEstado) throws OrdenNoEncontradaException;
    void asociarPago(int idOrden, int idPago) throws OrdenNoEncontradaException;
    void asociarEnvio(int idOrden, int idEnvio) throws OrdenNoEncontradaException;
    Map<String, Integer> contarPorEstado();
    Map<String, Integer> clientesConMasCompras(int limite);
}
