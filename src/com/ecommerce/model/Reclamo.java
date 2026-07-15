package com.ecommerce.model;

import java.util.Date;

public class Reclamo {
    private int numero;
    private String clienteEmail;
    private Integer idOrden;
    private String motivo;
    private Date fecha;
    private EstadoReclamo estado;

    public Reclamo(int numero, String clienteEmail, Integer idOrden, String motivo, Date fecha, EstadoReclamo estado) {
        this.numero = numero;
        this.clienteEmail = clienteEmail;
        this.idOrden = idOrden;
        this.motivo = motivo;
        this.fecha = fecha;
        this.estado = estado;
    }

    public int getNumero() { return numero; }
    public String getClienteEmail() { return clienteEmail; }
    public Integer getIdOrden() { return idOrden; }
    public String getMotivo() { return motivo; }
    public Date getFecha() { return fecha; }
    public EstadoReclamo getEstado() { return estado; }

    @Override
    public String toString() {
        return "#" + numero + " | Cliente: " + clienteEmail
                + " | Orden: " + (idOrden == null ? "-" : idOrden)
                + " | Motivo: " + motivo + " | Estado: " + estado + " | Fecha: " + fecha;
    }
}
