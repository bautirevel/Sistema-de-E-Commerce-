package com.ecommerce.dao.impl;
import com.ecommerce.dao.ReclamoDAO;
import com.ecommerce.utils.Conexion;
import java.sql.*;

public class ReclamoDAOImpl implements ReclamoDAO {
    private Conexion conexion = new Conexion();
    
    @Override 
    public void abrirReclamo(String motivo) {
        try (Connection con = conexion.conectar();
             PreparedStatement ps = con.prepareStatement("INSERT INTO reclamos (motivo, estado) VALUES (?, 'ABIERTO')")) {
            ps.setString(1, motivo);
            ps.executeUpdate();
            System.out.println("¡Reclamo/Devolucion registrado en BD!");
        } catch (SQLException e) { System.out.println("Error: " + e.getMessage()); }
    }
}