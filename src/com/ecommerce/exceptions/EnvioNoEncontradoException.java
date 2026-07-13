package com.ecommerce.exceptions;

public class EnvioNoEncontradoException extends Exception {
    public EnvioNoEncontradoException(String mensaje) {
        super(mensaje);
    }
}