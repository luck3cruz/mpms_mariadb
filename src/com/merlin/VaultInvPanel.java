/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.merlin;

import java.awt.Color;
import java.awt.Component;
import java.awt.event.ActionEvent;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.BorderFactory;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRExporterParameter;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperPrintManager;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.design.JasperDesign;
import net.sf.jasperreports.engine.export.JRXlsExporter;
import net.sf.jasperreports.engine.export.JRXlsExporterParameter;
import net.sf.jasperreports.engine.xml.JRXmlLoader;

/**
 *
 * @author Lucky
 */
public class VaultInvPanel extends javax.swing.JPanel {

    /**
     * Creates new form Expired
     */
    public VaultInvPanel() {
        initComponents();
        fileChoose.setVisible(false);
    }
    
    private String whereVIList = "";
    Config con = new Config();
    private final String driver = "jdbc:mariadb://" + this.con.getProp("IP") + ":" + this.con.getProp("port") + "/merlininventorydatabase";
    private final String finalUsername = this.con.getProp("username");
    private final String finalPassword = this.con.getProp("password");
    private String srcFilePath = this.con.getProp("rep_source");
    private String vilRepName = this.con.getProp("vil_rep");
    private DecimalHelper decHelp  = new DecimalHelper();
    private DateHelper dateHelp = new DateHelper();
    private Sangla oldSangla = new Sangla();
    
    public void updateCombo(JComboBox<String> combo) {
     try {
       combo.removeAllItems();
       Connection connect = DriverManager.getConnection(this.driver, this.finalUsername, this.finalPassword);
       Statement state = connect.createStatement();
       String query = "Select branch_name from merlininventorydatabase.branch_info";
       System.out.println(query);
       ResultSet rset = state.executeQuery(query);
       while (rset.next()) {
         combo.addItem(rset.getString(1));
       }
       state.close();
       connect.close();
       combo.setSelectedItem(this.con.getProp("branch"));
     } catch (SQLException ex) {
       this.con.saveProp("mpis_last_error", String.valueOf(ex));
       Logger.getLogger(Additionals.class.getName()).log(Level.SEVERE, (String)null, ex);
     } 
   }
   
   public void updateCombos() {
     updateCombo(this.branchVIL);
   }
 
   
   public void hideFileChooser() {
     this.fileChoose.setVisible(false);
   }
 
   
   private void updateVaultInventoryListing(String query) throws SQLException {
     DefaultTableModel defVilTab = (DefaultTableModel)this.vaultInventoryListing.getModel();
     while (defVilTab.getRowCount() > 0) {
       defVilTab.removeRow(0);
     }
     
     Connection connect = DriverManager.getConnection(this.driver, this.finalUsername, this.finalPassword);
     Statement state = connect.createStatement();
     ResultSet rset = state.executeQuery(query);
 
     
     Object[] row = new Object[6];
     while (rset.next()) {
       for (int i = 0; i < 6; i++) {
         row[i] = rset.getString(i + 1);
       }
       defVilTab.addRow(row);
     } 
     state.close();
     connect.close();
   }
 
   
   public void designTable() {
     TableCellRenderer renderer = new TableCellRenderer()
       {
         JLabel label = new JLabel();
         
 
 
         
         public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
           this.label.setOpaque(true);
           this.label.setText("" + value);
                    label.setVerticalAlignment(JLabel.TOP);
                label.setBorder(BorderFactory.createEmptyBorder(2, 10, 2, 10));
//           Color alternate = UIManager.getColor("Table.alternateRowColor");
                Color alternate = new Color(239,246,250);
           if (isSelected) {
             this.label.setBackground(Color.DARK_GRAY);
             this.label.setForeground(Color.WHITE);
           } else {
             this.label.setForeground(Color.black);
             if (row % 2 == 1) {
               this.label.setBackground(alternate);
             } else {
               this.label.setBackground(Color.WHITE);
             } 
           } 
           if (column == 5) {
             this.label.setHorizontalAlignment(4);
           } else if (column == 0 || column == 4) {
             this.label.setHorizontalAlignment(0);
           } else {
             this.label.setHorizontalAlignment(2);
           } 
           return this.label;
         }
       };
vaultInventoryListing.getColumnModel().getColumn(2).setCellRenderer(new TextAreaCellRenderer());
     this.vaultInventoryListing.setDefaultRenderer(Object.class, renderer);
     ((DefaultTableCellRenderer)this.vaultInventoryListing.getTableHeader().getDefaultRenderer()).setHorizontalAlignment(0);
   }
 
   
   public void printSubastaReport(String filePath, Date date, String branch) {
     try {
       Connection conn = DriverManager.getConnection(this.driver, this.finalUsername, this.finalPassword);
       try {
         JasperDesign jasperDesign = JRXmlLoader.load(getClass().getResourceAsStream(filePath));
         JasperReport jasperReport = JasperCompileManager.compileReport(jasperDesign);
         Map<String, Object> param = new HashMap<>();
         param.put("END_DATE", date);
         param.put("BRANCH_PARAM", branch);
         JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, param, conn);
 
         
         JasperPrintManager.printReport(jasperPrint, true);
       } catch (JRException ex) {
         this.con.saveProp("mpis_last_error", String.valueOf(ex));
         JOptionPane.showMessageDialog(null, ex);
       } 
     } catch (SQLException ex) {
       this.con.saveProp("mpis_last_error", String.valueOf(ex));
       Logger.getLogger(VaultInvPanel.class.getName()).log(Level.SEVERE, (String)null, ex);
     } 
   }
   
   public boolean saveSubastaReport(String filePath, String destination, String report, Date date, String branch) {
     boolean saveSuccessful = false;
     OutputStream output = null;
     try {
       Connection conn = DriverManager.getConnection(this.driver, this.finalUsername, this.finalPassword);
       JasperDesign jasperDesign = JRXmlLoader.load(getClass().getResourceAsStream(filePath));
       JasperReport jasperReport = JasperCompileManager.compileReport(jasperDesign);
       Map<String, Object> param = new HashMap<>();
       param.put("END_DATE", date);
       param.put("BRANCH_PARAM", branch);
       JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, param, conn);
       
       output = new FileOutputStream(new File(destination));
       JasperExportManager.exportReportToPdfStream(jasperPrint, output);
     } catch (FileNotFoundException ex) {
       this.con.saveProp("mpis_last_error", String.valueOf(ex));
       Logger.getLogger(VaultInvPanel.class.getName()).log(Level.SEVERE, (String)null, ex);
     } catch (JRException ex) {
       this.con.saveProp("mpis_last_error", String.valueOf(ex));
       Logger.getLogger(VaultInvPanel.class.getName()).log(Level.SEVERE, (String)null, (Throwable)ex);
     } catch (SQLException ex) {
       this.con.saveProp("mpis_last_error", String.valueOf(ex));
       Logger.getLogger(VaultInvPanel.class.getName()).log(Level.SEVERE, (String)null, ex);
     } finally {
       try {
         output.close();
         saveSuccessful = true;
       }
       catch (IOException ex) {
         this.con.saveProp("mpis_last_error", String.valueOf(ex));
         saveSuccessful = false;
         
         Logger.getLogger(VaultInvPanel.class.getName()).log(Level.SEVERE, (String)null, ex);
       } 
     } 
     return saveSuccessful;
   }
 
 
   
   private void printSubastaDot(String filePath, Date date, String branch) {
     File tempFolder = new File("C:\\MPIS\\tempRep");
     if (!tempFolder.exists()) {
       tempFolder.mkdir();
     }
     String destination = "C:\\MPIS\\tempRep\\tempVil.xls";
     OutputStream output = null;
     try {
       Connection conn = DriverManager.getConnection(this.driver, this.finalUsername, this.finalPassword);
       JasperDesign jasperDesign = JRXmlLoader.load(getClass().getResourceAsStream(filePath));
       JasperReport jasperReport = JasperCompileManager.compileReport(jasperDesign);
       Map<String, Object> param = new HashMap<>();
       param.put("END_DATE", date);
       param.put("BRANCH_PARAM", branch);
       JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, param, conn);
       JRXlsExporter exporter = new JRXlsExporter();
       exporter.setParameter(JRExporterParameter.JASPER_PRINT, jasperPrint);
       
       output = new FileOutputStream(new File(destination));
       exporter.setParameter(JRExporterParameter.OUTPUT_STREAM, output);
       exporter.setParameter((JRExporterParameter)JRXlsExporterParameter.IS_ONE_PAGE_PER_SHEET, Boolean.FALSE);
       
       exporter.setParameter((JRExporterParameter)JRXlsExporterParameter.IS_REMOVE_EMPTY_SPACE_BETWEEN_ROWS, Boolean.TRUE);
       exporter.setParameter((JRExporterParameter)JRXlsExporterParameter.IS_WHITE_PAGE_BACKGROUND, Boolean.FALSE);
       exporter.exportReport();
       PrintExcel px = new PrintExcel();
       px.printFile(new File(destination));
       try {
         output.close();
       } catch (IOException ex) {
         this.con.saveProp("mpis_last_error", String.valueOf(ex));
         Logger.getLogger(EmpenoRescate.class.getName()).log(Level.SEVERE, (String)null, ex);
       }
     
     } catch (FileNotFoundException ex) {
       this.con.saveProp("mpis_last_error", String.valueOf(ex));
       Logger.getLogger(org.jfree.data.statistics.Statistics.class.getName()).log(Level.SEVERE, (String)null, ex);
     } catch (JRException ex) {
       this.con.saveProp("mpis_last_error", String.valueOf(ex));
       Logger.getLogger(org.jfree.data.statistics.Statistics.class.getName()).log(Level.SEVERE, (String)null, (Throwable)ex);
     } catch (SQLException ex) {
       this.con.saveProp("mpis_last_error", String.valueOf(ex));
       Logger.getLogger(org.jfree.data.statistics.Statistics.class.getName()).log(Level.SEVERE, (String)null, ex);
     } finally {
       try {
         output.close();
       }
       catch (IOException ex) {
         this.con.saveProp("mpis_last_error", String.valueOf(ex));
         JOptionPane.showMessageDialog(null, "An error occured while printing your Subasta Report", "Subasta Report", 0);
         Logger.getLogger(org.jfree.data.statistics.Statistics.class.getName()).log(Level.SEVERE, (String)null, ex);
       } 
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

        jScrollPane2 = new javax.swing.JScrollPane();
        jPanel3 = new javax.swing.JPanel();
        jPanel1 = new javax.swing.JPanel();
        jLabel3 = new javax.swing.JLabel();
        branchVIL = new javax.swing.JComboBox<>();
        generateVIListing = new javax.swing.JButton();
        saveListingBtn = new javax.swing.JButton();
        printVIListing = new javax.swing.JButton();
        jButton4 = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        vaultInventoryListing = new javax.swing.JTable();
        fileChoose = new javax.swing.JFileChooser();

        setLayout(new java.awt.BorderLayout());

        jPanel3.setPreferredSize(new java.awt.Dimension(863, 768));

        jPanel1.setBackground(new java.awt.Color(255, 255, 255));
        jPanel1.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Helvetica Neue", 0, 13), new java.awt.Color(255, 255, 255))); // NOI18N

        jLabel3.setForeground(new java.awt.Color(102, 102, 102));
        jLabel3.setText("Branch:");

        branchVIL.setFont(new java.awt.Font("Segoe UI Semibold", 0, 13)); // NOI18N
        branchVIL.setForeground(new java.awt.Color(51, 51, 51));
        branchVIL.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));

        generateVIListing.setBackground(new java.awt.Color(79, 119, 141));
        generateVIListing.setFont(new java.awt.Font("Segoe UI Semibold", 0, 13)); // NOI18N
        generateVIListing.setForeground(new java.awt.Color(255, 255, 255));
        generateVIListing.setText("Generate List");
        generateVIListing.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                generateVIListingActionPerformed(evt);
            }
        });

        saveListingBtn.setFont(new java.awt.Font("Segoe UI Semibold", 0, 13)); // NOI18N
        saveListingBtn.setForeground(new java.awt.Color(79, 119, 141));
        saveListingBtn.setText("Save Listing");
        saveListingBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                saveListingBtnActionPerformed(evt);
            }
        });

        printVIListing.setFont(new java.awt.Font("Segoe UI Semibold", 0, 13)); // NOI18N
        printVIListing.setForeground(new java.awt.Color(79, 119, 141));
        printVIListing.setText("Print Listing");
        printVIListing.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                printVIListingActionPerformed(evt);
            }
        });

        jButton4.setFont(new java.awt.Font("Segoe UI Semibold", 0, 13)); // NOI18N
        jButton4.setForeground(new java.awt.Color(79, 119, 141));
        jButton4.setText("View Pap");
        jButton4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton4ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(19, 19, 19)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel3)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(branchVIL, javax.swing.GroupLayout.PREFERRED_SIZE, 157, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(generateVIListing, javax.swing.GroupLayout.PREFERRED_SIZE, 155, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(printVIListing, javax.swing.GroupLayout.PREFERRED_SIZE, 148, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(saveListingBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 140, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jButton4, javax.swing.GroupLayout.PREFERRED_SIZE, 106, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(57, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(10, 10, 10)
                .addComponent(jLabel3)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(branchVIL, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(generateVIListing, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(printVIListing, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(saveListingBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton4, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(15, Short.MAX_VALUE))
        );

        vaultInventoryListing.setBackground(new java.awt.Color(240, 240, 240));
        vaultInventoryListing.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null}
            },
            new String [] {
                "Pap No", "Client Name", "Item Description", "Remarks", "Date Loan", "Principal"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, true, false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        vaultInventoryListing.setFillsViewportHeight(true);
        vaultInventoryListing.setGridColor(new java.awt.Color(240, 240, 240));
        jScrollPane1.setViewportView(vaultInventoryListing);
        if (vaultInventoryListing.getColumnModel().getColumnCount() > 0) {
            vaultInventoryListing.getColumnModel().getColumn(0).setResizable(false);
            vaultInventoryListing.getColumnModel().getColumn(0).setPreferredWidth(35);
            vaultInventoryListing.getColumnModel().getColumn(1).setResizable(false);
            vaultInventoryListing.getColumnModel().getColumn(1).setPreferredWidth(80);
            vaultInventoryListing.getColumnModel().getColumn(2).setResizable(false);
            vaultInventoryListing.getColumnModel().getColumn(2).setPreferredWidth(220);
            vaultInventoryListing.getColumnModel().getColumn(3).setResizable(false);
            vaultInventoryListing.getColumnModel().getColumn(3).setPreferredWidth(70);
            vaultInventoryListing.getColumnModel().getColumn(4).setPreferredWidth(50);
            vaultInventoryListing.getColumnModel().getColumn(5).setResizable(false);
            vaultInventoryListing.getColumnModel().getColumn(5).setPreferredWidth(50);
        }

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane1)
                    .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
            .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jPanel3Layout.createSequentialGroup()
                    .addGap(0, 169, Short.MAX_VALUE)
                    .addComponent(fileChoose, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGap(0, 168, Short.MAX_VALUE)))
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 670, Short.MAX_VALUE)
                .addContainerGap())
            .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jPanel3Layout.createSequentialGroup()
                    .addGap(0, 221, Short.MAX_VALUE)
                    .addComponent(fileChoose, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGap(0, 221, Short.MAX_VALUE)))
        );

        jScrollPane2.setViewportView(jPanel3);

        add(jScrollPane2, java.awt.BorderLayout.CENTER);
    }// </editor-fold>//GEN-END:initComponents

    private void generateVIListingActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_generateVIListingActionPerformed
        String query = "Select a.pap_num, a.client_name, a.item_description, a.remarks, a.transaction_date, b.principal from merlininventorydatabase.client_info a inner join merlininventorydatabase.empeno b on a.item_code_a = b.pap_num";
        /* 486 */ setWhereVIList(" WHERE a.branch = '" + this.branchVIL.getSelectedItem().toString() + "' and a.status = 'Open' and a.new_pap_num = 'null' order by classification asc, a.transaction_date desc");
/* 487 */     query = query.concat(getWhereVIList());
     try {
/* 489 */       updateVaultInventoryListing(query);
/* 490 */       designTable();
/* 491 */     } catch (SQLException ex) {
/* 492 */       this.con.saveProp("mpis_last_error", String.valueOf(ex));
/* 493 */       Logger.getLogger(VaultInvPanel.class.getName()).log(Level.SEVERE, (String)null, ex);
     } 
    }//GEN-LAST:event_generateVIListingActionPerformed

    private void printVIListingActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_printVIListingActionPerformed
        if (this.vaultInventoryListing.getRowCount() > 0)
///* 499 */     { if ((new Config()).getProp("branch").equalsIgnoreCase("Tangos")) {
/* 500 */         printSubastaReport("/Reports/vault_inventory_listing.jrxml", Calendar.getInstance().getTime(), this.branchVIL.getSelectedItem().toString());
//       } else {
///* 502 */         printSubastaDot("/Reports/vault_inventory_dot.jrxml", Calendar.getInstance().getTime(), this.branchVIL.getSelectedItem().toString());
//       }  }
/* 504 */     else { JOptionPane.showMessageDialog(null, "No file to print. Please generate a report first.", "Vault Inventory", 0); 
        }
    }//GEN-LAST:event_printVIListingActionPerformed

    private void saveListingBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_saveListingBtnActionPerformed
        if (this.vaultInventoryListing.getRowCount() > 0) {
/* 510 */       String suggestedFileName = "VIL-" + this.dateHelp.getSavingDate() + "-" + this.branchVIL.getSelectedItem().toString().toUpperCase();
/* 511 */       this.fileChoose.setSelectedFile(new File(suggestedFileName));
/* 512 */       this.fileChoose.setVisible(true);
/* 513 */       int showSaveDialog = this.fileChoose.showSaveDialog((Component)null);
/* 514 */       if (showSaveDialog == 0) {
/* 515 */         String fileName = "";
/* 516 */         if (this.fileChoose.getSelectedFile().toString().substring(this.fileChoose.getSelectedFile().toString().length() - 4).equals(".pdf")) {
/* 517 */           fileName = this.fileChoose.getSelectedFile().getAbsolutePath();
         } else {
/* 519 */           fileName = this.fileChoose.getSelectedFile().getAbsolutePath().concat(".pdf");
         } 
         
/* 522 */         this.fileChoose.setSelectedFile(new File(fileName));
/* 523 */         if (this.fileChoose.getSelectedFile().exists()) {
/* 524 */           int replaceFile = JOptionPane.showConfirmDialog(null, "Item Already Exists. Do you want to replace this file?", "Replace File", 0);
/* 525 */           if (replaceFile == 1) {
/* 526 */             saveListingBtnActionPerformed((ActionEvent)null);
             return;
           } 
         } 
/* 530 */         if (saveSubastaReport("/Reports/vault_inventory_listing.jrxml", fileName, "Vault Inventory Listing Report", Calendar.getInstance().getTime(), this.branchVIL.getSelectedItem().toString()) == true) {
/* 531 */           JOptionPane.showMessageDialog(null, "Vault Inventory Listing Report successfully Saved!", "Vault Inventory Listing Report", 1);
         } else {
/* 533 */           JOptionPane.showMessageDialog(null, "An error occured while saving your Vault Inventory Listing Report", "Vault Inventory Listing Report", 0);
         } 
/* 535 */         this.fileChoose.setVisible(false);
       } 
     } else {
/* 538 */       JOptionPane.showMessageDialog(null, "No file to save. Please generate a report first.", "Vault Inventory", 0);
     }
    }//GEN-LAST:event_saveListingBtnActionPerformed

    private void jButton4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton4ActionPerformed
        this.oldSangla.setPap_num(this.vaultInventoryListing.getValueAt(this.vaultInventoryListing.getSelectedRow(), 0).toString());
/* 544 */     this.oldSangla.setBranch(this.branchVIL.getSelectedItem().toString());
/* 545 */     this.oldSangla.setItemCodes();
/* 546 */     this.oldSangla.retrieveSangla(this.oldSangla.getItem_code_a());
/* 547 */     ViewMode view = new ViewMode(null, true, this.oldSangla);
/* 548 */     view.setVisible(true);
/* 549 */     view.setAlwaysOnTop(true);
    }//GEN-LAST:event_jButton4ActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JComboBox<String> branchVIL;
    private javax.swing.JFileChooser fileChoose;
    private javax.swing.JButton generateVIListing;
    private javax.swing.JButton jButton4;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JButton printVIListing;
    private javax.swing.JButton saveListingBtn;
    private javax.swing.JTable vaultInventoryListing;
    // End of variables declaration//GEN-END:variables

    /**
     * @return the whereVIList
     */
    public String getWhereVIList() {
        return whereVIList;
    }

    /**
     * @param whereVIList the whereVIList to set
     */
    public void setWhereVIList(String whereVIList) {
        this.whereVIList = whereVIList;
    }
}
