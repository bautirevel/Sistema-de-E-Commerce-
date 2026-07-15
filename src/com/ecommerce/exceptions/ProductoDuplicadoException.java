package com.ecommerce.exceptions;

public class ProductoDuplicadoException extends Exception {
    private static final long serialVersionUID = 1L;
    public ProductoDuplicadoException(String mensaje) {
        super(mensaje);
    }
}