package com.ecommerce.utils;

import com.ecommerce.exceptions.PermisoDenegadoException;

public class Validador {
    public static void validarPermiso(boolean condicion) throws PermisoDenegadoException {
        if (!condicion) {
            throw new PermisoDenegadoException("Acceso bloqueado.");
        }
    }
}