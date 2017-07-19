/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controlador.VO;

/**
 *
 * @author PROGRAMADOR
 */
public class Proveedor {
    private String codigoAuxiliar;
    private String nombreAuxiliar;
    public static final String NOMBRE_ARCHIVO="archivoProveedor.csv";

    public Proveedor(String codigoAuxiliar, String nombreAuxiliar) {
        this.codigoAuxiliar = codigoAuxiliar;
        this.nombreAuxiliar = nombreAuxiliar;
    }

    public String getCodigoAuxiliar() {
        return codigoAuxiliar;
    }

    public void setCodigoAuxiliar(String codigoAuxiliar) {
        this.codigoAuxiliar = codigoAuxiliar;
    }

    public String getNombreAuxiliar() {
        return nombreAuxiliar.replace("*","").trim();
    }

    public void setNombreAuxiliar(String nombreAuxiliar) {
        this.nombreAuxiliar = nombreAuxiliar;
    }
    
    
}
