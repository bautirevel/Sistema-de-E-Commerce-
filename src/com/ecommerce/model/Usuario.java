package com.ecommerce.model;

public class Usuario {
    //atributos (en privado para el encapsulamiento)
    private int id;
    private String nombre;
    private String apellido;
    private String email;
    private String password;
    private boolean estado; //true activo / false inactivo
    private Rol rol;

    //constructor vacío
    public Usuario() {
    }

    //constructor
    public Usuario(int id, String nombre, String apellido, String email, String password, boolean estado, Rol rol) {
        this.id = id;
        this.nombre = nombre;
        this.apellido = apellido;
        this.email = email;
        this.password = password;
        this.estado = estado;
        this.rol = rol;
    }

    //getters setters

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }
    public void setNombre(String nombre) {
        this.nombre = nombre;
    }
    public String getApellido() {
        return apellido;
    }
    public void setApellido(String apellido) {
        this.apellido = apellido;
    }
    public String getEmail() {
        return email;
    }
    public void setEmail(String email) {
        this.email = email;
    }
    public String getPassword() {
        return password;
    }
    public void setPassword(String password) {
        this.password = password;
    }
    public boolean isEstado() {
        return estado;
    }
    public void setEstado(boolean estado) {
        this.estado = estado;
    }
    public Rol getRol() {
        return rol;
    }
    public void setRol(Rol rol) {
        this.rol = rol;
    }
}