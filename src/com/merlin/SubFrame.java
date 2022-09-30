/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.merlin;

import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontFormatException;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

/**
 *
 * @author Lucky
 */
public class SubFrame extends javax.swing.JPanel {

    /**
     * Creates new form SubFrame
     */
    public SubFrame() {
        try {
            this.robotoCondensedBold = Font.createFont(Font.TRUETYPE_FONT, getClass().getResourceAsStream("/font/RobotoCondensed-Bold.ttf"));
            this.robotoBold = Font.createFont(Font.TRUETYPE_FONT, getClass().getResourceAsStream("/font/Roboto-Bold.ttf"));
        } catch (FontFormatException ex) {
            Logger.getLogger(SubFrame.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(SubFrame.class.getName()).log(Level.SEVERE, null, ex);
        }
        newLoanActivated = false;
        initComponents();
    }
    boolean a = true;
    final Color highlightHover = new Color(209, 229, 240);
    final Color menuBgColor = new Color(239, 246, 250);
    final Color clickedColor = new Color(119, 179, 212);
    final Color menuSelectedColor = new Color(119,178,211);
        
    private boolean newLoanActivated;
//    private CardLayout cl = (CardLayout)this.leftPanel.getLayout();
    Font robotoCondensedBold;
    Font robotoBold;

    
    
    
    public void changeColor(JPanel hover, Color rand) {
        hover.setBackground(rand);
    }
    
    public void clickMenu (JPanel h1, JPanel h2, int numberBool) {
        if (numberBool == 1) {
            h1.setBackground(new Color(136,161,186));
            h2.setBackground(new Color(116,141,166));
        } else {
            h1.setBackground(new Color(116,141,166));
            h2.setBackground(new Color(136,161,186));
        }
    }
    
    public void changeIconImage(JLabel button, String resourceImg) {
        ImageIcon aimg = new ImageIcon(getClass().getResource(resourceImg));
        button.setIcon(aimg);
    }
    
    public void hideShow (JPanel menuShowHide, boolean dashboard, JLabel button) {
        if (dashboard == true) {
            menuShowHide.setPreferredSize(new Dimension(50, menuShowHide.getHeight()));
            changeIconImage(button, "/images/forward.png");
            
        } else {
            menuShowHide.setPreferredSize(new Dimension(210, menuShowHide.getHeight()));
            changeIconImage(button, "/images/back.png");
        }
        
    }
    
    public void showCard(int cardNum) {
        CardLayout cl = (CardLayout)this.middlePanel.getLayout();
        switch (cardNum) {
          case 1: 
//              if (knowYourClient.isCreateLoan())
            this.newPledge.updateCombos();
            cl.show(this.middlePanel, "new_pledge_card");
            break;
          case 2:
            this.renewal.updateCombos();
            cl.show(this.middlePanel, "renewal_card");
            break;
          case 3:
            this.redemption.updateCombos();
            cl.show(this.middlePanel, "redemption_card");
            break;
          case 4:
//            this.search.updateCombos();
            this.search.requestFocusOnSearchField();
            cl.show(this.middlePanel, "search_card");
            break;
          case 5:
            this.expired.updateCombos();
            cl.show(this.middlePanel, "expired_listing_card");
            break;
          case 6:
            this.vaultInvPanel.updateCombos();
            cl.show(this.middlePanel, "vault_inventory_card");
            break;
          case 7:
              if (!repossessedPanel.isCombosUpdated()) {
                  this.repossessedPanel.updateCombos();
                  this.repossessedPanel.updateStatusCombo();
              }
            
            cl.show(this.middlePanel, "repossessed_card"); 
                     break;
          case 8:
            cl.show(this.middlePanel, "statistics_card");
            try {
              this.statistics.updateCombos();
              this.statistics.updateComboYear();
              this.statistics.updateMonthlyEmpeno();
            } catch (Exception ex) {
              (new Config()).saveProp("mpis_last_error", String.valueOf(ex));
//              Logger.getLogger(SubFrame.class.getName()).log(Level.SEVERE, (String)null, ex);
            } 
            break;
          case 9:
            cl.show(this.middlePanel, "settings_card");
            try {
                this.management.updateCombo();
              this.management.updateBranchTable();
              this.management.updateUserTable();
              this.management.updateAddCatTable();
              this.management.updatePetCatTable();
              this.management.retrieveDefSettings();
            } catch (Exception ex) {
              (new Config()).saveProp("mpis_last_error", String.valueOf(ex));
//              Logger.getLogger(SubFrame.class.getName()).log(Level.SEVERE, (String)null, ex);
            }  break;
          case 10:
            try {
              this.empenoRescate.updateYearCombo();
              this.empenoRescate.beginScreen();
            } catch (Exception ex) {
              (new Config()).saveProp("mpis_last_error", String.valueOf(ex));
//              Logger.getLogger(SubFrame.class.getName()).log(Level.SEVERE, (String)null, ex);
            } 
            cl.show(this.middlePanel, "empeno_rescate_card");
            break;
          case 11:
            {
//                    knowYourClient.initProvCombo();
//                    knowYourClient.initTownCombo();
//                    knowYourClient.initBrgyCombo();
//                    knowYourClient.initIDsCombo();
//                    knowYourClient.initSOICombo();
            }
              cl.show(this.middlePanel, "kyc_card");
              break;


          case 99:
            setVisible(false);
            break;
        } 
        this.transaction.refreshTable();
        
    }
    
    public void resetMenuBackgroundColors() {
        helpIconPanel.setBackground(menuBgColor);
        helpPanel.setBackground(menuBgColor);
        newPledgePanel.setBackground(menuBgColor);
        newPledgeIconPanel.setBackground(menuBgColor);
        renewalIconPanel.setBackground(menuBgColor);
        renewalPanel.setBackground(menuBgColor);
        redeemPanel.setBackground(menuBgColor);
        redeemIconPanel.setBackground(menuBgColor);
        searchIconPanel.setBackground(menuBgColor);
        searchPanel.setBackground(menuBgColor);
        expiredIconPanel.setBackground(menuBgColor);
        expiredPanel.setBackground(menuBgColor);
        vaultPanel.setBackground(menuBgColor);
        vaultIconPanel.setBackground(menuBgColor);
        repoIconPanel.setBackground(menuBgColor);
        repoPanel.setBackground(menuBgColor);
        statIconPanel.setBackground(menuBgColor);
        statPanel.setBackground(menuBgColor);
        empenoIconPanel.setBackground(menuBgColor);
        empenoPanel.setBackground(menuBgColor);
        settingIconPanel.setBackground(menuBgColor);
        settingPanel.setBackground(menuBgColor);
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel2 = new javax.swing.JPanel();
        middlePanel = new javax.swing.JPanel();
        knowYourClient = new com.merlin.KnowYourClient();
        newPledge = new com.merlin.NewPledge();
        renewal = new com.merlin.Renewal();
        redemption = new com.merlin.Redemption();
        search = new com.merlin.Search();
        expired = new com.merlin.Expired();
        vaultInvPanel = new com.merlin.VaultInvPanel();
        repossessedPanel = new com.merlin.RepossessedPanel();
        statistics = new com.merlin.Statistics();
        management = new com.merlin.Management();
        empenoRescate = new com.merlin.EmpenoRescate();
        submenuPanel = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        jPanel5 = new javax.swing.JPanel();
        jPanel1 = new javax.swing.JPanel();
        jPanel4 = new javax.swing.JPanel();
        backButtonLabel = new javax.swing.JLabel();
        helpIconPanel = new javax.swing.JPanel();
        jLabel15 = new javax.swing.JLabel();
        newPledgeIconPanel = new javax.swing.JPanel();
        jLabel5 = new javax.swing.JLabel();
        renewalIconPanel = new javax.swing.JPanel();
        jLabel6 = new javax.swing.JLabel();
        redeemIconPanel = new javax.swing.JPanel();
        jLabel7 = new javax.swing.JLabel();
        searchIconPanel = new javax.swing.JPanel();
        jLabel8 = new javax.swing.JLabel();
        expiredIconPanel = new javax.swing.JPanel();
        jLabel9 = new javax.swing.JLabel();
        vaultIconPanel = new javax.swing.JPanel();
        jLabel10 = new javax.swing.JLabel();
        repoIconPanel = new javax.swing.JPanel();
        jLabel11 = new javax.swing.JLabel();
        statIconPanel = new javax.swing.JPanel();
        jLabel12 = new javax.swing.JLabel();
        empenoIconPanel = new javax.swing.JPanel();
        jLabel13 = new javax.swing.JLabel();
        settingIconPanel = new javax.swing.JPanel();
        jLabel14 = new javax.swing.JLabel();
        logoutIconPanel = new javax.swing.JPanel();
        jLabel2 = new javax.swing.JLabel();
        homeIconPanel = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jPanel3 = new javax.swing.JPanel();
        newPledgePanel = new javax.swing.JPanel();
        newpledgelabel = new javax.swing.JLabel();
        renewalPanel = new javax.swing.JPanel();
        renewallabel = new javax.swing.JLabel();
        redeemPanel = new javax.swing.JPanel();
        redeemlabel = new javax.swing.JLabel();
        searchPanel = new javax.swing.JPanel();
        searchlabel = new javax.swing.JLabel();
        expiredPanel = new javax.swing.JPanel();
        expiredlabel = new javax.swing.JLabel();
        vaultPanel = new javax.swing.JPanel();
        vaultlabel = new javax.swing.JLabel();
        repoPanel = new javax.swing.JPanel();
        repolabel = new javax.swing.JLabel();
        statPanel = new javax.swing.JPanel();
        statlabel = new javax.swing.JLabel();
        empenoPanel = new javax.swing.JPanel();
        empenolabel = new javax.swing.JLabel();
        settingPanel = new javax.swing.JPanel();
        settinglabel = new javax.swing.JLabel();
        helpPanel = new javax.swing.JPanel();
        helplabel = new javax.swing.JLabel();
        logoutPanel = new javax.swing.JPanel();
        logoutlabel = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        rightPanel = new javax.swing.JPanel();
        transaction = new com.merlin.Transaction();

        setMaximumSize(new java.awt.Dimension(880, 623));
        setMinimumSize(new java.awt.Dimension(880, 623));
        setLayout(new java.awt.CardLayout());

        jPanel2.setPreferredSize(new java.awt.Dimension(800, 768));
        jPanel2.setLayout(new java.awt.BorderLayout());

        middlePanel.setBackground(new java.awt.Color(209, 229, 240));
        middlePanel.setForeground(new java.awt.Color(102, 102, 102));
        middlePanel.setMinimumSize(new java.awt.Dimension(840, 768));
        middlePanel.setPreferredSize(new java.awt.Dimension(840, 768));
        middlePanel.setLayout(new java.awt.CardLayout());

        knowYourClient.addComponentListener(new java.awt.event.ComponentAdapter() {
            public void componentHidden(java.awt.event.ComponentEvent evt) {
                knowYourClientComponentHidden(evt);
            }
        });
        middlePanel.add(knowYourClient, "kyc_card");
        middlePanel.add(newPledge, "new_pledge_card");
        middlePanel.add(renewal, "renewal_card");
        middlePanel.add(redemption, "redemption_card");

        search.addComponentListener(new java.awt.event.ComponentAdapter() {
            public void componentHidden(java.awt.event.ComponentEvent evt) {
                searchComponentHidden(evt);
            }
        });
        middlePanel.add(search, "search_card");
        middlePanel.add(expired, "expired_listing_card");
        middlePanel.add(vaultInvPanel, "vault_inventory_card");
        middlePanel.add(repossessedPanel, "repossessed_card");
        middlePanel.add(statistics, "statistics_card");
        middlePanel.add(management, "settings_card");
        middlePanel.add(empenoRescate, "empeno_rescate_card");

        jPanel2.add(middlePanel, java.awt.BorderLayout.CENTER);

        submenuPanel.setBackground(new java.awt.Color(239, 246, 250));
        submenuPanel.setMinimumSize(new java.awt.Dimension(210, 768));
        submenuPanel.setPreferredSize(new java.awt.Dimension(210, 768));
        submenuPanel.setLayout(new java.awt.BorderLayout());

        jScrollPane1.setHorizontalScrollBarPolicy(javax.swing.ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);

        jPanel5.setLayout(new java.awt.BorderLayout());

        jPanel1.setBackground(new java.awt.Color(239, 246, 250));
        jPanel1.setMinimumSize(new java.awt.Dimension(50, 748));
        jPanel1.setOpaque(false);
        jPanel1.setPreferredSize(new java.awt.Dimension(50, 10));

        jPanel4.setBackground(new java.awt.Color(239, 246, 250));
        jPanel4.setPreferredSize(new java.awt.Dimension(50, 40));

        backButtonLabel.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/back.png"))); // NOI18N
        backButtonLabel.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                backButtonLabelMouseClicked(evt);
            }
        });
        jPanel4.add(backButtonLabel);

        helpIconPanel.setBackground(new java.awt.Color(119, 178, 211));
        helpIconPanel.setPreferredSize(new java.awt.Dimension(50, 40));

        jLabel15.setBackground(new java.awt.Color(227, 240, 251));
        jLabel15.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/patient.png"))); // NOI18N
        jLabel15.setToolTipText("Know Your Customer");
        jLabel15.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jLabel15MouseClicked(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                jLabel15MouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                jLabel15MouseExited(evt);
            }
        });
        helpIconPanel.add(jLabel15);

        newPledgeIconPanel.setBackground(new java.awt.Color(239, 246, 250));

        jLabel5.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/diamond32px.png"))); // NOI18N
        jLabel5.setToolTipText("New Pledge Loan");
        jLabel5.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jLabel5MouseClicked(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                jLabel5MouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                jLabel5MouseExited(evt);
            }
        });
        newPledgeIconPanel.add(jLabel5);

        renewalIconPanel.setBackground(new java.awt.Color(239, 246, 250));
        renewalIconPanel.setPreferredSize(new java.awt.Dimension(50, 40));

        jLabel6.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/renew32px.png"))); // NOI18N
        jLabel6.setToolTipText("Loan Renewal");
        jLabel6.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jLabel6MouseClicked(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                jLabel6MouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                jLabel6MouseExited(evt);
            }
        });
        renewalIconPanel.add(jLabel6);

        redeemIconPanel.setBackground(new java.awt.Color(239, 246, 250));
        redeemIconPanel.setPreferredSize(new java.awt.Dimension(50, 40));

        jLabel7.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/clipboard32px.png"))); // NOI18N
        jLabel7.setToolTipText("Loan Redemption");
        jLabel7.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jLabel7MouseClicked(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                jLabel7MouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                jLabel7MouseExited(evt);
            }
        });
        redeemIconPanel.add(jLabel7);

        searchIconPanel.setBackground(new java.awt.Color(239, 246, 250));
        searchIconPanel.setPreferredSize(new java.awt.Dimension(50, 40));

        jLabel8.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/search32px.png"))); // NOI18N
        jLabel8.setToolTipText("Client Search");
        jLabel8.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jLabel8MouseClicked(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                jLabel8MouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                jLabel8MouseExited(evt);
            }
        });
        searchIconPanel.add(jLabel8);

        expiredIconPanel.setBackground(new java.awt.Color(239, 246, 250));
        expiredIconPanel.setPreferredSize(new java.awt.Dimension(50, 40));

        jLabel9.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/time32px.png"))); // NOI18N
        jLabel9.setToolTipText("Expired Loans");
        jLabel9.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jLabel9MouseClicked(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                jLabel9MouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                jLabel9MouseExited(evt);
            }
        });
        expiredIconPanel.add(jLabel9);

        vaultIconPanel.setBackground(new java.awt.Color(239, 246, 250));
        vaultIconPanel.setPreferredSize(new java.awt.Dimension(50, 40));

        jLabel10.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/vault32px.png"))); // NOI18N
        jLabel10.setToolTipText("Vault Inventory");
        jLabel10.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jLabel10MouseClicked(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                jLabel10MouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                jLabel10MouseExited(evt);
            }
        });
        vaultIconPanel.add(jLabel10);

        repoIconPanel.setBackground(new java.awt.Color(239, 246, 250));
        repoIconPanel.setPreferredSize(new java.awt.Dimension(50, 40));

        jLabel11.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/repo32px.png"))); // NOI18N
        jLabel11.setToolTipText("Repossessed/Sold");
        jLabel11.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jLabel11MouseClicked(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                jLabel11MouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                jLabel11MouseExited(evt);
            }
        });
        repoIconPanel.add(jLabel11);

        statIconPanel.setBackground(new java.awt.Color(239, 246, 250));
        statIconPanel.setPreferredSize(new java.awt.Dimension(50, 40));

        jLabel12.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/line 32px.png"))); // NOI18N
        jLabel12.setToolTipText("Statistics");
        jLabel12.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jLabel12MouseClicked(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                jLabel12MouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                jLabel12MouseExited(evt);
            }
        });
        statIconPanel.add(jLabel12);

        empenoIconPanel.setBackground(new java.awt.Color(239, 246, 250));
        empenoIconPanel.setPreferredSize(new java.awt.Dimension(50, 40));

        jLabel13.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/books32px.png"))); // NOI18N
        jLabel13.setToolTipText("Empeno/Rescate");
        jLabel13.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jLabel13MouseClicked(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                jLabel13MouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                jLabel13MouseExited(evt);
            }
        });
        empenoIconPanel.add(jLabel13);

        settingIconPanel.setBackground(new java.awt.Color(239, 246, 250));
        settingIconPanel.setPreferredSize(new java.awt.Dimension(50, 40));

        jLabel14.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/setting32px.png"))); // NOI18N
        jLabel14.setToolTipText("Settings");
        jLabel14.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jLabel14MouseClicked(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                jLabel14MouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                jLabel14MouseExited(evt);
            }
        });
        settingIconPanel.add(jLabel14);

        logoutIconPanel.setBackground(new java.awt.Color(239, 246, 250));
        logoutIconPanel.setPreferredSize(new java.awt.Dimension(50, 40));

        jLabel2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/key32px.png"))); // NOI18N
        jLabel2.setToolTipText("Log Out");
        jLabel2.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jLabel2MouseClicked(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                jLabel2MouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                jLabel2MouseExited(evt);
            }
        });
        logoutIconPanel.add(jLabel2);

        homeIconPanel.setBackground(new java.awt.Color(239, 246, 250));
        homeIconPanel.setPreferredSize(new java.awt.Dimension(50, 40));

        jLabel1.setFont(new java.awt.Font("Segoe UI Semibold", 0, 12)); // NOI18N
        jLabel1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/merlin logo 32px.png"))); // NOI18N
        homeIconPanel.add(jLabel1);

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(homeIconPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
            .addComponent(jPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
            .addComponent(helpIconPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
            .addComponent(newPledgeIconPanel, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
            .addComponent(renewalIconPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
            .addComponent(redeemIconPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
            .addComponent(searchIconPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
            .addComponent(expiredIconPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
            .addComponent(vaultIconPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
            .addComponent(repoIconPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
            .addComponent(statIconPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
            .addComponent(empenoIconPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
            .addComponent(settingIconPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
            .addComponent(logoutIconPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(6, 6, 6)
                .addComponent(homeIconPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(20, 20, 20)
                .addComponent(jPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(10, 10, 10)
                .addComponent(helpIconPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(10, 10, 10)
                .addComponent(newPledgeIconPanel, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(10, 10, 10)
                .addComponent(renewalIconPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(10, 10, 10)
                .addComponent(redeemIconPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(10, 10, 10)
                .addComponent(searchIconPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(10, 10, 10)
                .addComponent(expiredIconPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(10, 10, 10)
                .addComponent(vaultIconPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(10, 10, 10)
                .addComponent(repoIconPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(10, 10, 10)
                .addComponent(statIconPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(10, 10, 10)
                .addComponent(empenoIconPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(15, 15, 15)
                .addComponent(settingIconPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(10, 10, 10)
                .addComponent(logoutIconPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        jPanel5.add(jPanel1, java.awt.BorderLayout.WEST);

        jPanel3.setBackground(new java.awt.Color(239, 246, 250));
        jPanel3.setMinimumSize(new java.awt.Dimension(160, 768));
        jPanel3.setOpaque(false);
        jPanel3.setPreferredSize(new java.awt.Dimension(160, 768));
        jPanel3.setRequestFocusEnabled(false);

        newPledgePanel.setBackground(new java.awt.Color(239, 246, 250));
        newPledgePanel.setLayout(new java.awt.BorderLayout());

        newpledgelabel.setFont(robotoBold.deriveFont(Font.BOLD, 12f));
        newpledgelabel.setForeground(new java.awt.Color(51, 51, 51));
        newpledgelabel.setText("NEW PLEDGE LOAN");
        newpledgelabel.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                newpledgelabelMouseClicked(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                newpledgelabelMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                newpledgelabelMouseExited(evt);
            }
        });
        newPledgePanel.add(newpledgelabel, java.awt.BorderLayout.CENTER);

        renewalPanel.setBackground(new java.awt.Color(239, 246, 250));
        renewalPanel.setPreferredSize(new java.awt.Dimension(160, 40));
        renewalPanel.setLayout(new java.awt.BorderLayout());

        renewallabel.setFont(robotoBold.deriveFont(Font.BOLD, 12f));
        renewallabel.setForeground(new java.awt.Color(51, 51, 51));
        renewallabel.setText("LOAN RENEWAL");
        renewallabel.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                renewallabelMouseClicked(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                renewallabelMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                renewallabelMouseExited(evt);
            }
        });
        renewalPanel.add(renewallabel, java.awt.BorderLayout.CENTER);

        redeemPanel.setBackground(new java.awt.Color(239, 246, 250));
        redeemPanel.setPreferredSize(new java.awt.Dimension(160, 40));
        redeemPanel.setLayout(new java.awt.BorderLayout());

        redeemlabel.setFont(robotoBold.deriveFont(Font.BOLD, 12f));
        redeemlabel.setForeground(new java.awt.Color(51, 51, 51));
        redeemlabel.setText("LOAN REDEMPTION");
        redeemlabel.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                redeemlabelMouseClicked(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                redeemlabelMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                redeemlabelMouseExited(evt);
            }
        });
        redeemPanel.add(redeemlabel, java.awt.BorderLayout.CENTER);

        searchPanel.setBackground(new java.awt.Color(239, 246, 250));
        searchPanel.setPreferredSize(new java.awt.Dimension(160, 40));
        searchPanel.setLayout(new java.awt.BorderLayout());

        searchlabel.setFont(robotoBold.deriveFont(Font.BOLD, 12f));
        searchlabel.setForeground(new java.awt.Color(51, 51, 51));
        searchlabel.setText("CLIENT SEARCH");
        searchlabel.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                searchlabelMouseClicked(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                searchlabelMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                searchlabelMouseExited(evt);
            }
        });
        searchPanel.add(searchlabel, java.awt.BorderLayout.CENTER);

        expiredPanel.setBackground(new java.awt.Color(239, 246, 250));
        expiredPanel.setPreferredSize(new java.awt.Dimension(160, 40));
        expiredPanel.setLayout(new java.awt.BorderLayout());

        expiredlabel.setFont(robotoBold.deriveFont(Font.BOLD, 12f));
        expiredlabel.setForeground(new java.awt.Color(51, 51, 51));
        expiredlabel.setText("EXPIRED LOANS");
        expiredlabel.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                expiredlabelMouseClicked(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                expiredlabelMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                expiredlabelMouseExited(evt);
            }
        });
        expiredPanel.add(expiredlabel, java.awt.BorderLayout.CENTER);

        vaultPanel.setBackground(new java.awt.Color(239, 246, 250));
        vaultPanel.setPreferredSize(new java.awt.Dimension(160, 40));
        vaultPanel.setLayout(new java.awt.BorderLayout());

        vaultlabel.setFont(robotoBold.deriveFont(Font.BOLD, 12f));
        vaultlabel.setForeground(new java.awt.Color(51, 51, 51));
        vaultlabel.setText("VAULT INVENTORY");
        vaultlabel.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                vaultlabelMouseClicked(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                vaultlabelMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                vaultlabelMouseExited(evt);
            }
        });
        vaultPanel.add(vaultlabel, java.awt.BorderLayout.CENTER);

        repoPanel.setBackground(new java.awt.Color(239, 246, 250));
        repoPanel.setPreferredSize(new java.awt.Dimension(160, 40));
        repoPanel.setLayout(new java.awt.BorderLayout());

        repolabel.setFont(robotoBold.deriveFont(Font.BOLD, 12f));
        repolabel.setForeground(new java.awt.Color(51, 51, 51));
        repolabel.setText("REPOSSESSED / SOLD");
        repolabel.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                repolabelMouseClicked(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                repolabelMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                repolabelMouseExited(evt);
            }
        });
        repoPanel.add(repolabel, java.awt.BorderLayout.CENTER);

        statPanel.setBackground(new java.awt.Color(239, 246, 250));
        statPanel.setPreferredSize(new java.awt.Dimension(160, 40));
        statPanel.setLayout(new java.awt.BorderLayout());

        statlabel.setFont(robotoBold.deriveFont(Font.BOLD, 12f));
        statlabel.setForeground(new java.awt.Color(51, 51, 51));
        statlabel.setText("STATISTICS");
        statlabel.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                statlabelMouseClicked(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                statlabelMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                statlabelMouseExited(evt);
            }
        });
        statPanel.add(statlabel, java.awt.BorderLayout.CENTER);

        empenoPanel.setBackground(new java.awt.Color(239, 246, 250));
        empenoPanel.setPreferredSize(new java.awt.Dimension(160, 40));
        empenoPanel.setLayout(new java.awt.BorderLayout());

        empenolabel.setFont(robotoBold.deriveFont(Font.BOLD, 12f));
        empenolabel.setForeground(new java.awt.Color(51, 51, 51));
        empenolabel.setText("EMPENO / RESCATE");
        empenolabel.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                empenolabelMouseClicked(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                empenolabelMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                empenolabelMouseExited(evt);
            }
        });
        empenoPanel.add(empenolabel, java.awt.BorderLayout.CENTER);

        settingPanel.setBackground(new java.awt.Color(239, 246, 250));
        settingPanel.setPreferredSize(new java.awt.Dimension(160, 40));
        settingPanel.setLayout(new java.awt.BorderLayout());

        settinglabel.setFont(robotoBold.deriveFont(Font.BOLD, 12f));
        settinglabel.setForeground(new java.awt.Color(51, 51, 51));
        settinglabel.setText("SETTINGS");
        settinglabel.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                settinglabelMouseClicked(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                settinglabelMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                settinglabelMouseExited(evt);
            }
        });
        settingPanel.add(settinglabel, java.awt.BorderLayout.CENTER);

        helpPanel.setBackground(new java.awt.Color(119, 178, 211));
        helpPanel.setPreferredSize(new java.awt.Dimension(160, 40));
        helpPanel.setLayout(new java.awt.BorderLayout());

        helplabel.setFont(robotoBold.deriveFont(Font.BOLD, 12f));
        helplabel.setForeground(new java.awt.Color(51, 51, 51));
        helplabel.setText("KNOW YOUR CUSTOMER");
        helplabel.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                helplabelMouseClicked(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                helplabelMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                helplabelMouseExited(evt);
            }
        });
        helpPanel.add(helplabel, java.awt.BorderLayout.CENTER);

        logoutPanel.setBackground(new java.awt.Color(239, 246, 250));
        logoutPanel.setPreferredSize(new java.awt.Dimension(160, 40));
        logoutPanel.setLayout(new java.awt.BorderLayout());

        logoutlabel.setFont(robotoBold.deriveFont(Font.BOLD, 12f));
        logoutlabel.setForeground(new java.awt.Color(51, 51, 51));
        logoutlabel.setText("LOG OUT");
        logoutlabel.setPreferredSize(new java.awt.Dimension(40, 16));
        logoutlabel.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                logoutlabelMouseClicked(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                logoutlabelMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                logoutlabelMouseExited(evt);
            }
        });
        logoutPanel.add(logoutlabel, java.awt.BorderLayout.CENTER);

        jLabel3.setFont(robotoCondensedBold.deriveFont(Font.BOLD, 13f));
        jLabel3.setForeground(new java.awt.Color(51, 51, 51));
        jLabel3.setText("MERLIN PAWNSHOP, INC.");
        jLabel3.setVerticalAlignment(javax.swing.SwingConstants.BOTTOM);

        jLabel4.setFont(robotoCondensedBold.deriveFont(Font.BOLD, 11f));
        jLabel4.setForeground(new java.awt.Color(51, 51, 51));
        jLabel4.setText("PAWN MANAGEMENT SYSTEM");
        jLabel4.setVerticalAlignment(javax.swing.SwingConstants.TOP);

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(helpPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
            .addComponent(newPledgePanel, javax.swing.GroupLayout.PREFERRED_SIZE, 160, javax.swing.GroupLayout.PREFERRED_SIZE)
            .addComponent(renewalPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
            .addComponent(redeemPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
            .addComponent(searchPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
            .addComponent(expiredPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
            .addComponent(vaultPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
            .addComponent(repoPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
            .addComponent(statPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
            .addComponent(empenoPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
            .addComponent(settingPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
            .addComponent(logoutPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel4)
                    .addComponent(jLabel3)))
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addGap(14, 14, 14)
                .addComponent(jLabel3)
                .addGap(0, 0, 0)
                .addComponent(jLabel4)
                .addGap(69, 69, 69)
                .addComponent(helpPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(10, 10, 10)
                .addComponent(newPledgePanel, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(10, 10, 10)
                .addComponent(renewalPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(10, 10, 10)
                .addComponent(redeemPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(10, 10, 10)
                .addComponent(searchPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(10, 10, 10)
                .addComponent(expiredPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(10, 10, 10)
                .addComponent(vaultPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(10, 10, 10)
                .addComponent(repoPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(10, 10, 10)
                .addComponent(statPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(10, 10, 10)
                .addComponent(empenoPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(15, 15, 15)
                .addComponent(settingPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(10, 10, 10)
                .addComponent(logoutPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        jPanel5.add(jPanel3, java.awt.BorderLayout.CENTER);

        jScrollPane1.setViewportView(jPanel5);

        submenuPanel.add(jScrollPane1, java.awt.BorderLayout.CENTER);

        jPanel2.add(submenuPanel, java.awt.BorderLayout.WEST);

        rightPanel.setLayout(new java.awt.BorderLayout());
        rightPanel.add(transaction, java.awt.BorderLayout.CENTER);

        jPanel2.add(rightPanel, java.awt.BorderLayout.EAST);

        add(jPanel2, "card2");
    }// </editor-fold>//GEN-END:initComponents

    private void newPledgeComponentHidden(java.awt.event.ComponentEvent evt) {//GEN-FIRST:event_newPledgeComponentHidden

    }//GEN-LAST:event_newPledgeComponentHidden

    private void renewalComponentHidden(java.awt.event.ComponentEvent evt) {//GEN-FIRST:event_renewalComponentHidden

    }//GEN-LAST:event_renewalComponentHidden

    private void renewalComponentShown(java.awt.event.ComponentEvent evt) {//GEN-FIRST:event_renewalComponentShown
        this.renewal.requestFocusInSearch();
        this.transaction.refreshTable();
    }//GEN-LAST:event_renewalComponentShown

    private void redemptionComponentHidden(java.awt.event.ComponentEvent evt) {//GEN-FIRST:event_redemptionComponentHidden

    }//GEN-LAST:event_redemptionComponentHidden

    private void redemptionComponentShown(java.awt.event.ComponentEvent evt) {//GEN-FIRST:event_redemptionComponentShown
        this.redemption.requestFocusInSearch();
        this.transaction.refreshTable();

    }//GEN-LAST:event_redemptionComponentShown

    private void newPledgeComponentShown(java.awt.event.ComponentEvent evt) {//GEN-FIRST:event_newPledgeComponentShown
        this.newPledge.requestFocusInName();
        this.transaction.refreshTable();
    }//GEN-LAST:event_newPledgeComponentShown

    private void searchComponentHidden(java.awt.event.ComponentEvent evt) {//GEN-FIRST:event_searchComponentHidden
        if (this.search.isNewLoanActivated()) {
            CardLayout cl = (CardLayout)this.middlePanel.getLayout();
            this.newPledge.fill_constants(this.search.getConstant1(), this.search.getConstant2(), this.search.getConstant3(), this.search.getConstant4(), this.search.getConstant5());
//            cl.show(this.middlePanel, "new_pledge_card");
            newpledgelabelMouseClicked(null);
        } else if (this.search.isRenewLoanActivated()) {
            this.renewal.updateCombos();
            this.renewal.renewLoanFromSearch(this.search.getRenewPapNo(), this.search.getRenewBranch());
            CardLayout cl = (CardLayout)this.middlePanel.getLayout();
            cl.show(this.middlePanel, "renewal_card");
        } else if (this.search.isRedeemLoanActivated()) {
            this.redemption.updateCombos();
            this.redemption.redeemLoanFromSearch(this.search.getRenewPapNo(), this.search.getRenewBranch());
            CardLayout cl = (CardLayout)this.middlePanel.getLayout();
            cl.show(this.middlePanel, "redemption_card");
        } else {
            CardLayout cl = (CardLayout)this.middlePanel.getLayout();
            cl.show(this.middlePanel, "menu_card");
          } 
          this.transaction.refreshTable();
    }//GEN-LAST:event_searchComponentHidden

    private void expiredComponentHidden(java.awt.event.ComponentEvent evt) {//GEN-FIRST:event_expiredComponentHidden
    }//GEN-LAST:event_expiredComponentHidden

    private void empenoRescateComponentHidden(java.awt.event.ComponentEvent evt) {//GEN-FIRST:event_empenoRescateComponentHidden
    }//GEN-LAST:event_empenoRescateComponentHidden

    private void repossessedPanelComponentHidden(java.awt.event.ComponentEvent evt) {//GEN-FIRST:event_repossessedPanelComponentHidden
    }//GEN-LAST:event_repossessedPanelComponentHidden

    private void vaultInvPanelComponentHidden(java.awt.event.ComponentEvent evt) {//GEN-FIRST:event_vaultInvPanelComponentHidden
    }//GEN-LAST:event_vaultInvPanelComponentHidden

    private void managementComponentHidden(java.awt.event.ComponentEvent evt) {//GEN-FIRST:event_managementComponentHidden
    }//GEN-LAST:event_managementComponentHidden

    private void statisticsComponentHidden(java.awt.event.ComponentEvent evt) {//GEN-FIRST:event_statisticsComponentHidden
    }//GEN-LAST:event_statisticsComponentHidden

    private void jLabel2MouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel2MouseExited
        logoutlabelMouseExited(evt);
    }//GEN-LAST:event_jLabel2MouseExited

    private void jLabel2MouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel2MouseEntered
        logoutlabelMouseEntered(evt);
    }//GEN-LAST:event_jLabel2MouseEntered

    private void jLabel2MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel2MouseClicked
        logoutlabelMouseClicked(evt);
    }//GEN-LAST:event_jLabel2MouseClicked

    private void jLabel15MouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel15MouseExited
        helplabelMouseExited(evt);
    }//GEN-LAST:event_jLabel15MouseExited

    private void jLabel15MouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel15MouseEntered
        helplabelMouseEntered(evt);
    }//GEN-LAST:event_jLabel15MouseEntered

    private void jLabel15MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel15MouseClicked
        helplabelMouseClicked(evt);
    }//GEN-LAST:event_jLabel15MouseClicked

    private void jLabel14MouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel14MouseExited
        settinglabelMouseExited(evt);
    }//GEN-LAST:event_jLabel14MouseExited

    private void jLabel14MouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel14MouseEntered
        settinglabelMouseEntered(evt);
    }//GEN-LAST:event_jLabel14MouseEntered

    private void jLabel14MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel14MouseClicked
        settinglabelMouseClicked(evt);
    }//GEN-LAST:event_jLabel14MouseClicked

    private void jLabel13MouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel13MouseExited
        empenolabelMouseExited(evt);
    }//GEN-LAST:event_jLabel13MouseExited

    private void jLabel13MouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel13MouseEntered
        empenolabelMouseEntered(evt);
    }//GEN-LAST:event_jLabel13MouseEntered

    private void jLabel13MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel13MouseClicked
        empenolabelMouseClicked(evt);
    }//GEN-LAST:event_jLabel13MouseClicked

    private void jLabel12MouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel12MouseExited
        statlabelMouseExited(evt);
    }//GEN-LAST:event_jLabel12MouseExited

    private void jLabel12MouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel12MouseEntered
        statlabelMouseEntered(evt);
    }//GEN-LAST:event_jLabel12MouseEntered

    private void jLabel12MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel12MouseClicked
        statlabelMouseClicked(evt);
    }//GEN-LAST:event_jLabel12MouseClicked

    private void jLabel11MouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel11MouseExited
        repolabelMouseExited(evt);
    }//GEN-LAST:event_jLabel11MouseExited

    private void jLabel11MouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel11MouseEntered
        repolabelMouseEntered(evt);
    }//GEN-LAST:event_jLabel11MouseEntered

    private void jLabel11MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel11MouseClicked
        repolabelMouseClicked(evt);
    }//GEN-LAST:event_jLabel11MouseClicked

    private void jLabel10MouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel10MouseExited
        vaultlabelMouseExited(evt);
    }//GEN-LAST:event_jLabel10MouseExited

    private void jLabel10MouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel10MouseEntered
        vaultlabelMouseEntered(evt);
    }//GEN-LAST:event_jLabel10MouseEntered

    private void jLabel10MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel10MouseClicked
        vaultlabelMouseClicked(evt);
    }//GEN-LAST:event_jLabel10MouseClicked

    private void jLabel9MouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel9MouseExited
        expiredlabelMouseExited(evt);
    }//GEN-LAST:event_jLabel9MouseExited

    private void jLabel9MouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel9MouseEntered
        expiredlabelMouseEntered(evt);
    }//GEN-LAST:event_jLabel9MouseEntered

    private void jLabel9MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel9MouseClicked
        expiredlabelMouseClicked(evt);
    }//GEN-LAST:event_jLabel9MouseClicked

    private void jLabel8MouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel8MouseExited
        searchlabelMouseExited(evt);
    }//GEN-LAST:event_jLabel8MouseExited

    private void jLabel8MouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel8MouseEntered
        searchlabelMouseEntered(evt);
    }//GEN-LAST:event_jLabel8MouseEntered

    private void jLabel8MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel8MouseClicked
        searchlabelMouseClicked(evt);
    }//GEN-LAST:event_jLabel8MouseClicked

    private void jLabel7MouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel7MouseExited
        redeemlabelMouseExited(evt);
    }//GEN-LAST:event_jLabel7MouseExited

    private void jLabel7MouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel7MouseEntered
        redeemlabelMouseEntered(evt);
    }//GEN-LAST:event_jLabel7MouseEntered

    private void jLabel7MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel7MouseClicked
        redeemlabelMouseClicked(evt);
    }//GEN-LAST:event_jLabel7MouseClicked

    private void jLabel6MouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel6MouseExited
        renewallabelMouseExited(evt);
    }//GEN-LAST:event_jLabel6MouseExited

    private void jLabel6MouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel6MouseEntered
        renewallabelMouseEntered(evt);
    }//GEN-LAST:event_jLabel6MouseEntered

    private void jLabel6MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel6MouseClicked
        renewallabelMouseClicked(evt);
    }//GEN-LAST:event_jLabel6MouseClicked

    private void jLabel5MouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel5MouseExited
        newpledgelabelMouseExited(evt);
    }//GEN-LAST:event_jLabel5MouseExited

    private void jLabel5MouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel5MouseEntered
        newpledgelabelMouseEntered(evt);
    }//GEN-LAST:event_jLabel5MouseEntered

    private void backButtonLabelMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_backButtonLabelMouseClicked
        if (a == true) {
            System.out.println("hiding panel");
            hideShow(submenuPanel, a, backButtonLabel);
            SwingUtilities.updateComponentTreeUI(this);
            System.out.println(jPanel3.getSize().toString());
            a = false;
        } else {
            System.out.println("showing panel");
            hideShow(submenuPanel, a, backButtonLabel);
            SwingUtilities.updateComponentTreeUI(this);
            jPanel3.setPreferredSize(new java.awt.Dimension(160, 768));
            System.out.println(jPanel3.getSize().toString());
            a = true;
        }
        
        
    }//GEN-LAST:event_backButtonLabelMouseClicked

    private void logoutlabelMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_logoutlabelMouseExited
        if (logoutPanel.getBackground() != menuSelectedColor) {
            changeColor(logoutPanel, menuBgColor);
            changeColor(logoutIconPanel, menuBgColor);
        }
    }//GEN-LAST:event_logoutlabelMouseExited

    private void logoutlabelMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_logoutlabelMouseEntered
        if (logoutPanel.getBackground() != menuSelectedColor) {
            changeColor(logoutPanel, highlightHover);
            changeColor(logoutIconPanel, highlightHover);
        }
    }//GEN-LAST:event_logoutlabelMouseEntered

    private void logoutlabelMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_logoutlabelMouseClicked
        changeColor(logoutPanel, clickedColor);
        changeColor(logoutIconPanel, clickedColor);
        showCard(99);
    }//GEN-LAST:event_logoutlabelMouseClicked

    private void helplabelMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_helplabelMouseExited
        if (helpPanel.getBackground() != menuSelectedColor) {
            changeColor(helpPanel, menuBgColor);
            changeColor(helpIconPanel, menuBgColor);
        }
    }//GEN-LAST:event_helplabelMouseExited

    private void helplabelMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_helplabelMouseEntered
        if (helpPanel.getBackground() != menuSelectedColor) {
            changeColor(helpPanel, highlightHover);
            changeColor(helpIconPanel, highlightHover);
        }
    }//GEN-LAST:event_helplabelMouseEntered

    private void helplabelMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_helplabelMouseClicked
        changeColor(helpPanel, clickedColor);
        changeColor(helpIconPanel, clickedColor);
        resetMenuBackgroundColors();
        helpIconPanel.setBackground(menuSelectedColor);
        helpPanel.setBackground(menuSelectedColor);
        showCard(11);
    }//GEN-LAST:event_helplabelMouseClicked

    private void empenolabelMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_empenolabelMouseExited
        if (empenoPanel.getBackground() != menuSelectedColor) {
            changeColor(empenoPanel, menuBgColor);
            changeColor(empenoIconPanel, menuBgColor);
        }
    }//GEN-LAST:event_empenolabelMouseExited

    private void empenolabelMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_empenolabelMouseEntered
        if (empenoPanel.getBackground() != menuSelectedColor) {
            changeColor(empenoPanel, highlightHover);
            changeColor(empenoIconPanel, highlightHover);
        }
    }//GEN-LAST:event_empenolabelMouseEntered

    private void empenolabelMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_empenolabelMouseClicked
        changeColor(empenoPanel, clickedColor);
        changeColor(empenoIconPanel, clickedColor);
        resetMenuBackgroundColors();
        empenoPanel.setBackground(menuSelectedColor);
        empenoIconPanel.setBackground(menuSelectedColor);
        showCard(10);
    }//GEN-LAST:event_empenolabelMouseClicked

    private void settinglabelMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_settinglabelMouseExited
        if (settingPanel.getBackground() != menuSelectedColor) {
            changeColor(settingPanel, menuBgColor);
            changeColor(settingIconPanel, menuBgColor);
        }
    }//GEN-LAST:event_settinglabelMouseExited

    private void settinglabelMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_settinglabelMouseEntered
        if (settingPanel.getBackground() != menuSelectedColor) {
            changeColor(settingPanel, highlightHover);
            changeColor(settingIconPanel, highlightHover);
        }
    }//GEN-LAST:event_settinglabelMouseEntered

    private void settinglabelMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_settinglabelMouseClicked
        changeColor(settingPanel, clickedColor);
        changeColor(settingIconPanel, clickedColor);
        resetMenuBackgroundColors();
        settingIconPanel.setBackground(menuSelectedColor);
        settingPanel.setBackground(menuSelectedColor);
        showCard(9);
    }//GEN-LAST:event_settinglabelMouseClicked

    private void statlabelMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_statlabelMouseExited
        if (statPanel.getBackground() != menuSelectedColor) {
            changeColor(statPanel, menuBgColor);
            changeColor(statIconPanel, menuBgColor);
        }
    }//GEN-LAST:event_statlabelMouseExited

    private void statlabelMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_statlabelMouseEntered
        if (statPanel.getBackground() != menuSelectedColor) {
            changeColor(statPanel, highlightHover);
            changeColor(statIconPanel, highlightHover);
        }
    }//GEN-LAST:event_statlabelMouseEntered

    private void statlabelMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_statlabelMouseClicked
        changeColor(statPanel, clickedColor);
        changeColor(statIconPanel, clickedColor);
        resetMenuBackgroundColors();
        statIconPanel.setBackground(menuSelectedColor);
        statPanel.setBackground(menuSelectedColor);
        showCard(8);
    }//GEN-LAST:event_statlabelMouseClicked

    private void repolabelMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_repolabelMouseExited
        if (repoPanel.getBackground() != menuSelectedColor) {
            changeColor(repoPanel, menuBgColor);
            changeColor(repoIconPanel, menuBgColor);
        }
    }//GEN-LAST:event_repolabelMouseExited

    private void repolabelMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_repolabelMouseEntered
        if (repoPanel.getBackground() != menuSelectedColor) {
            changeColor(repoPanel, highlightHover);
            changeColor(repoIconPanel, highlightHover);
        }
    }//GEN-LAST:event_repolabelMouseEntered

    private void repolabelMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_repolabelMouseClicked
        changeColor(repoPanel, clickedColor);
        changeColor(repoIconPanel, clickedColor);
        resetMenuBackgroundColors();
        repoIconPanel.setBackground(menuSelectedColor);
        repoPanel.setBackground(menuSelectedColor);
        showCard(7);
    }//GEN-LAST:event_repolabelMouseClicked

    private void vaultlabelMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_vaultlabelMouseExited
        if (vaultPanel.getBackground() != menuSelectedColor) {
            changeColor(vaultPanel, menuBgColor);
            changeColor(vaultIconPanel, menuBgColor);
        }
    }//GEN-LAST:event_vaultlabelMouseExited

    private void vaultlabelMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_vaultlabelMouseEntered
        if (vaultPanel.getBackground() != menuSelectedColor) {
            changeColor(vaultPanel, highlightHover);
            changeColor(vaultIconPanel, highlightHover);
        }
    }//GEN-LAST:event_vaultlabelMouseEntered

    private void vaultlabelMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_vaultlabelMouseClicked
        changeColor(vaultPanel, clickedColor);
        changeColor(vaultIconPanel, clickedColor);
        resetMenuBackgroundColors();
        vaultIconPanel.setBackground(menuSelectedColor);
        vaultPanel.setBackground(menuSelectedColor);
        showCard(6);
    }//GEN-LAST:event_vaultlabelMouseClicked

    private void expiredlabelMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_expiredlabelMouseExited
        if (expiredPanel.getBackground() != menuSelectedColor) {
            changeColor(expiredPanel, menuBgColor);
            changeColor(expiredIconPanel, menuBgColor);
        }
    }//GEN-LAST:event_expiredlabelMouseExited

    private void expiredlabelMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_expiredlabelMouseEntered
        if (expiredPanel.getBackground() != menuSelectedColor) {
            changeColor(expiredPanel, highlightHover);
            changeColor(expiredIconPanel, highlightHover);
        }
    }//GEN-LAST:event_expiredlabelMouseEntered

    private void expiredlabelMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_expiredlabelMouseClicked
        changeColor(expiredPanel, clickedColor);
        changeColor(expiredIconPanel, clickedColor);
        resetMenuBackgroundColors();
        expiredIconPanel.setBackground(menuSelectedColor);
        expiredPanel.setBackground(menuSelectedColor);
        showCard(5);
    }//GEN-LAST:event_expiredlabelMouseClicked

    private void searchlabelMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_searchlabelMouseExited
        if (searchPanel.getBackground() != menuSelectedColor) {
            changeColor(searchPanel, menuBgColor);
            changeColor(searchIconPanel, menuBgColor);
        }
    }//GEN-LAST:event_searchlabelMouseExited

    private void searchlabelMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_searchlabelMouseEntered
        if (searchPanel.getBackground() != menuSelectedColor) {
            changeColor(searchPanel, highlightHover);
            changeColor(searchIconPanel, highlightHover);
        }
    }//GEN-LAST:event_searchlabelMouseEntered

    private void searchlabelMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_searchlabelMouseClicked
        changeColor(searchPanel, clickedColor);
        changeColor(searchIconPanel, clickedColor);
        resetMenuBackgroundColors();
        searchIconPanel.setBackground(menuSelectedColor);
        searchPanel.setBackground(menuSelectedColor);
        showCard(4);
    }//GEN-LAST:event_searchlabelMouseClicked

    private void redeemlabelMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_redeemlabelMouseExited
        if (redeemPanel.getBackground() != menuSelectedColor) {
            changeColor(redeemPanel, menuBgColor);
            changeColor(redeemIconPanel, menuBgColor);
        }
    }//GEN-LAST:event_redeemlabelMouseExited

    private void redeemlabelMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_redeemlabelMouseEntered
        if (redeemPanel.getBackground() != menuSelectedColor) {
            changeColor(redeemPanel, highlightHover);
            changeColor(redeemIconPanel, highlightHover);
        }
    }//GEN-LAST:event_redeemlabelMouseEntered

    private void redeemlabelMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_redeemlabelMouseClicked
        changeColor(redeemPanel, clickedColor);
        changeColor(redeemIconPanel, clickedColor);
        resetMenuBackgroundColors();
        redeemIconPanel.setBackground(menuSelectedColor);
        redeemPanel.setBackground(menuSelectedColor);
        showCard(3);
    }//GEN-LAST:event_redeemlabelMouseClicked

    private void renewallabelMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_renewallabelMouseExited
        if (renewalPanel.getBackground() != menuSelectedColor) {
            changeColor(renewalPanel, menuBgColor);
            changeColor(renewalIconPanel, menuBgColor);
        }
    }//GEN-LAST:event_renewallabelMouseExited

    private void renewallabelMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_renewallabelMouseEntered
        if (renewalPanel.getBackground() != menuSelectedColor) {
            changeColor(renewalPanel, highlightHover);
            changeColor(renewalIconPanel, highlightHover);
        }
    }//GEN-LAST:event_renewallabelMouseEntered

    private void renewallabelMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_renewallabelMouseClicked
        changeColor(renewalPanel, clickedColor);
        changeColor(renewalIconPanel, clickedColor);
        resetMenuBackgroundColors();
        renewalIconPanel.setBackground(menuSelectedColor);
        renewalPanel.setBackground(menuSelectedColor);
        showCard(2);
    }//GEN-LAST:event_renewallabelMouseClicked

    private void newpledgelabelMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_newpledgelabelMouseExited
        if (newPledgePanel.getBackground() != menuSelectedColor) {
            changeColor(newPledgePanel, menuBgColor);
            changeColor(newPledgeIconPanel, menuBgColor);
        }
    }//GEN-LAST:event_newpledgelabelMouseExited

    private void newpledgelabelMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_newpledgelabelMouseEntered
        if (newPledgePanel.getBackground() != menuSelectedColor) {
            changeColor(newPledgePanel, highlightHover);
            changeColor(newPledgeIconPanel, highlightHover);
        }
    }//GEN-LAST:event_newpledgelabelMouseEntered

    private void newpledgelabelMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_newpledgelabelMouseClicked
        changeColor(newPledgePanel, clickedColor);
        changeColor(newPledgeIconPanel, clickedColor);
        resetMenuBackgroundColors();
        newPledgePanel.setBackground(menuSelectedColor);
        newPledgeIconPanel.setBackground(menuSelectedColor);
        showCard(1);
    }//GEN-LAST:event_newpledgelabelMouseClicked

    private void jLabel5MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel5MouseClicked
        newpledgelabelMouseClicked(evt);
    }//GEN-LAST:event_jLabel5MouseClicked

    private void knowYourClientComponentHidden(java.awt.event.ComponentEvent evt) {//GEN-FIRST:event_knowYourClientComponentHidden
        if (knowYourClient.isCreateLoan()) {
            knowYourClient.setCreateLoan(false);
            this.newPledge.fill_constants(this.knowYourClient.getClientName(), knowYourClient.getClientAddress(), knowYourClient.getClientNumber());
            newpledgelabelMouseClicked(null);
        }
    }//GEN-LAST:event_knowYourClientComponentHidden


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel backButtonLabel;
    private javax.swing.JPanel empenoIconPanel;
    private javax.swing.JPanel empenoPanel;
    private com.merlin.EmpenoRescate empenoRescate;
    private javax.swing.JLabel empenolabel;
    private com.merlin.Expired expired;
    private javax.swing.JPanel expiredIconPanel;
    private javax.swing.JPanel expiredPanel;
    private javax.swing.JLabel expiredlabel;
    private javax.swing.JPanel helpIconPanel;
    private javax.swing.JPanel helpPanel;
    private javax.swing.JLabel helplabel;
    private javax.swing.JPanel homeIconPanel;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel15;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JScrollPane jScrollPane1;
    private com.merlin.KnowYourClient knowYourClient;
    private javax.swing.JPanel logoutIconPanel;
    private javax.swing.JPanel logoutPanel;
    private javax.swing.JLabel logoutlabel;
    private com.merlin.Management management;
    private javax.swing.JPanel middlePanel;
    private com.merlin.NewPledge newPledge;
    private javax.swing.JPanel newPledgeIconPanel;
    private javax.swing.JPanel newPledgePanel;
    private javax.swing.JLabel newpledgelabel;
    private javax.swing.JPanel redeemIconPanel;
    private javax.swing.JPanel redeemPanel;
    private javax.swing.JLabel redeemlabel;
    private com.merlin.Redemption redemption;
    private com.merlin.Renewal renewal;
    private javax.swing.JPanel renewalIconPanel;
    private javax.swing.JPanel renewalPanel;
    private javax.swing.JLabel renewallabel;
    private javax.swing.JPanel repoIconPanel;
    private javax.swing.JPanel repoPanel;
    private javax.swing.JLabel repolabel;
    private com.merlin.RepossessedPanel repossessedPanel;
    private javax.swing.JPanel rightPanel;
    private com.merlin.Search search;
    private javax.swing.JPanel searchIconPanel;
    private javax.swing.JPanel searchPanel;
    private javax.swing.JLabel searchlabel;
    private javax.swing.JPanel settingIconPanel;
    private javax.swing.JPanel settingPanel;
    private javax.swing.JLabel settinglabel;
    private javax.swing.JPanel statIconPanel;
    private javax.swing.JPanel statPanel;
    private com.merlin.Statistics statistics;
    private javax.swing.JLabel statlabel;
    private javax.swing.JPanel submenuPanel;
    private com.merlin.Transaction transaction;
    private javax.swing.JPanel vaultIconPanel;
    private com.merlin.VaultInvPanel vaultInvPanel;
    private javax.swing.JPanel vaultPanel;
    private javax.swing.JLabel vaultlabel;
    // End of variables declaration//GEN-END:variables
}
