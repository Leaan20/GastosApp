package org.GastosApp;

import org.GastosApp.CSV.PersistenceCSV;
import org.GastosApp.services.DataService;
import org.GastosApp.logic.MenuConsole;
import org.GastosApp.model.Account;
import org.GastosApp.model.User;

import java.io.FileNotFoundException;
import java.io.IOException;

import java.util.HashMap;
import java.util.Map;

import java.util.Scanner;


   /*
            El flujo del programa:
            - Cargamos los datos (si los hay)
            - vinculamos con el DataService
            - Pedimos al usuario que se loguee
            - Damos inicio al menu
   */



public class Main {
    public static void main(String[] args) {
        Scanner teclado = new Scanner(System.in);
        User loginUs = null;
        Map<Integer, User> usuarios = new HashMap<>();
        Map<Integer, Account> cuentas = new HashMap<>();
        int intentos = 0;
        int maxIntentos = 3;


        // Hacemos la carga de informacion
        PersistenceCSV pcsvCuentas = new PersistenceCSV("datos_cuentas.csv");
        PersistenceCSV pcsvUsuarios = new PersistenceCSV("datos_usuarios.csv");
        try{
            usuarios = pcsvUsuarios.usersLoad();
            cuentas = pcsvCuentas.accountsLoad();
        } catch(FileNotFoundException exc){
            System.out.println(exc.getMessage());
            System.out.println("Error de obtencion de informacion");
        } catch (IOException exc) {
            throw new RuntimeException(exc);
        }

        // Si hay datos, el DataService vinculara las cuentas
        if(!usuarios.isEmpty() && !cuentas.isEmpty()){
            DataService.vincularCuentas(usuarios,cuentas);
        }

        if (usuarios.isEmpty()) {
            System.out.println("No hay usuarios registrados. Cree un usuario inicial en el archivo CSV.");
            return; // O llamar a un método de registro
        }

        // Inicio del programa - Interaccion Usuario

        System.out.println("Aplicacion de Control de Gastos");
        System.out.println("Financial Management App");

        System.out.println();
        String nombreUs;
        String passUs;

        // Utilizamos un bucle while para que el usuario ingrese sus datos
        while(loginUs == null && intentos <= maxIntentos){
            System.out.println("\nIntento " + (intentos + 1) + " de " + maxIntentos);
            System.out.println("Ingrese su nombre de usuario: ");
            nombreUs = teclado.nextLine();
            System.out.println("Ingrese su password: ");
            passUs = teclado.nextLine();

            for (User us : usuarios.values()) {
                if (us.getNombre().equals(nombreUs) && us.getPassword().equals(passUs)) {
                    loginUs = us;
                    break;
                }
            }

            if(loginUs == null){
                System.out.println("Credenciales incorrectas");
                intentos++;
            }

        }


        // Si son correctos los datos, queda logueado e ingresa al menu
        if (loginUs != null) {
            System.out.println("\n¡Login exitoso!");
            MenuConsole.establecerUsuario(loginUs);
            MenuConsole.configurarPersistencia(pcsvCuentas,pcsvUsuarios,usuarios);
            MenuConsole.mostrarMenu();
        } else {
            System.out.println("Haz superado el limite de intentos");
        }

        teclado.close();





    }
}