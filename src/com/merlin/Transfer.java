/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.merlin;

import java.io.FileOutputStream;
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
public class Transfer extends javax.swing.JDialog {

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
    public Transfer(java.awt.Frame parent, boolean modal) {
        super(parent, modal);
        initComponents();
    }
    
    private Config con = new Config();
    private final String driver = "jdbc:mariadb://" + this.con.getProp("IP") + ":" + this.con.getProp("port") 
            + "/cashtransactions";;
    private final String f_user = con.getProp("username");
    private final String f_pass = con.getProp("password");
    private CashTransactions xfer = new CashTransactions();
    private String database = "";
    private int id = 0;
    private DateHelper dateHelp = new DateHelper();
    NumberFormat vformat = new DecimalFormat("#0000");
    public SimpleDateFormat formatter = new SimpleDateFormat("MMyy");
    
    public void changeFundName() { this.fromBranch1.removeAllItems();
/*  68 */     if (getDatabase().equalsIgnoreCase("palawan")) {
/*  69 */       this.fromBranch1.addItem("Palawan EPP");
/*  70 */     } else if (getDatabase().equalsIgnoreCase("forex")) {
/*  71 */       this.fromBranch1.addItem("Money Changer");
} else {
/*  73 */       this.fromBranch1.addItem("Merlin");
        }
    }
    
    public void updateCombo(JComboBox<String> combo) {
        try {
/*  79 */       combo.removeAllItems();
/*  80 */       Connection connect = DriverManager.getConnection(this.driver, this.f_user, this.f_pass);
/*  81 */       Statement state = connect.createStatement();
/*  82 */       String query = "Select branch_name from merlininventorydatabase.branch_info";
/*  83 */       System.out.println(query);
/*  84 */       ResultSet rset = state.executeQuery(query);
/*  85 */       while (rset.next()) {
/*  86 */         combo.addItem(rset.getString(1));
         }
/*  88 */       state.close();
/*  89 */       connect.close();
/*  90 */       combo.setSelectedItem(this.con.getProp("branch"));
/*  91 */     } catch (SQLException ex) {
/*  92 */       this.con.saveProp("mpis_last_error", String.valueOf(ex));
/*  93 */       Logger.getLogger(Additionals.class.getName()).log(Level.SEVERE, (String)null, ex);
        }
    }

    public void updateCombos() {
/*  98 */     this.fromBranch.removeAllItems();
/*  99 */     this.fromBranch.addItem(this.con.getProp("branch"));
/* 100 */     updateCombo(this.toBranch);
/* 101 */     this.fromBranch1.removeAllItems();
/* 102 */     this.fromBranch1.addItem(this.con.getProp("branch"));
/* 103 */     updateCombo(this.toBranch1);
/* 104 */     this.prepBy.setText(this.con.getProp("last_username"));
    }

    public String getNextNo() throws SQLException {
/* 108 */     Connection connect = DriverManager.getConnection(this.driver, this.f_user, this.f_pass);
/* 109 */     Statement state = connect.createStatement();
/* 110 */     String query = "Select count(*) from " + getDatabase() + ".transfer where month(x_date) = " + (Calendar.getInstance().get(2) + 1) + " and year(x_date) = " + Calendar.getInstance().get(1);
/* 111 */     System.out.println(query);
/* 112 */     ResultSet rset = state.executeQuery(query);
/* 113 */     int key = 0;
/* 114 */     while (rset.next()) {
/* 115 */       key = rset.getInt(1);
       }
/* 117 */     key++;
/* 118 */     state.close();
/* 119 */     connect.close();
/* 120 */     Sangla forBC = new Sangla();
/* 121 */     String vNum = "";
/* 122 */     if (this.fromBranch.getItemCount() > 0 && this.toBranch.getItemCount() > 0) {
/* 123 */       String fromBC = forBC.getBranchCode(this.fromBranch.getSelectedItem().toString());
/* 124 */       String toBC = forBC.getBranchCode(this.toBranch.getSelectedItem().toString());
/* 125 */       vNum = this.formatter.format(Calendar.getInstance().getTime()).toString().concat(fromBC).concat("X").concat(toBC).concat(this.vformat.format(key));
       }
/* 127 */     return vNum;
    }

    public String getNextNoPal() throws SQLException {
/* 131 */     Connection connect = DriverManager.getConnection(this.driver, this.f_user, this.f_pass);
/* 132 */     Statement state = connect.createStatement();
/* 133 */     String query = "Select x_id from " + getDatabase() + ".transfer where month(x_date) = " + (Calendar.getInstance().get(2) + 1) + " and year(x_date) = " + Calendar.getInstance().get(1);
/* 134 */     System.out.println(query);
/* 135 */     ResultSet rset = state.executeQuery(query);
/* 136 */     int key = 0;
/* 137 */     String serial = "";
/* 138 */     while (rset.next()) {
/* 139 */       serial = rset.getString(1);
       }

/* 143 */     if (serial != "")
/* 144 */     { key = Integer.parseInt(serial.substring(7, serial.length())) + 1;
/* 145 */       DecimalHelper decHelp = new DecimalHelper();
/* 146 */       serial = decHelp.FormatPapeleta(key); }
/* 147 */     else { serial = "0001"; }
/* 148 */      state.close();
/* 149 */     connect.close();

/* 151 */     String vNum = this.dateHelp.getCurrentYear().concat("XFR").concat(serial);
/* 152 */     return vNum;
    }

    public void displayNewVoucherNo() {
        try {
/* 156 */       if (getDatabase().equalsIgnoreCase("cashtransactions")) {
/* 157 */         this.refNo.setText(getNextNo().toString());
/* 158 */         this.refNo1.setText(getNextNo().toString());
} else {
/* 160 */         this.refNo.setText(getNextNoPal().toString());
/* 161 */         this.refNo1.setText(getNextNoPal().toString());
         }
/* 163 */     } catch (SQLException ex) {
/* 164 */       this.con.saveProp("mpis_last_error", String.valueOf(ex));
/* 165 */       Logger.getLogger(PettyCash.class.getName()).log(Level.SEVERE, (String)null, ex);
        }
    }

    void closeQuietly(FileOutputStream out) {

/* 170 */     try { out.flush(); out.close(); } catch (Exception e) {}
    }
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jTabbedPane1 = new javax.swing.JTabbedPane();
        jPanel1 = new javax.swing.JPanel();
        jLabel10 = new javax.swing.JLabel();
        reprintBtn = new javax.swing.JButton();
        jButton5 = new javax.swing.JButton();
        jButton6 = new javax.swing.JButton();
        jPanel3 = new javax.swing.JPanel();
        jLabel14 = new javax.swing.JLabel();
        jLabel12 = new javax.swing.JLabel();
        jLabel18 = new javax.swing.JLabel();
        jComboBox1 = new javax.swing.JComboBox<>();
        rcvdBy = new javax.swing.JTextField();
        jLabel17 = new javax.swing.JLabel();
        jLabel13 = new javax.swing.JLabel();
        fromBranch = new javax.swing.JComboBox<>();
        refNo = new javax.swing.JTextField();
        prepBy = new javax.swing.JTextField();
        toBranch = new javax.swing.JComboBox<>();
        jLabel15 = new javax.swing.JLabel();
        jLabel11 = new javax.swing.JLabel();
        amount = new javax.swing.JTextField();
        jLabel16 = new javax.swing.JLabel();
        toFundName = new javax.swing.JComboBox<>();
        jPanel2 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jButton2 = new javax.swing.JButton();
        jButton3 = new javax.swing.JButton();
        jPanel4 = new javax.swing.JPanel();
        rcvdBy1 = new javax.swing.JTextField();
        prepBy1 = new javax.swing.JTextField();
        toBranch1 = new javax.swing.JComboBox<>();
        fromBranch1 = new javax.swing.JComboBox<>();
        jLabel7 = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        doc = new javax.swing.JTextArea();
        jLabel9 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        refNo1 = new javax.swing.JTextField();
        jLabel19 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);

        jLabel10.setFont(jLabel10.getFont().deriveFont(jLabel10.getFont().getStyle() | java.awt.Font.BOLD));
        jLabel10.setText("Create a Transfer Fund Transaction:");

        reprintBtn.setBackground(javax.swing.UIManager.getDefaults().getColor("Actions.Green"));
        reprintBtn.setFont(reprintBtn.getFont());
        reprintBtn.setForeground(new java.awt.Color(255, 255, 255));
        reprintBtn.setText("Reprint Transfer Slip");
        reprintBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                reprintBtnActionPerformed(evt);
            }
        });

        jButton5.setBackground(javax.swing.UIManager.getDefaults().getColor("Actions.Blue"));
        jButton5.setFont(jButton5.getFont());
        jButton5.setForeground(new java.awt.Color(255, 255, 255));
        jButton5.setText("OK");
        jButton5.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton5ActionPerformed(evt);
            }
        });

        jButton6.setBackground(new java.awt.Color(242, 242, 242));
        jButton6.setFont(jButton6.getFont());
        jButton6.setForeground(javax.swing.UIManager.getDefaults().getColor("Actions.Blue"));
        jButton6.setText("Cancel");
        jButton6.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton6ActionPerformed(evt);
            }
        });

        jLabel14.setBackground(javax.swing.UIManager.getDefaults().getColor("textInactiveText"));
        jLabel14.setFont(jLabel14.getFont());
        jLabel14.setText("From Fund Name");

        jLabel12.setBackground(javax.swing.UIManager.getDefaults().getColor("textInactiveText"));
        jLabel12.setFont(jLabel12.getFont());
        jLabel12.setText("Amount to be Transferred");

        jLabel18.setBackground(javax.swing.UIManager.getDefaults().getColor("textInactiveText"));
        jLabel18.setFont(jLabel18.getFont());
        jLabel18.setText("Received By");

        jComboBox1.setFont(jComboBox1.getFont().deriveFont(jComboBox1.getFont().getStyle() | java.awt.Font.BOLD));
        jComboBox1.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Merlin" }));

        rcvdBy.setFont(rcvdBy.getFont().deriveFont(rcvdBy.getFont().getStyle() | java.awt.Font.BOLD));

        jLabel17.setBackground(javax.swing.UIManager.getDefaults().getColor("textInactiveText"));
        jLabel17.setFont(jLabel17.getFont());
        jLabel17.setText("To Fund Name");

        jLabel13.setBackground(javax.swing.UIManager.getDefaults().getColor("textInactiveText"));
        jLabel13.setFont(jLabel13.getFont());
        jLabel13.setText("Transferring Branch");

        fromBranch.setFont(fromBranch.getFont().deriveFont(fromBranch.getFont().getStyle() | java.awt.Font.BOLD));
        fromBranch.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        fromBranch.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                fromBranchActionPerformed(evt);
            }
        });

        refNo.setFont(refNo.getFont().deriveFont(refNo.getFont().getStyle() | java.awt.Font.BOLD));

        prepBy.setFont(prepBy.getFont().deriveFont(prepBy.getFont().getStyle() | java.awt.Font.BOLD));

        toBranch.setFont(toBranch.getFont().deriveFont(toBranch.getFont().getStyle() | java.awt.Font.BOLD));
        toBranch.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        toBranch.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                toBranchActionPerformed(evt);
            }
        });

        jLabel15.setBackground(javax.swing.UIManager.getDefaults().getColor("textInactiveText"));
        jLabel15.setFont(jLabel15.getFont());
        jLabel15.setText("Prepared By");

        jLabel11.setBackground(javax.swing.UIManager.getDefaults().getColor("textInactiveText"));
        jLabel11.setFont(jLabel11.getFont());
        jLabel11.setText("Transfer Reference No.");

        amount.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        amount.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                amountFocusLost(evt);
            }
        });

        jLabel16.setBackground(javax.swing.UIManager.getDefaults().getColor("textInactiveText"));
        jLabel16.setFont(jLabel16.getFont());
        jLabel16.setText("Receiving Branch");

        toFundName.setFont(toFundName.getFont().deriveFont(toFundName.getFont().getStyle() | java.awt.Font.BOLD));
        toFundName.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel18)
                    .addComponent(jLabel16)
                    .addComponent(jLabel15)
                    .addComponent(jLabel13)
                    .addComponent(refNo, javax.swing.GroupLayout.PREFERRED_SIZE, 170, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel11, javax.swing.GroupLayout.PREFERRED_SIZE, 172, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                        .addComponent(rcvdBy, javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(toBranch, javax.swing.GroupLayout.Alignment.LEADING, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(prepBy, javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(fromBranch, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.PREFERRED_SIZE, 170, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(18, 18, 18)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jLabel12)
                    .addComponent(amount)
                    .addComponent(jLabel14)
                    .addComponent(jLabel17)
                    .addComponent(jComboBox1, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(toFundName, javax.swing.GroupLayout.PREFERRED_SIZE, 156, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel11)
                    .addComponent(jLabel12))
                .addGap(0, 0, 0)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(refNo, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(amount, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel13)
                    .addComponent(jLabel14))
                .addGap(0, 0, 0)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(fromBranch, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jComboBox1, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel15)
                .addGap(0, 0, 0)
                .addComponent(prepBy, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel16)
                    .addComponent(jLabel17))
                .addGap(0, 0, 0)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(toBranch, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(toFundName, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel18)
                .addGap(0, 0, 0)
                .addComponent(rcvdBy, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(6, 6, 6)
                        .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addContainerGap(33, Short.MAX_VALUE))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addComponent(reprintBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 170, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addComponent(jButton5, javax.swing.GroupLayout.PREFERRED_SIZE, 97, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jButton6, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addComponent(jLabel10)
                                .addGap(0, 0, Short.MAX_VALUE)))
                        .addContainerGap())))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(12, 12, 12)
                .addComponent(jLabel10)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jButton5, javax.swing.GroupLayout.DEFAULT_SIZE, 32, Short.MAX_VALUE)
                        .addComponent(jButton6, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addComponent(reprintBtn, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap(14, Short.MAX_VALUE))
        );

        jPanel1Layout.linkSize(javax.swing.SwingConstants.VERTICAL, new java.awt.Component[] {jButton5, jButton6});

        jTabbedPane1.addTab("Cash Transfer", jPanel1);

        jLabel1.setFont(jLabel1.getFont().deriveFont(jLabel1.getFont().getStyle() | java.awt.Font.BOLD));
        jLabel1.setText("Create a Document Transfer Transaction:");

        jButton2.setBackground(javax.swing.UIManager.getDefaults().getColor("Actions.Blue"));
        jButton2.setForeground(new java.awt.Color(255, 255, 255));
        jButton2.setText("OK");
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });

        jButton3.setBackground(new java.awt.Color(242, 242, 242));
        jButton3.setForeground(javax.swing.UIManager.getDefaults().getColor("Actions.Blue"));
        jButton3.setText("Cancel");
        jButton3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton3ActionPerformed(evt);
            }
        });

        rcvdBy1.setFont(rcvdBy1.getFont().deriveFont(rcvdBy1.getFont().getStyle() | java.awt.Font.BOLD));

        prepBy1.setFont(prepBy1.getFont().deriveFont(prepBy1.getFont().getStyle() | java.awt.Font.BOLD));

        toBranch1.setFont(toBranch1.getFont().deriveFont(toBranch1.getFont().getStyle() | java.awt.Font.BOLD));
        toBranch1.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));

        fromBranch1.setFont(fromBranch1.getFont().deriveFont(fromBranch1.getFont().getStyle() | java.awt.Font.BOLD));
        fromBranch1.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));

        jLabel7.setBackground(javax.swing.UIManager.getDefaults().getColor("textInactiveText"));
        jLabel7.setFont(jLabel7.getFont());
        jLabel7.setText("Receiving Branch:");

        doc.setColumns(20);
        doc.setFont(doc.getFont().deriveFont(doc.getFont().getStyle() | java.awt.Font.BOLD));
        doc.setRows(3);
        jScrollPane1.setViewportView(doc);

        jLabel9.setBackground(javax.swing.UIManager.getDefaults().getColor("textInactiveText"));
        jLabel9.setFont(jLabel9.getFont());
        jLabel9.setText("Received By:");

        jLabel6.setBackground(javax.swing.UIManager.getDefaults().getColor("textInactiveText"));
        jLabel6.setFont(jLabel6.getFont());
        jLabel6.setText("Prepared By:");

        refNo1.setFont(refNo1.getFont().deriveFont(refNo1.getFont().getStyle() | java.awt.Font.BOLD));

        jLabel19.setBackground(javax.swing.UIManager.getDefaults().getColor("textInactiveText"));
        jLabel19.setFont(jLabel19.getFont());
        jLabel19.setText("Details of Item to be Delivered:");

        jLabel2.setBackground(javax.swing.UIManager.getDefaults().getColor("textInactiveText"));
        jLabel2.setFont(jLabel2.getFont());
        jLabel2.setText("Transfer Reference No.");

        jLabel4.setBackground(javax.swing.UIManager.getDefaults().getColor("textInactiveText"));
        jLabel4.setFont(jLabel4.getFont());
        jLabel4.setText("Transferring Branch");

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                .addComponent(jLabel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(prepBy1, javax.swing.GroupLayout.Alignment.LEADING)
                                .addComponent(fromBranch1, javax.swing.GroupLayout.Alignment.LEADING, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(refNo1, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.PREFERRED_SIZE, 184, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(jLabel6)
                            .addComponent(jLabel19))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(toBranch1, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addGroup(jPanel4Layout.createSequentialGroup()
                                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel9)
                                    .addComponent(jLabel7))
                                .addGap(0, 71, Short.MAX_VALUE))
                            .addComponent(rcvdBy1)))
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addComponent(jLabel2)
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel2)
                .addGap(0, 0, 0)
                .addComponent(refNo1, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel4)
                    .addComponent(jLabel7))
                .addGap(0, 0, 0)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(fromBranch1, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(toBranch1, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel6)
                    .addComponent(jLabel9))
                .addGap(0, 0, 0)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(prepBy1, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(rcvdBy1, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel19)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 57, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(jButton2, javax.swing.GroupLayout.PREFERRED_SIZE, 96, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jButton3, javax.swing.GroupLayout.PREFERRED_SIZE, 103, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addComponent(jLabel1)
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGap(6, 6, 6)
                        .addComponent(jPanel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                .addContainerGap())
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(12, 12, 12)
                .addComponent(jLabel1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(12, 12, 12)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jButton3, javax.swing.GroupLayout.DEFAULT_SIZE, 32, Short.MAX_VALUE)
                    .addComponent(jButton2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap(40, Short.MAX_VALUE))
        );

        jTabbedPane1.addTab("Document Transfer", jPanel2);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jTabbedPane1)
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jTabbedPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 403, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        if (this.doc.getText().length() > 0 && this.prepBy1.getText().length() > 0 && this.rcvdBy1.getText().length() > 0) {
            TransferConfirm transcon = new TransferConfirm(null, true);
            transcon.setXrefNo(this.refNo1.getText());
            transcon.setXfrombranch(this.fromBranch1.getSelectedItem().toString());
            transcon.setXtobranch(this.toBranch1.getSelectedItem().toString());
            transcon.setXprepby(this.prepBy1.getText());
            transcon.setXrcvdby(this.rcvdBy1.getText());
            transcon.setXdoc(this.doc.getText());
            transcon.fillTB();
            transcon.setLocationRelativeTo(null);
            transcon.setVisible(true);
            if (transcon.isPrintOn()) {
                this.xfer.setDatabase(getDatabase());
                this.xfer.setBranch(this.fromBranch1.getSelectedItem().toString());
                this.xfer.setPrep_by(this.prepBy1.getText());
                this.xfer.setRcvd_by(this.rcvdBy1.getText());
                this.xfer.setXrefno(this.refNo1.getText());
                this.xfer.setXdoc(this.doc.getText());
                int tableRowCount = 0;
                try {
                    Connection connect = DriverManager.getConnection(this.driver, this.f_user, this.f_pass);
                    Statement query0 = connect.createStatement();
                    ResultSet rSet = query0.executeQuery("Select COUNT(*) from " + getDatabase() + ".transfer");
                    while (rSet.next()) {
                        tableRowCount = rSet.getInt(1);
                    }
                    System.out.println(tableRowCount);
                    tableRowCount++;
                } catch (SQLException ex) {
                    this.con.saveProp("mpis_last_error", String.valueOf(ex));
                    System.out.println("error in count search");
                }
                this.xfer.setCash_transaction_type("XFR");
                this.xfer.setTransaction_no(tableRowCount);
                this.xfer.generateTransaction_id();
                this.xfer.addDocTransferTransaction(this.fromBranch1.getSelectedItem().toString(), this.toBranch1.getSelectedItem().toString());
                if (this.xfer.isTransfer_inserted()) {

                    ReportPrinter pr = new ReportPrinter();
                    pr.setReport_name("Document Transfer Slip");
                    Map<Object, Object> parameters = new HashMap<>();
                    parameters.put("XNO", this.refNo1.getText());
//                    if (this.con.getProp("branch").equalsIgnoreCase("Tangos")) {
                        pr.setDestination(this.con.getProp("transfolder") + this.refNo1.getText() + ".pdf");
                        pr.printReport("/Reports/doctransfer.jrxml", parameters);
//                    } else {
//                        pr.setDestination(this.con.getProp("transfolder") + this.refNo1.getText() + ".xls");
//                        pr.printDotMatrix("/Reports/doctransfer.jrxml", parameters);
//                    }

                    dispose();
                } else {
                    JOptionPane.showMessageDialog(this.jPanel1, "There has been an error inserting this transaction to the database . Please retry. \n If problem persists, contact the database administrator.", "Error!", 0);
                }
            }
        } else {
            JOptionPane.showMessageDialog(null, "Please complete all important informations.");
        }
    }//GEN-LAST:event_jButton2ActionPerformed

    private void toBranchActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_toBranchActionPerformed
        if (this.fromBranch.getItemCount() > 0 && this.toBranch.getItemCount() > 0) {
/* 635 */       if (this.fromBranch.getSelectedItem().toString().equalsIgnoreCase(this.toBranch.getSelectedItem().toString())) {
/* 636 */         this.jComboBox1.setEnabled(true);
/* 637 */         this.toFundName.setEnabled(true);
/* 638 */         this.toFundName.removeAllItems();
/* 639 */         if (getDatabase().equalsIgnoreCase("palawan")) {
/* 640 */           this.toFundName.addItem("Money Changer");
/* 641 */           this.toFundName.addItem("Merlin");
/* 642 */         } else if (getDatabase().equalsIgnoreCase("forex")) {
/* 643 */           this.toFundName.addItem("Merlin");
/* 644 */           this.toFundName.addItem("Palawan EPP");
         } else {
/* 646 */           this.toFundName.addItem("Money Changer");
/* 647 */           this.toFundName.addItem("Palawan EPP");
    }

} else {

/* 652 */         this.toFundName.removeAllItems();
/* 653 */         this.toFundName.addItem("Merlin");
/* 654 */         this.toFundName.addItem("Palawan EPP");
/* 655 */         this.toFundName.addItem("Money Changer");
            }
        }
        try {
/* 659 */       this.refNo.setText(getNextNo());
/* 660 */     } catch (SQLException ex) {
/* 661 */       this.con.saveProp("mpis_last_error", String.valueOf(ex));
/* 662 */       Logger.getLogger(Transfer.class.getName()).log(Level.SEVERE, (String)null, ex);
        }
    }//GEN-LAST:event_toBranchActionPerformed

    private void jButton5ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton5ActionPerformed
        if (Double.parseDouble(this.amount.getText().replace(",", "")) > 0.0D && this.prepBy.getText().length() > 0 && this.rcvdBy.getText().length() > 0) {
/* 668 */       boolean isAutoAdd = false;
/* 669 */       TransferConfirm transcon = new TransferConfirm(null, true);
/* 670 */       transcon.setXrefNo(this.refNo.getText());
/* 671 */       transcon.setXamount(this.amount.getText());
/* 672 */       transcon.setXfrombranch(this.fromBranch.getSelectedItem().toString());
/* 673 */       transcon.setXtobranch(this.toBranch.getSelectedItem().toString());
/* 674 */       if (this.fromBranch.getSelectedItem().toString().equalsIgnoreCase(this.toBranch.getSelectedItem().toString())) {
/* 675 */         transcon.setXfromfund(this.jComboBox1.getSelectedItem().toString());
/* 676 */         transcon.setXtofund(this.toFundName.getSelectedItem().toString());
/* 677 */         isAutoAdd = true;
/* 678 */         System.out.println("auto add true");
       } else {
/* 680 */         transcon.setXfromfund("");
/* 681 */         transcon.setXtofund("");
            }
/* 683 */       transcon.setXprepby(this.prepBy.getText());
/* 684 */       transcon.setXrcvdby(this.rcvdBy.getText());
/* 685 */       transcon.fillTB();
/* 686 */       transcon.setLocationRelativeTo(null);
/* 687 */       transcon.setVisible(true);
/* 688 */       if (transcon.isPrintOn()) {
/* 689 */         this.xfer.setDatabase(getDatabase());
/* 690 */         this.xfer.setBranch(this.fromBranch.getSelectedItem().toString());
/* 691 */         this.xfer.setPrep_by(this.prepBy.getText());
/* 692 */         this.xfer.setRcvd_by(this.rcvdBy.getText());
/* 693 */         this.xfer.setXrefno(this.refNo.getText());
/* 694 */         this.xfer.setTransaction_amount(Double.parseDouble(this.amount.getText().replace(",", "")));
/* 695 */         int tableRowCount = 0;
    try {
/* 697 */           Connection connect = DriverManager.getConnection(this.driver, this.f_user, this.f_pass);
/* 698 */           Statement query0 = connect.createStatement();
/* 699 */           ResultSet rSet = query0.executeQuery("Select COUNT(*) from " + getDatabase() + ".transfer");
/* 700 */           while (rSet.next()) {
/* 701 */             tableRowCount = rSet.getInt(1);
             }
/* 703 */           System.out.println(tableRowCount);
/* 704 */           tableRowCount++;
/* 705 */         } catch (SQLException ex) {
/* 706 */           this.con.saveProp("mpis_last_error", String.valueOf(ex));
/* 707 */           System.out.println("error in count search");
    }
/* 709 */         this.xfer.setCash_transaction_type("XFR");
/* 710 */         this.xfer.setTransaction_no(tableRowCount);

/* 712 */         this.xfer.generateTransaction_id();
/* 713 */         if (isAutoAdd) {
/* 714 */           System.out.println("AUTOMATIC ADDITIONAL IN PROGRESS!");
/* 715 */           Additionals autAdd = new Additionals(null, true);
/* 716 */           autAdd.setTranxamt(Double.parseDouble(this.amount.getText().replace(",", "")));
/* 717 */           autAdd.setTranxrmk("Computer Generated.");
/* 718 */           autAdd.setRefnum(this.refNo.getText());
/* 719 */           autAdd.setAddtype("Transfer Fund");
/* 720 */           autAdd.setLoanname(this.rcvdBy.getText());
/* 721 */           autAdd.setRcvdby(this.prepBy.getText());
/* 722 */           autAdd.setFromfund(this.jComboBox1.getSelectedItem().toString());
/* 723 */           autAdd.setTofund(this.toFundName.getSelectedItem().toString());
/* 724 */           autAdd.setAuto(true);
/* 725 */           autAdd.setDatabase(getDatabase());
/* 726 */           System.out.println("chosen" + this.toFundName.getSelectedItem().toString());
/* 727 */           if (this.toFundName.getSelectedItem().toString().equalsIgnoreCase("Merlin")) {
/* 728 */             System.out.println("to database is merlin");
/* 729 */             autAdd.setToDatabase("cashtransactions");
/* 730 */           } else if (this.toFundName.getSelectedItem().toString().equalsIgnoreCase("Money Changer")) {
/* 731 */             System.out.println("to database is forex");
/* 732 */             autAdd.setToDatabase("forex");
} else {
/* 734 */             System.out.println("to database " + getDatabase());
/* 735 */             autAdd.setToDatabase("palawan");
    }
/* 737 */           System.out.println("here!" + autAdd.getToDatabase());
/* 738 */           autAdd.doAdditionalTranx();
    }
/* 740 */         if (this.fromBranch.getSelectedItem().toString().equalsIgnoreCase(this.toBranch.getSelectedItem().toString())) {
/* 741 */           this.xfer.addTransferTransaction(this.jComboBox1.getSelectedItem().toString(), this.toFundName.getSelectedItem().toString());
} else {
/* 743 */           this.xfer.addTransferTransaction(this.fromBranch.getSelectedItem().toString().concat("-" + this.jComboBox1.getSelectedItem().toString()), this.toBranch.getSelectedItem().toString().concat("-" + this.toFundName.getSelectedItem().toString()));
    }

/* 747 */         if (!this.xfer.isTransfer_inserted()) {
/* 748 */           JOptionPane.showMessageDialog(this.jPanel1, "There has been an error inserting this transaction to the database . Please retry. \n If problem persists, contact the database administrator.", "Error!", 0);
} else {
/* 750 */           TransferBreakdown xbreak = new TransferBreakdown(null, true);
/* 751 */           xbreak.setDatabase(getDatabase());
/* 752 */           xbreak.setRefNo(this.refNo.getText());
/* 753 */           xbreak.setTransfer(Double.parseDouble(this.amount.getText().replace(",", "")));
/* 754 */           if (this.fromBranch.getSelectedItem().toString().equalsIgnoreCase(this.toBranch.getSelectedItem().toString())) {
/* 755 */             xbreak.setFromFundName(this.jComboBox1.getSelectedItem().toString());
/* 756 */             xbreak.setToFundName(this.toFundName.getSelectedItem().toString());
/* 757 */             this.xfer.setTransaction_remarks("Transfer " + this.amount.getText().replace(",", "") + " from " + this.jComboBox1.getSelectedItem().toString() + " to " + this.toFundName.getSelectedItem().toString());
} else {
/* 759 */             xbreak.setFromFundName("");
/* 760 */             xbreak.setToFundName("");
/* 761 */             this.xfer.setTransaction_remarks("Transfer " + this.amount.getText().replace(",", "") + " from " + this.fromBranch.getSelectedItem().toString() + " to " + this.toBranch.getSelectedItem().toString());
    }
/* 763 */           this.xfer.addToDailyTransaction(getDatabase());

/* 765 */           dispose();
/* 766 */           xbreak.setXnum(this.refNo.getText());
/* 767 */           xbreak.setLocationRelativeTo(null);
/* 768 */           xbreak.setVisible(true);
                }

            }

        } else {

/* 776 */       JOptionPane.showMessageDialog(null, "Please complete all important informations.");
        }
    }//GEN-LAST:event_jButton5ActionPerformed

    private void jButton6ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton6ActionPerformed
        setVisible(false);
    }//GEN-LAST:event_jButton6ActionPerformed

    private void amountFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_amountFocusLost
        DecimalHelper decHelp = new DecimalHelper();
/* 786 */     this.amount.setText(decHelp.FormatNumber(Double.parseDouble(this.amount.getText().replace(",", ""))));
    }//GEN-LAST:event_amountFocusLost

    private void fromBranchActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_fromBranchActionPerformed
        if (this.fromBranch.getItemCount() > 0 && this.toBranch.getItemCount() > 0) {
/* 791 */       if (this.fromBranch.getSelectedItem().toString().equalsIgnoreCase(this.toBranch.getSelectedItem().toString())) {
/* 792 */         this.jComboBox1.setEnabled(true);
/* 793 */         this.toFundName.setEnabled(true);
/* 794 */         this.toFundName.removeAllItems();
/* 795 */         if (getDatabase().equalsIgnoreCase("palawan")) {
/* 796 */           this.toFundName.addItem("Money Changer");
/* 797 */           this.toFundName.addItem("Merlin");
/* 798 */         } else if (getDatabase().equalsIgnoreCase("forex")) {
/* 799 */           this.toFundName.addItem("Merlin");
/* 800 */           this.toFundName.addItem("Palawan EPP");
         } else {
/* 802 */           this.toFundName.addItem("Money Changer");
/* 803 */           this.toFundName.addItem("Palawan EPP");
    }

} else {

/* 808 */         this.toFundName.removeAllItems();
/* 809 */         this.toFundName.addItem("Merlin");
/* 810 */         this.toFundName.addItem("Palawan EPP");
/* 811 */         this.toFundName.addItem("Money Changer");
            }
        }
        try {
/* 815 */       this.refNo.setText(getNextNo());
/* 816 */     } catch (SQLException ex) {
/* 817 */       this.con.saveProp("mpis_last_error", String.valueOf(ex));
/* 818 */       Logger.getLogger(Transfer.class.getName()).log(Level.SEVERE, (String)null, ex);
        }
    }//GEN-LAST:event_fromBranchActionPerformed

    private void reprintBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_reprintBtnActionPerformed
        setVisible(false);
/* 828 */     ReprintTransfers rt = new ReprintTransfers(null, true);
/* 829 */     rt.setLocationRelativeTo(null);
/* 830 */    try {
            /* 830 */ rt.updateCombo();
        } catch (SQLException ex) {
            Logger.getLogger(Transfer.class.getName()).log(Level.SEVERE, null, ex);
        }
        rt.setVisible(true);
    }//GEN-LAST:event_reprintBtnActionPerformed

    private void jButton3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton3ActionPerformed
        setVisible(false);
    }//GEN-LAST:event_jButton3ActionPerformed

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
            java.util.logging.Logger.getLogger(Transfer.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(Transfer.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(Transfer.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(Transfer.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        //</editor-fold>

        /* Create and display the dialog */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                Transfer dialog = new Transfer(new javax.swing.JFrame(), true);
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
    private javax.swing.JTextArea doc;
    private javax.swing.JComboBox<String> fromBranch;
    private javax.swing.JComboBox<String> fromBranch1;
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jButton3;
    private javax.swing.JButton jButton5;
    private javax.swing.JButton jButton6;
    private javax.swing.JComboBox<String> jComboBox1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel15;
    private javax.swing.JLabel jLabel16;
    private javax.swing.JLabel jLabel17;
    private javax.swing.JLabel jLabel18;
    private javax.swing.JLabel jLabel19;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTabbedPane jTabbedPane1;
    private javax.swing.JTextField prepBy;
    private javax.swing.JTextField prepBy1;
    private javax.swing.JTextField rcvdBy;
    private javax.swing.JTextField rcvdBy1;
    private javax.swing.JTextField refNo;
    private javax.swing.JTextField refNo1;
    private javax.swing.JButton reprintBtn;
    private javax.swing.JComboBox<String> toBranch;
    private javax.swing.JComboBox<String> toBranch1;
    private javax.swing.JComboBox<String> toFundName;
    // End of variables declaration//GEN-END:variables
}
