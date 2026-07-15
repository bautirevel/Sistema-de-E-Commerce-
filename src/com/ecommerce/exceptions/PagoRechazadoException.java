package com.ecommerce.exceptions;

public class PagoRechazadoException extends Exception {
    private static final long serialVersionUID = 1L;
    public PagoRechazadoException(String mensaje) {
        super(mensaje);
    }
}