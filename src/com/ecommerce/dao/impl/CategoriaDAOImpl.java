package com.ecommerce.dao.impl;

import com.ecommerce.dao.CategoriaDAO;
import com.ecommerce.model.Categoria;
import com.ecommerce.exceptions.CategoriaNoEncontradaException;
import com.ecommerce.exceptions.DatosInvalidosException;
import com.ecommerce.utils.Conexion;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CategoriaDAOImpl implements CategoriaDAO {
    private Conexion conexion = Conexion.getInstancia();

    public CategoriaDAOImpl() {
        try (Connection con = conexion.conectar(); Statement st = con.createStatement()) {
            st.execute("CREATE TABLE IF NOT EXISTS categorias (id INT AUTO_INCREMENT PRIMARY KEY, nombre VARCHAR(50), descripcion VARCHAR(100), estado VARCHAR(20) DEFAULT 'ACTIVO')");
        } catch (SQLException e) {}
        try (Connection con = conexion.conectar(); Statement st = con.createStatement()) {
            st.execute("ALTER TABLE categorias ADD COLUMN estado VARCHAR(20) DEFAULT 'ACTIVO'");
        } catch (SQLException e) { /* la columna ya existe, se ignora */ }
    }

    @Override
    public void guardar(Categoria categoria) throws DatosInvalidosException {
        try (Connection con = conexion.conectar(); PreparedStatement psCheck = con.prepareStatement("SELECT id FROM categorias WHERE LOWER(nombre)=LOWER(?)")) {
            psCheck.setString(1, categoria.getNombre());
            ResultSet rsCheck = psCheck.executeQuery();
            if (rsCheck.next()) {
                throw new DatosInvalidosException("Ya existe una categoria con el nombre '" + categoria.getNombre() + "'.");
            }
        } catch (SQLException e) {
            System.out.println("Error verificando categoria existente: " + e.getMessage());
        }
        try (Connection con = conexion.conectar(); PreparedStatement ps = con.prepareStatement("INSERT INTO categorias (nombre, descripcion) VALUES (?, ?)")) {
            ps.setString(1, categoria.getNombre());
            ps.setString(2, categoria.getDescripcion());
            ps.executeUpdate();
            System.out.println("Categoria creada.");
        } catch (SQLException e) {
            System.out.println("Error al crear categoria: " + e.getMessage());
        }
    }

    @Override
    public Categoria buscarPorId(int id) throws CategoriaNoEncontradaException {
        try (Connection con = conexion.conectar(); PreparedStatement ps = con.prepareStatement("SELECT * FROM categorias WHERE id=?")) {
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) return mapear(rs);
        } catch (SQLException e) {
            System.out.println("Error buscando categoria: " + e.getMessage());
        }
        throw new CategoriaNoEncontradaException("No se encontro la categoria con id " + id);
    }

    @Override
    public List<Categoria> obtenerTodas() {
        List<Categoria> lista = new ArrayList<>();
        try (Connection con = conexion.conectar(); Statement st = con.createStatement(); ResultSet rs = st.executeQuery("SELECT * FROM categorias")) {
            while (rs.next()) lista.add(mapear(rs));
        } catch (SQLException e) {
            System.out.println("Error listando categorias: " + e.getMessage());
        }
        return lista;
    }

    @Override
    public void actualizar(int id, String nombre, String descripcion) throws CategoriaNoEncontradaException {
        try (Connection con = conexion.conectar(); PreparedStatement ps = con.prepareStatement("UPDATE categorias SET nombre=?, descripcion=? WHERE id=?")) {
            ps.setString(1, nombre); ps.setString(2, descripcion); ps.setInt(3, id);
            int filas = ps.executeUpdate();
            if (filas == 0) throw new CategoriaNoEncontradaException("No se encontro la categoria con id " + id);
            System.out.println("Categoria actualizada.");
        } catch (SQLException e) {
            System.out.println("Error al actualizar categoria: " + e.getMessage());
        }
    }

    @Override
    public void eliminar(int id) throws CategoriaNoEncontradaException {
        try (Connection con = conexion.conectar(); PreparedStatement ps = con.prepareStatement("UPDATE categorias SET estado='INACTIVO' WHERE id=?")) {
            ps.setInt(1, id);
            int filas = ps.executeUpdate();
            if (filas == 0) throw new CategoriaNoEncontradaException("No se encontro la categoria con id " + id);
            System.out.println("Categoria dada de baja.");
        } catch (SQLException e) {
            System.out.println("Error al dar de baja la categoria: " + e.getMessage());
        }
    }

    @Override
    public void asociarProducto(String codigoProducto, String nombreCategoria) {
        try (Connection con = conexion.conectar(); PreparedStatement ps = con.prepareStatement("UPDATE productos SET categoria=? WHERE codigo=?")) {
            ps.setString(1, nombreCategoria); ps.setString(2, codigoProducto);
            int filas = ps.executeUpdate();
            System.out.println(filas > 0 ? "Producto asociado a la categoria " + nombreCategoria + "." : "[ERROR]: No se encontro el producto.");
        } catch (SQLException e) {
            System.out.println("Error al asociar producto: " + e.getMessage());
        }
    }

    private Categoria mapear(ResultSet rs) throws SQLException {
        return new Categoria(rs.getInt("id"), rs.getString("nombre"), rs.getString("descripcion"), rs.getString("estado"));
    }
}
