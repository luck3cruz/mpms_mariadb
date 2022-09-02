/*    */ package com.merlin;
/*    */ 
/*    */ import java.awt.EventQueue;
/*    */ import java.util.logging.Level;
/*    */ import java.util.logging.Logger;
/*    */ import javax.swing.UIManager;
/*    */ import javax.swing.UnsupportedLookAndFeelException;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class Main
/*    */ {
/*    */   public static void main(String[] args) {
/* 19 */     EventQueue.invokeLater(new Runnable() {
/*    */           public void run() {
/*    */             try {
/* 22 */               for (UIManager.LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
/* 23 */                 if ("Windows".equals(info.getName())) {
/* 24 */                   UIManager.setLookAndFeel(info.getClassName());
/*    */                   break;
/*    */                 } 
/*    */               } 
/* 28 */             } catch (ClassNotFoundException ex) {
/* 29 */               Logger.getLogger(MainFrame.class.getName()).log(Level.SEVERE, (String)null, ex);
/* 30 */             } catch (InstantiationException ex) {
/* 31 */               Logger.getLogger(MainFrame.class.getName()).log(Level.SEVERE, (String)null, ex);
/* 32 */             } catch (IllegalAccessException ex) {
/* 33 */               Logger.getLogger(MainFrame.class.getName()).log(Level.SEVERE, (String)null, ex);
/* 34 */             } catch (UnsupportedLookAndFeelException ex) {
/* 35 */               Logger.getLogger(MainFrame.class.getName()).log(Level.SEVERE, (String)null, ex);
/*    */             } 
/* 37 */             MainFrame mframe = new MainFrame();
/* 38 */             mframe.setLocationRelativeTo(null);
/* 39 */             mframe.setVisible(true);
/*    */           }
/*    */         });
/*    */   }
/*    */ }


/* Location:              E:\MERLIN PROGRAM BACKUP\ALL SYSTEM\MPIS\dist\MPIS_MariaDB.jar!\MPI\Main.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */