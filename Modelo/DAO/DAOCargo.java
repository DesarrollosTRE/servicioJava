/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Modelo.DAO;
import Controlador.Conexiones.DataConnectionMYSQL;
import Controlador.Conexiones.DataConnectionSQL;
import Controlador.VO.Cargo;
import Vista.Interfaces.MYSQL;
import Vista.Interfaces.SQL2;
import Vista.Interfaces.ThrowingErrorLista;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
/**
 *
 * @author PROGRAMADOR
 */
public class DAOCargo implements MYSQL<ArrayList<Cargo>>, SQL2<Cargo>{

    private static DataConnectionMYSQL conexionMYSQL;
    private static DataConnectionSQL conexionSQL;

    public DAOCargo()throws Exception {
        conexionMYSQL=DataConnectionMYSQL.getInstance();
        conexionSQL = DataConnectionSQL.getInstance();
    }
    public void crear(ArrayList<Cargo> lista) throws SQLException {
        //
        String seleccion = "INSERT INTO cargo(codCargo,descripcion) VALUES(?,?);";
        lista.forEach((ThrowingErrorLista<Cargo>) cargo -> {
            PreparedStatement preparedStmt;
                preparedStmt = conexionMYSQL.getCon().prepareStatement(seleccion);
                preparedStmt.setString (1, cargo.getCodigo());
                preparedStmt.setString (2, cargo.getNombre());
                preparedStmt.execute();
       });   
    }

    @Override
    public void eliminar() throws SQLException {
        //
        String DELETE = "DELETE FROM cargo;";
        PreparedStatement preparedStmt= conexionMYSQL.getCon().prepareStatement(DELETE);
        preparedStmt.execute();
    }

    @Override
    public ArrayList<Cargo> read(int algo) throws SQLException {
        //
       ArrayList<Cargo> lista = new ArrayList<>(); //Creamos un arraylist para meter los datos extraídos de la tabla  
        String sql ="SELECT CarCod AS codCargo,CarNom AS descripcion FROM softland.cwtcarg;";
         try {
            PreparedStatement ps = conexionSQL.getCon().prepareStatement(sql);
            ResultSet rs = ps.executeQuery();
            conexionSQL.getCon().commit();
            while(rs.next()){
                //datos de la tabla
                Cargo nuevoCargo = new Cargo(rs.getString("codCargo"), rs.getString("descripcion"));
                lista.add(nuevoCargo);
            }
        } catch (SQLException e) {
             //System.out.println("eeee "+e.getMessage());            
        }
        return lista;
    }
    @Override
    public void backup() throws SQLException {
        //
        PreparedStatement ps;  
        String query="CREATE TABLE IF NOT EXISTS cargo_tmp LIKE cargo;";
        ps = conexionMYSQL.getCon().prepareStatement(query); 
        ps.executeUpdate(); 
        String DELETE="DELETE FROM cargo_tmp";
        ps = conexionMYSQL.getCon().prepareStatement(DELETE); 
        ps.executeUpdate(); 
        String insert = "INSERT cargo_tmp SELECT * FROM cargo;";
        ps = conexionMYSQL.getCon().prepareStatement(insert); 
        ps.executeUpdate(); 
    }

    @Override
    public void restore() throws SQLException {;
        
        String INSERT="INSERT cargo SELECT * FROM cargo_tmp;";
        PreparedStatement ps;
        ps = conexionMYSQL.getCon().prepareStatement(INSERT);
        ps.executeUpdate();
        // 
    }

    @Override
    public void deleteBackup() throws SQLException {
        
        String INSERT="DELETE FROM cargo_tmp;";
        PreparedStatement ps;
        ps = conexionMYSQL.getCon().prepareStatement(INSERT);
        ps.executeUpdate();
        // 
    }

}
