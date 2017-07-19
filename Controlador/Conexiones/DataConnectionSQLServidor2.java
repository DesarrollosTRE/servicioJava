/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controlador.Conexiones;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 *
 * @author PROGRAMADOR
 */
public class DataConnectionSQLServidor2 {
 
    static Connection con; // atribut per a guardar l’objecte connexió.
    private static DataConnectionSQLServidor2 INSTANCE = null;
 
    /**Método constructor que ejecuta el método para conectar con la base de datos
     *
     */
    private DataConnectionSQLServidor2() throws Exception {
        performConnection();
    }
 
    /**Crea una instancia de la base de datos en caso de no estar creada.
     *
     */
    private synchronized static void createInstance()throws Exception {
        if (INSTANCE == null) {
            INSTANCE = new DataConnectionSQLServidor2();
        }
    }
 
    /**Metodo para retorna una instancia de la conexion. Si no esta creada la crea, y si esta creada la retorna
     * @return retorna una instancia de la conexión a la base de datos
     */
    public static DataConnectionSQLServidor2 getInstance() throws Exception{
        if (INSTANCE == null) createInstance();
        return INSTANCE;
    }
     /**Método para eliminar solamente la instancia de la conexion
     *
     */
    public static void delOnlyInstance(){
        INSTANCE = null;
        con = null;
        System.gc();
    }
    /**Método para eliminar la instancia de la conexión
     *
     */
    public static void delInstance()throws SQLException {
        INSTANCE = null;
        closeConnection();
    }
 
    /**Método que crea la conexión a la base de datos
     *
     */
    public Connection getCon(){
     return con;
    }
    public void performConnection()throws Exception {
        String host = "host";
        String user = "user";
        String pass = "pass";
        String dtbs = "dtbs";
        Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
        String newConnectionURL = "jdbc:sqlserver://" + host + ";databaseName=" + dtbs;
        con = DriverManager.getConnection(newConnectionURL,user,pass);
    }
    /**Método para cerrar la conexión con la base de dades
     *
     */
    public static void closeConnection() throws SQLException {
            con.close();
    }    
}   