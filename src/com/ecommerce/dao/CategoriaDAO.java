package com.ecommerce.dao;
public interface CategoriaDAO {
    void insertar(String nombre, String descripcion);
    void listarTodos();
}