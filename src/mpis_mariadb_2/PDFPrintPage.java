/*     */ package mpis_mariadb_2;
/*     */ 
/*     */ import com.sun.pdfview.PDFFile;
/*     */ import com.sun.pdfview.PDFPage;
/*     */ import com.sun.pdfview.PDFRenderer;
/*     */ import java.awt.Graphics;
/*     */ import java.awt.Graphics2D;
/*     */ import java.awt.Rectangle;
/*     */ import java.awt.print.PageFormat;
/*     */ import java.awt.print.Printable;
/*     */ import java.awt.print.PrinterException;
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
/*     */ class PDFPrintPage
/*     */   implements Printable
/*     */ {
/*     */   private PDFFile file;
/*     */   /*     */   
/*     */   PDFPrintPage(PDFFile file) {
/* 113 */     this.file = file;
/*     */   }
/*     */   
/*     */   public int print(Graphics g, PageFormat format, int index) throws PrinterException {
/* 117 */     int pagenum = index + 1;
/* 118 */     if (pagenum >= 1 && pagenum <= this.file.getNumPages()) {
/* 119 */       Graphics2D g2 = (Graphics2D)g;
/* 120 */       PDFPage page = this.file.getPage(pagenum);
/*     */ 
/*     */       
/* 123 */       Rectangle imageArea = new Rectangle((int)format.getImageableX(), (int)format.getImageableY(), (int)format.getImageableWidth(), (int)format.getImageableHeight());
/*     */       
/* 125 */       g2.translate(0, 0);
/* 126 */       PDFRenderer pgs = new PDFRenderer(page, g2, imageArea, null, null);
/*     */       try {
/* 128 */         page.waitForFinish();
/* 129 */         pgs.run();
/* 130 */       } catch (InterruptedException ie) {}
/*     */ 
/*     */       
/* 133 */       return 0;
/*     */     } 
/* 135 */     return 1;
/*     */   }
/*     */ }

