package com.ecommerce.model;

public abstract class Producto {
    protected String codigoUnico;
    protected String nombre;
    protected String descripcion;
    protected double precioBase;
    protected String categoria;
    protected int stock;
    protected double peso;
    protected EstadoProducto estado;

    public Producto(String codigoUnico, String nombre, String descripcion, double precioBase, String categoria, int stock, double peso, EstadoProducto estado) {
        this.codigoUnico = codigoUnico;
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.precioBase = precioBase;
        this.categoria = categoria;
        this.stock = stock;
        this.peso = peso;
        this.estado = estado;
    }

    public abstract double calcularPrecioFinal();
    public abstract void mostrarInformacion();
    public abstract boolean validarDisponibilidad();
    public abstract void aplicarDescuento(double porcentaje);

    public String getCodigoUnico() { return codigoUnico; }
    public void setCodigoUnico(String codigoUnico) { this.codigoUnico = codigoUnico; }
    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }
    public String getDescripcion() { return descripcion; }
    public void setDescripcion(String descripcion) { this.descripcion = descripcion; }
    public double getPrecioBase() { return precioBase; }
    public void setPrecioBase(double precioBase) { this.precioBase = precioBase; }
    public String getCategoria() { return categoria; }
    public void setCategoria(String categoria) { this.categoria = categoria; }
    public int getStock() { return stock; }
    public void setStock(int stock) { this.stock = stock; }
    public double getPeso() { return peso; }
    public void setPeso(double peso) { this.peso = peso; }
    public EstadoProducto getEstado() { return estado; }
    public void setEstado(EstadoProducto estado) { this.estado = estado; }
}
