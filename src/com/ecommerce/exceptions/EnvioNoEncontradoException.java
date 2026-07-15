package com.ecommerce.exceptions;

public class EnvioNoEncontradoException extends Exception {
    private static final long serialVersionUID = 1L;
    public EnvioNoEncontradoException(String mensaje) {
        super(mensaje);
    }
}