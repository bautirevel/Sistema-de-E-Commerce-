package com.ecommerce.model;
public class ProductoDigital extends Producto {
    public ProductoDigital(String codigo, String nombre, double precio, int stock) {
        super(codigo, nombre, "Digital", precio, "Digital", stock, 0.0, EstadoProducto.ACTIVO);
    }

    public ProductoDigital(String codigo, String nombre, String descripcion, double precio, String categoria, int stock, EstadoProducto estado) {
        super(codigo, nombre, descripcion, precio, categoria, stock, 0.0, estado);
    }

    @Override public double calcularPrecioFinal() { return precio; }
    @Override public String mostrarInformacion() { return "Digital: " + nombre + " - $" + calcularPrecioFinal(); }
    @Override public boolean validarDisponibilidad() { return estado == EstadoProducto.ACTIVO; }
    @Override public void aplicarDescuento(double porcentaje) { this.precio -= this.precio * (porcentaje/100); }
}
