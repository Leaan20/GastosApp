package org.GastosApp.CSV;

import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.FileReader;
import java.io.IOException;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.opencsv.*;
import com.opencsv.exceptions.CsvValidationException;

import org.GastosApp.model.Account;
import org.GastosApp.model.User;

public class PersistenceCSV {
    private final String filePath;

    public PersistenceCSV(String path){
        this.filePath = path;
    }

    // Metodos de usuarios

    // Esto devolvera una lista de usuarios en el sistema(corresponde a un archivo unico)
    public Map<Integer, User> usersLoad() throws IOException{
        Map<Integer, User> usuariosCargados = new HashMap<>();


        try(FileReader fr = new FileReader(filePath);
            // usamos el CSVReaderBuilder para poder saltear las lineas de los titulos
            CSVReader csvReader = new CSVReaderBuilder(fr).withSkipLines(1).build()){

            String[] fila;

                /* Formato de los datos del archivo
                    id | nombre de usuario | password
                *
                * */

            while((fila = csvReader.readNext()) != null){
                int userId = Integer.parseInt(fila[0]);
                String nombre = fila[1];
                String pass = fila[2]; // TODO esto debe mejorarse
                // lo ingresamos al usuario al map
                String email = "prueba@gmail.com";
                usuariosCargados.put(userId ,new User(userId, nombre,pass,email));
            }
            // usamos el formato try-with-resources se autocierra el archivo
//                csvReader.close();
            return usuariosCargados;

            // Capturamos la excepcion en el caso que no se encuentre el archivo o este mal la ruta
        } catch(FileNotFoundException exc){
            System.out.println("Archivo no encontrado");
            System.out.println(exc.getMessage());
        } catch (IOException | CsvValidationException exc) {
            throw new RuntimeException("Error procesando el CSV: " + exc.getMessage(), exc);
        }
        // en caso de que no hay informacion la lista vacia
        return usuariosCargados;
    }

    public void usersSave(Map<Integer, User> usuarios) throws IOException{
        // arrayList necesario para escribir el archivo
        List<String[]> datos = new ArrayList<String[]>();
        // titulo del archivo
        String[] titulo = {"id_usuario", "nombre","password"};
        // agregamos primero el titulo
        datos.add(titulo);

        for (User us: usuarios.values()){
            // anadimos a la lista como un array de strings
            datos.add(new String[] {
                    Integer.toString(us.getId()),
                    us.getNombre(),
                    us.getPassword()
            });
        }
        try(FileWriter fr = new FileWriter(filePath);
            CSVWriter csvWriter = new CSVWriter(fr)){
            csvWriter.writeAll(datos);

        }catch(IOException exc){
            System.out.println("Error de escritura " + exc.getMessage());
        }
    }






    // Metodos de cuentas
    public Map<Integer, Account> accountsLoad() throws IOException{
            Map<Integer, Account> cuentasCargadas = new HashMap<>();

            try(FileReader fr = new FileReader(filePath);
                // usamos el CSVReaderBuilder para poder saltear las lineas de los titulos
                CSVReader csvReader = new CSVReaderBuilder(fr).withSkipLines(1).build()){

                String[] fila;

                /* Formato de los datos del archivo
                    id | nombre de la cuenta | monto | userID(usuario asociado)
                *
                * */

                while((fila = csvReader.readNext()) != null){
                    int id = Integer.parseInt(fila[0]);
                    BigDecimal monto = new BigDecimal(fila[2]);
                    String nombre = fila[1];
                    int userId = Integer.parseInt(fila[3]);
                    cuentasCargadas.put(id , new Account(id,monto,nombre,userId));
                }
                // usamos el formato try-with-resources se autocierra el archivo
//                csvReader.close();
                return cuentasCargadas;

                // Capturamos la excepcion en el caso que no se encuentre el archivo o este mal la ruta
            } catch(FileNotFoundException exc){
                System.out.println("Archivo no encontrado");
                System.out.println(exc.getMessage());
            } catch (IOException | CsvValidationException exc) {
                throw new RuntimeException("Error procesando el CSV: " + exc.getMessage(), exc);
            }
            // en caso de que no hay informacion la lista vacia
            return cuentasCargadas;
    }

    public void accountsSave(Map<Integer, Account> cuentas) throws IOException{
        // lista auxiliar para almacenar los datos
        List<String[]> datos = new ArrayList<>();

        // generamos el titulo para las celdas
        String[] titulo = {"id_cuenta", "nombre_cuenta", "monto", "id_usuario"};
        datos.add(titulo);

        // cargamos los datos de las cuentas
        for(Account acc : cuentas.values()){
            datos.add(new String[]{
                    Integer.toString(acc.getAccountId()),
                    acc.getNombre(),
                    Double.toString(acc.getMonto()),
                    Integer.toString(acc.getUserId())
            });
        }
            try(FileWriter fr = new FileWriter(filePath);
                CSVWriter csvWriter = new CSVWriter(fr)){

                csvWriter.writeAll(datos);

            }catch(IOException exc){
                System.out.println("Error de escritura " + exc.getMessage());
            }
    }





}
