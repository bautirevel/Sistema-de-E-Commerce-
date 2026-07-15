package com.ecommerce.dao.impl;

import com.ecommerce.dao.ReclamoDAO;
import com.ecommerce.model.Reclamo;
import com.ecommerce.model.EstadoReclamo;
import com.ecommerce.utils.Conexion;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ReclamoDAOImpl implements ReclamoDAO {
    private Conexion conexion = Conexion.getInstancia();

    public ReclamoDAOImpl() {
        try (Connection con = conexion.conectar(); Statement st = con.createStatement()) {
            st.execute("CREATE TABLE IF NOT EXISTS reclamos (id INT AUTO_INCREMENT PRIMARY KEY, motivo VARCHAR(200), estado VARCHAR(30) DEFAULT 'ABIERTO', cliente_email VARCHAR(100), id_orden INT NULL, fecha TIMESTAMP DEFAULT CURRENT_TIMESTAMP)");
        } catch (SQLException e) {}
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
    public Reclamo guardar(Reclamo reclamo) {
        try (Connection con = conexion.conectar();
             PreparedStatement ps = con.prepareStatement("INSERT INTO reclamos (motivo, cliente_email, id_orden, estado) VALUES (?, ?, ?, ?)", Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, reclamo.getMotivo());
            ps.setString(2, reclamo.getClienteEmail());
            if (reclamo.getIdOrden() != null) ps.setInt(3, reclamo.getIdOrden()); else ps.setNull(3, Types.INTEGER);
            ps.setString(4, reclamo.getEstado().name());
            ps.executeUpdate();
            ResultSet rs = ps.getGeneratedKeys();
            int numero = 0;
            if (rs.next()) numero = rs.getInt(1);
            System.out.println("Reclamo #" + numero + " registrado exitosamente. Nuestro equipo lo revisara.");
            return new Reclamo(numero, reclamo.getClienteEmail(), reclamo.getIdOrden(), reclamo.getMotivo(), reclamo.getFecha(), reclamo.getEstado());
        } catch (SQLException e) {
            System.out.println("Error al registrar el reclamo: " + e.getMessage());
            return null;
        }
    }

    @Override
    public List<Reclamo> obtenerTodos() {
        List<Reclamo> lista = new ArrayList<>();
        try (Connection con = conexion.conectar(); Statement st = con.createStatement();
             ResultSet rs = st.executeQuery("SELECT * FROM reclamos ORDER BY fecha DESC")) {
            while (rs.next()) {
                Integer idOrden = (Integer) rs.getObject("id_orden");
                lista.add(new Reclamo(rs.getInt("id"), rs.getString("cliente_email"), idOrden,
                        rs.getString("motivo"), rs.getTimestamp("fecha"), EstadoReclamo.valueOf(rs.getString("estado"))));
            }
        } catch (SQLException e) {
            System.out.println("Error al listar reclamos: " + e.getMessage());
        }
        return lista;
    }

    @Override
    public void cambiarEstado(int numero, EstadoReclamo nuevoEstado) {
        try (Connection con = conexion.conectar();
             PreparedStatement ps = con.prepareStatement("UPDATE reclamos SET estado=? WHERE id=?")) {
            ps.setString(1, nuevoEstado.name());
            ps.setInt(2, numero);
            int filas = ps.executeUpdate();
            System.out.println(filas > 0 ? "Estado del reclamo actualizado a " + nuevoEstado + "." : "[ERROR]: No se encontro el reclamo.");
        } catch (SQLException e) {
            System.out.println("Error al cambiar estado del reclamo: " + e.getMessage());
        }
    }

    @Override
    public int contarPorEstado(EstadoReclamo estado) {
        try (Connection con = conexion.conectar(); PreparedStatement ps = con.prepareStatement("SELECT COUNT(*) FROM reclamos WHERE estado=?")) {
            ps.setString(1, estado.name());
            ResultSet rs = ps.executeQuery();
            if (rs.next()) return rs.getInt(1);
        } catch (SQLException e) {
            System.out.println("Error contando reclamos: " + e.getMessage());
        }
        return 0;
    }
}
