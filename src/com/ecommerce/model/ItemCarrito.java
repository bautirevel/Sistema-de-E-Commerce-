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

    public double getSubtotal() { return this.precioUnitario * this.cantidad; }
    public Producto getProducto() { return this.producto; }
    public int getCantidad() { return this.cantidad; }

    @Override
    public String toString() {
        return cantidad + "x " + producto.mostrarInformacion() + " | Subtotal: $" + getSubtotal();
    }
}
