package org.GastosApp.view;

import org.GastosApp.CSV.PersistenceCSV;
import org.GastosApp.model.Account;
import org.GastosApp.model.Movement;
import org.GastosApp.model.User;
import org.GastosApp.services.OpenPDFService;

import org.GastosApp.controller.Controller;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import java.util.Scanner;

// generara una interfaz para interaccion
public class MenuConsole {

    Controller control = new Controller();


    private static int opcion = 0;
    private static boolean salir = false;
    private static Scanner teclado = new Scanner(System.in);
    // Maps para manejar las clases
    private static PersistenceCSV pcsvCuentas;
    private static PersistenceCSV pcsvUsuarios;
    private static Map<Integer, User> mapaUsuariosGlobal;
    private static User usuarioLogueado;

    // Constructor privado para evitar que alguien haga "new MenuConsole()"
    private MenuConsole() {
    }



    public static void configurarPersistencia(PersistenceCSV p1, PersistenceCSV p2, Map<Integer, User> mapa){
        pcsvCuentas = p1;
        pcsvUsuarios = p2;
        mapaUsuariosGlobal = mapa;
    }

    private static void guardarTodo(){
        try {
            Map<Integer, Account> todasLasCuentas = new HashMap<>();
            for(User us: mapaUsuariosGlobal.values()){
                todasLasCuentas.putAll(us.getAllAccounts());
            }
            pcsvCuentas.accountsSave(todasLasCuentas);
            pcsvUsuarios.usersSave(mapaUsuariosGlobal);
        } catch(IOException exc){
            System.out.println("Error crítico al guardar: " + exc.getMessage());
        }
    }




    // Método para inyectar el usuario que viene del Login del Main
    public static void establecerUsuario(User user) {
        usuarioLogueado = user;
        salir = false;
    }

    // Mejorarlo a mostrar menu segun este logueado el usuario
    public static void mostrarMenu(){
        if (usuarioLogueado == null) {
            System.out.println("No hay ningún usuario logueado.");
            return;
        }
            do{
                System.out.println("Aplicacion de gestion de gastos");

                System.out.println("\n--- Gestión de Gastos: " + usuarioLogueado.getNombre() + " ---");
                System.out.println("Saldo Global: $" + usuarioLogueado.showTotal());



                System.out.println("""
                        1- Añadir una nueva cuenta
                        2- Depositar a una cuenta
                        3- Transferir a otra cuenta
                        4- Eliminar una cuenta
                        5- Recibir informacion de mis cuentas
                        6- Movimientos
                        7- Generar reporte de movimientos
                        8- Desloguearse
                        """);
                System.out.println("Seleccione una opcion: ");

                int opcion = Integer.parseInt(teclado.nextLine());
                // TODO utilizar la logica y sacarla del switch y ponerla en metodos particulares
                switch(opcion){
                    case 1 -> nuevaCuenta();
                    case 2 -> depositarACuenta();
                    case 3 -> transferencia();
                    case 4 -> eliminarCuenta();
                    case 5 -> mostrarInfo();
                    case 6 -> mostrarMovimientos();
                    case 7 -> reporteMov();
                    case 8 -> {
                        System.out.println("Cerrando sesión de " + usuarioLogueado.getNombre());
                        usuarioLogueado = null;
                        salir = true;
                    }
                    default -> System.out.println("Opción no válida.");


                }
            }while(!salir);
    }

    // Metodos del menu
    private static void  nuevaCuenta(){
        System.out.println("Ingrese el nombre de la cuenta");
        String nombreCuenta = teclado.nextLine();
        System.out.println("Ingrese el monto inicial:  ");
        BigDecimal montoInicial = new BigDecimal(teclado.nextLine());
        usuarioLogueado.addAccount( montoInicial,nombreCuenta, usuarioLogueado.getId());
        // TODO aqui deberia integrar mi controller para trabajar con la db

        guardarTodo();
        System.out.println("Cuenta creada con exito");
    }

    private static void  depositarACuenta(){
        System.out.println("Ingrese el nombre de la cuenta a depositar: ");
        String cuenta = teclado.nextLine();
        System.out.println("Ingrese el monto a depositar: ");
        BigDecimal dinDeposito = new BigDecimal(teclado.nextLine());
        System.out.println(usuarioLogueado.depositar(cuenta,dinDeposito));
        guardarTodo();
    }

    private static void  transferencia(){
        System.out.println("Ingrese el nombre de la cuenta desde la cual transferir");
        String acc1 = teclado.nextLine();
        System.out.println("Ingrese el nombre de la cuenta destino");
        String acc2 = teclado.nextLine();
        System.out.println("Ingrese el monto a transferir");
        BigDecimal monto = new BigDecimal(teclado.nextLine());
        boolean exito = usuarioLogueado.getCm().transferencia(acc1, acc2, monto);
        if (exito) {
            System.out.println(exito ? "Transferencia realizada." : "Error: Saldo insuficiente.");
            guardarTodo();
        } else {
            System.out.println("Error: Una o ambas cuentas no existen.");
        }

    }

    private static void  eliminarCuenta(){
        System.out.println("Ingrese el nombre de la cuenta a eliminar: ");
        String accD = teclado.nextLine();
        System.out.println(usuarioLogueado.deleteAccount(accD));
        guardarTodo();
    }

    private static void  mostrarInfo(){
        System.out.println(usuarioLogueado.showAccounts());
    }

    private static void mostrarMovimientos(){
        List<Movement> info = usuarioLogueado.getCm().getRecord();
        StringBuilder i  = new StringBuilder();
        String informacion;
        for(Movement mov : info){
            i.append("-Categoria: ").append(mov.getCategory()).append("- Descripcion: ").append(mov.getDescription()).append("- $").append(mov.getAmount()).append("- ").append(mov.getDate());
            i.append("\n");
        }
        informacion = i.toString();
        System.out.println(informacion);
    }

    private static void reporteMov(){
        try{
            OpenPDFService.generarReporteMovimientos(usuarioLogueado);
        } catch(Exception exc){
            System.out.println(exc.getMessage());
        }

    }
}
