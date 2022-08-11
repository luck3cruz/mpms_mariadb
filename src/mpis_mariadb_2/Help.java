/*    */ package mpis_mariadb_2;
/*    */ 
/*    */ import java.awt.Dimension;
/*    */ import java.awt.event.ActionEvent;
/*    */ import java.awt.event.ActionListener;
/*    */ import java.io.File;
/*    */ import java.io.IOException;
/*    */ import java.util.logging.Level;
/*    */ import java.util.logging.Logger;
/*    */ import javax.swing.GroupLayout;
/*    */ import javax.swing.JButton;
/*    */ import javax.swing.JPanel;
/*    */ import javax.swing.JScrollPane;
/*    */ import javax.swing.JTextPane;
/*    */ import javax.swing.LayoutStyle;
/*    */ 
/*    */ public class Help
/*    */   extends JPanel {
/*    */   private JButton jButton1;
/*    */   private JScrollPane jScrollPane1;
/*    */   private JTextPane jTextPane1;
/*    */   /*    */   
/*    */   public Help() {
/* 24 */     initComponents();
/* 25 */     show();
/*    */   }
/*    */   /*    */   
/*    */   public void show() {
/*    */     try {
/* 30 */       File file = new File("C:\\MPIS\\MPIS.htm");
/* 31 */       this.jTextPane1.setPage(file.toURI().toURL());
/* 32 */     } catch (IOException ex) {
/* 33 */       (new Config()).saveProp("mpis_last_error", String.valueOf(ex));
/* 34 */       Logger.getLogger(Help.class.getName()).log(Level.SEVERE, (String)null, ex);
/*    */     } 
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   /*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   private void initComponents() {
/* 47 */     this.jScrollPane1 = new JScrollPane();
/* 48 */     this.jTextPane1 = new JTextPane();
/* 49 */     this.jButton1 = new JButton();
/*    */     
/* 51 */     setMaximumSize(new Dimension(760, 628));
/* 52 */     setMinimumSize(new Dimension(760, 628));
/*    */     
/* 54 */     this.jScrollPane1.setViewportView(this.jTextPane1);
/*    */     
/* 56 */     this.jButton1.setText("Back to Main Menu");
/* 57 */     this.jButton1.addActionListener(new ActionListener() {
/*    */           public void actionPerformed(ActionEvent evt) {
/* 59 */             Help.this.jButton1ActionPerformed(evt);
/*    */           }
/*    */         });
/*    */     
/* 63 */     GroupLayout layout = new GroupLayout(this);
/* 64 */     setLayout(layout);
/* 65 */     layout.setHorizontalGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING).addComponent(this.jScrollPane1).addGroup(layout.createSequentialGroup().addGap(334, 334, 334).addComponent(this.jButton1).addContainerGap(305, 32767)));
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */     
/* 73 */     layout.setVerticalGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING).addGroup(layout.createSequentialGroup().addComponent(this.jScrollPane1, -2, 588, -2).addPreferredGap(LayoutStyle.ComponentPlacement.RELATED).addComponent(this.jButton1).addGap(0, 11, 32767)));
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   private void jButton1ActionPerformed(ActionEvent evt) {
/* 84 */     setVisible(false);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   /*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public static void main(String[] args) {
/* 96 */     Help hp = new Help();
/* 97 */     hp.show();
/* 98 */     hp.setVisible(true);
/*    */   }
/*    */ }

