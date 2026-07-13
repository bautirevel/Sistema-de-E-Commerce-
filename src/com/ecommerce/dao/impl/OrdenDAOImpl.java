package com.ecommerce.dao.impl;
import com.ecommerce.dao.OrdenDAO;
import com.ecommerce.utils.Conexion;
import java.sql.*;

public class OrdenDAOImpl implements OrdenDAO {
    private Conexion conexion = new Conexion();
    public OrdenDAOImpl() {
        try (Connection con = conexion.conectar(); Statement st = con.createStatement()) {
            st.execute("CREATE TABLE IF NOT EXISTS ordenes (id INT AUTO_INCREMENT PRIMARY KEY, fecha TIMESTAMP DEFAULT CURRENT_TIMESTAMP, total DOUBLE, estado VARCHAR(30))");
        } catch (SQLException e) {}
    }
    @Override
    public void generarOrden(double total, String estado) {
        try (Connection con = conexion.conectar(); PreparedStatement ps = con.prepareStatement("INSERT INTO ordenes (total, estado) VALUES (?, ?)", Statement.RETURN_GENERATED_KEYS)) {
            ps.setDouble(1, total); ps.setString(2, estado); ps.executeUpdate();
            ResultSet rs = ps.getGeneratedKeys();
            if(rs.next()) System.out.println("Orden #" + rs.getInt(1) + " generada con exito.");
        } catch (SQLException e) {}
    }
}