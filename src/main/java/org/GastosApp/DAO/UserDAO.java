package org.GastosApp.DAO;


import org.mindrot.jbcrypt.BCrypt;

import org.GastosApp.model.User;
import org.GastosApp.DB.DBConnect;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;


// para escrituras --> executeUpdate
// para busquedas --> executeQuery

// TODO integrar OPTIONALS

// nuestro DAO, se encargara de recibir y enviar los datos a la DB --> MySQL
public class UserDAO {
        // Contendra los metodos para actualizar los registros de la base de datos y la tabla de usuarios
        public UserDAO(){}

        // Utilizamos Bcrypt
        public void DAOCreateUser(User usuario){
            // utilizar los datos del usuario(en este caso ya pasamos el objeto creado)
                // Como ya pase el nombre del schema en la conexion no necesito repetirlo aca
                String sql = "INSERT INTO users(name, password, email) VALUES(?, ?, ?)";

                // hasheamos la pass
                String passHash = BCrypt.hashpw(usuario.getPassword(), BCrypt.gensalt());

            // utilizamos try with resources para abrir y cerrar la conexion a la db
                try(Connection conn = DBConnect.getConexion()){
                        PreparedStatement pstmt = conn.prepareStatement(sql);
                        // utilizamos el preparedStatement seteando los elementos , en el orden que los queremos a traves de los indices, en este caso inician en 1..2..3
                        // no enviamos el id, ya que queremos que lo decida la base de datos directamente.
                        pstmt.setString(1,usuario.getNombre());
                        pstmt.setString(2,passHash); // enviamos las pass hasheada
                        pstmt.setString(3,usuario.getEmail());

                        pstmt.executeUpdate();
                        System.out.println("Usuario guardado!");

                }catch(SQLException exc){
                        System.out.println("Error al guardar el usuario " + exc.getMessage());
                }

            // realizamos los cambios necesarios y terminamos o informamos el error
        }

        public void DAOUpdateUser(User usuarioUp){
                String sql = "UPDATE users SET name= ? , password= ? , email= ? WHERE user_id = ?";

                try(Connection conn = DBConnect.getConexion();
                PreparedStatement pstmt = conn.prepareStatement(sql)){

                   pstmt.setString(1, usuarioUp.getNombre());
                   pstmt.setString(2, usuarioUp.getPassword());
                   pstmt.setString(3, usuarioUp.getEmail());
                   pstmt.setInt(4, usuarioUp.getId());
                   // ejecutamos la query
                   int filasModificadas = pstmt.executeUpdate();
                   if(filasModificadas > 0){
                       System.out.println("Actualizacion realizada con exito!");
                   }else {
                       System.out.println("No se pudo actualizar la informacion");
                   }


                }catch(SQLException exc){
                        System.out.println("Error al actualizar la informacion del usuario " + exc.getMessage());
                }
        }

        public void DAODeleteUser(User usuario){
                String sql = "DELETE FROM users WHERE user_id= ?";
                try(Connection conn = DBConnect.getConexion()){
                    PreparedStatement pstmt = conn.prepareStatement(sql);
                    pstmt.setInt(1,usuario.getId());
                    pstmt.executeUpdate();
                        System.out.println("usuario eliminado con exito!");
                }catch(SQLException exc){
                        System.out.println("Error al eliminar el usuario" + exc.getMessage());
                }
        }

        // Metodos de hidratacion de objetos
    // utilizando optional
        public Optional<User> DAOGetUser(int user_id){
                String sql = "SELECT * FROM users WHERE user_id= ?";
                User usuario = new User();

                try(Connection conn = DBConnect.getConexion();
                PreparedStatement pstmt = conn.prepareStatement(sql)){

                    pstmt.setInt(1,user_id);
                    ResultSet res = pstmt.executeQuery();
                    if(res.next()){
                        usuario.setUserId(res.getInt("user_id"));
                        usuario.setNombre(res.getString("name"));
                        usuario.setPassword(res.getString("password"));
                        usuario.setEmail(res.getString("email"));
                        return Optional.of(usuario); // si esta devolvemos el usuario
                    }

                    System.out.println("Usuario obtenido");

                }catch(SQLException exc){
                    System.out.println("Error al buscar al usuario " + exc.getMessage());
                }
            return Optional.empty();// caso contrario devolvemos un vacio
        }

        // TODO implementar metodo de busqueda por email(una vez logueado)
        public User DAOGetUserByEmail(String email){
            String sql = "SELECT * FROM users WHERE email = ?";
            User usuario = new User();

            try(Connection conn = DBConnect.getConexion();
                PreparedStatement pstmt = conn.prepareStatement(sql)){

                pstmt.setString(1,email);
                ResultSet res = pstmt.executeQuery();

                if(res.next()){
                    usuario.setUserId(res.getInt("user_id"));
                    usuario.setNombre(res.getString("name"));
                    usuario.setPassword(res.getString("password"));
                    usuario.setEmail(res.getString("email"));
                }

                System.out.println("Usuario obtenido");

            }catch(SQLException exc){
                System.out.println("Error al buscar al usuario " + exc.getMessage());
            }

            return usuario;
        }

        public Map<Integer, User> DAOGetUsers(){
            Map<Integer,User> usuariosCargados = new HashMap<>();

            String sql = "SELECT * FROM users";
            try(Connection conn = DBConnect.getConexion() ){
                PreparedStatement pstmt = conn.prepareStatement(sql);
                ResultSet res = pstmt.executeQuery();
                while(res.next()){
                    // utilizamos el mapa para guarda la informacion de los usuarios
                    usuariosCargados.put(res.getInt("user_id"), new User(
                            res.getInt("user_id"),
                            res.getString("name"),
                            res.getString("password"),
                            res.getString("email")

                    ));
                }
                System.out.println("Usuarios obtenidos exitosamente!");
            }catch(SQLException exc){
                System.out.println("Error al obtener los usuarios " + exc.getMessage());
            }

            return usuariosCargados;
        }

        // utilizamos nuevamente BCrypt para poder verificar si la  pass ingresada es correcta y verificar al usuario
        public boolean DAOVerifyLogin(String email, String passCandidate){
            String sql = "SELECT password FROM users WHERE email = ?";

            try(Connection conn = DBConnect.getConexion();
            PreparedStatement pstmt = conn.prepareStatement(sql)){
                pstmt.setString(1,email);

                ResultSet res = pstmt.executeQuery();
                if(res.next()){
                    String hashDB = res.getString("password");

                    // BCrypt verifica si la pass ingresada coincide con la guardada.
                    return BCrypt.checkpw(passCandidate, hashDB);
                }


            }catch(SQLException exc){
                System.out.println("Error al verificar la pass " + exc.getMessage());
            }
            // caso contrario retornamos un false que sera manejado por el controller
            return false;
        }
}
