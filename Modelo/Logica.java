/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Modelo;

import Vista.Interfaces.SQL2;
import Vista.Interfaces.MYSQL;
import Vista.Interfaces.VARIABLES;
import Vista.Interfaces.MENSAJES;
import Modelo.DAO.*;
import Controlador.Coordinador;
import Controlador.Mail;
import Controlador.Archivo;
import Controlador.Conexiones.*;
import com.mysql.jdbc.exceptions.jdbc4.MySQLNonTransientConnectionException;
import java.awt.Color;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.HashMap;

//

/**
 *
 * @author PROGRAMADOR
 */
public class Logica implements MENSAJES{
    /*
    Logica par aplicar validaciones en los CRUD 
    y envia un correo cuando algo ocurre
    */
    private DAOBanco procesosBanco;
    private DAOCargo procesosCargo;
    private DAOAreaTrabajo procesosAreaTrabajo;
    private DAOTrabajador procesosTrabajador;
    private DAOProveedor procesosProveedor;
    private Coordinador miCoordinador;
    private ArrayList<?> listaGeneral;
    private Object objeto; 
    private String nombreArchivo;
    private final Mail email;
    private LogicaProcesos logicaProcesos;
    private final HashMap<String,String[]> respuestaProcesos;
    private final String[] mensajesInternos;
    
    public Logica() {
        respuestaProcesos = new HashMap<>();
        mensajesInternos = new String[2];
        this.email= new Mail("user", "passs","to");
    }
    public void iniciarComponentes(){
        miCoordinador.setLblHoraIteracion("00");
        miCoordinador.setLMinutosIteracion("00");
                   try{

                    this.procesosBanco = new DAOBanco();
                    this.procesosCargo = new DAOCargo();
                    this.procesosAreaTrabajo = new DAOAreaTrabajo();
                    this.procesosTrabajador = new DAOTrabajador();
                    this.procesosProveedor = new DAOProveedor();
                    //
                    miCoordinador.setLblMensajeEstadoConexion("Conexión con MYSQL");
                    miCoordinador.cambiarEstadoLblTiempoReconexion("OK",Color.green);

                } catch (Exception e) {
                    this.procesosBanco = null;
                    this.procesosCargo = null;
                    this.procesosAreaTrabajo = null;
                    this.procesosTrabajador = null;
                    this.procesosProveedor = null;
                    miCoordinador.iniciarReconexion(MENSAJES.CONEXION_BASE_DATOS.concat(" se detalla a continuación ").concat(e.getLocalizedMessage()));         
        }
    }
    
    public void setLogicaProcesos(LogicaProcesos logicaProc){
        this.logicaProcesos=logicaProc;
    }
    public void setCoordinador(Coordinador coordinador){
        this.miCoordinador=coordinador;
    }

    /*
    FUNCTION 
    */
    private void validarOrigenObjeto(String departamento){
         this.objeto = null;
         System.gc();
             switch(departamento){
             case MENSAJES.PROCESO_BANCO: this.objeto=  this.procesosBanco;             
             break;
             case MENSAJES.PROCESO_CARGO: this.objeto = this.procesosCargo;
             break;
             case MENSAJES.PROCESO_AREATRABAJO: this.objeto = this.procesosAreaTrabajo;
                                                     nombreArchivo="area_trabajo.csv";
             break;
             case MENSAJES.PROCESO_TRABAJADOR: this.objeto = this.procesosTrabajador;
                                                    nombreArchivo="trabajador.csv";
             break;
             case MENSAJES.PROCESO_PROVEEDOR: this.objeto = this.procesosProveedor;
                                              nombreArchivo="archivoProveedor.csv";
             break;
         }
     }
    
    public HashMap<String,String[]> validarEliminacion(String departamento){
         this.validarOrigenObjeto(departamento);
         if(!logicaProcesos.procesoActivo(objeto))return null;
         // retorna false proceso inactivo no debe entrar aqui
         try {
             ((MYSQL)objeto).eliminar();
             mensajesInternos[0] = MENSAJES.TITULO_BD_SUCCESS.concat("Se ha eliminado ").concat(departamento).concat("\n\t\t  [").concat(departamento).concat("] ").concat(validarBackup(objeto, departamento));
             mensajesInternos[1] = "TRUE";
             respuestaProcesos.put(departamento,mensajesInternos);
         } catch (Exception e) {
             this.email.setAsunto("Error al eliminar " + departamento);
              this.email.setMensaje(Arrays.toString(e.getStackTrace()));
              try {
                 this.email.enviarEmail();
                 } catch (Exception ex) {
                     //this.restablecerCampos(ex, "Error al Enviar Email","Thread");
                }
              mensajesInternos[0] = (verficarConexionMYSQL(e)) ? MENSAJES.TITULO_BD_ERROR.concat(" Al eliminar ").concat(departamento).concat(", se detalla a continucación ").concat(e.getLocalizedMessage()).concat(" ").concat(logicaProcesos.verificarProcesoExcepcion()) : MENSAJES.CONEXION_BASE_DATOS.concat(" Se ha perdido la conexión.");
              mensajesInternos[1] = "FALSE";      
              respuestaProcesos.put(departamento,mensajesInternos);
         }
         return respuestaProcesos;
     }
    public HashMap<String,String[]> validarIngreso(String departamento){
          this.validarOrigenObjeto(departamento);
          if(!logicaProcesos.procesoActivo(objeto))return null;
          try {
             ((MYSQL)objeto).crear(validarListas(0,departamento));
             ((MYSQL)objeto).deleteBackup();
             mensajesInternos[0] = MENSAJES.TITULO_BD_SUCCESS.concat("Se ha insertado ").concat(departamento);
             mensajesInternos[1] = "TRUE";   
             respuestaProcesos.put(departamento,mensajesInternos);
          } catch (Exception e) {
             this.email.setAsunto("Error al insertar ".concat(departamento));
              this.email.setMensaje(Arrays.toString(e.getStackTrace()));
              try {
                 this.email.enviarEmail();
                 } catch (Exception ex) {
                     //this.restablecerCampos(ex, "Error al Enviar Email","Thread");
                }  
              mensajesInternos[0] = (verficarConexionMYSQL(e)) ? MENSAJES.TITULO_BD_ERROR.concat(" Al ingresar ").concat(departamento).concat(", se detalla a continucación ").concat(e.getLocalizedMessage()).concat(" ").concat(logicaProcesos.verificarProcesoExcepcion()).concat("\n\t\t  [").concat(departamento).concat("] ").concat(validarRestore(objeto, departamento)) : MENSAJES.CONEXION_BASE_DATOS.concat(" Se ha perdido la conexión.");
              mensajesInternos[1] = "FALSE";
              respuestaProcesos.put(departamento,mensajesInternos);
          }
        return respuestaProcesos;
     }
    public HashMap<String,String[]> validarCreacionArchivoVariables(String departamento,String tipoVariable){ 
        if(!logicaProcesos.procesoActivo(MENSAJES.PROCESO_VARIABLE))return null;
          int mes;
          String nombreArchivoVar="";
          mes= saberMesAConsultar();
          try {
             if(tipoVariable.equals("P171")) {
                 procesosTrabajador.agregarDatos(procesosTrabajador.readVariableP171(mes),true,VARIABLES.NOMBRE_ARCHIVO_P171);
                 nombreArchivoVar=VARIABLES.NOMBRE_ARCHIVO_P171;
             } else {
                 procesosTrabajador.agregarDatos(procesosTrabajador.readVariableP276(mes),true,VARIABLES.NOMBRE_ARCHIVO_P276);
                 nombreArchivoVar=VARIABLES.NOMBRE_ARCHIVO_P276;
             }
             //
             mensajesInternos[0] = MENSAJES.TITULO_PROCESO_SUCCESS+"Se ha Creado archivo ".concat(nombreArchivoVar);
             mensajesInternos[1] = "TRUE";
             respuestaProcesos.put(departamento,mensajesInternos);
          } catch (Exception e) {
             this.email.setAsunto("Error al crear Archivo "+nombreArchivoVar);
              this.email.setMensaje(Arrays.toString(e.getStackTrace()));
              try {
                 this.email.enviarEmail();
                 } catch (Exception ex) {
                     //this.restablecerCampos(ex, "Error al Enviar Email","Thread");
                }
             mensajesInternos[0] = (verficarConexionMYSQL(e)) ? MENSAJES.TITULO_PROCESO_ERROR.concat(" Al crear archivo ").concat(nombreArchivoVar).concat(", se detalla a continucación ").concat(e.getLocalizedMessage()).concat(" ").concat(logicaProcesos.verificarProcesoExcepcion()) : MENSAJES.CONEXION_BASE_DATOS.concat(" Se ha perdido la conexión.");
             mensajesInternos[1] = "FALSE";
             respuestaProcesos.put(departamento,mensajesInternos);
          }
        return respuestaProcesos;
     }
    public HashMap<String,String[]> validarCreacionArchivo(String departamento,char separador){
        this.validarOrigenObjeto(departamento);
         if(!logicaProcesos.procesoActivo(objeto))return null;
          int mes;
          try {
             mes = saberMesAConsultar();
             listaGeneral = validarListas(mes,departamento);
             ((Archivo) objeto).agregarDatos(listaGeneral,separador);
             //
             mensajesInternos[0] = MENSAJES.TITULO_BD_SUCCESS+"Se ha Creado archivo ".concat(nombreArchivo);
             mensajesInternos[1] = "TRUE";
             respuestaProcesos.put(departamento,mensajesInternos);
          } catch (Exception e) {
             this.email.setAsunto("Error al crear Archivo "+nombreArchivo);
              this.email.setMensaje(Arrays.toString(e.getStackTrace()));
              try {
                 this.email.enviarEmail();
                 } catch (Exception ex) {
                     //this.restablecerCampos(ex, "Error al Enviar Email","Thread");
                }
             mensajesInternos[0] = (verficarConexionMYSQL(e)) ? MENSAJES.TITULO_BD_ERROR.concat(" Al crear archivo ").concat(nombreArchivo).concat(", se detalla a continucación ").concat(e.getLocalizedMessage()).concat(" ").concat(logicaProcesos.verificarProcesoExcepcion()) : MENSAJES.CONEXION_BASE_DATOS.concat(" Se ha perdido la conexión.");
             mensajesInternos[1] = "FALSE";
             respuestaProcesos.put(departamento,mensajesInternos);
          }
        return respuestaProcesos;
     }
    public HashMap<String,String[]> validarIngresoArchivo(String departamento){
          if(!logicaProcesos.procesoActivo(objeto))return null;
          try {
             ((MYSQL) objeto).crear(null);// se envia null ya que es un archivo el que se esta subiendo
             ((MYSQL)objeto).deleteBackup();
             mensajesInternos[0] = MENSAJES.TITULO_BD_SUCCESS+"Se ha Ingresado archivo ".concat(nombreArchivo);
             mensajesInternos[1] = "TRUE";
             respuestaProcesos.put(departamento,mensajesInternos);
          } catch (Exception e) {
             this.email.setAsunto("Error al Ingresado Archivo ".concat(nombreArchivo));
              this.email.setMensaje(Arrays.toString(e.getStackTrace()));
              try {
                 this.email.enviarEmail();
                 } catch (Exception ex) {
                     //this.restablecerCampos(ex, "Error al Enviar Email","Thread");
                }
             mensajesInternos[0] = (verficarConexionMYSQL(e)) ? MENSAJES.TITULO_BD_ERROR.concat(" Al Ingresado archivo ").concat(nombreArchivo).concat(", se detalla a continucación ").concat(e.getLocalizedMessage()).concat(" ").concat(logicaProcesos.verificarProcesoExcepcion()):MENSAJES.CONEXION_BASE_DATOS.concat(" Se ha perdido la conexión.");
             mensajesInternos[1] = "FALSE";
             respuestaProcesos.put(departamento,mensajesInternos);
          }
        return respuestaProcesos;
     }
    private String validarBackup(Object objeto,String departamento){
          if(!logicaProcesos.procesoActivo(objeto))return null;
          try {
              
             ((MYSQL)objeto).backup();
             return MENSAJES.TITULO_BD_SUCCESS+"Se ha relizado BACKUP de tabla ".concat(departamento);
          } catch (Exception e) {
              System.out.println("validarbackup "+e.getLocalizedMessage());
             this.email.setAsunto("Error al realizar BACKUP de ".concat(departamento));
              this.email.setMensaje(Arrays.toString(e.getStackTrace()));
              try {
                 this.email.enviarEmail();
                 } catch (Exception ex) {
                     //this.restablecerCampos(ex, "Error al Enviar Email","Thread");
                }
             return (verficarConexionMYSQL(e))? MENSAJES.TITULO_BD_ERROR.concat(" Al realizar BACKUP de tabla ").concat(departamento).concat(", se detalla a continucación ").concat(e.getLocalizedMessage()):MENSAJES.CONEXION_BASE_DATOS.concat(" Se ha perdido la conexión.");
          }
     }
    private String validarRestore(Object objeto,String departamento){
          if(!logicaProcesos.procesoActivo(objeto))return null;
          try {
             ((MYSQL)objeto).restore();
             return MENSAJES.TITULO_BD_SUCCESS+"Se ha relizado RESTORE de tabla ".concat(departamento);
          } catch (Exception e) {
              /*
               LLamo a funcion verficarConexionMYSQL, para que siempre verifique la conexion
              En caso que esta se pierda se detiene el proceso y se trata de reconectar
              */
             verficarConexionMYSQL(e);
             this.email.setAsunto("Error al realizar RESTORE de ".concat(departamento));
              this.email.setMensaje(Arrays.toString(e.getStackTrace()));
              try {
                 this.email.enviarEmail();
                 } catch (Exception ex) {
                     //this.restablecerCampos(ex, "Error al Enviar Email","Thread");
                }
             return (verficarConexionMYSQL(e))? MENSAJES.TITULO_BD_ERROR.concat(" Al realizar RESTORE de tabla ").concat(departamento).concat(", se detalla a continucación ").concat(e.getLocalizedMessage()):MENSAJES.CONEXION_BASE_DATOS.concat(" Se ha perdido la conexión.");
          }
    }
    public ArrayList<?> validarListas(int mes,String departamento) throws Exception{  
        switch (departamento) {
            case MENSAJES.PROCESO_AREATRABAJO:
                return listaGeneral;
            case MENSAJES.PROCESO_TRABAJADOR:
                return ((SQL2)objeto).read(mes);
            default:
                listaGeneral= new ArrayList<>();
                return ((SQL2)objeto).read(mes);
        }
     }
    /**Método para verificar conexion con MYSQL. Si el error es de tipo MySQLNonTransientConnectionException, es porque se perdio la conexión. Por lo cual elimino la Instancia de DataConectionMYSQL y creo una nueva.
     *
     * @throws java.sql.SQLException
     */
    private boolean verficarConexionMYSQL(Throwable error){
        String trace = Arrays.toString(error.getStackTrace());
       if(error.getClass()==MySQLNonTransientConnectionException.class || -1!=trace.indexOf("com.microsoft.sqlserver.jdbc.SQLServerConnection.terminate")){
            DataConnectionMYSQL.delOnlyInstance();
            DataConnectionSQL.delOnlyInstance();
            DataConnectionSQLServidor2.delOnlyInstance();
            miCoordinador.iniciarReconexion(error.getLocalizedMessage());
            return false;
       }
       return true;
    }
    /**Método para saber el Mes a consultar en SQL, SOFTLAND.  Genera el mes para consultar en SOFTLAND en base al AÑO
     *
     * @return retorna un int con el mes.
     */
    private int saberMesAConsultar(){
         Calendar fecha = Calendar.getInstance();
         int año,mesActual,mesAConsultar;
         mesActual=fecha.get(Calendar.MONTH)+1;
         año =fecha.get(Calendar.YEAR);
         if(año % 2 == 0){
          // es par
             mesAConsultar= mesActual-1;
         }else{
         //es impar
             mesAConsultar= mesActual+11;
         }
         return mesAConsultar;
     }
   
}
