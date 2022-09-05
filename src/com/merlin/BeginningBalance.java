package com.merlin;

import java.awt.EventQueue;
import java.awt.Font;
import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Calendar;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.GroupLayout;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.LayoutStyle;
import com.merlin.Config;
import com.merlin.DateHelper;
import com.merlin.DecimalHelper;

public class BeginningBalance extends JDialog {
  private double beginBal = 0.0D;
  
  private JButton jButton1;
  
  private JLabel jLabel1;
  
  private JTextField jTextField1;
  private DatabaseUpdater dbu = new DatabaseUpdater();
  
  public BeginningBalance(Frame parent, boolean modal) {
    super(parent, modal);
    initComponents();
    setLocationRelativeTo(null);
    this.jTextField1.selectAll();
  }
  
  public double getBeginBal() {
    return this.beginBal;
  }
  
  private void initComponents() {
    this.jLabel1 = new JLabel();
    this.jTextField1 = new JTextField();
    this.jButton1 = new JButton();
    setDefaultCloseOperation(0);
    this.jLabel1.setText("Enter Beginning Balance:");
    this.jTextField1.setFont(new Font("Tahoma", 1, 12));
    this.jTextField1.setHorizontalAlignment(11);
    this.jTextField1.setText("0.00");
    this.jTextField1.addFocusListener(new FocusAdapter() {
          public void focusLost(FocusEvent evt) {
            BeginningBalance.this.jTextField1FocusLost(evt);
          }
        });
    this.jTextField1.addKeyListener(new KeyAdapter() {
          public void keyPressed(KeyEvent evt) {
            BeginningBalance.this.jTextField1KeyPressed(evt);
          }
          
          public void keyTyped(KeyEvent evt) {
            BeginningBalance.this.jTextField1KeyTyped(evt);
          }
        });
    this.jButton1.setText("OK");
    this.jButton1.addActionListener(new ActionListener() {
          public void actionPerformed(ActionEvent evt) {
            BeginningBalance.this.jButton1ActionPerformed(evt);
          }
        });
    GroupLayout layout = new GroupLayout(getContentPane());
    getContentPane().setLayout(layout);
    layout.setHorizontalGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING).addGroup(layout.createSequentialGroup().addContainerGap().addComponent(this.jLabel1).addPreferredGap(LayoutStyle.ComponentPlacement.UNRELATED).addComponent(this.jTextField1, -2, 112, -2).addPreferredGap(LayoutStyle.ComponentPlacement.UNRELATED).addComponent(this.jButton1).addContainerGap(-1, 32767)));
    layout.setVerticalGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING).addGroup(layout.createSequentialGroup().addContainerGap().addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE).addComponent(this.jLabel1).addComponent(this.jTextField1, -2, -1, -2).addComponent(this.jButton1)).addContainerGap(-1, 32767)));
    pack();
  }
  
  private void jTextField1KeyTyped(KeyEvent evt) {
    if (!Character.isDigit(evt.getKeyChar()) && evt.getKeyChar() != '.')
      evt.consume(); 
  }
  
  private void jTextField1KeyPressed(KeyEvent evt) {
    int key = evt.getKeyCode();
    if (key == 10) {
      jTextField1FocusLost((FocusEvent)null);
      jButton1ActionPerformed((ActionEvent)null);
    } 
  }
  
  private void jButton1ActionPerformed(ActionEvent evt) {
    Config con = new Config();
    String driver = "jdbc:mariadb://" + con.getProp("IP") + ":" + con.getProp("port") + "/cashtransactions";
    String username = con.getProp("username");
    String password = con.getProp("password");
    Calendar rightNow = Calendar.getInstance();
    DateHelper dateHelp = new DateHelper();
    Date currentDate = dateHelp.now(rightNow);
    String curDateText = dateHelp.formatDate(currentDate);
    try {
      Connection connect = DriverManager.getConnection(driver, username, password);
      Statement query = connect.createStatement();
      
      String updquery = "Insert into cashtransactions.running_balance (date, beginning_balance, ending_balance, total_cash_inflow, total_cash_outflow) values ('" + curDateText + "', " + Double.parseDouble(this.jTextField1.getText()) + ", " + Double.parseDouble(this.jTextField1.getText()) + ", " + 0.0D + ", " + 0.0D + ")";
      query.executeUpdate(updquery);
      
    } catch (SQLException ex) {
      con.saveProp("mpis_last_error", String.valueOf(ex));
      Logger.getLogger(BeginningBalance.class.getName()).log(Level.SEVERE, (String)null, ex);
      System.out.println("error in beginning balance dialogue box");
    } 
    dispose();
  }
  
  private void jTextField1FocusLost(FocusEvent evt) {
    DecimalHelper decHelp = new DecimalHelper();
    this.jTextField1.setText(decHelp.FormatNumber(Double.parseDouble(this.jTextField1.getText().replace(",", ""))));
  }
  
  public static void main(String[] args) {
    EventQueue.invokeLater(new Runnable() {
          public void run() {
            BeginningBalance dialog = new BeginningBalance(new JFrame(), true);
            dialog.addWindowListener(new WindowAdapter() {
                  public void windowClosing(WindowEvent e) {
                    System.exit(0);
                  }
                });
            dialog.setVisible(true);
          }
        });
  }
}
