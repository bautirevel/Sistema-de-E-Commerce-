package com.ecommerce.exceptions;

public class OrdenNoEncontradaException extends Exception {
    public OrdenNoEncontradaException(String mensaje) {
        super(mensaje);
    }
}