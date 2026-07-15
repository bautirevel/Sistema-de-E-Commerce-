package com.ecommerce.model;

public class ItemCarrito {
    private Producto producto;
    private int cantidad;
    private double precioUnitario;

    public ItemCarrito(Producto producto, int cantidad) {
        this.producto = producto;
        this.cantidad = cantidad;
        this.precioUnitario = producto.calcularPrecioFinal();
    }

    // Constructor para reconstruir un item ya persistido (no recalcula el precio final,
    // evita aplicar dos veces el markup al leer desde la base de datos).
    public ItemCarrito(Producto producto, int cantidad, double precioUnitario) {
        this.producto = producto;
        this.cantidad = cantidad;
        this.precioUnitario = precioUnitario;
    }

    public double getSubtotal() { return this.precioUnitario * this.cantidad; }
    public Producto getProducto() { return this.producto; }
    public int getCantidad() { return this.cantidad; }

    @Override
    public String toString() {
        return cantidad + "x " + producto.mostrarInformacion() + " | Subtotal: $" + getSubtotal();
    }
}
