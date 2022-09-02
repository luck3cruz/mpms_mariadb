/*     */ package com.merlin;
/*     */ 
/*     */ import java.awt.EventQueue;
/*     */ import java.awt.Frame;
/*     */ import java.awt.event.ActionEvent;
/*     */ import java.awt.event.ActionListener;
/*     */ import java.awt.event.WindowAdapter;
/*     */ import java.awt.event.WindowEvent;
/*     */ import java.util.logging.Level;
/*     */ import java.util.logging.Logger;
/*     */ import javax.swing.GroupLayout;
/*     */ import javax.swing.JButton;
/*     */ import javax.swing.JDialog;
/*     */ import javax.swing.JFrame;
/*     */ import javax.swing.JLabel;
/*     */ import javax.swing.JPasswordField;
/*     */ import javax.swing.LayoutStyle;
/*     */ import javax.swing.UIManager;
/*     */ import javax.swing.UnsupportedLookAndFeelException;
/*     */ 
/*     */ public class ConfirmPassword extends JDialog {
/*     */   private String password;
/*     */   /*     */   
/*     */   public ConfirmPassword(Frame parent, boolean modal) {
/*  25 */     super(parent, modal);
/*     */ 
/*     */ 
/*     */     
/*  29 */     this.password = "";
/*  30 */     this.pwGiven = false;
/*     */     initComponents();
/*     */   }
/*     */   
/*     */   private boolean pwGiven;
/*     */   private JButton jButton1;
/*     */   private JButton jButton2;
/*     */   private JLabel jLabel1;
/*     */   private JPasswordField jPasswordField1;
/*     */   /*     */   
/*     */   private void initComponents() {
/*  41 */     this.jLabel1 = new JLabel();
/*  42 */     this.jPasswordField1 = new JPasswordField();
/*  43 */     this.jButton1 = new JButton();
/*  44 */     this.jButton2 = new JButton();
/*     */     
/*  46 */     setDefaultCloseOperation(2);
/*     */     
/*  48 */     this.jLabel1.setText("Please Enter Old Password:");
/*     */     
/*  50 */     this.jPasswordField1.setText("jPasswordField1");
/*     */     
/*  52 */     this.jButton1.setText("OK");
/*  53 */     this.jButton1.addActionListener(new ActionListener() {
/*     */           public void actionPerformed(ActionEvent evt) {
/*  55 */             ConfirmPassword.this.jButton1ActionPerformed(evt);
/*     */           }
/*     */         });
/*     */     
/*  59 */     this.jButton2.setText("Cancel");
/*  60 */     this.jButton2.addActionListener(new ActionListener() {
/*     */           public void actionPerformed(ActionEvent evt) {
/*  62 */             ConfirmPassword.this.jButton2ActionPerformed(evt);
/*     */           }
/*     */         });
/*     */     
/*  66 */     GroupLayout layout = new GroupLayout(getContentPane());
/*  67 */     getContentPane().setLayout(layout);
/*  68 */     layout.setHorizontalGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING).addGroup(layout.createSequentialGroup().addContainerGap().addGroup(layout.createParallelGroup(GroupLayout.Alignment.TRAILING).addGroup(layout.createSequentialGroup().addComponent(this.jButton1).addPreferredGap(LayoutStyle.ComponentPlacement.RELATED).addComponent(this.jButton2)).addGroup(layout.createSequentialGroup().addComponent(this.jLabel1).addPreferredGap(LayoutStyle.ComponentPlacement.UNRELATED).addComponent(this.jPasswordField1, -2, -1, -2))).addContainerGap(-1, 32767)));
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
/*  83 */     layout.setVerticalGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING).addGroup(layout.createSequentialGroup().addContainerGap().addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE).addComponent(this.jLabel1).addComponent(this.jPasswordField1, -2, -1, -2)).addPreferredGap(LayoutStyle.ComponentPlacement.UNRELATED).addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE).addComponent(this.jButton2).addComponent(this.jButton1)).addContainerGap(-1, 32767)));
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
/*  97 */     pack();
/*     */   }
/*     */   
/*     */   private void jButton1ActionPerformed(ActionEvent evt) {
/* 101 */     setPassword(this.jPasswordField1.getText());
/* 102 */     setPwGiven(true);
/* 103 */     setVisible(false);
/*     */   }
/*     */   
/*     */   private void jButton2ActionPerformed(ActionEvent evt) {
/* 107 */     setPwGiven(false);
/* 108 */     setVisible(false);
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
/*     */   public static void main(String[] args) {
/*     */     try {
/* 121 */       for (UIManager.LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
/* 122 */         if ("Nimbus".equals(info.getName())) {
/* 123 */           UIManager.setLookAndFeel(info.getClassName());
/*     */           break;
/*     */         } 
/*     */       } 
/* 127 */     } catch (ClassNotFoundException ex) {
/* 128 */       Logger.getLogger(ConfirmPassword.class.getName()).log(Level.SEVERE, (String)null, ex);
/* 129 */     } catch (InstantiationException ex) {
/* 130 */       Logger.getLogger(ConfirmPassword.class.getName()).log(Level.SEVERE, (String)null, ex);
/* 131 */     } catch (IllegalAccessException ex) {
/* 132 */       Logger.getLogger(ConfirmPassword.class.getName()).log(Level.SEVERE, (String)null, ex);
/* 133 */     } catch (UnsupportedLookAndFeelException ex) {
/* 134 */       Logger.getLogger(ConfirmPassword.class.getName()).log(Level.SEVERE, (String)null, ex);
/*     */     } 
/*     */ 
/*     */ 
/*     */     /*     */ 
/*     */ 
/*     */     
/* 139 */     EventQueue.invokeLater(new Runnable()
/*     */         {
/*     */           public void run() {
/* 142 */             ConfirmPassword dialog = new ConfirmPassword(new JFrame(), true);
/* 143 */             dialog.addWindowListener(new WindowAdapter()
/*     */                 {
/*     */                   public void windowClosing(WindowEvent e)
/*     */                   {
/* 147 */                     System.exit(0);
/*     */                   }
/*     */                 });
/* 150 */             dialog.setVisible(true);
/*     */           }
/*     */         });
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
/*     */   
/*     */   public String getPassword() {
/* 165 */     return this.password;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setPassword(String password) {
/* 172 */     this.password = password;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isPwGiven() {
/* 179 */     return this.pwGiven;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setPwGiven(boolean pwGiven) {
/* 186 */     this.pwGiven = pwGiven;
/*     */   }
/*     */ }

