package com.ecommerce.model;
import com.ecommerce.interfaces.Mostrable;
import com.ecommerce.interfaces.Calculable;

public abstract class Producto implements Mostrable, Calculable {
    protected String codigo;
    protected String nombre;
    protected double precio;
    protected int stock;

    public Producto(String codigo, String nombre, double precio, int stock) {
        this.codigo = codigo;
        this.nombre = nombre;
        this.precio = precio;
        this.stock = stock;
    }

    public abstract boolean validarDisponibilidad();
    public abstract void aplicarDescuento(double porcentaje);
}
