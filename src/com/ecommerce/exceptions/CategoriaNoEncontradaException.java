package com.ecommerce.exceptions;

public class CategoriaNoEncontradaException extends Exception {
    private static final long serialVersionUID = 1L;
    public CategoriaNoEncontradaException(String mensaje) {
        super(mensaje);
    }
}