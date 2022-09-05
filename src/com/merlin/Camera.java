/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package com.merlin;


import com.formdev.flatlaf.FlatLightLaf;
import com.github.sarxos.webcam.Webcam;
import java.awt.Dimension;
import java.awt.Image;
import java.io.File;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JOptionPane;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

/**
 *
 * @author Lucky
 */
public class Camera extends javax.swing.JFrame {

    static {
//        Webcam.setDriver(new V414jDriver());
    }
    /**
     * @return the isRunning
     */
    public Boolean getIsRunning() {
        return isRunning;
    }

    /**
     * @param isRunning the isRunning to set
     */
    public void setIsRunning(Boolean isRunning) {
        this.isRunning = isRunning;
    }

    /**
     * @return the photoCaptured
     */
    public boolean isPhotoCaptured() {
        return photoCaptured;
    }

    /**
     * @param photoCaptured the photoCaptured to set
     */
    public void setPhotoCaptured(boolean photoCaptured) {
        this.photoCaptured = photoCaptured;
    }

    /**
     * @return the idCaptured
     */
    public boolean isIdCaptured() {
        return idCaptured;
    }

    /**
     * @param idCaptured the idCaptured to set
     */
    public void setIdCaptured(boolean idCaptured) {
        this.idCaptured = idCaptured;
    }

    /**
     * @return the photomode
     */
    public boolean isPhotomode() {
        return photomode;
    }

    /**
     * @param photomode the photomode to set
     */
    public void setPhotomode(boolean photomode) {
        this.photomode = photomode;
    }

    /**
     * @return the photo_filename
     */
    public String getPhoto_filename() {
        return photo_filename;
    }

    /**
     * @param photo_filename the photo_filename to set
     */
    public void setPhoto_filename(String photo_filename) {
        this.photo_filename = photo_filename;
    }

    /**
     * @return the id_filename
     */
    public String getId_filename() {
        return id_filename;
    }

    /**
     * @param id_filename the id_filename to set
     */
    public void setId_filename(String id_filename) {
        this.id_filename = id_filename;
    }

    /**
     * @return the file_dest
     */
    public String getFile_dest() {
        return file_dest;
    }

    /**
     * @param file_dest the file_dest to set
     */
    public void setFile_dest(String file_dest) {
        this.file_dest = file_dest;
    }

    /**
     * Creates new form Camera
     */
    public Camera() {
        initComponents();
        setLocationRelativeTo(null);
        
        webcam = Webcam.getDefault();
        System.out.println(Webcam.getWebcams());
        webcam.setViewSize(new Dimension(640, 480));
        webcam.open();
//        startWebcam();
        if (con.getProp("default_photo_folder") == null) {
            con.saveProp("default_photo_folder", "C:\\KYC_photos\\");
            if (!new File(con.getProp("default_photo_folder")).exists()) {
                new File(con.getProp("default_photo_folder")).mkdir();
            }
        } else {
            setFile_dest(con.getProp("default_photo_folder"));
        }
//        if (con.getProp("Default_ID_Folder") == null) {
//            con.saveProp("Default_ID_Folder", "C:\\\\KYC_ids\\\\");
//            if (!new File(con.getProp("Default_ID_Folder")).exists()) {
//                new File(con.getProp("Default_ID_Folder")).mkdir();
//            }
//        } else {
//            setFile_dest(con.getProp("Default_Photo_Folder"));
//        }
    }
    private final Config con = new Config();
    private String file_dest = "";
    private String photo_filename = "";
    private String id_filename = "";
    private boolean photomode = true;
    private boolean photoCaptured = false;
    private boolean idCaptured = false;
    private VideoFeedTaker vidta = new VideoFeedTaker();
    
    
    public void startWebcam() {
        if (!getIsRunning()) {
            setIsRunning((Boolean) true);
            vidta.start();
        } else {
            setIsRunning((Boolean) false);
        }
    }
    
    public void stopWebcam() {
        vidta.stopThread();
    }
    
    
    Webcam webcam;
    private Boolean isRunning = false;
    
    

    class VideoFeedTaker extends Thread {
        @Override
        public void run() {
            while (getIsRunning()) {
                try {
                    Image image = webcam.getImage();
                    jLabel1.setIcon(new ImageIcon(image));
                    Thread.sleep(50);
                    
                } catch (InterruptedException ex) {
                    if (!getIsRunning()) {
                        break;
                    }
                    Logger.getLogger(Camera.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
        
        public void stopThread() {
            isRunning = false;
            interrupt();
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

        jLabel1 = new javax.swing.JLabel();
        jButton1 = new javax.swing.JButton();
        jButton2 = new javax.swing.JButton();
        jButton3 = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DO_NOTHING_ON_CLOSE);
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowOpened(java.awt.event.WindowEvent evt) {
                formWindowOpened(evt);
            }
        });

        jLabel1.setBorder(javax.swing.BorderFactory.createMatteBorder(1, 1, 1, 1, new java.awt.Color(102, 102, 102)));
        jLabel1.setMinimumSize(new java.awt.Dimension(640, 480));

        jButton1.setFont(new java.awt.Font("Segoe UI Semibold", 0, 13)); // NOI18N
        jButton1.setForeground(new java.awt.Color(79, 119, 141));
        jButton1.setText("Default Destination Folder");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        jButton2.setBackground(new java.awt.Color(79, 119, 141));
        jButton2.setFont(new java.awt.Font("Segoe UI Semibold", 0, 13)); // NOI18N
        jButton2.setForeground(new java.awt.Color(255, 255, 255));
        jButton2.setText("Capture");
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });

        jButton3.setFont(new java.awt.Font("Segoe UI Semibold", 0, 13)); // NOI18N
        jButton3.setForeground(new java.awt.Color(79, 119, 141));
        jButton3.setText("Close");
        jButton3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton3ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jLabel1, javax.swing.GroupLayout.DEFAULT_SIZE, 640, Short.MAX_VALUE)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 222, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(27, 27, 27)
                .addComponent(jButton2, javax.swing.GroupLayout.PREFERRED_SIZE, 136, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jButton3, javax.swing.GroupLayout.PREFERRED_SIZE, 139, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 480, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jButton3, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(0, 7, Short.MAX_VALUE))
        );

        layout.linkSize(javax.swing.SwingConstants.VERTICAL, new java.awt.Component[] {jButton1, jButton2});

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        try {
            String dest = "";
            File f;
            if (isPhotomode()) {
                dest = getPhoto_filename();
                f = new File(getFile_dest().concat(getPhoto_filename()));
                setPhotoCaptured(true);
            } else {
                dest = getId_filename();
                f = new File(getFile_dest().concat(getId_filename()));
                setIdCaptured(true);
            }
            if(!f.exists()) {
                startWebcam();
                ImageIO.write(webcam.getImage(), "JPG", new File(getFile_dest().concat(dest)));
                webcam.close();
//                setVisible(false);
            } else { 
                int replaceFile = JOptionPane.showConfirmDialog(null, "Photo Already Exists. Do you want to replace this file?", "Replace Photo", JOptionPane.YES_NO_OPTION);
                if (replaceFile == JOptionPane.YES_OPTION) {
                    startWebcam();
                    ImageIO.write(webcam.getImage(), "JPG", new File(getFile_dest().concat(dest)));
                    webcam.close();
//                    setVisible(false);
                } else {
                    webcam.close();
//                    setVisible(false);
                }
            }
            dispose();
        } catch (IOException ex) {
            Logger.getLogger(Camera.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_jButton2ActionPerformed

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        
    }//GEN-LAST:event_jButton1ActionPerformed

    private void formWindowOpened(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowOpened
        startWebcam();
    }//GEN-LAST:event_formWindowOpened

    private void jButton3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton3ActionPerformed
        dispose();
    }//GEN-LAST:event_jButton3ActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        try {
            /* Set the Nimbus look and feel */
            //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
            /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
            * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html
            */
            UIManager.setLookAndFeel(new FlatLightLaf());
            //</editor-fold>
        } catch (UnsupportedLookAndFeelException ex) {
            Logger.getLogger(Camera.class.getName()).log(Level.SEVERE, null, ex);
        }

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new Camera().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jButton3;
    private javax.swing.JLabel jLabel1;
    // End of variables declaration//GEN-END:variables
}
