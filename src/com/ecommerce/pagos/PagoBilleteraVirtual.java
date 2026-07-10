package com.ecommerce.pagos;

public class PagoBilleteraVirtual implements ProcesadorPago {
    @Override
    public boolean procesarPago(double monto) {
        System.out.println("Procesando $" + monto + " con Billetera Virtual.");
        return true;
    }
}
