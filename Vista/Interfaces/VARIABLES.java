/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Vista.Interfaces;

import java.util.ArrayList;

/**
 *
 * @author PROGRAMADOR
 */
public interface VARIABLES {
    String NOMBRE_ARCHIVO_P171="variableP171.txt";
    String NOMBRE_ARCHIVO_P276="variableP276.txt";
    
    public ArrayList<String> readVariableP171(int mes) throws Exception;
    public ArrayList<String> readVariableP276(int mes) throws Exception;
}
