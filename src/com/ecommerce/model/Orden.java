package com.ecommerce.model;
import java.util.Date;

public class Orden {
    private int numero;
    private String clienteEmail;
    private Date fecha;
    private double total;
    private String estado;

    private Orden(Builder builder) {
        this.numero = builder.numero;
        this.clienteEmail = builder.clienteEmail;
        this.fecha = builder.fecha;
        this.total = builder.total;
        this.estado = builder.estado;
    }

    public static class Builder {
        private int numero;
        private String clienteEmail;
        private Date fecha = new Date();
        private double total;
        private String estado = "CREADA";

        public Builder setNumero(int numero) { this.numero = numero; return this; }
        public Builder setClienteEmail(String email) { this.clienteEmail = email; return this; }
        public Builder setTotal(double total) { this.total = total; return this; }
        public Builder setEstado(String estado) { this.estado = estado; return this; }
        public Orden build() { return new Orden(this); }
    }

    public int getNumero() { return numero; }
    public String getClienteEmail() { return clienteEmail; }
    public Date getFecha() { return fecha; }
    public double getTotal() { return total; }
    public String getEstado() { return estado; }
}