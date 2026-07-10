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
import com.ecommerce.exceptions.PermisoDenegadoException;
import com.ecommerce.utils.Validador;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        boolean salir = false;

        ProductoDAO productoDAO = new ProductoDAOImpl();
        UsuarioDAO usuarioDAO = new UsuarioDAOImpl();
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

            try {
                switch (opcion) {
                    case 1: // Gestion de Usuarios (Solo Admin)
                        Validador.validarPermiso(rolActual == Rol.ADMINISTRADOR);
                        System.out.println("\n--- GESTION DE USUARIOS ---");
                        System.out.println("1. Ingresar Usuario a BD");
                        System.out.println("2. Ver Usuarios en BD");
                        System.out.print("Elija opcion: ");
                        int opUser = scanner.nextInt(); scanner.nextLine();
                        if(opUser == 1) {
                            System.out.print("Nombre: "); String nom = scanner.nextLine();
                            System.out.print("Apellido: "); String ape = scanner.nextLine();
                            System.out.print("Email: "); String mail = scanner.nextLine();
                            Usuario nuevoUser = new Usuario(0, nom, ape, mail, "1234", new java.util.Date(), true, Rol.CLIENTE);
                            usuarioDAO.insertar(nuevoUser);
                            System.out.println("Usuario guardado en BD MySQL!");
                        } else if(opUser == 2) {
                            usuarioDAO.listarTodos();
                        }
                        break;

                    case 2: // Gestion de Roles (Solo Admin)
                    case 4: // Categorias (Solo Admin)
                    case 5: // Inventario (Solo Admin)
                    case 12: // Reportes (Solo Admin)
                        Validador.validarPermiso(rolActual == Rol.ADMINISTRADOR);
                        System.out.println("Modulo de Administrador autorizado. En desarrollo para esta entrega.");
                        break;

                    case 3: // Gestion de Productos (Solo Admin)
                        Validador.validarPermiso(rolActual == Rol.ADMINISTRADOR);
                        System.out.println("\n--- GESTION DE PRODUCTOS ---");
                        System.out.println("1. Ingresar Producto a BD");
                        System.out.println("2. Ver Productos en BD");
                        System.out.print("Elija opcion: ");
                        int opProd = scanner.nextInt(); scanner.nextLine();
                        if(opProd == 1) {
                            System.out.print("Codigo: "); String cod = scanner.nextLine();
                            System.out.print("Nombre: "); String nom = scanner.nextLine();
                            System.out.print("Precio: "); double prec = scanner.nextDouble(); scanner.nextLine();
                            Producto nuevo = new ProductoFisico(cod, nom, prec, 10);
                            productoDAO.insertar(nuevo);
                            System.out.println("Producto guardado en BD MySQL!");
                        } else if(opProd == 2) {
                            productoDAO.listarTodos();
                        }
                        break;

                    case 6: // Carrito de Compras (Solo Cliente)
                        Validador.validarPermiso(rolActual == Rol.CLIENTE);
                        System.out.println("\n--- CARRITO DE COMPRAS ---");
                        System.out.println("1. Agregar Producto al carrito");
                        System.out.println("2. Ver carrito y totales");
                        System.out.print("Opcion: ");
                        int opcCar = scanner.nextInt(); scanner.nextLine();
                        if (opcCar == 1) {
                            Producto p = new ProductoFisico("PROD-01", "Notebook", 1000.0, 10);
                            carrito.add(new ItemCarrito(p, 1));
                            System.out.println("Producto agregado correctamente al carrito en memoria.");
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

                    case 7: // Ordenes de Compra (Solo Cliente)
                        Validador.validarPermiso(rolActual == Rol.CLIENTE);
                        System.out.println("\n--- ORDENES DE COMPRA ---");
                        if(carrito.isEmpty()) throw new CarritoVacioException("El carrito esta vacio.");
                        carrito.clear();
                        System.out.println("Orden procesada correctamente y carrito vaciado.");
                        break;

                    case 8: // Procesamiento de Pagos (Solo Cliente)
                        Validador.validarPermiso(rolActual == Rol.CLIENTE);
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

                    case 9:  // Envios
                    case 10: // Seguimiento
                    case 11: // Reclamos
                        Validador.validarPermiso(rolActual == Rol.CLIENTE);
                        System.out.println("Modulo de Cliente autorizado. En desarrollo para esta entrega.");
                        break;

                    case 13:
                        salir = true;
                        System.out.println("Saliendo del sistema...");
                        break;
                    default:
                        System.out.println("Opcion incorrecta en el menu.");
                        break;
                }
            } catch (PermisoDenegadoException e) {
                System.out.println("[ERROR DE SEGURIDAD]: " + e.getMessage() + " Tu rol de " + rolActual + " no tiene permisos para esta opcion.");
            } catch (CarritoVacioException e) {
                System.out.println("[ERROR DE PROCESO]: " + e.getMessage());
            }
        }
        scanner.close();
    }
}