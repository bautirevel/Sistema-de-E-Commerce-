package com.ecommerce.pagos;
public class PagoTarjetaCredito implements ProcesadorPago {
    private static final double LIMITE = 5_000_000;

    @Override public boolean procesarPago(double monto) {
        if (monto > LIMITE) {
            System.out.println("Procesando pago de $" + monto + " con Tarjeta de Credito... [RECHAZADO: excede el limite de la tarjeta]");
            return false;
        }
        System.out.println("Procesando pago de $" + monto + " con Tarjeta de Credito... ¡Aprobado!");
        return true;
    }
}