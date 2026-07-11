package com.ecommerce.main;

import java.util.Scanner;
import java.util.ArrayList;
import java.util.List;

import com.ecommerce.dao.*;
import com.ecommerce.dao.impl.*;
import com.ecommerce.model.*;
import com.ecommerce.pagos.*;
import com.ecommerce.exceptions.*;
import com.ecommerce.reportes.GeneradorReportes;
import com.ecommerce.utils.Validador;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        boolean salir = false;

        ProductoDAO productoDAO = new ProductoDAOImpl();
        UsuarioDAO usuarioDAO = new UsuarioDAOImpl();
        InventarioDAO inventarioDAO = new InventarioDAOImpl();
        CategoriaDAO categoriaDAO = new CategoriaDAOImpl();
        OrdenDAO ordenDAO = new OrdenDAOImpl();
        GeneradorReportes reportes = new GeneradorReportes();
        List<ItemCarrito> carrito = new ArrayList<>();

        System.out.println("=== BIENVENIDO AL SISTEMA ===");
        System.out.println("1. Entrar como ADMINISTRADOR\n2. Entrar como CLIENTE");
        int seleccionRol = scanner.nextInt(); scanner.nextLine();
        Rol rolActual = (seleccionRol == 1) ? Rol.ADMINISTRADOR : Rol.CLIENTE;
        System.out.println("\n[SESION INICIADA]: " + rolActual);

        while (!salir) {
            System.out.println("\n=== SISTEMA E-COMMERCE ===");
            System.out.println("1. Gestion de Usuarios\n2. Gestion de Roles\n3. Gestion de Productos\n4. Gestion de Categorias\n5. Gestion de Inventario\n6. Carrito de Compras\n7. Ordenes de Compra\n8. Procesamiento de Pagos\n9. Gestion de Envios\n10. Seguimiento de Pedidos\n11. Reclamos y Devoluciones\n12. Reportes\n13. Salir");
            System.out.print("Opcion: "); int opcion = scanner.nextInt(); scanner.nextLine();

            try {
                switch (opcion) {
                    case 1:
                        Validador.validarPermiso(rolActual == Rol.ADMINISTRADOR);
                        System.out.println("1. Agregar | 2. Ver"); int opU = scanner.nextInt(); scanner.nextLine();
                        if(opU==1) {
                            System.out.print("Nombre: "); String n = scanner.nextLine();
                            System.out.print("Apellido: "); String a = scanner.nextLine();
                            System.out.print("Email: "); String m = scanner.nextLine();
                            usuarioDAO.insertar(new Usuario(0, n, a, m, "1234", new java.util.Date(), true, Rol.CLIENTE));
                            System.out.println("¡Usuario guardado!");
                        } else usuarioDAO.listarTodos();
                        break;
                    case 2:
                        Validador.validarPermiso(rolActual == Rol.ADMINISTRADOR);
                        System.out.println("Roles disponibles en el sistema: CLIENTE, ADMINISTRADOR, OPERADOR_VENTAS, RESPONSABLE_LOGISTICA");
                        break;
                    case 3:
                        Validador.validarPermiso(rolActual == Rol.ADMINISTRADOR);
                        System.out.println("1. Agregar | 2. Ver"); int opP = scanner.nextInt(); scanner.nextLine();
                        if(opP==1) {
                            System.out.print("Codigo: "); String c = scanner.nextLine();
                            System.out.print("Nombre: "); String n = scanner.nextLine();
                            System.out.print("Precio: "); double p = scanner.nextDouble(); scanner.nextLine();
                            productoDAO.insertar(new ProductoFisico(c, n, p, 10));
                            System.out.println("¡Producto guardado!");
                        } else productoDAO.listarTodos();
                        break;
                    case 4:
                        Validador.validarPermiso(rolActual == Rol.ADMINISTRADOR);
                        System.out.println("1. Agregar Categoria | 2. Ver Categorias"); int opC = scanner.nextInt(); scanner.nextLine();
                        if(opC==1) {
                            System.out.print("Nombre: "); String nc = scanner.nextLine();
                            System.out.print("Descripcion: "); String dc = scanner.nextLine();
                            categoriaDAO.insertar(nc, dc);
                            System.out.println("¡Categoria guardada en BD!");
                        } else categoriaDAO.listarTodos();
                        break;
                    case 5:
                        Validador.validarPermiso(rolActual == Rol.ADMINISTRADOR);
                        System.out.print("Ingrese Codigo de Producto a modificar: "); String codInv = scanner.nextLine();
                        System.out.print("Cantidad a ingresar (Stock): "); int cant = scanner.nextInt(); scanner.nextLine();
                        inventarioDAO.restarStock(codInv, -cant); // Restar negativo suma stock
                        System.out.println("¡Stock actualizado en BD!");
                        break;
                    case 6:
                        Validador.validarPermiso(rolActual == Rol.CLIENTE);
                        System.out.println("1. Agregar Producto\n2. Ver carrito"); int opCar = scanner.nextInt(); scanner.nextLine();
                        if(opCar==1) { carrito.add(new ItemCarrito(new ProductoFisico("PROD-01", "Notebook", 1000.0, 10), 1)); System.out.println("Agregado."); }
                        else if(opCar==2) { double t=0; for(ItemCarrito i:carrito){ System.out.println(i); t+=i.getSubtotal();} System.out.println("TOTAL: $"+t); }
                        break;
                    case 7:
                        Validador.validarPermiso(rolActual == Rol.CLIENTE);
                        if(carrito.isEmpty()) throw new CarritoVacioException("Carrito vacio.");
                        double totalOrd = 0;
                        for(ItemCarrito i : carrito) { totalOrd += i.getSubtotal(); inventarioDAO.restarStock(i.getProducto().getCodigo(), i.getCantidad()); }
                        ordenDAO.generarOrden(totalOrd, "CREADA");
                        carrito.clear(); System.out.println("Orden generada y stock descontado.");
                        break;
                    case 8:
                        Validador.validarPermiso(rolActual == Rol.CLIENTE);
                        System.out.print("Metodo (1.Tarjeta 2.Debito 3.Transf): "); int met = scanner.nextInt();
                        ProcesadorPago p = (met==1) ? new PagoTarjetaCredito() : (met==2) ? new PagoTarjetaDebito() : (met==3) ? new PagoTransferencia() : null;
                        if(p!=null) p.procesarPago(1000.0);
                        break;
                    case 12:
                        Validador.validarPermiso(rolActual == Rol.ADMINISTRADOR);
                        reportes.mostrarEstadisticas();
                        break;
                    case 13:
                        salir = true; break;
                    default:
                        System.out.println("Opcion reservada para Cliente (Envios, Seguimiento, Reclamos).");
                        break;
                }
            } catch (Exception e) { System.out.println("[ERROR]: " + e.getMessage()); }
        }
    }
}