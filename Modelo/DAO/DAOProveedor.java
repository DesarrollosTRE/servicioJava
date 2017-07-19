/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Modelo.DAO;

import Controlador.Conexiones.DataConnectionSQLServidor2;
import Controlador.Conexiones.DataConnectionMYSQL;
import Controlador.Archivo;
import Vista.Interfaces.MYSQL;
import Vista.Interfaces.SQL2;
import Controlador.VO.Proveedor;
import com.csvreader.CsvWriter;
import java.io.File;
import java.io.IOException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

/**
 *
 * @author PROGRAMADOR
 */
public class DAOProveedor extends Archivo<Proveedor> implements MYSQL<Proveedor>,SQL2<Proveedor>{
    
private static DataConnectionSQLServidor2 con;
private static DataConnectionMYSQL conexionMYSQL;

    public DAOProveedor()throws Exception{
        con= DataConnectionSQLServidor2.getInstance();
        conexionMYSQL = DataConnectionMYSQL.getInstance();
    }
/*PROVEEDORES*/
    @Override
    public void crear(Proveedor algo) throws SQLException {
        //
        String  rutaDatosPersonal = "C:\\INTRANET\\".concat(Proveedor.NOMBRE_ARCHIVO);//"C:\\prueba Fichero\\archivo.txt";
        File archivo = new File(rutaDatosPersonal);
        if(archivo.exists()){
                String loadQuery =  "LOAD DATA LOCAL INFILE 'C:/INTRANET/archivoProveedor.csv'"
                                  + " INTO TABLE proveedores_ordenCompra \n" +
                                    " FIELDS ENCLOSED BY '\"' TERMINATED BY '&' ESCAPED BY '\"'\n" +
                                    " LINES TERMINATED BY '\\r\n';";
                PreparedStatement stmt = conexionMYSQL.getCon().prepareStatement(loadQuery);
                stmt.execute();
                archivo.delete();
         }else{
            throw new SQLException("EL ".concat(Proveedor.NOMBRE_ARCHIVO).concat(" no existe. Se ha borrado."));
        }
    }

    @Override
    public void eliminar() throws SQLException {
        //
        String DELETE = "DELETE FROM proveedores_ordencompra;";
        PreparedStatement preparedStmt= conexionMYSQL.getCon().prepareStatement(DELETE);
        preparedStmt.execute();
    }

    @Override
    public ArrayList<Proveedor> read(int algo) throws Exception {
        //
        ArrayList<Proveedor> ls = new ArrayList<>(); //Creamos un arraylist para meter los datos extraídos de la tabla  
        String sql ="SELECT CodAux,NomAux FROM softland.cwtauxi;";
        PreparedStatement ps = con.getCon().prepareStatement(sql);
        ResultSet rs = ps.executeQuery();
        con.getCon().commit();
        while(rs.next()){
           //datos de la tabla
            Proveedor nuevoProveedor = new Proveedor(rs.getString("CodAux"), rs.getString("NomAux"));
            ls.add(nuevoProveedor);
        }
        return ls;
    }
    /*
    CLASE ABSTRACTA
    */
    @Override
    public void agregarDatos(ArrayList<Proveedor> lista, char separador) throws Exception {
       Archivo.crear(Proveedor.NOMBRE_ARCHIVO, "C:\\INTRANET\\");
       //se inicia el archivo
       CsvWriter csvOutput = Archivo.inicializarCsv(separador);
       //
       lista.forEach(proveedor -> {
            try {
                csvOutput.write(proveedor.getCodigoAuxiliar());   
                csvOutput.write(proveedor.getNombreAuxiliar());                 
                csvOutput.endRecord();
            } catch (IOException ex) {
                //
                
            }
       });
       csvOutput.close();
    }
    @Override
    public void agregarDatos(ArrayList<String> param,boolean isVariables,String nombreArchivo) throws IOException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    @Override
    public void backup() throws SQLException {
        //
        PreparedStatement ps;  
        String query="CREATE TABLE IF NOT EXISTS proveedor_tmp LIKE proveedores_ordencompra;";
        ps = conexionMYSQL.getCon().prepareStatement(query); 
        ps.executeUpdate(); 
                //
        String DELETE="DELETE FROM proveedor_tmp;";
        ps = conexionMYSQL.getCon().prepareStatement(DELETE); 
        ps.executeUpdate(); 
        
        String insert = "INSERT proveedor_tmp SELECT * FROM proveedores_ordencompra;";
        ps = conexionMYSQL.getCon().prepareStatement(insert); 
        ps.executeUpdate(); 
    }

    @Override
    public void restore() throws SQLException {
                
        String INSERT="INSERT proveedores_ordencompra SELECT * FROM proveedor_tmp;";
        PreparedStatement ps;
        ps = conexionMYSQL.getCon().prepareStatement(INSERT);
        ps.executeUpdate(); 
    }

    @Override
    public void deleteBackup() throws SQLException {
                
        String INSERT="DELETE FROM proveedor_tmp;";
        PreparedStatement ps;
        ps = conexionMYSQL.getCon().prepareStatement(INSERT);
        ps.executeUpdate();
    }

    @Override
    public void agregarDatos(Proveedor param, String nombreArchivo) throws Exception {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
}
