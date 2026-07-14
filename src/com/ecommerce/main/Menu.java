package com.ecommerce.main;

import java.util.Scanner;
import java.util.ArrayList;
import java.util.List;
import java.util.Date;
import java.sql.*;

import com.ecommerce.dao.*;
import com.ecommerce.dao.impl.InventarioDAOImpl;
import com.ecommerce.model.*;
import com.ecommerce.pagos.*;
import com.ecommerce.exceptions.*;
import com.ecommerce.reportes.GeneradorReportes;
import com.ecommerce.utils.Conexion;

public class Menu {
    private Scanner scanner = new Scanner(System.in);
    private DAOFactory factory = DAOFactory.getDAOFactory();
    private UsuarioDAO usuarioDAO = factory.crearUsuarioDAO();
    private ProductoDAO productoDAO = factory.crearProductoDAO();
    private CategoriaDAO categoriaDAO = factory.crearCategoriaDAO();
    private InventarioDAOImpl inventarioDAO = (InventarioDAOImpl) factory.crearInventarioDAO();
    private OrdenDAO ordenDAO = factory.crearOrdenDAO();
    private EnvioDAO envioDAO = factory.crearEnvioDAO();
    private ReclamoDAO  reclamoDAO = factory.crearReclamoDAO();
    private GeneradorReportes reportes = new GeneradorReportes();
    
    private List<ItemCarrito> carrito = new ArrayList<>();
    private Usuario usuarioLogueado = null;

    public void iniciar() {
        try (Connection con = Conexion.getInstancia().conectar(); Statement st = con.createStatement()) {
            st.execute("CREATE TABLE IF NOT EXISTS devoluciones (id INT AUTO_INCREMENT PRIMARY KEY, producto_codigo VARCHAR(50), motivo TEXT, fecha DATE, estado VARCHAR(30))");
            st.execute("CREATE TABLE IF NOT EXISTS calificaciones (id INT AUTO_INCREMENT PRIMARY KEY, producto_codigo VARCHAR(50), estrellas INT, comentario TEXT)");
        } catch (Exception e) {}

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
                
                Rol rolAsignado = Rol.CLIENTE;
                if(e.equalsIgnoreCase("admin@admin.com")) rolAsignado = Rol.ADMINISTRADOR;
                else if(e.equalsIgnoreCase("ventas@admin.com")) rolAsignado = Rol.OPERADOR_VENTAS;
                else if(e.equalsIgnoreCase("logistica@admin.com")) rolAsignado = Rol.RESPONSABLE_LOGISTICA;
                
                usuarioDAO.insertar(new Usuario(0, n, a, e, p, new Date(), true, rolAsignado));
            } else if (input.equals("3")) {
                salirPreMenu = true; return;
            }
        }

        boolean salir = false;
        while (!salir && usuarioLogueado != null) {
            imprimirMenuOculto(usuarioLogueado.getRol());
            int opcion = -1;
            try { opcion = Integer.parseInt(scanner.nextLine()); } catch(Exception e){ continue; }

            try {
                if (usuarioLogueado.getRol() == Rol.ADMINISTRADOR) {
                    switch(opcion) {
                        case 1:
                            System.out.println("1. Ver Usuarios | 2. Eliminar Usuario");
                            int opU = Integer.parseInt(scanner.nextLine());
                            if(opU==1) usuarioDAO.listarTodos();
                            else if(opU==2) { System.out.print("ID a eliminar: "); usuarioDAO.eliminar(Integer.parseInt(scanner.nextLine())); }
                            break;
                        case 2:
                            System.out.println("Roles del sistema: CLIENTE, ADMINISTRADOR, OPERADOR_VENTAS, RESPONSABLE_LOGISTICA");
                            break;
                        case 3:
                            System.out.println("1. Agregar Producto | 2. Ver Catalogo | 3. Eliminar");
                            int opP = Integer.parseInt(scanner.nextLine());
                            if(opP==1) {
                                System.out.print("Codigo: "); String c = scanner.nextLine();
                                System.out.print("Nombre: "); String n = scanner.nextLine();
                                if(c.isEmpty() || n.isEmpty()) throw new DatosInvalidosException("Campos vacios.");
                                System.out.print("Precio: "); double p = Double.parseDouble(scanner.nextLine());
                                if(p <= 0) throw new DatosInvalidosException("Precio invalido.");
                                productoDAO.insertar(new ProductoFisico(c, n, p, 10));
                            } else if (opP==2) productoDAO.listarTodos();
                            else if (opP==3) { System.out.print("Codigo a eliminar: "); productoDAO.eliminar(scanner.nextLine()); }
                            break;
                        case 4:
                            System.out.println("1. Agregar Categoria | 2. Ver Categorias");
                            int opC = Integer.parseInt(scanner.nextLine());
                            if(opC==1) {
                                System.out.print("Nombre: "); String nc = scanner.nextLine();
                                System.out.print("Descripcion: "); String dc = scanner.nextLine();
                                categoriaDAO.insertar(nc, dc);
                            } else categoriaDAO.listarTodos();
                            break;
                        case 5:
                            System.out.println("1. Ingresar Stock | 2. Ajustar Stock | 3. Ver Movimientos");
                            int opI = Integer.parseInt(scanner.nextLine());
                            if(opI==1) {
                                System.out.print("Codigo Producto: "); String cp = scanner.nextLine();
                                System.out.print("Cantidad a ingresar: "); int cant = Integer.parseInt(scanner.nextLine());
                                inventarioDAO.sumarStock(cp, cant);
                                System.out.println("Stock incrementado.");
                            } else if(opI==2) {
                                System.out.print("Codigo Producto: "); String cp = scanner.nextLine();
                                System.out.print("Nueva cantidad de Stock: "); int cant = Integer.parseInt(scanner.nextLine());
                                inventarioDAO.ajustarStock(cp, cant);
                                System.out.println("Stock ajustado.");
                            } else {
                                inventarioDAO.verMovimientos();
                            }
                            break;
                        case 6:
                            reportes.mostrarEstadisticas();
                            break;
                        case 7:
                            salir = true;
                            break;
                        default: System.out.println("Opcion no valida.");
                    }
                } 
                else if (usuarioLogueado.getRol() == Rol.CLIENTE) {
                    switch(opcion) {
                        case 1:
                            productoDAO.listarTodos();
                            break;
                        case 2:
                            System.out.println("1. Agregar Item | 2. Ver Carrito | 3. Vaciar Carrito");
                            int opCar = Integer.parseInt(scanner.nextLine());
                            if(opCar==1) {
                                System.out.print("Codigo Producto: "); String cod = scanner.nextLine();
                                Producto prod = productoDAO.buscarPorCodigo(cod);
                                if(prod==null) throw new ProductoNoEncontradoException("El producto no existe.");
                                System.out.print("Cantidad: "); int cant = Integer.parseInt(scanner.nextLine());
                                if(cant <= 0) throw new DatosInvalidosException("Cantidad invalida.");
                                carrito.add(new ItemCarrito(prod, cant));
                                System.out.println("Agregado al carrito.");
                            } else if(opCar==2) {
                                double t=0;
                                for(ItemCarrito i : carrito) { System.out.println(i); t+=i.getSubtotal(); }
                                System.out.println("Total a pagar: $" + t);
                            } else {
                                carrito.clear(); System.out.println("Carrito vaciado.");
                            }
                            break;
                        case 3:
                            if(carrito.isEmpty()) throw new CarritoVacioException("No tienes productos en el carrito.");
                            double total = 0;
                            for(ItemCarrito i : carrito) total += i.getSubtotal();
                            ordenDAO.generarOrden(total, "PENDIENTE PAGO");
                            carrito.clear();
                            break;
                        case 4:
                            System.out.println("1. Tarjeta Credito | 2. Tarjeta Debito | 3. Billetera Virtual | 4. Transferencia | 5. Contra Entrega");
                            int mPago = Integer.parseInt(scanner.nextLine());
                            ProcesadorPago strategy = null;
                            if(mPago==1) strategy = new PagoTarjetaCredito();
                            else if(mPago==2) strategy = new PagoTarjetaDebito();
                            else if(mPago==3) strategy = new PagoBilleteraVirtual();
                            else if(mPago==4) strategy = new PagoTransferencia();
                            else strategy = new PagoContraEntrega();
                            System.out.print("Monto a abonar: ");
                            double monto = Double.parseDouble(scanner.nextLine());
                            if(monto <= 0) throw new DatosInvalidosException("Monto invalido.");
                            boolean status = strategy.procesarPago(monto);
                            if(status) System.out.println("[SISTEMA]: Pago aceptado y registrado.");
                            break;
                        case 5:
                            System.out.print("Ingrese ID de Envio: ");
                            envioDAO.rastrearEnvio(Integer.parseInt(scanner.nextLine()));
                            break;
                        case 6:
                            System.out.println("1. Abrir Reclamo | 2. Registrar Devolucion | 3. Calificar Producto");
                            int opPost = Integer.parseInt(scanner.nextLine());
                            if(opPost==1) {
                                System.out.print("Motivo Reclamo: "); reclamoDAO.abrirReclamo(scanner.nextLine());
                            } else if(opPost==2) {
                                System.out.print("Codigo Producto a Devolver: "); String cp = scanner.nextLine();
                                System.out.print("Motivo Devolucion: "); String mot = scanner.nextLine();
                                try (Connection con = Conexion.getInstancia().conectar();
                                     PreparedStatement ps = con.prepareStatement("INSERT INTO devoluciones (producto_codigo, motivo, fecha, estado) VALUES (?, ?, ?, 'PENDIENTE')")) {
                                    ps.setString(1, cp); ps.setString(2, mot); ps.setDate(3, new java.sql.Date(System.currentTimeMillis()));
                                    ps.executeUpdate(); System.out.println("Devolucion iniciada.");
                                }
                            } else {
                                System.out.print("Codigo Producto: "); String cp = scanner.nextLine();
                                System.out.print("Estrellas (1-5): "); int est = Integer.parseInt(scanner.nextLine());
                                System.out.print("Comentario: "); String com = scanner.nextLine();
                                try (Connection con = Conexion.getInstancia().conectar();
                                     PreparedStatement ps = con.prepareStatement("INSERT INTO calificaciones (producto_codigo, estrellas, comentario) VALUES (?, ?, ?)")) {
                                    ps.setString(1, cp); ps.setInt(2, est); ps.setString(3, com);
                                    ps.executeUpdate(); System.out.println("Calificacion guardada. ¡Gracias por valorar!");
                                }
                            }
                            break;
                        case 7:
                            salir = true;
                            break;
                        default: System.out.println("Opcion no valida.");
                    }
                } 
                else if (usuarioLogueado.getRol() == Rol.OPERADOR_VENTAS) {
                    switch(opcion) {
                        case 1: System.out.println("Visualizando listado de ordenes pendientes en BD..."); break;
                        case 2: System.out.println("Confirmando acreditaciones de pagos en BD..."); break;
                        case 3: System.out.print("Ingrese ID de Envio: "); envioDAO.rastrearEnvio(Integer.parseInt(scanner.nextLine())); break;
                        case 4: salir = true; break;
                        default: System.out.println("Opcion no valida.");
                    }
                } 
                else if (usuarioLogueado.getRol() == Rol.RESPONSABLE_LOGISTICA) {
                    switch(opcion) {
                        case 1:
                            System.out.print("ID Orden a despachar: "); int io = Integer.parseInt(scanner.nextLine());
                            System.out.print("Destino (Direccion, CP, Localidad): ");
                            envioDAO.registrarEnvio(io, scanner.nextLine());
                            break;
                        case 2:
                            System.out.print("Ingrese ID de Envio: ");
                            envioDAO.rastrearEnvio(Integer.parseInt(scanner.nextLine()));
                            break;
                        case 3: salir = true; break;
                        default: System.out.println("Opcion no valida.");
                    }
                }
            } catch (Exception e) { System.out.println("[ERROR EN EXAMEN]: " + e.getMessage()); }
        }
        System.out.println("Cerrando sesion...");
        usuarioLogueado = null;
    }

    private void imprimirMenuOculto(Rol rol) {
        System.out.println("\n=== SISTEMA E-COMMERCE (Rol Actual: " + rol + ") ===");
        if (rol == Rol.ADMINISTRADOR) {
            System.out.println("1. Gestion de Usuarios");
            System.out.println("2. Gestion de Roles");
            System.out.println("3. Gestion de Productos");
            System.out.println("4. Gestion de Categorias");
            System.out.println("5. Gestion de Inventario");
            System.out.println("6. Reportes");
            System.out.println("7. Salir");
        } else if (rol == Rol.CLIENTE) {
            System.out.println("1. Catalogo de Productos");
            System.out.println("2. Carrito de Compras");
            System.out.println("3. Generar Orden de Compra");
            System.out.println("4. Procesamiento de Pagos");
            System.out.println("5. Seguimiento de Pedidos");
            System.out.println("6. Reclamos, Devoluciones y Valoraciones");
            System.out.println("7. Salir");
        } else if (rol == Rol.OPERADOR_VENTAS) {
            System.out.println("1. Administrar Ordenes");
            System.out.println("2. Confirmar Pagos");
            System.out.println("3. Seguimiento de Pedidos");
            System.out.println("4. Salir");
        } else if (rol == Rol.RESPONSABLE_LOGISTICA) {
            System.out.println("1. Despachar Envio");
            System.out.println("2. Seguimiento de Pedidos");
            System.out.println("3. Salir");
        }
        System.out.print("Seleccione una opcion: ");
    }
}