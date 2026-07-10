package com.ecommerce.dao.impl;
import com.ecommerce.dao.ReclamoDAO;
import java.sql.*;

public class ReclamoDAOImpl implements ReclamoDAO {
    private String url = "jdbc:sqlite:ecommerce.db";

    @Override 
    public void abrirReclamo() { 
        String sql = "INSERT INTO reclamos (orden_id, motivo, estado) VALUES (?, ?, ?)";
        try (Connection conn = DriverManager.getConnection(url);
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, 1);
            stmt.setString(2, "Producto defectuoso");
            stmt.setString(3, "ABIERTO");
            stmt.executeUpdate();
            System.out.println("Reclamo registrado correctamente en BD.");
        } catch (SQLException e) {
            System.out.println("Error JDBC al abrir reclamo: " + e.getMessage());
        }
    }
    @Override public void buscarReclamo() { System.out.println("Buscando reclamo en BD..."); }
    @Override public void actualizarEstado() { System.out.println("Reclamo actualizado en BD."); }
}
