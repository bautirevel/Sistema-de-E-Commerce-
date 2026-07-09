package com.ecommerce.model;

public class ProductoDigital extends Producto {
    //atributo propio de un producto digital, el poder descargarlo
    private String linkDescarga;

    //el constructor le asigna peso 0.0 (por ser digitales)
    public ProductoDigital(String codigoUnico, String nombre, String descripcion, double precioBase, 
                           String categoria, int stock, EstadoProducto estado, String linkDescarga) {
        super(codigoUnico, nombre, descripcion, precioBase, categoria, stock, 0.0, estado);
        this.linkDescarga = linkDescarga;
    }

    //overrides, polimorfismo de los métodos abstractos de Producto

    @Override
    public double calcularPrecioFinal() {
        // Los productos digitales no tienen recargos de envío ni aduanas
        return getPrecioBase();
    }

    @Override
    public void mostrarInformacion() {
        System.out.println("=== PRODUCTO DIGITAL ===");
        System.out.println("Código: " + getCodigoUnico());
        System.out.println("Nombre: " + getNombre());
        System.out.println("Categoría: " + getCategoria());
        System.out.println("Precio Base: $" + getPrecioBase());
        System.out.println("Enlace: " + linkDescarga);
        System.out.println("Estado: " + getEstado());
        System.out.println("Precio Final: $" + calcularPrecioFinal());
        System.out.println("========================");
    }

    @Override
    public boolean validarDisponibilidad() {
        //estos no dependen de su stock, dependen del estado que la plataforma proveedora del producto/servicio les asigne
        return getEstado() == EstadoProducto.ACTIVO;
    }

    @Override
    public void aplicarDescuento(double porcentaje) {
        if (porcentaje > 0 && porcentaje <= 100) {
            double nuevoPrecio = getPrecioBase() * (1 - (porcentaje / 100.0));
            setPrecioBase(nuevoPrecio);
        }
    }

    //getters y setters
    public String getLinkDescarga() { return linkDescarga; }
    public void setLinkDescarga(String linkDescarga) { this.linkDescarga = linkDescarga; }
}