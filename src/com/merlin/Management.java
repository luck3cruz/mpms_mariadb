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
import java.util.logging.Level;
import java.util.logging.Logger;
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
public class Management extends javax.swing.JPanel {

    /**
     * @return the fileNameSave
     */
    public String getFileNameSave() {
        return fileNameSave;
    }

    /**
     * @param fileNameSave the fileNameSave to set
     */
    public void setFileNameSave(String fileNameSave) {
        this.fileNameSave = fileNameSave;
    }

    /**
     * @return the fileNameOpen
     */
    public String getFileNameOpen() {
        return fileNameOpen;
    }

    /**
     * @param fileNameOpen the fileNameOpen to set
     */
    public void setFileNameOpen(String fileNameOpen) {
        this.fileNameOpen = fileNameOpen;
    }

    /**
     * @return the editMode
     */
    public boolean isEditMode() {
        return editMode;
    }

    /**
     * @param editMode the editMode to set
     */
    public void setEditMode(boolean editMode) {
        this.editMode = editMode;
    }

    /**
     * @return the oldPW
     */
    public String getOldPW() {
        return oldPW;
    }

    /**
     * @param oldPW the oldPW to set
     */
    public void setOldPW(String oldPW) {
        this.oldPW = oldPW;
    }

    /**
     * Creates new form Management
     */
    public Management() {
        initComponents();
        disableTB();
        chooseFile.setVisible(false);
    }
    
    Config con = new Config();
    private final String driver = "jdbc:mariadb://" + this.con.getProp("IP") + ":" + this.con.getProp("port") + "/merlininventorydatabase";
    private final String username = this.con.getProp("username");
    private final String password = this.con.getProp("password");
    private DatabaseUpdater dbu = new DatabaseUpdater();
    private boolean editMode = false;
    private DefaultTableModel defTable;
    private String oldPW = "";
    private String fileNameSave = "";
    private String fileNameOpen = "";

    public void updateCombo() {
        try {
/*   68 */       this.defaultBranch.removeAllItems();
/*   69 */       Connection connect = DriverManager.getConnection(this.driver, this.username, this.password);
/*   70 */       Statement state = connect.createStatement();
/*   71 */       String query = "Select branch_name from merlininventorydatabase.branch_info";
/*   72 */       System.out.println(query);
/*   73 */       ResultSet rset = state.executeQuery(query);
/*   74 */       while (rset.next()) {
/*   75 */         this.defaultBranch.addItem(rset.getString(1));
       }
/*   77 */       state.close();
/*   78 */       connect.close();
/*   79 */       this.defaultBranch.setSelectedItem(this.con.getProp("branch"));
/*   80 */     } catch (SQLException ex) {
/*   81 */       this.con.saveProp("mpis_last_error", String.valueOf(ex));
/*   82 */       Logger.getLogger(Additionals.class.getName()).log(Level.SEVERE, (String)null, ex);
     } 
    }
    
    public boolean backupDB(String dbName, String dbUserName, String dbPassword, String path) {
/*   87 */     File f1 = new File("C:\\Program Files\\MariaDB 10.0");
/*   88 */     File f2 = new File("C:\\Program Files (x86)\\MariaDB 10.0");
/*   89 */     String executeCmd = "";
/*   90 */     if (f1.exists()) {
/*   91 */       executeCmd = "C:\\Program Files\\MariaDB 10.0\\bin\\mysqldump -u " + dbUserName + " -p" + dbPassword + " --add-drop-database -B " + dbName + " cashtransactions -r \"" + path + "\"";
     }
/*   93 */     else if (f2.exists()) {
/*   94 */       executeCmd = "C:\\Program Files (x86)\\MariaDB 10.0\\bin\\mysqldump -u " + dbUserName + " -p" + dbPassword + " --add-drop-database -B " + dbName + " cashtransactions -r \"" + path + "\"";
     } 
     
/*   97 */     System.out.println(executeCmd);
 
     
     try {
/*  101 */       Process runtimeProcess = Runtime.getRuntime().exec(executeCmd);
/*  102 */       int processComplete = runtimeProcess.waitFor();
       
/*  104 */       if (processComplete == 0) {
/*  105 */         System.out.println("Backup created successfully");
/*  106 */         return true;
       } 
/*  108 */       System.out.println("Could not create the backup");
     }
/*  110 */     catch (Exception ex) {
/*  111 */       this.con.saveProp("mpis_last_error", String.valueOf(ex));
///*  112 */       Logger.getLogger(Management.class.getName()).log(Level.SEVERE, (String)null, ex);
     } 
     
/*  115 */     return false;
   }
    
    public boolean restoreDB(String dbUserName, String dbPassword, String source) {
/*  119 */     File f1 = new File("C:\\Program Files\\MariaDB 10.0");
/*  120 */     File f2 = new File("C:\\Program Files (x86)\\MariaDB 10.0");
/*  121 */     String[] executeCmd = null;
/*  122 */     if (f1.exists()) {
/*  123 */       executeCmd = new String[] { "C:\\Program Files\\MariaDB 10.0\\bin\\mysql", "--user=" + dbUserName, "--password=" + dbPassword, "-e", "source " + source };
/*  124 */     } else if (f2.exists()) {
/*  125 */       executeCmd = new String[] { "C:\\Program Files (x86)\\MariaDB 10.0\\bin\\mysql", "--user=" + dbUserName, "--password=" + dbPassword, "-e", "source " + source };
     } 
 
 
     
     try {
/*  131 */       Process runtimeProcess = Runtime.getRuntime().exec(executeCmd);
/*  132 */       int processComplete = runtimeProcess.waitFor();
       
/*  134 */       while (processComplete != 0) {
/*  135 */         System.out.println(processComplete);
       }
/*  137 */       if (processComplete == 0) {
/*  138 */         System.out.println("Backup restored successfully");
/*  139 */         return true;
       } 
/*  141 */       System.out.println("Could not restore the backup");
     }
/*  143 */     catch (Exception ex) {
/*  144 */       this.con.saveProp("mpis_last_error", String.valueOf(ex));
     } 
     
/*  147 */     return false;
   }
    
    public void createEmployeeTable() throws SQLException {
/*  151 */     Connection conn = DriverManager.getConnection(this.driver, this.username, this.password);
/*  152 */     Statement state = conn.createStatement();
/*  153 */     String create_query = "CREATE TABLE IF NOT EXISTS `use_pass` (`username` varchar(9) NOT NULL, `first_name` varchar(25) DEFAULT NULL, `last_name` varchar(25) DEFAULT NULL, `password` varchar(12) DEFAULT NULL, `user_level` int(11) DEFAULT NULL, `initials` varchar(4) DEFAULT NULL, PRIMARY KEY (`username`));";
/*  160 */     state.executeUpdate("USE `merlininventorydatabase`;");
/*  161 */     state.executeUpdate(create_query);
/*  162 */     state.close();
/*  163 */     conn.close();
   }
    
    public void updateBranchTable() throws SQLException {
        this.defTable = (DefaultTableModel)this.bchTable.getModel();
/*  233 */     while (this.defTable.getRowCount() > 0) {
/*  234 */       this.defTable.removeRow(0);
     }
/*  236 */     Object[] row = new Object[2];
     
/*  238 */     Connection connect = DriverManager.getConnection(this.driver, this.username, this.password);
/*  239 */     Statement state = connect.createStatement();
/*  240 */     String query = "Select branch_name, branch_code from branch_info";
/*  241 */     ResultSet rset = state.executeQuery(query);
/*  242 */     while (rset.next()) {
/*  243 */       for (int i = 0; i < row.length; i++) {
/*  244 */         row[i] = rset.getString(i + 1);
       }
/*  246 */       this.defTable.addRow(row);
     } 
/*  248 */     this.bchTable.setModel(this.defTable);
     
/*  250 */     TableCellRenderer renderer = new TableCellRenderer()
       {
/*  252 */         JLabel label = new JLabel();
 
 
 
         
         public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
/*  258 */           this.label.setOpaque(true);
/*  259 */           this.label.setText("   " + value);
///*  260 */           Color alternate = UIManager.getColor("Table.alternateRowColor");
Color alternate = new Color(239,246,250);
/*  261 */           if (isSelected) {
/*  262 */             this.label.setBackground(Color.DARK_GRAY);
/*  263 */             this.label.setForeground(Color.WHITE);
           } else {
/*  265 */             this.label.setForeground(Color.black);
/*  266 */             if (row % 2 == 1) {
/*  267 */               this.label.setBackground(alternate);
             } else {
/*  269 */               this.label.setBackground(Color.WHITE);
             } 
           } 
           
/*  273 */           this.label.setHorizontalAlignment(2);
           
/*  275 */           return this.label;
         }
       };
     
/*  279 */     this.bchTable.setDefaultRenderer(Object.class, renderer);
     
/*  281 */     ((DefaultTableCellRenderer)this.bchTable.getTableHeader().getDefaultRenderer()).setHorizontalAlignment(0);
/*  282 */     connect.close();
/*  283 */     state.close();
    }
    
    public void updateAddCatTable() throws SQLException {
        this.defTable = (DefaultTableModel)this.addcattable.getModel();
/*  288 */     while (this.defTable.getRowCount() > 0) {
/*  289 */       this.defTable.removeRow(0);
     }
/*  291 */     Object[] row = new Object[1];
     
/*  293 */     Connection connect = DriverManager.getConnection(this.driver, this.username, this.password);
/*  294 */     Statement state = connect.createStatement();
/*  295 */     String query = "Select addtl from merlininventorydatabase.combo_cat where addtl is not null order by addtl asc";
/*  296 */     ResultSet rset = state.executeQuery(query);
/*  297 */     while (rset.next()) {
/*  298 */       row[0] = rset.getString(1);
/*  299 */       this.defTable.addRow(row);
     } 
/*  301 */     this.addcattable.setModel(this.defTable);
     
/*  303 */     TableCellRenderer renderer = new TableCellRenderer()
       {
/*  305 */         JLabel label = new JLabel();
 
 
 
         
         public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
/*  311 */           this.label.setOpaque(true);
/*  312 */           this.label.setText("     " + value);
///*  313 */           Color alternate = UIManager.getColor("Table.alternateRowColor");
Color alternate = new Color(239,246,250);
/*  314 */           if (isSelected) {
/*  315 */             this.label.setBackground(Color.DARK_GRAY);
/*  316 */             this.label.setForeground(Color.WHITE);
           } else {
/*  318 */             this.label.setForeground(Color.black);
/*  319 */             if (row % 2 == 1) {
/*  320 */               this.label.setBackground(alternate);
             } else {
/*  322 */               this.label.setBackground(Color.WHITE);
             } 
           } 
           
/*  326 */           this.label.setHorizontalAlignment(2);
           
/*  328 */           return this.label;
         }
       };
     
/*  332 */     this.addcattable.setDefaultRenderer(Object.class, renderer);
     
/*  334 */     ((DefaultTableCellRenderer)this.addcattable.getTableHeader().getDefaultRenderer()).setHorizontalAlignment(0);
/*  335 */     connect.close();
/*  336 */     state.close();
    }
    
    public void updatePetCatTable() throws SQLException {
        this.defTable = (DefaultTableModel)this.petCatTable.getModel();
/*  341 */     while (this.defTable.getRowCount() > 0) {
/*  342 */       this.defTable.removeRow(0);
     }
/*  344 */     Object[] row = new Object[1];
     
/*  346 */     Connection connect = DriverManager.getConnection(this.driver, this.username, this.password);
/*  347 */     Statement state = connect.createStatement();
/*  348 */     String query = "Select petty from merlininventorydatabase.combo_cat where petty is not null order by petty asc";
/*  349 */     ResultSet rset = state.executeQuery(query);
/*  350 */     while (rset.next()) {
/*  351 */       row[0] = rset.getString(1);
/*  352 */       this.defTable.addRow(row);
     } 
/*  354 */     this.petCatTable.setModel(this.defTable);
     
/*  356 */     TableCellRenderer renderer = new TableCellRenderer()
       {
/*  358 */         JLabel label = new JLabel();
 
 
 
         
         public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
/*  364 */           this.label.setOpaque(true);
/*  365 */           this.label.setText("     " + value);
///*  366 */           Color alternate = UIManager.getColor("Table.alternateRowColor");
Color alternate = new Color(239,246,250);
/*  367 */           if (isSelected) {
/*  368 */             this.label.setBackground(Color.DARK_GRAY);
/*  369 */             this.label.setForeground(Color.WHITE);
           } else {
/*  371 */             this.label.setForeground(Color.black);
/*  372 */             if (row % 2 == 1) {
/*  373 */               this.label.setBackground(alternate);
             } else {
/*  375 */               this.label.setBackground(Color.WHITE);
             } 
           } 
           
/*  379 */           this.label.setHorizontalAlignment(2);
           
/*  381 */           return this.label;
         }
       };
     
/*  385 */     this.petCatTable.setDefaultRenderer(Object.class, renderer);
     
/*  387 */     ((DefaultTableCellRenderer)this.petCatTable.getTableHeader().getDefaultRenderer()).setHorizontalAlignment(0);
/*  388 */     connect.close();
/*  389 */     state.close();
    }
    
    public void updateUserTable() throws SQLException {
        this.defTable = (DefaultTableModel)this.jTable1.getModel();
/*  168 */     while (this.defTable.getRowCount() > 0) {
/*  169 */       this.defTable.removeRow(0);
     }
/*  171 */     Object[] row = new Object[4];
     
/*  173 */     Connection connect = DriverManager.getConnection(this.driver, this.username, this.password);
/*  174 */     Statement state = connect.createStatement();
/*  175 */     String query = "Select username, first_name, last_name, user_level from use_pass";
/*  176 */     ResultSet rset = state.executeQuery(query);
/*  177 */     while (rset.next()) {
/*  178 */       for (int i = 0; i < row.length; i++) {
/*  179 */         if (i < 3) {
/*  180 */           row[i] = rset.getString(i + 1);
         }
/*  182 */         else if (rset.getInt("user_level") == 0) {
/*  183 */           row[i] = "Admin";
/*  184 */         } else if (rset.getInt("user_level") == 1) {
/*  185 */           row[i] = "Clerk";
         } else {
/*  187 */           row[i] = "Others";
         } 
       } 
       
/*  191 */       this.defTable.addRow(row);
     } 
/*  193 */     this.jTable1.setModel(this.defTable);
     
/*  195 */     TableCellRenderer renderer = new TableCellRenderer()
       {
/*  197 */         JLabel label = new JLabel();
 
 
 
         
         public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
/*  203 */           this.label.setOpaque(true);
/*  204 */           this.label.setText("   " + value);
///*  205 */           Color alternate = UIManager.getColor("Table.alternateRowColor
Color alternate = new Color(239,246,250);

/*  206 */           if (isSelected) {
/*  207 */             this.label.setBackground(Color.DARK_GRAY);
/*  208 */             this.label.setForeground(Color.WHITE);
           } else {
/*  210 */             this.label.setForeground(Color.black);
/*  211 */             if (row % 2 == 1) {
/*  212 */               this.label.setBackground(alternate);
             } else {
/*  214 */               this.label.setBackground(Color.WHITE);
             } 
           } 
           
/*  218 */           this.label.setHorizontalAlignment(2);
           
/*  220 */           return this.label;
         }
       };
     
/*  224 */     this.jTable1.setDefaultRenderer(Object.class, renderer);
     
/*  226 */     ((DefaultTableCellRenderer)this.jTable1.getTableHeader().getDefaultRenderer()).setHorizontalAlignment(0);
/*  227 */     connect.close();
/*  228 */     state.close();
    }
    
    private void addAddCat() throws SQLException {
/*  393 */     Connection connect = DriverManager.getConnection(this.driver, this.username, this.password);
/*  394 */     Statement state = connect.createStatement();
/*  395 */     String query = "Insert into merlininventorydatabase.combo_cat (addtl) values ('" + this.addcattype.getText() + "')";
/*  396 */     state.executeUpdate(query);
/*  397 */     state.close();
/*  398 */     connect.close();
   }
    
    
    private void petAddCat() throws SQLException {
/*  402 */     Connection connect = DriverManager.getConnection(this.driver, this.username, this.password);
/*  403 */     Statement state = connect.createStatement();
/*  404 */     String query = "Insert into merlininventorydatabase.combo_cat (petty) values ('" + this.ptyCatType.getText() + "')";
/*  405 */     state.executeUpdate(query);
/*  406 */     state.close();
/*  407 */     connect.close();
   }
    
   private void addUser() throws SQLException {
/*  411 */     Connection connect = DriverManager.getConnection(this.driver, this.username, this.password);
/*  412 */     Statement state = connect.createStatement();
/*  413 */     String query = "";
/*  414 */     if (!isEditMode()) {
/*  415 */       query = "Insert into use_pass (`username`, `first_name`, `last_name`, `password`, `user_level`, `initials`) values ('" + this.jUsername.getText() + "', '" + this.jFirstName.getText() + "', '" + this.jLastName.getText() + "', '" + this.jPassword.getText() + "', " + (this.jComboBox1.getSelectedIndex() + 1) + ", '" + this.jInitial.getText() + "')";
     } else {
       
/*  418 */       query = "Update use_pass set `first_name` = '" + this.jFirstName.getText() + "', `last_name` = '" + this.jLastName.getText() + "', `user_level` = " + this.jComboBox1.getSelectedIndex() + ", `initials` = '" + this.jInitial.getText() + "' where username = '" + this.jUsername.getText() + "'";
     } 
 
 
     
/*  423 */     System.out.println(query);
/*  424 */     state.executeUpdate(query);
/*  425 */     state.close();
/*  426 */     connect.close();
   }
   
   private void disableTB() {
/*  430 */     this.jUsername.setEditable(false);
/*  431 */     this.jFirstName.setEditable(false);
/*  432 */     this.jLastName.setEditable(false);
/*  433 */     this.jInitial.setEditable(false);
/*  434 */     this.jPassword.setEditable(false);
/*  435 */     this.jPassword2.setEditable(false);
/*  436 */     this.jComboBox1.setEnabled(false);
   }
   
   private void enableTB() {
/*  440 */     this.jUsername.setEditable(true);
/*  441 */     this.jFirstName.setEditable(true);
/*  442 */     this.jLastName.setEditable(true);
/*  443 */     this.jInitial.setEditable(true);
/*  444 */     this.jPassword.setEditable(true);
/*  445 */     this.jPassword2.setEditable(true);
/*  446 */     this.jComboBox1.setEnabled(true);
   }
   
   private void clearTB() {
/*  450 */     this.jUsername.setText("");
/*  451 */     this.jFirstName.setText("");
/*  452 */     this.jLastName.setText("");
/*  453 */     this.jInitial.setText("");
/*  454 */     this.jPassword.setText("");
/*  455 */     this.jPassword2.setText("");
/*  456 */     this.jComboBox1.setSelectedIndex(0);
   }
   
   private void retrieveUser() throws SQLException {
/*  460 */     Connection connect = DriverManager.getConnection(this.driver, this.username, this.password);
/*  461 */     Statement state = connect.createStatement();
/*  462 */     String query = "Select * from use_pass where username = '" + this.jTable1.getValueAt(this.jTable1.getSelectedRow(), 0).toString() + "'";
/*  463 */     ResultSet rset = state.executeQuery(query);
/*  464 */     while (rset.next()) {
/*  465 */       this.jUsername.setText(rset.getString("username"));
/*  466 */       this.jFirstName.setText(rset.getString("first_name"));
/*  467 */       this.jLastName.setText(rset.getString("last_name"));
/*  468 */       setOldPW(rset.getString("password"));
/*  469 */       this.jPassword.setText(rset.getString("password"));
/*  470 */       this.jPassword2.setText(rset.getString("password"));
/*  471 */       this.jComboBox1.setSelectedIndex(rset.getInt("user_level"));
/*  472 */       this.jInitial.setText(rset.getString("initials"));
     } 
/*  474 */     state.close();
/*  475 */     connect.close();
/*  476 */     this.changePW.setEnabled(true);
   }
   
   private void retrieveBranch() throws SQLException {
/*  480 */     Connection connect = DriverManager.getConnection(this.driver, this.username, this.password);
/*  481 */     Statement state = connect.createStatement();
/*  482 */     String query = "Select * from branch_info where branch_name = '" + this.bchTable.getValueAt(this.bchTable.getSelectedRow(), 0).toString() + "'";
/*  483 */     ResultSet rset = state.executeQuery(query);
/*  484 */     while (rset.next()) {
/*  485 */       this.bchName.setText(rset.getString(2));
/*  486 */       this.bchCode.setText(rset.getString(1));
     } 
/*  488 */     state.close();
/*  489 */     connect.close();
   }
 
   
   public void retrieveDefSettings() {
/*  494 */     this.defIP.setText(this.con.getProp("IP"));
/*  495 */     this.defaultBranch.setSelectedItem(this.con.getProp("branch"));
/*  496 */     this.defPort.setText(this.con.getProp("port"));
                this.defKYCfolder.setText(this.con.getProp("default_photo_folder").replace("\"", ""));
   }
    
    

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        chooseFile = new javax.swing.JFileChooser();
        jScrollPane5 = new javax.swing.JScrollPane();
        jPanel9 = new javax.swing.JPanel();
        jPanel1 = new javax.swing.JPanel();
        newButton = new javax.swing.JButton();
        editButton = new javax.swing.JButton();
        changePW = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTable1 = new javax.swing.JTable();
        jLabel1 = new javax.swing.JLabel();
        jPanel10 = new javax.swing.JPanel();
        jFirstName = new javax.swing.JTextField();
        jPassword = new javax.swing.JTextField();
        jLabel8 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jUsername = new javax.swing.JTextField();
        jLabel6 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jInitial = new javax.swing.JTextField();
        jLastName = new javax.swing.JTextField();
        jLabel9 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jComboBox1 = new javax.swing.JComboBox<>();
        jPassword2 = new javax.swing.JTextField();
        jLabel7 = new javax.swing.JLabel();
        jPanel2 = new javax.swing.JPanel();
        jLabel10 = new javax.swing.JLabel();
        jLabel11 = new javax.swing.JLabel();
        bchName = new javax.swing.JTextField();
        bchCode = new javax.swing.JTextField();
        jScrollPane2 = new javax.swing.JScrollPane();
        bchTable = new javax.swing.JTable();
        newBch = new javax.swing.JButton();
        deleteBch = new javax.swing.JButton();
        jLabel20 = new javax.swing.JLabel();
        jPanel3 = new javax.swing.JPanel();
        jLabel12 = new javax.swing.JLabel();
        addcattype = new javax.swing.JTextField();
        newAddlCat = new javax.swing.JButton();
        deleteAddlBtn = new javax.swing.JButton();
        jScrollPane3 = new javax.swing.JScrollPane();
        addcattable = new javax.swing.JTable();
        jLabel5 = new javax.swing.JLabel();
        jPanel4 = new javax.swing.JPanel();
        jPanel7 = new javax.swing.JPanel();
        jLabel14 = new javax.swing.JLabel();
        exportFileName = new javax.swing.JTextField();
        startExportBT = new javax.swing.JButton();
        openChooseFileExport = new javax.swing.JButton();
        jPanel8 = new javax.swing.JPanel();
        jLabel15 = new javax.swing.JLabel();
        importFileName = new javax.swing.JTextField();
        startImportBT = new javax.swing.JButton();
        openChooseFileImport = new javax.swing.JButton();
        jLabel21 = new javax.swing.JLabel();
        jLabel23 = new javax.swing.JLabel();
        sqlDirPath = new javax.swing.JTextField();
        setPathButton = new javax.swing.JButton();
        jPanel5 = new javax.swing.JPanel();
        jScrollPane4 = new javax.swing.JScrollPane();
        petCatTable = new javax.swing.JTable();
        newPtyBtn = new javax.swing.JButton();
        deletePtyBtn = new javax.swing.JButton();
        ptyCatType = new javax.swing.JTextField();
        jLabel13 = new javax.swing.JLabel();
        jLabel19 = new javax.swing.JLabel();
        jPanel6 = new javax.swing.JPanel();
        jLabel16 = new javax.swing.JLabel();
        defIP = new javax.swing.JTextField();
        jLabel17 = new javax.swing.JLabel();
        defPort = new javax.swing.JTextField();
        jLabel18 = new javax.swing.JLabel();
        defaultBranch = new javax.swing.JComboBox<>();
        resetConnectSettingsBtn = new javax.swing.JButton();
        jButton13 = new javax.swing.JButton();
        jLabel22 = new javax.swing.JLabel();
        jLabel24 = new javax.swing.JLabel();
        defKYCfolder = new javax.swing.JTextField();

        jPanel1.setBackground(new java.awt.Color(255, 255, 255));

        newButton.setBackground(new java.awt.Color(79, 119, 141));
        newButton.setFont(new java.awt.Font("Segoe UI Semibold", 0, 13)); // NOI18N
        newButton.setForeground(new java.awt.Color(255, 255, 255));
        newButton.setText("New");
        newButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                newButtonActionPerformed(evt);
            }
        });

        editButton.setFont(new java.awt.Font("Segoe UI Semibold", 0, 13)); // NOI18N
        editButton.setForeground(new java.awt.Color(79, 119, 141));
        editButton.setText("Edit");
        editButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                editButtonActionPerformed(evt);
            }
        });

        changePW.setFont(new java.awt.Font("Segoe UI Semibold", 0, 13)); // NOI18N
        changePW.setForeground(new java.awt.Color(79, 119, 141));
        changePW.setText("Change Password");
        changePW.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                changePWActionPerformed(evt);
            }
        });

        jTable1.setBackground(new java.awt.Color(240, 240, 240));
        jTable1.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Username", "Branch Name", "Last Name", "User Type"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jTable1.setFillsViewportHeight(true);
        jTable1.setGridColor(new java.awt.Color(240, 240, 240));
        jTable1.setRowHeight(30);
        jTable1.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jTable1MouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(jTable1);

        jLabel1.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(102, 102, 102));
        jLabel1.setText("User Management");

        jPanel10.setBackground(new java.awt.Color(227, 240, 251));

        jFirstName.setFont(new java.awt.Font("Segoe UI Semibold", 0, 13)); // NOI18N
        jFirstName.setForeground(java.awt.Color.darkGray);

        jPassword.setFont(new java.awt.Font("Segoe UI Semibold", 0, 13)); // NOI18N
        jPassword.setForeground(java.awt.Color.darkGray);

        jLabel8.setForeground(new java.awt.Color(102, 102, 102));
        jLabel8.setText("User Type");

        jLabel2.setForeground(new java.awt.Color(102, 102, 102));
        jLabel2.setText("Username");

        jUsername.setFont(new java.awt.Font("Segoe UI Semibold", 0, 13)); // NOI18N
        jUsername.setForeground(java.awt.Color.darkGray);

        jLabel6.setForeground(new java.awt.Color(102, 102, 102));
        jLabel6.setText("First Name");

        jLabel4.setForeground(new java.awt.Color(102, 102, 102));
        jLabel4.setText("Retype Password");

        jInitial.setFont(new java.awt.Font("Segoe UI Semibold", 0, 13)); // NOI18N
        jInitial.setForeground(java.awt.Color.darkGray);

        jLastName.setFont(new java.awt.Font("Segoe UI Semibold", 0, 13)); // NOI18N
        jLastName.setForeground(java.awt.Color.darkGray);

        jLabel9.setForeground(new java.awt.Color(102, 102, 102));
        jLabel9.setText("Initials");

        jLabel3.setForeground(new java.awt.Color(102, 102, 102));
        jLabel3.setText("Password");

        jComboBox1.setFont(new java.awt.Font("Segoe UI Semibold", 0, 13)); // NOI18N
        jComboBox1.setForeground(java.awt.Color.darkGray);
        jComboBox1.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Clerk", "Cashier", "Admin" }));

        jPassword2.setFont(new java.awt.Font("Segoe UI Semibold", 0, 13)); // NOI18N
        jPassword2.setForeground(java.awt.Color.darkGray);

        jLabel7.setForeground(new java.awt.Color(102, 102, 102));
        jLabel7.setText("Last Name");

        javax.swing.GroupLayout jPanel10Layout = new javax.swing.GroupLayout(jPanel10);
        jPanel10.setLayout(jPanel10Layout);
        jPanel10Layout.setHorizontalGroup(
            jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel10Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(jPanel10Layout.createSequentialGroup()
                        .addGroup(jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                            .addComponent(jLabel6, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jPassword, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 149, Short.MAX_VALUE)
                            .addComponent(jLabel3, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jUsername, javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel2, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jFirstName)
                            .addComponent(jInitial, javax.swing.GroupLayout.Alignment.LEADING))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jLabel7)
                            .addComponent(jLabel8)
                            .addComponent(jComboBox1, 0, 130, Short.MAX_VALUE)
                            .addComponent(jLabel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jPassword2)
                            .addComponent(jLastName)))
                    .addComponent(jLabel9))
                .addContainerGap())
        );

        jPanel10Layout.linkSize(javax.swing.SwingConstants.HORIZONTAL, new java.awt.Component[] {jComboBox1, jFirstName, jInitial, jLastName, jPassword, jPassword2, jUsername});

        jPanel10Layout.setVerticalGroup(
            jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel10Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel2)
                    .addComponent(jLabel8))
                .addGap(1, 1, 1)
                .addGroup(jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jUsername, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jComboBox1, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel3)
                    .addComponent(jLabel4))
                .addGap(1, 1, 1)
                .addGroup(jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jPassword, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jPassword2, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel6)
                    .addComponent(jLabel7))
                .addGap(1, 1, 1)
                .addGroup(jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jFirstName, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLastName, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel9)
                .addGap(1, 1, 1)
                .addComponent(jInitial, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(26, 26, 26)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel1)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(newButton, javax.swing.GroupLayout.PREFERRED_SIZE, 81, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(changePW)
                        .addGap(5, 5, 5)
                        .addComponent(editButton, javax.swing.GroupLayout.PREFERRED_SIZE, 91, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jPanel10, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 421, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(37, 37, 37))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(15, 15, 15)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jLabel1)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jPanel10, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(newButton, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(changePW, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(editButton, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE)))))
                .addContainerGap(20, Short.MAX_VALUE))
        );

        jPanel2.setBackground(new java.awt.Color(255, 255, 255));

        jLabel10.setForeground(new java.awt.Color(102, 102, 102));
        jLabel10.setText("Branch Name");

        jLabel11.setForeground(new java.awt.Color(102, 102, 102));
        jLabel11.setText("Branch Code:");

        bchName.setFont(new java.awt.Font("Segoe UI", 1, 13)); // NOI18N
        bchName.setForeground(new java.awt.Color(51, 51, 51));

        bchCode.setFont(new java.awt.Font("Segoe UI", 1, 13)); // NOI18N
        bchCode.setForeground(new java.awt.Color(51, 51, 51));

        bchTable.setBackground(new java.awt.Color(240, 240, 240));
        bchTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null},
                {null, null},
                {null, null},
                {null, null}
            },
            new String [] {
                "Branch Name", "Branch Code"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        bchTable.setFillsViewportHeight(true);
        bchTable.setGridColor(new java.awt.Color(240, 240, 240));
        bchTable.setRowHeight(30);
        bchTable.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                bchTableMouseClicked(evt);
            }
        });
        jScrollPane2.setViewportView(bchTable);

        newBch.setBackground(new java.awt.Color(79, 119, 141));
        newBch.setFont(new java.awt.Font("Segoe UI Semibold", 0, 13)); // NOI18N
        newBch.setForeground(new java.awt.Color(255, 255, 255));
        newBch.setText("New");
        newBch.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                newBchActionPerformed(evt);
            }
        });

        deleteBch.setFont(new java.awt.Font("Segoe UI Semibold", 0, 13)); // NOI18N
        deleteBch.setForeground(new java.awt.Color(79, 119, 141));
        deleteBch.setText("Delete");
        deleteBch.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                deleteBchActionPerformed(evt);
            }
        });

        jLabel20.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel20.setForeground(new java.awt.Color(102, 102, 102));
        jLabel20.setText("Branch Management");

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jLabel20))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGap(19, 19, 19)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel2Layout.createSequentialGroup()
                                .addGap(167, 167, 167)
                                .addComponent(jLabel11)
                                .addGap(33, 33, 33)
                                .addComponent(newBch, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                            .addGroup(jPanel2Layout.createSequentialGroup()
                                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                    .addComponent(bchName, javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel10, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 157, Short.MAX_VALUE))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(bchCode, javax.swing.GroupLayout.PREFERRED_SIZE, 90, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addComponent(deleteBch, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
                    .addComponent(jScrollPane2, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE))
                .addContainerGap())
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel20)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel10)
                    .addComponent(jLabel11)
                    .addComponent(newBch, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(bchName, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(bchCode, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(deleteBch, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                .addContainerGap())
        );

        jPanel3.setBackground(new java.awt.Color(227, 240, 251));

        jLabel12.setForeground(new java.awt.Color(102, 102, 102));
        jLabel12.setText("Category");

        addcattype.setFont(new java.awt.Font("Segoe UI Semibold", 0, 13)); // NOI18N
        addcattype.setForeground(java.awt.Color.darkGray);
        addcattype.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                addcattypeFocusLost(evt);
            }
        });
        addcattype.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                addcattypeKeyPressed(evt);
            }
        });

        newAddlCat.setBackground(new java.awt.Color(79, 119, 141));
        newAddlCat.setFont(new java.awt.Font("Segoe UI Semibold", 0, 13)); // NOI18N
        newAddlCat.setForeground(new java.awt.Color(255, 255, 255));
        newAddlCat.setText("New");
        newAddlCat.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                newAddlCatActionPerformed(evt);
            }
        });

        deleteAddlBtn.setFont(new java.awt.Font("Segoe UI Semibold", 0, 13)); // NOI18N
        deleteAddlBtn.setForeground(new java.awt.Color(79, 119, 141));
        deleteAddlBtn.setText("Delete");

        addcattable.setBackground(new java.awt.Color(240, 240, 240));
        addcattable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null},
                {null},
                {null},
                {null}
            },
            new String [] {
                "Categories"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        addcattable.setFillsViewportHeight(true);
        addcattable.setGridColor(new java.awt.Color(240, 240, 240));
        addcattable.setRowHeight(30);
        addcattable.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                addcattableMouseClicked(evt);
            }
        });
        jScrollPane3.setViewportView(addcattable);

        jLabel5.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel5.setForeground(new java.awt.Color(102, 102, 102));
        jLabel5.setText("Additional Transactions Categories");

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 368, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel5)
                            .addComponent(jLabel12)
                            .addGroup(jPanel3Layout.createSequentialGroup()
                                .addComponent(addcattype, javax.swing.GroupLayout.PREFERRED_SIZE, 145, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(newAddlCat, javax.swing.GroupLayout.PREFERRED_SIZE, 104, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(deleteAddlBtn, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel5)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jLabel12)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(addcattype, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(newAddlCat, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(deleteAddlBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 167, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jPanel4.setBackground(new java.awt.Color(255, 255, 255));

        jPanel7.setBackground(new java.awt.Color(227, 240, 251));
        jPanel7.setBorder(javax.swing.BorderFactory.createTitledBorder("Export Database Dump File"));

        jLabel14.setForeground(new java.awt.Color(102, 102, 102));
        jLabel14.setText("File Path:");

        exportFileName.setFont(new java.awt.Font("Segoe UI Semibold", 1, 13)); // NOI18N
        exportFileName.setForeground(new java.awt.Color(51, 51, 51));
        exportFileName.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                exportFileNameActionPerformed(evt);
            }
        });

        startExportBT.setBackground(new java.awt.Color(79, 119, 141));
        startExportBT.setFont(new java.awt.Font("Segoe UI Semibold", 0, 13)); // NOI18N
        startExportBT.setForeground(new java.awt.Color(255, 255, 255));
        startExportBT.setText("Start Export");
        startExportBT.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                startExportBTActionPerformed(evt);
            }
        });

        openChooseFileExport.setFont(new java.awt.Font("Segoe UI Semibold", 0, 13)); // NOI18N
        openChooseFileExport.setForeground(new java.awt.Color(79, 119, 141));
        openChooseFileExport.setText("...");
        openChooseFileExport.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                openChooseFileExportActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel7Layout = new javax.swing.GroupLayout(jPanel7);
        jPanel7.setLayout(jPanel7Layout);
        jPanel7Layout.setHorizontalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel7Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel7Layout.createSequentialGroup()
                        .addComponent(jLabel14)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(exportFileName))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel7Layout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(openChooseFileExport)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(startExportBT)))
                .addContainerGap())
        );
        jPanel7Layout.setVerticalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel7Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel14)
                    .addComponent(exportFileName, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(startExportBT, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(openChooseFileExport, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)))
        );

        jPanel8.setBackground(new java.awt.Color(227, 240, 251));
        jPanel8.setBorder(javax.swing.BorderFactory.createTitledBorder("Import Database Dump File"));

        jLabel15.setForeground(new java.awt.Color(102, 102, 102));
        jLabel15.setText("File Path:");

        importFileName.setFont(new java.awt.Font("Segoe UI Semibold", 0, 13)); // NOI18N
        importFileName.setForeground(new java.awt.Color(51, 51, 51));

        startImportBT.setBackground(new java.awt.Color(79, 119, 141));
        startImportBT.setFont(new java.awt.Font("Segoe UI Semibold", 0, 13)); // NOI18N
        startImportBT.setForeground(new java.awt.Color(255, 255, 255));
        startImportBT.setText("Start Import");
        startImportBT.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                startImportBTActionPerformed(evt);
            }
        });

        openChooseFileImport.setFont(new java.awt.Font("Segoe UI Semibold", 0, 13)); // NOI18N
        openChooseFileImport.setForeground(new java.awt.Color(79, 119, 141));
        openChooseFileImport.setText("...");
        openChooseFileImport.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                openChooseFileImportActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel8Layout = new javax.swing.GroupLayout(jPanel8);
        jPanel8.setLayout(jPanel8Layout);
        jPanel8Layout.setHorizontalGroup(
            jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel8Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel8Layout.createSequentialGroup()
                        .addComponent(jLabel15)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(importFileName))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel8Layout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(openChooseFileImport)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(startImportBT)))
                .addContainerGap())
        );
        jPanel8Layout.setVerticalGroup(
            jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel8Layout.createSequentialGroup()
                .addGap(8, 8, 8)
                .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel15)
                    .addComponent(importFileName, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(startImportBT, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(openChooseFileImport, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jLabel21.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel21.setForeground(new java.awt.Color(102, 102, 102));
        jLabel21.setText("Import / Export Database");

        jLabel23.setForeground(new java.awt.Color(102, 102, 102));
        jLabel23.setText("SQL Database Directory Path");

        sqlDirPath.setFont(new java.awt.Font("Segoe UI Semibold", 1, 13)); // NOI18N
        sqlDirPath.setForeground(new java.awt.Color(51, 51, 51));
        sqlDirPath.setToolTipText("Directory Path to MariaDB or MySQL");
        sqlDirPath.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                sqlDirPathActionPerformed(evt);
            }
        });

        setPathButton.setBackground(new java.awt.Color(79, 119, 141));
        setPathButton.setFont(new java.awt.Font("Segoe UI Semibold", 0, 13)); // NOI18N
        setPathButton.setForeground(new java.awt.Color(255, 255, 255));
        setPathButton.setText("Set Path");
        setPathButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                setPathButtonActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addGap(12, 12, 12)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel8, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel7, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel23)
                    .addComponent(jLabel21)
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addComponent(sqlDirPath, javax.swing.GroupLayout.PREFERRED_SIZE, 257, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(setPathButton, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                .addContainerGap())
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel21)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel23)
                .addGap(4, 4, 4)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(sqlDirPath, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(setPathButton, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel7, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel8, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jPanel5.setBackground(new java.awt.Color(227, 240, 251));

        petCatTable.setBackground(new java.awt.Color(240, 240, 240));
        petCatTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null},
                {null},
                {null},
                {null}
            },
            new String [] {
                "Categories"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        petCatTable.setFillsViewportHeight(true);
        petCatTable.setGridColor(new java.awt.Color(240, 240, 240));
        petCatTable.setRowHeight(30);
        petCatTable.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                petCatTableMouseClicked(evt);
            }
        });
        jScrollPane4.setViewportView(petCatTable);

        newPtyBtn.setBackground(new java.awt.Color(79, 119, 141));
        newPtyBtn.setFont(new java.awt.Font("Segoe UI Semibold", 0, 13)); // NOI18N
        newPtyBtn.setForeground(new java.awt.Color(255, 255, 255));
        newPtyBtn.setText("New");
        newPtyBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                newPtyBtnActionPerformed(evt);
            }
        });

        deletePtyBtn.setFont(new java.awt.Font("Segoe UI Semibold", 0, 13)); // NOI18N
        deletePtyBtn.setForeground(new java.awt.Color(79, 119, 141));
        deletePtyBtn.setText("Delete");

        ptyCatType.setFont(new java.awt.Font("Segoe UI Semibold", 0, 13)); // NOI18N
        ptyCatType.setForeground(java.awt.Color.darkGray);
        ptyCatType.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                ptyCatTypeFocusLost(evt);
            }
        });
        ptyCatType.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                ptyCatTypeKeyPressed(evt);
            }
        });

        jLabel13.setForeground(new java.awt.Color(102, 102, 102));
        jLabel13.setText("Category");

        jLabel19.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel19.setForeground(new java.awt.Color(102, 102, 102));
        jLabel19.setText("Petty Cash Transactions Categories");

        javax.swing.GroupLayout jPanel5Layout = new javax.swing.GroupLayout(jPanel5);
        jPanel5.setLayout(jPanel5Layout);
        jPanel5Layout.setHorizontalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel13)
                    .addComponent(jLabel19)
                    .addGroup(jPanel5Layout.createSequentialGroup()
                        .addComponent(ptyCatType, javax.swing.GroupLayout.PREFERRED_SIZE, 166, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(newPtyBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 107, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(deletePtyBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 93, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jScrollPane4, javax.swing.GroupLayout.PREFERRED_SIZE, 378, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel5Layout.setVerticalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel19)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jLabel13)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(ptyCatType, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(newPtyBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(deletePtyBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane4, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                .addContainerGap())
        );

        jPanel6.setBackground(new java.awt.Color(227, 240, 251));

        jLabel16.setForeground(new java.awt.Color(102, 102, 102));
        jLabel16.setText("Default IP:");

        defIP.setFont(new java.awt.Font("Segoe UI Semibold", 0, 13)); // NOI18N
        defIP.setForeground(java.awt.Color.darkGray);

        jLabel17.setForeground(new java.awt.Color(102, 102, 102));
        jLabel17.setText("Default Port:");

        defPort.setFont(new java.awt.Font("Segoe UI Semibold", 0, 13)); // NOI18N
        defPort.setForeground(java.awt.Color.darkGray);

        jLabel18.setForeground(new java.awt.Color(102, 102, 102));
        jLabel18.setText("Default Branch:");

        defaultBranch.setFont(new java.awt.Font("Segoe UI Semibold", 0, 13)); // NOI18N
        defaultBranch.setForeground(java.awt.Color.darkGray);
        defaultBranch.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));

        resetConnectSettingsBtn.setFont(new java.awt.Font("Segoe UI Semibold", 0, 13)); // NOI18N
        resetConnectSettingsBtn.setForeground(new java.awt.Color(79, 119, 141));
        resetConnectSettingsBtn.setText("Reset");
        resetConnectSettingsBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                resetConnectSettingsBtnActionPerformed(evt);
            }
        });

        jButton13.setBackground(new java.awt.Color(79, 119, 141));
        jButton13.setFont(new java.awt.Font("Segoe UI Semibold", 0, 13)); // NOI18N
        jButton13.setForeground(new java.awt.Color(255, 255, 255));
        jButton13.setText("Save");
        jButton13.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton13ActionPerformed(evt);
            }
        });

        jLabel22.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel22.setForeground(new java.awt.Color(102, 102, 102));
        jLabel22.setText("Connection Settings");

        jLabel24.setForeground(new java.awt.Color(102, 102, 102));
        jLabel24.setText("Default KYC Folder:");

        javax.swing.GroupLayout jPanel6Layout = new javax.swing.GroupLayout(jPanel6);
        jPanel6.setLayout(jPanel6Layout);
        jPanel6Layout.setHorizontalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addGap(22, 22, 22)
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jLabel18)
                    .addComponent(jLabel16))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(defIP)
                    .addComponent(defaultBranch, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(18, 18, 18)
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel6Layout.createSequentialGroup()
                        .addComponent(jLabel17)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(defPort))
                    .addGroup(jPanel6Layout.createSequentialGroup()
                        .addComponent(jLabel24)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(defKYCfolder)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(resetConnectSettingsBtn)
                    .addComponent(jButton13))
                .addGap(8, 8, 8))
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel22)
                .addGap(0, 0, Short.MAX_VALUE))
        );
        jPanel6Layout.setVerticalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel22)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel16)
                    .addComponent(defIP, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel17)
                    .addComponent(defPort, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton13, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(defKYCfolder, javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabel18)
                        .addComponent(defaultBranch, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(resetConnectSettingsBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jLabel24)))
                .addGap(7, 7, 7))
        );

        javax.swing.GroupLayout jPanel9Layout = new javax.swing.GroupLayout(jPanel9);
        jPanel9.setLayout(jPanel9Layout);
        jPanel9Layout.setHorizontalGroup(
            jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel9Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, 795, Short.MAX_VALUE)
                    .addGroup(jPanel9Layout.createSequentialGroup()
                        .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                            .addComponent(jPanel6, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addGroup(jPanel9Layout.createSequentialGroup()
                                .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                    .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addComponent(jPanel5, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(jPanel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
        );
        jPanel9Layout.setVerticalGroup(
            jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel9Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel5, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel6, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jScrollPane5.setViewportView(jPanel9);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane5, javax.swing.GroupLayout.DEFAULT_SIZE, 819, Short.MAX_VALUE)
                .addGap(15, 15, 15))
            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                    .addContainerGap(117, Short.MAX_VALUE)
                    .addComponent(chooseFile, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addContainerGap(118, Short.MAX_VALUE)))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane5, javax.swing.GroupLayout.DEFAULT_SIZE, 756, Short.MAX_VALUE)
                .addContainerGap())
            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                    .addContainerGap(210, Short.MAX_VALUE)
                    .addComponent(chooseFile, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addContainerGap(211, Short.MAX_VALUE)))
        );
    }// </editor-fold>//GEN-END:initComponents

    private void newButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_newButtonActionPerformed
               
               enableTB();
/* 1340 */     clearTB();
/* 1341 */     setEditMode(false);
/* 1342 */     this.editButton.setText("Save");
/* 1343 */     this.changePW.setEnabled(false);
    }//GEN-LAST:event_newButtonActionPerformed

    private void editButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_editButtonActionPerformed
        if (this.editButton.getText().equals("Edit")) {
/* 1348 */       setEditMode(true);
/* 1349 */       enableTB();
/* 1350 */       this.editButton.setText("Save");
     } else {
/* 1352 */       disableTB();
/* 1353 */       if (this.jPassword.getText().equals(this.jPassword2.getText())) {
         try {
/* 1355 */           addUser();
/* 1356 */         } catch (SQLException ex) {
/* 1357 */           this.con.saveProp("mpis_last_error", String.valueOf(ex));
/* 1358 */           Logger.getLogger(Management.class.getName()).log(Level.SEVERE, (String)null, ex);
         } 
/* 1360 */         this.editButton.setText("Edit");
/* 1361 */         if (this.editMode) {
/* 1362 */           JOptionPane.showMessageDialog(null, "Employee record updated successfully!");
         } else {
/* 1364 */           JOptionPane.showMessageDialog(null, "New Employee record added successfully!");
         }  try {
/* 1366 */           updateUserTable();
/* 1367 */         } catch (SQLException ex) {
/* 1368 */           this.con.saveProp("mpis_last_error", String.valueOf(ex));
/* 1369 */           Logger.getLogger(Management.class.getName()).log(Level.SEVERE, (String)null, ex);
         } 
       } else {
/* 1372 */         JOptionPane.showMessageDialog(null, "Passwords typed did not matched. Please Retype passwords.");
       } 
     } 
    }//GEN-LAST:event_editButtonActionPerformed

    private void jTable1MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jTable1MouseClicked
        try {
/* 1379 */       retrieveUser();
/* 1380 */     } catch (SQLException ex) {
/* 1381 */       this.con.saveProp("mpis_last_error", String.valueOf(ex));
/* 1382 */       Logger.getLogger(Management.class.getName()).log(Level.SEVERE, (String)null, ex);
     } 
    }//GEN-LAST:event_jTable1MouseClicked

    private void changePWActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_changePWActionPerformed
        ConfirmPassword conPW = new ConfirmPassword(null, true);
/* 1396 */     conPW.setVisible(true);
/* 1397 */     if (conPW.isPwGiven() && 
/* 1398 */       conPW.getPassword().equals(getOldPW())) {
/* 1399 */       this.jPassword.setEditable(true);
/* 1400 */       this.jPassword2.setEditable(true);
     } 
    }//GEN-LAST:event_changePWActionPerformed

    private void startExportBTActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_startExportBTActionPerformed
        if (backupDB("merlininventorydatabase", this.username, this.password, getFileNameSave()) == true) {
/* 1444 */       JOptionPane.showMessageDialog(null, "Database Dump File creation successful!", "Back Up Database", 1);
/* 1445 */       this.exportFileName.setText("");
/* 1446 */       this.startExportBT.setEnabled(false);
     } else {
/* 1448 */       JOptionPane.showMessageDialog(null, "A problem occured while backing up your database. Please contact your POS provider.", "Back Up Database", 0);
     } 
    }//GEN-LAST:event_startExportBTActionPerformed

    private void startImportBTActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_startImportBTActionPerformed
        if (restoreDB(this.username, this.password, getFileNameOpen()) == true) {
/* 1454 */       JOptionPane.showMessageDialog(null, "Database Dump File successfully imported!", "Restore Back Up", 1);
/* 1455 */       this.startImportBT.setEnabled(false);
/* 1456 */       this.importFileName.setText("");
     } else {
/* 1458 */       JOptionPane.showMessageDialog(null, "A problem occured while restoring your database from a dump file. Please contact your POS provider.", "Restore Back Up", 0);
     }
    }//GEN-LAST:event_startImportBTActionPerformed

    private void exportFileNameActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_exportFileNameActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_exportFileNameActionPerformed

    private void openChooseFileExportActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_openChooseFileExportActionPerformed
        this.chooseFile.setVisible(true);
/* 1407 */     int saveAs = this.chooseFile.showSaveDialog((Component)null);
/* 1408 */     if (saveAs == 0) {
       
/* 1410 */       if (this.chooseFile.getSelectedFile().toString().substring(this.chooseFile.getSelectedFile().toString().length() - 4).equals(".sql")) {
/* 1411 */         setFileNameSave(this.chooseFile.getSelectedFile().getAbsolutePath());
       } else {
/* 1413 */         setFileNameSave(this.chooseFile.getSelectedFile().getAbsolutePath().concat(".sql"));
       } 
/* 1415 */       this.chooseFile.setSelectedFile(new File(getFileNameSave()));
/* 1416 */       if (this.chooseFile.getSelectedFile().exists()) {
/* 1417 */         int replaceFile = JOptionPane.showConfirmDialog(null, "A file with the same name already exists. Do you want to replace this file?", "Replace File", 0);
/* 1418 */         if (replaceFile == 1) {
/* 1419 */           openChooseFileExportActionPerformed((ActionEvent)null);
           return;
         } 
       } 
/* 1423 */       this.exportFileName.setText(this.chooseFile.getSelectedFile().getAbsolutePath().toString());
/* 1424 */       this.startExportBT.setEnabled(true);
     } 
    }//GEN-LAST:event_openChooseFileExportActionPerformed

    private void openChooseFileImportActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_openChooseFileImportActionPerformed
        this.chooseFile.setVisible(true);
/* 1430 */     int openFile = this.chooseFile.showOpenDialog((Component)null);
/* 1431 */     if (openFile == 0) {
/* 1432 */       if (this.chooseFile.getSelectedFile().toString().substring(this.chooseFile.getSelectedFile().toString().length() - 4).equals(".sql")) {
/* 1433 */         setFileNameOpen(this.chooseFile.getSelectedFile().getAbsolutePath());
       } else {
/* 1435 */         setFileNameOpen(this.chooseFile.getSelectedFile().getAbsolutePath().concat(".sql"));
       } 
/* 1437 */       this.importFileName.setText(this.chooseFile.getSelectedFile().getAbsolutePath().toString());
/* 1438 */       this.startImportBT.setEnabled(true);
     }
    }//GEN-LAST:event_openChooseFileImportActionPerformed

    private void bchTableMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_bchTableMouseClicked
        try {
/* 1468 */       retrieveBranch();
/* 1469 */     } catch (SQLException ex) {
/* 1470 */       this.con.saveProp("mpis_last_error", String.valueOf(ex));
/* 1471 */       Logger.getLogger(Management.class.getName()).log(Level.SEVERE, (String)null, ex);
     }
    }//GEN-LAST:event_bchTableMouseClicked

    private void newBchActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_newBchActionPerformed
        this.bchCode.setEditable(true);
/* 1477 */     this.bchName.setEditable(true);
/* 1478 */     this.deleteBch.setText("Save");
/* 1479 */     this.bchCode.setText("");
/* 1480 */     this.bchName.setText("");
    }//GEN-LAST:event_newBchActionPerformed

    private void deleteBchActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_deleteBchActionPerformed
        if (this.deleteBch.getText().equalsIgnoreCase("Delete")) {
       try {
/* 1488 */         Connection connect = DriverManager.getConnection(this.driver, this.username, this.password);
/* 1489 */         Statement state = connect.createStatement();
/* 1490 */         state.executeUpdate("Delete from branch_info where branch_code = '" + this.bchCode.getText() + "'");
/* 1491 */         connect.close();
/* 1492 */         state.close();
/* 1493 */         JOptionPane.showMessageDialog(null, "Branch information successfully deleted.");
/* 1494 */       } catch (SQLException ex) {
/* 1495 */         this.con.saveProp("mpis_last_error", String.valueOf(ex));
/* 1496 */         JOptionPane.showMessageDialog(null, "An error is encountered while deleting this branch information.");
/* 1497 */         Logger.getLogger(Management.class.getName()).log(Level.SEVERE, (String)null, ex);
       } 
     } else {
       try {
/* 1501 */         Connection connect = DriverManager.getConnection(this.driver, this.username, this.password);
/* 1502 */         Statement state = connect.createStatement();
/* 1503 */         state.executeUpdate("Insert into branch_info values ('" + this.bchCode.getText() + "', '" + this.bchName.getText() + "')");
/* 1504 */         connect.close();
/* 1505 */         state.close();
/* 1506 */         JOptionPane.showMessageDialog(null, "Branch information successfully added.");
/* 1507 */       } catch (SQLException ex) {
/* 1508 */         this.con.saveProp("mpis_last_error", String.valueOf(ex));
/* 1509 */         JOptionPane.showMessageDialog(null, "An error is encountered while adding this branch information.");
/* 1510 */         Logger.getLogger(Management.class.getName()).log(Level.SEVERE, (String)null, ex);
       } 
     } 
/* 1513 */     this.bchName.setText("");
/* 1514 */     this.bchCode.setText("");
     try {
/* 1516 */       updateBranchTable();
/* 1517 */     } catch (SQLException ex) {
/* 1518 */       this.con.saveProp("mpis_last_error", String.valueOf(ex));
/* 1519 */       Logger.getLogger(Management.class.getName()).log(Level.SEVERE, (String)null, ex);
     } 
    }//GEN-LAST:event_deleteBchActionPerformed

    private void addcattableMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_addcattableMouseClicked
        this.addcattype.setText((String)this.addcattable.getValueAt(this.addcattable.getSelectedRow(), 0));
/* 1525 */     this.deleteAddlBtn.setEnabled(true);
    }//GEN-LAST:event_addcattableMouseClicked

    private void newAddlCatActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_newAddlCatActionPerformed
        if (this.newAddlCat.getText().equalsIgnoreCase("New")) {
/* 1530 */       this.addcattype.setText("");
/* 1531 */       this.addcattype.requestFocusInWindow();
/* 1532 */       this.addcattype.setEditable(true);
/* 1533 */       this.newAddlCat.setText("Save");
     } else {
/* 1535 */       this.addcattype.setEditable(false);
       try {
/* 1537 */         addAddCat();
/* 1538 */         updateAddCatTable();
/* 1539 */         JOptionPane.showMessageDialog(null, "New Additional Type category is successfully added!");
/* 1540 */       } catch (SQLException ex) {
/* 1541 */         this.con.saveProp("mpis_last_error", String.valueOf(ex));
/* 1542 */         JOptionPane.showMessageDialog(null, "An error was encountered while trying to add this new Additional type. Please consult your database administrator. \n" + ex);
/* 1543 */         Logger.getLogger(Management.class.getName()).log(Level.SEVERE, (String)null, ex);
       } 
/* 1545 */       this.newAddlCat.setText("New");
/* 1546 */       this.addcattype.setEditable(false);
/* 1547 */       this.addcattype.setText("");
     } 
    }//GEN-LAST:event_newAddlCatActionPerformed

    private void petCatTableMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_petCatTableMouseClicked
        this.ptyCatType.setText((String)this.petCatTable.getValueAt(this.petCatTable.getSelectedRow(), 0));
/* 1558 */     this.deletePtyBtn.setEnabled(true);
    }//GEN-LAST:event_petCatTableMouseClicked

    private void newPtyBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_newPtyBtnActionPerformed
        if (this.newPtyBtn.getText().equalsIgnoreCase("New")) {
/* 1563 */       this.ptyCatType.setText("");
/* 1564 */       this.ptyCatType.requestFocusInWindow();
/* 1565 */       this.ptyCatType.setEditable(true);
/* 1566 */       this.newPtyBtn.setText("Save");
     } else {
/* 1568 */       this.ptyCatType.setEditable(false);
       try {
/* 1570 */         petAddCat();
/* 1571 */         updatePetCatTable();
/* 1572 */         JOptionPane.showMessageDialog(null, "New Petty Cash Type category is successfully added!");
/* 1573 */       } catch (SQLException ex) {
/* 1574 */         this.con.saveProp("mpis_last_error", String.valueOf(ex));
/* 1575 */         JOptionPane.showMessageDialog(null, "An error was encountered while trying to add this new Petty Cash type. Please consult your database administrator. \n" + ex);
/* 1576 */         Logger.getLogger(Management.class.getName()).log(Level.SEVERE, (String)null, ex);
       } 
/* 1578 */       this.newPtyBtn.setText("New");
/* 1579 */       this.ptyCatType.setEditable(false);
/* 1580 */       this.ptyCatType.setText("");
     } 
    }//GEN-LAST:event_newPtyBtnActionPerformed

    private void resetConnectSettingsBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_resetConnectSettingsBtnActionPerformed
        this.defaultBranch.setSelectedItem("Tangos");
/* 1590 */     this.defIP.setText("localhost");
/* 1591 */     this.defPort.setText("3307");
    }//GEN-LAST:event_resetConnectSettingsBtnActionPerformed

    private void addcattypeFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_addcattypeFocusLost
        if (!this.addcattype.getText().equalsIgnoreCase((String)this.addcattable.getValueAt(this.addcattable.getSelectedRow(), 0))) {
/* 1600 */       this.deleteAddlBtn.setEnabled(false);
     }
    }//GEN-LAST:event_addcattypeFocusLost

    private void ptyCatTypeFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_ptyCatTypeFocusLost
        if (!this.ptyCatType.getText().equalsIgnoreCase((String)this.petCatTable.getValueAt(this.petCatTable.getSelectedRow(), 0))) {
/* 1606 */       this.deletePtyBtn.setEnabled(false);
     }
    }//GEN-LAST:event_ptyCatTypeFocusLost

    private void addcattypeKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_addcattypeKeyPressed
        if (!this.addcattype.getText().equalsIgnoreCase((String)this.addcattable.getValueAt(this.addcattable.getSelectedRow(), 0))) {
/* 1612 */       this.deleteAddlBtn.setEnabled(false);
     }
    }//GEN-LAST:event_addcattypeKeyPressed

    private void ptyCatTypeKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_ptyCatTypeKeyPressed
        if (!this.addcattype.getText().equalsIgnoreCase((String)this.addcattable.getValueAt(this.addcattable.getSelectedRow(), 0))) {
/* 1618 */       this.deletePtyBtn.setEnabled(false);
     }
    }//GEN-LAST:event_ptyCatTypeKeyPressed

    private void jButton13ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton13ActionPerformed
        int conf = JOptionPane.showConfirmDialog(null, "Are you sure you want to save changes that were made on the default setting in this system?", "Default System Settings", 0);
     if (conf == 0) {
        this.con.saveProp("branch", this.defaultBranch.getSelectedItem().toString());
        this.con.saveProp("IP", this.defIP.getText());
        this.con.saveProp("port", this.defPort.getText());
        this.con.saveProp("default_kyc_folder", defKYCfolder.getText());
        JOptionPane.showMessageDialog(null, "Default System Settings successfully saved!");
     } 
    }//GEN-LAST:event_jButton13ActionPerformed

    private void sqlDirPathActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_sqlDirPathActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_sqlDirPathActionPerformed

    private void setPathButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_setPathButtonActionPerformed
        chooseFile.setVisible(true);
        int openFile = this.chooseFile.showOpenDialog((Component)null);
        this.sqlDirPath.setText(this.chooseFile.getSelectedFile().getAbsolutePath());
        
    }//GEN-LAST:event_setPathButtonActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JTable addcattable;
    private javax.swing.JTextField addcattype;
    private javax.swing.JTextField bchCode;
    private javax.swing.JTextField bchName;
    private javax.swing.JTable bchTable;
    private javax.swing.JButton changePW;
    private javax.swing.JFileChooser chooseFile;
    private javax.swing.JTextField defIP;
    private javax.swing.JTextField defKYCfolder;
    private javax.swing.JTextField defPort;
    private javax.swing.JComboBox<String> defaultBranch;
    private javax.swing.JButton deleteAddlBtn;
    private javax.swing.JButton deleteBch;
    private javax.swing.JButton deletePtyBtn;
    private javax.swing.JButton editButton;
    private javax.swing.JTextField exportFileName;
    private javax.swing.JTextField importFileName;
    private javax.swing.JButton jButton13;
    private javax.swing.JComboBox<String> jComboBox1;
    private javax.swing.JTextField jFirstName;
    private javax.swing.JTextField jInitial;
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
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JTextField jLastName;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel10;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JPanel jPanel7;
    private javax.swing.JPanel jPanel8;
    private javax.swing.JPanel jPanel9;
    private javax.swing.JTextField jPassword;
    private javax.swing.JTextField jPassword2;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JScrollPane jScrollPane4;
    private javax.swing.JScrollPane jScrollPane5;
    private javax.swing.JTable jTable1;
    private javax.swing.JTextField jUsername;
    private javax.swing.JButton newAddlCat;
    private javax.swing.JButton newBch;
    private javax.swing.JButton newButton;
    private javax.swing.JButton newPtyBtn;
    private javax.swing.JButton openChooseFileExport;
    private javax.swing.JButton openChooseFileImport;
    private javax.swing.JTable petCatTable;
    private javax.swing.JTextField ptyCatType;
    private javax.swing.JButton resetConnectSettingsBtn;
    private javax.swing.JButton setPathButton;
    private javax.swing.JTextField sqlDirPath;
    private javax.swing.JButton startExportBT;
    private javax.swing.JButton startImportBT;
    // End of variables declaration//GEN-END:variables
}
