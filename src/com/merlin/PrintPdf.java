/*     */ package com.merlin;
/*     */ 
/*     */ import com.sun.pdfview.PDFFile;
/*     */ import java.awt.print.Book;
/*     */ import java.awt.print.PageFormat;
/*     */ import java.awt.print.Paper;
/*     */ import java.awt.print.PrinterException;
/*     */ import java.awt.print.PrinterJob;
/*     */ import java.io.FileInputStream;
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ import java.nio.ByteBuffer;
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
/*     */ 
/*     */ 
/*     */ public class PrintPdf
/*     */ {
/*  32 */   private PrinterJob pjob = null;
/*     */   /*     */   
/*     */   public static void main(String[] args) throws IOException, PrinterException {
/*  35 */     if (args.length != 1) {
/*  36 */       System.err.println("The first parameter must have the location of the PDF file to be printed");
/*     */     }
/*  38 */     System.out.println("Printing: " + args[0]);
/*     */     
/*  40 */     FileInputStream fis = new FileInputStream(args[0]);
/*  41 */     PrintPdf printPDFFile = new PrintPdf(fis, "Test Print PDF");
/*  42 */     printPDFFile.print();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   /*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public PrintPdf(InputStream inputStream, String jobName) throws IOException, PrinterException {
/*  54 */     byte[] pdfContent = new byte[inputStream.available()];
/*  55 */     inputStream.read(pdfContent, 0, inputStream.available());
/*  56 */     initialize(pdfContent, jobName);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   /*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public PrintPdf(byte[] content, String jobName) throws IOException, PrinterException {
/*  68 */     initialize(content, jobName);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void initialize(byte[] pdfContent, String jobName) throws IOException, PrinterException {
/*  80 */     ByteBuffer bb = ByteBuffer.wrap(pdfContent);
/*     */     
/*  82 */     PDFFile pdfFile = new PDFFile(bb);
/*  83 */     PDFPrintPage pages = new PDFPrintPage(pdfFile);
/*     */ 
/*     */     
/*  86 */     this.pjob = PrinterJob.getPrinterJob();
/*  87 */     PageFormat pf = PrinterJob.getPrinterJob().defaultPage();
/*  88 */     this.pjob.setJobName(jobName);
/*  89 */     Book book = new Book();
/*  90 */     book.append(pages, pf, pdfFile.getNumPages());
/*  91 */     this.pjob.setPageable(book);
/*     */ 
/*     */     
/*  94 */     Paper paper = new Paper();
/*  95 */     paper.setImageableArea(0.0D, 0.0D, paper.getWidth(), paper.getHeight());
/*  96 */     pf.setPaper(paper);
/*     */   }
/*     */ 
/*     */   
/*     */   public void print() throws PrinterException {
/* 101 */     this.pjob.print();
/*     */   }
/*     */ }

