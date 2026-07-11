package com.ecommerce.dao.impl;
import com.ecommerce.dao.CategoriaDAO;
import com.ecommerce.utils.Conexion;
import java.sql.*;

public class CategoriaDAOImpl implements CategoriaDAO {
    private Conexion conexion = new Conexion();
    @Override
    public void insertar(String nombre, String descripcion) {
        try (Connection con = conexion.conectar();
             PreparedStatement ps = con.prepareStatement("INSERT INTO categorias (nombre, descripcion) VALUES (?, ?)")) {
            ps.setString(1, nombre);
            ps.setString(2, descripcion);
            ps.executeUpdate();
        } catch (SQLException e) { System.out.println("Error: " + e.getMessage()); }
    }
    @Override
    public void listarTodos() {
        try (Connection con = conexion.conectar();
             Statement st = con.createStatement();
             ResultSet rs = st.executeQuery("SELECT * FROM categorias")) {
            while(rs.next()) {
                System.out.println("ID: " + rs.getInt("id") + " | " + rs.getString("nombre") + " - " + rs.getString("descripcion"));
            }
        } catch (SQLException e) { System.out.println("Error: " + e.getMessage()); }
    }
}