package com.ecommerce.dao.impl;

import com.ecommerce.dao.PagoDAO;
import com.ecommerce.model.Pago;
import com.ecommerce.model.EstadoPago;
import com.ecommerce.utils.Conexion;
import java.sql.*;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class PagoDAOImpl implements PagoDAO {
    private Conexion conexion = Conexion.getInstancia();

    public PagoDAOImpl() {
        try (Connection con = conexion.conectar(); Statement st = con.createStatement()) {
            st.execute("CREATE TABLE IF NOT EXISTS pagos (" +
                    "id INT AUTO_INCREMENT PRIMARY KEY, " +
                    "id_orden INT, " +
                    "metodo VARCHAR(40), " +
                    "monto DOUBLE, " +
                    "estado VARCHAR(20), " +
                    "fecha TIMESTAMP DEFAULT CURRENT_TIMESTAMP)");
        } catch (SQLException e) {}
    }

    @Override
    public Pago guardar(Pago pago) {
        try (Connection con = conexion.conectar();
             PreparedStatement ps = con.prepareStatement("INSERT INTO pagos (id_orden, metodo, monto, estado) VALUES (?, ?, ?, ?)", Statement.RETURN_GENERATED_KEYS)) {
            ps.setInt(1, pago.getIdOrden());
            ps.setString(2, pago.getMetodo());
            ps.setDouble(3, pago.getMonto());
            ps.setString(4, pago.getEstado().name());
            ps.executeUpdate();
            ResultSet rs = ps.getGeneratedKeys();
            int id = 0;
            if (rs.next()) id = rs.getInt(1);
            return new Pago(id, pago.getIdOrden(), pago.getMetodo(), pago.getMonto(), pago.getEstado(), new java.util.Date());
        } catch (SQLException e) {
            System.out.println("Error al registrar pago: " + e.getMessage());
            return null;
        }
    }

    @Override
    public List<Pago> obtenerTodos() {
        List<Pago> lista = new ArrayList<>();
        try (Connection con = conexion.conectar(); Statement st = con.createStatement(); ResultSet rs = st.executeQuery("SELECT * FROM pagos ORDER BY fecha DESC")) {
            while (rs.next()) {
                lista.add(new Pago(rs.getInt("id"), rs.getInt("id_orden"), rs.getString("metodo"),
                        rs.getDouble("monto"), EstadoPago.valueOf(rs.getString("estado")), rs.getTimestamp("fecha")));
            }
        } catch (SQLException e) {
            System.out.println("Error listando pagos: " + e.getMessage());
        }
        return lista;
    }

    @Override
    public double recaudacionTotal() {
        try (Connection con = conexion.conectar(); PreparedStatement ps = con.prepareStatement("SELECT SUM(monto) FROM pagos WHERE estado='APROBADO'")) {
            ResultSet rs = ps.executeQuery();
            if (rs.next()) return rs.getDouble(1);
        } catch (SQLException e) {
            System.out.println("Error calculando recaudacion: " + e.getMessage());
        }
        return 0;
    }

    @Override
    public Map<String, Double> recaudacionPorMetodo() {
        Map<String, Double> mapa = new LinkedHashMap<>();
        try (Connection con = conexion.conectar();
             PreparedStatement ps = con.prepareStatement("SELECT metodo, SUM(monto) AS total FROM pagos WHERE estado='APROBADO' GROUP BY metodo")) {
            ResultSet rs = ps.executeQuery();
            while (rs.next()) mapa.put(rs.getString("metodo"), rs.getDouble("total"));
        } catch (SQLException e) {
            System.out.println("Error calculando recaudacion por metodo: " + e.getMessage());
        }
        return mapa;
    }
}
