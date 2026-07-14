package com.ecommerce.main;

import java.util.Scanner;
import java.util.ArrayList;
import java.util.List;
import java.util.Date;

import com.ecommerce.dao.impl.*;
import com.ecommerce.model.*;
import com.ecommerce.pagos.*;
import com.ecommerce.exceptions.*;
import com.ecommerce.reportes.GeneradorReportes;

public class Menu {
    private Scanner scanner = new Scanner(System.in);
    private UsuarioDAOImpl usuarioDAO = new UsuarioDAOImpl();
    private ProductoDAOImpl productoDAO = new ProductoDAOImpl();
    private CategoriaDAOImpl categoriaDAO = new CategoriaDAOImpl();
    private InventarioDAOImpl inventarioDAO = new InventarioDAOImpl();
    private OrdenDAOImpl ordenDAO = new OrdenDAOImpl();
    private EnvioDAOImpl envioDAO = new EnvioDAOImpl();
    private ReclamoDAOImpl reclamoDAO = new ReclamoDAOImpl();
    private GeneradorReportes reportes = new GeneradorReportes();
    private List<ItemCarrito> carrito = new ArrayList<>();
    private Usuario usuarioLogueado = null;

    public void iniciar() {
        boolean salirPreMenu = false;
        while (usuarioLogueado == null && !salirPreMenu) {
            System.out.println("\n==================================");
            System.out.println("   SISTEMA DE E-COMMERCE DA VINCI   ");
            System.out.println("==================================");
            System.out.println("1. Iniciar Sesion\n2. Registrar Nuevo Usuario\n3. Salir");
            System.out.print("Opcion: ");
            String input = scanner.nextLine();
            if(input.equals("1")) {
                System.out.print("Email: "); String email = scanner.nextLine();
                System.out.print("Contrasena: "); String pass = scanner.nextLine();
                usuarioLogueado = usuarioDAO.login(email, pass);
                if (usuarioLogueado == null) System.out.println("[ERROR]: Credenciales incorrectas.");
                else System.out.println("\n[EXITO]: Bienvenido/a " + usuarioLogueado.getNombre() + " (" + usuarioLogueado.getRol() + ")");
            } else if (input.equals("2")) {
                System.out.print("Nombre: "); String n = scanner.nextLine();
                System.out.print("Apellido: "); String a = scanner.nextLine();
                System.out.print("Email: "); String e = scanner.nextLine();
                System.out.print("Contrasena: "); String p = scanner.nextLine();
                Rol rolAsignado = e.equalsIgnoreCase("admin@admin.com") ? Rol.ADMINISTRADOR : Rol.CLIENTE;
                usuarioDAO.insertar(new Usuario(0, n, a, e, p, new Date(), true, rolAsignado));
            } else if (input.equals("3")) {
                salirPreMenu = true; System.out.println("¡Hasta luego!"); return;
            }
        }

        boolean salir = false;
        while (!salir && usuarioLogueado != null) {
            mostrarOpciones(usuarioLogueado.getRol());
            System.out.print("Seleccione una opcion: "); 
            int opcion = -1;
            try { opcion = Integer.parseInt(scanner.nextLine()); } catch(Exception e){ continue; }

            try {
                if (usuarioLogueado.getRol() == Rol.ADMINISTRADOR) {
                    switch (opcion) {
                        case 1:
                            System.out.println("1. Ver Usuarios | 2. Eliminar"); int opU = Integer.parseInt(scanner.nextLine());
                            if(opU==1) usuarioDAO.listarTodos();
                            else if(opU==2){ System.out.print("ID a eliminar: "); usuarioDAO.eliminar(Integer.parseInt(scanner.nextLine())); }
                            break;
                        case 2: System.out.println("Roles del sistema: CLIENTE, ADMINISTRADOR, OPERADOR_VENTAS, RESPONSABLE_LOGISTICA"); break;
                        case 3:
                            System.out.println("1. Agregar | 2. Ver | 3. Eliminar"); int opP = Integer.parseInt(scanner.nextLine());
                            if(opP==1) {
                                System.out.print("Codigo: "); String c = scanner.nextLine();
                                System.out.print("Nombre: "); String n = scanner.nextLine();
                                System.out.print("Precio: "); double p = Double.parseDouble(scanner.nextLine());
                                productoDAO.insertar(new ProductoFisico(c, n, p, 10)); // Por defecto crea Fisico con 10 de stock
                            } else if (opP==2) productoDAO.listarTodos();
                            else if (opP==3) { System.out.print("Codigo a eliminar: "); productoDAO.eliminar(scanner.nextLine()); }
                            break;
                        case 4:
                            System.out.println("1. Agregar | 2. Ver"); int opC = Integer.parseInt(scanner.nextLine());
                            if(opC==1) {
                                System.out.print("Nombre Categoria: "); String nc = scanner.nextLine();
                                System.out.print("Descripcion: "); String dc = scanner.nextLine();
                                categoriaDAO.insertar(nc, dc);
                            } else categoriaDAO.listarTodos();
                            break;
                        case 5:
                            System.out.print("Codigo Producto: "); String codInv = scanner.nextLine();
                            System.out.print("Cantidad a restar: "); int cant = Integer.parseInt(scanner.nextLine());
                            inventarioDAO.restarStock(codInv, cant); System.out.println("Stock actualizado.");
                            break;
                        case 6: reportes.mostrarEstadisticas(); break;
                        case 7: salir = true; break;
                    }
                } 
                else if (usuarioLogueado.getRol() == Rol.CLIENTE) {
                    switch (opcion) {
                        case 1: productoDAO.listarTodos(); break;
                        case 2:
                            System.out.println("1. Agregar Producto al Carrito | 2. Ver Carrito | 3. Vaciar"); 
                            int opCar = Integer.parseInt(scanner.nextLine());
                            if(opCar==1) { 
                                System.out.print("Ingrese el Codigo del Producto: ");
                                String cod = scanner.nextLine();
                                Producto prodElegido = productoDAO.buscarPorCodigo(cod);
                                if (prodElegido != null) {
                                    System.out.print("Cantidad a llevar: ");
                                    int cantidad = Integer.parseInt(scanner.nextLine());
                                    carrito.add(new ItemCarrito(prodElegido, cantidad));
                                    System.out.println("[EXITO]: Producto agregado al carrito.");
                                } else {
                                    System.out.println("[ERROR]: El producto con codigo '" + cod + "' no existe.");
                                }
                            }
                            else if(opCar==2) { 
                                if(carrito.isEmpty()) { System.out.println("El carrito esta vacio."); }
                                else {
                                    double t=0; 
                                    System.out.println("--- TU CARRITO ---");
                                    for(ItemCarrito i:carrito){ System.out.println(i); t+=i.getSubtotal();} 
                                    System.out.println("TOTAL A PAGAR: $"+t); 
                                }
                            }
                            else if(opCar==3) { carrito.clear(); System.out.println("Carrito vaciado."); }
                            break;
                        case 3:
                            if(carrito.isEmpty()) throw new CarritoVacioException("El carrito esta vacio, no se puede generar orden.");
                            double totalOrden = 0;
                            for(ItemCarrito i:carrito) totalOrden += i.getSubtotal();
                            ordenDAO.generarOrden(totalOrden, "PENDIENTE PAGO");
                            carrito.clear(); 
                            break;
                        case 4: new PagoTarjetaCredito().procesarPago(1500.0); break;
                        case 5: System.out.print("ID de Envio: "); envioDAO.rastrearEnvio(Integer.parseInt(scanner.nextLine())); break;
                        case 6: System.out.print("Motivo del reclamo: "); reclamoDAO.abrirReclamo(scanner.nextLine()); break;
                        case 7: salir = true; break;
                    }
                }
                else if (usuarioLogueado.getRol() == Rol.OPERADOR_VENTAS) {
                    switch(opcion) {
                        case 1: System.out.println("Consultando BD de ordenes..."); break;
                        case 2: System.out.println("Pagos confirmados."); break;
                        case 3: System.out.print("ID de Envio: "); envioDAO.rastrearEnvio(Integer.parseInt(scanner.nextLine())); break;
                        case 4: salir = true; break;
                    }
                }
                else if (usuarioLogueado.getRol() == Rol.RESPONSABLE_LOGISTICA) {
                    switch(opcion) {
                        case 1: System.out.print("ID Orden: "); int io=Integer.parseInt(scanner.nextLine()); System.out.print("Destino: "); envioDAO.registrarEnvio(io, scanner.nextLine()); break;
                        case 2: System.out.print("ID de Envio: "); envioDAO.rastrearEnvio(Integer.parseInt(scanner.nextLine())); break;
                        case 3: salir = true; break;
                    }
                }
            } catch (Exception e) { System.out.println("[ERROR]: " + e.getMessage()); }
        }
        System.out.println("Cerrando sesion...");
    }

    private void mostrarOpciones(Rol rol) {
        System.out.println("\n=== PANEL: " + rol + " ===");
        if (rol == Rol.ADMINISTRADOR) System.out.println("1. Usuarios\n2. Roles\n3. Productos\n4. Categorias\n5. Inventario\n6. Reportes\n7. Salir");
        else if (rol == Rol.CLIENTE) System.out.println("1. Catalogo\n2. Carrito\n3. Ordenes\n4. Pagos\n5. Seguimiento\n6. Reclamos\n7. Salir");
        else if (rol == Rol.OPERADOR_VENTAS) System.out.println("1. Administrar Ordenes\n2. Confirmar Pagos\n3. Seguimiento\n4. Salir");
        else if (rol == Rol.RESPONSABLE_LOGISTICA) System.out.println("1. Despachar Envio\n2. Seguimiento\n3. Salir");
    }
}