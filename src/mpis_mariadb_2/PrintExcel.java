/*    */ package mpis_mariadb_2;
/*    */ 
/*    */ import java.io.File;
/*    */ import java.io.FileWriter;
/*    */ import javax.swing.JFileChooser;
/*    */ import javax.swing.filechooser.FileNameExtensionFilter;
/*    */ import org.apache.commons.io.IOUtils;
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
/*    */ public class PrintExcel
/*    */ {
/*    */   public static void main(String[] args) {
/* 22 */     (new PrintExcel()).init();
/*    */   }
/*    */   
/*    */   public void init() {
/* 26 */     JFileChooser chooser = new JFileChooser();
/*    */     
/* 28 */     chooser.setFileFilter(new FileNameExtensionFilter("Excel", new String[] { "xls" }));
/*    */     
/* 30 */     int retVal = chooser.showOpenDialog(null);
/* 31 */     if (retVal == 0) {
/* 32 */       printFile(chooser.getSelectedFile());
/*    */     }
/*    */   }
/*    */   
/*    */   public void printFile(File file) {
/*    */     try {
/* 38 */       String vbs = "Dim AppExcel\r\nSet AppExcel = CreateObject(\"Excel.application\")\r\nAppExcel.Workbooks.Open(\"" + file.getPath() + "\")\r\n" + "appExcel.ActiveWindow.SelectedSheets.PrintOut\r\n" + "Appexcel.Quit\r\n" + "Set appExcel = Nothing";
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */       
/* 44 */       File vbScript = File.createTempFile("vbScript", ".vbs");
/* 45 */       vbScript.deleteOnExit();
/* 46 */       FileWriter fw = new FileWriter(vbScript);
/* 47 */       fw.write(vbs);
/* 48 */       fw.flush();
/* 49 */       fw.close();
/* 50 */       IOUtils.closeQuietly(fw);
/* 51 */       System.gc();
/* 52 */       Process p = Runtime.getRuntime().exec("cscript //NoLogo " + vbScript.getPath());
/* 53 */     } catch (Exception e) {
/* 54 */       e.printStackTrace();
/*    */     } 
/*    */   }
/*    */ }

