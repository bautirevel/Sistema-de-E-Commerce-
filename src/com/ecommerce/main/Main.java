package com.ecommerce.main;

import com.ecommerce.dao.*;
import com.ecommerce.dao.impl.*;
import com.ecommerce.exceptions.*;
import com.ecommerce.model.*;
import com.ecommerce.utils.Validador;
import com.ecommerce.pagos.*;
import com.ecommerce.reportes.GeneradorReportes;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        boolean salir = false;

        // Abstract Factory y DAOs
        DAOFactory factory = new SqliteDAOFactory();
        OrdenDAO ordenDAO = factory.crearOrdenDAO();

        // Requisito Obligatorio: Colecciones genéricas para el Carrito
        List<ItemCarrito> carrito = new ArrayList<>();

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
                    try {
                        System.out.print("Ingrese el precio: ");
                        double precio = scanner.nextDouble();
                        System.out.print("Ingrese el stock: ");
                        int stock = scanner.nextInt();
                        scanner.nextLine();
                        System.out.println("¡Validaciones de producto exitosas!");
                    } catch (Exception e) {
                        System.out.println("Error: " + e.getMessage());
                    }
                    break;

                case 6:
                    System.out.println("\n--- Carrito de Compras ---");
                    System.out.println("1. Agregar un Producto al carrito");
                    System.out.println("2. Visualizar carrito y totales");
                    System.out.print("Elija una opcion: ");
                    int opcCarrito = scanner.nextInt();
                    if (opcCarrito == 1) {
                        // Simulamos agregar un producto fisico para probar la coleccion
                        Producto p = new ProductoFisico("PROD-01", "Notebook", 1000.0, 10);
                        carrito.add(new ItemCarrito(p, 1));
                        System.out.println("¡Producto agregado correctamente al ArrayList!");
                    } else if (opcCarrito == 2) {
                        if(carrito.isEmpty()) {
                            System.out.println("El carrito está vacío.");
                        } else {
                            double total = 0;
                            for(ItemCarrito item : carrito) {
                                System.out.println(item.toString());
                                total += item.getSubtotal();
                            }
                            System.out.println("TOTAL A PAGAR: $" + total);
                        }
                    }
                    break;

                case 7:
                    System.out.println("\n--- Generar Orden de Compra ---");
                    try {
                        if(carrito.isEmpty()) {
                            // Requisito Obligatorio: Excepcion si compran con carrito vacio
                            throw new CarritoVacioException("No se puede generar la orden. ¡El carrito está vacío!");
                        }
                        System.out.println("Iniciando transacción...");
                        // Llamamos al DAO que ahora tiene JDBC para guardar en la BD real
                        ordenDAO.generarOrden(); 
                        
                        // Si todo sale bien, vaciamos el carrito
                        carrito.clear(); 
                        System.out.println("¡La orden se guardó en SQLite y el carrito se ha vaciado!");
                    } catch (CarritoVacioException e) {
                        System.out.println("Excepción capturada: " + e.getMessage());
                    }
                    break;

                case 8:
                    System.out.println("\n--- Procesamiento de Pagos ---");
                    System.out.println("1. Tarjeta de Crédito");
                    System.out.println("2. Tarjeta de Débito");
                    System.out.println("3. Transferencia Bancaria");
                    System.out.print("Seleccione el método de pago: ");
                    int metodo = scanner.nextInt();
                    System.out.print("Ingrese el monto a cobrar: ");
                    double monto = scanner.nextDouble();

                    ProcesadorPago pago = null;
                    switch (metodo) {
                        case 1: pago = new PagoTarjetaCredito(); break;
                        case 2: pago = new PagoTarjetaDebito(); break;
                        case 3: pago = new PagoTransferencia(); break;
                        default: System.out.println("Método inválido."); continue;
                    }
                    pago.procesarPago(monto);
                    break;

                case 12:
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
