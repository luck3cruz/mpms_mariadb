/*    */ package mpis_mariadb_2;
/*    */ 
/*    */ import java.util.Properties;
/*    */ import java.util.logging.Level;
/*    */ import java.util.logging.Logger;
/*    */ import javax.mail.Address;
/*    */ import javax.mail.Authenticator;
/*    */ import javax.mail.BodyPart;
/*    */ import javax.mail.Message;
/*    */ import javax.mail.MessagingException;
/*    */ import javax.mail.Multipart;
/*    */ import javax.mail.Session;
/*    */ import javax.mail.Transport;
/*    */ import javax.mail.internet.InternetAddress;
/*    */ import javax.mail.internet.MimeBodyPart;
/*    */ import javax.mail.internet.MimeMessage;
/*    */ import javax.mail.internet.MimeMultipart;
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
/*    */ 
/*    */ 
/*    */ 
/*    */ public class EmailSender
/*    */ {
/*    */   public static boolean sendMail(String from, String password, String subject, String[] to, String message) {
/* 36 */     String host = "smtp.mail.yahoo.com";
/* 37 */     Properties props = System.getProperties();
/* 38 */     props.put("mail.transport.protocol", "smtp");
/* 39 */     props.put("mail.smtp.starttls.enable", "true");
/* 40 */     props.put("mail.smtp.host", host);
/* 41 */     props.put("mail.smtp.user", from);
/* 42 */     props.put("mail.smtp.password", password);
/* 43 */     props.put("mail.smtp.port", Integer.valueOf(587));
/* 44 */     props.put("mail.smtp.auth", "true");
/*    */     
/* 46 */     props.put("mail.smtp.ssl.trust", "smtp.mail.yahoo.com");
/* 47 */     Authenticator auth = new MailAuthenticator();
/* 48 */     Session session = Session.getDefaultInstance(props, auth);
/* 49 */     MimeMultipart mimeMultipart = new MimeMultipart();
/* 50 */     MimeBodyPart mimeBodyPart = new MimeBodyPart();
/* 51 */     MimeMessage mimeMessage = new MimeMessage(session);
/*    */     /*    */     
/*    */     try {
/* 54 */       mimeBodyPart.setText(message);
/* 55 */       mimeMultipart.addBodyPart((BodyPart)mimeBodyPart);
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
/* 68 */       mimeMessage.setContent((Multipart)mimeMultipart);
/* 69 */       mimeMessage.setFrom((Address)new InternetAddress(from));
/* 70 */       InternetAddress[] toAddress = new InternetAddress[to.length]; int i;
/* 71 */       for (i = 0; i < to.length; i++) {
/* 72 */         toAddress[i] = new InternetAddress(to[i]);
/*    */       }
/* 74 */       for (i = 0; i < toAddress.length; i++) {
/* 75 */         mimeMessage.addRecipient(Message.RecipientType.TO, (Address)toAddress[i]);
/*    */       }
/* 77 */       mimeMessage.setSubject(subject);
/*    */       
/* 79 */       Transport transport = session.getTransport("smtp");
/* 80 */       transport.connect(host, from, password);
/* 81 */       transport.sendMessage((Message)mimeMessage, mimeMessage.getAllRecipients());
/* 82 */       transport.close();
/* 83 */       return true;
/* 84 */     } catch (MessagingException ex) {
/* 85 */       ex.printStackTrace();
/* 86 */       Logger.getLogger(EmailSender.class.getName()).log(Level.SEVERE, (String)null, (Throwable)ex);
/*    */       
/* 88 */       return false;
/*    */     } 
/*    */   }
/*    */ }

