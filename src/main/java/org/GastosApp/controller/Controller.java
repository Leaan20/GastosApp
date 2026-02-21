package org.GastosApp.controller;

import org.GastosApp.DAO.AccountDAO;
import org.GastosApp.DAO.UserDAO;
import org.GastosApp.model.Account;
import org.GastosApp.model.User;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/* TODO controller podria utilizar programacion funcional
sacando los try catch y utiilzando maps
* */

// nuestro controller se encargara de recibir las solicitudes y derivarlas al DAO correspondiente
// comunicara con la persistencia dejando de lado que lo hago el menu
public class Controller {
        private final UserDAO userDao = new UserDAO();
        private final AccountDAO accountDao = new AccountDAO();

        public Controller(){}

    // seccion user
        public void createUser(User us){
                try{
                        userDao.DAOCreateUser(us);
                        System.out.println("Usuario creado");
                }catch(Exception exc){
                        System.out.println("No pudo crearse el usuario correctamente");
                }
        }

        public void UpdateUser(User us){
            try{
                userDao.DAOUpdateUser(us);
                System.out.println("Usuario actualizado");
            }catch(Exception exc){
                System.out.println("Error al actualizar el usuario " + us.getId() + "\n" + exc.getMessage());
            }
        }

        public void deleteUser(User us){
            try{
                userDao.DAODeleteUser(us);
                System.out.println("Usuario eliminado");
            }catch(Exception exc){
                System.out.println("Error al eliminar el usuario " + us.getId() + "\n" + exc.getMessage());
            }
        }

        public User getUser(int user_id){
            return userDao.DAOGetUser(user_id)
                    .orElseGet( () -> {
                        System.out.println("Usuario no encontrado, devolviendo vacio");
                        return new User();
                    } );
        }

        public Map<Integer,User> getUsers(){
            Map<Integer,User> usuarios = new HashMap<Integer,User>();
            try{
                usuarios = userDao.DAOGetUsers();
                System.out.println("Usuarios obtenidos exitosamente!");
            }catch(Exception exc){
                System.out.println("Error al obtener los usuarios " + " \n" + exc.getMessage());
            }
            return usuarios;
        }

        // TODO utilizar el metodo DAOVerifyLogin y asi crear un logueo

        public User login(String email, String pass){
            User us;
            boolean logueo = false;
            try{
                logueo = userDao.DAOVerifyLogin(email,pass);
            }catch(Exception exc){
                System.out.println("Error de logueo " + exc.getMessage());
            }
            // si se genero el logueo correctamente , devolvemos el usuario con sus datos
            if(logueo){
                us =  userDao.DAOGetUserByEmail(email);
                return us;
            } else {
                System.out.println("Datos erroneos, vuelva a intentar");
                return us = new User();
            }

        }

    //seccion account
    public void createAccount(Account acc){
            try{
                accountDao.DAOCreateAccount(acc);
                System.out.println("Cuenta creada con exito!");
            }catch(Exception exc){
                System.out.println("Error al crear una nueva cuenta \n" +  exc.getMessage());
            }
    }

    public void updateAccount(Account acc){
            try{
                accountDao.DAOUpdateAccount(acc);
                System.out.println("Cuenta actualizada con exito!");
            }catch(Exception exc){
                System.out.println("Error al actualizar la cuenta N° " + acc.getAccountId() + "\n" + exc.getMessage());
            }
    }

    public void deleteAccount(Account acc){
            try{
                accountDao.DAODeleteAccount(acc);
                System.out.println("Cuenta eliminada con exito");
            }catch(Exception exc){
                System.out.println("Error al eliminar la cuenta " + acc.getAccountId() + "\n" + exc.getMessage());
            }
    }

    public Account getAccount(int account_id){
            Account acc = new Account();
            try{
                acc = accountDao.DAOGetAccount(account_id);
                System.out.println("Cuenta obtenida exitosamente");
            }catch(Exception exc){
                System.out.println("Error al obtener la cuenta " + account_id + "\n" + exc.getMessage());
            }

            return acc;
    }

    public List<Account> getAccounts(int user_id){
            List<Account> cuentas = new ArrayList<>();
            try{
                cuentas = accountDao.DAOGetUserAccounts(user_id);
                System.out.println("Cuentas del usuario " + user_id + " obtenidas exitosamente");
            }catch(Exception exc){
                System.out.println("Error al obtener las cuentas del usuario "+ user_id + "\n" + exc.getMessage());
            }
            return cuentas;
    }




}
