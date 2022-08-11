/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mpis_mariadb_2;

import java.awt.Font;
import java.awt.FontFormatException;
import java.awt.GraphicsEnvironment;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Calendar;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.table.DefaultTableCellRenderer;

/**
 *
 * @author Lucky
 */
public class Transaction extends javax.swing.JPanel {

    /**
     * Creates new form Transaction
     */
    public Transaction() {
//        try {
//            this.is = getClass().getResourceAsStream("/fonts/SourceSansPro-SemiBold.ttf");
//            sourceSansSemiBold = Font.createFont(Font.TRUETYPE_FONT, is);
//            this.is = getClass().getResourceAsStream("/fonts/SourceSansPro-Regular.ttf");
//            sourceSansRegular = Font.createFont(Font.TRUETYPE_FONT, is);
//            GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
//            
//            ge.registerFont(sourceSansRegular);
//            ge.registerFont(sourceSansSemiBold);
//        } catch (FontFormatException | IOException ex) {
//            Logger.getLogger(Transaction.class.getName()).log(Level.SEVERE, null, ex);
//        }
        initComponents();
    }
//    InputStream is;
//    Font sourceSansSemiBold; 
//    Font sourceSansRegular; 
    private Config con = new Config();
    private final String driver = "jdbc:mariadb://" + this.con.getProp("IP") + ":" + this.con.getProp("port") 
            + "/cashtransactions";;
    private final String finalUsername = con.getProp("username");
    private final String finalPassword = con.getProp("password");
    private CashTransactions pettyCash = new CashTransactions();
    private HistoryTableModel tablemodel = new HistoryTableModel();
    private TableHelper helpTable = new TableHelper();
    private Calendar rightNow = Calendar.getInstance();
    private DateHelper dateHelp = new DateHelper();
    private Date currentDate = this.dateHelp.now(this.rightNow);
    String curDateText = this.dateHelp.formatDate(this.currentDate);
    
    
    
    public void refreshTable() {
        try {
            updateTable();
            updateDisplay();
        } catch (SQLException ex) {
            this.con.saveProp("mpis_last_error", String.valueOf(ex));
            Logger.getLogger(Transaction.class.getName()).log(Level.SEVERE, (String) null, ex);
        }
    }
    
    
    public void updateTable() {
        String query = "Select transaction_name as 'Trans. Name', amount as 'Amt.', remarks as 'Remarks' from cashtransactions.daily_transactions where date between '"
                + this.curDateText + " 00:00:00' and '" + this.curDateText + " 23:59:59' order by date";
        String[] columnHeader = null;
        String[][] dBNames = {{"Transaction", "transaction_name"}, {"Amount", "amount"}, {"Remarks", "remarks"}};
        while (this.tablemodel.getRowCount() > 0) {
            this.tablemodel.removeRow(0);
        }
        DecimalHelper decHelp = new DecimalHelper();
        try {
            Connection connexion = DriverManager.getConnection(this.driver, this.finalUsername, this.finalPassword);
            Statement qry = connexion.createStatement();
            ResultSet queryResult = qry.executeQuery(query);
            ResultSetMetaData metaData = queryResult.getMetaData();
            int columnCount = metaData.getColumnCount();
            columnHeader = new String[columnCount];
            for (int i = 0; i < columnCount; i++) {
                columnHeader[i] = metaData.getColumnName(i + 1);
                for (int j = 0; j < dBNames.length; j++) {
                    if (dBNames[j][1].equals(columnHeader[i])) {
                        columnHeader[i] = dBNames[j][0];
                    }
                }
            }
            connexion.close();
            qry.close();
        } catch (SQLException ex) {
            this.con.saveProp("mpis_last_error", String.valueOf(ex));
            System.out.println("error in mysql");
            Logger.getLogger(Transaction.class.getName()).log(Level.SEVERE, (String) null, ex);
        }
        this.tablemodel.setColumnIdentifiers((Object[]) columnHeader);
        try {
            Connection connect = DriverManager.getConnection(this.driver, this.finalUsername, this.finalPassword);
            Statement state = connect.createStatement();
            ResultSet rSet = state.executeQuery(query);
            ResultSetMetaData rSetMetaData = rSet.getMetaData();
            int columnCount = rSetMetaData.getColumnCount();
            Object[] row = new Object[columnCount];
            while (rSet.next()) {
                for (int i = 0; i < columnCount; i++) {
                    if (i == 1) {
                        row[i] = decHelp.FormatNumber(rSet.getDouble(i + 1));
                    } else {
                        row[i] = rSet.getObject(i + 1);
                    }
                }
                this.tablemodel.addRow(row);
            }
            this.helpTable.autoResizeColWidth(this.transactionTable, this.tablemodel);
            this.transactionTable.setModel(this.tablemodel);
            connect.close();
            rSet.close();
            state.close();
        } catch (SQLException ex) {
            this.con.saveProp("mpis_last_error", String.valueOf(ex));
            System.out.println("error in mysql");
            Logger.getLogger(Transaction.class.getName()).log(Level.SEVERE, (String) null, ex);
        }
        DefaultTableCellRenderer renderRight = new DefaultTableCellRenderer();
        renderRight.setHorizontalAlignment(4);
        this.transactionTable.getColumn("Amount").setCellRenderer(renderRight);
    }

    public void updateDisplay() throws SQLException {
        Accounting acct = new Accounting();
        String totalOut = acct.computeCashOut();
        this.totalCashOutflow.setText(totalOut);
        String totalIn = acct.computeCashIn();
        this.totalCashInFlow.setText(totalIn);
        String cashOnHand = acct.computeRemaining();
        this.remainingCOH.setText(cashOnHand);
    }
    
    
    
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel2 = new javax.swing.JPanel();
        jLabel2 = new javax.swing.JLabel();
        remainingCOH = new javax.swing.JTextField();
        jLabel3 = new javax.swing.JLabel();
        totalCashInFlow = new javax.swing.JTextField();
        jLabel4 = new javax.swing.JLabel();
        totalCashOutflow = new javax.swing.JTextField();
        jButton5 = new javax.swing.JButton();
        xferBtn = new javax.swing.JButton();
        addtlBtn = new javax.swing.JButton();
        pettyBtn = new javax.swing.JButton();
        refreshBtn = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        transactionTable = new javax.swing.JTable();
        jPanel4 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();

        setBorder(new javax.swing.border.LineBorder(new java.awt.Color(153, 153, 153), 1, true));
        setMinimumSize(new java.awt.Dimension(300, 760));
        setPreferredSize(new java.awt.Dimension(300, 760));

        jLabel2.setFont(new java.awt.Font("Segoe UI Semibold", 1, 14)); // NOI18N
        jLabel2.setForeground(new java.awt.Color(51, 51, 51));
        jLabel2.setText("Cash on Hand:   ");

        remainingCOH.setEditable(false);
        remainingCOH.setFont(new java.awt.Font("Segoe UI Semibold", 1, 14)); // NOI18N
        remainingCOH.setForeground(new java.awt.Color(51, 51, 51));
        remainingCOH.setHorizontalAlignment(javax.swing.JTextField.TRAILING);
        remainingCOH.setText("0.00");

        jLabel3.setFont(new java.awt.Font("Segoe UI Semibold", 1, 14)); // NOI18N
        jLabel3.setForeground(new java.awt.Color(51, 51, 51));
        jLabel3.setText("Total Cash In Flow:   ");

        totalCashInFlow.setEditable(false);
        totalCashInFlow.setFont(new java.awt.Font("Segoe UI Semibold", 1, 14)); // NOI18N
        totalCashInFlow.setForeground(new java.awt.Color(51, 51, 51));
        totalCashInFlow.setHorizontalAlignment(javax.swing.JTextField.TRAILING);
        totalCashInFlow.setText("0.00");

        jLabel4.setFont(new java.awt.Font("Segoe UI Semibold", 1, 14)); // NOI18N
        jLabel4.setForeground(new java.awt.Color(51, 51, 51));
        jLabel4.setText("Total Cash Out Flow: ");

        totalCashOutflow.setEditable(false);
        totalCashOutflow.setFont(new java.awt.Font("Segoe UI Semibold", 1, 14)); // NOI18N
        totalCashOutflow.setForeground(new java.awt.Color(51, 51, 51));
        totalCashOutflow.setHorizontalAlignment(javax.swing.JTextField.TRAILING);
        totalCashOutflow.setText("0.00");

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jLabel3, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel2, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(remainingCOH, javax.swing.GroupLayout.DEFAULT_SIZE, 137, Short.MAX_VALUE)
                    .addComponent(totalCashInFlow)
                    .addComponent(totalCashOutflow))
                .addGap(50, 50, 50))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.CENTER)
                    .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(remainingCOH, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(5, 5, 5)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.CENTER)
                    .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(totalCashInFlow, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(5, 5, 5)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.CENTER)
                    .addComponent(jLabel4, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(totalCashOutflow, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
        );

        jButton5.setBackground(new java.awt.Color(119, 178, 211));
        jButton5.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jButton5.setForeground(new java.awt.Color(255, 255, 255));
        jButton5.setText("Print Cash Transactions Report");
        jButton5.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton5ActionPerformed(evt);
            }
        });

        xferBtn.setBackground(new java.awt.Color(119, 178, 211));
        xferBtn.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        xferBtn.setForeground(new java.awt.Color(255, 255, 255));
        xferBtn.setText("Transfer");
        xferBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                xferBtnActionPerformed(evt);
            }
        });

        addtlBtn.setBackground(new java.awt.Color(119, 178, 211));
        addtlBtn.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        addtlBtn.setForeground(new java.awt.Color(255, 255, 255));
        addtlBtn.setText("Additional");
        addtlBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                addtlBtnActionPerformed(evt);
            }
        });

        pettyBtn.setBackground(new java.awt.Color(119, 178, 211));
        pettyBtn.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        pettyBtn.setForeground(new java.awt.Color(255, 255, 255));
        pettyBtn.setText("Petty Cash");
        pettyBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                pettyBtnActionPerformed(evt);
            }
        });

        refreshBtn.setBackground(new java.awt.Color(119, 178, 211));
        refreshBtn.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        refreshBtn.setForeground(new java.awt.Color(255, 255, 255));
        refreshBtn.setIcon(new javax.swing.ImageIcon(getClass().getResource("/mpis_mariadb_2/refresh (2).png"))); // NOI18N
        refreshBtn.setText("  Refresh");
        refreshBtn.setToolTipText("");
        refreshBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                refreshBtnActionPerformed(evt);
            }
        });

        jScrollPane1.setBorder(null);

        transactionTable.setBackground(new java.awt.Color(240, 240, 240));
        transactionTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null}
            },
            new String [] {
                "Transaction", "Amount", "Remarks"
            }
        ));
        transactionTable.setFillsViewportHeight(true);
        transactionTable.setGridColor(new java.awt.Color(204, 204, 204));
        transactionTable.setRowHeight(30);
        transactionTable.setShowVerticalLines(true);
        jScrollPane1.setViewportView(transactionTable);

        jPanel4.setLayout(new javax.swing.BoxLayout(jPanel4, javax.swing.BoxLayout.LINE_AXIS));

        jLabel1.setBackground(new java.awt.Color(169, 45, 102));
        jLabel1.setFont(new java.awt.Font("Segoe UI", 1, 16)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(51, 51, 51));
        jLabel1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel1.setText("C A S H       T R A N S A C T I O N S");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(276, 276, 276)
                        .addComponent(jPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, 322, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                        .addGroup(javax.swing.GroupLayout.Alignment.LEADING, layout.createSequentialGroup()
                            .addComponent(xferBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 89, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addComponent(addtlBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 94, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addComponent(pettyBtn, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addComponent(jButton5, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.PREFERRED_SIZE, 285, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(refreshBtn, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                    .addComponent(jLabel1, javax.swing.GroupLayout.DEFAULT_SIZE, 290, Short.MAX_VALUE)))
        );

        layout.linkSize(javax.swing.SwingConstants.HORIZONTAL, new java.awt.Component[] {jLabel1, jScrollPane1, refreshBtn});

        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(refreshBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(1, 1, 1)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 491, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(xferBtn)
                    .addComponent(addtlBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(pettyBtn))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jButton5, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(9, 9, 9))
        );

        layout.linkSize(javax.swing.SwingConstants.VERTICAL, new java.awt.Component[] {addtlBtn, pettyBtn, xferBtn});

    }// </editor-fold>//GEN-END:initComponents

    private void addtlBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_addtlBtnActionPerformed
        Additionals addDiag = new Additionals(null, true);
        addDiag.setDatabase("cashtransactions");
        addDiag.updateCombos();
        addDiag.setLocationRelativeTo(null);
        addDiag.setVisible(true);
        refreshTable();
    }//GEN-LAST:event_addtlBtnActionPerformed

    private void refreshBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_refreshBtnActionPerformed
        try {
            updateTable();
            updateDisplay();
        } catch (SQLException ex) {
            this.con.saveProp("mpis_last_error", String.valueOf(ex));
            Logger.getLogger(Transaction.class.getName()).log(Level.SEVERE, (String) null, ex);
        }
    }//GEN-LAST:event_refreshBtnActionPerformed

    private void pettyBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_pettyBtnActionPerformed
        PettyCash petDiag = new PettyCash(null, true);
        petDiag.setDatabase("cashtransactions");
        petDiag.updateCombos();
        petDiag.setLocationRelativeTo(null);
        petDiag.displayNewVoucherNo();
        petDiag.setVisible(true);
        refreshTable();
    }//GEN-LAST:event_pettyBtnActionPerformed

    private void xferBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_xferBtnActionPerformed
        Transfer transDiag = new Transfer(null, true);
        transDiag.setDatabase("cashtransactions");
        transDiag.updateCombos();
        transDiag.setLocationRelativeTo(null);
        transDiag.displayNewVoucherNo();
        transDiag.setVisible(true);
        refreshTable();
    }//GEN-LAST:event_xferBtnActionPerformed

    private void jButton5ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton5ActionPerformed
        CashBreakdown cb = new CashBreakdown(null, true);
        cb.setCoh(Double.parseDouble(this.remainingCOH.getText().replace(",", "")));
        cb.setLocationRelativeTo(null);
        cb.setVisible(true);
    }//GEN-LAST:event_jButton5ActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton addtlBtn;
    private javax.swing.JButton jButton5;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JButton pettyBtn;
    private javax.swing.JButton refreshBtn;
    private javax.swing.JTextField remainingCOH;
    private javax.swing.JTextField totalCashInFlow;
    private javax.swing.JTextField totalCashOutflow;
    private javax.swing.JTable transactionTable;
    private javax.swing.JButton xferBtn;
    // End of variables declaration//GEN-END:variables
}
