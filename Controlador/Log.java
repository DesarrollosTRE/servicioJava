/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controlador;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.util.ArrayList;

/**
 *
 * @author PROGRAMADOR
 */
public class Log extends Archivo<String>{ 

    @Override
    public void agregarDatos(ArrayList<String> param, char separador) throws Exception {
        
    }

    @Override
    public void agregarDatos(String param, String nombreArchivo) throws Exception {
        Archivo.crear(nombreArchivo, "C:\\INTRANET\\");
        BufferedWriter bw;
        if(verificarArchivo()) {
            bw = new BufferedWriter(new FileWriter(getArchivo()));
            bw.write(param);
        } else {
            bw = new BufferedWriter(new FileWriter(getArchivo()));
            bw.write(param);
        }
        bw.close();
    }

    @Override
    public void agregarDatos(ArrayList<String> param, boolean isVariables, String nombreArchivo) throws Exception {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
}
