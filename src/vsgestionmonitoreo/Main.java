/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package vsgestionmonitoreo;

import javax.swing.ImageIcon;
import java.awt.Image;
import java.awt.Toolkit;
import javax.swing.*;

/**
 *
 * @author Luis Rosero
 */
public class Main {

    public static void main(String args[]) {

        java.awt.EventQueue.invokeLater(new Runnable() {

            public void run() {
                   new frmEquipoEstadoActual().setVisible(true) ;
            }
        });

    }
}
