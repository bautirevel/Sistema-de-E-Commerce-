package com.ecommerce.main;

import java.util.Scanner;
import java.util.ArrayList;
import java.util.List;

import com.ecommerce.dao.*;
import com.ecommerce.dao.impl.*;
import com.ecommerce.model.*;
import com.ecommerce.pagos.*;
import com.ecommerce.exceptions.*;
import com.ecommerce.utils.Validador;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        boolean salir = false;

        ProductoDAO productoDAO = new ProductoDAOImpl();
        UsuarioDAO usuarioDAO = new UsuarioDAOImpl();
        InventarioDAO inventarioDAO = new InventarioDAOImpl();
        OrdenDAO ordenDAO = new OrdenDAOImpl();
        List<ItemCarrito> carrito = new ArrayList<>();

        System.out.println("=== BIENVENIDO AL SISTEMA DE LOGIN ===");
        System.out.println("1. Iniciar sesion como ADMINISTRADOR");
        System.out.println("2. Iniciar sesion como CLIENTE");
        System.out.print("Seleccione una opcion: ");
        int seleccionRol = scanner.nextInt();
        scanner.nextLine();

        Rol rolActual = (seleccionRol == 1) ? Rol.ADMINISTRADOR : Rol.CLIENTE;
        System.out.println("\n[SESION INICIADA]: Bienvenido. Rol activo: " + rolActual);

        while (!salir) {
            System.out.println("\n=== SISTEMA E-COMMERCE ===");
            System.out.println("1. Gestion de Usuarios\n2. Gestion de Roles\n3. Gestion de Productos\n4. Gestion de Categorias\n5. Gestion de Inventario\n6. Carrito de Compras\n7. Ordenes de Compra\n8. Procesamiento de Pagos\n9. Gestion de Envios\n10. Seguimiento de Pedidos\n11. Reclamos y Devoluciones\n12. Reportes\n13. Salir");
            System.out.print("Seleccione una opcion: ");

            int opcion = scanner.nextInt();
            scanner.nextLine();

            try {
                switch (opcion) {
                    case 1:
                        Validador.validarPermiso(rolActual == Rol.ADMINISTRADOR);
                        System.out.println("\n--- GESTION DE USUARIOS ---");
                        System.out.println("1. Ingresar Usuario a BD\n2. Ver Usuarios en BD");
                        System.out.print("Elija opcion: ");
                        int opUser = scanner.nextInt(); scanner.nextLine();
                        if(opUser == 1) {
                            System.out.print("Nombre: "); String nom = scanner.nextLine();
                            System.out.print("Apellido: "); String ape = scanner.nextLine();
                            System.out.print("Email: "); String mail = scanner.nextLine();
                            usuarioDAO.insertar(new Usuario(0, nom, ape, mail, "1234", new java.util.Date(), true, Rol.CLIENTE));
                            System.out.println("Usuario guardado en BD MySQL!");
                        } else if(opUser == 2) usuarioDAO.listarTodos();
                        break;
                    case 3:
                        Validador.validarPermiso(rolActual == Rol.ADMINISTRADOR);
                        System.out.println("\n--- GESTION DE PRODUCTOS ---");
                        System.out.println("1. Ingresar Producto a BD\n2. Ver Productos en BD");
                        System.out.print("Elija opcion: ");
                        int opProd = scanner.nextInt(); scanner.nextLine();
                        if(opProd == 1) {
                            System.out.print("Codigo: "); String cod = scanner.nextLine();
                            System.out.print("Nombre: "); String nom = scanner.nextLine();
                            System.out.print("Precio: "); double prec = scanner.nextDouble(); scanner.nextLine();
                            productoDAO.insertar(new ProductoFisico(cod, nom, prec, 10));
                            System.out.println("Producto guardado en BD MySQL!");
                        } else if(opProd == 2) productoDAO.listarTodos();
                        break;
                    case 6:
                        Validador.validarPermiso(rolActual == Rol.CLIENTE);
                        System.out.println("\n--- CARRITO DE COMPRAS ---");
                        System.out.println("1. Agregar Producto al carrito\n2. Ver carrito y totales");
                        System.out.print("Opcion: ");
                        int opcCar = scanner.nextInt(); scanner.nextLine();
                        if (opcCar == 1) {
                            carrito.add(new ItemCarrito(new ProductoFisico("PROD-01", "Notebook", 1000.0, 10), 1));
                            System.out.println("Producto agregado correctamente al carrito.");
                        } else if (opcCar == 2) {
                            if(carrito.isEmpty()) System.out.println("El carrito esta vacio.");
                            else {
                                double t = 0;
                                for(ItemCarrito i : carrito) { System.out.println(i); t += i.getSubtotal(); }
                                System.out.println("TOTAL: $" + t);
                            }
                        }
                        break;
                    case 7:
                        Validador.validarPermiso(rolActual == Rol.CLIENTE);
                        System.out.println("\n--- ORDENES DE COMPRA ---");
                        if(carrito.isEmpty()) throw new CarritoVacioException("El carrito esta vacio.");
                        
                        double totalOrden = 0;
                        for(ItemCarrito item : carrito) {
                            totalOrden += item.getSubtotal();
                            // DESCUENTA EL STOCK REAL EN MYSQL
                            inventarioDAO.restarStock(item.getProducto().getCodigo(), item.getCantidad());
                        }
                        // GUARDA LA ORDEN REAL EN MYSQL
                        ordenDAO.generarOrden(totalOrden, "CREADA");
                        carrito.clear();
                        System.out.println("¡Compra finalizada! Orden generada en BD y stock actualizado.");
                        break;
                    case 8:
                        Validador.validarPermiso(rolActual == Rol.CLIENTE);
                        System.out.println("\n--- PROCESAMIENTO DE PAGOS ---");
                        System.out.println("1. Tarjeta\n2. Debito\n3. Transferencia\nMetodo: ");
                        int met = scanner.nextInt();
                        ProcesadorPago pago = (met==1) ? new PagoTarjetaCredito() : (met==2) ? new PagoTarjetaDebito() : (met==3) ? new PagoTransferencia() : null;
                        if(pago != null) pago.procesarPago(1000.0); else System.out.println("Invalido.");
                        break;
                    case 13:
                        salir = true; System.out.println("Saliendo...");
                        break;
                    default:
                        System.out.println("Modulo en desarrollo (Solo estructura UML/SQL).");
                        break;
                }
            } catch (Exception e) {
                System.out.println("[ERROR]: " + e.getMessage());
            }
        }
        scanner.close();
    }
}