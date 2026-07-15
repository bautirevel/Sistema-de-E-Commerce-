package com.ecommerce.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Orden {
    private int numero;
    private String clienteEmail;
    private Date fecha;
    private List<ItemCarrito> items;
    private double total;
    private EstadoOrden estado;
    private Integer idPago;
    private Integer idEnvio;

    private Orden(Builder builder) {
        this.numero = builder.numero;
        this.clienteEmail = builder.clienteEmail;
        this.fecha = builder.fecha;
        this.items = builder.items;
        this.total = builder.total;
        this.estado = builder.estado;
        this.idPago = builder.idPago;
        this.idEnvio = builder.idEnvio;
    }

    public static class Builder {
        private int numero;
        private String clienteEmail;
        private Date fecha = new Date();
        private List<ItemCarrito> items = new ArrayList<>();
        private double total;
        private EstadoOrden estado = EstadoOrden.CREADA;
        private Integer idPago;
        private Integer idEnvio;

        public Builder setNumero(int numero) { this.numero = numero; return this; }
        public Builder setClienteEmail(String email) { this.clienteEmail = email; return this; }
        public Builder setFecha(Date fecha) { this.fecha = fecha; return this; }
        public Builder setItems(List<ItemCarrito> items) { this.items = items; return this; }
        public Builder setTotal(double total) { this.total = total; return this; }
        public Builder setEstado(EstadoOrden estado) { this.estado = estado; return this; }
        public Builder setIdPago(Integer idPago) { this.idPago = idPago; return this; }
        public Builder setIdEnvio(Integer idEnvio) { this.idEnvio = idEnvio; return this; }
        public Orden build() { return new Orden(this); }
    }

    public int getNumero() { return numero; }
    public String getClienteEmail() { return clienteEmail; }
    public Date getFecha() { return fecha; }
    public List<ItemCarrito> getItems() { return items; }
    public double getTotal() { return total; }
    public EstadoOrden getEstado() { return estado; }
    public void setEstado(EstadoOrden estado) { this.estado = estado; }
    public Integer getIdPago() { return idPago; }
    public void setIdPago(Integer idPago) { this.idPago = idPago; }
    public Integer getIdEnvio() { return idEnvio; }
    public void setIdEnvio(Integer idEnvio) { this.idEnvio = idEnvio; }

    @Override
    public String toString() {
        return "Orden #" + numero + " | Cliente: " + clienteEmail + " | Fecha: " + fecha
                + " | Total: $" + total + " | Estado: " + estado
                + " | Pago: " + (idPago == null ? "-" : idPago)
                + " | Envio: " + (idEnvio == null ? "-" : idEnvio);
    }
}
