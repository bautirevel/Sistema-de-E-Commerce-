package com.ecommerce.pagos;

public class PagoTarjetaCredito implements ProcesadorPago {
    @Override
    public boolean procesarPago(double monto) {
        System.out.println("Procesando $" + monto + " con Tarjeta de Credito.");
        return true;
    }
}
