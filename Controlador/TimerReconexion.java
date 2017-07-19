/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controlador;

import Controlador.Conexiones.DataConnectionMYSQL;
import Controlador.Conexiones.DataConnectionSQL;
import Controlador.Conexiones.DataConnectionSQLServidor2;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.Timer;
import java.awt.Color;
import javax.swing.SwingWorker;

/**
 *
 * @author PROGRAMADOR
 */
public class TimerReconexion{
    
     private Timer countdownTimer;
     int timeRemaining =5;
     private Coordinador miCoordinador;     

    public void init() {
        miCoordinador= Coordinador.getInstance();
        countdownTimer = new Timer(1000, new CountdownTimerListener());
        countdownTimer.start(); 
    }
    
    public Coordinador getMiCoordinador() {
        return miCoordinador;
    }
    public void setMiCoordinador(Coordinador miCoordinador) {
        this.miCoordinador = miCoordinador;
    }   
   
class Working extends SwingWorker<String, Void>{
 @Override
    protected String doInBackground(){
        // Se vuelve a establecer la conexion con mysql
           try{    
                DataConnectionMYSQL.getInstance();
                DataConnectionSQL.getInstance();
                DataConnectionSQLServidor2.getInstance();
            }catch (Exception ex) {                 
                DataConnectionMYSQL.delOnlyInstance();
                DataConnectionSQL.delOnlyInstance();
                DataConnectionSQLServidor2.delOnlyInstance();
                cancel(true);
                
                miCoordinador.mostrarMensajeReeconexion(ex.getLocalizedMessage());
                miCoordinador.setLblMensajeEstadoConexion("Reeconectando en...");
        }
    return "";
    }
   @Override
   protected void done() {
    //       Mostramos el nombre del hilo para ver que efectivamente esto
    //       se ejecuta en el hilo de eventos.
          if(isCancelled()){
               //siginifica que ocurrio un error y se cancelo
               timeRemaining =5;
               countdownTimer.restart();
          }else if(isDone()){
            miCoordinador.inicarDAOS();                     
            miCoordinador.cambiarEstadoLblTiempoReconexion("OK",Color.green);
            miCoordinador.setLblMensajeEstadoConexion("Conexión con MYSQL.");
            miCoordinador.verificarProcesosActivos();  
          }
}
}
class CountdownTimerListener implements ActionListener {
          @Override
          public void actionPerformed(ActionEvent e) {
             if (--timeRemaining > 0) {
                 miCoordinador.setLblMensajeEstadoConexion("Reeconectando en...");
                 miCoordinador.cambiarEstadoLblTiempoReconexion(String.valueOf(timeRemaining),Color.red);
              } else {   
                countdownTimer.stop();
                new Working().execute();
                miCoordinador.cambiarEstadoLblTiempoReconexion("Conectando ...",Color.red);
                miCoordinador.setLblMensajeEstadoConexion("Estableciendo conexión con MYSQL...");   
                                                               
              }
          }
      }  
}
