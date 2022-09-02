/*    */ package com.merlin;
/*    */ 
/*    */ import javax.swing.table.DefaultTableModel;
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
/*    */ public class HistoryTableModel
/*    */   extends DefaultTableModel
/*    */ {
/*    */   public boolean isCellEditable(int row, int col) {
/* 18 */     return false;
/*    */   }
/*    */ }

