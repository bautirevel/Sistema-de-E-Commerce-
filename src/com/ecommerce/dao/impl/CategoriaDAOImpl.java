package com.ecommerce.dao.impl;
import com.ecommerce.dao.CategoriaDAO;
import java.sql.*;

public class CategoriaDAOImpl implements CategoriaDAO {
    private String url = "jdbc:sqlite:ecommerce.db";

    @Override 
    public void insertar() { 
        String sql = "INSERT INTO categorias (nombre, estado) VALUES (?, ?)";
        try (Connection conn = DriverManager.getConnection(url);
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, "Nueva Categoria");
            stmt.setString(2, "ACTIVO");
            stmt.executeUpdate();
            System.out.println("Categoría insertada correctamente en BD.");
        } catch (SQLException e) {
            System.out.println("Error JDBC: " + e.getMessage());
        }
    }
    @Override public void buscarPorId() { System.out.println("Buscando categoría en BD..."); }
    @Override public void listarTodos() { System.out.println("Listando categorías desde BD..."); }
    @Override public void actualizar() { System.out.println("Categoría actualizada en BD."); }
    @Override public void eliminar() { System.out.println("Categoría eliminada de BD."); }
}
