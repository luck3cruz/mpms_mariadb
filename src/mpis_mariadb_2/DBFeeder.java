/*     */ package mpis_mariadb_2;
/*     */ 
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
/*     */ public class DBFeeder
/*     */ {
/*  21 */   Config con = new Config();
/*  22 */   private final String driver = "jdbc:mariadb://" + this.con.getProp("IP") + ":" + this.con.getProp("port") + "/merlininventorydatabase";
/*  23 */   private final String user = this.con.getProp("username");
/*  24 */   private final String pass = this.con.getProp("password");
/*     */   
/*  26 */   private String code = "";
/*  27 */   private String item_desc = "";
/*  28 */   private String classification = "";
/*  29 */   private String trans_date = "";
/*  30 */   private String status = "";
/*  31 */   private String remarks = "";
/*  32 */   private String old_num = "";
/*  33 */   private String new_num = "";
/*  34 */   private int n = 0;
/*     */   
/*     */   public void addcolumns() throws SQLException {
/*  37 */     Connection connect = DriverManager.getConnection(this.driver, this.user, this.pass);
/*  38 */     Statement state = connect.createStatement();
/*  39 */     String query = "ALTER TABLE `merlininventorydatabase`.`client_info` ADD COLUMN `item_description` VARCHAR(175) NULL, ADD COLUMN `classification` VARCHAR(45) NULL, ADD COLUMN `transaction_date` DATE NULL, ADD COLUMN `status` VARCHAR(25) NULL, ADD COLUMN `remarks` VARCHAR(175) NULL, ADD COLUMN `old_pap_num` VARCHAR(7) NULL, ADD COLUMN `new_pap_num` VARCHAR(7) NULL;";
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*  47 */     state.executeUpdate(query);
/*  48 */     state.close();
/*  49 */     connect.close();
/*     */   }
/*     */ 
/*     */   
/*     */   public void feed() throws SQLException {
/*  54 */     int x = 0;
/*  55 */     Connection connect = DriverManager.getConnection(this.driver, this.user, this.pass);
/*  56 */     Statement state = connect.createStatement();
/*  57 */     String query = "Select item_code_a from pawn_info inner join client_info a on item_code_a = item_code_c where a.item_description is null";
/*  58 */     ResultSet rset = state.executeQuery(query);
/*  59 */     while (rset.next()) {
/*  60 */       Connection connect2 = DriverManager.getConnection(this.driver, this.user, this.pass);
/*  61 */       Statement state2 = connect2.createStatement();
/*  62 */       String update = "Update client_info set item_description = '" + getItem_desc(rset.getString(1)).replace("'", "\\'") + "', " + "classification = '" + getClassification(rset.getString(1)) + "', " + "transaction_date = '" + getTrans_date(rset.getString(1)) + "', " + "status = '" + getStatus(rset.getString(1)) + "', " + "remarks = '" + getRemarks(rset.getString(1)).replace("'", "\\'") + "', " + "old_pap_num = '" + getOld_num(rset.getString(1)) + "', " + "new_pap_num = '" + getNew_num(rset.getString(1)) + "' " + "where item_code_a = '" + rset.getString(1) + "'";
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */       
/*  71 */       x++;
/*  72 */       System.out.println(x);
/*  73 */       state2.executeUpdate(update);
/*     */       
/*  75 */       state2.close();
/*  76 */       connect2.close();
/*     */     } 
/*  78 */     setN(x);
/*  79 */     state.close();
/*  80 */     connect.close();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getCode() {
/*  88 */     return this.code;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setCode(String code) {
/*  95 */     this.code = code;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   /*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getItem_desc(String acctno) {
/*     */     try {
/* 103 */       Connection connect = DriverManager.getConnection(this.driver, this.user, this.pass);
/* 104 */       Statement state = connect.createStatement();
/* 105 */       String query = "Select item_description from pawn_info where item_code_c = '" + acctno + "'";
/*     */       
/* 107 */       ResultSet rset = state.executeQuery(query);
/* 108 */       while (rset.next()) {
/* 109 */         setItem_desc(rset.getString(1));
/*     */       }
/* 111 */       state.close();
/* 112 */       connect.close();
/* 113 */       return this.item_desc;
/* 114 */     } catch (SQLException ex) {
/* 115 */       this.con.saveProp("mpis_last_error", String.valueOf(ex));
/* 116 */       Logger.getLogger(DBFeeder.class.getName()).log(Level.SEVERE, (String)null, ex);
/* 117 */       return "*";
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setItem_desc(String item_desc) {
/* 125 */     this.item_desc = item_desc;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   /*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getClassification(String acctno) {
/*     */     try {
/* 133 */       Connection connect = DriverManager.getConnection(this.driver, this.user, this.pass);
/* 134 */       Statement state = connect.createStatement();
/* 135 */       String query = "Select classification from pawn_info where item_code_c = '" + acctno + "'";
/*     */       
/* 137 */       ResultSet rset = state.executeQuery(query);
/* 138 */       while (rset.next()) {
/* 139 */         setClassification(rset.getString(1));
/*     */       }
/* 141 */       state.close();
/* 142 */       connect.close();
/* 143 */       return this.classification;
/* 144 */     } catch (SQLException ex) {
/* 145 */       this.con.saveProp("mpis_last_error", String.valueOf(ex));
/* 146 */       Logger.getLogger(DBFeeder.class.getName()).log(Level.SEVERE, (String)null, ex);
/* 147 */       return "*";
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setClassification(String classification) {
/* 155 */     this.classification = classification;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   /*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getTrans_date(String acctno) {
/*     */     try {
/* 163 */       Connection connect = DriverManager.getConnection(this.driver, this.user, this.pass);
/* 164 */       Statement state = connect.createStatement();
/* 165 */       String query = "Select transaction_date from pawn_info where item_code_c = '" + acctno + "'";
/*     */       
/* 167 */       ResultSet rset = state.executeQuery(query);
/* 168 */       while (rset.next()) {
/* 169 */         setTrans_date(rset.getString(1));
/*     */       }
/* 171 */       state.close();
/* 172 */       connect.close();
/* 173 */       return this.trans_date;
/* 174 */     } catch (SQLException ex) {
/* 175 */       this.con.saveProp("mpis_last_error", String.valueOf(ex));
/* 176 */       Logger.getLogger(DBFeeder.class.getName()).log(Level.SEVERE, (String)null, ex);
/* 177 */       return "*";
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setTrans_date(String trans_date) {
/* 185 */     this.trans_date = trans_date;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   /*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getStatus(String acctno) {
/*     */     try {
/* 193 */       Connection connect = DriverManager.getConnection(this.driver, this.user, this.pass);
/* 194 */       Statement state = connect.createStatement();
/* 195 */       String query = "Select status from pawn_info where item_code_c = '" + acctno + "'";
/*     */       
/* 197 */       ResultSet rset = state.executeQuery(query);
/* 198 */       while (rset.next()) {
/* 199 */         setStatus(rset.getString(1));
/*     */       }
/* 201 */       state.close();
/* 202 */       connect.close();
/* 203 */       return this.status;
/* 204 */     } catch (SQLException ex) {
/* 205 */       this.con.saveProp("mpis_last_error", String.valueOf(ex));
/* 206 */       Logger.getLogger(DBFeeder.class.getName()).log(Level.SEVERE, (String)null, ex);
/* 207 */       return "*";
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setStatus(String status) {
/* 215 */     this.status = status;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   /*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getRemarks(String acctno) {
/*     */     try {
/* 223 */       Connection connect = DriverManager.getConnection(this.driver, this.user, this.pass);
/* 224 */       Statement state = connect.createStatement();
/* 225 */       String query = "Select remarks from pawn_info where item_code_c = '" + acctno + "'";
/*     */       
/* 227 */       ResultSet rset = state.executeQuery(query);
/* 228 */       while (rset.next()) {
/* 229 */         setRemarks(rset.getString(1));
/*     */       }
/* 231 */       state.close();
/* 232 */       connect.close();
/* 233 */       return this.remarks;
/* 234 */     } catch (SQLException ex) {
/* 235 */       this.con.saveProp("mpis_last_error", String.valueOf(ex));
/* 236 */       Logger.getLogger(DBFeeder.class.getName()).log(Level.SEVERE, (String)null, ex);
/* 237 */       return "*";
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setRemarks(String remarks) {
/* 245 */     this.remarks = remarks;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   /*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getOld_num(String acctno) {
/*     */     try {
/* 253 */       Connection connect = DriverManager.getConnection(this.driver, this.user, this.pass);
/* 254 */       Statement state = connect.createStatement();
/* 255 */       String query = "Select old_pap_num from pawn_info where item_code_c = '" + acctno + "'";
/*     */       
/* 257 */       ResultSet rset = state.executeQuery(query);
/* 258 */       while (rset.next()) {
/* 259 */         setOld_num(rset.getString(1));
/*     */       }
/* 261 */       state.close();
/* 262 */       connect.close();
/* 263 */       return this.old_num;
/* 264 */     } catch (SQLException ex) {
/* 265 */       this.con.saveProp("mpis_last_error", String.valueOf(ex));
/* 266 */       Logger.getLogger(DBFeeder.class.getName()).log(Level.SEVERE, (String)null, ex);
/* 267 */       return "*";
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setOld_num(String old_num) {
/* 275 */     this.old_num = old_num;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   /*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getNew_num(String acctno) {
/*     */     try {
/* 283 */       Connection connect = DriverManager.getConnection(this.driver, this.user, this.pass);
/* 284 */       Statement state = connect.createStatement();
/* 285 */       String query = "Select new_pap_num from pawn_info where item_code_c = '" + acctno + "'";
/*     */       
/* 287 */       ResultSet rset = state.executeQuery(query);
/* 288 */       while (rset.next()) {
/* 289 */         setNew_num(rset.getString(1));
/*     */       }
/* 291 */       state.close();
/* 292 */       connect.close();
/* 293 */       return this.new_num;
/* 294 */     } catch (SQLException ex) {
/* 295 */       this.con.saveProp("mpis_last_error", String.valueOf(ex));
/* 296 */       Logger.getLogger(DBFeeder.class.getName()).log(Level.SEVERE, (String)null, ex);
/* 297 */       return "*";
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setNew_num(String new_num) {
/* 305 */     this.new_num = new_num;
/*     */   }
/*     */   /*     */   
/*     */   public static void main(String[] args) {
/* 309 */     DBFeeder dbf = new DBFeeder();
/*     */     try {
/* 311 */       dbf.feed();
/* 312 */       System.out.println("X = " + dbf.getN());
/* 313 */       System.out.println("ALL DONE!");
/* 314 */     } catch (SQLException ex) {
/* 315 */       (new Config()).saveProp("mpis_last_error", String.valueOf(ex));
/* 316 */       System.out.println("X = " + dbf.getN());
/* 317 */       Logger.getLogger(DBFeeder.class.getName()).log(Level.SEVERE, (String)null, ex);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int getN() {
/* 325 */     return this.n;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setN(int n) {
/* 332 */     this.n = n;
/*     */   }
/*     */ }

