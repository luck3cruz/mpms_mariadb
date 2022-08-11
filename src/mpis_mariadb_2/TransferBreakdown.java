package mpis_mariadb_2;

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
import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.GroupLayout;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JSeparator;
import javax.swing.JTextField;
import javax.swing.LayoutStyle;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

public class TransferBreakdown extends JDialog {

    Config con = new Config();

    private final String driver;

    private final String f_user;

    private final String f_pass;

    private DateHelper dateHelp;

    private boolean valuesEntered;

    private NumberFormat formatter;

    private double transfer;

    private String xnum;

    private boolean cancelled;

    private String refNo;

    private String fromFundName;

    private String toFundName;

    private String database;

    private JTextField cent;

    private JTextField fhun;

    private JTextField fif;

    private JTextField fivc;

    private JTextField five;

    private JTextField gtot;

    private JTextField hun;

    private JButton jButton1;

    private JLabel jLabel1;

    private JLabel jLabel10;

    private JLabel jLabel11;

    private JLabel jLabel12;

    private JLabel jLabel14;

    private JLabel jLabel15;

    private JLabel jLabel2;

    private JLabel jLabel3;

    private JLabel jLabel4;

    private JLabel jLabel5;

    private JLabel jLabel6;

    private JLabel jLabel7;

    private JLabel jLabel8;

    private JLabel jLabel9;

    private JSeparator jSeparator1;

    private JTextField one;

    private JTextField tcent;

    private JTextField ten;

    private JTextField tenc;

    private JTextField tfce;

    private JTextField tfhun;

    private JTextField tfif;

    private JTextField tfive;

    private JTextField thou;

    private JTextField thun;

    private JTextField tohun;

    private JTextField tone;

    private JTextField ttce;

    private JTextField tten;

    private JTextField tthou;

    private JTextField tthun;

    private JTextField ttwen;

    private JTextField twen;

    public TransferBreakdown(Frame parent, boolean modal) {
        super(parent, modal);
        this.driver = "jdbc:mariadb://" + this.con.getProp("IP") + ":" + this.con.getProp("port") + "/merlininventorydatabase";
        this.f_user = this.con.getProp("username");
        this.f_pass = this.con.getProp("password");
        this.dateHelp = new DateHelper();
        this.valuesEntered = false;
        this.formatter = new DecimalFormat("#,##0.00");
        this.transfer = 0.0D;
        this.xnum = "";
        this.cancelled = false;
        this.refNo = "";
        this.fromFundName = "";
        this.toFundName = "";
        this.database = "";
        initComponents();
    }

    private void initComponents() {
        this.jLabel1 = new JLabel();
        this.thou = new JTextField();
        this.jLabel2 = new JLabel();
        this.tthou = new JTextField();
        this.fhun = new JTextField();
        this.jLabel3 = new JLabel();
        this.tfhun = new JTextField();
        this.thun = new JTextField();
        this.jLabel4 = new JLabel();
        this.tthun = new JTextField();
        this.hun = new JTextField();
        this.jLabel5 = new JLabel();
        this.tohun = new JTextField();
        this.fif = new JTextField();
        this.jLabel6 = new JLabel();
        this.tfif = new JTextField();
        this.twen = new JTextField();
        this.jLabel7 = new JLabel();
        this.ttwen = new JTextField();
        this.ten = new JTextField();
        this.jLabel8 = new JLabel();
        this.tten = new JTextField();
        this.five = new JTextField();
        this.jLabel9 = new JLabel();
        this.tfive = new JTextField();
        this.one = new JTextField();
        this.jLabel10 = new JLabel();
        this.tone = new JTextField();
        this.cent = new JTextField();
        this.jLabel11 = new JLabel();
        this.tcent = new JTextField();
        this.jButton1 = new JButton();
        this.jSeparator1 = new JSeparator();
        this.jLabel12 = new JLabel();
        this.gtot = new JTextField();
        this.tenc = new JTextField();
        this.jLabel14 = new JLabel();
        this.ttce = new JTextField();
        this.fivc = new JTextField();
        this.jLabel15 = new JLabel();
        this.tfce = new JTextField();
        setDefaultCloseOperation(0);
        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent evt) {
                TransferBreakdown.this.formWindowClosing(evt);
            }
        });
        this.jLabel1.setFont(new Font("Tahoma", 0, 12));
        this.jLabel1.setHorizontalAlignment(0);
        this.jLabel1.setText("CASH BREAKDOWN");
        this.thou.setHorizontalAlignment(11);
        this.thou.setText("0");
        this.thou.setNextFocusableComponent(this.fhun);
        this.thou.addFocusListener(new FocusAdapter() {
            public void focusGained(FocusEvent evt) {
                TransferBreakdown.this.thouFocusGained(evt);
            }

            public void focusLost(FocusEvent evt) {
                TransferBreakdown.this.thouFocusLost(evt);
            }
        });
        this.thou.addKeyListener(new KeyAdapter() {
            public void keyPressed(KeyEvent evt) {
                TransferBreakdown.this.thouKeyPressed(evt);
            }

            public void keyTyped(KeyEvent evt) {
                TransferBreakdown.this.thouKeyTyped(evt);
            }
        });
        this.jLabel2.setText("x Php 1000.00");
        this.tthou.setEditable(false);
        this.tthou.setFont(new Font("Tahoma", 1, 11));
        this.tthou.setHorizontalAlignment(11);
        this.tthou.setText("0.00");
        this.tthou.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                TransferBreakdown.this.tthouActionPerformed(evt);
            }
        });
        this.fhun.setHorizontalAlignment(11);
        this.fhun.setText("0");
        this.fhun.setNextFocusableComponent(this.thun);
        this.fhun.addFocusListener(new FocusAdapter() {
            public void focusGained(FocusEvent evt) {
                TransferBreakdown.this.fhunFocusGained(evt);
            }

            public void focusLost(FocusEvent evt) {
                TransferBreakdown.this.fhunFocusLost(evt);
            }
        });
        this.fhun.addKeyListener(new KeyAdapter() {
            public void keyPressed(KeyEvent evt) {
                TransferBreakdown.this.fhunKeyPressed(evt);
            }

            public void keyTyped(KeyEvent evt) {
                TransferBreakdown.this.fhunKeyTyped(evt);
            }
        });
        this.jLabel3.setText("x Php   500.00");
        this.tfhun.setEditable(false);
        this.tfhun.setFont(new Font("Tahoma", 1, 11));
        this.tfhun.setHorizontalAlignment(11);
        this.tfhun.setText("0.00");
        this.tfhun.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                TransferBreakdown.this.tfhunActionPerformed(evt);
            }
        });
        this.thun.setHorizontalAlignment(11);
        this.thun.setText("0");
        this.thun.setNextFocusableComponent(this.hun);
        this.thun.addFocusListener(new FocusAdapter() {
            public void focusGained(FocusEvent evt) {
                TransferBreakdown.this.thunFocusGained(evt);
            }

            public void focusLost(FocusEvent evt) {
                TransferBreakdown.this.thunFocusLost(evt);
            }
        });
        this.thun.addKeyListener(new KeyAdapter() {
            public void keyPressed(KeyEvent evt) {
                TransferBreakdown.this.thunKeyPressed(evt);
            }

            public void keyTyped(KeyEvent evt) {
                TransferBreakdown.this.thunKeyTyped(evt);
            }
        });
        this.jLabel4.setText("x Php   200.00");
        this.tthun.setEditable(false);
        this.tthun.setFont(new Font("Tahoma", 1, 11));
        this.tthun.setHorizontalAlignment(11);
        this.tthun.setText("0.00");
        this.tthun.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                TransferBreakdown.this.tthunActionPerformed(evt);
            }
        });
        this.hun.setHorizontalAlignment(11);
        this.hun.setText("0");
        this.hun.setNextFocusableComponent(this.fif);
        this.hun.addFocusListener(new FocusAdapter() {
            public void focusGained(FocusEvent evt) {
                TransferBreakdown.this.hunFocusGained(evt);
            }

            public void focusLost(FocusEvent evt) {
                TransferBreakdown.this.hunFocusLost(evt);
            }
        });
        this.hun.addKeyListener(new KeyAdapter() {
            public void keyPressed(KeyEvent evt) {
                TransferBreakdown.this.hunKeyPressed(evt);
            }

            public void keyTyped(KeyEvent evt) {
                TransferBreakdown.this.hunKeyTyped(evt);
            }
        });
        this.jLabel5.setText("x Php   100.00");
        this.tohun.setEditable(false);
        this.tohun.setFont(new Font("Tahoma", 1, 11));
        this.tohun.setHorizontalAlignment(11);
        this.tohun.setText("0.00");
        this.tohun.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                TransferBreakdown.this.tohunActionPerformed(evt);
            }
        });
        this.fif.setHorizontalAlignment(11);
        this.fif.setText("0");
        this.fif.setNextFocusableComponent(this.twen);
        this.fif.addFocusListener(new FocusAdapter() {
            public void focusGained(FocusEvent evt) {
                TransferBreakdown.this.fifFocusGained(evt);
            }

            public void focusLost(FocusEvent evt) {
                TransferBreakdown.this.fifFocusLost(evt);
            }
        });
        this.fif.addKeyListener(new KeyAdapter() {
            public void keyPressed(KeyEvent evt) {
                TransferBreakdown.this.fifKeyPressed(evt);
            }

            public void keyTyped(KeyEvent evt) {
                TransferBreakdown.this.fifKeyTyped(evt);
            }
        });
        this.jLabel6.setText("x Php     50.00");
        this.tfif.setEditable(false);
        this.tfif.setFont(new Font("Tahoma", 1, 11));
        this.tfif.setHorizontalAlignment(11);
        this.tfif.setText("0.00");
        this.tfif.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                TransferBreakdown.this.tfifActionPerformed(evt);
            }
        });
        this.twen.setHorizontalAlignment(11);
        this.twen.setText("0");
        this.twen.setNextFocusableComponent(this.ten);
        this.twen.addFocusListener(new FocusAdapter() {
            public void focusGained(FocusEvent evt) {
                TransferBreakdown.this.twenFocusGained(evt);
            }

            public void focusLost(FocusEvent evt) {
                TransferBreakdown.this.twenFocusLost(evt);
            }
        });
        this.twen.addKeyListener(new KeyAdapter() {
            public void keyPressed(KeyEvent evt) {
                TransferBreakdown.this.twenKeyPressed(evt);
            }

            public void keyTyped(KeyEvent evt) {
                TransferBreakdown.this.twenKeyTyped(evt);
            }
        });
        this.jLabel7.setText("x Php     20.00");
        this.ttwen.setEditable(false);
        this.ttwen.setFont(new Font("Tahoma", 1, 11));
        this.ttwen.setHorizontalAlignment(11);
        this.ttwen.setText("0.00");
        this.ttwen.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                TransferBreakdown.this.ttwenActionPerformed(evt);
            }
        });
        this.ten.setHorizontalAlignment(11);
        this.ten.setText("0");
        this.ten.setNextFocusableComponent(this.five);
        this.ten.addFocusListener(new FocusAdapter() {
            public void focusGained(FocusEvent evt) {
                TransferBreakdown.this.tenFocusGained(evt);
            }

            public void focusLost(FocusEvent evt) {
                TransferBreakdown.this.tenFocusLost(evt);
            }
        });
        this.ten.addKeyListener(new KeyAdapter() {
            public void keyPressed(KeyEvent evt) {
                TransferBreakdown.this.tenKeyPressed(evt);
            }

            public void keyTyped(KeyEvent evt) {
                TransferBreakdown.this.tenKeyTyped(evt);
            }
        });
        this.jLabel8.setText("x Php     10.00");
        this.tten.setEditable(false);
        this.tten.setFont(new Font("Tahoma", 1, 11));
        this.tten.setHorizontalAlignment(11);
        this.tten.setText("0.00");
        this.tten.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                TransferBreakdown.this.ttenActionPerformed(evt);
            }
        });
        this.five.setHorizontalAlignment(11);
        this.five.setText("0");
        this.five.setNextFocusableComponent(this.one);
        this.five.addFocusListener(new FocusAdapter() {
            public void focusGained(FocusEvent evt) {
                TransferBreakdown.this.fiveFocusGained(evt);
            }

            public void focusLost(FocusEvent evt) {
                TransferBreakdown.this.fiveFocusLost(evt);
            }
        });
        this.five.addKeyListener(new KeyAdapter() {
            public void keyPressed(KeyEvent evt) {
                TransferBreakdown.this.fiveKeyPressed(evt);
            }

            public void keyTyped(KeyEvent evt) {
                TransferBreakdown.this.fiveKeyTyped(evt);
            }
        });
        this.jLabel9.setText("x Php       5.00");
        this.tfive.setEditable(false);
        this.tfive.setFont(new Font("Tahoma", 1, 11));
        this.tfive.setHorizontalAlignment(11);
        this.tfive.setText("0.00");
        this.tfive.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                TransferBreakdown.this.tfiveActionPerformed(evt);
            }
        });
        this.one.setHorizontalAlignment(11);
        this.one.setText("0");
        this.one.setNextFocusableComponent(this.cent);
        this.one.addFocusListener(new FocusAdapter() {
            public void focusGained(FocusEvent evt) {
                TransferBreakdown.this.oneFocusGained(evt);
            }

            public void focusLost(FocusEvent evt) {
                TransferBreakdown.this.oneFocusLost(evt);
            }
        });
        this.one.addKeyListener(new KeyAdapter() {
            public void keyPressed(KeyEvent evt) {
                TransferBreakdown.this.oneKeyPressed(evt);
            }

            public void keyTyped(KeyEvent evt) {
                TransferBreakdown.this.oneKeyTyped(evt);
            }
        });
        this.jLabel10.setText("x Php       1.00");
        this.tone.setEditable(false);
        this.tone.setFont(new Font("Tahoma", 1, 11));
        this.tone.setHorizontalAlignment(11);
        this.tone.setText("0.00");
        this.tone.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                TransferBreakdown.this.toneActionPerformed(evt);
            }
        });
        this.cent.setHorizontalAlignment(11);
        this.cent.setText("0");
        this.cent.setNextFocusableComponent(this.tenc);
        this.cent.addFocusListener(new FocusAdapter() {
            public void focusGained(FocusEvent evt) {
                TransferBreakdown.this.centFocusGained(evt);
            }

            public void focusLost(FocusEvent evt) {
                TransferBreakdown.this.centFocusLost(evt);
            }
        });
        this.cent.addKeyListener(new KeyAdapter() {
            public void keyPressed(KeyEvent evt) {
                TransferBreakdown.this.centKeyPressed(evt);
            }

            public void keyTyped(KeyEvent evt) {
                TransferBreakdown.this.centKeyTyped(evt);
            }
        });
        this.jLabel11.setText("x Php       0.25");
        this.tcent.setEditable(false);
        this.tcent.setFont(new Font("Tahoma", 1, 11));
        this.tcent.setHorizontalAlignment(11);
        this.tcent.setText("0.00");
        this.tcent.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                TransferBreakdown.this.tcentActionPerformed(evt);
            }
        });
        this.jButton1.setText("Save & Print Transfer Slip");
        this.jButton1.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                TransferBreakdown.this.jButton1ActionPerformed(evt);
            }
        });
        this.jLabel12.setHorizontalAlignment(11);
        this.jLabel12.setText("Total:");
        this.gtot.setEditable(false);
        this.gtot.setFont(new Font("Tahoma", 1, 11));
        this.gtot.setHorizontalAlignment(11);
        this.gtot.setText("0.00");
        this.gtot.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                TransferBreakdown.this.gtotActionPerformed(evt);
            }
        });
        this.tenc.setHorizontalAlignment(11);
        this.tenc.setText("0");
        this.tenc.setNextFocusableComponent(this.fivc);
        this.tenc.addFocusListener(new FocusAdapter() {
            public void focusGained(FocusEvent evt) {
                TransferBreakdown.this.tencFocusGained(evt);
            }

            public void focusLost(FocusEvent evt) {
                TransferBreakdown.this.tencFocusLost(evt);
            }
        });
        this.tenc.addKeyListener(new KeyAdapter() {
            public void keyPressed(KeyEvent evt) {
                TransferBreakdown.this.tencKeyPressed(evt);
            }

            public void keyTyped(KeyEvent evt) {
                TransferBreakdown.this.tencKeyTyped(evt);
            }
        });
        this.jLabel14.setText("x Php       0.10");
        this.ttce.setEditable(false);
        this.ttce.setFont(new Font("Tahoma", 1, 11));
        this.ttce.setHorizontalAlignment(11);
        this.ttce.setText("0.00");
        this.ttce.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                TransferBreakdown.this.ttceActionPerformed(evt);
            }
        });
        this.fivc.setHorizontalAlignment(11);
        this.fivc.setText("0");
        this.fivc.setNextFocusableComponent(this.jButton1);
        this.fivc.addFocusListener(new FocusAdapter() {
            public void focusGained(FocusEvent evt) {
                TransferBreakdown.this.fivcFocusGained(evt);
            }

            public void focusLost(FocusEvent evt) {
                TransferBreakdown.this.fivcFocusLost(evt);
            }
        });
        this.fivc.addKeyListener(new KeyAdapter() {
            public void keyPressed(KeyEvent evt) {
                TransferBreakdown.this.fivcKeyPressed(evt);
            }

            public void keyTyped(KeyEvent evt) {
                TransferBreakdown.this.fivcKeyTyped(evt);
            }
        });
        this.jLabel15.setText("x Php       0.05");
        this.tfce.setEditable(false);
        this.tfce.setFont(new Font("Tahoma", 1, 11));
        this.tfce.setHorizontalAlignment(11);
        this.tfce.setText("0.00");
        this.tfce.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                TransferBreakdown.this.tfceActionPerformed(evt);
            }
        });
        GroupLayout layout = new GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING).addGroup(layout.createSequentialGroup().addContainerGap().addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING).addComponent(this.fivc, -2, 42, -2).addComponent(this.fhun, -2, 42, -2).addComponent(this.thun, -2, 42, -2).addComponent(this.hun, -2, 42, -2).addComponent(this.fif, -2, 42, -2).addComponent(this.twen, -2, 42, -2).addComponent(this.ten, -2, 42, -2).addComponent(this.five, -2, 42, -2).addComponent(this.one, -2, 42, -2).addComponent(this.cent, -2, 42, -2).addComponent(this.thou, -2, 42, -2).addComponent(this.tenc, -2, 42, -2)).addPreferredGap(LayoutStyle.ComponentPlacement.RELATED, 18, 32767).addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING).addComponent(this.jLabel2).addComponent(this.jLabel3).addComponent(this.jLabel4).addComponent(this.jLabel5).addComponent(this.jLabel6).addComponent(this.jLabel7).addComponent(this.jLabel8).addComponent(this.jLabel9).addComponent(this.jLabel10).addComponent(this.jLabel11).addComponent(this.jLabel14).addComponent(this.jLabel15, -2, 73, -2)).addPreferredGap(LayoutStyle.ComponentPlacement.RELATED, 6, 32767).addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING).addComponent(this.tfce, GroupLayout.Alignment.TRAILING, -2, 70, -2).addComponent(this.tfhun, GroupLayout.Alignment.TRAILING, -2, 70, -2).addComponent(this.tthun, GroupLayout.Alignment.TRAILING, -2, 70, -2).addComponent(this.tohun, GroupLayout.Alignment.TRAILING, -2, 70, -2).addComponent(this.tfif, GroupLayout.Alignment.TRAILING, -2, 70, -2).addComponent(this.ttwen, GroupLayout.Alignment.TRAILING, -2, 70, -2).addComponent(this.tten, GroupLayout.Alignment.TRAILING, -2, 70, -2).addComponent(this.tfive, GroupLayout.Alignment.TRAILING, -2, 70, -2).addComponent(this.tone, GroupLayout.Alignment.TRAILING, -2, 70, -2).addComponent(this.tcent, GroupLayout.Alignment.TRAILING, -2, 70, -2).addComponent(this.tthou, GroupLayout.Alignment.TRAILING, -2, 70, -2).addComponent(this.ttce, GroupLayout.Alignment.TRAILING, -2, 70, -2)).addContainerGap()).addGroup(GroupLayout.Alignment.TRAILING, layout.createSequentialGroup().addContainerGap().addComponent(this.jLabel12, -2, 118, -2).addPreferredGap(LayoutStyle.ComponentPlacement.RELATED, -1, 32767).addComponent(this.gtot, -2, 70, -2).addGap(10, 10, 10)).addGroup(layout.createSequentialGroup().addGap(10, 10, 10).addComponent(this.jLabel1, -1, -1, 32767).addGap(10, 10, 10)).addGroup(layout.createSequentialGroup().addContainerGap().addComponent(this.jSeparator1).addContainerGap()).addGroup(layout.createSequentialGroup().addContainerGap().addComponent(this.jButton1, -1, -1, 32767).addContainerGap()));
        layout.setVerticalGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING).addGroup(layout.createSequentialGroup().addContainerGap().addComponent(this.jLabel1).addPreferredGap(LayoutStyle.ComponentPlacement.RELATED).addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE).addComponent(this.thou, -2, -1, -2).addComponent(this.jLabel2).addComponent(this.tthou, -2, -1, -2)).addPreferredGap(LayoutStyle.ComponentPlacement.RELATED).addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE).addComponent(this.fhun, -2, -1, -2).addComponent(this.jLabel3).addComponent(this.tfhun, -2, -1, -2)).addPreferredGap(LayoutStyle.ComponentPlacement.RELATED).addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE).addComponent(this.thun, -2, -1, -2).addComponent(this.jLabel4).addComponent(this.tthun, -2, -1, -2)).addPreferredGap(LayoutStyle.ComponentPlacement.RELATED).addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE).addComponent(this.hun, -2, -1, -2).addComponent(this.jLabel5).addComponent(this.tohun, -2, -1, -2)).addPreferredGap(LayoutStyle.ComponentPlacement.RELATED).addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE).addComponent(this.fif, -2, -1, -2).addComponent(this.jLabel6).addComponent(this.tfif, -2, -1, -2)).addPreferredGap(LayoutStyle.ComponentPlacement.RELATED).addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE).addComponent(this.twen, -2, -1, -2).addComponent(this.jLabel7).addComponent(this.ttwen, -2, -1, -2)).addPreferredGap(LayoutStyle.ComponentPlacement.RELATED).addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE).addComponent(this.ten, -2, -1, -2).addComponent(this.jLabel8).addComponent(this.tten, -2, -1, -2)).addPreferredGap(LayoutStyle.ComponentPlacement.RELATED).addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE).addComponent(this.five, -2, -1, -2).addComponent(this.jLabel9).addComponent(this.tfive, -2, -1, -2)).addPreferredGap(LayoutStyle.ComponentPlacement.RELATED).addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE).addComponent(this.one, -2, -1, -2).addComponent(this.jLabel10).addComponent(this.tone, -2, -1, -2)).addPreferredGap(LayoutStyle.ComponentPlacement.RELATED).addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE).addComponent(this.cent, -2, -1, -2).addComponent(this.jLabel11).addComponent(this.tcent, -2, -1, -2)).addPreferredGap(LayoutStyle.ComponentPlacement.RELATED).addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE).addComponent(this.tenc, -2, -1, -2).addComponent(this.jLabel14).addComponent(this.ttce, -2, -1, -2)).addPreferredGap(LayoutStyle.ComponentPlacement.RELATED).addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE).addComponent(this.fivc, -2, -1, -2).addComponent(this.jLabel15).addComponent(this.tfce, -2, -1, -2)).addPreferredGap(LayoutStyle.ComponentPlacement.RELATED).addComponent(this.jSeparator1, -2, 10, -2).addPreferredGap(LayoutStyle.ComponentPlacement.RELATED, -1, 32767).addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE).addComponent(this.gtot, -2, -1, -2).addComponent(this.jLabel12)).addPreferredGap(LayoutStyle.ComponentPlacement.RELATED).addComponent(this.jButton1).addContainerGap()));
        pack();
    }

    private void tthouActionPerformed(ActionEvent evt) {
    }

    private void tfhunActionPerformed(ActionEvent evt) {
    }

    private void tthunActionPerformed(ActionEvent evt) {
    }

    private void tohunActionPerformed(ActionEvent evt) {
    }

    private void tfifActionPerformed(ActionEvent evt) {
    }

    private void ttwenActionPerformed(ActionEvent evt) {
    }

    private void ttenActionPerformed(ActionEvent evt) {
    }

    private void tfiveActionPerformed(ActionEvent evt) {
    }

    private void toneActionPerformed(ActionEvent evt) {
    }

    private void tcentActionPerformed(ActionEvent evt) {
    }

    private void jButton1ActionPerformed(ActionEvent evt) {
        setCancelled(false);
        if (getTransfer() != Double.parseDouble(this.gtot.getText().replace(",", ""))) {
            JOptionPane.showMessageDialog(null, "Cash breakdown and Amount to be transferred did not match. \n Please make sure that Cash Breakdown is equal to the amount to be transfer.", "Cash Transfer Breakdown", 1);
        } else {
            setValuesEntered(true);
            try {
                Connection connect = DriverManager.getConnection(this.driver, this.f_user, this.f_pass);
                Statement state = connect.createStatement();
                String query = "Insert into " + getDatabase() + ".x_breakdown (bd_date, v_no, bd_thou, bd_fhun, bd_thun, bd_hund, bd_fift, bd_twen, bd_ten, bd_five, bd_one, bd_cen, bd_tenc, bd_fivc, bd_total) values ('" + this.dateHelp.getDateTime() + "', '" + getXnum() + "', " + this.thou.getText().replace(",", "") + ", " + this.fhun.getText().replace(",", "") + ", " + this.thun.getText().replace(",", "") + ", " + this.hun.getText().replace(",", "") + ", " + this.fif.getText().replace(",", "") + ", " + this.twen.getText().replace(",", "") + ", " + this.ten.getText().replace(",", "") + ", " + this.five.getText().replace(",", "") + ", " + this.one.getText().replace(",", "") + ", " + this.cent.getText().replace(",", "") + ", " + this.tenc.getText().replace(",", "") + ", " + this.fivc.getText().replace(",", "") + ", " + this.gtot.getText().replace(",", "") + ")";
                System.out.println(query);
                state.executeUpdate(query);
                state.close();
                connect.close();
                File tempFolder = new File("C:\\MPIS\\tempRep\\transfers");
                if (!tempFolder.exists()) {
                    tempFolder.mkdir();
                }
                ReportPrinter pr = new ReportPrinter();
                Map<Object, Object> parameters = new HashMap<>();
                parameters.put("XNO", getXnum());
//                if (this.con.getProp("branch").equalsIgnoreCase("Tangos")) {
                    pr.setDestination("C:\\MPIS\\tempRep\\transfers\\" + getRefNo() + ".pdf");
                    if (this.fromFundName.equalsIgnoreCase("") && this.toFundName.equalsIgnoreCase("")) {
                        if (getDatabase().equalsIgnoreCase("cashtransactions")) {
                            pr.printReport("/Reports/transferslip2.jrxml", parameters);
                        } else if (getDatabase().equalsIgnoreCase("palawan")) {
                            pr.printReport("/Reports/transferslip2-palawan.jrxml", parameters);
                        } else if (getDatabase().equalsIgnoreCase("forex")) {
                            pr.printReport("/Reports/transferslip2-forex.jrxml", parameters);
                        }
                    } else {
                        if (getDatabase().equalsIgnoreCase("cashtransactions")) {
                            pr.printReport("/Reports/transferslip.jrxml", parameters);
                        } else if (getDatabase().equalsIgnoreCase("palawan")) {
                            pr.printReport("/Reports/transferslip-palawan.jrxml", parameters);
                        } else if (getDatabase().equalsIgnoreCase("forex")) {
                            pr.printReport("/Reports/transferslip-forex.jrxml", parameters);
                        }
                    }
//                } else {
//                    pr.setDestination("C:\\MPIS\\tempRep\\transfers\\" + getRefNo() + ".xls");
//                    if (this.fromFundName.equalsIgnoreCase("") && this.toFundName.equalsIgnoreCase("")) {
//                        System.out.println("whats the choice here3: " + getDatabase());
//                        if (getDatabase().equalsIgnoreCase("cashtransactions")) {
//                            pr.printDotMatrix("/Reports/transferslip2.jrxml", parameters);
//                        } else if (getDatabase().equalsIgnoreCase("palawan")) {
//                            pr.printDotMatrix("/Reports/transferslip2-palawan.jrxml", parameters);
//                        } else if (getDatabase().equalsIgnoreCase("forex")) {
//                            pr.printDotMatrix("/Reports/transferslip2-forex.jrxml", parameters);
//                        }
//                    } else {
//                        System.out.println("whats the choice here4: " + getDatabase());
//                        if (getDatabase().equalsIgnoreCase("cashtransactions")) {
//                            pr.printDotMatrix("/Reports/transferslip.jrxml", parameters);
//                        } else if (getDatabase().equalsIgnoreCase("palawan")) {
//                            pr.printDotMatrix("/Reports/transferslip-palawan.jrxml", parameters);
//                        } else if (getDatabase().equalsIgnoreCase("forex")) {
//                            pr.printDotMatrix("/Reports/transferslip-forex.jrxml", parameters);
//                        }
//                    }
//                }
                setVisible(false);
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(null, "Cash Breakdown Save Error.");
                Logger.getLogger(TransferBreakdown.class.getName()).log(Level.SEVERE, (String) null, ex);
            }
        }
    }

    private void thouKeyTyped(KeyEvent evt) {
        if (!Character.isDigit(evt.getKeyChar())) {
            evt.consume();
        }
    }

    private void fhunKeyTyped(KeyEvent evt) {
        if (!Character.isDigit(evt.getKeyChar())) {
            evt.consume();
        }
    }

    private void thunKeyTyped(KeyEvent evt) {
        if (!Character.isDigit(evt.getKeyChar())) {
            evt.consume();
        }
    }

    private void hunKeyTyped(KeyEvent evt) {
        if (!Character.isDigit(evt.getKeyChar())) {
            evt.consume();
        }
    }

    private void fifKeyTyped(KeyEvent evt) {
        if (!Character.isDigit(evt.getKeyChar())) {
            evt.consume();
        }
    }

    private void twenKeyTyped(KeyEvent evt) {
        if (!Character.isDigit(evt.getKeyChar())) {
            evt.consume();
        }
    }

    private void tenKeyTyped(KeyEvent evt) {
        if (!Character.isDigit(evt.getKeyChar())) {
            evt.consume();
        }
    }

    private void fiveKeyTyped(KeyEvent evt) {
        if (!Character.isDigit(evt.getKeyChar())) {
            evt.consume();
        }
    }

    private void oneKeyTyped(KeyEvent evt) {
        if (!Character.isDigit(evt.getKeyChar())) {
            evt.consume();
        }
    }

    private void centKeyTyped(KeyEvent evt) {
        if (!Character.isDigit(evt.getKeyChar())) {
            evt.consume();
        }
    }

    private void gtotActionPerformed(ActionEvent evt) {
    }

    private void thouFocusLost(FocusEvent evt) {
        this.tthou.setText(this.formatter.format(Double.parseDouble(this.thou.getText().replace(",", "")) * 1000.0D));
        this.gtot.setText(this.formatter.format(Double.parseDouble(this.tthou.getText().replace(",", "")) + Double.parseDouble(this.tfhun.getText().replace(",", "")) + Double.parseDouble(this.tthun.getText().replace(",", "")) + Double.parseDouble(this.tohun.getText().replace(",", "")) + Double.parseDouble(this.tfif.getText().replace(",", "")) + Double.parseDouble(this.ttwen.getText().replace(",", "")) + Double.parseDouble(this.tten.getText().replace(",", "")) + Double.parseDouble(this.tfive.getText().replace(",", "")) + Double.parseDouble(this.tone.getText().replace(",", "")) + Double.parseDouble(this.tcent.getText().replace(",", "")) + Double.parseDouble(this.ttce.getText().replace(",", "")) + Double.parseDouble(this.tfce.getText().replace(",", ""))));
    }

    private void fhunFocusLost(FocusEvent evt) {
        this.tfhun.setText(this.formatter.format(Double.parseDouble(this.fhun.getText().replace(",", "")) * 500.0D));
        this.gtot.setText(this.formatter.format(Double.parseDouble(this.tthou.getText().replace(",", "")) + Double.parseDouble(this.tfhun.getText().replace(",", "")) + Double.parseDouble(this.tthun.getText().replace(",", "")) + Double.parseDouble(this.tohun.getText().replace(",", "")) + Double.parseDouble(this.tfif.getText().replace(",", "")) + Double.parseDouble(this.ttwen.getText().replace(",", "")) + Double.parseDouble(this.tten.getText().replace(",", "")) + Double.parseDouble(this.tfive.getText().replace(",", "")) + Double.parseDouble(this.tone.getText().replace(",", "")) + Double.parseDouble(this.tcent.getText().replace(",", "")) + Double.parseDouble(this.ttce.getText().replace(",", "")) + Double.parseDouble(this.tfce.getText().replace(",", ""))));
    }

    private void thunFocusLost(FocusEvent evt) {
        this.tthun.setText(this.formatter.format(Double.parseDouble(this.thun.getText().replace(",", "")) * 200.0D));
        this.gtot.setText(this.formatter.format(Double.parseDouble(this.tthou.getText().replace(",", "")) + Double.parseDouble(this.tfhun.getText().replace(",", "")) + Double.parseDouble(this.tthun.getText().replace(",", "")) + Double.parseDouble(this.tohun.getText().replace(",", "")) + Double.parseDouble(this.tfif.getText().replace(",", "")) + Double.parseDouble(this.ttwen.getText().replace(",", "")) + Double.parseDouble(this.tten.getText().replace(",", "")) + Double.parseDouble(this.tfive.getText().replace(",", "")) + Double.parseDouble(this.tone.getText().replace(",", "")) + Double.parseDouble(this.tcent.getText().replace(",", "")) + Double.parseDouble(this.ttce.getText().replace(",", "")) + Double.parseDouble(this.tfce.getText().replace(",", ""))));
    }

    private void hunFocusLost(FocusEvent evt) {
        this.tohun.setText(this.formatter.format(Double.parseDouble(this.hun.getText().replace(",", "")) * 100.0D));
        this.gtot.setText(this.formatter.format(Double.parseDouble(this.tthou.getText().replace(",", "")) + Double.parseDouble(this.tfhun.getText().replace(",", "")) + Double.parseDouble(this.tthun.getText().replace(",", "")) + Double.parseDouble(this.tohun.getText().replace(",", "")) + Double.parseDouble(this.tfif.getText().replace(",", "")) + Double.parseDouble(this.ttwen.getText().replace(",", "")) + Double.parseDouble(this.tten.getText().replace(",", "")) + Double.parseDouble(this.tfive.getText().replace(",", "")) + Double.parseDouble(this.tone.getText().replace(",", "")) + Double.parseDouble(this.tcent.getText().replace(",", "")) + Double.parseDouble(this.ttce.getText().replace(",", "")) + Double.parseDouble(this.tfce.getText().replace(",", ""))));
    }

    private void fifFocusLost(FocusEvent evt) {
        this.tfif.setText(this.formatter.format(Double.parseDouble(this.fif.getText().replace(",", "")) * 50.0D));
        this.gtot.setText(this.formatter.format(Double.parseDouble(this.tthou.getText().replace(",", "")) + Double.parseDouble(this.tfhun.getText().replace(",", "")) + Double.parseDouble(this.tthun.getText().replace(",", "")) + Double.parseDouble(this.tohun.getText().replace(",", "")) + Double.parseDouble(this.tfif.getText().replace(",", "")) + Double.parseDouble(this.ttwen.getText().replace(",", "")) + Double.parseDouble(this.tten.getText().replace(",", "")) + Double.parseDouble(this.tfive.getText().replace(",", "")) + Double.parseDouble(this.tone.getText().replace(",", "")) + Double.parseDouble(this.tcent.getText().replace(",", "")) + Double.parseDouble(this.ttce.getText().replace(",", "")) + Double.parseDouble(this.tfce.getText().replace(",", ""))));
    }

    private void twenFocusLost(FocusEvent evt) {
        this.ttwen.setText(this.formatter.format(Double.parseDouble(this.twen.getText().replace(",", "")) * 20.0D));
        this.gtot.setText(this.formatter.format(Double.parseDouble(this.tthou.getText().replace(",", "")) + Double.parseDouble(this.tfhun.getText().replace(",", "")) + Double.parseDouble(this.tthun.getText().replace(",", "")) + Double.parseDouble(this.tohun.getText().replace(",", "")) + Double.parseDouble(this.tfif.getText().replace(",", "")) + Double.parseDouble(this.ttwen.getText().replace(",", "")) + Double.parseDouble(this.tten.getText().replace(",", "")) + Double.parseDouble(this.tfive.getText().replace(",", "")) + Double.parseDouble(this.tone.getText().replace(",", "")) + Double.parseDouble(this.tcent.getText().replace(",", "")) + Double.parseDouble(this.ttce.getText().replace(",", "")) + Double.parseDouble(this.tfce.getText().replace(",", ""))));
    }

    private void tenFocusLost(FocusEvent evt) {
        this.tten.setText(this.formatter.format(Double.parseDouble(this.ten.getText().replace(",", "")) * 10.0D));
        this.gtot.setText(this.formatter.format(Double.parseDouble(this.tthou.getText().replace(",", "")) + Double.parseDouble(this.tfhun.getText().replace(",", "")) + Double.parseDouble(this.tthun.getText().replace(",", "")) + Double.parseDouble(this.tohun.getText().replace(",", "")) + Double.parseDouble(this.tfif.getText().replace(",", "")) + Double.parseDouble(this.ttwen.getText().replace(",", "")) + Double.parseDouble(this.tten.getText().replace(",", "")) + Double.parseDouble(this.tfive.getText().replace(",", "")) + Double.parseDouble(this.tone.getText().replace(",", "")) + Double.parseDouble(this.tcent.getText().replace(",", "")) + Double.parseDouble(this.ttce.getText().replace(",", "")) + Double.parseDouble(this.tfce.getText().replace(",", ""))));
    }

    private void fiveFocusLost(FocusEvent evt) {
        this.tfive.setText(this.formatter.format(Double.parseDouble(this.five.getText().replace(",", "")) * 5.0D));
        this.gtot.setText(this.formatter.format(Double.parseDouble(this.tthou.getText().replace(",", "")) + Double.parseDouble(this.tfhun.getText().replace(",", "")) + Double.parseDouble(this.tthun.getText().replace(",", "")) + Double.parseDouble(this.tohun.getText().replace(",", "")) + Double.parseDouble(this.tfif.getText().replace(",", "")) + Double.parseDouble(this.ttwen.getText().replace(",", "")) + Double.parseDouble(this.tten.getText().replace(",", "")) + Double.parseDouble(this.tfive.getText().replace(",", "")) + Double.parseDouble(this.tone.getText().replace(",", "")) + Double.parseDouble(this.tcent.getText().replace(",", "")) + Double.parseDouble(this.ttce.getText().replace(",", "")) + Double.parseDouble(this.tfce.getText().replace(",", ""))));
    }

    private void oneFocusLost(FocusEvent evt) {
        this.tone.setText(this.formatter.format(Double.parseDouble(this.one.getText().replace(",", ""))));
        this.gtot.setText(this.formatter.format(Double.parseDouble(this.tthou.getText().replace(",", "")) + Double.parseDouble(this.tfhun.getText().replace(",", "")) + Double.parseDouble(this.tthun.getText().replace(",", "")) + Double.parseDouble(this.tohun.getText().replace(",", "")) + Double.parseDouble(this.tfif.getText().replace(",", "")) + Double.parseDouble(this.ttwen.getText().replace(",", "")) + Double.parseDouble(this.tten.getText().replace(",", "")) + Double.parseDouble(this.tfive.getText().replace(",", "")) + Double.parseDouble(this.tone.getText().replace(",", "")) + Double.parseDouble(this.tcent.getText().replace(",", "")) + Double.parseDouble(this.ttce.getText().replace(",", "")) + Double.parseDouble(this.tfce.getText().replace(",", ""))));
    }

    private void centFocusLost(FocusEvent evt) {
        this.tcent.setText(this.formatter.format(Double.parseDouble(this.cent.getText().replace(",", "")) * 0.25D));
        this.gtot.setText(this.formatter.format(Double.parseDouble(this.tthou.getText().replace(",", "")) + Double.parseDouble(this.tfhun.getText().replace(",", "")) + Double.parseDouble(this.tthun.getText().replace(",", "")) + Double.parseDouble(this.tohun.getText().replace(",", "")) + Double.parseDouble(this.tfif.getText().replace(",", "")) + Double.parseDouble(this.ttwen.getText().replace(",", "")) + Double.parseDouble(this.tten.getText().replace(",", "")) + Double.parseDouble(this.tfive.getText().replace(",", "")) + Double.parseDouble(this.tone.getText().replace(",", "")) + Double.parseDouble(this.tcent.getText().replace(",", "")) + Double.parseDouble(this.ttce.getText().replace(",", "")) + Double.parseDouble(this.tfce.getText().replace(",", ""))));
    }

    private void thouFocusGained(FocusEvent evt) {
        this.thou.selectAll();
    }

    private void fhunFocusGained(FocusEvent evt) {
        this.fhun.selectAll();
    }

    private void thunFocusGained(FocusEvent evt) {
        this.thun.selectAll();
    }

    private void hunFocusGained(FocusEvent evt) {
        this.hun.selectAll();
    }

    private void fifFocusGained(FocusEvent evt) {
        this.fif.selectAll();
    }

    private void twenFocusGained(FocusEvent evt) {
        this.twen.selectAll();
    }

    private void tenFocusGained(FocusEvent evt) {
        this.ten.selectAll();
    }

    private void fiveFocusGained(FocusEvent evt) {
        this.five.selectAll();
    }

    private void oneFocusGained(FocusEvent evt) {
        this.one.selectAll();
    }

    private void centFocusGained(FocusEvent evt) {
        this.cent.selectAll();
    }

    private void tencFocusGained(FocusEvent evt) {
        this.tenc.selectAll();
    }

    private void tencFocusLost(FocusEvent evt) {
        this.ttce.setText(this.formatter.format(Double.parseDouble(this.tenc.getText().replace(",", "")) * 0.1D));
        this.gtot.setText(this.formatter.format(Double.parseDouble(this.tthou.getText().replace(",", "")) + Double.parseDouble(this.tfhun.getText().replace(",", "")) + Double.parseDouble(this.tthun.getText().replace(",", "")) + Double.parseDouble(this.tohun.getText().replace(",", "")) + Double.parseDouble(this.tfif.getText().replace(",", "")) + Double.parseDouble(this.ttwen.getText().replace(",", "")) + Double.parseDouble(this.tten.getText().replace(",", "")) + Double.parseDouble(this.tfive.getText().replace(",", "")) + Double.parseDouble(this.tone.getText().replace(",", "")) + Double.parseDouble(this.tcent.getText().replace(",", "")) + Double.parseDouble(this.ttce.getText().replace(",", "")) + Double.parseDouble(this.tfce.getText().replace(",", ""))));
    }

    private void tencKeyTyped(KeyEvent evt) {
        if (!Character.isDigit(evt.getKeyChar())) {
            evt.consume();
        }
    }

    private void ttceActionPerformed(ActionEvent evt) {
    }

    private void fivcFocusGained(FocusEvent evt) {
        this.fivc.selectAll();
    }

    private void fivcFocusLost(FocusEvent evt) {
        this.tfce.setText(this.formatter.format(Double.parseDouble(this.fivc.getText().replace(",", "")) * 0.05D));
        this.gtot.setText(this.formatter.format(Double.parseDouble(this.tthou.getText().replace(",", "")) + Double.parseDouble(this.tfhun.getText().replace(",", "")) + Double.parseDouble(this.tthun.getText().replace(",", "")) + Double.parseDouble(this.tohun.getText().replace(",", "")) + Double.parseDouble(this.tfif.getText().replace(",", "")) + Double.parseDouble(this.ttwen.getText().replace(",", "")) + Double.parseDouble(this.tten.getText().replace(",", "")) + Double.parseDouble(this.tfive.getText().replace(",", "")) + Double.parseDouble(this.tone.getText().replace(",", "")) + Double.parseDouble(this.tcent.getText().replace(",", "")) + Double.parseDouble(this.ttce.getText().replace(",", "")) + Double.parseDouble(this.tfce.getText().replace(",", ""))));
    }

    private void fivcKeyTyped(KeyEvent evt) {
        if (!Character.isDigit(evt.getKeyChar())) {
            evt.consume();
        }
    }

    private void tfceActionPerformed(ActionEvent evt) {
    }

    private void formWindowClosing(WindowEvent evt) {
    }

    private void thouKeyPressed(KeyEvent evt) {
        if (evt.getKeyCode() == 10) {
            thouFocusLost((FocusEvent) null);
        }
    }

    private void fhunKeyPressed(KeyEvent evt) {
        if (evt.getKeyCode() == 10) {
            fhunFocusLost((FocusEvent) null);
        }
    }

    private void thunKeyPressed(KeyEvent evt) {
        if (evt.getKeyCode() == 10) {
            thunFocusLost((FocusEvent) null);
        }
    }

    private void hunKeyPressed(KeyEvent evt) {
        if (evt.getKeyCode() == 10) {
            hunFocusLost((FocusEvent) null);
        }
    }

    private void fifKeyPressed(KeyEvent evt) {
        if (evt.getKeyCode() == 10) {
            fifFocusLost((FocusEvent) null);
        }
    }

    private void twenKeyPressed(KeyEvent evt) {
        if (evt.getKeyCode() == 10) {
            twenFocusLost((FocusEvent) null);
        }
    }

    private void tenKeyPressed(KeyEvent evt) {
        if (evt.getKeyCode() == 10) {
            tenFocusLost((FocusEvent) null);
        }
    }

    private void fiveKeyPressed(KeyEvent evt) {
        if (evt.getKeyCode() == 10) {
            fiveFocusLost((FocusEvent) null);
        }
    }

    private void oneKeyPressed(KeyEvent evt) {
        if (evt.getKeyCode() == 10) {
            oneFocusLost((FocusEvent) null);
        }
    }

    private void centKeyPressed(KeyEvent evt) {
        if (evt.getKeyCode() == 10) {
            centFocusLost((FocusEvent) null);
        }
    }

    private void tencKeyPressed(KeyEvent evt) {
        if (evt.getKeyCode() == 10) {
            tencFocusLost((FocusEvent) null);
        }
    }

    private void fivcKeyPressed(KeyEvent evt) {
        if (evt.getKeyCode() == 10) {
            fivcFocusLost((FocusEvent) null);
        }
    }

    public static void main(String[] args) {
        try {
            for (UIManager.LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(TransferBreakdown.class.getName()).log(Level.SEVERE, (String) null, ex);
        } catch (InstantiationException ex) {
            Logger.getLogger(TransferBreakdown.class.getName()).log(Level.SEVERE, (String) null, ex);
        } catch (IllegalAccessException ex) {
            Logger.getLogger(TransferBreakdown.class.getName()).log(Level.SEVERE, (String) null, ex);
        } catch (UnsupportedLookAndFeelException ex) {
            Logger.getLogger(TransferBreakdown.class.getName()).log(Level.SEVERE, (String) null, ex);
        }
        EventQueue.invokeLater(new Runnable() {
            public void run() {
                TransferBreakdown dialog = new TransferBreakdown(new JFrame(), true);
                dialog.addWindowListener(new WindowAdapter() {
                    public void windowClosing(WindowEvent e) {
                        System.exit(0);
                    }
                });
                dialog.setVisible(true);
            }
        });
    }

    public boolean isValuesEntered() {
        return this.valuesEntered;
    }

    public void setValuesEntered(boolean valuesEntered) {
        this.valuesEntered = valuesEntered;
    }

    public double getTransfer() {
        return this.transfer;
    }

    public void setTransfer(double transfer) {
        this.transfer = transfer;
    }

    public String getXnum() {
        return this.xnum;
    }

    public void setXnum(String xnum) {
        this.xnum = xnum;
    }

    public boolean isCancelled() {
        return this.cancelled;
    }

    public void setCancelled(boolean cancelled) {
        this.cancelled = cancelled;
    }

    public String getRefNo() {
        return this.refNo;
    }

    public void setRefNo(String refNo) {
        this.refNo = refNo;
    }

    public String getFromFundName() {
        return this.fromFundName;
    }

    public void setFromFundName(String fromFundName) {
        this.fromFundName = fromFundName;
    }

    public String getToFundName() {
        return this.toFundName;
    }

    public void setToFundName(String toFundName) {
        this.toFundName = toFundName;
    }

    public String getDatabase() {
        return this.database;
    }

    public void setDatabase(String database) {
        this.database = database;
    }
}
