package com.ecommerce.dao;

import com.ecommerce.model.Producto;
import java.util.List;

public interface ProductoDAO {
    void insertar(Producto producto);
    Producto buscarPorCodigo(String codigo);
    List<Producto> listarTodos();
    void actualizar(Producto producto);
    void eliminar(String codigo);
}
