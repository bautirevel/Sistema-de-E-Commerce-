package com.ecommerce.dao.impl;

import com.ecommerce.dao.ProductoDAO;
import com.ecommerce.model.Producto;
import com.ecommerce.utils.Conexion;
import java.util.ArrayList;
import java.util.List;

public class ProductoDAOImpl implements ProductoDAO {
    private Conexion conexion;

    public ProductoDAOImpl() {
        this.conexion = new Conexion();
    }

    @Override
    public void insertar(Producto producto) {
    }

    @Override
    public Producto buscarPorCodigo(String codigo) {
        return null;
    }

    @Override
    public List<Producto> listarTodos() {
        return new ArrayList<>();
    }

    @Override
    public void actualizar(Producto producto) {
    }

    @Override
    public void eliminar(String codigo) {
    }
}
