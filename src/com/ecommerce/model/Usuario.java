package com.ecommerce.model;

import java.util.Date;

public class Usuario {
    private int id;
    private String nombre;
    private String apellido;
    private String email;
    private String contrasena;
    private Date fechaAlta;
    private boolean estado;
    private Rol rol;

    public Usuario(int id, String nombre, String apellido, String email, String contrasena, Date fechaAlta, boolean estado, Rol rol) {
        this.id = id;
        this.nombre = nombre;
        this.apellido = apellido;
        this.email = email;
        this.contrasena = contrasena;
        this.fechaAlta = fechaAlta;
        this.estado = estado;
        this.rol = rol;
    }

    public int getId() { return id; }
    public String getNombre() { return nombre; }
    public String getApellido() { return apellido; }
    public String getEmail() { return email; }
    public String getContrasena() { return contrasena; }
    public Date getFechaAlta() { return fechaAlta; }
    public boolean isEstado() { return estado; }
    public Rol getRol() { return rol; }
}