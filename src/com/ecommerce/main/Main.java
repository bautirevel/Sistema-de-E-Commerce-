package com.ecommerce.main;

import java.util.Scanner;
import java.util.ArrayList;
import java.util.List;

import com.ecommerce.dao.ProductoDAO;
import com.ecommerce.dao.impl.ProductoDAOImpl;
import com.ecommerce.dao.UsuarioDAO;
import com.ecommerce.dao.impl.UsuarioDAOImpl;
import com.ecommerce.model.Producto;
import com.ecommerce.model.ProductoFisico;
import com.ecommerce.model.Usuario;
import com.ecommerce.model.Rol;
import com.ecommerce.model.ItemCarrito;
import com.ecommerce.pagos.ProcesadorPago;
import com.ecommerce.pagos.PagoTarjetaCredito;
import com.ecommerce.pagos.PagoTarjetaDebito;
import com.ecommerce.pagos.PagoTransferencia;
import com.ecommerce.exceptions.CarritoVacioException;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        boolean salir = false;

        ProductoDAO productoDAO = new ProductoDAOImpl();
        UsuarioDAO usuarioDAO = new UsuarioDAOImpl();
        List<ItemCarrito> carrito = new ArrayList<>();

        while (!salir) {
            System.out.println("\n=== SISTEMA E-COMMERCE ===");
            System.out.println("1. Gestion de Usuarios");
            System.out.println("2. Gestion de Roles");
            System.out.println("3. Gestion de Productos");
            System.out.println("4. Gestion de Categorias");
            System.out.println("5. Gestion de Inventario");
            System.out.println("6. Carrito de Compras");
            System.out.println("7. Ordenes de Compra");
            System.out.println("8. Procesamiento de Pagos");
            System.out.println("9. Gestion de Envios");
            System.out.println("10. Seguimiento de Pedidos");
            System.out.println("11. Reclamos y Devoluciones");
            System.out.println("12. Reportes");
            System.out.println("13. Salir");
            System.out.print("Seleccione una opcion: ");

            int opcion = scanner.nextInt();
            scanner.nextLine();

            switch (opcion) {
                case 1:
                    System.out.println("\n--- GESTION DE USUARIOS ---");
                    System.out.println("1. Ingresar Usuario a BD");
                    System.out.println("2. Ver Usuarios en BD");
                    System.out.print("Elija opcion: ");
                    int opUser = scanner.nextInt();
                    scanner.nextLine();
                    if(opUser == 1) {
                        System.out.print("Nombre: "); String nom = scanner.nextLine();
                        System.out.print("Apellido: "); String ape = scanner.nextLine();
                        System.out.print("Email: "); String mail = scanner.nextLine();
                        Usuario nuevoUser = new Usuario(0, nom, ape, mail, "1234", new java.util.Date(), true, Rol.CLIENTE);
                        usuarioDAO.insertar(nuevoUser);
                        System.out.println("ÂUsuario guardado en BD MySQL!");
                    } else if(opUser == 2) {
                        usuarioDAO.listarTodos();
                    }
                    break;
                case 3:
                    System.out.println("\n--- GESTION DE PRODUCTOS ---");
                    System.out.println("1. Ingresar Producto a BD");
                    System.out.println("2. Ver Productos en BD");
                    System.out.print("Elija opcion: ");
                    int opProd = scanner.nextInt();
                    scanner.nextLine();
                    if(opProd == 1) {
                        System.out.print("Codigo: "); String cod = scanner.nextLine();
                        System.out.print("Nombre: "); String nom = scanner.nextLine();
                        System.out.print("Precio: "); double prec = scanner.nextDouble(); scanner.nextLine();
                        Producto nuevo = new ProductoFisico(cod, nom, prec, 10);
                        productoDAO.insertar(nuevo);
                        System.out.println("ÂProducto guardado en BD MySQL!");
                    } else if(opProd == 2) {
                        productoDAO.listarTodos();
                    }
                    break;
                case 6:
                    System.out.println("\n--- CARRITO DE COMPRAS ---");
                    System.out.println("1. Agregar Producto (Memoria)");
                    System.out.println("2. Ver carrito");
                    System.out.print("Opcion: ");
                    int opcCar = scanner.nextInt(); scanner.nextLine();
                    if (opcCar == 1) {
                        Producto p = new ProductoFisico("PROD-01", "Notebook", 1000.0, 10);
                        carrito.add(new ItemCarrito(p, 1));
                        System.out.println("Producto agregado correctamente al ArrayList.");
                    } else if (opcCar == 2) {
                        if(carrito.isEmpty()) System.out.println("El carrito esta vacio.");
                        else {
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
                    System.out.println("\n--- ORDENES DE COMPRA ---");
                    try {
                        if(carrito.isEmpty()) throw new CarritoVacioException("El carrito esta vacio.");
                        carrito.clear();
                        System.out.println("Orden procesada correctamente y carrito vaciado.");
                    } catch (CarritoVacioException e) {
                        System.out.println("Excepcion atrapada: " + e.getMessage());
                    }
                    break;
                case 8:
                    System.out.println("\n--- PROCESAMIENTO DE PAGOS ---");
                    System.out.println("1. Tarjeta de Credito\n2. Tarjeta de Debito\n3. Transferencia Bancaria");
                    System.out.print("Seleccione metodo: ");
                    int met = scanner.nextInt();
                    ProcesadorPago pago = null;
                    if(met==1) pago = new PagoTarjetaCredito();
                    else if(met==2) pago = new PagoTarjetaDebito();
                    else if(met==3) pago = new PagoTransferencia();
                    
                    if(pago != null) pago.procesarPago(1000.0);
                    else System.out.println("Metodo invalido.");
                    break;
                case 13:
                    salir = true;
                    System.out.println("Saliendo del sistema...");
                    break;
                default:
                    System.out.println("Modulo en desarrollo para esta entrega.");
                    break;
            }
        }
        scanner.close();
    }
}