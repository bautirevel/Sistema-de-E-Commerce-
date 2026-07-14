package com.ecommerce.utils;
import java.sql.*;

public class Conexion {
    private static Conexion instancia;
    private Conexion() {}
    public static synchronized Conexion getInstancia() {
        if (instancia == null) {
            instancia = new Conexion();
        }
        return instancia;
    }
    public Connection conectar() {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            return DriverManager.getConnection("jdbc:mysql://localhost/ecommerce", "root", "");
        } catch (Exception e) {
            System.out.println("[ERROR CRITICO DB]: Verifica que XAMPP (MySQL) este encendido.");
            return null;
        }
    }
}