/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.merlin;

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JComboBox;
import javax.swing.JOptionPane;

/**
 *
 * @author Lucky
 */
public class PettyCash extends javax.swing.JDialog {

    /**
     * @return the pettyCashAdded
     */
    public boolean isPettyCashAdded() {
        return pettyCashAdded;
    }

    /**
     * @param pettyCashAdded the pettyCashAdded to set
     */
    public void setPettyCashAdded(boolean pettyCashAdded) {
        this.pettyCashAdded = pettyCashAdded;
    }

    /**
     * @return the database
     */
    public String getDatabase() {
        return database;
    }

    /**
     * @param database the database to set
     */
    public void setDatabase(String database) {
        this.database = database;
    }

    /**
     * Creates new form TransferConfirm
     */
    public PettyCash(java.awt.Frame parent, boolean modal) {
        super(parent, modal);
        initComponents();
    }
    
    Config con = new Config();
/*  61 */   private final String driver = "jdbc:mariadb://" + this.con.getProp("IP") + ":" + this.con.getProp("port") + "/cashtransactions";
/*  62 */   private final String finalUsername = this.con.getProp("username");
/*  63 */   private final String finalPassword = this.con.getProp("password");
/*  64 */   private DecimalHelper decHelp = new DecimalHelper();
/*  65 */   private CashTransactions pettyCash = new CashTransactions();
/*  66 */   private TableHelper tableHelp = new TableHelper();
/*     */   private boolean pettyCashAdded = false;
/*  68 */   private ReportPrinter pr = new ReportPrinter();
/*  69 */   private String database = "";
/*     */   
/*  71 */   NumberFormat vformat = new DecimalFormat("#0000");
/*  72 */   public SimpleDateFormat formatter = new SimpleDateFormat("MMyy");

public void updateAddCombo(JComboBox<String> combo) {
/*     */     try {
/*  76 */       combo.removeAllItems();
/*  77 */       Connection connect = DriverManager.getConnection(this.driver, this.finalUsername, this.finalPassword);
/*  78 */       Statement state = connect.createStatement();
/*  79 */       String query = "Select petty from merlininventorydatabase.combo_cat where petty is not null";
/*  80 */       System.out.println(query);
/*  81 */       ResultSet rset = state.executeQuery(query);
/*  82 */       while (rset.next()) {
/*  83 */         combo.addItem(rset.getString(1));
/*     */       }
/*  85 */       state.close();
/*  86 */       connect.close();
/*  87 */     } catch (SQLException ex) {
/*  88 */       this.con.saveProp("mpis_last_error", String.valueOf(ex));
/*  89 */       Logger.getLogger(Additionals.class.getName()).log(Level.SEVERE, (String)null, ex);
/*     */     } 
/*     */   }

public void updateCombo(JComboBox<String> combo) {
/*     */     try {
/*  95 */       combo.removeAllItems();
/*  96 */       Connection connect = DriverManager.getConnection(this.driver, this.finalUsername, this.finalPassword);
/*  97 */       Statement state = connect.createStatement();
/*  98 */       String query = "Select branch_name from merlininventorydatabase.branch_info";
/*  99 */       System.out.println(query);
/* 100 */       ResultSet rset = state.executeQuery(query);
/* 101 */       while (rset.next()) {
/* 102 */         combo.addItem(rset.getString(1));
/*     */       }
/* 104 */       state.close();
/* 105 */       connect.close();
/* 106 */       combo.setSelectedItem(this.con.getProp("branch"));
/* 107 */     } catch (SQLException ex) {
/* 108 */       this.con.saveProp("mpis_last_error", String.valueOf(ex));
/* 109 */       Logger.getLogger(Additionals.class.getName()).log(Level.SEVERE, (String)null, ex);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public void updateCombos() {
/* 115 */     updateCombo(this.cashTabBranch1);
/* 116 */     updateAddCombo(this.pettyCashType);
/* 117 */     this.prepBy.setText(this.con.getProp("last_username"));
/*     */   }
/*     */ 
/*     */   
/*     */   public String getNextNo() throws SQLException {
/* 122 */     Connection connect = DriverManager.getConnection(this.driver, this.finalUsername, this.finalPassword);
/* 123 */     Statement state = connect.createStatement();
/* 124 */     String query = "Select count(*) from " + getDatabase() + ".petty_cash where month(pty_date) = " + (Calendar.getInstance().get(2) + 1) + " and year(pty_date) = " + Calendar.getInstance().get(1);
/* 125 */     System.out.println(query);
/* 126 */     ResultSet rset = state.executeQuery(query);
/* 127 */     int key = 0;
/* 128 */     while (rset.next()) {
/* 129 */       key = rset.getInt(1);
/*     */     }
/* 131 */     key++;
/* 132 */     state.close();
/* 133 */     connect.close();
/* 134 */     String vNum = "";
/* 135 */     Sangla forBC = new Sangla();
/* 136 */     if (this.cashTabBranch1.getItemCount() > 0) {
/* 137 */       String fromBC = forBC.getBranchCode(this.cashTabBranch1.getSelectedItem().toString());
/* 138 */       vNum = this.formatter.format(Calendar.getInstance().getTime()).toString().concat(fromBC).concat("P").concat(this.vformat.format(key));
/*     */     } 
/*     */     
/* 141 */     return vNum;
/*     */   }
/*     */   
/*     */   public void displayNewVoucherNo() {
/*     */     try {
/* 146 */       this.voucherCode.setText(getNextNo().toString());
/* 147 */     } catch (SQLException ex) {
/* 148 */       this.con.saveProp("mpis_last_error", String.valueOf(ex));
/* 149 */       Logger.getLogger(PettyCash.class.getName()).log(Level.SEVERE, (String)null, ex);
/*     */     } 
/*     */   }


    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        jLabel10 = new javax.swing.JLabel();
        jLabel11 = new javax.swing.JLabel();
        jLabel12 = new javax.swing.JLabel();
        jLabel13 = new javax.swing.JLabel();
        jLabel14 = new javax.swing.JLabel();
        jLabel15 = new javax.swing.JLabel();
        jLabel18 = new javax.swing.JLabel();
        voucherCode = new javax.swing.JTextField();
        amount = new javax.swing.JTextField();
        prepBy = new javax.swing.JTextField();
        rcvdBy = new javax.swing.JTextField();
        cashTabBranch1 = new javax.swing.JComboBox<>();
        pettyCashType = new javax.swing.JComboBox<>();
        ok = new javax.swing.JButton();
        jButton6 = new javax.swing.JButton();
        jSeparator2 = new javax.swing.JSeparator();
        jLabel3 = new javax.swing.JLabel();
        jScrollPane2 = new javax.swing.JScrollPane();
        remarks = new javax.swing.JTextArea();
        jButton7 = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);

        jLabel10.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jLabel10.setText("Create a Petty Cash Transaction:");

        jLabel11.setText("Voucher No.:");

        jLabel12.setText("Amount:");

        jLabel13.setText("Branch:");

        jLabel14.setText("Type:");

        jLabel15.setText("Prepared By:");

        jLabel18.setText("Received By:");

        voucherCode.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        voucherCode.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                voucherCodeFocusLost(evt);
            }
        });
        voucherCode.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                voucherCodeKeyPressed(evt);
            }
            public void keyTyped(java.awt.event.KeyEvent evt) {
                voucherCodeKeyTyped(evt);
            }
        });

        amount.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        amount.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                amountFocusLost(evt);
            }
        });
        amount.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                amountKeyPressed(evt);
            }
            public void keyTyped(java.awt.event.KeyEvent evt) {
                amountKeyTyped(evt);
            }
        });

        prepBy.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N

        rcvdBy.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N

        cashTabBranch1.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        cashTabBranch1.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));

        pettyCashType.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        pettyCashType.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));

        ok.setText("OK");
        ok.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                okActionPerformed(evt);
            }
        });

        jButton6.setText("Cancel");
        jButton6.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton6ActionPerformed(evt);
            }
        });

        jLabel3.setText("Remarks:");

        remarks.setColumns(20);
        remarks.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        remarks.setLineWrap(true);
        remarks.setRows(3);
        remarks.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                remarksFocusLost(evt);
            }
        });
        remarks.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                remarksKeyPressed(evt);
            }
        });
        jScrollPane2.setViewportView(remarks);

        jButton7.setText("Reprint Petty Cash Report");
        jButton7.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton7ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel1Layout.createSequentialGroup()
                        .addGap(22, 22, 22)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel15, javax.swing.GroupLayout.Alignment.TRAILING)
                                    .addComponent(jLabel12, javax.swing.GroupLayout.Alignment.TRAILING)
                                    .addComponent(jLabel11, javax.swing.GroupLayout.Alignment.TRAILING))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(prepBy)
                                    .addComponent(amount)
                                    .addComponent(voucherCode))
                                .addGap(18, 18, 18)
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(jPanel1Layout.createSequentialGroup()
                                        .addComponent(jLabel18)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                        .addComponent(rcvdBy))
                                    .addGroup(jPanel1Layout.createSequentialGroup()
                                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                            .addComponent(jLabel13)
                                            .addComponent(jLabel14))
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                            .addComponent(pettyCashType, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                            .addComponent(cashTabBranch1, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))))
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addComponent(jButton7, javax.swing.GroupLayout.PREFERRED_SIZE, 170, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 72, Short.MAX_VALUE)
                                .addComponent(ok)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(jButton6))))
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel1Layout.createSequentialGroup()
                                .addGap(25, 25, 25)
                                .addComponent(jLabel10))
                            .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel1Layout.createSequentialGroup()
                                .addContainerGap()
                                .addComponent(jSeparator2, javax.swing.GroupLayout.PREFERRED_SIZE, 234, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel1Layout.createSequentialGroup()
                        .addGap(40, 40, 40)
                        .addComponent(jLabel3)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jScrollPane2)))
                .addContainerGap())
        );

        jPanel1Layout.linkSize(javax.swing.SwingConstants.HORIZONTAL, new java.awt.Component[] {jButton6, ok});

        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel10)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jSeparator2, javax.swing.GroupLayout.PREFERRED_SIZE, 7, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(5, 5, 5)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel11)
                    .addComponent(voucherCode, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel13)
                    .addComponent(cashTabBranch1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel12)
                    .addComponent(amount, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel14)
                    .addComponent(pettyCashType, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel15)
                    .addComponent(prepBy, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel18)
                    .addComponent(rcvdBy, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel3)
                    .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 57, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(ok)
                    .addComponent(jButton6)
                    .addComponent(jButton7))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void okActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_okActionPerformed
        if (Double.parseDouble(this.amount.getText().replace(",", "")) > 0.0D && this.prepBy.getText().length() > 0 && this.rcvdBy.getText().length() > 0) {
/* 446 */       PettyConfirm ptcc = new PettyConfirm(null, true);
/* 447 */       ptcc.setBranch(this.cashTabBranch1.getSelectedItem().toString());
/* 448 */       ptcc.setPtype(this.pettyCashType.getSelectedItem().toString());
/* 449 */       ptcc.setPrepby(this.prepBy.getText());
/* 450 */       ptcc.setRcvdby(this.rcvdBy.getText());
/* 451 */       ptcc.setPamount(this.amount.getText());
/* 452 */       ptcc.setVoucherNo(this.voucherCode.getText());
/* 453 */       ptcc.setPremarks(this.remarks.getText());
/* 454 */       ptcc.displayData();
/* 455 */       ptcc.setLocationRelativeTo((Component)null);
/* 456 */       ptcc.setVisible(true);
/* 457 */       if (ptcc.isPrintOn()) {
/* 458 */         this.pettyCash.setDatabase(getDatabase());
/* 459 */         this.pettyCash.setPrep_by(this.prepBy.getText());
/* 460 */         this.pettyCash.setRcvd_by(this.rcvdBy.getText());
/* 461 */         this.pettyCash.setPetty_cash_voucher(this.voucherCode.getText());
/* 462 */         this.pettyCash.setTransaction_amount(Double.parseDouble(this.amount.getText().replace(",", "")));
/* 463 */         this.pettyCash.setTransaction_remarks(this.remarks.getText());
/* 464 */         this.pettyCash.setPetty_cast_type(this.pettyCashType.getSelectedItem().toString());
/* 465 */         this.pettyCash.setBranch(this.cashTabBranch1.getSelectedItem().toString());
/* 466 */         int tableRowCount = 0;
/*     */         try {
/* 468 */           Connection connect = DriverManager.getConnection(this.driver, this.finalUsername, this.finalPassword);
/* 469 */           Statement query0 = connect.createStatement();
/* 470 */           ResultSet rSet = query0.executeQuery("Select COUNT(*) from " + getDatabase() + ".petty_cash");
/* 471 */           while (rSet.next()) {
/* 472 */             tableRowCount = rSet.getInt(1);
/*     */           }
/* 474 */           System.out.println(tableRowCount);
/* 475 */           tableRowCount++;
/* 476 */         } catch (SQLException ex) {
/* 477 */           this.con.saveProp("mpis_last_error", String.valueOf(ex));
/* 478 */           System.out.println("error in count search");
/*     */         } 
/* 480 */         this.pettyCash.setCash_transaction_type("PTY");
/* 481 */         this.pettyCash.setTransaction_no(tableRowCount);
/* 482 */         this.pettyCash.generateTransaction_id();
/* 483 */         this.pettyCash.generateTransaction_remarks();
/* 484 */         System.out.println(this.pettyCash.getTransaction_id());
/* 485 */         System.out.println(this.pettyCash.getTransaction_remarks());
/* 486 */         this.pettyCash.addPettyCashTransactionToDB();
/* 487 */         if (!this.pettyCash.isPetty_cash_inserted()) {
/* 488 */           JOptionPane.showMessageDialog(this.jPanel1, "There has been an error inserting this transaction to the database . Please retry. \n If problem persists, contact the database administrator.", "Error!", 0);
/*     */         } else {
/* 490 */           this.pettyCash.setDatabase("cashtransactions");
/* 491 */           this.pettyCash.addToDailyTransaction();
/* 492 */           setPettyCashAdded(true);
/* 493 */           JOptionPane.showMessageDialog(this.jPanel1, "Petty cash transaction successful.", "Petty Cash Transaction!", 1);
/* 494 */           File tempFolder = new File("C:\\MPIS\\tempRep\\pettycash");
/* 495 */           if (!tempFolder.exists()) {
/* 496 */             tempFolder.mkdir();
/*     */           }
/* 498 */           Map<Object, Object> parameters = new HashMap<>();
/* 499 */           parameters.put("VOUCHER", this.voucherCode.getText());
/* 500 */           parameters.put("DATE", (new DateHelper()).getCurrentDate());
/* 501 */           this.pr.setReport_name("Petty Cash Voucher Form");
/* 503 */             this.pr.setDestination(this.con.getProp("pettyfolder").concat(this.voucherCode.getText() + ".pdf"));
/* 504 */             this.pr.printReport("/Reports/ptc_voucher.jrxml", parameters);
/*     */ 
/*     */           
/* 511 */           dispose();
/*     */         } 
/*     */       } 
/*     */     } else {
/* 515 */       JOptionPane.showMessageDialog(null, "Please complete all important informations.");
/*     */     }
    }//GEN-LAST:event_okActionPerformed

    private void voucherCodeFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_voucherCodeFocusLost
        
    }//GEN-LAST:event_voucherCodeFocusLost

    private void voucherCodeKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_voucherCodeKeyTyped
        if (!Character.isDigit(evt.getKeyChar()) || this.voucherCode.getText().length() > 10)
/* 529 */       evt.consume();
    }//GEN-LAST:event_voucherCodeKeyTyped

    private void amountFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_amountFocusLost
        this.amount.setText(this.decHelp.FormatNumber(Double.parseDouble(this.amount.getText().replace(",", ""))));
    }//GEN-LAST:event_amountFocusLost

    private void voucherCodeKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_voucherCodeKeyPressed
        int key = evt.getKeyCode();
/* 534 */     if (key == 10) {
/* 535 */       this.amount.requestFocusInWindow();
/* 536 */       this.amount.selectAll();
/* 537 */     } else if (key == 27) {
/* 538 */       dispose();
/*     */     } 
    }//GEN-LAST:event_voucherCodeKeyPressed

    private void amountKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_amountKeyPressed
        int key = evt.getKeyCode();
/* 544 */     if (key == 10) {
/* 545 */       this.remarks.requestFocusInWindow();
/* 546 */       this.remarks.selectAll();
/* 547 */     } else if (key == 27) {
/* 548 */       dispose();
/*     */     } 
    }//GEN-LAST:event_amountKeyPressed

    private void amountKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_amountKeyTyped
        if (!Character.isDigit(evt.getKeyChar()) || this.amount.getText().length() > 18)
/* 554 */       evt.consume(); 
    }//GEN-LAST:event_amountKeyTyped

    private void remarksKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_remarksKeyPressed
        int key = evt.getKeyCode();
/* 559 */     if (key == 10) {
/* 560 */       okActionPerformed((ActionEvent)null);
/* 561 */     } else if (key == 27) {
/* 562 */       dispose();
/*     */     } 
    }//GEN-LAST:event_remarksKeyPressed

    private void jButton6ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton6ActionPerformed
        dispose();
    }//GEN-LAST:event_jButton6ActionPerformed

    private void remarksFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_remarksFocusLost
        this.remarks.setText(this.remarks.getText().trim());
/* 572 */     this.remarks.setText(this.remarks.getText().replace("\r", "").replace("\n", ""));
    }//GEN-LAST:event_remarksFocusLost

    private void jButton7ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton7ActionPerformed
        setVisible(false);
        ReprintPettyCash rt = new ReprintPettyCash(null, true);
        rt.setLocationRelativeTo((Component) null);
        try {
            rt.updateCombo();
        } catch (SQLException ex) {
            Logger.getLogger(PettyCash.class.getName()).log(Level.SEVERE, null, ex);
        }
        rt.setVisible(true);
    }//GEN-LAST:event_jButton7ActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(PettyCash.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(PettyCash.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(PettyCash.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(PettyCash.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>

        /* Create and display the dialog */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                PettyCash dialog = new PettyCash(new javax.swing.JFrame(), true);
                dialog.addWindowListener(new java.awt.event.WindowAdapter() {
                    @Override
                    public void windowClosing(java.awt.event.WindowEvent e) {
                        System.exit(0);
                    }
                });
                dialog.setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JTextField amount;
    private javax.swing.JComboBox<String> cashTabBranch1;
    private javax.swing.JButton jButton6;
    private javax.swing.JButton jButton7;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel15;
    private javax.swing.JLabel jLabel18;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JSeparator jSeparator2;
    private javax.swing.JButton ok;
    private javax.swing.JComboBox<String> pettyCashType;
    private javax.swing.JTextField prepBy;
    private javax.swing.JTextField rcvdBy;
    private javax.swing.JTextArea remarks;
    private javax.swing.JTextField voucherCode;
    // End of variables declaration//GEN-END:variables
}
