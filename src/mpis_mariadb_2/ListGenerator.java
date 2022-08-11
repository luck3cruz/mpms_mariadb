/*    */ package mpis_mariadb_2;
/*    */ 
/*    */ import java.sql.Connection;
/*    */ import java.sql.DriverManager;
/*    */ import java.sql.ResultSet;
/*    */ import java.sql.ResultSetMetaData;
/*    */ import java.sql.SQLException;
/*    */ import java.sql.Statement;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class ListGenerator
/*    */ {
/* 19 */   Config con = new Config();
/* 20 */   private final String driver = "jdbc:mariadb://" + this.con.getProp("IP") + ":" + this.con.getProp("port") + "/merlininventorydatabase";
/* 21 */   private final String username = this.con.getProp("username");
/* 22 */   private final String password = this.con.getProp("password");
/*    */   
/* 24 */   private String[][] dBNames = new String[][] { { "Item Code", "item_code_a" }, { "Item Code", "item_code_b" }, { "Item Code", "item_code_c" }, { "Pap.No.", "pap_num" }, { "Client Name", "client_name" }, { "Branch", "branch" }, { "Client Address", "client_address" }, { "Processed by", "processed_by" }, { "Contact No", "contact_no" }, { "Item Description", "item_description" }, { "Classification", "classification" }, { "Trans. Date", "transaction_date" }, { "Mat. Date", "maturity_date" }, { "Exp. Date", "expiry_date" }, { "Status", "status" }, { "Remarks", "remarks" }, { "Principal", "principal" }, { "Adv. Int.", "advance_interest" }, { "Adv. Int. Rate", "advance_interest_rate" }, { "Interest", "interest" }, { "Int. Rate", "interest_rate" }, { "Service Chg", "service_charge" }, { "Other Chgs.", "other_charges" }, { "Net Proceeds", "net_proceeds" } };
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public String queryBuilder(String[][] queryArray) {
/* 33 */     String query = "SELECT item_code_a AS 'Item Code', pap_num AS 'Papeleta No', client_name AS 'Client Name'";
/* 34 */     String substring = "";
/* 35 */     for (int i = 0; i < 19; i++) {
/* 36 */       if (queryArray[i][1].equals("YES")) {
/* 37 */         substring = substring.concat(", " + queryArray[i][0] + " AS '" + queryArray[i][2] + "'");
/*    */       }
/*    */     } 
/* 40 */     substring = substring.concat(" FROM merlininventorydatabase.client_info inner join merlininventorydatabase.loan_info on item_code_a = item_code_b");
/* 41 */     query = query.concat(substring);
/* 42 */     return query;
/*    */   }
/*    */   
/*    */   public String expiredLoanListBuilder(String[][] queryArray) {
/* 46 */     String query = "SELECT pap_num AS 'Papeleta No', client_name AS 'Client Name'";
/* 47 */     String substring = "";
/* 48 */     for (int i = 0; i < 19; i++) {
/* 49 */       if (queryArray[i][1].equals("YES")) {
/* 50 */         substring = substring.concat(", " + queryArray[i][0] + " AS '" + queryArray[i][2] + "'");
/*    */       }
/*    */     } 
/* 53 */     substring = substring.concat(" FROM merlininventorydatabase.client_info inner join merlininventorydatabase.loan_info on item_code_a = item_code_b");
/* 54 */     query = query.concat(substring);
/* 55 */     return query;
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public String vaultInvListBuilder(String[][] queryArray) {
/* 61 */     String query = "select pap_num AS 'Pap. No.', client_name AS 'Client Name'";
/* 62 */     String substring = "";
/* 63 */     for (int i = 0; i < 19; i++) {
/* 64 */       if (queryArray[i][1].equals("YES")) {
/* 65 */         substring = substring.concat(", " + queryArray[i][0] + " AS '" + queryArray[i][2] + "'");
/*    */       }
/*    */     } 
/* 68 */     substring = substring.concat(" FROM merlininventorydatabase.client_info inner join merlininventorydatabase.loan_info on item_code_a = item_code_b");
/* 69 */     query = query.concat(substring);
/* 70 */     return query;
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public String[] getColumnHeaders(String query) {
/* 76 */     String[] columnHeader = null;
/*    */     
/*    */     try {
/* 79 */       Connection connexion = DriverManager.getConnection(this.driver, this.username, this.password);
/* 80 */       Statement qry = connexion.createStatement();
/* 81 */       ResultSet queryResult = qry.executeQuery(query);
/* 82 */       ResultSetMetaData metaData = queryResult.getMetaData();
/* 83 */       int columnCount = metaData.getColumnCount();
/* 84 */       columnHeader = new String[columnCount];
/* 85 */       for (int i = 0; i < columnCount; i++) {
/* 86 */         columnHeader[i] = metaData.getColumnName(i + 1);
/* 87 */         for (int j = 0; j < this.dBNames.length; j++) {
/* 88 */           if (this.dBNames[j][1].equals(columnHeader[i])) {
/* 89 */             columnHeader[i] = this.dBNames[j][0];
/*    */           }
/*    */         } 
/*    */       } 
/* 93 */       connexion.close();
/* 94 */       qry.close();
/* 95 */     } catch (SQLException ex) {
/* 96 */       this.con.saveProp("mpis_last_error", String.valueOf(ex));
/*    */     } 
/*    */     
/* 99 */     return columnHeader;
/*    */   }
/*    */ }

