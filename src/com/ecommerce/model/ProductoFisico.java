package com.ecommerce.model;
public class ProductoFisico extends Producto {
    public ProductoFisico(String codigo, String nombre, double precio, int stock) {
        super(codigo, nombre, "FÃƒÆ’Ã‚Â­sico", precio, "General", stock, 1.0, EstadoProducto.ACTIVO);
    }
    @Override public double calcularPrecioFinal() { return precio * 1.21; }
    @Override public String mostrarInformacion() { return "FÃƒÆ’Ã‚Â­sico: " + nombre + " - $" + calcularPrecioFinal(); }
    @Override public boolean validarDisponibilidad() { return stock > 0; }
    @Override public void aplicarDescuento(double porcentaje) { this.precio -= this.precio * (porcentaje/100); }
}