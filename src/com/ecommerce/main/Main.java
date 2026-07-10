package com.ecommerce.main;
import java.util.Scanner;
import com.ecommerce.dao.ProductoDAO;
import com.ecommerce.dao.impl.ProductoDAOImpl;
import com.ecommerce.dao.UsuarioDAO;
import com.ecommerce.dao.impl.UsuarioDAOImpl;
import com.ecommerce.model.Producto;
import com.ecommerce.model.ProductoFisico;
import com.ecommerce.model.Usuario;
import com.ecommerce.model.Rol;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        boolean salir = false;
        ProductoDAO productoDAO = new ProductoDAOImpl();
        UsuarioDAO usuarioDAO = new UsuarioDAOImpl();

        while (!salir) {
            System.out.println("\n=== SISTEMA E-COMMERCE ===");
            System.out.println("1. Ingresar Producto Fisico");
            System.out.println("2. Ver Productos en BD");
            System.out.println("3. Ingresar Usuario a mano");
            System.out.println("4. Ver Usuarios en BD");
            System.out.println("5. Salir");
            System.out.print("Seleccione una opcion: ");

            int opcion = scanner.nextInt();
            scanner.nextLine();

            if (opcion == 1) {
                System.out.print("Ingrese codigo: ");
                String cod = scanner.nextLine();
                System.out.print("Ingrese nombre: ");
                String nom = scanner.nextLine();
                System.out.print("Ingrese precio base: ");
                double prec = scanner.nextDouble();
                scanner.nextLine();
                
                Producto nuevo = new ProductoFisico(cod, nom, prec, 10);
                productoDAO.insertar(nuevo);
                System.out.println("¡Producto guardado con exito!");
            } else if (opcion == 2) {
                productoDAO.listarTodos();
            } else if (opcion == 3) {
                System.out.print("Ingrese nombre: ");
                String nom = scanner.nextLine();
                System.out.print("Ingrese apellido: ");
                String ape = scanner.nextLine();
                System.out.print("Ingrese email: ");
                String mail = scanner.nextLine();
                
                Usuario nuevoUser = new Usuario(0, nom, ape, mail, "1234", new java.util.Date(), true, Rol.CLIENTE);
                usuarioDAO.insertar(nuevoUser);
                System.out.println("¡Usuario guardado con exito!");
            } else if (opcion == 4) {
                usuarioDAO.listarTodos();
            } else if (opcion == 5) {
                salir = true;
                System.out.println("Saliendo...");
            } else {
                System.out.println("Opcion invalida.");
            }
        }
        scanner.close();
    }
}