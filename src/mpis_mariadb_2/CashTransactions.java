package mpis_mariadb_2;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import javax.swing.JOptionPane;

public class CashTransactions {

    private String transaction_id = "";

    private String transaction_remarks = "";

    private double transaction_amount = 0.0D;

    private String petty_cash_voucher = "";

    private String petty_cast_type = "";

    private String prep_by = "";

    private String rcvd_by = "";

    private String branch = "";

    private String loans_name = "";

    private String loans_item_code = "";

    private String cash_transaction_type = "";

    private String serial = "";

    private String fromBranch = "";

    private int transaction_no = 0;

    private boolean petty_cash_inserted = false;

    private boolean additionals_inserted = false;

    private boolean transfer_inserted = false;

    private boolean newloan_inserted = false;

    private boolean renewal_inserted = false;

    private boolean redeem_inserted = false;

    private boolean daily_trans_inserted = false;

    private double addsub = 0.0D;

    Config con = new Config();

    private final String driver = "jdbc:mariadb://" + this.con.getProp("IP") + ":" + this.con.getProp("port") + "/cashtransactions";

    private final String username = this.con.getProp("username");

    private final String password = this.con.getProp("password");

    Calendar rightNow = Calendar.getInstance();

    Date currentDate = this.rightNow.getTime();

    public SimpleDateFormat formatter = new SimpleDateFormat("MMddyy");

    public SimpleDateFormat dBformat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");

    String curDateText = this.formatter.format(this.currentDate);

    private String dateDBformat = this.dBformat.format(this.currentDate);

    private String xrefno = "";

    private String xdoc = "";

    private String database = "";

    NumberFormat papeletaFormat = new DecimalFormat("#00000");

    Sangla sangla = new Sangla();

    public void generateTransaction_id() {
        this.sangla.setBranch(this.branch);
        setSerial(this.papeletaFormat.format(this.transaction_no));
        String trans_id = this.curDateText.concat(getCash_transaction_type()).concat(this.sangla.getBranchCode(this.branch)).concat(this.serial);
        setTransaction_id(trans_id);
    }

    public void generateTransaction_remarks() {
        String remarks = "";
        if (!this.petty_cash_voucher.isEmpty()) {
            remarks = getTransaction_remarks();
        } else if (!this.loans_item_code.isEmpty()) {
            remarks = "<" + remarks.concat(getLoans_item_code() + "> ").concat(getTransaction_remarks()).concat(" by " + getLoans_name());
        } else {
            remarks = remarks.concat(getTransaction_remarks()).concat("(Received from: " + getLoans_name()) + "/" + getFromBranch() + ")";
        }
        setTransaction_remarks(remarks);
    }

    public boolean addPettyCashTransactionToDB() {
        String pettyCashIns = "Insert into " + getDatabase() + ".petty_cash (pty_id, pty_date, pty_branch, pty_type, pty_amount, pty_voucher, pty_remarks, pty_prep, pty_rcvd) values ( '" + this.transaction_id + "', '" + getDateDBformat() + "', '" + this.branch + "', '" + this.petty_cast_type + "', " + this.transaction_amount + ", '" + this.petty_cash_voucher + "', '" + this.transaction_remarks + "', '" + this.prep_by + "', '" + this.rcvd_by + "')";
        try {
            Connection connexion = DriverManager.getConnection(this.driver, this.username, this.password);
            Statement insert = connexion.createStatement();
            System.out.println(pettyCashIns);
            insert.executeUpdate(pettyCashIns);
            setPetty_cash_inserted(true);
            return true;
        } catch (SQLException ex) {
            this.con.saveProp("mpis_last_error", String.valueOf(ex));
            setPetty_cash_inserted(false);
            return false;
        }
    }

    public boolean delPettyCashTransactionToDB() {
        String pettyCashIns = "Delete from " + getDatabase() + ".petty_cash where pty_voucher = '" + getPetty_cash_voucher() + "'";
        try {
            Connection connexion = DriverManager.getConnection(this.driver, this.username, this.password);
            Statement insert = connexion.createStatement();
            insert.executeUpdate(pettyCashIns);
            return true;
        } catch (SQLException ex) {
            this.con.saveProp("mpis_last_error", String.valueOf(ex));
            return false;
        }
    }

    public boolean addTransferTransaction(String fromBranch, String toBranch) {
        try {
            String query = "Insert into " + getDatabase() + ".transfer (x_id, x_amount, x_branch, x_dest, x_prepared, x_received, x_date, x_no) values ('" + this.transaction_id + "', " + this.transaction_amount + ", '" + fromBranch + "', '" + toBranch + "', '" + this.prep_by + "', '" + this.rcvd_by + "', '" + getDateDBformat() + "', '" + getXrefno() + "')";
            Connection connect = DriverManager.getConnection(this.driver, this.username, this.password);
            Statement state = connect.createStatement();
            System.out.println(query);
            state.executeUpdate(query);
            connect.close();
            state.close();
            setTransfer_inserted(true);
            return true;
        } catch (SQLException ex) {
            this.con.saveProp("mpis_last_error", String.valueOf(ex));
            setTransfer_inserted(false);
            return false;
        }
    }

    public boolean delTransferTransaction(String fromBranch, String toBranch) {
        try {
            String query = "Delete from " + getDatabase() + ".transfer where x_id = '" + this.transaction_id + "'";
            Connection connect = DriverManager.getConnection(this.driver, this.username, this.password);
            Statement state = connect.createStatement();
            System.out.println(query);
            state.executeUpdate(query);
            connect.close();
            state.close();
            return true;
        } catch (SQLException ex) {
            this.con.saveProp("mpis_last_error", String.valueOf(ex));
            return false;
        }
    }

    public boolean addDocTransferTransaction(String fromBranch, String toBranch) {
        try {
            String query = "Insert into " + getDatabase() + ".transfer (x_id, x_doc, x_branch, x_dest, x_prepared, x_received, x_date, x_no) values ('" + this.transaction_id + "', '" + getXdoc() + "', '" + fromBranch + "', '" + toBranch + "', '" + this.prep_by + "', '" + this.rcvd_by + "', '" + getDateDBformat() + "', '" + getXrefno() + "')";
            Connection connect = DriverManager.getConnection(this.driver, this.username, this.password);
            Statement state = connect.createStatement();
            System.out.println(query);
            state.executeUpdate(query);
            connect.close();
            state.close();
            setTransfer_inserted(true);
            return true;
        } catch (SQLException ex) {
            this.con.saveProp("mpis_last_error", String.valueOf(ex));
            setTransfer_inserted(false);
            return false;
        }
    }

    public boolean delDocTransferTransaction(String fromBranch, String toBranch) {
        try {
            String query = "Delete from " + getDatabase() + ".transfer where x_id = '" + this.transaction_id + "'";
            Connection connect = DriverManager.getConnection(this.driver, this.username, this.password);
            Statement state = connect.createStatement();
            System.out.println(query);
            state.executeUpdate(query);
            connect.close();
            state.close();
            return true;
        } catch (SQLException ex) {
            this.con.saveProp("mpis_last_error", String.valueOf(ex));
            return false;
        }
    }

    public void checkAddType(String db) throws SQLException {
        String query = "SELECT count(*) FROM information_schema.COLUMNS WHERE TABLE_SCHEMA = '" + db + "' AND TABLE_NAME = 'additionals' AND COLUMN_NAME = 'add_type' ";
        String alter = "ALTER TABLE " + db + ".`additionals` ADD COLUMN `add_type` VARCHAR(30) NULL AFTER `add_sub`;";
        try {
            Connection connexion = DriverManager.getConnection(this.driver, this.username, this.password);
            Statement insert = connexion.createStatement();
            ResultSet rset = insert.executeQuery(query);
            int count = 0;
            while (rset.next()) {
                count = rset.getInt(1);
            }
            if (count <= 0) {
                insert.executeUpdate(alter);
            }
            connexion.close();
            insert.close();
        } catch (SQLException ex) {
            this.con.saveProp("mpis_last_error", String.valueOf(ex));
            JOptionPane.showMessageDialog(null, "Error in inserting add-type");
        }
    }

    public boolean addAdditionalTransactionToDB() {
        String additionalsInsert = "Insert into " + getDatabase() + ".additionals (add_id, add_date, add_branch, add_amount, add_from, add_remarks, add_dest, ref_no, add_by, add_sub, add_type) values ( '" + this.transaction_id + "', '" + getDateDBformat() + "', '" + this.branch + "', " + this.transaction_amount + ", '" + this.loans_name + "', '" + this.transaction_remarks + "', '" + getFromBranch() + "', '" + getXrefno() + "', '" + getRcvd_by() + "', " + getAddsub() + ", '" + getPetty_cast_type() + "')";
        try {
            Connection connexion = DriverManager.getConnection(this.driver, this.username, this.password);
            Statement insert = connexion.createStatement();
            System.out.println(additionalsInsert);
            insert.executeUpdate(additionalsInsert);
            setAdditionals_inserted(true);
            return true;
        } catch (SQLException ex) {
            this.con.saveProp("mpis_last_error", String.valueOf(ex));
            setAdditionals_inserted(false);
            return false;
        }
    }

    public boolean addAdditionalTransactionToDB(String database) {
        String additionalsInsert = "Insert into " + database + ".additionals (add_id, add_date, add_branch, add_amount, add_from, add_remarks, add_dest, ref_no, add_by, add_sub, add_type) values ( '" + this.transaction_id + "', '" + getDateDBformat() + "', '" + this.branch + "', " + this.transaction_amount + ", '" + this.loans_name + "', '" + this.transaction_remarks + "', '" + getFromBranch() + "', '" + getXrefno() + "', '" + getRcvd_by() + "', " + getAddsub() + ", '" + getPetty_cast_type() + "')";
        try {
            Connection connexion = DriverManager.getConnection(this.driver, this.username, this.password);
            Statement insert = connexion.createStatement();
            System.out.println(additionalsInsert);
            insert.executeUpdate(additionalsInsert);
            System.out.println(additionalsInsert);
            setAdditionals_inserted(true);
            return true;
        } catch (SQLException ex) {
            this.con.saveProp("mpis_last_error", String.valueOf(ex));
            setAdditionals_inserted(false);
            return false;
        }
    }

    public boolean delAdditionalTransactionToDB() {
        String additionalsInsert = "Delete from " + getDatabase() + ".additionals add_id = '" + this.transaction_id + "'";
        try {
            Connection connexion = DriverManager.getConnection(this.driver, this.username, this.password);
            Statement insert = connexion.createStatement();
            System.out.println(additionalsInsert);
            insert.executeUpdate(additionalsInsert);
            return true;
        } catch (SQLException ex) {
            this.con.saveProp("mpis_last_error", String.valueOf(ex));
            return false;
        }
    }

    public boolean addNewLoanTransactionToDB() {
        String additionalsInsert = "Insert into " + getDatabase() + ".new_loans (new_id, new_date, new_branch, new_amount, new_remarks) values ( '" + this.transaction_id + "', '" + getDateDBformat() + "', '" + this.branch + "', " + this.transaction_amount + ", '" + this.transaction_remarks + "')";
        try {
            Connection connexion = DriverManager.getConnection(this.driver, this.username, this.password);
            Statement insert = connexion.createStatement();
            insert.executeUpdate(additionalsInsert);
            setNewloan_inserted(true);
            return true;
        } catch (SQLException ex) {
            this.con.saveProp("mpis_last_error", String.valueOf(ex));
            setNewloan_inserted(false);
            return false;
        }
    }

    public boolean delNewLoanTransactionToDB() {
        String additionalsInsert = "DElete from " + getDatabase() + ".new_loans where new_id = '" + this.transaction_id + "'";
        try {
            Connection connexion = DriverManager.getConnection(this.driver, this.username, this.password);
            Statement insert = connexion.createStatement();
            insert.executeUpdate(additionalsInsert);
            return true;
        } catch (SQLException ex) {
            this.con.saveProp("mpis_last_error", String.valueOf(ex));
            return false;
        }
    }

    public boolean addRenewalTransactionToDB() {
        String additionalsInsert = "Insert into " + getDatabase() + ".renewals (rnw_id, rnw_date, rnw_branch, rnw_amount, rnw_remarks) values ( '" + this.transaction_id + "', '" + getDateDBformat() + "', '" + this.branch + "', " + this.transaction_amount + ", '" + this.transaction_remarks + "')";
        try {
            Connection connexion = DriverManager.getConnection(this.driver, this.username, this.password);
            Statement insert = connexion.createStatement();
            insert.executeUpdate(additionalsInsert);
            setRenewal_inserted(true);
            return true;
        } catch (SQLException ex) {
            this.con.saveProp("mpis_last_error", String.valueOf(ex));
            setRenewal_inserted(false);
            return false;
        }
    }

    public boolean delRenewalTransactionToDB() {
        String additionalsInsert = "Delete from " + getDatabase() + ".renewals where rnw_id = '" + this.transaction_id + "'";
        try {
            Connection connexion = DriverManager.getConnection(this.driver, this.username, this.password);
            Statement insert = connexion.createStatement();
            insert.executeUpdate(additionalsInsert);
            return true;
        } catch (SQLException ex) {
            this.con.saveProp("mpis_last_error", String.valueOf(ex));
            return false;
        }
    }

    public boolean addRedeemTransactionToDB() {
        String additionalsInsert = "Insert into " + getDatabase() + ".redeem (rdm_id, rdm_date, rdm_branch, rdm_amount, rdm_remarks) values ( '" + this.transaction_id + "', '" + getDateDBformat() + "', '" + this.branch + "', " + this.transaction_amount + ", '" + this.transaction_remarks + "')";
        try {
            Connection connexion = DriverManager.getConnection(this.driver, this.username, this.password);
            Statement insert = connexion.createStatement();
            insert.executeUpdate(additionalsInsert);
            setRedeem_inserted(true);
            return true;
        } catch (SQLException ex) {
            this.con.saveProp("mpis_last_error", String.valueOf(ex));
            setRedeem_inserted(false);
            return false;
        }
    }

    public boolean delRedeemTransactionToDB() {
        String additionalsInsert = "Delete from " + getDatabase() + ".redeem where rdm_id = '" + this.transaction_id + "'";
        try {
            Connection connexion = DriverManager.getConnection(this.driver, this.username, this.password);
            Statement insert = connexion.createStatement();
            insert.executeUpdate(additionalsInsert);
            return true;
        } catch (SQLException ex) {
            this.con.saveProp("mpis_last_error", String.valueOf(ex));
            return false;
        }
    }

    public boolean addToDailyTransaction() {
        String tableInsert = "Insert into " + getDatabase() + ".daily_transactions (transaction_name, date, amount, remarks) values ( '" + this.transaction_id + "', '" + getDateDBformat() + "', " + this.transaction_amount + ", '" + this.transaction_remarks + "')";
        try {
            Connection connexion = DriverManager.getConnection(this.driver, this.username, this.password);
            Statement insert = connexion.createStatement();
            System.out.println("daily trans \n" + tableInsert);
            insert.executeUpdate(tableInsert);
            setDaily_trans_inserted(true);
            return true;
        } catch (SQLException ex) {
            this.con.saveProp("mpis_last_error", String.valueOf(ex));
            setDaily_trans_inserted(false);
            System.out.println("Error in daily transaction" + ex);
            return false;
        }
    }

    public boolean addToDailyTransaction(String db) {
        String tableInsert = "Insert into " + db + ".daily_transactions (transaction_name, date, amount, remarks) values ( '" + this.transaction_id + "', '" + getDateDBformat() + "', " + this.transaction_amount + ", '" + this.transaction_remarks + "')";
        try {
            Connection connexion = DriverManager.getConnection(this.driver, this.username, this.password);
            Statement insert = connexion.createStatement();
            System.out.println("daily trans \n" + tableInsert);
            insert.executeUpdate(tableInsert);
            setDaily_trans_inserted(true);
            return true;
        } catch (SQLException ex) {
            this.con.saveProp("mpis_last_error", String.valueOf(ex));
            setDaily_trans_inserted(false);
            System.out.println("Error in daily transaction" + ex);
            return false;
        }
    }

    public boolean delToDailyTransaction() {
        String tableInsert = "Delete from " + getDatabase() + ".daily_transactions where transaction_name = '" + this.transaction_id + "'";
        try {
            Connection connexion = DriverManager.getConnection(this.driver, this.username, this.password);
            Statement insert = connexion.createStatement();
            System.out.println("daily trans \n" + tableInsert);
            insert.executeUpdate(tableInsert);
            return true;
        } catch (SQLException ex) {
            this.con.saveProp("mpis_last_error", String.valueOf(ex));
            return false;
        }
    }

    public String getTransaction_id() {
        return this.transaction_id;
    }

    public void setTransaction_id(String transaction_id) {
        this.transaction_id = transaction_id;
    }

    public String getTransaction_remarks() {
        return this.transaction_remarks;
    }

    public void setTransaction_remarks(String transaction_remarks) {
        this.transaction_remarks = transaction_remarks;
    }

    public double getTransaction_amount() {
        return this.transaction_amount;
    }

    public void setTransaction_amount(double transaction_amount) {
        this.transaction_amount = transaction_amount;
    }

    public String getPetty_cash_voucher() {
        return this.petty_cash_voucher;
    }

    public void setPetty_cash_voucher(String petty_cash_voucher) {
        this.petty_cash_voucher = petty_cash_voucher;
    }

    public String getPetty_cast_type() {
        return this.petty_cast_type;
    }

    public void setPetty_cast_type(String petty_cast_type) {
        this.petty_cast_type = petty_cast_type;
    }

    public String getBranch() {
        return this.branch;
    }

    public void setBranch(String branch) {
        this.branch = branch;
    }

    public String getLoans_name() {
        return this.loans_name;
    }

    public void setLoans_name(String loans_name) {
        this.loans_name = loans_name;
    }

    public String getLoans_item_code() {
        return this.loans_item_code;
    }

    public void setLoans_item_code(String loans_item_code) {
        this.loans_item_code = loans_item_code;
    }

    public String getCash_transaction_type() {
        return this.cash_transaction_type;
    }

    public void setCash_transaction_type(String cash_transaction_type) {
        this.cash_transaction_type = cash_transaction_type;
    }

    public String getSerial() {
        return this.serial;
    }

    public void setSerial(String serial) {
        this.serial = serial;
    }

    public int getTransaction_no() {
        return this.transaction_no;
    }

    public void setTransaction_no(int transaction_no) {
        this.transaction_no = transaction_no;
    }

    public boolean isPetty_cash_inserted() {
        return this.petty_cash_inserted;
    }

    public void setPetty_cash_inserted(boolean petty_cash_inserted) {
        this.petty_cash_inserted = petty_cash_inserted;
    }

    public boolean isAdditionals_inserted() {
        return this.additionals_inserted;
    }

    public void setAdditionals_inserted(boolean additionals_inserted) {
        this.additionals_inserted = additionals_inserted;
    }

    public boolean isNewloan_inserted() {
        return this.newloan_inserted;
    }

    public void setNewloan_inserted(boolean newloan_inserted) {
        this.newloan_inserted = newloan_inserted;
    }

    public boolean isRenewal_inserted() {
        return this.renewal_inserted;
    }

    public void setRenewal_inserted(boolean renewal_inserted) {
        this.renewal_inserted = renewal_inserted;
    }

    public boolean isRedeem_inserted() {
        return this.redeem_inserted;
    }

    public void setRedeem_inserted(boolean redeem_inserted) {
        this.redeem_inserted = redeem_inserted;
    }

    public boolean isDaily_trans_inserted() {
        return this.daily_trans_inserted;
    }

    public void setDaily_trans_inserted(boolean daily_trans_inserted) {
        this.daily_trans_inserted = daily_trans_inserted;
    }

    public String getDateDBformat() {
        return this.dateDBformat;
    }

    public void setDateDBformat(String dateDBformat) {
        this.dateDBformat = dateDBformat;
    }

    public String getPrep_by() {
        return this.prep_by;
    }

    public void setPrep_by(String prep_by) {
        this.prep_by = prep_by;
    }

    public String getRcvd_by() {
        return this.rcvd_by;
    }

    public void setRcvd_by(String rcvd_by) {
        this.rcvd_by = rcvd_by;
    }

    public boolean isTransfer_inserted() {
        return this.transfer_inserted;
    }

    public void setTransfer_inserted(boolean transfer_inserted) {
        this.transfer_inserted = transfer_inserted;
    }

    public String getXrefno() {
        return this.xrefno;
    }

    public void setXrefno(String xrefno) {
        this.xrefno = xrefno;
    }

    public String getXdoc() {
        return this.xdoc;
    }

    public void setXdoc(String xdoc) {
        this.xdoc = xdoc;
    }

    public String getFromBranch() {
        return this.fromBranch;
    }

    public void setFromBranch(String fromBranch) {
        this.fromBranch = fromBranch;
    }

    public double getAddsub() {
        return this.addsub;
    }

    public void setAddsub(double addsub) {
        this.addsub = addsub;
    }

    public String getDatabase() {
        return this.database;
    }

    public void setDatabase(String database) {
        this.database = database;
    }
}
