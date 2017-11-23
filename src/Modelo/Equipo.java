/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Modelo;

import javax.swing.ImageIcon;

/**
 *
 * @author Luis Rosero
 */
public class Equipo {

    public int getiCantidadFallas() {
        return iCantidadFallas;
    }

    public void setiCantidadFallas(int iCantidadFallas) {
        this.iCantidadFallas = iCantidadFallas;
    }

    private int iCantidadFallas = 0;
    private String nombreEquipo;
    private ImageIcon emptyIcon = new ImageIcon(getClass().getResource("imagenes/camara.gif"));
    private ImageIcon icon = new ImageIcon(getClass().getResource("imagenes/camara.gif"));

    public Equipo() {

    }
   public String getNombre(){
       return nombreEquipo;
   }
    
    public ImageIcon getEmptyIcon() {
        return emptyIcon;
    }

    public void setEmptyIcon(ImageIcon emptyIcon) {
        this.emptyIcon = emptyIcon;
    }

    public ImageIcon getIcon() {
        return icon;
    }

    public void setIcon(ImageIcon icon) {
        this.icon = icon;
    }

    public String getNombreEquipo() {
        return nombreEquipo;
    }

    public void setNombreEquipo(String nombreEquipo) {
        this.nombreEquipo = nombreEquipo;
    }

    @Override
    public String toString() {
        return this.nombreEquipo+'('+Integer.toString(this.getiCantidadFallas())+')';
    }
}
