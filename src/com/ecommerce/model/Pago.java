package com.ecommerce.model;

import java.util.Date;

public class Pago {
    private int id;
    private int idOrden;
    private String metodo;
    private double monto;
    private EstadoPago estado;
    private Date fecha;

    public Pago(int id, int idOrden, String metodo, double monto, EstadoPago estado, Date fecha) {
        this.id = id;
        this.idOrden = idOrden;
        this.metodo = metodo;
        this.monto = monto;
        this.estado = estado;
        this.fecha = fecha;
    }

    public int getId() { return id; }
    public int getIdOrden() { return idOrden; }
    public String getMetodo() { return metodo; }
    public double getMonto() { return monto; }
    public EstadoPago getEstado() { return estado; }
    public Date getFecha() { return fecha; }

    @Override
    public String toString() {
        return "Pago #" + id + " | Orden #" + idOrden + " | Metodo: " + metodo
                + " | Monto: $" + monto + " | Estado: " + estado;
    }
}
