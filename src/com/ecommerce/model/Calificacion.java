package com.ecommerce.model;
public class Calificacion {
    private int id; private Usuario cliente; private Producto producto;
    private int estrellas; private String comentario;
    public Calificacion(int id, Usuario cliente, Producto producto, int estrellas, String comentario) {
        this.id = id; this.cliente = cliente; this.producto = producto;
        this.estrellas = estrellas; this.comentario = comentario;
    }
}