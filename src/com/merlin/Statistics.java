/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.merlin;

import java.awt.Color;
import java.awt.Component;
import java.awt.event.ActionEvent;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Calendar;
import java.util.logging.Level;
import java.util.logging.Logger;
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
public class Statistics extends javax.swing.JPanel {

    /**
     * Creates new form Expired
     */
    public Statistics() {
        initComponents();
        transDateRet.setDate(Calendar.getInstance().getTime());
        try {
            updateComboYear();
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "Unable to retrive all available years from the Database.", "Database Error", JOptionPane.ERROR_MESSAGE);
            Logger.getLogger(Statistics.class.getName()).log(Level.SEVERE, null, ex);
        }
        updateCombos();
    }
    
    private Config con = new Config();
    private final String driver = "jdbc:mariadb://" + this.con.getProp("IP") + ":" + this.con.getProp("port") 
            + "/cashtransactions";;
    private final String f_user = con.getProp("username");
    private final String f_pass = con.getProp("password");
    private DecimalHelper decHelp = new DecimalHelper();
    private Sangla sangla = new Sangla();

    public void updateCombo(JComboBox<String> combo) {
        try {
            combo.removeAllItems();
            Connection connect = DriverManager.getConnection(this.driver, this.f_user, this.f_pass);
            Statement state = connect.createStatement();
            String query = "Select branch_name from merlininventorydatabase.branch_info";
            ResultSet rset = state.executeQuery(query);
            while (rset.next()) {
                combo.addItem(rset.getString(1));
            }
            state.close();
            connect.close();
            combo.setSelectedItem(this.con.getProp("branch"));
        } catch (SQLException ex) {
            this.con.saveProp("mpis_last_error", String.valueOf(ex));
            Logger.getLogger(Additionals.class.getName()).log(Level.SEVERE, (String) null, ex);
        }
    }
    
    public void updateCombos() {
        try {
            updateCombo(this.branch);
            updateAddlCategories();
            updatePettyCategories();
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "Unable to update Petty Cash and Additional Categories Combo Boxes \n" + String.valueOf(ex), "Database Error", JOptionPane.ERROR_MESSAGE);
//            Logger.getLogger(Statistics.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public void updateComboYear() throws SQLException {
        yearCombo.removeAllItems();
        Connection connect = DriverManager.getConnection(this.driver, this.f_user, this.f_pass);
        Statement state = connect.createStatement();
        String query = "Select distinct year(date) from merlininventorydatabase.empeno order by date desc";
        ResultSet rset = state.executeQuery(query);
        while (rset.next()) {
            this.yearCombo.addItem(rset.getString(1));
        }
        state.close();
        connect.close();
    }
    
    public void updatePettyCategories() throws SQLException {
        pettyCashType.removeAllItems();
        pettyCashType.addItem("All");
        pettyCashType1.removeAllItems();
        pettyCashType1.addItem("All");
        Connection connect = DriverManager.getConnection(this.driver, this.f_user, this.f_pass);
        Statement state = connect.createStatement();
        String query = "Select distinct (pty_type) from cashtransactions.petty_cash order by pty_type asc";
        ResultSet rset = state.executeQuery(query);

        while (rset.next()) {
            this.pettyCashType.addItem(rset.getString(1));
            this.pettyCashType1.addItem(rset.getString(1));
        }
        state.close();
        connect.close();
    }

    public void updateAddlCategories() throws SQLException {
        addlTypeDaily.removeAllItems();
        addlTypeDaily.addItem("All");
        addlTypeMonthly.removeAllItems();
        addlTypeMonthly.addItem("All");
        Connection connect = DriverManager.getConnection(this.driver, this.f_user, this.f_pass);
        Statement state = connect.createStatement();
        String query = "Select distinct (add_type) from cashtransactions.additionals order by add_type asc";
        ResultSet rset = state.executeQuery(query);

        while (rset.next()) {
            this.addlTypeDaily.addItem(rset.getString(1));
            this.addlTypeMonthly.addItem(rset.getString(1));
        }
        state.close();
        connect.close();
    }

    public void updateDailyEmpeno() throws SQLException {
        DefaultTableModel defTabMod = (DefaultTableModel) this.daiEmp.getModel();
        Connection connect = DriverManager.getConnection(this.driver, this.f_user, this.f_pass);
        Statement state = connect.createStatement();
        String query = "select date, count(principal), sum(principal) as 'total principal', sum(ai) as 'total adv. int.', sum(sc) as 'total svc chg', sum(insurance) as 'total insurance' from merlininventorydatabase.empeno where pap_num like '%" + this.sangla.getBranchCode(this.branch.getSelectedItem().toString()) + "' and MONTH(date) = " + (this.monthCombo.getSelectedIndex() + 1) + " and YEAR(date) = " + this.yearCombo.getSelectedItem().toString() + " group by date";
        ResultSet rset = state.executeQuery(query);
        while (defTabMod.getRowCount() > 0) {
            defTabMod.removeRow(0);
        }
        int tot_lot = 0;
        double tot_cap = 0.0D;
        double tot_ai = 0.0D;
        double tot_sc = 0.0D;
        double tot_ins = 0.0D;
        Object[] row = new Object[6];
        while (rset.next()) {
            tot_lot += rset.getInt(2);
            tot_cap += rset.getDouble(3);
            tot_ai += rset.getDouble(4);
            tot_sc += rset.getDouble(5);
            tot_ins += rset.getDouble(6);
            for (int i = 0; i < 6; i++) {
                if (i <= 1) {
                    row[i] = rset.getString(i + 1);
                } else {
                    row[i] = this.decHelp.FormatNumber(rset.getDouble(i + 1));
                }
            }
            defTabMod.addRow(row);
        }
        state.close();
        connect.close();
        this.daiEmp.setModel(defTabMod);
        designTable(this.daiEmp, 0);
        this.totLotes.setText(String.valueOf(tot_lot).concat("      "));
        this.totCapital.setText(this.decHelp.FormatNumber(tot_cap).concat("      "));
        this.totAI.setText(this.decHelp.FormatNumber(tot_ai).concat("          "));
        this.totSC.setText(this.decHelp.FormatNumber(tot_sc).concat("          "));
        this.totIns.setText(this.decHelp.FormatNumber(tot_ins).concat("          "));
    }

    public void updateMonthlyEmpeno() throws SQLException {
        DefaultTableModel defTabMod = (DefaultTableModel) this.monEmp.getModel();
        Connection connect = DriverManager.getConnection(this.driver, this.f_user, this.f_pass);
        Statement state = connect.createStatement();
        String query = "select Month(date), count(principal), sum(principal) as 'total principal', sum(ai) as 'total adv. int.', sum(sc) as 'total svc chg', sum(insurance) as 'total insurance' from merlininventorydatabase.empeno where pap_num like '%" + this.sangla.getBranchCode(this.branch.getSelectedItem().toString()) + "' and YEAR(date) = " + this.yearCombo.getSelectedItem().toString() + " group by MONTH(date)";
        ResultSet rset = state.executeQuery(query);
        while (defTabMod.getRowCount() > 0) {
            defTabMod.removeRow(0);
        }
        Object[] row = new Object[6];
        int tot_lot = 0;
        double tot_cap = 0.0D;
        double tot_ai = 0.0D;
        double tot_sc = 0.0D;
        double tot_ins = 0.0D;
        while (rset.next()) {
            tot_lot += rset.getInt(2);
            tot_cap += rset.getDouble(3);
            tot_ai += rset.getDouble(4);
            tot_sc += rset.getDouble(5);
            tot_ins += rset.getDouble(6);
            for (int i = 0; i < row.length; i++) {
                if (i == 0) {
                    row[i] = getMonthName(Integer.valueOf(rset.getInt(1))).concat(" " + this.yearCombo.getSelectedItem().toString());
                } else if (i == 1) {
                    row[i] = rset.getString(i + 1);
                } else {
                    row[i] = this.decHelp.FormatNumber(rset.getDouble(i + 1));
                }
            }
            defTabMod.addRow(row);
        }
        state.close();
        connect.close();
        this.monEmp.setModel(defTabMod);
        designTable(this.monEmp, 0);
        this.totLotes1.setText(String.valueOf(tot_lot).concat("     "));
        this.totCapital1.setText(this.decHelp.FormatNumber(tot_cap).concat("     "));
        this.totAI1.setText(this.decHelp.FormatNumber(tot_ai).concat("     "));
        this.totSC1.setText(this.decHelp.FormatNumber(tot_sc).concat("     "));
        this.totIns1.setText(this.decHelp.FormatNumber(tot_ins).concat("     "));
    }

    public void updateDailyRescate() throws SQLException {
        DefaultTableModel defTabMod = (DefaultTableModel) this.daiRes.getModel();
        Connection connect = DriverManager.getConnection(this.driver, this.f_user, this.f_pass);
        Statement state = connect.createStatement();
        String query = "select date, count(principal), sum(principal) as 'total principal', sum(interest) as 'total int.', (sum(principal)+sum(interest)) as 'total' from merlininventorydatabase.rescate where pap_num like '%" + this.sangla.getBranchCode(this.branch.getSelectedItem().toString()) + "' and MONTH(date) = " + (this.monthCombo.getSelectedIndex() + 1) + " and YEAR(date) = " + this.yearCombo.getSelectedItem().toString() + " and subasta = 0 group by date";
        ResultSet rset = state.executeQuery(query);
        while (defTabMod.getRowCount() > 0) {
            defTabMod.removeRow(0);
        }
        Object[] row = new Object[5];
        int tot_lot = 0;
        double tot_cap = 0.0D;
        double tot_int = 0.0D;
        double tot_tot = 0.0D;
        while (rset.next()) {
            tot_lot += rset.getInt(2);
            tot_cap += rset.getDouble(3);
            tot_int += rset.getDouble(4);
            tot_tot += rset.getDouble(5);
            for (int i = 0; i < row.length; i++) {
                if (i <= 1) {
                    row[i] = rset.getString(i + 1);
                } else {
                    row[i] = this.decHelp.FormatNumber(rset.getDouble(i + 1));
                }
            }
            defTabMod.addRow(row);
        }
        state.close();
        connect.close();
        this.daiRes.setModel(defTabMod);
        designTable(this.daiRes, 0);
        this.totLotes2.setText(String.valueOf(tot_lot).concat("     "));
        this.totCapital2.setText(this.decHelp.FormatNumber(tot_cap).concat("     "));
        this.totAI2.setText(this.decHelp.FormatNumber(tot_int).concat("     "));
        this.totSC2.setText(this.decHelp.FormatNumber(tot_tot).concat("     "));
    }

    public void updateMonthlyRescate() throws SQLException {
        DefaultTableModel defTabMod = (DefaultTableModel) this.monRes.getModel();
        Connection connect = DriverManager.getConnection(this.driver, this.f_user, this.f_pass);
        Statement state = connect.createStatement();
        String query = "select MONTH(date) as 'date', count(principal), sum(principal) as 'total principal', sum(interest) as 'total int.', (sum(principal)+sum(interest)) as 'total' from merlininventorydatabase.rescate where pap_num like '%" + this.sangla.getBranchCode(this.branch.getSelectedItem().toString()) + "' and YEAR(date) = " + this.yearCombo.getSelectedItem().toString() + " group by MONTH(date)";
        ResultSet rset = state.executeQuery(query);
        while (defTabMod.getRowCount() > 0) {
            defTabMod.removeRow(0);
        }
        Object[] row = new Object[5];
        int tot_lot = 0;
        double tot_cap = 0.0D;
        double tot_int = 0.0D;
        double tot_tot = 0.0D;
        while (rset.next()) {
            tot_lot += rset.getInt(2);
            tot_cap += rset.getDouble(3);
            tot_int += rset.getDouble(4);
            tot_tot += rset.getDouble(5);
            for (int i = 0; i < row.length; i++) {
                if (i == 0) {
                    row[i] = getMonthName(Integer.valueOf(rset.getInt(1))).concat(" " + this.yearCombo.getSelectedItem().toString());
                } else if (i == 1) {
                    row[i] = rset.getString(i + 1);
                } else {
                    row[i] = this.decHelp.FormatNumber(rset.getDouble(i + 1));
                }
            }
            defTabMod.addRow(row);
        }
        state.close();
        connect.close();
        this.monRes.setModel(defTabMod);
        designTable(this.monRes, 0);
        this.totLotes4.setText(String.valueOf(tot_lot).concat("     "));
        this.totCapital4.setText(this.decHelp.FormatNumber(tot_cap).concat("     "));
        this.totAI4.setText(this.decHelp.FormatNumber(tot_int).concat("     "));
        this.totSC4.setText(this.decHelp.FormatNumber(tot_tot).concat("     "));
    }

    public void designTable(JTable gvnTable, final int rn) {
        TableCellRenderer renderer = new TableCellRenderer() {
            JLabel label = new JLabel();

            public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
                this.label.setOpaque(true);
                this.label.setText("" + value);
//                Color alternate = UIManager.getColor("Table.alternateRowColor");
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
                if (column > rn) {
                    this.label.setHorizontalAlignment(4);
                    this.label.setText("" + value + "          ");
                } else {
                    this.label.setHorizontalAlignment(0);
                }
                return this.label;
            }
        };
        gvnTable.setDefaultRenderer(Object.class, renderer);
        ((DefaultTableCellRenderer) gvnTable.getTableHeader().getDefaultRenderer()).setHorizontalAlignment(0);
    }

    public void updateDailyTransfers() throws SQLException {
        String query, query2;
        DefaultTableModel defTabMod = (DefaultTableModel) this.transfersTable2.getModel();
        DefaultTableModel defTabMod2 = (DefaultTableModel) this.transfersTable.getModel();
        Connection connect = DriverManager.getConnection(this.driver, this.f_user, this.f_pass);
        Connection connect2 = DriverManager.getConnection(this.driver, this.f_user, this.f_pass);
        Statement state = connect.createStatement();
        Statement state2 = connect2.createStatement();
        String date = (new DateHelper()).formatDate(this.transDateRet.getDate());
        if (!this.jRadioButton1.isSelected()) {
            query = "select x_date, x_branch, x_dest, x_amount from cashtransactions.transfer where month(x_date) = " + (this.monthCombo.getSelectedIndex() + 1) + " and year(x_date) = " + this.yearCombo.getSelectedItem().toString() + " and x_branch != 'Merlin'" + " order by x_date asc";
            query2 = "select x_date, x_branch, x_dest, x_amount from cashtransactions.transfer where month(x_date) = " + (this.monthCombo.getSelectedIndex() + 1) + " and year(x_date) = " + this.yearCombo.getSelectedItem().toString() + " and x_branch = 'Merlin'" + " order by x_date asc";
        } else {
            query = "select x_date, x_branch, x_dest, x_amount from cashtransactions.transfer where x_date = '" + date + "' and x_branch != 'Merlin'" + " order by x_date asc";
            query2 = "select x_date, x_branch, x_dest, x_amount from cashtransactions.transfer where x_date = '" + date + "' and x_branch = 'Merlin'" + " order by x_date asc";
        }
        ResultSet rset = state.executeQuery(query);
        while (defTabMod.getRowCount() > 0) {
            defTabMod.removeRow(0);
        }
        Object[] row = new Object[4];
        while (rset.next()) {
            for (int i = 0; i < row.length; i++) {
                if (i > 2) {
                    row[i] = this.decHelp.FormatNumber(rset.getDouble(i + 1));
                } else {
                    row[i] = rset.getString(i + 1);
                }
            }
            defTabMod.addRow(row);
        }
        state.close();
        connect.close();
        ResultSet rset2 = state2.executeQuery(query2);
        while (defTabMod2.getRowCount() > 0) {
            defTabMod2.removeRow(0);
        }
        Object[] row2 = new Object[4];
        while (rset2.next()) {
            for (int i = 0; i < row2.length; i++) {
                if (i > 2) {
                    row2[i] = this.decHelp.FormatNumber(rset2.getDouble(i + 1));
                } else {
                    row2[i] = rset2.getString(i + 1);
                }
            }
            defTabMod2.addRow(row2);
        }
        state2.close();
        connect2.close();
        this.transfersTable2.setModel(defTabMod);
        this.transfersTable.setModel(defTabMod2);
        designTable(this.transfersTable, 2);
        designTable(this.transfersTable2, 2);
    }

    public void updateMonthlyTransfers() throws SQLException {
        DefaultTableModel defTabMod = (DefaultTableModel) this.transfersTable3.getModel();
        DefaultTableModel defTabMod2 = (DefaultTableModel) this.transfersTable4.getModel();
        Connection connect = DriverManager.getConnection(this.driver, this.f_user, this.f_pass);
        Connection connect2 = DriverManager.getConnection(this.driver, this.f_user, this.f_pass);
        Statement state = connect.createStatement();
        Statement state2 = connect2.createStatement();
        String query = "select month(x_date), x_branch, x_dest, sum(x_amount) as totalamt from cashtransactions.transfer where year(x_date) = " + this.yearCombo.getSelectedItem().toString() + " and x_branch != 'Merlin'" + " group by month(x_date), x_dest order by x_date asc, totalamt desc, x_dest asc";
        String query2 = "select month(x_date), x_branch, x_dest, sum(x_amount) as totalamt from cashtransactions.transfer where year(x_date) = " + this.yearCombo.getSelectedItem().toString() + " and x_branch = 'Merlin'" + " group by month(x_date), x_dest order by x_date asc, totalamt desc, x_dest asc";
        ResultSet rset = state.executeQuery(query);
        while (defTabMod.getRowCount() > 0) {
            defTabMod.removeRow(0);
        }
        Object[] row = new Object[4];
        int count = 0;
        int prevd = 0;
        while (rset.next()) {
            for (int i = 0; i < row.length; i++) {
                if (i > 2) {
                    row[i] = this.decHelp.FormatNumber(rset.getDouble(i + 1));
                } else if (i == 0) {
                    if (count == 0) {
                        prevd = rset.getInt(1);
                        row[0] = getMonthName(Integer.valueOf(rset.getInt(1)));
                    } else if (rset.getInt(1) == prevd) {
                        row[0] = "";
                    } else {
                        row[0] = getMonthName(Integer.valueOf(rset.getInt(1)));
                        prevd = rset.getInt(1);
                    }
                } else {
                    row[i] = rset.getString(i + 1);
                }
            }
            defTabMod.addRow(row);
            count++;
        }
        state.close();
        connect.close();
        ResultSet rset2 = state2.executeQuery(query2);
        while (defTabMod2.getRowCount() > 0) {
            defTabMod2.removeRow(0);
        }
        Object[] row2 = new Object[4];
        int count2 = 0;
        int prevd2 = 0;
        while (rset2.next()) {
            for (int i = 0; i < row2.length; i++) {
                if (i > 2) {
                    row2[i] = this.decHelp.FormatNumber(rset2.getDouble(i + 1));
                } else if (i == 0) {
                    if (count2 == 0) {
                        prevd2 = rset2.getInt(1);
                        row2[0] = getMonthName(Integer.valueOf(rset2.getInt(1)));
                    } else if (rset2.getInt(1) == prevd2) {
                        row2[0] = "";
                    } else {
                        row2[0] = getMonthName(Integer.valueOf(rset2.getInt(1)));
                        prevd2 = rset2.getInt(1);
                    }
                } else {
                    row2[i] = rset2.getString(i + 1);
                }
            }
            defTabMod2.addRow(row2);
            count2++;
        }
        state2.close();
        connect2.close();
        this.transfersTable3.setModel(defTabMod);
        this.transfersTable4.setModel(defTabMod2);
        designTable(this.transfersTable3, 2);
        designTable(this.transfersTable4, 2);
    }

    public void updateDailyPettyCash() throws SQLException {
        if (pettyCashType.getItemCount() > 0) {
            String query;
            DefaultTableModel defTabMod = (DefaultTableModel) this.DailyPettyCashTable.getModel();
            Connection connect = DriverManager.getConnection(this.driver, this.f_user, this.f_pass);
            Statement state = connect.createStatement();
            if (!this.dailyPettyCatRadioBtn.isSelected()) {
                query = "select pty_date, sum(pty_amount) as total from cashtransactions.petty_cash where month(pty_date) = " + (this.monthCombo.getSelectedIndex() + 1) + " and year(pty_date) = " + this.yearCombo.getSelectedItem().toString() + " group by pty_date order by pty_date asc, total desc";
            } else if (this.pettyCashType.getSelectedIndex() != 0) {
                query = "select pty_date, pty_type, sum(pty_amount) as total from cashtransactions.petty_cash where month(pty_date) = " + (this.monthCombo.getSelectedIndex() + 1) + " and year(pty_date) = " + this.yearCombo.getSelectedItem().toString() + " and pty_type = '" + this.pettyCashType.getSelectedItem().toString() + "'" + " group by pty_type, pty_date order by pty_date asc, pty_type desc, total desc";
            } else {
                query = "select pty_date, pty_type, sum(pty_amount) as total from cashtransactions.petty_cash where month(pty_date) = " + (this.monthCombo.getSelectedIndex() + 1) + " and year(pty_date) = " + this.yearCombo.getSelectedItem().toString() + " group by pty_type, pty_date order by pty_date asc, pty_type desc, total desc";
            }
//            System.out.println(query);
            ResultSet rset = state.executeQuery(query);
            while (defTabMod.getRowCount() > 0) {
                defTabMod.removeRow(0);
            }
            Object[] row = new Object[3];
            int count = 0;
            String prevd = "";
            while (rset.next()) {
                if (count == 0) {
                    prevd = rset.getString(1);
                    row[0] = rset.getString(1);
                } else if (rset.getString(1).equalsIgnoreCase(prevd)) {
                    row[0] = "";
                } else {
                    row[0] = rset.getString(1);
                    prevd = rset.getString(1);
                }
                if (this.dailyPettyCatRadioBtn.isSelected()) {
                    row[1] = rset.getString(2);
                    row[2] = this.decHelp.FormatNumber(rset.getDouble(3));
                } else {
                    row[1] = "";
                    row[2] = this.decHelp.FormatNumber(rset.getDouble(2));
                }
                defTabMod.addRow(row);
                count++;
            }
            state.close();
            connect.close();
            this.DailyPettyCashTable.setModel(defTabMod);
            designTable(this.DailyPettyCashTable, 1);
        }
    }

    public void updateMonthlyPettyCash() throws SQLException {
        if (pettyCashType.getItemCount() > 0 && pettyCashType1.getItemCount() > 0) {
            String query;
            DefaultTableModel defTabMod = (DefaultTableModel) this.monthlyPettyCashTable.getModel();
            Connection connect = DriverManager.getConnection(this.driver, this.f_user, this.f_pass);
            Statement state = connect.createStatement();
            if (!this.monthlyPettyCatRadioBtn.isSelected()) {
                query = "select month(pty_date) as monthly, sum(pty_amount) from cashtransactions.petty_cash where year(pty_date) = " + this.yearCombo.getSelectedItem().toString() + " group by month(pty_date) order by pty_date asc, pty_amount desc";
            } else if (this.pettyCashType1.getSelectedIndex() != 0) {
                query = "select month(pty_date), pty_type, sum(pty_amount) from cashtransactions.petty_cash where year(pty_date) = " + this.yearCombo.getSelectedItem().toString() + " and pty_type = '" + this.pettyCashType1.getSelectedItem().toString() + "'" + " group by month(pty_date), pty_type order by pty_date asc, pty_type desc, pty_amount desc";
            } else {
                query = "select month(pty_date), pty_type, sum(pty_amount) from cashtransactions.petty_cash where year(pty_date) = " + this.yearCombo.getSelectedItem().toString() + " group by month(pty_date), pty_type order by pty_date asc, pty_type desc, pty_amount desc";
            }
//            System.out.println(query);
            ResultSet rset = state.executeQuery(query);
            while (defTabMod.getRowCount() > 0) {
                defTabMod.removeRow(0);
            }
            Object[] row = new Object[3];
            int count = 0;
            int prevd = 0;
            while (rset.next()) {
                if (count == 0) {
                    prevd = rset.getInt(1);
                    row[0] = getMonthName(Integer.valueOf(rset.getInt(1))).concat(" " + this.yearCombo.getSelectedItem().toString());
                } else if (rset.getInt(1) == prevd) {
                    row[0] = "";
                } else {
                    row[0] = getMonthName(Integer.valueOf(rset.getInt(1))).concat(" " + this.yearCombo.getSelectedItem().toString());
                    prevd = rset.getInt(1);
                }
                if (this.monthlyPettyCatRadioBtn.isSelected()) {
                    row[1] = rset.getString(2);
                    row[2] = this.decHelp.FormatNumber(rset.getDouble(3));
                } else {
                    row[1] = "";
                    row[2] = this.decHelp.FormatNumber(rset.getDouble(2));
                }
                defTabMod.addRow(row);
                count++;
            }
            state.close();
            connect.close();
            this.monthlyPettyCashTable.setModel(defTabMod);
            designTable(this.monthlyPettyCashTable, 1);
        }
    }

    public void updateDailyAdditionals() throws SQLException {
        if (addlTypeDaily.getItemCount() > 0) {
            String query;
            DefaultTableModel defTabMod = (DefaultTableModel) this.dailyAdditionalTable.getModel();
            Connection connect = DriverManager.getConnection(this.driver, this.f_user, this.f_pass);
            Statement state = connect.createStatement();
            if (!this.catAddlDailyRadioBtn.isSelected()) {
                query = "select add_date, sum(add_amount) as total from cashtransactions.additionals where month(add_date) = " + (this.monthCombo.getSelectedIndex() + 1) + " and year(add_date) = " + this.yearCombo.getSelectedItem().toString() + " group by add_date order by add_date asc, total desc";
            } else if (this.addlTypeDaily.getSelectedIndex() != 0) {
                query = "select add_date, add_type, sum(add_amount) as total from cashtransactions.additionals where month(add_date) = " + (this.monthCombo.getSelectedIndex() + 1) + " and year(add_date) = " + this.yearCombo.getSelectedItem().toString() + " and add_type = '" + this.addlTypeDaily.getSelectedItem().toString() + "'" + " group by add_type, add_date order by add_date asc, add_type desc, total desc";
            } else {
                query = "select add_date, add_type, sum(add_amount) as total from cashtransactions.additionals where month(add_date) = " + (this.monthCombo.getSelectedIndex() + 1) + " and year(add_date) = " + this.yearCombo.getSelectedItem().toString() + " group by add_type, add_date order by add_date asc, add_type desc, total desc";
            }
//            System.out.println(query);
            ResultSet rset = state.executeQuery(query);
            while (defTabMod.getRowCount() > 0) {
                defTabMod.removeRow(0);
            }
            Object[] row = new Object[3];
            int count = 0;
            String prevd = "";
            while (rset.next()) {
                if (count == 0) {
                    prevd = rset.getString(1);
                    row[0] = rset.getString(1);
                } else if (rset.getString(1).equalsIgnoreCase(prevd)) {
                    row[0] = "";
                } else {
                    row[0] = rset.getString(1);
                    prevd = rset.getString(1);
                }
                if (this.catAddlDailyRadioBtn.isSelected()) {
                    row[1] = rset.getString(2);
                    row[2] = this.decHelp.FormatNumber(rset.getDouble(3));
                } else {
                    row[1] = "";
                    row[2] = this.decHelp.FormatNumber(rset.getDouble(2));
                }
                defTabMod.addRow(row);
                count++;
            }
            state.close();
            connect.close();
            this.dailyAdditionalTable.setModel(defTabMod);
            designTable(this.dailyAdditionalTable, 1);
        }
    }

    public void updateMonthlyAdditionals() throws SQLException {
        if (addlTypeMonthly.getItemCount() > 0) {
            String query;
            DefaultTableModel defTabMod = (DefaultTableModel) this.monthlyAdditionalTable.getModel();
            Connection connect = DriverManager.getConnection(this.driver, this.f_user, this.f_pass);
            Statement state = connect.createStatement();
            if (!this.catAddlMonthlyRadioBtn.isSelected()) {
                query = "select month(add_date) as monthly, sum(add_amount) from cashtransactions.additionals where year(add_date) = " + this.yearCombo.getSelectedItem().toString() + " group by month(add_date) order by add_date asc, add_amount desc";
            } else if (this.addlTypeMonthly.getSelectedIndex() != 0) {
                query = "select month(add_date), add_type, sum(add_amount) from cashtransactions.additionals where year(add_date) = " + this.yearCombo.getSelectedItem().toString() + " and add_type = '" + this.addlTypeMonthly.getSelectedItem().toString() + "'" + " group by month(add_date), add_type order by add_date asc, add_type desc, add_amount desc";
            } else {
                query = "select month(add_date), add_type, sum(add_amount) from cashtransactions.additionals where year(add_date) = " + this.yearCombo.getSelectedItem().toString() + " group by month(add_date), add_type order by add_date asc, add_type desc, add_amount desc";
            }
//            System.out.println(query);
            ResultSet rset = state.executeQuery(query);
            while (defTabMod.getRowCount() > 0) {
                defTabMod.removeRow(0);
            }
            Object[] row = new Object[3];
            int count = 0;
            int prevd = 0;
            while (rset.next()) {
                if (count == 0) {
                    prevd = rset.getInt(1);
                    row[0] = getMonthName(Integer.valueOf(rset.getInt(1))).concat(" " + this.yearCombo.getSelectedItem().toString());
                } else if (rset.getInt(1) == prevd) {
                    row[0] = "";
                } else {
                    row[0] = getMonthName(Integer.valueOf(rset.getInt(1))).concat(" " + this.yearCombo.getSelectedItem().toString());
                    prevd = rset.getInt(1);
                }
                if (this.catAddlMonthlyRadioBtn.isSelected()) {
                    row[1] = rset.getString(2);
                    row[2] = this.decHelp.FormatNumber(rset.getDouble(3));
                } else {
                    row[1] = "";
                    row[2] = this.decHelp.FormatNumber(rset.getDouble(2));
                }
                defTabMod.addRow(row);
                count++;
            }
            state.close();
            connect.close();
            this.monthlyAdditionalTable.setModel(defTabMod);
            designTable(this.monthlyAdditionalTable, 1);
        }
    }

    private String getMonthName(Integer mo) {
        String month = "";
        switch (mo.intValue()) {
            case 1:
                month = "January";
                break;
            case 2:
                month = "February";
                break;
            case 3:
                month = "March";
                break;
            case 4:
                month = "April";
                break;
            case 5:
                month = "May";
                break;
            case 6:
                month = "June";
                break;
            case 7:
                month = "July";
                break;
            case 8:
                month = "August";
                break;
            case 9:
                month = "September";
                break;
            case 10:
                month = "October";
                break;
            case 11:
                month = "November";
                break;
            case 12:
                month = "December";
                break;
        }
        return month;
    }

    public void updateTableContainer() {
        switch (this.jTabbedPane1.getSelectedIndex()) {
            case 0:
                this.monthCombo.setEnabled(true);
                try {
                    updateDailyEmpeno();
                } catch (SQLException ex) {
                    this.con.saveProp("mpis_last_error", String.valueOf(ex));
                    Logger.getLogger(Statistics.class.getName()).log(Level.SEVERE, (String) null, ex);
                }
                break;
            case 1:
                this.monthCombo.setEnabled(false);
                try {
                    updateMonthlyEmpeno();
                } catch (SQLException ex) {
                    this.con.saveProp("mpis_last_error", String.valueOf(ex));
                    Logger.getLogger(Statistics.class.getName()).log(Level.SEVERE, (String) null, ex);
                }
                break;
            case 3:
                this.monthCombo.setEnabled(false);
                try {
                    updateMonthlyRescate();
                } catch (SQLException ex) {
                    this.con.saveProp("mpis_last_error", String.valueOf(ex));
                    Logger.getLogger(Statistics.class.getName()).log(Level.SEVERE, (String) null, ex);
                }
                break;
            case 2:
                this.monthCombo.setEnabled(true);
                try {
                    updateDailyRescate();
                } catch (SQLException ex) {
                    this.con.saveProp("mpis_last_error", String.valueOf(ex));
                    Logger.getLogger(Statistics.class.getName()).log(Level.SEVERE, (String) null, ex);
                }
                break;
            case 4:
                this.monthCombo.setEnabled(true);
                this.transDateRet.setDate(Calendar.getInstance().getTime());
                try {
                    updateDailyTransfers();
                } catch (SQLException ex) {
                    this.con.saveProp("mpis_last_error", String.valueOf(ex));
                    Logger.getLogger(Statistics.class.getName()).log(Level.SEVERE, (String) null, ex);
                }
                break;
            case 5:
                this.monthCombo.setEnabled(false);
                try {
                    updateMonthlyTransfers();
                } catch (SQLException ex) {
                    this.con.saveProp("mpis_last_error", String.valueOf(ex));
                    Logger.getLogger(Statistics.class.getName()).log(Level.SEVERE, (String) null, ex);
                }
                break;
            case 6:
                this.monthCombo.setEnabled(true);
                try {
                    updateDailyPettyCash();
                    updateMonthlyPettyCash();
                } catch (SQLException ex) {
                    this.con.saveProp("mpis_last_error", String.valueOf(ex));
                    Logger.getLogger(Statistics.class.getName()).log(Level.SEVERE, (String) null, ex);
                }
                break;
            case 7:
                this.monthCombo.setEnabled(true);
                try {
                    updateAddlCategories();
                    updateDailyAdditionals();
                    updateMonthlyAdditionals();
                } catch (SQLException ex) {
                    this.con.saveProp("mpis_last_error", String.valueOf(ex));
                    Logger.getLogger(Statistics.class.getName()).log(Level.SEVERE, (String) null, ex);
                }
                break;
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

        jScrollPane1 = new javax.swing.JScrollPane();
        jPanel11 = new javax.swing.JPanel();
        jPanel9 = new javax.swing.JPanel();
        jLabel6 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        yearCombo = new javax.swing.JComboBox<>();
        monthCombo = new javax.swing.JComboBox<>();
        jLabel4 = new javax.swing.JLabel();
        branch = new javax.swing.JComboBox<>();
        jPanel10 = new javax.swing.JPanel();
        jTabbedPane1 = new javax.swing.JTabbedPane();
        jPanel3 = new javax.swing.JPanel();
        jScrollPane2 = new javax.swing.JScrollPane();
        daiEmp = new javax.swing.JTable();
        jLabel12 = new javax.swing.JLabel();
        totLotes = new javax.swing.JTextField();
        totCapital = new javax.swing.JTextField();
        totAI = new javax.swing.JTextField();
        totSC = new javax.swing.JTextField();
        totIns = new javax.swing.JTextField();
        jPanel1 = new javax.swing.JPanel();
        jScrollPane3 = new javax.swing.JScrollPane();
        monEmp = new javax.swing.JTable();
        jLabel13 = new javax.swing.JLabel();
        totLotes1 = new javax.swing.JTextField();
        totCapital1 = new javax.swing.JTextField();
        totAI1 = new javax.swing.JTextField();
        totSC1 = new javax.swing.JTextField();
        totIns1 = new javax.swing.JTextField();
        jPanel2 = new javax.swing.JPanel();
        jScrollPane4 = new javax.swing.JScrollPane();
        daiRes = new javax.swing.JTable();
        jLabel14 = new javax.swing.JLabel();
        totLotes2 = new javax.swing.JTextField();
        totCapital2 = new javax.swing.JTextField();
        totAI2 = new javax.swing.JTextField();
        totSC2 = new javax.swing.JTextField();
        jPanel4 = new javax.swing.JPanel();
        jScrollPane5 = new javax.swing.JScrollPane();
        monRes = new javax.swing.JTable();
        jLabel15 = new javax.swing.JLabel();
        totLotes4 = new javax.swing.JTextField();
        totCapital4 = new javax.swing.JTextField();
        totAI4 = new javax.swing.JTextField();
        totSC4 = new javax.swing.JTextField();
        jPanel5 = new javax.swing.JPanel();
        jLabel2 = new javax.swing.JLabel();
        transDateRet = new org.jdesktop.swingx.JXDatePicker();
        jRadioButton1 = new javax.swing.JRadioButton();
        jScrollPane6 = new javax.swing.JScrollPane();
        transfersTable2 = new javax.swing.JTable();
        jLabel7 = new javax.swing.JLabel();
        jScrollPane7 = new javax.swing.JScrollPane();
        transfersTable = new javax.swing.JTable();
        jPanel6 = new javax.swing.JPanel();
        jScrollPane8 = new javax.swing.JScrollPane();
        transfersTable3 = new javax.swing.JTable();
        jLabel8 = new javax.swing.JLabel();
        jLabel9 = new javax.swing.JLabel();
        jScrollPane9 = new javax.swing.JScrollPane();
        transfersTable4 = new javax.swing.JTable();
        jPanel7 = new javax.swing.JPanel();
        jLabel10 = new javax.swing.JLabel();
        dailyPettyCatRadioBtn = new javax.swing.JRadioButton();
        pettyCashType = new javax.swing.JComboBox<>();
        jLabel11 = new javax.swing.JLabel();
        monthlyPettyCatRadioBtn = new javax.swing.JRadioButton();
        pettyCashType1 = new javax.swing.JComboBox<>();
        jScrollPane10 = new javax.swing.JScrollPane();
        DailyPettyCashTable = new javax.swing.JTable();
        jScrollPane11 = new javax.swing.JScrollPane();
        monthlyPettyCashTable = new javax.swing.JTable();
        jPanel8 = new javax.swing.JPanel();
        jScrollPane12 = new javax.swing.JScrollPane();
        dailyAdditionalTable = new javax.swing.JTable();
        jLabel16 = new javax.swing.JLabel();
        catAddlDailyRadioBtn = new javax.swing.JRadioButton();
        addlTypeDaily = new javax.swing.JComboBox<>();
        jLabel17 = new javax.swing.JLabel();
        catAddlMonthlyRadioBtn = new javax.swing.JRadioButton();
        addlTypeMonthly = new javax.swing.JComboBox<>();
        jScrollPane13 = new javax.swing.JScrollPane();
        monthlyAdditionalTable = new javax.swing.JTable();

        setPreferredSize(new java.awt.Dimension(840, 768));
        setLayout(new java.awt.BorderLayout());

        jPanel11.setLayout(new java.awt.BorderLayout());

        jPanel9.setBackground(new java.awt.Color(255, 255, 255));

        jLabel6.setForeground(new java.awt.Color(102, 102, 102));
        jLabel6.setText("Year:");

        jLabel3.setForeground(new java.awt.Color(102, 102, 102));
        jLabel3.setText("Branch");

        yearCombo.setFont(new java.awt.Font("Segoe UI Semibold", 0, 13)); // NOI18N
        yearCombo.setForeground(new java.awt.Color(51, 51, 51));
        yearCombo.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "2022", "2021", "2020", "2019", "2018", "2017" }));
        yearCombo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                yearComboActionPerformed(evt);
            }
        });

        monthCombo.setFont(new java.awt.Font("Segoe UI Semibold", 0, 13)); // NOI18N
        monthCombo.setForeground(new java.awt.Color(51, 51, 51));
        monthCombo.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "January", "February", "March", "April", "May", "June", "July", "August", "September", "October", "November", "December" }));
        monthCombo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                monthComboActionPerformed(evt);
            }
        });

        jLabel4.setForeground(new java.awt.Color(102, 102, 102));
        jLabel4.setText("Month:");

        branch.setFont(new java.awt.Font("Segoe UI Semibold", 0, 13)); // NOI18N
        branch.setForeground(new java.awt.Color(51, 51, 51));
        branch.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        branch.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                branchActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel9Layout = new javax.swing.GroupLayout(jPanel9);
        jPanel9.setLayout(jPanel9Layout);
        jPanel9Layout.setHorizontalGroup(
            jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel9Layout.createSequentialGroup()
                .addGap(20, 20, 20)
                .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel3)
                    .addComponent(branch, javax.swing.GroupLayout.PREFERRED_SIZE, 135, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel4)
                    .addComponent(monthCombo, javax.swing.GroupLayout.PREFERRED_SIZE, 120, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel6)
                    .addComponent(yearCombo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(469, Short.MAX_VALUE))
        );
        jPanel9Layout.setVerticalGroup(
            jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel9Layout.createSequentialGroup()
                .addGap(17, 17, 17)
                .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel3)
                    .addComponent(jLabel4)
                    .addComponent(jLabel6))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(branch, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(monthCombo, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(yearCombo, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jPanel11.add(jPanel9, java.awt.BorderLayout.NORTH);

        jPanel10.setLayout(new java.awt.BorderLayout());

        jTabbedPane1.setForeground(new java.awt.Color(51, 51, 51));
        jTabbedPane1.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jTabbedPane1MouseClicked(evt);
            }
        });

        daiEmp.setBackground(new java.awt.Color(240, 240, 240));
        daiEmp.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null}
            },
            new String [] {
                "Date", "Lotes", "Capital", "Advance Interest", "Service Charge", "Insurance"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        daiEmp.setFillsViewportHeight(true);
        daiEmp.setGridColor(new java.awt.Color(240, 240, 240));
        daiEmp.setRowHeight(30);
        jScrollPane2.setViewportView(daiEmp);

        jLabel12.setBackground(new java.awt.Color(51, 51, 51));
        jLabel12.setFont(new java.awt.Font("Segoe UI", 1, 13)); // NOI18N
        jLabel12.setForeground(new java.awt.Color(51, 51, 51));
        jLabel12.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel12.setText("TOTAL");

        totLotes.setFont(new java.awt.Font("Segoe UI", 1, 13)); // NOI18N
        totLotes.setForeground(new java.awt.Color(51, 51, 51));
        totLotes.setHorizontalAlignment(javax.swing.JTextField.TRAILING);

        totCapital.setFont(new java.awt.Font("Segoe UI", 1, 13)); // NOI18N
        totCapital.setForeground(new java.awt.Color(51, 51, 51));
        totCapital.setHorizontalAlignment(javax.swing.JTextField.TRAILING);

        totAI.setFont(new java.awt.Font("Segoe UI", 1, 13)); // NOI18N
        totAI.setForeground(new java.awt.Color(51, 51, 51));
        totAI.setHorizontalAlignment(javax.swing.JTextField.TRAILING);

        totSC.setFont(new java.awt.Font("Segoe UI", 1, 13)); // NOI18N
        totSC.setForeground(new java.awt.Color(51, 51, 51));
        totSC.setHorizontalAlignment(javax.swing.JTextField.TRAILING);

        totIns.setFont(new java.awt.Font("Segoe UI", 1, 13)); // NOI18N
        totIns.setForeground(new java.awt.Color(51, 51, 51));
        totIns.setHorizontalAlignment(javax.swing.JTextField.TRAILING);

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addComponent(jScrollPane2)
                        .addContainerGap())
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addComponent(jLabel12, javax.swing.GroupLayout.PREFERRED_SIZE, 114, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(39, 39, 39)
                        .addComponent(totLotes, javax.swing.GroupLayout.DEFAULT_SIZE, 114, Short.MAX_VALUE)
                        .addGap(24, 24, 24)
                        .addComponent(totCapital, javax.swing.GroupLayout.DEFAULT_SIZE, 114, Short.MAX_VALUE)
                        .addGap(24, 24, 24)
                        .addComponent(totAI, javax.swing.GroupLayout.DEFAULT_SIZE, 114, Short.MAX_VALUE)
                        .addGap(24, 24, 24)
                        .addComponent(totSC, javax.swing.GroupLayout.DEFAULT_SIZE, 114, Short.MAX_VALUE)
                        .addGap(24, 24, 24)
                        .addComponent(totIns, javax.swing.GroupLayout.DEFAULT_SIZE, 114, Short.MAX_VALUE)
                        .addGap(15, 15, 15))))
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 702, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel12, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(totCapital, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(totAI, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(totSC, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(totIns, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(totLotes, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
        );

        jTabbedPane1.addTab("Daily Empeno", jPanel3);

        monEmp.setBackground(new java.awt.Color(240, 240, 240));
        monEmp.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null}
            },
            new String [] {
                "Date", "Lotes", "Capital", "Advance Interest", "Service Charge", "Insurance"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        monEmp.setFillsViewportHeight(true);
        monEmp.setGridColor(new java.awt.Color(240, 240, 240));
        jScrollPane3.setViewportView(monEmp);

        jLabel13.setFont(new java.awt.Font("Segoe UI", 1, 13)); // NOI18N
        jLabel13.setForeground(new java.awt.Color(51, 51, 51));
        jLabel13.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel13.setText("TOTAL");

        totLotes1.setFont(new java.awt.Font("Segoe UI", 1, 13)); // NOI18N
        totLotes1.setForeground(new java.awt.Color(51, 51, 51));
        totLotes1.setHorizontalAlignment(javax.swing.JTextField.TRAILING);

        totCapital1.setFont(new java.awt.Font("Segoe UI", 1, 13)); // NOI18N
        totCapital1.setForeground(new java.awt.Color(51, 51, 51));
        totCapital1.setHorizontalAlignment(javax.swing.JTextField.TRAILING);

        totAI1.setFont(new java.awt.Font("Segoe UI", 1, 13)); // NOI18N
        totAI1.setForeground(new java.awt.Color(51, 51, 51));
        totAI1.setHorizontalAlignment(javax.swing.JTextField.TRAILING);

        totSC1.setFont(new java.awt.Font("Segoe UI", 1, 13)); // NOI18N
        totSC1.setForeground(new java.awt.Color(51, 51, 51));
        totSC1.setHorizontalAlignment(javax.swing.JTextField.TRAILING);

        totIns1.setFont(new java.awt.Font("Segoe UI", 1, 13)); // NOI18N
        totIns1.setForeground(new java.awt.Color(51, 51, 51));
        totIns1.setHorizontalAlignment(javax.swing.JTextField.TRAILING);

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane3)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jLabel13, javax.swing.GroupLayout.PREFERRED_SIZE, 114, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(42, 42, 42)
                        .addComponent(totLotes1, javax.swing.GroupLayout.DEFAULT_SIZE, 115, Short.MAX_VALUE)
                        .addGap(24, 24, 24)
                        .addComponent(totCapital1, javax.swing.GroupLayout.DEFAULT_SIZE, 114, Short.MAX_VALUE)
                        .addGap(24, 24, 24)
                        .addComponent(totAI1, javax.swing.GroupLayout.DEFAULT_SIZE, 115, Short.MAX_VALUE)
                        .addGap(24, 24, 24)
                        .addComponent(totSC1, javax.swing.GroupLayout.DEFAULT_SIZE, 115, Short.MAX_VALUE)
                        .addGap(24, 24, 24)
                        .addComponent(totIns1, javax.swing.GroupLayout.DEFAULT_SIZE, 117, Short.MAX_VALUE)))
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane3, javax.swing.GroupLayout.DEFAULT_SIZE, 702, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel13, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(totLotes1, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(totCapital1, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(totAI1, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(totSC1, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(totIns1, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
        );

        jTabbedPane1.addTab("Monthly Empeno", jPanel1);

        daiRes.setBackground(new java.awt.Color(240, 240, 240));
        daiRes.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null}
            },
            new String [] {
                "Date", "Lotes", "Capital", "Interest", "Total"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        daiRes.setFillsViewportHeight(true);
        daiRes.setGridColor(new java.awt.Color(240, 240, 240));
        jScrollPane4.setViewportView(daiRes);

        jLabel14.setFont(new java.awt.Font("Segoe UI", 1, 13)); // NOI18N
        jLabel14.setForeground(new java.awt.Color(51, 51, 51));
        jLabel14.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel14.setText("TOTAL");

        totLotes2.setFont(new java.awt.Font("Segoe UI", 1, 13)); // NOI18N
        totLotes2.setForeground(new java.awt.Color(51, 51, 51));
        totLotes2.setHorizontalAlignment(javax.swing.JTextField.TRAILING);

        totCapital2.setFont(new java.awt.Font("Segoe UI", 1, 13)); // NOI18N
        totCapital2.setForeground(new java.awt.Color(51, 51, 51));
        totCapital2.setHorizontalAlignment(javax.swing.JTextField.TRAILING);

        totAI2.setFont(new java.awt.Font("Segoe UI", 1, 13)); // NOI18N
        totAI2.setForeground(new java.awt.Color(51, 51, 51));
        totAI2.setHorizontalAlignment(javax.swing.JTextField.TRAILING);

        totSC2.setFont(new java.awt.Font("Segoe UI", 1, 13)); // NOI18N
        totSC2.setForeground(new java.awt.Color(51, 51, 51));
        totSC2.setHorizontalAlignment(javax.swing.JTextField.TRAILING);

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane4)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addComponent(jLabel14, javax.swing.GroupLayout.PREFERRED_SIZE, 138, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(74, 74, 74)
                        .addComponent(totLotes2, javax.swing.GroupLayout.DEFAULT_SIZE, 138, Short.MAX_VALUE)
                        .addGap(24, 24, 24)
                        .addComponent(totCapital2, javax.swing.GroupLayout.DEFAULT_SIZE, 139, Short.MAX_VALUE)
                        .addGap(24, 24, 24)
                        .addComponent(totAI2, javax.swing.GroupLayout.DEFAULT_SIZE, 133, Short.MAX_VALUE)
                        .addGap(24, 24, 24)
                        .addComponent(totSC2, javax.swing.GroupLayout.DEFAULT_SIZE, 134, Short.MAX_VALUE)))
                .addContainerGap())
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane4, javax.swing.GroupLayout.DEFAULT_SIZE, 702, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel14, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(totLotes2, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(totCapital2, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(totAI2, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(totSC2, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
        );

        jTabbedPane1.addTab("Daily Rescate", jPanel2);

        monRes.setBackground(new java.awt.Color(240, 240, 240));
        monRes.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null}
            },
            new String [] {
                "Date", "Lotes", "Capital", "Interest", "Total"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        monRes.setFillsViewportHeight(true);
        monRes.setGridColor(new java.awt.Color(240, 240, 240));
        jScrollPane5.setViewportView(monRes);

        jLabel15.setFont(new java.awt.Font("Segoe UI", 1, 13)); // NOI18N
        jLabel15.setForeground(new java.awt.Color(51, 51, 51));
        jLabel15.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel15.setText("TOTAL");

        totLotes4.setFont(new java.awt.Font("Segoe UI", 1, 13)); // NOI18N
        totLotes4.setForeground(new java.awt.Color(51, 51, 51));
        totLotes4.setHorizontalAlignment(javax.swing.JTextField.TRAILING);

        totCapital4.setFont(new java.awt.Font("Segoe UI", 1, 13)); // NOI18N
        totCapital4.setForeground(new java.awt.Color(51, 51, 51));
        totCapital4.setHorizontalAlignment(javax.swing.JTextField.TRAILING);

        totAI4.setFont(new java.awt.Font("Segoe UI", 1, 13)); // NOI18N
        totAI4.setForeground(new java.awt.Color(51, 51, 51));
        totAI4.setHorizontalAlignment(javax.swing.JTextField.TRAILING);

        totSC4.setFont(new java.awt.Font("Segoe UI", 1, 13)); // NOI18N
        totSC4.setForeground(new java.awt.Color(51, 51, 51));
        totSC4.setHorizontalAlignment(javax.swing.JTextField.TRAILING);

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addGap(90, 90, 90)
                        .addComponent(jLabel15, javax.swing.GroupLayout.PREFERRED_SIZE, 140, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(totLotes4, javax.swing.GroupLayout.DEFAULT_SIZE, 138, Short.MAX_VALUE)
                        .addGap(18, 18, 18)
                        .addComponent(totCapital4, javax.swing.GroupLayout.DEFAULT_SIZE, 139, Short.MAX_VALUE)
                        .addGap(18, 18, 18)
                        .addComponent(totAI4, javax.swing.GroupLayout.DEFAULT_SIZE, 133, Short.MAX_VALUE)
                        .addGap(18, 18, 18)
                        .addComponent(totSC4, javax.swing.GroupLayout.DEFAULT_SIZE, 134, Short.MAX_VALUE))
                    .addComponent(jScrollPane5))
                .addContainerGap())
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane5, javax.swing.GroupLayout.DEFAULT_SIZE, 702, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel15, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(totLotes4, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(totCapital4, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(totAI4, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(totSC4, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
        );

        jTabbedPane1.addTab("Monthly Rescate", jPanel4);

        jLabel2.setFont(new java.awt.Font("Segoe UI Semibold", 1, 13)); // NOI18N
        jLabel2.setForeground(new java.awt.Color(102, 102, 102));
        jLabel2.setText("Fund Transfers to Other MPI Branches:");

        transDateRet.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                transDateRetActionPerformed(evt);
            }
        });

        jRadioButton1.setFont(new java.awt.Font("Segoe UI Semibold", 0, 13)); // NOI18N
        jRadioButton1.setForeground(new java.awt.Color(102, 102, 102));
        jRadioButton1.setSelected(true);
        jRadioButton1.setText("Show Table by Specific Date:");
        jRadioButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jRadioButton1ActionPerformed(evt);
            }
        });

        transfersTable2.setBackground(new java.awt.Color(240, 240, 240));
        transfersTable2.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Date", "From Branch", "To Branch", "Amount"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        transfersTable2.setFillsViewportHeight(true);
        transfersTable2.setGridColor(new java.awt.Color(240, 240, 240));
        jScrollPane6.setViewportView(transfersTable2);

        jLabel7.setFont(new java.awt.Font("Segoe UI Semibold", 0, 13)); // NOI18N
        jLabel7.setForeground(new java.awt.Color(102, 102, 102));
        jLabel7.setText("Fund Transfers Within the Branch (MPI to Palawan EPP / MPI to Money Changer):");

        transfersTable.setBackground(new java.awt.Color(240, 240, 240));
        transfersTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Date", "From Branch", "To Branch", "Amount"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        transfersTable.setFillsViewportHeight(true);
        transfersTable.setGridColor(new java.awt.Color(240, 240, 240));
        jScrollPane7.setViewportView(transfersTable);

        javax.swing.GroupLayout jPanel5Layout = new javax.swing.GroupLayout(jPanel5);
        jPanel5.setLayout(jPanel5Layout);
        jPanel5Layout.setHorizontalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane7)
                    .addGroup(jPanel5Layout.createSequentialGroup()
                        .addComponent(jLabel2)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 259, Short.MAX_VALUE)
                        .addComponent(jRadioButton1)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(transDateRet, javax.swing.GroupLayout.PREFERRED_SIZE, 133, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel5Layout.createSequentialGroup()
                        .addComponent(jLabel7)
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addComponent(jScrollPane6))
                .addContainerGap())
        );
        jPanel5Layout.setVerticalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(transDateRet, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jRadioButton1)
                    .addComponent(jLabel2))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane7, javax.swing.GroupLayout.DEFAULT_SIZE, 307, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel7)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane6, javax.swing.GroupLayout.DEFAULT_SIZE, 365, Short.MAX_VALUE)
                .addContainerGap())
        );

        jTabbedPane1.addTab("Daily Transfers", jPanel5);

        transfersTable3.setBackground(new java.awt.Color(240, 240, 240));
        transfersTable3.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Date", "From Branch", "To Branch", "Amount"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        transfersTable3.setFillsViewportHeight(true);
        transfersTable3.setGridColor(new java.awt.Color(240, 240, 240));
        jScrollPane8.setViewportView(transfersTable3);

        jLabel8.setFont(new java.awt.Font("Segoe UI Semibold", 0, 13)); // NOI18N
        jLabel8.setForeground(new java.awt.Color(102, 102, 102));
        jLabel8.setText("Fund Transfers to Other MPI Branches:");

        jLabel9.setFont(new java.awt.Font("Segoe UI Semibold", 0, 13)); // NOI18N
        jLabel9.setForeground(new java.awt.Color(102, 102, 102));
        jLabel9.setText("Fund Transfers Within the Branch (MPI to Palawan EPP / MPI to Money Changer):");

        transfersTable4.setBackground(new java.awt.Color(240, 240, 240));
        transfersTable4.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Date", "From Branch", "To Branch", "Amount"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        transfersTable4.setFillsViewportHeight(true);
        transfersTable4.setGridColor(new java.awt.Color(240, 240, 240));
        jScrollPane9.setViewportView(transfersTable4);

        javax.swing.GroupLayout jPanel6Layout = new javax.swing.GroupLayout(jPanel6);
        jPanel6.setLayout(jPanel6Layout);
        jPanel6Layout.setHorizontalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel6Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel6Layout.createSequentialGroup()
                        .addComponent(jLabel8)
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(jPanel6Layout.createSequentialGroup()
                        .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jScrollPane9, javax.swing.GroupLayout.DEFAULT_SIZE, 828, Short.MAX_VALUE)
                            .addGroup(jPanel6Layout.createSequentialGroup()
                                .addComponent(jLabel9)
                                .addGap(0, 0, Short.MAX_VALUE))
                            .addComponent(jScrollPane8))
                        .addContainerGap())))
        );
        jPanel6Layout.setVerticalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel6Layout.createSequentialGroup()
                .addGap(10, 10, 10)
                .addComponent(jLabel8)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane8, javax.swing.GroupLayout.DEFAULT_SIZE, 298, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel9)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane9, javax.swing.GroupLayout.DEFAULT_SIZE, 382, Short.MAX_VALUE)
                .addContainerGap())
        );

        jTabbedPane1.addTab("Monthly Transfers", jPanel6);

        jLabel10.setFont(new java.awt.Font("Segoe UI", 1, 13)); // NOI18N
        jLabel10.setForeground(new java.awt.Color(51, 51, 51));
        jLabel10.setText("Daily:");

        dailyPettyCatRadioBtn.setFont(new java.awt.Font("Segoe UI", 1, 13)); // NOI18N
        dailyPettyCatRadioBtn.setForeground(new java.awt.Color(51, 51, 51));
        dailyPettyCatRadioBtn.setSelected(true);
        dailyPettyCatRadioBtn.setText("Categorized by Type:");
        dailyPettyCatRadioBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                dailyPettyCatRadioBtnActionPerformed(evt);
            }
        });

        pettyCashType.setFont(new java.awt.Font("Segoe UI", 1, 13)); // NOI18N
        pettyCashType.setForeground(new java.awt.Color(51, 51, 51));
        pettyCashType.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "All", "Rentals", "Salaries", "Gas & Fuel", "Transportation", "Water & Electricity", "Office Supplies", "Tel. / Cable & Load", "Periodicals/Postages", "Repairs & Maint.", "Miscellaneous", "Transfer Fund", "Admin. Expense" }));
        pettyCashType.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                pettyCashTypeActionPerformed(evt);
            }
        });

        jLabel11.setFont(new java.awt.Font("Segoe UI", 1, 13)); // NOI18N
        jLabel11.setForeground(new java.awt.Color(51, 51, 51));
        jLabel11.setText("Monthly:");

        monthlyPettyCatRadioBtn.setFont(new java.awt.Font("Segoe UI", 1, 13)); // NOI18N
        monthlyPettyCatRadioBtn.setForeground(new java.awt.Color(51, 51, 51));
        monthlyPettyCatRadioBtn.setSelected(true);
        monthlyPettyCatRadioBtn.setText("Categorized by Type:");
        monthlyPettyCatRadioBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                monthlyPettyCatRadioBtnActionPerformed(evt);
            }
        });

        pettyCashType1.setFont(new java.awt.Font("Segoe UI", 1, 13)); // NOI18N
        pettyCashType1.setForeground(new java.awt.Color(51, 51, 51));
        pettyCashType1.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "All", "Rentals", "Salaries", "Gas & Fuel", "Transportation", "Water & Electricity", "Office Supplies", "Tel. / Cable & Load", "Periodicals/Postages", "Repairs & Maint.", "Miscellaneous", "Transfer Fund", "Admin. Expense" }));
        pettyCashType1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                pettyCashType1ActionPerformed(evt);
            }
        });

        DailyPettyCashTable.setBackground(new java.awt.Color(240, 240, 240));
        DailyPettyCashTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null}
            },
            new String [] {
                "Date", "Category", "Amount"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        DailyPettyCashTable.setFillsViewportHeight(true);
        DailyPettyCashTable.setGridColor(new java.awt.Color(240, 240, 240));
        jScrollPane10.setViewportView(DailyPettyCashTable);

        monthlyPettyCashTable.setBackground(new java.awt.Color(240, 240, 240));
        monthlyPettyCashTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null}
            },
            new String [] {
                "Date", "Category", "Amount"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        monthlyPettyCashTable.setFillsViewportHeight(true);
        monthlyPettyCashTable.setGridColor(new java.awt.Color(240, 240, 240));
        jScrollPane11.setViewportView(monthlyPettyCashTable);

        javax.swing.GroupLayout jPanel7Layout = new javax.swing.GroupLayout(jPanel7);
        jPanel7.setLayout(jPanel7Layout);
        jPanel7Layout.setHorizontalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel7Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel7Layout.createSequentialGroup()
                        .addComponent(jLabel10)
                        .addGap(61, 61, 61)
                        .addComponent(dailyPettyCatRadioBtn)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(pettyCashType, javax.swing.GroupLayout.PREFERRED_SIZE, 152, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jScrollPane10, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE))
                .addGap(10, 10, 10)
                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel7Layout.createSequentialGroup()
                        .addComponent(jLabel11)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 54, Short.MAX_VALUE)
                        .addComponent(monthlyPettyCatRadioBtn)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(pettyCashType1, javax.swing.GroupLayout.PREFERRED_SIZE, 141, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jScrollPane11, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE))
                .addContainerGap())
        );
        jPanel7Layout.setVerticalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel7Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel10)
                    .addComponent(dailyPettyCatRadioBtn)
                    .addComponent(pettyCashType, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel11)
                    .addComponent(monthlyPettyCatRadioBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(pettyCashType1, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane11, javax.swing.GroupLayout.DEFAULT_SIZE, 702, Short.MAX_VALUE)
                    .addComponent(jScrollPane10))
                .addContainerGap())
        );

        jTabbedPane1.addTab("Petty Cash", jPanel7);

        dailyAdditionalTable.setBackground(new java.awt.Color(240, 240, 240));
        dailyAdditionalTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null}
            },
            new String [] {
                "Date", "Category", "Amount"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        dailyAdditionalTable.setFillsViewportHeight(true);
        dailyAdditionalTable.setGridColor(new java.awt.Color(240, 240, 240));
        jScrollPane12.setViewportView(dailyAdditionalTable);

        jLabel16.setFont(new java.awt.Font("Segoe UI", 1, 13)); // NOI18N
        jLabel16.setForeground(new java.awt.Color(51, 51, 51));
        jLabel16.setText("Daily:");

        catAddlDailyRadioBtn.setFont(new java.awt.Font("Segoe UI", 1, 13)); // NOI18N
        catAddlDailyRadioBtn.setForeground(new java.awt.Color(51, 51, 51));
        catAddlDailyRadioBtn.setSelected(true);
        catAddlDailyRadioBtn.setText("Categorized by Type:");
        catAddlDailyRadioBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                catAddlDailyRadioBtnActionPerformed(evt);
            }
        });

        addlTypeDaily.setFont(new java.awt.Font("Segoe UI", 1, 13)); // NOI18N
        addlTypeDaily.setForeground(new java.awt.Color(51, 51, 51));
        addlTypeDaily.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "All", "Rentals", "Salaries", "Gas & Fuel", "Transportation", "Water & Electricity", "Office Supplies", "Tel. / Cable & Load", "Periodicals/Postages", "Repairs & Maint.", "Miscellaneous", "Transfer Fund", "Admin. Expense" }));
        addlTypeDaily.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                addlTypeDailyActionPerformed(evt);
            }
        });

        jLabel17.setFont(new java.awt.Font("Segoe UI", 1, 13)); // NOI18N
        jLabel17.setForeground(new java.awt.Color(51, 51, 51));
        jLabel17.setText("Monthly:");

        catAddlMonthlyRadioBtn.setFont(new java.awt.Font("Segoe UI", 1, 13)); // NOI18N
        catAddlMonthlyRadioBtn.setForeground(new java.awt.Color(51, 51, 51));
        catAddlMonthlyRadioBtn.setSelected(true);
        catAddlMonthlyRadioBtn.setText("Categorized by Type:");
        catAddlMonthlyRadioBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                catAddlMonthlyRadioBtnActionPerformed(evt);
            }
        });

        addlTypeMonthly.setFont(new java.awt.Font("Segoe UI", 1, 13)); // NOI18N
        addlTypeMonthly.setForeground(new java.awt.Color(51, 51, 51));
        addlTypeMonthly.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "All", "Rentals", "Salaries", "Gas & Fuel", "Transportation", "Water & Electricity", "Office Supplies", "Tel. / Cable & Load", "Periodicals/Postages", "Repairs & Maint.", "Miscellaneous", "Transfer Fund", "Admin. Expense" }));
        addlTypeMonthly.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                addlTypeMonthlyActionPerformed(evt);
            }
        });

        monthlyAdditionalTable.setBackground(new java.awt.Color(240, 240, 240));
        monthlyAdditionalTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null}
            },
            new String [] {
                "Date", "Category", "Amount"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        monthlyAdditionalTable.setFillsViewportHeight(true);
        monthlyAdditionalTable.setGridColor(new java.awt.Color(240, 240, 240));
        jScrollPane13.setViewportView(monthlyAdditionalTable);

        javax.swing.GroupLayout jPanel8Layout = new javax.swing.GroupLayout(jPanel8);
        jPanel8.setLayout(jPanel8Layout);
        jPanel8Layout.setHorizontalGroup(
            jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel8Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel8Layout.createSequentialGroup()
                        .addComponent(jLabel16)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(catAddlDailyRadioBtn)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(addlTypeDaily, javax.swing.GroupLayout.PREFERRED_SIZE, 152, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jScrollPane12, javax.swing.GroupLayout.DEFAULT_SIZE, 412, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel8Layout.createSequentialGroup()
                        .addComponent(jLabel17)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 52, Short.MAX_VALUE)
                        .addComponent(catAddlMonthlyRadioBtn)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(addlTypeMonthly, javax.swing.GroupLayout.PREFERRED_SIZE, 141, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(8, 8, 8))
                    .addGroup(jPanel8Layout.createSequentialGroup()
                        .addComponent(jScrollPane13, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                        .addContainerGap())))
        );
        jPanel8Layout.setVerticalGroup(
            jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel8Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel16, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(catAddlDailyRadioBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(addlTypeDaily, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel17, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(catAddlMonthlyRadioBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(addlTypeMonthly, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane12, javax.swing.GroupLayout.DEFAULT_SIZE, 702, Short.MAX_VALUE)
                    .addComponent(jScrollPane13))
                .addContainerGap())
        );

        jTabbedPane1.addTab("Additional Fund", jPanel8);

        jPanel10.add(jTabbedPane1, java.awt.BorderLayout.CENTER);

        jPanel11.add(jPanel10, java.awt.BorderLayout.CENTER);

        jScrollPane1.setViewportView(jPanel11);

        add(jScrollPane1, java.awt.BorderLayout.CENTER);
    }// </editor-fold>//GEN-END:initComponents

    private void jTabbedPane1MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jTabbedPane1MouseClicked
        updateTableContainer();
    }//GEN-LAST:event_jTabbedPane1MouseClicked

    private void monthComboActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_monthComboActionPerformed
        if (yearCombo.getItemCount() > 0) {
            updateTableContainer();
        }
    }//GEN-LAST:event_monthComboActionPerformed

    private void yearComboActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_yearComboActionPerformed
        monthComboActionPerformed((ActionEvent)null);
    }//GEN-LAST:event_yearComboActionPerformed

    private void branchActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_branchActionPerformed
        if (this.branch.getItemCount() > 0) monthComboActionPerformed((ActionEvent)null);
    }//GEN-LAST:event_branchActionPerformed

    private void addlTypeMonthlyActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_addlTypeMonthlyActionPerformed
        try {
            updateMonthlyAdditionals();
        } catch (SQLException ex) {
            this.con.saveProp("mpis_last_error", String.valueOf(ex));
            Logger.getLogger(Statistics.class.getName()).log(Level.SEVERE, (String) null, ex);
        }
    }//GEN-LAST:event_addlTypeMonthlyActionPerformed

    private void catAddlMonthlyRadioBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_catAddlMonthlyRadioBtnActionPerformed
        if (this.catAddlMonthlyRadioBtn.isSelected()) {
            this.addlTypeMonthly.setEnabled(true);
        } else {
            this.addlTypeMonthly.setEnabled(false);
        }
        addlTypeMonthlyActionPerformed((ActionEvent) null);
    }//GEN-LAST:event_catAddlMonthlyRadioBtnActionPerformed

    private void addlTypeDailyActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_addlTypeDailyActionPerformed
        try {
            updateDailyAdditionals();
        } catch (SQLException ex) {
            this.con.saveProp("mpis_last_error", String.valueOf(ex));
            Logger.getLogger(Statistics.class.getName()).log(Level.SEVERE, (String) null, ex);
        }
    }//GEN-LAST:event_addlTypeDailyActionPerformed

    private void catAddlDailyRadioBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_catAddlDailyRadioBtnActionPerformed
        if (this.catAddlDailyRadioBtn.isSelected()) {
            this.addlTypeDaily.setEnabled(true);
        } else {
            this.addlTypeDaily.setEnabled(false);
        }
        addlTypeDailyActionPerformed((ActionEvent) null);
    }//GEN-LAST:event_catAddlDailyRadioBtnActionPerformed

    private void pettyCashType1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_pettyCashType1ActionPerformed
        try {
            updateMonthlyPettyCash();
        } catch (SQLException ex) {
            this.con.saveProp("mpis_last_error", String.valueOf(ex));
            Logger.getLogger(Statistics.class.getName()).log(Level.SEVERE, (String) null, ex);
        }
    }//GEN-LAST:event_pettyCashType1ActionPerformed

    private void monthlyPettyCatRadioBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_monthlyPettyCatRadioBtnActionPerformed
        if (this.monthlyPettyCatRadioBtn.isSelected()) {
            this.pettyCashType1.setEnabled(true);
        } else {
            this.pettyCashType1.setEnabled(false);
        }
        pettyCashType1ActionPerformed((ActionEvent) null);
    }//GEN-LAST:event_monthlyPettyCatRadioBtnActionPerformed

    private void pettyCashTypeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_pettyCashTypeActionPerformed
        try {
            updateDailyPettyCash();
        } catch (SQLException ex) {
            this.con.saveProp("mpis_last_error", String.valueOf(ex));
            Logger.getLogger(Statistics.class.getName()).log(Level.SEVERE, (String) null, ex);
        }
    }//GEN-LAST:event_pettyCashTypeActionPerformed

    private void dailyPettyCatRadioBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_dailyPettyCatRadioBtnActionPerformed
        if (this.dailyPettyCatRadioBtn.isSelected()) {
            this.pettyCashType.setEnabled(true);
        } else {
            this.pettyCashType.setEnabled(false);
        }
        pettyCashTypeActionPerformed((ActionEvent) null);
    }//GEN-LAST:event_dailyPettyCatRadioBtnActionPerformed

    private void jRadioButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jRadioButton1ActionPerformed
        if (this.jRadioButton1.isSelected()) {
            this.transDateRet.setEnabled(true);
            transDateRetActionPerformed((ActionEvent) null);
        } else {
            this.transDateRet.setEnabled(false);
            try {
                updateDailyTransfers();
            } catch (SQLException ex) {
                this.con.saveProp("mpis_last_error", String.valueOf(ex));
                Logger.getLogger(Statistics.class.getName()).log(Level.SEVERE, (String) null, ex);
            }
        }
    }//GEN-LAST:event_jRadioButton1ActionPerformed

    private void transDateRetActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_transDateRetActionPerformed
        try {
            updateDailyTransfers();
        } catch (SQLException ex) {
            this.con.saveProp("mpis_last_error", String.valueOf(ex));
            Logger.getLogger(Statistics.class.getName()).log(Level.SEVERE, (String) null, ex);
        }
    }//GEN-LAST:event_transDateRetActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JTable DailyPettyCashTable;
    private javax.swing.JComboBox<String> addlTypeDaily;
    private javax.swing.JComboBox<String> addlTypeMonthly;
    private javax.swing.JComboBox<String> branch;
    private javax.swing.JRadioButton catAddlDailyRadioBtn;
    private javax.swing.JRadioButton catAddlMonthlyRadioBtn;
    private javax.swing.JTable daiEmp;
    private javax.swing.JTable daiRes;
    private javax.swing.JTable dailyAdditionalTable;
    private javax.swing.JRadioButton dailyPettyCatRadioBtn;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel15;
    private javax.swing.JLabel jLabel16;
    private javax.swing.JLabel jLabel17;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel10;
    private javax.swing.JPanel jPanel11;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JPanel jPanel7;
    private javax.swing.JPanel jPanel8;
    private javax.swing.JPanel jPanel9;
    private javax.swing.JRadioButton jRadioButton1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane10;
    private javax.swing.JScrollPane jScrollPane11;
    private javax.swing.JScrollPane jScrollPane12;
    private javax.swing.JScrollPane jScrollPane13;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JScrollPane jScrollPane4;
    private javax.swing.JScrollPane jScrollPane5;
    private javax.swing.JScrollPane jScrollPane6;
    private javax.swing.JScrollPane jScrollPane7;
    private javax.swing.JScrollPane jScrollPane8;
    private javax.swing.JScrollPane jScrollPane9;
    private javax.swing.JTabbedPane jTabbedPane1;
    private javax.swing.JTable monEmp;
    private javax.swing.JTable monRes;
    private javax.swing.JComboBox<String> monthCombo;
    private javax.swing.JTable monthlyAdditionalTable;
    private javax.swing.JTable monthlyPettyCashTable;
    private javax.swing.JRadioButton monthlyPettyCatRadioBtn;
    private javax.swing.JComboBox<String> pettyCashType;
    private javax.swing.JComboBox<String> pettyCashType1;
    private javax.swing.JTextField totAI;
    private javax.swing.JTextField totAI1;
    private javax.swing.JTextField totAI2;
    private javax.swing.JTextField totAI4;
    private javax.swing.JTextField totCapital;
    private javax.swing.JTextField totCapital1;
    private javax.swing.JTextField totCapital2;
    private javax.swing.JTextField totCapital4;
    private javax.swing.JTextField totIns;
    private javax.swing.JTextField totIns1;
    private javax.swing.JTextField totLotes;
    private javax.swing.JTextField totLotes1;
    private javax.swing.JTextField totLotes2;
    private javax.swing.JTextField totLotes4;
    private javax.swing.JTextField totSC;
    private javax.swing.JTextField totSC1;
    private javax.swing.JTextField totSC2;
    private javax.swing.JTextField totSC4;
    private org.jdesktop.swingx.JXDatePicker transDateRet;
    private javax.swing.JTable transfersTable;
    private javax.swing.JTable transfersTable2;
    private javax.swing.JTable transfersTable3;
    private javax.swing.JTable transfersTable4;
    private javax.swing.JComboBox<String> yearCombo;
    // End of variables declaration//GEN-END:variables
}
