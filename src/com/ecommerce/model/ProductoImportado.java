package com.ecommerce.model;
public class ProductoImportado extends Producto {
    public ProductoImportado(String codigo, String nombre, double precio, int stock) {
        super(codigo, nombre, "Importado", precio, "General", stock, 2.5, EstadoProducto.ACTIVO);
    }

    public ProductoImportado(String codigo, String nombre, String descripcion, double precio, String categoria, int stock, double peso, EstadoProducto estado) {
        super(codigo, nombre, descripcion, precio, categoria, stock, peso, estado);
    }

    @Override public double calcularPrecioFinal() { return precio * 1.50; }
    @Override public String mostrarInformacion() { return "Importado: " + nombre + " - $" + calcularPrecioFinal(); }
    @Override public boolean validarDisponibilidad() { return estado == EstadoProducto.ACTIVO && stock > 0; }
    @Override public void aplicarDescuento(double porcentaje) { this.precio -= this.precio * (porcentaje/100); }
}
