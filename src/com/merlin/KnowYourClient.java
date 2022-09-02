/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JPanel.java to edit this template
 */
package com.merlin;

import com.mxrck.autocompleter.TextAutoCompleter;
//import com.mysql.jdbc.Statement;
import java.awt.Color;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.RenderingHints;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.sql.*;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.*;
import java.util.TreeSet;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.SwingConstants;
import javax.swing.UIManager;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;
import org.imgscalr.Scalr;

/**
 *
 * @author Lucky
 */
public class KnowYourClient extends javax.swing.JPanel {

    /**
     * @return the clientName
     */
    public String getClientName() {
        return clientName;
    }

    /**
     * @param clientName the clientName to set
     */
    public void setClientName(String clientName) {
        this.clientName = clientName;
    }

    /**
     * @return the clientNumber
     */
    public String getClientNumber() {
        return clientNumber;
    }

    /**
     * @param clientNumber the clientNumber to set
     */
    public void setClientNumber(String clientNumber) {
        this.clientNumber = clientNumber;
    }

    /**
     * @return the clientAddress
     */
    public String getClientAddress() {
        return clientAddress;
    }

    /**
     * @param clientAddress the clientAddress to set
     */
    public void setClientAddress(String clientAddress) {
        this.clientAddress = clientAddress;
    }

    /**
     * @return the createLoan
     */
    public boolean isCreateLoan() {
        return createLoan;
    }

    /**
     * @param createLoan the createLoan to set
     */
    public void setCreateLoan(boolean createLoan) {
        this.createLoan = createLoan;
    }

    /**
     * @return the provList
     */
    public String[] getProvList() {
        return provList;
    }

    /**
     * @param provList the provList to set
     */
    public void setProvList(String[] provList) {
        this.provList = provList;
    }

    /**
     * @return the townList
     */
    public String[] getTownList() {
        return townList;
    }

    /**
     * @param townList the townList to set
     */
    public void setTownList(String[] townList) {
        this.townList = townList;
    }

    /**
     * @return the brgyList
     */
    public String[] getBrgyList() {
        return brgyList;
    }

    /**
     * @param brgyList the brgyList to set
     */
    public void setBrgyList(String[] brgyList) {
        this.brgyList = brgyList;
    }

    /**
     * @return the fileName
     */
    public String getFileName() {
        return fileName;
    }

    /**
     * @param fileName the fileName to set
     */
    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    /**
     * @return the photopath
     */
    public String getPhotopath() {
        return photopath;
    }

    /**
     * @param photopath the photopath to set
     */
    public void setPhotopath(String photopath) {
        this.photopath = photopath;
    }

    /**
     * @return the idPath
     */
    public String getIdPath() {
        return idPath;
    }

    /**
     * @param idPath the idPath to set
     */
    public void setIdPath(String idPath) {
        this.idPath = idPath;
    }

    /**
     * @return the recordRetrieved
     */
    public boolean isRecordRetrieved() {
        return recordRetrieved;
    }

    /**
     * @param recordRetrieved the recordRetrieved to set
     */
    public void setRecordRetrieved(boolean recordRetrieved) {
        this.recordRetrieved = recordRetrieved;
    }

    /**
     * @return the photoTaken
     */
    public boolean isPhotoTaken() {
        return photoTaken;
    }

    /**
     * @param photoTaken the photoTaken to set
     */
    public void setPhotoTaken(boolean photoTaken) {
        this.photoTaken = photoTaken;
    }

    /**
     * @return the idTaken
     */
    public boolean isIdTaken() {
        return idTaken;
    }

    /**
     * @param idTaken the idTaken to set
     */
    public void setIdTaken(boolean idTaken) {
        this.idTaken = idTaken;
    }

    /**
     * Creates new form KYC
     */
    public KnowYourClient() {
        initComponents();
        try {
            initializeProvCombo();
            initializeTownCombo(getProvList()[0]);
            initializeBrgyCombo(getProvList()[0], getTownList()[0]);
            initIDsCombo();
            initSOICombo();
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "System Error! Please report to your System Support.\n" + ex);
            Logger.getLogger(KnowYourClient.class.getName()).log(Level.SEVERE, null, ex);
        }
        enableTextsCombos(false);
        findMatch();
//        findMatch2();
//        findMatch3();
        populateTextbox();
//        populateClients();
        populateNations();
        designTable(jTable1);
//        File rootDir = File.listRoots()[0];
//        System.out.println("paths \n" + rootDir.getAbsolutePath());
    }
    
    public void initializeConfigurations() {
        Config con = new Config();
        if (con.getProp("username") == null) {
            
        }
    }
    
    private final Config con = new Config();
    private final String driver = "jdbc:mariadb://" + con.getProp("IP") + ":" + con.getProp("port") + "/merlininventorydatabase";
    private final String f_user = con.getProp("username");
    private final String f_pass = con.getProp("password");
    private String fileName = "";
    public SimpleDateFormat dateformatter = new SimpleDateFormat("yyyy-MM-dd");
    private String[] names = new String[1];
    private String[] nations = new String[1];
    private String[] nature = new String[1];
    private ArrayList<String[]> all = new ArrayList<String[]>();
//    private ArrayList<String[]> allnations = new ArrayList<String[]>();
//    private ArrayList<String[]> allnatures = new ArrayList<String[]>();
    private String[] provList; 
    private String[] townList; 
    private String[] brgyList;
    private String[] loanList;
    private String photopath = "";
    private String idPath = "";
    private Client cl1 = new Client();
    private boolean recordRetrieved = false;
    private boolean photoTaken = true;
    private boolean idTaken = false;
    private ImageIcon viewPhoto;
    private ImageIcon viewID;
    private Set<String> s;
//    private Set<String> t;
    private Set<String> u;
    private boolean createLoan = false;
    private String clientName = "";
    private String clientNumber = "";
    private String clientAddress = "";
    
    public void connectClientToLoans(String idkey, String[] loans) throws SQLException{
        Connection connect = DriverManager.getConnection(driver, f_user, f_pass);
        Statement state = connect.createStatement();
        String updateSt = "Update merlininventorydatabase.client_info set client_no = '" + idkey + "' where item_code_a IN (";
        for (int i=0; i < loans.length; i++) {
            updateSt = updateSt + "'" + loans[i];
            if (i != loans.length-1) {
                updateSt = updateSt + "', ";
            }
        }
        updateSt = updateSt + "')";
        state.executeUpdate(updateSt);
    }
    
    public void initializeProvCombo() throws SQLException{
        String qry1 = "select distinct(REPLACE(prov, 'Ã±', 'ñ')) from sort_add order by prov";
        String qry1c = "select count(REPLACE(prov, 'Ã±', 'ñ')) from sort_add order by prov";
        Connection connect = DriverManager.getConnection(driver, f_user, f_pass);
        Statement state = connect.createStatement();
        ResultSet rset = state.executeQuery(qry1c);
        int provCount = 0;
        while (rset.next()) {
             provCount = rset.getInt(1);
        }
        setProvList(new String[provCount]);
        rset = state.executeQuery(qry1);
        int i = 0;
        while (rset.next()) {
            getProvList()[i] = rset.getString(1);
            i++;
        }
        for (String prov : getProvList()) {
            provCombo.addItem(prov);
        }
        rset.close();
        state.close();
        connect.close();
    } 
    
    public void initializeTownCombo(String province) throws SQLException {
        Connection connect = DriverManager.getConnection(driver, f_user, f_pass);
        Statement state = connect.createStatement();
        ResultSet rset;
        
        if (getProvList().length > 0) {
            String qry2 = "select distinct(REPLACE(town, 'Ã±', 'ñ')) from sort_add where prov = '" + province + "' order by town";
            String qry2c = "select count(distinct(REPLACE(town, 'Ã±', 'ñ'))) from sort_add where prov = '" + province + "' order by town";
            state = connect.createStatement();
            rset = state.executeQuery(qry2c);
            int townCount = 0;
            while (rset.next()) {
                townCount = rset.getInt(1);
            }
            setTownList(new String[townCount]);
            rset = state.executeQuery(qry2);
            int i = 0;
            while (rset.next()) {
                getTownList()[i] = rset.getString(1);
               i++;
            }
            state.close();
            rset.close();
            connect.close();
            
            townCombo.removeAllItems();
            for (String town : getTownList()) {
                townCombo.addItem(town);
            }
        }
    }
        
     public void initializeBrgyCombo(String province, String town) throws SQLException {
        Connection connect = DriverManager.getConnection(driver, f_user, f_pass);
        Statement state = connect.createStatement();
        ResultSet rset;

        if (getTownList().length > 0) {
            String qry3 = "select distinct(REPLACE(brgy, 'Ã±', 'ñ')) from sort_add where town = '" + town + "' and prov = '" + province + "' order by brgy";
            String qry3c = "select count(distinct(REPLACE(brgy, 'Ã±', 'ñ'))) from sort_add where town = '" + town + "' and prov = '" + province + "' order by brgy";
            state = connect.createStatement();
            rset = state.executeQuery(qry3c);
            int brgyCount = 0;
            while (rset.next()) {
                brgyCount = rset.getInt(1);
            }
            setBrgyList(new String[brgyCount]);
            rset = state.executeQuery(qry3);
            int i = 0;
            while (rset.next()) {
                getBrgyList()[i] = rset.getString(1);
                i++;
            }
            state.close();
            rset.close();
            connect.close();
            
            brgyCombo.removeAllItems();
            for (String brgy : getBrgyList()) {
                brgyCombo.addItem(brgy);
            }
        }
    }
    
    
    
    private boolean isRequiredInfoComplete() {
        return (!fNameText.getText().isEmpty() || 
//                !mNameText.getText().isEmpty() || 
                !lNameText.getText().isEmpty() ||
                !streetText.getText().isEmpty() ||
                !bDateCal.getDate().equals(null) ||
                !pBirthText.getText().isEmpty() || 
                !natText.getText().isEmpty())
                &&
//                !spsNameText.getText().isEmpty() || 
//                !employerText.getText().isEmpty() && 
                (!sssText.getText().isEmpty() || !gsisText.getText().isEmpty() || !tinText.getText().isEmpty());
    }
    
    
    
    public void findValidLastName(String fn, String ln) throws SQLException {
        int spacecounter = 0;
        for (char c : fn.toCharArray()) {
            if (c == ' ') {
                spacecounter++;
            }
        }
        Connection connect = DriverManager.getConnection(driver, f_user, f_pass);
        PreparedStatement pst = connect.prepareStatement("Select * from merlininventorydatabase.kyc where fname = ? and lname = ?");
        String nln = ln;
        ResultSet rset;
        for (int i = 0; i < spacecounter; i++) {
            pst.setString(1, fn);
            pst.setString(2, nln);
            rset = pst.executeQuery();
            if (rset.next()) {
//                break;
            } else {
                int idx = fn.lastIndexOf(' ');
                if (idx == -1) throw new IllegalArgumentException(" Single name only: " + fn);
                nln = fn.substring(idx+1).concat(" ").concat(ln);
                fn = fn.substring(0,idx);
                i++;
            } 
        }
//        rset.close();
//        return nln;
        fNameText.setText(fn);
        lNameText.setText(nln);
    }
    
    
    public boolean isPresentInKYC () throws SQLException{
        Connection connect = DriverManager.getConnection(driver, f_user, f_pass);
        PreparedStatement pst = connect.prepareStatement("Select * from merlininventorydatabase.kyc where fname = ? and lname = ?");
        pst.setString(1, fNameText.getText());
        pst.setString(2, lNameText.getText());
        ResultSet rset = pst.executeQuery();
        boolean q;
        if (rset.next()) {
            q = true;
        } else {
            q = false;
        }
        rset.close();
        pst.close();
        connect.close();
        return q;
    }
    
    public void loadPhotoImage(String fname, String lname) throws SQLException, IOException{
        Connection connect = DriverManager.getConnection(driver, f_user, f_pass);
        PreparedStatement pst = connect.prepareStatement("Select face from merlininventorydatabase.kyc where fname = '" + fname +"' and lname = '" + lname + "'");
        ResultSet rs = pst.executeQuery();
        if (rs.next()) {
            
        }

        byte[] imagedata = rs.getBytes("face");
//        ImageIcon format = new ImageIcon(imagedata);
        InputStream is = new ByteArrayInputStream(imagedata);
          File photofile = new File(con.getProp("default_photo_folder"));
        if (!photofile.exists()) {
            photofile.mkdir();
        }
        BufferedImage img = ImageIO.read(is);
        cl1.setPhotoimg(new ImageIcon(Scalr.resize(img, Scalr.Method.QUALITY, 640,480)));
        ImageIcon icon = new ImageIcon(Scalr.resize(img, Scalr.Method.QUALITY, 240,180));
        photo.setIcon(icon);
        rs.close();
        pst.close();
        connect.close();
    }
    
    public void loadIDImage(String fname, String lname) throws SQLException, IOException{
        Connection connect = DriverManager.getConnection(driver, f_user, f_pass);
        PreparedStatement pst = connect.prepareStatement("Select idcard from merlininventorydatabase.kyc where fname = '" + fname +"' and lname = '" + lname + "'");
        ResultSet rs = pst.executeQuery();
        if (rs.next()) {
            
        }
        byte[] iddata = rs.getBytes("idcard");
        InputStream is = new ByteArrayInputStream(iddata);
        BufferedImage img = ImageIO.read(is);
        cl1.setIdimg(new ImageIcon(Scalr.resize(img, Scalr.Method.QUALITY, 640,480)));
        ImageIcon icon = new ImageIcon(Scalr.resize(img, Scalr.Method.QUALITY, 240,180));
//        ImageIcon format2 = new ImageIcon(iddata);
//        setViewID(format2);
//        Image mm2 = format2.getImage();
//        Image idmg2 = mm2.getScaledInstance(240, 180, Image.SCALE_SMOOTH);
//        ImageIcon image2 = new ImageIcon(idmg2);
        idPhoto.setIcon(icon);
        rs.close();
        pst.close();
        connect.close();
    }
    
    public boolean isPhotoNull(String fname, String lname) throws SQLException {
        Connection connect = DriverManager.getConnection(driver, f_user, f_pass);
        PreparedStatement pst = connect.prepareStatement("Select face from merlininventorydatabase.kyc where fname = '" + fname +"' and lname = '" + lname + "'");
        ResultSet rs = pst.executeQuery();
        boolean x = false;
        if (rs.next()) {
            if (rs.getBlob(1) == null) {
                x = true;
            } else {
                x = false;
            }
        }
        rs.close();
        pst.close();
        connect.close();
        return x;
    }
    
    public boolean isIDNull(String fname, String lname) throws SQLException {
        Connection connect = DriverManager.getConnection(driver, f_user, f_pass);
        PreparedStatement pst = connect.prepareStatement("Select idcard from merlininventorydatabase.kyc where fname = '" + fname +"' and lname = '" + lname + "'");
        ResultSet rs = pst.executeQuery();
        boolean x = false;
        if (rs.next()) {
            if (rs.getBlob(1) == null) {
                x = true;
            } else {
                x = false;
            }
        }
        rs.close();
        pst.close();
        connect.close();
        return x;
    }
    
    
    
    public void clearTextsCombos() {
        gNameText.setText("");
        fNameText.setText("");
        mNameText.setText("");
        lNameText.setText("");
        natText.setText("");
        pBirthText.setText("");
        spsNameText.setText("");
        givenAddText.setText("");
        streetText.setText("");
        employerText.setText("");
        natureBusText.setText("");
        tinText.setText("");
        sssText.setText("");
        gsisText.setText("");
        bDateCal.setDate(null);
        conNumText.setText("");
        photo.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/avatarmen-200x200.jpg")));
        idPhoto.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/idcard_180.jpg")));
//        riskCombo.setSelectedIndex(0);
//        genderCombo.setSelectedIndex(0);
//        brgyCombo.setSelectedIndex(0);
//        townCombo.setSelectedIndex(0);
//        provCombo.setSelectedIndex(0);
//        soiCombo.setSelectedIndex(0);
//        idpresCombo.setSelectedIndex(0);
    }
    
    public void enableTextsCombos(boolean state) {
        fNameText.setEditable(state);
        mNameText.setEditable(state);
        lNameText.setEditable(state);
        suffixCombo.setEnabled(state);
        genderCombo.setEnabled(state);
        natText.setEditable(state);
        pBirthText.setEditable(state);
        bDateCal.setEnabled(state);
        riskCombo.setEnabled(state);
        givenAddText.setEditable(state);
        spsNameText.setEditable(state);
        streetText.setEditable(state);
        brgyCombo.setEnabled(state);
        townCombo.setEnabled(state);
        provCombo.setEnabled(state);
        employerText.setEditable(state);
        natureBusText.setEditable(state);
        soiCombo.setEnabled(state);
        tinText.setEditable(state);
        sssText.setEditable(state);
        gsisText.setEditable(state);
        idpresCombo.setEnabled(state);
        updateID.setEnabled(state);
        updatePhoto.setEnabled(state);
        authRepBtn.setEnabled(state);
        heightCombo.setEnabled(state);
        complexionCombo.setEnabled(state);
        builtCombo.setEnabled(state);
        jTable1.setEnabled(state);
        captureIDbtn.setEnabled(state);
        capturePhoto.setEnabled(state);
        conNumText.setEditable(state);
        createLoanBtn.setEnabled(state);
    }
    
    private boolean ChangesMadeInKYCinfo () {
        if (fNameText.getText().equals(cl1.getFirst_name()) ||
                mNameText.getText().equals(cl1.getMiddle_name()) ||
                lNameText.getText().equals(cl1.getLast_name()) ||
                suffixCombo.getSelectedItem().toString().equals(cl1.getSuffix()) ||
                genderCombo.getSelectedItem().toString().equals(cl1.getGender()) ||
                riskCombo.getSelectedItem().toString().equals(cl1.getRisk()) || 
                natText.getText().equals(cl1.getNationality()) || 
                dateformatter.format(bDateCal.getDate()).equals(cl1.getBirthday()) ||
                pBirthText.getText().equals(cl1.getBirthplace()) ||
                streetText.getText().equals(cl1.getStreet()) ||
                brgyCombo.getSelectedItem().toString().equals(cl1.getBrgy()) || 
                townCombo.getSelectedItem().toString().equals(cl1.getTown()) ||
                provCombo.getSelectedItem().toString().equals(cl1.getProv()) ||
                sssText.getText().equals(cl1.getSss()) ||
 gsisText.getText().equals(cl1.getGsis())
                ||                tinText.getText().equals(cl1.getTin()) ||
                employerText.getText().equals(cl1.getEmployer()) ||
                natureBusText.getText().equals(cl1.getNature_business()) ||
                soiCombo.getSelectedItem().toString().equals(cl1.getSoi()) ||
                spsNameText.getText().equals(cl1.getSps_name()) ||
                idpresCombo.getSelectedItem().toString().equals(cl1.getId_presented()) ||
                getPhotopath().equals(cl1.getPhoto_loc()) || 
                getIdPath().equals(cl1.getId_loc())) {
            return true;
        } else{
            return false;
        }
    }
    
    private boolean isNamesEmpty() {
        if (fNameText.getText().isEmpty() || lNameText.getText().isEmpty()) {
            return true;
        } else {
            return false;
        }
    }
    
    
    public void checkInKYCdatabase() throws SQLException{
        Connection connect = DriverManager.getConnection(driver, f_user, f_pass);
        java.sql.Statement state = connect.createStatement();
        ResultSet rset = state.executeQuery("Select * from empeno where fname = '" + fNameText.getText() + "' and lname = '" + lNameText.getText() + "'");
        while (rset.next()) {
            mNameText.setText(rset.getString("mname"));
            genderCombo.setSelectedItem(rset.getString("gender"));
            suffixCombo.setSelectedItem(rset.getString("suffix"));
            riskCombo.setSelectedItem(rset.getString("risk"));
            bDateCal.setDate(rset.getDate("bdate"));
            pBirthText.setText(rset.getString("bplace"));
            streetText.setText(rset.getString("street"));
            brgyCombo.setSelectedItem(rset.getString("brgy"));
            townCombo.setSelectedItem(rset.getString("town"));
            provCombo.setSelectedItem(rset.getString("prov"));
            spsNameText.setText(rset.getString("spouse"));
            employerText.setText(rset.getString("employer"));
            natureBusText.setText(rset.getString("nat_bus"));
            soiCombo.setSelectedItem(rset.getString("soi"));
            tinText.setText(rset.getString("tin"));
            sssText.setText(rset.getString("sss"));
            gsisText.setText(rset.getString("gsis"));
            idpresCombo.setSelectedItem(rset.getString("id_pres"));
            builtCombo.setSelectedItem(rset.getString("built"));
            heightCombo.setSelectedItem(rset.getString("height"));
            complexionCombo.setSelectedItem(rset.getString("complexion"));
        }
        rset.close();
        state.close();
        connect.close();
    }
    
    private Image scaleImage(Image img, int w, int h) {
        BufferedImage resizedImage = new BufferedImage(w, h, BufferedImage.TYPE_INT_RGB);
        Graphics2D g2 = resizedImage.createGraphics();
        g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
        g2.drawImage(img, 0, 0, w, h, null);
        g2.dispose();
        return resizedImage;
    }
    
    public void designTable(JTable table) {
        TableCellRenderer renderer = new TableCellRenderer() {

        JLabel label = new JLabel();

        @Override
        public Component getTableCellRendererComponent(JTable table,
                Object value, boolean isSelected, boolean hasFocus,
                int row, int column) {
                label.setOpaque(true);
                label.setText("" + value);
                Color alternate = UIManager.getColor("Table.alternateRowColor");
                if (isSelected) {
                    label.setBackground(Color.DARK_GRAY);
                    label.setForeground(Color.WHITE);
                } else {
                    label.setForeground(Color.black);
                    if (row % 2 == 1) {
                        label.setBackground(alternate);
                    } else {
                        label.setBackground(Color.WHITE);
                    }
                }
                
//                if (column == 2) {
//                    label.setHorizontalAlignment(SwingConstants.RIGHT);
//                } 
                
                return label;
            }

        };
        table.setDefaultRenderer(Object.class, renderer);
        ((DefaultTableCellRenderer)table.getTableHeader().getDefaultRenderer()).setHorizontalAlignment(SwingConstants.CENTER);
    }
    
    private String getLoanAmount (String item_code) throws SQLException {
        String amt = "";
        Connection connect = DriverManager.getConnection(driver, f_user, f_pass);
        java.sql.Statement state = connect.createStatement();
        ResultSet rset = state.executeQuery("Select principal from empeno where pap_num = '" + item_code + "'");
        while (rset.next()) {
            amt = new DecimalFormat("#,###.00").format(rset.getDouble(1));
        }
        state.close();
        rset.close();
        connect.close();
        return amt;
    }
    
    public void updateLoanTable() throws SQLException {
        DefaultTableModel dm = (DefaultTableModel) jTable1.getModel();
        while (dm.getRowCount() >0 ) {
            dm.removeRow(0);
        }
        Connection connect = DriverManager.getConnection(driver, f_user, f_pass);
        java.sql.Statement state = connect.createStatement();
        String qry = "Select pap_num, branch, item_code_a, transaction_date, item_description, status, remarks from merlininventorydatabase.client_info where client_name = '" 
                + searchText.getText() + "' order by transaction_date desc";
        String qryC = "Select count(*) from merlininventorydatabase.client_info where client_name = '" + searchText.getText() + "' order by transaction_date desc";
        ResultSet rset = state.executeQuery(qryC);
        int loanCounts = 0;
        while (rset.next()) {
            loanCounts = rset.getInt(1);
        }
        loanList = new String[loanCounts];
        rset = state.executeQuery(qry);
        Object[] row = new Object[7];
        int i=0;
        while (rset.next()) {
            row[0] = rset.getString(1);
            row[1] = rset.getString(2);
            row[2] = getLoanAmount(rset.getString(3));
            loanList[i] = rset.getString(3);
            row[3] = rset.getString(4);
            row[4] = rset.getString(5);
            row[5] = rset.getString(6);
            row[6] = rset.getString(7);
            dm.addRow(row);
            i++;
        }
        state.close();
        connect.close();
        connectClientToLoans(String.valueOf(cl1.getIdkey()), loanList);
    }
    
    public void resetTable() {
        DefaultTableModel dm = (DefaultTableModel) jTable1.getModel();
        while (dm.getRowCount() >0 ) {
            dm.removeRow(0);
        }
    }
    
    
    
    public void populateTextbox() {
        s = new TreeSet<String>();
        try {
            Connection connect = DriverManager.getConnection(driver, f_user, f_pass);
            java.sql.Statement state = connect.createStatement();
            ResultSet rset = state.executeQuery("Select distinct(nat_bus) from merlininventorydatabase.kyc");
            while (rset.next()) {
                s.add(rset.getString(1));
            }
            rset.close();
            state.close();
            connect.close();
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "Database Error! Please contact your database manager.\n" + ex);
            Logger.getLogger(KnowYourClient.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
//    public void populateClients() {
//        t = new TreeSet<String>();
//        try {
//            Connection connect = DriverManager.getConnection(driver, f_user, f_pass);
//            java.sql.Statement state = connect.createStatement();
//            ResultSet rset = state.executeQuery("Select distinct(client_name) from merlininventorydatabase.client_info union SELECT CONCAT(fname, ' ', lname) FROM kyc");
//            while (rset.next()) {
//                t.add(rset.getString(1));
//            }
//            rset.close();
//            state.close();
//            connect.close();
//        } catch (SQLException ex) {
//            Logger.getLogger(KnowYourClient.class.getName()).log(Level.SEVERE, null, ex);
//        }
//    }
    
    public void populateNations() {
        u = new TreeSet<String>();
        try {
            Connection connect = DriverManager.getConnection(driver, f_user, f_pass);
            java.sql.Statement state = connect.createStatement();
            ResultSet rset = state.executeQuery("Select distinct(nation) from merlininventorydatabase.kyc");
            while (rset.next()) {
                u.add(rset.getString(1));
            }
            rset.close();
            state.close();
            connect.close();
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "Database Error! Please contact your database manager.\n" + ex);
            Logger.getLogger(KnowYourClient.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public void findMatch(){
        all.removeAll(all);
        try {
            TextAutoCompleter tac = new TextAutoCompleter(searchText);
            Connection connect = DriverManager.getConnection(driver, f_user, f_pass);
            java.sql.Statement state = connect.createStatement();
            ResultSet rset = state.executeQuery("Select distinct(client_name) from merlininventorydatabase.client_info union SELECT CONCAT(fname, ' ', lname) FROM kyc");
            while (rset.next()) {
                for (int i = 0; i < getNames().length; i++) {
                    getNames()[i] = rset.getString(i+1);
                    tac.addItem(getNames()[i]);
                }
                all.add(getNames().clone());
            }
            state.close();
            connect.close();
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "Database Error! Please contact your database manager.\n" + ex);
            Logger.getLogger(KnowYourClient.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
//    public void findMatch2(){
//        try {
//            TextAutoCompleter tac = new TextAutoCompleter(natText);
//            Connection connect = DriverManager.getConnection(driver, f_user, f_pass);
//            java.sql.Statement state = connect.createStatement();
//            ResultSet rset = state.executeQuery("Select distinct(nation) from merlininventorydatabase.kyc");
//            while (rset.next()) {
//                for (int i = 0; i < getNations().length; i++) {
//                    getNations()[i] = rset.getString(i+1);
//                    tac.addItem(getNations()[i]);
//                }
//                allnations.add(getNations().clone());
//            }
//            state.close();
//            connect.close();
//        } catch (SQLException ex) {
//            Logger.getLogger(KnowYourClient.class.getName()).log(Level.SEVERE, null, ex);
//        }
//    }
    
//    public void findMatch3(){
//        try {
//            TextAutoCompleter tac = new TextAutoCompleter(natureBusText);
//            Connection connect = DriverManager.getConnection(driver, f_user, f_pass);
//            java.sql.Statement state = connect.createStatement();
//            ResultSet rset = state.executeQuery("Select distinct(nat_bus) from merlininventorydatabase.kyc");
//            while (rset.next()) {
//                for (int i = 0; i < getNature().length; i++) {
//                    getNature()[i] = rset.getString(i+1);
//                    tac.addItem(getNature()[i]);
//                }
//                allnatures.add(getNature().clone());
//            }
//            state.close();
//            connect.close();
//        } catch (SQLException ex) {
//            Logger.getLogger(KnowYourClient.class.getName()).log(Level.SEVERE, null, ex);
//        }
//    }
    
    
    
    
//    public void initProvCombo() {
//        
//        
//        while (provCombo.getItemCount() > 0) {
//            provCombo.removeAllItems();
//        }
//        
//        try ( Connection connect = DriverManager.getConnection(driver, f_user, f_pass)) {
//            
//            try (PreparedStatement pState = connect.prepareStatement("select distinct(prov) from sort_add order by prov")) {
//                
//                try (ResultSet rset = pState.executeQuery()) {
//                    while (rset.next()) {
//                        provCombo.addItem(rset.getString(1));
//                    }
//                }
//            }
//        } catch (SQLException ex) {
//            Logger.getLogger(KnowYourClient.class.getName()).log(Level.SEVERE, null, ex);
//        } 
//        
//    }
    
    public void initSOICombo() {
        Connection connect = null;
        Statement state = null;
        ResultSet rset = null;
        
        while (soiCombo.getItemCount() > 0) {
            soiCombo.removeAllItems();
        }
        try {
            connect = DriverManager.getConnection(driver, f_user, f_pass);
            state = (Statement) connect.createStatement();
            rset = state.executeQuery("select soi_name from accept_soi");
           while (rset.next()) {
               soiCombo.addItem(rset.getString(1));
           }
        } catch (SQLException ex) {
            
        } finally {
            try {
                rset.close();
                state.close();
                connect.close();
            } catch (SQLException ex) {
                Logger.getLogger(KnowYourClient.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
    
//    public void initTownCombo() {
//        while (townCombo.getItemCount() > 0) {
//            townCombo.removeAllItems();
//        }
//        try ( Connection connect = DriverManager.getConnection(driver, f_user, f_pass)) {
//            
//            try (PreparedStatement pstate = connect.prepareStatement("select distinct(town) from sort_add where prov = '" + provCombo.getSelectedItem().toString() + "' order by town")) {
//                
//                try (ResultSet rset = pstate.executeQuery();) {
//                    while (rset.next()) {
//                    townCombo.addItem(rset.getString(1));
//                    }
//                }
//            } 
//        } catch (SQLException ex) {
//            JOptionPane.showMessageDialog(null, "Database Error! Please contact your database manager.\n" + ex);
//            Logger.getLogger(KnowYourClient.class.getName()).log(Level.SEVERE, null, ex);
//        } 
//    }
//    
//    public void initBrgyCombo() {
//        
//        while (brgyCombo.getItemCount() > 0) {
//            brgyCombo.removeAllItems();
//        }
//        try ( Connection connect = DriverManager.getConnection(driver, f_user, f_pass)) {
//            
//            try (PreparedStatement pstate = connect.prepareStatement("select distinct(brgy) from sort_add where town = '" + townCombo.getSelectedItem().toString() + "' and prov = '" + provCombo.getSelectedItem().toString() + "' order by brgy")) {
//                
//                try (ResultSet rset = pstate.executeQuery();) {
//                    while (rset.next()) {
//                    brgyCombo.addItem(rset.getString(1));
//                    }
//                }
//            } 
//        } catch (SQLException ex) {
//            JOptionPane.showMessageDialog(null, "Database Error! Please contact your database manager.\n" + ex);
//            Logger.getLogger(KnowYourClient.class.getName()).log(Level.SEVERE, null, ex);
//        }  
//    }
    
    public void initIDsCombo() {
        Connection connect = null;
        Statement state = null;
        ResultSet rset = null;
        while (idpresCombo.getItemCount() > 0) {
            idpresCombo.removeAllItems();
        }
        try {
            connect = DriverManager.getConnection(driver, f_user, f_pass);
            state = (Statement) connect.createStatement();
            rset = state.executeQuery("select id_name from accept_id");
            while (rset.next()) {
                idpresCombo.addItem(rset.getString(1));
            }
        } catch (SQLException ex) {
            
        } finally {
            try {
                rset.close();
                state.close();
                connect.close();
            } catch (SQLException ex) {
                Logger.getLogger(KnowYourClient.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
    
    public void fillTable () throws SQLException{
        Connection connect = DriverManager.getConnection(driver, f_user, f_pass);
        Statement state = (Statement) connect.createStatement();
        ResultSet rset = state.executeQuery("select distinct(prov) from sort_add order by prov");
        while (rset.next()) {
            provCombo.addItem(rset.getString(1));
        }
        state.close();
        connect.close();
        
    }
    
    public static Dimension getScaledDimension(Dimension imgSize, Dimension boundary) {

    int original_width = imgSize.width;
    int original_height = imgSize.height;
    int bound_width = boundary.width;
    int bound_height = boundary.height;
    int new_width = original_width;
    int new_height = original_height;

    // first check if we need to scale width
    if (original_width > bound_width) {
        //scale width to fit
        new_width = bound_width;
        //scale height to maintain aspect ratio
        new_height = (new_width * original_height) / original_width;
    }

    // then check if we need to scale even with the new height
    if (new_height > bound_height) {
        //scale height to fit instead
        new_height = bound_height;
        //scale width to maintain aspect ratio
        new_width = (new_height * original_width) / original_height;
    }

    return new Dimension(new_width, new_height);
}
    
    public void refreshPhoto(boolean isCaptured, boolean fileExists, File photopath, JLabel photo) {
        if (isCaptured && fileExists) {
            try {
                BufferedImage img=ImageIO.read(photopath);
                ImageIcon icon = new ImageIcon(scaleImage(img, photo.getWidth(), photo.getHeight()));
                photo.setIcon(icon);
            } catch (IOException ex) {
                JOptionPane.showMessageDialog(null, "Unable to read client photo");
//                Logger.getLogger(KnowYourClient.class.getName()).log(Level.SEVERE, null, ex);
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
        jPanel1 = new javax.swing.JPanel();
        jPanel12 = new javax.swing.JPanel();
        searchText = new javax.swing.JTextField();
        searchBtn = new javax.swing.JButton();
        newCancel = new javax.swing.JButton();
        editSave = new javax.swing.JButton();
        jPanel2 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        riskCombo = new javax.swing.JComboBox<>();
        gNameText = new javax.swing.JTextField();
        jPanel3 = new javax.swing.JPanel();
        jLabel5 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        complexionCombo = new javax.swing.JComboBox<>();
        builtCombo = new javax.swing.JComboBox<>();
        heightCombo = new javax.swing.JComboBox<>();
        jLabel15 = new javax.swing.JLabel();
        jPanel6 = new javax.swing.JPanel();
        jLabel3 = new javax.swing.JLabel();
        jLabel10 = new javax.swing.JLabel();
        tinText = new javax.swing.JTextField();
        jLabel11 = new javax.swing.JLabel();
        sssText = new javax.swing.JTextField();
        gsisText = new javax.swing.JTextField();
        jPanel7 = new javax.swing.JPanel();
        jLabel18 = new javax.swing.JLabel();
        jLabel19 = new javax.swing.JLabel();
        jLabel20 = new javax.swing.JLabel();
        givenAddText = new javax.swing.JTextField();
        spsNameText = new javax.swing.JTextField();
        conNumText = new javax.swing.JTextField();
        jLabel21 = new javax.swing.JLabel();
        streetText = new javax.swing.JTextField();
        provCombo = new javax.swing.JComboBox<>();
        jLabel23 = new javax.swing.JLabel();
        jLabel22 = new javax.swing.JLabel();
        townCombo = new javax.swing.JComboBox<>();
        jLabel24 = new javax.swing.JLabel();
        brgyCombo = new javax.swing.JComboBox<>();
        jPanel8 = new javax.swing.JPanel();
        jLabel25 = new javax.swing.JLabel();
        jLabel26 = new javax.swing.JLabel();
        jLabel27 = new javax.swing.JLabel();
        natureBusText = new javax.swing.JTextField();
        employerText = new javax.swing.JTextField();
        idpresCombo = new javax.swing.JComboBox<>();
        soiCombo = new javax.swing.JComboBox<>();
        jLabel32 = new javax.swing.JLabel();
        jPanel9 = new javax.swing.JPanel();
        jLabel33 = new javax.swing.JLabel();
        jLabel34 = new javax.swing.JLabel();
        fNameText = new javax.swing.JTextField();
        jLabel35 = new javax.swing.JLabel();
        mNameText = new javax.swing.JTextField();
        lNameText = new javax.swing.JTextField();
        jLabel36 = new javax.swing.JLabel();
        bDateCal = new org.jdesktop.swingx.JXDatePicker();
        jLabel37 = new javax.swing.JLabel();
        pBirthText = new javax.swing.JTextField();
        jLabel38 = new javax.swing.JLabel();
        genderCombo = new javax.swing.JComboBox<>();
        jLabel39 = new javax.swing.JLabel();
        suffixCombo = new javax.swing.JComboBox<>();
        jLabel40 = new javax.swing.JLabel();
        natText = new javax.swing.JTextField();
        jScrollPane2 = new javax.swing.JScrollPane();
        jTable1 = new javax.swing.JTable();
        jLabel12 = new javax.swing.JLabel();
        jSeparator1 = new javax.swing.JSeparator();
        jPanel4 = new javax.swing.JPanel();
        capturePhoto = new javax.swing.JButton();
        jPanel10 = new javax.swing.JPanel();
        photo = new javax.swing.JLabel();
        updatePhoto = new javax.swing.JButton();
        jPanel5 = new javax.swing.JPanel();
        captureIDbtn = new javax.swing.JButton();
        updateID = new javax.swing.JButton();
        jPanel11 = new javax.swing.JPanel();
        idPhoto = new javax.swing.JLabel();
        jPanel13 = new javax.swing.JPanel();
        createLoanBtn = new javax.swing.JButton();
        jPanel14 = new javax.swing.JPanel();
        authRepBtn = new javax.swing.JButton();

        setMinimumSize(new java.awt.Dimension(840, 768));
        setPreferredSize(new java.awt.Dimension(840, 768));
        addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                formFocusGained(evt);
            }
        });
        addComponentListener(new java.awt.event.ComponentAdapter() {
            public void componentShown(java.awt.event.ComponentEvent evt) {
                formComponentShown(evt);
            }
        });
        setLayout(new java.awt.BorderLayout());

        jPanel1.setForeground(new java.awt.Color(255, 255, 255));
        jPanel1.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                jPanel1FocusGained(evt);
            }
        });

        jPanel12.setLayout(new java.awt.BorderLayout());

        searchText.setFont(new java.awt.Font("Segoe UI Semibold", 0, 13)); // NOI18N
        searchText.setForeground(new java.awt.Color(51, 51, 51));
        searchText.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                searchTextActionPerformed(evt);
            }
        });
        searchText.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                searchTextKeyPressed(evt);
            }
        });
        jPanel12.add(searchText, java.awt.BorderLayout.CENTER);

        searchBtn.setBackground(new java.awt.Color(79, 119, 141));
        searchBtn.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/magnifying-glass-search.png"))); // NOI18N
        searchBtn.setMinimumSize(new java.awt.Dimension(30, 30));
        searchBtn.setPreferredSize(new java.awt.Dimension(30, 30));
        searchBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                searchBtnActionPerformed(evt);
            }
        });
        jPanel12.add(searchBtn, java.awt.BorderLayout.EAST);

        newCancel.setBackground(new java.awt.Color(79, 119, 141));
        newCancel.setFont(new java.awt.Font("Segoe UI Semibold", 0, 13)); // NOI18N
        newCancel.setForeground(new java.awt.Color(255, 255, 255));
        newCancel.setText("New");
        newCancel.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                newCancelActionPerformed(evt);
            }
        });
        newCancel.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                newCancelKeyPressed(evt);
            }
        });

        editSave.setBackground(new java.awt.Color(242, 242, 242));
        editSave.setFont(new java.awt.Font("Segoe UI Semibold", 0, 13)); // NOI18N
        editSave.setForeground(new java.awt.Color(79, 119, 141));
        editSave.setText("Edit");
        editSave.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                editSaveActionPerformed(evt);
            }
        });

        jPanel2.setBackground(new java.awt.Color(255, 255, 255));

        jLabel1.setForeground(new java.awt.Color(102, 102, 102));
        jLabel1.setText("Given Name:");

        jLabel2.setForeground(new java.awt.Color(102, 102, 102));
        jLabel2.setText("Risk Profile");

        riskCombo.setFont(new java.awt.Font("Segoe UI Semibold", 0, 13)); // NOI18N
        riskCombo.setForeground(new java.awt.Color(51, 51, 51));
        riskCombo.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Low", "Medium", "High", "Max" }));

        gNameText.setFont(new java.awt.Font("Segoe UI Semibold", 0, 13)); // NOI18N
        gNameText.setForeground(new java.awt.Color(51, 51, 51));
        gNameText.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                gNameTextActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(20, 20, 20)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(riskCombo, 0, 113, Short.MAX_VALUE)
                    .addComponent(jLabel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel1)
                    .addComponent(gNameText, javax.swing.GroupLayout.PREFERRED_SIZE, 322, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                .addGap(16, 16, 16)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel2)
                    .addComponent(jLabel1))
                .addGap(1, 1, 1)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(riskCombo, javax.swing.GroupLayout.DEFAULT_SIZE, 30, Short.MAX_VALUE)
                    .addComponent(gNameText))
                .addContainerGap(10, Short.MAX_VALUE))
        );

        jPanel3.setBackground(new java.awt.Color(255, 255, 255));

        jLabel5.setForeground(new java.awt.Color(102, 102, 102));
        jLabel5.setText("Complexion");

        jLabel4.setForeground(new java.awt.Color(102, 102, 102));
        jLabel4.setText("Built/Frame");

        complexionCombo.setFont(new java.awt.Font("Segoe UI Semibold", 0, 13)); // NOI18N
        complexionCombo.setForeground(new java.awt.Color(51, 51, 51));
        complexionCombo.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Light", "Fair", "Dark" }));
        complexionCombo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                complexionComboActionPerformed(evt);
            }
        });

        builtCombo.setFont(new java.awt.Font("Segoe UI Semibold", 0, 13)); // NOI18N
        builtCombo.setForeground(new java.awt.Color(51, 51, 51));
        builtCombo.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Small", "Medium", "Big" }));
        builtCombo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                builtComboActionPerformed(evt);
            }
        });

        heightCombo.setFont(new java.awt.Font("Segoe UI Semibold", 0, 13)); // NOI18N
        heightCombo.setForeground(new java.awt.Color(51, 51, 51));
        heightCombo.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Short", "Medium", "Tall" }));
        heightCombo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                heightComboActionPerformed(evt);
            }
        });

        jLabel15.setForeground(new java.awt.Color(102, 102, 102));
        jLabel15.setText("Height");

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addGap(20, 20, 20)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(heightCombo, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel15, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(builtCombo, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(complexionCombo, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel5, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addGap(16, 16, 16)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel4)
                    .addComponent(jLabel5)
                    .addComponent(jLabel15))
                .addGap(1, 1, 1)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(heightCombo, javax.swing.GroupLayout.DEFAULT_SIZE, 30, Short.MAX_VALUE)
                    .addComponent(builtCombo, javax.swing.GroupLayout.DEFAULT_SIZE, 30, Short.MAX_VALUE)
                    .addComponent(complexionCombo, javax.swing.GroupLayout.DEFAULT_SIZE, 30, Short.MAX_VALUE))
                .addGap(10, 10, 10))
        );

        jPanel6.setBackground(new java.awt.Color(239, 246, 250));

        jLabel3.setForeground(new java.awt.Color(102, 102, 102));
        jLabel3.setText("Tax Identification No.");

        jLabel10.setForeground(new java.awt.Color(102, 102, 102));
        jLabel10.setText("SSS No.");

        tinText.setFont(new java.awt.Font("Segoe UI Semibold", 0, 13)); // NOI18N
        tinText.setForeground(new java.awt.Color(51, 51, 51));
        tinText.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                tinTextActionPerformed(evt);
            }
        });
        tinText.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                tinTextKeyTyped(evt);
            }
        });

        jLabel11.setForeground(new java.awt.Color(102, 102, 102));
        jLabel11.setText("GSIS No.");

        sssText.setFont(new java.awt.Font("Segoe UI Semibold", 0, 13)); // NOI18N
        sssText.setForeground(new java.awt.Color(51, 51, 51));
        sssText.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                sssTextActionPerformed(evt);
            }
        });
        sssText.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                sssTextKeyTyped(evt);
            }
        });

        gsisText.setFont(new java.awt.Font("Segoe UI Semibold", 0, 13)); // NOI18N
        gsisText.setForeground(new java.awt.Color(51, 51, 51));
        gsisText.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                gsisTextActionPerformed(evt);
            }
        });
        gsisText.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                gsisTextKeyTyped(evt);
            }
        });

        javax.swing.GroupLayout jPanel6Layout = new javax.swing.GroupLayout(jPanel6);
        jPanel6.setLayout(jPanel6Layout);
        jPanel6Layout.setHorizontalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addGap(20, 20, 20)
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(tinText))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(sssText)
                    .addGroup(jPanel6Layout.createSequentialGroup()
                        .addComponent(jLabel10, javax.swing.GroupLayout.PREFERRED_SIZE, 104, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel11, javax.swing.GroupLayout.PREFERRED_SIZE, 120, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(gsisText))
                .addGap(27, 27, 27))
        );
        jPanel6Layout.setVerticalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addGap(12, 12, 12)
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel3)
                    .addComponent(jLabel10)
                    .addComponent(jLabel11))
                .addGap(1, 1, 1)
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE, false)
                    .addComponent(tinText, javax.swing.GroupLayout.DEFAULT_SIZE, 30, Short.MAX_VALUE)
                    .addComponent(sssText, javax.swing.GroupLayout.DEFAULT_SIZE, 30, Short.MAX_VALUE)
                    .addComponent(gsisText, javax.swing.GroupLayout.DEFAULT_SIZE, 30, Short.MAX_VALUE))
                .addContainerGap(16, Short.MAX_VALUE))
        );

        jPanel7.setBackground(new java.awt.Color(239, 246, 250));

        jLabel18.setForeground(new java.awt.Color(102, 102, 102));
        jLabel18.setText("Spouse Name");

        jLabel19.setForeground(new java.awt.Color(102, 102, 102));
        jLabel19.setText("Contact No.");

        jLabel20.setForeground(new java.awt.Color(102, 102, 102));
        jLabel20.setText("Given Address");

        givenAddText.setFont(new java.awt.Font("Segoe UI Semibold", 0, 13)); // NOI18N
        givenAddText.setForeground(new java.awt.Color(51, 51, 51));
        givenAddText.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                givenAddTextActionPerformed(evt);
            }
        });

        spsNameText.setFont(new java.awt.Font("Segoe UI Semibold", 0, 13)); // NOI18N
        spsNameText.setForeground(new java.awt.Color(51, 51, 51));
        spsNameText.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                spsNameTextActionPerformed(evt);
            }
        });

        conNumText.setFont(new java.awt.Font("Segoe UI Semibold", 0, 13)); // NOI18N
        conNumText.setForeground(new java.awt.Color(51, 51, 51));
        conNumText.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                conNumTextActionPerformed(evt);
            }
        });
        conNumText.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                conNumTextKeyTyped(evt);
            }
        });

        jLabel21.setForeground(new java.awt.Color(102, 102, 102));
        jLabel21.setText("House No. Street:");

        streetText.setFont(new java.awt.Font("Segoe UI Semibold", 0, 13)); // NOI18N
        streetText.setForeground(new java.awt.Color(51, 51, 51));
        streetText.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                streetTextActionPerformed(evt);
            }
        });

        provCombo.setFont(new java.awt.Font("Segoe UI Semibold", 0, 13)); // NOI18N
        provCombo.setForeground(new java.awt.Color(51, 51, 51));
        provCombo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                provComboActionPerformed(evt);
            }
        });

        jLabel23.setForeground(new java.awt.Color(102, 102, 102));
        jLabel23.setText("Town/City");

        jLabel22.setForeground(new java.awt.Color(102, 102, 102));
        jLabel22.setText("Province");

        townCombo.setFont(new java.awt.Font("Segoe UI Semibold", 0, 13)); // NOI18N
        townCombo.setForeground(new java.awt.Color(51, 51, 51));
        townCombo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                townComboActionPerformed(evt);
            }
        });

        jLabel24.setForeground(new java.awt.Color(102, 102, 102));
        jLabel24.setText("Barangay");

        brgyCombo.setFont(new java.awt.Font("Segoe UI Semibold", 0, 13)); // NOI18N
        brgyCombo.setForeground(new java.awt.Color(51, 51, 51));
        brgyCombo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                brgyComboActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel7Layout = new javax.swing.GroupLayout(jPanel7);
        jPanel7.setLayout(jPanel7Layout);
        jPanel7Layout.setHorizontalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel7Layout.createSequentialGroup()
                .addGap(21, 21, 21)
                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel7Layout.createSequentialGroup()
                        .addComponent(jLabel20, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGap(259, 259, 259))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel7Layout.createSequentialGroup()
                        .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel7Layout.createSequentialGroup()
                                .addComponent(jLabel23, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addGap(12, 12, 12))
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel7Layout.createSequentialGroup()
                                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                    .addComponent(townCombo, javax.swing.GroupLayout.Alignment.LEADING, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(streetText, javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel21, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)))
                        .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(brgyCombo, javax.swing.GroupLayout.PREFERRED_SIZE, 206, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel24, javax.swing.GroupLayout.PREFERRED_SIZE, 206, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(provCombo, javax.swing.GroupLayout.PREFERRED_SIZE, 206, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel22, javax.swing.GroupLayout.PREFERRED_SIZE, 206, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(28, 28, 28))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel7Layout.createSequentialGroup()
                        .addComponent(givenAddText)
                        .addGap(28, 28, 28))
                    .addGroup(jPanel7Layout.createSequentialGroup()
                        .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jLabel18, javax.swing.GroupLayout.DEFAULT_SIZE, 288, Short.MAX_VALUE)
                            .addComponent(spsNameText))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel19, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(conNumText))
                        .addGap(28, 28, 28))))
        );

        jPanel7Layout.linkSize(javax.swing.SwingConstants.HORIZONTAL, new java.awt.Component[] {brgyCombo, jLabel22, jLabel24, provCombo});

        jPanel7Layout.setVerticalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel7Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel18)
                    .addComponent(jLabel19))
                .addGap(1, 1, 1)
                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(spsNameText, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(conNumText, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel20)
                .addGap(1, 1, 1)
                .addComponent(givenAddText, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(1, 1, 1)
                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel21)
                    .addComponent(jLabel22))
                .addGap(1, 1, 1)
                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(streetText, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(provCombo, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel23)
                    .addComponent(jLabel24))
                .addGap(1, 1, 1)
                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(townCombo, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(brgyCombo, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(8, 8, 8))
        );

        jPanel7Layout.linkSize(javax.swing.SwingConstants.VERTICAL, new java.awt.Component[] {conNumText, spsNameText});

        jPanel8.setBackground(new java.awt.Color(239, 246, 250));

        jLabel25.setForeground(new java.awt.Color(102, 102, 102));
        jLabel25.setText("Occupation/Employer/Name of Business");

        jLabel26.setForeground(new java.awt.Color(102, 102, 102));
        jLabel26.setText("ID Presented");

        jLabel27.setForeground(new java.awt.Color(102, 102, 102));
        jLabel27.setText("Nature of Work or Business");

        natureBusText.setFont(new java.awt.Font("Segoe UI Semibold", 0, 13)); // NOI18N
        natureBusText.setForeground(new java.awt.Color(51, 51, 51));
        natureBusText.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                natureBusTextActionPerformed(evt);
            }
        });
        natureBusText.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                natureBusTextKeyReleased(evt);
            }
            public void keyTyped(java.awt.event.KeyEvent evt) {
                natureBusTextKeyTyped(evt);
            }
        });

        employerText.setFont(new java.awt.Font("Segoe UI Semibold", 0, 13)); // NOI18N
        employerText.setForeground(new java.awt.Color(51, 51, 51));
        employerText.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                employerTextActionPerformed(evt);
            }
        });
        employerText.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                employerTextKeyTyped(evt);
            }
        });

        idpresCombo.setFont(new java.awt.Font("Segoe UI Semibold", 0, 13)); // NOI18N
        idpresCombo.setForeground(new java.awt.Color(51, 51, 51));
        idpresCombo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                idpresComboActionPerformed(evt);
            }
        });

        soiCombo.setFont(new java.awt.Font("Segoe UI Semibold", 0, 13)); // NOI18N
        soiCombo.setForeground(new java.awt.Color(51, 51, 51));
        soiCombo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                soiComboActionPerformed(evt);
            }
        });

        jLabel32.setForeground(new java.awt.Color(102, 102, 102));
        jLabel32.setText("Source of Income");

        javax.swing.GroupLayout jPanel8Layout = new javax.swing.GroupLayout(jPanel8);
        jPanel8.setLayout(jPanel8Layout);
        jPanel8Layout.setHorizontalGroup(
            jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel8Layout.createSequentialGroup()
                .addGap(20, 20, 20)
                .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel27, javax.swing.GroupLayout.DEFAULT_SIZE, 228, Short.MAX_VALUE)
                    .addComponent(natureBusText)
                    .addComponent(jLabel25, javax.swing.GroupLayout.PREFERRED_SIZE, 228, Short.MAX_VALUE)
                    .addComponent(employerText))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(soiCombo, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel26, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(idpresCombo, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel32, javax.swing.GroupLayout.DEFAULT_SIZE, 191, Short.MAX_VALUE))
                .addGap(28, 28, 28))
        );

        jPanel8Layout.linkSize(javax.swing.SwingConstants.HORIZONTAL, new java.awt.Component[] {jLabel25, jLabel27});

        jPanel8Layout.linkSize(javax.swing.SwingConstants.HORIZONTAL, new java.awt.Component[] {jLabel26, jLabel32});

        jPanel8Layout.setVerticalGroup(
            jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel8Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.CENTER)
                    .addComponent(jLabel25)
                    .addComponent(jLabel32))
                .addGap(1, 1, 1)
                .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(employerText, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(soiCombo, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel27)
                    .addComponent(jLabel26))
                .addGap(1, 1, 1)
                .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(natureBusText, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(idpresCombo, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
        );

        jPanel9.setBackground(new java.awt.Color(239, 246, 250));

        jLabel33.setForeground(new java.awt.Color(102, 102, 102));
        jLabel33.setText("First Name");

        jLabel34.setForeground(new java.awt.Color(102, 102, 102));
        jLabel34.setText("Middle Name");

        fNameText.setFont(new java.awt.Font("Segoe UI Semibold", 0, 13)); // NOI18N
        fNameText.setForeground(new java.awt.Color(51, 51, 51));
        fNameText.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                fNameTextActionPerformed(evt);
            }
        });

        jLabel35.setForeground(new java.awt.Color(102, 102, 102));
        jLabel35.setText("Last Name");

        mNameText.setFont(new java.awt.Font("Segoe UI Semibold", 0, 13)); // NOI18N
        mNameText.setForeground(new java.awt.Color(51, 51, 51));
        mNameText.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                mNameTextFocusLost(evt);
            }
        });
        mNameText.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mNameTextActionPerformed(evt);
            }
        });

        lNameText.setFont(new java.awt.Font("Segoe UI Semibold", 0, 13)); // NOI18N
        lNameText.setForeground(new java.awt.Color(51, 51, 51));
        lNameText.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                lNameTextActionPerformed(evt);
            }
        });

        jLabel36.setForeground(new java.awt.Color(102, 102, 102));
        jLabel36.setText("Place of Birth");

        bDateCal.setForeground(new java.awt.Color(102, 102, 102));
        bDateCal.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                bDateCalActionPerformed(evt);
            }
        });

        jLabel37.setForeground(new java.awt.Color(102, 102, 102));
        jLabel37.setText("Date of Birth");

        pBirthText.setFont(new java.awt.Font("Segoe UI Semibold", 0, 13)); // NOI18N
        pBirthText.setForeground(new java.awt.Color(51, 51, 51));
        pBirthText.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                pBirthTextActionPerformed(evt);
            }
        });
        pBirthText.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                pBirthTextKeyTyped(evt);
            }
        });

        jLabel38.setForeground(new java.awt.Color(102, 102, 102));
        jLabel38.setText("Gender");

        genderCombo.setFont(new java.awt.Font("Segoe UI Semibold", 0, 13)); // NOI18N
        genderCombo.setForeground(new java.awt.Color(51, 51, 51));
        genderCombo.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Male", "Female" }));
        genderCombo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                genderComboActionPerformed(evt);
            }
        });

        jLabel39.setForeground(new java.awt.Color(102, 102, 102));
        jLabel39.setText("Suffix");

        suffixCombo.setFont(new java.awt.Font("Segoe UI Semibold", 0, 13)); // NOI18N
        suffixCombo.setForeground(new java.awt.Color(51, 51, 51));
        suffixCombo.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "N/A", "Jr.", "Sr.", "II", "III", "IV" }));
        suffixCombo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                suffixComboActionPerformed(evt);
            }
        });

        jLabel40.setForeground(new java.awt.Color(102, 102, 102));
        jLabel40.setText("Nationality");

        natText.setFont(new java.awt.Font("Segoe UI Semibold", 0, 13)); // NOI18N
        natText.setForeground(new java.awt.Color(51, 51, 51));
        natText.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                natTextActionPerformed(evt);
            }
        });
        natText.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                natTextKeyReleased(evt);
            }
        });

        javax.swing.GroupLayout jPanel9Layout = new javax.swing.GroupLayout(jPanel9);
        jPanel9.setLayout(jPanel9Layout);
        jPanel9Layout.setHorizontalGroup(
            jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel9Layout.createSequentialGroup()
                .addGap(20, 20, 20)
                .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel9Layout.createSequentialGroup()
                        .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel36, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jLabel33, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(pBirthText))
                        .addGap(8, 8, 8))
                    .addGroup(jPanel9Layout.createSequentialGroup()
                        .addComponent(fNameText)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)))
                .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel9Layout.createSequentialGroup()
                        .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                            .addComponent(jLabel37, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(bDateCal, javax.swing.GroupLayout.PREFERRED_SIZE, 120, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jLabel40, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(natText, javax.swing.GroupLayout.DEFAULT_SIZE, 120, Short.MAX_VALUE)))
                    .addGroup(jPanel9Layout.createSequentialGroup()
                        .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                            .addComponent(jLabel34, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 120, Short.MAX_VALUE)
                            .addComponent(mNameText, javax.swing.GroupLayout.Alignment.LEADING))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(lNameText, javax.swing.GroupLayout.DEFAULT_SIZE, 120, Short.MAX_VALUE)
                            .addComponent(jLabel35, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(suffixCombo, javax.swing.GroupLayout.PREFERRED_SIZE, 71, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel39, javax.swing.GroupLayout.PREFERRED_SIZE, 66, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel38, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(genderCombo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(15, 15, 15))
        );

        jPanel9Layout.linkSize(javax.swing.SwingConstants.HORIZONTAL, new java.awt.Component[] {genderCombo, suffixCombo});

        jPanel9Layout.setVerticalGroup(
            jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel9Layout.createSequentialGroup()
                .addGap(12, 12, 12)
                .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel33)
                    .addComponent(jLabel34)
                    .addComponent(jLabel35)
                    .addComponent(jLabel39))
                .addGap(1, 1, 1)
                .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(fNameText, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(mNameText, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(lNameText, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(suffixCombo, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel36)
                    .addComponent(jLabel37)
                    .addComponent(jLabel38)
                    .addComponent(jLabel40))
                .addGap(1, 1, 1)
                .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(bDateCal, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(natText, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(genderCombo, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(pBirthText, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(10, 10, 10))
        );

        jPanel9Layout.linkSize(javax.swing.SwingConstants.VERTICAL, new java.awt.Component[] {bDateCal, genderCombo, natText, pBirthText});

        jPanel9Layout.linkSize(javax.swing.SwingConstants.VERTICAL, new java.awt.Component[] {fNameText, lNameText, mNameText});

        jTable1.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Pap No.", "Branch", "Loan Amount", "Loan Date", "Item Description", "Status", "Remarks"
            }
        ));
        jTable1.setRowHeight(30);
        jScrollPane2.setViewportView(jTable1);

        jLabel12.setFont(new java.awt.Font("Segoe UI Semibold", 1, 18)); // NOI18N
        jLabel12.setForeground(new java.awt.Color(102, 102, 102));
        jLabel12.setText(" Loan History");

        capturePhoto.setFont(new java.awt.Font("Segoe UI Semibold", 0, 13)); // NOI18N
        capturePhoto.setForeground(new java.awt.Color(79, 119, 141));
        capturePhoto.setText("Attach Photo");
        capturePhoto.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                capturePhotoActionPerformed(evt);
            }
        });

        jPanel10.setBackground(new java.awt.Color(241, 241, 243));

        photo.setBackground(new java.awt.Color(241, 241, 243));
        photo.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        photo.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/avatarmen-200x200.jpg"))); // NOI18N
        photo.setPreferredSize(new java.awt.Dimension(240, 180));
        photo.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                photoMouseClicked(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                photoMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                photoMouseExited(evt);
            }
        });
        photo.addComponentListener(new java.awt.event.ComponentAdapter() {
            public void componentShown(java.awt.event.ComponentEvent evt) {
                photoComponentShown(evt);
            }
        });
        photo.addPropertyChangeListener(new java.beans.PropertyChangeListener() {
            public void propertyChange(java.beans.PropertyChangeEvent evt) {
                photoPropertyChange(evt);
            }
        });

        javax.swing.GroupLayout jPanel10Layout = new javax.swing.GroupLayout(jPanel10);
        jPanel10.setLayout(jPanel10Layout);
        jPanel10Layout.setHorizontalGroup(
            jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel10Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(photo, javax.swing.GroupLayout.PREFERRED_SIZE, 280, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel10Layout.setVerticalGroup(
            jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel10Layout.createSequentialGroup()
                .addGap(1, 1, 1)
                .addComponent(photo, javax.swing.GroupLayout.PREFERRED_SIZE, 200, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        updatePhoto.setBackground(new java.awt.Color(79, 119, 141));
        updatePhoto.setFont(new java.awt.Font("Segoe UI Semibold", 0, 13)); // NOI18N
        updatePhoto.setForeground(new java.awt.Color(255, 255, 255));
        updatePhoto.setText("Take Photo");
        updatePhoto.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                updatePhotoActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addGap(18, 18, 18)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel10, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addGap(39, 39, 39)
                        .addComponent(updatePhoto, javax.swing.GroupLayout.DEFAULT_SIZE, 110, Short.MAX_VALUE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(capturePhoto)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jPanel4Layout.linkSize(javax.swing.SwingConstants.HORIZONTAL, new java.awt.Component[] {capturePhoto, updatePhoto});

        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addGap(1, 1, 1)
                .addComponent(jPanel10, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(updatePhoto, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(capturePhoto, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
        );

        captureIDbtn.setFont(new java.awt.Font("Segoe UI Semibold", 0, 13)); // NOI18N
        captureIDbtn.setForeground(new java.awt.Color(79, 119, 141));
        captureIDbtn.setText("Attach File");
        captureIDbtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                captureIDbtnActionPerformed(evt);
            }
        });

        updateID.setBackground(new java.awt.Color(79, 119, 141));
        updateID.setFont(new java.awt.Font("Segoe UI Semibold", 0, 13)); // NOI18N
        updateID.setForeground(new java.awt.Color(255, 255, 255));
        updateID.setText("Scan ID");
        updateID.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                updateIDActionPerformed(evt);
            }
        });

        jPanel11.setBackground(new java.awt.Color(241, 241, 243));

        idPhoto.setBackground(new java.awt.Color(241, 241, 243));
        idPhoto.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        idPhoto.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/idcard_180.jpg"))); // NOI18N
        idPhoto.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                idPhotoMouseClicked(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                idPhotoMouseEntered(evt);
            }
        });

        javax.swing.GroupLayout jPanel11Layout = new javax.swing.GroupLayout(jPanel11);
        jPanel11.setLayout(jPanel11Layout);
        jPanel11Layout.setHorizontalGroup(
            jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel11Layout.createSequentialGroup()
                .addGap(8, 8, 8)
                .addComponent(idPhoto, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );
        jPanel11Layout.setVerticalGroup(
            jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(idPhoto, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        javax.swing.GroupLayout jPanel5Layout = new javax.swing.GroupLayout(jPanel5);
        jPanel5.setLayout(jPanel5Layout);
        jPanel5Layout.setHorizontalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel5Layout.createSequentialGroup()
                        .addGap(47, 47, 47)
                        .addComponent(updateID, javax.swing.GroupLayout.PREFERRED_SIZE, 111, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(captureIDbtn, javax.swing.GroupLayout.PREFERRED_SIZE, 107, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel5Layout.createSequentialGroup()
                        .addGap(18, 18, 18)
                        .addComponent(jPanel11, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel5Layout.setVerticalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addGap(1, 1, 1)
                .addComponent(jPanel11, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(updateID, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(captureIDbtn, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(18, Short.MAX_VALUE))
        );

        createLoanBtn.setBackground(new java.awt.Color(79, 119, 141));
        createLoanBtn.setFont(new java.awt.Font("Segoe UI Semibold", 0, 13)); // NOI18N
        createLoanBtn.setForeground(new java.awt.Color(255, 255, 255));
        createLoanBtn.setText("Create Loan for this Client");
        createLoanBtn.setEnabled(false);
        createLoanBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                createLoanBtnActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel13Layout = new javax.swing.GroupLayout(jPanel13);
        jPanel13.setLayout(jPanel13Layout);
        jPanel13Layout.setHorizontalGroup(
            jPanel13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel13Layout.createSequentialGroup()
                .addGap(33, 33, 33)
                .addComponent(createLoanBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 281, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel13Layout.setVerticalGroup(
            jPanel13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel13Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(createLoanBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        authRepBtn.setBackground(new java.awt.Color(79, 119, 141));
        authRepBtn.setFont(new java.awt.Font("Segoe UI Semibold", 0, 13)); // NOI18N
        authRepBtn.setForeground(new java.awt.Color(255, 255, 255));
        authRepBtn.setText("Authorized Representatives");
        authRepBtn.setEnabled(false);
        authRepBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                authRepBtnActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel14Layout = new javax.swing.GroupLayout(jPanel14);
        jPanel14.setLayout(jPanel14Layout);
        jPanel14Layout.setHorizontalGroup(
            jPanel14Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel14Layout.createSequentialGroup()
                .addGap(39, 39, 39)
                .addComponent(authRepBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 266, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel14Layout.setVerticalGroup(
            jPanel14Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel14Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(authRepBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(10, 10, 10)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane2)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addGap(3, 3, 3)
                                .addComponent(jPanel12, javax.swing.GroupLayout.PREFERRED_SIZE, 309, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(newCancel, javax.swing.GroupLayout.PREFERRED_SIZE, 81, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(editSave)
                                .addGap(0, 0, Short.MAX_VALUE))
                            .addComponent(jPanel8, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jPanel9, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jPanel6, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jPanel7, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jPanel13, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jPanel14, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                    .addComponent(jPanel5, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(jPanel3, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(jPanel4, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                                .addGap(0, 0, Short.MAX_VALUE))))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                        .addComponent(jLabel12, javax.swing.GroupLayout.PREFERRED_SIZE, 117, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jSeparator1)))
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(16, 16, 16)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.CENTER)
                    .addComponent(jPanel12, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(newCancel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(editSave, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel14, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(12, 12, 12)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jPanel5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jPanel13, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jPanel9, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jPanel7, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jPanel8, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jPanel6, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel12, javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jSeparator1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 232, Short.MAX_VALUE)
                .addContainerGap())
        );

        jPanel1Layout.linkSize(javax.swing.SwingConstants.VERTICAL, new java.awt.Component[] {editSave, jPanel12, newCancel});

        jScrollPane1.setViewportView(jPanel1);

        add(jScrollPane1, java.awt.BorderLayout.CENTER);
    }// </editor-fold>//GEN-END:initComponents

    private void gNameTextActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_gNameTextActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_gNameTextActionPerformed

    private void builtComboActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_builtComboActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_builtComboActionPerformed

    private void heightComboActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_heightComboActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_heightComboActionPerformed

    private void complexionComboActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_complexionComboActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_complexionComboActionPerformed

    private void tinTextActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_tinTextActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_tinTextActionPerformed

    private void sssTextActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_sssTextActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_sssTextActionPerformed

    private void gsisTextActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_gsisTextActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_gsisTextActionPerformed

    private void givenAddTextActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_givenAddTextActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_givenAddTextActionPerformed

    private void spsNameTextActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_spsNameTextActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_spsNameTextActionPerformed

    private void conNumTextActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_conNumTextActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_conNumTextActionPerformed

    private void streetTextActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_streetTextActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_streetTextActionPerformed

    private void provComboActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_provComboActionPerformed
            if (provCombo.getItemCount() > 0) {
                try {
                    initializeTownCombo(provCombo.getSelectedItem().toString());
                    initializeBrgyCombo(provCombo.getSelectedItem().toString(), townCombo.getSelectedItem().toString());
                } catch (SQLException ex) {
                    JOptionPane.showMessageDialog(null, "Database Connection Error! \n Please report to your Database Admnistrator.", "Database Error", JOptionPane.ERROR_MESSAGE );
                    Logger.getLogger(KnowYourClient.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
    }//GEN-LAST:event_provComboActionPerformed

    private void townComboActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_townComboActionPerformed
        if (townCombo.getItemCount() > 0 ) {
            try {
                    initializeBrgyCombo(provCombo.getSelectedItem().toString(), townCombo.getSelectedItem().toString());
                } catch (SQLException ex) {
                    JOptionPane.showMessageDialog(null, "Database Connection Error! \n Please report to your Database Admnistrator.", "Database Error", JOptionPane.ERROR_MESSAGE );
                    Logger.getLogger(KnowYourClient.class.getName()).log(Level.SEVERE, null, ex);
                }
        }
    }//GEN-LAST:event_townComboActionPerformed

    private void brgyComboActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_brgyComboActionPerformed
//            initBrgyCombo();
    }//GEN-LAST:event_brgyComboActionPerformed

    private void natureBusTextActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_natureBusTextActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_natureBusTextActionPerformed

    private void employerTextActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_employerTextActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_employerTextActionPerformed

    private void idpresComboActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_idpresComboActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_idpresComboActionPerformed

    private void soiComboActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_soiComboActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_soiComboActionPerformed

    private void fNameTextActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_fNameTextActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_fNameTextActionPerformed

    private void mNameTextActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mNameTextActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_mNameTextActionPerformed

    private void lNameTextActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_lNameTextActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_lNameTextActionPerformed

    private void bDateCalActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_bDateCalActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_bDateCalActionPerformed

    private void pBirthTextActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_pBirthTextActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_pBirthTextActionPerformed

    private void genderComboActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_genderComboActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_genderComboActionPerformed

    private void suffixComboActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_suffixComboActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_suffixComboActionPerformed

    private void natTextActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_natTextActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_natTextActionPerformed

    private void capturePhotoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_capturePhotoActionPerformed
        JFileChooser chooseFile = new JFileChooser();
        FileNameExtensionFilter fnwf = new FileNameExtensionFilter("PNG JPG AND JPEG", "png", "jpeg", "jpg");
        chooseFile.setFileFilter(fnwf);
        int result = chooseFile.showOpenDialog(null);
        chooseFile.setVisible(true);
        if (result == JFileChooser.APPROVE_OPTION) {
            if(!chooseFile.getSelectedFile().getAbsolutePath().equals(null)) {
                setFileName(chooseFile.getSelectedFile().getAbsolutePath());
                setPhotopath(chooseFile.getSelectedFile().getAbsolutePath());
                cl1.setPhoto_loc(getPhotopath());
                try {
                    BufferedImage img=ImageIO.read(new File(getFileName()));
                    ImageIcon icon = new ImageIcon(Scalr.resize(img, Scalr.Method.QUALITY, 240,180));
                    cl1.setPhotoimg(new ImageIcon(Scalr.resize(img, Scalr.Method.QUALITY, 640,480)));
                    ImageIO.write(Scalr.resize(img, Scalr.Method.QUALITY, 640,480), "jpg", new File(con.getProp("default_photo_folder") + fNameText.getText().concat(" " + lNameText.getText()) + "_photo.jpg"));
                    photo.setIcon(icon);
                    cl1.setPhoto_loc(con.getProp("default_photo_folder") + fNameText.getText().concat(" " + lNameText.getText()) + "_photo.jpg");
                } catch (IOException ex) {
                    JOptionPane.showMessageDialog(null, "Image Output Error! Please contact your database manager.\n" + ex);
                    Logger.getLogger(KnowYourClient.class.getName()).log(Level.SEVERE, null, ex);
                }
                chooseFile.setVisible(false);
            } else {
                int replaceFile = JOptionPane.showConfirmDialog(null, "Do you want to replace this photo?", "Replace Photo", JOptionPane.YES_NO_OPTION);
                if (replaceFile == JOptionPane.YES_OPTION) {
                    try {
                        BufferedImage img=ImageIO.read(new File(getFileName()));
                        ImageIcon icon = new ImageIcon(Scalr.resize(img, Scalr.Method.QUALITY, 240,180));
                        cl1.setPhotoimg(new ImageIcon(Scalr.resize(img, Scalr.Method.QUALITY, 640,480)));
                        ImageIO.write(Scalr.resize(img, Scalr.Method.QUALITY, 640,480), "jpg", new File(con.getProp("default_photo_folder") + fNameText.getText().concat(" " + lNameText.getText()) + "_photo.jpg"));
                        photo.setIcon(icon);
                        cl1.setPhoto_loc(con.getProp("default_photo_folder") + fNameText.getText().concat(" " + lNameText.getText()) + "_photo.jpg");
//                        ImageIO.write((RenderedImage) scaleImage(img, 640, 480), "jpg", new File(con.getProp("default_photo_folder") + fNameText.getText().concat(" " + lNameText.getText()) + "_photo.jpg")); //change path where you want it saved
                    } catch (IOException ex) {
                        JOptionPane.showMessageDialog(null, "Image Output Error! Please contact your database manager.\n" + ex);
                        Logger.getLogger(KnowYourClient.class.getName()).log(Level.SEVERE, null, ex);
                    }
                } else {

                }
                chooseFile.setVisible(false);
            }
        }
    }//GEN-LAST:event_capturePhotoActionPerformed

    private void newCancelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_newCancelActionPerformed
        setCreateLoan(false);
        if (newCancel.getText().equals("New")) {   //New Button Pressed
            cl1.resetClient();
            fNameText.requestFocus();
            if (!isNamesEmpty() && ChangesMadeInKYCinfo()) {
                int replaceFile = JOptionPane.showConfirmDialog(null, "Do you want to save the changes made for this client?", "Save Changes", JOptionPane.YES_NO_OPTION);
                if (replaceFile == JOptionPane.YES_OPTION) {
                    //INSERT KYC TABLE
                }
            }
            clearTextsCombos();
            resetTable();
            enableTextsCombos(true);
            searchText.setEnabled(false);
            searchBtn.setEnabled(false);
            newCancel.setText("Cancel");
            editSave.setText("Save");
            editSave.setEnabled(true);
            setRecordRetrieved(false);
        } else {    //Cancel Button Pressed
            if (isNamesEmpty()) {    //No information present in textboxes
                clearTextsCombos();
                resetTable();
                enableTextsCombos(false);
                editSave.setEnabled(false);
            } else {
                if (ChangesMadeInKYCinfo()) {
                    int replaceFile = JOptionPane.showConfirmDialog(null, "Do you want to save the changes made for this client?", "Save Changes", JOptionPane.YES_NO_OPTION);
                    if (replaceFile == JOptionPane.YES_OPTION) {
                        //UPDATE KYC TABLE
                    }
                    if (!isRecordRetrieved()) {
                        clearTextsCombos();
                        resetTable();
                    }
                    enableTextsCombos(false);
                    editSave.setEnabled(true);
                }
            }
            newCancel.setText("New");
            editSave.setText("Edit");
            searchBtn.setEnabled(true);
            searchText.setEnabled(true);
            createLoanBtn.setEnabled(true);
        }
    }//GEN-LAST:event_newCancelActionPerformed

    private void editSaveActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_editSaveActionPerformed
        if (editSave.getText().equalsIgnoreCase("Edit")) {
            editSave.setText("Save");
            enableTextsCombos(true);
        } else {
            cl1.setRisk(riskCombo.getSelectedItem().toString());
            cl1.setFirst_name(fNameText.getText());
            cl1.setMiddle_name(mNameText.getText());
            cl1.setLast_name(lNameText.getText());
            cl1.setSuffix(suffixCombo.getSelectedItem().toString());
            cl1.setGender(genderCombo.getSelectedItem().toString());
            cl1.setId_presented(idpresCombo.getSelectedItem().toString());
            cl1.setNationality(natText.getText());
            cl1.setBirthday(new java.sql.Date(bDateCal.getDate().getTime()));
            cl1.setBirthplace(pBirthText.getText());
            cl1.setStreet(streetText.getText());
            cl1.setSps_name(spsNameText.getText());
            cl1.setBrgy(brgyCombo.getSelectedItem().toString());
            cl1.setTown(townCombo.getSelectedItem().toString());
            cl1.setProv(provCombo.getSelectedItem().toString());
            cl1.setEmployer(employerText.getText());
            cl1.setSoi(soiCombo.getSelectedItem().toString());
            cl1.setTin(tinText.getText());
            cl1.setSss(sssText.getText());
            cl1.setGsis(gsisText.getText());
            cl1.setSps_name(spsNameText.getText());
            cl1.setNationality(natText.getText());
            cl1.setNature_business(natureBusText.getText());
            cl1.setContact_no(conNumText.getText());
            cl1.setBlt(builtCombo.getSelectedItem().toString());
            cl1.setHt(heightCombo.getSelectedItem().toString());
            cl1.setCmplxn(complexionCombo.getSelectedItem().toString());
//            cl1.setPhoto_loc(getPhotopath());
//            cl1.setId_loc(getIdPath());

            try {
                if (isRecordRetrieved()) {
                    if (isRequiredInfoComplete()) {
                        if (cl1.insertToKYCdatabase2() > 0) {
                            this.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
                            enableTextsCombos(false);
                            newCancel.setText("New");
                            newCancel.setEnabled(true);
                            searchText.setEnabled(true);
                            searchBtn.setEnabled(true);
                            editSave.setText("Edit");
//                            findMatch();
                            if (!s.contains(natureBusText.getText())) {
                                s.add(natureBusText.getText()); 
                            }
                            this.setCursor(Cursor.getDefaultCursor());
                            JOptionPane.showMessageDialog(null, "Client record successfully edited in the database.", "Edit Client", JOptionPane.INFORMATION_MESSAGE);
                        } else {
                            JOptionPane.showMessageDialog(null, "An error occured while adding this client to database.\n Please report this to your Database Administrator.", "Add Client", JOptionPane.ERROR_MESSAGE);
                        }
                        
                    } else {
                        JOptionPane.showMessageDialog(this, "Please complete required information indicated by * sign.");
                    }
                } else {
                    if (!isPresentInKYC()) {
                        if (isRequiredInfoComplete()) {
                            if (cl1.insertToKYCdatabase2() > 0) {
                                enableTextsCombos(false);
                                searchText.setEnabled(true);
                                searchBtn.setEnabled(true);
                                newCancel.setText("New");
                                newCancel.setEnabled(true);
                                editSave.setText("Edit");
//                                if (t.contains(fNameText.getText().concat(" ").concat(lNameText.getText()))) {
//                                    t.add(fNameText.getText().concat(" ").concat(lNameText.getText()));
//                                }
//                                all.add();
                                findMatch();
                                JOptionPane.showMessageDialog(null, "Client successfully added to database.", "Add Client", JOptionPane.INFORMATION_MESSAGE);
                            } else {
                                JOptionPane.showMessageDialog(null, "An error occured while adding this client to database.\n Please report this to your Database Administrator.", "Add Client", JOptionPane.ERROR_MESSAGE);
                            }
                        } else {
                            JOptionPane.showMessageDialog(this, "Please complete required information indicated by * sign.");    
                        }
                    } else {
                        int conf = JOptionPane.showConfirmDialog(this, "This client cannot be added to the database. \n A client with this name is already in the database.\n  Would you like to edit this client record instead?", "Client Already in Database", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
                        
                        if (conf == JOptionPane.YES_OPTION) {
                            searchText.setText(fNameText.getText().concat(" ").concat(lNameText.getText()));
                            editSave.setText("Edit");
                            searchBtnActionPerformed(null);
                        } else {
                            clearTextsCombos();
                            resetTable();
                        }
                    }
                }
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(null, "Database Error! Please contact your database manager.\n" + ex);
                Logger.getLogger(KnowYourClient.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }//GEN-LAST:event_editSaveActionPerformed

    private void jPanel1FocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_jPanel1FocusGained
        
    }//GEN-LAST:event_jPanel1FocusGained

    private void searchBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_searchBtnActionPerformed
        cl1.resetClient();
        clearTextsCombos();
        resetTable();
        if (editSave.getText().equalsIgnoreCase("Edit")) {
            try {
                
                //TRY TO SEPARATE GIVEN NAME TO FIRST AND LAST BY FINDING THE LAST INDEX OF SPACE FOR SINGLE WORDED LAST NAMES
                int idx = searchText.getText().lastIndexOf(' ');
                gNameText.setText(searchText.getText());
                // WILL RETURN AN ERROR FOR SINGLE WORDED NAMES (NO FIRST NAME AND LAST NAME)
                if (idx == -1) throw new IllegalArgumentException(" Single name only: " + searchText.getText());
                
                //FIND LAST NAME METHOD WILL TAKE CARE OF SEPARATING THE LAST NAMES OF 2 WORDED LAST NAMES
                findValidLastName(searchText.getText().substring(0, idx), searchText.getText().substring(idx+1));
                cl1.setFirst_name(fNameText.getText());
                cl1.setLast_name(lNameText.getText());
                cl1.setGiven_name(searchText.getText());
                try {
                    cl1.filldetailsFromClientInfo();
                    
                } catch (SQLException ex) {
                    JOptionPane.showMessageDialog(null, "Database Error! Please contact your database manager.\n" + ex);
                    Logger.getLogger(KnowYourClient.class.getName()).log(Level.SEVERE, null, ex);
                }
                givenAddText.setText(cl1.getAddress());
                conNumText.setText(cl1.getContact_no());
                if (cl1.isClientFoundInKYC(fNameText.getText(), lNameText.getText())) {
                    try {
                        cl1.retrieveFromKYC();
                        mNameText.setText(cl1.getMiddle_name());
                        streetText.setText(cl1.getStreet());
                        provCombo.setSelectedItem(cl1.getProv());
//                        initTownCombo();
                        townCombo.setSelectedItem(cl1.getTown());
                        conNumText.setText(cl1.getContact_no());
//                        initBrgyCombo();
                        brgyCombo.setSelectedItem(cl1.getBrgy());
                        spsNameText.setText(cl1.getSps_name());
                        pBirthText.setText(cl1.getBirthplace());
                        bDateCal.setDate(cl1.getBirthday());
                        tinText.setText(cl1.getTin());
                        sssText.setText(cl1.getSss());
                        gsisText.setText(cl1.getGsis());
                        employerText.setText(cl1.getEmployer());
                        soiCombo.setSelectedItem(cl1.getSoi());
                        idpresCombo.setSelectedItem(cl1.getId_presented());
                        natText.setText(cl1.getNationality());
                        genderCombo.setSelectedItem(cl1.getGender());
                        suffixCombo.setSelectedItem(cl1.getSuffix());
                        natureBusText.setText(cl1.getNature_business());
                        if (!isPhotoNull(fNameText.getText(), lNameText.getText())) {
                            loadPhotoImage(fNameText.getText(), lNameText.getText());
                        } else {
                            photo.setIcon(new javax.swing.ImageIcon(getClass().getResource("/kyc_merlin/avatarmen-200x200.jpg")));
                        }
                        if (!isIDNull(fNameText.getText(), lNameText.getText())) {
                            loadIDImage(fNameText.getText(), lNameText.getText());
                        } else {
                            idPhoto.setIcon(new javax.swing.ImageIcon(getClass().getResource("/kyc_merlin/idcard_160.jpg")));
                        }
                        updateLoanTable();
                    } catch (SQLException ex) {
                        JOptionPane.showMessageDialog(null, "Database Error! Please contact your database manager.\n" + ex);
                        Logger.getLogger(KnowYourClient.class.getName()).log(Level.SEVERE, null, ex);
                    } catch (IOException ex) {
                        JOptionPane.showMessageDialog(null, "Image Output Error! Please contact your database manager.\n" + ex);
                        Logger.getLogger(KnowYourClient.class.getName()).log(Level.SEVERE, null, ex);
                    }
                    
                } else {
                    int adx = givenAddText.getText().lastIndexOf(' ');
                    if (adx == -1) throw new IllegalArgumentException("Single add only " + givenAddText);
                    for (int i = 0; i < provCombo.getItemCount(); i++) {
                        if (givenAddText.getText().substring(adx+1).equalsIgnoreCase(provCombo.getItemAt(i))) {
                            provCombo.setSelectedIndex(i);
                            
                            for (int j = 0; j < townCombo.getItemCount(); j++) {
                                if (givenAddText.getText().toLowerCase().contains(townCombo.getItemAt(j).toLowerCase())) {
                                    townCombo.setSelectedIndex(j);
                                }
                            }
                            for (int k = 0; k < brgyCombo.getItemCount(); k++) {
                                if (givenAddText.getText().toLowerCase().contains(brgyCombo.getItemAt(k).toLowerCase())) {
                                    brgyCombo.setSelectedIndex(k);
                                }
                            }
                        }
                    }
                }
                designTable(jTable1);
//                enableTextsCombos(true);
                editSave.setEnabled(true);
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(null, "Database Error! Please contact your database manager.\n" + ex);
                Logger.getLogger(KnowYourClient.class.getName()).log(Level.SEVERE, null, ex);
            }
        } else {
            if (ChangesMadeInKYCinfo()) {
                int replaceFile = JOptionPane.showConfirmDialog(null, "Do you want to save the changes made for this client?", "Save Changes", JOptionPane.YES_NO_OPTION);
                if (replaceFile == JOptionPane.YES_OPTION) {
                    try {
                        //UPDATE KYC TABLE
                        cl1.insertToKYCdatabase2();
                    } catch (SQLException ex) {
                        JOptionPane.showMessageDialog(null, "Database Error! Please contact your database manager.\n" + ex);
                        Logger.getLogger(KnowYourClient.class.getName()).log(Level.SEVERE, null, ex);
                    }
                } else {
                }
            }
            editSave.setText("Save");
        }
        createLoanBtn.setEnabled(true);
        setRecordRetrieved(true);
    }//GEN-LAST:event_searchBtnActionPerformed

    private void searchTextKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_searchTextKeyPressed
//        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
//            searchBtnActionPerformed(null);
//        }
    }//GEN-LAST:event_searchTextKeyPressed

    private void updatePhotoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_updatePhotoActionPerformed
        Camera cam = new Camera();
        
        cam.setTitle("Capture Client Photo");
        cam.setPhotomode(true);
        setPhotoTaken(true);
        setIdTaken(false);
        cam.setLocationRelativeTo(null);
        cam.setPhoto_filename(lNameText.getText().concat("_").concat(fNameText.getText()).concat("_photo.jpg"));
        setPhotopath(con.getProp("default_photo_folder").concat(lNameText.getText().concat("_").concat(fNameText.getText()).concat("_photo.jpg")));
        cam.setVisible(true);
        
        File photofile = new File(con.getProp("default_photo_folder").concat(cam.getPhoto_filename()));
        cl1.setPhoto_loc(con.getProp("default_photo_folder").concat(cam.getPhoto_filename()));
        cam.addWindowListener(new WindowAdapter() {
            @Override
                    public void windowClosed (WindowEvent e) {
                        System.out.println("accessing window event");
                        refreshPhoto(cam.isPhotoCaptured(), photofile.exists(), photofile, photo);
                    }
            
        });
    }//GEN-LAST:event_updatePhotoActionPerformed

    private void updateIDActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_updateIDActionPerformed
        Camera cam2 = new Camera();
        cam2.setTitle("Capture Client ID");
        cam2.setPhotomode(false);
        setIdTaken(true);
        setPhotoTaken(false);
        cam2.setLocationRelativeTo(null);
        cam2.setId_filename(lNameText.getText().concat("_").concat(fNameText.getText()).concat("_id.jpg"));
        setIdPath(con.getProp("default_photo_folder").concat(lNameText.getText().concat("_").concat(fNameText.getText()).concat("_id.jpg")));
        cam2.setVisible(true);
        File photofile = new File(con.getProp("default_photo_folder").concat(cam2.getId_filename()));
        cl1.setId_loc(con.getProp("default_photo_folder").concat(cam2.getId_filename()));
        cam2.addWindowListener(new WindowAdapter() {
            @Override
                    public void windowClosed (WindowEvent e) {
                        System.out.println("accessing window event");
                        refreshPhoto(cam2.isPhotoCaptured(), photofile.exists(), photofile, idPhoto);
                    }
            
        });
//        if (cam2.isIdCaptured()&& photofile.exists()) {
//            try {
//                BufferedImage img=ImageIO.read(photofile);
//                ImageIcon icon = new ImageIcon(scaleImage(img, photo.getWidth(), photo.getHeight()));
//                photo.setIcon(icon);
//            } catch (IOException ex) {
//                JOptionPane.showMessageDialog(null, "Image Output Error! Please contact your database manager.\n" + ex);
//                Logger.getLogger(KnowYourClient.class.getName()).log(Level.SEVERE, null, ex);
//            }
//        }
    }//GEN-LAST:event_updateIDActionPerformed

    private void captureIDbtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_captureIDbtnActionPerformed
        JFileChooser chooseFile = new JFileChooser();
        FileNameExtensionFilter fnwf = new FileNameExtensionFilter("PNG JPG AND JPEG", "png", "jpeg", "jpg");
        chooseFile.setFileFilter(fnwf);
        int result = chooseFile.showOpenDialog(null);
        chooseFile.setVisible(true);
        if (result == JFileChooser.APPROVE_OPTION) {
            if(!chooseFile.getSelectedFile().getAbsolutePath().equals(null)) {
                setFileName(chooseFile.getSelectedFile().getAbsolutePath());
                setIdPath(chooseFile.getSelectedFile().getAbsolutePath());
                try {
                    BufferedImage img=ImageIO.read(new File(getFileName()));
                    ImageIcon icon = new ImageIcon(Scalr.resize(img, Scalr.Method.QUALITY, 240,180));
                    cl1.setIdimg(new ImageIcon(Scalr.resize(img, Scalr.Method.QUALITY, 640,480)));
                    ImageIO.write(Scalr.resize(img, Scalr.Method.QUALITY, 640,480), "jpg", new File(con.getProp("default_photo_folder") + fNameText.getText().concat(" " + lNameText.getText()) + "_id.jpg"));
                    idPhoto.setIcon(icon);
                    cl1.setId_loc(con.getProp("default_photo_folder") + fNameText.getText().concat(" " + lNameText.getText()) + "_id.jpg");
                } catch (IOException ex) {
                    JOptionPane.showMessageDialog(null, "Image Output Error! Please contact your database manager.\n" + ex);
                    Logger.getLogger(KnowYourClient.class.getName()).log(Level.SEVERE, null, ex);
                }
                chooseFile.setVisible(false);
            } else {
                int replaceFile = JOptionPane.showConfirmDialog(null, "Do you want to replace this photo?", "Replace Photo", JOptionPane.YES_NO_OPTION);
                if (replaceFile == JOptionPane.YES_OPTION) {
                    try {
                        BufferedImage img=ImageIO.read(new File(getFileName()));
                        ImageIcon icon = new ImageIcon(Scalr.resize(img, Scalr.Method.QUALITY, 240,180));
                    cl1.setIdimg(new ImageIcon(Scalr.resize(img, Scalr.Method.QUALITY, 640,480)));
                    ImageIO.write(Scalr.resize(img, Scalr.Method.QUALITY, 640,480), "jpg", new File(con.getProp("default_photo_folder") + fNameText.getText().concat(" " + lNameText.getText()) + "_id.jpg"));
                    idPhoto.setIcon(icon);
                    cl1.setId_loc(con.getProp("default_photo_folder") + fNameText.getText().concat(" " + lNameText.getText()) + "_id.jpg");
                    } catch (IOException ex) {
                        JOptionPane.showMessageDialog(null, "Image Output Error! Please contact your database manager.\n" + ex);
                        Logger.getLogger(KnowYourClient.class.getName()).log(Level.SEVERE, null, ex);
                    }
                } else {

                }
                chooseFile.setVisible(false);
            }
        } else if (result == JFileChooser.CANCEL_OPTION) {

        }
    }//GEN-LAST:event_captureIDbtnActionPerformed

    private void photoMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_photoMouseClicked
        if (isRecordRetrieved()) {
            ViewID vid = new ViewID(null, true);
            vid.setLocationRelativeTo(null);
            vid.showID(cl1.getPhotoimg());
            vid.setVisible(true);
        }
    }//GEN-LAST:event_photoMouseClicked

    private void newCancelKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_newCancelKeyPressed
        int key = evt.getKeyCode();
        switch (key) {
            case KeyEvent.VK_F1:
            newCancelActionPerformed(null);
            break;
            case KeyEvent.VK_F2:
            editSaveActionPerformed(null);
            break;
            case KeyEvent.VK_F3:
            searchBtnActionPerformed(null);
            break;
        }
    }//GEN-LAST:event_newCancelKeyPressed

    private void mNameTextFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_mNameTextFocusLost
                // TODO add your handling code here:
    }//GEN-LAST:event_mNameTextFocusLost

    private void formFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_formFocusGained
        if (isPhotoTaken()) {
            if (new File(cl1.getPhoto_loc()).exists()) {
                try {
                    BufferedImage img=ImageIO.read(new File(cl1.getPhoto_loc()));
                    ImageIcon icon = new ImageIcon(scaleImage(img, 240, 180));
                    photo.setIcon(icon);
                } catch (IOException ex) {
                    JOptionPane.showMessageDialog(null, "Image Output Error! Please contact your database manager.\n" + ex);
                    Logger.getLogger(KnowYourClient.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        } 
        
        if (isIdTaken()) {
            if (new File(cl1.getId_loc()).exists()) {
                try {
                    BufferedImage img=ImageIO.read(new File(cl1.getId_loc()));
                    ImageIcon icon = new ImageIcon(scaleImage(img, 240, 180));
                    idPhoto.setIcon(icon);
                } catch (IOException ex) {
                    JOptionPane.showMessageDialog(null, "Image Output Error! Please contact your database manager.\n" + ex);
                    Logger.getLogger(KnowYourClient.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        } 
    }//GEN-LAST:event_formFocusGained

    private void idPhotoMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_idPhotoMouseClicked
        if (isRecordRetrieved()) {
            ViewID vid = new ViewID(null, true);
            vid.setLocationRelativeTo(null);
//            vid.showID(getViewPhoto());
            vid.showID(cl1.getIdimg());
            vid.setVisible(true);
        }
    }//GEN-LAST:event_idPhotoMouseClicked

    private void conNumTextKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_conNumTextKeyTyped
        char c = evt.getKeyChar();
        if ( ((c < '0') || (c > '9')) && (c != KeyEvent.VK_BACK_SPACE)) {
            evt.consume();  // if it's not a number, ignore the event
        }
        if (conNumText.getText().length() > 12) {
            evt.consume();
        }
    }//GEN-LAST:event_conNumTextKeyTyped

    private void tinTextKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_tinTextKeyTyped
        char c = evt.getKeyChar();
        if ( ((c < '0') || (c > '9')) && (c != KeyEvent.VK_BACK_SPACE) && (c != KeyEvent.VK_MINUS)) {
            evt.consume();  // if it's not a number, ignore the event
        }
    }//GEN-LAST:event_tinTextKeyTyped

    private void sssTextKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_sssTextKeyTyped
        char c = evt.getKeyChar();
        if ( ((c < '0') || (c > '9')) && (c != KeyEvent.VK_BACK_SPACE) && (c != KeyEvent.VK_MINUS)) {
            evt.consume();  // if it's not a number, ignore the event
        }
    }//GEN-LAST:event_sssTextKeyTyped

    private void gsisTextKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_gsisTextKeyTyped
        char c = evt.getKeyChar();
        if ( ((c < '0') || (c > '9')) && (c != KeyEvent.VK_BACK_SPACE) && (c != KeyEvent.VK_MINUS)) {
            evt.consume();  // if it's not a number, ignore the event
        }
    }//GEN-LAST:event_gsisTextKeyTyped

    private void natureBusTextKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_natureBusTextKeyReleased
        if (evt.getKeyCode() == KeyEvent.VK_BACK_SPACE || evt.getKeyCode() == KeyEvent.VK_DELETE) {
            
        } else {
            String to_check = natureBusText.getText();
            int to_check_len = to_check.length();
            for (String data:s) {
                
                String check_from_data = "";
                for (int i = 0; i < to_check_len; i++) {
                    
                    if (to_check_len <= data.length()) {
                        check_from_data = check_from_data + data.charAt(i);
                    }
                }
                if (check_from_data.equals(to_check)) {
                    natureBusText.setText(data);
                    natureBusText.setSelectionStart(to_check_len);
                    natureBusText.setSelectionEnd(data.length());
                    break;
                }
            }
        }
    }//GEN-LAST:event_natureBusTextKeyReleased

    private void natTextKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_natTextKeyReleased
        if (evt.getKeyCode() == KeyEvent.VK_BACK_SPACE || evt.getKeyCode() == KeyEvent.VK_DELETE) {
            
        } else {
            String to_check = natText.getText();
            int to_check_len = to_check.length();
            for (String data:u) {
                
                String check_from_data = "";
                for (int i = 0; i < to_check_len; i++) {
                    
                    if (to_check_len <= data.length()) {
                        check_from_data = check_from_data + data.charAt(i);
                    }
                }
                if (check_from_data.equals(to_check)) {
                    natText.setText(data);
                    natText.setSelectionStart(to_check_len);
                    natText.setSelectionEnd(data.length());
                    break;
                }
            }
        }
    }//GEN-LAST:event_natTextKeyReleased

    private void employerTextKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_employerTextKeyTyped
        if (employerText.getText().length() > 25) {
            evt.consume();
        }
    }//GEN-LAST:event_employerTextKeyTyped

    private void natureBusTextKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_natureBusTextKeyTyped
        if (natureBusText.getText().length() > 25) {
            evt.consume();
        }
    }//GEN-LAST:event_natureBusTextKeyTyped

    private void pBirthTextKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_pBirthTextKeyTyped
        if (pBirthText.getText().length() > 40) {
            evt.consume();
        }
    }//GEN-LAST:event_pBirthTextKeyTyped

    private void authRepBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_authRepBtnActionPerformed
        try {
            AuthRep arep = new AuthRep(null, true);
            arep.setLocationRelativeTo(null);
            if (cl1.isAuthRepsPresent()) {
                arep.enablePhotoButtons(true);
            }
            arep.setVisible(true);
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "Authorized Representative Error!\n" + ex);
            Logger.getLogger(KnowYourClient.class.getName()).log(Level.SEVERE, null, ex);
            System.exit(0);
        }
    }//GEN-LAST:event_authRepBtnActionPerformed

    private void searchTextActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_searchTextActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_searchTextActionPerformed

    private void createLoanBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_createLoanBtnActionPerformed
        setCreateLoan(true);
        setClientName(fNameText.getText().concat(" ").concat(lNameText.getText()));
        setClientNumber(conNumText.getText());
        if (streetText.getText().length() <= 0) {
            setClientAddress(String.format("%s, %s, %s",brgyCombo.getSelectedItem().toString(), townCombo.getSelectedItem().toString(), provCombo.getSelectedItem().toString()));

        } else {
            
            setClientAddress(String.format("%s, %s, %s, %s", streetText.getText(), brgyCombo.getSelectedItem().toString(), townCombo.getSelectedItem().toString(), provCombo.getSelectedItem().toString()));
        }
        setVisible(false);
    }//GEN-LAST:event_createLoanBtnActionPerformed

    private void formComponentShown(java.awt.event.ComponentEvent evt) {//GEN-FIRST:event_formComponentShown
        // TODO add your handling code here:
    }//GEN-LAST:event_formComponentShown

    private void photoPropertyChange(java.beans.PropertyChangeEvent evt) {//GEN-FIRST:event_photoPropertyChange
         
    }//GEN-LAST:event_photoPropertyChange

    private void photoComponentShown(java.awt.event.ComponentEvent evt) {//GEN-FIRST:event_photoComponentShown
        
    }//GEN-LAST:event_photoComponentShown

    private void photoMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_photoMouseEntered
        System.out.println("photo location is: " + cl1.getPhoto_loc());
        if (isPhotoTaken()) {
            if (new File(cl1.getPhoto_loc()).exists()) {
                try {
                    BufferedImage img=ImageIO.read(new File(cl1.getPhoto_loc()));
                    ImageIcon icon = new ImageIcon(scaleImage(img, photo.getWidth(), photo.getHeight()));
                    photo.setIcon(icon);
                } catch (IOException ex) {
                    JOptionPane.showMessageDialog(null, "Unable to display captured photo\n" + cl1.getPhoto_loc() + "\n" + ex);
//                    Logger.getLogger(KnowYourClient.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        } 
        
        
    }//GEN-LAST:event_photoMouseEntered

    private void idPhotoMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_idPhotoMouseEntered
        
//        if (isIdTaken()) {
//            if (new File(cl1.getId_loc()).exists()) {
//                try {
//                    BufferedImage img=ImageIO.read(new File(cl1.getId_loc()));
//                    ImageIcon icon = new ImageIcon(scaleImage(img, idPhoto.getWidth(), idPhoto.getHeight()));
//                    idPhoto.setIcon(icon);
//                } catch (IOException ex) {
//                    JOptionPane.showMessageDialog(null, "Image Output Error! Please contact your database manager.\n" + ex);
////                    Logger.getLogger(KnowYourClient.class.getName()).log(Level.SEVERE, null, ex);
//                }
//            }
//        } 
    }//GEN-LAST:event_idPhotoMouseEntered

    private void photoMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_photoMouseExited
        
    }//GEN-LAST:event_photoMouseExited


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton authRepBtn;
    private org.jdesktop.swingx.JXDatePicker bDateCal;
    private javax.swing.JComboBox<String> brgyCombo;
    private javax.swing.JComboBox<String> builtCombo;
    private javax.swing.JButton captureIDbtn;
    private javax.swing.JButton capturePhoto;
    private javax.swing.JComboBox<String> complexionCombo;
    private javax.swing.JTextField conNumText;
    private javax.swing.JButton createLoanBtn;
    private javax.swing.JButton editSave;
    private javax.swing.JTextField employerText;
    private javax.swing.JTextField fNameText;
    private javax.swing.JTextField gNameText;
    private javax.swing.JComboBox<String> genderCombo;
    private javax.swing.JTextField givenAddText;
    private javax.swing.JTextField gsisText;
    private javax.swing.JComboBox<String> heightCombo;
    private javax.swing.JLabel idPhoto;
    private javax.swing.JComboBox<String> idpresCombo;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel15;
    private javax.swing.JLabel jLabel18;
    private javax.swing.JLabel jLabel19;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel20;
    private javax.swing.JLabel jLabel21;
    private javax.swing.JLabel jLabel22;
    private javax.swing.JLabel jLabel23;
    private javax.swing.JLabel jLabel24;
    private javax.swing.JLabel jLabel25;
    private javax.swing.JLabel jLabel26;
    private javax.swing.JLabel jLabel27;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel32;
    private javax.swing.JLabel jLabel33;
    private javax.swing.JLabel jLabel34;
    private javax.swing.JLabel jLabel35;
    private javax.swing.JLabel jLabel36;
    private javax.swing.JLabel jLabel37;
    private javax.swing.JLabel jLabel38;
    private javax.swing.JLabel jLabel39;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel40;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel10;
    private javax.swing.JPanel jPanel11;
    private javax.swing.JPanel jPanel12;
    private javax.swing.JPanel jPanel13;
    private javax.swing.JPanel jPanel14;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JPanel jPanel7;
    private javax.swing.JPanel jPanel8;
    private javax.swing.JPanel jPanel9;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JTable jTable1;
    private javax.swing.JTextField lNameText;
    private javax.swing.JTextField mNameText;
    private javax.swing.JTextField natText;
    private javax.swing.JTextField natureBusText;
    private javax.swing.JButton newCancel;
    private javax.swing.JTextField pBirthText;
    private javax.swing.JLabel photo;
    private javax.swing.JComboBox<String> provCombo;
    private javax.swing.JComboBox<String> riskCombo;
    private javax.swing.JButton searchBtn;
    private javax.swing.JTextField searchText;
    private javax.swing.JComboBox<String> soiCombo;
    private javax.swing.JTextField spsNameText;
    private javax.swing.JTextField sssText;
    private javax.swing.JTextField streetText;
    private javax.swing.JComboBox<String> suffixCombo;
    private javax.swing.JTextField tinText;
    private javax.swing.JComboBox<String> townCombo;
    private javax.swing.JButton updateID;
    private javax.swing.JButton updatePhoto;
    // End of variables declaration//GEN-END:variables

    /**
     * @return the names
     */
    public String[] getNames() {
        return names;
    }

    /**
     * @param names the names to set
     */
    public void setNames(String[] names) {
        this.names = names;
    }
}
