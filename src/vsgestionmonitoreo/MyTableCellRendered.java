/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package vsgestionmonitoreo;

import java.awt.Color;
import java.awt.Component;
import javax.swing.Icon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;

/**
 *
 * @author Luis Rosero
 */


public class MyTableCellRendered extends DefaultTableCellRenderer {

    @Override
    public Component getTableCellRendererComponent(JTable jtable, Object o, boolean bln, boolean bln1, int i, int i1) {
        // o = new JCheckBox("Nuevo");
        setBackground(Color.white); 
        if (i1==10) {
           String sb=((Boolean)o).toString();
           if (sb.equals("false")){             
               setBackground(Color.MAGENTA);
           } else
           {
              setBackground(Color.white); 
           }
        }
        return super.getTableCellRendererComponent(jtable, o, bln, bln1, i, i1); //To change body of generated methods, choose Tools | Templates.
    }

}
