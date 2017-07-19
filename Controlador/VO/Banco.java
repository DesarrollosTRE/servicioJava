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
public class Banco {
    private String codBanco;
    private String nombre;

    public Banco(String codBanco, String nombre) {
        this.codBanco = codBanco;
        this.nombre = nombre;
    }

    public String getCodBanco() {
        return codBanco;
    }

    public void setCodBanco(String codBanco) {
        this.codBanco = codBanco;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }
    
    
}
