package com.ecommerce.pagos;
public class PagoContraEntrega implements ProcesadorPago {
    @Override public boolean procesarPago(double monto) {
        System.out.println("Monto de $" + monto + " registrado para pago contra entrega en el destino.");
        return true;
    }
}