/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controlador;

import com.csvreader.CsvWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

/**
 *
 * @author PROGRAMADOR
 */
public abstract class Archivo <UnObjeto>{

private static String nombre;
private static String ruta;
private static File archivo;


    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombreArc) {
        nombre = nombreArc;
    }

    public String getRuta() {
        return ruta;
    }

    public void setRuta(String rutaArc) {
        ruta = rutaArc;
    }

    public File getArchivo() {
       return archivo;
    }
    public boolean verificarArchivo(){
     return archivo.exists();
    }
    public static void crear(String nombreArchivo, String rutaArchivo) {
        nombre = nombreArchivo;
        ruta = rutaArchivo;
        //
        ruta=ruta.concat(nombre);
        //
        archivo = new File(ruta);
    }
    
    public static CsvWriter inicializarCsv(char separador) throws IOException{
      CsvWriter csvOutput = new CsvWriter(new FileWriter(archivo, true), '&'); 
      return csvOutput;
    }
    public abstract void agregarDatos(ArrayList<UnObjeto> param,char separador)throws Exception;
    public abstract void agregarDatos(UnObjeto param, String nombreArchivo)throws Exception;
    public abstract void agregarDatos(ArrayList<String> param,boolean isVariables,String nombreArchivo)throws Exception;
}
