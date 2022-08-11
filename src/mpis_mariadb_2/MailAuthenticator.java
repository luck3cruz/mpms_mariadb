/*    */ package mpis_mariadb_2;
/*    */ 
/*    */ import javax.mail.Authenticator;
/*    */ import javax.mail.PasswordAuthentication;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class MailAuthenticator
/*    */   extends Authenticator
/*    */ {
/* 20 */   Config con = new Config();
/*    */   
/*    */   public PasswordAuthentication getPasswordAuthentication() {
/* 23 */     return new PasswordAuthentication(this.con.getProp("email_add"), this.con.getProp("email_pass"));
/*    */   }
/*    */ }


/* Location:              E:\MERLIN PROGRAM BACKUP\ALL SYSTEM\MPIS\dist\MPIS_MariaDB.jar!\MPI\MailAuthenticator.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */