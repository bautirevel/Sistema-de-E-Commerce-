package com.ecommerce.dao;

import com.ecommerce.model.Categoria;
import com.ecommerce.exceptions.CategoriaNoEncontradaException;
import com.ecommerce.exceptions.DatosInvalidosException;
import java.util.List;

public interface CategoriaDAO {
    void guardar(Categoria categoria) throws DatosInvalidosException;
    Categoria buscarPorId(int id) throws CategoriaNoEncontradaException;
    List<Categoria> obtenerTodas();
    void actualizar(int id, String nombre, String descripcion) throws CategoriaNoEncontradaException;
    void eliminar(int id) throws CategoriaNoEncontradaException;
    void asociarProducto(String codigoProducto, String nombreCategoria);
}
