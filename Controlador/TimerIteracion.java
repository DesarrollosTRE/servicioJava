/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controlador;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import javax.swing.Timer;

/**
 *
 * @author PROGRAMADOR
 */
public class TimerIteracion {
    private Timer timer;
    int minutosReinicio;
    int horasReinicio = 0;// 5 minutos
    int segundos = 59;
    private Coordinador miCoordinador;
    private final DateFormat segundosFormat = new SimpleDateFormat("ss");
    
    public boolean verificarInicio(){
        try {
            return this.timer.isRunning(); 
        } catch (java.lang.NullPointerException e) {
            return false;
        }
    }
    public void detenerTimer(){
        this.horasReinicio = 0;
        this.minutosReinicio = 0;
        this.segundos = 59;
        try {
            timer.stop();
//            timer = null;
        } catch (NullPointerException e) {
        }
    }
    public void init(int minutosIteracionActual ,int minutosProximaIteracion) {
        System.out.println("minutosIteracionActual "+minutosIteracionActual);
        System.out.println("minutosProximaIteracion "+minutosProximaIteracion);
        segundos = 60 ;
        /**
         *Verifico que los minutos de la iteracion actual sean menor a los de la proxima iteracion Esto para que la resta se con signo positivo 
         */
        minutosReinicio = (minutosIteracionActual > minutosProximaIteracion) ? (minutosIteracionActual - minutosProximaIteracion) : (minutosProximaIteracion - minutosIteracionActual);
        //Verifico que los minutos sean mayor a 0, para que no muestre -1 al llegar a 0 minutos
        if(minutosReinicio > 0 || minutosReinicio == 60) minutosReinicio--;
        
        //Mientras los minutos de reinicio sean mayor a 60, agrego una hora. Transformo los minutos en Horas 
        while(minutosReinicio > 60){
            horasReinicio++;
            minutosReinicio-=60;
        }
        
        if(horasReinicio > 0 ) miCoordinador.setLblHoraIteracion("0"+String.valueOf(horasReinicio));
        // muestro todo en la vista
        miCoordinador.setLSegundosIteracion(String.valueOf(segundos));
        miCoordinador.setLMinutosIteracion((String.valueOf(minutosReinicio).length()>1) ? String.valueOf(minutosReinicio) : "0"+String.valueOf(minutosReinicio));
        timer = new Timer(1000, new CountdownTimerListener());
        timer.start(); 
    }

    public Coordinador getMiCoordinador() {
        return miCoordinador;
    }

    public void setMiCoordinador(Coordinador miCoordinador) {
        this.miCoordinador = miCoordinador;
    }
    
    class CountdownTimerListener implements ActionListener {
          @Override
          public void actionPerformed(ActionEvent e) {
             if (--segundos > 0) { //minutosReinicio != 0
                 miCoordinador.setLSegundosIteracion((segundos >= 10) ? String.valueOf(segundos) : "0"+segundos);
             } else {
                 if(segundos == 0) miCoordinador.setLSegundosIteracion("00");
                 segundos = 60;
                 --minutosReinicio;
                 if(minutosReinicio == 0 && horasReinicio > 0){
                    //debo reiniciar los minutos y segundos ademas de restar una hora para seguir con el timer
                    minutosReinicio = 59;
                    horasReinicio--;
                    segundos = 60;
                    miCoordinador.setLblHoraIteracion((horasReinicio >= 10) ? String.valueOf(horasReinicio) : "0"+horasReinicio);
                 }
                 miCoordinador.setLMinutosIteracion((minutosReinicio == -1) ? "00" : (minutosReinicio >= 10) ? String.valueOf(minutosReinicio) : "0"+minutosReinicio); 
                                                               
              }
          }
      } 
}
