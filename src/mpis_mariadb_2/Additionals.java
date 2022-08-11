/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mpis_mariadb_2;

import java.awt.event.ActionEvent;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JComboBox;
import javax.swing.JOptionPane;
import javax.swing.JTextField;

/**
 *
 * @author Lucky
 */
public class Additionals extends javax.swing.JDialog {

    /**
     * @return the tranxamt
     */
    public double getTranxamt() {
        return tranxamt;
    }

    /**
     * @param tranxamt the tranxamt to set
     */
    public void setTranxamt(double tranxamt) {
        this.tranxamt = tranxamt;
    }

    /**
     * @return the tranxrmk
     */
    public String getTranxrmk() {
        return tranxrmk;
    }

    /**
     * @param tranxrmk the tranxrmk to set
     */
    public void setTranxrmk(String tranxrmk) {
        this.tranxrmk = tranxrmk;
    }

    /**
     * @return the refnum
     */
    public String getRefnum() {
        return refnum;
    }

    /**
     * @param refnum the refnum to set
     */
    public void setRefnum(String refnum) {
        this.refnum = refnum;
    }

    /**
     * @return the addtype
     */
    public String getAddtype() {
        return addtype;
    }

    /**
     * @param addtype the addtype to set
     */
    public void setAddtype(String addtype) {
        this.addtype = addtype;
    }

    /**
     * @return the loanname
     */
    public String getLoanname() {
        return loanname;
    }

    /**
     * @param loanname the loanname to set
     */
    public void setLoanname(String loanname) {
        this.loanname = loanname;
    }

    /**
     * @return the rcvdby
     */
    public String getRcvdby() {
        return rcvdby;
    }

    /**
     * @param rcvdby the rcvdby to set
     */
    public void setRcvdby(String rcvdby) {
        this.rcvdby = rcvdby;
    }

    /**
     * @return the fromfund
     */
    public String getFromfund() {
        return fromfund;
    }

    /**
     * @param fromfund the fromfund to set
     */
    public void setFromfund(String fromfund) {
        this.fromfund = fromfund;
    }

    /**
     * @return the tofund
     */
    public String getTofund() {
        return tofund;
    }

    /**
     * @param tofund the tofund to set
     */
    public void setTofund(String tofund) {
        this.tofund = tofund;
    }

    /**
     * @return the auto
     */
    public boolean isAuto() {
        return auto;
    }

    /**
     * @param auto the auto to set
     */
    public void setAuto(boolean auto) {
        this.auto = auto;
    }

    /**
     * @return the toDatabase
     */
    public String getToDatabase() {
        return toDatabase;
    }

    /**
     * @param toDatabase the toDatabase to set
     */
    public void setToDatabase(String toDatabase) {
        this.toDatabase = toDatabase;
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
    public Additionals(java.awt.Frame parent, boolean modal) {
        super(parent, modal);
        initComponents();
        updateCombos();
    }

    private Config con = new Config();
    private final String driver = "jdbc:mariadb://" + this.con.getProp("IP") + ":" + this.con.getProp("port") 
            + "/merlininventorydatabase";;
    private final String f_user = con.getProp("username");
    private final String f_pass = con.getProp("password");
    
    private double tranxamt = 0.0D; 
    private String tranxrmk = ""; 
    private String refnum = ""; 
    private String addtype = ""; 
    private String loanname = ""; 
    private String rcvdby = ""; 
    private String fromfund = ""; 
    private String tofund = ""; 
    private boolean auto = false; 
    private String toDatabase = ""; 
    private String database = ""; 
    
    public void updateAddCombo(JComboBox<String> combo) { 
         
        try {
            combo.removeAllItems();
            Connection connect = DriverManager.getConnection(this.driver, this.f_user, this.f_pass);
            Statement state = connect.createStatement();
            String query = "Select addtl from merlininventorydatabase.combo_cat where addtl is not null";
            ResultSet rset = state.executeQuery(query);
            while (rset.next()) {
                  combo.addItem(rset.getString(1));
            }
            state.close(); 
            connect.close();
            rset.close();
        } catch (SQLException ex) {
            Logger.getLogger(Additionals.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public void updateCombo(JComboBox<String> combo) {
        try {
            combo.removeAllItems();
            Connection connect = DriverManager.getConnection(this.driver, this.f_user, this.f_pass);
            Statement state = connect.createStatement();
            String query = "Select branch_name from merlininventorydatabase.branch_info";
            ResultSet rset = state.executeQuery(query);
            while (rset.next())
            combo.addItem(rset.getString(1)); 
            state.close();
            connect.close();
            combo.setSelectedItem(this.con.getProp("branch"));
        } catch (SQLException ex) {
            this.con.saveProp("mpis_last_error", String.valueOf(ex));
            Logger.getLogger(Additionals.class.getName()).log(Level.SEVERE, (String)null, ex);
        } 
    }
    
    
    public void updateCombos() {
        updateCombo(this.branch);
        updateCombo(this.branch2);
        updateCombo(this.branch3);
        updateAddCombo(this.typeCombo);
        this.receivedBy.setText(this.con.getProp("last_username"));
        this.receivedBy1.setText(this.con.getProp("last_username"));
    }
    
    public void changeFundName() {
        this.fundNameCombo.removeAllItems();
        if (getDatabase().equalsIgnoreCase("palawan")) {
            this.fundNameCombo.addItem("Pawalan EPP");
        } else if (getDatabase().equalsIgnoreCase("forex")) {
            this.fundNameCombo.addItem("Money Changer");
        } else {
            this.fundNameCombo.addItem("Merlin");
        }
    }
    
    public void doAdditionalTranx() {
        Config con = new Config();
        String driver = "jdbc:mariadb://" + con.getProp("IP") + ":" + con.getProp("port") + "/cashtransactions";
        String finalUsername = con.getProp("username");
        String finalPassword = con.getProp("password");
        CashTransactions additional = new CashTransactions();
        additional.setTransaction_amount(getTranxamt());
        additional.setTransaction_remarks(getTranxrmk());
        additional.setXrefno(getRefnum());
        additional.setPetty_cast_type(getAddtype());
        additional.setLoans_name(getLoanname());
        additional.setRcvd_by(getRcvdby());
        additional.setFromBranch(getFromfund());
        additional.setBranch(getTofund());
        additional.setDatabase(getDatabase());
        additional.setCash_transaction_type("ADD");
        int tableRowCount = 0;
        try {
            additional.checkAddType(getToDatabase());
            Connection connect = DriverManager.getConnection(driver, finalUsername, finalPassword);
            Statement query0 = connect.createStatement();
            ResultSet rSet = query0.executeQuery("Select COUNT(*) from " + getDatabase() + ".additionals");
            while (rSet.next())
                tableRowCount = rSet.getInt(1); 
            tableRowCount++;
        } catch (SQLException ex) {
            con.saveProp("mpis_last_error", String.valueOf(ex));
            JOptionPane.showMessageDialog(null, "There has been an error in this transaction.", "Additional", 0);
        }
        if (isAuto()) {
            if (getToDatabase().equalsIgnoreCase("palawan") || getToDatabase().equalsIgnoreCase("forex")) {
                additional.setTransaction_id((new SimpleDateFormat("yyyy")).format(Calendar.getInstance().getTime()).concat("ADD").concat((new DecimalFormat("#00000")).format(tableRowCount)));
            } else {
                additional.setTransaction_id((new SimpleDateFormat("MMddyy")).format(Calendar.getInstance().getTime()).concat("ADD").concat((new DecimalFormat("#00000")).format(tableRowCount)));
            } 
            additional.setTransaction_remarks("Transfer " + getTranxamt() + " from (" + con.getProp("branch") + ") " + getFromfund() + " to " + getTofund());
        } else {
            additional.setTransaction_no(tableRowCount);
            additional.generateTransaction_id();
            additional.generateTransaction_remarks();
        } 
        additional.addAdditionalTransactionToDB(getToDatabase());
        if (!additional.isAdditionals_inserted()) {
            JOptionPane.showMessageDialog(this.jPanel1, "There has been an error inserting this transaction to the database . Please retry. \n If problem persists, contact the database administrator.", "Error!", 0);
        } else {
            if (!this.auto)
                JOptionPane.showMessageDialog(this.jPanel1, "Additional transaction successful.", "Additional Transaction!", 1); 
            if (getTranxamt() > 0.0D)
                additional.addToDailyTransaction(getToDatabase()); 
            dispose();
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

        jTabbedPane1 = new javax.swing.JTabbedPane();
        jPanel1 = new javax.swing.JPanel();
        jLabel10 = new javax.swing.JLabel();
        jLabel11 = new javax.swing.JLabel();
        jLabel12 = new javax.swing.JLabel();
        jLabel13 = new javax.swing.JLabel();
        jLabel14 = new javax.swing.JLabel();
        jLabel15 = new javax.swing.JLabel();
        jLabel16 = new javax.swing.JLabel();
        jLabel18 = new javax.swing.JLabel();
        refNo = new javax.swing.JTextField();
        additionalsAmount = new javax.swing.JTextField();
        receivedFrom = new javax.swing.JTextField();
        receivedBy = new javax.swing.JTextField();
        typeCombo = new javax.swing.JComboBox<>();
        branch = new javax.swing.JComboBox<>();
        branch1 = new javax.swing.JComboBox<>();
        okFundBtn = new javax.swing.JButton();
        cancelButton = new javax.swing.JButton();
        jSeparator2 = new javax.swing.JSeparator();
        jLabel3 = new javax.swing.JLabel();
        jScrollPane2 = new javax.swing.JScrollPane();
        remarks = new javax.swing.JTextArea();
        fundNameCombo = new javax.swing.JComboBox<>();
        jPanel2 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        jLabel9 = new javax.swing.JLabel();
        refNo1 = new javax.swing.JTextField();
        receivedFrom1 = new javax.swing.JTextField();
        receivedBy1 = new javax.swing.JTextField();
        branch2 = new javax.swing.JComboBox<>();
        branch3 = new javax.swing.JComboBox<>();
        okDocBtn = new javax.swing.JButton();
        jButton3 = new javax.swing.JButton();
        jLabel19 = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        remarks1 = new javax.swing.JTextArea();
        jSeparator1 = new javax.swing.JSeparator();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);

        jLabel10.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jLabel10.setText("Create an Additional Transaction:");

        jLabel11.setText("Reference No.:");

        jLabel12.setText("Amount:");

        jLabel13.setText("Type");

        jLabel14.setText("From:");

        jLabel15.setText("Received From:");

        jLabel16.setText("To:");

        jLabel18.setText("Received By:");

        refNo.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N

        additionalsAmount.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        additionalsAmount.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                additionalsAmountFocusLost(evt);
            }
        });
        additionalsAmount.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                additionalsAmountKeyPressed(evt);
            }
            public void keyTyped(java.awt.event.KeyEvent evt) {
                additionalsAmountKeyTyped(evt);
            }
        });

        receivedFrom.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        receivedFrom.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                receivedFromKeyPressed(evt);
            }
        });

        receivedBy.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N

        typeCombo.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        typeCombo.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Transfer Fund", "Additional Fund", "Borrowed Fund", "In and Out", "Petty Cash Change", "Transfer Sangla", "Subasta Item Sold", "Partial Payment", "Renew-Subasta", "Tubos-Subasta ", "Philam Life", "Other Charges", "Others" }));
        typeCombo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                typeComboActionPerformed(evt);
            }
        });

        branch.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        branch.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        branch.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                branchActionPerformed(evt);
            }
        });

        branch1.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        branch1.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Merlin", "" }));

        okFundBtn.setText("OK");
        okFundBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                okFundBtnActionPerformed(evt);
            }
        });

        cancelButton.setText("Cancel");
        cancelButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cancelButtonActionPerformed(evt);
            }
        });

        jLabel3.setText("Remarks:");

        remarks.setBackground(new java.awt.Color(240, 240, 240));
        remarks.setColumns(20);
        remarks.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        remarks.setLineWrap(true);
        remarks.setRows(3);
        remarks.setWrapStyleWord(true);
        remarks.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                remarksKeyPressed(evt);
            }
        });
        jScrollPane2.setViewportView(remarks);

        fundNameCombo.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        fundNameCombo.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Palawan EPP", "Money Changer" }));

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(25, 25, 25)
                        .addComponent(jLabel10))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(22, 22, 22)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addComponent(okFundBtn)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(cancelButton))
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                    .addComponent(jLabel12)
                                    .addComponent(jLabel11)
                                    .addComponent(jLabel13)
                                    .addComponent(jLabel14)
                                    .addComponent(jLabel15)
                                    .addComponent(jLabel16)
                                    .addComponent(jLabel18)
                                    .addComponent(jLabel3))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addComponent(refNo)
                                    .addComponent(additionalsAmount)
                                    .addComponent(typeCombo, 0, 128, Short.MAX_VALUE)
                                    .addComponent(receivedFrom)
                                    .addComponent(branch, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(branch1, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(receivedBy)
                                    .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                                    .addComponent(fundNameCombo, javax.swing.GroupLayout.Alignment.TRAILING, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jSeparator2, javax.swing.GroupLayout.PREFERRED_SIZE, 234, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(21, Short.MAX_VALUE))
        );

        jPanel1Layout.linkSize(javax.swing.SwingConstants.HORIZONTAL, new java.awt.Component[] {cancelButton, okFundBtn});

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
                    .addComponent(refNo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel12)
                    .addComponent(additionalsAmount, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel13)
                    .addComponent(typeCombo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel14)
                    .addComponent(branch, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(4, 4, 4)
                .addComponent(fundNameCombo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel16)
                    .addComponent(branch1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel15)
                    .addComponent(receivedFrom, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(11, 11, 11)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel18)
                    .addComponent(receivedBy, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel3)
                    .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 57, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(okFundBtn)
                    .addComponent(cancelButton))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jTabbedPane1.addTab("Additional Fund", jPanel1);

        jLabel1.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jLabel1.setText("Record Document Transfer Transaction:");

        jLabel2.setText(" Reference No.:");

        jLabel4.setText("From:");

        jLabel6.setText("To:");

        jLabel7.setText("Received From:");

        jLabel9.setText("Received By:");

        receivedFrom1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                receivedFrom1ActionPerformed(evt);
            }
        });

        branch2.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));

        branch3.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));

        okDocBtn.setText("OK");
        okDocBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                okDocBtnActionPerformed(evt);
            }
        });

        jButton3.setText("Cancel");

        jLabel19.setText("Document Details:");

        remarks1.setBackground(new java.awt.Color(240, 240, 240));
        remarks1.setColumns(20);
        remarks1.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        remarks1.setLineWrap(true);
        remarks1.setRows(4);
        jScrollPane1.setViewportView(remarks1);

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGap(25, 25, 25)
                        .addComponent(jLabel1))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGap(19, 19, 19)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jLabel2)
                            .addComponent(jLabel4)
                            .addComponent(jLabel6)
                            .addComponent(jLabel7)
                            .addComponent(jLabel9)
                            .addComponent(jLabel19))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(refNo1, javax.swing.GroupLayout.DEFAULT_SIZE, 102, Short.MAX_VALUE)
                            .addComponent(branch2, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(receivedFrom1)
                            .addComponent(branch3, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                            .addComponent(receivedBy1, javax.swing.GroupLayout.DEFAULT_SIZE, 128, Short.MAX_VALUE)))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jSeparator1, javax.swing.GroupLayout.PREFERRED_SIZE, 249, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGap(62, 62, 62)
                        .addComponent(okDocBtn)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jButton3)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jPanel2Layout.linkSize(javax.swing.SwingConstants.HORIZONTAL, new java.awt.Component[] {branch2, branch3, receivedBy1, receivedFrom1, refNo1});

        jPanel2Layout.linkSize(javax.swing.SwingConstants.HORIZONTAL, new java.awt.Component[] {jButton3, okDocBtn});

        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel1)
                .addGap(3, 3, 3)
                .addComponent(jSeparator1, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(5, 5, 5)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel2)
                    .addComponent(refNo1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel4)
                    .addComponent(branch2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel6)
                    .addComponent(branch3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel7)
                    .addComponent(receivedFrom1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(21, 21, 21)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel9)
                    .addComponent(receivedBy1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(21, 21, 21)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel19)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(21, 21, 21)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(okDocBtn)
                    .addComponent(jButton3))
                .addContainerGap(16, Short.MAX_VALUE))
        );

        jTabbedPane1.addTab("Accept Document Transfer", jPanel2);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jTabbedPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 270, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jTabbedPane1)
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void receivedFrom1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_receivedFrom1ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_receivedFrom1ActionPerformed

    private void okFundBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_okFundBtnActionPerformed
        if (Double.parseDouble(this.additionalsAmount.getText().replace(",", "")) > 0.0D && this.receivedFrom.getText().length() > 0) {
        AddlConfirm adCon = new AddlConfirm(null, true);
        adCon.setReferNo(this.refNo.getText());
        adCon.setAmt(this.additionalsAmount.getText());
        adCon.setFrombranch(this.branch.getSelectedItem().toString());
        adCon.setTobranch(this.con.getProp("branch"));
        adCon.setRcvdfrom(this.receivedFrom.getText());
        adCon.setRcvdby(this.receivedBy.getText());
        adCon.setAremarks(this.remarks.getText());
        adCon.setAddtype(this.typeCombo.getSelectedItem().toString());
        adCon.fillTB();
        adCon.setLocationRelativeTo(null);
        adCon.setVisible(true);
        if (adCon.isPrintOn()) {
            setAuto(false);
            setTranxamt(Double.parseDouble(this.additionalsAmount.getText().replace(",", "")));
            setTranxrmk(this.remarks.getText());
            setRefnum(this.refNo.getText());
            setAddtype(this.typeCombo.getSelectedItem().toString());
            setLoanname(this.receivedFrom.getText());
            setRcvdby(this.receivedBy.getText());
            setTofund(this.fundNameCombo.getSelectedItem().toString());
            setDatabase(getDatabase());
            setToDatabase(getDatabase());
            if (this.typeCombo.getSelectedItem().toString().equalsIgnoreCase("Transfer Fund")) {
                if (this.typeCombo.getSelectedItem().toString().equalsIgnoreCase("Transfer Sangla")) {
                setFromfund(this.branch.getSelectedItem().toString());
            } else {
                setFromfund(this.branch.getSelectedItem().toString().concat("-" + this.fundNameCombo.getSelectedItem().toString()));
                } 
            } else {
                setFromfund("");
                setTofund(this.con.getProp("branch"));
            } 
            doAdditionalTranx();
        } 
        } else {
            JOptionPane.showMessageDialog(null, "Please complete all important informations.");
        }
    }//GEN-LAST:event_okFundBtnActionPerformed

    private void okDocBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_okDocBtnActionPerformed
       //jcombobox2 == fundNameCombo
        //jcombobox1 == typeCombo
        //jcombobox3 == branch1
        
        if (this.receivedBy1.getText().length() > 0 && this.receivedFrom1.getText().length() > 0) {
      AddlConfirm adCon = new AddlConfirm(null, true);
      adCon.setReferNo(this.refNo1.getText());
      adCon.setFrombranch(this.branch2.getSelectedItem().toString());
      adCon.setTobranch(this.branch3.getSelectedItem().toString());
      adCon.setRcvdfrom(this.receivedFrom1.getText());
      adCon.setRcvdby(this.receivedBy1.getText());
      adCon.setAremarks(this.remarks1.getText());
      adCon.fillTB();
      adCon.setLocationRelativeTo(null);
      adCon.setVisible(true);
      if (adCon.isPrintOn()) {
        Config con = new Config();
        String driver = "jdbc:mariadb://" + con.getProp("IP") + ":" + con.getProp("port") + "/cashtransactions";
        String finalUsername = con.getProp("username");
        String finalPassword = con.getProp("password");
        CashTransactions additional = new CashTransactions();
        additional.setDatabase(getDatabase());
        additional.setTransaction_remarks(this.remarks1.getText());
        additional.setXrefno(this.refNo1.getText());
        additional.setLoans_name(this.receivedFrom1.getText());
        additional.setRcvd_by(this.receivedBy1.getText());
        additional.setFromBranch(this.branch2.getSelectedItem().toString());
        additional.setBranch(this.branch3.getSelectedItem().toString());
        additional.setCash_transaction_type("ADD");
        int tableRowCount = 0;
        try {
          Connection connect = DriverManager.getConnection(driver, finalUsername, finalPassword);
          Statement query0 = connect.createStatement();
          ResultSet rSet = query0.executeQuery("Select COUNT(*) from " + getDatabase() + ".additionals");
          while (rSet.next())
            tableRowCount = rSet.getInt(1); 
          tableRowCount++;
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "MySQL Error \n" + String.valueOf(ex), "Database Error", JOptionPane.ERROR_MESSAGE
            );
          con.saveProp("mpis_last_error", String.valueOf(ex));
          }
        additional.setTransaction_no(tableRowCount);
        additional.generateTransaction_id();
        additional.generateTransaction_remarks();
        additional.addAdditionalTransactionToDB();
        if (!additional.isAdditionals_inserted()) {
          JOptionPane.showMessageDialog(this.jPanel1, "There has been an error inserting this transaction to the database . Please retry. \n If problem persists, contact the database administrator.", "Error!", 0);
        } else {
          JOptionPane.showMessageDialog(this.jPanel1, "Document Transfer Transaction recorded successfully.", "Additional Transaction!", 1);
          if (Double.parseDouble(this.additionalsAmount.getText().replace(",", "")) > 0.0D)
            additional.addToDailyTransaction(); 
          dispose();
        } 
      } 
    } else {
      JOptionPane.showMessageDialog(null, "Please complete all important informations.");
    }  
    }//GEN-LAST:event_okDocBtnActionPerformed

    private void remarksKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_remarksKeyPressed
       int key = evt.getKeyCode();
        if (key == 10) {
          okFundBtnActionPerformed((ActionEvent)null);
        } else if (key == 27) {
          dispose();
        }
    }//GEN-LAST:event_remarksKeyPressed

    private void branchActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_branchActionPerformed
        if (this.branch.getItemCount() > 0)
      if (this.con.getProp("branch").equalsIgnoreCase(this.branch.getSelectedItem().toString())) {
        this.fundNameCombo.removeAllItems();
        if (getDatabase().equalsIgnoreCase("palawan")) {
          this.fundNameCombo.addItem("Money Changer");
          this.fundNameCombo.addItem("Merlin");
        } else if (getDatabase().equalsIgnoreCase("forex")) {
          this.fundNameCombo.addItem("Merlin");
          this.fundNameCombo.addItem("Pawalan EPP");
        } else {
          this.fundNameCombo.addItem("Money Changer");
          this.fundNameCombo.addItem("Pawalan EPP");
        } 
      } else {
        this.fundNameCombo.removeAllItems();
        this.fundNameCombo.addItem("Merlin");
        this.fundNameCombo.addItem("Pawalan EPP");
        this.fundNameCombo.addItem("Money Changer");
      }
        
        
        //jcombobox2 == fundNameCombo
        //jcombobox1 == typeCombo
        //jcombobox3 == branch1
    }//GEN-LAST:event_branchActionPerformed

    private void additionalsAmountFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_additionalsAmountFocusLost
         DecimalHelper decHelp = new DecimalHelper();
    this.additionalsAmount.setText(decHelp.FormatNumber(Double.parseDouble(this.additionalsAmount.getText().replace(",", ""))));
    }//GEN-LAST:event_additionalsAmountFocusLost

    private void additionalsAmountKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_additionalsAmountKeyPressed
        int key = evt.getKeyCode();
    if (key == 10) {
      this.receivedFrom.requestFocusInWindow();
      this.receivedFrom.selectAll();
    } else if (key == 27) {
      dispose();
    } 
    }//GEN-LAST:event_additionalsAmountKeyPressed

    private void additionalsAmountKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_additionalsAmountKeyTyped
        if (!Character.isDigit(evt.getKeyChar()) || this.additionalsAmount.getText().length() > 10)
      evt.consume(); 
    }//GEN-LAST:event_additionalsAmountKeyTyped

    private void receivedFromKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_receivedFromKeyPressed
        int key = evt.getKeyCode();
    if (key == 10) {
      this.remarks.requestFocusInWindow();
      this.remarks.selectAll();
    } else if (key == 27) {
      dispose();
    }  
    }//GEN-LAST:event_receivedFromKeyPressed

    private void cancelButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cancelButtonActionPerformed
        dispose();
    }//GEN-LAST:event_cancelButtonActionPerformed

    private void typeComboActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_typeComboActionPerformed
        if (this.typeCombo.getItemCount() > 0)
      if (!this.typeCombo.getSelectedItem().toString().equalsIgnoreCase("Transfer Fund")) {
        this.branch.setEnabled(false);
        this.fundNameCombo.setEnabled(false);
        if (!this.typeCombo.getSelectedItem().toString().equalsIgnoreCase("Transfer Sangla")) {
          this.branch.setEnabled(false);
        } else {
          this.branch.setEnabled(true);
        } 
      } else {
        this.branch.setEnabled(true);
        this.fundNameCombo.setEnabled(true);
      }
    }//GEN-LAST:event_typeComboActionPerformed

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
            java.util.logging.Logger.getLogger(Additionals.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(Additionals.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(Additionals.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(Additionals.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>

        /* Create and display the dialog */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                Additionals dialog = new Additionals(new javax.swing.JFrame(), true);
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
    private javax.swing.JTextField additionalsAmount;
    private javax.swing.JComboBox<String> branch;
    private javax.swing.JComboBox<String> branch1;
    private javax.swing.JComboBox<String> branch2;
    private javax.swing.JComboBox<String> branch3;
    private javax.swing.JButton cancelButton;
    private javax.swing.JComboBox<String> fundNameCombo;
    private javax.swing.JButton jButton3;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel15;
    private javax.swing.JLabel jLabel16;
    private javax.swing.JLabel jLabel18;
    private javax.swing.JLabel jLabel19;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JSeparator jSeparator2;
    private javax.swing.JTabbedPane jTabbedPane1;
    private javax.swing.JButton okDocBtn;
    private javax.swing.JButton okFundBtn;
    private javax.swing.JTextField receivedBy;
    private javax.swing.JTextField receivedBy1;
    private javax.swing.JTextField receivedFrom;
    private javax.swing.JTextField receivedFrom1;
    private javax.swing.JTextField refNo;
    private javax.swing.JTextField refNo1;
    private javax.swing.JTextArea remarks;
    private javax.swing.JTextArea remarks1;
    private javax.swing.JComboBox<String> typeCombo;
    // End of variables declaration//GEN-END:variables
}
