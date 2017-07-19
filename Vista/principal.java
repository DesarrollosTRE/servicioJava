/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Vista;
import java.awt.SystemTray;
import java.awt.TrayIcon;
import javax.swing.ImageIcon;
import javax.swing.JOptionPane;
import Controlador.*;
import Vista.Interfaces.MENSAJES;
import java.awt.Color;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.TrayIcon.MessageType;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.JSlider;
import javax.swing.JTextPane;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyleContext;
import javax.swing.text.AttributeSet;
/**
 *
 * @author PROGRAMADOR
 */
public class principal extends javax.swing.JFrame implements MENSAJES{
  private ImageIcon imagenIcono;
  private TrayIcon trayIcon;
  private SystemTray systemTray;
  private Coordinador miCoordinador; //objeto miCoordinador que permite la relacion entre esta clase y la clase coordinador
  JScrollPane scroll; 

    public principal() {
        this.setTitle("Intranet .:IER:. Version 1.5.2");   
        scroll = new JScrollPane(txtLogCreacion);    
        scroll.setBounds(new Rectangle(30,30,100,200));
        //
        imagenIcono= new ImageIcon(this.getClass().getResource("/imagenes/icon.png"));
        initComponents();
        initTextoLog();
        this.setIconImage(imagenIcono.getImage());
        instanciarTrayIcon();
        //
        Image play;
        try {
            play = ImageIO.read(getClass().getResource("/imagenes/play.png"));
            //btnIniciar.setIcon(new ImageIcon(play));
              //
              Image stop = ImageIO.read(getClass().getResource("/imagenes/stop.png"));
              //btnDetener.setIcon(new ImageIcon(stop));
            } catch (IOException ex) {
            Logger.getLogger(principal.class.getName()).log(Level.SEVERE, null, ex);
        }
        this.getContentPane().setBackground(Color.WHITE);
        cerrar();
        minimizar();
        //
    }
    public void setCoordinador(Coordinador miCoordinador) {
        this.miCoordinador=miCoordinador;
    }

    public JLabel getLblMensajeEstadoRec() {
        return lblMensajeEstadoRec;
    }

    public void setLblMensajeEstadoRec(String lblMensajeEstadoRec) {
        this.lblMensajeEstadoRec.setText(lblMensajeEstadoRec);
    }
    public void setLblHoraIteracion(String hora) {
        this.lblHoraProximaTer.setText(hora);
    }
    public void setLblMinutosIteracion(String minuto){
        this.lblMinutoProximaIte.setText(minuto);
    }
    public void setLblSegundosIteracion(String segundos){
        this.lblSegundosTiempoIte.setText(segundos);
    }
    public void reestablecerTimer(){
        this.lblSegundosTiempoIte.setText("00");
        this.lblMinutoProximaIte.setText("00");
        this.lblHoraProximaTer.setText("00");
    }
    public JLabel getLblTiempoReconexion() {
        return lblTiempoReconexion;
    }

    public void setLblTiempoReconexion(String lblTiempoReconexion) {
        this.lblTiempoReconexion.setText(lblTiempoReconexion);
    }
    public String getTxtLogCreacion(){
        return this.txtLogCreacion.getText();
    }
    private void initTextoLog(){
        MensajeLog("Al iniciar cada proceso se demora:\n\n" +
"	\t\t\t-Proceso de Variables: Inicia a los 5 Min.\n" +
"	\t\t\t-Proceso de Proveedor: Inica a los 10 Min.\n" +
"	\t\t\t-Proceso de Trabajador: Inicia a los 15 Min.", true);
        MensajeLog("\n",false);
    }
    private void instanciarTrayIcon(){
        trayIcon=new TrayIcon(imagenIcono.getImage(),"Mostrar Ventana",popUp);
        ActionListener actionListener = new ActionListener() {
            @Override
            public void actionPerformed( ActionEvent e ) {
                systemTray.remove(trayIcon);
                visible();
            }
        };
        trayIcon.addActionListener(actionListener);
        trayIcon.setImageAutoSize(true);
        systemTray= SystemTray.getSystemTray();
    }
    private void visible(){
        this.setVisible(true);
        this.setExtendedState(NORMAL);
    }
    public void minimizar(){
//        this.setExtendedState(JFrame.);
        addWindowListener(new WindowAdapter(){
             @Override
             public void windowIconified(WindowEvent e){
                 try {
                     if(SystemTray.isSupported()){
                         systemTray.add(trayIcon);
                         e.getWindow().setVisible(false);
                     }
                    } catch (Exception ex) {
                        JOptionPane.showMessageDialog(null,ex.getMessage(), 
                   "Error En Segundo Plano", JOptionPane.ERROR_MESSAGE);
                    }
             }
            });
    }
    public void cerrar(){
        try {
            this.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
            addWindowListener(new WindowAdapter(){
             @Override
             public void windowClosing(WindowEvent e){
              confirmarSalida();
             }
            });
            setVisible(true);
        } catch (Exception e) {

        }
    }
    public void confirmarSalida(){
       if(miCoordinador.consultarServicioIniciado(MENSAJES.PROCESO_TRABAJADOR) || miCoordinador.consultarServicioIniciado(MENSAJES.PROCESO_VARIABLE) || miCoordinador.consultarServicioIniciado(MENSAJES.PROCESO_PROVEEDOR)){
            int dialogButton = JOptionPane.YES_NO_OPTION;
            int dialogResult = JOptionPane.showConfirmDialog (null, "Existe un Servicio iniciado, ¿Dese salir y finalizar este?","Advertencia",dialogButton);
            if(dialogResult == JOptionPane.YES_OPTION){
               //miCoordinador.finalizarServicio(MENSAJES.PROCESO_TODOS);
                System.exit(0); 
            }  
        }else{
            System.exit(0); 
        }
    }
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        menuBar1 = new java.awt.MenuBar();
        menu1 = new java.awt.Menu();
        popUp = new java.awt.PopupMenu();
        primerPlano = new java.awt.MenuItem();
        jPanel1 = new javax.swing.JPanel();
        btnIniciar = new javax.swing.JButton();
        btnDetener = new javax.swing.JButton();
        jSeparator1 = new javax.swing.JSeparator();
        lblEstadoServicioVariables = new javax.swing.JLabel();
        lblEstadoServicioTrabajador = new javax.swing.JLabel();
        lblEstadoServicioProveedor = new javax.swing.JLabel();
        lblNombreProcesoVariables = new javax.swing.JLabel();
        lblNombreProcesoTrabajador = new javax.swing.JLabel();
        lblNombreProcesoProveedor = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        txtLogCreacion = new javax.swing.JTextPane();
        jPanel2 = new javax.swing.JPanel();
        lblTiempoReconexion = new javax.swing.JLabel();
        lblMensajeEstadoRec = new javax.swing.JLabel();
        jPanel3 = new javax.swing.JPanel();
        jPanel4 = new javax.swing.JPanel();
        lblHoraProximaTer = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        lblMinutoProximaIte = new javax.swing.JLabel();
        jLabel1 = new javax.swing.JLabel();
        lblSegundosTiempoIte = new javax.swing.JLabel();
        jSeparator2 = new javax.swing.JSeparator();
        jSeparator3 = new javax.swing.JSeparator();

        menu1.setLabel("File");
        menu1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                menu1ActionPerformed(evt);
            }
        });
        menuBar1.add(menu1);

        popUp.setLabel("popupMenu1");
        popUp.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                popUpActionPerformed(evt);
            }
        });

        primerPlano.setLabel("Mostrar Aplicacion");
        primerPlano.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                primerPlanoActionPerformed(evt);
            }
        });
        popUp.add(primerPlano);

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setMinimumSize(new java.awt.Dimension(900, 600));
        setName("Generador P267 P171"); // NOI18N

        jPanel1.setBackground(new java.awt.Color(255, 255, 255));
        jPanel1.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createEtchedBorder(), "Acciones", javax.swing.border.TitledBorder.LEFT, javax.swing.border.TitledBorder.DEFAULT_POSITION));

        btnIniciar.setText("Iniciar Todo");
        btnIniciar.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
        btnIniciar.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnIniciar.setMaximumSize(new java.awt.Dimension(103, 23));
        btnIniciar.setMinimumSize(new java.awt.Dimension(103, 23));
        btnIniciar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnIniciarActionPerformed(evt);
            }
        });

        btnDetener.setText("Detener Todo");
        btnDetener.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
        btnDetener.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnDetener.setMaximumSize(new java.awt.Dimension(103, 23));
        btnDetener.setMinimumSize(new java.awt.Dimension(103, 23));
        btnDetener.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnDetenerActionPerformed(evt);
            }
        });

        lblEstadoServicioVariables.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblEstadoServicioVariables.setText("Sin Iniciar");

        lblEstadoServicioTrabajador.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblEstadoServicioTrabajador.setText("Sin Iniciar");

        lblEstadoServicioProveedor.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblEstadoServicioProveedor.setText("Sin Iniciar");

        lblNombreProcesoVariables.setText("Proc. Variables");

        lblNombreProcesoTrabajador.setText("Proc. Trabajador");

        lblNombreProcesoProveedor.setText("Proc. Proveedor");

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jSeparator1)
            .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                .addGroup(jPanel1Layout.createSequentialGroup()
                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(jPanel1Layout.createSequentialGroup()
                            .addComponent(lblNombreProcesoProveedor)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(lblEstadoServicioProveedor, javax.swing.GroupLayout.PREFERRED_SIZE, 65, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                            .addComponent(lblNombreProcesoVariables)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(lblEstadoServicioVariables, javax.swing.GroupLayout.PREFERRED_SIZE, 65, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                            .addComponent(lblNombreProcesoTrabajador)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(lblEstadoServicioTrabajador, javax.swing.GroupLayout.PREFERRED_SIZE, 65, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGap(110, 110, 110))
                .addGroup(jPanel1Layout.createSequentialGroup()
                    .addContainerGap(72, Short.MAX_VALUE)
                    .addComponent(btnIniciar, javax.swing.GroupLayout.PREFERRED_SIZE, 142, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGap(38, 38, 38)
                    .addComponent(btnDetener, javax.swing.GroupLayout.PREFERRED_SIZE, 151, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addContainerGap(46, Short.MAX_VALUE)))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(btnDetener, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(btnIniciar, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(18, 18, 18)
                        .addComponent(jSeparator1, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(lblEstadoServicioVariables, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(lblNombreProcesoVariables))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(lblEstadoServicioTrabajador, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(lblNombreProcesoTrabajador))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(lblEstadoServicioProveedor, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                        .addComponent(lblNombreProcesoProveedor)
                        .addGap(6, 6, 6))))
        );

        jScrollPane1.setViewportView(txtLogCreacion);

        jPanel2.setBackground(new java.awt.Color(255, 255, 255));
        jPanel2.setBorder(javax.swing.BorderFactory.createEtchedBorder(new java.awt.Color(0, 0, 0), new java.awt.Color(204, 204, 204)));

        lblTiempoReconexion.setForeground(new java.awt.Color(255, 255, 255));
        lblTiempoReconexion.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);

        lblMensajeEstadoRec.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lblMensajeEstadoRec.setText("Estableciendo conexión MYSQL");
        lblMensajeEstadoRec.setToolTipText("");

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                .addGap(0, 0, Short.MAX_VALUE)
                .addComponent(lblMensajeEstadoRec, javax.swing.GroupLayout.PREFERRED_SIZE, 376, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(lblTiempoReconexion, javax.swing.GroupLayout.PREFERRED_SIZE, 110, javax.swing.GroupLayout.PREFERRED_SIZE))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(lblMensajeEstadoRec, javax.swing.GroupLayout.PREFERRED_SIZE, 21, javax.swing.GroupLayout.PREFERRED_SIZE)
            .addComponent(lblTiempoReconexion, javax.swing.GroupLayout.PREFERRED_SIZE, 21, javax.swing.GroupLayout.PREFERRED_SIZE)
        );

        jPanel3.setBackground(new java.awt.Color(255, 255, 255));
        jPanel3.setBorder(javax.swing.BorderFactory.createTitledBorder("Proxima Iteración"));

        jPanel4.setBackground(new java.awt.Color(0, 0, 0));
        jPanel4.setBorder(javax.swing.BorderFactory.createEtchedBorder(java.awt.Color.red, java.awt.Color.orange));

        lblHoraProximaTer.setFont(new java.awt.Font("Nirmala UI Semilight", 1, 36)); // NOI18N
        lblHoraProximaTer.setForeground(new java.awt.Color(255, 255, 255));
        lblHoraProximaTer.setText("00");

        jLabel3.setFont(new java.awt.Font("Nirmala UI Semilight", 1, 36)); // NOI18N
        jLabel3.setForeground(new java.awt.Color(255, 255, 255));
        jLabel3.setText(":");

        lblMinutoProximaIte.setFont(new java.awt.Font("Nirmala UI Semilight", 1, 36)); // NOI18N
        lblMinutoProximaIte.setForeground(new java.awt.Color(255, 255, 255));
        lblMinutoProximaIte.setText("00");

        jLabel1.setFont(new java.awt.Font("Nirmala UI Semilight", 1, 36)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(255, 255, 255));
        jLabel1.setText(":");

        lblSegundosTiempoIte.setFont(new java.awt.Font("Nirmala UI Semilight", 1, 36)); // NOI18N
        lblSegundosTiempoIte.setForeground(new java.awt.Color(255, 255, 255));
        lblSegundosTiempoIte.setText("00");

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(lblHoraProximaTer, javax.swing.GroupLayout.DEFAULT_SIZE, 44, Short.MAX_VALUE)
                .addGap(5, 5, 5)
                .addComponent(jLabel3)
                .addGap(5, 5, 5)
                .addComponent(lblMinutoProximaIte)
                .addGap(5, 5, 5)
                .addComponent(jLabel1)
                .addGap(5, 5, 5)
                .addComponent(lblSegundosTiempoIte, javax.swing.GroupLayout.PREFERRED_SIZE, 52, javax.swing.GroupLayout.PREFERRED_SIZE))
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(lblHoraProximaTer, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(lblMinutoProximaIte, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(lblSegundosTiempoIte, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 63, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(0, 0, Short.MAX_VALUE))
        );

        jSeparator2.setForeground(new java.awt.Color(255, 153, 0));

        jSeparator3.setForeground(new java.awt.Color(255, 51, 51));

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addComponent(jSeparator3, javax.swing.GroupLayout.PREFERRED_SIZE, 144, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(1, 1, 1)
                .addComponent(jPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(1, 1, 1)
                .addComponent(jSeparator2, javax.swing.GroupLayout.DEFAULT_SIZE, 154, Short.MAX_VALUE))
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addContainerGap(23, Short.MAX_VALUE)
                        .addComponent(jPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addGap(48, 48, 48)
                        .addComponent(jSeparator2, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(24, 24, 24))
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addGap(48, 48, 48)
                .addComponent(jSeparator3, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane1, javax.swing.GroupLayout.Alignment.TRAILING)
            .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGap(18, 18, 18)
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(9, 9, 9)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 376, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void menu1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_menu1ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_menu1ActionPerformed

    private void popUpActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_popUpActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_popUpActionPerformed

    private void primerPlanoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_primerPlanoActionPerformed
        // TODO add your handling code here:
          systemTray.remove(trayIcon);
          visible();
    }//GEN-LAST:event_primerPlanoActionPerformed

    private void btnDetenerActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnDetenerActionPerformed
        // TODO add your handling code here:
        try{
            if(!miCoordinador.consultarServicioIniciado(MENSAJES.PROCESO_TRABAJADOR) && !miCoordinador.consultarServicioIniciado(MENSAJES.PROCESO_VARIABLE) && !miCoordinador.consultarServicioIniciado(MENSAJES.PROCESO_PROVEEDOR)){
                JOptionPane.showMessageDialog(null, "Todos los servicios estan detenidos",
                    "Error", JOptionPane.ERROR_MESSAGE);
            }
            if(miCoordinador.consultarServicioIniciado(MENSAJES.PROCESO_TRABAJADOR) && miCoordinador.consultarServicioIniciado(MENSAJES.PROCESO_VARIABLE) && miCoordinador.consultarServicioIniciado(MENSAJES.PROCESO_PROVEEDOR)){
                miCoordinador.finalizarServicio();
            }
        } catch (Exception e) {
            System.out.println(e.getLocalizedMessage());
            MensajeLog(e.getLocalizedMessage(), true);
            MensajeLog("\n",false);
        }
    }//GEN-LAST:event_btnDetenerActionPerformed

    private void btnIniciarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnIniciarActionPerformed
        // TODO add your handling code here:
        try {
            if(miCoordinador.consultarServicioIniciado(MENSAJES.PROCESO_TRABAJADOR) && miCoordinador.consultarServicioIniciado(MENSAJES.PROCESO_VARIABLE) && miCoordinador.consultarServicioIniciado(MENSAJES.PROCESO_PROVEEDOR)){
                JOptionPane.showMessageDialog(null, "Los servicios estan iniciados",
                    "Error", JOptionPane.ERROR_MESSAGE);
            }else if(miCoordinador.consultarServicioIniciado(MENSAJES.PROCESO_TRABAJADOR) || miCoordinador.consultarServicioIniciado(MENSAJES.PROCESO_VARIABLE) || miCoordinador.consultarServicioIniciado(MENSAJES.PROCESO_PROVEEDOR)){
                JOptionPane.showMessageDialog(null, "Existe un proceso iniciado",
                    "Error", JOptionPane.ERROR_MESSAGE);
            }else{
                int tiempoIntervalos = miCoordinador.calcularHoraSiguienteIteracion();
                miCoordinador.iniciarTodosLosServicios(tiempoIntervalos);
            }
        } catch (NullPointerException e) {
            MensajeLog(e.getLocalizedMessage(), true);
            MensajeLog("\n",false);
        }
    }//GEN-LAST:event_btnIniciarActionPerformed
  public void cambiarColorLblTiempoConexion(String textoEstado, Color colorFondo){
    lblTiempoReconexion.setText(textoEstado);
    lblTiempoReconexion.setBackground(colorFondo);
    lblTiempoReconexion.setOpaque(true);
  }
//Muestra una burbuja con la accion que se realiza
    public void MensajeTrayIcon(String texto, MessageType tipo)
    {
        trayIcon.displayMessage("Mensaje:", texto, tipo);        
    }
    public void MensajeLog(String texto,boolean isError){
        if(texto.equals("\n")){
            agregarTextoConColorLog(txtLogCreacion,MENSAJES.LINEA_SEPARACION,Color.LIGHT_GRAY); 
        }else{
            Date date = new Date();
            DateFormat hourdateFormat = new SimpleDateFormat("HH:mm:ss  dd/MM/yyyy");
            if(isError){ 
                agregarTextoConColorLog(txtLogCreacion,"\n"+hourdateFormat.format(date)+"   [General]   "+texto,Color.RED);
            }else{
                agregarTextoConColorLog(txtLogCreacion,"\n"+hourdateFormat.format(date)+"   [General]   "+texto,Color.BLACK);
            } 
         }
    }
    public void MensajeLog(HashMap<String,String[]> respuesta,String departamento){
        Date date = new Date();
        DateFormat hourdateFormat = new SimpleDateFormat("HH:mm:ss  dd/MM/yyyy");
        if(!Boolean.valueOf(respuesta.get(departamento)[1])){
            agregarTextoConColorLog(txtLogCreacion,"\n"+hourdateFormat.format(date)+"   ["+departamento+"]  "+respuesta.get(departamento)[0],Color.RED);   
            cambiarLabelsServicios(departamento,Color.RED,"Detenido");
        }else if(departamento.equals(MENSAJES.PROCESO_TODOS)){
            cambiarLabelsServicios(departamento,Color.GREEN,"Corriendo");
            agregarTextoConColorLog(txtLogCreacion,"\n"+hourdateFormat.format(date)+"   ["+departamento+"]  "+respuesta.get(departamento)[0],Color.BLACK);   
        }else{
            agregarTextoConColorLog(txtLogCreacion,"\n"+hourdateFormat.format(date)+"   ["+departamento+"]  "+respuesta.get(departamento)[0],Color.BLACK);   
        }
    }
    public void cambiarLabelsServicios(String texto,Color colorFondo,String textoEstado){
        switch(texto){
            case MENSAJES.PROCESO_CARGO:
            case MENSAJES.PROCESO_BANCO:
            case MENSAJES.PROCESO_AREATRABAJO:
            case MENSAJES.PROCESO_TRABAJADOR:
                    lblEstadoServicioTrabajador.setText(textoEstado);
                    lblEstadoServicioTrabajador.setBackground(colorFondo);
                    lblEstadoServicioTrabajador.setOpaque(true);
            break;
            case MENSAJES.PROCESO_VARIABLE:
                    lblEstadoServicioVariables.setText(textoEstado);
                    lblEstadoServicioVariables.setBackground(colorFondo);
                    lblEstadoServicioVariables.setOpaque(true);
            break;
            case MENSAJES.PROCESO_PROVEEDOR:
                    lblEstadoServicioProveedor.setText(textoEstado);
                    lblEstadoServicioProveedor.setBackground(colorFondo);
                    lblEstadoServicioProveedor.setOpaque(true);
            break;
            case MENSAJES.PROCESO_TODOS:
                    lblEstadoServicioVariables.setText(textoEstado);
                    lblEstadoServicioVariables.setBackground(colorFondo);
                    lblEstadoServicioVariables.setOpaque(true);
                    //
                    lblEstadoServicioTrabajador.setText(textoEstado);
                    lblEstadoServicioTrabajador.setBackground(colorFondo);
                    lblEstadoServicioTrabajador.setOpaque(true);
                    //
                    lblEstadoServicioProveedor.setText(textoEstado);
                    lblEstadoServicioProveedor.setBackground(colorFondo);
                    lblEstadoServicioProveedor.setOpaque(true);
            break;
        }
    }
    
    private static void agregarTextoConColorLog(JTextPane tp, String msg, Color c)
    {    
        StyleContext sc = StyleContext.getDefaultStyleContext();
        AttributeSet aset = sc.addAttribute(SimpleAttributeSet.EMPTY, StyleConstants.Foreground, c);

        aset = sc.addAttribute(aset, StyleConstants.FontFamily, "Lucida Console");
        aset = sc.addAttribute(aset, StyleConstants.Alignment, StyleConstants.ALIGN_JUSTIFIED);
        
        int len = tp.getDocument().getLength();
        
        tp.setCaretPosition(len);
        tp.setCharacterAttributes(aset, false);
        //tp.setText(textoAnterior+"\n"+msg);//SETTEXT PERMITE AGREFAR TEXTO EN MODO EDITABLE
        tp.replaceSelection(msg);//NO PERMITE AGREGAR TEXTO EN MODO NO EDITABLE, LA VENTAJA DE ESTE ES QUE NO SE DEBE AGREGAR EL TEXTO ANTERIOR
    }


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnDetener;
    private javax.swing.JButton btnIniciar;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JSeparator jSeparator2;
    private javax.swing.JSeparator jSeparator3;
    private javax.swing.JLabel lblEstadoServicioProveedor;
    private javax.swing.JLabel lblEstadoServicioTrabajador;
    private javax.swing.JLabel lblEstadoServicioVariables;
    private javax.swing.JLabel lblHoraProximaTer;
    private javax.swing.JLabel lblMensajeEstadoRec;
    private javax.swing.JLabel lblMinutoProximaIte;
    private javax.swing.JLabel lblNombreProcesoProveedor;
    private javax.swing.JLabel lblNombreProcesoTrabajador;
    private javax.swing.JLabel lblNombreProcesoVariables;
    private javax.swing.JLabel lblSegundosTiempoIte;
    private javax.swing.JLabel lblTiempoReconexion;
    private java.awt.Menu menu1;
    private java.awt.MenuBar menuBar1;
    private java.awt.PopupMenu popUp;
    private java.awt.MenuItem primerPlano;
    private javax.swing.JTextPane txtLogCreacion;
    // End of variables declaration//GEN-END:variables

}
