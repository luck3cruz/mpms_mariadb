/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
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
 * @author Lucky
 */
public class CashBreakdown extends javax.swing.JDialog {

    /**
     * Creates new form CashBreakdown
     */
    public CashBreakdown(java.awt.Frame parent, boolean modal) {
        super(parent, modal);
        initComponents();
    }
    
    Config con = new Config();
/*   64 */   private final String driver = "jdbc:mariadb://" + this.con.getProp("IP") + ":" + this.con.getProp("port") + "/cashtransactions";
/*   65 */   private final String f_user = this.con.getProp("username");
/*   66 */   private final String f_pass = this.con.getProp("password");
    private ReportComposer repC = new ReportComposer();;
    private DateHelper dateHelp = new DateHelper();
/*   69 */   private DecimalHelper decHelp = new DecimalHelper();
    private boolean valuesEntered = false;
/*      */   private NumberFormat formatter = new DecimalFormat("#,##0.00");
/*      */   private double coh = 0.0D;
/*      */   private double empeno = 0.0D;
/*      */   private double rescate = 0.0D;
/*      */   private double ai = 0.0D;
/*      */   private double interest = 0.0D;
/*      */   private double sc = 0.0D;
/*      */   private double ins = 0.0D;
/*      */   private double ptc = 0.0D;
/*      */   private double fin = 0.0D;
/*      */   private double add = 0.0D;
/*      */   private double bb = 0.0D;
/*      */   private double eb = 0.0D;
/*      */   private double transfer = 0.0D;
/*      */   private double in = 0.0D;
/*      */   private double out = 0.0D;
/*      */   private ReportPrinter pr = new ReportPrinter();

    public boolean isPtcPresent() throws SQLException {
/*   65 */     Connection connect = DriverManager.getConnection(this.driver, this.f_user, this.f_pass);
/*   66 */     Statement state = connect.createStatement();
/*   67 */     String query = "Select count(*) from cashtransactions.petty_cash where pty_date = '" + this.dateHelp.formatDate(Calendar.getInstance().getTime()) + "'";
/*   68 */     int count = 0;
/*   69 */     ResultSet rset = state.executeQuery(query);
/*   70 */     while (rset.next()) {
/*   71 */       count = rset.getInt(1);
/*      */     }
/*   73 */     if (count > 0) return true; 
/*   74 */     return false;
/*      */   }
    
    public boolean isAddPresent() throws SQLException {
/*   78 */     Connection connect = DriverManager.getConnection(this.driver, this.f_user, this.f_pass);
/*   79 */     Statement state = connect.createStatement();
/*   80 */     String query = "Select count(*) from cashtransactions.additionals where add_date = '" + this.dateHelp.formatDate(Calendar.getInstance().getTime()) + "'";
/*   81 */     int count = 0;
/*   82 */     ResultSet rset = state.executeQuery(query);
/*   83 */     while (rset.next()) {
/*   84 */       count = rset.getInt(1);
/*      */     }
/*   86 */     if (count > 0) return true; 
/*   87 */     return false;
/*      */   }
/*      */   
/*      */   public boolean isXfrPresent() throws SQLException {
/*   91 */     Connection connect = DriverManager.getConnection(this.driver, this.f_user, this.f_pass);
/*   92 */     Statement state = connect.createStatement();
/*   93 */     String query = "Select count(*) from cashtransactions.transfer where x_date = '" + this.dateHelp.formatDate(Calendar.getInstance().getTime()) + "'";
/*   94 */     int count = 0;
/*   95 */     ResultSet rset = state.executeQuery(query);
/*   96 */     while (rset.next()) {
/*   97 */       count = rset.getInt(1);
/*      */     }
/*   99 */     if (count > 0) return true; 
/*  100 */     return false;
/*      */   }

public boolean isValuesEntered() {
/* 1382 */     return this.valuesEntered;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setValuesEntered(boolean valuesEntered) {
/* 1389 */     this.valuesEntered = valuesEntered;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public double getCoh() {
/* 1396 */     return this.coh;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setCoh(double coh) {
/* 1403 */     this.coh = coh;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public double getEmpeno() {
/*      */     try {
/* 1411 */       Connection connect = DriverManager.getConnection(this.driver, this.f_user, this.f_pass);
/* 1412 */       Statement state = connect.createStatement();
/* 1413 */       String query = "Select sum(principal) from merlininventorydatabase.empeno where date = '" + this.dateHelp.getCurrentDate() + "'";
/* 1414 */       System.out.println(query);
/* 1415 */       ResultSet rset = state.executeQuery(query);
/* 1416 */       while (rset.next()) {
/* 1417 */         setEmpeno(rset.getDouble(1));
/* 1418 */         this.repC.setEmpenoamt(this.formatter.format(rset.getDouble(1)));
/* 1419 */         System.out.println(rset.getDouble(1));
/*      */       } 
/* 1421 */       state.close();
/* 1422 */       connect.close();
/* 1423 */     } catch (SQLException ex) {
/* 1424 */       this.con.saveProp("mpis_last_error", String.valueOf(ex));
/* 1425 */       Logger.getLogger(CashBreakdown.class.getName()).log(Level.SEVERE, (String)null, ex);
/*      */     } 
/* 1427 */     return this.empeno;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setEmpeno(double empeno) {
/* 1434 */     this.empeno = empeno;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public double getRescate() {
/*      */     try {
/* 1442 */       Connection connect = DriverManager.getConnection(this.driver, this.f_user, this.f_pass);
/* 1443 */       Statement state = connect.createStatement();
/* 1444 */       String query = "Select sum(principal) from merlininventorydatabase.rescate where subasta = 0 and date = '" + this.dateHelp.getCurrentDate() + "'";
/* 1445 */       System.out.println(query);
/* 1446 */       ResultSet rset = state.executeQuery(query);
/* 1447 */       while (rset.next()) {
/* 1448 */         setRescate(rset.getDouble(1));
/* 1449 */         this.repC.setRescateamt(this.formatter.format(rset.getDouble(1)));
/* 1450 */         System.out.println(rset.getDouble(1));
/*      */       } 
/* 1452 */       state.close();
/* 1453 */       connect.close();
/* 1454 */     } catch (SQLException ex) {
/* 1455 */       this.con.saveProp("mpis_last_error", String.valueOf(ex));
/* 1456 */       Logger.getLogger(CashBreakdown.class.getName()).log(Level.SEVERE, (String)null, ex);
/*      */     } 
/* 1458 */     return this.rescate;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setRescate(double rescate) {
/* 1465 */     this.rescate = rescate;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public double getAi() {
/*      */     try {
/* 1473 */       Connection connect = DriverManager.getConnection(this.driver, this.f_user, this.f_pass);
/* 1474 */       Statement state = connect.createStatement();
/* 1475 */       String query = "Select sum(ai) from merlininventorydatabase.empeno where date = '" + this.dateHelp.getCurrentDate() + "'";
/* 1476 */       System.out.println(query);
/* 1477 */       ResultSet rset = state.executeQuery(query);
/* 1478 */       while (rset.next()) {
/* 1479 */         setAi(rset.getDouble(1));
/* 1480 */         this.repC.setAi(this.formatter.format(rset.getDouble(1)));
/*      */       } 
/* 1482 */       state.close();
/* 1483 */       connect.close();
/* 1484 */     } catch (SQLException ex) {
/* 1485 */       this.con.saveProp("mpis_last_error", String.valueOf(ex));
/* 1486 */       Logger.getLogger(CashBreakdown.class.getName()).log(Level.SEVERE, (String)null, ex);
/*      */     } 
/* 1488 */     return this.ai;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setAi(double ai) {
/* 1495 */     this.ai = ai;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public double getInterest() {
/*      */     try {
/* 1503 */       Connection connect = DriverManager.getConnection(this.driver, this.f_user, this.f_pass);
/* 1504 */       Statement state = connect.createStatement();
/* 1505 */       String query = "Select sum(interest) from merlininventorydatabase.rescate where subasta = 0 and date = '" + this.dateHelp.getCurrentDate() + "'";
/* 1506 */       System.out.println(query);
/* 1507 */       ResultSet rset = state.executeQuery(query);
/* 1508 */       while (rset.next()) {
/* 1509 */         setInterest(rset.getDouble(1));
/* 1510 */         System.out.println(rset.getDouble(1));
/* 1511 */         this.repC.setInterest(this.formatter.format(rset.getDouble(1)));
/*      */       } 
/* 1513 */       state.close();
/* 1514 */       connect.close();
/* 1515 */     } catch (SQLException ex) {
/* 1516 */       this.con.saveProp("mpis_last_error", String.valueOf(ex));
/* 1517 */       Logger.getLogger(CashBreakdown.class.getName()).log(Level.SEVERE, (String)null, ex);
/*      */     } 
/* 1519 */     return this.interest;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setInterest(double interest) {
/* 1526 */     this.interest = interest;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public double getSc() {
/*      */     try {
/* 1534 */       Connection connect = DriverManager.getConnection(this.driver, this.f_user, this.f_pass);
/* 1535 */       Statement state = connect.createStatement();
/* 1536 */       String query = "Select sum(sc) from merlininventorydatabase.empeno where date = '" + this.dateHelp.getCurrentDate() + "'";
/* 1537 */       System.out.println(query);
/* 1538 */       ResultSet rset = state.executeQuery(query);
/* 1539 */       while (rset.next()) {
/* 1540 */         setSc(rset.getDouble(1));
/* 1541 */         this.repC.setSc(this.formatter.format(rset.getDouble(1)));
/*      */       } 
/* 1543 */       state.close();
/* 1544 */       connect.close();
/* 1545 */     } catch (SQLException ex) {
/* 1546 */       this.con.saveProp("mpis_last_error", String.valueOf(ex));
/* 1547 */       Logger.getLogger(CashBreakdown.class.getName()).log(Level.SEVERE, (String)null, ex);
/*      */     } 
/* 1549 */     return this.sc;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setSc(double sc) {
/* 1556 */     this.sc = sc;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public double getIns() {
/*      */     try {
/* 1564 */       Connection connect = DriverManager.getConnection(this.driver, this.f_user, this.f_pass);
/* 1565 */       Statement state = connect.createStatement();
/* 1566 */       String query = "Select sum(insurance) from merlininventorydatabase.empeno where date = '" + this.dateHelp.getCurrentDate() + "'";
/* 1567 */       System.out.println(query);
/* 1568 */       ResultSet rset = state.executeQuery(query);
/* 1569 */       while (rset.next()) {
/* 1570 */         setIns(rset.getDouble(1));
/* 1571 */         this.repC.setIns(this.formatter.format(rset.getDouble(1)));
/*      */       } 
/* 1573 */       state.close();
/* 1574 */       connect.close();
/* 1575 */     } catch (SQLException ex) {
/* 1576 */       this.con.saveProp("mpis_last_error", String.valueOf(ex));
/* 1577 */       Logger.getLogger(CashBreakdown.class.getName()).log(Level.SEVERE, (String)null, ex);
/*      */     } 
/* 1579 */     return this.ins;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setIns(double ins) {
/* 1586 */     this.ins = ins;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public double getPtc() {
/*      */     try {
/* 1594 */       Connection connect = DriverManager.getConnection(this.driver, this.f_user, this.f_pass);
/* 1595 */       Statement state = connect.createStatement();
/* 1596 */       String query = "Select sum(pty_amount) from cashtransactions.petty_cash where pty_date = '" + this.dateHelp.getCurrentDate() + "'";
/* 1597 */       System.out.println(query);
/* 1598 */       ResultSet rset = state.executeQuery(query);
/* 1599 */       while (rset.next()) {
/* 1600 */         setPtc(rset.getDouble(1));
/* 1601 */         this.repC.setPtcamt(this.formatter.format(rset.getDouble(1)));
/* 1602 */         System.out.println(rset.getDouble(1));
/*      */       } 
/* 1604 */       state.close();
/* 1605 */       connect.close();
/* 1606 */     } catch (SQLException ex) {
/* 1607 */       this.con.saveProp("mpis_last_error", String.valueOf(ex));
/* 1608 */       Logger.getLogger(CashBreakdown.class.getName()).log(Level.SEVERE, (String)null, ex);
/*      */     } 
/* 1610 */     return this.ptc;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setPtc(double ptc) {
/* 1617 */     this.ptc = ptc;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public double getFin() {
/* 1624 */     return this.fin;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setFin(double fin) {
/* 1631 */     this.fin = fin;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public double getAdd() {
/* 1638 */     double partial_add = 0.0D;
/*      */     try {
/* 1640 */       Connection connect = DriverManager.getConnection(this.driver, this.f_user, this.f_pass);
/* 1641 */       Statement state = connect.createStatement();
/* 1642 */       String query = "Select sum(add_amount) from cashtransactions.additionals where add_date = '" + this.dateHelp.getCurrentDate() + "'";
/*      */       
/* 1644 */       ResultSet rset = state.executeQuery(query);
/* 1645 */       while (rset.next()) {
/* 1646 */         partial_add = rset.getDouble(1);
/* 1647 */         System.out.println(rset.getDouble(1));
/*      */       } 
/* 1649 */       state.close();
/* 1650 */       connect.close();
/* 1651 */       Connection connect1 = DriverManager.getConnection(this.driver, this.f_user, this.f_pass);
/* 1652 */       Statement state1 = connect1.createStatement();
/* 1653 */       String query1 = "Select sum(principal) from merlininventorydatabase.rescate where date = '" + this.dateHelp.getCurrentDate() + "' and subasta = 1 and tubos = 0";
/* 1654 */       ResultSet rset1 = state1.executeQuery(query1);
/* 1655 */       while (rset1.next()) {
/* 1656 */         partial_add += rset1.getDouble(1);
/*      */         
/* 1658 */         this.repC.setAddlamt(this.formatter.format(partial_add));
/*      */       } 
/* 1660 */       state1.close();
/* 1661 */       connect1.close();
/* 1662 */     } catch (SQLException ex) {
/* 1663 */       this.con.saveProp("mpis_last_error", String.valueOf(ex));
/* 1664 */       Logger.getLogger(CashBreakdown.class.getName()).log(Level.SEVERE, (String)null, ex);
/*      */     } 
/* 1666 */     System.out.println("PARTIAL_ADD = " + partial_add);
/* 1667 */     return partial_add;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setAdd(double add) {
/* 1674 */     this.add = add;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public double getBb() {
/*      */     try {
/* 1682 */       Connection connect = DriverManager.getConnection(this.driver, this.f_user, this.f_pass);
/* 1683 */       Statement state = connect.createStatement();
/* 1684 */       String query = "Select beginning_balance from cashtransactions.running_balance where date = '" + this.dateHelp.getCurrentDate() + "'";
/* 1685 */       System.out.println(query);
/* 1686 */       ResultSet rset = state.executeQuery(query);
/* 1687 */       while (rset.next()) {
/* 1688 */         setBb(rset.getDouble(1));
/* 1689 */         System.out.println(rset.getDouble(1));
/* 1690 */         this.repC.setBegbal(this.formatter.format(rset.getDouble(1)));
/*      */       } 
/* 1692 */       state.close();
/* 1693 */       connect.close();
/* 1694 */     } catch (SQLException ex) {
/* 1695 */       this.con.saveProp("mpis_last_error", String.valueOf(ex));
/* 1696 */       Logger.getLogger(CashBreakdown.class.getName()).log(Level.SEVERE, (String)null, ex);
/*      */     } 
/* 1698 */     return this.bb;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setBb(double bb) {
/* 1705 */     this.bb = bb;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public double getEb() {
/* 1712 */     return this.eb;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setEb(double eb) {
/* 1719 */     this.eb = eb;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public double getTransfer() {
/*      */     try {
/* 1727 */       Connection connect = DriverManager.getConnection(this.driver, this.f_user, this.f_pass);
/* 1728 */       Statement state = connect.createStatement();
/* 1729 */       String query = "Select sum(x_amount) from cashtransactions.transfer where x_date = '" + this.dateHelp.getCurrentDate() + "'";
/* 1730 */       System.out.println(query);
/* 1731 */       ResultSet rset = state.executeQuery(query);
/* 1732 */       while (rset.next()) {
/* 1733 */         setTransfer(rset.getDouble(1));
/* 1734 */         this.repC.setExpected(this.formatter.format(rset.getDouble(1)));
/* 1735 */         System.out.println(rset.getDouble(1));
/*      */       } 
/* 1737 */       state.close();
/* 1738 */       connect.close();
/* 1739 */     } catch (SQLException ex) {
/* 1740 */       this.con.saveProp("mpis_last_error", String.valueOf(ex));
/* 1741 */       Logger.getLogger(CashBreakdown.class.getName()).log(Level.SEVERE, (String)null, ex);
/* 1742 */     }  return this.transfer;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setTransfer(double transfer) {
/* 1749 */     this.transfer = transfer;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public double getIn() {
/* 1756 */     return this.in;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setIn(double in) {
/* 1763 */     this.in = in;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public double getOut() {
/* 1770 */     return this.out;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setOut(double out) {
/* 1777 */     this.out = out;
/*      */   }
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
        thou = new javax.swing.JTextField();
        tthou = new javax.swing.JTextField();
        fhun = new javax.swing.JTextField();
        tfhun = new javax.swing.JTextField();
        thun = new javax.swing.JTextField();
        tthun = new javax.swing.JTextField();
        hun = new javax.swing.JTextField();
        tohun = new javax.swing.JTextField();
        fif = new javax.swing.JTextField();
        tfif = new javax.swing.JTextField();
        twen = new javax.swing.JTextField();
        ttwen = new javax.swing.JTextField();
        ten = new javax.swing.JTextField();
        tten = new javax.swing.JTextField();
        five = new javax.swing.JTextField();
        tfive = new javax.swing.JTextField();
        one = new javax.swing.JTextField();
        tone = new javax.swing.JTextField();
        cent = new javax.swing.JTextField();
        tcent = new javax.swing.JTextField();
        tenc = new javax.swing.JTextField();
        ttce = new javax.swing.JTextField();
        fivc = new javax.swing.JTextField();
        tfce = new javax.swing.JTextField();
        cov = new javax.swing.JTextField();
        jLabel14 = new javax.swing.JLabel();
        gtot = new javax.swing.JTextField();
        jSeparator1 = new javax.swing.JSeparator();
        jLabel15 = new javax.swing.JLabel();
        jLabel16 = new javax.swing.JLabel();
        ccRb = new javax.swing.JRadioButton();
        addRb = new javax.swing.JRadioButton();
        pcRb = new javax.swing.JRadioButton();
        ftRb = new javax.swing.JRadioButton();
        jButton1 = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);

        jLabel1.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jLabel1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel1.setText("CASH BREAKDOWN");

        jLabel2.setHorizontalAlignment(javax.swing.SwingConstants.TRAILING);
        jLabel2.setText("x Php 1000.00");

        jLabel3.setHorizontalAlignment(javax.swing.SwingConstants.TRAILING);
        jLabel3.setText("x Php 500.00");

        jLabel4.setHorizontalAlignment(javax.swing.SwingConstants.TRAILING);
        jLabel4.setText("x Php   200.00");

        jLabel5.setHorizontalAlignment(javax.swing.SwingConstants.TRAILING);
        jLabel5.setText("x Php   100.00");

        jLabel6.setHorizontalAlignment(javax.swing.SwingConstants.TRAILING);
        jLabel6.setText("x Php     50.00");

        jLabel7.setHorizontalAlignment(javax.swing.SwingConstants.TRAILING);
        jLabel7.setText("x Php     20.00");

        jLabel8.setHorizontalAlignment(javax.swing.SwingConstants.TRAILING);
        jLabel8.setText("x Php     10.00");

        jLabel9.setHorizontalAlignment(javax.swing.SwingConstants.TRAILING);
        jLabel9.setText("x Php       5.00");

        jLabel10.setHorizontalAlignment(javax.swing.SwingConstants.TRAILING);
        jLabel10.setText("x Php       1.00");

        jLabel11.setHorizontalAlignment(javax.swing.SwingConstants.TRAILING);
        jLabel11.setText("x Php       0.25");

        jLabel12.setHorizontalAlignment(javax.swing.SwingConstants.TRAILING);
        jLabel12.setText("x Php       0.10");

        jLabel13.setHorizontalAlignment(javax.swing.SwingConstants.TRAILING);
        jLabel13.setText("x Php       0.05");

        thou.setHorizontalAlignment(javax.swing.JTextField.TRAILING);
        thou.setText("0");
        thou.setToolTipText("");
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

        tthou.setEditable(false);
        tthou.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        tthou.setHorizontalAlignment(javax.swing.JTextField.TRAILING);
        tthou.setText("0.00");
        tthou.setToolTipText("");

        fhun.setHorizontalAlignment(javax.swing.JTextField.TRAILING);
        fhun.setText("0");
        fhun.setToolTipText("");
        fhun.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                fhunFocusGained(evt);
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                fhunFocusLost(evt);
            }
        });
        fhun.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                fhunKeyTyped(evt);
            }
        });

        tfhun.setEditable(false);
        tfhun.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        tfhun.setHorizontalAlignment(javax.swing.JTextField.TRAILING);
        tfhun.setText("0.00");
        tfhun.setToolTipText("");

        thun.setHorizontalAlignment(javax.swing.JTextField.TRAILING);
        thun.setText("0");
        thun.setToolTipText("");
        thun.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                thunFocusGained(evt);
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                thunFocusLost(evt);
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

        tthun.setEditable(false);
        tthun.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        tthun.setHorizontalAlignment(javax.swing.JTextField.TRAILING);
        tthun.setText("0.00");
        tthun.setToolTipText("");

        hun.setHorizontalAlignment(javax.swing.JTextField.TRAILING);
        hun.setText("0");
        hun.setToolTipText("");
        hun.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                hunFocusGained(evt);
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                hunFocusLost(evt);
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

        tohun.setEditable(false);
        tohun.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        tohun.setHorizontalAlignment(javax.swing.JTextField.TRAILING);
        tohun.setText("0.00");
        tohun.setToolTipText("");

        fif.setHorizontalAlignment(javax.swing.JTextField.TRAILING);
        fif.setText("0");
        fif.setToolTipText("");
        fif.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                fifFocusGained(evt);
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                fifFocusLost(evt);
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

        tfif.setEditable(false);
        tfif.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        tfif.setHorizontalAlignment(javax.swing.JTextField.TRAILING);
        tfif.setText("0.00");
        tfif.setToolTipText("");

        twen.setHorizontalAlignment(javax.swing.JTextField.TRAILING);
        twen.setText("0");
        twen.setToolTipText("");
        twen.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                twenFocusGained(evt);
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                twenFocusLost(evt);
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

        ttwen.setEditable(false);
        ttwen.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        ttwen.setHorizontalAlignment(javax.swing.JTextField.TRAILING);
        ttwen.setText("0.00");
        ttwen.setToolTipText("");

        ten.setHorizontalAlignment(javax.swing.JTextField.TRAILING);
        ten.setText("0");
        ten.setToolTipText("");
        ten.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                tenFocusGained(evt);
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                tenFocusLost(evt);
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

        tten.setEditable(false);
        tten.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        tten.setHorizontalAlignment(javax.swing.JTextField.TRAILING);
        tten.setText("0.00");
        tten.setToolTipText("");

        five.setHorizontalAlignment(javax.swing.JTextField.TRAILING);
        five.setText("0");
        five.setToolTipText("");
        five.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                fiveFocusGained(evt);
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                fiveFocusLost(evt);
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

        tfive.setEditable(false);
        tfive.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        tfive.setHorizontalAlignment(javax.swing.JTextField.TRAILING);
        tfive.setText("0.00");
        tfive.setToolTipText("");

        one.setHorizontalAlignment(javax.swing.JTextField.TRAILING);
        one.setText("0");
        one.setToolTipText("");
        one.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                oneFocusGained(evt);
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                oneFocusLost(evt);
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

        tone.setEditable(false);
        tone.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        tone.setHorizontalAlignment(javax.swing.JTextField.TRAILING);
        tone.setText("0.00");
        tone.setToolTipText("");

        cent.setHorizontalAlignment(javax.swing.JTextField.TRAILING);
        cent.setText("0");
        cent.setToolTipText("");
        cent.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                centFocusGained(evt);
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                centFocusLost(evt);
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

        tcent.setEditable(false);
        tcent.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        tcent.setHorizontalAlignment(javax.swing.JTextField.TRAILING);
        tcent.setText("0.00");
        tcent.setToolTipText("");

        tenc.setHorizontalAlignment(javax.swing.JTextField.TRAILING);
        tenc.setText("0");
        tenc.setToolTipText("");
        tenc.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                tencFocusGained(evt);
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                tencFocusLost(evt);
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

        ttce.setEditable(false);
        ttce.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        ttce.setHorizontalAlignment(javax.swing.JTextField.TRAILING);
        ttce.setText("0.00");
        ttce.setToolTipText("");

        fivc.setHorizontalAlignment(javax.swing.JTextField.TRAILING);
        fivc.setText("0");
        fivc.setToolTipText("");
        fivc.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                fivcFocusGained(evt);
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                fivcFocusLost(evt);
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

        tfce.setEditable(false);
        tfce.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        tfce.setHorizontalAlignment(javax.swing.JTextField.TRAILING);
        tfce.setText("0.00");
        tfce.setToolTipText("");

        cov.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        cov.setHorizontalAlignment(javax.swing.JTextField.TRAILING);
        cov.setText("0.00");
        cov.setToolTipText("");
        cov.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                covFocusLost(evt);
            }
        });
        cov.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                covKeyPressed(evt);
            }
        });

        jLabel14.setText("Cash on Vault:");

        gtot.setEditable(false);
        gtot.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        gtot.setHorizontalAlignment(javax.swing.JTextField.TRAILING);
        gtot.setText("0.00");
        gtot.setToolTipText("");

        jLabel15.setText("Total:");

        jLabel16.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel16.setText("Please Select Report/s to be Printed:");

        ccRb.setSelected(true);
        ccRb.setText("Cash Count");

        addRb.setSelected(true);
        addRb.setText("Additionals");

        pcRb.setSelected(true);
        pcRb.setText("Petty Cash");

        ftRb.setSelected(true);
        ftRb.setText("Fund Transfer");

        jButton1.setText("Save and Print Cash Count Report");
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
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addGap(29, 29, 29)
                                .addComponent(jLabel14))
                            .addGroup(layout.createSequentialGroup()
                                .addGap(11, 11, 11)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(addRb)
                                    .addComponent(ccRb))
                                .addGap(18, 18, 18)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(ftRb)
                                    .addComponent(pcRb)))
                            .addGroup(layout.createSequentialGroup()
                                .addGap(11, 11, 11)
                                .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 206, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(layout.createSequentialGroup()
                                .addGap(14, 14, 14)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(layout.createSequentialGroup()
                                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                            .addComponent(fhun, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addComponent(thun, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addComponent(hun, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addComponent(fif, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addComponent(twen, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addComponent(ten, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addComponent(five, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addComponent(one, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addComponent(cent, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addComponent(tenc, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addComponent(fivc, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addComponent(thou, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE))
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                            .addComponent(jLabel3)
                                            .addComponent(jLabel4, javax.swing.GroupLayout.Alignment.TRAILING)
                                            .addComponent(jLabel5, javax.swing.GroupLayout.Alignment.TRAILING)
                                            .addComponent(jLabel6, javax.swing.GroupLayout.Alignment.TRAILING)
                                            .addComponent(jLabel8, javax.swing.GroupLayout.Alignment.TRAILING)
                                            .addComponent(jLabel9, javax.swing.GroupLayout.Alignment.TRAILING)
                                            .addComponent(jLabel11, javax.swing.GroupLayout.Alignment.TRAILING)
                                            .addComponent(jLabel12, javax.swing.GroupLayout.Alignment.TRAILING)
                                            .addComponent(jLabel13, javax.swing.GroupLayout.Alignment.TRAILING)
                                            .addComponent(jLabel7, javax.swing.GroupLayout.Alignment.TRAILING)
                                            .addComponent(jLabel2)
                                            .addComponent(jLabel10, javax.swing.GroupLayout.Alignment.TRAILING)))
                                    .addComponent(jLabel15, javax.swing.GroupLayout.Alignment.TRAILING))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(gtot, javax.swing.GroupLayout.PREFERRED_SIZE, 69, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(tthou, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(tfhun, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(tthun, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(tohun, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(tfif, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(ttwen, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(tten, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(tfive, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(tone, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(tcent, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(ttce, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(tfce, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(cov, javax.swing.GroupLayout.PREFERRED_SIZE, 69, javax.swing.GroupLayout.PREFERRED_SIZE)))
                            .addGroup(layout.createSequentialGroup()
                                .addGap(11, 11, 11)
                                .addComponent(jSeparator1, javax.swing.GroupLayout.PREFERRED_SIZE, 198, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addComponent(jLabel1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel16, javax.swing.GroupLayout.PREFERRED_SIZE, 202, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        layout.linkSize(javax.swing.SwingConstants.HORIZONTAL, new java.awt.Component[] {cov, tcent, tfce, tfhun, tfif, tfive, tohun, tone, ttce, tten, tthou, tthun, ttwen});

        layout.linkSize(javax.swing.SwingConstants.HORIZONTAL, new java.awt.Component[] {cent, fhun, fif, fivc, five, hun, one, ten, tenc, thou, thun, twen});

        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(cov, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel14))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel2)
                    .addComponent(thou, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(tthou, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel3)
                    .addComponent(fhun, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(tfhun, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel4)
                    .addComponent(thun, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(tthun, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabel5)
                        .addComponent(tohun, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(hun, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel6)
                    .addComponent(fif, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(tfif, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel7)
                    .addComponent(twen, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(ttwen, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel8)
                    .addComponent(ten, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(tten, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel9)
                    .addComponent(five, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(tfive, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel10)
                    .addComponent(one, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(tone, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel11)
                    .addComponent(cent, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(tcent, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel12)
                    .addComponent(tenc, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(ttce, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel13)
                    .addComponent(fivc, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(tfce, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(24, 24, 24)
                .addComponent(jSeparator1, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(gtot, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel15))
                .addGap(18, 18, 18)
                .addComponent(jLabel16)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(ccRb)
                    .addComponent(pcRb))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(addRb)
                    .addComponent(ftRb))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jButton1)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

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
            
            String tempfolder = "C:\\MPIS\\tempRep\\";
            
            String[] to = { this.con.getProp("recipient"), this.con.getProp("bcc") };
            this.repC.setEndbal(this.formatter.format(getBb() - getEmpeno() - getPtc() + getRescate() + getInterest() + getAi() + getSc() + getIns() + getAdd() - getTransfer()));
            if (EmailSender.sendMail(this.con.getProp("email_add"), this.con.getProp("email_pass"), "Daily Cash Report " + this.con.getProp("branch") + " for " + this.dateHelp.getCurrentDate(), to, this.repC.generateReport(this.dateHelp.getCurrentDate()))) {
                this.con.saveProp("lastemailsent", this.dateHelp.getSavingDate().concat(" ").concat(this.dateHelp.getSavingTime()));
                System.out.println("Email sent successfully");
            } else {
                System.out.println("Email sending failed");
            } 
        } catch (SQLException ex) {
            this.con.saveProp("mpis_last_error", String.valueOf(ex));
            JOptionPane.showMessageDialog(null, "Cash Breakdown Save Error.");
            Logger.getLogger(CashBreakdown.class.getName()).log(Level.SEVERE, (String)null, ex);
        } 
    }//GEN-LAST:event_jButton1ActionPerformed

    private void thouKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_thouKeyTyped
    if (!Character.isDigit(evt.getKeyChar())) {
        evt.consume();
    }
    }//GEN-LAST:event_thouKeyTyped

    private void thouFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_thouFocusLost
        this.tthou.setText(this.formatter.format(Double.parseDouble(this.thou.getText().replace(",", "")) * 1000.0D));
/* 1008 */     this.gtot.setText(this.formatter.format(Double.parseDouble(this.tthou.getText().replace(",", "")) + Double.parseDouble(this.tfhun.getText().replace(",", "")) + Double.parseDouble(this.tthun.getText().replace(",", "")) + Double.parseDouble(this.tohun.getText().replace(",", "")) + Double.parseDouble(this.tfif.getText().replace(",", "")) + Double.parseDouble(this.ttwen.getText().replace(",", "")) + Double.parseDouble(this.tten.getText().replace(",", "")) + Double.parseDouble(this.tfive.getText().replace(",", "")) + Double.parseDouble(this.tone.getText().replace(",", "")) + Double.parseDouble(this.tcent.getText().replace(",", "")) + Double.parseDouble(this.cov.getText().replace(",", "")) + Double.parseDouble(this.tfce.getText().replace(",", "")) + Double.parseDouble(this.tfce.getText().replace(",", ""))));
/*      */   
    }//GEN-LAST:event_thouFocusLost

    private void fhunFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_fhunFocusLost
        this.tfhun.setText(this.formatter.format(Double.parseDouble(this.fhun.getText().replace(",", "")) * 500.0D));
/* 1017 */     this.gtot.setText(this.formatter.format(Double.parseDouble(this.tthou.getText().replace(",", "")) + Double.parseDouble(this.tfhun.getText().replace(",", "")) + Double.parseDouble(this.tthun.getText().replace(",", "")) + Double.parseDouble(this.tohun.getText().replace(",", "")) + Double.parseDouble(this.tfif.getText().replace(",", "")) + Double.parseDouble(this.ttwen.getText().replace(",", "")) + Double.parseDouble(this.tten.getText().replace(",", "")) + Double.parseDouble(this.tfive.getText().replace(",", "")) + Double.parseDouble(this.tone.getText().replace(",", "")) + Double.parseDouble(this.tcent.getText().replace(",", "")) + Double.parseDouble(this.cov.getText().replace(",", "")) + Double.parseDouble(this.ttce.getText().replace(",", "")) + Double.parseDouble(this.tfce.getText().replace(",", ""))));
/*      */   
    }//GEN-LAST:event_fhunFocusLost

    private void fhunKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_fhunKeyTyped
        if (!Character.isDigit(evt.getKeyChar())) {
/*  999 */       evt.consume();
/*      */     }
    }//GEN-LAST:event_fhunKeyTyped

    private void thunKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_thunKeyTyped
        if (!Character.isDigit(evt.getKeyChar())) {
/*  999 */       evt.consume();
/*      */     }
    }//GEN-LAST:event_thunKeyTyped

    private void hunKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_hunKeyTyped
        if (!Character.isDigit(evt.getKeyChar())) {
/*  999 */       evt.consume();
/*      */     }
    }//GEN-LAST:event_hunKeyTyped

    private void fifKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_fifKeyTyped
        if (!Character.isDigit(evt.getKeyChar())) {
/*  999 */       evt.consume();
/*      */     }
    }//GEN-LAST:event_fifKeyTyped

    private void twenKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_twenKeyTyped
        if (!Character.isDigit(evt.getKeyChar())) {
/*  999 */       evt.consume();
/*      */     }
    }//GEN-LAST:event_twenKeyTyped

    private void tenKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_tenKeyTyped
        if (!Character.isDigit(evt.getKeyChar())) {
/*  999 */       evt.consume();
/*      */     }
    }//GEN-LAST:event_tenKeyTyped

    private void fiveKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_fiveKeyTyped
        if (!Character.isDigit(evt.getKeyChar())) {
/*  999 */       evt.consume();
/*      */     }
    }//GEN-LAST:event_fiveKeyTyped

    private void oneKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_oneKeyTyped
        if (!Character.isDigit(evt.getKeyChar())) {
/*  999 */       evt.consume();
/*      */     }
    }//GEN-LAST:event_oneKeyTyped

    private void centKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_centKeyTyped
        if (!Character.isDigit(evt.getKeyChar())) {
/*  999 */       evt.consume();
/*      */     }
    }//GEN-LAST:event_centKeyTyped

    private void tencKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_tencKeyTyped
        if (!Character.isDigit(evt.getKeyChar())) {
/*  999 */       evt.consume();
/*      */     }
    }//GEN-LAST:event_tencKeyTyped

    private void fivcKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_fivcKeyTyped
        if (!Character.isDigit(evt.getKeyChar())) {
/*  999 */       evt.consume();
/*      */     }
    }//GEN-LAST:event_fivcKeyTyped

    private void thunFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_thunFocusLost
        this.tthun.setText(this.formatter.format(Double.parseDouble(this.thun.getText().replace(",", "")) * 200.0D));
/* 1026 */     this.gtot.setText(this.formatter.format(Double.parseDouble(this.tthou.getText().replace(",", "")) + Double.parseDouble(this.tfhun.getText().replace(",", "")) + Double.parseDouble(this.tthun.getText().replace(",", "")) + Double.parseDouble(this.tohun.getText().replace(",", "")) + Double.parseDouble(this.tfif.getText().replace(",", "")) + Double.parseDouble(this.ttwen.getText().replace(",", "")) + Double.parseDouble(this.tten.getText().replace(",", "")) + Double.parseDouble(this.tfive.getText().replace(",", "")) + Double.parseDouble(this.tone.getText().replace(",", "")) + Double.parseDouble(this.tcent.getText().replace(",", "")) + Double.parseDouble(this.cov.getText().replace(",", "")) + Double.parseDouble(this.ttce.getText().replace(",", "")) + Double.parseDouble(this.tfce.getText().replace(",", ""))));
    }//GEN-LAST:event_thunFocusLost

    private void hunFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_hunFocusLost
        this.tohun.setText(this.formatter.format(Double.parseDouble(this.hun.getText().replace(",", "")) * 100.0D));
/* 1035 */     this.gtot.setText(this.formatter.format(Double.parseDouble(this.tthou.getText().replace(",", "")) + Double.parseDouble(this.tfhun.getText().replace(",", "")) + Double.parseDouble(this.tthun.getText().replace(",", "")) + Double.parseDouble(this.tohun.getText().replace(",", "")) + Double.parseDouble(this.tfif.getText().replace(",", "")) + Double.parseDouble(this.ttwen.getText().replace(",", "")) + Double.parseDouble(this.tten.getText().replace(",", "")) + Double.parseDouble(this.tfive.getText().replace(",", "")) + Double.parseDouble(this.tone.getText().replace(",", "")) + Double.parseDouble(this.tcent.getText().replace(",", "")) + Double.parseDouble(this.cov.getText().replace(",", "")) + Double.parseDouble(this.ttce.getText().replace(",", "")) + Double.parseDouble(this.tfce.getText().replace(",", ""))));
    }//GEN-LAST:event_hunFocusLost

    private void fifFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_fifFocusLost
        this.tfif.setText(this.formatter.format(Double.parseDouble(this.fif.getText().replace(",", "")) * 50.0D));
/* 1044 */     this.gtot.setText(this.formatter.format(Double.parseDouble(this.tthou.getText().replace(",", "")) + Double.parseDouble(this.tfhun.getText().replace(",", "")) + Double.parseDouble(this.tthun.getText().replace(",", "")) + Double.parseDouble(this.tohun.getText().replace(",", "")) + Double.parseDouble(this.tfif.getText().replace(",", "")) + Double.parseDouble(this.ttwen.getText().replace(",", "")) + Double.parseDouble(this.tten.getText().replace(",", "")) + Double.parseDouble(this.tfive.getText().replace(",", "")) + Double.parseDouble(this.tone.getText().replace(",", "")) + Double.parseDouble(this.tcent.getText().replace(",", "")) + Double.parseDouble(this.cov.getText().replace(",", "")) + Double.parseDouble(this.ttce.getText().replace(",", "")) + Double.parseDouble(this.tfce.getText().replace(",", ""))));
    }//GEN-LAST:event_fifFocusLost

    private void twenFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_twenFocusLost
        this.ttwen.setText(this.formatter.format(Double.parseDouble(this.twen.getText().replace(",", "")) * 20.0D));
/* 1053 */     this.gtot.setText(this.formatter.format(Double.parseDouble(this.tthou.getText().replace(",", "")) + Double.parseDouble(this.tfhun.getText().replace(",", "")) + Double.parseDouble(this.tthun.getText().replace(",", "")) + Double.parseDouble(this.tohun.getText().replace(",", "")) + Double.parseDouble(this.tfif.getText().replace(",", "")) + Double.parseDouble(this.ttwen.getText().replace(",", "")) + Double.parseDouble(this.tten.getText().replace(",", "")) + Double.parseDouble(this.tfive.getText().replace(",", "")) + Double.parseDouble(this.tone.getText().replace(",", "")) + Double.parseDouble(this.tcent.getText().replace(",", "")) + Double.parseDouble(this.cov.getText().replace(",", "")) + Double.parseDouble(this.ttce.getText().replace(",", "")) + Double.parseDouble(this.tfce.getText().replace(",", ""))));
    }//GEN-LAST:event_twenFocusLost

    private void tenFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_tenFocusLost
        this.tten.setText(this.formatter.format(Double.parseDouble(this.ten.getText().replace(",", "")) * 10.0D));
/* 1062 */     this.gtot.setText(this.formatter.format(Double.parseDouble(this.tthou.getText().replace(",", "")) + Double.parseDouble(this.tfhun.getText().replace(",", "")) + Double.parseDouble(this.tthun.getText().replace(",", "")) + Double.parseDouble(this.tohun.getText().replace(",", "")) + Double.parseDouble(this.tfif.getText().replace(",", "")) + Double.parseDouble(this.ttwen.getText().replace(",", "")) + Double.parseDouble(this.tten.getText().replace(",", "")) + Double.parseDouble(this.tfive.getText().replace(",", "")) + Double.parseDouble(this.tone.getText().replace(",", "")) + Double.parseDouble(this.tcent.getText().replace(",", "")) + Double.parseDouble(this.cov.getText().replace(",", "")) + Double.parseDouble(this.ttce.getText().replace(",", "")) + Double.parseDouble(this.tfce.getText().replace(",", ""))));
    }//GEN-LAST:event_tenFocusLost

    private void fiveFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_fiveFocusLost
        this.tfive.setText(this.formatter.format(Double.parseDouble(this.five.getText().replace(",", "")) * 5.0D));
/* 1071 */     this.gtot.setText(this.formatter.format(Double.parseDouble(this.tthou.getText().replace(",", "")) + Double.parseDouble(this.tfhun.getText().replace(",", "")) + Double.parseDouble(this.tthun.getText().replace(",", "")) + Double.parseDouble(this.tohun.getText().replace(",", "")) + Double.parseDouble(this.tfif.getText().replace(",", "")) + Double.parseDouble(this.ttwen.getText().replace(",", "")) + Double.parseDouble(this.tten.getText().replace(",", "")) + Double.parseDouble(this.tfive.getText().replace(",", "")) + Double.parseDouble(this.tone.getText().replace(",", "")) + Double.parseDouble(this.tcent.getText().replace(",", "")) + Double.parseDouble(this.cov.getText().replace(",", "")) + Double.parseDouble(this.ttce.getText().replace(",", "")) + Double.parseDouble(this.tfce.getText().replace(",", ""))));
    }//GEN-LAST:event_fiveFocusLost

    private void oneFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_oneFocusLost
        this.tone.setText(this.formatter.format(Double.parseDouble(this.one.getText().replace(",", ""))));
/* 1080 */     this.gtot.setText(this.formatter.format(Double.parseDouble(this.tthou.getText().replace(",", "")) + Double.parseDouble(this.tfhun.getText().replace(",", "")) + Double.parseDouble(this.tthun.getText().replace(",", "")) + Double.parseDouble(this.tohun.getText().replace(",", "")) + Double.parseDouble(this.tfif.getText().replace(",", "")) + Double.parseDouble(this.ttwen.getText().replace(",", "")) + Double.parseDouble(this.tten.getText().replace(",", "")) + Double.parseDouble(this.tfive.getText().replace(",", "")) + Double.parseDouble(this.tone.getText().replace(",", "")) + Double.parseDouble(this.tcent.getText().replace(",", "")) + Double.parseDouble(this.cov.getText().replace(",", "")) + Double.parseDouble(this.ttce.getText().replace(",", "")) + Double.parseDouble(this.tfce.getText().replace(",", ""))));
    }//GEN-LAST:event_oneFocusLost

    private void centFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_centFocusLost
        this.tcent.setText(this.formatter.format(Double.parseDouble(this.cent.getText().replace(",", "")) * 0.25D));
/* 1089 */     this.gtot.setText(this.formatter.format(Double.parseDouble(this.tthou.getText().replace(",", "")) + Double.parseDouble(this.tfhun.getText().replace(",", "")) + Double.parseDouble(this.tthun.getText().replace(",", "")) + Double.parseDouble(this.tohun.getText().replace(",", "")) + Double.parseDouble(this.tfif.getText().replace(",", "")) + Double.parseDouble(this.ttwen.getText().replace(",", "")) + Double.parseDouble(this.tten.getText().replace(",", "")) + Double.parseDouble(this.tfive.getText().replace(",", "")) + Double.parseDouble(this.tone.getText().replace(",", "")) + Double.parseDouble(this.tcent.getText().replace(",", "")) + Double.parseDouble(this.cov.getText().replace(",", "")) + Double.parseDouble(this.ttce.getText().replace(",", "")) + Double.parseDouble(this.tfce.getText().replace(",", ""))));
    }//GEN-LAST:event_centFocusLost

    private void tencFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_tencFocusLost
this.ttce.setText(this.formatter.format(Double.parseDouble(this.tenc.getText().replace(",", "")) * 0.1D));
/* 1155 */     this.gtot.setText(this.formatter.format(Double.parseDouble(this.tthou.getText().replace(",", "")) + Double.parseDouble(this.tfhun.getText().replace(",", "")) + Double.parseDouble(this.tthun.getText().replace(",", "")) + Double.parseDouble(this.tohun.getText().replace(",", "")) + Double.parseDouble(this.tfif.getText().replace(",", "")) + Double.parseDouble(this.ttwen.getText().replace(",", "")) + Double.parseDouble(this.tten.getText().replace(",", "")) + Double.parseDouble(this.tfive.getText().replace(",", "")) + Double.parseDouble(this.tone.getText().replace(",", "")) + Double.parseDouble(this.tcent.getText().replace(",", "")) + Double.parseDouble(this.cov.getText().replace(",", "")) + Double.parseDouble(this.ttce.getText().replace(",", "")) + Double.parseDouble(this.tfce.getText().replace(",", ""))));
    }//GEN-LAST:event_tencFocusLost

    private void covFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_covFocusLost
        this.cov.setText(this.formatter.format(Double.parseDouble(this.cov.getText().replace(",", ""))));
/* 1138 */     this.gtot.setText(this.formatter.format(Double.parseDouble(this.tthou.getText().replace(",", "")) + Double.parseDouble(this.tfhun.getText().replace(",", "")) + Double.parseDouble(this.tthun.getText().replace(",", "")) + Double.parseDouble(this.tohun.getText().replace(",", "")) + Double.parseDouble(this.tfif.getText().replace(",", "")) + Double.parseDouble(this.ttwen.getText().replace(",", "")) + Double.parseDouble(this.tten.getText().replace(",", "")) + Double.parseDouble(this.tfive.getText().replace(",", "")) + Double.parseDouble(this.tone.getText().replace(",", "")) + Double.parseDouble(this.tcent.getText().replace(",", "")) + Double.parseDouble(this.cov.getText().replace(",", "")) + Double.parseDouble(this.ttce.getText().replace(",", "")) + Double.parseDouble(this.tfce.getText().replace(",", ""))));
    }//GEN-LAST:event_covFocusLost

    private void fivcFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_fivcFocusLost
        this.tfce.setText(this.formatter.format(Double.parseDouble(this.fivc.getText().replace(",", "")) * 0.05D));
/* 1177 */     this.gtot.setText(this.formatter.format(Double.parseDouble(this.tthou.getText().replace(",", "")) + Double.parseDouble(this.tfhun.getText().replace(",", "")) + Double.parseDouble(this.tthun.getText().replace(",", "")) + Double.parseDouble(this.tohun.getText().replace(",", "")) + Double.parseDouble(this.tfif.getText().replace(",", "")) + Double.parseDouble(this.ttwen.getText().replace(",", "")) + Double.parseDouble(this.tten.getText().replace(",", "")) + Double.parseDouble(this.tfive.getText().replace(",", "")) + Double.parseDouble(this.tone.getText().replace(",", "")) + Double.parseDouble(this.tcent.getText().replace(",", "")) + Double.parseDouble(this.cov.getText().replace(",", "")) + Double.parseDouble(this.ttce.getText().replace(",", "")) + Double.parseDouble(this.tfce.getText().replace(",", ""))));
    }//GEN-LAST:event_fivcFocusLost

    private void fivcFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_fivcFocusGained
        this.fivc.selectAll();
    }//GEN-LAST:event_fivcFocusGained

    private void thouFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_thouFocusGained
        this.thou.selectAll();
    }//GEN-LAST:event_thouFocusGained

    private void fhunFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_fhunFocusGained
        this.fhun.selectAll();
    }//GEN-LAST:event_fhunFocusGained

    private void thunFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_thunFocusGained
        this.thun.selectAll();
    }//GEN-LAST:event_thunFocusGained

    private void hunFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_hunFocusGained
        this.hun.selectAll();
    }//GEN-LAST:event_hunFocusGained

    private void fifFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_fifFocusGained
        this.fif.selectAll();
    }//GEN-LAST:event_fifFocusGained

    private void twenFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_twenFocusGained
        this.twen.selectAll();
    }//GEN-LAST:event_twenFocusGained

    private void tenFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_tenFocusGained
        this.ten.selectAll();
    }//GEN-LAST:event_tenFocusGained

    private void fiveFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_fiveFocusGained
        this.five.selectAll();
    }//GEN-LAST:event_fiveFocusGained

    private void oneFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_oneFocusGained
        this.one.selectAll();
    }//GEN-LAST:event_oneFocusGained

    private void centFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_centFocusGained
        this.cent.selectAll();
    }//GEN-LAST:event_centFocusGained

    private void tencFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_tencFocusGained
        this.tenc.selectAll();
    }//GEN-LAST:event_tencFocusGained

    private void thouKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_thouKeyPressed
        if (evt.getKeyCode() == 10) {
/* 1203 */       thouFocusLost((FocusEvent)null);
/*      */     }
    }//GEN-LAST:event_thouKeyPressed

    private void covKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_covKeyPressed
        if (evt.getKeyCode() == 10) {
/* 1213 */       covFocusLost((FocusEvent)null);
/*      */     }
    }//GEN-LAST:event_covKeyPressed

    private void thunKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_thunKeyPressed
        if (evt.getKeyCode() == 10) {
/* 1219 */       thunFocusLost((FocusEvent)null);
/*      */     }
    }//GEN-LAST:event_thunKeyPressed

    private void hunKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_hunKeyPressed
        if (evt.getKeyCode() == 10) {
/* 1225 */       hunFocusLost((FocusEvent)null);
/*      */     }
    }//GEN-LAST:event_hunKeyPressed

    private void fifKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_fifKeyPressed
        if (evt.getKeyCode() == 10) {
/* 1231 */       fifFocusLost((FocusEvent)null);
/*      */     }
    }//GEN-LAST:event_fifKeyPressed

    private void twenKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_twenKeyPressed
        if (evt.getKeyCode() == 10) {
/* 1237 */       twenFocusLost((FocusEvent)null);
/*      */     }
    }//GEN-LAST:event_twenKeyPressed

    private void tenKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_tenKeyPressed
        if (evt.getKeyCode() == 10) {
/* 1243 */       tenFocusLost((FocusEvent)null);
/*      */     }
    }//GEN-LAST:event_tenKeyPressed

    private void fiveKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_fiveKeyPressed
        if (evt.getKeyCode() == 10) {
/* 1249 */       fiveFocusLost((FocusEvent)null);
/*      */     }
    }//GEN-LAST:event_fiveKeyPressed

    private void oneKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_oneKeyPressed
        if (evt.getKeyCode() == 10) {
/* 1255 */       oneFocusLost((FocusEvent)null);
/*      */     }
    }//GEN-LAST:event_oneKeyPressed

    private void centKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_centKeyPressed
        if (evt.getKeyCode() == 10) {
/* 1261 */       centFocusLost((FocusEvent)null);
/*      */     }
    }//GEN-LAST:event_centKeyPressed

    private void tencKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_tencKeyPressed
        if (evt.getKeyCode() == 10) {
/* 1267 */       tencFocusLost((FocusEvent)null);
/*      */     }
    }//GEN-LAST:event_tencKeyPressed

    private void fivcKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_fivcKeyPressed
        if (evt.getKeyCode() == 10) {
/* 1273 */       fivcFocusLost((FocusEvent)null);
/*      */     }
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
