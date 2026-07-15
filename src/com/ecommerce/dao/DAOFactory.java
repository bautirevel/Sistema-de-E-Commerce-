package com.ecommerce.dao;
import com.ecommerce.dao.impl.*;

public abstract class DAOFactory {
    public abstract UsuarioDAO crearUsuarioDAO();
    public abstract ProductoDAO crearProductoDAO();
    public abstract CategoriaDAO crearCategoriaDAO();
    public abstract InventarioDAO crearInventarioDAO();
    public abstract OrdenDAO crearOrdenDAO();
    public abstract EnvioDAO crearEnvioDAO();
    public abstract ReclamoDAO crearReclamoDAO();
    public abstract PagoDAO crearPagoDAO();

    public static DAOFactory getDAOFactory() {
        return new com.ecommerce.dao.impl.MySQLDAOFactory();
    }
}