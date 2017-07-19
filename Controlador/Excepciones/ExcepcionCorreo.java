/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controlador.Excepciones;

import Controlador.Mail;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.logging.Level;
import java.util.logging.Logger;
/**
 *
 * @author PROGRAMADOR
 */
public class ExcepcionCorreo {

    private int tamañoArray;
    private StringBuilder mensaje;
    private static Mail email;
    private String asunto;
    
    public ExcepcionCorreo() {
        this.asunto="Asunto Default Correo";
        mensaje = new StringBuilder();
        email= new Mail("tomas.escalona24@gmail.com", "88348642TsRe","t.rosales@fundacionier.cl");
      }
//    public MySqlExcepcion(String Mensaje) {
//        super(Mensaje);
//                System.out.println(Mensaje);
//    }
    public void setAsunto(String asunto){
     this.asunto=asunto;
    }
    public String getAsunto(){
     return this.asunto;
    }
    public void setTamañoArray(int tamaño){
     this.tamañoArray=tamaño;
    }
    public int getTamañoArray(){
     return this.tamañoArray;
    }
    public int verificarEnvio(){
        return this.mensaje.length();
    }
//    public void getMensaje(String error, String rut,String centroCosto, String codCargo){
//        this.mensaje=this.mensaje.append("<tr><td><b>Mensaje SQL: </b>").append(error).append("</td><td><b>Ficha: </b>").append(rut).append("</td><td><b>CentroCosto: </b>").append(centroCosto).append("</td><td><b>codCargo: </b>").append(codCargo).append("</td><tr>");
//       
//    }
    public void setMensaje(String mensaje){
     this.mensaje.append("<p>").append(mensaje).append("</p>");
    }
    public void enviarEmail(String funcion,LocalDate fecha,LocalTime hora){
        this.mensaje.insert(0,"<html><head></head><body><h1>Se ha producido un error en la funcion: "+funcion+"</h1>").append("<h2>Fecha: ").append(fecha).append(", Hora: ").append(hora).append("</h2>");
        this.mensaje.append("</body></html>");
        try {
            email.setAsunto(getAsunto());
            email.setMensaje(this.mensaje.toString());
            email.enviarEmail();
        } catch (Exception ex) {
            Logger.getLogger(ExcepcionCorreo.class.getName()).log(Level.SEVERE, null, ex);
        }
        this.mensaje= new StringBuilder();
    }
}
