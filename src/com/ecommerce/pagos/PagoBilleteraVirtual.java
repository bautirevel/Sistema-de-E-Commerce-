package com.ecommerce.pagos;
public class PagoBilleteraVirtual implements ProcesadorPago {
    @Override public void procesarPago(double monto) {
        System.out.println("Procesando pago de $" + monto + " con Billetera Virtual... ¡Aprobado!");
    }
}