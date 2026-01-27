package org.GastosApp.model;

import org.GastosApp.logic.CostsManager;

import java.util.Map;

public class User {
    private static int countId = 0;

    private String nombre;
    private String password;
    private int userId;
    // cada usuario podra tener su propio administrador de gastos
    private CostsManager cm;

    public User(String nombre, String pass){
        this.nombre = nombre;
        this.password = pass;
        countId++;
        this.userId = countId;
        this.cm = new CostsManager();
    }
    public User(int id, String nombre, String pass){
        this.nombre = nombre;
        this.password = pass;
        this.userId = id;
        if(id >= countId){
            countId = id;
        }
        this.cm = new CostsManager();
    }


    public int getId(){
        return this.userId;
    }

    public String getNombre(){
        return this.nombre;
    }

    // TODO esto deberia mejorarse
    public String getPassword(){
        return this.password;
    }
    public CostsManager getCm() {
        return cm;
    }

    // hacemos eso para acortar el metodo
    public void addAccount(double monto, String nombre, int idUser){
         this.cm.addAccount(monto, nombre, idUser);
    }

    public String deleteAccount(String nom){
        return this.cm.deleteAccountByName(nom);
    }

    public void showAccountsInfo(){
        this.cm.showAccountsInfo();
    }

    public String depositar(String nom, double din){
        boolean depo = this.cm.deposito(nom, din);
        if(!depo){
            return "No es posible efectuar el deposito";
        } else {
            return "Deposito realizado correctamente";
        }
    }

    public String transferir(String cuenta1, String cuenta2, double din){
        boolean result = this.cm.transferencia(cuenta1,cuenta2,din);
        if(!result){
            return "No pudo realizarse la trasnferencia";
        }else {
            return "Transferencia realizada";
        }
    }

    public double showTotal(){
        return this.cm.getTotalGlobal();
    }

    public Map<Integer, Account> getAllAccounts(){
        return this.cm.getAccounts();
    }

    public int accountsQuantity(){
        return this.cm.accountsQuantity();
    }

    public String showAccounts(){
        String[] info = this.cm.showAccountsInfo();
        StringBuilder i  = new StringBuilder();
        String informacion;
        for(String in: info){
            i.append(in);
            i.append("\n");
        }
        informacion = i.toString();
        return informacion;
    }

}
