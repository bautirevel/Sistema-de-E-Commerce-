package com.ecommerce.model;

public class ProductoFisico extends Producto {
    //el atributo propio de un producto físico (q lo diferencia de uno digital)
    private double costoEnvioBase;

    //constructor
    public ProductoFisico(String codigoUnico, String nombre, String descripcion, double precioBase, 
                          String categoria, int stock, double peso, EstadoProducto estado, double costoEnvioBase) {
        super(codigoUnico, nombre, descripcion, precioBase, categoria, stock, peso, estado);
        this.costoEnvioBase = costoEnvioBase;
    }

    //polimorfismo y overrides

    @Override
    public double calcularPrecioFinal() {
        //el precio base + el costo por peso
        return getPrecioBase() + costoEnvioBase + (getPeso() * 100.0);
    }

    @Override
    public void mostrarInformacion() {
        System.out.println("=== PRODUCTO FÍSICO ===");
        System.out.println("Código: " + getCodigoUnico());
        System.out.println("Nombre: " + getNombre());
        System.out.println("Categoría: " + getCategoria());
        System.out.println("Precio Base: $" + getPrecioBase());
        System.out.println("Peso: " + getPeso() + " kg");
        System.out.println("Estado: " + getEstado());
        System.out.println("Stock: " + getStock());
        System.out.println("Precio Final (con envío): $" + calcularPrecioFinal());
        System.out.println("=======================");
    }

    @Override
    public boolean validarDisponibilidad() {
        //se valida stock/disponibilidad
        return getEstado() == EstadoProducto.ACTIVO && getStock() > 0;
    }

    @Override
    public void aplicarDescuento(double porcentaje) {
        if (porcentaje > 0 && porcentaje <= 100) {
            double nuevoPrecio = getPrecioBase() * (1 - (porcentaje / 100.0));
            setPrecioBase(nuevoPrecio);
        }
    }

    //getters y setters
    public double getCostoEnvioBase() { return costoEnvioBase; }
    public void setCostoEnvioBase(double costoEnvioBase) { this.costoEnvioBase = costoEnvioBase; }
}