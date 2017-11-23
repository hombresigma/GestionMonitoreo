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
public class Monitoreo {

    public int getiCantidadFallas() {
        return iCantidadFallas;
    }

    public void setiCantidadFallas(int iCantidadFallas) {
        this.iCantidadFallas = iCantidadFallas;
    }
    private String nombreRaiz;
    private ImageIcon emptyIcon;
    private ImageIcon icon;
    private int iCantidadFallas = 0;

    public Monitoreo() {
        this.emptyIcon = new ImageIcon(getClass().getResource("imagenes/velsis.png"));
        this.icon = new ImageIcon(getClass().getResource("imagenes/velsis.png"));
    }

    public String getNombreRaiz() {
        return nombreRaiz;
    }

    public void setNombreRaiz(String nombreRaiz) {
        this.nombreRaiz = nombreRaiz;
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
        return "Ciudades y equipos ("+Integer.toString(this.getiCantidadFallas())+')';
    }

}
