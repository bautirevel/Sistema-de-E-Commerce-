package com.ecommerce.exceptions;

public class OrdenNoEncontradaException extends Exception {
    private static final long serialVersionUID = 1L;
    public OrdenNoEncontradaException(String mensaje) {
        super(mensaje);
    }
}