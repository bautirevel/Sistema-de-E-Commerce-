package com.ecommerce.dao.impl;

import com.ecommerce.dao.OrdenDAO;
import com.ecommerce.model.*;
import com.ecommerce.exceptions.OrdenNoEncontradaException;
import com.ecommerce.utils.Conexion;
import java.sql.*;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class OrdenDAOImpl implements OrdenDAO {
    private Conexion conexion = Conexion.getInstancia();

    public OrdenDAOImpl() {
        try (Connection con = conexion.conectar(); Statement st = con.createStatement()) {
            st.execute("CREATE TABLE IF NOT EXISTS ordenes (" +
                    "id INT AUTO_INCREMENT PRIMARY KEY, " +
                    "cliente_email VARCHAR(100), " +
                    "fecha TIMESTAMP DEFAULT CURRENT_TIMESTAMP, " +
                    "total DOUBLE, " +
                    "estado VARCHAR(30), " +
                    "id_pago INT NULL, " +
                    "id_envio INT NULL)");
        } catch (SQLException e) {}
        agregarColumnaSiNoExiste("cliente_email VARCHAR(100)");
        agregarColumnaSiNoExiste("id_pago INT NULL");
        agregarColumnaSiNoExiste("id_envio INT NULL");
        try (Connection con = conexion.conectar(); Statement st = con.createStatement()) {
            st.execute("CREATE TABLE IF NOT EXISTS orden_items (" +
                    "id INT AUTO_INCREMENT PRIMARY KEY, " +
                    "id_orden INT, " +
                    "producto_codigo VARCHAR(20), " +
                    "cantidad INT, " +
                    "precio_unitario DOUBLE, " +
                    "subtotal DOUBLE)");
        } catch (SQLException e) {}
    }

    private void agregarColumnaSiNoExiste(String definicionColumna) {
        try (Connection con = conexion.conectar(); Statement st = con.createStatement()) {
            st.execute("ALTER TABLE ordenes ADD COLUMN " + definicionColumna);
        } catch (SQLException e) { /* la columna ya existe, se ignora */ }
    }

    @Override
    public Orden guardar(Orden orden) {
        try (Connection con = conexion.conectar();
             PreparedStatement ps = con.prepareStatement(
                     "INSERT INTO ordenes (cliente_email, total, estado) VALUES (?, ?, ?)", Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, orden.getClienteEmail());
            ps.setDouble(2, orden.getTotal());
            ps.setString(3, orden.getEstado().name());
            ps.executeUpdate();
            ResultSet rs = ps.getGeneratedKeys();
            int id = 0;
            if (rs.next()) id = rs.getInt(1);

            if (orden.getItems() != null) {
                try (PreparedStatement psItem = con.prepareStatement(
                        "INSERT INTO orden_items (id_orden, producto_codigo, cantidad, precio_unitario, subtotal) VALUES (?, ?, ?, ?, ?)")) {
                    for (ItemCarrito item : orden.getItems()) {
                        psItem.setInt(1, id);
                        psItem.setString(2, item.getProducto().getCodigo());
                        psItem.setInt(3, item.getCantidad());
                        psItem.setDouble(4, item.getProducto().calcularPrecioFinal());
                        psItem.setDouble(5, item.getSubtotal());
                        psItem.addBatch();
                    }
                    psItem.executeBatch();
                }
            }
            System.out.println("Orden #" + id + " generada con exito.");
            return new Orden.Builder().setNumero(id).setClienteEmail(orden.getClienteEmail())
                    .setItems(orden.getItems()).setTotal(orden.getTotal()).setEstado(orden.getEstado()).build();
        } catch (SQLException e) {
            System.out.println("Error al generar la orden: " + e.getMessage());
            return null;
        }
    }

    @Override
    public Orden buscarPorId(int id) throws OrdenNoEncontradaException {
        try (Connection con = conexion.conectar(); PreparedStatement ps = con.prepareStatement("SELECT * FROM ordenes WHERE id=?")) {
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) return mapear(rs, con);
        } catch (SQLException e) {
            System.out.println("Error buscando orden: " + e.getMessage());
        }
        throw new OrdenNoEncontradaException("No se encontro la orden #" + id);
    }

    @Override
    public List<Orden> obtenerTodas() {
        List<Orden> lista = new ArrayList<>();
        try (Connection con = conexion.conectar(); Statement st = con.createStatement(); ResultSet rs = st.executeQuery("SELECT * FROM ordenes ORDER BY fecha DESC")) {
            while (rs.next()) lista.add(mapear(rs, con));
        } catch (SQLException e) {
            System.out.println("Error listando ordenes: " + e.getMessage());
        }
        return lista;
    }

    @Override
    public List<Orden> obtenerPorCliente(String email) {
        List<Orden> lista = new ArrayList<>();
        try (Connection con = conexion.conectar(); PreparedStatement ps = con.prepareStatement("SELECT * FROM ordenes WHERE cliente_email=? ORDER BY fecha DESC")) {
            ps.setString(1, email);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) lista.add(mapear(rs, con));
        } catch (SQLException e) {
            System.out.println("Error listando ordenes del cliente: " + e.getMessage());
        }
        return lista;
    }

    @Override
    public void actualizarEstado(int id, EstadoOrden nuevoEstado) throws OrdenNoEncontradaException {
        try (Connection con = conexion.conectar(); PreparedStatement ps = con.prepareStatement("UPDATE ordenes SET estado=? WHERE id=?")) {
            ps.setString(1, nuevoEstado.name());
            ps.setInt(2, id);
            int filas = ps.executeUpdate();
            if (filas == 0) throw new OrdenNoEncontradaException("No se encontro la orden #" + id);
            System.out.println("Orden #" + id + " actualizada a estado " + nuevoEstado + ".");
        } catch (SQLException e) {
            System.out.println("Error al actualizar estado de la orden: " + e.getMessage());
        }
    }

    @Override
    public void asociarPago(int idOrden, int idPago) throws OrdenNoEncontradaException {
        try (Connection con = conexion.conectar(); PreparedStatement ps = con.prepareStatement("UPDATE ordenes SET id_pago=? WHERE id=?")) {
            ps.setInt(1, idPago);
            ps.setInt(2, idOrden);
            int filas = ps.executeUpdate();
            if (filas == 0) throw new OrdenNoEncontradaException("No se encontro la orden #" + idOrden);
        } catch (SQLException e) {
            System.out.println("Error al asociar pago a la orden: " + e.getMessage());
        }
    }

    @Override
    public void asociarEnvio(int idOrden, int idEnvio) throws OrdenNoEncontradaException {
        try (Connection con = conexion.conectar(); PreparedStatement ps = con.prepareStatement("UPDATE ordenes SET id_envio=? WHERE id=?")) {
            ps.setInt(1, idEnvio);
            ps.setInt(2, idOrden);
            int filas = ps.executeUpdate();
            if (filas == 0) throw new OrdenNoEncontradaException("No se encontro la orden #" + idOrden);
        } catch (SQLException e) {
            System.out.println("Error al asociar envio a la orden: " + e.getMessage());
        }
    }

    @Override
    public Map<String, Integer> contarPorEstado() {
        Map<String, Integer> mapa = new LinkedHashMap<>();
        try (Connection con = conexion.conectar(); Statement st = con.createStatement();
             ResultSet rs = st.executeQuery("SELECT estado, COUNT(*) AS cantidad FROM ordenes GROUP BY estado")) {
            while (rs.next()) mapa.put(rs.getString("estado"), rs.getInt("cantidad"));
        } catch (SQLException e) {
            System.out.println("Error agrupando ordenes por estado: " + e.getMessage());
        }
        return mapa;
    }

    @Override
    public Map<String, Integer> clientesConMasCompras(int limite) {
        Map<String, Integer> mapa = new LinkedHashMap<>();
        try (Connection con = conexion.conectar();
             PreparedStatement ps = con.prepareStatement(
                     "SELECT cliente_email, COUNT(*) AS cantidad FROM ordenes GROUP BY cliente_email ORDER BY cantidad DESC LIMIT ?")) {
            ps.setInt(1, limite);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) mapa.put(rs.getString("cliente_email"), rs.getInt("cantidad"));
        } catch (SQLException e) {
            System.out.println("Error obteniendo clientes con mas compras: " + e.getMessage());
        }
        return mapa;
    }

    private Orden mapear(ResultSet rs, Connection con) throws SQLException {
        int id = rs.getInt("id");
        List<ItemCarrito> items = new ArrayList<>();
        try (PreparedStatement psItems = con.prepareStatement("SELECT * FROM orden_items WHERE id_orden=?")) {
            psItems.setInt(1, id);
            ResultSet rsItems = psItems.executeQuery();
            while (rsItems.next()) {
                Producto p = new ProductoFisico(rsItems.getString("producto_codigo"), rsItems.getString("producto_codigo"),
                        rsItems.getDouble("precio_unitario"), 0);
                items.add(new ItemCarrito(p, rsItems.getInt("cantidad"), rsItems.getDouble("precio_unitario")));
            }
        }
        Integer idPago = (Integer) rs.getObject("id_pago");
        Integer idEnvio = (Integer) rs.getObject("id_envio");
        return new Orden.Builder()
                .setNumero(id)
                .setClienteEmail(rs.getString("cliente_email"))
                .setFecha(rs.getTimestamp("fecha"))
                .setItems(items)
                .setTotal(rs.getDouble("total"))
                .setEstado(EstadoOrden.valueOf(rs.getString("estado")))
                .setIdPago(idPago)
                .setIdEnvio(idEnvio)
                .build();
    }
}
