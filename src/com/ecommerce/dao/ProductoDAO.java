package com.ecommerce.dao;

import com.ecommerce.model.Producto;
import com.ecommerce.exceptions.ProductoDuplicadoException;
import java.util.List;

public interface ProductoDAO {
    void insertar(Producto producto) throws ProductoDuplicadoException;
    void listarTodos();
    List<Producto> obtenerTodos();
    void eliminar(String codigo);
    Producto buscarPorCodigo(String codigo);
}
