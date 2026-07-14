package com.ecommerce.pagos;
public class PagoTarjetaCredito implements ProcesadorPago {
    @Override public boolean procesarPago(double monto) {
        System.out.println("Procesando pago de $" + monto + " con Tarjeta de Credito... ¡Aprobado!");
        return true;
    }
}