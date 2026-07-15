package com.ecommerce.dao.impl;

import com.ecommerce.dao.EnvioDAO;
import com.ecommerce.model.Envio;
import com.ecommerce.model.EstadoEnvio;
import com.ecommerce.model.TipoEnvio;
import com.ecommerce.exceptions.EnvioNoEncontradoException;
import com.ecommerce.utils.Conexion;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class EnvioDAOImpl implements EnvioDAO {
    private Conexion conexion = Conexion.getInstancia();

    public EnvioDAOImpl() {
        try (Connection con = conexion.conectar(); Statement st = con.createStatement()) {
            st.execute("CREATE TABLE IF NOT EXISTS envios (" +
                    "id INT AUTO_INCREMENT PRIMARY KEY, " +
                    "id_orden INT, " +
                    "codigo_seguimiento VARCHAR(50), " +
                    "direccion VARCHAR(150), " +
                    "provincia VARCHAR(60), " +
                    "ciudad VARCHAR(60), " +
                    "codigo_postal VARCHAR(20), " +
                    "tipo_envio VARCHAR(30), " +
                    "estado VARCHAR(30) DEFAULT 'PENDIENTE', " +
                    "costo DOUBLE DEFAULT 0)");
        } catch (SQLException e) {}
    }

    @Override
    public Envio guardar(Envio envio) {
        String sql = "INSERT INTO envios (id_orden, codigo_seguimiento, direccion, provincia, ciudad, codigo_postal, tipo_envio, estado, costo) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try (Connection con = conexion.conectar(); PreparedStatement ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setInt(1, envio.getIdOrden());
            ps.setString(2, envio.getCodigoSeguimiento());
            ps.setString(3, envio.getDireccion());
            ps.setString(4, envio.getProvincia());
            ps.setString(5, envio.getCiudad());
            ps.setString(6, envio.getCodigoPostal());
            ps.setString(7, envio.getTipoEnvio().name());
            ps.setString(8, envio.getEstado().name());
            ps.setDouble(9, envio.getCosto());
            ps.executeUpdate();
            ResultSet rs = ps.getGeneratedKeys();
            int id = 0;
            if (rs.next()) id = rs.getInt(1);
            System.out.println("Envio #" + id + " registrado hacia: " + envio.getDireccion() + ", " + envio.getCiudad());
            return new Envio(id, envio.getIdOrden(), envio.getCodigoSeguimiento(), envio.getDireccion(), envio.getProvincia(),
                    envio.getCiudad(), envio.getCodigoPostal(), envio.getTipoEnvio(), envio.getEstado(), envio.getCosto());
        } catch (SQLException e) {
            System.out.println("Error al registrar envio: " + e.getMessage());
            return null;
        }
    }

    @Override
    public Envio buscarPorId(int id) throws EnvioNoEncontradoException {
        try (Connection con = conexion.conectar(); PreparedStatement ps = con.prepareStatement("SELECT * FROM envios WHERE id=?")) {
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) return mapear(rs);
        } catch (SQLException e) {
            System.out.println("Error consultando envio: " + e.getMessage());
        }
        throw new EnvioNoEncontradoException("No se encontro el envio con id " + id);
    }

    @Override
    public List<Envio> obtenerTodos() {
        List<Envio> lista = new ArrayList<>();
        try (Connection con = conexion.conectar(); Statement st = con.createStatement(); ResultSet rs = st.executeQuery("SELECT * FROM envios")) {
            while (rs.next()) lista.add(mapear(rs));
        } catch (SQLException e) {
            System.out.println("Error listando envios: " + e.getMessage());
        }
        return lista;
    }

    @Override
    public void actualizarEstado(int id, EstadoEnvio nuevoEstado) throws EnvioNoEncontradoException {
        try (Connection con = conexion.conectar(); PreparedStatement ps = con.prepareStatement("UPDATE envios SET estado=? WHERE id=?")) {
            ps.setString(1, nuevoEstado.name());
            ps.setInt(2, id);
            int filas = ps.executeUpdate();
            if (filas == 0) throw new EnvioNoEncontradoException("No se encontro el envio con id " + id);
            System.out.println("Estado del envio actualizado a " + nuevoEstado + ".");
        } catch (SQLException e) {
            System.out.println("Error al actualizar envio: " + e.getMessage());
        }
    }

    @Override
    public int contarPorEstado(EstadoEnvio estado) {
        try (Connection con = conexion.conectar(); PreparedStatement ps = con.prepareStatement("SELECT COUNT(*) FROM envios WHERE estado=?")) {
            ps.setString(1, estado.name());
            ResultSet rs = ps.executeQuery();
            if (rs.next()) return rs.getInt(1);
        } catch (SQLException e) {
            System.out.println("Error contando envios: " + e.getMessage());
        }
        return 0;
    }

    private Envio mapear(ResultSet rs) throws SQLException {
        return new Envio(rs.getInt("id"), rs.getInt("id_orden"), rs.getString("codigo_seguimiento"),
                rs.getString("direccion"), rs.getString("provincia"), rs.getString("ciudad"), rs.getString("codigo_postal"),
                TipoEnvio.valueOf(rs.getString("tipo_envio")), EstadoEnvio.valueOf(rs.getString("estado")), rs.getDouble("costo"));
    }
}
