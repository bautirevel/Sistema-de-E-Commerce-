package com.ecommerce.pagos;
public class PagoTarjetaDebito implements ProcesadorPago {
    private static final double LIMITE = 2_000_000;

    @Override public boolean procesarPago(double monto) {
        if (monto > LIMITE) {
            System.out.println("Procesando pago de $" + monto + " con Tarjeta de Debito... [RECHAZADO: excede el limite diario]");
            return false;
        }
        System.out.println("Procesando pago de $" + monto + " con Tarjeta de Debito... ¡Aprobado!");
        return true;
    }
}
