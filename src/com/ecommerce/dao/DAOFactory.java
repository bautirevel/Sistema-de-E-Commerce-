package com.ecommerce.dao;
public abstract class DAOFactory {
    public abstract UsuarioDAO crearUsuarioDAO();
    public abstract CategoriaDAO crearCategoriaDAO();
    public abstract InventarioDAO crearInventarioDAO();
    public abstract OrdenDAO crearOrdenDAO();
    public abstract ReclamoDAO crearReclamoDAO();
}
