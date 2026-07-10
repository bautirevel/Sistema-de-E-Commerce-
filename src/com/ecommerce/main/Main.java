package com.ecommerce.main;

import com.ecommerce.dao.*;
import com.ecommerce.dao.impl.*;
import com.ecommerce.exceptions.*;
import com.ecommerce.model.*;
import com.ecommerce.pagos.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        boolean salir = false;

        DAOFactory factory = new SqliteDAOFactory();
        OrdenDAO ordenDAO = factory.crearOrdenDAO();
        List<ItemCarrito> carrito = new ArrayList<>();

        while (!salir) {
            System.out.println("\n=== SISTEMA E-COMMERCE ===");
            System.out.println("1. Gestión de Usuarios");
            System.out.println("2. Gestión de Roles");
            System.out.println("3. Gestión de Productos");
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
                    System.out.println("Módulo de productos derivado a BD.");
                    break;
                case 6:
                    System.out.println("\n--- Carrito de Compras ---");
                    System.out.println("1. Agregar un Producto al carrito");
                    System.out.println("2. Visualizar carrito y totales");
                    System.out.print("Elija una opcion: ");
                    int opcCarrito = scanner.nextInt();
                    if (opcCarrito == 1) {
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
                        if(carrito.isEmpty()) throw new CarritoVacioException("¡El carrito está vacío!");
                        ordenDAO.generarOrden();
                        carrito.clear();
                        System.out.println("¡Orden guardada en SQLite y carrito vaciado!");
                    } catch (CarritoVacioException e) {
                        System.out.println("Excepción: " + e.getMessage());
                    }
                    break;
                case 8:
                    System.out.println("\n--- Procesamiento de Pagos ---");
                    System.out.println("1. Tarjeta de Crédito\n2. Tarjeta de Débito\n3. Transferencia Bancaria");
                    System.out.print("Seleccione método: ");
                    int metodo = scanner.nextInt();
                    ProcesadorPago pago = null;
                    if(metodo==1) pago = new PagoTarjetaCredito();
                    else if(metodo==2) pago = new PagoTarjetaDebito();
                    else if(metodo==3) pago = new PagoTransferencia();
                    if(pago != null) {
                        pago.procesarPago(1000.0);
                    } else {
                        System.out.println("Método inválido.");
                    }
                    break;
                case 13:
                    salir = true;
                    System.out.println("Saliendo del sistema...");
                    break;
                default:
                    System.out.println("Módulo en desarrollo para esta entrega.");
                    break;
            }
        }
        scanner.close();
    }
}