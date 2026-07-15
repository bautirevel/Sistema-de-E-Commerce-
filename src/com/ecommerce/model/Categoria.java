package com.ecommerce.model;

public class Categoria {
    private int id;
    private String nombre;
    private String descripcion;
    private String estado;

    public Categoria(int id, String nombre, String descripcion, String estado) {
        this.id = id;
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.estado = estado;
    }

    public Categoria(String nombre, String descripcion) {
        this(0, nombre, descripcion, "ACTIVO");
    }

    public int getId() { return id; }
    public String getNombre() { return nombre; }
    public String getDescripcion() { return descripcion; }
    public String getEstado() { return estado; }

    @Override
    public String toString() {
        return id + ". " + nombre + " - " + descripcion + " [" + estado + "]";
    }
}
