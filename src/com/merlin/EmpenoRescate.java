/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.merlin;

import com.lowagie.text.DocumentException;
import com.lowagie.text.pdf.PdfReader;
import com.lowagie.text.pdf.PdfStamper;
import java.awt.Color;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.event.ActionEvent;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.design.JRDesignQuery;
import net.sf.jasperreports.engine.design.JasperDesign;
import net.sf.jasperreports.engine.xml.JRXmlLoader;

import org.apache.pdfbox.io.MemoryUsageSetting;
import org.apache.pdfbox.multipdf.PDFMergerUtility;

/**
 *
 * @author Lucky
 */
public class EmpenoRescate extends javax.swing.JPanel {

    /**
     * @return the fquery
     */
    public String getFquery() {
        return fquery;
    }

    /**
     * @param fquery the fquery to set
     */
    public void setFquery(String fquery) {
        this.fquery = fquery;
    }
    
    

    /**
     * Creates new form Expired
     */
    public EmpenoRescate() {
        initComponents();
        this.saveFile.setVisible(false);
        this.date1.setDate(Calendar.getInstance().getTime());
        this.date2.setDate(Calendar.getInstance().getTime());
        this.dateCombo.setDate(Calendar.getInstance().getTime());
        this.dateCombo.setEnabled(true);
        this.date1.setEnabled(false);
        this.date2.setEnabled(false);
        this.monthCombo.setEnabled(false);
        this.yearComboBox.setEnabled(false);
        if (con.getProp("branch").equalsIgnoreCase("Tangos")) {
            con.saveProp("div", "50000");
        } else {
            con.saveProp("div", "10000");
        }
        updateEmpenoTable(" where date = '" + this.dateHelp.formatDate(this.dateCombo.getDate()) + "'");
    }
    
    Config con = new Config();
    private final String driver = "jdbc:mariadb://" + this.con.getProp("IP") + ":" + this.con.getProp("port") + "/merlininventorydatabase";
    private final String f_user = this.con.getProp("username");
    private final String f_pass = this.con.getProp("password");
    private String fquery = "";
    private DateHelper dateHelp = new DateHelper();
    private DecimalHelper decHelp = new DecimalHelper();
    private SimpleDateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd");

    private ReportPrinter pr = new ReportPrinter();

    public void updateYearCombo() throws SQLException{
        yearComboBox.removeAllItems();
        Connection connect = DriverManager.getConnection(this.driver, this.f_user, this.f_pass);
        Statement state = connect.createStatement();
        String query = "Select distinct year(transaction_date) from merlininventorydatabase.client_info order by transaction_date desc";

        ResultSet rset = state.executeQuery(query);
        this.yearComboBox.removeAllItems();
        while (rset.next()) {
            this.yearComboBox.addItem(rset.getString(1));
        }
        state.close();
        connect.close();
    }
    
    public void beginScreen() {
        updateEmpenoTable(" where date = '" + this.dateHelp.formatDate(this.dateCombo.getDate()) + "'");
    }
    
    public void updateEmpenoTable(String whereState) {
        try {
            Connection connect = DriverManager.getConnection(this.driver, this.f_user, this.f_pass);
            Statement state = connect.createStatement();
            String query = "SELECT case when principal > " + con.getProp("div") + " then 'big' else 'small' END AS amount_grp, a.branch, a.pap_num, client_name, client_address, principal, ai, sc, insurance, item_description, date, remarks, old_pap_num, new_pap_num from merlininventorydatabase.client_info a inner join merlininventorydatabase.empeno b on a.item_code_a = b.pap_num" + whereState + " order by amount_grp, date asc, a.pap_num asc";
            setFquery(query);
            ResultSet rset = state.executeQuery(query);
            DefaultTableModel defTabMod = (DefaultTableModel) this.empeno.getModel();
            while (defTabMod.getRowCount() > 0) {
                defTabMod.removeRow(0);
            }
            Object[] row = new Object[10];
            int count = 0;
            String prevd = "";
            while (rset.next()) {
                if (count == 0) {
                    prevd = rset.getString("date");
                    row[0] = rset.getString("date");
                } else if (rset.getString("date").equalsIgnoreCase(prevd)) {
                    row[0] = "";
} else {
             row[0] = rset.getString("date");
             prevd = rset.getString("date");
    }

           row[2] = rset.getString("client_name");
           row[6] = rset.getString("insurance");
           row[1] = rset.getString("pap_num");
           row[3] = this.decHelp.FormatNumber(rset.getDouble("principal"));
           row[4] = this.decHelp.FormatNumber(rset.getDouble("ai"));
           row[5] = this.decHelp.FormatNumber(rset.getDouble("sc"));
           row[7] = this.decHelp.FormatNumber(rset.getDouble("principal") - rset.getDouble("ai") - rset.getDouble("sc") - rset.getDouble("insurance"));
           row[8] = rset.getString("item_description");
           row[9] = rset.getString("client_address");
           defTabMod.addRow(row);
           count++;
            }
            state.close();
            connect.close();

            this.empeno.setModel(defTabMod);
            designTable();
        } catch (SQLException ex) {
            this.con.saveProp("mpis_last_error", String.valueOf(ex));
            Logger.getLogger(EmpenoRescate.class.getName()).log(Level.SEVERE, (String) null, ex);
        }
    }

    public void designTable() {
        TableCellRenderer renderer = new TableCellRenderer() {
            JLabel label = new JLabel();

            public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
                this.label.setOpaque(true);
                this.label.setText("" + value);
                this.label.setVerticalAlignment(JLabel.TOP);
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
                if (column > 2 && column < 8) {
                    this.label.setHorizontalAlignment(4);
                } else if (column == 0 || column == 1) {
                    this.label.setHorizontalAlignment(0);
                } else {
                    this.label.setHorizontalAlignment(2);
                }
                return this.label;
            }
        };
        empeno.getColumnModel().getColumn(2).setCellRenderer(new TextAreaCellRenderer());
        empeno.getColumnModel().getColumn(8).setCellRenderer(new TextAreaCellRenderer());
        empeno.getColumnModel().getColumn(9).setCellRenderer(new TextAreaCellRenderer());
        this.empeno.setDefaultRenderer(Object.class, renderer);
        ((DefaultTableCellRenderer) this.empeno.getTableHeader().getDefaultRenderer()).setHorizontalAlignment(0);
    }

    public void designResTable() {
        TableCellRenderer renderer = new TableCellRenderer() {
            JLabel label = new JLabel();

            public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
                this.label.setOpaque(true);
                this.label.setText("" + value);
                Color alternate = new Color(239,246,250);
//                Color alternate = UIManager.getColor("Table.alternateRowColor");
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
                if (column > 2) {
                    this.label.setHorizontalAlignment(4);
                } else if (column == 0 || column == 1) {
                    this.label.setHorizontalAlignment(0);
                } else {
                    this.label.setHorizontalAlignment(2);
                }
                return this.label;
            }
        };
        this.rescate.setDefaultRenderer(Object.class, renderer);
        ((DefaultTableCellRenderer) this.rescate.getTableHeader().getDefaultRenderer()).setHorizontalAlignment(0);
    }

    public void updateRescateTable(String whereState) {
        try {
            Connection connect = DriverManager.getConnection(this.driver, this.f_user, this.f_pass);
            Statement state = connect.createStatement();
            String query = "SELECT case when principal > " + con.getProp("div") + " then 'big' else 'small' END AS amount_grp, "
                    + "a.pap_num, "
                    + "a.client_name, "
                    + "b.principal, "
                    + "b.interest, "
                    + "b.date "
                    + "from merlininventorydatabase.client_info a "
                    + "inner join merlininventorydatabase.rescate b "
                    + "on a.item_code_a = b.pap_num"
                    + whereState
                    + " order by amount_grp, date asc, b.pap_num asc";


            setFquery(query);
            ResultSet rset = state.executeQuery(query);
            DefaultTableModel defTabMod = (DefaultTableModel) this.rescate.getModel();
            while (defTabMod.getRowCount() > 0) {
                defTabMod.removeRow(0);
         }
            Object[] row = new Object[6];
            int count = 0;
            String prevd = "";
            while (rset.next()) {
                if (count == 0) {
                    prevd = rset.getString("b.date");
                    row[0] = rset.getString("b.date");
                } else if (rset.getString("b.date").equalsIgnoreCase(prevd)) {
                    row[0] = "";
                } else {
                    row[0] = rset.getString("b.date");
                    prevd = rset.getString("b.date");
                }

                row[1] = rset.getString("a.pap_num");
                row[2] = rset.getString("a.client_name");
                row[3] = this.decHelp.FormatNumber(rset.getDouble("b.principal"));
           row[4] = this.decHelp.FormatNumber(rset.getDouble("interest"));
           row[5] = this.decHelp.FormatNumber(rset.getDouble("principal") + rset.getDouble("interest"));
           defTabMod.addRow(row);
           count++;
         }
            state.close();
            connect.close();
            designResTable();
        } catch (SQLException ex) {
            this.con.saveProp("mpis_last_error", String.valueOf(ex));
            Logger.getLogger(EmpenoRescate.class.getName()).log(Level.SEVERE, (String) null, ex);
        }
    }

    private void saveReport(String filePath, String query, String destination, String report) {
        OutputStream output = null;
        try {
            Connection conn = DriverManager.getConnection(this.driver, this.f_user, this.f_pass);
            JasperDesign jasperDesign = JRXmlLoader.load(getClass().getResourceAsStream(filePath));
            JRDesignQuery newQuery = new JRDesignQuery();

            newQuery.setText(query);
            jasperDesign.setQuery(newQuery);

            Map<String, Object> parameters = new HashMap<>();
            Map<String, Object> parameters2 = new HashMap<>();
            
            if (byDayRadio.isSelected()) {
                parameters.put("EMPENO_DATE", this.dateHelp.formatDate(this.dateCombo.getDate()));
                parameters.put("EMPENO_DATE2", this.dateHelp.formatDate(this.dateCombo.getDate()));
                parameters2.put("EMPENO_DATE", this.dateHelp.formatDate(this.dateCombo.getDate()));
                parameters2.put("EMPENO_DATE2", this.dateHelp.formatDate(this.dateCombo.getDate()));
            } else if (byMonthRadio.isSelected()) {
                parameters.put("EMPENO_DATE", yearComboBox.getSelectedItem().toString() + "-" + Integer.toString(monthCombo.getSelectedIndex() + 1) + "-01");
                parameters2.put("EMPENO_DATE", yearComboBox.getSelectedItem().toString() + "-" + Integer.toString(monthCombo.getSelectedIndex() + 1) + "-01");
                try {
                    parameters.put("EMPENO_DATE2", this.dateHelp.getLastDateOfMonth(new DecimalFormat("00").format(monthCombo.getSelectedIndex() + 1), yearComboBox.getSelectedItem().toString()));
                    parameters2.put("EMPENO_DATE2", this.dateHelp.getLastDateOfMonth(new DecimalFormat("00").format(monthCombo.getSelectedIndex() + 1), yearComboBox.getSelectedItem().toString()));
                } catch (ParseException ex) {
                    Logger.getLogger(EmpenoRescate.class.getName()).log(Level.SEVERE, null, ex);
                }
            } else {
                parameters.put("EMPENO_DATE", this.dateHelp.formatDate(this.date1.getDate()));
                parameters.put("EMPENO_DATE2", this.dateHelp.formatDate(this.date2.getDate()));
                parameters2.put("EMPENO_DATE", this.dateHelp.formatDate(this.date1.getDate()));
                parameters2.put("EMPENO_DATE2", this.dateHelp.formatDate(this.date2.getDate()));
            }

            if (con.getProp("branch").equalsIgnoreCase("Tangos")) {
                parameters.put("AMOUNT_DIVISION", 50000.00);
                parameters2.put("AMOUNT_DIVISION", 50000.00);
                parameters.put("BRACKET1", 100.00);
                parameters.put("BRACKET2", 5000.00);
                parameters.put("BRACKET3", 10000.00);
            } else {
                parameters.put("AMOUNT_DIVISION", 10000.00);
                parameters2.put("AMOUNT_DIVISION", 10000.00);
                parameters.put("BRACKET1", 100.00);
                parameters.put("BRACKET2", 1000.00);
                parameters.put("BRACKET3", 5000.00);
            }

            JasperReport jasperReport = JasperCompileManager.compileReport(jasperDesign);
            JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parameters, conn);

               output = new FileOutputStream(new File(destination));
            JasperExportManager.exportReportToPdfStream(jasperPrint, output);

            InputStream is = new FileInputStream(new File(destination));
            try {
                is.close();
                File fi = new File(destination);
                fi.deleteOnExit();

                } catch (IOException ex) {
                    this.con.saveProp("mpis_last_error", String.valueOf(ex));
                    Logger.getLogger(EmpenoRescate.class.getName()).log(Level.SEVERE, (String) null, ex);
                }
                conn.close();
            } catch (FileNotFoundException | net.sf.jasperreports.engine.JRException | SQLException ex) {
                this.con.saveProp("mpis_last_error", String.valueOf(ex));
                JOptionPane.showMessageDialog(null, "An error occured while saving your " + report + "\n" + ex, report, 0);
                Logger.getLogger(org.jfree.data.statistics.Statistics.class.getName()).log(Level.SEVERE, (String) null, ex);
            } finally {
               try {
                   output.close();
   //                JOptionPane.showMessageDialog(null, report + " successfully Saved!", report, 1);
               } catch (IOException ex) {
                   this.con.saveProp("mpis_last_error", String.valueOf(ex));
                   JOptionPane.showMessageDialog(null, "An error occured while saving your " + report, report, 0);
                   Logger.getLogger(org.jfree.data.statistics.Statistics.class.getName()).log(Level.SEVERE, (String) null, ex);
               }
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
        jPanel7 = new javax.swing.JPanel();
        jTabbedPane1 = new javax.swing.JTabbedPane();
        jPanel3 = new javax.swing.JPanel();
        jScrollPane2 = new javax.swing.JScrollPane();
        empeno = new javax.swing.JTable();
        jPanel1 = new javax.swing.JPanel();
        jScrollPane3 = new javax.swing.JScrollPane();
        rescate = new javax.swing.JTable();
        jPanel2 = new javax.swing.JPanel();
        jPanel4 = new javax.swing.JPanel();
        dateCombo = new org.jdesktop.swingx.JXDatePicker();
        byDayRadio = new javax.swing.JRadioButton();
        jPanel5 = new javax.swing.JPanel();
        yearComboBox = new javax.swing.JComboBox<>();
        monthCombo = new javax.swing.JComboBox<>();
        byMonthRadio = new javax.swing.JRadioButton();
        jPanel6 = new javax.swing.JPanel();
        date1 = new org.jdesktop.swingx.JXDatePicker();
        byIntervalRadio = new javax.swing.JRadioButton();
        jLabel3 = new javax.swing.JLabel();
        date2 = new org.jdesktop.swingx.JXDatePicker();
        saveBtn = new javax.swing.JButton();
        printBtn = new javax.swing.JButton();
        saveFile = new javax.swing.JFileChooser();

        jTabbedPane1.addHierarchyListener(new java.awt.event.HierarchyListener() {
            public void hierarchyChanged(java.awt.event.HierarchyEvent evt) {
                jTabbedPane1HierarchyChanged(evt);
            }
        });
        jTabbedPane1.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jTabbedPane1MouseClicked(evt);
            }
        });

        jPanel3.addComponentListener(new java.awt.event.ComponentAdapter() {
            public void componentShown(java.awt.event.ComponentEvent evt) {
                jPanel3ComponentShown(evt);
            }
        });

        empeno.setBackground(new java.awt.Color(240, 240, 240));
        empeno.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Date", "Pap No.", "Client Name", "Principal", "AI", "SC", "INS", "Net", "Item Description", "Address"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false, false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        empeno.setFillsViewportHeight(true);
        empeno.setGridColor(new java.awt.Color(240, 240, 240));
        empeno.setRowHeight(30);
        jScrollPane2.setViewportView(empeno);
        if (empeno.getColumnModel().getColumnCount() > 0) {
            empeno.getColumnModel().getColumn(0).setResizable(false);
            empeno.getColumnModel().getColumn(0).setPreferredWidth(15);
            empeno.getColumnModel().getColumn(1).setResizable(false);
            empeno.getColumnModel().getColumn(1).setPreferredWidth(15);
            empeno.getColumnModel().getColumn(2).setResizable(false);
            empeno.getColumnModel().getColumn(2).setPreferredWidth(45);
            empeno.getColumnModel().getColumn(3).setResizable(false);
            empeno.getColumnModel().getColumn(3).setPreferredWidth(25);
            empeno.getColumnModel().getColumn(4).setResizable(false);
            empeno.getColumnModel().getColumn(4).setPreferredWidth(20);
            empeno.getColumnModel().getColumn(5).setResizable(false);
            empeno.getColumnModel().getColumn(5).setPreferredWidth(15);
            empeno.getColumnModel().getColumn(6).setResizable(false);
            empeno.getColumnModel().getColumn(6).setPreferredWidth(20);
            empeno.getColumnModel().getColumn(7).setResizable(false);
            empeno.getColumnModel().getColumn(7).setPreferredWidth(30);
            empeno.getColumnModel().getColumn(8).setResizable(false);
            empeno.getColumnModel().getColumn(8).setPreferredWidth(120);
            empeno.getColumnModel().getColumn(9).setPreferredWidth(100);
        }

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 837, Short.MAX_VALUE)
                .addContainerGap())
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane2, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 617, Short.MAX_VALUE)
        );

        jTabbedPane1.addTab("EMPENO", jPanel3);

        rescate.setBackground(new java.awt.Color(240, 240, 240));
        rescate.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Date", "Loan No.", "Name of Pawner", "Principal", "Interest", "Total"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        rescate.setAutoResizeMode(javax.swing.JTable.AUTO_RESIZE_OFF);
        rescate.setFillsViewportHeight(true);
        rescate.setGridColor(new java.awt.Color(240, 240, 240));
        rescate.setRowHeight(30);
        jScrollPane3.setViewportView(rescate);
        if (rescate.getColumnModel().getColumnCount() > 0) {
            rescate.getColumnModel().getColumn(0).setPreferredWidth(80);
            rescate.getColumnModel().getColumn(1).setPreferredWidth(80);
            rescate.getColumnModel().getColumn(2).setPreferredWidth(250);
            rescate.getColumnModel().getColumn(3).setPreferredWidth(120);
            rescate.getColumnModel().getColumn(4).setPreferredWidth(100);
            rescate.getColumnModel().getColumn(5).setPreferredWidth(120);
        }

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane3, javax.swing.GroupLayout.DEFAULT_SIZE, 837, Short.MAX_VALUE)
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 573, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jTabbedPane1.addTab("RESCATE", jPanel1);

        jPanel2.setBackground(new java.awt.Color(255, 255, 255));

        jPanel4.setBackground(new java.awt.Color(227, 240, 251));

        dateCombo.setToolTipText("YYYY-MM-DD");
        dateCombo.setFont(new java.awt.Font("Segoe UI", 0, 13)); // NOI18N
        dateCombo.setFormats(dateFormatter);
        dateCombo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                dateComboActionPerformed(evt);
            }
        });

        byDayRadio.setFont(new java.awt.Font("Segoe UI Semibold", 0, 13)); // NOI18N
        byDayRadio.setForeground(new java.awt.Color(51, 51, 51));
        byDayRadio.setSelected(true);
        byDayRadio.setText("by Day:");
        byDayRadio.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                byDayRadioActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(byDayRadio)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(dateCombo, javax.swing.GroupLayout.DEFAULT_SIZE, 173, Short.MAX_VALUE)
                .addContainerGap())
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(byDayRadio, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(dateCombo, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
        );

        jPanel5.setBackground(new java.awt.Color(227, 240, 251));

        yearComboBox.setFont(new java.awt.Font("Segoe UI Semibold", 0, 13)); // NOI18N
        yearComboBox.setForeground(new java.awt.Color(51, 51, 51));
        yearComboBox.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        yearComboBox.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                yearComboBoxActionPerformed(evt);
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

        byMonthRadio.setFont(new java.awt.Font("Segoe UI Semibold", 0, 13)); // NOI18N
        byMonthRadio.setForeground(new java.awt.Color(51, 51, 51));
        byMonthRadio.setText("by Month:");
        byMonthRadio.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                byMonthRadioActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel5Layout = new javax.swing.GroupLayout(jPanel5);
        jPanel5.setLayout(jPanel5Layout);
        jPanel5Layout.setHorizontalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(byMonthRadio)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(monthCombo, javax.swing.GroupLayout.PREFERRED_SIZE, 98, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(yearComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, 85, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel5Layout.setVerticalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(monthCombo, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(yearComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(byMonthRadio, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
        );

        jPanel6.setBackground(new java.awt.Color(227, 240, 251));

        date1.setToolTipText("YYYY-MM-DD");
        date1.setFont(new java.awt.Font("Segoe UI", 0, 13)); // NOI18N
        date1.setFormats(dateFormatter);
        date1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                date1ActionPerformed(evt);
            }
        });

        byIntervalRadio.setFont(new java.awt.Font("Segoe UI Semibold", 0, 13)); // NOI18N
        byIntervalRadio.setForeground(new java.awt.Color(51, 51, 51));
        byIntervalRadio.setText("by Date Interval:");
        byIntervalRadio.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                byIntervalRadioActionPerformed(evt);
            }
        });

        jLabel3.setText("to");

        date2.setToolTipText("YYYY-MM-DD");
        date2.setFont(new java.awt.Font("Segoe UI", 0, 13)); // NOI18N
        date2.setFormats(dateFormatter);
        date2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                date2ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel6Layout = new javax.swing.GroupLayout(jPanel6);
        jPanel6.setLayout(jPanel6Layout);
        jPanel6Layout.setHorizontalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addGap(18, 18, 18)
                .addComponent(byIntervalRadio)
                .addGap(18, 18, 18)
                .addComponent(date1, javax.swing.GroupLayout.PREFERRED_SIZE, 164, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jLabel3)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(date2, javax.swing.GroupLayout.PREFERRED_SIZE, 167, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jPanel6Layout.linkSize(javax.swing.SwingConstants.HORIZONTAL, new java.awt.Component[] {date1, date2});

        jPanel6Layout.setVerticalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(byIntervalRadio, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(date1, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(date2, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
        );

        saveBtn.setBackground(new java.awt.Color(79, 119, 141));
        saveBtn.setFont(new java.awt.Font("Segoe UI Semibold", 0, 13)); // NOI18N
        saveBtn.setForeground(new java.awt.Color(255, 255, 255));
        saveBtn.setText("Save");
        saveBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                saveBtnActionPerformed(evt);
            }
        });

        printBtn.setBackground(new java.awt.Color(79, 119, 141));
        printBtn.setFont(new java.awt.Font("Segoe UI Semibold", 0, 13)); // NOI18N
        printBtn.setForeground(new java.awt.Color(255, 255, 255));
        printBtn.setText("Print");
        printBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                printBtnActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(55, 55, 55)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addComponent(jPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jPanel5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jPanel6, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(18, 18, 18)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(printBtn, javax.swing.GroupLayout.DEFAULT_SIZE, 139, Short.MAX_VALUE)
                    .addComponent(saveBtn, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap(76, Short.MAX_VALUE))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.CENTER)
                    .addComponent(jPanel5, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(saveBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.CENTER)
                    .addComponent(printBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jPanel6, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );

        javax.swing.GroupLayout jPanel7Layout = new javax.swing.GroupLayout(jPanel7);
        jPanel7.setLayout(jPanel7Layout);
        jPanel7Layout.setHorizontalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jTabbedPane1)
            .addGroup(jPanel7Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
            .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jPanel7Layout.createSequentialGroup()
                    .addGap(0, 174, Short.MAX_VALUE)
                    .addComponent(saveFile, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGap(0, 174, Short.MAX_VALUE)))
        );
        jPanel7Layout.setVerticalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel7Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jTabbedPane1))
            .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jPanel7Layout.createSequentialGroup()
                    .addGap(0, 219, Short.MAX_VALUE)
                    .addComponent(saveFile, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGap(0, 219, Short.MAX_VALUE)))
        );

        jScrollPane1.setViewportView(jPanel7);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 828, Short.MAX_VALUE)
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 762, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
    }// </editor-fold>//GEN-END:initComponents

    private void byIntervalRadioActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_byIntervalRadioActionPerformed
        if (this.byIntervalRadio.isEnabled()) {
//       this.printBtn.setEnabled(false);
            this.date1.setEnabled(true);
            this.date2.setEnabled(true);
            this.byMonthRadio.setSelected(false);
            this.monthCombo.setEnabled(false);
            this.yearComboBox.setEnabled(false);
            this.byDayRadio.setSelected(false);
            this.dateCombo.setEnabled(false);
     } 
        if (this.jTabbedPane1.getSelectedIndex() == 0) {
            updateEmpenoTable(" where date between '" + this.dateHelp.formatDate(this.date1.getDate()) + "' and '" + this.dateHelp.formatDate(this.date2.getDate()) + "'");
        } else if (this.jTabbedPane1.getSelectedIndex() == 1) {
            updateRescateTable(" where date between '" + this.dateHelp.formatDate(this.date1.getDate()) + "' and '" + this.dateHelp.formatDate(this.date2.getDate()) + "' and subasta = 0");
        }
    }//GEN-LAST:event_byIntervalRadioActionPerformed

    private void byMonthRadioActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_byMonthRadioActionPerformed
        if (this.byMonthRadio.isEnabled()) {
//            this.printBtn.setEnabled(false);
            this.monthCombo.setEnabled(true);
            this.yearComboBox.setEnabled(true);
            this.byIntervalRadio.setSelected(false);
            this.date1.setEnabled(false);
            this.date2.setEnabled(false);
            this.byDayRadio.setSelected(false);
            this.dateCombo.setEnabled(false);
     } 
        if (this.jTabbedPane1.getSelectedIndex() == 0) {
            updateEmpenoTable(" where month(date) = " + (this.monthCombo.getSelectedIndex() + 1) + " and year(date) = " + this.yearComboBox.getSelectedItem());
        } else if (this.jTabbedPane1.getSelectedIndex() == 1) {
            updateRescateTable(" where month(date) = " + (this.monthCombo.getSelectedIndex() + 1) + " and year(date) = " + this.yearComboBox.getSelectedItem() + " and subasta = 0");
        }
    }//GEN-LAST:event_byMonthRadioActionPerformed

    private void dateComboActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_dateComboActionPerformed
        if (this.jTabbedPane1.getSelectedIndex() == 0) {
            updateEmpenoTable(" where date = '" + this.dateHelp.formatDate(this.dateCombo.getDate()) + "'");
        } else if (this.jTabbedPane1.getSelectedIndex() == 1) {
            updateRescateTable(" where date = '" + this.dateHelp.formatDate(this.dateCombo.getDate()) + "'" + "  and subasta = 0");
        }
    }//GEN-LAST:event_dateComboActionPerformed

    private void byDayRadioActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_byDayRadioActionPerformed
        if (this.byDayRadio.isEnabled()) {
            this.dateCombo.setEnabled(true);
            this.byIntervalRadio.setSelected(false);
            this.date1.setEnabled(false);
            this.date2.setEnabled(false);
            this.byMonthRadio.setSelected(false);
            this.monthCombo.setEnabled(false);
            this.yearComboBox.setEnabled(false);
     } 
        if (this.jTabbedPane1.getSelectedIndex() == 0) {
            updateEmpenoTable(" where date = '" + this.dateHelp.formatDate(this.dateCombo.getDate()) + "'");
        } else if (this.jTabbedPane1.getSelectedIndex() == 1) {
            updateRescateTable(" where date = '" + this.dateHelp.formatDate(this.dateCombo.getDate()) + "'" + "  and subasta = 0");
        }
    }//GEN-LAST:event_byDayRadioActionPerformed

    private void monthComboActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_monthComboActionPerformed
        if (this.jTabbedPane1.getSelectedIndex() == 0) {
            updateEmpenoTable(" where month(date) = " + (this.monthCombo.getSelectedIndex() + 1) + " and year(date) = " + this.yearComboBox.getSelectedItem());
        } else if (this.jTabbedPane1.getSelectedIndex() == 1) {
            updateRescateTable(" where month(date) = " + (this.monthCombo.getSelectedIndex() + 1) + " and year(date) = " + this.yearComboBox.getSelectedItem() + "  and subasta = 0");
     } 
    }//GEN-LAST:event_monthComboActionPerformed

    private void date1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_date1ActionPerformed
        if (this.jTabbedPane1.getSelectedIndex() == 0) {
            updateEmpenoTable(" where date between '" + this.dateHelp.formatDate(this.date1.getDate()) + "' and '" + this.dateHelp.formatDate(this.date2.getDate()) + "'");
        } else if (this.jTabbedPane1.getSelectedIndex() == 1) {
            updateRescateTable(" where date between '" + this.dateHelp.formatDate(this.date1.getDate()) + "' and '" + this.dateHelp.formatDate(this.date2.getDate()) + "'" + "  and subasta = 0");
        } 
    }//GEN-LAST:event_date1ActionPerformed

    private void date2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_date2ActionPerformed
        if (this.jTabbedPane1.getSelectedIndex() == 0) {
            updateEmpenoTable(" where date between '" + this.dateHelp.formatDate(this.date1.getDate()) + "' and '" + this.dateHelp.formatDate(this.date2.getDate()) + "'");
        } else if (this.jTabbedPane1.getSelectedIndex() == 1) {
            updateRescateTable(" where date between '" + this.dateHelp.formatDate(this.date1.getDate()) + "' and '" + this.dateHelp.formatDate(this.date2.getDate()) + "'" + "  and subasta = 0");
        } 
    }//GEN-LAST:event_date2ActionPerformed

    private void saveBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_saveBtnActionPerformed
        this.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
        this.saveFile.setVisible(true);
        String fileName = "";
        int showSaveDialog = this.saveFile.showSaveDialog((Component) null);
        if (showSaveDialog == 0) {
            if (this.saveFile.getSelectedFile().toString().substring(this.saveFile.getSelectedFile().toString().length() - 4).equals(".pdf")) {
                fileName = this.saveFile.getSelectedFile().getAbsolutePath();
            } else {
                fileName = this.saveFile.getSelectedFile().getAbsolutePath().concat(".pdf");
            }
            this.saveFile.setSelectedFile(new File(fileName));
            if (this.saveFile.getSelectedFile().exists()) {
                int replaceFile = JOptionPane.showConfirmDialog(null, "Item Already Exists. Do you want to replace this file?", "Replace File", 0);
                if (replaceFile == 1) {
                    saveBtnActionPerformed((ActionEvent) null);
                    return;
                }
            }
            // SAVE EMPENO
            if (jTabbedPane1.getSelectedIndex() == 0) {
                String filePath = "/Reports/empenoreport.jrxml";
                String filePath2 = "/Reports/empenoreport2.jrxml";
                saveReport(filePath, getFquery(), con.getProp("temp_folder").concat("empreport1.pdf"), "Empeno Report1");
                saveReport(filePath2, getFquery(), con.getProp("temp_folder").concat("empreport2.pdf"), "Empeno Report2");

                PDFMergerUtility pdm = new PDFMergerUtility();
                pdm.setDestinationFileName(con.getProp("temp_folder").concat("empenocombined.pdf"));
                try {

                    //MERGE 2 PDF TEMPORARY FILES
                    pdm.addSource(new File(con.getProp("temp_folder").concat("empreport1.pdf")));
                    pdm.addSource(new File(con.getProp("temp_folder").concat("empreport2.pdf")));
                    pdm.mergeDocuments(MemoryUsageSetting.setupMainMemoryOnly());

                    // REARRANGED PDF TO MAKE BOOK SPREAD
                    PdfReader reader = new PdfReader(con.getProp("temp_folder").concat("empenocombined.pdf"));
                    int half = reader.getNumberOfPages() / 2;
                    String listed = "";
                    for (int i = 0; i < half; i++) {
                        listed = listed + Integer.toString(i + 1) + ", " + Integer.toString(half + i + 1);
                        if (i != half) {
                            listed = listed + ", ";
                        }
                    }

                    reader.selectPages(listed);
                    PdfStamper stamper = new PdfStamper(reader, new FileOutputStream(fileName));
                    stamper.close();
                    JOptionPane.showMessageDialog(null, "Empeno Report successfully saved!", "Save Empeno", JOptionPane.INFORMATION_MESSAGE);
                } catch (FileNotFoundException ex) {
                    JOptionPane.showMessageDialog(null, "File not found error!" + ex, "Save Empeno", JOptionPane.ERROR_MESSAGE);
                    //                Logger.getLogger(EmpenoRescate.class.getName()).log(Level.SEVERE, null, ex);
                } catch (IOException ex) {
                    JOptionPane.showMessageDialog(null, "Could not merge PDF files!" + ex, "Save Empeno", JOptionPane.ERROR_MESSAGE);
                    //                Logger.getLogger(EmpenoRescate.class.getName()).log(Level.SEVERE, null, ex);
                } catch (DocumentException ex) {
                    JOptionPane.showMessageDialog(null, "Could not rearrange pages in PDF file!" + ex, "Save Empeno", JOptionPane.ERROR_MESSAGE);
                    //                Logger.getLogger(EmpenoRescate.class.getName()).log(Level.SEVERE, null, ex);
                }
            } else {   //SAVE RESCATE
                String filePath = "/Reports/rescate.jrxml";
                saveReport(filePath, getFquery(), fileName, "Rescate Report");
                Map<Object, Object> parameters = new HashMap<>();
                parameters.put("DATE", this.dateHelp.formatDate(this.dateCombo.getDate()));
                if ((new Config()).getProp("branch").equalsIgnoreCase("Tangos")) {
                    parameters.put("AMOUNT_DIVISION", 50000.00);
                    parameters.put("BRACKET1", 100);
                    parameters.put("BRACKET2", 5000);
                    parameters.put("BRACKET3", 10000);
                } else {
                    parameters.put("AMOUNT_DIVISION", 10000.00);
                    parameters.put("BRACKET1", 100);
                    parameters.put("BRACKET2", 1000);
                    parameters.put("BRACKET3", 5000);
                }
                JOptionPane.showMessageDialog(null, "Rescate Report successfully saved!", "Save Rescate", JOptionPane.INFORMATION_MESSAGE);
            }
        }
        this.setCursor(Cursor.getDefaultCursor());
        this.saveFile.setVisible(false);
    }//GEN-LAST:event_saveBtnActionPerformed

    private void jPanel3ComponentShown(java.awt.event.ComponentEvent evt) {//GEN-FIRST:event_jPanel3ComponentShown
        if (this.byMonthRadio.isSelected()) {
            updateEmpenoTable(" where month(date) = " + (this.monthCombo.getSelectedIndex() + 1) + " and year(date) = " + this.yearComboBox.getSelectedItem());
        } else if (this.byIntervalRadio.isSelected()) {
            updateEmpenoTable(" where date between '" + this.dateHelp.formatDate(this.date1.getDate()) + "' and '" + this.dateHelp.formatDate(this.date2.getDate()) + "'");
        } else if (this.byDayRadio.isSelected()) {
            updateEmpenoTable(" where date = '" + this.dateHelp.formatDate(this.dateCombo.getDate()) + "'");
        }
    }//GEN-LAST:event_jPanel3ComponentShown

    private void printBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_printBtnActionPerformed
        this.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
        File tempFolder = new File("C:\\MPIS\\tempRep");
        if (!tempFolder.exists())
            tempFolder.mkdir(); 
        if (this.jTabbedPane1.getSelectedIndex() == 0) {
            Map<Object, Object> parameters = new HashMap<>();
            Map<Object, Object> parameters2 = new HashMap<>();
            if (byDayRadio.isSelected()) {
                parameters.put("EMPENO_DATE", this.dateHelp.formatDate(this.dateCombo.getDate()));
                parameters.put("EMPENO_DATE2", this.dateHelp.formatDate(this.dateCombo.getDate()));
                parameters2.put("EMPENO_DATE", this.dateHelp.formatDate(this.dateCombo.getDate()));
                parameters2.put("EMPENO_DATE2", this.dateHelp.formatDate(this.dateCombo.getDate()));
            } else if (byMonthRadio.isSelected()) {
                parameters.put("EMPENO_DATE", yearComboBox.getSelectedItem().toString() + "-" + Integer.toString(monthCombo.getSelectedIndex() + 1) + "-01");
                parameters2.put("EMPENO_DATE", yearComboBox.getSelectedItem().toString() + "-" + Integer.toString(monthCombo.getSelectedIndex() + 1) + "-01");
                try {
                    parameters.put("EMPENO_DATE2", this.dateHelp.getLastDateOfMonth(new DecimalFormat("00").format(monthCombo.getSelectedIndex() + 1), yearComboBox.getSelectedItem().toString()));
                    parameters2.put("EMPENO_DATE2", this.dateHelp.getLastDateOfMonth(new DecimalFormat("00").format(monthCombo.getSelectedIndex() + 1), yearComboBox.getSelectedItem().toString()));
                } catch (ParseException ex) {
                    Logger.getLogger(EmpenoRescate.class.getName()).log(Level.SEVERE, null, ex);
                }
            } else {
                parameters.put("EMPENO_DATE", this.dateHelp.formatDate(this.date1.getDate()));
                parameters.put("EMPENO_DATE2", this.dateHelp.formatDate(this.date2.getDate()));
                parameters2.put("EMPENO_DATE", this.dateHelp.formatDate(this.date1.getDate()));
                parameters2.put("EMPENO_DATE2", this.dateHelp.formatDate(this.date2.getDate()));
            }
            /**
             *  THE FOLLOWING PARAMETERS ARE DISABLED DUE TO NEW IMPLEMENTATION WHEREIN TOTAL PRINCIPAL, AI, SC, INS PER DAY
             *  CAN JUST BE VIEWED USING EMPENO RESCATE AND USERS ARE ASKED TO WRITE FOR MANUAL COMPUTATIONS FOR THE
             *  DAILY REPORTS

            parameters.put("BRACKET1", Double.valueOf(getBracket1()));
            parameters.put("BRACKET2", Double.valueOf(getBracket2()));
            parameters.put("BRACKET3", Double.valueOf(getBracket3()));
            parameters.put("BRACKETM1", Double.valueOf(getBracketm1()));
            parameters.put("BRACKETM2", Double.valueOf(getBracketm2()));
            parameters.put("BRACKETM3", Double.valueOf(getBracketm3()));
            parameters.put("TOTALM", Double.valueOf(getTotalm()));
            parameters.put("COUNTM", Integer.valueOf(getMcount()));
            parameters.put("AIM", Double.valueOf(getAim()));
            
             * parameters2.put("SCM", Double.valueOf(getScm()));
             * parameters2.put("INSM", Double.valueOf(getInsm()));
             * parameters2.put("NETM", Double.valueOf(getNetm()));
             *
             *
             */

            if ((new Config()).getProp("branch").equalsIgnoreCase("Tangos")) {
                parameters.put("AMOUNT_DIVISION", 50000.00);
                parameters2.put("AMOUNT_DIVISION", 50000.00);
                parameters.put("BRACKET1", 100.00);
                parameters.put("BRACKET2", 5000.00);
                parameters.put("BRACKET3", 10000.00);
            } else {
                parameters.put("AMOUNT_DIVISION", 10000.00);
                parameters2.put("AMOUNT_DIVISION", 10000.00);
                parameters.put("BRACKET1", 100.00);
                parameters.put("BRACKET2", 1000.00);
                parameters.put("BRACKET3", 5000.00);
            }

            this.pr.setReport_name("Empeno Report");
            this.pr.setDestination(this.con.getProp("temp_folder").concat("tempEmp.pdf"));
            this.pr.printReport("/Reports/empenoreport.jrxml", parameters, getFquery());
            this.pr.setDestination(this.con.getProp("temp_folder").concat("tempEmp2.pdf"));
            this.pr.printReport("/Reports/empenoreport2.jrxml", parameters2, getFquery());
            
        } else {
            Map<Object, Object> parameters = new HashMap<>();
            parameters.put("DATE", this.dateHelp.formatDate(this.dateCombo.getDate()));

            /**
             *  THE FOLLOWING PARAMETERS ARE DISABLED DUE TO NEW IMPLEMENTATION WHEREIN TOTAL PRINCIPAL, AI, SC, INS PER DAY
             *  CAN JUST BE VIEWED USING EMPENO RESCATE AND USERS ARE ASKED TO WRITE FOR MANUAL COMPUTATIONS FOR THE
             *  DAILY REPORTS

            parameters.put("BRACKET1", Double.valueOf(getBracket1r()));
            parameters.put("BRACKET2", Double.valueOf(getBracket2r()));
            parameters.put("BRACKET3", Double.valueOf(getBracket3r()));
            parameters.put("BRACKETM1", Double.valueOf(getBracketm1r()));
            parameters.put("BRACKETM2", Double.valueOf(getBracketm2r()));
            parameters.put("BRACKETM3", Double.valueOf(getBracketm3r()));
            parameters.put("TOTALM", Double.valueOf(getTotalmr()));
            parameters.put("COUNTM", Integer.valueOf(getMcountr()));

             *
               Map<Object, Object> parameters2 = new HashMap<>();
            parameters2.put("DATE", this.dateHelp.formatDate(this.dateCombo.getDate()));
            parameters2.put("INTERESTM", Double.valueOf(getIntM()));
            parameters2.put("TOTALRES", Double.valueOf(getTotResM()));
            */

            if ((new Config()).getProp("branch").equalsIgnoreCase("Tangos")) {
                parameters.put("AMOUNT_DIVISION", 50000.00);
                parameters.put("BRACKET1", 100);
                parameters.put("BRACKET2", 5000);
                parameters.put("BRACKET3", 10000);
            } else {
                parameters.put("AMOUNT_DIVISION", 10000.00);
                parameters.put("BRACKET1", 100);
                parameters.put("BRACKET2", 1000);
                parameters.put("BRACKET3", 5000);
            }
            
            this.pr.setReport_name("Rescate Report");
            this.pr.setDestination(this.con.getProp("temp_folder").concat("tempRes.pdf"));
            this.pr.printReport("/Reports/rescate.jrxml", parameters);
//            this.pr.setDestination(this.con.getProp("temp_folder").concat("tempRes2.pdf"));
//            this.pr.printReport("/Reports/rescate2.jrxml", parameters2);
            this.setCursor(Cursor.getDefaultCursor());
        } 
    }//GEN-LAST:event_printBtnActionPerformed

    private void yearComboBoxActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_yearComboBoxActionPerformed
        if (this.jTabbedPane1.getSelectedIndex() == 0) {
            updateEmpenoTable(" where month(date) = " + (this.monthCombo.getSelectedIndex() + 1) + " and year(date) = " + this.yearComboBox.getSelectedItem() );
        } else if (this.jTabbedPane1.getSelectedIndex() == 1) {
            updateRescateTable(" where month(date) = " + (this.monthCombo.getSelectedIndex() + 1) + " and year(date) = " + this.yearComboBox.getSelectedItem() + "  and subasta = 0");
        } 
    }//GEN-LAST:event_yearComboBoxActionPerformed

    private void jTabbedPane1MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jTabbedPane1MouseClicked
        if (this.jTabbedPane1.getSelectedIndex() == 0) {
            updateEmpenoTable(" where date = '" + this.dateHelp.formatDate(this.dateCombo.getDate()) + "'" );
        } else if (this.jTabbedPane1.getSelectedIndex() == 1) {
            updateRescateTable(" where date = '" + this.dateHelp.formatDate(this.dateCombo.getDate()) + "'  and subasta = 0");
        } 
    }//GEN-LAST:event_jTabbedPane1MouseClicked

    private void jTabbedPane1HierarchyChanged(java.awt.event.HierarchyEvent evt) {//GEN-FIRST:event_jTabbedPane1HierarchyChanged
        
    }//GEN-LAST:event_jTabbedPane1HierarchyChanged


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JRadioButton byDayRadio;
    private javax.swing.JRadioButton byIntervalRadio;
    private javax.swing.JRadioButton byMonthRadio;
    private org.jdesktop.swingx.JXDatePicker date1;
    private org.jdesktop.swingx.JXDatePicker date2;
    private org.jdesktop.swingx.JXDatePicker dateCombo;
    private javax.swing.JTable empeno;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JPanel jPanel7;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JTabbedPane jTabbedPane1;
    private javax.swing.JComboBox<String> monthCombo;
    private javax.swing.JButton printBtn;
    private javax.swing.JTable rescate;
    private javax.swing.JButton saveBtn;
    private javax.swing.JFileChooser saveFile;
    private javax.swing.JComboBox<String> yearComboBox;
    // End of variables declaration//GEN-END:variables
}
