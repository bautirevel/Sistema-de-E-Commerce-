package com.ecommerce.dao.impl;
import com.ecommerce.dao.ReclamoDAO;
import com.ecommerce.utils.Conexion;
import java.sql.*;

public class ReclamoDAOImpl implements ReclamoDAO {
    private Conexion conexion = new Conexion();
    public ReclamoDAOImpl() {
        try (Connection con = conexion.conectar(); Statement st = con.createStatement()) {
            st.execute("CREATE TABLE IF NOT EXISTS reclamos (id INT AUTO_INCREMENT PRIMARY KEY, motivo VARCHAR(200), estado VARCHAR(30) DEFAULT 'ABIERTO')");
        } catch (SQLException e) {}
    }
    @Override
    public void abrirReclamo(String motivo) {
        try (Connection con = conexion.conectar(); PreparedStatement ps = con.prepareStatement("INSERT INTO reclamos (motivo) VALUES (?)")) {
            ps.setString(1, motivo); ps.executeUpdate(); System.out.println("Reclamo registrado exitosamente. Nuestro equipo lo revisara.");
        } catch (SQLException e) {}
    }
}