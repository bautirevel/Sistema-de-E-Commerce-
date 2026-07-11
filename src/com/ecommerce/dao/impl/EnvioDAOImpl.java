package com.ecommerce.dao.impl;
import com.ecommerce.dao.EnvioDAO;
import com.ecommerce.utils.Conexion;
import java.sql.*;

public class EnvioDAOImpl implements EnvioDAO {
    private Conexion conexion = new Conexion();
    
    public EnvioDAOImpl() {
        try (Connection con = conexion.conectar();
             Statement st = con.createStatement()) {
            st.execute("CREATE TABLE IF NOT EXISTS envios (id INT AUTO_INCREMENT PRIMARY KEY, id_orden INT, direccion VARCHAR(100), estado VARCHAR(20) DEFAULT 'PENDIENTE')");
        } catch (SQLException e) {}
    }

    @Override
    public void registrarEnvio(int idOrden, String direccion) {
        try (Connection con = conexion.conectar();
             PreparedStatement ps = con.prepareStatement("INSERT INTO envios (id_orden, direccion) VALUES (?, ?)")) {
            ps.setInt(1, idOrden);
            ps.setString(2, direccion);
            ps.executeUpdate();
        } catch (SQLException e) { System.out.println("Error: " + e.getMessage()); }
    }

    @Override
    public void rastrearEnvio(int idEnvio) {
        try (Connection con = conexion.conectar();
             PreparedStatement ps = con.prepareStatement("SELECT * FROM envios WHERE id = ?")) {
            ps.setInt(1, idEnvio);
            ResultSet rs = ps.executeQuery();
            if(rs.next()) System.out.println("Envio #" + rs.getInt("id") + " | Orden: " + rs.getInt("id_orden") + " | Estado: " + rs.getString("estado") + " | Destino: " + rs.getString("direccion"));
            else System.out.println("Envio no encontrado.");
        } catch (SQLException e) { System.out.println("Error: " + e.getMessage()); }
    }
}