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
public class Ciudad {

    public int getiCantidadFallas() {
        return iCantidadFallas;
    }

    public void setiCantidadFallas(int iCantidadFallas) {
        this.iCantidadFallas = iCantidadFallas;
    }
    private int iCantidadFallas = 0;
    private String nombreCiudad;
    private ImageIcon emptyIcon = new ImageIcon(getClass().getResource("imagenes/ciudad.gif"));
    private ImageIcon icon = new ImageIcon(getClass().getResource("imagenes/ciudad.gif"));

    public String getNombre() {
        return nombreCiudad;
    }

    public String getNombreCiudad() {
        return nombreCiudad;
    }

    public void setNombreCiudad(String nombreCiudad) {
        this.nombreCiudad = nombreCiudad;
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

    @Override
    public String toString() {
        return this.nombreCiudad + '(' + Integer.toString(this.getiCantidadFallas()) + ')';
    }

}
