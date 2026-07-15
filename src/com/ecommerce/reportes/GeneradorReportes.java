package com.ecommerce.reportes;

import com.ecommerce.dao.DAOFactory;
import com.ecommerce.dao.EnvioDAO;
import com.ecommerce.dao.OrdenDAO;
import com.ecommerce.dao.PagoDAO;
import com.ecommerce.model.EstadoEnvio;
import com.ecommerce.utils.Conexion;
import java.sql.*;
import java.util.Map;

public class GeneradorReportes {
    private PagoDAO pagoDAO = DAOFactory.getDAOFactory().crearPagoDAO();
    private EnvioDAO envioDAO = DAOFactory.getDAOFactory().crearEnvioDAO();
    private OrdenDAO ordenDAO = DAOFactory.getDAOFactory().crearOrdenDAO();

    public void mostrarEstadisticas() {
        System.out.println("\n==========================================");
        System.out.println("      REPORTES DE GESTION DA VINCI        ");
        System.out.println("==========================================");
        try (Connection con = Conexion.getInstancia().conectar()) {

            contarSimple(con, "-> Cantidad total de usuarios: ", "SELECT COUNT(*) FROM usuarios");
            contarSimple(con, "-> Cantidad de clientes: ", "SELECT COUNT(*) FROM usuarios WHERE rol = 'CLIENTE'");
            contarSimple(con, "-> Cantidad de productos en catalogo: ", "SELECT COUNT(*) FROM productos");

            System.out.println("-> Productos por categoria:");
            try (Statement st = con.createStatement();
                 ResultSet rs = st.executeQuery("SELECT categoria, COUNT(*) AS cantidad FROM productos GROUP BY categoria")) {
                while (rs.next()) System.out.println("     " + rs.getString("categoria") + ": " + rs.getInt("cantidad"));
            } catch (SQLException e) { System.out.println("     (sin datos)"); }

            contarSimple(con, "-> Productos sin stock: ", "SELECT COUNT(*) FROM productos WHERE stock = 0");

            System.out.println("-> Productos mas vendidos:");
            try (Statement st = con.createStatement();
                 ResultSet rs = st.executeQuery(
                         "SELECT producto_codigo, SUM(cantidad) AS total_vendido FROM orden_items GROUP BY producto_codigo ORDER BY total_vendido DESC LIMIT 5")) {
                while (rs.next()) System.out.println("     " + rs.getString("producto_codigo") + ": " + rs.getInt("total_vendido") + " unidades");
            } catch (SQLException e) { System.out.println("     (sin datos)"); }

            contarSimple(con, "-> Ordenes generadas: ", "SELECT COUNT(*) FROM ordenes");

            System.out.println("-> Ordenes por estado:");
            Map<String, Integer> ordenesPorEstado = ordenDAO.contarPorEstado();
            if (ordenesPorEstado.isEmpty()) System.out.println("     (sin datos)");
            for (Map.Entry<String, Integer> entry : ordenesPorEstado.entrySet()) {
                System.out.println("     " + entry.getKey() + ": " + entry.getValue());
            }

            System.out.println("-> Recaudacion total: $" + pagoDAO.recaudacionTotal());

            System.out.println("-> Recaudacion por metodo de pago:");
            Map<String, Double> recaudacionPorMetodo = pagoDAO.recaudacionPorMetodo();
            if (recaudacionPorMetodo.isEmpty()) System.out.println("     (sin datos)");
            for (Map.Entry<String, Double> entry : recaudacionPorMetodo.entrySet()) {
                System.out.println("     " + entry.getKey() + ": $" + entry.getValue());
            }

            System.out.println("-> Clientes con mas compras:");
            Map<String, Integer> clientesTop = ordenDAO.clientesConMasCompras(5);
            if (clientesTop.isEmpty()) System.out.println("     (sin datos)");
            for (Map.Entry<String, Integer> entry : clientesTop.entrySet()) {
                System.out.println("     " + entry.getKey() + ": " + entry.getValue() + " compras");
            }

            contarSimple(con, "-> Reclamos abiertos: ", "SELECT COUNT(*) FROM reclamos WHERE estado = 'ABIERTO'");
            contarSimple(con, "-> Reclamos resueltos: ", "SELECT COUNT(*) FROM reclamos WHERE estado = 'RESUELTO'");

            System.out.println("-> Envios pendientes: " + envioDAO.contarPorEstado(EstadoEnvio.PENDIENTE));
            System.out.println("-> Envios entregados: " + envioDAO.contarPorEstado(EstadoEnvio.ENTREGADO));

        } catch (SQLException e) {
            System.out.println("Error ejecutando reportes: " + e.getMessage());
        }
        System.out.println("==========================================\n");
    }

    private void contarSimple(Connection con, String etiqueta, String sql) {
        try (Statement st = con.createStatement(); ResultSet rs = st.executeQuery(sql)) {
            if (rs.next()) System.out.println(etiqueta + rs.getInt(1));
        } catch (SQLException e) {
            System.out.println(etiqueta + "0");
        }
    }
}
