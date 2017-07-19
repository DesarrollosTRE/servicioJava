/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Vista.Interfaces;

import java.sql.SQLException;

/**
 *
 * @author PROGRAMADOR
 */
public interface MYSQL <unObjecto>{
    public void crear(unObjecto objeto) throws SQLException;
    public void eliminar() throws SQLException;
    public void restore() throws SQLException;   
    public void backup() throws SQLException;
    public void deleteBackup() throws SQLException;
    
}
