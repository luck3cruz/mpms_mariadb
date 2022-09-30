package com.merlin;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Calendar;
import java.util.Date;

public class Accounting {
  private double new_loan = 0.0D;
  
  private double renewal = 0.0D;
  
  private double redemption = 0.0D;
  
  private double petty_cash = 0.0D;
  
  private double additional = 0.0D;
  
  private double beginning = 0.0D;
  
  private double ending = 0.0D;
  
  private double cash_in = 0.0D;
  
  private double cash_out = 0.0D;
  
  private double remaining = 0.0D;
  
  private DecimalHelper decHelp = new DecimalHelper();
  
  Config con = new Config();
  
  private final String driver = "jdbc:mariadb://" + this.con.getProp("IP") + ":" + this.con.getProp("port") + "/cashtransactions";
  
  private final String finalUsername = this.con.getProp("username");
  
  private final String finalPassword = this.con.getProp("password");
  private DatabaseUpdater dbu = new DatabaseUpdater();
  
  public String computeRemaining() throws SQLException {
    String cash_on_hand = "";
    this.remaining = getBeginningBalance() + this.cash_in - this.cash_out;
    cash_on_hand = this.decHelp.FormatNumber(this.remaining);
    return cash_on_hand;
  }
  
  public String computeCashIn() throws SQLException {
    String inCash = "";
    this.cash_in = this.cash_in + getTotalAdditionals() + getTotalRedeem() + getTotalRenewals();
    inCash = this.decHelp.FormatNumber(this.cash_in);
    updateCashInFlow();
    return inCash;
  }
  
  public String computeCashOut() throws SQLException {
    String outCash = "";
    this.cash_out = getTotalPettyCash() + getTotalNewLoans() + getTotalRenewalsWithNetInc() + getTotalTransfer();
    outCash = this.decHelp.FormatNumber(this.cash_out);
    updateCashOutFlow();
    return outCash;
  }
  
  public String computeEndingBalance() {
    String endingBal = "";
    this.ending = this.beginning + this.cash_in - this.cash_out;
    this.decHelp.FormatNumber(this.ending);
    return endingBal;
  }
  
  public Double computeEndingBal() {
    this.ending = this.beginning + this.cash_in - this.cash_out;
    return Double.valueOf(this.ending);
  }
  
  public void updateBeginningBalance() throws SQLException {
    Connection connect = DriverManager.getConnection(this.driver, this.finalUsername, this.finalPassword);
    Statement state = connect.createStatement();
    String uquery = "Update cashtransactions.running_balance set beginning_balance = " + getBeginning() + " where date = '" + getCurrentDate() + "'";
    state.executeUpdate(uquery);
    dbu.editFile(uquery, dbu.getCurDateBackUpFilename());
    connect.close();
    state.close();
  }
  
  public void updateEndingBalance() throws SQLException {
    Connection connect = DriverManager.getConnection(this.driver, this.finalUsername, this.finalPassword);
    Statement state = connect.createStatement();
    String uquery = "Update cashtransactions.running_balance set ending_balance = " + getEnding() + " where date = '" + getCurrentDate() + "'";
    state.executeUpdate(uquery);
    dbu.editFile(uquery, dbu.getCurDateBackUpFilename());
    connect.close();
    state.close();
  }
  
  public void updateFinalEndingBalance() throws SQLException {
    double shov = 0.0D;
    Connection connect3 = DriverManager.getConnection(this.driver, this.finalUsername, this.finalPassword);
    Statement state3 = connect3.createStatement();
    ResultSet rset3 = state3.executeQuery("Select ovsh from cashtransactions.breakdown where date(bd_date) = '" + getCurrentDate() + "' order by bd_date limit 1");
    while (rset3.next())
      shov = rset3.getDouble(1); 
    Connection connect = DriverManager.getConnection(this.driver, this.finalUsername, this.finalPassword);
    Statement state = connect.createStatement();
    String query = "Select * from cashtransactions.running_balance where date = '" + getCurrentDate() + "'";
    ResultSet rset = state.executeQuery(query);
    double new_end = 0.0D;
    while (rset.next())
      new_end = rset.getDouble("beginning_balance") - rset.getDouble("total_cash_outflow") + rset.getDouble("total_cash_inflow") + shov; 
    Connection connect2 = DriverManager.getConnection(this.driver, this.finalUsername, this.finalPassword);
    Statement state2 = connect2.createStatement();
    String uquery = "Update cashtransactions.running_balance set ending_balance = " + new_end + " where date = '" + getCurrentDate() + "'";
    state2.executeUpdate(uquery);
    dbu.editFile(uquery, dbu.getCurDateBackUpFilename());
    state2.close();
    connect2.close();
    state.close();
    connect.close();
    state3.close();
    connect3.close();
  }
  
  public void updateCashInFlow() throws SQLException {
    Connection connect = DriverManager.getConnection(this.driver, this.finalUsername, this.finalPassword);
    Statement state = connect.createStatement();
    String uquery = "Update cashtransactions.running_balance set total_cash_inflow = " + this.decHelp.FormatNumber(getCash_in()).replace(",", "") + " where date = '" + getCurrentDate() + "'";
    state.executeUpdate(uquery);
    dbu.editFile(uquery, dbu.getCurDateBackUpFilename());
    connect.close();
    state.close();
    updateFinalEndingBalance();
  }
  
  public void updateCashOutFlow() throws SQLException {
    Connection connect = DriverManager.getConnection(this.driver, this.finalUsername, this.finalPassword);
    Statement state = connect.createStatement();
    String uquery = "Update cashtransactions.running_balance set total_cash_outflow = " + this.decHelp.FormatNumber(getCash_out()).replace(",", "") + " where date = '" + getCurrentDate() + "'";
    state.executeUpdate(uquery);
    dbu.editFile(uquery, dbu.getCurDateBackUpFilename());
    connect.close();
    state.close();
    updateFinalEndingBalance();
  }
  
  public double getTotalPettyCash() throws SQLException {
    Connection connect = DriverManager.getConnection(this.driver, this.finalUsername, this.finalPassword);
    Statement state = connect.createStatement();
    ResultSet rSet = state.executeQuery("Select pty_amount from cashtransactions.petty_cash where pty_date = '" + getCurrentDate() + "'");
    double total_petty_cash = 0.0D;
    while (rSet.next())
      total_petty_cash += rSet.getDouble(1); 
    connect.close();
    rSet.close();
    state.close();
    return total_petty_cash;
  }
  
  public double getTotalTransfer() throws SQLException {
    Connection connect = DriverManager.getConnection(this.driver, this.finalUsername, this.finalPassword);
    Statement state = connect.createStatement();
    ResultSet rSet = state.executeQuery("Select x_amount from cashtransactions.transfer where x_date = '" + getCurrentDate() + "'");
    double total_transfer = 0.0D;
    while (rSet.next())
      total_transfer += rSet.getDouble(1); 
    connect.close();
    rSet.close();
    state.close();
    return total_transfer;
  }
  
  public double getTotalNewLoans() throws SQLException {
    Connection connect = DriverManager.getConnection(this.driver, this.finalUsername, this.finalPassword);
    Statement state = connect.createStatement();
    ResultSet rSet = state.executeQuery("Select new_amount from cashtransactions.new_loans where new_date = '" + getCurrentDate() + "'");
    double total_new_loans = 0.0D;
    while (rSet.next())
      total_new_loans += rSet.getDouble(1); 
    connect.close();
    rSet.close();
    state.close();
    return total_new_loans;
  }
  
  public double getTotalRenewals() throws SQLException {
    Connection connect = DriverManager.getConnection(this.driver, this.finalUsername, this.finalPassword);
    Connection connect1 = DriverManager.getConnection(this.driver, this.finalUsername, this.finalPassword);
    Connection connect2 = DriverManager.getConnection(this.driver, this.finalUsername, this.finalPassword);
    Statement state = connect.createStatement();
    Statement state1 = connect1.createStatement();
    Statement state2 = connect2.createStatement();
    ResultSet rSet = state.executeQuery("Select rnw_amount from cashtransactions.renewals where rnw_date = '" + getCurrentDate() + "' and rnw_id like '%RNW%'");
    ResultSet rSet1 = state1.executeQuery("Select rnw_amount from cashtransactions.renewals where rnw_date = '" + getCurrentDate() + "' and rnw_id like '%RBC%'");
    ResultSet rSet2 = state2.executeQuery("Select rnw_amount from cashtransactions.renewals where rnw_date = '" + getCurrentDate() + "' and rnw_id like '%RSB%'");
    double total_renew_loans = 0.0D;
    while (rSet.next())
      total_renew_loans += rSet.getDouble(1); 
    while (rSet1.next())
      total_renew_loans += rSet1.getDouble(1); 
    while (rSet2.next())
      total_renew_loans += rSet2.getDouble(1); 
    connect.close();
    rSet.close();
    state.close();
    connect1.close();
    rSet1.close();
    state1.close();
    connect2.close();
    rSet2.close();
    state2.close();
    return total_renew_loans;
  }
  
  public double getTotalRenewalsWithNetInc() throws SQLException {
    Connection connect = DriverManager.getConnection(this.driver, this.finalUsername, this.finalPassword);
    Statement state = connect.createStatement();
    ResultSet rSet = state.executeQuery("Select rnw_amount from cashtransactions.renewals where rnw_date = '" + getCurrentDate() + "' and rnw_id like '%RDC%'");
    double total_renew_loans = 0.0D;
    while (rSet.next())
      total_renew_loans += rSet.getDouble(1); 
    connect.close();
    rSet.close();
    state.close();
    return total_renew_loans;
  }
  
  public double getTotalRedeem() throws SQLException {
    Connection connect = DriverManager.getConnection(this.driver, this.finalUsername, this.finalPassword);
    Statement state = connect.createStatement();
    ResultSet rSet = state.executeQuery("Select rdm_amount from cashtransactions.redeem where rdm_date = '" + getCurrentDate() + "'");
    double total_redeem_loans = 0.0D;
    while (rSet.next())
      total_redeem_loans += rSet.getDouble(1); 
    connect.close();
    rSet.close();
    state.close();
    return total_redeem_loans;
  }
  
  public double getTotalAdditionals() throws SQLException {
    Connection connect = DriverManager.getConnection(this.driver, this.finalUsername, this.finalPassword);
    Statement state = connect.createStatement();
    ResultSet rSet = state.executeQuery("Select add_amount from cashtransactions.additionals where add_date = '" + getCurrentDate() + "'");
    double total_additionals = 0.0D;
    while (rSet.next())
      total_additionals += rSet.getDouble(1); 
    connect.close();
    rSet.close();
    state.close();
    return total_additionals;
  }
  
  public double getBeginningBalance() throws SQLException {
    Connection connect = DriverManager.getConnection(this.driver, this.finalUsername, this.finalPassword);
    Statement state = connect.createStatement();
    ResultSet rSet = state.executeQuery("Select beginning_balance from cashtransactions.running_balance where date = '" + getCurrentDate() + "'");
    while (rSet.next())
      this.beginning = rSet.getDouble(1); 
    rSet.close();
    connect.close();
    return this.beginning;
  }
  
  public String getCurrentDate() {
    Calendar rightNow = Calendar.getInstance();
    DateHelper dateHelp = new DateHelper();
    Date currentDate = dateHelp.now(rightNow);
    String curDateText = dateHelp.formatDate(currentDate);
    return curDateText;
  }
  
  public double getNew_loan() {
    return this.new_loan;
  }
  
  public void setNew_loan(double new_loan) {
    this.new_loan = new_loan;
  }
  
  public double getRenewal() {
    return this.renewal;
  }
  
  public void setRenewal(double renewal) {
    this.renewal = renewal;
  }
  
  public double getRedemption() {
    return this.redemption;
  }
  
  public void setRedemption(double redemption) {
    this.redemption = redemption;
  }
  
  public double getPetty_cash() {
    return this.petty_cash;
  }
  
  public void setPetty_cash(double petty_cash) {
    this.petty_cash = petty_cash;
  }
  
  public double getAdditional() {
    return this.additional;
  }
  
  public void setAdditional(double additional) {
    this.additional = additional;
  }
  
  public double getBeginning() {
    return this.beginning;
  }
  
  public void setBeginning(double beginning) {
    this.beginning = beginning;
  }
  
  public double getEnding() {
    return this.ending;
  }
  
  public void setEnding(double ending) {
    this.ending = ending;
  }
  
  public double getCash_in() {
    return this.cash_in;
  }
  
  public void setCash_in(double cash_in) {
    this.cash_in = cash_in;
  }
  
  public double getCash_out() {
    return this.cash_out;
  }
  
  public void setCash_out(double cash_out) {
    this.cash_out = cash_out;
  }
  
  public double getRemaining() {
    return this.remaining;
  }
  
  public void setRemaining(double remaining) {
    this.remaining = remaining;
  }
}
