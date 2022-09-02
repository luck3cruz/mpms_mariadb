/*    */ package com.merlin;
/*    */ 
/*    */ import java.text.DecimalFormat;
/*    */ import java.text.NumberFormat;
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
/*    */ public class DecimalHelper
/*    */ {
/* 18 */   NumberFormat formatter = new DecimalFormat("#,##0.00");
/* 19 */   NumberFormat percent = new DecimalFormat("##0.0");
/* 20 */   NumberFormat papeleta = new DecimalFormat("#0000");
/*    */   private double givenNumber;
/*    */   private String givenString;
/*    */   private int givenPapeleta;
/*    */   
/*    */   public String FormatNumber(double givenNumber) {
/* 26 */     this.givenNumber = givenNumber;
/* 27 */     String formattedNo = this.formatter.format(givenNumber).toString();
/* 28 */     return formattedNo;
/*    */   }
/*    */   
/*    */   public String FormatPercent(double givenNumber) {
/* 32 */     this.givenNumber = givenNumber;
/* 33 */     return this.percent.format(givenNumber).toString();
/*    */   }
/*    */   
/*    */   public String FormatPapeleta(int givenPapeleta) {
/* 37 */     this.givenPapeleta = givenPapeleta;
/* 38 */     return this.papeleta.format(givenPapeleta);
/*    */   }
/*    */ }

