package com.ecommerce.utils;
import java.sql.*;

public class Conexion {
    private static Conexion instancia;
    private static boolean baseVerificada = false;

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
            asegurarBaseDeDatos();
            return DriverManager.getConnection("jdbc:mysql://localhost/ecommerce?useSSL=false&serverTimezone=UTC", "root", "");
        } catch (Exception e) {
            System.out.println("[ERROR CRITICO DB]: Verifica que XAMPP (MySQL) este encendido. Detalle: " + e.getMessage());
            return null;
        }
    }

    private void asegurarBaseDeDatos() throws SQLException {
        if (baseVerificada) return;
        try (Connection con = DriverManager.getConnection("jdbc:mysql://localhost/?useSSL=false&serverTimezone=UTC", "root", "");
             Statement st = con.createStatement()) {
            st.execute("CREATE DATABASE IF NOT EXISTS ecommerce");
            baseVerificada = true;
        }
    }
}
