package com.ecommerce.pagos;
public class PagoBilleteraVirtual implements ProcesadorPago {
    @Override public boolean procesarPago(double monto) {
        System.out.println("Procesando pago de $" + monto + " con Billetera Virtual... ¡Aprobado!");
        return true;
    }
}