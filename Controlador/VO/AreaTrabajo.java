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
public class AreaTrabajo extends Cargo {
    private int codigoArea;
    private String centroCosto;
    private String centroCostoAbr;
    public static final String NOMBRE_ARCHIVO="area_trabajo.csv";

    public AreaTrabajo() {
      
    }
    
    public AreaTrabajo(int codigoA,String centroCosto, String centroCostoAbr, String codigo, String nombre) {
        super(codigo, nombre);
        this.codigoArea = codigoA;
        this.centroCosto = centroCosto;
        this.centroCostoAbr = centroCostoAbr;
    }

    public void setCodigoArea(int codigoArea) {
        this.codigoArea = codigoArea;
    }

    public int getCodigoArea() {
        return codigoArea;
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
    
}
    
   
