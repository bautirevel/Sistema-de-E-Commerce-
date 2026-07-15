package com.ecommerce.exceptions;

public class PermisoDenegadoException extends Exception {
    private static final long serialVersionUID = 1L;
    public PermisoDenegadoException(String mensaje) {
        super(mensaje);
    }
}