/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controlador;

import Modelo.Logica;
import Modelo.LogicaProcesos;
import Vista.principal;
import java.awt.Color;
/**
 *
 * @author PROGRAMADOR
 */
public class Principal {
    private Logica miLogica;
    private LogicaProcesos miLogicaProcesos;
    private Coordinador miCoordinador;
    private static principal ventanaPrincipal;
    private TimerIteracion miTimerIteracion;
    
     /**
     * @param args the command line arguments
     */
    private void iniciar (){
        this.miCoordinador= Coordinador.getInstance();
        this.miLogicaProcesos = new LogicaProcesos();
        this.miLogica = new Logica();
        this.miTimerIteracion = new TimerIteracion();
        
        ventanaPrincipal = new principal();
                
        this.miLogica.setCoordinador(miCoordinador);
        this.miLogica.setLogicaProcesos(miLogicaProcesos);
        this.miLogicaProcesos.setCoordinador(miCoordinador);
         
        ventanaPrincipal.setCoordinador(miCoordinador);         
        //
        this.miCoordinador.setMiLogica(miLogica);
        this.miCoordinador.setMiLogicaProcesos(miLogicaProcesos);
        this.miCoordinador.setVentanaPrincipal(ventanaPrincipal);
        this.miCoordinador.setMiTimerIteracion(miTimerIteracion);
        //
        this.miTimerIteracion.setMiCoordinador(miCoordinador);
        //
        ventanaPrincipal.cambiarColorLblTiempoConexion("Conectando..",Color.red);                  
        miLogica.iniciarComponentes();
        ventanaPrincipal.setVisible(true);
        try {
            int nuevaHoraInicioEnMinutos = this.miLogicaProcesos.calcularHoraReinicio();
            this.miCoordinador.iniciarTodosLosServicios(nuevaHoraInicioEnMinutos);

        } catch (NullPointerException e) {
            
        }
    }
    public static void main(String args[]) {
        Principal inicio = new Principal();
        inicio.iniciar();
    }
}
