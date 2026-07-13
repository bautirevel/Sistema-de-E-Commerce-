package com.ecommerce.pagos;
public class PagoTransferencia implements ProcesadorPago {
    @Override public void procesarPago(double monto) {
        System.out.println("Procesando pago de $" + monto + " con Transferencia Bancaria... ¡Aprobado!");
    }
}