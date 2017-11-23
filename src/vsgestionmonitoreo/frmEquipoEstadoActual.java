/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package vsgestionmonitoreo;

import Modelo.Ciudad;
import Modelo.Equipo;
import Modelo.Monitoreo;
import java.awt.event.ActionEvent;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import javax.swing.Timer;
import javax.swing.table.DefaultTableModel;
import javax.swing.tree.DefaultMutableTreeNode;
import java.awt.Toolkit;
import java.awt.event.ActionListener;
import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import javax.swing.tree.DefaultTreeModel;
import java.sql.PreparedStatement;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
import javax.swing.JOptionPane;
import javax.swing.tree.TreeNode;
import javax.swing.tree.TreePath;
import javax.swing.tree.TreeSelectionModel;

public final class frmEquipoEstadoActual extends javax.swing.JFrame {

    DefaultTableModel modelo;
    DefaultTableModel modeloHistorial;
    conectar cc;
    Connection cn;
    String sRutaPlanos;
    String ssql;
    String ssqlHistorial;
    String ssqlConteo;
    int iIntervaloLectura;

    public frmEquipoEstadoActual() {

        this.cc = new conectar();
        this.cn = cc.conexion();
        this.sRutaPlanos = "/";
        this.iIntervaloLectura = 120000;
        this.ssql = "SELECT eea.Equipo_idEquipo,e.nombreEquipo,c.idCiudad,c.nombreCiudad, eea.EstadoOperacion_idEstadoOperacion,eo.descripcionEstado,eea.fechaHora horaServidor,eea.fechaHoraCliente,cast(timediff( now(),eea.fechaHoraCliente) as char), eea.detalleEstadoOperacion, eea.actualizacionno "
                + "  FROM equipoestadoactual eea,EstadoOperacion eo,Equipo e, Ciudad c  "
                + "  WHERE bCorrecto=0 AND eea.Equipo_idEquipo=e.idEquipo AND eea.EstadoOperacion_idEstadoOperacion=eo.idEstadoOperacion AND c.idCiudad=e.Ciudad_idCiudad ";
        this.ssqlHistorial = "SELECT eea.Equipo_idEquipo,e.nombreEquipo,c.idCiudad,c.nombreCiudad, eea.EstadoOperacion_idEstadoOperacion,eo.descripcionEstado,eea.fechaHora horaServidor,eea.fechaHoraCliente,cast(timediff( now(),eea.fechaHoraCliente) as char), eea.detalleEstadoOperacion,eea.bcorrecto "
                + "  FROM equipoestado eea,EstadoOperacion eo,Equipo e, Ciudad c  "
                + "  WHERE eea.Equipo_idEquipo=e.idEquipo AND eea.EstadoOperacion_idEstadoOperacion=eo.idEstadoOperacion AND c.idCiudad=e.Ciudad_idCiudad "
                + "  AND e.idEquipo=((1)) AND eo.idEstadoOperacion=((2)) ";
        String sql = "SELECT rutaPlanos,intervaloLectura FROM configuracion ";

        try {
            Statement st = cn.createStatement();
            ResultSet rs = st.executeQuery(sql);
            while (rs.next()) {
                sRutaPlanos = rs.getString(1);
                iIntervaloLectura = rs.getInt(2);
                break;
            }
        } catch (SQLException ex) {
        }

        initComponents();
        initModelo();
        this.initModeloHistorial();
        initTreeview();
        mostrardatos("");

        timer = new Timer(iIntervaloLectura, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                mostrardatos("");
                informarNovedades(); 

            }
        });
        timer.setRepeats(true);
        timer.start();

    }

    public void initModelo() {
        modelo = new DefaultTableModel();
        modelo.addColumn("IdEquipo");
        modelo.addColumn("NombreEquipo");
        modelo.addColumn("IdCiudad");
        modelo.addColumn("Ciudad");
        modelo.addColumn("IdEstadoOperacion");
        modelo.addColumn("EstadoOperacion");
        modelo.addColumn("FechaHora");
        modelo.addColumn("FechaHoraCliente");
        modelo.addColumn("Duracion");
        modelo.addColumn("Detalle");
        modelo.addColumn("Visto");

        tbTabla.setDefaultRenderer(Object.class, new MyTableCellRendered());
        tbTabla.setModel(modelo);
        tbTabla.getColumnModel().getColumn(0).setPreferredWidth(60);
        tbTabla.getColumnModel().getColumn(1).setPreferredWidth(150);
        tbTabla.getColumnModel().getColumn(2).setPreferredWidth(25);
        tbTabla.getColumnModel().getColumn(3).setPreferredWidth(60);
        tbTabla.getColumnModel().getColumn(4).setPreferredWidth(40);
        tbTabla.getColumnModel().getColumn(5).setPreferredWidth(300);
        tbTabla.getColumnModel().getColumn(6).setPreferredWidth(140);
        tbTabla.getColumnModel().getColumn(7).setPreferredWidth(140);
        tbTabla.getColumnModel().getColumn(8).setPreferredWidth(70);
        tbTabla.getColumnModel().getColumn(9).setPreferredWidth(250);

    }

    public void initModeloHistorial() {
        modeloHistorial = new DefaultTableModel();
        modeloHistorial.addColumn("IdEquipo");
        modeloHistorial.addColumn("NombreEquipo");
        modeloHistorial.addColumn("IdCiudad");
        modeloHistorial.addColumn("Ciudad");
        modeloHistorial.addColumn("IdEstadoOperacion");
        modeloHistorial.addColumn("EstadoOperacion");
        modeloHistorial.addColumn("FechaHora");
        modeloHistorial.addColumn("FechaHoraCliente");
        modeloHistorial.addColumn("Duracion");
        modeloHistorial.addColumn("Detalle");
        modeloHistorial.addColumn("Correcto");

        tbTablaHistorial.setModel(modeloHistorial);
        tbTablaHistorial.getColumnModel().getColumn(0).setPreferredWidth(60);
        tbTablaHistorial.getColumnModel().getColumn(1).setPreferredWidth(150);
        tbTablaHistorial.getColumnModel().getColumn(2).setPreferredWidth(25);
        tbTablaHistorial.getColumnModel().getColumn(3).setPreferredWidth(60);
        tbTablaHistorial.getColumnModel().getColumn(4).setPreferredWidth(40);
        tbTablaHistorial.getColumnModel().getColumn(5).setPreferredWidth(300);
        tbTablaHistorial.getColumnModel().getColumn(6).setPreferredWidth(140);
        tbTablaHistorial.getColumnModel().getColumn(7).setPreferredWidth(140);
        tbTablaHistorial.getColumnModel().getColumn(8).setPreferredWidth(70);
        tbTablaHistorial.getColumnModel().getColumn(9).setPreferredWidth(250);
        tbTablaHistorial.getColumnModel().getColumn(10).setPreferredWidth(60);
    }

    private void mostrardatos(String valor) {
        try {
            int filas = tbTabla.getRowCount();
            for (int i = 0; filas > i; i++) {
                modelo.removeRow(0);
            }
        } catch (Exception e) {

        }

        try {
            int cuenta = 0;
            Statement st = cn.createStatement();
            ResultSet rs = st.executeQuery(ssql);
            while (rs.next()) {
                cuenta++;
                Object[] datos = {"", "", "", "", "", "", "", "", "", "", new Boolean(false)};
                datos[0] = rs.getString(1);
                datos[1] = rs.getString(2);
                datos[2] = rs.getString(3);
                datos[3] = rs.getString(4);
                datos[4] = rs.getString(5);
                datos[5] = rs.getString(6);
                datos[6] = rs.getString(7);
                datos[7] = rs.getString(8);
                datos[8] = rs.getString(9);
                datos[9] = rs.getString(10);
                datos[10] = rs.getBoolean(11);
                modelo.addRow(datos);
            }

            DefaultMutableTreeNode selectedNode = (DefaultMutableTreeNode) this.jtCiudades.getLastSelectedPathComponent();
            if (valor == "Monitoreo") {
                ((Monitoreo) selectedNode.getUserObject()).setiCantidadFallas(cuenta);
            }
            if (valor == "Ciudad") {
                ((Ciudad) selectedNode.getUserObject()).setiCantidadFallas(cuenta);
            }
            if (valor == "Equipo") {
                ((Equipo) selectedNode.getUserObject()).setiCantidadFallas(cuenta);
            }

        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
        }
    }

    private void mostrardatosHistoria(String valor) {
        int iFila = -1;
        int iColumna = -1;
        iFila = tbTabla.getSelectedRow();
        iColumna = tbTabla.getSelectedColumn();
        String sValor = (String) tbTabla.getValueAt(iFila, 0);
        if (iFila < 0) {
            return;
        }
        String ssqlHistorial1 = ssqlHistorial.replaceFirst("((1))", sValor);
        sValor = (String) tbTabla.getValueAt(iFila, 4);
        ssqlHistorial1 = ssqlHistorial1.replaceFirst("((2))", sValor);
        try {
            int filas = tbTablaHistorial.getRowCount();
            for (int i = 0; filas > i; i++) {
                modeloHistorial.removeRow(0);
            }
        } catch (Exception e) {

        }
        String[] datos = new String[11];
        try {
            int cuenta = 0;
            Statement st = cn.createStatement();
            ResultSet rs = st.executeQuery(ssqlHistorial1);
            while (rs.next()) {
                cuenta++;
                datos[0] = rs.getString(1);
                datos[1] = rs.getString(2);
                datos[2] = rs.getString(3);
                datos[3] = rs.getString(4);
                datos[4] = rs.getString(5);
                datos[5] = rs.getString(6);
                datos[6] = rs.getString(7);
                datos[7] = rs.getString(8);
                datos[8] = rs.getString(9);
                datos[9] = rs.getString(10);
                datos[10] = rs.getString(11);
                modeloHistorial.addRow(datos);
            }
            System.out.println(cuenta);
        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
        }
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jCheckBox1 = new javax.swing.JCheckBox();
        jspEstadoEquipo = new javax.swing.JSplitPane();
        pnDatos = new javax.swing.JScrollPane();
        jspDatos = new javax.swing.JSplitPane();
        jspTabla = new javax.swing.JScrollPane();
        tbTabla = new javax.swing.JTable();
        jspTablaHistorial = new javax.swing.JScrollPane();
        tbTablaHistorial = new javax.swing.JTable();
        pnTreeView = new javax.swing.JScrollPane();
        jtCiudades = new javax.swing.JTree();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("SISTEMA DE MONITOREO DE EQUIPOS VELSIS");
        addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                formKeyPressed(evt);
            }
            public void keyTyped(java.awt.event.KeyEvent evt) {
                formKeyTyped(evt);
            }
        });

        jCheckBox1.setText("jCheckBox1");

        jspDatos.setOrientation(javax.swing.JSplitPane.VERTICAL_SPLIT);
        jspDatos.setAutoscrolls(true);

        tbTabla.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {

            }
        ));
        tbTabla.setAutoResizeMode(javax.swing.JTable.AUTO_RESIZE_OFF);
        tbTabla.setName(""); // NOI18N
        tbTabla.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tbTablaMouseClicked(evt);
            }
        });
        tbTabla.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                tbTablaKeyPressed(evt);
            }
            public void keyReleased(java.awt.event.KeyEvent evt) {
                tbTablaKeyReleased(evt);
            }
            public void keyTyped(java.awt.event.KeyEvent evt) {
                tbTablaKeyTyped(evt);
            }
        });
        jspTabla.setViewportView(tbTabla);

        jspDatos.setLeftComponent(jspTabla);

        tbTablaHistorial.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {

            }
        ));
        tbTablaHistorial.setAutoResizeMode(javax.swing.JTable.AUTO_RESIZE_OFF);
        tbTablaHistorial.setName(""); // NOI18N
        tbTablaHistorial.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                tbTablaHistorialKeyPressed(evt);
            }
        });
        jspTablaHistorial.setViewportView(tbTablaHistorial);

        jspDatos.setBottomComponent(jspTablaHistorial);

        pnDatos.setViewportView(jspDatos);

        jspEstadoEquipo.setRightComponent(pnDatos);

        javax.swing.tree.DefaultMutableTreeNode treeNode1 = new javax.swing.tree.DefaultMutableTreeNode("root");
        jtCiudades.setModel(new javax.swing.tree.DefaultTreeModel(treeNode1));
        jtCiudades.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jtCiudadesMouseClicked(evt);
            }
        });
        jtCiudades.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                jtCiudadesKeyPressed(evt);
            }
        });
        pnTreeView.setViewportView(jtCiudades);

        jspEstadoEquipo.setLeftComponent(pnTreeView);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jspEstadoEquipo, javax.swing.GroupLayout.DEFAULT_SIZE, 1655, Short.MAX_VALUE)
            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(layout.createSequentialGroup()
                    .addGap(0, 0, Short.MAX_VALUE)
                    .addComponent(jCheckBox1, javax.swing.GroupLayout.PREFERRED_SIZE, 1537, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGap(0, 0, Short.MAX_VALUE)))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jspEstadoEquipo, javax.swing.GroupLayout.PREFERRED_SIZE, 967, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(layout.createSequentialGroup()
                    .addGap(0, 0, Short.MAX_VALUE)
                    .addComponent(jCheckBox1, javax.swing.GroupLayout.PREFERRED_SIZE, 554, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGap(0, 0, Short.MAX_VALUE)))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    public void seleccionaDatosFiltrados() {
        String sTipoNodo;
        sTipoNodo = "";

        String dato;
        dato = "";

        DefaultMutableTreeNode selectedNode = (DefaultMutableTreeNode) this.jtCiudades.getLastSelectedPathComponent();

        String sWhereAux;
        sWhereAux = "";
        if (null == selectedNode) {
        } else {
            if (selectedNode.getUserObject() instanceof Equipo) {
                sTipoNodo = "Equipo";
                dato = ((Equipo) selectedNode.getUserObject()).getNombreEquipo();
                sWhereAux = "  AND e.nombreEquipo=\"" + dato + "\"";
            } else {
                if (selectedNode.getUserObject() instanceof Ciudad) {
                    sTipoNodo = "Ciudad";
                    dato = ((Ciudad) selectedNode.getUserObject()).getNombreCiudad();
                    sWhereAux = " AND c.nombreCiudad=\"" + dato + "\"";
                }
            }
        }

        ssql = "SELECT eea.Equipo_idEquipo,e.nombreEquipo,c.idCiudad,c.nombreCiudad, eea.EstadoOperacion_idEstadoOperacion,eo.descripcionEstado,eea.fechaHora horaServidor,eea.fechaHoraCliente,cast(timediff( now(),eea.fechaHoraCliente) as char), eea.detalleEstadoOperacion, eea.actualizacionno "
                + "  FROM equipoestadoactual eea,EstadoOperacion eo,Equipo e, Ciudad c  "
                + "  WHERE bCorrecto=0 AND eea.Equipo_idEquipo=e.idEquipo AND eea.EstadoOperacion_idEstadoOperacion=eo.idEstadoOperacion AND c.idCiudad=e.Ciudad_idCiudad "
                + sWhereAux;

        this.mostrardatos(sTipoNodo);
        //this.modificaTreeview();
    }

    /**
     *
     * @param sTipoNodo El tipo de nodo Ciudad o Equipo
     * @param dato El dato de la ciudad o del equipo
     * @return sentencia consulta SQL según tipo de nodo
     */
    public String sentenciaSQLConteo(String sTipoNodo, String dato) {
        String sWhereAux;
        sWhereAux = "";

        if ("Equipo".equals(sTipoNodo)) {
            sWhereAux = "  AND e.nombreEquipo=\"" + dato + "\"";
        } else {
            if ("Ciudad".equals(sTipoNodo)) {
                sWhereAux = " AND c.nombreCiudad=\"" + dato + "\"";
            }
        }
        ssqlConteo = "SELECT count(*) "
                + "  FROM equipoestadoactual eea,EstadoOperacion eo,Equipo e, Ciudad c  "
                + "  WHERE bCorrecto=0 AND eea.Equipo_idEquipo=e.idEquipo AND eea.EstadoOperacion_idEstadoOperacion=eo.idEstadoOperacion AND c.idCiudad=e.Ciudad_idCiudad "
                + sWhereAux;
        return ssqlConteo;
    }


    private void jtCiudadesMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jtCiudadesMouseClicked
        seleccionaDatosFiltrados();

    }//GEN-LAST:event_jtCiudadesMouseClicked

    private void formKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_formKeyTyped

    }//GEN-LAST:event_formKeyTyped

    private void formKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_formKeyPressed

    }//GEN-LAST:event_formKeyPressed

    private void jtCiudadesKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jtCiudadesKeyPressed
    }//GEN-LAST:event_jtCiudadesKeyPressed

    private void tbTablaKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_tbTablaKeyPressed
        mostrardatosHistoria("");
    }//GEN-LAST:event_tbTablaKeyPressed

    private void tbTablaHistorialKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_tbTablaHistorialKeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_tbTablaHistorialKeyPressed

    private void tbTablaMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tbTablaMouseClicked
        mostrardatosHistoria("");

    }//GEN-LAST:event_tbTablaMouseClicked

    private void tbTablaKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_tbTablaKeyTyped
        mostrardatosHistoria("");
    }//GEN-LAST:event_tbTablaKeyTyped

    private void tbTablaKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_tbTablaKeyReleased
        mostrardatosHistoria("");         // TODO add your handling code here:
    }//GEN-LAST:event_tbTablaKeyReleased

    private void initTreeview() {
        setIconImage(Toolkit.getDefaultToolkit().getImage("d:/monitoreo.gif"));
        jtCiudades.setCellRenderer(new MyTreeCellRenderer());
        // jtCiudades.setCellRenderer( new MyTreeCellRenderer());
        Monitoreo mn = new Monitoreo();
        mn.setNombreRaiz("Velsis");
        DefaultMutableTreeNode root = new DefaultMutableTreeNode(mn);
        this.rootgeneral = root;
        int iCantidadFallas = this.contar("Monitoreo", "");
        mn.setiCantidadFallas(iCantidadFallas);
        String sql = "SELECT idciudad,nombreciudad  FROM ciudad ";
        int iCantidadNovedadesGeneral = 0;
        int cuenta = 0;
        try {
            Statement st = cn.createStatement();
            ResultSet rs = st.executeQuery(sql);
            rs.first();
            do {
                if (cuenta > 20) {
                    System.out.println("Se supera máximo de ciudades");
                    break;
                }
                Ciudad cd = new Ciudad();
                cd.setNombreCiudad(rs.getString(2));
                DefaultMutableTreeNode nodoCiudad = new DefaultMutableTreeNode(cd);

                iCantidadFallas = this.contar("Ciudad", rs.getString(2));
                cd.setiCantidadFallas(iCantidadFallas);

                String sql2 = "SELECT idEquipo,nombreEquipo  FROM equipo where Ciudad_idCiudad=" + Integer.toString(rs.getInt(1));
                int cuenta2 = 0;
                int iCantidadNovedades = 0;
                try {
                    Statement st2 = cn.createStatement();
                    ResultSet rs2 = st2.executeQuery(sql2);
                    rs2.first();
                    do {
                        if (cuenta2 > 100) {
                            System.out.println("Se supera máximo de equipos");
                            break;
                        }
                        Equipo eq = new Equipo();
                        eq.setNombreEquipo(rs2.getString(2));
                        DefaultMutableTreeNode nodoEquipo = new DefaultMutableTreeNode(eq);

                        iCantidadFallas = this.contar("Equipo", rs2.getString(2));
                        eq.setiCantidadFallas(iCantidadFallas);
                        nodoCiudad.add(nodoEquipo);
                        iCantidadNovedades = iCantidadNovedades + eq.getiCantidadFallas();
                        cuenta2++;
                    } while (rs2.next());
                } catch (SQLException ex) {

                }
                cd.setiCantidadFallas(iCantidadNovedades);
                root.add(nodoCiudad);
                iCantidadNovedadesGeneral = iCantidadNovedadesGeneral + iCantidadNovedades;
                cuenta++;
            } while (rs.next());
            //jtCiudades.expandPath(jtCiudades.getEditingPath());
            mn.setiCantidadFallas(iCantidadNovedadesGeneral);
            DefaultTreeModel modelo1 = new DefaultTreeModel(root);
            jtCiudades.setModel(modelo1);
        } catch (SQLException ex) {

        }
    }

    private void modificaTreeview() {
        for (int i = 0; i < rootgeneral.getChildCount(); i++) {
            DefaultMutableTreeNode dmtCiudad = (DefaultMutableTreeNode) this.rootgeneral.getChildAt(i);
            Ciudad cd = ((Ciudad) dmtCiudad.getUserObject());
            int iCantidadFallas = this.contar("Ciudad", cd.getNombreCiudad());
            String sNombreCiudad = cd.getNombreCiudad();
            if (sNombreCiudad.equals("Bello")) {
                sNombreCiudad = "otra";
            }
            String s = sNombreCiudad;
            cd.setiCantidadFallas(iCantidadFallas);
            for (int j = 0; j < dmtCiudad.getChildCount(); j++) {
                DefaultMutableTreeNode dmtEquipo = (DefaultMutableTreeNode) dmtCiudad.getChildAt(j);
                Equipo eq = ((Equipo) dmtEquipo.getUserObject());
                iCantidadFallas = this.contar("Equipo", eq.getNombreEquipo());
                eq.setiCantidadFallas(iCantidadFallas);
            }
        }

        //TreePath p;
        //TreeSelectionModel a = this.jtCiudades.getSelectionModel();
        //p = a.getSelectionPath();
        DefaultTreeModel model = (DefaultTreeModel) this.jtCiudades.getModel();
        DefaultMutableTreeNode root = (DefaultMutableTreeNode) model.getRoot();
        model.reload(root);
        //jtCiudades.expandPath(p.getParentPath());

    }

    public int contar(String ssqlConteo) {
        int cuenta = 0;
        try {
            Statement st = cn.createStatement();
            try (ResultSet rs = st.executeQuery(ssqlConteo)) {
                rs.first();
                cuenta = rs.getInt(1);
            }
        } catch (SQLException ex) {

        }
        return cuenta;
    }

    public int contar(String sTipoNodo, String sDato) {
        String sqlconteo = this.sentenciaSQLConteo(sTipoNodo, sDato);
        return this.contar(sqlconteo);
    }

    public void reproducirSonido() {
        try {
            Clip sonido = null;
            try {

                try {
                    sonido = AudioSystem.getClip();
                    //ile a = new File(nombreSonido);
                } catch (LineUnavailableException ex) {
                    JOptionPane.showMessageDialog(null, "Verifique la salida de audio.");
                }

                InputStream path = null;
                try {
                    //path=new BufferedInputStream(new FileInputStream("/bell3.wav"));  //La raiz del disco
                    //path=new BufferedInputStream(new FileInputStream("bell5.wav"));  //La raiz del proyecto  sin src
                    path = new BufferedInputStream(new FileInputStream(sNombreSonido));  //La raiz del proyecto
                } catch (FileNotFoundException ex) {
                    JOptionPane.showMessageDialog(null, "Archivo no encontrado:" + sNombreSonido);
                }

                try {
                    //Relativa a la carpeta . en este caso vsgestionmonitoreo
                    //path = this.getClass().getResourceAsStream("Sonidos/bell2.wav");
                    //Relativa a Src
                    //path = this.getClass().getResourceAsStream("/bell3.wav");
                    sonido.open(AudioSystem.getAudioInputStream(path));
                } catch (LineUnavailableException ex) {
                    JOptionPane.showMessageDialog(null, "Linea de audio no disponible.");
                }
                sonido.start();
            } catch (UnsupportedAudioFileException | IOException ex) {
                JOptionPane.showMessageDialog(null, "Problema desconocido con la reproducción de audio.");
            }
            Thread.sleep(1000); //
            if (sonido != null) {
                sonido.close();
            } else {
            }

        } catch (InterruptedException ex) {
            Logger.getLogger(frmEquipoEstadoActual.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * Informa de cambios en los estados de las alertas ON a OFF o viceversa
     *
     */
    public void informarNovedades() {

        DefaultMutableTreeNode selectedNode = (DefaultMutableTreeNode) frmEquipoEstadoActual.this.jtCiudades.getLastSelectedPathComponent();
        TreeNode p;
        if (!selectedNode.isRoot()) {
            p = selectedNode.getParent();

        } else {
            p = selectedNode;
        }
        TreeSelectionModel tsm = jtCiudades.getSelectionModel();
        jtCiudades.setSelectionModel(null);
        modificaTreeview();

        try {
            Statement st;
            st = cn.createStatement();
            ResultSet rs = st.executeQuery(ssql);
            while (rs.next()) {
                if (rs.getString(11).equals("0")) {
                    this.reproducirSonido();
                    JOptionPane.showMessageDialog(null, "Equipo " + rs.getString(2) + " " + rs.getString(6));
                    PreparedStatement pst;
                    String ssqlu = "UPDATE EquipoEstadoActual SET actualizacionno=1 WHERE Equipo_idEquipo=" + rs.getString(1) + " AND EstadoOperacion_idEstadoOperacion=" + rs.getString(5);
                    pst = cn.prepareStatement(ssqlu);
                    pst.executeUpdate();
                }
            };

        } catch (SQLException ex) {
            Logger.getLogger(frmEquipoEstadoActual.class
                    .getName()).log(Level.SEVERE, null, ex);
        }

        modificaTreeview();
        jtCiudades.expandPath(new TreePath(((DefaultMutableTreeNode) p).getPath()));
        jtCiudades.setSelectionModel(tsm);
    }

    TreePath tp;
    DefaultMutableTreeNode rootgeneral;
    Timer timer;
    private String sNombreSonido = "dive10.wav";
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JCheckBox jCheckBox1;
    private javax.swing.JSplitPane jspDatos;
    private javax.swing.JSplitPane jspEstadoEquipo;
    private javax.swing.JScrollPane jspTabla;
    private javax.swing.JScrollPane jspTablaHistorial;
    private javax.swing.JTree jtCiudades;
    private javax.swing.JScrollPane pnDatos;
    private javax.swing.JScrollPane pnTreeView;
    private javax.swing.JTable tbTabla;
    private javax.swing.JTable tbTablaHistorial;
    // End of variables declaration//GEN-END:variables

}
