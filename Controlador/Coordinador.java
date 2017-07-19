/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controlador;
import Controlador.Conexiones.DataConnectionMYSQL;
import Modelo.Logica;
import Modelo.LogicaProcesos;
import Vista.Interfaces.MENSAJES;
import Vista.principal;
import java.awt.Color;
import java.io.File;
/**
 *
 * @author PROGRAMADOR
 */
 /*
     Para ahorrar codigo, y gestionar de mejor manera,los tres procesos que se involucran
     en general, eliminar de MYSQL, rescatar de SQL e insertar en MYSQL. Los realico en 
     gestionar, Ejemplo gestionarTrabajador:
     -Elimina todos los trabajadores de MYSQl
     -Rescata de SQL
     -Crea Archivo CSV
     -Inserta Archivo CSV en MYSQL
*/
public class Coordinador implements MENSAJES{
    
    private static Coordinador INSTANCIA = null;
    private Logica miLogica;
    private LogicaProcesos miLogicaProcesos;
    private principal miVentanaPrincipal;
    private final File ruta;
    String rutaDiscoLocal = "C:\\INTRANET";
    private final TimerReconexion reeconexion;
    private TimerIteracion miTimerIteracion;
    //

    private Coordinador() {
        this.ruta = new File(rutaDiscoLocal);
        this.reeconexion = new TimerReconexion();
    }
        /**Crea una instancia de la base de datos en caso de no estar creada.
     *
     */
    private synchronized static void createInstance(){
        if (INSTANCIA == null) {
            INSTANCIA = new Coordinador();
        }
    }
    /**Metodo para retorna una instancia de la conexion. Si no esta creada la crea, y si esta creada la retorna
     * @return retorna una instancia de la conexión a la base de datos
     */
    public static Coordinador getInstance(){
        if (INSTANCIA == null) createInstance();
        return INSTANCIA;
    }

    public TimerIteracion getMiTimerIteracion() {
        return miTimerIteracion;
    }

    public void setMiTimerIteracion(TimerIteracion miTimerIteracion) {
        this.miTimerIteracion = miTimerIteracion;
    }
        /**
     * Funcion iniciarTimerIteracion. Funcion que iniciar el timer. Tomando como parametro los minutos de la iteracion actual, de donde es llamada y los minutos de la proxima iteracion
     * @param minutosIteracionActual
     * @param minutosProximaIteracion 
     */
    public void iniciarTimerIteracion(int minutosIteracionActual, int minutosProximaIteracion){
        if(!this.miTimerIteracion.verificarInicio()) this.miTimerIteracion.init(minutosIteracionActual,minutosProximaIteracion);
    }
    public void detenerTimerIteracion(){
        this.miTimerIteracion.detenerTimer();
        miVentanaPrincipal.reestablecerTimer();
    }
    public void reestablecerTimer(){
     miVentanaPrincipal.reestablecerTimer();
    }
    public Logica getMiLogica() {
	return miLogica;
    }
    public void setMiLogica(Logica miLogica) {
	this.miLogica = miLogica;
    }
    public LogicaProcesos getMiLogicaProcesos() {
	return miLogicaProcesos;
    }
    public void setMiLogicaProcesos(LogicaProcesos miLogicaProc) {
	this.miLogicaProcesos = miLogicaProc;
    }
    public principal getVentanaPrincipal(){
        return this.miVentanaPrincipal;
    }
    public void setVentanaPrincipal(principal miVentanaPrincipal) {
        this.miVentanaPrincipal = miVentanaPrincipal;
    }
    private void verificarDirectorio(){
        if(!this.ruta.isDirectory()){
            finalizarServicio();
            miVentanaPrincipal.MensajeLog("El directorio INTRANET no existe.",true);
            miVentanaPrincipal.MensajeLog("\n",false);
        }
    }
    /*
    
        GESTIONAR SERVICIOS
    
    */
    public void gestionarProveedor(){
         //Detengo el timer de iteracion
         detenerTimerIteracion();
         verificarDirectorio();
         miVentanaPrincipal.MensajeLog(miLogica.validarCreacionArchivo(MENSAJES.PROCESO_PROVEEDOR,'&'),MENSAJES.PROCESO_PROVEEDOR);
         miVentanaPrincipal.MensajeLog(miLogica.validarEliminacion(MENSAJES.PROCESO_PROVEEDOR),MENSAJES.PROCESO_PROVEEDOR);
         miVentanaPrincipal.MensajeLog(miLogica.validarIngresoArchivo(MENSAJES.PROCESO_PROVEEDOR),MENSAJES.PROCESO_PROVEEDOR);
         miVentanaPrincipal.MensajeLog("\n",false);
         //inicio el timer con el tiempo de intervalo de proveedor y el tiempo de la proxima iteracion que seria Trabajador
         int tiempo = (LogicaProcesos.TIEMPO_AGREGADO_INTERVALO_PROVEEDOR_ERROR_MYSQL_MIN == 0) ? LogicaProcesos.TIEMPO_AGREGADO_INTERVALO_PROVEEDOR_MIN : LogicaProcesos.TIEMPO_AGREGADO_INTERVALO_PROVEEDOR_ERROR_MYSQL_MIN;
         //
         LogicaProcesos.TIEMPO_AGREGADO_INTERVALO_PROVEEDOR_ERROR_MYSQL_MIN = 0;
         iniciarTimerIteracion(tiempo,LogicaProcesos.TIEMPO_AGREGADO_INTERVALO_TRABAJADOR_MIN);
    }
    public void gestionarVariables(){
         //Detengo el timer de iteracion
         detenerTimerIteracion();
         verificarDirectorio();
         miVentanaPrincipal.MensajeLog(miLogica.validarCreacionArchivoVariables(MENSAJES.PROCESO_VARIABLE,"P171"),MENSAJES.PROCESO_VARIABLE);// variableP171
         miVentanaPrincipal.MensajeLog(miLogica.validarCreacionArchivoVariables(MENSAJES.PROCESO_VARIABLE,"P276"),MENSAJES.PROCESO_VARIABLE);// variableP276
         miVentanaPrincipal.MensajeLog("\n",false);
          //inicio el timer con el tiempo de intervalo de variables y el tiempo de la proxima iteracion que seria proveedor
         int tiempo = (LogicaProcesos.TIEMPO_AGREGADO_INTERVALO_VARIABLES_ERROR_MYSQL_MIN == 0)? LogicaProcesos.TIEMPO_AGREGADO_INTERVALO_VARIABLES_MIN : LogicaProcesos.TIEMPO_AGREGADO_INTERVALO_VARIABLES_ERROR_MYSQL_MIN;
         //
         LogicaProcesos.TIEMPO_AGREGADO_INTERVALO_VARIABLES_ERROR_MYSQL_MIN = 0;
         iniciarTimerIteracion(tiempo,LogicaProcesos.TIEMPO_AGREGADO_INTERVALO_PROVEEDOR_MIN);
    }
    private void mostrarTiempoIntervalos(String departamento){
        switch(departamento){
            case MENSAJES.PROCESO_VARIABLE:
                 miVentanaPrincipal.MensajeLog("El intervalo para el Proceso Variables es de: 120 Min. y "+LogicaProcesos.TIEMPO_AGREGADO_INTERVALO_VARIABLES_MIN+" Min.",false);
            break;
            case MENSAJES.PROCESO_TRABAJADOR:
                 miVentanaPrincipal.MensajeLog("El intervalo para el Proceso Trabajador es de: 120 Min. y "+LogicaProcesos.TIEMPO_AGREGADO_INTERVALO_PROVEEDOR_MIN+" Min.",false);
            break;
            case MENSAJES.PROCESO_PROVEEDOR:
                 miVentanaPrincipal.MensajeLog("El intervalo para el Proceso Proveedor es de: 120 Min. y "+LogicaProcesos.TIEMPO_AGREGADO_INTERVALO_TRABAJADOR_MIN+" Min.",false);
            break;
            case MENSAJES.PROCESO_TODOS:
                miVentanaPrincipal.MensajeLog("El intervalo para el Proceso Variables es de: 120 Min. y "+LogicaProcesos.TIEMPO_AGREGADO_INTERVALO_VARIABLES_MIN+" Min.",false);
                miVentanaPrincipal.MensajeLog("El intervalo para el Proceso Trabajador es de: 120 Min. y "+LogicaProcesos.TIEMPO_AGREGADO_INTERVALO_PROVEEDOR_MIN+" Min.",false);
                miVentanaPrincipal.MensajeLog("El intervalo para el Proceso Proveedor es de: 120 Min. y "+LogicaProcesos.TIEMPO_AGREGADO_INTERVALO_TRABAJADOR_MIN+" Min.",false);
            break;
        }
        miVentanaPrincipal.MensajeLog("\n",false);
    }
    public void gestionarTrabajador(){
            //Detengo el timer de iteracion
            detenerTimerIteracion();
            verificarDirectorio();
//          miVentanaPrincipal.MensajeLog("Se ha iniciado el Servicio");
            /*PRIMERO GESTIONO LOS CARGOS*/
            miVentanaPrincipal.MensajeLog(miLogica.validarEliminacion(MENSAJES.PROCESO_CARGO),MENSAJES.PROCESO_CARGO);
            miVentanaPrincipal.MensajeLog(miLogica.validarIngreso(MENSAJES.PROCESO_CARGO),MENSAJES.PROCESO_CARGO);

            /*SEGUNDO GESTIONO LOS BANCOS*/
            miVentanaPrincipal.MensajeLog(miLogica.validarEliminacion(MENSAJES.PROCESO_BANCO),MENSAJES.PROCESO_BANCO);
            miVentanaPrincipal.MensajeLog(miLogica.validarIngreso(MENSAJES.PROCESO_BANCO),MENSAJES.PROCESO_BANCO);        
                        /*
                ABAJO VALIDO LA CREACION DEL ARCHIVO
            */
            miVentanaPrincipal.MensajeLog(miLogica.validarCreacionArchivo(MENSAJES.PROCESO_TRABAJADOR,'&'),MENSAJES.PROCESO_TRABAJADOR);
            /*
                ABAJO VALIDO LA CREACION DEL ARCHIVO
            */
            miVentanaPrincipal.MensajeLog(miLogica.validarCreacionArchivo(MENSAJES.PROCESO_AREATRABAJO,'&'),MENSAJES.PROCESO_AREATRABAJO);   
            miVentanaPrincipal.MensajeLog(miLogica.validarEliminacion(MENSAJES.PROCESO_AREATRABAJO),MENSAJES.PROCESO_AREATRABAJO);
            miVentanaPrincipal.MensajeLog(miLogica.validarIngresoArchivo(MENSAJES.PROCESO_AREATRABAJO),MENSAJES.PROCESO_AREATRABAJO);
            //
            miVentanaPrincipal.MensajeLog(miLogica.validarEliminacion(MENSAJES.PROCESO_TRABAJADOR),MENSAJES.PROCESO_TRABAJADOR);
            miVentanaPrincipal.MensajeLog(miLogica.validarIngresoArchivo(MENSAJES.PROCESO_TRABAJADOR),MENSAJES.PROCESO_TRABAJADOR);
            miVentanaPrincipal.MensajeLog("\n",false);
            //inicio el timer con el tiempo de intervalo de trabajador pero al llegar a trabajador el ciclo comeinza nuevamente, por lo que debo agregar los 120 minutos mas el tiempo agregado con la primera iteracion; el proceso de variables
            //en el segundo parametro agrego 0 ya que el ciclo comienza nuevamente, si agrego un valor se resta con el tiempo del ciclo actual        
            int tiempo = (LogicaProcesos.TIEMPO_AGREGADO_INTERVALO_VARIABLES_ERROR_MYSQL_MIN == 0)? LogicaProcesos.TIEMPO_AGREGADO_INTERVALO_VARIABLES_MIN : LogicaProcesos.TIEMPO_AGREGADO_INTERVALO_VARIABLES_ERROR_MYSQL_MIN;
            //Vuelvo a establecer el valor en 0 para que solo ocurra una vez 
            LogicaProcesos.TIEMPO_AGREGADO_INTERVALO_VARIABLES_ERROR_MYSQL_MIN = 0;
            iniciarTimerIteracion(120+tiempo, 0);
    }
    public void detenerProcesoExcepcion(){
        miVentanaPrincipal.MensajeLog("\n",false);
        miVentanaPrincipal.MensajeLog(miLogicaProcesos.verificarProcesoExcepcion(),false);
        miVentanaPrincipal.MensajeLog("\n",false);
    }
    
    /**
     * Funcion iniciarServicioProveedor. Se llama cuando se ha perdido la conexion con mysql y anteriormente se estaba ejecutando este servicio. Para establecer la nueva hora de intervalo entre cada hilo.
     * @param tiempo 
     */
    public void iniciarServicioProveedor(int tiempo){
       if(!DataConnectionMYSQL.verificarInstance()){
            throw new NullPointerException("Sin Conexión a MYSQL");
        }
        miVentanaPrincipal.MensajeLog("\n",false);
        mostrarTiempoIntervalos(MENSAJES.PROCESO_PROVEEDOR);
        miVentanaPrincipal.MensajeLog(miLogicaProcesos.verifivarInicioServicioProveedor(tiempo),MENSAJES.PROCESO_PROVEEDOR);
    }
    /**
     * Funcion iniciarServicioVariables. Se llama cuando se ha perdido la conexion con mysql y anteriormente se estaba ejecutando este servicio. Para establecer la nueva hora de intervalo entre cada hilo.
     * @param tiempo 
     */
    public void iniciarServicioVariables(int tiempo){
        if(!DataConnectionMYSQL.verificarInstance()){
           throw new NullPointerException("Sin Conexión a MYSQL");
        }
        miVentanaPrincipal.MensajeLog("\n",false);
        mostrarTiempoIntervalos(MENSAJES.PROCESO_VARIABLE);
        miVentanaPrincipal.MensajeLog(miLogicaProcesos.verifivarIniciarServicioVariables(tiempo),MENSAJES.PROCESO_VARIABLE); 
        }
    /**
     * Funcion iniciarServicioTrabajador. Se llama cuando se ha perdido la conexion con mysql y anteriormente se estaba ejecutando este servicio. Para establecer la nueva hora de intervalo entre cada hilo.
     * @param nuevaHoraInicioEnMinutos 
     */
    public void iniciarServicioTrabajador(int nuevaHoraInicioEnMinutos){
        if(!DataConnectionMYSQL.verificarInstance()){
            throw new NullPointerException("Sin Conexión a MYSQL");
        }
        miVentanaPrincipal.MensajeLog("\n",false);
        mostrarTiempoIntervalos(MENSAJES.PROCESO_TRABAJADOR);
        miVentanaPrincipal.MensajeLog(miLogicaProcesos.verifivarIniciarServicioTrabajador(nuevaHoraInicioEnMinutos),MENSAJES.PROCESO_TRABAJADOR);
    }
    public void finalizarServicio(){
        miVentanaPrincipal.MensajeLog(miLogicaProcesos.verificarDetenencionProcesos(),MENSAJES.PROCESO_TODOS);
        miVentanaPrincipal.MensajeLog("\n",false);
    }
    public boolean consultarServicioIniciado(String nombreServicio){
        try{  
            return miLogicaProcesos.isServicioIniciado(nombreServicio);
        } catch (NullPointerException e) {
            return false;          
        }      
    }
    public String rescatarInformacionLog(){
        return miVentanaPrincipal.getTxtLogCreacion();
    }
    /**
     * Funcion iniciarTodosLosServicios. Solo se llama cuandos se pierde la conexion. Se pasa como parametro el tiempo calculado para que comeince justo en la siguiente iteracion.
     * @param tiempo
     */
    public void iniciarTodosLosServicios(int tiempo){
        if(!DataConnectionMYSQL.verificarInstance()){
            throw new NullPointerException("Sin Conexión a MYSQL");
        }
        miVentanaPrincipal.MensajeLog("\n",false);
        mostrarTiempoIntervalos(MENSAJES.PROCESO_TODOS);
        miVentanaPrincipal.MensajeLog(miLogicaProcesos.verificarInicioTodosLosServicios(tiempo),MENSAJES.PROCESO_TODOS);
    }
    /*
    Timer Reeconexion
    */
    public void iniciarReconexion(String text){
                finalizarServicio();
                this.reeconexion.init();
    }
    public void inicarDAOS(){
        miLogica.iniciarComponentes();
    }
    public void setLblMensajeEstadoConexion(String texto){
        miVentanaPrincipal.setLblMensajeEstadoRec(texto);
    }
    public void setLblHoraIteracion(String hora){
        miVentanaPrincipal.setLblHoraIteracion(hora);
    }
    public int calcularHoraSiguienteIteracion(){
        return miLogicaProcesos.calcularHoraReinicio();
    }
    public void setLMinutosIteracion(String minutos){
        miVentanaPrincipal.setLblMinutosIteracion(minutos);
    }
    public void setLSegundosIteracion(String Segundos){
        miVentanaPrincipal.setLblSegundosIteracion(Segundos);
    }
    public void setLblTiempoReeconexion(String texto){
        miVentanaPrincipal.setLblTiempoReconexion(texto);
    }
    public void cambiarEstadoLblTiempoReconexion(String texto,Color color){
        miVentanaPrincipal.cambiarColorLblTiempoConexion(texto,color);
    }
    public void mostrarMensajeReeconexion(String error){
        miVentanaPrincipal.MensajeLog(error.concat(MENSAJES.LINEA_SEPARACION), true);
    }
    public void verificarProcesosActivos(){
        System.gc();
        int nuevaHoraInicioEnMinutos = miLogicaProcesos.calcularHoraReinicio();
        iniciarTodosLosServicios(nuevaHoraInicioEnMinutos);
    }
}