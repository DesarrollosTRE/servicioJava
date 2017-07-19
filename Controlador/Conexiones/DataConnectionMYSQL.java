/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controlador.Conexiones;

import Controlador.Excepciones.ExcepcionCorreo;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 *
 * @author PROGRAMADOR
 */
public class DataConnectionMYSQL{
    
    static Connection con; // atribut per a guardar l’objecte connexió.
    static ExcepcionCorreo excepciones;
    private static DataConnectionMYSQL INSTANCE = null;
    
    /**Método constructor que ejecuta el método para conectar con la base de datos
     *
     */
    private DataConnectionMYSQL()throws Exception {
        excepciones = new ExcepcionCorreo();
        performConnection();
    }
 
    /**Crea una instancia de la base de datos en caso de no estar creada.
     *
     */
    private synchronized static void createInstance()throws Exception {
        if (INSTANCE == null) {
            INSTANCE = new DataConnectionMYSQL();
        }
    }
 
    /**Metodo para retorna una instancia de la conexion. Si no esta creada la crea, y si esta creada la retorna
     * @return retorna una instancia de la conexión a la base de datos
     */
    public static DataConnectionMYSQL getInstance()throws Exception {
        if (INSTANCE == null) createInstance();
        return INSTANCE;
    }
    public static boolean verificarInstance(){
        if(INSTANCE == null) return false;
        return true;
    }
//    /**Método para eliminar la instancia de la conexión
//     * @return 
//     */
//    public static boolean getStateConection(){
//        try {
//            return con.isClosed();
//        } catch (SQLException ex) {
//            return true;
//        }
//    }
    /*
    
    */
    public static void delInstance() throws Exception{
        INSTANCE = null;
        closeConnection();
    }
    public static void delOnlyInstance(){
        INSTANCE = null;
        con = null;
    }
     public Connection getCon(){
        return con;
    }
    /**Método que crea la conexión a la base de datos
     *
     * @throws java.lang.Exception
     */
    public void performConnection()throws Exception {
        String host = "host";
        String user = "user";
        String pass = "pass";
        String dtbs = "dtbs";
        
        Class.forName("com.mysql.jdbc.Driver");
        String newConnectionURL = "jdbc:mysql://" + host + "/" + dtbs+"?rewriteBatchedStatements=true&relaxAutoCommit=true";//&autoReconnect=true
        con = DriverManager.getConnection(newConnectionURL,user,pass);
    }
    /**Método para cerrar la conexión con la base de dades
     *
     * @throws java.sql.SQLException
     */
    public static void closeConnection()throws SQLException {
            con.close();
    }
}
