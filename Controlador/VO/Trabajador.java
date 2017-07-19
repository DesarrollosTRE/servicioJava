/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controlador.VO;
import java.sql.Date;
import java.util.ArrayList;
/**
 *
 * @author PROGRAMADOR
 */
public class Trabajador extends AreaTrabajo {
    private String $apellidoP;
    private String $apellidoM;
    private String $nombres;
    private String $run;
    private Date $fechaNacimiento;
    private String $sexo;
    
    private String codSucursal;
    private String codEstudios;
    private String correo;
    private String direccion;
    private String codComuna;
    private String codCiudad;
    private String fono1;
    private String fono2;
    private String fono3;
    private String estadoCivil;
    private String nacionalidad;
    private Date fechaIngreso;
    private Date fechaContrato;
    private Date fechaFiniquito;
    private String estado;
    private String ficha;
    //
    private String centroCosto;
    private String centroCostoAbr;
    private String unidad;
    private String codCargo;
    private String nombreCargo;
    
    /*fechas vigentes*/
    private ArrayList<Date> fechasVigentes;
    static String resultado;
    public static final String NOMBRE_ARCHIVO="archivo.csv";

    public Trabajador(){

    }
//    /*
//    Constructor solo trabajador
//    */
//    public Trabajador(
//                    String codBancoSuc,
//                    String codEstudios,
//                    String appaterno, 
//                    String apmaterno,
//                    String nombres,                 
//                    String Email,
//                    String rut,
//                    String direccion,
//                    String codComuna,                   
//                    String codCiudad,
//                    String telefono1, 
//                    String telefono2, 
//                    String telefono3,
//                    Date fechaNacimient,                  
//                    String sexo,                     
//                    String estadoCivil,  
//                    String nacionalidad,                     
//                    Date fechaIngreso,  
//                    Date fechaPrimerCon, 
//                    Date fechaFiniquito,  
//                    String estado, 
//                    String ficha
//    ) {
//                    this.$apellidoP = appaterno;
//                    this.$apellidoM = apmaterno;
//                    this.$nombres = nombres;
//                    this.$run = rut;
//                    this.$fechaNacimiento = fechaNacimient;
//                    this.$sexo = sexo;
//                    this.codSucursal = codBancoSuc;
//                    this.codEstudios = codEstudios;                
//                    this.correo = Email;
//                    this.direccion = direccion;
//                    this.codComuna = codComuna;                   
//                    this.codCiudad = codCiudad;
//                    this.fono1 = telefono1; 
//                    this.fono2 = telefono2; 
//                    this.fono3 = telefono3;                  
//                    this.estadoCivil = estadoCivil;  
//                    this.nacionalidad = nacionalidad;        
//                    this.fechaIngreso = fechaIngreso;  
//                    this.fechaContrato = fechaPrimerCon; 
//                    this.fechaFiniquito = fechaFiniquito;  
//                    this.estado = estado; 
//                    this.ficha = ficha;
//    }
    /*
    Constructor trabajador + area de trabajo
    */
    public Trabajador(
                    String codBancoSuc,
                    String codEstudios,
                    String appaterno, 
                    String apmaterno,
                    String nombres,                 
                    String Email,
                    String rut,
                    String direccion,
                    String codComuna,                   
                    String codCiudad,
                    String telefono1, 
                    String telefono2, 
                    String telefono3,
                    Date fechaNacimient,                  
                    String sexo,                     
                    String estadoCivil,  
                    String nacionalidad,                     
                    Date fechaIngreso,  
                    Date fechaPrimerCon, 
                    Date fechaFiniquito,  
                    String estado, 
                    String ficha,
                    int codAreaTra,
                    String centroCosto,  
                    String centroCostoAbr, 
                    String unidad, 
                    String codCargo
    ) {
        super(codAreaTra,centroCosto, centroCostoAbr, codCargo, unidad);
                    this.$apellidoP = appaterno;
                    this.$apellidoM = apmaterno;
                    this.$nombres = nombres;
                    this.$run = rut;
                    this.$fechaNacimiento = fechaNacimient;
                    this.$sexo = sexo;
                    this.codSucursal = codBancoSuc;
                    this.codEstudios = codEstudios;                
                    this.correo = Email;
                    this.direccion = direccion;
                    this.codComuna = codComuna;                   
                    this.codCiudad = codCiudad;
                    this.fono1 = telefono1; 
                    this.fono2 = telefono2; 
                    this.fono3 = telefono3;                  
                    this.estadoCivil = estadoCivil;  
                    this.nacionalidad = nacionalidad;        
                    this.fechaIngreso = fechaIngreso;  
                    this.fechaContrato = fechaPrimerCon; 
                    this.fechaFiniquito = fechaFiniquito;  
                    this.estado = estado; 
                    this.ficha = ficha;
                    this.centroCosto = centroCosto;  
                    this.centroCostoAbr = centroCostoAbr; 
                    this.unidad = unidad; 
                    this.codCargo = codCargo;
    }

    public String get$sexo() {
        return $sexo;
    }

    public void set$sexo(String $sexo) {
        this.$sexo = $sexo;
    }
    public String get$apellidoP() {
        return $apellidoP;
    }

    public void set$apellidoP(String $apellidoP) {
        this.$apellidoP = $apellidoP;
    }

    public String get$apellidoM() {
        return $apellidoM;
    }

    public void set$apellidoM(String $apellidoM) {
        this.$apellidoM = $apellidoM;
    }

    public String get$nombres() {
        return $nombres;
    }

    public void set$nombres(String $nombres) {
        this.$nombres = $nombres;
    }
    public String get$run() {
        return $run;
    }

    public void set$run(String $run) {
        this.$run = $run;
    }
    
    public Date get$fechaNacimiento() {
        return $fechaNacimiento;
    }

    public void set$fechaNacimiento(Date $fechaNacimiento) {
        this.$fechaNacimiento = $fechaNacimiento;
    }
    
    public String getCodSucursal() {
        if(codSucursal == null) codSucursal="NULL";
        return codSucursal;
    }

    public void setCodSucursal(String $codSucursal) {
        this.codSucursal = $codSucursal;
    }

    public String getCodEstudios() {
        if(codEstudios == null) codEstudios="NULL";
        return codEstudios;
    }

    public void setCodEstudios(String $codEstudios) {
        this.codEstudios = $codEstudios;
    }
    public String getCorreo() {
        return correo;
    }

    public void setCorreo(String $correo) {
        this.correo = $correo;
    }

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String $direccion) {
        this.direccion = $direccion;
    }

    public String getCodComuna() {
        return codComuna;
    }

    public void setCodComuna(String $codComuna) {
        this.codComuna = $codComuna;
    }

    public String getCodCiudad() {
        return codCiudad;
    }

    public void setCodCiudad(String $codCiudad) {
        this.codCiudad = $codCiudad;
    }

    public String getFono1() {
        return fono1;
    }

    public void setFono1(String $fono1) {
        this.fono1 = $fono1;
    }

    public String getFono2() {
        return fono2;
    }

    public void setFono2(String $fono2) {
        this.fono2 = $fono2;
    }

    public String getFono3() {
        return fono3;
    }

    public void setFono3(String $fono3) {
        this.fono3 = $fono3;
    }
    public String getEstadoCivil() {
        return estadoCivil;
    }

    public void setEstadoCivil(String $estadoCivil) {
        this.estadoCivil = $estadoCivil;
    }

    public String getNacionalidad() {
        return nacionalidad;
    }

    public void setNacionalidad(String $nacionalidad) {
        this.nacionalidad = $nacionalidad;
    }

    public Date getFechaIngreso() {
        return fechaIngreso;
    }

    public void setFechaIngreso(Date $fechaIngreso) {
        this.fechaIngreso = $fechaIngreso;
    }

    public Date getFechaContrato() {
        return fechaContrato;
    }

    public void setFechaContrato(Date $fechaContrato) {
        this.fechaContrato = $fechaContrato;
    }

    public Date getFechaFiniquito() {
        return fechaFiniquito;
    }

    public void setFechaFiniquito(Date $fechaFiniquito) {
        this.fechaFiniquito = $fechaFiniquito;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String $estado) {
        this.estado = $estado;
    }

    public String getFicha() {
        return ficha;
    }

    public void setFicha(String $ficha) {
        this.ficha = $ficha;
    }

    public String getCentroCosto() {
        return centroCosto;
    }

    public void setCentroCosto(String centroCosto) {
        this.centroCosto = centroCosto;
    }

    public String getCentroCostoAbr() {
        return centroCostoAbr;
    }

    public void setCentroCostoAbr(String centroCostoAbr) {
        this.centroCostoAbr = centroCostoAbr;
    }

    public String getUnidad() {
        return unidad;
    }

    public void setUnidad(String unidad) {
        this.unidad = unidad;
    }

    public String getCodCargo() {
        return codCargo;
    }

    public void setCodCargo(String codCargo) {
        this.codCargo = codCargo;
    }

    public String getNombreCargo() {
        return nombreCargo;
    }

    public void setNombreCargo(String nombreCargo) {
        this.nombreCargo = nombreCargo;
    }    
    /*FUNCION PARA ALMACENAR FECHAS VIGENTES DESDE*/
    public void setFechasVigentes(Date fecha){
      this.fechasVigentes.add(fecha);
    }
    public String getFechasVigentes(){
        this.fechasVigentes.stream().forEach((fecha) -> {
            resultado+= fecha.toString()+" ";
        });
      return resultado;
    } 
}
