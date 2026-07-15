package com.ecommerce.dao.impl;

import com.ecommerce.dao.ProductoDAO;
import com.ecommerce.model.*;
import com.ecommerce.exceptions.ProductoDuplicadoException;
import com.ecommerce.utils.Conexion;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ProductoDAOImpl implements ProductoDAO {
    private Conexion conexion = Conexion.getInstancia();

    public ProductoDAOImpl() {
        try (Connection con = conexion.conectar(); Statement st = con.createStatement()) {
            st.execute("CREATE TABLE IF NOT EXISTS productos (" +
                    "codigo VARCHAR(20) PRIMARY KEY, " +
                    "nombre VARCHAR(100), " +
                    "descripcion VARCHAR(255), " +
                    "precio DOUBLE, " +
                    "categoria VARCHAR(50), " +
                    "stock INT, " +
                    "peso DOUBLE DEFAULT 0, " +
                    "estado VARCHAR(20) DEFAULT 'ACTIVO', " +
                    "tipo VARCHAR(20) DEFAULT 'FISICO')");
        } catch (SQLException e) {}
        // Migracion defensiva para bases creadas con el esquema viejo (solo codigo/nombre/precio/stock/categoria).
        agregarColumnaSiNoExiste("descripcion VARCHAR(255)");
        agregarColumnaSiNoExiste("peso DOUBLE DEFAULT 0");
        agregarColumnaSiNoExiste("estado VARCHAR(20) DEFAULT 'ACTIVO'");
        agregarColumnaSiNoExiste("tipo VARCHAR(20) DEFAULT 'FISICO'");
    }

    private void agregarColumnaSiNoExiste(String definicionColumna) {
        try (Connection con = conexion.conectar(); Statement st = con.createStatement()) {
            st.execute("ALTER TABLE productos ADD COLUMN " + definicionColumna);
        } catch (SQLException e) { /* la columna ya existe, se ignora */ }
    }

    @Override
    public void insertar(Producto producto) throws ProductoDuplicadoException {
        if (buscarPorCodigo(producto.getCodigo()) != null) {
            throw new ProductoDuplicadoException("Ya existe un producto con el codigo " + producto.getCodigo());
        }
        String tipo = obtenerTipo(producto);
        try (Connection con = conexion.conectar();
             PreparedStatement ps = con.prepareStatement(
                     "INSERT INTO productos (codigo, nombre, descripcion, precio, categoria, stock, peso, estado, tipo) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)")) {
            ps.setString(1, producto.getCodigo());
            ps.setString(2, producto.getNombre());
            ps.setString(3, producto.getDescripcion());
            ps.setDouble(4, producto.getPrecio());
            ps.setString(5, producto.getCategoria());
            ps.setInt(6, producto.getStock());
            ps.setDouble(7, producto.getPeso());
            ps.setString(8, producto.getEstado().name());
            ps.setString(9, tipo);
            ps.executeUpdate();
            System.out.println("Producto guardado en BD.");
        } catch (SQLException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    @Override
    public void listarTodos() {
        System.out.println("--- CATALOGO DE PRODUCTOS ---");
        for (Producto p : obtenerTodos()) {
            System.out.println("[" + p.getCodigo() + "] " + p.mostrarInformacion()
                    + " | Categoria: " + p.getCategoria() + " | Stock: " + p.getStock()
                    + " | Peso: " + p.getPeso() + " | Estado: " + p.getEstado());
        }
    }

    @Override
    public List<Producto> obtenerTodos() {
        List<Producto> lista = new ArrayList<>();
        try (Connection con = conexion.conectar(); Statement st = con.createStatement(); ResultSet rs = st.executeQuery("SELECT * FROM productos")) {
            while (rs.next()) lista.add(mapear(rs));
        } catch (SQLException e) {
            System.out.println("Error listando productos: " + e.getMessage());
        }
        return lista;
    }

    @Override
    public void eliminar(String codigo) {
        try (Connection con = conexion.conectar(); PreparedStatement ps = con.prepareStatement("DELETE FROM productos WHERE codigo=?")) {
            ps.setString(1, codigo); ps.executeUpdate(); System.out.println("Producto eliminado.");
        } catch (SQLException e) {}
    }

    @Override
    public Producto buscarPorCodigo(String codigo) {
        try (Connection con = conexion.conectar(); PreparedStatement ps = con.prepareStatement("SELECT * FROM productos WHERE codigo=?")) {
            ps.setString(1, codigo);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) return mapear(rs);
        } catch (SQLException e) {}
        return null;
    }

    private String obtenerTipo(Producto producto) {
        if (producto instanceof ProductoDigital) return "DIGITAL";
        if (producto instanceof ProductoImportado) return "IMPORTADO";
        return "FISICO";
    }

    // Reconstruye la subclase correcta (Fisico/Digital/Importado) para no perder el polimorfismo al leer de la BD.
    private Producto mapear(ResultSet rs) throws SQLException {
        String codigo = rs.getString("codigo");
        String nombre = rs.getString("nombre");
        String descripcion = rs.getString("descripcion");
        double precio = rs.getDouble("precio");
        String categoria = rs.getString("categoria");
        int stock = rs.getInt("stock");
        double peso = rs.getDouble("peso");
        EstadoProducto estado = EstadoProducto.valueOf(rs.getString("estado") == null ? "ACTIVO" : rs.getString("estado"));
        String tipo = rs.getString("tipo") == null ? "FISICO" : rs.getString("tipo");

        switch (tipo) {
            case "DIGITAL":
                return new ProductoDigital(codigo, nombre, descripcion, precio, categoria, stock, estado);
            case "IMPORTADO":
                return new ProductoImportado(codigo, nombre, descripcion, precio, categoria, stock, peso, estado);
            default:
                return new ProductoFisico(codigo, nombre, descripcion, precio, categoria, stock, peso, estado);
        }
    }
}
