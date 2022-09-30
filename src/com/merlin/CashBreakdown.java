/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JDialog.java to edit this template
 */
package com.merlin;

import java.awt.event.FocusEvent;
import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;

/**
 *
 * @author luckycruz
 */
public class CashBreakdown extends javax.swing.JDialog {

    /**
     * @return the valuesEntered
     */
    public boolean isValuesEntered() {
        return valuesEntered;
    }

    /**
     * @param valuesEntered the valuesEntered to set
     */
    public void setValuesEntered(boolean valuesEntered) {
        this.valuesEntered = valuesEntered;
    }

    /**
     * @return the coh
     */
    public double getCoh() {
        return coh;
    }

    /**
     * @param coh the coh to set
     */
    public void setCoh(double coh) {
        this.coh = coh;
    }

    /**
     * @return the empeno
     */
    public double getEmpeno() {
        try {
            Connection connect = DriverManager.getConnection(this.driver, this.f_user, this.f_pass);
            Statement state = connect.createStatement();
            String query = "Select sum(principal) from merlininventorydatabase.empeno where date = '" + this.dateHelp.getCurrentDate() + "'";
            System.out.println(query);
            ResultSet rset = state.executeQuery(query);
            while (rset.next()) {
              setEmpeno(rset.getDouble(1));
              System.out.println(rset.getDouble(1));
            } 
            state.close();
            connect.close();
          } catch (SQLException ex) {
            this.con.saveProp("last_error", String.valueOf(ex));
            Logger.getLogger(CashBreakdown.class.getName()).log(Level.SEVERE, (String)null, ex);
          } 
          return this.empeno;
    }

    /**
     * @param empeno the empeno to set
     */
    public void setEmpeno(double empeno) {
        this.empeno = empeno;
    }

    /**
     * @return the rescate
     */
    public double getRescate() {
        try {
            Connection connect = DriverManager.getConnection(this.driver, this.f_user, this.f_pass);
            Statement state = connect.createStatement();
            String query = "Select sum(principal) from merlininventorydatabase.rescate where subasta = 0 and date = '" + this.dateHelp.getCurrentDate() + "'";
            System.out.println(query);
            ResultSet rset = state.executeQuery(query);
            while (rset.next()) {
              setRescate(rset.getDouble(1));
              System.out.println(rset.getDouble(1));
            } 
            state.close();
            connect.close();
          } catch (SQLException ex) {
            this.con.saveProp("last_error", String.valueOf(ex));
            Logger.getLogger(CashBreakdown.class.getName()).log(Level.SEVERE, (String)null, ex);
          } 
          return this.rescate;
    }

    /**
     * @param rescate the rescate to set
     */
    public void setRescate(double rescate) {
        this.rescate = rescate;
    }

    /**
     * @return the ai
     */
    public double getAi() {
        try {
            Connection connect = DriverManager.getConnection(this.driver, this.f_user, this.f_pass);
            Statement state = connect.createStatement();
            String query = "Select sum(ai) from merlininventorydatabase.empeno where date = '" + this.dateHelp.getCurrentDate() + "'";
            System.out.println(query);
            ResultSet rset = state.executeQuery(query);
            while (rset.next())
              setAi(rset.getDouble(1)); 
            state.close();
            connect.close();
          } catch (SQLException ex) {
            this.con.saveProp("last_error", String.valueOf(ex));
            Logger.getLogger(CashBreakdown.class.getName()).log(Level.SEVERE, (String)null, ex);
          } 
          return this.ai;
    }

    /**
     * @param ai the ai to set
     */
    public void setAi(double ai) {
        this.ai = ai;
    }

    /**
     * @return the interest
     */
    public double getInterest() {
        try {
            Connection connect = DriverManager.getConnection(this.driver, this.f_user, this.f_pass);
            Statement state = connect.createStatement();
            String query = "Select sum(interest) from merlininventorydatabase.rescate where subasta = 0 and date = '" + this.dateHelp.getCurrentDate() + "'";
            System.out.println(query);
            ResultSet rset = state.executeQuery(query);
            while (rset.next()) {
              setInterest(rset.getDouble(1));
              System.out.println(rset.getDouble(1));
            } 
            state.close();
            connect.close();
          } catch (SQLException ex) {
            this.con.saveProp("last_error", String.valueOf(ex));
            Logger.getLogger(CashBreakdown.class.getName()).log(Level.SEVERE, (String)null, ex);
          } 
          return this.interest;
    }

    /**
     * @param interest the interest to set
     */
    public void setInterest(double interest) {
        this.interest = interest;
    }

    /**
     * @return the sc
     */
    public double getSc() {
        try {
            Connection connect = DriverManager.getConnection(this.driver, this.f_user, this.f_pass);
            Statement state = connect.createStatement();
            String query = "Select sum(sc) from merlininventorydatabase.empeno where date = '" + this.dateHelp.getCurrentDate() + "'";
            System.out.println(query);
            ResultSet rset = state.executeQuery(query);
            while (rset.next())
              setSc(rset.getDouble(1)); 
            state.close();
            connect.close();
          } catch (SQLException ex) {
            this.con.saveProp("last_error", String.valueOf(ex));
            Logger.getLogger(CashBreakdown.class.getName()).log(Level.SEVERE, (String)null, ex);
          } 
          return this.sc;
    }

    /**
     * @param sc the sc to set
     */
    public void setSc(double sc) {
        this.sc = sc;
    }

    /**
     * @return the ins
     */
    public double getIns() {
        try {
            Connection connect = DriverManager.getConnection(this.driver, this.f_user, this.f_pass);
            Statement state = connect.createStatement();
            String query = "Select sum(insurance) from merlininventorydatabase.empeno where date = '" + this.dateHelp.getCurrentDate() + "'";
            System.out.println(query);
            ResultSet rset = state.executeQuery(query);
            while (rset.next())
              setIns(rset.getDouble(1)); 
            state.close();
            connect.close();
          } catch (SQLException ex) {
            this.con.saveProp("last_error", String.valueOf(ex));
            Logger.getLogger(CashBreakdown.class.getName()).log(Level.SEVERE, (String)null, ex);
          } 
          return this.ins;
    }

    /**
     * @param ins the ins to set
     */
    public void setIns(double ins) {
        this.ins = ins;
    }

    /**
     * @return the ptc
     */
    public double getPtc() {
        try {
            Connection connect = DriverManager.getConnection(this.driver, this.f_user, this.f_pass);
            Statement state = connect.createStatement();
            String query = "Select sum(pty_amount) from cashtransactions.petty_cash where pty_date = '" + this.dateHelp.getCurrentDate() + "'";
            System.out.println(query);
            ResultSet rset = state.executeQuery(query);
            while (rset.next()) {
              setPtc(rset.getDouble(1));
              System.out.println(rset.getDouble(1));
            } 
            state.close();
            connect.close();
          } catch (SQLException ex) {
            this.con.saveProp("last_error", String.valueOf(ex));
            Logger.getLogger(CashBreakdown.class.getName()).log(Level.SEVERE, (String)null, ex);
          } 
          return this.ptc;
    }

    /**
     * @param ptc the ptc to set
     */
    public void setPtc(double ptc) {
        this.ptc = ptc;
    }

    /**
     * @return the fin
     */
    public double getFin() {
        return fin;
    }

    /**
     * @param fin the fin to set
     */
    public void setFin(double fin) {
        this.fin = fin;
    }

    /**
     * @return the add
     */
    public double getAdd() {
        double partial_add = 0.0D;
        try {
          Connection connect = DriverManager.getConnection(this.driver, this.f_user, this.f_pass);
          Statement state = connect.createStatement();
          String query = "Select sum(add_amount) from cashtransactions.additionals where add_date = '" + this.dateHelp.getCurrentDate() + "'";
          ResultSet rset = state.executeQuery(query);
          while (rset.next()) {
            partial_add = rset.getDouble(1);
            System.out.println(rset.getDouble(1));
          } 
          state.close();
          connect.close();
          Connection connect1 = DriverManager.getConnection(this.driver, this.f_user, this.f_pass);
          Statement state1 = connect1.createStatement();
          String query1 = "Select sum(principal) from merlininventorydatabase.rescate where date = '" + this.dateHelp.getCurrentDate() + "' and subasta = 1 and tubos = 0";
          ResultSet rset1 = state1.executeQuery(query1);
          while (rset1.next())
            partial_add += rset1.getDouble(1); 
          state1.close();
          connect1.close();
        } catch (SQLException ex) {
          this.con.saveProp("last_error", String.valueOf(ex));
          Logger.getLogger(CashBreakdown.class.getName()).log(Level.SEVERE, (String)null, ex);
        } 
        System.out.println("PARTIAL_ADD = " + partial_add);
        return partial_add;
    }

    /**
     * @param add the add to set
     */
    public void setAdd(double add) {
        this.add = add;
    }

    /**
     * @return the bb
     */
    public double getBb() {
        try {
      Connection connect = DriverManager.getConnection(this.driver, this.f_user, this.f_pass);
      Statement state = connect.createStatement();
      String query = "Select beginning_balance from cashtransactions.running_balance where date = '" + this.dateHelp.getCurrentDate() + "'";
      System.out.println(query);
      ResultSet rset = state.executeQuery(query);
      while (rset.next()) {
        setBb(rset.getDouble(1));
        System.out.println(rset.getDouble(1));
      } 
      state.close();
      connect.close();
    } catch (SQLException ex) {
      this.con.saveProp("last_error", String.valueOf(ex));
      Logger.getLogger(CashBreakdown.class.getName()).log(Level.SEVERE, (String)null, ex);
    } 
    return this.bb;
    }

    /**
     * @param bb the bb to set
     */
    public void setBb(double bb) {
        this.bb = bb;
    }

    /**
     * @return the eb
     */
    public double getEb() {
        return eb;
    }

    /**
     * @param eb the eb to set
     */
    public void setEb(double eb) {
        this.eb = eb;
    }

    /**
     * @return the transfer
     */
    public double getTransfer() {
        try {
      Connection connect = DriverManager.getConnection(this.driver, this.f_user, this.f_pass);
      Statement state = connect.createStatement();
      String query = "Select sum(x_amount) from cashtransactions.transfer where x_date = '" + this.dateHelp.getCurrentDate() + "'";
      System.out.println(query);
      ResultSet rset = state.executeQuery(query);
      while (rset.next()) {
        setTransfer(rset.getDouble(1));
        System.out.println(rset.getDouble(1));
      } 
      state.close();
      connect.close();
    } catch (SQLException ex) {
      this.con.saveProp("last_error", String.valueOf(ex));
      Logger.getLogger(CashBreakdown.class.getName()).log(Level.SEVERE, (String)null, ex);
    } 
    return this.transfer;
    }

    /**
     * @param transfer the transfer to set
     */
    public void setTransfer(double transfer) {
        this.transfer = transfer;
    }

    /**
     * @return the in
     */
    public double getIn() {
        return in;
    }

    /**
     * @param in the in to set
     */
    public void setIn(double in) {
        this.in = in;
    }

    /**
     * @return the out
     */
    public double getOut() {
        return out;
    }

    /**
     * @param out the out to set
     */
    public void setOut(double out) {
        this.out = out;
    }

    /**
     * Creates new form CashBreakdownMer
     */
    public CashBreakdown(java.awt.Frame parent, boolean modal) {
        super(parent, modal);
        this.con = new Config();
        this.driver = "jdbc:mariadb://" + this.con.getProp("IP") + ":" + this.con.getProp("port") + "/cashtransactions";
        this.f_user = this.con.getProp("username");
        this.f_pass = this.con.getProp("password");
        this.dateHelp = new DateHelper();
        this.valuesEntered = false;
        this.formatter = new DecimalFormat("#,##0.00");
        this.coh = 0.0D;
        this.empeno = 0.0D;
        this.rescate = 0.0D;
        this.ai = 0.0D;
        this.interest = 0.0D;
        this.sc = 0.0D;
        this.ins = 0.0D;
        this.ptc = 0.0D;
        this.fin = 0.0D;
        this.add = 0.0D;
        this.bb = 0.0D;
        this.eb = 0.0D;
        this.transfer = 0.0D;
        this.in = 0.0D;
        this.out = 0.0D;
        this.pr = new ReportPrinter();
        initComponents();
    }
    
    Config con;
    private final String driver;
    private final String f_user;
    private final String f_pass;
    private DateHelper dateHelp;
    private boolean valuesEntered;
    private NumberFormat formatter;
    private double coh;
    private double empeno;
    private double rescate;
    private double ai;
    private double interest;
    private double sc;
    private double ins;
    private double ptc;
    private double fin;
    private double add;
    private double bb;
    private double eb;
    private double transfer;
    private double in;
    private double out;
    private ReportPrinter pr;
    
    
    public boolean isPtcPresent() throws SQLException {
    Connection connect = DriverManager.getConnection(this.driver, this.f_user, this.f_pass);
    Statement state = connect.createStatement();
    String query = "Select count(*) from cashtransactions.petty_cash where pty_date = '" + this.dateHelp.formatDate(Calendar.getInstance().getTime()) + "'";
    int count = 0;
    ResultSet rset = state.executeQuery(query);
    while (rset.next())
      count = rset.getInt(1); 
    state.close();
    connect.close();
    if (count > 0)
      return true; 
    return false;
  }
  
  public boolean isAddPresent() throws SQLException {
    Connection connect = DriverManager.getConnection(this.driver, this.f_user, this.f_pass);
    Statement state = connect.createStatement();
    String query = "Select count(*) from cashtransactions.additionals where add_date = '" + this.dateHelp.formatDate(Calendar.getInstance().getTime()) + "'";
    int count = 0;
    ResultSet rset = state.executeQuery(query);
    while (rset.next())
      count = rset.getInt(1); 
    state.close();
    connect.close();
    if (count > 0)
      return true; 
    return false;
  }
  
  public boolean isXfrPresent() throws SQLException {
    Connection connect = DriverManager.getConnection(this.driver, this.f_user, this.f_pass);
    Statement state = connect.createStatement();
    String query = "Select count(*) from cashtransactions.transfer where x_date = '" + this.dateHelp.formatDate(Calendar.getInstance().getTime()) + "'";
    int count = 0;
    ResultSet rset = state.executeQuery(query);
    while (rset.next())
      count = rset.getInt(1); 
    state.close();
    connect.close();
    if (count > 0)
      return true; 
    return false;
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
        jLabel2 = new javax.swing.JLabel();
        cov = new javax.swing.JTextField();
        tthou = new javax.swing.JTextField();
        tfhun = new javax.swing.JTextField();
        tthun = new javax.swing.JTextField();
        tohun = new javax.swing.JTextField();
        tfif = new javax.swing.JTextField();
        ttwen = new javax.swing.JTextField();
        tten = new javax.swing.JTextField();
        tfive = new javax.swing.JTextField();
        tone = new javax.swing.JTextField();
        tcent = new javax.swing.JTextField();
        ttce = new javax.swing.JTextField();
        tfce = new javax.swing.JTextField();
        gtot = new javax.swing.JTextField();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        jLabel9 = new javax.swing.JLabel();
        jLabel10 = new javax.swing.JLabel();
        jLabel11 = new javax.swing.JLabel();
        jLabel12 = new javax.swing.JLabel();
        jLabel13 = new javax.swing.JLabel();
        jLabel14 = new javax.swing.JLabel();
        thou = new javax.swing.JTextField();
        thun = new javax.swing.JTextField();
        fhun = new javax.swing.JTextField();
        hun = new javax.swing.JTextField();
        fif = new javax.swing.JTextField();
        twen = new javax.swing.JTextField();
        ten = new javax.swing.JTextField();
        five = new javax.swing.JTextField();
        one = new javax.swing.JTextField();
        cent = new javax.swing.JTextField();
        tenc = new javax.swing.JTextField();
        fivc = new javax.swing.JTextField();
        jSeparator1 = new javax.swing.JSeparator();
        jLabel15 = new javax.swing.JLabel();
        jLabel16 = new javax.swing.JLabel();
        ccRb = new javax.swing.JRadioButton();
        addRb = new javax.swing.JRadioButton();
        pcRb = new javax.swing.JRadioButton();
        ftRb = new javax.swing.JRadioButton();
        jButton1 = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);

        jLabel1.setFont(jLabel1.getFont().deriveFont(jLabel1.getFont().getStyle() | java.awt.Font.BOLD, jLabel1.getFont().getSize()+1));
        jLabel1.setForeground(new java.awt.Color(79, 119, 141));
        jLabel1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel1.setText("CASH BREAKDOWN");

        jLabel2.setText("Cash on Vault");

        cov.setHorizontalAlignment(javax.swing.JTextField.TRAILING);
        cov.setText("0.00");
        cov.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                covFocusLost(evt);
            }
        });
        cov.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                covActionPerformed(evt);
            }
        });

        tthou.setEditable(false);
        tthou.setHorizontalAlignment(javax.swing.JTextField.TRAILING);
        tthou.setText("0.00");
        tthou.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                tthouActionPerformed(evt);
            }
        });

        tfhun.setEditable(false);
        tfhun.setHorizontalAlignment(javax.swing.JTextField.TRAILING);
        tfhun.setText("0.00");
        tfhun.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                tfhunActionPerformed(evt);
            }
        });

        tthun.setEditable(false);
        tthun.setHorizontalAlignment(javax.swing.JTextField.TRAILING);
        tthun.setText("0.00");
        tthun.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                tthunActionPerformed(evt);
            }
        });

        tohun.setEditable(false);
        tohun.setHorizontalAlignment(javax.swing.JTextField.TRAILING);
        tohun.setText("0.00");
        tohun.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                tohunActionPerformed(evt);
            }
        });

        tfif.setEditable(false);
        tfif.setHorizontalAlignment(javax.swing.JTextField.TRAILING);
        tfif.setText("0.00");
        tfif.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                tfifActionPerformed(evt);
            }
        });

        ttwen.setEditable(false);
        ttwen.setHorizontalAlignment(javax.swing.JTextField.TRAILING);
        ttwen.setText("0.00");
        ttwen.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ttwenActionPerformed(evt);
            }
        });

        tten.setEditable(false);
        tten.setHorizontalAlignment(javax.swing.JTextField.TRAILING);
        tten.setText("0.00");
        tten.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ttenActionPerformed(evt);
            }
        });

        tfive.setEditable(false);
        tfive.setHorizontalAlignment(javax.swing.JTextField.TRAILING);
        tfive.setText("0.00");
        tfive.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                tfiveActionPerformed(evt);
            }
        });

        tone.setEditable(false);
        tone.setHorizontalAlignment(javax.swing.JTextField.TRAILING);
        tone.setText("0.00");
        tone.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                toneActionPerformed(evt);
            }
        });

        tcent.setEditable(false);
        tcent.setHorizontalAlignment(javax.swing.JTextField.TRAILING);
        tcent.setText("0.00");
        tcent.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                tcentActionPerformed(evt);
            }
        });

        ttce.setEditable(false);
        ttce.setHorizontalAlignment(javax.swing.JTextField.TRAILING);
        ttce.setText("0.00");
        ttce.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ttceActionPerformed(evt);
            }
        });

        tfce.setEditable(false);
        tfce.setHorizontalAlignment(javax.swing.JTextField.TRAILING);
        tfce.setText("0.00");
        tfce.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                tfceActionPerformed(evt);
            }
        });

        gtot.setEditable(false);
        gtot.setHorizontalAlignment(javax.swing.JTextField.TRAILING);
        gtot.setText("0.00");
        gtot.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                gtotActionPerformed(evt);
            }
        });

        jLabel3.setText("x 1000.00");

        jLabel4.setText("x  500.00");

        jLabel5.setText("x  200.00");

        jLabel6.setText("x  100.00");

        jLabel7.setText("x   50.00");

        jLabel8.setText("x   20.00");

        jLabel9.setText("x   10.00");

        jLabel10.setText("x    5.00");

        jLabel11.setText("x    1.00");

        jLabel12.setText("x    0.25");

        jLabel13.setText("x    0.10");

        jLabel14.setText("x    0.05");

        thou.setHorizontalAlignment(javax.swing.JTextField.TRAILING);
        thou.setText("0");
        thou.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                thouFocusGained(evt);
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                thouFocusLost(evt);
            }
        });
        thou.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                thouKeyPressed(evt);
            }
            public void keyTyped(java.awt.event.KeyEvent evt) {
                thouKeyTyped(evt);
            }
        });

        thun.setHorizontalAlignment(javax.swing.JTextField.TRAILING);
        thun.setText("0");
        thun.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                thunFocusGained(evt);
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                thunFocusLost(evt);
            }
        });
        thun.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                thunActionPerformed(evt);
            }
        });
        thun.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                thunKeyPressed(evt);
            }
            public void keyTyped(java.awt.event.KeyEvent evt) {
                thunKeyTyped(evt);
            }
        });

        fhun.setHorizontalAlignment(javax.swing.JTextField.TRAILING);
        fhun.setText("0");
        fhun.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                fhunFocusGained(evt);
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                fhunFocusLost(evt);
            }
        });
        fhun.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                fhunActionPerformed(evt);
            }
        });
        fhun.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                fhunKeyPressed(evt);
            }
            public void keyTyped(java.awt.event.KeyEvent evt) {
                fhunKeyTyped(evt);
            }
        });

        hun.setHorizontalAlignment(javax.swing.JTextField.TRAILING);
        hun.setText("0");
        hun.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                hunFocusGained(evt);
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                hunFocusLost(evt);
            }
        });
        hun.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                hunActionPerformed(evt);
            }
        });
        hun.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                hunKeyPressed(evt);
            }
            public void keyTyped(java.awt.event.KeyEvent evt) {
                hunKeyTyped(evt);
            }
        });

        fif.setHorizontalAlignment(javax.swing.JTextField.TRAILING);
        fif.setText("0");
        fif.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                fifFocusGained(evt);
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                fifFocusLost(evt);
            }
        });
        fif.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                fifActionPerformed(evt);
            }
        });
        fif.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                fifKeyPressed(evt);
            }
            public void keyTyped(java.awt.event.KeyEvent evt) {
                fifKeyTyped(evt);
            }
        });

        twen.setHorizontalAlignment(javax.swing.JTextField.TRAILING);
        twen.setText("0");
        twen.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                twenFocusGained(evt);
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                twenFocusLost(evt);
            }
        });
        twen.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                twenActionPerformed(evt);
            }
        });
        twen.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                twenKeyPressed(evt);
            }
            public void keyTyped(java.awt.event.KeyEvent evt) {
                twenKeyTyped(evt);
            }
        });

        ten.setHorizontalAlignment(javax.swing.JTextField.TRAILING);
        ten.setText("0");
        ten.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                tenFocusGained(evt);
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                tenFocusLost(evt);
            }
        });
        ten.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                tenActionPerformed(evt);
            }
        });
        ten.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                tenKeyPressed(evt);
            }
            public void keyTyped(java.awt.event.KeyEvent evt) {
                tenKeyTyped(evt);
            }
        });

        five.setHorizontalAlignment(javax.swing.JTextField.TRAILING);
        five.setText("0");
        five.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                fiveFocusGained(evt);
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                fiveFocusLost(evt);
            }
        });
        five.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                fiveActionPerformed(evt);
            }
        });
        five.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                fiveKeyPressed(evt);
            }
            public void keyTyped(java.awt.event.KeyEvent evt) {
                fiveKeyTyped(evt);
            }
        });

        one.setHorizontalAlignment(javax.swing.JTextField.TRAILING);
        one.setText("0");
        one.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                oneFocusGained(evt);
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                oneFocusLost(evt);
            }
        });
        one.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                oneActionPerformed(evt);
            }
        });
        one.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                oneKeyPressed(evt);
            }
            public void keyTyped(java.awt.event.KeyEvent evt) {
                oneKeyTyped(evt);
            }
        });

        cent.setHorizontalAlignment(javax.swing.JTextField.TRAILING);
        cent.setText("0");
        cent.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                centFocusGained(evt);
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                centFocusLost(evt);
            }
        });
        cent.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                centActionPerformed(evt);
            }
        });
        cent.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                centKeyPressed(evt);
            }
            public void keyTyped(java.awt.event.KeyEvent evt) {
                centKeyTyped(evt);
            }
        });

        tenc.setHorizontalAlignment(javax.swing.JTextField.TRAILING);
        tenc.setText("0");
        tenc.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                tencFocusGained(evt);
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                tencFocusLost(evt);
            }
        });
        tenc.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                tencActionPerformed(evt);
            }
        });
        tenc.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                tencKeyPressed(evt);
            }
            public void keyTyped(java.awt.event.KeyEvent evt) {
                tencKeyTyped(evt);
            }
        });

        fivc.setHorizontalAlignment(javax.swing.JTextField.TRAILING);
        fivc.setText("0");
        fivc.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                fivcFocusGained(evt);
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                fivcFocusLost(evt);
            }
        });
        fivc.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                fivcActionPerformed(evt);
            }
        });
        fivc.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                fivcKeyPressed(evt);
            }
            public void keyTyped(java.awt.event.KeyEvent evt) {
                fivcKeyTyped(evt);
            }
        });

        jLabel15.setText("Total");

        jLabel16.setText("Please select reports to be printed:");

        ccRb.setSelected(true);
        ccRb.setText("Cash Count");

        addRb.setSelected(true);
        addRb.setText("Additionals");
        addRb.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                addRbActionPerformed(evt);
            }
        });

        pcRb.setSelected(true);
        pcRb.setText("Petty Cash");

        ftRb.setSelected(true);
        ftRb.setText("Fund Transfer");

        jButton1.setBackground(new java.awt.Color(79, 119, 141));
        jButton1.setForeground(new java.awt.Color(255, 255, 255));
        jButton1.setText("Save and Print Cash Report");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(0, 4, Short.MAX_VALUE)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                                            .addComponent(jLabel15)
                                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                            .addComponent(gtot, javax.swing.GroupLayout.PREFERRED_SIZE, 122, javax.swing.GroupLayout.PREFERRED_SIZE))
                                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                            .addGroup(layout.createSequentialGroup()
                                                .addComponent(jLabel2)
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                                .addComponent(cov, javax.swing.GroupLayout.PREFERRED_SIZE, 122, javax.swing.GroupLayout.PREFERRED_SIZE))
                                            .addGroup(layout.createSequentialGroup()
                                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                    .addComponent(thou, javax.swing.GroupLayout.PREFERRED_SIZE, 51, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                    .addComponent(fhun, javax.swing.GroupLayout.PREFERRED_SIZE, 51, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                    .addComponent(thun, javax.swing.GroupLayout.PREFERRED_SIZE, 51, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                    .addComponent(hun, javax.swing.GroupLayout.PREFERRED_SIZE, 51, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                    .addComponent(fif, javax.swing.GroupLayout.PREFERRED_SIZE, 51, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                    .addComponent(twen, javax.swing.GroupLayout.PREFERRED_SIZE, 51, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                    .addComponent(ten, javax.swing.GroupLayout.PREFERRED_SIZE, 51, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                    .addComponent(five, javax.swing.GroupLayout.PREFERRED_SIZE, 51, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                    .addComponent(one, javax.swing.GroupLayout.PREFERRED_SIZE, 51, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                    .addComponent(cent, javax.swing.GroupLayout.PREFERRED_SIZE, 51, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                    .addComponent(tenc, javax.swing.GroupLayout.PREFERRED_SIZE, 51, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                    .addComponent(fivc, javax.swing.GroupLayout.PREFERRED_SIZE, 51, javax.swing.GroupLayout.PREFERRED_SIZE))
                                                .addGap(18, 18, 18)
                                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                                                        .addComponent(jLabel4)
                                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                                        .addComponent(tfhun, javax.swing.GroupLayout.PREFERRED_SIZE, 122, javax.swing.GroupLayout.PREFERRED_SIZE))
                                                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                                                        .addComponent(jLabel5)
                                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                                        .addComponent(tthun, javax.swing.GroupLayout.PREFERRED_SIZE, 122, javax.swing.GroupLayout.PREFERRED_SIZE))
                                                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                                                        .addComponent(jLabel6)
                                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                                        .addComponent(tohun, javax.swing.GroupLayout.PREFERRED_SIZE, 122, javax.swing.GroupLayout.PREFERRED_SIZE))
                                                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                                                        .addComponent(jLabel7)
                                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                                        .addComponent(tfif, javax.swing.GroupLayout.PREFERRED_SIZE, 122, javax.swing.GroupLayout.PREFERRED_SIZE))
                                                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                                                        .addComponent(jLabel8)
                                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                                        .addComponent(ttwen, javax.swing.GroupLayout.PREFERRED_SIZE, 122, javax.swing.GroupLayout.PREFERRED_SIZE))
                                                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                                                        .addComponent(jLabel9)
                                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                                        .addComponent(tten, javax.swing.GroupLayout.PREFERRED_SIZE, 122, javax.swing.GroupLayout.PREFERRED_SIZE))
                                                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                                                        .addComponent(jLabel10)
                                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                                        .addComponent(tfive, javax.swing.GroupLayout.PREFERRED_SIZE, 122, javax.swing.GroupLayout.PREFERRED_SIZE))
                                                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                                                        .addComponent(jLabel11)
                                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                                        .addComponent(tone, javax.swing.GroupLayout.PREFERRED_SIZE, 122, javax.swing.GroupLayout.PREFERRED_SIZE))
                                                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                                                        .addComponent(jLabel12)
                                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                                        .addComponent(tcent, javax.swing.GroupLayout.PREFERRED_SIZE, 122, javax.swing.GroupLayout.PREFERRED_SIZE))
                                                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                                                        .addComponent(jLabel13)
                                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                                        .addComponent(ttce, javax.swing.GroupLayout.PREFERRED_SIZE, 122, javax.swing.GroupLayout.PREFERRED_SIZE))
                                                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                                                        .addComponent(jLabel14)
                                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                                        .addComponent(tfce, javax.swing.GroupLayout.PREFERRED_SIZE, 122, javax.swing.GroupLayout.PREFERRED_SIZE))
                                                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                                                        .addComponent(jLabel3)
                                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                                        .addComponent(tthou, javax.swing.GroupLayout.PREFERRED_SIZE, 122, javax.swing.GroupLayout.PREFERRED_SIZE))))))
                                    .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 269, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(10, 10, 10))
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jSeparator1, javax.swing.GroupLayout.PREFERRED_SIZE, 264, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jLabel16)
                                    .addGroup(layout.createSequentialGroup()
                                        .addGap(18, 18, 18)
                                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                            .addGroup(layout.createSequentialGroup()
                                                .addComponent(ccRb)
                                                .addGap(18, 18, 18)
                                                .addComponent(pcRb))
                                            .addGroup(layout.createSequentialGroup()
                                                .addComponent(addRb)
                                                .addGap(21, 21, 21)
                                                .addComponent(ftRb)))))
                                .addContainerGap())))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addComponent(jButton1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addContainerGap())))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(12, 12, 12)
                .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 27, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel2)
                    .addComponent(cov, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(tthou, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel3)
                    .addComponent(thou, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(tfhun, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel4)
                    .addComponent(fhun, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(tthun, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel5)
                    .addComponent(thun, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(tohun, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel6)
                    .addComponent(hun, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(tfif, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel7)
                    .addComponent(fif, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(ttwen, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel8)
                    .addComponent(twen, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(tten, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel9)
                    .addComponent(ten, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(tfive, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel10)
                    .addComponent(five, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(tone, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel11)
                    .addComponent(one, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(tcent, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel12)
                    .addComponent(cent, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(ttce, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel13)
                    .addComponent(tenc, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(tfce, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel14)
                    .addComponent(fivc, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jSeparator1, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(gtot, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel15))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jLabel16)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(ccRb)
                    .addComponent(pcRb))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(addRb)
                    .addComponent(ftRb))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void covActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_covActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_covActionPerformed

    private void tthouActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_tthouActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_tthouActionPerformed

    private void tfhunActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_tfhunActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_tfhunActionPerformed

    private void tthunActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_tthunActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_tthunActionPerformed

    private void tohunActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_tohunActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_tohunActionPerformed

    private void tfifActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_tfifActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_tfifActionPerformed

    private void ttwenActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ttwenActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_ttwenActionPerformed

    private void ttenActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ttenActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_ttenActionPerformed

    private void tfiveActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_tfiveActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_tfiveActionPerformed

    private void toneActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_toneActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_toneActionPerformed

    private void tcentActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_tcentActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_tcentActionPerformed

    private void ttceActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ttceActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_ttceActionPerformed

    private void tfceActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_tfceActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_tfceActionPerformed

    private void gtotActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_gtotActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_gtotActionPerformed

    private void thunActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_thunActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_thunActionPerformed

    private void fhunActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_fhunActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_fhunActionPerformed

    private void hunActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_hunActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_hunActionPerformed

    private void fifActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_fifActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_fifActionPerformed

    private void twenActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_twenActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_twenActionPerformed

    private void tenActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_tenActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_tenActionPerformed

    private void fiveActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_fiveActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_fiveActionPerformed

    private void oneActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_oneActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_oneActionPerformed

    private void centActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_centActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_centActionPerformed

    private void tencActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_tencActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_tencActionPerformed

    private void fivcActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_fivcActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_fivcActionPerformed

    private void addRbActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_addRbActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_addRbActionPerformed

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        setValuesEntered(true);
    try {
      Connection connect = DriverManager.getConnection(this.driver, this.f_user, this.f_pass);
      Statement state = connect.createStatement();
      String query = "Replace into cashtransactions.breakdown (bd_date, cov, bd_thou, bd_fhun, bd_thun, bd_hund, bd_fift, bd_twen, bd_ten, bd_five, bd_one, bd_cen, bd_tenc, bd_fivc, bd_total, ovsh) values ('" + this.dateHelp.getDateTime() + "', " + this.cov.getText().replace(",", "") + ", " + this.thou.getText().replace(",", "") + ", " + this.fhun.getText().replace(",", "") + ", " + this.thun.getText().replace(",", "") + ", " + this.hun.getText().replace(",", "") + ", " + this.fif.getText().replace(",", "") + ", " + this.twen.getText().replace(",", "") + ", " + this.ten.getText().replace(",", "") + ", " + this.five.getText().replace(",", "") + ", " + this.one.getText().replace(",", "") + ", " + this.cent.getText().replace(",", "") + ", " + this.tenc.getText().replace(",", "") + ", " + this.fivc.getText().replace(",", "") + ", " + this.gtot.getText().replace(",", "") + ", " + (Double.parseDouble(this.gtot.getText().replace(",", "")) - getCoh()) + ")";
      state.executeUpdate(query);
      state.close();
      connect.close();
      Accounting acct = new Accounting();
      setVisible(false);
      File tempFolder = new File("C:\\MPIS\\tempRep");
      if (!tempFolder.exists())
        tempFolder.mkdir(); 
      Map<Object, Object> parameters = new HashMap<>();
      parameters.put("BB", Double.valueOf(getBb()));
      parameters.put("EMPENO", Double.valueOf(getEmpeno()));
      parameters.put("RESCATE", Double.valueOf(getRescate()));
      parameters.put("AI", Double.valueOf(getAi()));
      parameters.put("INT", Double.valueOf(getInterest()));
      parameters.put("SC", Double.valueOf(getSc()));
      parameters.put("INS", Double.valueOf(getIns()));
      parameters.put("AI", Double.valueOf(getAi()));
      parameters.put("ADD", Double.valueOf(getAdd()));
      parameters.put("FIN", Double.valueOf(getFin()));
      parameters.put("PTC", Double.valueOf(getPtc()));
      parameters.put("XFER", Double.valueOf(getTransfer()));
      parameters.put("DATE", this.dateHelp.formatDate(Calendar.getInstance().getTime()));
      Map<Object, Object> parameters2 = new HashMap<>();
      parameters2.put("DATE", this.dateHelp.formatDate(Calendar.getInstance().getTime()));
      if (this.con.getProp("branch").equalsIgnoreCase("Tangos")) {
        if (this.ccRb.isSelected()) {
          this.pr.setDestination(this.con.getProp("temp_folder").concat("tempCcPal.pdf"));
          this.pr.setReport_name("Cash Count Report");
          this.pr.printReport("/Reports/cashcount.jrxml", parameters);
        } 
        if (this.pcRb.isSelected() && isPtcPresent()) {
          this.pr.setDestination(this.con.getProp("temp_folder").concat("tempptcpal.pdf"));
          this.pr.setReport_name("Petty Cash Report");
          this.pr.printReport("/Reports/ptc.jrxml", parameters2);
        } 
        if (this.ftRb.isSelected() && isXfrPresent()) {
          this.pr.setDestination(this.con.getProp("temp_folder").concat("tempxfrpal.pdf"));
          this.pr.setReport_name("Transfer Fund Report");
          this.pr.printReport("/Reports/transfer_report.jrxml", parameters2);
        } 
        if (this.addRb.isSelected() && isAddPresent()) {
          this.pr.setDestination(this.con.getProp("temp_folder").concat("tempaddpal.pdf"));
          this.pr.setReport_name("Additional Funds Report");
          this.pr.printReport("/Reports/additional_report.jrxml", parameters2);
        } 
      } else {
        if (this.ccRb.isSelected()) {
          this.pr.setDestination(this.con.getProp("temp_folder").concat("tempCcPal.pdf"));
          this.pr.setReport_name("Cash Count Report");
          this.pr.printDotMatrix("/Reports/cashcount.jrxml", parameters);
        } 
        if (this.pcRb.isSelected() && isPtcPresent()) {
          this.pr.setDestination(this.con.getProp("temp_folder").concat("tempptcpal.pdf"));
          this.pr.setReport_name("Petty Cash Report");
          this.pr.printDotMatrix("/Reports/ptc.jrxml", parameters2);
        } 
        if (this.ftRb.isSelected() && isXfrPresent()) {
          this.pr.setDestination(this.con.getProp("temp_folder").concat("tempxfrpal.pdf"));
          this.pr.setReport_name("Transfer Fund Report");
          this.pr.printDotMatrix("/Reports/transfer_report.jrxml", parameters2);
        } 
        if (this.addRb.isSelected() && isAddPresent()) {
          this.pr.setDestination(this.con.getProp("temp_folder").concat("tempaddpal.pdf"));
          this.pr.setReport_name("Additional Funds Report");
          this.pr.printDotMatrix("/Reports/additional_report.jrxml", parameters2);
        } 
      } 
    } catch (SQLException ex) {
      this.con.saveProp("last_error", String.valueOf(ex));
      JOptionPane.showMessageDialog(null, "Cash Breakdown Save Error.");
      Logger.getLogger(CashBreakdown.class.getName()).log(Level.SEVERE, (String)null, ex);
    } 
    }//GEN-LAST:event_jButton1ActionPerformed

    private void thouKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_thouKeyTyped
        if (!Character.isDigit(evt.getKeyChar()))
      evt.consume(); 
    }//GEN-LAST:event_thouKeyTyped

    private void fhunKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_fhunKeyTyped
        if (!Character.isDigit(evt.getKeyChar()))
      evt.consume(); 
    }//GEN-LAST:event_fhunKeyTyped

    private void thunKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_thunKeyTyped
        if (!Character.isDigit(evt.getKeyChar()))
      evt.consume(); 
    }//GEN-LAST:event_thunKeyTyped

    private void hunKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_hunKeyTyped
        if (!Character.isDigit(evt.getKeyChar()))
      evt.consume(); 
    }//GEN-LAST:event_hunKeyTyped

    private void fifKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_fifKeyTyped
        if (!Character.isDigit(evt.getKeyChar()))
      evt.consume(); 
    }//GEN-LAST:event_fifKeyTyped

    private void twenKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_twenKeyTyped
        if (!Character.isDigit(evt.getKeyChar()))
      evt.consume(); 
    }//GEN-LAST:event_twenKeyTyped

    private void tenKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_tenKeyTyped
        if (!Character.isDigit(evt.getKeyChar()))
      evt.consume(); 
    }//GEN-LAST:event_tenKeyTyped

    private void fiveKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_fiveKeyTyped
        if (!Character.isDigit(evt.getKeyChar()))
      evt.consume(); 
    }//GEN-LAST:event_fiveKeyTyped

    private void oneKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_oneKeyTyped
        if (!Character.isDigit(evt.getKeyChar()))
      evt.consume(); 
    }//GEN-LAST:event_oneKeyTyped

    private void centKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_centKeyTyped
        if (!Character.isDigit(evt.getKeyChar()))
      evt.consume(); 
    }//GEN-LAST:event_centKeyTyped

    private void tencKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_tencKeyTyped
        if (!Character.isDigit(evt.getKeyChar()))
      evt.consume(); 
    }//GEN-LAST:event_tencKeyTyped

    private void fivcKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_fivcKeyTyped
        if (!Character.isDigit(evt.getKeyChar()))
      evt.consume(); 
    }//GEN-LAST:event_fivcKeyTyped

    private void thouFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_thouFocusLost
        this.tthou.setText(this.formatter.format(Double.parseDouble(this.thou.getText().replace(",", "")) * 1000.0D));
        this.gtot.setText(this.formatter.format(Double.parseDouble(this.tthou.getText().replace(",", "")) + Double.parseDouble(this.tfhun.getText().replace(",", "")) + Double.parseDouble(this.tthun.getText().replace(",", "")) + Double.parseDouble(this.tohun.getText().replace(",", "")) + Double.parseDouble(this.tfif.getText().replace(",", "")) + Double.parseDouble(this.ttwen.getText().replace(",", "")) + Double.parseDouble(this.tten.getText().replace(",", "")) + Double.parseDouble(this.tfive.getText().replace(",", "")) + Double.parseDouble(this.tone.getText().replace(",", "")) + Double.parseDouble(this.tcent.getText().replace(",", "")) + Double.parseDouble(this.cov.getText().replace(",", "")) + Double.parseDouble(this.ttce.getText().replace(",", "")) + Double.parseDouble(this.tfce.getText().replace(",", ""))));
    }//GEN-LAST:event_thouFocusLost

    private void fhunFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_fhunFocusLost
        this.tfhun.setText(this.formatter.format(Double.parseDouble(this.fhun.getText().replace(",", "")) * 500.0D));
        this.gtot.setText(this.formatter.format(Double.parseDouble(this.tthou.getText().replace(",", "")) + Double.parseDouble(this.tfhun.getText().replace(",", "")) + Double.parseDouble(this.tthun.getText().replace(",", "")) + Double.parseDouble(this.tohun.getText().replace(",", "")) + Double.parseDouble(this.tfif.getText().replace(",", "")) + Double.parseDouble(this.ttwen.getText().replace(",", "")) + Double.parseDouble(this.tten.getText().replace(",", "")) + Double.parseDouble(this.tfive.getText().replace(",", "")) + Double.parseDouble(this.tone.getText().replace(",", "")) + Double.parseDouble(this.tcent.getText().replace(",", "")) + Double.parseDouble(this.cov.getText().replace(",", "")) + Double.parseDouble(this.ttce.getText().replace(",", "")) + Double.parseDouble(this.tfce.getText().replace(",", ""))));
    }//GEN-LAST:event_fhunFocusLost

    private void thunFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_thunFocusLost
        this.tthun.setText(this.formatter.format(Double.parseDouble(this.thun.getText().replace(",", "")) * 200.0D));
        this.gtot.setText(this.formatter.format(Double.parseDouble(this.tthou.getText().replace(",", "")) + Double.parseDouble(this.tfhun.getText().replace(",", "")) + Double.parseDouble(this.tthun.getText().replace(",", "")) + Double.parseDouble(this.tohun.getText().replace(",", "")) + Double.parseDouble(this.tfif.getText().replace(",", "")) + Double.parseDouble(this.ttwen.getText().replace(",", "")) + Double.parseDouble(this.tten.getText().replace(",", "")) + Double.parseDouble(this.tfive.getText().replace(",", "")) + Double.parseDouble(this.tone.getText().replace(",", "")) + Double.parseDouble(this.tcent.getText().replace(",", "")) + Double.parseDouble(this.cov.getText().replace(",", "")) + Double.parseDouble(this.ttce.getText().replace(",", "")) + Double.parseDouble(this.tfce.getText().replace(",", ""))));
  
    }//GEN-LAST:event_thunFocusLost

    private void hunFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_hunFocusLost
        this.tohun.setText(this.formatter.format(Double.parseDouble(this.hun.getText().replace(",", "")) * 100.0D));
    this.gtot.setText(this.formatter.format(Double.parseDouble(this.tthou.getText().replace(",", "")) + Double.parseDouble(this.tfhun.getText().replace(",", "")) + Double.parseDouble(this.tthun.getText().replace(",", "")) + Double.parseDouble(this.tohun.getText().replace(",", "")) + Double.parseDouble(this.tfif.getText().replace(",", "")) + Double.parseDouble(this.ttwen.getText().replace(",", "")) + Double.parseDouble(this.tten.getText().replace(",", "")) + Double.parseDouble(this.tfive.getText().replace(",", "")) + Double.parseDouble(this.tone.getText().replace(",", "")) + Double.parseDouble(this.tcent.getText().replace(",", "")) + Double.parseDouble(this.cov.getText().replace(",", "")) + Double.parseDouble(this.ttce.getText().replace(",", "")) + Double.parseDouble(this.tfce.getText().replace(",", ""))));
  
    }//GEN-LAST:event_hunFocusLost

    private void fifFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_fifFocusLost
        this.tfif.setText(this.formatter.format(Double.parseDouble(this.fif.getText().replace(",", "")) * 50.0D));
    this.gtot.setText(this.formatter.format(Double.parseDouble(this.tthou.getText().replace(",", "")) + Double.parseDouble(this.tfhun.getText().replace(",", "")) + Double.parseDouble(this.tthun.getText().replace(",", "")) + Double.parseDouble(this.tohun.getText().replace(",", "")) + Double.parseDouble(this.tfif.getText().replace(",", "")) + Double.parseDouble(this.ttwen.getText().replace(",", "")) + Double.parseDouble(this.tten.getText().replace(",", "")) + Double.parseDouble(this.tfive.getText().replace(",", "")) + Double.parseDouble(this.tone.getText().replace(",", "")) + Double.parseDouble(this.tcent.getText().replace(",", "")) + Double.parseDouble(this.cov.getText().replace(",", "")) + Double.parseDouble(this.ttce.getText().replace(",", "")) + Double.parseDouble(this.tfce.getText().replace(",", ""))));
  
    }//GEN-LAST:event_fifFocusLost

    private void twenFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_twenFocusLost
        this.ttwen.setText(this.formatter.format(Double.parseDouble(this.twen.getText().replace(",", "")) * 20.0D));
    this.gtot.setText(this.formatter.format(Double.parseDouble(this.tthou.getText().replace(",", "")) + Double.parseDouble(this.tfhun.getText().replace(",", "")) + Double.parseDouble(this.tthun.getText().replace(",", "")) + Double.parseDouble(this.tohun.getText().replace(",", "")) + Double.parseDouble(this.tfif.getText().replace(",", "")) + Double.parseDouble(this.ttwen.getText().replace(",", "")) + Double.parseDouble(this.tten.getText().replace(",", "")) + Double.parseDouble(this.tfive.getText().replace(",", "")) + Double.parseDouble(this.tone.getText().replace(",", "")) + Double.parseDouble(this.tcent.getText().replace(",", "")) + Double.parseDouble(this.cov.getText().replace(",", "")) + Double.parseDouble(this.ttce.getText().replace(",", "")) + Double.parseDouble(this.tfce.getText().replace(",", ""))));
  
    }//GEN-LAST:event_twenFocusLost

    private void tenFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_tenFocusLost
        this.tten.setText(this.formatter.format(Double.parseDouble(this.ten.getText().replace(",", "")) * 10.0D));
    this.gtot.setText(this.formatter.format(Double.parseDouble(this.tthou.getText().replace(",", "")) + Double.parseDouble(this.tfhun.getText().replace(",", "")) + Double.parseDouble(this.tthun.getText().replace(",", "")) + Double.parseDouble(this.tohun.getText().replace(",", "")) + Double.parseDouble(this.tfif.getText().replace(",", "")) + Double.parseDouble(this.ttwen.getText().replace(",", "")) + Double.parseDouble(this.tten.getText().replace(",", "")) + Double.parseDouble(this.tfive.getText().replace(",", "")) + Double.parseDouble(this.tone.getText().replace(",", "")) + Double.parseDouble(this.tcent.getText().replace(",", "")) + Double.parseDouble(this.cov.getText().replace(",", "")) + Double.parseDouble(this.ttce.getText().replace(",", "")) + Double.parseDouble(this.tfce.getText().replace(",", ""))));
 
    }//GEN-LAST:event_tenFocusLost

    private void fiveFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_fiveFocusLost
        this.tfive.setText(this.formatter.format(Double.parseDouble(this.five.getText().replace(",", "")) * 5.0D));
    this.gtot.setText(this.formatter.format(Double.parseDouble(this.tthou.getText().replace(",", "")) + Double.parseDouble(this.tfhun.getText().replace(",", "")) + Double.parseDouble(this.tthun.getText().replace(",", "")) + Double.parseDouble(this.tohun.getText().replace(",", "")) + Double.parseDouble(this.tfif.getText().replace(",", "")) + Double.parseDouble(this.ttwen.getText().replace(",", "")) + Double.parseDouble(this.tten.getText().replace(",", "")) + Double.parseDouble(this.tfive.getText().replace(",", "")) + Double.parseDouble(this.tone.getText().replace(",", "")) + Double.parseDouble(this.tcent.getText().replace(",", "")) + Double.parseDouble(this.cov.getText().replace(",", "")) + Double.parseDouble(this.ttce.getText().replace(",", "")) + Double.parseDouble(this.tfce.getText().replace(",", ""))));
 
    }//GEN-LAST:event_fiveFocusLost

    private void oneFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_oneFocusLost
        this.tone.setText(this.formatter.format(Double.parseDouble(this.one.getText().replace(",", ""))));
    this.gtot.setText(this.formatter.format(Double.parseDouble(this.tthou.getText().replace(",", "")) + Double.parseDouble(this.tfhun.getText().replace(",", "")) + Double.parseDouble(this.tthun.getText().replace(",", "")) + Double.parseDouble(this.tohun.getText().replace(",", "")) + Double.parseDouble(this.tfif.getText().replace(",", "")) + Double.parseDouble(this.ttwen.getText().replace(",", "")) + Double.parseDouble(this.tten.getText().replace(",", "")) + Double.parseDouble(this.tfive.getText().replace(",", "")) + Double.parseDouble(this.tone.getText().replace(",", "")) + Double.parseDouble(this.tcent.getText().replace(",", "")) + Double.parseDouble(this.cov.getText().replace(",", "")) + Double.parseDouble(this.ttce.getText().replace(",", "")) + Double.parseDouble(this.tfce.getText().replace(",", ""))));

    }//GEN-LAST:event_oneFocusLost

    private void centFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_centFocusLost
       this.tcent.setText(this.formatter.format(Double.parseDouble(this.cent.getText().replace(",", "")) * 0.25D));
    this.gtot.setText(this.formatter.format(Double.parseDouble(this.tthou.getText().replace(",", "")) + Double.parseDouble(this.tfhun.getText().replace(",", "")) + Double.parseDouble(this.tthun.getText().replace(",", "")) + Double.parseDouble(this.tohun.getText().replace(",", "")) + Double.parseDouble(this.tfif.getText().replace(",", "")) + Double.parseDouble(this.ttwen.getText().replace(",", "")) + Double.parseDouble(this.tten.getText().replace(",", "")) + Double.parseDouble(this.tfive.getText().replace(",", "")) + Double.parseDouble(this.tone.getText().replace(",", "")) + Double.parseDouble(this.tcent.getText().replace(",", "")) + Double.parseDouble(this.cov.getText().replace(",", "")) + Double.parseDouble(this.ttce.getText().replace(",", "")) + Double.parseDouble(this.tfce.getText().replace(",", ""))));
  
    }//GEN-LAST:event_centFocusLost

    private void tencFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_tencFocusLost
        this.ttce.setText(this.formatter.format(Double.parseDouble(this.tenc.getText().replace(",", "")) * 0.1D));
    this.gtot.setText(this.formatter.format(Double.parseDouble(this.tthou.getText().replace(",", "")) + Double.parseDouble(this.tfhun.getText().replace(",", "")) + Double.parseDouble(this.tthun.getText().replace(",", "")) + Double.parseDouble(this.tohun.getText().replace(",", "")) + Double.parseDouble(this.tfif.getText().replace(",", "")) + Double.parseDouble(this.ttwen.getText().replace(",", "")) + Double.parseDouble(this.tten.getText().replace(",", "")) + Double.parseDouble(this.tfive.getText().replace(",", "")) + Double.parseDouble(this.tone.getText().replace(",", "")) + Double.parseDouble(this.tcent.getText().replace(",", "")) + Double.parseDouble(this.cov.getText().replace(",", "")) + Double.parseDouble(this.ttce.getText().replace(",", "")) + Double.parseDouble(this.tfce.getText().replace(",", ""))));
  
    }//GEN-LAST:event_tencFocusLost

    private void fivcFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_fivcFocusLost
        this.tfce.setText(this.formatter.format(Double.parseDouble(this.fivc.getText().replace(",", "")) * 0.05D));
    this.gtot.setText(this.formatter.format(Double.parseDouble(this.tthou.getText().replace(",", "")) + Double.parseDouble(this.tfhun.getText().replace(",", "")) + Double.parseDouble(this.tthun.getText().replace(",", "")) + Double.parseDouble(this.tohun.getText().replace(",", "")) + Double.parseDouble(this.tfif.getText().replace(",", "")) + Double.parseDouble(this.ttwen.getText().replace(",", "")) + Double.parseDouble(this.tten.getText().replace(",", "")) + Double.parseDouble(this.tfive.getText().replace(",", "")) + Double.parseDouble(this.tone.getText().replace(",", "")) + Double.parseDouble(this.tcent.getText().replace(",", "")) + Double.parseDouble(this.cov.getText().replace(",", "")) + Double.parseDouble(this.ttce.getText().replace(",", "")) + Double.parseDouble(this.tfce.getText().replace(",", ""))));
  
    }//GEN-LAST:event_fivcFocusLost

    private void covFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_covFocusLost
        this.cov.setText(this.formatter.format(Double.parseDouble(this.cov.getText().replace(",", ""))));
    this.gtot.setText(this.formatter.format(Double.parseDouble(this.tthou.getText().replace(",", "")) + Double.parseDouble(this.tfhun.getText().replace(",", "")) + Double.parseDouble(this.tthun.getText().replace(",", "")) + Double.parseDouble(this.tohun.getText().replace(",", "")) + Double.parseDouble(this.tfif.getText().replace(",", "")) + Double.parseDouble(this.ttwen.getText().replace(",", "")) + Double.parseDouble(this.tten.getText().replace(",", "")) + Double.parseDouble(this.tfive.getText().replace(",", "")) + Double.parseDouble(this.tone.getText().replace(",", "")) + Double.parseDouble(this.tcent.getText().replace(",", "")) + Double.parseDouble(this.cov.getText().replace(",", "")) + Double.parseDouble(this.ttce.getText().replace(",", "")) + Double.parseDouble(this.tfce.getText().replace(",", ""))));
  
    }//GEN-LAST:event_covFocusLost

    private void thouFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_thouFocusGained
        thou.selectAll();
    }//GEN-LAST:event_thouFocusGained

    private void fhunFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_fhunFocusGained
        fhun.selectAll();
    }//GEN-LAST:event_fhunFocusGained

    private void thunFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_thunFocusGained
        thun.selectAll();
    }//GEN-LAST:event_thunFocusGained

    private void hunFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_hunFocusGained
        hun.selectAll();
    }//GEN-LAST:event_hunFocusGained

    private void fifFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_fifFocusGained
        fif.selectAll();
    }//GEN-LAST:event_fifFocusGained

    private void twenFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_twenFocusGained
        twen.selectAll();
    }//GEN-LAST:event_twenFocusGained

    private void tenFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_tenFocusGained
        ten.selectAll();
    }//GEN-LAST:event_tenFocusGained

    private void fiveFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_fiveFocusGained
        five.selectAll();
    }//GEN-LAST:event_fiveFocusGained

    private void oneFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_oneFocusGained
        one.selectAll();
    }//GEN-LAST:event_oneFocusGained

    private void centFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_centFocusGained
        cent.selectAll();
    }//GEN-LAST:event_centFocusGained

    private void tencFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_tencFocusGained
        tenc.selectAll();
    }//GEN-LAST:event_tencFocusGained

    private void fivcFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_fivcFocusGained
        fivc.selectAll();
    }//GEN-LAST:event_fivcFocusGained

    private void thouKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_thouKeyPressed
        if (evt.getKeyCode() == 10)
            thouFocusLost((FocusEvent)null); 
    }//GEN-LAST:event_thouKeyPressed

    private void fhunKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_fhunKeyPressed
        if (evt.getKeyCode() == 10)
            fhunFocusLost((FocusEvent)null); 
    }//GEN-LAST:event_fhunKeyPressed

    private void thunKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_thunKeyPressed
        if (evt.getKeyCode() == 10)
      thunFocusLost((FocusEvent)null); 
    }//GEN-LAST:event_thunKeyPressed

    private void hunKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_hunKeyPressed
        if (evt.getKeyCode() == 10)
      hunFocusLost((FocusEvent)null); 
    }//GEN-LAST:event_hunKeyPressed

    private void fifKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_fifKeyPressed
        if (evt.getKeyCode() == 10)
      fifFocusLost((FocusEvent)null); 
    }//GEN-LAST:event_fifKeyPressed

    private void twenKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_twenKeyPressed
        if (evt.getKeyCode() == 10)
      twenFocusLost((FocusEvent)null); 
    }//GEN-LAST:event_twenKeyPressed

    private void tenKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_tenKeyPressed
        if (evt.getKeyCode() == 10)
      tenFocusLost((FocusEvent)null); 
    }//GEN-LAST:event_tenKeyPressed

    private void fiveKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_fiveKeyPressed
        if (evt.getKeyCode() == 10)
      fiveFocusLost((FocusEvent)null); 
    }//GEN-LAST:event_fiveKeyPressed

    private void oneKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_oneKeyPressed
        if (evt.getKeyCode() == 10)
      oneFocusLost((FocusEvent)null); 
    }//GEN-LAST:event_oneKeyPressed

    private void centKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_centKeyPressed
        if (evt.getKeyCode() == 10)
      centFocusLost((FocusEvent)null); 
    }//GEN-LAST:event_centKeyPressed

    private void tencKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_tencKeyPressed
        if (evt.getKeyCode() == 10)
      tencFocusLost((FocusEvent)null); 
    }//GEN-LAST:event_tencKeyPressed

    private void fivcKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_fivcKeyPressed
        if (evt.getKeyCode() == 10)
      fivcFocusLost((FocusEvent)null); 
    }//GEN-LAST:event_fivcKeyPressed

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
            java.util.logging.Logger.getLogger(CashBreakdown.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(CashBreakdown.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(CashBreakdown.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(CashBreakdown.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>

        /* Create and display the dialog */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                CashBreakdown dialog = new CashBreakdown(new javax.swing.JFrame(), true);
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
    private javax.swing.JRadioButton addRb;
    private javax.swing.JRadioButton ccRb;
    private javax.swing.JTextField cent;
    private javax.swing.JTextField cov;
    private javax.swing.JTextField fhun;
    private javax.swing.JTextField fif;
    private javax.swing.JTextField fivc;
    private javax.swing.JTextField five;
    private javax.swing.JRadioButton ftRb;
    private javax.swing.JTextField gtot;
    private javax.swing.JTextField hun;
    private javax.swing.JButton jButton1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel15;
    private javax.swing.JLabel jLabel16;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JTextField one;
    private javax.swing.JRadioButton pcRb;
    private javax.swing.JTextField tcent;
    private javax.swing.JTextField ten;
    private javax.swing.JTextField tenc;
    private javax.swing.JTextField tfce;
    private javax.swing.JTextField tfhun;
    private javax.swing.JTextField tfif;
    private javax.swing.JTextField tfive;
    private javax.swing.JTextField thou;
    private javax.swing.JTextField thun;
    private javax.swing.JTextField tohun;
    private javax.swing.JTextField tone;
    private javax.swing.JTextField ttce;
    private javax.swing.JTextField tten;
    private javax.swing.JTextField tthou;
    private javax.swing.JTextField tthun;
    private javax.swing.JTextField ttwen;
    private javax.swing.JTextField twen;
    // End of variables declaration//GEN-END:variables
}
