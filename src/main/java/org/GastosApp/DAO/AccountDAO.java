package org.GastosApp.DAO;

import org.GastosApp.DB.DBConnect;
import org.GastosApp.model.Account;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class AccountDAO {
        public AccountDAO(){}

        public void DAOCreateAccount(Account acc){
                String sql = "INSERT INTO accounts(name, mount, user_id) VALUES(?,?,?) ";

                try(Connection conn = DBConnect.getConexion()){
                        PreparedStatement pstmt = conn.prepareStatement(sql);

                        pstmt.setString(1,acc.getNombre());
                        pstmt.setBigDecimal(2,acc.getMonto());
                        pstmt.setInt(1,acc.getUserId());

                        pstmt.executeUpdate();
                        System.out.println("Nueva cuenta creada!");
                }catch(SQLException exc){
                        System.out.println("Error al crear la cuenta " + exc.getMessage());
                }

        }

        public void DAOUpdateAccount(Account accUp){
                String sql = "UPDATE accounts SET name=?, mount= ? WHERE account_id= ?";

                try(Connection conn = DBConnect.getConexion()){
                        PreparedStatement pstmt = conn.prepareStatement(sql);

                        pstmt.setString(1,accUp.getNombre());
                        pstmt.setBigDecimal(2,accUp.getMonto());
                        pstmt.setInt(3,accUp.getAccountId());

                        pstmt.executeUpdate();
                        System.out.println("Cuenta actualizada");
                }catch(SQLException exc){
                        System.out.println("Error al actualizar la cuenta" + exc.getMessage());
                }
        }

        public void DAODeleteAccount(Account acc){
                String sql = "DELETE FROM accounts WHERE account_id = ?";

                try(Connection conn = DBConnect.getConexion()){
                        PreparedStatement pstmt = conn.prepareStatement(sql);

                        pstmt.setInt(1, acc.getAccountId());

                        pstmt.executeUpdate();
                        System.out.println("Cuenta eliminada");
                }catch(SQLException exc){
                        System.out.println("Error al eliminar la cuenta" + exc.getMessage());
                }
        }

        public Account DAOGetAccount(int account_id){
                String sql = "SELECT * FROM accounts WHERE account_id = ?";
                Account acc = null;

                try(Connection conn = DBConnect.getConexion()){
                        PreparedStatement pstmt = conn.prepareStatement(sql);
                        pstmt.setInt(1,account_id);

                        ResultSet res = pstmt.executeQuery();

                        while(res.next()){
                                acc = new Account(res.getInt("account_id"), res.getBigDecimal("mount"), res.getString("name"), res.getInt("user_id") );

                        }
                        System.out.println("Cuentsa obtenida");

                }catch(SQLException exc){
                        System.out.println("Error al encontrar la cuenta " + exc.getMessage());
                }

                return acc;
        }
}
