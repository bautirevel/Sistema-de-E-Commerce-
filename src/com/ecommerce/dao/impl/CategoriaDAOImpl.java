package com.ecommerce.dao.impl;
import com.ecommerce.dao.CategoriaDAO;
import com.ecommerce.utils.Conexion;
public class CategoriaDAOImpl implements CategoriaDAO {
    private Conexion conexion = new Conexion();
    @Override public void insertar() { System.out.println("Categoria conectada a MySQL."); }
    @Override public void buscarPorId() {}
    @Override public void listarTodos() {}
    @Override public void actualizar() {}
    @Override public void eliminar() {}
}