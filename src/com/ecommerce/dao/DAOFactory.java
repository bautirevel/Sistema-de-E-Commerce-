package com.ecommerce.dao;

import com.ecommerce.dao.impl.ProductoDAOImpl;
import com.ecommerce.dao.impl.UsuarioDAOImpl;

public class DAOFactory {

    //método estático para obtener la impl de productdao
    public static ProductoDAO getProductoDAO() {
        return new ProductoDAOImpl();
    }

    // método estático para obtener la impl de usuariodao
    public static UsuarioDAO getUsuarioDAO() {
        return new UsuarioDAOImpl();
    }
}