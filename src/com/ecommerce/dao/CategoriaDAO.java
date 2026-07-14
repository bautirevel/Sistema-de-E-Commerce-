package com.ecommerce.dao;
public interface CategoriaDAO {
    void insertar(String nombre, String descripcion);
    void listarTodos();
    void actualizar(int id, String nombre, String descripcion);
    void eliminar(int id);
    void asociarProducto(String codigoProducto, String nombreCategoria);
}