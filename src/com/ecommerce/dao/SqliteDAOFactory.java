package com.ecommerce.dao;

import com.ecommerce.dao.impl.*;

public class SqliteDAOFactory extends DAOFactory {
    @Override public UsuarioDAO crearUsuarioDAO() { return new UsuarioDAOImpl(); }
    @Override public ProductoDAO crearProductoDAO() { return new ProductoDAOImpl(); }
    @Override public CategoriaDAO crearCategoriaDAO() { return new CategoriaDAOImpl(); }
    @Override public InventarioDAO crearInventarioDAO() { return new InventarioDAOImpl(); }
    @Override public OrdenDAO crearOrdenDAO() { return new OrdenDAOImpl(); }
    @Override public ReclamoDAO crearReclamoDAO() { return new ReclamoDAOImpl(); }
}
