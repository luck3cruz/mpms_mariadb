/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mpis_mariadb_2;

import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.sql.SQLException;
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
        newLoanActivated = false;
        initComponents();
    }
    boolean a = true;
    final Color highlightHover = new Color(209, 229, 240);
    final Color menuBgColor = new Color(239, 246, 250);
    final Color clickedColor = new Color(119, 179, 212);
    private boolean newLoanActivated;
//    private CardLayout cl = (CardLayout)this.leftPanel.getLayout();
    
    
    
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
            changeIconImage(button, "/mpis_mariadb_2/forward.png");
        } else {
            menuShowHide.setPreferredSize(new Dimension(270, menuShowHide.getHeight()));
            changeIconImage(button, "/mpis_mariadb_2/back.png");
        }
    }
    
    public void showCard(int cardNum) {
        CardLayout cl = (CardLayout)this.middlePanel.getLayout();
        switch (cardNum) {
          case 1:
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
            this.search.updateCombos();
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
            this.repossessedPanel.updateCombos();
            cl.show(this.middlePanel, "repossessed_card"); 
                     break;
          case 8:
            cl.show(this.middlePanel, "statistics_card");
            try {
              this.statistics.updateCombos();
              this.statistics.updateComboYear();
              this.statistics.updateMonthlyEmpeno();
            } catch (SQLException ex) {
              (new Config()).saveProp("mpis_last_error", String.valueOf(ex));
              Logger.getLogger(SubFrame.class.getName()).log(Level.SEVERE, (String)null, ex);
            } 
            break;
          case 9:
            cl.show(this.middlePanel, "settings_card");
            try {
              this.management.updateBranchTable();
              this.management.updateUserTable();
              this.management.updateAddCatTable();
              this.management.updatePetCatTable();
              this.management.retrieveDefSettings();
            } catch (SQLException ex) {
              (new Config()).saveProp("mpis_last_error", String.valueOf(ex));
              Logger.getLogger(SubFrame.class.getName()).log(Level.SEVERE, (String)null, ex);
            }  break;
          case 10:
            try {
              this.empenoRescate.updateYearCombo();
              this.empenoRescate.beginScreen();
            } catch (SQLException ex) {
              (new Config()).saveProp("mpis_last_error", String.valueOf(ex));
              Logger.getLogger(SubFrame.class.getName()).log(Level.SEVERE, (String)null, ex);
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

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel2 = new javax.swing.JPanel();
        transaction = new mpis_mariadb_2.Transaction();
        middlePanel = new javax.swing.JPanel();
        knowYourClient = new mpis_mariadb_2.KnowYourClient();
        newPledge = new mpis_mariadb_2.NewPledge();
        renewal = new mpis_mariadb_2.Renewal();
        redemption = new mpis_mariadb_2.Redemption();
        search = new mpis_mariadb_2.Search();
        expired = new mpis_mariadb_2.Expired();
        vaultInvPanel = new mpis_mariadb_2.VaultInvPanel();
        repossessedPanel = new mpis_mariadb_2.RepossessedPanel();
        statistics = new mpis_mariadb_2.Statistics();
        management = new mpis_mariadb_2.Management();
        empenoRescate = new mpis_mariadb_2.EmpenoRescate();
        renewal1 = new mpis_mariadb_2.Renewal();
        submenuPanel = new javax.swing.JPanel();
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
        jLabel42 = new javax.swing.JLabel();
        jLabel43 = new javax.swing.JLabel();
        jLabel44 = new javax.swing.JLabel();
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

        setMaximumSize(new java.awt.Dimension(880, 623));
        setMinimumSize(new java.awt.Dimension(880, 623));
        setLayout(new java.awt.CardLayout());

        jPanel2.setPreferredSize(new java.awt.Dimension(800, 768));
        jPanel2.setLayout(new java.awt.BorderLayout());

        transaction.setBorder(null);
        jPanel2.add(transaction, java.awt.BorderLayout.LINE_END);

        middlePanel.setBackground(new java.awt.Color(209, 229, 240));
        middlePanel.setForeground(new java.awt.Color(102, 102, 102));
        middlePanel.setMinimumSize(new java.awt.Dimension(840, 768));
        middlePanel.setPreferredSize(new java.awt.Dimension(840, 768));
        middlePanel.setLayout(new java.awt.CardLayout());
        middlePanel.add(knowYourClient, "kyc_card");

        newPledge.setPreferredSize(new java.awt.Dimension(760, 768));
        newPledge.addComponentListener(new java.awt.event.ComponentAdapter() {
            public void componentHidden(java.awt.event.ComponentEvent evt) {
                newPledgeComponentHidden(evt);
            }
            public void componentShown(java.awt.event.ComponentEvent evt) {
                newPledgeComponentShown(evt);
            }
        });
        middlePanel.add(newPledge, "new_pledge_card");

        renewal.addComponentListener(new java.awt.event.ComponentAdapter() {
            public void componentHidden(java.awt.event.ComponentEvent evt) {
                renewalComponentHidden(evt);
            }
            public void componentShown(java.awt.event.ComponentEvent evt) {
                renewalComponentShown(evt);
            }
        });
        middlePanel.add(renewal, "renewal_card");

        redemption.addComponentListener(new java.awt.event.ComponentAdapter() {
            public void componentHidden(java.awt.event.ComponentEvent evt) {
                redemptionComponentHidden(evt);
            }
            public void componentShown(java.awt.event.ComponentEvent evt) {
                redemptionComponentShown(evt);
            }
        });
        middlePanel.add(redemption, "redemption_card");

        search.addComponentListener(new java.awt.event.ComponentAdapter() {
            public void componentHidden(java.awt.event.ComponentEvent evt) {
                searchComponentHidden(evt);
            }
        });
        middlePanel.add(search, "search_card");

        expired.addComponentListener(new java.awt.event.ComponentAdapter() {
            public void componentHidden(java.awt.event.ComponentEvent evt) {
                expiredComponentHidden(evt);
            }
        });
        middlePanel.add(expired, "expired_listing_card");

        vaultInvPanel.addComponentListener(new java.awt.event.ComponentAdapter() {
            public void componentHidden(java.awt.event.ComponentEvent evt) {
                vaultInvPanelComponentHidden(evt);
            }
        });
        middlePanel.add(vaultInvPanel, "vault_inventory_card");

        repossessedPanel.addComponentListener(new java.awt.event.ComponentAdapter() {
            public void componentHidden(java.awt.event.ComponentEvent evt) {
                repossessedPanelComponentHidden(evt);
            }
        });
        middlePanel.add(repossessedPanel, "repossessed_card");

        statistics.addComponentListener(new java.awt.event.ComponentAdapter() {
            public void componentHidden(java.awt.event.ComponentEvent evt) {
                statisticsComponentHidden(evt);
            }
        });
        middlePanel.add(statistics, "statistics_card");

        management.addComponentListener(new java.awt.event.ComponentAdapter() {
            public void componentHidden(java.awt.event.ComponentEvent evt) {
                managementComponentHidden(evt);
            }
        });
        middlePanel.add(management, "settings_card");

        empenoRescate.addComponentListener(new java.awt.event.ComponentAdapter() {
            public void componentHidden(java.awt.event.ComponentEvent evt) {
                empenoRescateComponentHidden(evt);
            }
        });
        middlePanel.add(empenoRescate, "empeno_rescate_card");
        middlePanel.add(renewal1, "card13");

        jPanel2.add(middlePanel, java.awt.BorderLayout.CENTER);

        submenuPanel.setBackground(new java.awt.Color(239, 246, 250));
        submenuPanel.setMinimumSize(new java.awt.Dimension(210, 768));
        submenuPanel.setPreferredSize(new java.awt.Dimension(210, 768));
        submenuPanel.setLayout(new java.awt.BorderLayout());

        jPanel1.setBackground(new java.awt.Color(239, 246, 250));
        jPanel1.setMinimumSize(new java.awt.Dimension(50, 748));
        jPanel1.setOpaque(false);
        jPanel1.setPreferredSize(new java.awt.Dimension(50, 10));
        jPanel1.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jPanel4.setBackground(new java.awt.Color(239, 246, 250));
        jPanel4.setPreferredSize(new java.awt.Dimension(50, 40));

        backButtonLabel.setIcon(new javax.swing.ImageIcon(getClass().getResource("/mpis_mariadb_2/back.png"))); // NOI18N
        backButtonLabel.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                backButtonLabelMouseClicked(evt);
            }
        });
        jPanel4.add(backButtonLabel);

        jPanel1.add(jPanel4, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 90, -1, -1));

        helpIconPanel.setBackground(new java.awt.Color(239, 246, 250));
        helpIconPanel.setPreferredSize(new java.awt.Dimension(50, 40));

        jLabel15.setIcon(new javax.swing.ImageIcon(getClass().getResource("/mpis_mariadb_2/patient.png"))); // NOI18N
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

        jPanel1.add(helpIconPanel, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 140, -1, -1));

        newPledgeIconPanel.setBackground(new java.awt.Color(239, 246, 250));

        jLabel5.setIcon(new javax.swing.ImageIcon(getClass().getResource("/mpis_mariadb_2/diamond32px.png"))); // NOI18N
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

        jPanel1.add(newPledgeIconPanel, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 190, 50, 40));

        renewalIconPanel.setBackground(new java.awt.Color(239, 246, 250));
        renewalIconPanel.setPreferredSize(new java.awt.Dimension(50, 40));

        jLabel6.setIcon(new javax.swing.ImageIcon(getClass().getResource("/mpis_mariadb_2/renew32px.png"))); // NOI18N
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

        jPanel1.add(renewalIconPanel, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 240, -1, -1));

        redeemIconPanel.setBackground(new java.awt.Color(239, 246, 250));
        redeemIconPanel.setPreferredSize(new java.awt.Dimension(50, 40));

        jLabel7.setIcon(new javax.swing.ImageIcon(getClass().getResource("/mpis_mariadb_2/clipboard32px.png"))); // NOI18N
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

        jPanel1.add(redeemIconPanel, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 290, -1, -1));

        searchIconPanel.setBackground(new java.awt.Color(239, 246, 250));
        searchIconPanel.setPreferredSize(new java.awt.Dimension(50, 40));

        jLabel8.setIcon(new javax.swing.ImageIcon(getClass().getResource("/mpis_mariadb_2/search32px.png"))); // NOI18N
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

        jPanel1.add(searchIconPanel, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 340, -1, -1));

        expiredIconPanel.setBackground(new java.awt.Color(239, 246, 250));
        expiredIconPanel.setPreferredSize(new java.awt.Dimension(50, 40));

        jLabel9.setIcon(new javax.swing.ImageIcon(getClass().getResource("/mpis_mariadb_2/time32px.png"))); // NOI18N
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

        jPanel1.add(expiredIconPanel, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 390, -1, -1));

        vaultIconPanel.setBackground(new java.awt.Color(239, 246, 250));
        vaultIconPanel.setPreferredSize(new java.awt.Dimension(50, 40));

        jLabel10.setIcon(new javax.swing.ImageIcon(getClass().getResource("/mpis_mariadb_2/vault32px.png"))); // NOI18N
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

        jPanel1.add(vaultIconPanel, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 440, -1, -1));

        repoIconPanel.setBackground(new java.awt.Color(239, 246, 250));
        repoIconPanel.setPreferredSize(new java.awt.Dimension(50, 40));

        jLabel11.setIcon(new javax.swing.ImageIcon(getClass().getResource("/mpis_mariadb_2/repo32px.png"))); // NOI18N
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

        jPanel1.add(repoIconPanel, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 490, -1, -1));

        statIconPanel.setBackground(new java.awt.Color(239, 246, 250));
        statIconPanel.setPreferredSize(new java.awt.Dimension(50, 40));

        jLabel12.setIcon(new javax.swing.ImageIcon(getClass().getResource("/mpis_mariadb_2/line 32px.png"))); // NOI18N
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

        jPanel1.add(statIconPanel, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 540, -1, -1));

        empenoIconPanel.setBackground(new java.awt.Color(239, 246, 250));
        empenoIconPanel.setPreferredSize(new java.awt.Dimension(50, 40));

        jLabel13.setIcon(new javax.swing.ImageIcon(getClass().getResource("/mpis_mariadb_2/books32px.png"))); // NOI18N
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

        jPanel1.add(empenoIconPanel, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 590, -1, -1));

        settingIconPanel.setBackground(new java.awt.Color(239, 246, 250));
        settingIconPanel.setPreferredSize(new java.awt.Dimension(50, 40));

        jLabel14.setIcon(new javax.swing.ImageIcon(getClass().getResource("/mpis_mariadb_2/setting32px.png"))); // NOI18N
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

        jPanel1.add(settingIconPanel, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 660, -1, -1));

        logoutIconPanel.setBackground(new java.awt.Color(239, 246, 250));
        logoutIconPanel.setPreferredSize(new java.awt.Dimension(50, 40));

        jLabel2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/mpis_mariadb_2/key32px.png"))); // NOI18N
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

        jPanel1.add(logoutIconPanel, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 710, -1, -1));

        homeIconPanel.setBackground(new java.awt.Color(239, 246, 250));
        homeIconPanel.setPreferredSize(new java.awt.Dimension(50, 40));

        jLabel1.setFont(new java.awt.Font("Segoe UI Semibold", 0, 12)); // NOI18N
        jLabel1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/mpis_mariadb_2/merlin logo 32px.png"))); // NOI18N
        homeIconPanel.add(jLabel1);

        jPanel1.add(homeIconPanel, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 6, -1, -1));

        submenuPanel.add(jPanel1, java.awt.BorderLayout.LINE_START);

        jPanel3.setBackground(new java.awt.Color(239, 246, 250));
        jPanel3.setMinimumSize(new java.awt.Dimension(160, 768));
        jPanel3.setOpaque(false);
        jPanel3.setPreferredSize(new java.awt.Dimension(160, 768));
        jPanel3.setRequestFocusEnabled(false);
        jPanel3.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel42.setFont(new java.awt.Font("Segoe UI Semibold", 1, 12)); // NOI18N
        jLabel42.setText("MERLIN PAWNSHOP, INC.");
        jPanel3.add(jLabel42, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 10, 150, 20));

        jLabel43.setFont(new java.awt.Font("Segoe UI Semibold", 1, 12)); // NOI18N
        jLabel43.setText("PAWN MANAGEMENT");
        jPanel3.add(jLabel43, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 30, 150, 20));

        jLabel44.setFont(new java.awt.Font("Segoe UI Semibold", 1, 12)); // NOI18N
        jLabel44.setText("SYSTEM ");
        jPanel3.add(jLabel44, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 50, 150, 20));

        newPledgePanel.setBackground(new java.awt.Color(239, 246, 250));
        newPledgePanel.setLayout(new java.awt.BorderLayout());

        newpledgelabel.setFont(new java.awt.Font("Segoe UI Semilight", 0, 12)); // NOI18N
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

        jPanel3.add(newPledgePanel, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 190, 160, 40));

        renewalPanel.setBackground(new java.awt.Color(239, 246, 250));
        renewalPanel.setPreferredSize(new java.awt.Dimension(160, 40));
        renewalPanel.setLayout(new java.awt.BorderLayout());

        renewallabel.setFont(new java.awt.Font("Segoe UI Semilight", 0, 12)); // NOI18N
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

        jPanel3.add(renewalPanel, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 240, -1, -1));

        redeemPanel.setBackground(new java.awt.Color(239, 246, 250));
        redeemPanel.setPreferredSize(new java.awt.Dimension(160, 40));
        redeemPanel.setLayout(new java.awt.BorderLayout());

        redeemlabel.setFont(new java.awt.Font("Segoe UI Semilight", 0, 12)); // NOI18N
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

        jPanel3.add(redeemPanel, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 290, -1, -1));

        searchPanel.setBackground(new java.awt.Color(239, 246, 250));
        searchPanel.setPreferredSize(new java.awt.Dimension(160, 40));
        searchPanel.setLayout(new java.awt.BorderLayout());

        searchlabel.setFont(new java.awt.Font("Segoe UI Semilight", 0, 12)); // NOI18N
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

        jPanel3.add(searchPanel, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 340, -1, -1));

        expiredPanel.setBackground(new java.awt.Color(239, 246, 250));
        expiredPanel.setPreferredSize(new java.awt.Dimension(160, 40));
        expiredPanel.setLayout(new java.awt.BorderLayout());

        expiredlabel.setFont(new java.awt.Font("Segoe UI Semilight", 0, 12)); // NOI18N
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

        jPanel3.add(expiredPanel, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 390, -1, -1));

        vaultPanel.setBackground(new java.awt.Color(239, 246, 250));
        vaultPanel.setPreferredSize(new java.awt.Dimension(160, 40));
        vaultPanel.setLayout(new java.awt.BorderLayout());

        vaultlabel.setFont(new java.awt.Font("Segoe UI Semilight", 0, 12)); // NOI18N
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

        jPanel3.add(vaultPanel, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 440, -1, -1));

        repoPanel.setBackground(new java.awt.Color(239, 246, 250));
        repoPanel.setPreferredSize(new java.awt.Dimension(160, 40));
        repoPanel.setLayout(new java.awt.BorderLayout());

        repolabel.setFont(new java.awt.Font("Segoe UI Semilight", 0, 12)); // NOI18N
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

        jPanel3.add(repoPanel, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 490, -1, -1));

        statPanel.setBackground(new java.awt.Color(239, 246, 250));
        statPanel.setPreferredSize(new java.awt.Dimension(160, 40));
        statPanel.setLayout(new java.awt.BorderLayout());

        statlabel.setFont(new java.awt.Font("Segoe UI Semilight", 0, 12)); // NOI18N
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

        jPanel3.add(statPanel, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 540, -1, -1));

        empenoPanel.setBackground(new java.awt.Color(239, 246, 250));
        empenoPanel.setPreferredSize(new java.awt.Dimension(160, 40));
        empenoPanel.setLayout(new java.awt.BorderLayout());

        empenolabel.setFont(new java.awt.Font("Segoe UI Semilight", 0, 12)); // NOI18N
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

        jPanel3.add(empenoPanel, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 590, -1, -1));

        settingPanel.setBackground(new java.awt.Color(239, 246, 250));
        settingPanel.setPreferredSize(new java.awt.Dimension(160, 40));
        settingPanel.setLayout(new java.awt.BorderLayout());

        settinglabel.setFont(new java.awt.Font("Segoe UI Semilight", 0, 12)); // NOI18N
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

        jPanel3.add(settingPanel, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 660, -1, -1));

        helpPanel.setBackground(new java.awt.Color(239, 246, 250));
        helpPanel.setPreferredSize(new java.awt.Dimension(160, 40));
        helpPanel.setLayout(new java.awt.BorderLayout());

        helplabel.setFont(new java.awt.Font("Segoe UI Semilight", 0, 12)); // NOI18N
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

        jPanel3.add(helpPanel, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 140, -1, -1));

        logoutPanel.setBackground(new java.awt.Color(239, 246, 250));
        logoutPanel.setPreferredSize(new java.awt.Dimension(160, 40));
        logoutPanel.setLayout(new java.awt.BorderLayout());

        logoutlabel.setFont(new java.awt.Font("Segoe UI Semilight", 0, 12)); // NOI18N
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

        jPanel3.add(logoutPanel, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 710, -1, -1));

        submenuPanel.add(jPanel3, java.awt.BorderLayout.CENTER);

        jPanel2.add(submenuPanel, java.awt.BorderLayout.WEST);

        add(jPanel2, "card2");
    }// </editor-fold>//GEN-END:initComponents

    private void newPledgeComponentHidden(java.awt.event.ComponentEvent evt) {//GEN-FIRST:event_newPledgeComponentHidden
//        CardLayout cl = (CardLayout)this.leftPanel.getLayout();
//        cl.show(this.leftPanel, "menu_card");
//        this.transaction.refreshTable();
    }//GEN-LAST:event_newPledgeComponentHidden

    private void renewalComponentHidden(java.awt.event.ComponentEvent evt) {//GEN-FIRST:event_renewalComponentHidden
//        CardLayout cl = (CardLayout)this.leftPanel.getLayout();
//        cl.show(this.leftPanel, "menu_card");
//        this.transaction.refreshTable();
    }//GEN-LAST:event_renewalComponentHidden

    private void renewalComponentShown(java.awt.event.ComponentEvent evt) {//GEN-FIRST:event_renewalComponentShown
        this.renewal.requestFocusInSearch();
        this.transaction.refreshTable();
    }//GEN-LAST:event_renewalComponentShown

    private void redemptionComponentHidden(java.awt.event.ComponentEvent evt) {//GEN-FIRST:event_redemptionComponentHidden
//        CardLayout cl = (CardLayout)this.leftPanel.getLayout();
//        cl.show(this.leftPanel, "menu_card");
//        this.transaction.refreshTable();
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
            cl.show(this.middlePanel, "new_pledge_card");
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
///* 312 */   CardLayout cl = (CardLayout)this.leftPanel.getLayout();  
//            cl.show(this.leftPanel, "menu_card");
///* 313 */     this.transaction.refreshTable();
    }//GEN-LAST:event_expiredComponentHidden

    private void empenoRescateComponentHidden(java.awt.event.ComponentEvent evt) {//GEN-FIRST:event_empenoRescateComponentHidden
//        CardLayout cl = (CardLayout)this.leftPanel.getLayout();
//        cl.show(this.leftPanel, "menu_card");
//        this.transaction.refreshTable();
    }//GEN-LAST:event_empenoRescateComponentHidden

    private void repossessedPanelComponentHidden(java.awt.event.ComponentEvent evt) {//GEN-FIRST:event_repossessedPanelComponentHidden
//        CardLayout cl = (CardLayout)this.leftPanel.getLayout();
//        cl.show(this.leftPanel, "menu_card");
//        this.transaction.refreshTable();
    }//GEN-LAST:event_repossessedPanelComponentHidden

    private void vaultInvPanelComponentHidden(java.awt.event.ComponentEvent evt) {//GEN-FIRST:event_vaultInvPanelComponentHidden
//        CardLayout cl = (CardLayout)this.leftPanel.getLayout();
//        cl.show(this.leftPanel, "menu_card");
//        this.transaction.refreshTable();
    }//GEN-LAST:event_vaultInvPanelComponentHidden

    private void managementComponentHidden(java.awt.event.ComponentEvent evt) {//GEN-FIRST:event_managementComponentHidden
//        CardLayout cl = (CardLayout)this.leftPanel.getLayout();
//        cl.show(this.leftPanel, "menu_card");
    }//GEN-LAST:event_managementComponentHidden

    private void statisticsComponentHidden(java.awt.event.ComponentEvent evt) {//GEN-FIRST:event_statisticsComponentHidden
//        CardLayout cl = (CardLayout)this.leftPanel.getLayout();
//        cl.show(this.leftPanel, "menu_card");
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
        // TODO add your handling code here:
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
        changeColor(logoutPanel, menuBgColor);
        changeColor(logoutIconPanel, menuBgColor);
    }//GEN-LAST:event_logoutlabelMouseExited

    private void logoutlabelMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_logoutlabelMouseEntered
        changeColor(logoutPanel, highlightHover);
        changeColor(logoutIconPanel, highlightHover);
    }//GEN-LAST:event_logoutlabelMouseEntered

    private void logoutlabelMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_logoutlabelMouseClicked
        changeColor(logoutPanel, clickedColor);
        changeColor(logoutIconPanel, clickedColor);
        showCard(99);
    }//GEN-LAST:event_logoutlabelMouseClicked

    private void helplabelMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_helplabelMouseExited
        changeColor(helpPanel, menuBgColor);
        changeColor(helpIconPanel, menuBgColor);
    }//GEN-LAST:event_helplabelMouseExited

    private void helplabelMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_helplabelMouseEntered
        changeColor(helpPanel, highlightHover);
        changeColor(helpIconPanel, highlightHover);
    }//GEN-LAST:event_helplabelMouseEntered

    private void helplabelMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_helplabelMouseClicked
        changeColor(helpPanel, clickedColor);
        changeColor(helpIconPanel, clickedColor);
        showCard(11);
    }//GEN-LAST:event_helplabelMouseClicked

    private void empenolabelMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_empenolabelMouseExited
        changeColor(empenoPanel, menuBgColor);
        changeColor(empenoIconPanel, menuBgColor);
    }//GEN-LAST:event_empenolabelMouseExited

    private void empenolabelMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_empenolabelMouseEntered
        changeColor(empenoPanel, highlightHover);
        changeColor(empenoIconPanel, highlightHover);
    }//GEN-LAST:event_empenolabelMouseEntered

    private void empenolabelMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_empenolabelMouseClicked
        changeColor(empenoPanel, clickedColor);
        changeColor(empenoIconPanel, clickedColor);
        showCard(10);
    }//GEN-LAST:event_empenolabelMouseClicked

    private void settinglabelMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_settinglabelMouseExited
        changeColor(settingPanel, menuBgColor);
        changeColor(settingIconPanel, menuBgColor);
    }//GEN-LAST:event_settinglabelMouseExited

    private void settinglabelMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_settinglabelMouseEntered
        changeColor(settingPanel, highlightHover);
        changeColor(settingIconPanel, highlightHover);
    }//GEN-LAST:event_settinglabelMouseEntered

    private void settinglabelMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_settinglabelMouseClicked
        changeColor(settingPanel, clickedColor);
        changeColor(settingIconPanel, clickedColor);
        showCard(9);
    }//GEN-LAST:event_settinglabelMouseClicked

    private void statlabelMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_statlabelMouseExited
        changeColor(statPanel, menuBgColor);
        changeColor(statIconPanel, menuBgColor);
    }//GEN-LAST:event_statlabelMouseExited

    private void statlabelMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_statlabelMouseEntered
        changeColor(statPanel, highlightHover);
        changeColor(statIconPanel, highlightHover);
    }//GEN-LAST:event_statlabelMouseEntered

    private void statlabelMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_statlabelMouseClicked
        changeColor(statPanel, clickedColor);
        changeColor(statIconPanel, clickedColor);
        showCard(8);
    }//GEN-LAST:event_statlabelMouseClicked

    private void repolabelMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_repolabelMouseExited
        changeColor(repoPanel, menuBgColor);
        changeColor(repoIconPanel, menuBgColor);
    }//GEN-LAST:event_repolabelMouseExited

    private void repolabelMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_repolabelMouseEntered
        changeColor(repoPanel, highlightHover);
        changeColor(repoIconPanel, highlightHover);
    }//GEN-LAST:event_repolabelMouseEntered

    private void repolabelMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_repolabelMouseClicked
        changeColor(repoPanel, clickedColor);
        changeColor(repoIconPanel, clickedColor);
        showCard(7);
    }//GEN-LAST:event_repolabelMouseClicked

    private void vaultlabelMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_vaultlabelMouseExited
        changeColor(vaultPanel, menuBgColor);
        changeColor(vaultIconPanel, menuBgColor);
    }//GEN-LAST:event_vaultlabelMouseExited

    private void vaultlabelMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_vaultlabelMouseEntered
        changeColor(vaultPanel, highlightHover);
        changeColor(vaultIconPanel, highlightHover);
    }//GEN-LAST:event_vaultlabelMouseEntered

    private void vaultlabelMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_vaultlabelMouseClicked
        changeColor(vaultPanel, clickedColor);
        changeColor(vaultIconPanel, clickedColor);
        showCard(6);
    }//GEN-LAST:event_vaultlabelMouseClicked

    private void expiredlabelMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_expiredlabelMouseExited
        changeColor(expiredPanel, menuBgColor);
        changeColor(expiredIconPanel, menuBgColor);
    }//GEN-LAST:event_expiredlabelMouseExited

    private void expiredlabelMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_expiredlabelMouseEntered
        changeColor(expiredPanel, highlightHover);
        changeColor(expiredIconPanel, highlightHover);
    }//GEN-LAST:event_expiredlabelMouseEntered

    private void expiredlabelMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_expiredlabelMouseClicked
        changeColor(expiredPanel, clickedColor);
        changeColor(expiredIconPanel, clickedColor);
        showCard(5);
    }//GEN-LAST:event_expiredlabelMouseClicked

    private void searchlabelMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_searchlabelMouseExited
        changeColor(searchPanel, menuBgColor);
        changeColor(searchIconPanel, menuBgColor);
    }//GEN-LAST:event_searchlabelMouseExited

    private void searchlabelMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_searchlabelMouseEntered
        changeColor(searchPanel, highlightHover);
        changeColor(searchIconPanel, highlightHover);
    }//GEN-LAST:event_searchlabelMouseEntered

    private void searchlabelMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_searchlabelMouseClicked
        changeColor(searchPanel, clickedColor);
        changeColor(searchIconPanel, clickedColor);
        showCard(4);
    }//GEN-LAST:event_searchlabelMouseClicked

    private void redeemlabelMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_redeemlabelMouseExited
        changeColor(redeemPanel, menuBgColor);
        changeColor(redeemIconPanel, menuBgColor);
    }//GEN-LAST:event_redeemlabelMouseExited

    private void redeemlabelMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_redeemlabelMouseEntered
        changeColor(redeemPanel, highlightHover);
        changeColor(redeemIconPanel, highlightHover);
    }//GEN-LAST:event_redeemlabelMouseEntered

    private void redeemlabelMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_redeemlabelMouseClicked
        changeColor(redeemPanel, clickedColor);
        changeColor(redeemIconPanel, clickedColor);
        showCard(3);
    }//GEN-LAST:event_redeemlabelMouseClicked

    private void renewallabelMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_renewallabelMouseExited
        changeColor(renewalPanel, menuBgColor);
        changeColor(renewalIconPanel, menuBgColor);
    }//GEN-LAST:event_renewallabelMouseExited

    private void renewallabelMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_renewallabelMouseEntered
        changeColor(renewalPanel, highlightHover);
        changeColor(renewalIconPanel, highlightHover);
    }//GEN-LAST:event_renewallabelMouseEntered

    private void renewallabelMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_renewallabelMouseClicked
        changeColor(renewalPanel, clickedColor);
        changeColor(renewalIconPanel, clickedColor);
        showCard(2);
    }//GEN-LAST:event_renewallabelMouseClicked

    private void newpledgelabelMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_newpledgelabelMouseExited
        changeColor(newPledgePanel, menuBgColor);
        changeColor(newPledgeIconPanel, menuBgColor);
    }//GEN-LAST:event_newpledgelabelMouseExited

    private void newpledgelabelMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_newpledgelabelMouseEntered
        changeColor(newPledgePanel, highlightHover);
        changeColor(newPledgeIconPanel, highlightHover);
    }//GEN-LAST:event_newpledgelabelMouseEntered

    private void newpledgelabelMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_newpledgelabelMouseClicked
        changeColor(newPledgePanel, clickedColor);
        changeColor(newPledgeIconPanel, clickedColor);
        showCard(1);
    }//GEN-LAST:event_newpledgelabelMouseClicked

    private void jLabel5MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel5MouseClicked
        newpledgelabelMouseClicked(evt);
    }//GEN-LAST:event_jLabel5MouseClicked


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel backButtonLabel;
    private javax.swing.JPanel empenoIconPanel;
    private javax.swing.JPanel empenoPanel;
    private mpis_mariadb_2.EmpenoRescate empenoRescate;
    private javax.swing.JLabel empenolabel;
    private mpis_mariadb_2.Expired expired;
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
    private javax.swing.JLabel jLabel42;
    private javax.swing.JLabel jLabel43;
    private javax.swing.JLabel jLabel44;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private mpis_mariadb_2.KnowYourClient knowYourClient;
    private javax.swing.JPanel logoutIconPanel;
    private javax.swing.JPanel logoutPanel;
    private javax.swing.JLabel logoutlabel;
    private mpis_mariadb_2.Management management;
    private javax.swing.JPanel middlePanel;
    private mpis_mariadb_2.NewPledge newPledge;
    private javax.swing.JPanel newPledgeIconPanel;
    private javax.swing.JPanel newPledgePanel;
    private javax.swing.JLabel newpledgelabel;
    private javax.swing.JPanel redeemIconPanel;
    private javax.swing.JPanel redeemPanel;
    private javax.swing.JLabel redeemlabel;
    private mpis_mariadb_2.Redemption redemption;
    private mpis_mariadb_2.Renewal renewal;
    private mpis_mariadb_2.Renewal renewal1;
    private javax.swing.JPanel renewalIconPanel;
    private javax.swing.JPanel renewalPanel;
    private javax.swing.JLabel renewallabel;
    private javax.swing.JPanel repoIconPanel;
    private javax.swing.JPanel repoPanel;
    private javax.swing.JLabel repolabel;
    private mpis_mariadb_2.RepossessedPanel repossessedPanel;
    private mpis_mariadb_2.Search search;
    private javax.swing.JPanel searchIconPanel;
    private javax.swing.JPanel searchPanel;
    private javax.swing.JLabel searchlabel;
    private javax.swing.JPanel settingIconPanel;
    private javax.swing.JPanel settingPanel;
    private javax.swing.JLabel settinglabel;
    private javax.swing.JPanel statIconPanel;
    private javax.swing.JPanel statPanel;
    private mpis_mariadb_2.Statistics statistics;
    private javax.swing.JLabel statlabel;
    private javax.swing.JPanel submenuPanel;
    private mpis_mariadb_2.Transaction transaction;
    private javax.swing.JPanel vaultIconPanel;
    private mpis_mariadb_2.VaultInvPanel vaultInvPanel;
    private javax.swing.JPanel vaultPanel;
    private javax.swing.JLabel vaultlabel;
    // End of variables declaration//GEN-END:variables
}
