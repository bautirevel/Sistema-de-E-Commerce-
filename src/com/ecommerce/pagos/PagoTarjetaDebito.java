package com.ecommerce.pagos;

public class PagoTarjetaDebito implements ProcesadorPago {
    @Override
    public boolean procesarPago(double monto) {
        System.out.println("Procesando $" + monto + " con Tarjeta de Debito.");
        return true;
    }
}
