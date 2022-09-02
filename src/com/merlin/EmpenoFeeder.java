/*     */ package com.merlin;
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
/*     */ public class EmpenoFeeder
/*     */ {
/*  20 */   Config con = new Config();
/*  21 */   private final String driver = "jdbc:mariadb://" + this.con.getProp("IP") + ":" + this.con.getProp("port") + "/merlininventorydatabase";
/*  22 */   private final String user = this.con.getProp("username");
/*  23 */   private final String pass = this.con.getProp("password");
/*     */   
/*  25 */   private String pap_num = "";
/*  26 */   private Double principal = Double.valueOf(0.0D);
/*  27 */   private Double interest = Double.valueOf(0.0D);
/*  28 */   private Double sc = Double.valueOf(0.0D);
/*  29 */   private Double insurance = Double.valueOf(0.0D);
/*     */ 
/*     */   
/*     */   public void feed() throws SQLException {
/*  33 */     int x = 0;
/*  34 */     Connection connect = DriverManager.getConnection(this.driver, this.user, this.pass);
/*  35 */     Statement state = connect.createStatement();
/*  36 */     String query = "Select item_code_b, transaction_date, principal, advance_interest, service_charge, insurance_fee from loan_info inner join pawn_info on item_code_b = item_code_c";
/*  37 */     ResultSet rset = state.executeQuery(query);
/*  38 */     while (rset.next()) {
/*  39 */       Connection connect2 = DriverManager.getConnection(this.driver, this.user, this.pass);
/*  40 */       Statement state2 = connect2.createStatement();
/*  41 */       String insert = "Replace into empeno (pap_num, date, principal, ai, sc, insurance) values ('" + rset.getString(1) + "', '" + rset.getString(2) + "', " + rset.getDouble(3) + ", " + rset.getDouble(4) + ", " + rset.getDouble(5) + ", " + rset.getDouble(6) + ")";
/*     */ 
/*     */ 
/*     */ 
/*     */       
/*  46 */       x++;
/*  47 */       System.out.println(x);
/*  48 */       state2.executeUpdate(insert);
/*     */       
/*  50 */       state2.close();
/*  51 */       connect2.close();
/*     */     } 
/*  53 */     state.close();
/*  54 */     connect.close();
/*     */   }
/*     */   
/*     */   public void rescatefeed(String query) throws SQLException {
/*  58 */     int x = 0;
/*  59 */     Connection connect = DriverManager.getConnection(this.driver, this.user, this.pass);
/*  60 */     Statement state = connect.createStatement();
/*  61 */     System.out.println(query);
/*  62 */     ResultSet rset = state.executeQuery(query);
/*  63 */     double intOth = 0.0D;
/*  64 */     while (rset.next()) {
/*  65 */       Connection connect2 = DriverManager.getConnection(this.driver, this.user, this.pass);
/*  66 */       Statement state2 = connect2.createStatement();
/*  67 */       intOth = rset.getDouble(4) + rset.getDouble(5);
/*  68 */       String insert = "Replace into rescate (pap_num, date, principal, interest) values ('" + rset.getString(1) + "', '" + rset.getString(2) + "', " + rset.getDouble(3) + ", " + intOth + ")";
/*     */ 
/*     */       
/*  71 */       System.out.println(insert);
/*  72 */       x++;
/*     */       
/*  74 */       state2.executeUpdate(insert);
/*     */       
/*  76 */       state2.close();
/*  77 */       connect2.close();
/*     */     } 
/*  79 */     state.close();
/*  80 */     connect.close();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Double getPrincipal(String acctno) {
/*     */     try {
/*  88 */       Connection connect = DriverManager.getConnection(this.driver, this.user, this.pass);
/*  89 */       Statement state = connect.createStatement();
/*  90 */       String query = "Select principal from loan_info where item_code_b = '" + acctno + "'";
/*     */       
/*  92 */       ResultSet rset = state.executeQuery(query);
/*  93 */       while (rset.next()) {
/*  94 */         setPrincipal(Double.valueOf(rset.getDouble(1)));
/*     */       }
/*  96 */       state.close();
/*  97 */       connect.close();
/*  98 */       return this.principal;
/*  99 */     } catch (SQLException ex) {
/* 100 */       this.con.saveProp("mpis_last_error", String.valueOf(ex));
/* 101 */       Logger.getLogger(DBFeeder.class.getName()).log(Level.SEVERE, (String)null, ex);
/* 102 */       return Double.valueOf(0.0D);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setPrincipal(Double principal) {
/* 110 */     this.principal = principal;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Double getInterest(String acctno) {
/*     */     try {
/* 118 */       Connection connect = DriverManager.getConnection(this.driver, this.user, this.pass);
/* 119 */       Statement state = connect.createStatement();
/* 120 */       String query = "Select advance_interest from loan_info where item_code_b = '" + acctno + "'";
/*     */       
/* 122 */       ResultSet rset = state.executeQuery(query);
/* 123 */       while (rset.next()) {
/* 124 */         setInterest(Double.valueOf(rset.getDouble(1)));
/*     */       }
/* 126 */       state.close();
/* 127 */       connect.close();
/* 128 */       return this.interest;
/* 129 */     } catch (SQLException ex) {
/* 130 */       this.con.saveProp("mpis_last_error", String.valueOf(ex));
/* 131 */       Logger.getLogger(DBFeeder.class.getName()).log(Level.SEVERE, (String)null, ex);
/* 132 */       return Double.valueOf(0.0D);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setInterest(Double adv) {
/* 140 */     this.interest = adv;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Double getSc(String acctno) {
/*     */     try {
/* 148 */       Connection connect = DriverManager.getConnection(this.driver, this.user, this.pass);
/* 149 */       Statement state = connect.createStatement();
/* 150 */       String query = "Select service_charge from loan_info where item_code_b = '" + acctno + "'";
/*     */       
/* 152 */       ResultSet rset = state.executeQuery(query);
/* 153 */       while (rset.next()) {
/* 154 */         setSc(Double.valueOf(rset.getDouble(1)));
/*     */       }
/* 156 */       state.close();
/* 157 */       connect.close();
/* 158 */       return this.sc;
/* 159 */     } catch (SQLException ex) {
/* 160 */       this.con.saveProp("mpis_last_error", String.valueOf(ex));
/* 161 */       Logger.getLogger(DBFeeder.class.getName()).log(Level.SEVERE, (String)null, ex);
/* 162 */       return Double.valueOf(0.0D);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setSc(Double sc) {
/* 170 */     this.sc = sc;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Double getInsurance(String acctno) {
/*     */     try {
/* 178 */       Connection connect = DriverManager.getConnection(this.driver, this.user, this.pass);
/* 179 */       Statement state = connect.createStatement();
/* 180 */       String query = "Select insurance_fee from loan_info where item_code_b = '" + acctno + "'";
/*     */       
/* 182 */       ResultSet rset = state.executeQuery(query);
/* 183 */       while (rset.next()) {
/* 184 */         setInsurance(Double.valueOf(rset.getDouble(1)));
/*     */       }
/* 186 */       state.close();
/* 187 */       connect.close();
/* 188 */       return this.insurance;
/* 189 */     } catch (SQLException ex) {
/* 190 */       this.con.saveProp("mpis_last_error", String.valueOf(ex));
/* 191 */       Logger.getLogger(DBFeeder.class.getName()).log(Level.SEVERE, (String)null, ex);
/* 192 */       return Double.valueOf(0.0D);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setInsurance(Double insurance) {
/* 200 */     this.insurance = insurance;
/*     */   }
/*     */ }

