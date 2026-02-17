package org.GastosApp.logic;

 // Sera nuestro cerebro de la app
// Tendra las reglas de negocio

import org.GastosApp.model.Account;
import org.GastosApp.model.Movement;


import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;



public class CostsManager {
        // sera 0 al principio
        private double totalGlobal = 0;
        private final Map<Integer, Account> accounts =  new HashMap<Integer, Account>();
        private final String[] categorias = {"Nueva Cuenta", "Eliminación de cuenta", "Deposito", "Transferencia", "Gasto"};
        // record guardara en forma de string los movimientos de cuentas(TODO Podria darle algun persistencia )
        // va a contener una lista de movimientos
        private final List<Movement> record = new ArrayList<>();



        public void registrarMovimiento(String cate, String desc, BigDecimal amount){
            this.record.add(new Movement(cate,desc,amount));
        }

        public CostsManager() {}

        public void addAccount(BigDecimal montoInicial, String nombre, int idUser){
                Account acc = new Account(montoInicial, nombre, idUser);
                // Actualizamos a Map
                accounts.put(acc.getAccountId(), acc);
                registrarMovimiento(categorias[0],"Cuenta agregada: " + acc.getNombre(), acc.getMonto());
        }

        public void addAccount(Account acc){
            accounts.put(acc.getAccountId(), acc);
            registrarMovimiento(categorias[0], "Cuenta agregada: " + acc.getNombre(), acc.getMonto());
        }

        public String deleteAccountByName(String nombreBusqueda) {
            Account accFound = findAccountByName(nombreBusqueda);
            String cuentaNom;
            if(accFound != null){
                if(accFound.getMonto().compareTo(BigDecimal.ZERO) <= 0){
                    cuentaNom = accFound.getNombre();
                    accounts.remove(accFound.getAccountId());
                    registrarMovimiento(categorias[1],"Cuenta eliminada " + cuentaNom, accFound.getMonto());
                    return "Cuenta " + cuentaNom + " fue eliminada con exito";
                } else {
                    return "No puede eliminarse una cuenta con fondos";
                }
            } else {
                return "No es posible eliminar la cuenta";
            }
        }


        public BigDecimal getTotalGlobal() {
            BigDecimal sum = new BigDecimal(0);
            for(Account acc: accounts.values()){
                sum.add(acc.getMonto());
            }
            return sum;
        }

        public Map<Integer,Account> getAccounts(){
            return this.accounts;
        }

    public String[] showAccountsInfo() {
        String[] info = new String[accounts.size()];
        int i = 0; // Usamos este contador para el índice del array

        for (Account acc : accounts.values()) {
            info[i] = acc.showInfo();
            i++; // Incrementamos para la siguiente posición
        }
        return info;
    }

        public Account getAccountById(int accId){
            return accounts.get(accId);

        }

        public Account findAccountByName(String nombreBusqueda){
            for(Account acc: accounts.values()){
                if(acc.getNombre().equalsIgnoreCase(nombreBusqueda)){

                    return acc;
                }
            }
            return null;

        }

        public boolean deposito(String nom, BigDecimal din){
            Account acc = findAccountByName(nom);
            if(acc != null){
                acc.deposito(din);
                registrarMovimiento(categorias[2], "Deposito de dinero: $" + din + " en cuenta: " + acc.getNombre(), acc.getMonto());
                return true;
            } else {
                return false;
            }
        }
        // metodo de transferencia TODO utilizar metodos propios de la clase Account deposito, extraccion
        // debo restar de la cuenta inicio el monto necesario, previamente verificando que lo tenga disponible, luego "depositarlo" en la cuenta destino
        // de momento solo el que consuma este metodo manejara el resultado brindado
        public boolean transferencia(String cuenta1, String cuenta2, BigDecimal din ){
            Account acc1 = findAccountByName(cuenta1);
            Account acc2 = findAccountByName(cuenta2);
            if(accounts.containsKey(acc1.getAccountId()) && accounts.containsKey(acc2.getAccountId()) && acc1.getMonto().compareTo(din) >= 0){
                acc1.extraccion(din);
                acc2.deposito(din);
                registrarMovimiento(categorias[3], "Se realizo una transferencia de la cuenta " + acc1.getNombre() + " a la cuenta " + acc2.getNombre() + " por el monto de $" + din, din);
                return true;
            } else {
                return false;
            }
        }

        /*
                1: El primer valor es mayor que el segundo.

                0: Ambos valores son iguales.

               -1: El primer valor es menor que el segundo
            * */
        public boolean gasto(String nom, BigDecimal din){
            Account acc = findAccountByName(nom);
            if(acc!= null && (acc.getMonto().compareTo(din)) >= 0){
                // utilizamos el metodo de extraccion para el gasto
                acc.extraccion(din);
                registrarMovimiento(categorias[4], "Se realizo el gasto de $" + din + " desde la cuenta " + acc.getNombre(), din);
                return true;
            } else {
                return false;
            }
        }

        public int accountsQuantity(){
            return accounts.size();
        }

        // otorgamos los movimientos registrados del usuario
        public List<Movement> getRecord(){
            return this.record;
        }

}
