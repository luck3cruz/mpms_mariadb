/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.merlin;

import java.awt.Cursor;
import java.awt.event.ActionEvent;
import java.awt.event.FocusEvent;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.print.Doc;
import javax.print.DocFlavor;
import javax.print.DocPrintJob;
import javax.print.PrintException;
import javax.print.PrintService;
import javax.print.PrintServiceLookup;
import javax.print.SimpleDoc;
import javax.print.attribute.HashPrintRequestAttributeSet;
import javax.print.attribute.PrintRequestAttributeSet;
import javax.print.attribute.standard.Sides;
import javax.swing.JComboBox;
import javax.swing.JOptionPane;

/**
 *
 * @author Lucky
 */
public class Redemption extends javax.swing.JPanel {

    /**
     * @return the monthDiffDisplayed
     */
    public String getMonthDiffDisplayed() {
        return monthDiffDisplayed;
    }

    /**
     * @param monthDiffDisplayed the monthDiffDisplayed to set
     */
    public void setMonthDiffDisplayed(String monthDiffDisplayed) {
        this.monthDiffDisplayed = monthDiffDisplayed;
    }

    /**
     * @return the intRateDisplayed
     */
    public String getInterestDisplayed() {
        return interestDisplayed;
    }

    /**
     * @param intRateDisplayed the intRateDisplayed to set
     */
    public void setInterestDisplayed(String intRateDisplayed) {
        this.interestDisplayed = intRateDisplayed;
    }

    /**
     * @return the intMonthRateAutoAdj
     */
    public boolean isIntMonthRateAutoAdj() {
        return intMonthRateAutoAdj;
    }

    /**
     * @param intMonthRateAutoAdj the intMonthRateAutoAdj to set
     */
    public void setIntMonthRateAutoAdj(boolean intMonthRateAutoAdj) {
        this.intMonthRateAutoAdj = intMonthRateAutoAdj;
    }

    /**
     * @return the tranx_date
     */
    public Date getTranx_date() {
        return tranx_date;
    }

    /**
     * @param tranx_date the tranx_date to set
     */
    public void setTranx_date(Date tranx_date) {
        this.tranx_date = tranx_date;
    }

    /**
     * @return the exp_date
     */
    public String getExp_date() {
        return exp_date;
    }

    /**
     * @param exp_date the exp_date to set
     */
    public void setExp_date(String exp_date) {
        this.exp_date = exp_date;
    }

    /**
     * Creates new form Renewal
     */
    public Redemption() {
        initComponents();
    }
    
    private Config con = new Config();
    private final String driver = "jdbc:mariadb://" + this.con.getProp("IP") + ":" + this.con.getProp("port") 
            + "/cashtransactions";;
    private final String f_user = con.getProp("username");
    private final String f_pass = con.getProp("password");
    private DecimalHelper decHelp = new DecimalHelper();
    private DateHelper dateHelp = new DateHelper();
    private Computation calculate = new Computation();
    private SimpleDateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd");
    private ReportPrinter pr = new ReportPrinter();
    private Sangla oldSangla = new Sangla();
    private String[] oldConstants = new String[5];
    private String exp_date = "";
    private Date tranx_date = Calendar.getInstance().getTime();
    private boolean intMonthRateAutoAdj = true;

    //FOR LINKED COMPUTATIONS (NUM_MONTHS, INT_RATE, INTEREST)
    private String monthDiffDisplayed = "";
    private String interestDisplayed = "";
    
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
        updateCombo(this.branchSearchRenew);
        updateCombo(this.branchRet);
    }
    
    public void requestFocusInSearch() {
        this.oldPapNoLRenew.requestFocusInWindow();
         this.oldPapNoLRenew.selectAll();
    }

    
    public void clearFieldsInLoanRenewal() {
        this.papNumRet.setText("");
        this.statusRenew.setSelectedIndex(0);
        this.procByRet.setText("");
        this.nameRet.setText("");
        this.addRet.setText("");
        this.conNumRet.setText("");
        this.descRet.setText("");
        this.classificationRet.setSelectedIndex(0);
        this.remarksRet.setText("");
        this.transDateRet.setDate(null);
        this.matDateRet.setDate(null);
        this.expDateRet.setDate(null);
        this.principalRet.setText("");
        this.interestRet.setText("");
        this.svcChgRet.setText("");
        this.othChgRet.setText("");
        this.netProRet.setText("");
        this.insuranceRet.setText("");
        this.principalRdm.setText("0.00");
        this.intRateSlider.setValue(7);
        this.interestRdm.setText("0.00");
        this.numOfMonths.setValue(0);
        this.stamp.setText("0.00");
        this.liqDam.setText("0.00");
        this.affLoss.setText("0.00");
        this.unpaidAI.setText("0.00");
        this.totalOutBalRdm.setText("0.00");
        this.partialPaymentBox.setText("0.00");

        //RESET LINKED BUTTON SETTINGS TO DEFAULT
        setIntMonthRateAutoAdj(true);
    }
   
    public void retrieveButtonStatusRnw(boolean status) {
        this.classificationRet.setEnabled(status);
        this.branchRet.setEnabled(status);
        this.procByRet.setEditable(status);
        this.nameRet.setEditable(status);
        this.addRet.setEditable(status);
        this.descRet.setEditable(status);
        this.classificationRet.setEnabled(status);
        this.remarksRet.setEditable(status);
        this.statusRenew.setEnabled(status);
        this.transDateRet.setEditable(status);
        this.matDateRet.setEditable(status);
        this.expDateRet.setEditable(status);
        this.intRateSlider.setEnabled(status);
        this.interestRdm.setEditable(status);
        this.numOfMonths.setEnabled(status);
        this.stamp.setEditable(status);
        this.affLoss.setEditable(status);
        this.liqDam.setEditable(status);
        this.unpaidAI.setEditable(status);
        this.partialPaymentBox.setEditable(status);
    }
 
 
   
    public void displayRetrievedInfo() {
        System.out.println(this.oldSangla.isRetrievalSuccessful());
        if (this.oldSangla.isRetrievalSuccessful()) {
          this.papNumRet.setText(this.oldSangla.getPap_num());
          this.nameRet.setText(this.oldSangla.getClient_name());
          this.addRet.setText(this.oldSangla.getClient_address());
          this.branchRet.setSelectedItem(this.oldSangla.getBranch());
          this.procByRet.setText(this.oldSangla.getProcessed_by());
          this.descRet.setText(this.oldSangla.getItem_description());
          this.classificationRet.setSelectedItem(this.oldSangla.getClassification());
          if (this.oldSangla.getMotorIn() == 1) {
                motorInCheckbox.setSelected(true);
            } else {
                motorInCheckbox.setSelected(false);
            }
          try {
                this.transDateRet.setDate(this.dateFormatter.parse(this.oldSangla.getTransaction_date()));
                this.matDateRet.setDate(this.dateFormatter.parse(this.oldSangla.getMaturity_date()));
                this.expDateRet.setDate(this.dateFormatter.parse(this.oldSangla.getExpiration_date()));
          } catch (ParseException ex) {
                this.con.saveProp("mpis_last_error", String.valueOf(ex));
                Logger.getLogger(Redemption.class.getName()).log(Level.SEVERE, (String)null, ex);
          } 
          this.statusRenew.setSelectedItem(this.oldSangla.getStatus());
          this.remarksRet.setText(this.oldSangla.getRemarks());
          this.principalRet.setText(this.decHelp.FormatNumber(this.oldSangla.getPrincipal()));
          this.interestRet.setText(this.decHelp.FormatNumber(this.oldSangla.getAdvance_interest()));
          this.svcChgRet.setText(this.decHelp.FormatNumber(this.oldSangla.getService_charge()));
          this.insuranceRet.setText(this.decHelp.FormatNumber(this.oldSangla.getInsurance()));
          this.othChgRet.setText(this.decHelp.FormatNumber(this.oldSangla.getOther_charges()));
          this.netProRet.setText(this.decHelp.FormatNumber(this.oldSangla.getNet_proceeds()));
          this.conNumRet.setText(this.oldSangla.getContact_no());
          boolean retrieveStatus = false;
          retrieveButtonStatusRnw(retrieveStatus);
        } else {
            JOptionPane.showMessageDialog(null, "No item found.\n", "Message", 0);
        } 
    }
  
  
    public void initializeRetrievedInfoDisplayRenew() {
        this.papNumRet.setText("");
        this.nameRet.setText("");
        this.addRet.setText("");
        this.branchRet.setSelectedIndex(0);
        this.procByRet.setText("");
        this.descRet.setText("");
        this.classificationRet.setSelectedIndex(1);
        this.transDateRet.setDate(null);
        this.matDateRet.setDate(null);
        this.expDateRet.setDate(null);
        this.statusRenew.setSelectedItem("Open");
        this.principalRet.setText("");
        this.interestRet.setText("");
        this.insuranceRet.setText("");
        this.svcChgRet.setText("");
        this.othChgRet.setText("");
        this.netProRet.setText("");
        this.remarksRet.setText("");
    }

    public void redeemLoanFromSearch(String papNo, String branch) {
        oldPapNoLRenew.setText(papNo);
        branchSearchRenew.setSelectedItem(branch);
        System.out.println(oldPapNoLRenew.getText() + branchSearchRenew.getSelectedItem().toString());
        retrieveInfoActionPerformed((ActionEvent)null);
   }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jScrollPane4 = new javax.swing.JScrollPane();
        jPanel4 = new javax.swing.JPanel();
        jPanel5 = new javax.swing.JPanel();
        jLabel7 = new javax.swing.JLabel();
        jLabel37 = new javax.swing.JLabel();
        oldPapNoLRenew = new javax.swing.JTextField();
        jLabel38 = new javax.swing.JLabel();
        branchSearchRenew = new javax.swing.JComboBox<>();
        retrieveInfo = new javax.swing.JButton();
        jPanel6 = new javax.swing.JPanel();
        jLabel41 = new javax.swing.JLabel();
        jLabel42 = new javax.swing.JLabel();
        jLabel43 = new javax.swing.JLabel();
        jLabel44 = new javax.swing.JLabel();
        jLabel45 = new javax.swing.JLabel();
        jLabel46 = new javax.swing.JLabel();
        jLabel47 = new javax.swing.JLabel();
        jLabel48 = new javax.swing.JLabel();
        jLabel49 = new javax.swing.JLabel();
        jLabel50 = new javax.swing.JLabel();
        jLabel51 = new javax.swing.JLabel();
        jLabel52 = new javax.swing.JLabel();
        jLabel53 = new javax.swing.JLabel();
        reprintCompSht = new javax.swing.JButton();
        redeemThisItem = new javax.swing.JButton();
        updateStatus = new javax.swing.JButton();
        papNumRet = new javax.swing.JTextField();
        nameRet = new javax.swing.JTextField();
        conNumRet = new javax.swing.JTextField();
        procByRet = new javax.swing.JTextField();
        svcChgRet = new javax.swing.JTextField();
        interestRet = new javax.swing.JTextField();
        principalRet = new javax.swing.JTextField();
        jScrollPane5 = new javax.swing.JScrollPane();
        descRet = new javax.swing.JTextArea();
        jScrollPane6 = new javax.swing.JScrollPane();
        addRet = new javax.swing.JTextArea();
        branchRet = new javax.swing.JComboBox<>();
        statusRenew = new javax.swing.JComboBox<>();
        classificationRet = new javax.swing.JComboBox<>();
        jLabel54 = new javax.swing.JLabel();
        jScrollPane7 = new javax.swing.JScrollPane();
        remarksRet = new javax.swing.JTextArea();
        jLabel55 = new javax.swing.JLabel();
        jLabel56 = new javax.swing.JLabel();
        jLabel57 = new javax.swing.JLabel();
        jLabel58 = new javax.swing.JLabel();
        jLabel59 = new javax.swing.JLabel();
        netProRet = new javax.swing.JTextField();
        othChgRet = new javax.swing.JTextField();
        insuranceRet = new javax.swing.JTextField();
        transDateRet = new org.jdesktop.swingx.JXDatePicker();
        expDateRet = new org.jdesktop.swingx.JXDatePicker();
        matDateRet = new org.jdesktop.swingx.JXDatePicker();
        motorInCheckbox = new javax.swing.JCheckBox();
        jPanel7 = new javax.swing.JPanel();
        jLabel13 = new javax.swing.JLabel();
        principalRdm = new javax.swing.JTextField();
        amountDueLabel = new javax.swing.JLabel();
        jSeparator2 = new javax.swing.JSeparator();
        totalOutBalRdm = new javax.swing.JTextField();
        jPanel8 = new javax.swing.JPanel();
        jLabel23 = new javax.swing.JLabel();
        jLabel64 = new javax.swing.JLabel();
        jLabel24 = new javax.swing.JLabel();
        interestRdm = new javax.swing.JTextField();
        intRateSlider = new javax.swing.JSlider();
        numOfMonths = new javax.swing.JSpinner();
        intLabel = new javax.swing.JLabel();
        linkToggleButton = new javax.swing.JToggleButton();
        jPanel9 = new javax.swing.JPanel();
        stamp = new javax.swing.JTextField();
        jLabel65 = new javax.swing.JLabel();
        affLoss = new javax.swing.JTextField();
        liqDam = new javax.swing.JTextField();
        jLabel66 = new javax.swing.JLabel();
        jLabel67 = new javax.swing.JLabel();
        jPanel11 = new javax.swing.JPanel();
        unpaidAiCheckbox = new javax.swing.JCheckBox();
        unpaidAI = new javax.swing.JTextField();
        partialPaymentBox = new javax.swing.JTextField();
        partialPaymentCheckbox = new javax.swing.JCheckBox();
        jLabel4 = new javax.swing.JLabel();
        finalizeRedemptionBtn = new javax.swing.JButton();
        cancelBtn = new javax.swing.JButton();
        finalizeUpdateBtn = new javax.swing.JButton();

        jPanel5.setBackground(new java.awt.Color(255, 255, 255));
        jPanel5.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(255, 255, 255), 1, true));

        jLabel7.setFont(new java.awt.Font("Segoe UI Semibold", 0, 13)); // NOI18N
        jLabel7.setForeground(new java.awt.Color(51, 51, 51));
        jLabel7.setText("Enter Papeleta No. and Branch of Item to be redeem.");

        jLabel37.setForeground(new java.awt.Color(102, 102, 102));
        jLabel37.setText("Papeleta No.:");
        jLabel37.setVerticalAlignment(javax.swing.SwingConstants.BOTTOM);

        oldPapNoLRenew.setFont(new java.awt.Font("Segoe UI Semibold", 0, 13)); // NOI18N
        oldPapNoLRenew.setForeground(new java.awt.Color(51, 51, 51));
        oldPapNoLRenew.setMargin(new java.awt.Insets(2, 20, 2, 20));
        oldPapNoLRenew.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                oldPapNoLRenewFocusLost(evt);
            }
        });
        oldPapNoLRenew.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                oldPapNoLRenewKeyTyped(evt);
            }
        });

        jLabel38.setForeground(new java.awt.Color(102, 102, 102));
        jLabel38.setText("Branch:");
        jLabel38.setVerticalAlignment(javax.swing.SwingConstants.BOTTOM);

        branchSearchRenew.setFont(new java.awt.Font("Segoe UI Semibold", 0, 13)); // NOI18N
        branchSearchRenew.setForeground(new java.awt.Color(51, 51, 51));
        branchSearchRenew.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));

        retrieveInfo.setBackground(new java.awt.Color(79, 119, 141));
        retrieveInfo.setFont(new java.awt.Font("Segoe UI Semibold", 0, 13)); // NOI18N
        retrieveInfo.setForeground(new java.awt.Color(255, 255, 255));
        retrieveInfo.setText("Retrieve Info");
        retrieveInfo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                retrieveInfoActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel5Layout = new javax.swing.GroupLayout(jPanel5);
        jPanel5.setLayout(jPanel5Layout);
        jPanel5Layout.setHorizontalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addGap(11, 11, 11)
                .addComponent(jLabel7)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addGap(26, 26, 26)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(oldPapNoLRenew, javax.swing.GroupLayout.DEFAULT_SIZE, 177, Short.MAX_VALUE)
                    .addComponent(jLabel37, javax.swing.GroupLayout.PREFERRED_SIZE, 98, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel38)
                    .addGroup(jPanel5Layout.createSequentialGroup()
                        .addComponent(branchSearchRenew, 0, 105, Short.MAX_VALUE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(retrieveInfo, javax.swing.GroupLayout.PREFERRED_SIZE, 127, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(18, 18, 18))
        );
        jPanel5Layout.setVerticalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addGap(20, 20, 20)
                .addComponent(jLabel7)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel37)
                    .addComponent(jLabel38))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(oldPapNoLRenew, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(branchSearchRenew, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(retrieveInfo, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(13, Short.MAX_VALUE))
        );

        jPanel5Layout.linkSize(javax.swing.SwingConstants.VERTICAL, new java.awt.Component[] {branchSearchRenew, oldPapNoLRenew, retrieveInfo});

        jPanel6.setBackground(new java.awt.Color(239, 246, 250));
        jPanel6.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(227, 240, 251), 1, true));

        jLabel41.setForeground(new java.awt.Color(102, 102, 102));
        jLabel41.setText("Papeleta No.:");
        jLabel41.setVerticalAlignment(javax.swing.SwingConstants.BOTTOM);

        jLabel42.setForeground(new java.awt.Color(102, 102, 102));
        jLabel42.setText("Branch:");
        jLabel42.setVerticalAlignment(javax.swing.SwingConstants.BOTTOM);

        jLabel43.setForeground(new java.awt.Color(102, 102, 102));
        jLabel43.setText("Client Name:");
        jLabel43.setVerticalAlignment(javax.swing.SwingConstants.BOTTOM);

        jLabel44.setForeground(new java.awt.Color(102, 102, 102));
        jLabel44.setText("Client Address:");
        jLabel44.setVerticalAlignment(javax.swing.SwingConstants.BOTTOM);

        jLabel45.setForeground(new java.awt.Color(102, 102, 102));
        jLabel45.setText("Contact No.:");
        jLabel45.setVerticalAlignment(javax.swing.SwingConstants.BOTTOM);

        jLabel46.setForeground(new java.awt.Color(102, 102, 102));
        jLabel46.setText("Status:");
        jLabel46.setVerticalAlignment(javax.swing.SwingConstants.BOTTOM);

        jLabel47.setForeground(new java.awt.Color(102, 102, 102));
        jLabel47.setText("Item Description:");
        jLabel47.setVerticalAlignment(javax.swing.SwingConstants.BOTTOM);

        jLabel48.setForeground(new java.awt.Color(102, 102, 102));
        jLabel48.setText("Classification:");
        jLabel48.setVerticalAlignment(javax.swing.SwingConstants.BOTTOM);

        jLabel49.setForeground(new java.awt.Color(102, 102, 102));
        jLabel49.setText("Processed by:");
        jLabel49.setVerticalAlignment(javax.swing.SwingConstants.BOTTOM);

        jLabel50.setForeground(new java.awt.Color(102, 102, 102));
        jLabel50.setText("Loan Date:");
        jLabel50.setVerticalAlignment(javax.swing.SwingConstants.BOTTOM);

        jLabel51.setForeground(new java.awt.Color(102, 102, 102));
        jLabel51.setText("Remarks:");
        jLabel51.setVerticalAlignment(javax.swing.SwingConstants.BOTTOM);

        jLabel52.setForeground(new java.awt.Color(102, 102, 102));
        jLabel52.setText("Principal:");
        jLabel52.setVerticalAlignment(javax.swing.SwingConstants.BOTTOM);

        jLabel53.setForeground(new java.awt.Color(102, 102, 102));
        jLabel53.setText("Expiry Date:");
        jLabel53.setVerticalAlignment(javax.swing.SwingConstants.BOTTOM);

        reprintCompSht.setBackground(new java.awt.Color(227, 240, 251));
        reprintCompSht.setFont(new java.awt.Font("Segoe UI Semibold", 0, 13)); // NOI18N
        reprintCompSht.setForeground(new java.awt.Color(79, 119, 141));
        reprintCompSht.setText("Reprint Computation Sheet");
        reprintCompSht.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                reprintCompShtActionPerformed(evt);
            }
        });

        redeemThisItem.setBackground(new java.awt.Color(79, 119, 141));
        redeemThisItem.setFont(new java.awt.Font("Segoe UI Semibold", 0, 13)); // NOI18N
        redeemThisItem.setForeground(new java.awt.Color(255, 255, 255));
        redeemThisItem.setText("Redeem This Loan");
        redeemThisItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                redeemThisItemActionPerformed(evt);
            }
        });

        updateStatus.setBackground(new java.awt.Color(227, 240, 251));
        updateStatus.setFont(new java.awt.Font("Segoe UI Semibold", 0, 13)); // NOI18N
        updateStatus.setForeground(new java.awt.Color(79, 119, 141));
        updateStatus.setText("Update Status");
        updateStatus.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                updateStatusActionPerformed(evt);
            }
        });

        papNumRet.setFont(new java.awt.Font("Segoe UI Semibold", 0, 13)); // NOI18N
        papNumRet.setForeground(new java.awt.Color(51, 51, 51));
        papNumRet.setToolTipText("Old Papelete No.");
        papNumRet.setMargin(new java.awt.Insets(2, 20, 2, 20));
        papNumRet.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                papNumRetFocusLost(evt);
            }
        });

        nameRet.setFont(new java.awt.Font("Segoe UI Semibold", 0, 13)); // NOI18N
        nameRet.setForeground(new java.awt.Color(51, 51, 51));
        nameRet.setMargin(new java.awt.Insets(2, 20, 2, 20));

        conNumRet.setFont(new java.awt.Font("Segoe UI Semibold", 0, 13)); // NOI18N
        conNumRet.setForeground(new java.awt.Color(51, 51, 51));
        conNumRet.setMargin(new java.awt.Insets(2, 20, 2, 20));
        conNumRet.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                conNumRetFocusLost(evt);
            }
        });
        conNumRet.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                conNumRetActionPerformed(evt);
            }
        });
        conNumRet.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                conNumRetKeyPressed(evt);
            }
        });

        procByRet.setFont(new java.awt.Font("Segoe UI Semibold", 0, 13)); // NOI18N
        procByRet.setForeground(new java.awt.Color(51, 51, 51));
        procByRet.setMargin(new java.awt.Insets(2, 20, 2, 20));
        procByRet.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                procByRetFocusLost(evt);
            }
        });
        procByRet.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                procByRetKeyPressed(evt);
            }
            public void keyTyped(java.awt.event.KeyEvent evt) {
                procByRetKeyTyped(evt);
            }
        });

        svcChgRet.setEditable(false);
        svcChgRet.setFont(new java.awt.Font("Segoe UI Semibold", 0, 13)); // NOI18N
        svcChgRet.setForeground(new java.awt.Color(51, 51, 51));
        svcChgRet.setHorizontalAlignment(javax.swing.JTextField.TRAILING);
        svcChgRet.setMargin(new java.awt.Insets(2, 20, 2, 20));
        svcChgRet.setMinimumSize(new java.awt.Dimension(36, 20));
        svcChgRet.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                svcChgRetActionPerformed(evt);
            }
        });

        interestRet.setEditable(false);
        interestRet.setFont(new java.awt.Font("Segoe UI Semibold", 0, 13)); // NOI18N
        interestRet.setForeground(new java.awt.Color(51, 51, 51));
        interestRet.setHorizontalAlignment(javax.swing.JTextField.TRAILING);
        interestRet.setMargin(new java.awt.Insets(2, 20, 2, 20));
        interestRet.setMinimumSize(new java.awt.Dimension(36, 20));

        principalRet.setEditable(false);
        principalRet.setFont(new java.awt.Font("Segoe UI Semibold", 0, 13)); // NOI18N
        principalRet.setForeground(new java.awt.Color(51, 51, 51));
        principalRet.setHorizontalAlignment(javax.swing.JTextField.TRAILING);
        principalRet.setMargin(new java.awt.Insets(2, 20, 2, 20));
        principalRet.setMinimumSize(new java.awt.Dimension(36, 20));
        principalRet.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                principalRetActionPerformed(evt);
            }
        });

        descRet.setColumns(40);
        descRet.setFont(new java.awt.Font("Segoe UI Semibold", 0, 13)); // NOI18N
        descRet.setForeground(new java.awt.Color(51, 51, 51));
        descRet.setLineWrap(true);
        descRet.setRows(2);
        descRet.setWrapStyleWord(true);
        descRet.setMargin(new java.awt.Insets(2, 20, 2, 20));
        descRet.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                descRetFocusLost(evt);
            }
        });
        descRet.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                descRetKeyPressed(evt);
            }
        });
        jScrollPane5.setViewportView(descRet);

        addRet.setColumns(40);
        addRet.setFont(new java.awt.Font("Segoe UI Semibold", 0, 13)); // NOI18N
        addRet.setForeground(new java.awt.Color(51, 51, 51));
        addRet.setLineWrap(true);
        addRet.setRows(2);
        addRet.setWrapStyleWord(true);
        addRet.setMargin(new java.awt.Insets(2, 20, 2, 20));
        addRet.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                addRetFocusLost(evt);
            }
        });
        addRet.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                addRetKeyPressed(evt);
            }
        });
        jScrollPane6.setViewportView(addRet);

        branchRet.setFont(new java.awt.Font("Segoe UI Semibold", 0, 13)); // NOI18N
        branchRet.setForeground(new java.awt.Color(51, 51, 51));
        branchRet.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));

        statusRenew.setFont(new java.awt.Font("Segoe UI Semibold", 0, 13)); // NOI18N
        statusRenew.setForeground(new java.awt.Color(51, 51, 51));
        statusRenew.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Open", "Renewed", "Expired", "Closed", "Repossessed", "Sold" }));

        classificationRet.setFont(new java.awt.Font("Segoe UI Semibold", 0, 13)); // NOI18N
        classificationRet.setForeground(new java.awt.Color(51, 51, 51));
        classificationRet.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Appliance", "Gadget", "Jewelry", "Vehicle - 2W", "Vehicle - 4W", "Others" }));
        classificationRet.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                classificationRetActionPerformed(evt);
            }
        });

        jLabel54.setForeground(new java.awt.Color(102, 102, 102));
        jLabel54.setText("Maturity Date:");

        remarksRet.setColumns(20);
        remarksRet.setFont(new java.awt.Font("Segoe UI Semibold", 0, 13)); // NOI18N
        remarksRet.setForeground(new java.awt.Color(51, 51, 51));
        remarksRet.setLineWrap(true);
        remarksRet.setRows(2);
        remarksRet.setWrapStyleWord(true);
        remarksRet.setMargin(new java.awt.Insets(2, 20, 2, 20));
        remarksRet.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                remarksRetFocusLost(evt);
            }
        });
        remarksRet.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                remarksRetKeyPressed(evt);
            }
        });
        jScrollPane7.setViewportView(remarksRet);

        jLabel55.setForeground(new java.awt.Color(102, 102, 102));
        jLabel55.setText("Adv. Interest:");

        jLabel56.setForeground(new java.awt.Color(102, 102, 102));
        jLabel56.setText("Service Charge:");

        jLabel57.setForeground(new java.awt.Color(102, 102, 102));
        jLabel57.setText("Insurance:");

        jLabel58.setForeground(new java.awt.Color(102, 102, 102));
        jLabel58.setText("Other Charges:");

        jLabel59.setForeground(new java.awt.Color(102, 102, 102));
        jLabel59.setText("Net Proceeds:");

        netProRet.setEditable(false);
        netProRet.setFont(new java.awt.Font("Segoe UI Semibold", 0, 13)); // NOI18N
        netProRet.setForeground(new java.awt.Color(51, 51, 51));
        netProRet.setHorizontalAlignment(javax.swing.JTextField.TRAILING);
        netProRet.setMargin(new java.awt.Insets(2, 20, 2, 20));
        netProRet.setMinimumSize(new java.awt.Dimension(36, 20));
        netProRet.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                netProRetActionPerformed(evt);
            }
        });

        othChgRet.setEditable(false);
        othChgRet.setFont(new java.awt.Font("Segoe UI Semibold", 0, 13)); // NOI18N
        othChgRet.setForeground(new java.awt.Color(51, 51, 51));
        othChgRet.setHorizontalAlignment(javax.swing.JTextField.TRAILING);
        othChgRet.setMargin(new java.awt.Insets(2, 20, 2, 20));
        othChgRet.setMinimumSize(new java.awt.Dimension(36, 20));

        insuranceRet.setEditable(false);
        insuranceRet.setFont(new java.awt.Font("Segoe UI Semibold", 0, 13)); // NOI18N
        insuranceRet.setForeground(new java.awt.Color(51, 51, 51));
        insuranceRet.setHorizontalAlignment(javax.swing.JTextField.TRAILING);
        insuranceRet.setMargin(new java.awt.Insets(2, 20, 2, 20));
        insuranceRet.setMinimumSize(new java.awt.Dimension(36, 20));

        transDateRet.setForeground(new java.awt.Color(102, 102, 102));
        transDateRet.setToolTipText("YYYY-MM-DD");
        transDateRet.setFormats(dateFormatter);
        transDateRet.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                transDateRetActionPerformed(evt);
            }
        });

        expDateRet.setForeground(new java.awt.Color(102, 102, 102));
        expDateRet.setToolTipText("YYYY-MM-DD");
        expDateRet.setFormats(dateFormatter);

        matDateRet.setForeground(new java.awt.Color(102, 102, 102));
        matDateRet.setToolTipText("YYYY-MM-DD");
        matDateRet.setFormats(dateFormatter);

        motorInCheckbox.setFont(new java.awt.Font("Segoe UI Semibold", 1, 13)); // NOI18N
        motorInCheckbox.setForeground(new java.awt.Color(102, 102, 102));
        motorInCheckbox.setSelected(true);
        motorInCheckbox.setText("Motor-in");
        motorInCheckbox.setEnabled(false);
        motorInCheckbox.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                motorInCheckboxActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel6Layout = new javax.swing.GroupLayout(jPanel6);
        jPanel6.setLayout(jPanel6Layout);
        jPanel6Layout.setHorizontalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addGap(27, 27, 27)
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel6Layout.createSequentialGroup()
                        .addComponent(jLabel48, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jLabel49, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGap(444, 444, 444))
                    .addGroup(jPanel6Layout.createSequentialGroup()
                        .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel6Layout.createSequentialGroup()
                                .addGap(3, 3, 3)
                                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel52, javax.swing.GroupLayout.PREFERRED_SIZE, 96, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(principalRet, javax.swing.GroupLayout.PREFERRED_SIZE, 96, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(insuranceRet, javax.swing.GroupLayout.PREFERRED_SIZE, 96, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jLabel57, javax.swing.GroupLayout.PREFERRED_SIZE, 96, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(12, 12, 12)
                                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(interestRet, javax.swing.GroupLayout.PREFERRED_SIZE, 121, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jLabel55, javax.swing.GroupLayout.PREFERRED_SIZE, 121, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jLabel58, javax.swing.GroupLayout.PREFERRED_SIZE, 121, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(othChgRet, javax.swing.GroupLayout.PREFERRED_SIZE, 121, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(12, 12, 12)
                                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(svcChgRet, javax.swing.GroupLayout.PREFERRED_SIZE, 123, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(netProRet, javax.swing.GroupLayout.PREFERRED_SIZE, 123, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jLabel59, javax.swing.GroupLayout.PREFERRED_SIZE, 123, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jLabel56, javax.swing.GroupLayout.PREFERRED_SIZE, 123, javax.swing.GroupLayout.PREFERRED_SIZE)))
                            .addComponent(jLabel51)
                            .addComponent(jLabel47)
                            .addComponent(jLabel44)
                            .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                .addComponent(jScrollPane5, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.PREFERRED_SIZE, 394, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(jScrollPane6, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.PREFERRED_SIZE, 394, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel6Layout.createSequentialGroup()
                                    .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                        .addComponent(jLabel43, javax.swing.GroupLayout.DEFAULT_SIZE, 246, Short.MAX_VALUE)
                                        .addComponent(nameRet))
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                    .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addGroup(jPanel6Layout.createSequentialGroup()
                                            .addComponent(jLabel45, javax.swing.GroupLayout.PREFERRED_SIZE, 115, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addGap(0, 25, Short.MAX_VALUE))
                                        .addComponent(conNumRet))))
                            .addGroup(jPanel6Layout.createSequentialGroup()
                                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel41, javax.swing.GroupLayout.PREFERRED_SIZE, 106, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(papNumRet, javax.swing.GroupLayout.PREFERRED_SIZE, 114, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(jPanel6Layout.createSequentialGroup()
                                        .addComponent(jLabel42, javax.swing.GroupLayout.PREFERRED_SIZE, 145, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                        .addComponent(jLabel46))
                                    .addGroup(jPanel6Layout.createSequentialGroup()
                                        .addComponent(branchRet, javax.swing.GroupLayout.PREFERRED_SIZE, 145, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                        .addComponent(statusRenew, javax.swing.GroupLayout.PREFERRED_SIZE, 115, javax.swing.GroupLayout.PREFERRED_SIZE))))
                            .addComponent(jScrollPane7, javax.swing.GroupLayout.PREFERRED_SIZE, 396, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(jPanel6Layout.createSequentialGroup()
                                .addComponent(classificationRet, javax.swing.GroupLayout.PREFERRED_SIZE, 129, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(motorInCheckbox, javax.swing.GroupLayout.PREFERRED_SIZE, 101, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(procByRet, javax.swing.GroupLayout.PREFERRED_SIZE, 140, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(jPanel6Layout.createSequentialGroup()
                                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel50, javax.swing.GroupLayout.PREFERRED_SIZE, 130, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(transDateRet, javax.swing.GroupLayout.PREFERRED_SIZE, 127, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel54, javax.swing.GroupLayout.PREFERRED_SIZE, 130, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(matDateRet, javax.swing.GroupLayout.PREFERRED_SIZE, 124, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel53)
                                    .addComponent(expDateRet, javax.swing.GroupLayout.PREFERRED_SIZE, 127, javax.swing.GroupLayout.PREFERRED_SIZE)))
                            .addGroup(jPanel6Layout.createSequentialGroup()
                                .addGap(29, 29, 29)
                                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                    .addComponent(reprintCompSht, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addGroup(jPanel6Layout.createSequentialGroup()
                                        .addComponent(redeemThisItem, javax.swing.GroupLayout.PREFERRED_SIZE, 178, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(updateStatus, javax.swing.GroupLayout.PREFERRED_SIZE, 169, javax.swing.GroupLayout.PREFERRED_SIZE)))))
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
        );

        jPanel6Layout.linkSize(javax.swing.SwingConstants.HORIZONTAL, new java.awt.Component[] {expDateRet, jLabel50, jLabel54, transDateRet});

        jPanel6Layout.linkSize(javax.swing.SwingConstants.HORIZONTAL, new java.awt.Component[] {insuranceRet, interestRet, jLabel52, jLabel55, jLabel56, jLabel57, jLabel58, jLabel59, netProRet, othChgRet, principalRet, svcChgRet});

        jPanel6Layout.setVerticalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addGap(17, 17, 17)
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel41)
                    .addComponent(jLabel42)
                    .addComponent(jLabel46))
                .addGap(1, 1, 1)
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(papNumRet, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(branchRet, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(statusRenew, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel43)
                    .addComponent(jLabel45))
                .addGap(1, 1, 1)
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(conNumRet, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(nameRet, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(13, 13, 13)
                .addComponent(jLabel44)
                .addGap(1, 1, 1)
                .addComponent(jScrollPane6, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel47)
                .addGap(1, 1, 1)
                .addComponent(jScrollPane5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel48)
                    .addComponent(jLabel49))
                .addGap(1, 1, 1)
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(classificationRet, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(motorInCheckbox, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(procByRet, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel50)
                    .addComponent(jLabel54)
                    .addComponent(jLabel53))
                .addGap(1, 1, 1)
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(transDateRet, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(matDateRet, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(expDateRet, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jLabel51)
                .addGap(1, 1, 1)
                .addComponent(jScrollPane7, javax.swing.GroupLayout.PREFERRED_SIZE, 48, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel52)
                    .addComponent(jLabel55)
                    .addComponent(jLabel56))
                .addGap(1, 1, 1)
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(principalRet, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(svcChgRet, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(interestRet, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel57)
                    .addComponent(jLabel58)
                    .addComponent(jLabel59))
                .addGap(1, 1, 1)
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(insuranceRet, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(othChgRet, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(netProRet, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(updateStatus, javax.swing.GroupLayout.DEFAULT_SIZE, 35, Short.MAX_VALUE)
                    .addComponent(redeemThisItem, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(reprintCompSht, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(15, Short.MAX_VALUE))
        );

        jPanel6Layout.linkSize(javax.swing.SwingConstants.VERTICAL, new java.awt.Component[] {classificationRet, procByRet});

        jPanel6Layout.linkSize(javax.swing.SwingConstants.VERTICAL, new java.awt.Component[] {branchRet, papNumRet, statusRenew});

        jPanel7.setBackground(new java.awt.Color(255, 255, 255));
        jPanel7.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(255, 255, 255), 1, true));

        jLabel13.setForeground(new java.awt.Color(92, 92, 92));
        jLabel13.setText("Principal:");
        jLabel13.setVerticalAlignment(javax.swing.SwingConstants.BOTTOM);

        principalRdm.setEditable(false);
        principalRdm.setFont(new java.awt.Font("Segoe UI Semibold", 0, 13)); // NOI18N
        principalRdm.setForeground(new java.awt.Color(51, 51, 51));
        principalRdm.setHorizontalAlignment(javax.swing.JTextField.TRAILING);
        principalRdm.setText("0.00");
        principalRdm.setToolTipText("");
        principalRdm.setMargin(new java.awt.Insets(2, 20, 2, 20));
        principalRdm.setMinimumSize(new java.awt.Dimension(36, 20));
        principalRdm.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                principalRdmKeyTyped(evt);
            }
        });

        amountDueLabel.setFont(new java.awt.Font("Segoe UI Semibold", 1, 13)); // NOI18N
        amountDueLabel.setForeground(new java.awt.Color(92, 92, 92));
        amountDueLabel.setText("Total Outstanding Balance");
        amountDueLabel.setVerticalAlignment(javax.swing.SwingConstants.BOTTOM);

        jSeparator2.setForeground(new java.awt.Color(102, 102, 102));

        totalOutBalRdm.setEditable(false);
        totalOutBalRdm.setBackground(new java.awt.Color(119, 179, 212));
        totalOutBalRdm.setFont(new java.awt.Font("Segoe UI Semibold", 0, 14)); // NOI18N
        totalOutBalRdm.setForeground(new java.awt.Color(255, 255, 255));
        totalOutBalRdm.setHorizontalAlignment(javax.swing.JTextField.TRAILING);
        totalOutBalRdm.setText("0.00");
        totalOutBalRdm.setMargin(new java.awt.Insets(2, 20, 2, 20));

        jPanel8.setBackground(new java.awt.Color(239, 246, 250));
        jPanel8.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(227, 240, 251), 1, true));

        jLabel23.setForeground(new java.awt.Color(92, 92, 92));
        jLabel23.setText("Months");
        jLabel23.setVerticalAlignment(javax.swing.SwingConstants.BOTTOM);

        jLabel64.setForeground(new java.awt.Color(92, 92, 92));
        jLabel64.setText("Interest");
        jLabel64.setVerticalAlignment(javax.swing.SwingConstants.BOTTOM);

        jLabel24.setForeground(new java.awt.Color(92, 92, 92));
        jLabel24.setText("Int. Rate (in %)");
        jLabel24.setVerticalAlignment(javax.swing.SwingConstants.BOTTOM);

        interestRdm.setEditable(false);
        interestRdm.setFont(new java.awt.Font("Segoe UI Semibold", 0, 13)); // NOI18N
        interestRdm.setForeground(new java.awt.Color(51, 51, 51));
        interestRdm.setHorizontalAlignment(javax.swing.JTextField.TRAILING);
        interestRdm.setText("0.00");
        interestRdm.setToolTipText("");
        interestRdm.setMargin(new java.awt.Insets(2, 20, 2, 20));
        interestRdm.setPreferredSize(new java.awt.Dimension(64, 115));
        interestRdm.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                interestRdmFocusGained(evt);
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                interestRdmFocusLost(evt);
            }
        });
        interestRdm.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                interestRdmActionPerformed(evt);
            }
        });
        interestRdm.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                interestRdmKeyPressed(evt);
            }
            public void keyTyped(java.awt.event.KeyEvent evt) {
                interestRdmKeyTyped(evt);
            }
        });

        intRateSlider.setMaximum(15);
        intRateSlider.setMinorTickSpacing(1);
        intRateSlider.setPaintLabels(true);
        intRateSlider.setSnapToTicks(true);
        intRateSlider.setToolTipText("Interest Rate");
        intRateSlider.setValue(5);
        intRateSlider.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                intRateSliderStateChanged(evt);
            }
        });

        numOfMonths.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                numOfMonthsStateChanged(evt);
            }
        });

        intLabel.setFont(new java.awt.Font("Segoe UI Semibold", 0, 13)); // NOI18N
        intLabel.setForeground(new java.awt.Color(51, 51, 51));
        intLabel.setText("0%");

        linkToggleButton.setBackground(new java.awt.Color(79, 119, 141));
        linkToggleButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/link (11).png"))); // NOI18N
        linkToggleButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                linkToggleButtonActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel8Layout = new javax.swing.GroupLayout(jPanel8);
        jPanel8.setLayout(jPanel8Layout);
        jPanel8Layout.setHorizontalGroup(
            jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel8Layout.createSequentialGroup()
                .addGap(18, 18, 18)
                .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jLabel64, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.PREFERRED_SIZE, 64, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel8Layout.createSequentialGroup()
                        .addComponent(interestRdm, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(linkToggleButton, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(24, 24, 24))
                    .addGroup(jPanel8Layout.createSequentialGroup()
                        .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(intRateSlider, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.PREFERRED_SIZE, 1, Short.MAX_VALUE)
                            .addGroup(jPanel8Layout.createSequentialGroup()
                                .addComponent(jLabel24, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addGap(18, 18, 18)
                                .addComponent(intLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 27, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel23)
                            .addComponent(numOfMonths, javax.swing.GroupLayout.PREFERRED_SIZE, 52, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addGap(73, 73, 73))
        );
        jPanel8Layout.setVerticalGroup(
            jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel8Layout.createSequentialGroup()
                .addGap(7, 7, 7)
                .addComponent(jLabel64)
                .addGap(1, 1, 1)
                .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(interestRdm, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(linkToggleButton, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(13, 13, 13)
                .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel24)
                    .addComponent(intLabel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel23))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(intRateSlider, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(numOfMonths, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
        );

        jPanel9.setBackground(new java.awt.Color(239, 246, 250));
        jPanel9.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(227, 240, 251), 1, true));

        stamp.setEditable(false);
        stamp.setFont(new java.awt.Font("Segoe UI Semibold", 0, 13)); // NOI18N
        stamp.setForeground(new java.awt.Color(51, 51, 51));
        stamp.setHorizontalAlignment(javax.swing.JTextField.TRAILING);
        stamp.setText("0.00");
        stamp.setToolTipText("");
        stamp.setMargin(new java.awt.Insets(2, 20, 2, 20));
        stamp.setPreferredSize(new java.awt.Dimension(64, 25));
        stamp.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                stampFocusGained(evt);
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                stampFocusLost(evt);
            }
        });
        stamp.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                stampKeyPressed(evt);
            }
            public void keyTyped(java.awt.event.KeyEvent evt) {
                stampKeyTyped(evt);
            }
        });

        jLabel65.setForeground(new java.awt.Color(92, 92, 92));
        jLabel65.setText("Affidavit of Loss:");
        jLabel65.setVerticalAlignment(javax.swing.SwingConstants.BOTTOM);

        affLoss.setEditable(false);
        affLoss.setFont(new java.awt.Font("Segoe UI Semibold", 0, 13)); // NOI18N
        affLoss.setForeground(new java.awt.Color(51, 51, 51));
        affLoss.setHorizontalAlignment(javax.swing.JTextField.TRAILING);
        affLoss.setText("0.00");
        affLoss.setToolTipText("");
        affLoss.setMargin(new java.awt.Insets(2, 20, 2, 20));
        affLoss.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                affLossFocusGained(evt);
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                affLossFocusLost(evt);
            }
        });
        affLoss.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                affLossKeyPressed(evt);
            }
            public void keyTyped(java.awt.event.KeyEvent evt) {
                affLossKeyTyped(evt);
            }
        });

        liqDam.setEditable(false);
        liqDam.setFont(new java.awt.Font("Segoe UI Semibold", 0, 13)); // NOI18N
        liqDam.setForeground(new java.awt.Color(51, 51, 51));
        liqDam.setHorizontalAlignment(javax.swing.JTextField.TRAILING);
        liqDam.setText("0.00");
        liqDam.setToolTipText("");
        liqDam.setMargin(new java.awt.Insets(2, 20, 2, 20));
        liqDam.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                liqDamFocusGained(evt);
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                liqDamFocusLost(evt);
            }
        });
        liqDam.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                liqDamKeyPressed(evt);
            }
            public void keyTyped(java.awt.event.KeyEvent evt) {
                liqDamKeyTyped(evt);
            }
        });

        jLabel66.setForeground(new java.awt.Color(92, 92, 92));
        jLabel66.setText("Liquidated Damages:");
        jLabel66.setVerticalAlignment(javax.swing.SwingConstants.BOTTOM);

        jLabel67.setForeground(new java.awt.Color(92, 92, 92));
        jLabel67.setText("Stamp/Notarial Fee:");
        jLabel67.setVerticalAlignment(javax.swing.SwingConstants.BOTTOM);

        javax.swing.GroupLayout jPanel9Layout = new javax.swing.GroupLayout(jPanel9);
        jPanel9.setLayout(jPanel9Layout);
        jPanel9Layout.setHorizontalGroup(
            jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel9Layout.createSequentialGroup()
                .addGap(18, 18, 18)
                .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel9Layout.createSequentialGroup()
                        .addComponent(jLabel65)
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addGroup(jPanel9Layout.createSequentialGroup()
                        .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                            .addComponent(affLoss)
                            .addComponent(jLabel67, javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(stamp, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jLabel66, javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(liqDam))
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
        );
        jPanel9Layout.setVerticalGroup(
            jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel9Layout.createSequentialGroup()
                .addGap(10, 10, 10)
                .addComponent(jLabel67)
                .addGap(1, 1, 1)
                .addComponent(stamp, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jLabel66)
                .addGap(1, 1, 1)
                .addComponent(liqDam, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jLabel65)
                .addGap(1, 1, 1)
                .addComponent(affLoss, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jPanel11.setBackground(new java.awt.Color(239, 246, 250));
        jPanel11.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(227, 240, 251), 1, true));

        unpaidAiCheckbox.setFont(new java.awt.Font("Segoe UI Semibold", 1, 13)); // NOI18N
        unpaidAiCheckbox.setSelected(true);
        unpaidAiCheckbox.setText("with Unpaid Advance Interest?");
        unpaidAiCheckbox.setEnabled(false);
        unpaidAiCheckbox.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                unpaidAiCheckboxActionPerformed(evt);
            }
        });

        unpaidAI.setEditable(false);
        unpaidAI.setFont(new java.awt.Font("Segoe UI Semibold", 0, 13)); // NOI18N
        unpaidAI.setForeground(new java.awt.Color(51, 51, 51));
        unpaidAI.setHorizontalAlignment(javax.swing.JTextField.TRAILING);
        unpaidAI.setText("0.00");
        unpaidAI.setToolTipText("");
        unpaidAI.setMargin(new java.awt.Insets(2, 20, 2, 20));
        unpaidAI.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                unpaidAIFocusGained(evt);
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                unpaidAIFocusLost(evt);
            }
        });
        unpaidAI.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                unpaidAIActionPerformed(evt);
            }
        });
        unpaidAI.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                unpaidAIKeyPressed(evt);
            }
            public void keyTyped(java.awt.event.KeyEvent evt) {
                unpaidAIKeyTyped(evt);
            }
        });

        partialPaymentBox.setEditable(false);
        partialPaymentBox.setFont(new java.awt.Font("Segoe UI Semibold", 0, 13)); // NOI18N
        partialPaymentBox.setForeground(new java.awt.Color(51, 51, 51));
        partialPaymentBox.setHorizontalAlignment(javax.swing.JTextField.TRAILING);
        partialPaymentBox.setText("0.00");
        partialPaymentBox.setToolTipText("");
        partialPaymentBox.setMargin(new java.awt.Insets(2, 20, 2, 20));
        partialPaymentBox.setPreferredSize(new java.awt.Dimension(64, 30));
        partialPaymentBox.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                partialPaymentBoxFocusGained(evt);
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                partialPaymentBoxFocusLost(evt);
            }
        });
        partialPaymentBox.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                partialPaymentBoxActionPerformed(evt);
            }
        });
        partialPaymentBox.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                partialPaymentBoxKeyPressed(evt);
            }
            public void keyTyped(java.awt.event.KeyEvent evt) {
                partialPaymentBoxKeyTyped(evt);
            }
        });

        partialPaymentCheckbox.setFont(new java.awt.Font("Segoe UI Semibold", 1, 13)); // NOI18N
        partialPaymentCheckbox.setSelected(true);
        partialPaymentCheckbox.setText("with Partial Payment?");
        partialPaymentCheckbox.setEnabled(false);
        partialPaymentCheckbox.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                partialPaymentCheckboxActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel11Layout = new javax.swing.GroupLayout(jPanel11);
        jPanel11.setLayout(jPanel11Layout);
        jPanel11Layout.setHorizontalGroup(
            jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel11Layout.createSequentialGroup()
                .addGap(18, 18, 18)
                .addGroup(jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(partialPaymentBox, javax.swing.GroupLayout.PREFERRED_SIZE, 155, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(partialPaymentCheckbox)
                    .addComponent(unpaidAiCheckbox)
                    .addComponent(unpaidAI, javax.swing.GroupLayout.PREFERRED_SIZE, 156, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel11Layout.setVerticalGroup(
            jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel11Layout.createSequentialGroup()
                .addGap(10, 10, 10)
                .addComponent(partialPaymentCheckbox, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(1, 1, 1)
                .addComponent(partialPaymentBox, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(unpaidAiCheckbox, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(unpaidAI, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(6, 6, 6))
        );

        jLabel4.setFont(new java.awt.Font("Segoe UI Semibold", 1, 18)); // NOI18N
        jLabel4.setForeground(new java.awt.Color(102, 102, 102));
        jLabel4.setText("Loan Redemption Computation");

        finalizeRedemptionBtn.setBackground(new java.awt.Color(79, 119, 141));
        finalizeRedemptionBtn.setFont(new java.awt.Font("Segoe UI Semibold", 0, 13)); // NOI18N
        finalizeRedemptionBtn.setForeground(new java.awt.Color(255, 255, 255));
        finalizeRedemptionBtn.setText("Finalize Redemption");
        finalizeRedemptionBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                finalizeRedemptionBtnActionPerformed(evt);
            }
        });

        cancelBtn.setFont(new java.awt.Font("Segoe UI Semibold", 0, 13)); // NOI18N
        cancelBtn.setForeground(new java.awt.Color(79, 119, 141));
        cancelBtn.setText("Cancel");
        cancelBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cancelBtnActionPerformed(evt);
            }
        });

        finalizeUpdateBtn.setFont(new java.awt.Font("Segoe UI Semibold", 0, 13)); // NOI18N
        finalizeUpdateBtn.setForeground(new java.awt.Color(79, 119, 141));
        finalizeUpdateBtn.setText("Finalize Update");
        finalizeUpdateBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                finalizeUpdateBtnActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel7Layout = new javax.swing.GroupLayout(jPanel7);
        jPanel7.setLayout(jPanel7Layout);
        jPanel7Layout.setHorizontalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel7Layout.createSequentialGroup()
                .addGap(9, 9, 9)
                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel7Layout.createSequentialGroup()
                        .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(amountDueLabel, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jLabel4, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addGroup(jPanel7Layout.createSequentialGroup()
                                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                    .addComponent(jPanel8, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(jPanel9, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(jPanel11, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                                .addGap(6, 6, 6))
                            .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel7Layout.createSequentialGroup()
                                .addComponent(totalOutBalRdm, javax.swing.GroupLayout.PREFERRED_SIZE, 175, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(0, 0, Short.MAX_VALUE)))
                        .addGap(4, 4, 4))
                    .addGroup(jPanel7Layout.createSequentialGroup()
                        .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel13, javax.swing.GroupLayout.PREFERRED_SIZE, 141, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(principalRdm, javax.swing.GroupLayout.PREFERRED_SIZE, 141, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jSeparator2, javax.swing.GroupLayout.PREFERRED_SIZE, 294, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(0, 0, Short.MAX_VALUE))))
            .addGroup(jPanel7Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(finalizeRedemptionBtn, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(jPanel7Layout.createSequentialGroup()
                        .addComponent(finalizeUpdateBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 156, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(cancelBtn, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                .addContainerGap())
        );
        jPanel7Layout.setVerticalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel7Layout.createSequentialGroup()
                .addGap(25, 25, 25)
                .addComponent(jLabel4)
                .addGap(18, 18, 18)
                .addComponent(jLabel13)
                .addGap(1, 1, 1)
                .addComponent(principalRdm, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jPanel8, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jPanel9, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jPanel11, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jSeparator2, javax.swing.GroupLayout.PREFERRED_SIZE, 3, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(amountDueLabel)
                .addGap(1, 1, 1)
                .addComponent(totalOutBalRdm, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(finalizeRedemptionBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(finalizeUpdateBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(cancelBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel5, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel6, javax.swing.GroupLayout.PREFERRED_SIZE, 473, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel7, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(41, 41, 41))
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jPanel7, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addComponent(jPanel5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(jPanel6, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jScrollPane4.setViewportView(jPanel4);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane4, javax.swing.GroupLayout.DEFAULT_SIZE, 800, Short.MAX_VALUE)
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane4)
                .addContainerGap())
        );
    }// </editor-fold>//GEN-END:initComponents

    private void finalizeRedemptionBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_finalizeRedemptionBtnActionPerformed
        if ( this.interestRdm.getText().isEmpty() ||  this.stamp.getText().replace(",", "").isEmpty()) {
            JOptionPane.showMessageDialog(null, "Please complete all information required.", "Redeem Loan", 0);
        } else if (!this.statusRenew.getSelectedItem().toString().equals("Closed")) {
            JOptionPane.showMessageDialog(null, "Please set STATUS of item to 'Closed'.", "Redeem Loan", 0);
        } else {
            int n = JOptionPane.showConfirmDialog(null, "Are you sure you want to Redeem this item?", "Confirm Redeem of Loan", 0, 3);
            if (n == 0) {
                this.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));

                // INSERT NEW TABLE FOR DISCOUNTS
                
//                Discount discDia = new Discount(null, true);
//                discDia.setLocationRelativeTo(null);
//                discDia.setVisible(true);

                this.oldSangla.setInterest(Double.parseDouble(this.interestRdm.getText().replace(",", "")));
                this.oldSangla.setInterest_rate(Double.valueOf(this.intRateSlider.getValue()));
                this.oldSangla.setStamp(Double.parseDouble(this.stamp.getText().replace(",", "")));
                this.oldSangla.setLiq_dam(Double.parseDouble(this.liqDam.getText().replace(",", "")));
                this.oldSangla.setAff(Double.parseDouble(this.affLoss.getText().replace(",", "")));
                this.oldSangla.setUnpaid(Double.parseDouble(this.unpaidAI.getText().replace(",", "")));
                this.oldSangla.setNet_proceeds(Double.parseDouble(this.totalOutBalRdm.getText().replace(",", "")));
                this.oldSangla.setTransaction_date(this.dateFormatter.format(this.transDateRet.getDate()));
                System.out.println("dayn diff = " + this.dateHelp.getDayDifference(Calendar.getInstance().getTime(), this.dateHelp.formatStringToDate(getExp_date())));
                if (this.dateHelp.formatStringToDate(getExp_date()).getMonth() - Calendar.getInstance().getTime().getMonth() <= 0) {
                    if (this.oldSangla.getStatus().equalsIgnoreCase("Expired")
                            && (this.dateHelp.getDayDifference(Calendar.getInstance().getTime(), this.dateHelp.formatStringToDate(getExp_date())) > 3
                            || this.dateHelp.formatStringToDate(this.oldSangla.getExpiration_date()).getDate() >= 30)) {
                        this.oldSangla.setSubastaStatus(1);
                        this.oldSangla.setTubosStatus(1);
                    } else if (this.dateHelp.formatStringToDate(getExp_date()).getDate() == this.dateHelp.getLastDateOfCurrentMonth()
                            && this.dateHelp.getDayDifference(Calendar.getInstance().getTime(), this.dateHelp.formatStringToDate(getExp_date())) <= 3) {
                        this.oldSangla.setSubastaStatus(1);
                    }
                } else {
                    this.oldSangla.setSubastaStatus(0);
                    this.oldSangla.setTubosStatus(0);
                }
                this.oldSangla.saveToRescate(this.oldSangla.getItem_code_a());
                this.oldSangla.updateStatusToClosed();
                if (this.oldSangla.isUpdateTubosPapStatus()) {
                    JOptionPane.showMessageDialog(null, "Item update successful! \n Please return item to client. \n Thank you!", "Redeem Loan", 1);
                    File tempFolder = new File("C:\\MPIS\\daytranx");
                    if (!tempFolder.exists()) {
                        tempFolder.mkdir();
                    }
                    Map<Object, Object> parameters = new HashMap<>();
                    parameters.put("ITEM_CODE", this.papNumRet.getText().concat(this.oldSangla.getBranchCode(this.branchRet.getSelectedItem().toString())));
                    parameters.put("SC", Double.valueOf(0.0D));
                    parameters.put("AI", Double.valueOf(0.0D));
                    parameters.put("NO_OF_MONTHS", Integer.valueOf(Integer.parseInt(this.numOfMonths.getValue().toString())));
                    parameters.put("CODE", "T");
                    parameters.put("PARTIAL", Double.valueOf(Double.parseDouble(this.partialPaymentBox.getText().replace(",", ""))));
                    parameters.put("TRANX_DATE", getTranx_date());
                    parameters.put("STATUS", "Redeemed");
                    parameters.put("INSURANCE", Double.valueOf(0.0D));
                    parameters.put("BAWAS_CAP", Double.valueOf(0.0D));
                    parameters.put("BC", Double.valueOf(0.0D));
                    this.pr.setDestination(this.con.getProp("tranx_folder").concat(this.oldSangla.getItem_code_a()).concat(".pdf"));
                    this.pr.printReport("/Reports/compusheet.jrxml", parameters);
                    CashTransactions redeem = new CashTransactions();
                    redeem.setDatabase("cashtransactions");
                    String redeemCashTransactionQuery = "";
                    redeemCashTransactionQuery = "Select COUNT(*) from cashtransactions.redeem";
                    if (!this.oldSangla.getStatus().equalsIgnoreCase("Expired")
                            && this.dateHelp.getDayDifference(Calendar.getInstance().getTime(), this.dateHelp.formatStringToDate(getExp_date())) > 3) {
                        System.out.println("ITEM IS NOT EXPIRED!");
                        redeem.setBranch(this.branchRet.getSelectedItem().toString());
                        redeem.setCash_transaction_type("RDM");
                        redeem.setTransaction_remarks("Redeem (");
                        redeem.setLoans_item_code(this.oldSangla.getItem_code_a());
                        redeem.setLoans_name(this.nameRet.getText());
                        redeem.setTransaction_amount(Double.parseDouble(this.totalOutBalRdm.getText().replace(",", "")));
                        int tableRowCount = 0;
                        try {
                            Connection connect = DriverManager.getConnection(this.driver, this.f_user, this.f_pass);
                            Statement query0 = connect.createStatement();
                            ResultSet rSet = query0.executeQuery(redeemCashTransactionQuery);
                            while (rSet.next()) {
                                tableRowCount = rSet.getInt(1);
                            }
                            tableRowCount++;
                        } catch (SQLException ex) {
                            this.con.saveProp("mpis_last_error", String.valueOf(ex));
                            System.out.println("error in count search");
                        }
                        redeem.setTransaction_no(tableRowCount);
                        redeem.generateTransaction_id();
                        redeem.generateTransaction_remarks();
                        redeem.addRedeemTransactionToDB();
                        if (!redeem.isRedeem_inserted()) {
                            System.out.println("There has been an error inserting this transaction to the database");
                        } else {
                            redeem.addToDailyTransaction();
                        }
                    } else if (this.dateHelp.getDayDifference(Calendar.getInstance().getTime(), this.dateHelp.formatStringToDate(getExp_date())) > 3) {
                        System.out.println("ITEM IS ALREADY EXPIRED!");
                        CashTransactions additional = new CashTransactions();
                        additional.setBranch(this.branchRet.getSelectedItem().toString());
                        additional.setCash_transaction_type("ADD");
                        additional.setPetty_cast_type("Tubos Subasta");
                        additional.setTransaction_remarks("Tubos Subasta Pap#" + this.oldSangla.getPap_num() + "( Received from: " + this.oldSangla.getClient_name() + ")");
                        additional.setTransaction_amount(Double.parseDouble(this.principalRdm.getText().replace(",", "")) + Double.parseDouble(this.interestRdm.getText().replace(",", ""))
                                + Double.parseDouble(this.stamp.getText().replace(",", "")) + Double.parseDouble(this.affLoss.getText().replace(",", ""))
                                + Double.parseDouble(this.unpaidAI.getText().replace(",", "")) + Double.parseDouble(this.liqDam.getText().replace(",", "")));
                        additional.setAddsub(0.0D);
                        additional.setLoans_name(this.oldSangla.getClient_name());
                        int tableRowCount = 0;
                        try {
                            Connection connect = DriverManager.getConnection(this.driver, this.f_user, this.f_pass);
                            Statement query0 = connect.createStatement();
                            ResultSet rSet = query0.executeQuery("Select COUNT(*) from cashtransactions.additionals");
                            while (rSet.next()) {
                                tableRowCount = rSet.getInt(1);
                            }
                            tableRowCount++;
                            System.out.println(tableRowCount);
                        } catch (SQLException ex) {
                            this.con.saveProp("mpis_last_error", String.valueOf(ex));
                            System.out.println("error in count search");
                        }
                        additional.setTransaction_no(tableRowCount);
                        additional.generateTransaction_id();
                        additional.addAdditionalTransactionToDB();
                        if (!additional.isAdditionals_inserted()) {
                            JOptionPane.showMessageDialog(this.jPanel5, "There has been an error inserting this transaction to the database . "
                                    + "Please retry. \n If problem persists, contact the database administrator.", "Error!", 0);
                        } else {
                            additional.addToDailyTransaction();
                        }
                    } else {
                        System.out.println("ITEM IS WITHIN GRACE PERIOD BUT EXPIRED!");
                        redeem.setBranch(this.branchRet.getSelectedItem().toString());
                        redeem.setCash_transaction_type("RDM");
                        redeem.setTransaction_remarks("Redeem (");
                        redeem.setLoans_item_code(this.oldSangla.getItem_code_a());
                        redeem.setLoans_name(this.nameRet.getText());
                        redeem.setTransaction_amount(Double.parseDouble(this.totalOutBalRdm.getText().replace(",", "")));
                        int tableRowCount = 0;
                        try {
                            Connection connect = DriverManager.getConnection(this.driver, this.f_user, this.f_pass);
                            Statement query0 = connect.createStatement();
                            System.out.println("PASOK DITO");
                            System.out.println(redeemCashTransactionQuery);
                            ResultSet rSet = query0.executeQuery(redeemCashTransactionQuery);
                            while (rSet.next()) {
                                tableRowCount = rSet.getInt(1);
                            }
                            tableRowCount++;
                        } catch (SQLException ex) {
                            this.con.saveProp("mpis_last_error", String.valueOf(ex));
                            System.out.println("error in count search");
                        }
                        redeem.setTransaction_no(tableRowCount);
                        redeem.generateTransaction_id();
                        redeem.generateTransaction_remarks();
                        redeem.addRedeemTransactionToDB();
                        if (!redeem.isRedeem_inserted()) {
                            System.out.println("There has been an error inserting this transaction to the database");
                        } else {
                            redeem.addToDailyTransaction();
                        }
                    }
                } else {
                    JOptionPane.showMessageDialog(null, "Unable update database! \n Please check your connection.", "Redeem Loan", 0);
                }
                clearFieldsInLoanRenewal();
                this.oldPapNoLRenew.setText("");
                retrieveButtonStatusRnw(false);
                this.oldPapNoLRenew.setEnabled(true);
                this.branchSearchRenew.setEnabled(true);
                this.retrieveInfo.setEnabled(true);
                this.redeemThisItem.setEnabled(false);
                this.updateStatus.setEnabled(false);
                this.finalizeRedemptionBtn.setEnabled(false);
                this.finalizeUpdateBtn.setEnabled(false);
                this.statusRenew.setEnabled(false);
            }
            this.setCursor(Cursor.getDefaultCursor());
        }
    }//GEN-LAST:event_finalizeRedemptionBtnActionPerformed

    private void cancelBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cancelBtnActionPerformed
        clearFieldsInLoanRenewal();
        retrieveButtonStatusRnw(false);
        this.statusRenew.setEnabled(false);
        this.oldPapNoLRenew.setText("");
        this.oldPapNoLRenew.setEnabled(true);
        this.branchSearchRenew.setEnabled(true);
        this.retrieveInfo.setEnabled(true);
        this.redeemThisItem.setEnabled(false);
        this.updateStatus.setEnabled(false);
        this.finalizeRedemptionBtn.setEnabled(false);
        this.finalizeUpdateBtn.setEnabled(false);
        this.statusRenew.setEnabled(false);
    }//GEN-LAST:event_cancelBtnActionPerformed

    private void finalizeUpdateBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_finalizeUpdateBtnActionPerformed
        if (!this.statusRenew.getSelectedItem().toString().equals("Repossessed") && !this.statusRenew.getSelectedItem().toString().equals("Sold")) {
/* 1694 */       JOptionPane.showMessageDialog(null, "Please set the status to 'Repossessed' or 'Sold'.", "Redeem Loan", 0);
     } else {
/* 1696 */       this.oldSangla.setStatus(this.statusRenew.getSelectedItem().toString());
/* 1697 */       this.oldSangla.setRemarks(this.remarksRet.getText());
/* 1698 */       this.oldSangla.updateStatusToRepossesed();
/* 1699 */       if (this.oldSangla.isUpdateStatusToRepossessed()) {
/* 1700 */         JOptionPane.showMessageDialog(null, "Item update successful! \n", "Redeem Loan", 1);
/* 1701 */         this.oldPapNoLRenew.setEnabled(true);
/* 1702 */         this.branchSearchRenew.setEnabled(true);
/* 1703 */         this.retrieveInfo.setEnabled(true);
/* 1704 */         this.redeemThisItem.setEnabled(false);
/* 1705 */         this.updateStatus.setEnabled(false);
/* 1706 */         this.finalizeRedemptionBtn.setEnabled(false);
/* 1707 */         this.finalizeUpdateBtn.setEnabled(false);
/* 1708 */         this.statusRenew.setEnabled(false);
/* 1709 */         clearFieldsInLoanRenewal();
/* 1710 */         this.oldPapNoLRenew.setText("");
/* 1711 */         retrieveButtonStatusRnw(false);
       } else {
/* 1713 */         JOptionPane.showMessageDialog(null, "Unable to update database! \n", "Redeem Loan", 0);
       } 
     } 
    }//GEN-LAST:event_finalizeUpdateBtnActionPerformed

    private void oldPapNoLRenewFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_oldPapNoLRenewFocusLost
        this.oldPapNoLRenew.setText(this.oldPapNoLRenew.getText().trim());
    }//GEN-LAST:event_oldPapNoLRenewFocusLost

    private void oldPapNoLRenewKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_oldPapNoLRenewKeyTyped
        if ((!Character.isDigit(evt.getKeyChar()) && evt.getKeyChar() != '.') || this.oldPapNoLRenew.getText().length() > 5)
        evt.consume(); 
    }//GEN-LAST:event_oldPapNoLRenewKeyTyped

    private void retrieveInfoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_retrieveInfoActionPerformed
        clearFieldsInLoanRenewal();
        this.oldSangla.setPap_num(this.oldPapNoLRenew.getText());
        this.oldSangla.setOld_pap_num(this.oldPapNoLRenew.getText());
        this.oldSangla.setBranch(this.branchSearchRenew.getSelectedItem().toString());
        this.oldSangla.setOld_item_code();
        this.oldSangla.setItemCodes();
        String itemCode = this.oldSangla.getItem_code_a();
        this.oldSangla.initiateRetrieval(itemCode);
        if (this.oldSangla.isProceedRetrieval()) {
          this.oldSangla.retrieveSangla(itemCode);
          initializeRetrievedInfoDisplayRenew();
          displayRetrievedInfo();
          setTranx_date(this.dateHelp.formatStringToDate(this.oldSangla.getTransaction_date()));
          this.redeemThisItem.requestFocusInWindow();
          this.oldPapNoLRenew.setEnabled(false);
          this.branchSearchRenew.setEnabled(false);
          this.retrieveInfo.setEnabled(false);
          this.updateStatus.setEnabled(false);
          if ((new File("C:\\MPIS\\daytranx\\" + itemCode + ".xls")).exists()) {
            this.oldPapNoLRenew.setEnabled(false);
            this.branchSearchRenew.setEnabled(false);
            this.retrieveInfo.setEnabled(false);
            this.reprintCompSht.setEnabled(true);
          } else {
            this.reprintCompSht.setEnabled(false);
          } 
        } else {
          JOptionPane.showMessageDialog(null, "No item found.\n", "Message", 0);
          this.oldPapNoLRenew.setEnabled(true);
          this.branchSearchRenew.setEnabled(true);
          this.retrieveInfo.setEnabled(true);
          this.updateStatus.setEnabled(true);
          this.oldPapNoLRenew.requestFocusInWindow();
        } 
        setExp_date(this.dateHelp.formatDate(this.expDateRet.getDate()));
        this.redeemThisItem.setEnabled(true);
        this.updateStatus.setEnabled(true);
    }//GEN-LAST:event_retrieveInfoActionPerformed

    private void principalRdmKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_principalRdmKeyTyped
        if ((!Character.isDigit(evt.getKeyChar()) && evt.getKeyChar() != '.') || this.principalRdm.getText().replace(",", "").length() > 10)
        evt.consume();
    }//GEN-LAST:event_principalRdmKeyTyped

    private void interestRdmFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_interestRdmFocusGained
        this.interestRdm.selectAll();
    }//GEN-LAST:event_interestRdmFocusGained

    private void interestRdmFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_interestRdmFocusLost
        if (isIntMonthRateAutoAdj()) {
            if (Double.parseDouble(this.interestRdm.getText().replace(",", "")) > 0.0D) {
            this.intRateSlider.setValue(Integer.parseInt(this.calculate.computeInterestRate(Double.parseDouble(this.principalRdm.getText().replace(",", "")),
                    Double.parseDouble(this.interestRdm.getText().replace(",", "")), Integer.parseInt(this.numOfMonths.getValue().toString()))));
            }
        } else {
            this.interestRdm.setText(this.decHelp.FormatNumber(Double.parseDouble(this.interestRdm.getText().replace(",", ""))));
            this.totalOutBalRdm.setText(this.calculate.computeTotalOutstandingBalance(Double.parseDouble(this.principalRdm.getText().replace(",", "")),
                    Double.parseDouble(this.interestRdm.getText().replace(",", "")),
                    (Double.parseDouble(this.stamp.getText().replace(",", "")) + Double.parseDouble(this.affLoss.getText().replace(",", ""))
                        + Double.parseDouble(this.unpaidAI.getText().replace(",", "")) + Double.parseDouble(this.liqDam.getText().replace(",", ""))),
                    0.0D,
                    Double.parseDouble(this.partialPaymentBox.getText().replace(",", ""))));
        }
    }//GEN-LAST:event_interestRdmFocusLost

    private void interestRdmActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_interestRdmActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_interestRdmActionPerformed

    private void interestRdmKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_interestRdmKeyPressed
        int key = evt.getKeyCode();
        if (key == 10) {
            interestRdmFocusLost((FocusEvent)null);
            this.stamp.requestFocusInWindow();
            this.stamp.selectAll();
        }
    }//GEN-LAST:event_interestRdmKeyPressed

    private void interestRdmKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_interestRdmKeyTyped
        if ((!Character.isDigit(evt.getKeyChar()) && evt.getKeyChar() != '.') || this.interestRdm.getText().replace(",", "").length() > 10)
        evt.consume();
    }//GEN-LAST:event_interestRdmKeyTyped

    private void intRateSliderStateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_intRateSliderStateChanged
        intLabel.setText(Integer.toString(intRateSlider.getValue()).concat("%"));
        if (isIntMonthRateAutoAdj()) {
            this.interestRdm.setText(this.calculate.computeInterest(Double.parseDouble(this.principalRdm.getText().replace(",", "")),
                Double.valueOf(this.intRateSlider.getValue()), Integer.parseInt(this.numOfMonths.getValue().toString())));
        } else {
            this.totalOutBalRdm.setText(this.calculate.computeTotalOutstandingBalance(Double.parseDouble(this.principalRdm.getText().replace(",", "")),
                     Double.valueOf(this.intRateSlider.getValue()),
                    Integer.parseInt(this.numOfMonths.getValue().toString()),
                (Double.parseDouble(this.stamp.getText().replace(",", "")) + Double.parseDouble(this.affLoss.getText().replace(",", ""))
                    + Double.parseDouble(this.unpaidAI.getText().replace(",", "")) + Double.parseDouble(this.liqDam.getText().replace(",", ""))),
                    0.0D,
                    Double.parseDouble(this.partialPaymentBox.getText().replace(",", ""))));
        }
    }//GEN-LAST:event_intRateSliderStateChanged

    private void numOfMonthsStateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_numOfMonthsStateChanged
        if (isIntMonthRateAutoAdj()) {
            this.interestRdm.setText(this.calculate.computeInterest(Double.parseDouble(this.principalRdm.getText().replace(",", "")),
                    Double.valueOf(this.intRateSlider.getValue()), Integer.parseInt(this.numOfMonths.getValue().toString())));
        this.totalOutBalRdm.setText(this.calculate.computeTotalOutstandingBalance(Double.parseDouble(this.principalRdm.getText().replace(",", "")),
                Double.valueOf(this.intRateSlider.getValue()), Integer.parseInt(this.numOfMonths.getValue().toString()),
                Double.parseDouble(this.stamp.getText().replace(",", "")) + Double.parseDouble(this.affLoss.getText().replace(",", ""))
                + Double.parseDouble(this.unpaidAI.getText().replace(",", "")) + Double.parseDouble(this.liqDam.getText().replace(",", "")),
                0.0D, Double.parseDouble(this.partialPaymentBox.getText().replace(",", ""))));
        } 
    }//GEN-LAST:event_numOfMonthsStateChanged

    private void stampFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_stampFocusGained
        this.stamp.selectAll();
    }//GEN-LAST:event_stampFocusGained

    private void stampFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_stampFocusLost
        this.stamp.setText(this.decHelp.FormatNumber(Double.parseDouble(this.stamp.getText().replace(",", ""))));
        if (isIntMonthRateAutoAdj()) {
        this.totalOutBalRdm.setText(
                this.calculate.computeTotalOutstandingBalance(Double.parseDouble(this.principalRdm.getText().replace(",", "")),
                        Double.valueOf(this.intRateSlider.getValue()), Integer.parseInt(this.numOfMonths.getValue().toString()),
                        Double.parseDouble(this.stamp.getText().replace(",", "")) + Double.parseDouble(this.affLoss.getText().replace(",", ""))
                        + Double.parseDouble(this.unpaidAI.getText().replace(",", "")) + Double.parseDouble(this.liqDam.getText().replace(",", "")),
                            0.0D, Double.parseDouble(this.partialPaymentBox.getText().replace(",", ""))));
        }
    }//GEN-LAST:event_stampFocusLost

    private void stampKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_stampKeyPressed
        int key = evt.getKeyCode();
        if (key == 10) {
            intRateSliderStateChanged(null);
            this.liqDam.requestFocusInWindow();
            this.liqDam.selectAll();
        }
    }//GEN-LAST:event_stampKeyPressed

    private void stampKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_stampKeyTyped
        if ((!Character.isDigit(evt.getKeyChar()) && evt.getKeyChar() != '.') || this.stamp.getText().replace(",", "").length() > 10)
        evt.consume();
    }//GEN-LAST:event_stampKeyTyped

    private void affLossFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_affLossFocusGained
        this.affLoss.selectAll();
    }//GEN-LAST:event_affLossFocusGained

    private void affLossFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_affLossFocusLost
        this.affLoss.setText(this.decHelp.FormatNumber(Double.parseDouble(this.affLoss.getText().replace(",", ""))));
        this.totalOutBalRdm.setText(this.calculate.computeTotalOutstandingBalance(Double.parseDouble(this.principalRdm.getText().replace(",", "")), Double.valueOf(this.intRateSlider.getValue()), Integer.parseInt(this.numOfMonths.getValue().toString()), Double.parseDouble(this.stamp.getText().replace(",", "")) + Double.parseDouble(this.affLoss.getText().replace(",", "")) + Double.parseDouble(this.unpaidAI.getText().replace(",", "")) + Double.parseDouble(this.liqDam.getText().replace(",", "")), 0.0D, Double.parseDouble(this.partialPaymentBox.getText().replace(",", ""))));
   
    }//GEN-LAST:event_affLossFocusLost

    private void affLossKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_affLossKeyPressed
        int key = evt.getKeyCode();
        if (key == 10) {
            intRateSliderStateChanged(null);
            this.unpaidAI.requestFocusInWindow();
            this.unpaidAI.selectAll();
        }
    }//GEN-LAST:event_affLossKeyPressed

    private void affLossKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_affLossKeyTyped
        if ((!Character.isDigit(evt.getKeyChar()) && evt.getKeyChar() != '.') || this.stamp.getText().replace(",", "").length() > 10)
        evt.consume();
    }//GEN-LAST:event_affLossKeyTyped

    private void liqDamFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_liqDamFocusGained
        this.liqDam.selectAll();
    }//GEN-LAST:event_liqDamFocusGained

    private void liqDamFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_liqDamFocusLost
        this.liqDam.setText(this.decHelp.FormatNumber(Double.parseDouble(this.liqDam.getText().replace(",", ""))));
        if (isIntMonthRateAutoAdj()) {
            this.totalOutBalRdm.setText(this.calculate.computeTotalOutstandingBalance(Double.parseDouble(this.principalRdm.getText().replace(",", "")),
                    Double.valueOf(this.intRateSlider.getValue()),
                    Integer.parseInt(this.numOfMonths.getValue().toString()),
                    Double.parseDouble(this.stamp.getText().replace(",", "")) + Double.parseDouble(this.affLoss.getText().replace(",", ""))
                    + Double.parseDouble(this.unpaidAI.getText().replace(",", "")) + Double.parseDouble(this.liqDam.getText().replace(",", "")),
                    0.0D,
                    Double.parseDouble(this.partialPaymentBox.getText().replace(",", ""))));
        }
    }//GEN-LAST:event_liqDamFocusLost

    private void liqDamKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_liqDamKeyPressed
        int key = evt.getKeyCode();
        if (key == 10) {
            intRateSliderStateChanged(null);
            this.affLoss.requestFocusInWindow();
            this.affLoss.selectAll();
        }
    }//GEN-LAST:event_liqDamKeyPressed

    private void liqDamKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_liqDamKeyTyped
        if ((!Character.isDigit(evt.getKeyChar()) && evt.getKeyChar() != '.') || this.stamp.getText().replace(",", "").length() > 10)
        evt.consume();
    }//GEN-LAST:event_liqDamKeyTyped

    private void unpaidAiCheckboxActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_unpaidAiCheckboxActionPerformed
        unpaidAI.setEditable(unpaidAiCheckbox.isSelected());
    }//GEN-LAST:event_unpaidAiCheckboxActionPerformed

    private void unpaidAIFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_unpaidAIFocusGained
        this.unpaidAI.selectAll();
    }//GEN-LAST:event_unpaidAIFocusGained

    private void unpaidAIFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_unpaidAIFocusLost
         this.unpaidAI.setText(this.decHelp.FormatNumber(Double.parseDouble(this.unpaidAI.getText().replace(",", ""))));
     this.totalOutBalRdm.setText(this.calculate.computeTotalOutstandingBalance(Double.parseDouble(this.principalRdm.getText().replace(",", "")), Double.valueOf(this.intRateSlider.getValue()), Integer.parseInt(this.numOfMonths.getValue().toString()), Double.parseDouble(this.stamp.getText().replace(",", "")) + Double.parseDouble(this.affLoss.getText().replace(",", "")) + Double.parseDouble(this.unpaidAI.getText().replace(",", "")) + Double.parseDouble(this.liqDam.getText().replace(",", "")), 0.0D, Double.parseDouble(this.partialPaymentBox.getText().replace(",", ""))));
   
    }//GEN-LAST:event_unpaidAIFocusLost

    private void unpaidAIActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_unpaidAIActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_unpaidAIActionPerformed

    private void unpaidAIKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_unpaidAIKeyPressed
       
    }//GEN-LAST:event_unpaidAIKeyPressed

    private void unpaidAIKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_unpaidAIKeyTyped
        if ((!Character.isDigit(evt.getKeyChar()) && evt.getKeyChar() != '.') || this.stamp.getText().replace(",", "").length() > 10)
        evt.consume();
    }//GEN-LAST:event_unpaidAIKeyTyped

    private void partialPaymentBoxFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_partialPaymentBoxFocusGained
        this.partialPaymentBox.selectAll();
    }//GEN-LAST:event_partialPaymentBoxFocusGained

    private void partialPaymentBoxFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_partialPaymentBoxFocusLost
        this.partialPaymentBox.setText(this.decHelp.FormatNumber(Double.parseDouble(this.partialPaymentBox.getText().replace(",", ""))));
        this.totalOutBalRdm.setText(this.calculate.computeTotalOutstandingBalance(Double.parseDouble(this.principalRdm.getText().replace(",", "")), Double.valueOf(this.intRateSlider.getValue()), Integer.parseInt(this.numOfMonths.getValue().toString()), Double.parseDouble(this.stamp.getText().replace(",", "")) + Double.parseDouble(this.affLoss.getText().replace(",", "")) + Double.parseDouble(this.unpaidAI.getText().replace(",", "")) + Double.parseDouble(this.liqDam.getText().replace(",", "")), 0.0D, Double.parseDouble(this.partialPaymentBox.getText().replace(",", ""))));
   
    }//GEN-LAST:event_partialPaymentBoxFocusLost

    private void partialPaymentBoxActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_partialPaymentBoxActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_partialPaymentBoxActionPerformed

    private void partialPaymentBoxKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_partialPaymentBoxKeyPressed
        int key = evt.getKeyCode();
        if (key == 10) {
            intRateSliderStateChanged(null);;
            unpaidAI.requestFocusInWindow();
        }
    }//GEN-LAST:event_partialPaymentBoxKeyPressed

    private void partialPaymentBoxKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_partialPaymentBoxKeyTyped
        if ((!Character.isDigit(evt.getKeyChar()) && evt.getKeyChar() != '.') || this.partialPaymentBox.getText().replace(",", "").length() > 10)
        evt.consume();
    }//GEN-LAST:event_partialPaymentBoxKeyTyped

    private void partialPaymentCheckboxActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_partialPaymentCheckboxActionPerformed
        partialPaymentBox.setEditable(partialPaymentCheckbox.isSelected());
    }//GEN-LAST:event_partialPaymentCheckboxActionPerformed

    private void linkToggleButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_linkToggleButtonActionPerformed
        if (interestRdm.isEditable()) {
            if (oldSangla.getStatus().equalsIgnoreCase("Expired")) {
                if (isIntMonthRateAutoAdj()) {
                    linkToggleButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com.merlin/link (10).png")));  //TOGGLE ICON TO GRAY
                    setIntMonthRateAutoAdj(false);
                    intRateSlider.setValue(0);
                    numOfMonths.setValue(0);
                    intRateSlider.setEnabled(false);
                    numOfMonths.setEnabled(false);
                    interestRdm.requestFocusInWindow();
                } else {
                    linkToggleButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com.merlin/link (11).png"))); 
                    setIntMonthRateAutoAdj(true);
                    this.intRateSlider.setValue(7);
                    this.numOfMonths.setValue(Integer.parseInt(getMonthDiffDisplayed()));
                    this.interestRdm.setText(getInterestDisplayed());
                    intRateSlider.setEnabled(true);
                    numOfMonths.setEnabled(true);
                    intRateSlider.requestFocusInWindow();
                }
            } else {
                JOptionPane.showMessageDialog(null, "This function is only allowed on Expired Loans.");
            }

        }
    }//GEN-LAST:event_linkToggleButtonActionPerformed

    private void motorInCheckboxActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_motorInCheckboxActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_motorInCheckboxActionPerformed

    private void transDateRetActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_transDateRetActionPerformed
        Calendar cal = Calendar.getInstance();
        cal.setTime(this.transDateRet.getDate());
        this.matDateRet.setDate(this.dateHelp.oneMonth(cal));
        this.expDateRet.setDate(this.dateHelp.threeMonths(cal));
    }//GEN-LAST:event_transDateRetActionPerformed

    private void netProRetActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_netProRetActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_netProRetActionPerformed

    private void remarksRetKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_remarksRetKeyPressed
        int key = evt.getKeyCode();
        if (key == 10) {
            this.intRateSlider.requestFocusInWindow();
        }
    }//GEN-LAST:event_remarksRetKeyPressed

    private void remarksRetFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_remarksRetFocusLost
        this.remarksRet.setText(this.remarksRet.getText().trim());
        this.remarksRet.setText(this.remarksRet.getText().replace("\r", "").replace("\n", ""));
    }//GEN-LAST:event_remarksRetFocusLost

    private void classificationRetActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_classificationRetActionPerformed
        if (classificationRet.getSelectedItem().toString().equalsIgnoreCase("Vehicle - 2W") ||
            classificationRet.getSelectedItem().toString().equalsIgnoreCase("Vehicle - 4W")) {
            motorInCheckbox.setEnabled(true);
        } else {
            motorInCheckbox.setEnabled(false);
        }
    }//GEN-LAST:event_classificationRetActionPerformed

    private void addRetKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_addRetKeyPressed
        int key = evt.getKeyCode();
        if (key == 10) {
            this.conNumRet.requestFocusInWindow();
        }
    }//GEN-LAST:event_addRetKeyPressed

    private void addRetFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_addRetFocusLost
        this.addRet.setText(this.addRet.getText().trim());
        this.addRet.setText(this.addRet.getText().replace("\r", "").replace("\n", ""));
    }//GEN-LAST:event_addRetFocusLost

    private void descRetKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_descRetKeyPressed
        int key = evt.getKeyCode();
        if (key == 10) {
            this.remarksRet.requestFocusInWindow();
        }
    }//GEN-LAST:event_descRetKeyPressed

    private void descRetFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_descRetFocusLost
        this.descRet.setText(this.descRet.getText().trim());
        this.descRet.setText(this.descRet.getText().replace("\r", "").replace("\n", ""));
    }//GEN-LAST:event_descRetFocusLost

    private void principalRetActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_principalRetActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_principalRetActionPerformed

    private void svcChgRetActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_svcChgRetActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_svcChgRetActionPerformed

    private void procByRetKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_procByRetKeyTyped
        if (this.procByRet.getText().length() > 45)
        evt.consume();
    }//GEN-LAST:event_procByRetKeyTyped

    private void procByRetKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_procByRetKeyPressed
        int key = evt.getKeyCode();
        if (key == 10) {
            this.descRet.requestFocusInWindow();
        }
    }//GEN-LAST:event_procByRetKeyPressed

    private void procByRetFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_procByRetFocusLost
        this.procByRet.setText(this.procByRet.getText().trim());
    }//GEN-LAST:event_procByRetFocusLost

    private void conNumRetKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_conNumRetKeyPressed
        int key = evt.getKeyCode();
        if (key == 10) {
            this.procByRet.requestFocusInWindow();
        }
    }//GEN-LAST:event_conNumRetKeyPressed

    private void conNumRetActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_conNumRetActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_conNumRetActionPerformed

    private void conNumRetFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_conNumRetFocusLost
        this.conNumRet.setText(this.conNumRet.getText().trim());
    }//GEN-LAST:event_conNumRetFocusLost

    private void papNumRetFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_papNumRetFocusLost
        this.papNumRet.setText(this.papNumRet.getText().trim());
    }//GEN-LAST:event_papNumRetFocusLost

    private void updateStatusActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_updateStatusActionPerformed
        this.statusRenew.setEnabled(true);
        this.statusRenew.setSelectedItem("Repossessed");
        this.remarksRet.setEnabled(true);
        this.redeemThisItem.setEnabled(false);
        this.finalizeRedemptionBtn.setEnabled(false);
        this.finalizeUpdateBtn.setEnabled(true);
        this.remarksRet.setEditable(true);
    }//GEN-LAST:event_updateStatusActionPerformed

    private void redeemThisItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_redeemThisItemActionPerformed
        System.out.println("DAY diff = " + this.dateHelp.getDayDifference(Calendar.getInstance().getTime(), this.dateHelp.formatStringToDate(this.oldSangla.getExpiration_date())));
        Date currentDate = Calendar.getInstance().getTime();
        if (this.oldSangla.getStatus().equals("Renewed") || this.oldSangla.getStatus().equals("Sold") || this.oldSangla.getStatus().equals("Closed")) {
            JOptionPane.showMessageDialog(null, "Unable to redeem item. Please check status.", "Redeem Loan", 0);
        } else {
            retrieveButtonStatusRnw(true);
            this.transDateRet.setEditable(true);
            this.transDateRet.setDate(currentDate);

            this.principalRet.setText(this.principalRet.getText().replace(",", ""));
            this.principalRet.setEditable(false);

            this.numOfMonths.setValue(Integer.parseInt(this.dateHelp.getMonthDifference(currentDate, this.matDateRet.getDate())));
            setMonthDiffDisplayed(numOfMonths.getValue().toString());

            this.procByRet.setText(this.con.getProp("last_username"));
            this.principalRdm.setText(this.principalRet.getText().replace(",", ""));
            this.intRateSlider.setValue(7);
            this.interestRdm.setText(this.calculate.computeInterest(Double.parseDouble(this.principalRdm.getText().replace(",", "")),
                Double.valueOf(this.intRateSlider.getValue()), Integer.parseInt(this.numOfMonths.getValue().toString())));
        setInterestDisplayed(interestRdm.getText());
        this.totalOutBalRdm.setText(this.calculate.computeTotalOutstandingBalance(Double.parseDouble(this.principalRdm.getText().replace(",", "")),
            Double.valueOf(this.intRateSlider.getValue()), Integer.parseInt(this.numOfMonths.getValue().toString()),
            Double.parseDouble(this.stamp.getText().replace(",", "")) + Double.parseDouble(this.affLoss.getText().replace(",", ""))
            + Double.parseDouble(this.unpaidAI.getText().replace(",", "")) + Double.parseDouble(this.liqDam.getText().replace(",", "")), 0.0D,
            Double.parseDouble(this.partialPaymentBox.getText().replace(",", ""))));
    this.statusRenew.setSelectedItem("Closed");
    this.finalizeRedemptionBtn.setEnabled(true);
    this.updateStatus.setEnabled(false);
    this.finalizeUpdateBtn.setEnabled(false);
    this.matDateRet.setDate(null);
    this.expDateRet.setDate(null);
    }
    }//GEN-LAST:event_redeemThisItemActionPerformed

    private void reprintCompShtActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_reprintCompShtActionPerformed
        int conf = JOptionPane.showConfirmDialog(null, "Are you sure you want to reprint the computation sheet for this Pap. No.?", "Reprint Computation Sheet", 0);
        Sangla forRp = new Sangla();
        if (conf == 0) {
            Config con = new Config();
            if (con.getProp("branch").equalsIgnoreCase("Tangos")) {

                try {
                    DocFlavor flavor = DocFlavor.SERVICE_FORMATTED.PAGEABLE;
                    PrintRequestAttributeSet patts = new HashPrintRequestAttributeSet();
                    patts.add(Sides.DUPLEX);
                    PrintService[] ps = PrintServiceLookup.lookupPrintServices(flavor, patts);
                    if (ps.length == 0) {
                        throw new IllegalStateException("No Printer found");
                    }
                    System.out.println("Available printers: " + Arrays.<PrintService>asList(ps));

                    PrintService myService = null;
                    for (PrintService printService : ps) {
                        if (printService.getName().equals("Your printer name")) {
                            myService = printService;

                            break;
                        }
                    }
                    if (myService == null) {
                        throw new IllegalStateException("Printer not found");
                    }
                    try (FileInputStream fis = new FileInputStream("C:\\MPIS\\daytranx\\" + this.oldPapNoLRenew.getText() + forRp.getBranchCode(this.branchSearchRenew.getSelectedItem().toString()) + ".pdf")) {
                        Doc pdfDoc = new SimpleDoc(fis, DocFlavor.INPUT_STREAM.AUTOSENSE, null);
                        DocPrintJob printJob = myService.createPrintJob();

                        printJob.print(pdfDoc, new HashPrintRequestAttributeSet());

                    } catch (PrintException ex) {
                        Logger.getLogger(Redemption.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
                catch (FileNotFoundException ex) {
                    con.saveProp("mpis_last_error", String.valueOf(ex));
                    Logger.getLogger(ReprintTransfer.class.getName()).log(Level.SEVERE, (String) null, ex);

                    con.saveProp("mpis_last_error", String.valueOf(ex));
                }
                catch (java.io.IOException ex) {
                    con.saveProp("mpis_last_error", String.valueOf(ex));
                    Logger.getLogger(ReprintTransfer.class.getName()).log(Level.SEVERE, (String) null, ex);
                }
            } else {

                PrintExcel px = new PrintExcel();
                String destination = "C:\\MPIS\\daytranx\\" + this.oldPapNoLRenew.getText() + forRp.getBranchCode(this.branchSearchRenew.getSelectedItem().toString()) + ".xls";
                px.printFile(new File(destination));
            }
        }
    }//GEN-LAST:event_reprintCompShtActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JTextArea addRet;
    private javax.swing.JTextField affLoss;
    private javax.swing.JLabel amountDueLabel;
    private javax.swing.JComboBox<String> branchRet;
    private javax.swing.JComboBox<String> branchSearchRenew;
    private javax.swing.JButton cancelBtn;
    private javax.swing.JComboBox<String> classificationRet;
    private javax.swing.JTextField conNumRet;
    private javax.swing.JTextArea descRet;
    private org.jdesktop.swingx.JXDatePicker expDateRet;
    private javax.swing.JButton finalizeRedemptionBtn;
    private javax.swing.JButton finalizeUpdateBtn;
    private javax.swing.JTextField insuranceRet;
    private javax.swing.JLabel intLabel;
    private javax.swing.JSlider intRateSlider;
    private javax.swing.JTextField interestRdm;
    private javax.swing.JTextField interestRet;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel23;
    private javax.swing.JLabel jLabel24;
    private javax.swing.JLabel jLabel37;
    private javax.swing.JLabel jLabel38;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel41;
    private javax.swing.JLabel jLabel42;
    private javax.swing.JLabel jLabel43;
    private javax.swing.JLabel jLabel44;
    private javax.swing.JLabel jLabel45;
    private javax.swing.JLabel jLabel46;
    private javax.swing.JLabel jLabel47;
    private javax.swing.JLabel jLabel48;
    private javax.swing.JLabel jLabel49;
    private javax.swing.JLabel jLabel50;
    private javax.swing.JLabel jLabel51;
    private javax.swing.JLabel jLabel52;
    private javax.swing.JLabel jLabel53;
    private javax.swing.JLabel jLabel54;
    private javax.swing.JLabel jLabel55;
    private javax.swing.JLabel jLabel56;
    private javax.swing.JLabel jLabel57;
    private javax.swing.JLabel jLabel58;
    private javax.swing.JLabel jLabel59;
    private javax.swing.JLabel jLabel64;
    private javax.swing.JLabel jLabel65;
    private javax.swing.JLabel jLabel66;
    private javax.swing.JLabel jLabel67;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JPanel jPanel11;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JPanel jPanel7;
    private javax.swing.JPanel jPanel8;
    private javax.swing.JPanel jPanel9;
    private javax.swing.JScrollPane jScrollPane4;
    private javax.swing.JScrollPane jScrollPane5;
    private javax.swing.JScrollPane jScrollPane6;
    private javax.swing.JScrollPane jScrollPane7;
    private javax.swing.JSeparator jSeparator2;
    private javax.swing.JToggleButton linkToggleButton;
    private javax.swing.JTextField liqDam;
    private org.jdesktop.swingx.JXDatePicker matDateRet;
    private javax.swing.JCheckBox motorInCheckbox;
    private javax.swing.JTextField nameRet;
    private javax.swing.JTextField netProRet;
    private javax.swing.JSpinner numOfMonths;
    private javax.swing.JTextField oldPapNoLRenew;
    private javax.swing.JTextField othChgRet;
    private javax.swing.JTextField papNumRet;
    private javax.swing.JTextField partialPaymentBox;
    private javax.swing.JCheckBox partialPaymentCheckbox;
    private javax.swing.JTextField principalRdm;
    private javax.swing.JTextField principalRet;
    private javax.swing.JTextField procByRet;
    private javax.swing.JButton redeemThisItem;
    private javax.swing.JTextArea remarksRet;
    private javax.swing.JButton reprintCompSht;
    private javax.swing.JButton retrieveInfo;
    private javax.swing.JTextField stamp;
    private javax.swing.JComboBox<String> statusRenew;
    private javax.swing.JTextField svcChgRet;
    private javax.swing.JTextField totalOutBalRdm;
    private org.jdesktop.swingx.JXDatePicker transDateRet;
    private javax.swing.JTextField unpaidAI;
    private javax.swing.JCheckBox unpaidAiCheckbox;
    private javax.swing.JButton updateStatus;
    // End of variables declaration//GEN-END:variables
}
