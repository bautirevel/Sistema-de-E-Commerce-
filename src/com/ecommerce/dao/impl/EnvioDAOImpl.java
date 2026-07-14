package com.ecommerce.dao.impl;
import com.ecommerce.dao.EnvioDAO;
import com.ecommerce.utils.Conexion;
import java.sql.*;

public class EnvioDAOImpl implements EnvioDAO {
    private Conexion conexion = Conexion.getInstancia();
    public EnvioDAOImpl() {
        try (Connection con = conexion.conectar(); Statement st = con.createStatement()) {
            st.execute("CREATE TABLE IF NOT EXISTS envios (id INT AUTO_INCREMENT PRIMARY KEY, id_orden INT, direccion VARCHAR(150), estado VARCHAR(30))");
        } catch (SQLException e) {}
    }
    @Override
    public void registrarEnvio(int idOrden, String direccion) {
        try (Connection con = conexion.conectar(); PreparedStatement ps = con.prepareStatement("INSERT INTO envios (id_orden, direccion, estado) VALUES (?, ?, 'EN CAMINO')")) {
            ps.setInt(1, idOrden); ps.setString(2, direccion); ps.executeUpdate();
            System.out.println("Envio registrado hacia: " + direccion);
        } catch (SQLException e) {}
    }
    @Override
    public void rastrearEnvio(int idEnvio) {
        try (Connection con = conexion.conectar(); PreparedStatement ps = con.prepareStatement("SELECT * FROM envios WHERE id=?")) {
            ps.setInt(1, idEnvio); ResultSet rs = ps.executeQuery();
            if(rs.next()) System.out.println("Envio #" + idEnvio + " | Estado: " + rs.getString("estado") + " | Destino: " + rs.getString("direccion"));
            else System.out.println("Envio no encontrado.");
        } catch (SQLException e) {}
    }
}