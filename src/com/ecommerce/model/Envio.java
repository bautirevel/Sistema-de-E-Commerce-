package com.ecommerce.model;

public class Envio {
    private int id;
    private int idOrden;
    private String codigoSeguimiento;
    private String direccion;
    private String provincia;
    private String ciudad;
    private String codigoPostal;
    private TipoEnvio tipoEnvio;
    private EstadoEnvio estado;
    private double costo;

    public Envio(int id, int idOrden, String codigoSeguimiento, String direccion, String provincia,
                 String ciudad, String codigoPostal, TipoEnvio tipoEnvio, EstadoEnvio estado, double costo) {
        this.id = id;
        this.idOrden = idOrden;
        this.codigoSeguimiento = codigoSeguimiento;
        this.direccion = direccion;
        this.provincia = provincia;
        this.ciudad = ciudad;
        this.codigoPostal = codigoPostal;
        this.tipoEnvio = tipoEnvio;
        this.estado = estado;
        this.costo = costo;
    }

    public int getId() { return id; }
    public int getIdOrden() { return idOrden; }
    public String getCodigoSeguimiento() { return codigoSeguimiento; }
    public String getDireccion() { return direccion; }
    public String getProvincia() { return provincia; }
    public String getCiudad() { return ciudad; }
    public String getCodigoPostal() { return codigoPostal; }
    public TipoEnvio getTipoEnvio() { return tipoEnvio; }
    public EstadoEnvio getEstado() { return estado; }
    public void setEstado(EstadoEnvio estado) { this.estado = estado; }
    public double getCosto() { return costo; }

    @Override
    public String toString() {
        return "Envio #" + id + " | Orden #" + idOrden + " | Seguimiento: " + codigoSeguimiento
                + " | " + direccion + ", " + ciudad + ", " + provincia + " (CP " + codigoPostal + ")"
                + " | Tipo: " + tipoEnvio + " | Estado: " + estado + " | Costo: $" + costo;
    }
}
