package com.ecommerce.model;
public class ProductoDigital extends Producto {
    public ProductoDigital(String codigo, String nombre, double precio, int stock) {
        super(codigo, nombre, "Digital", precio, "Digital", stock, 0.0, EstadoProducto.ACTIVO);
    }
    @Override public double calcularPrecioFinal() { return precio; }
    @Override public String mostrarInformacion() { return "Digital: " + nombre + " - $" + calcularPrecioFinal(); }
    @Override public boolean validarDisponibilidad() { return true; }
    @Override public void aplicarDescuento(double porcentaje) { this.precio -= this.precio * (porcentaje/100); }
}