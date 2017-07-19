/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Modelo.DAO;

import Controlador.Conexiones.DataConnectionMYSQL;
import Controlador.Conexiones.DataConnectionSQL;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import Vista.Interfaces.SQL2;
import java.util.ArrayList;
import Controlador.VO.Banco;
import Vista.Interfaces.MYSQL;
import Vista.Interfaces.ThrowingErrorLista;
import java.sql.ResultSet;
/**
 *
 * @author PROGRAMADOR
 */
public class DAOBanco implements MYSQL<ArrayList<Banco>>, SQL2<Banco>{
    
    private static DataConnectionMYSQL conexionMYSQL;
    private static DataConnectionSQL conexionSQL;

    public DAOBanco() throws Exception{
        conexionMYSQL = DataConnectionMYSQL.getInstance();
        conexionSQL = DataConnectionSQL.getInstance();
    }
    @Override
    public void eliminar()throws SQLException{
        //
        String DELETE = "DELETE FROM banco;";
        PreparedStatement preparedStmt= conexionMYSQL.getCon().prepareStatement(DELETE);
        preparedStmt.execute();
    } 

    @Override
    public void crear(ArrayList<Banco> lista) throws SQLException {
        //
            lista.forEach((ThrowingErrorLista<Banco>) (Banco b) -> {
                String seleccion = "INSERT INTO banco(codBanco,descripcion) VALUES(?,?);";
                 PreparedStatement preparedStmt= conexionMYSQL.getCon().prepareStatement(seleccion);
                 preparedStmt.setString (1, b.getCodBanco());
                 preparedStmt.setString (2, b.getNombre());
                 preparedStmt.execute();
            });
    }

    @Override
    public ArrayList<Banco> read(int algo) throws SQLException{
        //        
       ArrayList<Banco> lista = new ArrayList<>(); //Creamos un arraylist para meter los datos extraídos de la tabla  
        String sql ="SELECT codBancoSuc,descripcion FROM softland.sw_banco_suc;";
         try {
            PreparedStatement ps = conexionSQL.getCon().prepareStatement(sql);
            ResultSet rs = ps.executeQuery();
            conexionSQL.getCon().commit();
            while(rs.next()){
                //datos de la tabla
               Banco map = new Banco(rs.getString("codBancoSuc"), rs.getString("descripcion"));
               lista.add(map);
            }
        } catch (SQLException e) {
             //System.out.println("eeee "+e.getMessage());            
        }
        return lista;
    }

    @Override
    public void restore() throws SQLException {
        
        String INSERT="INSERT banco SELECT * FROM banco_tmp;";
        PreparedStatement ps;
        ps = conexionMYSQL.getCon().prepareStatement(INSERT);
        ps.executeUpdate();
        //
    }

    @Override
    public void backup() throws SQLException {
//        //
        PreparedStatement ps;  
        String query="CREATE TABLE IF NOT EXISTS banco_tmp LIKE banco;";
        ps = conexionMYSQL.getCon().prepareStatement(query); 
        ps.executeUpdate(); 
        String DELETE="DELETE FROM banco_tmp";
        ps = conexionMYSQL.getCon().prepareStatement(DELETE); 
        ps.executeUpdate();  
        String insert = "INSERT banco_tmp SELECT * FROM banco;";
        ps = conexionMYSQL.getCon().prepareStatement(insert); 
        ps.executeUpdate(); 
    }

    @Override
    public void deleteBackup() throws SQLException {
//        //
        PreparedStatement ps; 
        String DELETE="DELETE FROM banco_tmp";
        ps = conexionMYSQL.getCon().prepareStatement(DELETE); 
        ps.executeUpdate(); 
    }
}
