package com.ecommerce.main;

import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        boolean salir = false;

        while (!salir) {
            System.out.println("=== SISTEMA E-COMMERCE ===");
            System.out.println("1. Gestionar Productos");
            System.out.println("2. Gestionar Usuarios");
            System.out.println("3. Salir");
            System.out.print("Seleccione una opcion: ");

            int opcion = scanner.nextInt();
            scanner.nextLine();

            switch (opcion) {
                case 1:
                    System.out.println("Modulo de Productos en construccion...");
                    break;
                case 2:
                    System.out.println("Modulo de Usuarios en construccion...");
                    break;
                case 3:
                    salir = true;
                    System.out.println("Saliendo del sistema...");
                    break;
                default:
                    System.out.println("Opcion invalida.");
            }
        }
        scanner.close();
    }
}
