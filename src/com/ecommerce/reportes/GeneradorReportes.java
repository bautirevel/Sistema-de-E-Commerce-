package com.ecommerce.reportes;

import com.ecommerce.utils.Conexion;
import java.sql.*;

public class GeneradorReportes {
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
            try (Statement st = con.createStatement();
                 ResultSet rs = st.executeQuery("SELECT estado, COUNT(*) AS cantidad FROM ordenes GROUP BY estado")) {
                while (rs.next()) System.out.println("     " + rs.getString("estado") + ": " + rs.getInt("cantidad"));
            } catch (SQLException e) { System.out.println("     (sin datos)"); }

            try (Statement st = con.createStatement();
                 ResultSet rs = st.executeQuery("SELECT SUM(monto) AS total FROM pagos WHERE estado = 'APROBADO'")) {
                if (rs.next()) System.out.println("-> Recaudacion total: $" + rs.getDouble("total"));
            } catch (SQLException e) { System.out.println("-> Recaudacion total: $0"); }

            System.out.println("-> Recaudacion por metodo de pago:");
            try (Statement st = con.createStatement();
                 ResultSet rs = st.executeQuery("SELECT metodo, SUM(monto) AS total FROM pagos WHERE estado = 'APROBADO' GROUP BY metodo")) {
                while (rs.next()) System.out.println("     " + rs.getString("metodo") + ": $" + rs.getDouble("total"));
            } catch (SQLException e) { System.out.println("     (sin datos)"); }

            System.out.println("-> Clientes con mas compras:");
            try (Statement st = con.createStatement();
                 ResultSet rs = st.executeQuery("SELECT cliente_email, COUNT(*) AS cantidad FROM ordenes GROUP BY cliente_email ORDER BY cantidad DESC LIMIT 5")) {
                while (rs.next()) System.out.println("     " + rs.getString("cliente_email") + ": " + rs.getInt("cantidad") + " compras");
            } catch (SQLException e) { System.out.println("     (sin datos)"); }

            contarSimple(con, "-> Reclamos abiertos: ", "SELECT COUNT(*) FROM reclamos WHERE estado = 'ABIERTO'");
            contarSimple(con, "-> Reclamos resueltos: ", "SELECT COUNT(*) FROM reclamos WHERE estado = 'RESUELTO'");
            contarSimple(con, "-> Envios pendientes: ", "SELECT COUNT(*) FROM envios WHERE estado = 'PENDIENTE'");
            contarSimple(con, "-> Envios entregados: ", "SELECT COUNT(*) FROM envios WHERE estado = 'ENTREGADO'");

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
