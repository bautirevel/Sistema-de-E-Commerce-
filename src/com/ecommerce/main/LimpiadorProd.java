package com.ecommerce.main;
import com.ecommerce.utils.Conexion;
import java.sql.*;

public class LimpiadorProd {
    public static void main(String[] args) {
        try (Connection con = new Conexion().conectar(); Statement st = con.createStatement()) {
            st.execute("DROP TABLE IF EXISTS productos");
            System.out.println("[SISTEMA]: Tabla de productos reseteada para forzar columna 'stock'.");
        } catch (Exception e) {}
    }
}