package com.ecommerce.model;
public class ProductoImportado extends Producto {
    public ProductoImportado(String codigo, String nombre, double precio, int stock) {
        super(codigo, nombre, precio, stock);
    }
    @Override public double calcularPrecioFinal() { return precio * 1.50; }
    @Override public String mostrarInformacion() { return "Producto Importado: " + nombre + " - $" + calcularPrecioFinal(); }
    @Override public boolean validarDisponibilidad() { return stock > 0; }
    @Override public void aplicarDescuento(double porcentaje) { this.precio -= this.precio * (porcentaje/100); }
}
