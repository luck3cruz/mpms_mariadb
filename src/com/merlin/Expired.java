/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.merlin;

import java.awt.Color;
import java.awt.Component;
import java.awt.event.ActionEvent;
import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.BorderFactory;
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
public class Expired extends javax.swing.JPanel {

    /**
     * Creates new form Expired
     */
    public Expired() {
        initComponents();
        fileChoose.setVisible(false);
        this.toDateExpLoan.setDate(this.dateHelp.now(Calendar.getInstance()));
    }
    
    Config con = new Config();
    private final String driver = "jdbc:mariadb://" + this.con.getProp("IP") + ":" + this.con.getProp("port") + "/merlininventorydatabase";
    private final String f_user = this.con.getProp("username");
    private final String f_pass = this.con.getProp("password");
    private DateHelper dateHelp = new DateHelper();
    private DecimalHelper decHelp = new DecimalHelper();
    private final String srcFilePath = this.con.getProp("rep_source");;
    private final String subastaRepName = this.con.getProp("subastaRepName");
    private SimpleDateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd");
    private String whereExpiry = "";
    private DatabaseUpdater dbu = new DatabaseUpdater();
    private ReportPrinter pr = new ReportPrinter();
    
    
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
            Logger.getLogger(Additionals.class.getName()).log(Level.SEVERE, (String) null, ex);
        }
    }

    public void updateCombos() {
        updateCombo(this.branchExpLoan);
    }
    
    private void updateStatusOfLoans() {
        Calendar presentDate = Calendar.getInstance();
        Date expDate = this.dateHelp.lessFourMonths(presentDate.getTime());
        System.out.println(this.dateHelp.formatDate(expDate));
        try {
            String updateStatusExp = "update merlininventorydatabase.client_info set status = 'Expired' where new_pap_num = 'null' and transaction_date <= '" + this.dateHelp.formatDate(expDate) 
                    + "' and status = 'Open'";
            System.out.println("updating status of loans...\n" + updateStatusExp);
            Connection connectUpdate = DriverManager.getConnection(this.driver, this.f_user, this.f_pass);
            Statement queryUpdate = connectUpdate.createStatement();
            queryUpdate.executeUpdate(updateStatusExp);
            dbu.editFile(updateStatusExp, dbu.getCurDateBackUpFilename());
//            System.out.println(presentDate + " successful status updates");
//            System.out.println(updateStatusExp);
        } catch (SQLException ex) {
            this.con.saveProp("mpis_last_error", String.valueOf(ex));
            System.out.println(ex);
        }
    }

    private void updateExpiredTable(String query) throws SQLException {
        DefaultTableModel defExpTab = (DefaultTableModel) this.expiredLoanListingTable.getModel();
        while (defExpTab.getRowCount() > 0) {
            defExpTab.removeRow(0);
        }

        Connection connect = DriverManager.getConnection(this.driver, this.f_user, this.f_pass);
        Statement state = connect.createStatement();

        ResultSet rset = state.executeQuery(query);

        Object[] row = new Object[8];
        int counter = 1;
        while (rset.next()) {
            row[0] = counter;
            for (int i = 0; i < 7; i++) {
                row[i+1] = rset.getString(i + 1);
            }
            counter++;
            defExpTab.addRow(row);
        }
        state.close();
        connect.close();
    }

    public void designTable() {
        TableCellRenderer renderer = new TableCellRenderer() {
            JLabel label = new JLabel();

            public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
                this.label.setOpaque(true);
                this.label.setText("" + value);
                this.label.setVerticalAlignment(JLabel.TOP);
                this.label.setBorder(BorderFactory.createEmptyBorder(2, 10, 2, 10));
//                label.setLineWrap(true);
//                label.setWrapStyleWord(true);
//                label.setEditable(false);
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
                if (column == 0  || column == 7) {
                    this.label.setHorizontalAlignment(JLabel.RIGHT);
                } else if (column == 1 || column == 6) {
                    this.label.setHorizontalAlignment(JLabel.LEFT);
                } else {
                    this.label.setHorizontalAlignment(JLabel.CENTER);
                }
                return this.label;
            }
        };
        expiredLoanListingTable.getColumnModel().getColumn(3).setCellRenderer(new TextAreaCellRenderer());
        expiredLoanListingTable.getColumnModel().getColumn(4).setCellRenderer(new TextAreaCellRenderer());
        this.expiredLoanListingTable.setDefaultRenderer(Object.class, renderer);
        ((DefaultTableCellRenderer) this.expiredLoanListingTable.getTableHeader().getDefaultRenderer()).setHorizontalAlignment(0);
        
//        new TableHelper().autoResizeColWidth(expiredLoanListingTable, (DefaultTableModel) expiredLoanListingTable.getModel());
    }
    
     /**
     * 
     * OBSOLETE CODE: INCORPORATED SAVEREPORT IN REPORTPRINTER CLASS
     * 
    public boolean saveSubastaReport(String filePath, String destination, String report, Date date, String branch, Date disp_date) {
                boolean saveSuccessful = false;
                OutputStream output = null;
                try {
                    Connection conn = DriverManager.getConnection(this.driver, this.f_user, this.f_pass);
                    JasperDesign jasperDesign = JRXmlLoader.load(getClass().getResourceAsStream(filePath));
                    JasperReport jasperReport = JasperCompileManager.compileReport(jasperDesign);
                    Map<String, Object> param = new HashMap<>() {};
                    param.put("END_DATE", date);
                    param.put("BRANCH_PARAM", branch);
                    param.put("DISPLAY_DATE", disp_date);
                    JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, param, conn);
                    output = new FileOutputStream(new File(destination));
                    JasperExportManager.exportReportToPdfStream(jasperPrint, output);
                } catch (Exception ex) {
                    this.con.saveProp("mpis_last_error", String.valueOf(ex));
                    Logger.getLogger(Expired.class.getName()).log(Level.SEVERE, (String)null, ex);
                } finally {
                    try {
                        output.close();
                        saveSuccessful = true;
                    } catch (IOException ex) {
                        this.con.saveProp("mpis_last_error", String.valueOf(ex));
                        saveSuccessful = false;
                    Logger.getLogger(Expired.class.getName()).log(Level.SEVERE, (String)null, ex);
                    } 
                } 
                return saveSuccessful;
    }

    public void printSubastaReport(String filePath, Date date, String branch, Date disp_date) {
        try {
            Connection conn = DriverManager.getConnection(this.driver, this.f_user, this.f_pass);
         try {
             JasperDesign jasperDesign = JRXmlLoader.load(getClass().getResourceAsStream(filePath));
             JasperReport jasperReport = JasperCompileManager.compileReport(jasperDesign);
             Map<String, Object> param = new HashMap<>();
             param.put("END_DATE", date);
             param.put("DISPLAY_DATE", disp_date);
             param.put("BRANCH_PARAM", branch);
             JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, param, conn);

             JasperPrintManager.printReport(jasperPrint, true);
         } catch (JRException ex) {
             this.con.saveProp("mpis_last_error", String.valueOf(ex));
             JOptionPane.showMessageDialog(null, ex);
         }
        } catch (SQLException ex) {
            this.con.saveProp("mpis_last_error", String.valueOf(ex));
            Logger.getLogger(Expired.class.getName()).log(Level.SEVERE, (String) null, ex);
        }
    }

    private void printSubastaDot(String filePath, Date date, String branch) {
        File tempFolder = new File("C:\\MPIS\\tempRep");
        if (!tempFolder.exists()) {
            tempFolder.mkdir();
       }
        String destination = "C:\\MPIS\\tempRep\\tempASub.xls";
        OutputStream output = null;
       try {
           Connection conn = DriverManager.getConnection(this.driver, this.f_user, this.f_pass);
           JasperDesign jasperDesign = JRXmlLoader.load(getClass().getResourceAsStream(filePath));
           JasperReport jasperReport = JasperCompileManager.compileReport(jasperDesign);
           Map<String, Object> param = new HashMap<>();
           param.put("END_DATE", date);
           param.put("BRANCH_PARAM", branch);
           JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, param, conn);
           JRXlsExporter exporter = new JRXlsExporter();
           exporter.setParameter(JRExporterParameter.JASPER_PRINT, jasperPrint);

           output = new FileOutputStream(new File(destination));
           exporter.setParameter(JRExporterParameter.OUTPUT_STREAM, output);
           exporter.setParameter((JRExporterParameter) JRXlsExporterParameter.IS_ONE_PAGE_PER_SHEET, Boolean.FALSE);
           exporter.setParameter((JRExporterParameter) JRXlsExporterParameter.IS_REMOVE_EMPTY_SPACE_BETWEEN_ROWS, Boolean.TRUE);
           exporter.setParameter((JRExporterParameter) JRXlsExporterParameter.IS_WHITE_PAGE_BACKGROUND, Boolean.FALSE);
           exporter.exportReport();
         try {
             Desktop.getDesktop().print(new File(destination));
         } catch (IOException ex) {
             System.out.println("print error");
             Logger.getLogger(EmpenoRescate.class.getName()).log(Level.SEVERE, (String) null, ex);
         }
           InputStream is = new FileInputStream(new File(destination));
         try {
             is.close();
             File fi = new File(destination);
             fi.deleteOnExit();
         } catch (IOException ex) {
             this.con.saveProp("mpis_last_error", String.valueOf(ex));
             Logger.getLogger(EmpenoRescate.class.getName()).log(Level.SEVERE, (String) null, ex);
         }
       } catch (Exception ex) {
           this.con.saveProp("mpis_last_error", String.valueOf(ex));
           Logger.getLogger(org.jfree.data.statistics.Statistics.class.getName()).log(Level.SEVERE, (String) null, ex);
     } finally {
         try {
             output.close();
         } catch (IOException ex) {
             this.con.saveProp("mpis_last_error", String.valueOf(ex));
             JOptionPane.showMessageDialog(null, "An error occured while printing your Subasta Report", "Subasta Report", 0);
             Logger.getLogger(org.jfree.data.statistics.Statistics.class.getName()).log(Level.SEVERE, (String) null, ex);
            }
        }
    }
    
    **/

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
        jLabel2 = new javax.swing.JLabel();
        toDateExpLoan = new org.jdesktop.swingx.JXDatePicker();
        jLabel3 = new javax.swing.JLabel();
        branchExpLoan = new javax.swing.JComboBox<>();
        generateExpiredLoanList = new javax.swing.JButton();
        saveBtn = new javax.swing.JButton();
        printBtn = new javax.swing.JButton();
        viewPapBtn = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        expiredLoanListingTable = new javax.swing.JTable();
        fileChoose = new javax.swing.JFileChooser();

        setMinimumSize(new java.awt.Dimension(840, 768));
        setPreferredSize(new java.awt.Dimension(840, 768));
        setLayout(new java.awt.BorderLayout());

        jPanel1.setBackground(new java.awt.Color(255, 255, 255));

        jLabel2.setForeground(new java.awt.Color(102, 102, 102));
        jLabel2.setText("Expired Loans as of:");

        toDateExpLoan.setToolTipText("YYYY-MM-DD");
        toDateExpLoan.setFormats(dateFormatter);

        jLabel3.setForeground(new java.awt.Color(102, 102, 102));
        jLabel3.setText("Branch:");

        branchExpLoan.setFont(new java.awt.Font("Segoe UI Semibold", 0, 13)); // NOI18N
        branchExpLoan.setForeground(new java.awt.Color(51, 51, 51));
        branchExpLoan.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));

        generateExpiredLoanList.setBackground(new java.awt.Color(79, 119, 141));
        generateExpiredLoanList.setFont(new java.awt.Font("Segoe UI Semibold", 0, 13)); // NOI18N
        generateExpiredLoanList.setForeground(new java.awt.Color(255, 255, 255));
        generateExpiredLoanList.setText("Generate");
        generateExpiredLoanList.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                generateExpiredLoanListActionPerformed(evt);
            }
        });

        saveBtn.setFont(new java.awt.Font("Segoe UI Semibold", 0, 13)); // NOI18N
        saveBtn.setForeground(new java.awt.Color(79, 119, 141));
        saveBtn.setText("Save");
        saveBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                saveBtnActionPerformed(evt);
            }
        });

        printBtn.setFont(new java.awt.Font("Segoe UI Semibold", 0, 13)); // NOI18N
        printBtn.setForeground(new java.awt.Color(79, 119, 141));
        printBtn.setText("Print");
        printBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                printBtnActionPerformed(evt);
            }
        });

        viewPapBtn.setFont(new java.awt.Font("Segoe UI Semibold", 0, 13)); // NOI18N
        viewPapBtn.setForeground(new java.awt.Color(79, 119, 141));
        viewPapBtn.setText("View Pap");
        viewPapBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                viewPapBtnActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(19, 19, 19)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jLabel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(toDateExpLoan, javax.swing.GroupLayout.PREFERRED_SIZE, 130, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel3)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(branchExpLoan, javax.swing.GroupLayout.PREFERRED_SIZE, 133, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(generateExpiredLoanList, javax.swing.GroupLayout.PREFERRED_SIZE, 134, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(saveBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(printBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 63, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(viewPapBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 118, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(0, 50, Short.MAX_VALUE))
        );

        jPanel1Layout.linkSize(javax.swing.SwingConstants.HORIZONTAL, new java.awt.Component[] {printBtn, saveBtn});

        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(14, 14, 14)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel2)
                    .addComponent(jLabel3))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(toDateExpLoan, javax.swing.GroupLayout.DEFAULT_SIZE, 30, Short.MAX_VALUE)
                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(branchExpLoan)
                        .addComponent(generateExpiredLoanList, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(saveBtn)
                        .addComponent(printBtn)
                        .addComponent(viewPapBtn)))
                .addContainerGap(12, Short.MAX_VALUE))
        );

        jPanel1Layout.linkSize(javax.swing.SwingConstants.VERTICAL, new java.awt.Component[] {branchExpLoan, generateExpiredLoanList, printBtn, saveBtn, toDateExpLoan, viewPapBtn});

        expiredLoanListingTable.setBackground(new java.awt.Color(240, 240, 240));
        expiredLoanListingTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "#", "Pap No", "Client Name", "Client Address", "Item Description", "Remarks", "Date Loan", "Principal"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        expiredLoanListingTable.setFillsViewportHeight(true);
        expiredLoanListingTable.setGridColor(new java.awt.Color(102, 102, 102));
        expiredLoanListingTable.setRowHeight(30);
        expiredLoanListingTable.getTableHeader().setReorderingAllowed(false);
        jScrollPane1.setViewportView(expiredLoanListingTable);
        if (expiredLoanListingTable.getColumnModel().getColumnCount() > 0) {
            expiredLoanListingTable.getColumnModel().getColumn(0).setResizable(false);
            expiredLoanListingTable.getColumnModel().getColumn(0).setPreferredWidth(3);
            expiredLoanListingTable.getColumnModel().getColumn(1).setResizable(false);
            expiredLoanListingTable.getColumnModel().getColumn(1).setPreferredWidth(30);
            expiredLoanListingTable.getColumnModel().getColumn(2).setResizable(false);
            expiredLoanListingTable.getColumnModel().getColumn(2).setPreferredWidth(100);
            expiredLoanListingTable.getColumnModel().getColumn(3).setResizable(false);
            expiredLoanListingTable.getColumnModel().getColumn(3).setPreferredWidth(180);
            expiredLoanListingTable.getColumnModel().getColumn(4).setResizable(false);
            expiredLoanListingTable.getColumnModel().getColumn(4).setPreferredWidth(200);
            expiredLoanListingTable.getColumnModel().getColumn(5).setResizable(false);
            expiredLoanListingTable.getColumnModel().getColumn(5).setPreferredWidth(70);
            expiredLoanListingTable.getColumnModel().getColumn(6).setResizable(false);
            expiredLoanListingTable.getColumnModel().getColumn(6).setPreferredWidth(40);
            expiredLoanListingTable.getColumnModel().getColumn(7).setResizable(false);
            expiredLoanListingTable.getColumnModel().getColumn(7).setPreferredWidth(40);
        }

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel3Layout.createSequentialGroup()
                .addGap(12, 12, 12)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGap(13, 13, 13))
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addComponent(jScrollPane1)
                        .addContainerGap())))
            .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jPanel3Layout.createSequentialGroup()
                    .addGap(0, 0, Short.MAX_VALUE)
                    .addComponent(fileChoose, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGap(0, 0, Short.MAX_VALUE)))
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addGap(12, 12, 12)
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 668, Short.MAX_VALUE)
                .addContainerGap())
            .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jPanel3Layout.createSequentialGroup()
                    .addGap(0, 0, Short.MAX_VALUE)
                    .addComponent(fileChoose, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGap(0, 0, Short.MAX_VALUE)))
        );

        jScrollPane2.setViewportView(jPanel3);

        add(jScrollPane2, java.awt.BorderLayout.CENTER);
    }// </editor-fold>//GEN-END:initComponents

    private void generateExpiredLoanListActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_generateExpiredLoanListActionPerformed
        updateStatusOfLoans();
        String query = "select a.pap_num, client_name, client_address, item_description, remarks, transaction_date, principal from merlininventorydatabase.client_info a inner join merlininventorydatabase.empeno b on a.item_code_a = b.pap_num ";
        if (this.toDateExpLoan.getDate() == null) {
            JOptionPane.showMessageDialog(null, "Please enter a 'Reference Date' in the Search fields.", "Search Option", 1);
        } else {
            this.whereExpiry = " WHERE branch = '" + this.branchExpLoan.getSelectedItem().toString() + "' AND DATE(transaction_date) <= '" + this.dateFormatter.format(this.dateHelp.lessFourMonths(this.toDateExpLoan.getDate())) + "' and new_pap_num = 'null' and status = 'Expired'  order by classification asc, transaction_date desc";
            query = query.concat(this.whereExpiry);
            System.out.println(query);
            try {
              updateExpiredTable(query);
              designTable();
            } catch (SQLException ex) {
              this.con.saveProp("mpis_last_error", String.valueOf(ex));
              Logger.getLogger(Expired.class.getName()).log(Level.SEVERE, (String)null, ex);
            } 
        } 
    }//GEN-LAST:event_generateExpiredLoanListActionPerformed

    private void saveBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_saveBtnActionPerformed
        if (this.expiredLoanListingTable.getRowCount() > 0) {
            String suggestedFileName = "SUB-" + this.dateFormatter.format(this.toDateExpLoan.getDate()) + "-" + this.branchExpLoan.getSelectedItem().toString().toUpperCase();
            this.fileChoose.setSelectedFile(new File(suggestedFileName));
            this.fileChoose.setVisible(true);
            int showSaveDialog = this.fileChoose.showSaveDialog((Component)null);
            if (showSaveDialog == 0) {
                String fileName = "";
                if (this.fileChoose.getSelectedFile().toString().substring(this.fileChoose.getSelectedFile().toString().length() - 4).equals(".pdf")) {
                    fileName = this.fileChoose.getSelectedFile().getAbsolutePath();
                } else {
                    fileName = this.fileChoose.getSelectedFile().getAbsolutePath().concat(".pdf");
                } 
                this.fileChoose.setSelectedFile(new File(fileName));
                if (this.fileChoose.getSelectedFile().exists()) {
                    int replaceFile = JOptionPane.showConfirmDialog(null, "Item Already Exists. Do you want to replace this file?", "Replace File", 0);
                    if (replaceFile == 1) {
                        saveBtnActionPerformed((ActionEvent)null);
                        return;
                    } 
                } 
                Map<String, Object> param = new HashMap<>() {};
                param.put("END_DATE", (dateHelp.lessFourMonths(this.toDateExpLoan.getDate())));
                param.put("BRANCH_PARAM", this.branchExpLoan.getSelectedItem().toString());
                param.put("DISPLAY_DATE", this.toDateExpLoan.getDate());
                pr.setReport_name("Subasta Report");
                if (pr.saveReport("/Reports/subasta.jrxml", fileName, param)) {
//                if (saveSubastaReport("/Reports/subasta.jrxml", fileName, "Expired Loan Listing (Subasta Report)", (dateHelp.lessFourMonths(this.toDateExpLoan.getDate())), this.branchExpLoan.getSelectedItem().toString(), this.toDateExpLoan.getDate()) == true) {
                JOptionPane.showMessageDialog(null, "Expired Loan Listing (Subasta Report) successfully Saved!", "Expired Loan Listing (Subasta Report)", 1);
            } else {
                JOptionPane.showMessageDialog(null, "An error occured while saving your Expired Loan Listing (Subasta Report)", "Expired Loan Listing (Subasta Report)", 0);
            } 
            this.fileChoose.setVisible(false);
            } 
        } else {
            JOptionPane.showMessageDialog(null, "No file to save. Please generate a report first.", "Expired Loan", 0);
        } 
    }//GEN-LAST:event_saveBtnActionPerformed

    private void printBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_printBtnActionPerformed
        if (this.expiredLoanListingTable.getRowCount() > 0) {
            Map<String, Object> param = new HashMap<>() {};
                    param.put("END_DATE", (dateHelp.lessFourMonths(this.toDateExpLoan.getDate())));
                    param.put("BRANCH_PARAM", this.branchExpLoan.getSelectedItem().toString());
                    param.put("DISPLAY_DATE", this.toDateExpLoan.getDate());
                    pr.setReport_name("Subasta Report");
                    pr.printReport("/Reports/subasta.jrxml", param);
            
//            printSubastaReport("/Reports/subasta.jrxml", (dateHelp.lessFourMonths(this.toDateExpLoan.getDate())), this.branchExpLoan.getSelectedItem().toString(), this.toDateExpLoan.getDate());
        } else {
            JOptionPane.showMessageDialog(null, "No file to print. Please generate a report first.", "Expired Loan", 0);
        } 
    }//GEN-LAST:event_printBtnActionPerformed

    private void viewPapBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_viewPapBtnActionPerformed
        Sangla oldSangla = new Sangla();
        oldSangla.setPap_num(this.expiredLoanListingTable.getValueAt(this.expiredLoanListingTable.getSelectedRow(), 1).toString());
        oldSangla.setBranch(this.branchExpLoan.getSelectedItem().toString());
        oldSangla.setItemCodes();
        oldSangla.retrieveSangla(oldSangla.getItem_code_a());
        ViewMode view = new ViewMode(null, true, oldSangla);
        view.setVisible(true);
        view.setAlwaysOnTop(true);
    }//GEN-LAST:event_viewPapBtnActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JComboBox<String> branchExpLoan;
    private javax.swing.JTable expiredLoanListingTable;
    private javax.swing.JFileChooser fileChoose;
    private javax.swing.JButton generateExpiredLoanList;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JButton printBtn;
    private javax.swing.JButton saveBtn;
    private org.jdesktop.swingx.JXDatePicker toDateExpLoan;
    private javax.swing.JButton viewPapBtn;
    // End of variables declaration//GEN-END:variables
}
