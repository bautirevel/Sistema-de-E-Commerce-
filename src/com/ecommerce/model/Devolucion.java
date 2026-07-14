package com.ecommerce.model;
import java.util.Date;
public class Devolucion {
    private int id; private Usuario cliente; private Producto producto;
    private String motivo; private Date fecha; private String estado;
    public Devolucion(int id, Usuario cliente, Producto producto, String motivo) {
        this.id = id; this.cliente = cliente; this.producto = producto;
        this.motivo = motivo; this.fecha = new Date(); this.estado = "PENDIENTE";
    }
}