package com.ecommerce.pagos;
public class PagoTarjetaDebito implements ProcesadorPago {
    @Override public void procesarPago(double monto) {
        System.out.println("Procesando pago de $" + monto + " con Tarjeta de Debito... ¡Aprobado!");
    }
}