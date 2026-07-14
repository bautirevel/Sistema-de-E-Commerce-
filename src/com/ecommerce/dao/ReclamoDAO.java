package com.ecommerce.dao;
public interface ReclamoDAO {
    void abrirReclamo(String clienteEmail, Integer idOrden, String motivo);
    void listarTodos();
    void cambiarEstado(int id, String nuevoEstado);
}