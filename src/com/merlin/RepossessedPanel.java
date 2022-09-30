/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.merlin;

import java.awt.Color;
import java.awt.Component;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Calendar;
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

/**
 *
 * @author Lucky
 */
public class RepossessedPanel extends javax.swing.JPanel {

    /**
     * @return the combosUpdated
     */
    public boolean isCombosUpdated() {
        return combosUpdated;
    }

    /**
     * @param combosUpdated the combosUpdated to set
     */
    public void setCombosUpdated(boolean combosUpdated) {
        this.combosUpdated = combosUpdated;
    }

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

    /**
     * Creates new form Expired
     */
    public RepossessedPanel() {
        initComponents();
    }
    
    Config con = new Config();
    private final String driver = "jdbc:mariadb://" + this.con.getProp("IP") + ":" + this.con.getProp("port") + "/merlininventorydatabase";
    private final String finalUsername = this.con.getProp("username");
    private final String finalPassword = this.con.getProp("password");
    private String srcFilePath = "C:\\Program Files\\MPIS\\JRXML\\";
    private String vilRepName = "vault_inventory_listing.jrxml";
    private String whereVIList;
    private DecimalHelper decHelp = new DecimalHelper();
    private DateHelper dateHelp = new DateHelper();
    private Sangla oldSangla = new Sangla();
    private boolean combosUpdated = false;
    private ReportPrinter pr = new ReportPrinter();
    private String reportFilePath = "/Reports/vault_inventory_listing.jrxml";
    
    public void updateCombo(JComboBox<String> combo) {
     try {
       combo.removeAllItems();
       Connection connect = DriverManager.getConnection(this.driver, this.finalUsername, this.finalPassword);
       Statement state = connect.createStatement();
       String query = "Select branch_name from merlininventorydatabase.branch_info";
       ResultSet rset = state.executeQuery(query);
       while (rset.next()) {
         combo.addItem(rset.getString(1));
       }
       state.close();
       connect.close();
         setCombosUpdated(true);
       combo.setSelectedItem(this.con.getProp("branch"));
     } catch (SQLException ex) {
       this.con.saveProp("mpis_last_error", String.valueOf(ex));
       Logger.getLogger(Additionals.class.getName()).log(Level.SEVERE, (String)null, ex);
     } 
   }
    
    public void updateStatusCombo() {
        try {
            statusComboBox.removeAllItems();
            Connection connect = DriverManager.getConnection(driver, finalUsername, finalPassword);
            Statement state = connect.createStatement();
            ResultSet rset = state.executeQuery("Select distinct(status) from merlininventorydatabase.client_info");
            while (rset.next()) {
                statusComboBox.addItem(rset.getString(1));
            }
            setCombosUpdated(true);
            state.close();
            connect.close();
        } catch (SQLException ex) {
            Logger.getLogger(RepossessedPanel.class.getName()).log(Level.SEVERE, null, ex);
        }
            
    }
   
   public void updateCombos() {
/*  74 */     updateCombo(this.branchVIL);
   }
 
 
   
   private void updateRepossessedListing(String query) throws SQLException {
/*  80 */     DefaultTableModel defVilTab = (DefaultTableModel)this.repossessedListing.getModel();
/*  81 */     while (defVilTab.getRowCount() > 0) {
/*  82 */       defVilTab.removeRow(0);
     }
     
/*  85 */     Connection connect = DriverManager.getConnection(this.driver, this.finalUsername, this.finalPassword);
/*  86 */     Statement state = connect.createStatement();
/*  87 */     ResultSet rset = state.executeQuery(query);
 
     
/*  90 */     Object[] row = new Object[6];
/*  91 */     while (rset.next()) {
/*  92 */       for (int i = 0; i < 6; i++) {
/*  93 */         row[i] = rset.getString(i + 1);
       }
/*  95 */       defVilTab.addRow(row);
     } 
/*  97 */     state.close();
/*  98 */     connect.close();
   }
 
 
   
   public void designTable() {
/* 104 */     TableCellRenderer renderer = new TableCellRenderer()
       {
/* 106 */         JLabel label = new JLabel();
 
 
 
 
         
         public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
/* 113 */           this.label.setOpaque(true);
/* 114 */           this.label.setText("" + value);
                label.setVerticalAlignment(JLabel.TOP);
                label.setBorder(BorderFactory.createEmptyBorder(2, 10, 2, 10));
///* 115 */           Color alternate = UIManager.getColor("Table.alternateRowColor");
                Color alternate = new Color(239,246,250);
/* 116 */           if (isSelected) {
/* 117 */             this.label.setBackground(Color.DARK_GRAY);
/* 118 */             this.label.setForeground(Color.WHITE);
           } else {
/* 120 */             this.label.setForeground(Color.black);
/* 121 */             if (row % 2 == 1) {
/* 122 */               this.label.setBackground(alternate);
             } else {
/* 124 */               this.label.setBackground(Color.WHITE);
             } 
           } 
/* 127 */           if (column == 5) {
/* 128 */             this.label.setHorizontalAlignment(4);
/* 129 */           } else if (column == 0 || column == 4) {
/* 130 */             this.label.setHorizontalAlignment(0);
           } else {
/* 132 */             this.label.setHorizontalAlignment(2);
           } 
/* 134 */           return this.label;
         }
       };
repossessedListing.getColumnModel().getColumn(2).setCellRenderer(new TextAreaCellRenderer());
/* 137 */     this.repossessedListing.setDefaultRenderer(Object.class, renderer);
/* 138 */     ((DefaultTableCellRenderer)this.repossessedListing.getTableHeader().getDefaultRenderer()).setHorizontalAlignment(0);
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
        jButton2 = new javax.swing.JButton();
        jButton3 = new javax.swing.JButton();
        jButton4 = new javax.swing.JButton();
        statusComboBox = new javax.swing.JComboBox<>();
        jLabel4 = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        repossessedListing = new javax.swing.JTable();

        setLayout(new java.awt.BorderLayout());

        jPanel1.setBackground(new java.awt.Color(255, 255, 255));
        jPanel1.setForeground(new java.awt.Color(51, 51, 51));

        jLabel3.setText("Branch:");

        branchVIL.setFont(new java.awt.Font("Segoe UI Semibold", 1, 13)); // NOI18N
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

        jButton2.setFont(new java.awt.Font("Segoe UI Semibold", 0, 13)); // NOI18N
        jButton2.setForeground(new java.awt.Color(79, 119, 141));
        jButton2.setText("Save Listing");
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });

        jButton3.setFont(new java.awt.Font("Segoe UI Semibold", 0, 13)); // NOI18N
        jButton3.setForeground(new java.awt.Color(79, 119, 141));
        jButton3.setText("Print Listing");
        jButton3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton3ActionPerformed(evt);
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

        statusComboBox.setEditable(true);
        statusComboBox.setFont(new java.awt.Font("Segoe UI Semibold", 0, 13)); // NOI18N
        statusComboBox.setForeground(new java.awt.Color(51, 51, 51));
        statusComboBox.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));

        jLabel4.setText("Status");

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(statusComboBox, 0, 126, Short.MAX_VALUE)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jLabel4)
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel3)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(branchVIL, javax.swing.GroupLayout.PREFERRED_SIZE, 147, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(generateVIListing, javax.swing.GroupLayout.PREFERRED_SIZE, 145, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(jButton3, javax.swing.GroupLayout.PREFERRED_SIZE, 117, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jButton2, javax.swing.GroupLayout.PREFERRED_SIZE, 109, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jButton4, javax.swing.GroupLayout.PREFERRED_SIZE, 103, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(33, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(15, 15, 15)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel3)
                    .addComponent(jLabel4))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(generateVIListing, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton3, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton2, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton4, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(statusComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(branchVIL, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(20, Short.MAX_VALUE))
        );

        repossessedListing.setBackground(new java.awt.Color(240, 240, 240));
        repossessedListing.setModel(new javax.swing.table.DefaultTableModel(
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
                false, false, false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        repossessedListing.setFillsViewportHeight(true);
        repossessedListing.setGridColor(new java.awt.Color(239, 246, 250));
        repossessedListing.setShowVerticalLines(true);
        jScrollPane1.setViewportView(repossessedListing);
        if (repossessedListing.getColumnModel().getColumnCount() > 0) {
            repossessedListing.getColumnModel().getColumn(0).setResizable(false);
            repossessedListing.getColumnModel().getColumn(0).setPreferredWidth(30);
            repossessedListing.getColumnModel().getColumn(1).setResizable(false);
            repossessedListing.getColumnModel().getColumn(1).setPreferredWidth(75);
            repossessedListing.getColumnModel().getColumn(2).setResizable(false);
            repossessedListing.getColumnModel().getColumn(2).setPreferredWidth(225);
            repossessedListing.getColumnModel().getColumn(3).setResizable(false);
            repossessedListing.getColumnModel().getColumn(3).setPreferredWidth(65);
            repossessedListing.getColumnModel().getColumn(4).setResizable(false);
            repossessedListing.getColumnModel().getColumn(4).setPreferredWidth(45);
            repossessedListing.getColumnModel().getColumn(5).setResizable(false);
            repossessedListing.getColumnModel().getColumn(5).setPreferredWidth(45);
        }

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 828, Short.MAX_VALUE)
                    .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 660, Short.MAX_VALUE))
        );

        jScrollPane2.setViewportView(jPanel3);

        add(jScrollPane2, java.awt.BorderLayout.CENTER);
    }// </editor-fold>//GEN-END:initComponents

    private void generateVIListingActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_generateVIListingActionPerformed
        try {
/* 309 */       String query = "select a.pap_num, a.client_name, a.item_description, a.remarks, a.transaction_date, b.principal from merlininventorydatabase.client_info a inner join merlininventorydatabase.empeno b on a.item_code_a = b.pap_num";
/* 310 */       setWhereVIList(" WHERE a.branch = '" + this.branchVIL.getSelectedItem().toString() + "' and a.status = '" + statusComboBox.getSelectedItem().toString() + "' order by transaction_date desc");

/* 311 */       query = query.concat(getWhereVIList());
            System.out.println(query);
/* 312 */       updateRepossessedListing(query);
/* 313 */       designTable();
/* 314 */     } catch (SQLException ex) {
/* 315 */       this.con.saveProp("mpis_last_error", String.valueOf(ex));
/* 316 */       Logger.getLogger(RepossessedPanel.class.getName()).log(Level.SEVERE, (String)null, ex);
     } 
    }//GEN-LAST:event_generateVIListingActionPerformed

    private void jButton4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton4ActionPerformed
        Sangla oldSangla = new Sangla();
/* 575 */     oldSangla.setPap_num(this.repossessedListing.getValueAt(this.repossessedListing.getSelectedRow(), 0).toString());
/* 576 */     oldSangla.setBranch(this.branchVIL.getSelectedItem().toString());
/* 577 */     oldSangla.setItemCodes();
/* 578 */     oldSangla.retrieveSangla(oldSangla.getItem_code_a());
/* 579 */     ViewMode view = new ViewMode(null, true, oldSangla);
/* 580 */     view.setVisible(true);
/* 581 */     view.setAlwaysOnTop(true);
    }//GEN-LAST:event_jButton4ActionPerformed

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jButton2ActionPerformed

    private void jButton3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton3ActionPerformed
        if (this.repossessedListing.getRowCount() > 0) {
            pr.setReport_name("Vault Inventory Listing Report");
            Map<String, Object> param = new HashMap<>();
            param.put("END_DATE", Calendar.getInstance().getTime());
            param.put("BRANCH_PARAM", this.branchVIL.getSelectedItem().toString());
            pr.printReport(reportFilePath, param);
//            printSubastaReport("/Reports/vault_inventory_listing.jrxml", Calendar.getInstance().getTime(), this.branchVIL.getSelectedItem().toString());
        } else { 
            JOptionPane.showMessageDialog(null, "No file to print. Please generate a report first.", "Vault Inventory", 0); 
        }
    }//GEN-LAST:event_jButton3ActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JComboBox<String> branchVIL;
    private javax.swing.JButton generateVIListing;
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jButton3;
    private javax.swing.JButton jButton4;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JTable repossessedListing;
    private javax.swing.JComboBox<String> statusComboBox;
    // End of variables declaration//GEN-END:variables
}
