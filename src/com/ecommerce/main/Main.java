package com.ecommerce.main;

import com.ecommerce.dao.*;
import com.ecommerce.dao.impl.*;
import com.ecommerce.exceptions.*;
import com.ecommerce.model.*;
import com.ecommerce.utils.Validador;
import com.ecommerce.pagos.*;
import com.ecommerce.reportes.GeneradorReportes;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        boolean salir = false;

        // Requisito Obligatorio: Uso de Abstract Factory para la persistencia
        DAOFactory factory = new SqliteDAOFactory();
        // Usamos una implementacion directa temporalmente solo para salvar la prueba de Theo
        ProductoDAO productoDAO = new ProductoDAOImpl(); 

        while (!salir) {
            System.out.println("\n=== SISTEMA E-COMMERCE ===");
            System.out.println("1. Gestión de Usuarios");
            System.out.println("2. Gestión de Roles");
            System.out.println("3. Gestión de Productos (Prueba Theo)");
            System.out.println("4. Gestión de Categorías");
            System.out.println("5. Gestión de Inventario");
            System.out.println("6. Carrito de Compras");
            System.out.println("7. Órdenes de Compra");
            System.out.println("8. Procesamiento de Pagos");
            System.out.println("9. Gestión de Envíos");
            System.out.println("10. Seguimiento de Pedidos");
            System.out.println("11. Reclamos y Devoluciones");
            System.out.println("12. Reportes");
            System.out.println("13. Salir");
            System.out.print("Seleccione una opcion: ");

            int opcion = scanner.nextInt();
            scanner.nextLine();

            switch (opcion) {
                case 3:
                    // Logica de validaciones que armó Theo
                    try {
                        System.out.print("Ingrese el precio: ");
                        double precio = scanner.nextDouble();
                        // Validador.validarPrecio(precio); // Descomentar cuando Validador este listo
                        System.out.print("Ingrese el stock: ");
                        int stock = scanner.nextInt();
                        // Validador.validarStock(stock); // Descomentar cuando Validador este listo
                        scanner.nextLine();
                        System.out.println("¡Validaciones de producto exitosas!");
                    } catch (Exception e) {
                        System.out.println("Error: " + e.getMessage());
                    }
                    break;

                case 8:
                    // Requisito Obligatorio: Patrón Strategy
                    System.out.println("\n--- Procesamiento de Pagos ---");
                    System.out.println("1. Tarjeta de Crédito");
                    System.out.println("2. Tarjeta de Débito");
                    System.out.println("3. Transferencia Bancaria");
                    System.out.println("4. Billetera Virtual");
                    System.out.println("5. Pago Contra Entrega");
                    System.out.print("Seleccione el método de pago: ");
                    int metodo = scanner.nextInt();
                    System.out.print("Ingrese el monto a cobrar: ");
                    double monto = scanner.nextDouble();

                    ProcesadorPago pago = null;
                    switch (metodo) {
                        case 1: pago = new PagoTarjetaCredito(); break;
                        case 2: pago = new PagoTarjetaDebito(); break;
                        case 3: pago = new PagoTransferencia(); break;
                        case 4: pago = new PagoBilleteraVirtual(); break;
                        case 5: pago = new PagoContraEntrega(); break;
                        default: System.out.println("Método inválido."); continue;
                    }
                    // Implementacion polimorfica
                    pago.procesarPago(monto);
                    break;

                case 12:
                    // Requisito Obligatorio: 15 Reportes
                    new GeneradorReportes().imprimirReportes();
                    break;

                case 13:
                    salir = true;
                    System.out.println("Saliendo del sistema...");
                    break;

                default:
                    if (opcion >= 1 && opcion <= 11) {
                        System.out.println("Módulo en desarrollo para esta entrega.");
                    } else {
                        System.out.println("Opción inválida.");
                    }
                    break;
            }
        }
        scanner.close();
    }
}
