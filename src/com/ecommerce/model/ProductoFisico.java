package com.ecommerce.model;
public class ProductoFisico extends Producto {
    public ProductoFisico(String codigo, String nombre, double precio, int stock) {
        super(codigo, nombre, "Fisico", precio, "General", stock, 1.0, EstadoProducto.ACTIVO);
    }

    // Constructor completo: se usa al reconstruir el producto desde la base de datos
    // o cuando el usuario carga todos los campos desde el menu.
    public ProductoFisico(String codigo, String nombre, String descripcion, double precio, String categoria, int stock, double peso, EstadoProducto estado) {
        super(codigo, nombre, descripcion, precio, categoria, stock, peso, estado);
    }

    @Override public double calcularPrecioFinal() { return precio * 1.21; }
    @Override public String mostrarInformacion() { return "Fisico: " + nombre + " - $" + calcularPrecioFinal(); }
    @Override public boolean validarDisponibilidad() { return estado == EstadoProducto.ACTIVO && stock > 0; }
    @Override public void aplicarDescuento(double porcentaje) { this.precio -= this.precio * (porcentaje/100); }
}
