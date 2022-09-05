package com.merlin;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Sangla {

    /**
     * @return the motorIn
     */
    public int getMotorIn() {
        return motorIn;
    }

    /**
     * @param motorIn the motorIn to set
     */
    public void setMotorIn(int motorIn) {
        this.motorIn = motorIn;
    }

    Config con = new Config();

    private DecimalHelper decHelp = new DecimalHelper();
    private String client_name = "";
    private String client_address = "";
    private String old_branch = "";
    private String branch = "";
    private String branch_code = "";
    private String pap_num = "";
    private String old_pap_num = "";
    private String new_pap_num = "";
    private String contact_no = "";
    private String item_code_a = "";
    private double principal = 0.0D;
    private double advance_interest = 0.0D;
    private double advance_interest_rate = 0.0D;
    private double interest = 0.0D;
    private double interest_rate = 0.0D;
    private double insurance = 0.0D;
    private double service_charge = 5.0D;
    private double other_charges = 0.0D;
    private double net_proceeds = 0.0D;
    private double net_increase = 0.0D;
    private double net_decrease = 0.0D;
    private double stamp = 0.0D;
    private double liq_dam = 0.0D;
    private double aff = 0.0D;
    private double unpaid = 0.0D;
    private String item_description = "";
    private String classification = "";
    private String remarks = "";
    private String transaction_date = "";
    private String status = "";
    private String old_item_code;
    private String processed_by = "";
    private boolean proceedRetrieval = false;

    private boolean retrievalSuccessful = false;

    private boolean insertToClientStatus = false;

    private boolean updateOldPapStatus = false;

    private boolean updateTubosPapStatus = false;

    private boolean updateStatusToRepossessed = false;

    private boolean papNoAlreadyUsed = false;

    private boolean empInserted = false;

    private boolean resInserted = false;

    private boolean empDeleted = false;

    private boolean resDeleted = false;

    private boolean cliDeleted = false;

    private int motorIn = 0;
    private int subastaStatus = 0;

    private int tubosStatus = 0;

    private DateHelper dateHelp = new DateHelper();

    private final String driver = "jdbc:mariadb://" + this.con.getProp("IP") + ":" + this.con.getProp("port") + "/cashtransactions";

    private final String username = this.con.getProp("username");

    private final String password = this.con.getProp("password");
    private DatabaseUpdater dbu = new DatabaseUpdater();
    public Double computeOtherCharges() {
        double oth = getLiq_dam() + getStamp() + getAff() + getUnpaid();
        return Double.valueOf(oth);
    }

    public boolean saveToEmpeno() {
        try {
            Connection connect = DriverManager.getConnection(this.driver, this.username, this.password);
            Statement state = connect.createStatement();
            String insertStatement = "Insert into merlininventorydatabase.empeno (pap_num, date, principal, ai, sc,insurance, other_charges) values ('" + this.item_code_a + "', '" + this.transaction_date + "', " + this.principal + ", " + this.advance_interest + ", " + this.service_charge + ", " + this.insurance + ", " + this.other_charges + ")";
            dbu.writeToFile(insertStatement, dbu.getCurDateBackUpFilename());
            state.executeUpdate(insertStatement);
            connect.close();
            return true;
        } catch (SQLException ex) {
            this.con.saveProp("mpis_last_error", String.valueOf(ex));
            return false;
        }
    }

    public boolean saveToRescate() {
        try {
            Connection connect = DriverManager.getConnection(this.driver, this.username, this.password);
            Statement state = connect.createStatement();
            double total_interest = this.interest + getStamp() + getAff() + getUnpaid() + getLiq_dam();
            String insertStatement = "Replace into merlininventorydatabase.rescate (pap_num, date, principal, interest, stamp, liq_dam, affidavit, unpaid_ai, subasta, tubos) values ('" + this.old_pap_num + getBranchCode(getOld_branch()) + "', '" + this.transaction_date + "', " + this.principal + ", " + total_interest + ", " + getStamp() + ", " + getLiq_dam() + ", " + getAff() + ", " + getUnpaid() + ", " + getSubastaStatus() + ", " + getTubosStatus() + ")";
            state.executeUpdate(insertStatement);
            dbu.writeToFile(insertStatement, dbu.getCurDateBackUpFilename());
            connect.close();
            setResInserted(true);
            return true;
        } catch (SQLException ex) {
            this.con.saveProp("mpis_last_error", String.valueOf(ex));
            setResInserted(false);
            return false;
        }
    }

    public boolean saveToRescate(String given_id) {
        try {
            Connection connect = DriverManager.getConnection(this.driver, this.username, this.password);
            Statement state = connect.createStatement();
            double total_interest = this.interest + getStamp() + getAff() + getUnpaid() + getLiq_dam();
            String insertStatement = "Replace into merlininventorydatabase.rescate (pap_num, date, principal, interest, stamp, liq_dam, affidavit, unpaid_ai, subasta, tubos) values ('" + given_id + "', '" + this.transaction_date + "', " + this.principal + ", " + total_interest + ", " + getStamp() + ", " + getLiq_dam() + ", " + getAff() + ", " + getUnpaid() + ", " + getSubastaStatus() + ", " + getTubosStatus() + ")";
            state.executeUpdate(insertStatement);
            dbu.writeToFile(insertStatement, dbu.getCurDateBackUpFilename());
            connect.close();
            setResInserted(true);
            return true;
        } catch (SQLException ex) {
            this.con.saveProp("mpis_last_error", String.valueOf(ex));
            setResInserted(false);
            return false;
        }
    }

    public void initiateRetrieval(String item_code) {
        int resultCount = 0;
        try {
            Connection connexion = DriverManager.getConnection(this.driver, this.username, this.password);
            Statement qry = connexion.createStatement();
            String query = "SELECT COUNT(*) from merlininventorydatabase.client_info where item_code_a = '" + item_code + "'";
            System.out.println(query);
            ResultSet rs0 = qry.executeQuery(query);
            while (rs0.next()) {
                resultCount = rs0.getInt(1);
            }
            if (resultCount > 0) {
                setProceedRetrieval(true);
            } else {
                setProceedRetrieval(false);
            }
        } catch (SQLException ex) {
            this.con.saveProp("mpis_last_error", String.valueOf(ex));
        }
    }

    public void retrieveSangla(String item_code) {
        try {
            Connection connexion = DriverManager.getConnection(this.driver, this.username, this.password);
            Statement qry = connexion.createStatement();
            String squery = "SELECT * from merlininventorydatabase.client_info where item_code_a = '" + item_code + "'";
            System.out.println(squery);
            ResultSet rs1 = qry.executeQuery(squery);
            while (rs1.next()) {
                this.pap_num = rs1.getString("pap_num");
                this.client_name = rs1.getString("client_name");
                this.client_address = rs1.getString("client_address");
                this.contact_no = rs1.getString("contact_no");
                this.branch = rs1.getString("branch");
                this.processed_by = rs1.getString("processed_by");
                this.item_description = rs1.getString("item_description");
                this.classification = rs1.getString("classification");
                this.transaction_date = rs1.getString("transaction_date");
                this.status = rs1.getString("status");
                this.remarks = rs1.getString("remarks");
                this.old_pap_num = rs1.getString("old_pap_num");
                this.new_pap_num = rs1.getString("new_pap_num");
                this.retrievalSuccessful = true;
                setMotorIn(rs1.getInt("motor_in"));
            }
            connexion.close();
            Connection connect = DriverManager.getConnection(this.driver, this.username, this.password);
            Statement query = connect.createStatement();
            ResultSet rset = query.executeQuery("Select * from merlininventorydatabase.empeno where pap_num = '" + item_code + "'");
            while (rset.next()) {
                this.principal = rset.getDouble("principal");
                this.advance_interest = rset.getDouble("ai");
                this.insurance = rset.getDouble("insurance");
                this.service_charge = rset.getDouble("sc");
                this.other_charges = rset.getDouble("other_charges");
            }
            this.net_proceeds = this.principal - this.advance_interest - this.insurance - this.service_charge - this.other_charges;
            connect.close();
        } catch (SQLException ex) {
            this.con.saveProp("mpis_last_error", String.valueOf(ex));
            System.out.println("error in sql during retrieval of sangla");
            this.retrievalSuccessful = false;
        } catch (NullPointerException ex) {
            this.con.saveProp("mpis_last_error", String.valueOf(ex));
            System.out.println("null pointer exception error during retrieval of sangla");
            this.retrievalSuccessful = false;
        }
    }

    public String getBranchCode(String givenBranch) {
        try {
            Connection connect = DriverManager.getConnection(this.driver, this.username, this.password);
            Statement qry = connect.createStatement();
            ResultSet bCodeRS = qry.executeQuery("SELECT branch_code FROM merlininventorydatabase.branch_info WHERE branch_name = '" + givenBranch + "'");
            while (bCodeRS.next()) {
                this.branch_code = bCodeRS.getString("branch_code");
            }
            connect.close();
            qry.close();
        } catch (SQLException ex) {
            this.con.saveProp("mpis_last_error", String.valueOf(ex));
        }
        return this.branch_code;
    }

    public void setItemCodes() {
        this.item_code_a = this.pap_num.concat(getBranchCode(getBranch()));
    }

    public void setItemCodes(String given_item_code) {
        this.item_code_a = given_item_code;
    }

    public void setOld_item_code() {
        this.old_item_code = this.old_pap_num.concat(getBranchCode(getBranch()));
    }

    public boolean updateStatusToRepossesed() {
        String updateStatusRep = "Update merlininventorydatabase.client_info set status = '" + this.status + "', remarks = '" + this.remarks 
                + "' where item_code_a = '" + this.item_code_a + "'";
        try {
            Connection connexion = DriverManager.getConnection(this.driver, this.username, this.password);
            Statement qry = connexion.createStatement();
            qry.executeUpdate(updateStatusRep);
            dbu.writeToFile(updateStatusRep, dbu.getCurDateBackUpFilename());
            setUpdateStatusToRepossessed(true);
            return true;
        } catch (SQLException ex) {
            this.con.saveProp("mpis_last_error", String.valueOf(ex));
            setUpdateStatusToRepossessed(false);
            return false;
        }
    }

    public boolean updateStatusToClosed() {
        String updateStatusRep = "Update merlininventorydatabase.client_info set status = 'Closed', transaction_date = '" + getTransaction_date() 
                + "' where item_code_a = '" + this.item_code_a + "'";
        try {
            Connection connexion = DriverManager.getConnection(this.driver, this.username, this.password);
            Statement qry = connexion.createStatement();
            qry.executeUpdate(updateStatusRep);
            dbu.writeToFile(updateStatusRep, dbu.getCurDateBackUpFilename());
            setUpdateTubosPapStatus(true);
            return true;
        } catch (SQLException ex) {
            this.con.saveProp("mpis_last_error", String.valueOf(ex));
            setUpdateTubosPapStatus(false);
            return false;
        }
    }

    public boolean insertToClienInfo() {
        String clientInfoInsert = "INSERT INTO merlininventorydatabase.client_info (item_code_a, pap_num, client_name, client_address, contact_no, branch, processed_by, "
                + "item_description, classification, transaction_date, status, remarks, old_pap_num, new_pap_num, motor_in) VALUES ( '" 
                + this.item_code_a + "', '" + this.pap_num + "', '" + this.client_name + "', '" + this.client_address + "', '" + this.contact_no + "', '" 
                + this.branch + "', '" + this.processed_by + "', '" + this.item_description + "', '" + this.classification + "', '" + this.transaction_date + "', '" 
                + this.status + "', '" + this.remarks + "', '" + this.old_pap_num + "', '" + this.new_pap_num + "', " + this.motorIn + ")";
        System.out.println(clientInfoInsert);
        try {
            Connection connexion = DriverManager.getConnection(this.driver, this.username, this.password);
            Statement insert = connexion.createStatement();
            insert.executeUpdate(clientInfoInsert);
            dbu.writeToFile(clientInfoInsert, dbu.getCurDateBackUpFilename());
            setInsertToClientStatus(true);
            return true;
        } catch (SQLException ex) {
            System.out.println("insertToClientInfo: " + ex);
            this.con.saveProp("mpis_last_error", String.valueOf(ex));
            setInsertToClientStatus(false);
            return false;
        }
    }

    public boolean updateOldPapeleta(String old_num) {
        String oldPapUpdate = "UPDATE merlininventorydatabase.client_info SET new_pap_num = '" + this.pap_num + "', status = 'Renewed' WHERE item_code_a = '" + old_num + getBranchCode(this.old_branch) + "'";
        try {
            Connection connexion = DriverManager.getConnection(this.driver, this.username, this.password);
            Statement qry = connexion.createStatement();
            qry.executeUpdate(oldPapUpdate);
            dbu.writeToFile(oldPapUpdate, dbu.getCurDateBackUpFilename());
            setUpdateOldPapStatus(true);
            return true;
        } catch (SQLException ex) {
            this.con.saveProp("mpis_last_error", String.valueOf(ex));
            setUpdateOldPapStatus(false);
            return false;
        }
    }

    public boolean undoUpdateOldPapeleta(String old_num) {
        String oldPapUpdate = "UPDATE merlininventorydatabase.client_info SET new_pap_num = '" + this.pap_num + "', status = '" + getStatus() + "' WHERE item_code_a = '" + old_num + getBranchCode(this.old_branch) + "'";
        try {
            Connection connexion = DriverManager.getConnection(this.driver, this.username, this.password);
            Statement qry = connexion.createStatement();
            qry.executeUpdate(oldPapUpdate);
            dbu.writeToFile(oldPapUpdate, dbu.getCurDateBackUpFilename());
            setUpdateOldPapStatus(true);
            return true;
        } catch (SQLException ex) {
            this.con.saveProp("mpis_last_error", String.valueOf(ex));
            setUpdateOldPapStatus(false);
            return false;
        }
    }

    public boolean deleteEntryFromEmpeno() {
        try {
            Connection connect = DriverManager.getConnection(this.driver, this.username, this.password);
            Statement state = connect.createStatement();
            String insertStatement = "Delete from merlininventorydatabase.empeno where pap_num = '" + getItem_code_a() + "'";
            state.executeUpdate(insertStatement);
            dbu.writeToFile(insertStatement, dbu.getCurDateBackUpFilename());
            connect.close();
            setEmpDeleted(true);
            return true;
        } catch (SQLException ex) {
            this.con.saveProp("mpis_last_error", String.valueOf(ex));
            setEmpDeleted(false);
            return false;
        }
    }

    public boolean deleteEntryFromRescate() {
        try {
            Connection connect = DriverManager.getConnection(this.driver, this.username, this.password);
            Statement state = connect.createStatement();
            String insertStatement = "Delete from merlininventorydatabase.rescate where pap_num = '" + getItem_code_a() + "'";
            state.executeUpdate(insertStatement);
            dbu.writeToFile(insertStatement, dbu.getCurDateBackUpFilename());
            connect.close();
            setResDeleted(true);
            return true;
        } catch (SQLException ex) {
            this.con.saveProp("mpis_last_error", String.valueOf(ex));
            setResDeleted(false);
            return false;
        }
    }

    public boolean deleteEntryFromClient() {
        String deleteThis = "Delete from merlininventorydatabase.client_info where item_code_a = '" + this.item_code_a + "'";
        try {
            Connection connexion = DriverManager.getConnection(this.driver, this.username, this.password);
            Statement qry = connexion.createStatement();
            qry.executeUpdate(deleteThis);
            dbu.writeToFile(deleteThis, dbu.getCurDateBackUpFilename());
            qry.close();
            connexion.close();
            setCliDeleted(true);
            return true;
        } catch (SQLException ex) {
            this.con.saveProp("mpis_last_error", String.valueOf(ex));
            setCliDeleted(false);
            return false;
        }
    }

    public String getClient_name() {
        return this.client_name;
    }

    public void setClient_name(String client_name) {
        this.client_name = client_name;
    }

    public String getClient_address() {
        return this.client_address;
    }

    public void setClient_address(String client_address) {
        this.client_address = client_address;
    }

    public String getBranch() {
        return this.branch;
    }

    public void setBranch(String branch) {
        this.branch = branch;
    }

    public void setBranch_code(String branch_code) {
        this.branch_code = branch_code;
    }

    public String getPap_num() {
        return this.pap_num;
    }

    public void setPap_num(String papeleta_number) {
        this.pap_num = papeleta_number;
    }

    public String getOld_pap_num() {
        return this.old_pap_num;
    }

    public void setOld_pap_num(String old_papeleta_number) {
        this.old_pap_num = old_papeleta_number;
    }

    public String getNew_pap_num() {
        return this.new_pap_num;
    }

    public void setNew_pap_num(String new_papeleta_number) {
        this.new_pap_num = new_papeleta_number;
    }

    public double getPrincipal() {
        return this.principal;
    }

    public void setPrincipal(double principal) {
        this.principal = principal;
    }

    public double getAdvance_interest() {
        return this.advance_interest;
    }

    public void setAdvance_interest(double advance_interest) {
        this.advance_interest = advance_interest;
    }

    public double getAdvance_interest_rate() {
        return this.advance_interest_rate;
    }

    public void setAdvance_interest_rate(double advance_interest_rate) {
        this.advance_interest_rate = advance_interest_rate;
    }

    public double getInterest() {
        return this.interest;
    }

    public void setInterest(double interest) {
        this.interest = interest;
    }

    public double getInterest_rate() {
        return this.interest_rate;
    }

    public void setInterest_rate(double interest_rate) {
        this.interest_rate = interest_rate;
    }

    public double getService_charge() {
        return this.service_charge;
    }

    public void setService_charge(double service_charge) {
        this.service_charge = service_charge;
    }

    public double getOther_charges() {
        return this.other_charges;
    }

    public void setOther_charges(double other_charges) {
        this.other_charges = other_charges;
    }

    public double getNet_proceeds() {
        return this.net_proceeds;
    }

    public void setNet_proceeds(double net_proceeds) {
        this.net_proceeds = net_proceeds;
    }

    public String getItem_description() {
        return this.item_description;
    }

    public void setItem_description(String item_description) {
        this.item_description = item_description;
    }

    public String getClassification() {
        return this.classification;
    }

    public void setClassification(String classification) {
        this.classification = classification;
    }

    public String getRemarks() {
        return this.remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    public String getTransaction_date() {
        return this.transaction_date;
    }

    public void setTransaction_date(String transaction_date) {
        this.transaction_date = transaction_date;
    }

    public String getMaturity_date() {
        System.out.println(getTransaction_date());
        return this.dateHelp.oneMonth(getTransaction_date());
    }

    public String getExpiration_date() {
        return this.dateHelp.fourMonths(getTransaction_date());
    }

    public String getProcessed_by() {
        return this.processed_by;
    }

    public void setProcessed_by(String processed_by) {
        this.processed_by = processed_by;
    }

    public boolean isInsertToClientStatus() {
        return this.insertToClientStatus;
    }

    public void setInsertToClientStatus(boolean insertToClientStatus) {
        this.insertToClientStatus = insertToClientStatus;
    }

    public boolean isUpdateOldPapStatus() {
        return this.updateOldPapStatus;
    }

    public void setUpdateOldPapStatus(boolean updateOldPapStatus) {
        this.updateOldPapStatus = updateOldPapStatus;
    }

    public boolean isUpdateTubosPapStatus() {
        return this.updateTubosPapStatus;
    }

    public void setUpdateTubosPapStatus(boolean updateTubosPapStatus) {
        this.updateTubosPapStatus = updateTubosPapStatus;
    }

    public String getContact_no() {
        return this.contact_no;
    }

    public void setContact_no(String contact_no) {
        this.contact_no = contact_no;
    }

    public String getItem_code_a() {
        return this.item_code_a;
    }

    public void setItem_code_a(String item_code_a) {
        this.item_code_a = item_code_a;
    }

    public String getStatus() {
        return this.status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getOld_item_code() {
        return this.old_item_code;
    }

    public void setOld_item_code(String old_item_code) {
        this.old_item_code = old_item_code;
    }

    public boolean isRetrievalSuccessful() {
        return this.retrievalSuccessful;
    }

    public void setRetrievalSuccessful(boolean retrievalSuccessful) {
        this.retrievalSuccessful = retrievalSuccessful;
    }

    public boolean isProceedRetrieval() {
        return this.proceedRetrieval;
    }

    public void setProceedRetrieval(boolean proceedRetrieval) {
        this.proceedRetrieval = proceedRetrieval;
    }

    public boolean isUpdateStatusToRepossessed() {
        return this.updateStatusToRepossessed;
    }

    public void setUpdateStatusToRepossessed(boolean updateStatusToRepossessed) {
        this.updateStatusToRepossessed = updateStatusToRepossessed;
    }

    public double getNet_increase() {
        return this.net_increase;
    }

    public void setNet_increase(double net_increase) {
        this.net_increase = net_increase;
    }

    public double getNet_decrease() {
        return this.net_decrease;
    }

    public void setNet_decrease(double net_decrease) {
        this.net_decrease = net_decrease;
    }

    public double getInsurance() {
        return this.insurance;
    }

    public void setInsurance(double insurance) {
        this.insurance = insurance;
    }

    public String getOld_branch() {
        return this.old_branch;
    }

    public void setOld_branch(String old_branch) {
        this.old_branch = old_branch;
    }

    public boolean isPapNoAlreadyUsed() {
        try {
            Connection connect = DriverManager.getConnection(this.driver, this.username, this.password);
            Statement state = connect.createStatement();
            String query = "Select count(*) from merlininventorydatabase.client_info where pap_num = '" + getPap_num() + "'";
            ResultSet rset = state.executeQuery(query);
            while (rset.next()) {
                if (rset.getInt(1) > 0) {
                    setPapNoAlreadyUsed(true);
                    continue;
                }
                setPapNoAlreadyUsed(false);
            }
        } catch (SQLException ex) {
            this.con.saveProp("mpis_last_error", String.valueOf(ex));
            Logger.getLogger(Sangla.class.getName()).log(Level.SEVERE, (String) null, ex);
        }
        return this.papNoAlreadyUsed;
    }

    public void setPapNoAlreadyUsed(boolean papNoAlreadyUsed) {
        this.papNoAlreadyUsed = papNoAlreadyUsed;
    }

    public double getStamp() {
        return this.stamp;
    }

    public void setStamp(double stamp) {
        this.stamp = stamp;
    }

    public double getLiq_dam() {
        return this.liq_dam;
    }

    public void setLiq_dam(double liq_dam) {
        this.liq_dam = liq_dam;
    }

    public double getAff() {
        return this.aff;
    }

    public void setAff(double aff) {
        this.aff = aff;
    }

    public double getUnpaid() {
        return this.unpaid;
    }

    public void setUnpaid(double unpaid) {
        this.unpaid = unpaid;
    }

    public int getSubastaStatus() {
        return this.subastaStatus;
    }

    public void setSubastaStatus(int subastaStatus) {
        this.subastaStatus = subastaStatus;
    }

    public int getTubosStatus() {
        return this.tubosStatus;
    }

    public void setTubosStatus(int tubosStatus) {
        this.tubosStatus = tubosStatus;
    }

    public boolean isEmpInserted() {
        return this.empInserted;
    }

    public void setEmpInserted(boolean empInserted) {
        this.empInserted = empInserted;
    }

    public boolean isResInserted() {
        return this.resInserted;
    }

    public void setResInserted(boolean resInserted) {
        this.resInserted = resInserted;
    }

    public boolean isEmpDeleted() {
        return this.empDeleted;
    }

    public void setEmpDeleted(boolean empDeleted) {
        this.empDeleted = empDeleted;
    }

    public boolean isResDeleted() {
        return this.resDeleted;
    }

    public void setResDeleted(boolean resDeleted) {
        this.resDeleted = resDeleted;
    }

    public boolean isCliDeleted() {
        return this.cliDeleted;
    }

    public void setCliDeleted(boolean cliDeleted) {
        this.cliDeleted = cliDeleted;
    }
}
