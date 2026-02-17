package org.GastosApp.controller;

import org.GastosApp.DAO.AccountDAO;
import org.GastosApp.DAO.UserDAO;

// nuestro controller se encargara de recibir las solicitudes y derivarlas al DAO correspondiente
// comunicara con la persistencia dejando de lado que lo hago el menu
public class Controller {
        private final UserDAO userDao = new UserDAO();
        private final AccountDAO accountDao = new AccountDAO();

        public Controller(){}
    // seccion user


    //seccion account
}
