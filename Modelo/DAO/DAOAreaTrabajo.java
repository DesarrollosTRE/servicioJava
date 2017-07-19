/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Modelo.DAO;

import Vista.Interfaces.SQL2;
import Vista.Interfaces.MYSQL;
import Vista.Interfaces.ThrowingErrorLista;
import Controlador.Conexiones.DataConnectionMYSQL;
import Controlador.Conexiones.DataConnectionSQL;
import Controlador.VO.AreaTrabajo;
import Controlador.Archivo;
import Controlador.VO.Trabajador;
import com.csvreader.CsvWriter;
import java.io.File;
import java.io.IOException;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
/**
 *
 * @author PROGRAMADOR
 */
public class DAOAreaTrabajo extends Archivo<Trabajador> implements MYSQL<AreaTrabajo>, SQL2<AreaTrabajo>{
    private static DataConnectionMYSQL conexionMYSQL;
    private static DataConnectionSQL conexionSQL;

    public DAOAreaTrabajo()throws Exception{
        conexionMYSQL = DataConnectionMYSQL.getInstance();
        conexionSQL = DataConnectionSQL.getInstance();
        //
    }
    /*
        Si la conexion se perdido enviar una excepcion notificando esto
    */
    @Override
    public void crear(AreaTrabajo clase) throws SQLException {
        //
        String  rutaDatosPersonal;//"C:\\prueba Fichero\\archivo.txt";
        rutaDatosPersonal = "C:\\INTRANET\\".concat(AreaTrabajo.NOMBRE_ARCHIVO);
        File archivo = new File(rutaDatosPersonal);
        if(archivo.exists()){
                String loadQuery =  "LOAD DATA LOCAL INFILE 'C:/INTRANET/"
                                    .concat(AreaTrabajo.NOMBRE_ARCHIVO)
                                    .concat("'")
                                    .concat(" INTO TABLE area_trabajo\n")
                                    .concat(" FIELDS ENCLOSED BY '\"' TERMINATED BY '&' ESCAPED BY '\"'\n")
                                    .concat(" LINES TERMINATED BY '\\r\n';");
                PreparedStatement stmt = conexionMYSQL.getCon().prepareStatement(loadQuery);
                stmt.execute();
                archivo.delete();
         }else{
            throw new SQLException("EL ".concat(AreaTrabajo.NOMBRE_ARCHIVO).concat(" no existe. Se ha borrado."));
        }
    }

    @Override
    public void eliminar() throws SQLException {
        //
        String DELETE = "DELETE FROM area_trabajo;";
        PreparedStatement preparedStmt= conexionMYSQL.getCon().prepareStatement(DELETE);
        preparedStmt.execute();
    }
    /*
        Overload de metodo read en interface
    */
    @Override
    public ArrayList<AreaTrabajo> read(int mes) throws SQLException {
//        conexionMYSQL.getStateConection();
//        //
//        ArrayList<AreaTrabajo> listaArea = new ArrayList<>();
//        String sql ="SELECT rut,softland.sw_ccostoper.codiCC AS centroCosto,SUBSTRING(softland.sw_ccostoper.codiCC,0,6) AS centroCostoAbr,softland.cwtcarg.CarCod AS codCargo,softland.cwtcarg.CarNom AS nombreCargo \n" +
//                "FROM softland.sw_personal INNER JOIN softland.sw_estadoper ON softland.sw_estadoper.ficha=softland.sw_personal.ficha\n" +
//                "INNER JOIN softland.sw_cargoper ON softland.sw_cargoper.ficha=softland.sw_personal.ficha \n" +
//                "INNER JOIN softland.cwtcarg ON softland.cwtcarg.CarCod=softland.sw_cargoper.carCod \n" +
//                "INNER JOIN softland.sw_ccostoper on softland.sw_ccostoper.ficha=softland.sw_personal.ficha \n" +
//                "INNER JOIN softland.cwtccos on softland.cwtccos.CodiCC=softland.sw_ccostoper.codiCC \n" +
//                "WHERE softland.sw_ccostoper.vigHasta LIKE '%9999%' ORDER BY softland.sw_personal.ficha DESC";//"WHERE softland.sw_cargoper.vigHasta LIKE '%9999%'";
//        PreparedStatement ps = conexionSQL.getCon().prepareStatement(sql);
//
////        ArrayList<Trabajador> trabajadorNuevo = new ArrayList<>(); //Creamos un arraylist para meter los datos extraídos de la tabla
//
//        ResultSet rs = ps.executeQuery();
//        conexionSQL.getCon().commit();
//        while(rs.next()){
//            AreaTrabajo nuevaArea = new AreaTrabajo(rs.getString("rut"),rs.getString("centroCosto"), rs.getString("centroCostoAbr"), rs.getString("codCargo"), rs.getString("nombreCargo"));
//            listaArea.add(nuevaArea);
//        }
//        return listaArea;
    return null;
    }
    /*
    CLASE ABSTRACTA
    */
    @Override
    public void agregarDatos(ArrayList<Trabajador> lista, char separador) throws IOException {
       Archivo.crear(AreaTrabajo.NOMBRE_ARCHIVO, "C:\\INTRANET\\");
       //se inicia el archivo
       CsvWriter csvOutput = Archivo.inicializarCsv(separador);
       //
       lista.forEach((ThrowingErrorLista<Trabajador>) areaTra -> {
                csvOutput.write(areaTra.getCodigoArea()+"");   
                csvOutput.write(areaTra.getCentroCosto());                 
                csvOutput.write(areaTra.getCentroCostoAbr());     
                csvOutput.write(areaTra.getNombre()); 
                csvOutput.write(areaTra.getCodigo()); 
                csvOutput.endRecord();
       });
       csvOutput.close();
    }
    @Override
    public void agregarDatos(ArrayList<String> param,boolean isVariables,String nombreArchivo) throws Exception {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }


    @Override
    public void restore() throws SQLException {
        
        String INSERT="INSERT area_trabajo SELECT * FROM area_trabajo_tmp;";
        PreparedStatement ps;
        ps = conexionMYSQL.getCon().prepareStatement(INSERT);
        ps.executeUpdate();
//        //  
    }

    @Override
    public void backup() throws SQLException {
        //
        PreparedStatement ps;  
        String query="CREATE TABLE IF NOT EXISTS area_trabajo_tmp LIKE area_trabajo;";
        ps = conexionMYSQL.getCon().prepareStatement(query); 
        ps.executeUpdate(); 
        String DELETE="DELETE FROM area_trabajo_tmp";
        ps = conexionMYSQL.getCon().prepareStatement(DELETE); 
        ps.executeUpdate(); 
        String insert = "INSERT area_trabajo_tmp SELECT * FROM area_trabajo;";
        ps = conexionMYSQL.getCon().prepareStatement(insert); 
        ps.executeUpdate();  
    }

    @Override
    public void deleteBackup() throws SQLException {
        //
        PreparedStatement ps;  
        String insert = "DELETE FROM area_trabajo_tmp;";
        ps = conexionMYSQL.getCon().prepareStatement(insert); 
        ps.executeUpdate();  
    }

    @Override
    public void agregarDatos(Trabajador param, String nombreArchivo) throws Exception {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

}
