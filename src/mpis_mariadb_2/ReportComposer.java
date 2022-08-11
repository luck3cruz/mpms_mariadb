/*     */ package mpis_mariadb_2;
/*     */ 
/*     */ import java.io.FileWriter;
/*     */ import java.io.IOException;
/*     */ import java.io.PrintWriter;
/*     */ import java.sql.Connection;
/*     */ import java.sql.DriverManager;
/*     */ import java.sql.ResultSet;
/*     */ import java.sql.SQLException;
/*     */ import java.sql.Statement;
/*     */ import java.util.logging.Level;
/*     */ import java.util.logging.Logger;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class ReportComposer
/*     */ {
/*  26 */   private String date = "";
/*  27 */   private String branch = "";
/*  28 */   private String begbal = "";
/*  29 */   private String empenoamt = "";
/*  30 */   private String ptcamt = "";
/*  31 */   private String finan = "";
/*  32 */   private String rescateamt = "";
/*  33 */   private String interest = "";
/*  34 */   private String ai = "";
/*  35 */   private String sc = "";
/*  36 */   private String ins = "";
/*  37 */   private String addlamt = "";
/*  38 */   private String payfin = "";
/*  39 */   private String endbal = "";
/*  40 */   private String expected = "";
/*  41 */   private String empeno = "";
/*  42 */   private String rescate = "";
/*  43 */   private String ptc = "";
/*  44 */   private String addl = "";
/*  45 */   private String wholemsg = "";
/*     */   
/*  47 */   Config con = new Config();
/*  48 */   private final String driver = "jdbc:mariadb://" + this.con.getProp("IP") + ":" + this.con.getProp("port") + "/cashtransactions";
/*  49 */   private final String f_user = this.con.getProp("username");
/*  50 */   private final String f_pass = this.con.getProp("password");
/*  51 */   private DecimalHelper decimalChanger = new DecimalHelper();
/*     */   
/*     */   public String generateReport(String date) throws SQLException {
/*  54 */     setBranch(this.con.getProp("branch"));
/*  55 */     setDate(date);
/*  56 */     String tempRep = getBranch() + getDate() + getBegbal() + getEmpenoamt() + getPtcamt() + getFinan() + getRescateamt() + getInterest() + getAi() + getSc() + getIns() + getAddlamt() + getFinan() + getExpected() + "\n" + getEndbal() + "\n" + retrieveBreakdown(date) + "\nEMPENO DETAILS:\n" + retrieveEmpenoDetails(date) + "\nRESCATE DETAILS:\n" + retrieveRescateDetails(date) + "\n" + retrievePtcDetails(date) + "\n" + retrieveAddDetails(date) + "\n\n" + retrieveXBreakdown(date);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*  71 */     System.out.println(tempRep);
/*  72 */     return tempRep;
/*     */   }
/*     */   /*     */   
/*     */   public void composeRep() {
/*     */     try {
/*  77 */       FileWriter fw = new FileWriter("report.txt");
/*  78 */       PrintWriter pw = new PrintWriter(fw);
/*  79 */       pw.println("");
/*     */     }
/*  81 */     catch (IOException ex) {
/*  82 */       Logger.getLogger(ReportComposer.class.getName()).log(Level.SEVERE, (String)null, ex);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public String retrieveEmpenoDetails(String date) throws SQLException {
/*  88 */     Connection connect = DriverManager.getConnection(this.driver, this.f_user, this.f_pass);
/*  89 */     Statement state = connect.createStatement();
/*  90 */     String query = "Select a.pap_num, item_description, principal from merlininventorydatabase.client_info a inner join merlininventorydatabase.empeno b on item_code_a = b.pap_num where transaction_date = '" + date + "' and status = 'Open'";
/*  91 */     System.out.println(query);
/*  92 */     ResultSet rset = state.executeQuery(query);
/*  93 */     String all_emp = "";
/*  94 */     while (rset.next()) {
/*  95 */       all_emp = all_emp + rset.getString(3).concat(" - ").concat("(" + rset.getString(1) + ") ").concat(rset.getString(2)).concat("\n");
/*     */     }
/*  97 */     state.close();
/*  98 */     connect.close();
/*  99 */     setEmpeno(all_emp);
/* 100 */     System.out.println(all_emp);
/* 101 */     return all_emp;
/*     */   }
/*     */   
/*     */   public String retrieveRescateDetails(String date) throws SQLException {
/* 105 */     Connection connect = DriverManager.getConnection(this.driver, this.f_user, this.f_pass);
/* 106 */     Statement state = connect.createStatement();
/* 107 */     String query = "Select pap_num, principal from merlininventorydatabase.rescate where date = '" + date + "'";
/* 108 */     ResultSet rset = state.executeQuery(query);
/* 109 */     String all_res = "";
/* 110 */     while (rset.next()) {
/* 111 */       all_res = all_res + rset.getString(2).concat(" - ").concat("(" + rset.getString(1) + ") ").concat("\n");
/*     */     }
/* 113 */     state.close();
/* 114 */     connect.close();
/* 115 */     setRescate(all_res);
/* 116 */     return all_res;
/*     */   }
/*     */   
/*     */   public String retrievePtcDetails(String date) throws SQLException {
/* 120 */     Connection connect = DriverManager.getConnection(this.driver, this.f_user, this.f_pass);
/* 121 */     Statement state = connect.createStatement();
/* 122 */     String query = "Select pty_amount, pty_remarks from cashtransactions.petty_cash where pty_date = '" + date + "'";
/* 123 */     ResultSet rset = state.executeQuery(query);
/* 124 */     String all_ptc = "PTC: ";
/* 125 */     while (rset.next()) {
/* 126 */       all_ptc = all_ptc + rset.getString(1).concat(" - ").concat(rset.getString(2)).concat("\n");
/*     */     }
/* 128 */     state.close();
/* 129 */     connect.close();
/* 130 */     setPtc(all_ptc);
/* 131 */     return all_ptc;
/*     */   }
/*     */   
/*     */   public String retrieveAddDetails(String date) throws SQLException {
/* 135 */     Connection connect = DriverManager.getConnection(this.driver, this.f_user, this.f_pass);
/* 136 */     Statement state = connect.createStatement();
/* 137 */     String query = "Select add_amount, add_remarks from cashtransactions.additionals where add_date = '" + date + "'";
/* 138 */     ResultSet rset = state.executeQuery(query);
/* 139 */     String all_add = "ADDITIONALS: ";
/* 140 */     while (rset.next()) {
/* 141 */       all_add = all_add + rset.getString(1).concat(" - ").concat(rset.getString(2)).concat("\n");
/*     */     }
/* 143 */     state.close();
/* 144 */     connect.close();
/* 145 */     setAddl(all_add);
/* 146 */     return all_add;
/*     */   }
/*     */   
/*     */   public String retrieveBreakdown(String date) throws SQLException {
/* 150 */     Connection connect = DriverManager.getConnection(this.driver, this.f_user, this.f_pass);
/* 151 */     Statement state = connect.createStatement();
/* 152 */     String query = "Select * from cashtransactions.breakdown where date(bd_date) = '" + date + "'";
/* 153 */     ResultSet rset = state.executeQuery(query);
/* 154 */     String bdown = "";
/* 155 */     while (rset.next()) {
/* 156 */       bdown = "------------ \n\nCASH DENOMINATION \nCOV = " + this.decimalChanger.FormatNumber(rset.getDouble("cov")) + "\n" + "1000 x " + rset.getInt("bd_thou") + " = " + this.decimalChanger.FormatNumber(rset.getDouble("bd_thou") * 1000.0D) + "\n" + "500 x " + rset.getInt("bd_fhun") + " = " + this.decimalChanger.FormatNumber(rset.getDouble("bd_fhun") * 500.0D) + "\n" + "200 x " + rset.getInt("bd_thun") + " = " + this.decimalChanger.FormatNumber(rset.getDouble("bd_thun") * 200.0D) + "\n" + "100 x " + rset.getInt("bd_hund") + " = " + this.decimalChanger.FormatNumber(rset.getDouble("bd_hund") * 100.0D) + "\n" + "50 x " + rset.getInt("bd_fift") + " = " + this.decimalChanger.FormatNumber(rset.getDouble("bd_fift") * 50.0D) + "\n" + "20 x " + rset.getInt("bd_twen") + " = " + this.decimalChanger.FormatNumber(rset.getDouble("bd_twen") * 20.0D) + "\n" + "10 x " + rset.getInt("bd_ten") + " = " + this.decimalChanger.FormatNumber(rset.getDouble("bd_ten") * 10.0D) + "\n" + "5 x " + rset.getInt("bd_five") + " = " + this.decimalChanger.FormatNumber(rset.getDouble("bd_five") * 5.0D) + "\n" + "1 x " + rset.getInt("bd_one") + " = " + this.decimalChanger.FormatNumber(rset.getDouble("bd_one") * 1.0D) + "\n" + "0.25 x " + rset.getInt("bd_cen") + " = " + this.decimalChanger.FormatNumber(rset.getDouble("bd_cen") * 0.25D) + "\n" + "0.10 x " + rset.getInt("bd_tenc") + " = " + this.decimalChanger.FormatNumber(rset.getDouble("bd_tenc") * 0.1D) + "\n" + "0.05 x " + rset.getInt("bd_fivc") + " = " + this.decimalChanger.FormatNumber(rset.getDouble("bd_fivc") * 0.05D) + "\n\n" + "TOTAL = " + this.decimalChanger.FormatNumber(rset.getDouble("bd_total")) + "\n\n ------------";
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 173 */     state.close();
/* 174 */     connect.close();
/* 175 */     return bdown;
/*     */   }
/*     */   
/*     */   public String retrieveXBreakdown(String date) throws SQLException {
/* 179 */     Connection connect = DriverManager.getConnection(this.driver, this.f_user, this.f_pass);
/* 180 */     Statement state = connect.createStatement();
/* 181 */     Connection connect1 = DriverManager.getConnection(this.driver, this.f_user, this.f_pass);
/* 182 */     Statement state1 = connect1.createStatement();
/* 183 */     String query = "Select count(*) from cashtransactions.x_breakdown where date(bd_date) = '" + date + "'";
/* 184 */     String query1 = "Select * from cashtransactions.x_breakdown where date(bd_date) = '" + date + "'";
/* 185 */     ResultSet rset = state.executeQuery(query);
/* 186 */     ResultSet rset1 = state1.executeQuery(query1);
/* 187 */     int count = 0;
/* 188 */     System.out.println(query);
/* 189 */     while (rset.next()) {
/* 190 */       count = rset.getInt(1);
/* 191 */       System.out.println(count);
/*     */     } 
/* 193 */     String bdown = "";
/* 194 */     bdown = "------------ \n\nEXPECTED (CASH DENOMINATION) \n";
/*     */ 
/*     */ 
/*     */     
/* 198 */     System.out.println(query1);
/* 199 */     while (rset1.next()) {
/* 200 */       bdown = bdown + "1000 x " + rset1.getInt("bd_thou") + " = " + this.decimalChanger.FormatNumber(rset1.getDouble("bd_thou") * 1000.0D) + "\n" + "500 x " + rset1.getInt("bd_fhun") + " = " + this.decimalChanger.FormatNumber(rset1.getDouble("bd_fhun") * 500.0D) + "\n" + "200 x " + rset1.getInt("bd_thun") + " = " + this.decimalChanger.FormatNumber(rset1.getDouble("bd_thun") * 200.0D) + "\n" + "100 x " + rset1.getInt("bd_hund") + " = " + this.decimalChanger.FormatNumber(rset1.getDouble("bd_hund") * 100.0D) + "\n" + "50 x " + rset1.getInt("bd_fift") + " = " + this.decimalChanger.FormatNumber(rset1.getDouble("bd_fift") * 50.0D) + "\n" + "20 x " + rset1.getInt("bd_twen") + " = " + this.decimalChanger.FormatNumber(rset1.getDouble("bd_twen") * 20.0D) + "\n" + "10 x " + rset1.getInt("bd_ten") + " = " + this.decimalChanger.FormatNumber(rset1.getDouble("bd_ten") * 10.0D) + "\n" + "5 x " + rset1.getInt("bd_five") + " = " + this.decimalChanger.FormatNumber(rset1.getDouble("bd_five") * 5.0D) + "\n" + "1 x " + rset1.getInt("bd_one") + " = " + this.decimalChanger.FormatNumber(rset1.getDouble("bd_one") * 1.0D) + "\n" + "0.25 x " + rset1.getInt("bd_cen") + " = " + this.decimalChanger.FormatNumber(rset1.getDouble("bd_cen") * 0.25D) + "\n" + "0.10 x " + rset1.getInt("bd_tenc") + " = " + this.decimalChanger.FormatNumber(rset1.getDouble("bd_tenc") * 0.1D) + "\n" + "0.05 x " + rset1.getInt("bd_fivc") + " = " + this.decimalChanger.FormatNumber(rset1.getDouble("bd_fivc") * 0.05D) + "\n" + "TOTAL = " + this.decimalChanger.FormatNumber(rset1.getDouble("bd_total")) + "\n\n";
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 217 */     bdown = bdown + "\n ------------ \nREPORTED BY: " + this.con.getProp("last_username") + "\nEND OF REPORT";
/* 218 */     System.out.println(bdown);
/* 219 */     state.close();
/* 220 */     connect.close();
/* 221 */     state1.close();
/* 222 */     connect1.close();
/* 223 */     return bdown;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getDate() {
/* 234 */     return this.date;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setDate(String date) {
/* 241 */     this.date = date + "\n\n";
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getBranch() {
/* 248 */     return this.branch;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setBranch(String branch) {
/* 255 */     this.branch = "DAILY CASH REPORT - " + branch.toUpperCase() + "\n";
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getBegbal() {
/* 262 */     return this.begbal;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setBegbal(String begbal) {
/* 269 */     this.begbal = "BEGINNING BALANCE: " + begbal + "\n";
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getEmpenoamt() {
/* 276 */     return this.empenoamt;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setEmpenoamt(String empenoamt) {
/* 283 */     this.empenoamt = "EMPENO: " + empenoamt + "\n";
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getPtcamt() {
/* 290 */     return this.ptcamt;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setPtcamt(String ptcamt) {
/* 297 */     this.ptcamt = "PTC: " + ptcamt + "\n";
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getFinan() {
/* 304 */     return this.finan;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setFinan(String finan) {
/* 311 */     this.finan = "FINANCING" + finan + "\n";
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getRescateamt() {
/* 318 */     return this.rescateamt;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setRescateamt(String rescateamt) {
/* 325 */     this.rescateamt = "RESCATE: " + rescateamt + "\n";
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getInterest() {
/* 332 */     return this.interest;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setInterest(String interest) {
/* 339 */     this.interest = "INTEREST: " + interest + "\n";
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getAi() {
/* 346 */     return this.ai;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setAi(String ai) {
/* 353 */     this.ai = "AI: " + ai + "\n";
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getSc() {
/* 360 */     return this.sc;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setSc(String sc) {
/* 367 */     this.sc = "SC: " + sc + "\n";
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getIns() {
/* 374 */     return this.ins;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setIns(String ins) {
/* 381 */     this.ins = "INSURANCE: " + ins + "\n";
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getAddlamt() {
/* 388 */     return this.addlamt;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setAddlamt(String addlamt) {
/* 395 */     this.addlamt = "ADDITIONAL: " + addlamt + "\n";
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getPayfin() {
/* 402 */     return this.payfin;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setPayfin(String payfin) {
/* 409 */     this.payfin = "PAYMENT FINANCING: " + payfin + "\n";
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getEndbal() {
/* 416 */     return this.endbal;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setEndbal(String endbal) {
/* 423 */     this.endbal = "\nENDING BALANCE: " + endbal + "\n";
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getExpected() {
/* 430 */     return this.expected;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setExpected(String expected) {
/* 437 */     this.expected = "EXPECTED: " + expected + "\n";
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getEmpeno() {
/* 444 */     return this.empeno;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setEmpeno(String empeno) {
/* 451 */     this.empeno = empeno;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getRescate() {
/* 458 */     return this.rescate;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setRescate(String rescate) {
/* 465 */     this.rescate = rescate;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getPtc() {
/* 472 */     return this.ptc;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setPtc(String ptc) {
/* 479 */     this.ptc = ptc;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getAddl() {
/* 486 */     return this.addl;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setAddl(String addl) {
/* 493 */     this.addl = addl;
/*     */   }
/*     */   /*     */   
/*     */   public static void main(String[] args) {
/* 497 */     ReportComposer rp = new ReportComposer();
/*     */     try {
/* 499 */       rp.retrieveXBreakdown("2016-02-06");
/* 500 */       rp.retrieveEmpenoDetails("2016-02-06");
/* 501 */     } catch (SQLException ex) {
/* 502 */       Logger.getLogger(ReportComposer.class.getName()).log(Level.SEVERE, (String)null, ex);
/*     */     } 
/*     */   }
/*     */ }


/* Location:              E:\MERLIN PROGRAM BACKUP\ALL SYSTEM\MPIS\dist\MPIS_MariaDB.jar!\MPI\ReportComposer.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */