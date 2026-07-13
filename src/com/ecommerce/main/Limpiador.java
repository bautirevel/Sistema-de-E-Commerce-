package com.ecommerce.main;
import com.ecommerce.utils.Conexion;
import java.sql.*;

public class Limpiador {
    public static void main(String[] args) {
        try (Connection con = new Conexion().conectar(); Statement st = con.createStatement()) {
            st.execute("DROP TABLE IF EXISTS usuarios");
            System.out.println("[SISTEMA]: Tabla de usuarios reseteada. Lista para el nuevo esquema.");
        } catch (Exception e) {}
    }
}