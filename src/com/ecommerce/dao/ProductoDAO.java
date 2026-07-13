package com.ecommerce.dao;
import com.ecommerce.model.Producto;
public interface ProductoDAO {
    void insertar(Producto producto);
    void listarTodos();
    void eliminar(String codigo);
}