/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mpis_mariadb_2;

import java.awt.Color;
import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
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
import javafx.scene.input.KeyCode;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.UIManager;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;

/**
 *
 * @author Lucky
 */
public class Search extends javax.swing.JPanel {

    /**
     * @return the constant1
     */
    public String getConstant1() {
        return constant1;
    }

    /**
     * @param constant1 the constant1 to set
     */
    public void setConstant1(String constant1) {
        this.constant1 = constant1;
    }

    /**
     * @return the constant2
     */
    public String getConstant2() {
        return constant2;
    }

    /**
     * @param constant2 the constant2 to set
     */
    public void setConstant2(String constant2) {
        this.constant2 = constant2;
    }

    /**
     * @return the constant3
     */
    public String getConstant3() {
        return constant3;
    }

    /**
     * @param constant3 the constant3 to set
     */
    public void setConstant3(String constant3) {
        this.constant3 = constant3;
    }

    /**
     * @return the constant4
     */
    public String getConstant4() {
        return constant4;
    }

    /**
     * @param constant4 the constant4 to set
     */
    public void setConstant4(String constant4) {
        this.constant4 = constant4;
    }

    /**
     * @return the constant5
     */
    public String getConstant5() {
        return constant5;
    }

    /**
     * @param constant5 the constant5 to set
     */
    public void setConstant5(String constant5) {
        this.constant5 = constant5;
    }

    /**
     * @return the renewPapNo
     */
    public String getRenewPapNo() {
        return renewPapNo;
    }

    /**
     * @param renewPapNo the renewPapNo to set
     */
    public void setRenewPapNo(String renewPapNo) {
        this.renewPapNo = renewPapNo;
    }

    /**
     * @return the renewBranch
     */
    public String getRenewBranch() {
        return renewBranch;
    }

    /**
     * @param renewBranch the renewBranch to set
     */
    public void setRenewBranch(String renewBranch) {
        this.renewBranch = renewBranch;
    }

    /**
     * @return the renewLoanActivated
     */
    public boolean isRenewLoanActivated() {
        return renewLoanActivated;
    }

    /**
     * @param renewLoanActivated the renewLoanActivated to set
     */
    public void setRenewLoanActivated(boolean renewLoanActivated) {
        this.renewLoanActivated = renewLoanActivated;
    }

    /**
     * @return the redeemLoanActivated
     */
    public boolean isRedeemLoanActivated() {
        return redeemLoanActivated;
    }

    /**
     * @param redeemLoanActivated the redeemLoanActivated to set
     */
    public void setRedeemLoanActivated(boolean redeemLoanActivated) {
        this.redeemLoanActivated = redeemLoanActivated;
    }

    /**
     * @return the newLoanActivated
     */
    public boolean isNewLoanActivated() {
        return newLoanActivated;
    }

    /**
     * @param newLoanActivated the newLoanActivated to set
     */
    public void setNewLoanActivated(boolean newLoanActivated) {
        this.newLoanActivated = newLoanActivated;
    }

    /**
     * Creates new form Renewal
     */
    public Search() {
        initComponents();
    }
    
    Config con = new Config();
    private final String driver = "jdbc:mariadb://" + this.con.getProp("IP") + ":" + this.con.getProp("port") + "/cashtransactions";
    private final String f_user = this.con.getProp("username");
    private final String f_pass = this.con.getProp("password");
    private DecimalHelper decHelp = new DecimalHelper();
    private DateHelper dateHelp = new DateHelper();
    private boolean newLoanActivated = false;
    private boolean renewLoanActivated = false;
    private boolean redeemLoanActivated = false;
    private String whereSearchPawn = "";
    private Sangla viewSangla = new Sangla();
    private ListGenerator listGen = new ListGenerator();
    private String[] constantInfo = new String[5];
    private String renewPapNo = "";
    private String renewBranch = "";
    private String constant1;
    private String constant2;
    private String constant3;
    private String constant4;
    private String constant5;
    
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
        updateCombo(this.branchSearch);
    }
    
    private void updateStatusOfLoans() { 
        Calendar presentDate = Calendar.getInstance();
        Date expDate = this.dateHelp.lessFourMonths(presentDate.getTime());   //CORRECTED JUNE 2022 TO CORRECT PREMATURE INCLUSION OF PLEDGE LOANS INCLUDED IN EXPIRED LISTS WHEN CONTRACT IS ABOUT TO BE MAXED!
        try {
            String updateStatusExp = "update merlininventorydatabase.client_info set status = 'Expired' where new_pap_num = 'null' and transaction_date <= '" + this.dateHelp.formatDate(expDate) + "' and status != 'Closed' and status != 'Expired' and status != 'Repossessed' and status != 'Sold'";
            Connection connectUpdate = DriverManager.getConnection(this.driver, this.f_user, this.f_pass);
            Statement queryUpdate = connectUpdate.createStatement();
            queryUpdate.executeUpdate(updateStatusExp);
        } catch (SQLException ex) {
            this.con.saveProp("mpis_last_error", String.valueOf(ex));
        } 
    }


private void updateTable(String query) throws SQLException {
     DefaultTableModel defSearchTableMod = (DefaultTableModel)this.customerListingTable.getModel();
     while (defSearchTableMod.getRowCount() > 0) {
       defSearchTableMod.removeRow(0);
  }
     Connection connect = DriverManager.getConnection(this.driver, this.f_user, this.f_pass);
     Statement state = connect.createStatement();
//     state.execute("set @i = 0");
     System.out.println(query);
     ResultSet rset = state.executeQuery(query);
     Object[] row = new Object[7];
     while (rset.next()) {
       for (int i = 0; i < 6; i++) {
         row[i] = rset.getString(i + 1);
    }
       defSearchTableMod.addRow(row);
  } 
  
     connect.close();
     this.customerListingTable.setModel(defSearchTableMod);
  
     TableCellRenderer renderer = new TableCellRenderer()
    {
         JLabel label = new JLabel();
 
 
 
      
      public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
           this.label.setOpaque(true);
           this.label.setText("" + value);
//           Color alternate = UIManager.getColor("Table.alternateRowColor");
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
           if (column == 6 || column == 0) {
             this.label.setHorizontalAlignment(4);
           } else if (column == 5 || column == 1) {
             this.label.setHorizontalAlignment(0);
        } else {
             this.label.setHorizontalAlignment(2);
        } 
           return this.label;
      }
    };
     customerListingTable.getColumnModel().getColumn(2).setCellRenderer(new TextAreaCellRenderer());
     customerListingTable.getColumnModel().getColumn(3).setCellRenderer(new TextAreaCellRenderer());
     this.customerListingTable.setDefaultRenderer(Object.class, renderer);
     ((DefaultTableCellRenderer)this.customerListingTable.getTableHeader().getDefaultRenderer()).setHorizontalAlignment(0);
     if (defSearchTableMod.getRowCount() <= 0) {
       JOptionPane.showMessageDialog(null, "No Results Found!", "Search Customers/Papeleta", 1);
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

        jScrollPane5 = new javax.swing.JScrollPane();
        jPanel4 = new javax.swing.JPanel();
        searchPanel = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        tempSearchPapNum = new javax.swing.JTextField();
        searchLoan = new javax.swing.JButton();
        tempSearchName = new javax.swing.JTextField();
        clearSelection = new javax.swing.JButton();
        jPanel2 = new javax.swing.JPanel();
        jLabel6 = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        jLabel9 = new javax.swing.JLabel();
        jLabel10 = new javax.swing.JLabel();
        jLabel13 = new javax.swing.JLabel();
        jLabel14 = new javax.swing.JLabel();
        jLabel15 = new javax.swing.JLabel();
        jLabel16 = new javax.swing.JLabel();
        jLabel17 = new javax.swing.JLabel();
        jLabel18 = new javax.swing.JLabel();
        jLabel19 = new javax.swing.JLabel();
        jLabel20 = new javax.swing.JLabel();
        papeletaNoVM = new javax.swing.JTextField();
        clientNameVM = new javax.swing.JTextField();
        oldPapNoVM = new javax.swing.JTextField();
        processedByVM = new javax.swing.JTextField();
        itemClassificationVM = new javax.swing.JTextField();
        jScrollPane1 = new javax.swing.JScrollPane();
        itemDescVM = new javax.swing.JTextArea();
        jScrollPane2 = new javax.swing.JScrollPane();
        clientAddressVM = new javax.swing.JTextArea();
        jLabel23 = new javax.swing.JLabel();
        jScrollPane3 = new javax.swing.JScrollPane();
        remarksVM = new javax.swing.JTextArea();
        jLabel24 = new javax.swing.JLabel();
        jLabel25 = new javax.swing.JLabel();
        jLabel26 = new javax.swing.JLabel();
        netProceedsVM = new javax.swing.JTextField();
        newPapNoVM = new javax.swing.JTextField();
        statusVM = new javax.swing.JTextField();
        jLabel30 = new javax.swing.JLabel();
        branchVM = new javax.swing.JTextField();
        jLabel35 = new javax.swing.JLabel();
        maturityDateVM = new javax.swing.JTextField();
        transactionDateVM = new javax.swing.JTextField();
        expiryDateVM = new javax.swing.JTextField();
        principalVM = new javax.swing.JTextField();
        interestVM = new javax.swing.JTextField();
        serviceChargeVM = new javax.swing.JTextField();
        jLabel36 = new javax.swing.JLabel();
        insuranceVM = new javax.swing.JTextField();
        jScrollPane4 = new javax.swing.JScrollPane();
        customerListingTable = new javax.swing.JTable();
        jScrollPane6 = new javax.swing.JScrollPane();
        customerListingTable1 = new javax.swing.JTable();
        jPanel3 = new javax.swing.JPanel();
        viewHistory = new javax.swing.JButton();
        newLoanBtn = new javax.swing.JButton();
        renewLoanBtn = new javax.swing.JButton();
        redeemLoanBtn = new javax.swing.JButton();
        jPanel1 = new javax.swing.JPanel();
        jLabel4 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        branchSearch = new javax.swing.JComboBox<>();
        jLabel7 = new javax.swing.JLabel();
        classificationSearch = new javax.swing.JComboBox<>();
        jLabel11 = new javax.swing.JLabel();
        classificationSearch1 = new javax.swing.JComboBox<>();
        jLabel12 = new javax.swing.JLabel();
        classificationSearch2 = new javax.swing.JComboBox<>();
        jLabel21 = new javax.swing.JLabel();
        classificationSearch3 = new javax.swing.JComboBox<>();

        setMinimumSize(new java.awt.Dimension(840, 768));
        setPreferredSize(new java.awt.Dimension(840, 768));
        setLayout(new java.awt.BorderLayout());

        searchPanel.setBackground(new java.awt.Color(255, 255, 255));
        searchPanel.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(255, 255, 255), 1, true));

        jLabel1.setForeground(new java.awt.Color(102, 102, 102));
        jLabel1.setText("Search by Client's Name");

        jLabel2.setForeground(new java.awt.Color(102, 102, 102));
        jLabel2.setText("Search by Loan No.");

        tempSearchPapNum.setFont(new java.awt.Font("Segoe UI Semibold", 0, 13)); // NOI18N
        tempSearchPapNum.setForeground(new java.awt.Color(51, 51, 51));
        tempSearchPapNum.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                tempSearchPapNumKeyPressed(evt);
            }
            public void keyTyped(java.awt.event.KeyEvent evt) {
                tempSearchPapNumKeyTyped(evt);
            }
        });

        searchLoan.setBackground(new java.awt.Color(79, 119, 141));
        searchLoan.setFont(new java.awt.Font("Segoe UI Semibold", 0, 13)); // NOI18N
        searchLoan.setForeground(new java.awt.Color(255, 255, 255));
        searchLoan.setIcon(new javax.swing.ImageIcon(getClass().getResource("/mpis_mariadb_2/magnifying-glass-search.png"))); // NOI18N
        searchLoan.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                searchLoanActionPerformed(evt);
            }
        });

        tempSearchName.setFont(new java.awt.Font("Segoe UI Semibold", 0, 13)); // NOI18N
        tempSearchName.setForeground(new java.awt.Color(51, 51, 51));
        tempSearchName.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                tempSearchNameKeyPressed(evt);
            }
        });

        clearSelection.setFont(new java.awt.Font("Segoe UI Semibold", 0, 13)); // NOI18N
        clearSelection.setForeground(new java.awt.Color(79, 119, 141));
        clearSelection.setText("Clear");
        clearSelection.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                clearSelectionActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout searchPanelLayout = new javax.swing.GroupLayout(searchPanel);
        searchPanel.setLayout(searchPanelLayout);
        searchPanelLayout.setHorizontalGroup(
            searchPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(searchPanelLayout.createSequentialGroup()
                .addGap(13, 13, 13)
                .addGroup(searchPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(tempSearchName)
                    .addComponent(jLabel1, javax.swing.GroupLayout.DEFAULT_SIZE, 191, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(searchPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(tempSearchPapNum)
                    .addComponent(jLabel2, javax.swing.GroupLayout.DEFAULT_SIZE, 119, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(searchLoan, javax.swing.GroupLayout.PREFERRED_SIZE, 81, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(clearSelection, javax.swing.GroupLayout.PREFERRED_SIZE, 74, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        searchPanelLayout.setVerticalGroup(
            searchPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(searchPanelLayout.createSequentialGroup()
                .addGap(26, 26, 26)
                .addGroup(searchPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1)
                    .addComponent(jLabel2))
                .addGap(1, 1, 1)
                .addGroup(searchPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(searchPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(tempSearchName, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(tempSearchPapNum, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(searchLoan, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(clearSelection, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(0, 12, Short.MAX_VALUE))
        );

        jPanel2.setBackground(new java.awt.Color(227, 240, 251));
        jPanel2.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(227, 240, 251), 1, true));

        jLabel6.setForeground(new java.awt.Color(102, 102, 102));
        jLabel6.setText("Loan No.");

        jLabel8.setForeground(new java.awt.Color(102, 102, 102));
        jLabel8.setText("Old Loan No.");

        jLabel9.setForeground(new java.awt.Color(102, 102, 102));
        jLabel9.setText("Name");

        jLabel10.setForeground(new java.awt.Color(102, 102, 102));
        jLabel10.setText("Address");

        jLabel13.setForeground(new java.awt.Color(102, 102, 102));
        jLabel13.setText("New Loan No.");

        jLabel14.setForeground(new java.awt.Color(102, 102, 102));
        jLabel14.setText("Item Description");

        jLabel15.setForeground(new java.awt.Color(102, 102, 102));
        jLabel15.setText("Status");

        jLabel16.setForeground(new java.awt.Color(102, 102, 102));
        jLabel16.setText("Processed by");

        jLabel17.setForeground(new java.awt.Color(102, 102, 102));
        jLabel17.setText("Loan Date");

        jLabel18.setForeground(new java.awt.Color(102, 102, 102));
        jLabel18.setText("Remarks");

        jLabel19.setForeground(new java.awt.Color(102, 102, 102));
        jLabel19.setText("Principal");

        jLabel20.setForeground(new java.awt.Color(102, 102, 102));
        jLabel20.setText("Expiry Date");

        papeletaNoVM.setEditable(false);
        papeletaNoVM.setFont(new java.awt.Font("Segoe UI Semibold", 0, 13)); // NOI18N
        papeletaNoVM.setForeground(new java.awt.Color(51, 51, 51));

        clientNameVM.setEditable(false);
        clientNameVM.setFont(new java.awt.Font("Segoe UI Semibold", 0, 13)); // NOI18N
        clientNameVM.setForeground(new java.awt.Color(51, 51, 51));

        oldPapNoVM.setEditable(false);
        oldPapNoVM.setFont(new java.awt.Font("Segoe UI Semibold", 0, 13)); // NOI18N
        oldPapNoVM.setForeground(new java.awt.Color(51, 51, 51));
        oldPapNoVM.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                oldPapNoVMActionPerformed(evt);
            }
        });

        processedByVM.setEditable(false);
        processedByVM.setFont(new java.awt.Font("Segoe UI Semibold", 0, 13)); // NOI18N
        processedByVM.setForeground(new java.awt.Color(51, 51, 51));

        itemClassificationVM.setEditable(false);
        itemClassificationVM.setFont(new java.awt.Font("Segoe UI Semibold", 0, 13)); // NOI18N
        itemClassificationVM.setForeground(new java.awt.Color(51, 51, 51));
        itemClassificationVM.setMinimumSize(new java.awt.Dimension(36, 20));

        itemDescVM.setEditable(false);
        itemDescVM.setBackground(new java.awt.Color(240, 240, 240));
        itemDescVM.setColumns(20);
        itemDescVM.setFont(new java.awt.Font("Segoe UI Semibold", 0, 13)); // NOI18N
        itemDescVM.setForeground(new java.awt.Color(51, 51, 51));
        itemDescVM.setLineWrap(true);
        itemDescVM.setRows(3);
        itemDescVM.setWrapStyleWord(true);
        jScrollPane1.setViewportView(itemDescVM);

        clientAddressVM.setEditable(false);
        clientAddressVM.setBackground(new java.awt.Color(240, 240, 240));
        clientAddressVM.setColumns(20);
        clientAddressVM.setFont(new java.awt.Font("Segoe UI Semibold", 0, 13)); // NOI18N
        clientAddressVM.setForeground(new java.awt.Color(51, 51, 51));
        clientAddressVM.setLineWrap(true);
        clientAddressVM.setRows(2);
        clientAddressVM.setWrapStyleWord(true);
        jScrollPane2.setViewportView(clientAddressVM);

        jLabel23.setForeground(new java.awt.Color(102, 102, 102));
        jLabel23.setText("Maturity Date");

        remarksVM.setEditable(false);
        remarksVM.setBackground(new java.awt.Color(240, 240, 240));
        remarksVM.setColumns(20);
        remarksVM.setFont(new java.awt.Font("Segoe UI Semibold", 0, 13)); // NOI18N
        remarksVM.setForeground(new java.awt.Color(51, 51, 51));
        remarksVM.setLineWrap(true);
        remarksVM.setRows(2);
        remarksVM.setWrapStyleWord(true);
        jScrollPane3.setViewportView(remarksVM);

        jLabel24.setForeground(new java.awt.Color(102, 102, 102));
        jLabel24.setText("Advance Interest");

        jLabel25.setForeground(new java.awt.Color(102, 102, 102));
        jLabel25.setText("Service Charge");

        jLabel26.setForeground(new java.awt.Color(102, 102, 102));
        jLabel26.setText("Net Proceeds");

        netProceedsVM.setEditable(false);
        netProceedsVM.setFont(new java.awt.Font("Segoe UI Semibold", 0, 13)); // NOI18N
        netProceedsVM.setForeground(new java.awt.Color(51, 51, 51));
        netProceedsVM.setMinimumSize(new java.awt.Dimension(36, 20));

        newPapNoVM.setEditable(false);
        newPapNoVM.setFont(new java.awt.Font("Segoe UI Semibold", 0, 13)); // NOI18N
        newPapNoVM.setForeground(new java.awt.Color(51, 51, 51));
        newPapNoVM.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                newPapNoVMActionPerformed(evt);
            }
        });

        statusVM.setEditable(false);
        statusVM.setFont(new java.awt.Font("Segoe UI Semibold", 0, 13)); // NOI18N
        statusVM.setForeground(new java.awt.Color(51, 51, 51));
        statusVM.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                statusVMActionPerformed(evt);
            }
        });
        statusVM.addPropertyChangeListener(new java.beans.PropertyChangeListener() {
            public void propertyChange(java.beans.PropertyChangeEvent evt) {
                statusVMPropertyChange(evt);
            }
        });

        jLabel30.setForeground(new java.awt.Color(102, 102, 102));
        jLabel30.setText("Branch");

        branchVM.setEditable(false);
        branchVM.setFont(new java.awt.Font("Segoe UI Semibold", 0, 13)); // NOI18N
        branchVM.setForeground(new java.awt.Color(51, 51, 51));
        branchVM.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                branchVMActionPerformed(evt);
            }
        });

        jLabel35.setForeground(new java.awt.Color(102, 102, 102));
        jLabel35.setText("Item Type");

        maturityDateVM.setEditable(false);
        maturityDateVM.setFont(new java.awt.Font("Segoe UI Semibold", 0, 13)); // NOI18N
        maturityDateVM.setForeground(new java.awt.Color(51, 51, 51));
        maturityDateVM.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                maturityDateVMActionPerformed(evt);
            }
        });

        transactionDateVM.setEditable(false);
        transactionDateVM.setFont(new java.awt.Font("Segoe UI Semibold", 0, 13)); // NOI18N
        transactionDateVM.setForeground(new java.awt.Color(51, 51, 51));
        transactionDateVM.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                transactionDateVMActionPerformed(evt);
            }
        });

        expiryDateVM.setEditable(false);
        expiryDateVM.setFont(new java.awt.Font("Segoe UI Semibold", 0, 13)); // NOI18N
        expiryDateVM.setForeground(new java.awt.Color(51, 51, 51));
        expiryDateVM.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                expiryDateVMActionPerformed(evt);
            }
        });

        principalVM.setEditable(false);
        principalVM.setFont(new java.awt.Font("Segoe UI Semibold", 0, 13)); // NOI18N
        principalVM.setForeground(new java.awt.Color(51, 51, 51));
        principalVM.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                principalVMActionPerformed(evt);
            }
        });

        interestVM.setEditable(false);
        interestVM.setFont(new java.awt.Font("Segoe UI Semibold", 0, 13)); // NOI18N
        interestVM.setForeground(new java.awt.Color(51, 51, 51));
        interestVM.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                interestVMActionPerformed(evt);
            }
        });

        serviceChargeVM.setEditable(false);
        serviceChargeVM.setFont(new java.awt.Font("Segoe UI Semibold", 0, 13)); // NOI18N
        serviceChargeVM.setForeground(new java.awt.Color(51, 51, 51));
        serviceChargeVM.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                serviceChargeVMActionPerformed(evt);
            }
        });

        jLabel36.setForeground(new java.awt.Color(102, 102, 102));
        jLabel36.setText("Insurance");

        insuranceVM.setEditable(false);
        insuranceVM.setFont(new java.awt.Font("Segoe UI Semibold", 0, 13)); // NOI18N
        insuranceVM.setForeground(new java.awt.Color(51, 51, 51));
        insuranceVM.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                insuranceVMActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(25, 25, 25)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(clientNameVM)
                    .addComponent(jScrollPane2)
                    .addComponent(jScrollPane1)
                    .addComponent(jScrollPane3)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(branchVM, javax.swing.GroupLayout.DEFAULT_SIZE, 149, Short.MAX_VALUE)
                            .addComponent(jLabel30, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel16)
                            .addComponent(processedByVM)))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addComponent(jLabel35, javax.swing.GroupLayout.PREFERRED_SIZE, 148, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel15, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addComponent(itemClassificationVM, javax.swing.GroupLayout.PREFERRED_SIZE, 148, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(statusVM))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel9)
                            .addComponent(jLabel10)
                            .addComponent(jLabel14)
                            .addComponent(jLabel18)
                            .addGroup(jPanel2Layout.createSequentialGroup()
                                .addComponent(jLabel36, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jLabel26, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(jPanel2Layout.createSequentialGroup()
                                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(papeletaNoVM, javax.swing.GroupLayout.DEFAULT_SIZE, 100, Short.MAX_VALUE)
                                    .addComponent(jLabel6, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addComponent(oldPapNoVM, javax.swing.GroupLayout.DEFAULT_SIZE, 100, Short.MAX_VALUE)
                                    .addComponent(jLabel8, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel13, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(newPapNoVM, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)))
                            .addGroup(jPanel2Layout.createSequentialGroup()
                                .addComponent(transactionDateVM, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(maturityDateVM, javax.swing.GroupLayout.DEFAULT_SIZE, 100, Short.MAX_VALUE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(expiryDateVM, javax.swing.GroupLayout.DEFAULT_SIZE, 100, Short.MAX_VALUE))
                            .addGroup(jPanel2Layout.createSequentialGroup()
                                .addComponent(jLabel17, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jLabel23, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jLabel20, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(jPanel2Layout.createSequentialGroup()
                                .addComponent(jLabel19, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jLabel24, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jLabel25, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(jPanel2Layout.createSequentialGroup()
                                .addComponent(principalVM, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(interestVM, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(serviceChargeVM, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(jPanel2Layout.createSequentialGroup()
                                .addComponent(insuranceVM, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(netProceedsVM, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addGap(0, 22, Short.MAX_VALUE))
        );

        jPanel2Layout.linkSize(javax.swing.SwingConstants.HORIZONTAL, new java.awt.Component[] {oldPapNoVM, papeletaNoVM});

        jPanel2Layout.linkSize(javax.swing.SwingConstants.HORIZONTAL, new java.awt.Component[] {expiryDateVM, maturityDateVM});

        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(24, 24, 24)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel6)
                    .addComponent(jLabel13)
                    .addComponent(jLabel8))
                .addGap(1, 1, 1)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(papeletaNoVM)
                    .addComponent(oldPapNoVM, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(newPapNoVM, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel30)
                    .addComponent(jLabel16))
                .addGap(1, 1, 1)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(branchVM, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(processedByVM, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel9)
                .addGap(1, 1, 1)
                .addComponent(clientNameVM, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel10)
                .addGap(1, 1, 1)
                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel14)
                .addGap(1, 1, 1)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel18)
                .addGap(1, 1, 1)
                .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel35)
                    .addComponent(jLabel15))
                .addGap(1, 1, 1)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(itemClassificationVM, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(statusVM, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel17)
                    .addComponent(jLabel23)
                    .addComponent(jLabel20))
                .addGap(1, 1, 1)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(transactionDateVM, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(maturityDateVM, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(expiryDateVM, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel19)
                    .addComponent(jLabel24)
                    .addComponent(jLabel25))
                .addGap(1, 1, 1)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(principalVM, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(interestVM, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(serviceChargeVM, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel36)
                    .addComponent(jLabel26))
                .addGap(1, 1, 1)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(insuranceVM, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(netProceedsVM, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(19, 19, 19))
        );

        jPanel2Layout.linkSize(javax.swing.SwingConstants.VERTICAL, new java.awt.Component[] {newPapNoVM, oldPapNoVM, papeletaNoVM});

        jPanel2Layout.linkSize(javax.swing.SwingConstants.VERTICAL, new java.awt.Component[] {expiryDateVM, maturityDateVM, transactionDateVM});

        jPanel2Layout.linkSize(javax.swing.SwingConstants.VERTICAL, new java.awt.Component[] {itemClassificationVM, statusVM});

        customerListingTable.setBackground(new java.awt.Color(240, 240, 240));
        customerListingTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null}
            },
            new String [] {
                "Pap No", "Branch", "Client Name", "Item Description", "Date Loan"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        customerListingTable.setFillsViewportHeight(true);
        customerListingTable.setGridColor(new java.awt.Color(240, 240, 240));
        customerListingTable.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                customerListingTableMouseClicked(evt);
            }
        });
        customerListingTable.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                customerListingTableKeyPressed(evt);
            }
        });
        jScrollPane4.setViewportView(customerListingTable);
        if (customerListingTable.getColumnModel().getColumnCount() > 0) {
            customerListingTable.getColumnModel().getColumn(0).setPreferredWidth(30);
            customerListingTable.getColumnModel().getColumn(1).setPreferredWidth(50);
            customerListingTable.getColumnModel().getColumn(2).setPreferredWidth(100);
            customerListingTable.getColumnModel().getColumn(3).setPreferredWidth(170);
            customerListingTable.getColumnModel().getColumn(4).setPreferredWidth(60);
        }

        customerListingTable1.setBackground(new java.awt.Color(240, 240, 240));
        customerListingTable1.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null}
            },
            new String [] {
                "#", "Pap No", "Client Name", "Branch", "Item Description", "Date Loan"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        customerListingTable1.setFillsViewportHeight(true);
        customerListingTable1.setGridColor(new java.awt.Color(240, 240, 240));
        customerListingTable1.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                customerListingTableMouseClicked(evt);
            }
        });
        customerListingTable1.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                customerListingTableKeyPressed(evt);
            }
        });
        jScrollPane6.setViewportView(customerListingTable1);
        if (customerListingTable1.getColumnModel().getColumnCount() > 0) {
            customerListingTable1.getColumnModel().getColumn(0).setPreferredWidth(5);
            customerListingTable1.getColumnModel().getColumn(1).setPreferredWidth(40);
            customerListingTable1.getColumnModel().getColumn(2).setPreferredWidth(70);
            customerListingTable1.getColumnModel().getColumn(3).setPreferredWidth(60);
            customerListingTable1.getColumnModel().getColumn(4).setPreferredWidth(120);
            customerListingTable1.getColumnModel().getColumn(5).setPreferredWidth(60);
        }

        jPanel3.setBackground(new java.awt.Color(227, 240, 251));
        jPanel3.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Segoe UI", 0, 12), new java.awt.Color(227, 240, 251))); // NOI18N

        viewHistory.setBackground(new java.awt.Color(79, 119, 141));
        viewHistory.setFont(new java.awt.Font("Segoe UI Semibold", 0, 13)); // NOI18N
        viewHistory.setForeground(new java.awt.Color(255, 255, 255));
        viewHistory.setText("View History");
        viewHistory.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                viewHistoryActionPerformed(evt);
            }
        });

        newLoanBtn.setFont(new java.awt.Font("Segoe UI Semibold", 0, 13)); // NOI18N
        newLoanBtn.setForeground(new java.awt.Color(79, 119, 141));
        newLoanBtn.setText("New Loan");
        newLoanBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                newLoanBtnActionPerformed(evt);
            }
        });

        renewLoanBtn.setFont(new java.awt.Font("Segoe UI Semibold", 0, 13)); // NOI18N
        renewLoanBtn.setForeground(new java.awt.Color(79, 119, 141));
        renewLoanBtn.setText("Renew");
        renewLoanBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                renewLoanBtnActionPerformed(evt);
            }
        });

        redeemLoanBtn.setFont(new java.awt.Font("Segoe UI Semibold", 0, 13)); // NOI18N
        redeemLoanBtn.setForeground(new java.awt.Color(79, 119, 141));
        redeemLoanBtn.setText("Redeem");
        redeemLoanBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                redeemLoanBtnActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addGap(20, 20, 20)
                .addComponent(viewHistory, javax.swing.GroupLayout.PREFERRED_SIZE, 136, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(newLoanBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 120, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(renewLoanBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(redeemLoanBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(20, Short.MAX_VALUE))
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addGap(15, 15, 15)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(redeemLoanBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(renewLoanBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(newLoanBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(viewHistory, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(12, Short.MAX_VALUE))
        );

        jPanel1.setBackground(new java.awt.Color(255, 255, 255));

        jLabel4.setForeground(new java.awt.Color(102, 102, 102));
        jLabel4.setText("Filters:");

        jLabel3.setForeground(new java.awt.Color(102, 102, 102));
        jLabel3.setText("Branch");

        branchSearch.setFont(new java.awt.Font("Segoe UI Semibold", 0, 13)); // NOI18N
        branchSearch.setForeground(new java.awt.Color(51, 51, 51));
        branchSearch.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));

        jLabel7.setForeground(new java.awt.Color(102, 102, 102));
        jLabel7.setText("Item Type:");

        classificationSearch.setFont(new java.awt.Font("Segoe UI Semibold", 0, 13)); // NOI18N
        classificationSearch.setForeground(new java.awt.Color(51, 51, 51));
        classificationSearch.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "All", "Vehicle - 2W", "Vehicle - 4W", "Jewelry", "Appliance", "Gadget", "Others" }));

        jLabel11.setForeground(new java.awt.Color(102, 102, 102));
        jLabel11.setText("Status");

        classificationSearch1.setFont(new java.awt.Font("Segoe UI Semibold", 0, 13)); // NOI18N
        classificationSearch1.setForeground(new java.awt.Color(51, 51, 51));
        classificationSearch1.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "All", "Vehicle - 2W", "Vehicle - 4W", "Jewelry", "Appliance", "Gadget", "Others" }));

        jLabel12.setForeground(new java.awt.Color(102, 102, 102));
        jLabel12.setText("Month");

        classificationSearch2.setFont(new java.awt.Font("Segoe UI Semibold", 0, 13)); // NOI18N
        classificationSearch2.setForeground(new java.awt.Color(51, 51, 51));
        classificationSearch2.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "All", "Vehicle - 2W", "Vehicle - 4W", "Jewelry", "Appliance", "Gadget", "Others" }));

        jLabel21.setForeground(new java.awt.Color(102, 102, 102));
        jLabel21.setText("Year");

        classificationSearch3.setFont(new java.awt.Font("Segoe UI Semibold", 0, 13)); // NOI18N
        classificationSearch3.setForeground(new java.awt.Color(51, 51, 51));
        classificationSearch3.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "All", "Vehicle - 2W", "Vehicle - 4W", "Jewelry", "Appliance", "Gadget", "Others" }));

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(32, 32, 32)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                .addComponent(jLabel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(branchSearch, 0, 171, Short.MAX_VALUE))
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addComponent(jLabel12, javax.swing.GroupLayout.DEFAULT_SIZE, 83, Short.MAX_VALUE)
                                    .addComponent(classificationSearch2, 0, 0, Short.MAX_VALUE))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                    .addComponent(jLabel21, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(classificationSearch3, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.PREFERRED_SIZE, 77, javax.swing.GroupLayout.PREFERRED_SIZE))))
                        .addGap(12, 12, 12)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel7)
                            .addComponent(classificationSearch, javax.swing.GroupLayout.PREFERRED_SIZE, 128, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel11)
                            .addComponent(classificationSearch1, javax.swing.GroupLayout.PREFERRED_SIZE, 128, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(17, 17, 17)
                        .addComponent(jLabel4)))
                .addContainerGap(17, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel4)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(classificationSearch3, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel3)
                            .addComponent(jLabel7))
                        .addGap(1, 1, 1)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(branchSearch, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(classificationSearch, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addComponent(jLabel11)
                                .addGap(1, 1, 1)
                                .addComponent(classificationSearch1, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(jLabel12)
                                    .addComponent(jLabel21))
                                .addGap(1, 1, 1)
                                .addComponent(classificationSearch2, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)))))
                .addContainerGap(14, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(searchPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jScrollPane4)
                    .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addComponent(searchPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jScrollPane4)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jScrollPane5.setViewportView(jPanel4);

        add(jScrollPane5, java.awt.BorderLayout.CENTER);
    }// </editor-fold>//GEN-END:initComponents

    private void insuranceVMActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_insuranceVMActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_insuranceVMActionPerformed

    private void serviceChargeVMActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_serviceChargeVMActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_serviceChargeVMActionPerformed

    private void interestVMActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_interestVMActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_interestVMActionPerformed

    private void principalVMActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_principalVMActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_principalVMActionPerformed

    private void expiryDateVMActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_expiryDateVMActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_expiryDateVMActionPerformed

    private void transactionDateVMActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_transactionDateVMActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_transactionDateVMActionPerformed

    private void maturityDateVMActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_maturityDateVMActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_maturityDateVMActionPerformed

    private void branchVMActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_branchVMActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_branchVMActionPerformed

    private void statusVMActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_statusVMActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_statusVMActionPerformed

    private void newPapNoVMActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_newPapNoVMActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_newPapNoVMActionPerformed

    private void oldPapNoVMActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_oldPapNoVMActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_oldPapNoVMActionPerformed

    private void tempSearchNameKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_tempSearchNameKeyPressed
        int key = evt.getKeyCode();
/*  790 */     if (key == 10) {
/*  791 */       this.tempSearchPapNum.requestFocusInWindow();
/*  792 */       this.tempSearchPapNum.selectAll();
  } 
    }//GEN-LAST:event_tempSearchNameKeyPressed

    private void tempSearchPapNumKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_tempSearchPapNumKeyPressed
        int key = evt.getKeyCode();
/*  798 */     if (key == 10) {
/*  799 */       searchLoanActionPerformed((ActionEvent)null);
  }
    }//GEN-LAST:event_tempSearchPapNumKeyPressed

    private void searchLoanActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_searchLoanActionPerformed
        updateStatusOfLoans();
 
 
  
     String searchSubstring = "";
     String searchSubstring1 = "";
     String searchSubstring2 = "";
     String query = "select pap_num, branch, client_name, item_description, transaction_date from merlininventorydatabase.client_info ";
     if (this.tempSearchName.getText().isEmpty() && this.tempSearchPapNum.getText().isEmpty()) {
       JOptionPane.showMessageDialog(null, "Please enter a 'Name' or 'Papeleta Number' in the Search field.", "Search Option", 1);
        } else {
             if (this.branchSearch.getSelectedIndex() > 0) {
               searchSubstring1 = " and branch = '" + this.branchSearch.getSelectedItem().toString() + "'";
          }
             if (this.classificationSearch.getSelectedIndex() > 0) {
               searchSubstring2 = " and classification = '" + this.classificationSearch.getSelectedItem().toString() + "'";
          }
             if (!this.tempSearchName.getText().isEmpty() && !this.tempSearchPapNum.getText().isEmpty()) {
               searchSubstring = " where client_name like '%" + this.tempSearchName.getText() + "%' or pap_num = '" + this.tempSearchPapNum.getText() + "'";
               query = query.concat(this.whereSearchPawn);
             } else if (!this.tempSearchName.getText().isEmpty()) {
               searchSubstring = " where client_name like '%" + this.tempSearchName.getText() + "%'";
               query = query.concat(this.whereSearchPawn);
             } else if (!this.tempSearchPapNum.getText().isEmpty()) {
               searchSubstring = " where pap_num = '" + this.tempSearchPapNum.getText() + "'";
               query = query.concat(this.whereSearchPawn);
          } 

          try {
               updateTable(query + searchSubstring + searchSubstring1 + searchSubstring2 + " order by transaction_date desc");
             } catch (SQLException ex) {
               this.con.saveProp("mpis_last_error", String.valueOf(ex));
               Logger.getLogger(Search.class.getName()).log(Level.SEVERE, (String)null, ex);
          } 
        } 
    }//GEN-LAST:event_searchLoanActionPerformed

    private void tempSearchPapNumKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_tempSearchPapNumKeyTyped
        if (!Character.isDigit(evt.getKeyChar())) {
/*  848 */       evt.consume();
  }
    }//GEN-LAST:event_tempSearchPapNumKeyTyped

    private void clearSelectionActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_clearSelectionActionPerformed
        this.tempSearchName.setText("");
/*  861 */     this.tempSearchPapNum.setText("");
    }//GEN-LAST:event_clearSelectionActionPerformed

    private void customerListingTableMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_customerListingTableMouseClicked
        this.viewSangla.setPap_num(this.customerListingTable.getValueAt(this.customerListingTable.getSelectedRow(), 1).toString());
        this.viewSangla.setBranch(this.customerListingTable.getValueAt(this.customerListingTable.getSelectedRow(), 2).toString());
        this.viewSangla.setItemCodes();
        this.viewSangla.retrieveSangla(this.viewSangla.getItem_code_a());
        this.papeletaNoVM.setText(this.viewSangla.getPap_num());
        this.oldPapNoVM.setText(this.viewSangla.getOld_pap_num());
        this.newPapNoVM.setText(this.viewSangla.getNew_pap_num());
        this.clientNameVM.setText(this.viewSangla.getClient_name());
        this.clientAddressVM.setText(this.viewSangla.getClient_address());
        this.itemDescVM.setText(this.viewSangla.getItem_description());
        this.branchVM.setText(this.viewSangla.getBranch());
        this.itemClassificationVM.setText(this.viewSangla.getClassification());
        this.processedByVM.setText(this.viewSangla.getProcessed_by());
        this.transactionDateVM.setText(this.viewSangla.getTransaction_date());
        this.maturityDateVM.setText(this.viewSangla.getMaturity_date());
        this.expiryDateVM.setText(this.viewSangla.getExpiration_date());
        this.principalVM.setText(this.decHelp.FormatNumber(this.viewSangla.getPrincipal()));
        this.interestVM.setText(this.decHelp.FormatNumber(this.viewSangla.getAdvance_interest()));
        this.serviceChargeVM.setText(this.decHelp.FormatNumber(this.viewSangla.getService_charge()));
        this.insuranceVM.setText(this.decHelp.FormatNumber(this.viewSangla.getInsurance()));
        this.netProceedsVM.setText(this.decHelp.FormatNumber(this.viewSangla.getNet_proceeds()));
        this.statusVM.setText(this.viewSangla.getStatus());
        this.remarksVM.setText(this.viewSangla.getRemarks());
//        System.out.println(this.viewSangla.getStatus());
        setConstant1(this.viewSangla.getClient_name());
        setConstant2(this.viewSangla.getClient_address());
        setConstant3(this.viewSangla.getContact_no());
        setConstant4(this.viewSangla.getItem_description());
        setConstant5(this.viewSangla.getClassification());
        
        
    }//GEN-LAST:event_customerListingTableMouseClicked

    private void viewHistoryActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_viewHistoryActionPerformed
        String constantQuery = "Select client_name, client_address, contact_no, item_description, classification from merlininventorydatabase.client_info where item_code_a = '" + this.viewSangla.getItem_code_a() + "'";
    String[] constants = new String[5];
    HistoryTableModel modelTable = new HistoryTableModel();
    try {
      Connection connect = DriverManager.getConnection(this.driver, this.f_user, this.f_pass);
      Statement query1 = connect.createStatement();
      ResultSet rSet = query1.executeQuery(constantQuery);
      while (rSet.next()) {
        constants[0] = rSet.getString("client_name");
        constants[1] = rSet.getString("client_address");
        constants[2] = rSet.getString("contact_no");
        constants[3] = rSet.getString("item_description");
        constants[4] = rSet.getString("classification");
        setConstant1(rSet.getString("client_name"));
        setConstant2(rSet.getString("client_address"));
        setConstant3(rSet.getString("contact_no"));
        setConstant4(rSet.getString("item_description"));
        setConstant5(rSet.getString("classification"));
      } 
      String historyTableQuery = "Select a.pap_num, branch, status, transaction_date, principal, ai, sc, insurance from merlininventorydatabase.client_info a inner join merlininventorydatabase.empeno b on item_code_a = b.pap_num where client_name = '" + getConstant1() + "' and client_address = '" + getConstant2() + "' and contact_no = '" + getConstant3() + "' and item_description = '" + getConstant4() + "' and classification = '" + getConstant5() + "' order by transaction_date desc";
      query1.close();
      rSet.close();
      modelTable.setColumnIdentifiers((Object[])this.listGen.getColumnHeaders(historyTableQuery));
      Statement query2 = connect.createStatement();
      ResultSet rSet2 = query2.executeQuery(historyTableQuery);
      ResultSetMetaData rSet2MetaData = rSet2.getMetaData();
      int columnCount = rSet2MetaData.getColumnCount();
      Object[] row = new Object[columnCount];
      while (rSet2.next()) {
        for (int j = 0; j < columnCount; j++)
          row[j] = rSet2.getObject(j + 1); 
        modelTable.addRow(row);
      } 
      connect.close();
      query2.close();
      rSet2.close();
    } catch (SQLException ex) {
      this.con.saveProp("mpis_last_error", String.valueOf(ex));
      System.out.println("sql error on constant query in view history mode");
    } 
    try {
      ViewHistoryMode viewHist = new ViewHistoryMode(null, true, constants, modelTable);
      viewHist.setVisible(true);
    } catch (SQLException ex) {
      this.con.saveProp("mpis_last_error", String.valueOf(ex));
      System.out.println("error on sql during creation of view history mode dialog");
    } 
    }//GEN-LAST:event_viewHistoryActionPerformed

    private void newLoanBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_newLoanBtnActionPerformed
        setNewLoanActivated(true);
        setRenewLoanActivated(false);
        setRedeemLoanActivated(false);
        setVisible(false);
    }//GEN-LAST:event_newLoanBtnActionPerformed

    private void renewLoanBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_renewLoanBtnActionPerformed
        Sangla pickedLoan = new Sangla();
     pickedLoan.setPap_num(this.customerListingTable.getValueAt(this.customerListingTable.getSelectedRow(), 1).toString());
     pickedLoan.setBranch(this.customerListingTable.getValueAt(this.customerListingTable.getSelectedRow(), 2).toString());
     pickedLoan.retrieveSangla(this.customerListingTable.getValueAt(this.customerListingTable.getSelectedRow(), 1).toString().concat(pickedLoan.getBranchCode(this.customerListingTable.getValueAt(this.customerListingTable.getSelectedRow(), 2).toString())));
  
     if (!pickedLoan.getStatus().equalsIgnoreCase("Open") && !pickedLoan.getStatus().equalsIgnoreCase("Expired")) {
       JOptionPane.showMessageDialog(null, "Unable to renew this loan. Please check the current status of this loan.", "Loan Renewal", 1);
  }
  else {
    
       setRenewPapNo(this.customerListingTable.getValueAt(this.customerListingTable.getSelectedRow(), 1).toString());
       setRenewBranch(this.customerListingTable.getValueAt(this.customerListingTable.getSelectedRow(), 2).toString());
       setRenewLoanActivated(true);
       setNewLoanActivated(false);
       setRedeemLoanActivated(false);
       setVisible(false);
  } 
    }//GEN-LAST:event_renewLoanBtnActionPerformed

    private void redeemLoanBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_redeemLoanBtnActionPerformed

        Sangla pickedLoan = new Sangla();
        pickedLoan.setPap_num(this.customerListingTable.getValueAt(this.customerListingTable.getSelectedRow(), 1).toString());
        pickedLoan.setBranch(this.customerListingTable.getValueAt(this.customerListingTable.getSelectedRow(), 2).toString());
        pickedLoan.retrieveSangla(this.customerListingTable.getValueAt(this.customerListingTable.getSelectedRow(), 1).toString().concat(pickedLoan.getBranchCode(this.customerListingTable.getValueAt(this.customerListingTable.getSelectedRow(), 2).toString())));
        if (!pickedLoan.getStatus().equalsIgnoreCase("Open") && !pickedLoan.getStatus().equalsIgnoreCase("Expired")) {
          JOptionPane.showMessageDialog(null, "Unable to redeem this loan. Please check the current status of this loan.", "Loan Renewal", 1);
        } else {
          setRenewPapNo(this.customerListingTable.getValueAt(this.customerListingTable.getSelectedRow(), 1).toString());
          setRenewBranch(this.customerListingTable.getValueAt(this.customerListingTable.getSelectedRow(), 2).toString());
          setRedeemLoanActivated(true);
          setNewLoanActivated(false);
          setRenewLoanActivated(false);
          setVisible(false);
        } 
    }//GEN-LAST:event_redeemLoanBtnActionPerformed

    private void customerListingTableKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_customerListingTableKeyPressed
        
        if (evt.getKeyCode() == KeyEvent.VK_UP || evt.getKeyCode() == KeyEvent.VK_DOWN ) {
            customerListingTableMouseClicked(null);
        }
    }//GEN-LAST:event_customerListingTableKeyPressed

    private void statusVMPropertyChange(java.beans.PropertyChangeEvent evt) {//GEN-FIRST:event_statusVMPropertyChange
//        if (!statusVM.getText().equalsIgnoreCase("Open") || !statusVM.getText().equalsIgnoreCase("Expired")) {
//            renewLoanBtn.setEnabled(false);
//            redeemLoanBtn.setEnabled(false);
//            newLoanBtn.setEnabled(true);
//        } else {
//            renewLoanBtn.setEnabled(true);
//            redeemLoanBtn.setEnabled(true);
//            newLoanBtn.setEnabled(false);
//        }
    }//GEN-LAST:event_statusVMPropertyChange


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JComboBox<String> branchSearch;
    private javax.swing.JTextField branchVM;
    private javax.swing.JComboBox<String> classificationSearch;
    private javax.swing.JComboBox<String> classificationSearch1;
    private javax.swing.JComboBox<String> classificationSearch2;
    private javax.swing.JComboBox<String> classificationSearch3;
    private javax.swing.JButton clearSelection;
    private javax.swing.JTextArea clientAddressVM;
    private javax.swing.JTextField clientNameVM;
    private javax.swing.JTable customerListingTable;
    private javax.swing.JTable customerListingTable1;
    private javax.swing.JTextField expiryDateVM;
    private javax.swing.JTextField insuranceVM;
    private javax.swing.JTextField interestVM;
    private javax.swing.JTextField itemClassificationVM;
    private javax.swing.JTextArea itemDescVM;
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
    private javax.swing.JLabel jLabel23;
    private javax.swing.JLabel jLabel24;
    private javax.swing.JLabel jLabel25;
    private javax.swing.JLabel jLabel26;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel30;
    private javax.swing.JLabel jLabel35;
    private javax.swing.JLabel jLabel36;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JScrollPane jScrollPane4;
    private javax.swing.JScrollPane jScrollPane5;
    private javax.swing.JScrollPane jScrollPane6;
    private javax.swing.JTextField maturityDateVM;
    private javax.swing.JTextField netProceedsVM;
    private javax.swing.JButton newLoanBtn;
    private javax.swing.JTextField newPapNoVM;
    private javax.swing.JTextField oldPapNoVM;
    private javax.swing.JTextField papeletaNoVM;
    private javax.swing.JTextField principalVM;
    private javax.swing.JTextField processedByVM;
    private javax.swing.JButton redeemLoanBtn;
    private javax.swing.JTextArea remarksVM;
    private javax.swing.JButton renewLoanBtn;
    private javax.swing.JButton searchLoan;
    private javax.swing.JPanel searchPanel;
    private javax.swing.JTextField serviceChargeVM;
    private javax.swing.JTextField statusVM;
    private javax.swing.JTextField tempSearchName;
    private javax.swing.JTextField tempSearchPapNum;
    private javax.swing.JTextField transactionDateVM;
    private javax.swing.JButton viewHistory;
    // End of variables declaration//GEN-END:variables
}
