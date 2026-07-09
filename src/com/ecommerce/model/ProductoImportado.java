package com.ecommerce.model;

public class ProductoImportado extends Producto {
    // atributo propio que es el porcentaje de recargo por aduana
    private double impuestoAduana;

    public ProductoImportado(String codigoUnico, String nombre, String descripcion, double precioBase, 
                             String categoria, int stock, double peso, EstadoProducto estado, double impuestoAduana) {
        super(codigoUnico, nombre, descripcion, precioBase, categoria, stock, peso, estado);
        this.impuestoAduana = impuestoAduana;
    }

    //polimorfismo para el recargo de aduana

    @Override
    public double calcularPrecioFinal() {
        // El precio final le aplica el recargo porcentual por importación
        double recargo = getPrecioBase() * (impuestoAduana / 100.0);
        return getPrecioBase() + recargo;
    }

    @Override
    public void mostrarInformacion() {
        System.out.println("=== PRODUCTO IMPORTADO ===");
        System.out.println("Código: " + getCodigoUnico());
        System.out.println("Nombre: " + getNombre());
        System.out.println("Categoría: " + getCategoria());
        System.out.println("Precio Base: $" + getPrecioBase());
        System.out.println("Impuesto de Aduana: " + impuestoAduana + "%");
        System.out.println("Peso: " + getPeso() + " kg");
        System.out.println("Estado: " + getEstado());
        System.out.println("Stock: " + getStock());
        System.out.println("Precio Final (con impuestos): $" + calcularPrecioFinal());
        System.out.println("==========================");
    }

    @Override
    public boolean validarDisponibilidad() {
        //al igual que un producto físico se valida el stock y la disponibilidad
        return getEstado() == EstadoProducto.ACTIVO && getStock() > 0;
    }

    @Override
    public void aplicarDescuento(double porcentaje) {
        if (porcentaje > 0 && porcentaje <= 100) {
            double nuevoPrecio = getPrecioBase() * (1 - (porcentaje / 100.0));
            setPrecioBase(nuevoPrecio);
        }
    }

    //getters, setters
    public double getImpuestoAduana() { return impuestoAduana; }
    public void setImpuestoAduana(double impuestoAduana) { this.impuestoAduana = impuestoAduana; }
}