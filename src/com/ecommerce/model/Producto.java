package com.ecommerce.model;

public abstract class Producto {
    protected String codigo; protected String nombre; protected String descripcion;
    protected double precio; protected String categoria; protected int stock;
    protected double peso; protected EstadoProducto estado;

    public Producto(String codigo, String nombre, String descripcion, double precio, String categoria, int stock, double peso, EstadoProducto estado) {
        this.codigo = codigo; this.nombre = nombre; this.descripcion = descripcion;
        this.precio = precio; this.categoria = categoria; this.stock = stock;
        this.peso = peso; this.estado = estado;
    }

    public abstract double calcularPrecioFinal();
    public abstract String mostrarInformacion();
    public abstract boolean validarDisponibilidad();
    public abstract void aplicarDescuento(double porcentaje);

    public String getNombre() { return nombre; }
    public double getPrecio() { return precio; }
    public double getPrecioBase() { return precio; }
    public String getCodigo() { return codigo; }
    public String getCodigoUnico() { return codigo; } // Alias para el DAO de Theo
    public String getDescripcion() { return descripcion; }
    public String getCategoria() { return categoria; }
    public int getStock() { return stock; }
    public double getPeso() { return peso; }
    public EstadoProducto getEstado() { return estado; }
}