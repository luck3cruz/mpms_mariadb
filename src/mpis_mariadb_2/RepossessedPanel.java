/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mpis_mariadb_2;

import java.awt.Color;
import java.awt.Component;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.UIManager;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;

/**
 *
 * @author Lucky
 */
public class RepossessedPanel extends javax.swing.JPanel {

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
    
    public void updateCombo(JComboBox<String> combo) {
/*     */     try {
/*  55 */       combo.removeAllItems();
/*  56 */       Connection connect = DriverManager.getConnection(this.driver, this.finalUsername, this.finalPassword);
/*  57 */       Statement state = connect.createStatement();
/*  58 */       String query = "Select branch_name from merlininventorydatabase.branch_info";
/*  59 */       System.out.println(query);
/*  60 */       ResultSet rset = state.executeQuery(query);
/*  61 */       while (rset.next()) {
/*  62 */         combo.addItem(rset.getString(1));
/*     */       }
/*  64 */       state.close();
/*  65 */       connect.close();
/*  66 */       combo.setSelectedItem(this.con.getProp("branch"));
/*  67 */     } catch (SQLException ex) {
/*  68 */       this.con.saveProp("mpis_last_error", String.valueOf(ex));
/*  69 */       Logger.getLogger(Additionals.class.getName()).log(Level.SEVERE, (String)null, ex);
/*     */     } 
/*     */   }
/*     */   
/*     */   public void updateCombos() {
/*  74 */     updateCombo(this.branchVIL);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private void updateRepossessedListing(String query) throws SQLException {
/*  80 */     DefaultTableModel defVilTab = (DefaultTableModel)this.repossessedListing.getModel();
/*  81 */     while (defVilTab.getRowCount() > 0) {
/*  82 */       defVilTab.removeRow(0);
/*     */     }
/*     */     
/*  85 */     Connection connect = DriverManager.getConnection(this.driver, this.finalUsername, this.finalPassword);
/*  86 */     Statement state = connect.createStatement();
/*  87 */     ResultSet rset = state.executeQuery(query);
/*     */ 
/*     */     
/*  90 */     Object[] row = new Object[6];
/*  91 */     while (rset.next()) {
/*  92 */       for (int i = 0; i < 6; i++) {
/*  93 */         row[i] = rset.getString(i + 1);
/*     */       }
/*  95 */       defVilTab.addRow(row);
/*     */     } 
/*  97 */     state.close();
/*  98 */     connect.close();
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void designTable() {
/* 104 */     TableCellRenderer renderer = new TableCellRenderer()
/*     */       {
/* 106 */         JLabel label = new JLabel();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */         
/*     */         public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
/* 113 */           this.label.setOpaque(true);
/* 114 */           this.label.setText("" + value);
///* 115 */           Color alternate = UIManager.getColor("Table.alternateRowColor");
                Color alternate = new Color(239,246,250);
/* 116 */           if (isSelected) {
/* 117 */             this.label.setBackground(Color.DARK_GRAY);
/* 118 */             this.label.setForeground(Color.WHITE);
/*     */           } else {
/* 120 */             this.label.setForeground(Color.black);
/* 121 */             if (row % 2 == 1) {
/* 122 */               this.label.setBackground(alternate);
/*     */             } else {
/* 124 */               this.label.setBackground(Color.WHITE);
/*     */             } 
/*     */           } 
/* 127 */           if (column == 5) {
/* 128 */             this.label.setHorizontalAlignment(4);
/* 129 */           } else if (column == 0 || column == 4) {
/* 130 */             this.label.setHorizontalAlignment(0);
/*     */           } else {
/* 132 */             this.label.setHorizontalAlignment(2);
/*     */           } 
/* 134 */           return this.label;
/*     */         }
/*     */       };
repossessedListing.getColumnModel().getColumn(2).setCellRenderer(new TextAreaCellRenderer());
/* 137 */     this.repossessedListing.setDefaultRenderer(Object.class, renderer);
/* 138 */     ((DefaultTableCellRenderer)this.repossessedListing.getTableHeader().getDefaultRenderer()).setHorizontalAlignment(0);
/*     */   }

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

        jButton3.setFont(new java.awt.Font("Segoe UI Semibold", 0, 13)); // NOI18N
        jButton3.setForeground(new java.awt.Color(79, 119, 141));
        jButton3.setText("Print Listing");

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
                .addGap(20, 20, 20)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(branchVIL, javax.swing.GroupLayout.PREFERRED_SIZE, 147, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(generateVIListing, javax.swing.GroupLayout.PREFERRED_SIZE, 145, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(30, 30, 30)
                        .addComponent(jButton3, javax.swing.GroupLayout.PREFERRED_SIZE, 117, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jButton2, javax.swing.GroupLayout.PREFERRED_SIZE, 109, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jButton4, javax.swing.GroupLayout.PREFERRED_SIZE, 103, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jLabel3))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(20, 20, 20)
                .addComponent(jLabel3)
                .addGap(1, 1, 1)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(branchVIL, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(generateVIListing, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton3, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton2, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton4, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE))
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
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 654, Short.MAX_VALUE)
                .addContainerGap())
        );

        jScrollPane2.setViewportView(jPanel3);

        add(jScrollPane2, java.awt.BorderLayout.CENTER);
    }// </editor-fold>//GEN-END:initComponents

    private void generateVIListingActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_generateVIListingActionPerformed
        try {
/* 309 */       String query = "select a.pap_num, a.client_name, a.item_description, a.remarks, a.transaction_date, b.principal from merlininventorydatabase.client_info a inner join merlininventorydatabase.loan_info b on a.item_code_a = b.item_code_b";
/* 310 */       setWhereVIList(" WHERE a.branch = '" + this.branchVIL.getSelectedItem().toString() + "' and a.status = 'Sold' or a.status = 'Repossessed' and a.new_pap_num = 'null'");
/* 311 */       query = query.concat(getWhereVIList());
/* 312 */       updateRepossessedListing(query);
/* 313 */       designTable();
/* 314 */     } catch (SQLException ex) {
/* 315 */       this.con.saveProp("mpis_last_error", String.valueOf(ex));
/* 316 */       Logger.getLogger(RepossessedPanel.class.getName()).log(Level.SEVERE, (String)null, ex);
/*     */     } 
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


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JComboBox<String> branchVIL;
    private javax.swing.JButton generateVIListing;
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jButton3;
    private javax.swing.JButton jButton4;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JTable repossessedListing;
    // End of variables declaration//GEN-END:variables
}
