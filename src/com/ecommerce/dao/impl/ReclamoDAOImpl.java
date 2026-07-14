package com.ecommerce.dao.impl;
import com.ecommerce.dao.ReclamoDAO;
import com.ecommerce.utils.Conexion;
import java.sql.*;

public class ReclamoDAOImpl implements ReclamoDAO {
    private Conexion conexion = Conexion.getInstancia();
    public ReclamoDAOImpl() {
        try (Connection con = conexion.conectar(); Statement st = con.createStatement()) {
            st.execute("CREATE TABLE IF NOT EXISTS reclamos (id INT AUTO_INCREMENT PRIMARY KEY, motivo VARCHAR(200), estado VARCHAR(30) DEFAULT 'ABIERTO', cliente_email VARCHAR(100), id_orden INT NULL, fecha TIMESTAMP DEFAULT CURRENT_TIMESTAMP)");
        } catch (SQLException e) {}
        // Compatibilidad con bases creadas por una version anterior de la tabla.
        agregarColumnaSiNoExiste("cliente_email VARCHAR(100)");
        agregarColumnaSiNoExiste("id_orden INT NULL");
        agregarColumnaSiNoExiste("fecha TIMESTAMP DEFAULT CURRENT_TIMESTAMP");
    }

    private void agregarColumnaSiNoExiste(String definicionColumna) {
        try (Connection con = conexion.conectar(); Statement st = con.createStatement()) {
            st.execute("ALTER TABLE reclamos ADD COLUMN " + definicionColumna);
        } catch (SQLException e) { /* la columna ya existe, se ignora */ }
    }

    @Override
    public void abrirReclamo(String clienteEmail, Integer idOrden, String motivo) {
        try (Connection con = conexion.conectar();
             PreparedStatement ps = con.prepareStatement("INSERT INTO reclamos (motivo, cliente_email, id_orden) VALUES (?, ?, ?)")) {
            ps.setString(1, motivo);
            ps.setString(2, clienteEmail);
            if (idOrden != null) ps.setInt(3, idOrden); else ps.setNull(3, Types.INTEGER);
            ps.executeUpdate();
            System.out.println("Reclamo registrado exitosamente. Nuestro equipo lo revisara.");
        } catch (SQLException e) {
            System.out.println("Error al registrar el reclamo: " + e.getMessage());
        }
    }

    @Override
    public void listarTodos() {
        try (Connection con = conexion.conectar(); Statement st = con.createStatement();
             ResultSet rs = st.executeQuery("SELECT * FROM reclamos ORDER BY fecha DESC")) {
            System.out.println("--- RECLAMOS ---");
            while (rs.next()) {
                System.out.println("#" + rs.getInt("id") + " | Cliente: " + rs.getString("cliente_email")
                        + " | Orden: " + (rs.getObject("id_orden") == null ? "-" : rs.getInt("id_orden"))
                        + " | Motivo: " + rs.getString("motivo")
                        + " | Estado: " + rs.getString("estado")
                        + " | Fecha: " + rs.getTimestamp("fecha"));
            }
        } catch (SQLException e) {
            System.out.println("Error al listar reclamos: " + e.getMessage());
        }
    }

    @Override
    public void cambiarEstado(int id, String nuevoEstado) {
        try (Connection con = conexion.conectar();
             PreparedStatement ps = con.prepareStatement("UPDATE reclamos SET estado=? WHERE id=?")) {
            ps.setString(1, nuevoEstado);
            ps.setInt(2, id);
            int filas = ps.executeUpdate();
            System.out.println(filas > 0 ? "Estado del reclamo actualizado a " + nuevoEstado + "." : "[ERROR]: No se encontro el reclamo.");
        } catch (SQLException e) {
            System.out.println("Error al cambiar estado del reclamo: " + e.getMessage());
        }
    }
}