/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Modelo;

import Controlador.Coordinador;
import Controlador.Log;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import Controlador.Servicio;
import Modelo.DAO.DAOAreaTrabajo;
import Modelo.DAO.DAOBanco;
import Modelo.DAO.DAOCargo;
import Modelo.DAO.DAOProveedor;
import Modelo.DAO.DAOTrabajador;
import Vista.Interfaces.MENSAJES;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.concurrent.ScheduledExecutorService;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author PROGRAMADOR
 */
public class LogicaProcesos implements MENSAJES{
    private final Servicio servicios;
    private ScheduledExecutorService servicioCreacionVariables;
    private ScheduledExecutorService servicioGestionTrabajador;
    private ScheduledExecutorService servicioGestionProveedor;
    private final HashMap<String, Boolean> serviciosIniciados;
    private Runnable servicioTrabajador;
    private Runnable servicioVariables;
    private Runnable servicioProveedor;
    private Coordinador miCoordinador;  
    private Date date;
    private final int[] intervalosHoras;
    private final DateFormat hourFormat = new SimpleDateFormat("HH");

    //
    private final HashMap<String,String[]> respuestaProcesos;
    private final String[] mensajesInternos;
    public static int TIEMPO_INTERVALO_ESTABLECIDO_MIN=120;//2 HORAS
    public static int TIEMPO_AGREGADO_INTERVALO_TRABAJADOR_MIN=4;
    public static int TIEMPO_AGREGADO_INTERVALO_VARIABLES_MIN=1;
    public static int TIEMPO_AGREGADO_INTERVALO_PROVEEDOR_MIN=2;
    
    public static int TIEMPO_AGREGADO_INTERVALO_VARIABLES_ERROR_MYSQL_MIN=0;
    public static int TIEMPO_AGREGADO_INTERVALO_PROVEEDOR_ERROR_MYSQL_MIN=0;
    
    public static final int TIEMPO_RETRASO_INICIO_TRABAJADOR_MIN=4;
    public static final int TIEMPO_RETRASO_INICIO_VARIABLES_MIN=1;
    public static final int TIEMPO_RETRASO_INICIO_PROVEEDOR_MIN=2;
    
    private volatile boolean estadoProcesoTrabajador;
    private volatile boolean estadoProcesoVariables;
    private volatile boolean estadoProcesoProveedor;
    /*
        CONSTRUCTOR
    */
    public LogicaProcesos() {
        respuestaProcesos = new HashMap<>();
        serviciosIniciados = new HashMap<>();
        mensajesInternos = new String[2];
        servicios = new Servicio();
        intervalosHoras = new int[]{8,10,12,14,16,18,20,22};
        instanciarRunnable();
    }
    /*
        GETTER AND SETTER
    */
    public void setEstadoProcesoTrabajador(boolean estadoProcesoTrabajador) {
        this.estadoProcesoTrabajador = estadoProcesoTrabajador;
    }
    public void setEstadoProcesoVariables(boolean estadoProcesoVariables) {
        this.estadoProcesoVariables = estadoProcesoVariables;
    }
    public void setEstadoProcesoProveedor(boolean estadoProcesoProveedor) {
        this.estadoProcesoProveedor = estadoProcesoProveedor;
    }   
    public void setCoordinador(Coordinador coordinador){
        miCoordinador=coordinador;
    }
    public boolean isServicioIniciado(String nombre){
      if(serviciosIniciados.containsKey(nombre)){
           return serviciosIniciados.get(nombre);
      }
        return false;
    }
   
    public HashMap<String,Boolean> getAllProcesos(){
        return serviciosIniciados;
    }
    /*
        GETTER AND SETTER
    */
    
    /*
        FUNCIONES PROPIAS DE LA CLASE
    */
    /**
     * 
     * @param task
     * @param period
     * @param unit
     * @param executor
     * @param timeInitial
     * @param nombreServicio 
     */
    public void correrServicio(Runnable task, long period, TimeUnit unit, ScheduledExecutorService executor,int timeInitial,String nombreServicio) {
        servicios.agregarRunnable(task);
        servicios.correr(executor,timeInitial, period, unit);
        serviciosIniciados.put(nombreServicio,Boolean.TRUE);
    }
    /**
     * Funcion calcularHoraReinicio. Calcula la diferencia entre la hora actual y la proxima iteracion. Esto solamente para casos en los que se cae la conexion MYSQL.
     * @return 
     */
    public int calcularHoraReinicio(){
        date = new Date();
        DateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");
        String[] arrayHoraActual = dateFormat.format(date).split(":");
        int diferenciaHoraEnMinutos = 0,restaHoras,horaEnMinutos,horaActual,minutosHoraActual;
        //verifico si la hora es par
        for(int horasIntervalos : intervalosHoras){
            
            if(Integer.parseInt(arrayHoraActual[0]) % 2 == 0) return diferenciaHoraEnMinutos;
            //verifico que la hora actual sea menor a una hora dentro del intervalo ya establecido
            if(Integer.parseInt(arrayHoraActual[0]) < horasIntervalos) 
            {
                horaActual = Integer.parseInt(arrayHoraActual[0]);
                minutosHoraActual = Integer.parseInt(arrayHoraActual[1]);
                //
                restaHoras = horasIntervalos - horaActual;
                if(restaHoras > 1){
                    //quiere decir que es mas de una hora de diferencia
                    //multiplico este resultado por 60
                    horaEnMinutos = restaHoras * 60;
                    diferenciaHoraEnMinutos = horaEnMinutos - minutosHoraActual ;
                }else if(restaHoras <= 1){
                    //si es menor o igual a 1, quiere decir que la diferencia en hora es 0 por lo cual solo debo sacar la diferencia en minutos
                    minutosHoraActual = 60 - minutosHoraActual;
                    diferenciaHoraEnMinutos = minutosHoraActual;
                }
            break;
            }
        }
        return diferenciaHoraEnMinutos+TIEMPO_AGREGADO_INTERVALO_VARIABLES_MIN;
    }
    private void verificarHilos(){
       date = new Date();
        if(Integer.parseInt(hourFormat.format(date)) == 22){
           try {
               String informacionLog = miCoordinador.rescatarInformacionLog();
               Log nuevoLog = new Log();
               DateFormat dateFormat = new SimpleDateFormat("dd_MM_yyyy");
               nuevoLog.agregarDatos(informacionLog, "Log ".concat(dateFormat.format(date)).concat(".txt"));
               System.exit(0);
           } catch (Exception ex) {
               Logger.getLogger(LogicaProcesos.class.getName()).log(Level.SEVERE, null, ex);
           }
        }
    }
    private void instanciarRunnable(){
            servicioTrabajador = new Runnable() {
                @Override
                public void run() {
                    verificarHilos();
                    miCoordinador.gestionarTrabajador();
                }
            };
            servicioVariables = new Runnable() {
                @Override
                public void run() {
                    verificarHilos();
                    miCoordinador.gestionarVariables();
                }
            };   
            servicioProveedor= new Runnable() {
                @Override
                public void run() {
                    verificarHilos();
                    miCoordinador.gestionarProveedor();
                }
            };
    }
        /**
         * Funcion verificarInicioTodosLosServicios. Funcion que se llama cuando se vuelve a establecer la conexion en MYSQL perdida.
         * @param tiempo
         * @return 
         */
    public HashMap<String,String[]> verificarInicioTodosLosServicios(int tiempo){   
        try {
            servicioCreacionVariables = Executors.newScheduledThreadPool(1); 
            servicioGestionProveedor = Executors.newScheduledThreadPool(1); 
            servicioGestionTrabajador = Executors.newScheduledThreadPool(1); 
            //
            setEstadoProcesoProveedor(true);
            setEstadoProcesoTrabajador(true);
            setEstadoProcesoVariables(true);
            //
            //establezco los tiempos de intervalo en error
            TIEMPO_AGREGADO_INTERVALO_VARIABLES_ERROR_MYSQL_MIN = tiempo+TIEMPO_RETRASO_INICIO_VARIABLES_MIN;
            TIEMPO_AGREGADO_INTERVALO_PROVEEDOR_ERROR_MYSQL_MIN = tiempo+TIEMPO_RETRASO_INICIO_PROVEEDOR_MIN;   
            miCoordinador.iniciarTimerIteracion(TIEMPO_AGREGADO_INTERVALO_VARIABLES_ERROR_MYSQL_MIN,0);
            
            correrServicio(servicioVariables,(TIEMPO_INTERVALO_ESTABLECIDO_MIN+TIEMPO_AGREGADO_INTERVALO_VARIABLES_MIN),TimeUnit.MINUTES,servicioCreacionVariables,TIEMPO_AGREGADO_INTERVALO_VARIABLES_ERROR_MYSQL_MIN,MENSAJES.PROCESO_VARIABLE);             
            correrServicio(servicioProveedor,(TIEMPO_INTERVALO_ESTABLECIDO_MIN+TIEMPO_AGREGADO_INTERVALO_PROVEEDOR_MIN),TimeUnit.MINUTES,servicioGestionProveedor,TIEMPO_AGREGADO_INTERVALO_PROVEEDOR_ERROR_MYSQL_MIN,MENSAJES.PROCESO_PROVEEDOR);
            correrServicio(servicioTrabajador,(TIEMPO_INTERVALO_ESTABLECIDO_MIN+TIEMPO_AGREGADO_INTERVALO_TRABAJADOR_MIN),TimeUnit.MINUTES,servicioGestionTrabajador,TIEMPO_AGREGADO_INTERVALO_TRABAJADOR_MIN,MENSAJES.PROCESO_TRABAJADOR);  
            mensajesInternos[0] = MENSAJES.TITULO_PROCESO_SUCCESS.concat("Proveedor,Trabajador,Variables.");
            mensajesInternos[1] = "TRUE";
            respuestaProcesos.put(MENSAJES.PROCESO_TODOS,mensajesInternos);
        } catch (Exception e) {
            mensajesInternos[0] = MENSAJES.TITULO_PROCESO_ERROR.concat("Proveedor,Trabajador,Variables ").concat(e.getLocalizedMessage());
            mensajesInternos[1] = "FALSE";
            respuestaProcesos.put(MENSAJES.PROCESO_TODOS,mensajesInternos);
        }
        return respuestaProcesos;
    }
    /**
     * Funcion verifivarInicioServicioProveedor.Se llama cuando se perdio la conexion con MYSQL y este proceso estaba previamente iniciada.
     * @param tiempo
     * @return 
     */
        public HashMap<String,String[]> verifivarInicioServicioProveedor(int tiempo){
        try {
             setEstadoProcesoProveedor(true);
             servicioGestionProveedor = Executors.newScheduledThreadPool(1); 
             correrServicio(servicioProveedor,(TIEMPO_INTERVALO_ESTABLECIDO_MIN+TIEMPO_AGREGADO_INTERVALO_PROVEEDOR_MIN),TimeUnit.MINUTES,servicioGestionProveedor,tiempo,MENSAJES.PROCESO_PROVEEDOR);
             mensajesInternos[0]= MENSAJES.TITULO_PROCESO_SUCCESS.concat(MENSAJES.PROCESO_PROVEEDOR);
             mensajesInternos[1]= "TRUE";
             respuestaProcesos.put(MENSAJES.PROCESO_PROVEEDOR,mensajesInternos);
        } catch (Exception e) {
            mensajesInternos[0] = MENSAJES.TITULO_PROCESO_ERROR.concat(MENSAJES.PROCESO_PROVEEDOR).concat(", ").concat(e.getLocalizedMessage());
            mensajesInternos[1] =  "FALSE";
            
            respuestaProcesos.put(MENSAJES.PROCESO_PROVEEDOR,mensajesInternos);
        }
        return respuestaProcesos;
    }
  
         /**
         * Funcion verifivarIniciarServicioVariables. Se llama cuando se perdio la conexion con MYSQL y este proceso estaba previamente iniciada.
         * @param nuevaHoraInicioEnMinutos
         * @return 
         */
        public HashMap<String,String[]> verifivarIniciarServicioVariables(int nuevaHoraInicioEnMinutos){
        try {
            setEstadoProcesoVariables(true);
            servicioCreacionVariables = Executors.newScheduledThreadPool(1); 
            correrServicio(servicioVariables,(TIEMPO_INTERVALO_ESTABLECIDO_MIN+TIEMPO_AGREGADO_INTERVALO_VARIABLES_MIN),TimeUnit.MINUTES,servicioCreacionVariables,nuevaHoraInicioEnMinutos,MENSAJES.PROCESO_VARIABLE);
            mensajesInternos[0] = MENSAJES.TITULO_PROCESO_SUCCESS.concat(MENSAJES.PROCESO_VARIABLE);
            mensajesInternos[1] = "TRUE";
            respuestaProcesos.put(MENSAJES.PROCESO_VARIABLE,mensajesInternos);
        } catch (Exception e) {
            mensajesInternos[0] = MENSAJES.TITULO_PROCESO_ERROR.concat(MENSAJES.PROCESO_VARIABLE).concat(", ").concat(e.getLocalizedMessage());
            mensajesInternos[1] = "TRUE";
            respuestaProcesos.put(MENSAJES.PROCESO_VARIABLE,mensajesInternos);
        }
        return respuestaProcesos;
    }
    /**
     * Funcion verifivarIniciarServicioTrabajador. Se llama cuando se pierde la conexion con MYSQL y el servicio Trabajador esta previamente iniciado.
     * @param nuevaHoraInicioEnMinutos
     * @return 
     */
    public HashMap<String,String[]> verifivarIniciarServicioTrabajador(int nuevaHoraInicioEnMinutos){ 
        try {
                setEstadoProcesoTrabajador(true);
                servicioGestionTrabajador = Executors.newScheduledThreadPool(1);
                correrServicio(servicioTrabajador,(TIEMPO_INTERVALO_ESTABLECIDO_MIN+TIEMPO_AGREGADO_INTERVALO_TRABAJADOR_MIN),TimeUnit.MINUTES,servicioGestionTrabajador,nuevaHoraInicioEnMinutos,MENSAJES.PROCESO_TRABAJADOR);
                mensajesInternos[0] = MENSAJES.TITULO_PROCESO_SUCCESS.concat(MENSAJES.PROCESO_TRABAJADOR);
                mensajesInternos[1] = "TRUE";
                respuestaProcesos.put(MENSAJES.PROCESO_TRABAJADOR,mensajesInternos);
        } catch (Exception e) {          
            mensajesInternos[0] = MENSAJES.TITULO_PROCESO_ERROR.concat(MENSAJES.PROCESO_TRABAJADOR).concat(", ").concat(e.getLocalizedMessage());
            mensajesInternos[1] = "FALSE";
            respuestaProcesos.put(MENSAJES.PROCESO_TRABAJADOR,mensajesInternos);
        }
        return respuestaProcesos;
      }  
    public String verificarProcesoExcepcion(){
          return verificarDetenencionProcesos().get(MENSAJES.PROCESO_TODOS)[0];
       }
    public boolean procesoActivo(Object algunaClase){
        System.out.println("algunaClase, funcion procesoActivo "+algunaClase);
        boolean respuesta = false;
        if(algunaClase instanceof DAOTrabajador || algunaClase instanceof DAOBanco || algunaClase instanceof DAOCargo || algunaClase instanceof DAOAreaTrabajo){
            respuesta = estadoProcesoTrabajador;
         }else if(algunaClase instanceof DAOProveedor){
            respuesta = estadoProcesoProveedor;
         }else if(algunaClase instanceof String){
             //como no tiene DAO la clase variables, mando por string la plabra Variables
            respuesta = estadoProcesoVariables;
         }
        return respuesta;
    }
    public HashMap<String,String[]> verificarDetenencionProcesos(){
                    miCoordinador.detenerTimerIteracion();
                    
                    TIEMPO_AGREGADO_INTERVALO_PROVEEDOR_ERROR_MYSQL_MIN = 0;
                    TIEMPO_AGREGADO_INTERVALO_VARIABLES_ERROR_MYSQL_MIN = 0;
                    
                    serviciosIniciados.put(MENSAJES.PROCESO_TRABAJADOR,Boolean.FALSE);
                    serviciosIniciados.put(MENSAJES.PROCESO_PROVEEDOR,Boolean.FALSE);
                    serviciosIniciados.put(MENSAJES.PROCESO_VARIABLE,Boolean.FALSE);
                    servicioGestionProveedor.shutdown();
                    servicioCreacionVariables.shutdown();
                    servicioGestionTrabajador.shutdown();
                    
                    servicioGestionProveedor = null;
                    servicioCreacionVariables = null;
                    servicioGestionTrabajador = null;
                    
                    System.gc();
                    
                    setEstadoProcesoProveedor(false);
                    setEstadoProcesoTrabajador(false);
                    setEstadoProcesoVariables(false);
                    mensajesInternos[0] = MENSAJES.PROCESO_TODOS.concat(" los Servicios se han Finalizado");
                    mensajesInternos[1] = "FALSE";
                    respuestaProcesos.put(MENSAJES.PROCESO_TODOS,mensajesInternos);
        return respuestaProcesos;
    }
}
