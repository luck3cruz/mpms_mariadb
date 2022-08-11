package mpis_mariadb_2;

import java.awt.Component;
import java.awt.EventQueue;
import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.print.Doc;
import javax.print.DocFlavor;
import javax.print.DocPrintJob;
import javax.print.PrintException;
import javax.print.PrintService;
import javax.print.PrintServiceLookup;
import javax.print.SimpleDoc;
import javax.print.attribute.HashPrintRequestAttributeSet;
import javax.print.attribute.PrintRequestAttributeSet;
import javax.print.attribute.standard.Sides;
import javax.swing.DefaultComboBoxModel;
import javax.swing.GroupLayout;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.LayoutStyle;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

public class ReprintPettyCash extends JDialog {

    private JButton jButton1;

    private JButton jButton2;

    private JComboBox jComboBox1;

    private JLabel jLabel1;

    Config con = new Config();
    private final String driver = "jdbc:mariadb://" + this.con.getProp("IP") + ":" + this.con.getProp("port") + "/merlininventorydatabase";
    private final String f_user = this.con.getProp("username");
    private final String f_pass = this.con.getProp("password");

    public ReprintPettyCash(Frame parent, boolean modal) {
        super(parent, modal);
        initComponents();
    }

    public void updateCombo() throws SQLException {
        this.jComboBox1.removeAllItems();
//    File file = new File("C:\\MPIS\\tempRep\\pettycash");
//    File[] contents = file.listFiles();
//    if (contents != null)
//      for (File f : contents)
//        FileOutputStream output = null;

        Connection connect = DriverManager.getConnection(this.driver, this.f_user, this.f_pass);
        Statement state = connect.createStatement();
        String query = "Select pty_voucher from cashtransactions.petty_cash where pty_date = '" + new DateHelper().getCurrentDate() + "'";
        System.out.println(query);
        ResultSet rset = state.executeQuery(query);
        while (rset.next()) {
            jComboBox1.addItem(rset.getString(1));
        }
        state.close();
        connect.close();
    }


    private void initComponents() {
        this.jLabel1 = new JLabel();
        this.jComboBox1 = new JComboBox();
        this.jButton1 = new JButton();
        this.jButton2 = new JButton();
        setDefaultCloseOperation(2);
        setTitle("Reprint Petty Cash Transaction");
        this.jLabel1.setText("Please select 'Voucher No.' of Petty Cash Transaction you want to reprint:");
        this.jComboBox1.setModel(new DefaultComboBoxModel<>(new String[]{"Item 1", "Item 2", "Item 3", "Item 4"}));
        this.jComboBox1.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                ReprintPettyCash.this.jComboBox1ActionPerformed(evt);
            }
        });
        this.jButton1.setText("OK");
        this.jButton1.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                ReprintPettyCash.this.jButton1ActionPerformed(evt);
            }
        });
        this.jButton2.setText("Cancel");
        this.jButton2.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                ReprintPettyCash.this.jButton2ActionPerformed(evt);
            }
        });
        GroupLayout layout = new GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING).addGroup(layout.createSequentialGroup().addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING).addGroup(layout.createSequentialGroup().addGap(18, 18, 18).addComponent(this.jLabel1).addPreferredGap(LayoutStyle.ComponentPlacement.UNRELATED).addComponent(this.jComboBox1, -2, 110, -2)).addGroup(layout.createSequentialGroup().addGap(166, 166, 166).addComponent(this.jButton1).addPreferredGap(LayoutStyle.ComponentPlacement.UNRELATED).addComponent(this.jButton2))).addContainerGap(-1, 32767)));
        layout.linkSize(0, new Component[]{this.jButton1, this.jButton2});
        layout.setVerticalGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING).addGroup(layout.createSequentialGroup().addContainerGap().addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE).addComponent(this.jLabel1).addComponent(this.jComboBox1, -2, -1, -2)).addPreferredGap(LayoutStyle.ComponentPlacement.UNRELATED).addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING).addComponent(this.jButton2).addComponent(this.jButton1)).addContainerGap(-1, 32767)));
        pack();
    }

    private void jComboBox1ActionPerformed(ActionEvent evt) {
    }

    private void jButton1ActionPerformed(ActionEvent evt) {
        ReportPrinter repPrnt = new ReportPrinter();
        Map<Object, Object> parameters = new HashMap<>();
        parameters.put("VOUCHER", jComboBox1.getSelectedItem().toString());
        repPrnt.printReport("/Reports/ptc_voucher.jrxml", parameters);
        setVisible(false);
        setVisible(false);
    }

    private void jButton2ActionPerformed(ActionEvent evt) {
        setVisible(false);
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
            Logger.getLogger(ReprintPettyCash.class.getName()).log(Level.SEVERE, (String) null, ex);
        } catch (InstantiationException ex) {
            Logger.getLogger(ReprintPettyCash.class.getName()).log(Level.SEVERE, (String) null, ex);
        } catch (IllegalAccessException ex) {
            Logger.getLogger(ReprintPettyCash.class.getName()).log(Level.SEVERE, (String) null, ex);
        } catch (UnsupportedLookAndFeelException ex) {
            Logger.getLogger(ReprintPettyCash.class.getName()).log(Level.SEVERE, (String) null, ex);
        }
        EventQueue.invokeLater(new Runnable() {
            public void run() {
                ReprintPettyCash dialog = new ReprintPettyCash(new JFrame(), true);
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
