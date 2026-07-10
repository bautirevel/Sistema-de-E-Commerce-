package com.ecommerce.pagos;

public class PagoTransferencia implements ProcesadorPago {
    @Override
    public boolean procesarPago(double monto) {
        System.out.println("Procesando $" + monto + " con Transferencia Bancaria.");
        return true;
    }
}
