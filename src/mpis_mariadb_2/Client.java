/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package mpis_mariadb_2;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

//import com.mysql.jdbc.Statement;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.ImageIcon;

/**
 *
 * @author Lucky
 */
public class Client {

    /**
     * @return the idkey
     */
    public Integer getIdkey() {
        return idkey;
    }

    /**
     * @param idkey the idkey to set
     */
    public void setIdkey(Integer idkey) {
        this.idkey = idkey;
    }

    /**
     * @return the authName1
     */
    public String getAuthName1() {
        return authName1;
    }

    /**
     * @param authName1 the authName1 to set
     */
    public void setAuthName1(String authName1) {
        this.authName1 = authName1;
    }

    /**
     * @return the authName2
     */
    public String getAuthName2() {
        return authName2;
    }

    /**
     * @param authName2 the authName2 to set
     */
    public void setAuthName2(String authName2) {
        this.authName2 = authName2;
    }

    /**
     * @return the authRel1
     */
    public String getAuthRel1() {
        return authRel1;
    }

    /**
     * @param authRel1 the authRel1 to set
     */
    public void setAuthRel1(String authRel1) {
        this.authRel1 = authRel1;
    }

    /**
     * @return the authRel2
     */
    public String getAuthRel2() {
        return authRel2;
    }

    /**
     * @param authRel2 the authRel2 to set
     */
    public void setAuthRel2(String authRel2) {
        this.authRel2 = authRel2;
    }

    /**
     * @return the authPhoto1
     */
    public ImageIcon getAuthPhoto1() {
        return authPhoto1;
    }

    /**
     * @param authPhoto1 the authPhoto1 to set
     */
    public void setAuthPhoto1(ImageIcon authPhoto1) {
        this.authPhoto1 = authPhoto1;
    }

    /**
     * @return the authAttach1
     */
    public ImageIcon getAuthAttach1() {
        return authAttach1;
    }

    /**
     * @param authAttach1 the authAttach1 to set
     */
    public void setAuthAttach1(ImageIcon authAttach1) {
        this.authAttach1 = authAttach1;
    }

    /**
     * @return the authPhoto2
     */
    public ImageIcon getAuthPhoto2() {
        return authPhoto2;
    }

    /**
     * @param authPhoto2 the authPhoto2 to set
     */
    public void setAuthPhoto2(ImageIcon authPhoto2) {
        this.authPhoto2 = authPhoto2;
    }

    /**
     * @return the authAttach2
     */
    public ImageIcon getAuthAttach2() {
        return authAttach2;
    }

    /**
     * @param authAttach2 the authAttach2 to set
     */
    public void setAuthAttach2(ImageIcon authAttach2) {
        this.authAttach2 = authAttach2;
    }

    /**
     * @return the photoimg
     */
    public ImageIcon getPhotoimg() {
        return photoimg;
    }

    /**
     * @param photoimg the photoimg to set
     */
    public void setPhotoimg(ImageIcon photoimg) {
        this.photoimg = photoimg;
    }

    /**
     * @return the idimg
     */
    public ImageIcon getIdimg() {
        return idimg;
    }

    /**
     * @param idimg the idimg to set
     */
    public void setIdimg(ImageIcon idimg) {
        this.idimg = idimg;
    }

    

    private final Config con = new Config();
    private final String driver = "jdbc:mariadb://" + con.getProp("IP") + ":" + con.getProp("port") + "/merlininventorydatabase";
    private final String f_user = con.getProp("username");
    private final String f_pass = con.getProp("password");
    
    
    private Integer idkey;
    private String given_name = "";
    private String first_name = "";
    private String middle_name = "";
    private String last_name = "";
    private String suffix = "";
    private String sps_name = "";
    private String address = "";
    private String contact_no = "";
    private String gender = "";
    private Date birthday = null;
    private String birthplace = "";
    private String nationality = "";
    private String given_add = "";
    private String street = "";
    private String brgy = "";
    private String town = "";
    private String prov = "";
    private String employer = "";
    private String nature_business = "";
    private String soi = "";
    private String tin = "";
    private String sss = "";
    private String gsis = "";
    private String photo_loc = "";
    private String id_loc = "";
    private String risk = "";
    private String id_presented = "";
    private ImageIcon photoimg = null;
    private ImageIcon idimg = null;
    
    private String authName1 = "";
    private String authName2 = "";
    private String authRel1 = "";
    private String authRel2 = "";
    private ImageIcon authPhoto1 = null;
    private ImageIcon authAttach1 = null;
    private ImageIcon authPhoto2 = null;
    private ImageIcon authAttach2 = null;
    
    
    
    public void resetClient() {
        setFirst_name("");
        setMiddle_name("");
        setLast_name("");
        setSuffix("");
        setSps_name("");
        setAddress("");
        setContact_no("");
        setGender("");
        setBirthday(null);
        setBirthplace("");
        setNationality("");
        setGiven_add("");
        setStreet("");
        setBrgy("");
        setTown("");
        setProv("");
        setEmployer("");
        setNature_business("");
        setSoi("");
        setTin("");
        setSss("");
        setGsis("");
        setPhoto_loc("");
        setId_loc("");
        setRisk("");
        setId_presented("");
        setPhotoimg(null);
        setIdimg(null);
    }
    
    
    
    public void filldetailsFromClientInfo() throws SQLException {
        Connection connect = DriverManager.getConnection(driver, f_user, f_pass);
        Statement state = connect.createStatement();
        ResultSet rset = state.executeQuery("select client_address, contact_no from merlininventorydatabase.client_info where client_name = '" + getGiven_name() + "' order by transaction_date desc limit 1");
        while (rset.next()) {
            setAddress(rset.getString(1));
            setContact_no(rset.getString(2));
        }
        rset.close();
//        rset = state.executeQuery("Select * from kyc where ")
        state.close();
        connect.close();
        
    }
    
    public void insertToKYCdatabase() throws SQLException{
        Connection connect = DriverManager.getConnection(driver, f_user, f_pass);
        Statement state = connect.createStatement();
        String updSt = "INSERT INTO `merlininventorydatabase`.`kyc` (`fname`, `mname`, `lname`, `suffix`, `gender`, `bdate`, `bplace`, `street`, `brgy`, `town`, `prov`, `employer`, `nat_bus`, `soi`, `tin`, `sss`, `gsis`, `risk`, `id_pres`, `photo`, `idimage`) VALUES ('"
                + getFirst_name() + "', '" + getMiddle_name() + "', '" + getLast_name() + "', '" + getSuffix() + "', '" + getGender() + "', '" + new SimpleDateFormat("yyyy-MM-dd").format(getBirthday())
                + "', '" + getBirthplace() + "', '" + getStreet() + "', '" + getBrgy() + "', '" + getTown() + "', '" + getProv() + "', '" + getEmployer() + "', '" + getNature_business() + "', '"
                + getSoi() + "', '" + getTin() + "', '" + getSss() + "', '" + getGsis() + "', '" + getRisk() + "', '" + getId_presented() + "', '" 
                + getPhoto_loc() + "', '" + getId_loc() + "')";
        state.executeUpdate(updSt);
        state.close();
        connect.close();
    }
    
    public int insertToKYCdatabase2() throws SQLException{
        Connection connect = DriverManager.getConnection(driver, f_user, f_pass);
        
        String updSt = "REPLACE INTO `merlininventorydatabase`.`kyc` (`fname`, `mname`, `lname`, `suffix`, `gender`, `bdate`, `bplace`, `street`, `brgy`, `town`, `prov`, `employer`, "
                + "`soi`, `tin`, `sss`, `gsis`, `risk`, `id_pres`, `face`, `nat_bus`, `idcard`, `spouse`, `nation`, `con_num`) "
                + "VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
        
        PreparedStatement state = connect.prepareStatement(updSt);
        
        state.setString(1, getFirst_name());
        state.setString(2, getMiddle_name());
        state.setString(3, getLast_name());
        state.setString(4, getSuffix());
        state.setString(5, getGender());
        state.setDate(6, getBirthday());
        state.setString(7, getBirthplace());
        state.setString(8, getStreet());
        state.setString(9, getBrgy());
        state.setString(10, getTown());
        state.setString(11, getProv());
        state.setString(12, getEmployer());
        state.setString(13, getSoi());
        state.setString(14, getTin());
        state.setString(15, getSss());
        state.setString(16, getGsis());
        state.setString(17, getRisk());
        state.setString(18, getId_presented());
//        state.setString(19, getPhoto_loc());
//        state.setString(18, getId_loc());
        state.setString(20, getNature_business());
        state.setString(22, getSps_name());
        state.setString(23, getNationality());
        state.setString(24, getContact_no());
        System.out.println(getPhoto_loc().concat(" & ").concat(getId_loc()));
        
        try {
            if (!getPhoto_loc().equalsIgnoreCase("")) {
                File f = new File(getPhoto_loc());
                if (f.exists()) {
                    InputStream is = new FileInputStream(f);
                    state.setBlob(19, is);
                } else {
                    state.setNull(19, java.sql.Types.BLOB);
                }
            } else {
                state.setNull(19, java.sql.Types.BLOB);
            }
            if (!getId_loc().equalsIgnoreCase("")) {
                File f2 = new File(getId_loc());
                if (f2.exists()) {
                    InputStream is2 = new FileInputStream(f2);
                    state.setBlob(21, is2);
                } else {
                    state.setNull(21, java.sql.Types.BLOB);
                }
            } else {
                state.setNull(21, java.sql.Types.BLOB);
            }
        } catch (FileNotFoundException ex) {
            Logger.getLogger(Client.class.getName()).log(Level.SEVERE, null, ex);
        }
        int inserted = state.executeUpdate();
        state.close();
        connect.close();
        return inserted;
    }
    
    public boolean isClientFoundInKYC(String fn, String ln) throws SQLException{
        Connection connect = DriverManager.getConnection(driver, f_user, f_pass);
        PreparedStatement pst = connect.prepareStatement("Select * from merlininventorydatabase.kyc where fname = ? and lname = ?");
        pst.setString(1, fn);
        pst.setString(2, ln);
        ResultSet rs = pst.executeQuery();
        boolean x;
        if (rs.next()) {
            setIdkey(rs.getInt("kyc_id"));
            setMiddle_name(rs.getString("mname"));
            setSuffix(rs.getString("suffix"));
            setGender(rs.getString("gender"));
            setBirthday(rs.getDate("bdate"));
            setBirthplace(rs.getString("bplace"));
            setSps_name(rs.getString("spouse"));
            setStreet(rs.getString("street"));
            setBrgy(rs.getString("brgy"));
            setTown(rs.getString("town"));
            setProv(rs.getString("prov"));
            setEmployer(rs.getString("employer"));
            setNature_business(rs.getString("nat_bus"));
            setSoi(rs.getString("soi"));
            setTin(rs.getString("tin"));
            setSss(rs.getString("sss"));
            setGsis(rs.getString("gsis"));
            setRisk(rs.getString("risk"));
            setId_presented(rs.getString("id_pres"));
            setNationality(rs.getString("nation"));
            setContact_no(rs.getString("con_num"));
            x = true;
        } else {
            x = false;
        }
        rs.close();
        pst.close();
        connect.close();
        return x;
    }
    
    
    public void retrieveFromKYC() throws SQLException{
        Connection connect = DriverManager.getConnection(driver, f_user, f_pass);
        String query = "Select * from merlininventorydatabase.kyc where fname = ? and lname = ?";
        PreparedStatement pst = connect.prepareStatement(query);
        System.out.println(query);
        pst.setString(1, getFirst_name());
        pst.setString(2, getLast_name());
        ResultSet rs = pst.executeQuery();
        while (rs.next()) {
            setMiddle_name(rs.getString("mname"));
            setSuffix(rs.getString("suffix"));
            setGender(rs.getString("gender"));
            setBirthday(rs.getDate("bdate"));
            setBirthplace(rs.getString("bplace"));
            setStreet(rs.getString("street"));
            setBrgy(rs.getString("brgy"));
            setTown(rs.getString("town"));
            setProv(rs.getString("prov"));
            setNature_business(rs.getString("nat_bus"));
            setEmployer(rs.getString("employer"));
            setSoi(rs.getString("soi"));
            setTin(rs.getString("tin"));
            setSss(rs.getString("sss"));
            setGsis(rs.getString("gsis"));
            setRisk(rs.getString("risk"));
            setId_presented(rs.getString("id_pres"));
            setNationality(rs.getString("nation"));
            setSps_name(rs.getString("spouse"));
        }
        rs.close();
        pst.close();
        connect.close();
    }
    
    public void saveAuthRep1 () throws SQLException{
        Connection connect = DriverManager.getConnection(driver, f_user, f_pass);
        
        String updSt = "UPDATE `merlininventorydatabase`.`kyc` SET `auth_name1` = ?, `auth_rel1` = ? where `kyc_id` = " + getIdkey(); 
        
        PreparedStatement state = connect.prepareStatement(updSt);
        
        state.setString(1, getAuthName1());
        state.setString(2, getAuthRel1());
        state.execute();
        state.close();
        connect.close();
    }
    
    public void saveAuthRep2 () throws SQLException{
        Connection connect = DriverManager.getConnection(driver, f_user, f_pass);
        
        String updSt = "UPDATE `merlininventorydatabase`.`kyc` SET `auth_name2` = ?, `auth_rel2` = ? where `kyc_id` = " + getIdkey(); 
        
        PreparedStatement state = connect.prepareStatement(updSt);
        
        state.setString(1, getAuthName2());
        state.setString(2, getAuthRel2());
        state.execute();
        state.close();
        connect.close();
    }
    
    public boolean isAuthRepsPresent () throws SQLException {
        boolean pres = false;
        Connection connect = DriverManager.getConnection(driver, f_user, f_pass);
        PreparedStatement pst = connect.prepareStatement("Select auth_name1, auth_rel1, auth_name2, auth_rel2 from merlininventorydatabase.kyc where kyc_id = ?");
        pst.setInt(1, getIdkey());
        ResultSet rs = pst.executeQuery();
        while (rs.next()) {
            if (rs.getString(1) == null || rs.getString(2) == null || rs.getString(3) == null || rs.getString(4) == null) {
                pres = false;
            } else {
                pres = true;
            }
        }
        pst.close();
        rs.close();
        connect.close();
        return pres;
    }
    
    /**
     * @return the given_name
     */
    public String getGiven_name() {
        return given_name;
    }

    /**
     * @param given_name the given_name to set
     */
    public void setGiven_name(String given_name) {
        this.given_name = given_name;
    }

    /**
     * @return the first_name
     */
    public String getFirst_name() {
        return first_name;
    }

    /**
     * @param first_name the first_name to set
     */
    public void setFirst_name(String first_name) {
        this.first_name = first_name;
    }

    /**
     * @return the middle_name
     */
    public String getMiddle_name() {
        return middle_name;
    }

    /**
     * @param middle_name the middle_name to set
     */
    public void setMiddle_name(String middle_name) {
        this.middle_name = middle_name;
    }

    /**
     * @return the last_name
     */
    public String getLast_name() {
        return last_name;
    }

    /**
     * @param last_name the last_name to set
     */
    public void setLast_name(String last_name) {
        this.last_name = last_name;
    }

    /**
     * @return the address
     */
    public String getAddress() {
        return address;
    }

    /**
     * @param address the address to set
     */
    public void setAddress(String address) {
        this.address = address;
    }

    /**
     * @return the contact_no
     */
    public String getContact_no() {
        return contact_no;
    }

    /**
     * @param contact_no the contact_no to set
     */
    public void setContact_no(String contact_no) {
        this.contact_no = contact_no;
    }

    /**
     * @return the gender
     */
    public String getGender() {
        return gender;
    }

    /**
     * @param gender the gender to set
     */
    public void setGender(String gender) {
        this.gender = gender;
    }

    /**
     * @return the birthday
     */
    public Date getBirthday() {
        return birthday;
    }

    /**
     * @param birthday the birthday to set
     */
    public void setBirthday(Date birthday) {
        this.birthday = birthday;
    }

    /**
     * @return the given_add
     */
    public String getGiven_add() {
        return given_add;
    }

    /**
     * @param given_add the given_add to set
     */
    public void setGiven_add(String given_add) {
        this.given_add = given_add;
    }

    /**
     * @return the street
     */
    public String getStreet() {
        return street;
    }

    /**
     * @param street the street to set
     */
    public void setStreet(String street) {
        this.street = street;
    }

    /**
     * @return the brgy
     */
    public String getBrgy() {
        return brgy;
    }

    /**
     * @param brgy the brgy to set
     */
    public void setBrgy(String brgy) {
        this.brgy = brgy;
    }

    /**
     * @return the town
     */
    public String getTown() {
        return town;
    }

    /**
     * @param town the town to set
     */
    public void setTown(String town) {
        this.town = town;
    }

    /**
     * @return the prov
     */
    public String getProv() {
        return prov;
    }

    /**
     * @param prov the prov to set
     */
    public void setProv(String prov) {
        this.prov = prov;
    }

    /**
     * @return the employer
     */
    public String getEmployer() {
        return employer;
    }

    /**
     * @param employer the employer to set
     */
    public void setEmployer(String employer) {
        this.employer = employer;
    }

    /**
     * @return the soi
     */
    public String getSoi() {
        return soi;
    }

    /**
     * @param soi the soi to set
     */
    public void setSoi(String soi) {
        this.soi = soi;
    }

    /**
     * @return the tin
     */
    public String getTin() {
        return tin;
    }

    /**
     * @param tin the tin to set
     */
    public void setTin(String tin) {
        this.tin = tin;
    }

    /**
     * @return the sss
     */
    public String getSss() {
        return sss;
    }

    /**
     * @param sss the sss to set
     */
    public void setSss(String sss) {
        this.sss = sss;
    }

    /**
     * @return the gsis
     */
    public String getGsis() {
        return gsis;
    }

    /**
     * @param gsis the gsis to set
     */
    public void setGsis(String gsis) {
        this.gsis = gsis;
    }
    
    
    /**
     * @return the nature_business
     */
    public String getNature_business() {
        return nature_business;
    }

    /**
     * @param nature_business the nature_business to set
     */
    public void setNature_business(String nature_business) {
        this.nature_business = nature_business;
    }

    /**
     * @return the id_presented
     */
    public String getId_presented() {
        return id_presented;
    }

    /**
     * @param id_presented the id_presented to set
     */
    public void setId_presented(String id_presented) {
        this.id_presented = id_presented;
    }

    /**
     * @return the sps_name
     */
    public String getSps_name() {
        return sps_name;
    }

    /**
     * @param sps_name the sps_name to set
     */
    public void setSps_name(String sps_name) {
        this.sps_name = sps_name;
    }

    /**
     * @return the birthplace
     */
    public String getBirthplace() {
        return birthplace;
    }

    /**
     * @param birthplace the birthplace to set
     */
    public void setBirthplace(String birthplace) {
        this.birthplace = birthplace;
    }

    /**
     * @return the nationality
     */
    public String getNationality() {
        return nationality;
    }

    /**
     * @param nationality the nationality to set
     */
    public void setNationality(String nationality) {
        this.nationality = nationality;
    }

    /**
     * @return the risk
     */
    public String getRisk() {
        return risk;
    }

    /**
     * @param risk the risk to set
     */
    public void setRisk(String risk) {
        this.risk = risk;
    }

    /**
     * @return the suffix
     */
    public String getSuffix() {
        return suffix;
    }

    /**
     * @param suffix the suffix to set
     */
    public void setSuffix(String suffix) {
        this.suffix = suffix;
    }

    /**
     * @return the photo_loc
     */
    public String getPhoto_loc() {
        return photo_loc;
    }

    /**
     * @param photo_loc the photo_loc to set
     */
    public void setPhoto_loc(String photo_loc) {
        this.photo_loc = photo_loc;
    }

    /**
     * @return the id_loc
     */
    public String getId_loc() {
        return id_loc;
    }

    /**
     * @param id_loc the id_loc to set
     */
    public void setId_loc(String id_loc) {
        this.id_loc = id_loc;
    }
    
}
