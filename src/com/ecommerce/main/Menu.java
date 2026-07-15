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
import com.ecommerce.utils.Validador;

public class Menu {
    private Scanner scanner = new Scanner(System.in);
    private DAOFactory factory = DAOFactory.getDAOFactory();
    private UsuarioDAO usuarioDAO = factory.crearUsuarioDAO();
    private ProductoDAO productoDAO = factory.crearProductoDAO();
    private CategoriaDAO categoriaDAO = factory.crearCategoriaDAO();
    private InventarioDAOImpl inventarioDAO = (InventarioDAOImpl) factory.crearInventarioDAO();
    private OrdenDAO ordenDAO = factory.crearOrdenDAO();
    private EnvioDAO envioDAO = factory.crearEnvioDAO();
    private ReclamoDAO reclamoDAO = factory.crearReclamoDAO();
    private PagoDAO pagoDAO = factory.crearPagoDAO();
    private GeneradorReportes reportes = new GeneradorReportes();

    private List<ItemCarrito> carrito = new ArrayList<>();
    private Usuario usuarioLogueado = null;
    private Integer ordenPendienteId = null;

    public void iniciar() {
        try (Connection con = Conexion.getInstancia().conectar(); Statement st = con.createStatement()) {
            st.execute("CREATE TABLE IF NOT EXISTS devoluciones (id INT AUTO_INCREMENT PRIMARY KEY, producto_codigo VARCHAR(50), motivo TEXT, fecha DATE, estado VARCHAR(30), cliente_email VARCHAR(100))");
            st.execute("CREATE TABLE IF NOT EXISTS calificaciones (id INT AUTO_INCREMENT PRIMARY KEY, producto_codigo VARCHAR(50), estrellas INT, comentario TEXT)");
        } catch (Exception e) {}
        try (Connection con = Conexion.getInstancia().conectar(); Statement st = con.createStatement()) {
            st.execute("ALTER TABLE devoluciones ADD COLUMN cliente_email VARCHAR(100)");
        } catch (Exception e) { /* la columna ya existe, se ignora */ }

        boolean salirPreMenu = false;
        while (usuarioLogueado == null && !salirPreMenu) {
            System.out.println("\n==================================");
            System.out.println("   SISTEMA DE E-COMMERCE DA VINCI   ");
            System.out.println("==================================");
            System.out.println("1. Iniciar Sesion\n2. Registrar Nuevo Usuario\n3. Salir");
            System.out.print("Opcion: ");
            String input = scanner.nextLine();
            if (input.equals("1")) {
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

                if (n.isEmpty() || a.isEmpty() || e.isEmpty() || p.isEmpty()) {
                    System.out.println("[ERROR]: Ningun campo puede quedar vacio.");
                    continue;
                }

                Rol rolAsignado = Rol.CLIENTE;
                if (e.equalsIgnoreCase("admin@admin.com")) rolAsignado = Rol.ADMINISTRADOR;
                else if (e.equalsIgnoreCase("ventas@admin.com")) rolAsignado = Rol.OPERADOR_VENTAS;
                else if (e.equalsIgnoreCase("logistica@admin.com")) rolAsignado = Rol.RESPONSABLE_LOGISTICA;

                usuarioDAO.insertar(new Usuario(0, n, a, e, p, new Date(), true, rolAsignado));
            } else if (input.equals("3")) {
                salirPreMenu = true; return;
            }
        }

        boolean salir = false;
        while (!salir && usuarioLogueado != null) {
            imprimirMenuOculto(usuarioLogueado.getRol());
            int opcion = -1;
            try { opcion = Integer.parseInt(scanner.nextLine()); } catch (Exception e) { continue; }

            try {
                if (usuarioLogueado.getRol() == Rol.ADMINISTRADOR) {
                    salir = menuAdministrador(opcion);
                } else if (usuarioLogueado.getRol() == Rol.OPERADOR_VENTAS) {
                    salir = menuOperadorVentas(opcion);
                } else if (usuarioLogueado.getRol() == Rol.RESPONSABLE_LOGISTICA) {
                    salir = menuLogistica(opcion);
                } else if (usuarioLogueado.getRol() == Rol.CLIENTE) {
                    salir = menuCliente(opcion);
                }
            } catch (Exception e) {
                System.out.println("[ERROR]: " + e.getMessage());
            }
        }
        System.out.println("Cerrando sesion...");
        usuarioLogueado = null;
    }

    private boolean menuAdministrador(int opcion) throws Exception {
        Validador.validarPermiso(usuarioLogueado.getRol() == Rol.ADMINISTRADOR);
        switch (opcion) {
            case 1:
                System.out.println("1. Ver Usuarios | 2. Eliminar Usuario | 3. Buscar Usuario | 4. Modificar Usuario | 5. Activar/Desactivar Usuario");
                int opU = Integer.parseInt(scanner.nextLine());
                if (opU == 1) usuarioDAO.listarTodos();
                else if (opU == 2) { System.out.print("ID a eliminar: "); usuarioDAO.eliminar(Integer.parseInt(scanner.nextLine())); }
                else if (opU == 3) {
                    System.out.println("Buscar por: 1. ID | 2. Email");
                    int opB = Integer.parseInt(scanner.nextLine());
                    Usuario encontrado;
                    if (opB == 1) { System.out.print("ID: "); encontrado = usuarioDAO.buscarPorId(Integer.parseInt(scanner.nextLine())); }
                    else { System.out.print("Email: "); encontrado = usuarioDAO.buscarPorEmail(scanner.nextLine()); }
                    if (encontrado == null) throw new UsuarioNoEncontradoException("No se encontro el usuario.");
                    System.out.println("ID: " + encontrado.getId() + " | " + encontrado.getNombre() + " " + encontrado.getApellido()
                            + " | Email: " + encontrado.getEmail() + " | Rol: " + encontrado.getRol() + " | Activo: " + encontrado.isEstado());
                } else if (opU == 4) {
                    System.out.print("ID a modificar: "); int idMod = Integer.parseInt(scanner.nextLine());
                    Usuario aModificar = usuarioDAO.buscarPorId(idMod);
                    if (aModificar == null) throw new UsuarioNoEncontradoException("No se encontro el usuario.");
                    System.out.print("Nuevo Nombre (" + aModificar.getNombre() + "): "); String nn = scanner.nextLine();
                    System.out.print("Nuevo Apellido (" + aModificar.getApellido() + "): "); String na = scanner.nextLine();
                    System.out.print("Nuevo Email (" + aModificar.getEmail() + "): "); String ne = scanner.nextLine();
                    if (nn.isEmpty() || na.isEmpty() || ne.isEmpty()) throw new DatosInvalidosException("Los campos no pueden quedar vacios.");
                    Usuario actualizado = new Usuario(idMod, nn, na, ne, aModificar.getContrasena(), aModificar.getFechaAlta(), aModificar.isEstado(), aModificar.getRol());
                    usuarioDAO.actualizar(actualizado);
                } else if (opU == 5) {
                    System.out.print("ID a activar/desactivar: "); int idEst = Integer.parseInt(scanner.nextLine());
                    System.out.println("1. Activar | 2. Desactivar");
                    int opEst = Integer.parseInt(scanner.nextLine());
                    if (opEst == 1) usuarioDAO.activar(idEst); else usuarioDAO.desactivar(idEst);
                }
                return false;
            case 2:
                System.out.println("Roles del sistema: CLIENTE, ADMINISTRADOR, OPERADOR_VENTAS, RESPONSABLE_LOGISTICA");
                return false;
            case 3:
                System.out.println("1. Agregar Producto | 2. Ver Catalogo | 3. Eliminar");
                int opP = Integer.parseInt(scanner.nextLine());
                if (opP == 1) {
                    System.out.print("Codigo: "); String c = scanner.nextLine();
                    System.out.print("Nombre: "); String n = scanner.nextLine();
                    if (c.isEmpty() || n.isEmpty()) throw new DatosInvalidosException("Campos vacios.");
                    System.out.print("Descripcion: "); String desc = scanner.nextLine();
                    System.out.print("Precio: "); double p = Double.parseDouble(scanner.nextLine());
                    if (p <= 0) throw new DatosInvalidosException("Precio invalido.");
                    System.out.print("Categoria: "); String cat = scanner.nextLine();
                    if (cat.isEmpty()) cat = "General";
                    System.out.print("Stock inicial: "); int stockInicial = Integer.parseInt(scanner.nextLine());
                    if (stockInicial < 0) throw new DatosInvalidosException("El stock no puede ser negativo.");
                    System.out.println("Tipo de producto: 1. Fisico | 2. Digital | 3. Importado");
                    int opTipoProd = Integer.parseInt(scanner.nextLine());
                    Producto nuevoProducto;
                    if (opTipoProd == 2) {
                        nuevoProducto = new ProductoDigital(c, n, desc, p, cat, stockInicial, EstadoProducto.ACTIVO);
                    } else {
                        System.out.print("Peso (kg): "); double peso = Double.parseDouble(scanner.nextLine());
                        if (opTipoProd == 3) nuevoProducto = new ProductoImportado(c, n, desc, p, cat, stockInicial, peso, EstadoProducto.ACTIVO);
                        else nuevoProducto = new ProductoFisico(c, n, desc, p, cat, stockInicial, peso, EstadoProducto.ACTIVO);
                    }
                    productoDAO.insertar(nuevoProducto);
                } else if (opP == 2) productoDAO.listarTodos();
                else if (opP == 3) { System.out.print("Codigo a eliminar: "); productoDAO.eliminar(scanner.nextLine()); }
                return false;
            case 4:
                System.out.println("1. Agregar Categoria | 2. Ver Categorias | 3. Modificar Categoria | 4. Eliminar Categoria | 5. Asociar Producto a Categoria");
                int opC = Integer.parseInt(scanner.nextLine());
                if (opC == 1) {
                    System.out.print("Nombre: "); String nc = scanner.nextLine();
                    System.out.print("Descripcion: "); String dc = scanner.nextLine();
                    if (nc.isEmpty()) throw new DatosInvalidosException("El nombre no puede estar vacio.");
                    categoriaDAO.guardar(new Categoria(nc, dc));
                } else if (opC == 2) {
                    for (Categoria cat : categoriaDAO.obtenerTodas()) System.out.println(cat);
                } else if (opC == 3) {
                    System.out.print("ID de la categoria a modificar: "); int idCat = Integer.parseInt(scanner.nextLine());
                    System.out.print("Nuevo Nombre: "); String ncm = scanner.nextLine();
                    System.out.print("Nueva Descripcion: "); String dcm = scanner.nextLine();
                    if (ncm.isEmpty()) throw new DatosInvalidosException("El nombre no puede estar vacio.");
                    categoriaDAO.actualizar(idCat, ncm, dcm);
                } else if (opC == 4) {
                    System.out.print("ID de la categoria a eliminar: "); categoriaDAO.eliminar(Integer.parseInt(scanner.nextLine()));
                } else {
                    System.out.print("Codigo de Producto: "); String codProd = scanner.nextLine();
                    System.out.print("Nombre de la Categoria a asociar: "); String nomCat = scanner.nextLine();
                    categoriaDAO.asociarProducto(codProd, nomCat);
                }
                return false;
            case 5:
                System.out.println("1. Ingresar Stock | 2. Ajustar Stock | 3. Ver Movimientos");
                int opI = Integer.parseInt(scanner.nextLine());
                if (opI == 1) {
                    System.out.print("Codigo Producto: "); String cp = scanner.nextLine();
                    System.out.print("Cantidad a ingresar: "); int cant = Integer.parseInt(scanner.nextLine());
                    inventarioDAO.sumarStock(cp, cant);
                    System.out.println("Stock incrementado.");
                } else if (opI == 2) {
                    System.out.print("Codigo Producto: "); String cp = scanner.nextLine();
                    System.out.print("Nueva cantidad de Stock: "); int cant = Integer.parseInt(scanner.nextLine());
                    inventarioDAO.ajustarStock(cp, cant);
                    System.out.println("Stock ajustado.");
                } else {
                    inventarioDAO.verMovimientos();
                }
                return false;
            case 6:
                reportes.mostrarEstadisticas();
                return false;
            case 7:
                System.out.println("1. Ver Reclamos | 2. Cambiar Estado de Reclamo | 3. Ver Devoluciones | 4. Cambiar Estado de Devolucion");
                int opGR = Integer.parseInt(scanner.nextLine());
                if (opGR == 1) {
                    for (Reclamo r : reclamoDAO.obtenerTodos()) System.out.println(r);
                } else if (opGR == 2) {
                    System.out.print("ID de Reclamo: "); int idRec = Integer.parseInt(scanner.nextLine());
                    System.out.println("Nuevo estado: 1. EN_REVISION | 2. RESUELTO | 3. RECHAZADO");
                    int opEstRec = Integer.parseInt(scanner.nextLine());
                    EstadoReclamo nuevoEstadoRec = opEstRec == 1 ? EstadoReclamo.EN_REVISION : opEstRec == 2 ? EstadoReclamo.RESUELTO : EstadoReclamo.RECHAZADO;
                    reclamoDAO.cambiarEstado(idRec, nuevoEstadoRec);
                } else if (opGR == 3) {
                    try (Connection con = Conexion.getInstancia().conectar();
                         Statement stD = con.createStatement();
                         ResultSet rsD = stD.executeQuery("SELECT * FROM devoluciones ORDER BY fecha DESC")) {
                        System.out.println("--- DEVOLUCIONES ---");
                        while (rsD.next()) {
                            System.out.println("#" + rsD.getInt("id") + " | Cliente: " + rsD.getString("cliente_email")
                                    + " | Producto: " + rsD.getString("producto_codigo") + " | Motivo: " + rsD.getString("motivo")
                                    + " | Estado: " + rsD.getString("estado") + " | Fecha: " + rsD.getDate("fecha"));
                        }
                    }
                } else {
                    System.out.print("ID de Devolucion: "); int idDev = Integer.parseInt(scanner.nextLine());
                    System.out.println("Nuevo estado: 1. EN_REVISION | 2. APROBADA | 3. RECHAZADA");
                    int opEstDev = Integer.parseInt(scanner.nextLine());
                    String nuevoEstadoDev = opEstDev == 1 ? "EN_REVISION" : opEstDev == 2 ? "APROBADA" : "RECHAZADA";
                    try (Connection con = Conexion.getInstancia().conectar();
                         PreparedStatement psDev = con.prepareStatement("UPDATE devoluciones SET estado=? WHERE id=?")) {
                        psDev.setString(1, nuevoEstadoDev); psDev.setInt(2, idDev);
                        int filasDev = psDev.executeUpdate();
                        System.out.println(filasDev > 0 ? "Estado de devolucion actualizado." : "[ERROR]: No se encontro la devolucion.");
                    }
                }
                return false;
            case 8:
                return true;
            default:
                System.out.println("Opcion no valida.");
                return false;
        }
    }

    private boolean menuCliente(int opcion) throws Exception {
        Validador.validarPermiso(usuarioLogueado.getRol() == Rol.CLIENTE);
        switch (opcion) {
            case 1:
                productoDAO.listarTodos();
                return false;
            case 2:
                System.out.println("1. Agregar Item | 2. Ver Carrito | 3. Vaciar Carrito");
                int opCar = Integer.parseInt(scanner.nextLine());
                if (opCar == 1) {
                    System.out.print("Codigo Producto: "); String cod = scanner.nextLine();
                    Producto prod = productoDAO.buscarPorCodigo(cod);
                    if (prod == null) throw new ProductoNoEncontradoException("El producto no existe.");
                    System.out.print("Cantidad: "); int cant = Integer.parseInt(scanner.nextLine());
                    if (cant <= 0) throw new DatosInvalidosException("Cantidad invalida.");
                    if (cant > prod.getStock()) throw new StockInsuficienteException("Stock insuficiente. Disponible: " + prod.getStock());
                    carrito.add(new ItemCarrito(prod, cant));
                    System.out.println("Agregado al carrito.");
                } else if (opCar == 2) {
                    double t = 0;
                    for (ItemCarrito i : carrito) { System.out.println(i); t += i.getSubtotal(); }
                    System.out.println("Total a pagar: $" + t);
                } else {
                    carrito.clear(); System.out.println("Carrito vaciado.");
                }
                return false;
            case 3: {
                if (carrito.isEmpty()) throw new CarritoVacioException("No tienes productos en el carrito.");
                double total = 0;
                for (ItemCarrito i : carrito) {
                    int stockActual = inventarioDAO.consultarStock(i.getProducto().getCodigo());
                    if (stockActual < i.getCantidad()) {
                        throw new StockInsuficienteException("Stock insuficiente para " + i.getProducto().getNombre() + ". Disponible: " + stockActual);
                    }
                    total += i.getSubtotal();
                }
                Orden nuevaOrden = new Orden.Builder()
                        .setClienteEmail(usuarioLogueado.getEmail())
                        .setItems(new ArrayList<>(carrito))
                        .setTotal(total)
                        .setEstado(EstadoOrden.PENDIENTE_PAGO)
                        .build();
                Orden guardada = ordenDAO.guardar(nuevaOrden);
                ordenPendienteId = guardada.getNumero();
                for (ItemCarrito i : carrito) {
                    inventarioDAO.restarStock(i.getProducto().getCodigo(), i.getCantidad());
                }
                carrito.clear();
                System.out.println("Orden #" + ordenPendienteId + " generada. Andá a 'Procesamiento de Pagos' para abonarla.");
                return false;
            }
            case 4: {
                if (ordenPendienteId == null) throw new DatosInvalidosException("No tenes ninguna orden pendiente de pago. Primero generá una compra.");
                Orden orden = ordenDAO.buscarPorId(ordenPendienteId);
                System.out.println("1. Tarjeta Credito | 2. Tarjeta Debito | 3. Billetera Virtual | 4. Transferencia | 5. Contra Entrega");
                int mPago = Integer.parseInt(scanner.nextLine());
                ProcesadorPago strategy; String nombreMetodo;
                if (mPago == 1) { strategy = new PagoTarjetaCredito(); nombreMetodo = "TARJETA_CREDITO"; }
                else if (mPago == 2) { strategy = new PagoTarjetaDebito(); nombreMetodo = "TARJETA_DEBITO"; }
                else if (mPago == 3) { strategy = new PagoBilleteraVirtual(); nombreMetodo = "BILLETERA_VIRTUAL"; }
                else if (mPago == 4) { strategy = new PagoTransferencia(); nombreMetodo = "TRANSFERENCIA"; }
                else { strategy = new PagoContraEntrega(); nombreMetodo = "CONTRA_ENTREGA"; }

                double monto = orden.getTotal();
                System.out.println("Monto a abonar: $" + monto);
                boolean aprobado = strategy.procesarPago(monto);

                Pago pago = new Pago(0, ordenPendienteId, nombreMetodo, monto, aprobado ? EstadoPago.APROBADO : EstadoPago.RECHAZADO, new Date());
                Pago guardado = pagoDAO.guardar(pago);
                ordenDAO.asociarPago(ordenPendienteId, guardado.getId());

                if (!aprobado) {
                    throw new PagoRechazadoException("El pago fue rechazado por el medio de pago elegido. Proba con otro metodo.");
                }
                ordenDAO.actualizarEstado(ordenPendienteId, EstadoOrden.PAGADA);
                System.out.println("[SISTEMA]: Pago aceptado y registrado. Orden #" + ordenPendienteId + " confirmada.");
                ordenPendienteId = null;
                return false;
            }
            case 5:
                System.out.println("1. Ver mis pedidos | 2. Consultar pedido por numero");
                int opSeg = Integer.parseInt(scanner.nextLine());
                if (opSeg == 1) {
                    for (Orden o : ordenDAO.obtenerPorCliente(usuarioLogueado.getEmail())) System.out.println(o);
                } else {
                    System.out.print("Numero de orden: "); int idOrdenSeg = Integer.parseInt(scanner.nextLine());
                    Orden o = ordenDAO.buscarPorId(idOrdenSeg);
                    System.out.println(o);
                    if (o.getIdEnvio() != null) {
                        System.out.println(envioDAO.buscarPorId(o.getIdEnvio()));
                    } else {
                        System.out.println("Todavia no tiene un envio asociado.");
                    }
                }
                return false;
            case 6:
                System.out.println("1. Abrir Reclamo | 2. Registrar Devolucion | 3. Calificar Producto");
                int opPost = Integer.parseInt(scanner.nextLine());
                if (opPost == 1) {
                    System.out.print("ID de Orden asociada (Enter para omitir): "); String idOrdenStr = scanner.nextLine();
                    Integer idOrdenReclamo = idOrdenStr.isEmpty() ? null : Integer.parseInt(idOrdenStr);
                    System.out.print("Motivo Reclamo: "); String motivoReclamo = scanner.nextLine();
                    if (motivoReclamo.isEmpty()) throw new DatosInvalidosException("El motivo no puede estar vacio.");
                    reclamoDAO.guardar(new Reclamo(0, usuarioLogueado.getEmail(), idOrdenReclamo, motivoReclamo, new Date(), EstadoReclamo.ABIERTO));
                } else if (opPost == 2) {
                    System.out.print("Codigo Producto a Devolver: "); String cp = scanner.nextLine();
                    System.out.print("Motivo Devolucion: "); String mot = scanner.nextLine();
                    if (cp.isEmpty() || mot.isEmpty()) throw new DatosInvalidosException("El codigo de producto y el motivo no pueden estar vacios.");
                    if (productoDAO.buscarPorCodigo(cp) == null) throw new ProductoNoEncontradoException("El producto no existe.");
                    try (Connection con = Conexion.getInstancia().conectar();
                         PreparedStatement ps = con.prepareStatement("INSERT INTO devoluciones (producto_codigo, motivo, fecha, estado, cliente_email) VALUES (?, ?, ?, 'PENDIENTE', ?)")) {
                        ps.setString(1, cp); ps.setString(2, mot); ps.setDate(3, new java.sql.Date(System.currentTimeMillis()));
                        ps.setString(4, usuarioLogueado.getEmail());
                        ps.executeUpdate(); System.out.println("Devolucion iniciada.");
                    }
                } else {
                    System.out.print("Codigo Producto: "); String cp = scanner.nextLine();
                    if (productoDAO.buscarPorCodigo(cp) == null) throw new ProductoNoEncontradoException("El producto no existe.");
                    System.out.print("Estrellas (1-5): "); int est = Integer.parseInt(scanner.nextLine());
                    if (est < 1 || est > 5) throw new DatosInvalidosException("Las estrellas deben estar entre 1 y 5.");
                    System.out.print("Comentario: "); String com = scanner.nextLine();
                    try (Connection con = Conexion.getInstancia().conectar();
                         PreparedStatement ps = con.prepareStatement("INSERT INTO calificaciones (producto_codigo, estrellas, comentario) VALUES (?, ?, ?)")) {
                        ps.setString(1, cp); ps.setInt(2, est); ps.setString(3, com);
                        ps.executeUpdate(); System.out.println("Calificacion guardada. ¡Gracias por valorar!");
                    }
                }
                return false;
            case 7:
                return true;
            default:
                System.out.println("Opcion no valida.");
                return false;
        }
    }

    private boolean menuOperadorVentas(int opcion) throws Exception {
        Validador.validarPermiso(usuarioLogueado.getRol() == Rol.OPERADOR_VENTAS);
        switch (opcion) {
            case 1:
                System.out.println("1. Ver todas las ordenes | 2. Cambiar estado de una orden");
                int opAO = Integer.parseInt(scanner.nextLine());
                if (opAO == 1) {
                    for (Orden o : ordenDAO.obtenerTodas()) System.out.println(o);
                } else {
                    System.out.print("Numero de orden: "); int idOrd = Integer.parseInt(scanner.nextLine());
                    ordenDAO.buscarPorId(idOrd);
                    System.out.println("Nuevo estado: 1. PREPARACION | 2. DESPACHADA | 3. EN_TRANSITO | 4. ENTREGADA | 5. CANCELADA | 6. DEVUELTA");
                    int opEstOrd = Integer.parseInt(scanner.nextLine());
                    EstadoOrden[] estados = { EstadoOrden.PREPARACION, EstadoOrden.DESPACHADA, EstadoOrden.EN_TRANSITO, EstadoOrden.ENTREGADA, EstadoOrden.CANCELADA, EstadoOrden.DEVUELTA };
                    if (opEstOrd < 1 || opEstOrd > estados.length) throw new DatosInvalidosException("Opcion de estado invalida.");
                    ordenDAO.actualizarEstado(idOrd, estados[opEstOrd - 1]);
                }
                return false;
            case 2:
                System.out.println("--- ORDENES PENDIENTES DE CONFIRMAR PAGO ---");
                boolean hayPendientes = false;
                for (Orden o : ordenDAO.obtenerTodas()) {
                    if (o.getEstado() == EstadoOrden.PENDIENTE_PAGO) { System.out.println(o); hayPendientes = true; }
                }
                if (!hayPendientes) { System.out.println("No hay ordenes pendientes de pago."); return false; }
                System.out.print("Numero de orden a confirmar (Enter para omitir): "); String idConf = scanner.nextLine();
                if (idConf.isEmpty()) return false;
                int idOrdenConf = Integer.parseInt(idConf);
                Orden ordenConf = ordenDAO.buscarPorId(idOrdenConf);
                Pago pagoManual = pagoDAO.guardar(new Pago(0, idOrdenConf, "CONFIRMACION_MANUAL", ordenConf.getTotal(), EstadoPago.APROBADO, new Date()));
                ordenDAO.asociarPago(idOrdenConf, pagoManual.getId());
                ordenDAO.actualizarEstado(idOrdenConf, EstadoOrden.PAGADA);
                System.out.println("Pago de la orden #" + idOrdenConf + " confirmado manualmente.");
                return false;
            case 3:
                System.out.print("Ingrese ID de Envio: ");
                System.out.println(envioDAO.buscarPorId(Integer.parseInt(scanner.nextLine())));
                return false;
            case 4:
                return true;
            default:
                System.out.println("Opcion no valida.");
                return false;
        }
    }

    private boolean menuLogistica(int opcion) throws Exception {
        Validador.validarPermiso(usuarioLogueado.getRol() == Rol.RESPONSABLE_LOGISTICA);
        switch (opcion) {
            case 1: {
                System.out.print("ID Orden a despachar: "); int io = Integer.parseInt(scanner.nextLine());
                Orden orden = ordenDAO.buscarPorId(io);
                if (orden.getEstado() != EstadoOrden.PAGADA) throw new DatosInvalidosException("La orden #" + io + " todavia no fue pagada.");
                System.out.print("Direccion: "); String direccion = scanner.nextLine();
                System.out.print("Provincia: "); String provincia = scanner.nextLine();
                System.out.print("Ciudad: "); String ciudad = scanner.nextLine();
                System.out.print("Codigo Postal: "); String cp = scanner.nextLine();
                if (direccion.isEmpty() || provincia.isEmpty() || ciudad.isEmpty() || cp.isEmpty()) {
                    throw new DatosInvalidosException("Todos los datos del envio son obligatorios.");
                }
                System.out.println("Tipo de envio: 1. Retiro en Sucursal | 2. Estandar | 3. Express | 4. Internacional");
                int opTipo = Integer.parseInt(scanner.nextLine());
                TipoEnvio[] tipos = { TipoEnvio.RETIRO_SUCURSAL, TipoEnvio.ESTANDAR, TipoEnvio.EXPRESS, TipoEnvio.INTERNACIONAL };
                double[] costos = { 0, 1500, 3500, 9000 };
                if (opTipo < 1 || opTipo > tipos.length) throw new DatosInvalidosException("Tipo de envio invalido.");
                TipoEnvio tipoEnvio = tipos[opTipo - 1];
                String codigoSeguimiento = "ENV-" + io + "-" + System.currentTimeMillis() % 100000;

                Envio envio = new Envio(0, io, codigoSeguimiento, direccion, provincia, ciudad, cp, tipoEnvio, EstadoEnvio.DESPACHADO, costos[opTipo - 1]);
                Envio guardado = envioDAO.guardar(envio);
                ordenDAO.asociarEnvio(io, guardado.getId());
                ordenDAO.actualizarEstado(io, EstadoOrden.DESPACHADA);
                return false;
            }
            case 2: {
                System.out.print("ID de Envio a actualizar: "); int idEnvUp = Integer.parseInt(scanner.nextLine());
                Envio envio = envioDAO.buscarPorId(idEnvUp);
                System.out.println("Nuevo estado: 1. PREPARACION | 2. DESPACHADO | 3. EN_TRANSITO | 4. ENTREGADO | 5. DEMORADO | 6. CANCELADO");
                int opEstEnv = Integer.parseInt(scanner.nextLine());
                EstadoEnvio[] estadosEnv = { EstadoEnvio.PREPARACION, EstadoEnvio.DESPACHADO, EstadoEnvio.EN_TRANSITO, EstadoEnvio.ENTREGADO, EstadoEnvio.DEMORADO, EstadoEnvio.CANCELADO };
                if (opEstEnv < 1 || opEstEnv > estadosEnv.length) throw new DatosInvalidosException("Opcion de estado invalida.");
                envioDAO.actualizarEstado(idEnvUp, estadosEnv[opEstEnv - 1]);
                if (estadosEnv[opEstEnv - 1] == EstadoEnvio.EN_TRANSITO) ordenDAO.actualizarEstado(envio.getIdOrden(), EstadoOrden.EN_TRANSITO);
                else if (estadosEnv[opEstEnv - 1] == EstadoEnvio.ENTREGADO) ordenDAO.actualizarEstado(envio.getIdOrden(), EstadoOrden.ENTREGADA);
                return false;
            }
            case 3:
                System.out.print("Ingrese ID de Envio: ");
                System.out.println(envioDAO.buscarPorId(Integer.parseInt(scanner.nextLine())));
                return false;
            case 4:
                return true;
            default:
                System.out.println("Opcion no valida.");
                return false;
        }
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
            System.out.println("7. Gestion de Reclamos y Devoluciones");
            System.out.println("8. Salir");
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
            System.out.println("2. Actualizar Estado de Envio");
            System.out.println("3. Seguimiento de Pedidos");
            System.out.println("4. Salir");
        }
        System.out.print("Seleccione una opcion: ");
    }
}
