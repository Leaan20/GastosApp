package org.GastosApp.services;

import org.GastosApp.model.Account;
import org.GastosApp.model.User;

import java.util.Map;

// Esta clase transformará los datos recibidos
public abstract class  DataService {

    public static void vincularCuentas(Map<Integer, User> usuarios, Map<Integer, Account> cuentas){
        for(Account acc : cuentas.values()){
            for(User us: usuarios.values()){
                if(acc.getUserId() == us.getId()){
                    us.getCm().addAccount(acc);
                    break;
                }
            }
        }
    }




    // TODO podria encargarse de guardar las cuentas tambien
}
