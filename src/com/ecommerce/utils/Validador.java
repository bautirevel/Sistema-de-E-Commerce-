package com.ecommerce.utils;

import com.ecommerce.dao.ProductoDAO;
import com.ecommerce.exceptions.*;

public class Validador {
    public static void validarCadena(String valor, String campo) throws DatosInvalidosException {
        if (valor == null || valor.trim().isEmpty()) throw new DatosInvalidosException("El campo " + campo + " no puede estar vacío.");
    }

    public static void validarPrecio(double precio) throws DatosInvalidosException {
        if (precio <= 0) throw new DatosInvalidosException("El precio debe ser mayor a cero.");
    }

    public static void validarStock(int stock) throws DatosInvalidosException {
        if (stock < 0) throw new DatosInvalidosException("El stock no puede ser negativo.");
    }

    public static void validarProductoDuplicado(String codigo, ProductoDAO dao) throws ProductoDuplicadoException {
        if (dao.buscarPorCodigo(codigo) != null) throw new ProductoDuplicadoException("El código " + codigo + " ya existe.");
    }

    public static void validarStockDisponible(int solicitado, int disponible) throws StockInsuficienteException {
        if (solicitado > disponible || disponible <= 0) throw new StockInsuficienteException("Stock insuficiente.");
    }

    public static void validarPermiso(boolean tienePermiso) throws PermisoDenegadoException {
        if (!tienePermiso) throw new PermisoDenegadoException("Acceso denegado.");
    }
}