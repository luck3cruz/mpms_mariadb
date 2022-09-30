/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.merlin;

import java.awt.event.ActionEvent;
import java.awt.event.FocusEvent;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
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
import javax.swing.JSlider;
import javax.swing.JSpinner;
import javax.swing.JTextField;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperPrintManager;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.design.JasperDesign;
import net.sf.jasperreports.engine.xml.JRXmlLoader;

/**
 *
 * @author Lucky
 */
public class Renewal extends javax.swing.JPanel {

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

    private Config con = new Config();
    private final String driver = "jdbc:mariadb://" + this.con.getProp("IP") + ":" + this.con.getProp("port") 
            + "/merlininventorydatabase";;
    private final String f_user = con.getProp("username");
    private final String f_pass = con.getProp("password");
    private Sangla oldSangla = new Sangla();
    private DecimalHelper decHelp = new DecimalHelper();
    private DateHelper dateHelp = new DateHelper();
    private Computation calculate = new Computation();
    private SimpleDateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd");
    private String[] oldConstants = new String[5];
    private String exp_date = "";
    private ReportPrinter pr = new ReportPrinter();
    
    /**
     * Creates new form Renewal
     */
    public Renewal() {
        
        initComponents();
    }
    
    public void updateCombo(JComboBox<String> combo) {
        try {
            combo.removeAllItems();
            Connection connect = DriverManager.getConnection(this.driver, this.f_user, this.f_pass);
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
        updateCombo(this.branchSearchRenew);
     updateCombo(this.branchRet);
    
    }
    
    public void requestFocusInSearch() {
        this.oldPapNoLRenew.requestFocusInWindow();
     this.oldPapNoLRenew.selectAll();
    }
    
    public void clearFieldsInLoanRenewal() {
        this.papNumRet.setText("");
        this.newPapNumRet.setText("");
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
        this.principalRet.setText("0.00");
        this.interestRet.setText("0.00");
        this.svcChgRet.setText("0.00");
        this.othChgRet.setText("0.00");
        this.netProRet.setText("0.00");
        this.principalRnw.setText("0.00");
        this.intRateSlider.setValue(0);
        this.numOfMonths.setValue(0);
        this.insuranceRet.setText("");
        this.svcChgRnw.setText("0.00");
        this.affLoss.setText("0.00");
        this.affLoss.setText("0.00");
        this.liqDam.setText("0.00");
        this.unpaidAI.setText("0.00");
        this.aiRateSlider.setValue(0);
        this.advIntRnw.setText("0.00");
        this.netDecRnw.setText("0.00");
        this.netIncRnw.setText("0.00");
        this.netProRnw.setText("0.00");
        this.insuFeeRnw.setText("0.00");
        this.partialPaymentBox.setText("0.00");
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
        this.intRateSlider.setEnabled(status);
        this.numOfMonths.setEnabled(status);
        this.insuFeeRnw.setEditable(status);
        this.netIncRnw.setEditable(status);
        this.netDecRnw.setEditable(status);
        this.svcChgRnw.setEditable(status);
        this.affLoss.setEditable(status);
        this.affLoss.setEditable(status);
        this.partialPaymentBox.setEditable(status);
        this.unpaidAI.setEditable(status);
        this.liqDam.setEditable(status);
        this.statusRenew.setEnabled(status);
        this.stamp.setEditable(status);
        this.advIntRnw.setEditable(status);
        this.newPapNumRet.setEditable(status);
        this.transDateRet.setEditable(status);
        this.matDateRet.setEditable(status);
        this.expDateRet.setEditable(status);
        interestRnw.setEditable(status);
        unpaidAiCheckbox.setEnabled(status);
        partialPaymentCheckbox.setEnabled(status);
    }

    private void printReport(String filePath, String item, String destination, Date trans) {
        try {
       OutputStream output = null;
       Connection conn = DriverManager.getConnection(this.driver, this.f_user, this.f_pass);
         try {
         JasperDesign jasperDesign = JRXmlLoader.load(getClass().getResourceAsStream(filePath));
         Map<String, Object> parameters = new HashMap<>();
         parameters.put("ITEM_CODE", item);
         parameters.put("SC", Double.valueOf(extractAmount(svcChgRnw)));
         parameters.put("AI", Double.valueOf(extractAmount(advIntRnw)));
         parameters.put("NO_OF_MONTHS", this.numOfMonths.getValue());
         parameters.put("TRANX_DATE", trans);
         parameters.put("PARTIAL", Double.valueOf(extractAmount(partialPaymentBox)));

         parameters.put("CODE", "R");
         parameters.put("STATUS", "Renewed");
         parameters.put("INSURANCE", Double.valueOf(extractAmount(insuFeeRnw)));

         if (extractAmount(netDecRnw) > 0.0D) {
           parameters.put("BC", Double.valueOf(extractAmount(netDecRnw) * -1.0D));
           parameters.put("CAP_STAT", "Bawas");
         } else if (extractAmount(netIncRnw) > 0.0D) {
           parameters.put("BC", Double.valueOf(extractAmount(netIncRnw)));
           parameters.put("CAP_STAT", "Dagdag");
            } else {
           parameters.put("BC", Double.valueOf(0.0D));
           parameters.put("CAP_STAT", "");
           }
         parameters.put("BC", Double.valueOf(extractAmount(netDecRnw)));
         JasperReport jasperReport = JasperCompileManager.compileReport(jasperDesign);
         JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parameters, conn);
         JasperPrintManager.printReport(jasperPrint, false);
         output = new FileOutputStream(new File(destination));
         JasperExportManager.exportReportToPdfStream(jasperPrint, output);
       } catch (JRException ex) {
         this.con.saveProp("mpis_last_error", String.valueOf(ex));
         JOptionPane.showMessageDialog(null, "An error occured while printing your Computation Sheet \n " + ex, "Computation Sheet", 0);
       } catch (FileNotFoundException ex) {
         this.con.saveProp("mpis_last_error", String.valueOf(ex));
         Logger.getLogger(TransferBreakdown.class.getName()).log(Level.SEVERE, (String)null, ex);
         JOptionPane.showMessageDialog(null, "An error occured while printing your Computation Sheet \n " + ex, "Computation Sheet", 0);
         }
     } catch (SQLException ex) {
       this.con.saveProp("mpis_last_error", String.valueOf(ex));
       JOptionPane.showMessageDialog(null, "An error occured while printing your Computation Sheet \n " + ex, "Computation Sheet", 0);
       Logger.getLogger(TransferBreakdown.class.getName()).log(Level.SEVERE, (String)null, ex);
        }
    }

    

    public void displayRetrievedInfo() {
     System.out.println("success?" + this.oldSangla.isRetrievalSuccessful());
     if (this.oldSangla.isProceedRetrieval()) {
       this.papNumRet.setText(this.oldSangla.getPap_num());
       this.nameRet.setText(this.oldSangla.getClient_name());
       this.addRet.setText(this.oldSangla.getClient_address());
       this.branchRet.setSelectedItem(this.oldSangla.getBranch());
       this.procByRet.setText(this.oldSangla.getProcessed_by());
       this.descRet.setText(this.oldSangla.getItem_description());
       this.classificationRet.setSelectedItem(this.oldSangla.getClassification());
         System.out.println("motor-in " + oldSangla.getMotorIn());
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
         Logger.getLogger(Renewal.class.getName()).log(Level.SEVERE, (String)null, ex);
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

     this.procByRet.setText("");
     this.descRet.setText("");
     this.classificationRet.setSelectedIndex(1);

     this.transDateRet.setDate(null);
     this.matDateRet.setDate(null);
     this.expDateRet.setDate(null);
     this.statusRenew.setSelectedItem("Open");
     this.principalRet.setText("0.00");
     this.interestRet.setText("0.00");
     this.insuranceRet.setText("0.00");
     this.svcChgRet.setText("0.00");
     this.othChgRet.setText("0.00");
     this.netProRet.setText("0.00");
     this.remarksRet.setText("");
    }

    public void renewLoanFromSearch(String papNo, String branch) {
     this.oldPapNoLRenew.setText(papNo);
     this.branchSearchRenew.setSelectedItem(branch);
     retrieveInfoActionPerformed((ActionEvent)null);
    }
    
    private Double extractAmount(JTextField textfld) {
        return Double.parseDouble(textfld.getText().replace(",", ""));
    }
    
    private Double extractValue(JSlider slider) {
        return Double.valueOf(slider.getValue());
    } 
    
    private Integer extractInt(JSpinner spinner) {
        return Integer.parseInt(spinner.getValue().toString());
    }
    
    private String getComputedAmtDue() {
        return calculate.computeAmountDue(extractAmount(principalRnw)
                , extractAmount(interestRnw)
                , extractAmount(advIntRnw)
                , extractAmount(svcChgRnw)
                , extractAmount(stamp) + extractAmount(affLoss) + extractAmount(unpaidAI) + extractAmount(liqDam)
                , extractAmount(insuFeeRnw)
                , extractAmount(partialPaymentBox));
    }
    
    private String getComputedAmtDueInc() {
        return calculate.computeNetDueOrProceeds(extractAmount(principalRnw)
                ,extractAmount(interestRnw)
                , extractAmount(netIncRnw)
                , extractAmount(advIntRnw)
                , extractAmount(svcChgRnw)
                , extractAmount(stamp) + extractAmount(affLoss) + extractAmount(unpaidAI) + extractAmount(liqDam)
                , extractAmount(insuFeeRnw)
                , extractAmount(partialPaymentBox));
    }
    
    private String getComputedAmtDueDec() {
        return calculate.computeAmountDueWithNetDecrease(extractAmount(principalRnw)
                , extractAmount(interestRnw)
                , extractAmount(netDecRnw)
                , extractAmount(advIntRnw)
                , extractAmount(svcChgRnw)
                , extractAmount(stamp) + extractAmount(affLoss) + extractAmount(unpaidAI) + extractAmount(liqDam)
                , extractAmount(insuFeeRnw)
                , extractAmount(partialPaymentBox));
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
        jPanel8 = new javax.swing.JPanel();
        jPanel1 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        oldPapNoLRenew = new javax.swing.JTextField();
        jLabel3 = new javax.swing.JLabel();
        branchSearchRenew = new javax.swing.JComboBox<>();
        retrieveInfo = new javax.swing.JButton();
        jPanel3 = new javax.swing.JPanel();
        jLabel12 = new javax.swing.JLabel();
        principalRnw = new javax.swing.JTextField();
        jLabel30 = new javax.swing.JLabel();
        jLabel35 = new javax.swing.JLabel();
        jLabel36 = new javax.swing.JLabel();
        jLabel37 = new javax.swing.JLabel();
        svcChgRnw = new javax.swing.JTextField();
        netIncRnw = new javax.swing.JTextField();
        netDecRnw = new javax.swing.JTextField();
        insuFeeRnw = new javax.swing.JTextField();
        amountDueLabel = new javax.swing.JLabel();
        jSeparator1 = new javax.swing.JSeparator();
        netProRnw = new javax.swing.JTextField();
        cancelButton = new javax.swing.JButton();
        finalizeButton = new javax.swing.JButton();
        jLabel7 = new javax.swing.JLabel();
        newPapNumRet = new javax.swing.JTextField();
        jPanel4 = new javax.swing.JPanel();
        jLabel22 = new javax.swing.JLabel();
        jLabel29 = new javax.swing.JLabel();
        jLabel21 = new javax.swing.JLabel();
        interestRnw = new javax.swing.JTextField();
        intRateSlider = new javax.swing.JSlider();
        numOfMonths = new javax.swing.JSpinner();
        intLabel = new javax.swing.JLabel();
        jPanel5 = new javax.swing.JPanel();
        stamp = new javax.swing.JTextField();
        jLabel33 = new javax.swing.JLabel();
        affLoss = new javax.swing.JTextField();
        liqDam = new javax.swing.JTextField();
        jLabel32 = new javax.swing.JLabel();
        jLabel31 = new javax.swing.JLabel();
        jPanel6 = new javax.swing.JPanel();
        jLabel41 = new javax.swing.JLabel();
        jLabel38 = new javax.swing.JLabel();
        advIntRnw = new javax.swing.JTextField();
        aiRateSlider = new javax.swing.JSlider();
        aiLable = new javax.swing.JLabel();
        jPanel7 = new javax.swing.JPanel();
        unpaidAiCheckbox = new javax.swing.JCheckBox();
        unpaidAI = new javax.swing.JTextField();
        partialPaymentBox = new javax.swing.JTextField();
        partialPaymentCheckbox = new javax.swing.JCheckBox();
        jLabel4 = new javax.swing.JLabel();
        jPanel2 = new javax.swing.JPanel();
        jLabel6 = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        jLabel9 = new javax.swing.JLabel();
        jLabel10 = new javax.swing.JLabel();
        jLabel11 = new javax.swing.JLabel();
        jLabel13 = new javax.swing.JLabel();
        jLabel14 = new javax.swing.JLabel();
        jLabel15 = new javax.swing.JLabel();
        jLabel16 = new javax.swing.JLabel();
        jLabel17 = new javax.swing.JLabel();
        jLabel18 = new javax.swing.JLabel();
        jLabel19 = new javax.swing.JLabel();
        jLabel20 = new javax.swing.JLabel();
        reprintCompSht = new javax.swing.JButton();
        renewThisItem = new javax.swing.JButton();
        undoChangesRnw = new javax.swing.JButton();
        papNumRet = new javax.swing.JTextField();
        nameRet = new javax.swing.JTextField();
        conNumRet = new javax.swing.JTextField();
        procByRet = new javax.swing.JTextField();
        svcChgRet = new javax.swing.JTextField();
        interestRet = new javax.swing.JTextField();
        principalRet = new javax.swing.JTextField();
        jScrollPane1 = new javax.swing.JScrollPane();
        descRet = new javax.swing.JTextArea();
        jScrollPane2 = new javax.swing.JScrollPane();
        addRet = new javax.swing.JTextArea();
        branchRet = new javax.swing.JComboBox<>();
        statusRenew = new javax.swing.JComboBox<>();
        classificationRet = new javax.swing.JComboBox<>();
        jLabel23 = new javax.swing.JLabel();
        jScrollPane3 = new javax.swing.JScrollPane();
        remarksRet = new javax.swing.JTextArea();
        jLabel24 = new javax.swing.JLabel();
        jLabel25 = new javax.swing.JLabel();
        jLabel26 = new javax.swing.JLabel();
        jLabel27 = new javax.swing.JLabel();
        jLabel28 = new javax.swing.JLabel();
        netProRet = new javax.swing.JTextField();
        othChgRet = new javax.swing.JTextField();
        insuranceRet = new javax.swing.JTextField();
        transDateRet = new org.jdesktop.swingx.JXDatePicker();
        expDateRet = new org.jdesktop.swingx.JXDatePicker();
        matDateRet = new org.jdesktop.swingx.JXDatePicker();
        motorInCheckbox = new javax.swing.JCheckBox();

        setPreferredSize(new java.awt.Dimension(840, 768));
        setLayout(new javax.swing.BoxLayout(this, javax.swing.BoxLayout.LINE_AXIS));

        jPanel1.setBackground(new java.awt.Color(255, 255, 255));
        jPanel1.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(255, 255, 255), 1, true));

        jLabel1.setFont(new java.awt.Font("Segoe UI Semibold", 0, 13)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(51, 51, 51));
        jLabel1.setText("Enter Papeleta No. and Branch of Item to be renewed.");

        jLabel2.setForeground(new java.awt.Color(102, 102, 102));
        jLabel2.setText("Papeleta No.:");
        jLabel2.setVerticalAlignment(javax.swing.SwingConstants.BOTTOM);

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

        jLabel3.setForeground(new java.awt.Color(102, 102, 102));
        jLabel3.setText("Branch:");
        jLabel3.setVerticalAlignment(javax.swing.SwingConstants.BOTTOM);

        branchSearchRenew.setFont(new java.awt.Font("Segoe UI Semibold", 0, 13)); // NOI18N
        branchSearchRenew.setForeground(new java.awt.Color(51, 51, 51));
        branchSearchRenew.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        branchSearchRenew.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                branchSearchRenewActionPerformed(evt);
            }
        });

        retrieveInfo.setBackground(new java.awt.Color(79, 119, 141));
        retrieveInfo.setFont(new java.awt.Font("Segoe UI Semibold", 0, 13)); // NOI18N
        retrieveInfo.setForeground(new java.awt.Color(255, 255, 255));
        retrieveInfo.setText("Retrieve Info");
        retrieveInfo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                retrieveInfoActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(oldPapNoLRenew)
                            .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 98, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel3)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addComponent(branchSearchRenew, 0, 143, Short.MAX_VALUE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(retrieveInfo, javax.swing.GroupLayout.DEFAULT_SIZE, 135, Short.MAX_VALUE)))
                        .addGap(18, 18, 18))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jLabel1)
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(20, 20, 20)
                .addComponent(jLabel1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel2)
                    .addComponent(jLabel3))
                .addGap(0, 0, 0)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(oldPapNoLRenew, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(branchSearchRenew, javax.swing.GroupLayout.DEFAULT_SIZE, 30, Short.MAX_VALUE)
                    .addComponent(retrieveInfo, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(17, Short.MAX_VALUE))
        );

        jPanel1Layout.linkSize(javax.swing.SwingConstants.VERTICAL, new java.awt.Component[] {branchSearchRenew, oldPapNoLRenew, retrieveInfo});

        jPanel3.setBackground(new java.awt.Color(255, 255, 255));
        jPanel3.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(255, 255, 255), 1, true));

        jLabel12.setForeground(new java.awt.Color(92, 92, 92));
        jLabel12.setText("Principal:");
        jLabel12.setVerticalAlignment(javax.swing.SwingConstants.BOTTOM);

        principalRnw.setEditable(false);
        principalRnw.setFont(new java.awt.Font("Segoe UI Semibold", 0, 13)); // NOI18N
        principalRnw.setForeground(new java.awt.Color(51, 51, 51));
        principalRnw.setHorizontalAlignment(javax.swing.JTextField.TRAILING);
        principalRnw.setText("0.00");
        principalRnw.setToolTipText("");
        principalRnw.setMargin(new java.awt.Insets(2, 20, 2, 20));
        principalRnw.setMinimumSize(new java.awt.Dimension(36, 20));
        principalRnw.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                principalRnwKeyTyped(evt);
            }
        });

        jLabel30.setForeground(new java.awt.Color(92, 92, 92));
        jLabel30.setText("Service Charge:");
        jLabel30.setVerticalAlignment(javax.swing.SwingConstants.BOTTOM);

        jLabel35.setForeground(new java.awt.Color(92, 92, 92));
        jLabel35.setText("Net Increase");
        jLabel35.setVerticalAlignment(javax.swing.SwingConstants.BOTTOM);

        jLabel36.setForeground(new java.awt.Color(92, 92, 92));
        jLabel36.setText("Net Decrease");
        jLabel36.setVerticalAlignment(javax.swing.SwingConstants.BOTTOM);

        jLabel37.setForeground(new java.awt.Color(92, 92, 92));
        jLabel37.setText("Insurance Fee:");
        jLabel37.setVerticalAlignment(javax.swing.SwingConstants.BOTTOM);

        svcChgRnw.setEditable(false);
        svcChgRnw.setFont(new java.awt.Font("Segoe UI Semibold", 0, 13)); // NOI18N
        svcChgRnw.setForeground(new java.awt.Color(51, 51, 51));
        svcChgRnw.setHorizontalAlignment(javax.swing.JTextField.TRAILING);
        svcChgRnw.setText("0.00");
        svcChgRnw.setToolTipText("");
        svcChgRnw.setMargin(new java.awt.Insets(2, 20, 2, 20));
        svcChgRnw.setPreferredSize(new java.awt.Dimension(67, 30));
        svcChgRnw.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                svcChgRnwFocusGained(evt);
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                svcChgRnwFocusLost(evt);
            }
        });
        svcChgRnw.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                svcChgRnwActionPerformed(evt);
            }
        });
        svcChgRnw.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                svcChgRnwKeyPressed(evt);
            }
            public void keyTyped(java.awt.event.KeyEvent evt) {
                svcChgRnwKeyTyped(evt);
            }
        });

        netIncRnw.setEditable(false);
        netIncRnw.setFont(new java.awt.Font("Segoe UI Semibold", 0, 13)); // NOI18N
        netIncRnw.setForeground(new java.awt.Color(51, 51, 51));
        netIncRnw.setHorizontalAlignment(javax.swing.JTextField.TRAILING);
        netIncRnw.setText("0.00");
        netIncRnw.setToolTipText("");
        netIncRnw.setMargin(new java.awt.Insets(2, 20, 2, 20));
        netIncRnw.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                netIncRnwFocusGained(evt);
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                netIncRnwFocusLost(evt);
            }
        });
        netIncRnw.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                netIncRnwKeyPressed(evt);
            }
            public void keyTyped(java.awt.event.KeyEvent evt) {
                netIncRnwKeyTyped(evt);
            }
        });

        netDecRnw.setEditable(false);
        netDecRnw.setFont(new java.awt.Font("Segoe UI Semibold", 0, 13)); // NOI18N
        netDecRnw.setForeground(new java.awt.Color(51, 51, 51));
        netDecRnw.setHorizontalAlignment(javax.swing.JTextField.TRAILING);
        netDecRnw.setText("0.00");
        netDecRnw.setToolTipText("");
        netDecRnw.setMargin(new java.awt.Insets(2, 20, 2, 20));
        netDecRnw.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                netDecRnwFocusGained(evt);
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                netDecRnwFocusLost(evt);
            }
        });
        netDecRnw.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                netDecRnwKeyPressed(evt);
            }
            public void keyTyped(java.awt.event.KeyEvent evt) {
                netDecRnwKeyTyped(evt);
            }
        });

        insuFeeRnw.setEditable(false);
        insuFeeRnw.setFont(new java.awt.Font("Segoe UI Semibold", 0, 13)); // NOI18N
        insuFeeRnw.setForeground(new java.awt.Color(51, 51, 51));
        insuFeeRnw.setHorizontalAlignment(javax.swing.JTextField.TRAILING);
        insuFeeRnw.setText("0.00");
        insuFeeRnw.setToolTipText("");
        insuFeeRnw.setMargin(new java.awt.Insets(2, 20, 2, 20));
        insuFeeRnw.setPreferredSize(new java.awt.Dimension(67, 30));
        insuFeeRnw.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                insuFeeRnwFocusGained(evt);
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                insuFeeRnwFocusLost(evt);
            }
        });
        insuFeeRnw.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                insuFeeRnwKeyPressed(evt);
            }
            public void keyTyped(java.awt.event.KeyEvent evt) {
                insuFeeRnwKeyTyped(evt);
            }
        });

        amountDueLabel.setFont(new java.awt.Font("Segoe UI Semibold", 1, 13)); // NOI18N
        amountDueLabel.setForeground(new java.awt.Color(92, 92, 92));
        amountDueLabel.setText("Total Amount Due");
        amountDueLabel.setVerticalAlignment(javax.swing.SwingConstants.BOTTOM);

        jSeparator1.setForeground(new java.awt.Color(102, 102, 102));

        netProRnw.setEditable(false);
        netProRnw.setBackground(new java.awt.Color(119, 179, 212));
        netProRnw.setFont(new java.awt.Font("Segoe UI Semibold", 0, 14)); // NOI18N
        netProRnw.setForeground(new java.awt.Color(255, 255, 255));
        netProRnw.setHorizontalAlignment(javax.swing.JTextField.TRAILING);
        netProRnw.setText("0.00");
        netProRnw.setMargin(new java.awt.Insets(2, 20, 2, 20));

        cancelButton.setFont(new java.awt.Font("Segoe UI Semibold", 0, 13)); // NOI18N
        cancelButton.setForeground(new java.awt.Color(79, 119, 141));
        cancelButton.setText("Cancel");
        cancelButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cancelButtonActionPerformed(evt);
            }
        });

        finalizeButton.setBackground(new java.awt.Color(79, 119, 141));
        finalizeButton.setFont(new java.awt.Font("Segoe UI Semibold", 0, 13)); // NOI18N
        finalizeButton.setForeground(new java.awt.Color(255, 255, 255));
        finalizeButton.setText("Finalize");
        finalizeButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                finalizeButtonActionPerformed(evt);
            }
        });

        jLabel7.setForeground(new java.awt.Color(102, 102, 102));
        jLabel7.setText("New Papeleta No.:");
        jLabel7.setVerticalAlignment(javax.swing.SwingConstants.BOTTOM);

        newPapNumRet.setEditable(false);
        newPapNumRet.setFont(new java.awt.Font("Segoe UI Semibold", 0, 13)); // NOI18N
        newPapNumRet.setMargin(new java.awt.Insets(2, 20, 2, 20));
        newPapNumRet.setPreferredSize(new java.awt.Dimension(64, 30));
        newPapNumRet.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                newPapNumRetFocusLost(evt);
            }
        });
        newPapNumRet.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                newPapNumRetActionPerformed(evt);
            }
        });
        newPapNumRet.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                newPapNumRetKeyPressed(evt);
            }
        });

        jPanel4.setBackground(new java.awt.Color(227, 240, 251));
        jPanel4.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(227, 240, 251), 1, true));

        jLabel22.setForeground(new java.awt.Color(92, 92, 92));
        jLabel22.setText("Months");
        jLabel22.setVerticalAlignment(javax.swing.SwingConstants.BOTTOM);

        jLabel29.setForeground(new java.awt.Color(92, 92, 92));
        jLabel29.setText("Interest");
        jLabel29.setVerticalAlignment(javax.swing.SwingConstants.BOTTOM);

        jLabel21.setForeground(new java.awt.Color(92, 92, 92));
        jLabel21.setText("Int. Rate (in %)");
        jLabel21.setVerticalAlignment(javax.swing.SwingConstants.BOTTOM);

        interestRnw.setEditable(false);
        interestRnw.setFont(new java.awt.Font("Segoe UI Semibold", 0, 13)); // NOI18N
        interestRnw.setForeground(new java.awt.Color(51, 51, 51));
        interestRnw.setHorizontalAlignment(javax.swing.JTextField.TRAILING);
        interestRnw.setText("0.00");
        interestRnw.setToolTipText("");
        interestRnw.setMargin(new java.awt.Insets(2, 20, 2, 20));
        interestRnw.setPreferredSize(new java.awt.Dimension(64, 115));
        interestRnw.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                interestRnwFocusGained(evt);
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                interestRnwFocusLost(evt);
            }
        });
        interestRnw.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                interestRnwActionPerformed(evt);
            }
        });
        interestRnw.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                interestRnwKeyPressed(evt);
            }
            public void keyTyped(java.awt.event.KeyEvent evt) {
                interestRnwKeyTyped(evt);
            }
        });

        intRateSlider.setMaximum(15);
        intRateSlider.setMinorTickSpacing(1);
        intRateSlider.setPaintLabels(true);
        intRateSlider.setPaintTicks(true);
        intRateSlider.setSnapToTicks(true);
        intRateSlider.setToolTipText("Interest Rate");
        intRateSlider.setValue(5);
        intRateSlider.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                intRateSliderStateChanged(evt);
            }
        });
        intRateSlider.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                intRateSliderMouseClicked(evt);
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

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jLabel29, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(interestRnw, javax.swing.GroupLayout.DEFAULT_SIZE, 110, Short.MAX_VALUE))
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addGap(8, 8, 8)
                        .addComponent(intRateSlider, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel4Layout.createSequentialGroup()
                        .addGap(14, 14, 14)
                        .addComponent(jLabel21, javax.swing.GroupLayout.PREFERRED_SIZE, 144, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(intLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel22)
                    .addComponent(numOfMonths, javax.swing.GroupLayout.PREFERRED_SIZE, 52, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addGap(10, 10, 10)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabel21)
                        .addComponent(jLabel22))
                    .addComponent(jLabel29, javax.swing.GroupLayout.Alignment.TRAILING))
                .addGap(1, 1, 1)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(interestRnw, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(intRateSlider, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 31, Short.MAX_VALUE)
                    .addComponent(intLabel, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(numOfMonths))
                .addContainerGap(13, Short.MAX_VALUE))
        );

        jPanel4Layout.linkSize(javax.swing.SwingConstants.VERTICAL, new java.awt.Component[] {intLabel, intRateSlider, interestRnw, numOfMonths});

        jPanel5.setBackground(new java.awt.Color(227, 240, 251));
        jPanel5.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(227, 240, 251), 1, true));

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

        jLabel33.setForeground(new java.awt.Color(92, 92, 92));
        jLabel33.setText("Affidavit of Loss:");
        jLabel33.setVerticalAlignment(javax.swing.SwingConstants.BOTTOM);

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

        jLabel32.setForeground(new java.awt.Color(92, 92, 92));
        jLabel32.setText("Liquidated Damages:");
        jLabel32.setVerticalAlignment(javax.swing.SwingConstants.BOTTOM);

        jLabel31.setForeground(new java.awt.Color(92, 92, 92));
        jLabel31.setText("Stamp/Notarial Fee:");
        jLabel31.setVerticalAlignment(javax.swing.SwingConstants.BOTTOM);

        javax.swing.GroupLayout jPanel5Layout = new javax.swing.GroupLayout(jPanel5);
        jPanel5.setLayout(jPanel5Layout);
        jPanel5Layout.setHorizontalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel31)
                    .addGroup(jPanel5Layout.createSequentialGroup()
                        .addComponent(stamp, javax.swing.GroupLayout.PREFERRED_SIZE, 115, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(13, 13, 13)
                        .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel5Layout.createSequentialGroup()
                                .addComponent(jLabel32)
                                .addGap(18, 18, 18)
                                .addComponent(jLabel33))
                            .addGroup(jPanel5Layout.createSequentialGroup()
                                .addComponent(liqDam, javax.swing.GroupLayout.DEFAULT_SIZE, 110, Short.MAX_VALUE)
                                .addGap(18, 18, 18)
                                .addComponent(affLoss, javax.swing.GroupLayout.PREFERRED_SIZE, 110, javax.swing.GroupLayout.PREFERRED_SIZE)))))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel5Layout.setVerticalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addGap(10, 10, 10)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel31)
                    .addComponent(jLabel32)
                    .addComponent(jLabel33))
                .addGap(1, 1, 1)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(stamp, javax.swing.GroupLayout.DEFAULT_SIZE, 30, Short.MAX_VALUE)
                    .addComponent(affLoss)
                    .addComponent(liqDam))
                .addContainerGap(11, Short.MAX_VALUE))
        );

        jPanel6.setBackground(new java.awt.Color(227, 240, 251));
        jPanel6.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(227, 240, 251), 1, true));

        jLabel41.setForeground(new java.awt.Color(92, 92, 92));
        jLabel41.setText("Adv. Int. Rate (in %)");

        jLabel38.setForeground(new java.awt.Color(92, 92, 92));
        jLabel38.setText("Advance Interest");
        jLabel38.setVerticalAlignment(javax.swing.SwingConstants.BOTTOM);

        advIntRnw.setEditable(false);
        advIntRnw.setFont(new java.awt.Font("Segoe UI Semibold", 0, 13)); // NOI18N
        advIntRnw.setForeground(new java.awt.Color(51, 51, 51));
        advIntRnw.setHorizontalAlignment(javax.swing.JTextField.TRAILING);
        advIntRnw.setText("0.00");
        advIntRnw.setToolTipText("");
        advIntRnw.setMargin(new java.awt.Insets(2, 20, 2, 20));
        advIntRnw.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                advIntRnwFocusGained(evt);
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                advIntRnwFocusLost(evt);
            }
        });
        advIntRnw.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                advIntRnwKeyPressed(evt);
            }
            public void keyTyped(java.awt.event.KeyEvent evt) {
                advIntRnwKeyTyped(evt);
            }
        });

        aiRateSlider.setMaximum(15);
        aiRateSlider.setMinorTickSpacing(1);
        aiRateSlider.setPaintLabels(true);
        aiRateSlider.setPaintTicks(true);
        aiRateSlider.setSnapToTicks(true);
        aiRateSlider.setValue(5);
        aiRateSlider.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                aiRateSliderStateChanged(evt);
            }
        });
        aiRateSlider.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                aiRateSliderMouseClicked(evt);
            }
        });

        aiLable.setFont(new java.awt.Font("Segoe UI Semibold", 0, 13)); // NOI18N
        aiLable.setForeground(new java.awt.Color(51, 51, 51));
        aiLable.setText("0%");

        javax.swing.GroupLayout jPanel6Layout = new javax.swing.GroupLayout(jPanel6);
        jPanel6.setLayout(jPanel6Layout);
        jPanel6Layout.setHorizontalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel38)
                    .addComponent(advIntRnw, javax.swing.GroupLayout.PREFERRED_SIZE, 110, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel41)
                    .addGroup(jPanel6Layout.createSequentialGroup()
                        .addComponent(aiRateSlider, javax.swing.GroupLayout.PREFERRED_SIZE, 177, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(aiLable, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel6Layout.setVerticalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addGap(10, 10, 10)
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel41)
                    .addComponent(jLabel38))
                .addGap(1, 1, 1)
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.CENTER)
                    .addComponent(aiRateSlider, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(aiLable, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(advIntRnw, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(15, 15, 15))
        );

        jPanel7.setBackground(new java.awt.Color(227, 240, 251));
        jPanel7.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(227, 240, 251), 1, true));

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

        javax.swing.GroupLayout jPanel7Layout = new javax.swing.GroupLayout(jPanel7);
        jPanel7.setLayout(jPanel7Layout);
        jPanel7Layout.setHorizontalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel7Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(unpaidAiCheckbox)
                    .addComponent(partialPaymentCheckbox))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(partialPaymentBox, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(unpaidAI, javax.swing.GroupLayout.PREFERRED_SIZE, 142, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel7Layout.setVerticalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel7Layout.createSequentialGroup()
                .addGap(10, 10, 10)
                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(partialPaymentCheckbox, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(partialPaymentBox, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(unpaidAiCheckbox, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(unpaidAI, javax.swing.GroupLayout.DEFAULT_SIZE, 30, Short.MAX_VALUE))
                .addGap(6, 6, 6))
        );

        jLabel4.setFont(new java.awt.Font("Segoe UI Semibold", 1, 18)); // NOI18N
        jLabel4.setForeground(new java.awt.Color(102, 102, 102));
        jLabel4.setText("Loan Renewal Computation");

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addGap(9, 9, 9)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                            .addComponent(jLabel12, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(principalRnw, javax.swing.GroupLayout.DEFAULT_SIZE, 122, Short.MAX_VALUE))
                        .addGap(18, 18, 18)
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(netIncRnw)
                            .addComponent(jLabel35, javax.swing.GroupLayout.DEFAULT_SIZE, 111, Short.MAX_VALUE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel36, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addGroup(jPanel3Layout.createSequentialGroup()
                                .addComponent(netDecRnw, javax.swing.GroupLayout.PREFERRED_SIZE, 110, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(0, 0, Short.MAX_VALUE))))
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jPanel5, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jPanel4, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jLabel7, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jPanel6, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jPanel7, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(amountDueLabel, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jSeparator1, javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel4, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel3Layout.createSequentialGroup()
                                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel3Layout.createSequentialGroup()
                                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                            .addComponent(svcChgRnw, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                            .addComponent(jLabel30, javax.swing.GroupLayout.DEFAULT_SIZE, 116, Short.MAX_VALUE))
                                        .addGap(18, 18, 18)
                                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                            .addComponent(insuFeeRnw, javax.swing.GroupLayout.DEFAULT_SIZE, 120, Short.MAX_VALUE)
                                            .addComponent(jLabel37, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                                    .addComponent(newPapNumRet, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.PREFERRED_SIZE, 122, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(netProRnw, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.PREFERRED_SIZE, 194, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(0, 0, Short.MAX_VALUE)))
                        .addGap(4, 4, 4)))
                .addContainerGap())
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel3Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(finalizeButton, javax.swing.GroupLayout.PREFERRED_SIZE, 139, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(cancelButton, javax.swing.GroupLayout.PREFERRED_SIZE, 144, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(19, 19, 19))
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addGap(33, 33, 33)
                .addComponent(jLabel4)
                .addGap(18, 18, 18)
                .addComponent(jLabel7)
                .addGap(1, 1, 1)
                .addComponent(newPapNumRet, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel12)
                    .addComponent(jLabel35)
                    .addComponent(jLabel36))
                .addGap(1, 1, 1)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(principalRnw, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(netIncRnw, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(netDecRnw, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel30)
                    .addComponent(jLabel37))
                .addGap(1, 1, 1)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(insuFeeRnw, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(svcChgRnw, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jPanel5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jPanel6, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jPanel7, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jSeparator1, javax.swing.GroupLayout.PREFERRED_SIZE, 3, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(amountDueLabel)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(netProRnw, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(50, 50, 50)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(finalizeButton, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(cancelButton, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jPanel3Layout.linkSize(javax.swing.SwingConstants.VERTICAL, new java.awt.Component[] {cancelButton, finalizeButton});

        jPanel3Layout.linkSize(javax.swing.SwingConstants.VERTICAL, new java.awt.Component[] {netDecRnw, netIncRnw, principalRnw});

        jPanel2.setBackground(new java.awt.Color(227, 240, 251));
        jPanel2.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(227, 240, 251), 1, true));

        jLabel6.setForeground(new java.awt.Color(102, 102, 102));
        jLabel6.setText("Papeleta No.:");
        jLabel6.setVerticalAlignment(javax.swing.SwingConstants.BOTTOM);

        jLabel8.setForeground(new java.awt.Color(102, 102, 102));
        jLabel8.setText("Branch:");
        jLabel8.setVerticalAlignment(javax.swing.SwingConstants.BOTTOM);

        jLabel9.setForeground(new java.awt.Color(102, 102, 102));
        jLabel9.setText("Client Name:");
        jLabel9.setVerticalAlignment(javax.swing.SwingConstants.BOTTOM);

        jLabel10.setForeground(new java.awt.Color(102, 102, 102));
        jLabel10.setText("Client Address:");
        jLabel10.setVerticalAlignment(javax.swing.SwingConstants.BOTTOM);

        jLabel11.setForeground(new java.awt.Color(102, 102, 102));
        jLabel11.setText("Contact No.:");
        jLabel11.setVerticalAlignment(javax.swing.SwingConstants.BOTTOM);

        jLabel13.setForeground(new java.awt.Color(102, 102, 102));
        jLabel13.setText("Status:");
        jLabel13.setVerticalAlignment(javax.swing.SwingConstants.BOTTOM);

        jLabel14.setForeground(new java.awt.Color(102, 102, 102));
        jLabel14.setText("Item Description:");
        jLabel14.setVerticalAlignment(javax.swing.SwingConstants.BOTTOM);

        jLabel15.setForeground(new java.awt.Color(102, 102, 102));
        jLabel15.setText("Classification:");
        jLabel15.setVerticalAlignment(javax.swing.SwingConstants.BOTTOM);

        jLabel16.setForeground(new java.awt.Color(102, 102, 102));
        jLabel16.setText("Processed by:");
        jLabel16.setVerticalAlignment(javax.swing.SwingConstants.BOTTOM);

        jLabel17.setForeground(new java.awt.Color(102, 102, 102));
        jLabel17.setText("Loan Date:");
        jLabel17.setVerticalAlignment(javax.swing.SwingConstants.BOTTOM);

        jLabel18.setForeground(new java.awt.Color(102, 102, 102));
        jLabel18.setText("Remarks:");
        jLabel18.setVerticalAlignment(javax.swing.SwingConstants.BOTTOM);

        jLabel19.setForeground(new java.awt.Color(102, 102, 102));
        jLabel19.setText("Principal:");
        jLabel19.setVerticalAlignment(javax.swing.SwingConstants.BOTTOM);

        jLabel20.setForeground(new java.awt.Color(102, 102, 102));
        jLabel20.setText("Expiry Date:");
        jLabel20.setVerticalAlignment(javax.swing.SwingConstants.BOTTOM);

        reprintCompSht.setBackground(new java.awt.Color(79, 119, 141));
        reprintCompSht.setFont(new java.awt.Font("Segoe UI", 1, 13)); // NOI18N
        reprintCompSht.setForeground(new java.awt.Color(255, 255, 255));
        reprintCompSht.setText("Reprint Computation Sheet");
        reprintCompSht.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                reprintCompShtActionPerformed(evt);
            }
        });

        renewThisItem.setBackground(new java.awt.Color(79, 119, 141));
        renewThisItem.setFont(new java.awt.Font("Segoe UI Semibold", 0, 13)); // NOI18N
        renewThisItem.setForeground(new java.awt.Color(255, 255, 255));
        renewThisItem.setText("Renew This Loan");
        renewThisItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                renewThisItemActionPerformed(evt);
            }
        });

        undoChangesRnw.setFont(new java.awt.Font("Segoe UI Semibold", 0, 13)); // NOI18N
        undoChangesRnw.setForeground(new java.awt.Color(79, 119, 141));
        undoChangesRnw.setText("Undo Changes");
        undoChangesRnw.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                undoChangesRnwActionPerformed(evt);
            }
        });

        papNumRet.setEditable(false);
        papNumRet.setFont(new java.awt.Font("Segoe UI Semibold", 0, 13)); // NOI18N
        papNumRet.setForeground(new java.awt.Color(102, 102, 102));
        papNumRet.setToolTipText("Old Papelete No.");
        papNumRet.setMargin(new java.awt.Insets(2, 20, 2, 20));
        papNumRet.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                papNumRetFocusLost(evt);
            }
        });

        nameRet.setFont(new java.awt.Font("Segoe UI Semibold", 0, 13)); // NOI18N
        nameRet.setForeground(new java.awt.Color(102, 102, 102));
        nameRet.setMargin(new java.awt.Insets(2, 20, 2, 20));

        conNumRet.setFont(new java.awt.Font("Segoe UI Semibold", 0, 13)); // NOI18N
        conNumRet.setForeground(new java.awt.Color(102, 102, 102));
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
        procByRet.setForeground(new java.awt.Color(102, 102, 102));
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
        svcChgRet.setForeground(new java.awt.Color(102, 102, 102));
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
        interestRet.setForeground(new java.awt.Color(102, 102, 102));
        interestRet.setHorizontalAlignment(javax.swing.JTextField.TRAILING);
        interestRet.setMargin(new java.awt.Insets(2, 20, 2, 20));
        interestRet.setMinimumSize(new java.awt.Dimension(36, 20));

        principalRet.setEditable(false);
        principalRet.setFont(new java.awt.Font("Segoe UI Semibold", 0, 13)); // NOI18N
        principalRet.setForeground(new java.awt.Color(102, 102, 102));
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
        descRet.setForeground(new java.awt.Color(102, 102, 102));
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
        jScrollPane1.setViewportView(descRet);

        addRet.setColumns(40);
        addRet.setFont(new java.awt.Font("Segoe UI Semibold", 0, 13)); // NOI18N
        addRet.setForeground(new java.awt.Color(102, 102, 102));
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
        jScrollPane2.setViewportView(addRet);

        branchRet.setFont(new java.awt.Font("Segoe UI Semibold", 0, 13)); // NOI18N
        branchRet.setForeground(new java.awt.Color(102, 102, 102));
        branchRet.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));

        statusRenew.setFont(new java.awt.Font("Segoe UI Semibold", 0, 13)); // NOI18N
        statusRenew.setForeground(new java.awt.Color(102, 102, 102));
        statusRenew.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Open", "Renewed", "Expired", "Closed", "Repossessed", "Sold" }));

        classificationRet.setFont(new java.awt.Font("Segoe UI Semibold", 0, 13)); // NOI18N
        classificationRet.setForeground(new java.awt.Color(102, 102, 102));
        classificationRet.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Appliance", "Gadget", "Jewelry", "Vehicle - 2W", "Vehicle - 4W", "Others" }));
        classificationRet.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                classificationRetActionPerformed(evt);
            }
        });

        jLabel23.setForeground(new java.awt.Color(102, 102, 102));
        jLabel23.setText("Maturity Date:");

        remarksRet.setColumns(20);
        remarksRet.setFont(new java.awt.Font("Segoe UI Semibold", 0, 13)); // NOI18N
        remarksRet.setForeground(new java.awt.Color(102, 102, 102));
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
        jScrollPane3.setViewportView(remarksRet);

        jLabel24.setForeground(new java.awt.Color(102, 102, 102));
        jLabel24.setText("Adv. Interest:");

        jLabel25.setForeground(new java.awt.Color(102, 102, 102));
        jLabel25.setText("Service Charge:");

        jLabel26.setForeground(new java.awt.Color(102, 102, 102));
        jLabel26.setText("Insurance:");

        jLabel27.setForeground(new java.awt.Color(102, 102, 102));
        jLabel27.setText("Other Charges:");

        jLabel28.setForeground(new java.awt.Color(102, 102, 102));
        jLabel28.setText("Net Proceeds:");

        netProRet.setEditable(false);
        netProRet.setFont(new java.awt.Font("Segoe UI Semibold", 0, 13)); // NOI18N
        netProRet.setForeground(new java.awt.Color(102, 102, 102));
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
        othChgRet.setForeground(new java.awt.Color(102, 102, 102));
        othChgRet.setHorizontalAlignment(javax.swing.JTextField.TRAILING);
        othChgRet.setMargin(new java.awt.Insets(2, 20, 2, 20));
        othChgRet.setMinimumSize(new java.awt.Dimension(36, 20));

        insuranceRet.setEditable(false);
        insuranceRet.setFont(new java.awt.Font("Segoe UI Semibold", 0, 13)); // NOI18N
        insuranceRet.setForeground(new java.awt.Color(102, 102, 102));
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

        expDateRet.setForeground(new java.awt.Color(51, 51, 51));
        expDateRet.setToolTipText("YYYY-MM-DD");
        expDateRet.setFormats(dateFormatter);

        matDateRet.setForeground(new java.awt.Color(51, 51, 51));
        matDateRet.setToolTipText("YYYY-MM-DD");
        matDateRet.setFormats(dateFormatter);

        motorInCheckbox.setFont(new java.awt.Font("Segoe UI Semibold", 0, 13)); // NOI18N
        motorInCheckbox.setForeground(new java.awt.Color(102, 102, 102));
        motorInCheckbox.setSelected(true);
        motorInCheckbox.setText("Motor-in");
        motorInCheckbox.setEnabled(false);
        motorInCheckbox.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                motorInCheckboxActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(9, 9, 9)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel2Layout.createSequentialGroup()
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(papNumRet)
                            .addComponent(jLabel6, javax.swing.GroupLayout.DEFAULT_SIZE, 97, Short.MAX_VALUE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel2Layout.createSequentialGroup()
                                .addComponent(jLabel8, javax.swing.GroupLayout.PREFERRED_SIZE, 153, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jLabel13)
                                .addGap(0, 0, Short.MAX_VALUE))
                            .addGroup(jPanel2Layout.createSequentialGroup()
                                .addComponent(branchRet, 0, 147, Short.MAX_VALUE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(statusRenew, 0, 132, Short.MAX_VALUE))))
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel2Layout.createSequentialGroup()
                        .addComponent(nameRet, javax.swing.GroupLayout.PREFERRED_SIZE, 250, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel11, javax.swing.GroupLayout.PREFERRED_SIZE, 115, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(conNumRet, javax.swing.GroupLayout.DEFAULT_SIZE, 138, Short.MAX_VALUE)))
                    .addComponent(reprintCompSht, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jScrollPane3, javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane2, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                    .addComponent(jLabel10, javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel9, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.PREFERRED_SIZE, 230, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel18, javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel14, javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel15, javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel2Layout.createSequentialGroup()
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel26, javax.swing.GroupLayout.DEFAULT_SIZE, 131, Short.MAX_VALUE)
                            .addComponent(jLabel19, javax.swing.GroupLayout.DEFAULT_SIZE, 131, Short.MAX_VALUE)
                            .addComponent(principalRet, javax.swing.GroupLayout.PREFERRED_SIZE, 123, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(insuranceRet, javax.swing.GroupLayout.PREFERRED_SIZE, 123, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(0, 0, 0)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel24, javax.swing.GroupLayout.PREFERRED_SIZE, 113, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel27, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(interestRet, javax.swing.GroupLayout.PREFERRED_SIZE, 123, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(othChgRet, javax.swing.GroupLayout.PREFERRED_SIZE, 123, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jLabel28, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jLabel25, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(netProRet, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(svcChgRet, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel2Layout.createSequentialGroup()
                        .addComponent(renewThisItem, javax.swing.GroupLayout.PREFERRED_SIZE, 193, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(undoChangesRnw, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel2Layout.createSequentialGroup()
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jLabel17, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(classificationRet, 0, 130, Short.MAX_VALUE)
                            .addComponent(transDateRet, javax.swing.GroupLayout.PREFERRED_SIZE, 130, Short.MAX_VALUE))
                        .addGap(18, 18, 18)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addGroup(jPanel2Layout.createSequentialGroup()
                                .addComponent(jLabel23, javax.swing.GroupLayout.PREFERRED_SIZE, 128, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(jLabel20))
                            .addGroup(jPanel2Layout.createSequentialGroup()
                                .addComponent(motorInCheckbox)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel16)
                                    .addComponent(procByRet)))
                            .addGroup(jPanel2Layout.createSequentialGroup()
                                .addComponent(matDateRet, javax.swing.GroupLayout.PREFERRED_SIZE, 120, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(expDateRet, javax.swing.GroupLayout.PREFERRED_SIZE, 120, javax.swing.GroupLayout.PREFERRED_SIZE)))))
                .addGap(9, 9, 9))
        );

        jPanel2Layout.linkSize(javax.swing.SwingConstants.HORIZONTAL, new java.awt.Component[] {jLabel19, jLabel24, jLabel25, jLabel26, jLabel27, jLabel28, netProRet});

        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(20, 20, 20)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel6)
                    .addComponent(jLabel8)
                    .addComponent(jLabel13))
                .addGap(1, 1, 1)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(papNumRet, javax.swing.GroupLayout.DEFAULT_SIZE, 31, Short.MAX_VALUE)
                    .addComponent(branchRet, javax.swing.GroupLayout.DEFAULT_SIZE, 32, Short.MAX_VALUE)
                    .addComponent(statusRenew, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel9)
                    .addComponent(jLabel11))
                .addGap(1, 1, 1)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(conNumRet, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(nameRet, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(13, 13, 13)
                .addComponent(jLabel10)
                .addGap(1, 1, 1)
                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel14)
                .addGap(1, 1, 1)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel15)
                    .addComponent(jLabel16))
                .addGap(1, 1, 1)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(classificationRet, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(motorInCheckbox, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(procByRet, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel17)
                    .addComponent(jLabel23)
                    .addComponent(jLabel20))
                .addGap(1, 1, 1)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(transDateRet, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(matDateRet, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(expDateRet, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jLabel18)
                .addGap(1, 1, 1)
                .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 48, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel19)
                    .addComponent(jLabel24)
                    .addComponent(jLabel25))
                .addGap(1, 1, 1)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(principalRet, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(interestRet, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(svcChgRet, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel26)
                    .addComponent(jLabel27)
                    .addComponent(jLabel28))
                .addGap(1, 1, 1)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(insuranceRet, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(othChgRet, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(netProRet, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(14, 14, 14)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(renewThisItem, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(undoChangesRnw, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(reprintCompSht, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jPanel2Layout.linkSize(javax.swing.SwingConstants.VERTICAL, new java.awt.Component[] {conNumRet, insuranceRet, interestRet, netProRet, othChgRet, papNumRet, principalRet, procByRet, svcChgRet});

        jPanel2Layout.linkSize(javax.swing.SwingConstants.VERTICAL, new java.awt.Component[] {branchRet, nameRet, statusRenew});

        jPanel2Layout.linkSize(javax.swing.SwingConstants.VERTICAL, new java.awt.Component[] {renewThisItem, reprintCompSht});

        javax.swing.GroupLayout jPanel8Layout = new javax.swing.GroupLayout(jPanel8);
        jPanel8.setLayout(jPanel8Layout);
        jPanel8Layout.setHorizontalGroup(
            jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel8Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        jPanel8Layout.setVerticalGroup(
            jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel8Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(jPanel8Layout.createSequentialGroup()
                        .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(13, Short.MAX_VALUE))
        );

        jScrollPane4.setViewportView(jPanel8);

        add(jScrollPane4);
    }// </editor-fold>//GEN-END:initComponents

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

    private void undoChangesRnwActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_undoChangesRnwActionPerformed
        this.finalizeButton.setEnabled(false);
        boolean status = false;
        this.transDateRet.setEditable(false);
        try {
            this.transDateRet.setDate(this.dateFormatter.parse(this.oldSangla.getTransaction_date()));
            this.matDateRet.setDate(this.dateFormatter.parse(this.oldSangla.getMaturity_date()));
            this.expDateRet.setDate(this.dateFormatter.parse(this.oldSangla.getExpiration_date()));
        } catch (ParseException ex) {
            this.con.saveProp("mpis_last_error", String.valueOf(ex));
        }

        this.statusRenew.setSelectedItem(this.oldSangla.getStatus());
        this.newPapNumRet.setText("");
        this.principalRnw.setText("0.00");
        this.intRateSlider.setValue(7);
        this.numOfMonths.setValue(0);
        this.interestRnw.setText("0.00");
        this.svcChgRnw.setText("5.00");
        this.stamp.setText("0.00");
        this.netIncRnw.setText("0.00");
        this.netIncRnw.setEditable(true);
        this.netDecRnw.setText("0.00");
        this.netDecRnw.setEditable(true);
        //     this.withAIRnw.setSelected(false);
        this.advIntRnw.setText("0.00");
        this.advIntRnw.setEditable(false);
        this.aiRateSlider.setValue(0);
        this.aiRateSlider.setEnabled(false);
        this.insuFeeRnw.setText("0.00");
        this.partialPaymentBox.setText("0.00");
        this.partialPaymentBox.setEditable(false);
        retrieveButtonStatusRnw(status);

        this.renewThisItem.setEnabled(true);
    }//GEN-LAST:event_undoChangesRnwActionPerformed

    private void renewThisItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_renewThisItemActionPerformed
        this.oldConstants[0] = this.nameRet.getText();
        this.oldConstants[1] = this.addRet.getText();
        this.oldConstants[2] = this.conNumRet.getText();
        this.oldConstants[3] = this.descRet.getText();
        this.oldConstants[4] = this.classificationRet.getSelectedItem().toString();

        if (this.statusRenew.getSelectedItem().toString().equals("Closed") || this.statusRenew.getSelectedItem().toString().equals("Renewed") || this.statusRenew.getSelectedItem().toString().equals("Sold")) {
            JOptionPane.showMessageDialog(null, "Unable to renew Pledge Loan. \n Please make sure that status of loan is still open.");
        } else {
            retrieveButtonStatusRnw(true);

            Date currentDate = Calendar.getInstance().getTime();
            this.transDateRet.setEditable(true);
            this.transDateRet.setDate(currentDate);
            this.matDateRet.setDate(this.dateHelp.oneMonth(Calendar.getInstance()));
            this.expDateRet.setDate(this.dateHelp.fourMonths(Calendar.getInstance()));
            this.numOfMonths.setValue(Integer.parseInt(this.dateHelp.getMonthDifference(currentDate, this.dateHelp.formatStringToDate(this.oldSangla.getMaturity_date()))));
            //       this.numOfMonthsRnw.setText(this.dateHelp.getMonthDifference(currentDate, this.dateHelp.formatStringToDate(this.oldSangla.getMaturity_date())));
            if (this.classificationRet.getSelectedItem().toString().equals("Gadget") || this.classificationRet.getSelectedItem().toString().equals("Application") || this.classificationRet.getSelectedItem().toString().equals("Jewelry") || this.classificationRet.getSelectedItem().toString().equals("Others")) {

                this.intRateSlider.setValue(5);
            } else {
                this.intRateSlider.setValue(7);
            }

            this.transDateRet.setDate(Calendar.getInstance().getTime());
            Calendar cal = Calendar.getInstance();
            cal.setTime(this.transDateRet.getDate());
            this.procByRet.setText(this.con.getProp("last_username"));
            this.matDateRet.setDate(this.dateHelp.oneMonth(cal));
            this.expDateRet.setDate(this.dateHelp.threeMonths(cal));
            this.principalRnw.setText(this.decHelp.FormatNumber(extractAmount(principalRet)));
            this.principalRnw.setEditable(false);
            this.svcChgRnw.setText("5.00");
            this.statusRenew.setSelectedItem("Renewed");
            this.insuFeeRnw.setText(this.calculate.computeInsurance(extractAmount(principalRet)));
            this.stamp.setText("0.00");
            this.finalizeButton.setEnabled(true);
            this.newPapNumRet.requestFocusInWindow();
            this.newPapNumRet.selectAll();
            this.undoChangesRnw.setEnabled(true);
            this.interestRnw.setText(this.calculate.computeInterest(extractAmount(principalRnw), extractValue(intRateSlider), extractInt(numOfMonths)));

            this.renewThisItem.setEnabled(false);
        }
    }//GEN-LAST:event_renewThisItemActionPerformed

    private void reprintCompShtActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_reprintCompShtActionPerformed
        int conf = JOptionPane.showConfirmDialog(null, "Are you sure you want to reprint the computation sheet for this Pap. No.?", "Reprint Computation Sheet", 0);
        Sangla forRp = new Sangla();
        if (conf == 0) {
            Config con = new Config();
//            if (con.getProp("branch").equalsIgnoreCase("Tangos")) {

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
                    FileInputStream fis = new FileInputStream("C:\\MPIS\\daytranx\\" + this.oldPapNoLRenew.getText() + forRp.getBranchCode(this.branchSearchRenew.getSelectedItem().toString()) + ".pdf");
                        Doc pdfDoc = new SimpleDoc(fis, DocFlavor.INPUT_STREAM.AUTOSENSE, null);
                        DocPrintJob printJob = myService.createPrintJob();
                        printJob.print(pdfDoc, new HashPrintRequestAttributeSet());
                        fis.close();
                    }          catch (FileNotFoundException ex) {
                        con.saveProp("mpis_last_error", String.valueOf(ex));
                        Logger.getLogger(ReprintTransfer.class.getName()).log(Level.SEVERE, (String)null, ex);

                    }          catch (PrintException|IOException ex) {
                        con.saveProp("mpis_last_error", String.valueOf(ex));
                        Logger.getLogger(ReprintTransfer.class.getName()).log(Level.SEVERE, (String)null, ex);
                    }
//                } else {
//
//                    PrintExcel px = new PrintExcel();
//                    String destination = "C:\\MPIS\\daytranx\\" + this.oldPapNoLRenew.getText() + forRp.getBranchCode(this.branchSearchRenew.getSelectedItem().toString()) + ".xls";
//                    px.printFile(new File(destination));
//                }
            }
    }//GEN-LAST:event_reprintCompShtActionPerformed

    private void partialPaymentCheckboxActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_partialPaymentCheckboxActionPerformed
        partialPaymentBox.setEditable(partialPaymentCheckbox.isSelected());

    }//GEN-LAST:event_partialPaymentCheckboxActionPerformed

    private void partialPaymentBoxKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_partialPaymentBoxKeyTyped
        if ((!Character.isDigit(evt.getKeyChar()) && evt.getKeyChar() != '.') || this.advIntRnw.getText().replace(",", "").length() > 10)
        evt.consume();
    }//GEN-LAST:event_partialPaymentBoxKeyTyped

    private void partialPaymentBoxKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_partialPaymentBoxKeyPressed
        int key = evt.getKeyCode();
        if (key == 10) {
            intRateSliderStateChanged(null);;
            this.finalizeButton.requestFocusInWindow();
        }
    }//GEN-LAST:event_partialPaymentBoxKeyPressed

    private void partialPaymentBoxActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_partialPaymentBoxActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_partialPaymentBoxActionPerformed

    private void partialPaymentBoxFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_partialPaymentBoxFocusLost
        this.partialPaymentBox.setText(this.decHelp.FormatNumber(extractAmount(partialPaymentBox)));
        if ((extractAmount(netIncRnw)) > 0.0D) {
            this.netProRnw.setText(getComputedAmtDueInc());
            if (extractAmount(netProRnw) > 0.0D) {
                this.amountDueLabel.setText("Total Net Proceeds");
            } else {
                double absNetProRnw = Math.abs(extractAmount(netProRnw));
                this.netProRnw.setText(String.valueOf(absNetProRnw));
                this.amountDueLabel.setText("Total Amount Due");
            }
        } else if (extractAmount(netDecRnw) > 0.0D) {
            this.netProRnw.setText(getComputedAmtDueDec());
            this.amountDueLabel.setText("Total Amount Due");
        } else {
            this.netProRnw.setText(getComputedAmtDue());
            this.amountDueLabel.setText("Total Amount Due");
        }
    }//GEN-LAST:event_partialPaymentBoxFocusLost

    private void partialPaymentBoxFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_partialPaymentBoxFocusGained
        this.partialPaymentBox.selectAll();
    }//GEN-LAST:event_partialPaymentBoxFocusGained

    private void unpaidAIKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_unpaidAIKeyTyped
        if ((!Character.isDigit(evt.getKeyChar()) && evt.getKeyChar() != '.') || this.stamp.getText().replace(",", "").length() > 10)
        evt.consume();
    }//GEN-LAST:event_unpaidAIKeyTyped

    private void unpaidAIKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_unpaidAIKeyPressed
        int key = evt.getKeyCode();
        if (key == 10) {
            intRateSliderStateChanged(null);
            this.netIncRnw.requestFocusInWindow();
            this.netIncRnw.selectAll();
        }
    }//GEN-LAST:event_unpaidAIKeyPressed

    private void unpaidAIActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_unpaidAIActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_unpaidAIActionPerformed

    private void unpaidAIFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_unpaidAIFocusLost
        this.unpaidAI.setText(this.decHelp.FormatNumber(extractAmount(unpaidAI)));
        if (extractAmount(netIncRnw) > 0.0D) {
            this.netProRnw.setText(getComputedAmtDueInc());
        if (extractAmount(netProRnw) > 0.0D) {
            this.amountDueLabel.setText("Total Net Proceeds");
        } else {
            double absNetProRnw = Math.abs(extractAmount(netProRnw));
            this.netProRnw.setText(String.valueOf(absNetProRnw));
            this.amountDueLabel.setText("Total Amount Due");
        }
        } else if (extractAmount(netDecRnw) > 0.0D) {
            this.netProRnw.setText(getComputedAmtDueDec());
        this.amountDueLabel.setText("Total Amount Due");
        } else {
            this.netProRnw.setText(getComputedAmtDue());
        this.amountDueLabel.setText("Total Amount Due");
        }
    }//GEN-LAST:event_unpaidAIFocusLost

    private void unpaidAIFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_unpaidAIFocusGained
        this.unpaidAI.selectAll();
    }//GEN-LAST:event_unpaidAIFocusGained

    private void unpaidAiCheckboxActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_unpaidAiCheckboxActionPerformed
        unpaidAI.setEditable(unpaidAiCheckbox.isSelected());
    }//GEN-LAST:event_unpaidAiCheckboxActionPerformed

    private void aiRateSliderStateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_aiRateSliderStateChanged
        if (calculate.isAiRateRounded()) {
            aiLable.setText("≈" + Integer.toString(aiRateSlider.getValue()).concat("%"));
        } else {
            aiLable.setText(Integer.toString(aiRateSlider.getValue()).concat("%"));
        }
//        aiLable.setText(Integer.toString(aiRateSlider.getValue()).concat("%"));
        if (extractAmount(netIncRnw) > 0.0D) {
            this.advIntRnw.setText(this.calculate.computeAdvanceInterestWithNetIncrease(extractAmount(principalRnw), extractAmount(netIncRnw), extractValue(aiRateSlider)));
            this.netProRnw.setText(getComputedAmtDueInc());
        if (extractAmount(netProRnw) > 0.0D) {
            this.amountDueLabel.setText("Total Net Proceeds");
        } else {
            double absNetProRnw = Math.abs(extractAmount(netProRnw));
            this.netProRnw.setText(String.valueOf(absNetProRnw));
            this.amountDueLabel.setText("Total Amount Due");
        }
        } else if (extractAmount(netDecRnw) > 0.0D) {
            this.advIntRnw.setText(this.calculate.computeAdvanceInterestWithNetDecrease(extractAmount(principalRnw), extractAmount(netDecRnw), extractValue(aiRateSlider)));
            this.netProRnw.setText(getComputedAmtDueDec());
        this.amountDueLabel.setText("Total Amount Due");
        } else {
            this.advIntRnw.setText(this.calculate.computeAdvanceInterest(extractAmount(principalRnw), extractValue(aiRateSlider)));
            this.netProRnw.setText(getComputedAmtDue());
        this.amountDueLabel.setText("Total Amount Due");
        }
    }//GEN-LAST:event_aiRateSliderStateChanged

    private void advIntRnwKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_advIntRnwKeyTyped
        if ((!Character.isDigit(evt.getKeyChar()) && evt.getKeyChar() != '.') || this.advIntRnw.getText().replace(",", "").length() > 10)
        evt.consume();
    }//GEN-LAST:event_advIntRnwKeyTyped

    private void advIntRnwKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_advIntRnwKeyPressed
        int key = evt.getKeyCode();
        if (key == 10) {
            intRateSliderStateChanged(null);;
            this.partialPaymentBox.requestFocusInWindow();
        }
    }//GEN-LAST:event_advIntRnwKeyPressed

    private void advIntRnwFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_advIntRnwFocusLost
        this.advIntRnw.setText(this.decHelp.FormatNumber(extractAmount(advIntRnw)));
        
        if (extractAmount(netIncRnw) > 0.0D) {
            this.aiRateSlider.setValue(this.calculate.computeRoundedAdvIntRateWithNetInc(extractAmount(principalRnw), extractAmount(netIncRnw), extractAmount(advIntRnw)));
            this.netProRnw.setText(getComputedAmtDueInc());
            if (extractAmount(netProRnw) > 0.0D) {
                this.amountDueLabel.setText("Total Net Proceeds");
            } else {
                double absNetProRnw = Math.abs(extractAmount(netProRnw));
                this.netProRnw.setText(String.valueOf(absNetProRnw));
                this.amountDueLabel.setText("Total Amount Due");
            }
        } else if (extractAmount(netDecRnw) > 0.0D) {
            this.aiRateSlider.setValue((this.calculate.computeRoundedAdvIntRateWithNetDec(extractAmount(principalRnw), extractAmount(netDecRnw), extractAmount(advIntRnw))));
            this.netProRnw.setText(getComputedAmtDueDec());
            this.amountDueLabel.setText("Total Amount Due");
        } else {
            this.aiRateSlider.setValue(this.calculate.computeRoundedAdvIntRate(extractAmount(principalRnw), extractAmount(advIntRnw)));
            System.out.println("is it rounded? " + calculate.isAiRateRounded());
            this.netProRnw.setText(getComputedAmtDue());
            this.amountDueLabel.setText("Total Amount Due");
        }
    }//GEN-LAST:event_advIntRnwFocusLost

    private void advIntRnwFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_advIntRnwFocusGained
        this.advIntRnw.selectAll();
    }//GEN-LAST:event_advIntRnwFocusGained

    private void liqDamKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_liqDamKeyTyped
        if ((!Character.isDigit(evt.getKeyChar()) && evt.getKeyChar() != '.') || this.stamp.getText().replace(",", "").length() > 10)
        evt.consume();
    }//GEN-LAST:event_liqDamKeyTyped

    private void liqDamKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_liqDamKeyPressed
        int key = evt.getKeyCode();
        if (key == 10) {
            intRateSliderStateChanged(null);
            this.affLoss.requestFocusInWindow();
            this.affLoss.selectAll();
        }
    }//GEN-LAST:event_liqDamKeyPressed

    private void liqDamFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_liqDamFocusLost
        this.liqDam.setText(this.decHelp.FormatNumber(extractAmount(liqDam)));
        if (extractAmount(netIncRnw) > 0.0D) {
            this.netProRnw.setText(getComputedAmtDueInc());
        if (extractAmount(netProRnw) > 0.0D) {
            this.amountDueLabel.setText("Total Net Proceeds");
        } else {
            double absNetProRnw = Math.abs(extractAmount(netProRnw));
            this.netProRnw.setText(String.valueOf(absNetProRnw));
            this.amountDueLabel.setText("Total Amount Due");
        }
        } else if (extractAmount(netDecRnw) > 0.0D) {
            this.netProRnw.setText(getComputedAmtDueDec());
        this.amountDueLabel.setText("Total Amount Due");
        } else {
            this.netProRnw.setText(getComputedAmtDue());
        this.amountDueLabel.setText("Total Amount Due");
        }
    }//GEN-LAST:event_liqDamFocusLost

    private void liqDamFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_liqDamFocusGained
        this.liqDam.selectAll();
    }//GEN-LAST:event_liqDamFocusGained

    private void affLossKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_affLossKeyTyped
        if ((!Character.isDigit(evt.getKeyChar()) && evt.getKeyChar() != '.') || this.stamp.getText().replace(",", "").length() > 10)
        evt.consume();
    }//GEN-LAST:event_affLossKeyTyped

    private void affLossKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_affLossKeyPressed
        int key = evt.getKeyCode();
        if (key == 10) {
            intRateSliderStateChanged(null);
            this.unpaidAI.requestFocusInWindow();
            this.unpaidAI.selectAll();
        }
    }//GEN-LAST:event_affLossKeyPressed

    private void affLossFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_affLossFocusLost
        this.affLoss.setText(this.decHelp.FormatNumber(extractAmount(affLoss)));
        if (extractAmount(netIncRnw) > 0.0D) {
            this.netProRnw.setText(getComputedAmtDueInc());
        if (extractAmount(netProRnw) > 0.0D) {
            this.amountDueLabel.setText("Total Net Proceeds");
        } else {
            double absNetProRnw = Math.abs(extractAmount(netProRnw));
            this.netProRnw.setText(String.valueOf(absNetProRnw));
            this.amountDueLabel.setText("Total Amount Due");
        }
        } else if (extractAmount(netDecRnw) > 0.0D) {
            this.netProRnw.setText(getComputedAmtDueDec());
        this.amountDueLabel.setText("Total Amount Due");
        } else {
            this.netProRnw.setText(getComputedAmtDue());
        this.amountDueLabel.setText("Total Amount Due");
        }
    }//GEN-LAST:event_affLossFocusLost

    private void affLossFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_affLossFocusGained
        this.affLoss.selectAll();
    }//GEN-LAST:event_affLossFocusGained

    private void stampKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_stampKeyTyped
        if ((!Character.isDigit(evt.getKeyChar()) && evt.getKeyChar() != '.') || this.stamp.getText().replace(",", "").length() > 10)
        evt.consume();
    }//GEN-LAST:event_stampKeyTyped

    private void stampKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_stampKeyPressed
        int key = evt.getKeyCode();
        if (key == 10) {
            intRateSliderStateChanged(null);
            this.liqDam.requestFocusInWindow();
            this.liqDam.selectAll();
        }
    }//GEN-LAST:event_stampKeyPressed

    private void stampFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_stampFocusLost
        this.stamp.setText(this.decHelp.FormatNumber(extractAmount(stamp)));
        if (extractAmount(netIncRnw) > 0.0D) {
            this.netProRnw.setText(getComputedAmtDueInc());
        if (extractAmount(netProRnw) > 0.0D) {
            this.amountDueLabel.setText("Total Net Proceeds");
        } else {
            double absNetProRnw = Math.abs(extractAmount(netProRnw));
            this.netProRnw.setText(String.valueOf(absNetProRnw));
            this.amountDueLabel.setText("Total Amount Due");
        }
        } else if (extractAmount(netDecRnw) > 0.0D) {
            this.netProRnw.setText(getComputedAmtDueDec());
            this.amountDueLabel.setText("Total Amount Due");
        } else {
            this.netProRnw.setText(getComputedAmtDue());
            this.amountDueLabel.setText("Total Amount Due");
        }
    }//GEN-LAST:event_stampFocusLost

    private void stampFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_stampFocusGained
        this.stamp.selectAll();
    }//GEN-LAST:event_stampFocusGained

    private void numOfMonthsStateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_numOfMonthsStateChanged
        interestRnw.setText(this.calculate.computeInterest(extractAmount(principalRnw), extractValue(intRateSlider), extractInt(numOfMonths)));
        if (extractAmount(netIncRnw) > 0.0D) {
            this.netProRnw.setText(getComputedAmtDueInc());
        if (extractAmount(netProRnw) > 0.0D) {
            this.amountDueLabel.setText("Total Net Proceeds");
        } else {
            double absNetProRnw = Math.abs(extractAmount(netProRnw));
            this.netProRnw.setText(String.valueOf(absNetProRnw));
            this.amountDueLabel.setText("Total Amount Due");
        }
        } else if (extractAmount(netDecRnw) > 0.0D) {
            this.netProRnw.setText(getComputedAmtDueDec());
        this.amountDueLabel.setText("Total Amount Due");
        } else {
            this.netProRnw.setText(getComputedAmtDue());
        this.amountDueLabel.setText("Total Amount Due");
        }
    }//GEN-LAST:event_numOfMonthsStateChanged

    private void intRateSliderStateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_intRateSliderStateChanged
        if (calculate.isIntRateRounded()) {
            intLabel.setText("≈" + Integer.toString(intRateSlider.getValue()).concat("%"));
        } else {
            intLabel.setText(Integer.toString(intRateSlider.getValue()).concat("%"));
        }
        this.interestRnw.setText(this.calculate.computeInterest(extractAmount(principalRnw),
            extractValue(intRateSlider), extractInt(numOfMonths)));
    if (extractAmount(netIncRnw) > 0.0D) {
        this.netProRnw.setText(getComputedAmtDueInc());
    if (extractAmount(netProRnw) > 0.0D) {
        this.amountDueLabel.setText("Total Net Proceeds");
        } else {
            double absNetProRnw = Math.abs(extractAmount(netProRnw));
            this.netProRnw.setText(String.valueOf(absNetProRnw));
            this.amountDueLabel.setText("Total Amount Due");
        }
        } else if (extractAmount(netDecRnw) > 0.0D) {
            this.netProRnw.setText(getComputedAmtDueDec());
        this.amountDueLabel.setText("Total Amount Due");
        } else {
            this.netProRnw.setText(getComputedAmtDue());
        this.amountDueLabel.setText("Total Amount Due");
        }
    }//GEN-LAST:event_intRateSliderStateChanged

    private void interestRnwKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_interestRnwKeyTyped
        if ((!Character.isDigit(evt.getKeyChar()) && evt.getKeyChar() != '.') || this.interestRnw.getText().replace(",", "").length() > 10)
        evt.consume();
    }//GEN-LAST:event_interestRnwKeyTyped

    private void interestRnwKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_interestRnwKeyPressed
        int key = evt.getKeyCode();
        if (key == 10) {
            interestRnwFocusLost((FocusEvent)null);
            this.svcChgRnw.requestFocusInWindow();
            this.svcChgRnw.selectAll();
        }
    }//GEN-LAST:event_interestRnwKeyPressed

    private void interestRnwActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_interestRnwActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_interestRnwActionPerformed

    private void interestRnwFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_interestRnwFocusLost
        this.interestRnw.setText(this.decHelp.FormatNumber(extractAmount(interestRnw)));
        if (extractValue(intRateSlider) > 0.0D && extractAmount(interestRnw) > 0.0D) {
            this.intRateSlider.setValue(this.calculate.computeRoundedInterestRate(extractAmount(principalRnw),
                extractAmount(interestRnw), Integer.valueOf(this.numOfMonths.getValue().toString())));
        }
    if (extractAmount(netIncRnw) > 0.0D) {
        this.netProRnw.setText(getComputedAmtDueInc());
        if (extractAmount(netProRnw) > 0.0D) {
            this.amountDueLabel.setText("Total Net Proceeds");
        } else {
            double absNetProRnw = Math.abs(extractAmount(netProRnw));
            this.netProRnw.setText(String.valueOf(absNetProRnw));
            this.amountDueLabel.setText("Total Amount Due");
        }
        } else if (extractAmount(netDecRnw) > 0.0D) {
            this.netProRnw.setText(getComputedAmtDueDec());
            this.amountDueLabel.setText("Total Amount Due");
        } else {
            this.netProRnw.setText(getComputedAmtDue());
            this.amountDueLabel.setText("Total Amount Due");
        }
    }//GEN-LAST:event_interestRnwFocusLost

    private void interestRnwFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_interestRnwFocusGained
        this.interestRnw.selectAll();
    }//GEN-LAST:event_interestRnwFocusGained

    private void newPapNumRetKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_newPapNumRetKeyPressed
        int key = evt.getKeyCode();
        if (key == 10) {
            this.addRet.requestFocusInWindow();
        }
    }//GEN-LAST:event_newPapNumRetKeyPressed

    private void newPapNumRetActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_newPapNumRetActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_newPapNumRetActionPerformed

    private void newPapNumRetFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_newPapNumRetFocusLost
        this.newPapNumRet.setText(this.newPapNumRet.getText().trim());
        if (this.newPapNumRet.getText().length() != 0) {
            this.transDateRet.setDate(Calendar.getInstance().getTime());
            Calendar cal = Calendar.getInstance();
            cal.setTime(this.transDateRet.getDate());
            this.matDateRet.setDate(this.dateHelp.oneMonth(cal));
            this.expDateRet.setDate(this.dateHelp.threeMonths(cal));
        } else {
            try {
                this.transDateRet.setDate(this.dateFormatter.parse(this.oldSangla.getTransaction_date()));
                this.matDateRet.setDate(this.dateFormatter.parse(this.oldSangla.getMaturity_date()));
                this.expDateRet.setDate(this.dateFormatter.parse(this.oldSangla.getExpiration_date()));
            } catch (ParseException ex) {
                Logger.getLogger(Renewal.class.getName()).log(Level.SEVERE, (String)null, ex);
            }
        }
    }//GEN-LAST:event_newPapNumRetFocusLost

    private void finalizeButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_finalizeButtonActionPerformed
        String newPrincipal = this.calculate.computeNewPrincipal(extractAmount(principalRnw),
            extractAmount(netIncRnw), extractAmount(netDecRnw));

        double netProForDB = calculate.computeNetProceeds(extractAmount(principalRnw)
                ,extractAmount(advIntRnw)
                , extractAmount(svcChgRnw)
                , extractAmount(stamp) + extractAmount(affLoss) + extractAmount(unpaidAI) + extractAmount(liqDam)
                , extractAmount(insuFeeRnw)
                , extractAmount(partialPaymentBox));

    if (this.newPapNumRet.getText().isEmpty()
        || this.procByRet.getText().isEmpty()
        || this.descRet.getText().isEmpty()
        || this.principalRet.getText().replace(",", "").isEmpty()
        || this.advIntRnw.getText().replace(",", "").isEmpty()
        || this.svcChgRet.getText().replace(",", "").isEmpty()
        || this.othChgRet.getText().isEmpty()
        || this.interestRnw.getText().replace(",", "").isEmpty())
        {

            JOptionPane.showMessageDialog(null, "Please complete all information.", "Loan Renewal", 0);

        } else if (!this.statusRenew.getSelectedItem().toString().equals("Renewed")) {
            JOptionPane.showMessageDialog(null, "Please update 'STATUS' to 'Renewed'.", "Loan Renewal", 0);
        } else {

            Sangla renewedLoan = new Sangla();
            renewedLoan.setPap_num(this.newPapNumRet.getText());
            if (renewedLoan.isPapNoAlreadyUsed()) {
                JOptionPane.showMessageDialog(null, "The new Papeleta No. given is already used. Please use another Papeleta Number.");
            } else {
                renewedLoan.setBranch(this.branchRet.getSelectedItem().toString());
                renewedLoan.setItemCodes();
                renewedLoan.setClient_name(this.nameRet.getText());
                renewedLoan.setClient_address(this.addRet.getText());
                renewedLoan.setContact_no(this.conNumRet.getText());
                renewedLoan.setProcessed_by(this.procByRet.getText());
                renewedLoan.setItem_description(this.descRet.getText());
                renewedLoan.setClassification(this.classificationRet.getSelectedItem().toString());
                renewedLoan.setTransaction_date(this.dateFormatter.format(this.transDateRet.getDate()));
                renewedLoan.setStatus("Open");
                renewedLoan.setOld_pap_num(this.papNumRet.getText());
                renewedLoan.setRemarks(this.remarksRet.getText());
                renewedLoan.setNew_pap_num(null);
                renewedLoan.setPrincipal(Double.parseDouble(newPrincipal.replace(",", "")));
                renewedLoan.setAdvance_interest(extractAmount(advIntRnw));
                renewedLoan.setInterest(extractAmount(interestRet));
                renewedLoan.setService_charge(extractAmount(svcChgRnw));
                renewedLoan.setInsurance(extractAmount(insuFeeRnw));
                renewedLoan.setOther_charges(extractAmount(othChgRet));
                renewedLoan.setNet_proceeds(netProForDB);
                renewedLoan.setOld_branch(this.branchSearchRenew.getSelectedItem().toString());
                renewedLoan.setNet_increase(extractAmount(netIncRnw));
                renewedLoan.setNet_decrease(extractAmount(netDecRnw));

                Sangla tubosSangla = new Sangla();
                tubosSangla.setPap_num(this.oldPapNoLRenew.getText());
                tubosSangla.setBranch(this.branchRet.getSelectedItem().toString());
                tubosSangla.setTransaction_date(this.dateHelp.formatDate(this.transDateRet.getDate()));
                tubosSangla.setPrincipal(extractAmount(principalRet));
                tubosSangla.setInterest(extractAmount(interestRnw));
                tubosSangla.setUnpaid(extractAmount(unpaidAI));
                tubosSangla.setLiq_dam(extractAmount(liqDam));
                tubosSangla.setStamp(extractAmount(stamp));
                tubosSangla.setAff(extractAmount(affLoss));
                tubosSangla.setOther_charges(tubosSangla.computeOtherCharges().doubleValue());

                ViewRenewedItem viewRenew = new ViewRenewedItem(null, true, renewedLoan);
                viewRenew.setVisible(true);

                if (viewRenew.allowAddToDatabase()) {
                    renewedLoan.insertToClienInfo();
                    //               System.out.println("New Pap No. Added!");
                    renewedLoan.saveToEmpeno();
                                   System.out.println(">>>>exp_date " + getExp_date());
                    if (this.dateHelp.formatStringToDate(getExp_date()).getMonth() - Calendar.getInstance().getTime().getMonth() <= 0) {
                        if (this.oldSangla.getStatus().equalsIgnoreCase("Expired")
                            && this.dateHelp.getDayDifference(Calendar.getInstance().getTime(), this.dateHelp.formatStringToDate(getExp_date())) > 3) {
                            //  IF MORE THAN 3 DAYS GRACE PERIOD
                            tubosSangla.setSubastaStatus(1);
                        } else if (this.dateHelp.getDayDifference(Calendar.getInstance().getTime(), this.dateHelp.formatStringToDate(getExp_date())) == 1
                            && Calendar.getInstance().getTime().getDate() == 1 && Calendar.getInstance().getTime().getHours() > 12) {
                                // IF MORNING OF NEXT DAY OF EXPIRATION
                            tubosSangla.setSubastaStatus(1);
                        } else if (this.dateHelp.formatStringToDate(getExp_date()).getDate() == this.dateHelp.getLastDateOfCurrentMonth()
                            && this.dateHelp.getDayDifference(Calendar.getInstance().getTime(), this.dateHelp.formatStringToDate(getExp_date())) <= 3) {
                            tubosSangla.setSubastaStatus(1);
                        }
                    } else {
                        tubosSangla.setSubastaStatus(0);
                        tubosSangla.setTubosStatus(0);
                    }
                    tubosSangla.saveToRescate(this.papNumRet.getText().concat(tubosSangla.getBranchCode(this.branchRet.getSelectedItem().toString())));
                    System.out.println("Old Pap No. Updated!");
                    renewedLoan.updateOldPapeleta(this.papNumRet.getText());
                }

                if (renewedLoan.isInsertToClientStatus() && renewedLoan.isUpdateOldPapStatus()) {

                    if (!this.nameRet.getText().equals(this.oldConstants[0])
                        || !this.addRet.getText().equals(this.oldConstants[1])
                        || !this.conNumRet.getText().equals(this.oldConstants[2])
                        || !this.descRet.getText().equals(this.oldConstants[3])
                        || !this.classificationRet.getSelectedItem().toString().equals(this.oldConstants[4])) {

                        String updateClientInfoHistory = "Update merlininventorydatabase.client_info set client_name = '"
                        + this.nameRet.getText() + "', client_address = '" + this.addRet.getText()
                        + "', contact_no = '" + this.conNumRet.getText() + "', item_description = '"
                        + this.descRet.getText() + "', classification = '"
                        + this.classificationRet.getSelectedItem().toString()
                        + "' where item_code_a = '" + tubosSangla.getItem_code_a() + "'";

                        try {
                            Connection connexion = DriverManager.getConnection(this.driver, this.f_user, this.f_pass);
                            Statement qry = connexion.createStatement();
                            qry.executeUpdate(updateClientInfoHistory);

                            System.out.println(updateClientInfoHistory);
                            System.out.println("Information were updated!");
                        } catch (SQLException ex) {
                            this.con.saveProp("mpis_last_error", String.valueOf(ex));
                            System.out.println("problem in updating information of history (client info)");
                        }
                    }

                    JOptionPane.showMessageDialog(null, "Item added successfully!", "Renew Loan", 1);
                    File tempFolder = new File("C:\\MPIS\\daytranx");
                    if (!tempFolder.exists()) {
                        tempFolder.mkdir();
                    }

                    Map<Object, Object> parameters = new HashMap<>();
                    parameters.put("ITEM_CODE", this.papNumRet.getText().concat(this.oldSangla.getBranchCode(this.branchRet.getSelectedItem().toString())));
                    parameters.put("SC", Double.valueOf(extractAmount(svcChgRnw)));
                    parameters.put("AI", Double.valueOf(extractAmount(advIntRnw)));
                    parameters.put("NO_OF_MONTHS", Integer.valueOf(extractInt(numOfMonths)));
                    parameters.put("TRANX_DATE", this.dateHelp.formatStringToDate(this.oldSangla.getTransaction_date()));
                    parameters.put("PARTIAL", Double.valueOf(extractAmount(partialPaymentBox)));

                    parameters.put("CODE", "R");
                    parameters.put("STATUS", "Renewed");
                    parameters.put("INSURANCE", Double.valueOf(extractAmount(insuFeeRnw)));
                    String insString = this.insuFeeRnw.getText().replaceAll(",", "");
                    String wordedInsurance = EnglishNumberToWords.convert(Long.parseLong(insString.substring(0, insString.lastIndexOf("."))));
                    if (Double.parseDouble(insString.substring(insString.lastIndexOf(".") + 1)) > 0) {
                        wordedInsurance = wordedInsurance + " " + insString.substring(insString.lastIndexOf(".") + 1) + "/100";
                    }
                    wordedInsurance = wordedInsurance.concat(" PESOS ONLY");
                    System.out.println(wordedInsurance);
                    parameters.put("INSURANCE_WORD", wordedInsurance);

                    if (extractAmount(netDecRnw) > 0.0D) {
                        parameters.put("BC", Double.valueOf(extractAmount(netDecRnw) * -1.0D));
                        parameters.put("CAP_STAT", "Bawas");
                    } else if (extractAmount(netIncRnw) > 0.0D) {
                        parameters.put("BC", Double.valueOf(extractAmount(netIncRnw)));
                        parameters.put("CAP_STAT", "Dagdag");
                    } else {
                        parameters.put("BC", Double.valueOf(0.0D));
                        parameters.put("CAP_STAT", "");
                    }
                    parameters.put("BC", Double.valueOf(extractAmount(netDecRnw)));

                    pr.setDestination(this.con.getProp("tranx_folder").concat(this.oldSangla.getItem_code_a()).concat(".pdf"));
                    pr.printReport("/Reports/compusheet.jrxml", parameters);
                    //                    System.out.println("computation sheet printed");
                    if (!this.oldSangla.getStatus().equalsIgnoreCase("Expired")) {
                        System.out.println("ITEM IS NOT EXPIRED");
                        CashTransactions rnwEntry = new CashTransactions();
                        rnwEntry.setDatabase("cashtransactions");
                        rnwEntry.setBranch(this.branchRet.getSelectedItem().toString());
                        String rnwEntryCashTransactionQuery = "Select COUNT(*) from cashtransactions.renewals";
                        int trc = 0;
                        try {
                            Connection connect = DriverManager.getConnection(this.driver, this.f_user, this.f_pass);
                            Statement query0 = connect.createStatement();
                            ResultSet rSet = query0.executeQuery(rnwEntryCashTransactionQuery);
                            while (rSet.next()) {
                                trc = rSet.getInt(1);
                            }
                            trc++;
                            System.out.println(trc);
                        } catch (SQLException ex) {
                            this.con.saveProp("mpis_last_error", String.valueOf(ex));
                            System.out.println("error in count search");
                        }
                        rnwEntry.setTransaction_no(trc);
                        double increase = extractAmount(netIncRnw);
                        double decrease = extractAmount(netDecRnw);

                        if (increase > 0.0D) {
                            rnwEntry.setCash_transaction_type("RDC");
                            rnwEntry.setTransaction_remarks("Renew-Dagdag-Capital ");
                        } else if (decrease > 0.0D) {
                            rnwEntry.setCash_transaction_type("RBC");
                            rnwEntry.setTransaction_remarks("Renew-Bawas-Capital ");
                        } else {
                            rnwEntry.setCash_transaction_type("RNW");
                            rnwEntry.setTransaction_remarks("Renewal ");
                        }
                        rnwEntry.generateTransaction_id();
                        rnwEntry.setLoans_item_code(this.oldSangla.getItem_code_a());
                        rnwEntry.setLoans_name(this.nameRet.getText());
                        rnwEntry.setTransaction_amount(extractAmount(netProRnw));

                        rnwEntry.generateTransaction_remarks();
                        rnwEntry.addRenewalTransactionToDB();
                        if (!rnwEntry.isRenewal_inserted()) {
                            System.out.println("There has been an error inserting this transaction to the database");
                        } else {
                            rnwEntry.addToDailyTransaction();
                            System.out.println("Unexpired item added to daily transaction");

                        }

                    } else if (this.dateHelp.getDayDifference(Calendar.getInstance().getTime(), this.dateHelp.formatStringToDate(getExp_date())) > 3 || this.dateHelp.formatStringToDate(getExp_date()).getDate() >= 30) {
                        System.out.println("CURRENT DATE " + Calendar.getInstance().getTime() + " EXPIRY DATE " + this.oldSangla.getExpiration_date());
                        System.out.println("ITEM IS ALREADY EXPIRED");

                        CashTransactions addEntry = new CashTransactions();
                        addEntry.setDatabase("cashtransactions");
                        addEntry.setBranch(this.branchRet.getSelectedItem().toString());
                        String addCashTransQuery = "Select COUNT(*) from cashtransactions.additionals";
                        int tableRowCount = 0;
                        try {
                            Connection connect = DriverManager.getConnection(this.driver, this.f_user, this.f_pass);
                            Statement query0 = connect.createStatement();
                            ResultSet rSet = query0.executeQuery(addCashTransQuery);
                            while (rSet.next()) {
                                tableRowCount = rSet.getInt(1);
                            }
                            tableRowCount++;
                            System.out.println(tableRowCount);
                        } catch (SQLException ex) {
                            this.con.saveProp("mpis_last_error", String.valueOf(ex));
                            System.out.println("error in count search");
                        }
                        addEntry.setAddsub(extractAmount(principalRnw));
                        addEntry.setTransaction_no(tableRowCount);
                        addEntry.setCash_transaction_type("ADD");
                        addEntry.setLoans_name(this.nameRet.getText());
                        addEntry.generateTransaction_id();
                        addEntry.setPetty_cast_type("Renew Subasta");
                        addEntry.setTransaction_remarks("Renew-Subasta Pap#" + this.oldSangla.getPap_num() + "( Received from: " + this.oldSangla.getClient_name() + ")");
                        addEntry.setTransaction_amount(extractAmount(interestRnw) + extractAmount(stamp) + extractAmount(affLoss) + extractAmount(unpaidAI) + extractAmount(liqDam) + extractAmount(netDecRnw) - extractAmount(netIncRnw));

                        addEntry.addAdditionalTransactionToDB();
                        if (!addEntry.isAdditionals_inserted()) {
                            System.out.println("There has been an error inserting this transaction to the database");
                        } else {
                            addEntry.addToDailyTransaction();
                        }

                        CashTransactions rnwEntry = new CashTransactions();
                        rnwEntry.setDatabase("cashtransactions");
                        rnwEntry.setBranch(this.branchRet.getSelectedItem().toString());
                        String rnwEntryCashTransactionQuery = "Select COUNT(*) from cashtransactions.renewals";
                        int trc = 0;
                        try {
                            Connection connect = DriverManager.getConnection(this.driver, this.f_user, this.f_pass);
                            Statement query0 = connect.createStatement();
                            ResultSet rSet = query0.executeQuery(rnwEntryCashTransactionQuery);
                            while (rSet.next()) {
                                trc = rSet.getInt(1);
                            }
                            trc++;
                            System.out.println(trc);
                        } catch (SQLException ex) {
                            this.con.saveProp("mpis_last_error", String.valueOf(ex));
                            System.out.println("error in count search");
                        }
                        rnwEntry.setTransaction_no(trc);
                        double increase = extractAmount(netIncRnw);
                        double decrease = extractAmount(netDecRnw);

                        if (increase > 0.0D) {
                            rnwEntry.setCash_transaction_type("RDC");
                            rnwEntry.setTransaction_remarks("Renew-Dagdag-Capital ");
                        } else if (decrease > 0.0D) {
                            rnwEntry.setCash_transaction_type("RBC");
                            rnwEntry.setTransaction_remarks("Renew-Bawas-Capital ");
                        } else {
                            rnwEntry.setCash_transaction_type("RNW");
                            rnwEntry.setTransaction_remarks("Renewal ");
                        }
                        rnwEntry.generateTransaction_id();
                        rnwEntry.setLoans_item_code(this.oldSangla.getItem_code_a());
                        rnwEntry.setLoans_name(this.nameRet.getText());
                        rnwEntry.setTransaction_amount(extractAmount(advIntRnw) + extractAmount(svcChgRnw) + extractAmount(insuFeeRnw));
                        rnwEntry.generateTransaction_remarks();
                        rnwEntry.addRenewalTransactionToDB();
                        if (!rnwEntry.isRenewal_inserted()) {
                            System.out.println("There has been an error inserting this transaction to the database");
                        } else {
                            rnwEntry.addToDailyTransaction();
                        }
                    } else {
                        System.out.println("ITEM IS WITHIN GRACE PERIOD BUT EXPIRED");

                        CashTransactions rnwEntry = new CashTransactions();
                        rnwEntry.setDatabase("cashtransactions");
                        rnwEntry.setBranch(this.branchRet.getSelectedItem().toString());
                        String rnwCashTransQuery = "Select COUNT(*) from cashtransactions.renewals";
                        int tableRowCount = 0;
                        try {
                            Connection connect = DriverManager.getConnection(this.driver, this.f_user, this.f_pass);
                            Statement query0 = connect.createStatement();
                            ResultSet rSet = query0.executeQuery(rnwCashTransQuery);
                            while (rSet.next()) {
                                tableRowCount = rSet.getInt(1);
                            }
                            tableRowCount++;
                            System.out.println(tableRowCount);
                        } catch (SQLException ex) {
                            this.con.saveProp("mpis_last_error", String.valueOf(ex));
                            System.out.println("error in count search");
                        }
                        rnwEntry.setTransaction_no(tableRowCount);
                        double increase = extractAmount(netIncRnw);
                        double decrease = extractAmount(netDecRnw);

                        if (increase > 0.0D) {
                            rnwEntry.setCash_transaction_type("RDC");
                            rnwEntry.setTransaction_remarks("Renew-Dagdag-Capital ");
                        } else if (decrease > 0.0D) {
                            rnwEntry.setCash_transaction_type("RBC");
                            rnwEntry.setTransaction_remarks("Renew-Bawas-Capital ");
                        } else {
                            rnwEntry.setCash_transaction_type("RNW");
                            rnwEntry.setTransaction_remarks("Renewal ");
                        }
                        rnwEntry.generateTransaction_id();
                        rnwEntry.setLoans_item_code(this.oldSangla.getItem_code_a());
                        rnwEntry.setLoans_name(this.nameRet.getText());
                        rnwEntry.setTransaction_amount(extractAmount(netProRnw));

                        rnwEntry.generateTransaction_remarks();
                        rnwEntry.addRenewalTransactionToDB();
                        if (!rnwEntry.isRenewal_inserted()) {
                            System.out.println("There has been an error inserting this transaction to the database");
                        } else {
                            rnwEntry.addToDailyTransaction();
                        }
                    }
                }

                clearFieldsInLoanRenewal();
                this.oldPapNoLRenew.setText("");
                retrieveButtonStatusRnw(false);
                this.oldPapNoLRenew.setEnabled(true);
                this.branchSearchRenew.setEnabled(true);
                this.retrieveInfo.setEnabled(true);
            }
        }

        requestFocusInSearch();
    }//GEN-LAST:event_finalizeButtonActionPerformed

    private void cancelButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cancelButtonActionPerformed
        clearFieldsInLoanRenewal();
        retrieveButtonStatusRnw(false);
        this.oldPapNoLRenew.setText("");
        this.oldPapNoLRenew.setEnabled(true);
        this.branchSearchRenew.setEnabled(true);
        this.retrieveInfo.setEnabled(true);
        this.oldPapNoLRenew.setEnabled(true);
        this.branchSearchRenew.setEnabled(true);
        this.retrieveInfo.setEnabled(true);
        this.undoChangesRnw.setEnabled(false);
    }//GEN-LAST:event_cancelButtonActionPerformed

    private void insuFeeRnwKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_insuFeeRnwKeyTyped
        if ((!Character.isDigit(evt.getKeyChar()) && evt.getKeyChar() != '.') || this.insuFeeRnw.getText().replace(",", "").length() > 10)
        evt.consume();
    }//GEN-LAST:event_insuFeeRnwKeyTyped

    private void insuFeeRnwKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_insuFeeRnwKeyPressed
        int key = evt.getKeyCode();
        if (key == 10) {
            intRateSliderStateChanged(null);
            this.aiRateSlider.requestFocusInWindow();
            //       this.aiRateSlider.selectAll();
        }
    }//GEN-LAST:event_insuFeeRnwKeyPressed

    private void insuFeeRnwFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_insuFeeRnwFocusLost
        this.insuFeeRnw.setText(this.decHelp.FormatNumber(extractAmount(insuFeeRnw)));
        if (extractAmount(netIncRnw) > 0.0D) {
            this.netProRnw.setText(getComputedAmtDueInc());
        if (extractAmount(netProRnw) > 0.0D) {
            this.amountDueLabel.setText("Total Net Proceeds");
        } else {
            double absNetProRnw = Math.abs(extractAmount(netProRnw));
            this.netProRnw.setText(String.valueOf(absNetProRnw));
            this.amountDueLabel.setText("Total Amount Due");
        }
        } else if (extractAmount(netDecRnw) > 0.0D) {
            this.netProRnw.setText(getComputedAmtDueDec());
        this.amountDueLabel.setText("Total Amount Due");
        } else {
            this.netProRnw.setText(getComputedAmtDue());
        this.amountDueLabel.setText("Total Amount Due");
        }
    }//GEN-LAST:event_insuFeeRnwFocusLost

    private void insuFeeRnwFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_insuFeeRnwFocusGained
        this.insuFeeRnw.selectAll();
    }//GEN-LAST:event_insuFeeRnwFocusGained

    private void netDecRnwKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_netDecRnwKeyTyped
        if ((!Character.isDigit(evt.getKeyChar()) && evt.getKeyChar() != '.') || this.netDecRnw.getText().replace(",", "").length() > 10)
        evt.consume();
    }//GEN-LAST:event_netDecRnwKeyTyped

    private void netDecRnwKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_netDecRnwKeyPressed
        int key = evt.getKeyCode();
        if (key == 10) {
            intRateSliderStateChanged(null);
            this.insuFeeRnw.requestFocusInWindow();
            this.insuFeeRnw.selectAll();
        }
    }//GEN-LAST:event_netDecRnwKeyPressed

    private void netDecRnwFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_netDecRnwFocusLost
        this.netDecRnw.setText(this.decHelp.FormatNumber(extractAmount(netDecRnw)));
        if (extractAmount(netDecRnw) == 0.0D)
        this.netIncRnw.setEditable(true);
        this.insuFeeRnw.setText( this.calculate.computeInsuranceWithNetDecrease ( extractAmount(principalRnw), extractAmount(netDecRnw) ) );
        if (extractAmount(netIncRnw) > 0.0D) {
            this.netProRnw.setText(getComputedAmtDueInc());
        if (extractAmount(netProRnw) > 0.0D) {
            this.amountDueLabel.setText("Total Net Proceeds");
        } else {
            double absNetProRnw = Math.abs(extractAmount(netProRnw));
            this.netProRnw.setText(String.valueOf(absNetProRnw));
            this.amountDueLabel.setText("Total Amount Due");
        }
        } else if (extractAmount(netDecRnw) > 0.0D) {
            this.netIncRnw.setEditable(false);
            this.netIncRnw.setText("0.00");
            this.netProRnw.setText(getComputedAmtDueDec());
        this.advIntRnw.setText(this.calculate.computeAdvanceInterestWithNetDecrease(extractAmount(principalRnw), extractAmount(netDecRnw), extractValue(aiRateSlider)));
        this.aiRateSlider.setValue(this.calculate.computeRoundedAdvIntRateWithNetDec(extractAmount(principalRnw), extractAmount(netDecRnw), extractAmount(advIntRnw)));
        this.insuFeeRnw.setText(this.calculate.computeInsuranceWithNetDecrease(extractAmount(principalRnw), extractAmount(netDecRnw)));
        this.amountDueLabel.setText("Total Amount Due");
        } else {
            this.netProRnw.setText(getComputedAmtDue());
        this.amountDueLabel.setText("Total Amount Due");
        }
    }//GEN-LAST:event_netDecRnwFocusLost

    private void netDecRnwFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_netDecRnwFocusGained
        this.netDecRnw.selectAll();
    }//GEN-LAST:event_netDecRnwFocusGained

    private void netIncRnwKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_netIncRnwKeyTyped
        if ((!Character.isDigit(evt.getKeyChar()) && evt.getKeyChar() != '.') || this.netIncRnw.getText().replace(",", "").length() > 10)
        evt.consume();
    }//GEN-LAST:event_netIncRnwKeyTyped

    private void netIncRnwKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_netIncRnwKeyPressed
        int key = evt.getKeyCode();
        if (key == 10) {
            intRateSliderStateChanged(null);
            this.netDecRnw.requestFocusInWindow();
            this.netDecRnw.selectAll();
        }
    }//GEN-LAST:event_netIncRnwKeyPressed

    private void netIncRnwFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_netIncRnwFocusLost
        this.netIncRnw.setText(this.decHelp.FormatNumber(extractAmount(netIncRnw)));
        if (extractAmount(netIncRnw) == 0.0D)
        this.netDecRnw.setEditable(true);
        this.insuFeeRnw.setText(this.calculate.computeInsuranceWithNetIncrease(extractAmount(principalRnw), extractAmount(netIncRnw)));
        if (extractAmount(netIncRnw) > 0.0D) {
            this.netDecRnw.setEditable(false);
            this.netDecRnw.setText("0.00");
            this.netProRnw.setText(getComputedAmtDueInc());

            this.aiRateSlider.setValue(this.calculate.computeRoundedAdvIntRateWithNetInc(extractAmount(principalRnw), extractAmount(netIncRnw), extractAmount(advIntRnw)));
            this.advIntRnw.setText(this.calculate.computeAdvanceInterestWithNetIncrease(extractAmount(principalRnw), extractAmount(netIncRnw), extractValue(aiRateSlider)));
            this.insuFeeRnw.setText(this.calculate.computeInsuranceWithNetIncrease(extractAmount(principalRnw), extractAmount(netIncRnw)));
            if (extractAmount(netProRnw) > 0.0D) {
                this.amountDueLabel.setText("Total Net Proceeds");
            } else {
                double absNetProRnw = Math.abs(extractAmount(netProRnw));
                this.netProRnw.setText(String.valueOf(absNetProRnw));
                this.amountDueLabel.setText("Total Amount Due");
            }
        } else if (extractAmount(netDecRnw) > 0.0D) {
            this.netProRnw.setText(getComputedAmtDueDec());
            this.insuFeeRnw.setText(this.calculate.computeInsuranceWithNetDecrease(extractAmount(principalRnw), extractAmount(netDecRnw)));
            this.amountDueLabel.setText("Total Amount Due");
        } else {
            this.netProRnw.setText(getComputedAmtDue());
            this.amountDueLabel.setText("Total Amount Due");
        }
    }//GEN-LAST:event_netIncRnwFocusLost

    private void netIncRnwFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_netIncRnwFocusGained
        this.netIncRnw.selectAll();
    }//GEN-LAST:event_netIncRnwFocusGained

    private void svcChgRnwKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_svcChgRnwKeyTyped
        if ((!Character.isDigit(evt.getKeyChar()) && evt.getKeyChar() != '.') || this.svcChgRnw.getText().replace(",", "").length() > 8)
        evt.consume();
    }//GEN-LAST:event_svcChgRnwKeyTyped

    private void svcChgRnwKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_svcChgRnwKeyPressed
        int key = evt.getKeyCode();
        if (key == 10) {
            intRateSliderStateChanged(null);
            this.stamp.requestFocusInWindow();
            this.stamp.selectAll();
        }
    }//GEN-LAST:event_svcChgRnwKeyPressed

    private void svcChgRnwActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_svcChgRnwActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_svcChgRnwActionPerformed

    private void svcChgRnwFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_svcChgRnwFocusLost
        this.svcChgRnw.setText(this.decHelp.FormatNumber(extractAmount(svcChgRnw)));
        if (extractAmount(netIncRnw) > 0.0D) {
            this.netProRnw.setText(getComputedAmtDueInc());
        if (extractAmount(netProRnw) > 0.0D) {
            this.amountDueLabel.setText("Total Net Proceeds");
        } else {
            double absNetProRnw = Math.abs(extractAmount(netProRnw));
            this.netProRnw.setText(String.valueOf(absNetProRnw));
            this.amountDueLabel.setText("Total Amount Due");
        }
        } else if (extractAmount(netDecRnw) > 0.0D) {
            this.netProRnw.setText(getComputedAmtDueDec());
        this.amountDueLabel.setText("Total Amount Due");
        } else {
            this.netProRnw.setText(getComputedAmtDueDec());
        this.amountDueLabel.setText("Total Amount Due");
        }
    }//GEN-LAST:event_svcChgRnwFocusLost

    private void svcChgRnwFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_svcChgRnwFocusGained
        this.svcChgRnw.selectAll();
    }//GEN-LAST:event_svcChgRnwFocusGained

    private void principalRnwKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_principalRnwKeyTyped
        if ((!Character.isDigit(evt.getKeyChar()) && evt.getKeyChar() != '.') || this.principalRnw.getText().replace(",", "").length() > 10)
        evt.consume();
    }//GEN-LAST:event_principalRnwKeyTyped

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
            this.renewThisItem.requestFocusInWindow();
            this.oldPapNoLRenew.setEnabled(false);
            this.branchSearchRenew.setEnabled(false);
            this.retrieveInfo.setEnabled(false);
            this.undoChangesRnw.setEnabled(false);

            this.renewThisItem.setEnabled(true);
            this.undoChangesRnw.setEnabled(true);
            if ((new File("C:\\MPIS\\daytranx\\" + itemCode + ".xls")).exists()) {
                this.oldPapNoLRenew.setEnabled(false);
                this.branchSearchRenew.setEnabled(false);
                this.retrieveInfo.setEnabled(false);
                this.reprintCompSht.setEnabled(true);
            } else {
                this.reprintCompSht.setEnabled(false);
            }
            setExp_date(oldSangla.getExpiration_date());
        } else {
            JOptionPane.showMessageDialog(null, "No item found.\n", "Message", 0);
            this.oldPapNoLRenew.setEnabled(true);
            this.branchSearchRenew.setEnabled(true);
            this.retrieveInfo.setEnabled(true);
//            this.undoChangesRnw.setEnabled(false);
            this.oldPapNoLRenew.requestFocusInWindow();
            this.oldPapNoLRenew.setEditable(true);
        }
                
//                System.out.println("GRACE PERIOD" + this.dateHelp.getDayDifference(Calendar.getInstance().getTime(), this.dateHelp.formatStringToDate(getExp_date())));
//                System.out.println("DATE" + this.dateHelp.formatStringToDate(getExp_date()).getDate());
    }//GEN-LAST:event_retrieveInfoActionPerformed

    private void branchSearchRenewActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_branchSearchRenewActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_branchSearchRenewActionPerformed

    private void oldPapNoLRenewKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_oldPapNoLRenewKeyTyped
        if ((!Character.isDigit(evt.getKeyChar()) && evt.getKeyChar() != '.') || this.oldPapNoLRenew.getText().length() > 5)
        /* 1585 */       evt.consume();
    }//GEN-LAST:event_oldPapNoLRenewKeyTyped

    private void oldPapNoLRenewFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_oldPapNoLRenewFocusLost
        this.oldPapNoLRenew.setText(this.oldPapNoLRenew.getText().trim());
    }//GEN-LAST:event_oldPapNoLRenewFocusLost

    private void classificationRetActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_classificationRetActionPerformed
        if (classificationRet.getSelectedItem().toString().equalsIgnoreCase("Vehicle - 2W") ||  
                classificationRet.getSelectedItem().toString().equalsIgnoreCase("Vehicle - 4W")) {
            motorInCheckbox.setEnabled(true);
        } else {
            motorInCheckbox.setEnabled(false);
        }
    }//GEN-LAST:event_classificationRetActionPerformed

    private void intRateSliderMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_intRateSliderMouseClicked
        calculate.setIntRateRounded(false);
    }//GEN-LAST:event_intRateSliderMouseClicked

    private void aiRateSliderMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_aiRateSliderMouseClicked
                // TODO add your handling code here:
    }//GEN-LAST:event_aiRateSliderMouseClicked


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JTextArea addRet;
    private javax.swing.JTextField advIntRnw;
    private javax.swing.JTextField affLoss;
    private javax.swing.JLabel aiLable;
    private javax.swing.JSlider aiRateSlider;
    private javax.swing.JLabel amountDueLabel;
    private javax.swing.JComboBox<String> branchRet;
    private javax.swing.JComboBox<String> branchSearchRenew;
    private javax.swing.JButton cancelButton;
    private javax.swing.JComboBox<String> classificationRet;
    private javax.swing.JTextField conNumRet;
    private javax.swing.JTextArea descRet;
    private org.jdesktop.swingx.JXDatePicker expDateRet;
    private javax.swing.JButton finalizeButton;
    private javax.swing.JTextField insuFeeRnw;
    private javax.swing.JTextField insuranceRet;
    private javax.swing.JLabel intLabel;
    private javax.swing.JSlider intRateSlider;
    private javax.swing.JTextField interestRet;
    private javax.swing.JTextField interestRnw;
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
    private javax.swing.JLabel jLabel20;
    private javax.swing.JLabel jLabel21;
    private javax.swing.JLabel jLabel22;
    private javax.swing.JLabel jLabel23;
    private javax.swing.JLabel jLabel24;
    private javax.swing.JLabel jLabel25;
    private javax.swing.JLabel jLabel26;
    private javax.swing.JLabel jLabel27;
    private javax.swing.JLabel jLabel28;
    private javax.swing.JLabel jLabel29;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel30;
    private javax.swing.JLabel jLabel31;
    private javax.swing.JLabel jLabel32;
    private javax.swing.JLabel jLabel33;
    private javax.swing.JLabel jLabel35;
    private javax.swing.JLabel jLabel36;
    private javax.swing.JLabel jLabel37;
    private javax.swing.JLabel jLabel38;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel41;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JPanel jPanel7;
    private javax.swing.JPanel jPanel8;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JScrollPane jScrollPane4;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JTextField liqDam;
    private org.jdesktop.swingx.JXDatePicker matDateRet;
    private javax.swing.JCheckBox motorInCheckbox;
    private javax.swing.JTextField nameRet;
    private javax.swing.JTextField netDecRnw;
    private javax.swing.JTextField netIncRnw;
    private javax.swing.JTextField netProRet;
    private javax.swing.JTextField netProRnw;
    private javax.swing.JTextField newPapNumRet;
    private javax.swing.JSpinner numOfMonths;
    private javax.swing.JTextField oldPapNoLRenew;
    private javax.swing.JTextField othChgRet;
    private javax.swing.JTextField papNumRet;
    private javax.swing.JTextField partialPaymentBox;
    private javax.swing.JCheckBox partialPaymentCheckbox;
    private javax.swing.JTextField principalRet;
    private javax.swing.JTextField principalRnw;
    private javax.swing.JTextField procByRet;
    private javax.swing.JTextArea remarksRet;
    private javax.swing.JButton renewThisItem;
    private javax.swing.JButton reprintCompSht;
    private javax.swing.JButton retrieveInfo;
    private javax.swing.JTextField stamp;
    private javax.swing.JComboBox<String> statusRenew;
    private javax.swing.JTextField svcChgRet;
    private javax.swing.JTextField svcChgRnw;
    private org.jdesktop.swingx.JXDatePicker transDateRet;
    private javax.swing.JButton undoChangesRnw;
    private javax.swing.JTextField unpaidAI;
    private javax.swing.JCheckBox unpaidAiCheckbox;
    // End of variables declaration//GEN-END:variables
}
