package com.merlin;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRExporterParameter;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperPrintManager;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.design.JRDesignQuery;
import net.sf.jasperreports.engine.design.JasperDesign;
import net.sf.jasperreports.engine.export.JRXlsExporter;
import net.sf.jasperreports.engine.export.JRXlsExporterParameter;
import net.sf.jasperreports.engine.xml.JRXmlLoader;
import org.jfree.data.statistics.Statistics;

public class ReportPrinter {

    private final Config con = new Config();
    private final String driver = "jdbc:mariadb://" + this.con.getProp("IP") + ":" + this.con.getProp("port") + "/merlininventorydatabase";
    private final String f_user = this.con.getProp("username");
    private final String f_pass = this.con.getProp("password");

    private String destination = "";
    private String report_name = "";
    private String jrxml_name = "";

    public void printReport(String filePath, Map parameters) {
        try {
            OutputStream output = null;
            Connection conn = DriverManager.getConnection(this.driver, this.f_user, this.f_pass);
         try {
             JasperDesign jasperDesign = JRXmlLoader.load(getClass().getResourceAsStream(filePath));
             JasperReport jasperReport = JasperCompileManager.compileReport(jasperDesign);
             JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parameters, conn);
             JasperPrintManager.printReport(jasperPrint, false);
//             output = new FileOutputStream(new File(getDestination()));
//             JasperExportManager.exportReportToPdfStream(jasperPrint, output);
         } catch (JRException ex) {
             this.con.saveProp("mpis_last_error", String.valueOf(ex));
             JOptionPane.showMessageDialog(null, "An error occured while printing your " + getReport_name() + " \n " + ex, getReport_name(), 0);
//         } catch (FileNotFoundException ex) {
//             this.con.saveProp("mpis_last_error", String.valueOf(ex));
//             JOptionPane.showMessageDialog(null, "An error occured while printing your " + getReport_name() + " \n " + ex, getReport_name(), 0);
//             Logger.getLogger(TransferBreakdown.class.getName()).log(Level.SEVERE, (String) null, ex);
         }
        } catch (SQLException ex) {
            this.con.saveProp("mpis_last_error", String.valueOf(ex));
            JOptionPane.showMessageDialog(null, "An error occured while printing your " + getReport_name() + " \n " + ex, getReport_name(), 0);
            Logger.getLogger(TransferBreakdown.class.getName()).log(Level.SEVERE, (String) null, ex);
         }
     }

     public void printReport(String filePath, String query) {
         try {
/*  78 */       OutputStream output = null;
/*  79 */       Connection conn = DriverManager.getConnection(this.driver, this.f_user, this.f_pass);
         try {
/*  81 */         JasperDesign jasperDesign = JRXmlLoader.load(getClass().getResourceAsStream(filePath));
/*  82 */         JRDesignQuery newQuery = new JRDesignQuery();
/*  83 */         newQuery.setText(query);
/*  84 */         System.out.println(newQuery);
/*  85 */         jasperDesign.setQuery(newQuery);
/*  86 */         JasperReport jasperReport = JasperCompileManager.compileReport(jasperDesign);
/*  87 */         JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, null, conn);
/*  88 */         JasperPrintManager.printReport(jasperPrint, false);
/*  89 */         output = new FileOutputStream(new File(getDestination()));
/*  90 */         JasperExportManager.exportReportToPdfStream(jasperPrint, output);
/*  91 */       } catch (JRException|FileNotFoundException ex) {
/*  92 */         this.con.saveProp("mpis_last_error", String.valueOf(ex));
/*  93 */         JOptionPane.showMessageDialog(null, "An error occured while printing your " + getReport_name() + " \n " + ex, getReport_name(), 0);
         }

/*  96 */     } catch (SQLException ex) {
/*  97 */       JOptionPane.showMessageDialog(null, "An error occured while printing your " + getReport_name() + " \n " + ex, getReport_name(), 0);
/*  98 */       Logger.getLogger(TransferBreakdown.class.getName()).log(Level.SEVERE, (String)null, ex);
         }
     }

     public void printReport(String filePath, Map parameters, String query) {
         try {
/* 104 */       OutputStream output = null;
/* 105 */       Connection conn = DriverManager.getConnection(this.driver, this.f_user, this.f_pass);
         try {
/* 107 */         JasperDesign jasperDesign = JRXmlLoader.load(getClass().getResourceAsStream(filePath));
/* 108 */         JRDesignQuery newQuery = new JRDesignQuery();
/* 109 */         newQuery.setText(query);
/* 110 */         System.out.println(newQuery);
/* 111 */         jasperDesign.setQuery(newQuery);
/* 112 */         JasperReport jasperReport = JasperCompileManager.compileReport(jasperDesign);
/* 113 */         JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parameters, conn);
/* 114 */         JasperPrintManager.printReport(jasperPrint, false);
/* 115 */         output = new FileOutputStream(new File(getDestination()));
/* 116 */         JasperExportManager.exportReportToPdfStream(jasperPrint, output);
/* 117 */       } catch (JRException|FileNotFoundException ex) {
/* 118 */         this.con.saveProp("mpis_last_error", String.valueOf(ex));
/* 119 */         JOptionPane.showMessageDialog(null, "An error occured while printing your " + getReport_name() + " \n " + ex, getReport_name(), 0);
         }

/* 122 */     } catch (SQLException ex) {
/* 123 */       this.con.saveProp("mpis_last_error", String.valueOf(ex));
/* 124 */       JOptionPane.showMessageDialog(null, "An error occured while printing your " + getReport_name() + " \n " + ex, getReport_name(), 0);
/* 125 */       Logger.getLogger(TransferBreakdown.class.getName()).log(Level.SEVERE, (String)null, ex);
        }
    }

    public void printDotMatrix(String filePath, Map parameters) {
        OutputStream output = null;
        try {
            Connection conn = DriverManager.getConnection(this.driver, this.f_user, this.f_pass);
            JasperDesign jasperDesign = JRXmlLoader.load(getClass().getResourceAsStream(filePath));
            JRXlsExporter exporter = new JRXlsExporter();
            JasperReport jasperReport = JasperCompileManager.compileReport(jasperDesign);

            JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parameters, conn);
            exporter.setParameter(JRExporterParameter.JASPER_PRINT, jasperPrint);

            output = new FileOutputStream(new File(this.destination));
            exporter.setParameter(JRExporterParameter.OUTPUT_STREAM, output);
            exporter.setParameter((JRExporterParameter) JRXlsExporterParameter.IS_ONE_PAGE_PER_SHEET, Boolean.FALSE);
            exporter.setParameter((JRExporterParameter) JRXlsExporterParameter.IS_REMOVE_EMPTY_SPACE_BETWEEN_ROWS, Boolean.TRUE);
            exporter.setParameter((JRExporterParameter) JRXlsExporterParameter.IS_WHITE_PAGE_BACKGROUND, Boolean.FALSE);
            exporter.exportReport();
            PrintExcel px = new PrintExcel();
            px.printFile(new File(this.destination));
        } catch (FileNotFoundException | JRException | SQLException ex) {
            this.con.saveProp("mpis_last_error", String.valueOf(ex));
            Logger.getLogger(TransferBreakdown.class.getName()).log(Level.SEVERE, (String) null, ex);
            JOptionPane.showMessageDialog(null, "An error occured while printing your " + getReport_name() + " \n " + ex, getReport_name(), 0);
            Logger.getLogger(Statistics.class.getName()).log(Level.SEVERE, (String) null, ex);
        } finally {
            try {
                if (output != null) {
                    output.close();
                }
            } catch (IOException ex) {
                this.con.saveProp("mpis_last_error", String.valueOf(ex));
                JOptionPane.showMessageDialog(null, "An error occured while printing your " + getReport_name(), getReport_name(), 0);
                Logger.getLogger(Statistics.class.getName()).log(Level.SEVERE, (String) null, ex);
            }
        }
    }

    public void printDotMatrix(String filePath, Map parameters, String query) {
        OutputStream output = null;
        try {
            Connection conn = DriverManager.getConnection(this.driver, this.f_user, this.f_pass);
            JasperDesign jasperDesign = JRXmlLoader.load(getClass().getResourceAsStream(filePath));
            JRDesignQuery newQuery = new JRDesignQuery();
            newQuery.setText(query);
            jasperDesign.setQuery(newQuery);
            JRXlsExporter exporter = new JRXlsExporter();
            JasperReport jasperReport = JasperCompileManager.compileReport(jasperDesign);
            JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parameters, conn);
            exporter.setParameter(JRExporterParameter.JASPER_PRINT, jasperPrint);

            output = new FileOutputStream(new File(this.destination));
            exporter.setParameter(JRExporterParameter.OUTPUT_STREAM, output);
            exporter.setParameter((JRExporterParameter) JRXlsExporterParameter.IS_ONE_PAGE_PER_SHEET, Boolean.FALSE);
            exporter.setParameter((JRExporterParameter) JRXlsExporterParameter.IS_REMOVE_EMPTY_SPACE_BETWEEN_ROWS, Boolean.TRUE);
            exporter.setParameter((JRExporterParameter) JRXlsExporterParameter.IS_WHITE_PAGE_BACKGROUND, Boolean.FALSE);
            exporter.exportReport();
            PrintExcel px = new PrintExcel();
            px.printFile(new File(this.destination));
        } catch (FileNotFoundException | JRException | SQLException ex) {
            this.con.saveProp("mpis_last_error", String.valueOf(ex));
            JOptionPane.showMessageDialog(null, "An error occured while printing your " + getReport_name(), getReport_name(), 0);
            Logger.getLogger(Statistics.class.getName()).log(Level.SEVERE, (String) null, ex);
        } finally {
            try {
                if (output != null) {
                    output.close();
                }
            } catch (IOException ex) {
                this.con.saveProp("mpis_last_error", String.valueOf(ex));
                JOptionPane.showMessageDialog(null, "An error occured while printing your " + getReport_name(), getReport_name(), 0);
                Logger.getLogger(Statistics.class.getName()).log(Level.SEVERE, (String) null, ex);
            }
        }
    }

    public void printDotMatrix(String filePath, String query) {
        OutputStream output = null;
        try {
            Connection conn = DriverManager.getConnection(this.driver, this.f_user, this.f_pass);
            JasperDesign jasperDesign = JRXmlLoader.load(getClass().getResourceAsStream(filePath));
            JRDesignQuery newQuery = new JRDesignQuery();
            newQuery.setText(query);
            jasperDesign.setQuery(newQuery);
            JRXlsExporter exporter = new JRXlsExporter();
            JasperReport jasperReport = JasperCompileManager.compileReport(jasperDesign);
            JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, null, conn);
            exporter.setParameter(JRExporterParameter.JASPER_PRINT, jasperPrint);

            output = new FileOutputStream(new File(this.destination));
            exporter.setParameter(JRExporterParameter.OUTPUT_STREAM, output);
            exporter.setParameter((JRExporterParameter) JRXlsExporterParameter.IS_ONE_PAGE_PER_SHEET, Boolean.FALSE);
            exporter.setParameter((JRExporterParameter) JRXlsExporterParameter.IS_REMOVE_EMPTY_SPACE_BETWEEN_ROWS, Boolean.TRUE);
            exporter.setParameter((JRExporterParameter) JRXlsExporterParameter.IS_WHITE_PAGE_BACKGROUND, Boolean.FALSE);
            exporter.exportReport();
            PrintExcel px = new PrintExcel();
            px.printFile(new File(this.destination));
        } catch (FileNotFoundException | JRException | SQLException ex) {
            this.con.saveProp("mpis_last_error", String.valueOf(ex));
            JOptionPane.showMessageDialog(null, "An error occured while printing your " + getReport_name(), getReport_name(), 0);
            Logger.getLogger(Statistics.class.getName()).log(Level.SEVERE, (String) null, ex);
        } finally {
            try {
                if (output != null) {
                    output.close();
                }
            } catch (IOException ex) {
                this.con.saveProp("mpis_last_error", String.valueOf(ex));
                JOptionPane.showMessageDialog(null, "An error occured while printing your " + getReport_name(), getReport_name(), 0);
                Logger.getLogger(Statistics.class.getName()).log(Level.SEVERE, (String) null, ex);
            }
        }
    }

    public String getDestination() {
        return this.destination;
    }

    public void setDestination(String destination) {
        this.destination = destination;
    }

    public String getReport_name() {
        return this.report_name;
    }

    public void setReport_name(String report_name) {
        this.report_name = report_name;
    }

    public String getJrxml_name() {
        return this.jrxml_name;
    }

    public void setJrxml_name(String jrxml_name) {
        this.jrxml_name = jrxml_name;
    }
}

