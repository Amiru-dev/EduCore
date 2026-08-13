package lk.corevision.fruitshop.gui;

import com.formdev.flatlaf.FlatClientProperties;
import com.toedter.calendar.JDateChooser;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Image;
import java.awt.Toolkit;
import java.io.IOException;
import java.io.InputStream;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.SwingConstants;
import javax.swing.table.DefaultTableCellRenderer;
import raven.toast.Notifications;
import java.awt.datatransfer.*;
import java.awt.event.MouseEvent;
import java.sql.Timestamp;
import java.sql.ResultSet;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Vector;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JDialog;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;
import lk.corevision.fruitshop.model.MySQL;
import java.util.regex.Pattern;
import javax.swing.JColorChooser;
import java.text.DecimalFormat;
import java.util.Calendar;
import javax.swing.JFormattedTextField;
import javax.swing.JSpinner;
import javax.swing.SpinnerDateModel;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.data.JRTableModelDataSource;
import net.sf.jasperreports.view.JasperViewer;
import java.sql.SQLException;
import javaswingdev.chart.ModelPieChart;
import raven.chart.ModelChart;
import lk.corevision.fruitshop.charts.CurveLineChart;
import lk.corevision.fruitshop.charts.PieChart;
import lk.corevision.fruitshop.email.SendEmail;
import lk.corevision.fruitshop.validate.JOptionIcon;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import net.sf.jasperreports.engine.JRException;
import java.util.logging.Level;

/**
 *
 * @author Amiru Upek
 */
public class Product extends javax.swing.JPanel {

    private final Dashboard dashboard;
    private PieChart pieChart1;
    private CurveLineChart chart;
    private JFormattedTextField tf;
    private JFormattedTextField tf1;
    private boolean isResetting = false;
    private int totalTasksCompleted = 0;
    private double avgDuration;
    private int row1; // jTable4
    public static DefaultTableCellRenderer renderer;
    public static Color foreground = Color.BLACK;
    public static Color background = Color.WHITE;
    private static final HashMap<String, String> fruitMap = new HashMap<>();
    private static final HashMap<String, String> yieldMap = new HashMap<>();
    private static final HashMap<String, String> taskMap = new HashMap<>();
    private static final HashMap<String, String> taskStatusMap = new HashMap<>();
    private static final HashMap<String, String> issueTypeMap = new HashMap<>();

    /**
     * Creates new form Product
     *
     * @param dashboard
     */
    public Product(Dashboard dashboard) {
        this.dashboard = dashboard;
        initComponents();
        init();
    }

    public void loadFruits() {
        try {

            ResultSet resultSet = MySQL.executeSearch("SELECT * FROM `fruit`");
            Vector<String> vector = new Vector<>();
            vector.add("Select");

            while (resultSet.next()) {
                vector.add(resultSet.getString("fruit_name"));
                fruitMap.put(resultSet.getString("fruit_name"), resultSet.getString("fruit_id"));
            }

            DefaultComboBoxModel model = new DefaultComboBoxModel(vector);
            jComboBox1.setModel(model);

        } catch (Exception e) {
            SignIn.getLoggerObject().log(Level.WARNING, "loadFruits method Exception found!", e);
        }
    }

    public void loadYield() {
        try {

            ResultSet resultSet = MySQL.executeSearch("SELECT * FROM `yield_location`");
            Vector<String> vector = new Vector<>();
            vector.add("Select");

            while (resultSet.next()) {
                vector.add(resultSet.getString("yield_location"));
                yieldMap.put(resultSet.getString("yield_location"), resultSet.getString("yield_location_id"));
            }

            DefaultComboBoxModel model = new DefaultComboBoxModel(vector);
            jComboBox5.setModel(model);

        } catch (Exception e) {
            SignIn.getLoggerObject().log(Level.WARNING, "loadYield method Exception found!", e);
        }
    }
    
    public void loadTasks() {
        try {

            ResultSet resultSet = MySQL.executeSearch("SELECT * FROM `task_name`");
            Vector<String> vector = new Vector<>();
            vector.add("Select");

            while (resultSet.next()) {
                vector.add(resultSet.getString("task_name"));
                taskMap.put(resultSet.getString("task_name"), resultSet.getString("task_name_id"));
            }

            DefaultComboBoxModel model = new DefaultComboBoxModel(vector);
            jComboBox3.setModel(model);

        } catch (Exception e) {
            SignIn.getLoggerObject().log(Level.WARNING, "loadTasks method Exception found!", e);
        }
    }

    public void loadTaskStatus() {
        try {

            ResultSet resultSet = MySQL.executeSearch("SELECT * FROM `task_status`");
            Vector<String> vector = new Vector<>();
            vector.add("Select");

            while (resultSet.next()) {
                vector.add(resultSet.getString("task_status"));
                taskStatusMap.put(resultSet.getString("task_status"), resultSet.getString("task_status_id"));
            }

            DefaultComboBoxModel model = new DefaultComboBoxModel(vector);
            jComboBox13.setModel(model);

        } catch (Exception e) {
            SignIn.getLoggerObject().log(Level.WARNING, "loadTaskStatus method Exception found!", e);
        }
    }

    public void loadIssueType() {
        try {

            ResultSet resultSet = MySQL.executeSearch("SELECT * FROM `issue_type`");
            Vector<String> vector = new Vector<>();
            vector.add("Select");

            while (resultSet.next()) {
                vector.add(resultSet.getString("issue_type"));
                issueTypeMap.put(resultSet.getString("issue_type"), resultSet.getString("issue_type_id"));
            }

            DefaultComboBoxModel model = new DefaultComboBoxModel(vector);
            jComboBox4.setModel(model);

        } catch (Exception e) {
            SignIn.getLoggerObject().log(Level.WARNING, "loadIssueType method Exception found!", e);
        }
    }

    private void loadDate() {
        String date = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
        jTextField8.setText(date);
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPopupMenu1 = new javax.swing.JPopupMenu();
        jMenuItem1 = new javax.swing.JMenuItem();
        jMenuItem2 = new javax.swing.JMenuItem();
        jMenuItem3 = new javax.swing.JMenuItem();
        jPopupMenu2 = new javax.swing.JPopupMenu();
        jMenuItem5 = new javax.swing.JMenuItem();
        jMenuItem6 = new javax.swing.JMenuItem();
        jPopupMenu3 = new javax.swing.JPopupMenu();
        jMenuItem7 = new javax.swing.JMenuItem();
        jMenuItem8 = new javax.swing.JMenuItem();
        inventoryAuditMenuItem = new javax.swing.JMenuItem();
        jMenuItem9 = new javax.swing.JMenuItem();
        jPopupMenu4 = new javax.swing.JPopupMenu();
        jMenuItem11 = new javax.swing.JMenuItem();
        jMenuItem12 = new javax.swing.JMenuItem();
        jMenuItem13 = new javax.swing.JMenuItem();
        jMenuItem14 = new javax.swing.JMenuItem();
        jPopupMenu5 = new javax.swing.JPopupMenu();
        jMenuItem15 = new javax.swing.JMenuItem();
        jMenuItem16 = new javax.swing.JMenuItem();
        jMenuItem17 = new javax.swing.JMenuItem();
        jPopupMenu6 = new javax.swing.JPopupMenu();
        jMenuItem19 = new javax.swing.JMenuItem();
        jMenuItem20 = new javax.swing.JMenuItem();
        jMenuItem21 = new javax.swing.JMenuItem();
        jPanel1 = new javax.swing.JPanel();
        jTabbedPane1 = new javax.swing.JTabbedPane();
        jPanel2 = new javax.swing.JPanel();
        jLabel90 = new javax.swing.JLabel();
        pieChart1Panel = new javax.swing.JPanel();
        jPanel39 = new javax.swing.JPanel();
        jButton20 = new javax.swing.JButton();
        jButton22 = new javax.swing.JButton();
        jButton28 = new javax.swing.JButton();
        jButton43 = new javax.swing.JButton();
        jButton44 = new javax.swing.JButton();
        jButton45 = new javax.swing.JButton();
        jButton46 = new javax.swing.JButton();
        jButton47 = new javax.swing.JButton();
        jButton48 = new javax.swing.JButton();
        jButton49 = new javax.swing.JButton();
        lineChartPanel = new javax.swing.JPanel();
        jLabel91 = new javax.swing.JLabel();
        jPanel3 = new javax.swing.JPanel();
        jLabel23 = new javax.swing.JLabel();
        jPanel21 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTable1 = new javax.swing.JTable();
        jLabel24 = new javax.swing.JLabel();
        jComboBox1 = new javax.swing.JComboBox<>();
        jLabel25 = new javax.swing.JLabel();
        jTextField1 = new javax.swing.JTextField();
        jLabel28 = new javax.swing.JLabel();
        jButton1 = new javax.swing.JButton();
        jButton2 = new javax.swing.JButton();
        jButton3 = new javax.swing.JButton();
        jLabel48 = new javax.swing.JLabel();
        jTextField6 = new javax.swing.JTextField();
        jComboBox6 = new javax.swing.JComboBox<>();
        jButton16 = new javax.swing.JButton();
        jButton6 = new javax.swing.JButton();
        jButton36 = new javax.swing.JButton();
        jComboBox5 = new javax.swing.JComboBox<>();
        jPanel4 = new javax.swing.JPanel();
        jLabel29 = new javax.swing.JLabel();
        jPanel22 = new javax.swing.JPanel();
        jLabel30 = new javax.swing.JLabel();
        jLabel64 = new javax.swing.JLabel();
        jLabel83 = new javax.swing.JLabel();
        jPanel25 = new javax.swing.JPanel();
        jLabel31 = new javax.swing.JLabel();
        jLabel66 = new javax.swing.JLabel();
        jLabel85 = new javax.swing.JLabel();
        jPanel26 = new javax.swing.JPanel();
        jLabel32 = new javax.swing.JLabel();
        jLabel65 = new javax.swing.JLabel();
        jLabel84 = new javax.swing.JLabel();
        jPanel27 = new javax.swing.JPanel();
        jLabel33 = new javax.swing.JLabel();
        jLabel67 = new javax.swing.JLabel();
        jLabel86 = new javax.swing.JLabel();
        jSeparator1 = new javax.swing.JSeparator();
        jDateChooser1 = new com.toedter.calendar.JDateChooser();
        jDateChooser2 = new com.toedter.calendar.JDateChooser();
        jLabel34 = new javax.swing.JLabel();
        jLabel35 = new javax.swing.JLabel();
        jComboBox2 = new javax.swing.JComboBox<>();
        jLabel36 = new javax.swing.JLabel();
        jScrollPane2 = new javax.swing.JScrollPane();
        jTable2 = new javax.swing.JTable();
        jButton10 = new javax.swing.JButton();
        jSeparator5 = new javax.swing.JSeparator();
        jSeparator6 = new javax.swing.JSeparator();
        jButton37 = new javax.swing.JButton();
        jPanel5 = new javax.swing.JPanel();
        jLabel37 = new javax.swing.JLabel();
        jScrollPane3 = new javax.swing.JScrollPane();
        jTable3 = new javax.swing.JTable();
        jLabel39 = new javax.swing.JLabel();
        jLabel40 = new javax.swing.JLabel();
        jScrollPane4 = new javax.swing.JScrollPane();
        jTable4 = new javax.swing.JTable();
        jLabel41 = new javax.swing.JLabel();
        jTextField4 = new javax.swing.JTextField();
        jLabel42 = new javax.swing.JLabel();
        jComboBox3 = new javax.swing.JComboBox<>();
        jButton4 = new javax.swing.JButton();
        jButton5 = new javax.swing.JButton();
        jButton9 = new javax.swing.JButton();
        jLabel26 = new javax.swing.JLabel();
        jScrollPane9 = new javax.swing.JScrollPane();
        jTextArea1 = new javax.swing.JTextArea();
        jLabel58 = new javax.swing.JLabel();
        jButton24 = new javax.swing.JButton();
        jComboBox13 = new javax.swing.JComboBox<>();
        jButton29 = new javax.swing.JButton();
        jButton35 = new javax.swing.JButton();
        jButton38 = new javax.swing.JButton();
        jPanel37 = new javax.swing.JPanel();
        jSeparator11 = new javax.swing.JSeparator();
        jDateChooser3 = new com.toedter.calendar.JDateChooser();
        jDateChooser4 = new com.toedter.calendar.JDateChooser();
        jSpinner1 = new javax.swing.JSpinner();
        jSpinner2 = new javax.swing.JSpinner();
        jLabel2 = new javax.swing.JLabel();
        jLabel38 = new javax.swing.JLabel();
        jLabel88 = new javax.swing.JLabel();
        jLabel89 = new javax.swing.JLabel();
        jSeparator12 = new javax.swing.JSeparator();
        jSeparator13 = new javax.swing.JSeparator();
        jPanel6 = new javax.swing.JPanel();
        jScrollPane5 = new javax.swing.JScrollPane();
        jTable5 = new javax.swing.JTable();
        jLabel43 = new javax.swing.JLabel();
        jButton7 = new javax.swing.JButton();
        jPanel30 = new javax.swing.JPanel();
        jLabel27 = new javax.swing.JLabel();
        jPanel32 = new javax.swing.JPanel();
        jLabel44 = new javax.swing.JLabel();
        jPanel33 = new javax.swing.JPanel();
        jLabel45 = new javax.swing.JLabel();
        jSeparator3 = new javax.swing.JSeparator();
        jSeparator4 = new javax.swing.JSeparator();
        jLabel46 = new javax.swing.JLabel();
        jLabel57 = new javax.swing.JLabel();
        jLabel60 = new javax.swing.JLabel();
        jTextField2 = new javax.swing.JTextField();
        jButton31 = new javax.swing.JButton();
        jPanel7 = new javax.swing.JPanel();
        jLabel47 = new javax.swing.JLabel();
        jScrollPane6 = new javax.swing.JScrollPane();
        jTable6 = new javax.swing.JTable();
        jComboBox7 = new javax.swing.JComboBox<>();
        jButton11 = new javax.swing.JButton();
        jSeparator2 = new javax.swing.JSeparator();
        jLabel49 = new javax.swing.JLabel();
        jButton12 = new javax.swing.JButton();
        jButton13 = new javax.swing.JButton();
        jTextField20 = new javax.swing.JTextField();
        jButton32 = new javax.swing.JButton();
        jSeparator7 = new javax.swing.JSeparator();
        jSeparator8 = new javax.swing.JSeparator();
        jButton39 = new javax.swing.JButton();
        jPanel8 = new javax.swing.JPanel();
        jLabel50 = new javax.swing.JLabel();
        jComboBox8 = new javax.swing.JComboBox<>();
        jPanel31 = new javax.swing.JPanel();
        jScrollPane7 = new javax.swing.JScrollPane();
        jTable7 = new javax.swing.JTable();
        jButton17 = new javax.swing.JButton();
        jLabel51 = new javax.swing.JLabel();
        jLabel52 = new javax.swing.JLabel();
        jLabel59 = new javax.swing.JLabel();
        jTextField13 = new javax.swing.JTextField();
        jLabel61 = new javax.swing.JLabel();
        jTextField12 = new javax.swing.JTextField();
        jButton18 = new javax.swing.JButton();
        jButton19 = new javax.swing.JButton();
        jTextField15 = new javax.swing.JTextField();
        jTextField16 = new javax.swing.JTextField();
        jLabel63 = new javax.swing.JLabel();
        jTextField19 = new javax.swing.JTextField();
        jButton30 = new javax.swing.JButton();
        jButton33 = new javax.swing.JButton();
        jButton40 = new javax.swing.JButton();
        jPanel9 = new javax.swing.JPanel();
        jLabel54 = new javax.swing.JLabel();
        jScrollPane8 = new javax.swing.JScrollPane();
        jTable8 = new javax.swing.JTable();
        jComboBox9 = new javax.swing.JComboBox<>();
        jTextField7 = new javax.swing.JTextField();
        jLabel53 = new javax.swing.JLabel();
        jLabel55 = new javax.swing.JLabel();
        jLabel56 = new javax.swing.JLabel();
        jTextField10 = new javax.swing.JTextField();
        jButton14 = new javax.swing.JButton();
        jButton15 = new javax.swing.JButton();
        jLabel62 = new javax.swing.JLabel();
        jTextField14 = new javax.swing.JTextField();
        jButton23 = new javax.swing.JButton();
        jTextField17 = new javax.swing.JTextField();
        jTextField18 = new javax.swing.JTextField();
        jScrollPane10 = new javax.swing.JScrollPane();
        jTable9 = new javax.swing.JTable();
        jButton34 = new javax.swing.JButton();
        jButton41 = new javax.swing.JButton();
        jLabel87 = new javax.swing.JLabel();
        jButton51 = new javax.swing.JButton();
        jPanel10 = new javax.swing.JPanel();
        jLabel68 = new javax.swing.JLabel();
        jPanel28 = new javax.swing.JPanel();
        jLabel69 = new javax.swing.JLabel();
        jLabel70 = new javax.swing.JLabel();
        jFormattedTextField1 = new javax.swing.JFormattedTextField();
        jPanel29 = new javax.swing.JPanel();
        jLabel71 = new javax.swing.JLabel();
        jLabel72 = new javax.swing.JLabel();
        jFormattedTextField2 = new javax.swing.JFormattedTextField();
        jPanel34 = new javax.swing.JPanel();
        jLabel73 = new javax.swing.JLabel();
        jLabel74 = new javax.swing.JLabel();
        jFormattedTextField3 = new javax.swing.JFormattedTextField();
        jPanel35 = new javax.swing.JPanel();
        jSeparator9 = new javax.swing.JSeparator();
        jSeparator10 = new javax.swing.JSeparator();
        jPanel11 = new javax.swing.JPanel();
        jLabel75 = new javax.swing.JLabel();
        jScrollPane11 = new javax.swing.JScrollPane();
        jTable10 = new javax.swing.JTable();
        jLabel76 = new javax.swing.JLabel();
        jTextField5 = new javax.swing.JTextField();
        jLabel77 = new javax.swing.JLabel();
        jComboBox4 = new javax.swing.JComboBox<>();
        jLabel78 = new javax.swing.JLabel();
        jTextField8 = new javax.swing.JTextField();
        jLabel79 = new javax.swing.JLabel();
        jScrollPane12 = new javax.swing.JScrollPane();
        jTextArea2 = new javax.swing.JTextArea();
        jButton25 = new javax.swing.JButton();
        jButton8 = new javax.swing.JButton();
        jButton21 = new javax.swing.JButton();
        jButton50 = new javax.swing.JButton();
        jPanel12 = new javax.swing.JPanel();
        jLabel80 = new javax.swing.JLabel();
        jPanel36 = new javax.swing.JPanel();
        jScrollPane13 = new javax.swing.JScrollPane();
        jTable11 = new javax.swing.JTable();
        jLabel81 = new javax.swing.JLabel();
        jTextField9 = new javax.swing.JTextField();
        jLabel82 = new javax.swing.JLabel();
        jTextField11 = new javax.swing.JTextField();
        jButton26 = new javax.swing.JButton();
        jButton27 = new javax.swing.JButton();
        jButton42 = new javax.swing.JButton();
        jTextField3 = new javax.swing.JTextField();

        jMenuItem1.setText("change foreground colour");
        jMenuItem1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem1ActionPerformed(evt);
            }
        });
        jPopupMenu1.add(jMenuItem1);

        jMenuItem2.setText("change background colour");
        jMenuItem2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem2ActionPerformed(evt);
            }
        });
        jPopupMenu1.add(jMenuItem2);

        jMenuItem3.setText("update");
        jMenuItem3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem3ActionPerformed(evt);
            }
        });
        jPopupMenu1.add(jMenuItem3);

        jMenuItem5.setText("change foreground colour");
        jMenuItem5.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem5ActionPerformed(evt);
            }
        });
        jPopupMenu2.add(jMenuItem5);

        jMenuItem6.setText("change background colour");
        jMenuItem6.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem6ActionPerformed(evt);
            }
        });
        jPopupMenu2.add(jMenuItem6);

        jMenuItem7.setText("change foreground colour");
        jMenuItem7.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem7ActionPerformed(evt);
            }
        });
        jPopupMenu3.add(jMenuItem7);

        jMenuItem8.setText("change background colour");
        jMenuItem8.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem8ActionPerformed(evt);
            }
        });
        jPopupMenu3.add(jMenuItem8);

        inventoryAuditMenuItem.setText("Inventory Audit");
        inventoryAuditMenuItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                inventoryAuditMenuItemActionPerformed(evt);
            }
        });
        jPopupMenu3.add(inventoryAuditMenuItem);

        jMenuItem9.setText("update");
        jMenuItem9.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem9ActionPerformed(evt);
            }
        });
        jPopupMenu3.add(jMenuItem9);

        jMenuItem11.setText("change foreground colour");
        jMenuItem11.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem11ActionPerformed(evt);
            }
        });
        jPopupMenu4.add(jMenuItem11);

        jMenuItem12.setText("change background colour");
        jMenuItem12.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem12ActionPerformed(evt);
            }
        });
        jPopupMenu4.add(jMenuItem12);

        jMenuItem13.setText("update");
        jMenuItem13.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem13ActionPerformed(evt);
            }
        });
        jPopupMenu4.add(jMenuItem13);

        jMenuItem14.setText("delete");
        jMenuItem14.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem14ActionPerformed(evt);
            }
        });
        jPopupMenu4.add(jMenuItem14);

        jMenuItem15.setText("change foreground colour");
        jMenuItem15.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem15ActionPerformed(evt);
            }
        });
        jPopupMenu5.add(jMenuItem15);

        jMenuItem16.setText("change background colour");
        jMenuItem16.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem16ActionPerformed(evt);
            }
        });
        jPopupMenu5.add(jMenuItem16);

        jMenuItem17.setText("update");
        jMenuItem17.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem17ActionPerformed(evt);
            }
        });
        jPopupMenu5.add(jMenuItem17);

        jMenuItem19.setText("change foreground colour");
        jMenuItem19.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem19ActionPerformed(evt);
            }
        });
        jPopupMenu6.add(jMenuItem19);

        jMenuItem20.setText("change background colour");
        jMenuItem20.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem20ActionPerformed(evt);
            }
        });
        jPopupMenu6.add(jMenuItem20);

        jMenuItem21.setText("update");
        jMenuItem21.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem21ActionPerformed(evt);
            }
        });
        jPopupMenu6.add(jMenuItem21);

        setLayout(new java.awt.BorderLayout());

        jPanel1.setPreferredSize(new java.awt.Dimension(1066, 675));
        jPanel1.setLayout(new java.awt.BorderLayout());

        jTabbedPane1.setPreferredSize(new java.awt.Dimension(833, 670));

        jPanel2.setPreferredSize(new java.awt.Dimension(1194, 645));

        jLabel90.setFont(new java.awt.Font("Segoe UI Black", 0, 18)); // NOI18N
        jLabel90.setText("Production Dashboard");

        pieChart1Panel.setOpaque(false);
        pieChart1Panel.setPreferredSize(new java.awt.Dimension(403, 337));

        javax.swing.GroupLayout pieChart1PanelLayout = new javax.swing.GroupLayout(pieChart1Panel);
        pieChart1Panel.setLayout(pieChart1PanelLayout);
        pieChart1PanelLayout.setHorizontalGroup(
            pieChart1PanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 378, Short.MAX_VALUE)
        );
        pieChart1PanelLayout.setVerticalGroup(
            pieChart1PanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 378, Short.MAX_VALUE)
        );

        jPanel39.setLayout(new java.awt.GridLayout(2, 5, 3, 3));

        jButton20.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jButton20.setIcon(new javax.swing.ImageIcon(getClass().getResource("/lk/corevision/fruitshop/resources/production/plantation.png"))); // NOI18N
        jButton20.setText("  Plantation");
        jButton20.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton20ActionPerformed(evt);
            }
        });
        jPanel39.add(jButton20);

        jButton22.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jButton22.setIcon(new javax.swing.ImageIcon(getClass().getResource("/lk/corevision/fruitshop/resources/production/analyse.png"))); // NOI18N
        jButton22.setText("  Analytics");
        jButton22.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton22ActionPerformed(evt);
            }
        });
        jPanel39.add(jButton22);

        jButton28.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jButton28.setIcon(new javax.swing.ImageIcon(getClass().getResource("/lk/corevision/fruitshop/resources/production/task.png"))); // NOI18N
        jButton28.setText("  Workflow");
        jButton28.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton28ActionPerformed(evt);
            }
        });
        jPanel39.add(jButton28);

        jButton43.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jButton43.setIcon(new javax.swing.ImageIcon(getClass().getResource("/lk/corevision/fruitshop/resources/production/progress.png"))); // NOI18N
        jButton43.setText("  Progress");
        jButton43.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton43ActionPerformed(evt);
            }
        });
        jPanel39.add(jButton43);

        jButton44.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jButton44.setIcon(new javax.swing.ImageIcon(getClass().getResource("/lk/corevision/fruitshop/resources/production/worker.png"))); // NOI18N
        jButton44.setText("  Workers");
        jButton44.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton44ActionPerformed(evt);
            }
        });
        jPanel39.add(jButton44);

        jButton45.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jButton45.setIcon(new javax.swing.ImageIcon(getClass().getResource("/lk/corevision/fruitshop/resources/production/stock.png"))); // NOI18N
        jButton45.setText("  Inventory");
        jButton45.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton45ActionPerformed(evt);
            }
        });
        jPanel39.add(jButton45);

        jButton46.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jButton46.setIcon(new javax.swing.ImageIcon(getClass().getResource("/lk/corevision/fruitshop/resources/production/materials.png"))); // NOI18N
        jButton46.setText("  Materials");
        jButton46.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton46ActionPerformed(evt);
            }
        });
        jPanel39.add(jButton46);

        jButton47.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jButton47.setIcon(new javax.swing.ImageIcon(getClass().getResource("/lk/corevision/fruitshop/resources/production/cost.png"))); // NOI18N
        jButton47.setText("  Cost");
        jButton47.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton47ActionPerformed(evt);
            }
        });
        jPanel39.add(jButton47);

        jButton48.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jButton48.setIcon(new javax.swing.ImageIcon(getClass().getResource("/lk/corevision/fruitshop/resources/production/issue.png"))); // NOI18N
        jButton48.setText("  Issues");
        jButton48.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton48ActionPerformed(evt);
            }
        });
        jPanel39.add(jButton48);

        jButton49.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jButton49.setIcon(new javax.swing.ImageIcon(getClass().getResource("/lk/corevision/fruitshop/resources/production/fruit.png"))); // NOI18N
        jButton49.setText("  Registration");
        jButton49.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton49ActionPerformed(evt);
            }
        });
        jPanel39.add(jButton49);

        lineChartPanel.setOpaque(false);
        lineChartPanel.setPreferredSize(new java.awt.Dimension(403, 337));

        javax.swing.GroupLayout lineChartPanelLayout = new javax.swing.GroupLayout(lineChartPanel);
        lineChartPanel.setLayout(lineChartPanelLayout);
        lineChartPanelLayout.setHorizontalGroup(
            lineChartPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 0, Short.MAX_VALUE)
        );
        lineChartPanelLayout.setVerticalGroup(
            lineChartPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 355, Short.MAX_VALUE)
        );

        jLabel91.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        jLabel91.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel91.setText("Inventory Distribution by Weight");

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(21, 21, 21)
                .addComponent(jLabel90, javax.swing.GroupLayout.PREFERRED_SIZE, 216, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                        .addGap(9, 9, 9)
                        .addComponent(lineChartPanel, javax.swing.GroupLayout.DEFAULT_SIZE, 680, Short.MAX_VALUE)
                        .addGap(18, 18, 18)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jLabel91, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(pieChart1Panel, javax.swing.GroupLayout.PREFERRED_SIZE, 378, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(16, 16, 16))
                    .addComponent(jPanel39, javax.swing.GroupLayout.DEFAULT_SIZE, 1101, Short.MAX_VALUE))
                .addContainerGap())
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(15, 15, 15)
                .addComponent(jLabel90)
                .addGap(18, 18, 18)
                .addComponent(jPanel39, javax.swing.GroupLayout.PREFERRED_SIZE, 114, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(35, 35, 35)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addComponent(lineChartPanel, javax.swing.GroupLayout.DEFAULT_SIZE, 355, Short.MAX_VALUE)
                        .addGap(79, 79, 79))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addComponent(pieChart1Panel, javax.swing.GroupLayout.PREFERRED_SIZE, 378, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel91)
                        .addContainerGap(25, Short.MAX_VALUE))))
        );

        jTabbedPane1.addTab("Home", jPanel2);

        jLabel23.setFont(new java.awt.Font("Serif", 1, 36)); // NOI18N
        jLabel23.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel23.setText("Plantation");

        jPanel21.setBackground(new java.awt.Color(255, 102, 51));

        jTable1.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "plantation id", "fruit name", "yield location", "quantity", "date and time"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jTable1.setMaximumSize(new java.awt.Dimension(2147483647, 2147483647));
        jTable1.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jTable1MouseClicked(evt);
            }
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                jTable1MouseReleased(evt);
            }
        });
        jScrollPane1.setViewportView(jTable1);

        javax.swing.GroupLayout jPanel21Layout = new javax.swing.GroupLayout(jPanel21);
        jPanel21.setLayout(jPanel21Layout);
        jPanel21Layout.setHorizontalGroup(
            jPanel21Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel21Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 811, Short.MAX_VALUE)
                .addContainerGap())
        );
        jPanel21Layout.setVerticalGroup(
            jPanel21Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel21Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 399, Short.MAX_VALUE)
                .addContainerGap())
        );

        jLabel24.setFont(new java.awt.Font("Serif", 0, 18)); // NOI18N
        jLabel24.setText("Fruit Name");

        jComboBox1.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N

        jLabel25.setFont(new java.awt.Font("Serif", 0, 18)); // NOI18N
        jLabel25.setText("Quantity");

        jTextField1.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jTextField1.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                jTextField1KeyTyped(evt);
            }
        });

        jLabel28.setFont(new java.awt.Font("Serif", 0, 18)); // NOI18N
        jLabel28.setText("Plantation Id");

        jButton1.setBackground(new java.awt.Color(255, 153, 0));
        jButton1.setFont(new java.awt.Font("Segoe UI Black", 1, 18)); // NOI18N
        jButton1.setText("+");
        jButton1.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0), 2));
        jButton1.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        jButton2.setFont(new java.awt.Font("Segoe UI Black", 0, 14)); // NOI18N
        jButton2.setText("Update");
        jButton2.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });

        jButton3.setFont(new java.awt.Font("Segoe UI Black", 0, 14)); // NOI18N
        jButton3.setText("Add");
        jButton3.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        jButton3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton3ActionPerformed(evt);
            }
        });

        jLabel48.setFont(new java.awt.Font("Serif", 0, 18)); // NOI18N
        jLabel48.setText("Yield Location");

        jTextField6.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N

        jComboBox6.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Quantity in Ascending Order", "Quantity in Descending Order", "Fruit Names A-Z", "Fruit Names Z-A" }));
        jComboBox6.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                jComboBox6ItemStateChanged(evt);
            }
        });

        jButton16.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jButton16.setText("View plantation report");
        jButton16.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        jButton16.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton16ActionPerformed(evt);
            }
        });

        jButton6.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        jButton6.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton6ActionPerformed(evt);
            }
        });

        jButton36.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        jButton36.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton36ActionPerformed(evt);
            }
        });

        jComboBox5.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addGap(20, 20, 20)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jLabel28, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel24, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jComboBox1, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel25, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addComponent(jComboBox5, javax.swing.GroupLayout.PREFERRED_SIZE, 158, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(jButton1, javax.swing.GroupLayout.DEFAULT_SIZE, 42, Short.MAX_VALUE))
                    .addComponent(jLabel48, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jButton3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jButton2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jTextField1)
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addComponent(jTextField6, javax.swing.GroupLayout.PREFERRED_SIZE, 177, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jButton6, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)))
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addGap(55, 55, 55)
                        .addComponent(jComboBox6, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(jButton36, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jButton16))
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel3Layout.createSequentialGroup()
                        .addGap(46, 46, 46)
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel3Layout.createSequentialGroup()
                                .addComponent(jLabel23, javax.swing.GroupLayout.PREFERRED_SIZE, 776, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(0, 0, Short.MAX_VALUE))
                            .addComponent(jPanel21, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
                .addContainerGap())
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addGap(19, 19, 19)
                .addComponent(jLabel23, javax.swing.GroupLayout.PREFERRED_SIZE, 48, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(31, 31, 31)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addComponent(jLabel28, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jTextField6)
                            .addComponent(jButton6, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addGap(45, 45, 45)
                        .addComponent(jLabel24, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(7, 7, 7)
                        .addComponent(jComboBox1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(45, 45, 45)
                        .addComponent(jLabel25, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jTextField1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(45, 45, 45)
                        .addComponent(jLabel48, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 26, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jComboBox5))
                        .addGap(54, 54, 54)
                        .addComponent(jButton3, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jButton2, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jComboBox6, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jButton16, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jButton36, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(20, 20, 20)
                        .addComponent(jPanel21, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(54, Short.MAX_VALUE))
        );

        jTabbedPane1.addTab("Plantation", jPanel3);

        jPanel4.setPreferredSize(new java.awt.Dimension(810, 626));

        jLabel29.setFont(new java.awt.Font("Serif", 1, 36)); // NOI18N
        jLabel29.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel29.setText("Yield Analytics");

        jPanel22.setBackground(new java.awt.Color(255, 0, 0));
        jPanel22.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(255, 0, 255), 3));

        jLabel30.setBackground(new java.awt.Color(255, 102, 102));
        jLabel30.setFont(new java.awt.Font("Serif", 1, 24)); // NOI18N
        jLabel30.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel30.setText("Total Yield");
        jLabel30.setOpaque(true);

        jLabel64.setFont(new java.awt.Font("Segoe UI", 0, 24)); // NOI18N
        jLabel64.setText("kg");

        jLabel83.setFont(new java.awt.Font("Segoe UI", 0, 24)); // NOI18N
        jLabel83.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel83.setText("100");

        javax.swing.GroupLayout jPanel22Layout = new javax.swing.GroupLayout(jPanel22);
        jPanel22.setLayout(jPanel22Layout);
        jPanel22Layout.setHorizontalGroup(
            jPanel22Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel22Layout.createSequentialGroup()
                .addGap(19, 19, 19)
                .addComponent(jLabel83, javax.swing.GroupLayout.PREFERRED_SIZE, 74, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jLabel64)
                .addContainerGap(30, Short.MAX_VALUE))
            .addComponent(jLabel30, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        jPanel22Layout.setVerticalGroup(
            jPanel22Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel22Layout.createSequentialGroup()
                .addGap(14, 14, 14)
                .addComponent(jLabel30, javax.swing.GroupLayout.PREFERRED_SIZE, 42, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addGroup(jPanel22Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel64, javax.swing.GroupLayout.PREFERRED_SIZE, 42, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel83, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap(27, Short.MAX_VALUE))
        );

        jPanel25.setBackground(new java.awt.Color(0, 255, 51));
        jPanel25.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(51, 255, 51), 3));

        jLabel31.setBackground(new java.awt.Color(102, 255, 102));
        jLabel31.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        jLabel31.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel31.setText("Mango");
        jLabel31.setOpaque(true);

        jLabel66.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        jLabel66.setText("kg");

        jLabel85.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        jLabel85.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel85.setText("50");

        javax.swing.GroupLayout jPanel25Layout = new javax.swing.GroupLayout(jPanel25);
        jPanel25.setLayout(jPanel25Layout);
        jPanel25Layout.setHorizontalGroup(
            jPanel25Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel25Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel25Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel25Layout.createSequentialGroup()
                        .addComponent(jLabel31, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addContainerGap())
                    .addGroup(jPanel25Layout.createSequentialGroup()
                        .addComponent(jLabel85, javax.swing.GroupLayout.PREFERRED_SIZE, 53, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel66, javax.swing.GroupLayout.PREFERRED_SIZE, 26, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(15, 15, 15))))
        );
        jPanel25Layout.setVerticalGroup(
            jPanel25Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel25Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel31, javax.swing.GroupLayout.PREFERRED_SIZE, 29, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addGroup(jPanel25Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel66, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel85, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jPanel26.setBackground(new java.awt.Color(0, 153, 255));
        jPanel26.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 153, 255), 3));

        jLabel32.setBackground(new java.awt.Color(102, 153, 255));
        jLabel32.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        jLabel32.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel32.setText("Guava");
        jLabel32.setOpaque(true);

        jLabel65.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        jLabel65.setText("kg");

        jLabel84.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        jLabel84.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel84.setText("20");

        javax.swing.GroupLayout jPanel26Layout = new javax.swing.GroupLayout(jPanel26);
        jPanel26.setLayout(jPanel26Layout);
        jPanel26Layout.setHorizontalGroup(
            jPanel26Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel26Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel32, javax.swing.GroupLayout.DEFAULT_SIZE, 94, Short.MAX_VALUE)
                .addContainerGap())
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel26Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jLabel84, javax.swing.GroupLayout.PREFERRED_SIZE, 47, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel65, javax.swing.GroupLayout.PREFERRED_SIZE, 26, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(14, 14, 14))
        );
        jPanel26Layout.setVerticalGroup(
            jPanel26Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel26Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel32, javax.swing.GroupLayout.PREFERRED_SIZE, 29, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addGroup(jPanel26Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel65, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel84, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jPanel27.setBackground(new java.awt.Color(0, 255, 255));
        jPanel27.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 255, 255), 3));

        jLabel33.setBackground(new java.awt.Color(102, 255, 255));
        jLabel33.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        jLabel33.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel33.setText("Other");
        jLabel33.setOpaque(true);

        jLabel67.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        jLabel67.setText("kg");

        jLabel86.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        jLabel86.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel86.setText("30");

        javax.swing.GroupLayout jPanel27Layout = new javax.swing.GroupLayout(jPanel27);
        jPanel27.setLayout(jPanel27Layout);
        jPanel27Layout.setHorizontalGroup(
            jPanel27Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel27Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel33, javax.swing.GroupLayout.DEFAULT_SIZE, 94, Short.MAX_VALUE)
                .addContainerGap())
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel27Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jLabel86, javax.swing.GroupLayout.PREFERRED_SIZE, 47, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel67, javax.swing.GroupLayout.PREFERRED_SIZE, 26, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(16, 16, 16))
        );
        jPanel27Layout.setVerticalGroup(
            jPanel27Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel27Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel33, javax.swing.GroupLayout.PREFERRED_SIZE, 29, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addGroup(jPanel27Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel67, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel86, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap(16, Short.MAX_VALUE))
        );

        jSeparator1.setBackground(new java.awt.Color(255, 153, 51));
        jSeparator1.setOpaque(true);

        jDateChooser1.setDateFormatString("yyyy-MM-dd");
        jDateChooser1.addPropertyChangeListener(new java.beans.PropertyChangeListener() {
            public void propertyChange(java.beans.PropertyChangeEvent evt) {
                jDateChooser1PropertyChange(evt);
            }
        });

        jDateChooser2.setDateFormatString("yyyy-MM-dd");
        jDateChooser2.addPropertyChangeListener(new java.beans.PropertyChangeListener() {
            public void propertyChange(java.beans.PropertyChangeEvent evt) {
                jDateChooser2PropertyChange(evt);
            }
        });

        jLabel34.setFont(new java.awt.Font("Segoe UI", 0, 48)); // NOI18N
        jLabel34.setText("-");

        jLabel35.setFont(new java.awt.Font("Serif", 0, 18)); // NOI18N
        jLabel35.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel35.setText("Date Range");

        jComboBox2.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Weight in Ascending Order", "Weight in Descending Order", "Fruit Names A-Z", "Fruit Names Z-A" }));
        jComboBox2.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                jComboBox2ItemStateChanged(evt);
            }
        });

        jLabel36.setFont(new java.awt.Font("Serif", 0, 18)); // NOI18N
        jLabel36.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel36.setText("Sort by");

        jTable2.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "inventory id", "fruit name", "weight (kg)", "yield location", "date"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jTable2.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                jTable2MouseReleased(evt);
            }
        });
        jScrollPane2.setViewportView(jTable2);

        jButton10.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jButton10.setText("View analytics report");
        jButton10.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        jButton10.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton10ActionPerformed(evt);
            }
        });

        jSeparator5.setBackground(new java.awt.Color(255, 153, 51));
        jSeparator5.setOpaque(true);

        jSeparator6.setBackground(new java.awt.Color(255, 153, 51));
        jSeparator6.setOpaque(true);

        jButton37.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        jButton37.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton37ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jSeparator1)
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel4Layout.createSequentialGroup()
                                .addContainerGap(112, Short.MAX_VALUE)
                                .addComponent(jPanel22, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 124, Short.MAX_VALUE)
                                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(jPanel4Layout.createSequentialGroup()
                                        .addComponent(jLabel29, javax.swing.GroupLayout.PREFERRED_SIZE, 284, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addGap(0, 0, Short.MAX_VALUE))
                                    .addGroup(jPanel4Layout.createSequentialGroup()
                                        .addComponent(jPanel25, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 104, Short.MAX_VALUE)
                                        .addComponent(jPanel26, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 105, Short.MAX_VALUE)
                                        .addComponent(jPanel27, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                                .addGap(151, 151, 151))
                            .addGroup(jPanel4Layout.createSequentialGroup()
                                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                    .addGroup(jPanel4Layout.createSequentialGroup()
                                        .addGap(86, 86, 86)
                                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                            .addComponent(jLabel35, javax.swing.GroupLayout.PREFERRED_SIZE, 285, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addGroup(jPanel4Layout.createSequentialGroup()
                                                .addComponent(jDateChooser1, javax.swing.GroupLayout.PREFERRED_SIZE, 121, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                                .addComponent(jLabel34)
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                                .addComponent(jDateChooser2, javax.swing.GroupLayout.PREFERRED_SIZE, 121, javax.swing.GroupLayout.PREFERRED_SIZE)))
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                            .addComponent(jLabel36, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                            .addComponent(jComboBox2, 0, 233, Short.MAX_VALUE))
                                        .addGap(121, 121, 121)
                                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                            .addComponent(jButton10)
                                            .addComponent(jButton37, javax.swing.GroupLayout.PREFERRED_SIZE, 29, javax.swing.GroupLayout.PREFERRED_SIZE)))
                                    .addGroup(jPanel4Layout.createSequentialGroup()
                                        .addGap(0, 0, Short.MAX_VALUE)
                                        .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 965, javax.swing.GroupLayout.PREFERRED_SIZE)))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jSeparator5, javax.swing.GroupLayout.PREFERRED_SIZE, 8, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jSeparator6, javax.swing.GroupLayout.PREFERRED_SIZE, 8, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addContainerGap())
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addComponent(jLabel29, javax.swing.GroupLayout.PREFERRED_SIZE, 55, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jPanel22, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(jPanel4Layout.createSequentialGroup()
                                .addGap(13, 13, 13)
                                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addComponent(jPanel25, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(jPanel26, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(jPanel27, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))))
                    .addComponent(jSeparator6, javax.swing.GroupLayout.PREFERRED_SIZE, 235, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jSeparator1, javax.swing.GroupLayout.PREFERRED_SIZE, 11, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel4Layout.createSequentialGroup()
                                .addGap(54, 54, 54)
                                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(jPanel4Layout.createSequentialGroup()
                                        .addComponent(jLabel35, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                            .addComponent(jDateChooser1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addComponent(jLabel34, javax.swing.GroupLayout.PREFERRED_SIZE, 27, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addComponent(jDateChooser2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                                    .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                        .addGroup(jPanel4Layout.createSequentialGroup()
                                            .addComponent(jButton37, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addGap(38, 38, 38))
                                        .addComponent(jButton10)
                                        .addComponent(jComboBox2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))))
                            .addGroup(jPanel4Layout.createSequentialGroup()
                                .addGap(55, 55, 55)
                                .addComponent(jLabel36, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addGap(28, 28, 28)
                        .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 227, Short.MAX_VALUE))
                    .addComponent(jSeparator5))
                .addContainerGap())
        );

        jTabbedPane1.addTab("Yield Analytics", jPanel4);

        jPanel5.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel37.setFont(new java.awt.Font("Serif", 1, 36)); // NOI18N
        jLabel37.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel37.setText("Production Workflow Management");
        jPanel5.add(jLabel37, new org.netbeans.lib.awtextra.AbsoluteConstraints(310, 10, -1, 52));

        jTable3.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Worker NIC", "Name", "Mobile"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jTable3.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jTable3MouseClicked(evt);
            }
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                jTable3MouseReleased(evt);
            }
        });
        jScrollPane3.setViewportView(jTable3);
        if (jTable3.getColumnModel().getColumnCount() > 0) {
            jTable3.getColumnModel().getColumn(1).setPreferredWidth(15);
        }

        jPanel5.add(jScrollPane3, new org.netbeans.lib.awtextra.AbsoluteConstraints(94, 145, 437, 113));

        jLabel39.setFont(new java.awt.Font("Serif", 0, 18)); // NOI18N
        jLabel39.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel39.setText("Task Assigned Workers");
        jPanel5.add(jLabel39, new org.netbeans.lib.awtextra.AbsoluteConstraints(780, 106, 189, 33));

        jLabel40.setFont(new java.awt.Font("Serif", 0, 18)); // NOI18N
        jLabel40.setText("Idle Workers");
        jPanel5.add(jLabel40, new org.netbeans.lib.awtextra.AbsoluteConstraints(189, 106, 114, 33));

        jTable4.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Worker NIC", "Task Id", "Task", "Task Status"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jTable4.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jTable4MouseClicked(evt);
            }
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                jTable4MouseReleased(evt);
            }
        });
        jScrollPane4.setViewportView(jTable4);

        jPanel5.add(jScrollPane4, new org.netbeans.lib.awtextra.AbsoluteConstraints(606, 145, 460, 113));

        jLabel41.setFont(new java.awt.Font("Serif", 0, 18)); // NOI18N
        jLabel41.setText("Worker NIC");
        jPanel5.add(jLabel41, new org.netbeans.lib.awtextra.AbsoluteConstraints(80, 311, 180, 28));

        jTextField4.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jPanel5.add(jTextField4, new org.netbeans.lib.awtextra.AbsoluteConstraints(80, 360, 153, -1));

        jLabel42.setFont(new java.awt.Font("Serif", 0, 18)); // NOI18N
        jLabel42.setText("Task");
        jPanel5.add(jLabel42, new org.netbeans.lib.awtextra.AbsoluteConstraints(80, 430, 173, 28));

        jComboBox3.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jPanel5.add(jComboBox3, new org.netbeans.lib.awtextra.AbsoluteConstraints(80, 470, 129, -1));

        jButton4.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        jButton4.setText("Add");
        jButton4.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        jButton4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton4ActionPerformed(evt);
            }
        });
        jPanel5.add(jButton4, new org.netbeans.lib.awtextra.AbsoluteConstraints(890, 540, 169, -1));

        jButton5.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        jButton5.setText("Update");
        jButton5.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        jButton5.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton5ActionPerformed(evt);
            }
        });
        jPanel5.add(jButton5, new org.netbeans.lib.awtextra.AbsoluteConstraints(890, 590, 169, -1));

        jButton9.setBackground(new java.awt.Color(255, 153, 0));
        jButton9.setFont(new java.awt.Font("Segoe UI Black", 1, 18)); // NOI18N
        jButton9.setText("+");
        jButton9.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0), 2));
        jButton9.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        jButton9.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton9ActionPerformed(evt);
            }
        });
        jPanel5.add(jButton9, new org.netbeans.lib.awtextra.AbsoluteConstraints(210, 470, -1, 28));

        jLabel26.setFont(new java.awt.Font("Serif", 0, 18)); // NOI18N
        jLabel26.setText("Task Description");
        jPanel5.add(jLabel26, new org.netbeans.lib.awtextra.AbsoluteConstraints(390, 310, 190, 34));

        jTextArea1.setColumns(20);
        jTextArea1.setRows(5);
        jScrollPane9.setViewportView(jTextArea1);

        jPanel5.add(jScrollPane9, new org.netbeans.lib.awtextra.AbsoluteConstraints(390, 360, 260, 140));

        jLabel58.setFont(new java.awt.Font("Serif", 0, 18)); // NOI18N
        jLabel58.setText("Task Status");
        jPanel5.add(jLabel58, new org.netbeans.lib.awtextra.AbsoluteConstraints(80, 540, 161, -1));

        jButton24.setBackground(new java.awt.Color(255, 255, 51));
        jButton24.setFont(new java.awt.Font("Segoe UI Black", 1, 18)); // NOI18N
        jButton24.setText("+");
        jButton24.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0), 2));
        jButton24.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        jButton24.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton24ActionPerformed(evt);
            }
        });
        jPanel5.add(jButton24, new org.netbeans.lib.awtextra.AbsoluteConstraints(210, 590, 31, -1));

        jComboBox13.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jPanel5.add(jComboBox13, new org.netbeans.lib.awtextra.AbsoluteConstraints(80, 590, 128, 31));

        jButton29.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        jButton29.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton29ActionPerformed(evt);
            }
        });
        jPanel5.add(jButton29, new org.netbeans.lib.awtextra.AbsoluteConstraints(245, 360, 27, 26));

        jButton35.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        jButton35.setText("Task Completed");
        jButton35.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        jButton35.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton35ActionPerformed(evt);
            }
        });
        jPanel5.add(jButton35, new org.netbeans.lib.awtextra.AbsoluteConstraints(400, 580, 250, -1));

        jButton38.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        jButton38.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton38ActionPerformed(evt);
            }
        });
        jPanel5.add(jButton38, new org.netbeans.lib.awtextra.AbsoluteConstraints(830, 590, 36, 31));

        jSeparator11.setBackground(new java.awt.Color(255, 153, 51));
        jSeparator11.setOpaque(true);

        jDateChooser3.setDateFormatString("yyyy-MM-dd");

        jDateChooser4.setDateFormatString("yyyy-MM-dd");
        jDateChooser4.addPropertyChangeListener(new java.beans.PropertyChangeListener() {
            public void propertyChange(java.beans.PropertyChangeEvent evt) {
                jDateChooser4PropertyChange(evt);
            }
        });

        jSpinner1.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        jSpinner1.setCursor(new java.awt.Cursor(java.awt.Cursor.TEXT_CURSOR));

        jSpinner2.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        jSpinner2.setCursor(new java.awt.Cursor(java.awt.Cursor.TEXT_CURSOR));

        jLabel2.setFont(new java.awt.Font("Serif", 0, 18)); // NOI18N
        jLabel2.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel2.setText("Due");

        jLabel38.setFont(new java.awt.Font("Serif", 0, 18)); // NOI18N
        jLabel38.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel38.setText("Date & Time");

        jLabel88.setFont(new java.awt.Font("Serif", 0, 18)); // NOI18N
        jLabel88.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel88.setText("Completion");

        jLabel89.setFont(new java.awt.Font("Serif", 0, 18)); // NOI18N
        jLabel89.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel89.setText("Date & Time");

        jSeparator12.setBackground(new java.awt.Color(255, 153, 51));
        jSeparator12.setOpaque(true);

        jSeparator13.setBackground(new java.awt.Color(255, 153, 51));
        jSeparator13.setOpaque(true);

        javax.swing.GroupLayout jPanel37Layout = new javax.swing.GroupLayout(jPanel37);
        jPanel37.setLayout(jPanel37Layout);
        jPanel37Layout.setHorizontalGroup(
            jPanel37Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel37Layout.createSequentialGroup()
                .addGap(1, 1, 1)
                .addComponent(jSeparator12, javax.swing.GroupLayout.PREFERRED_SIZE, 9, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel37Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jDateChooser3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jSpinner1)
                    .addComponent(jLabel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel38, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 127, Short.MAX_VALUE))
                .addGap(12, 12, 12)
                .addComponent(jSeparator11, javax.swing.GroupLayout.PREFERRED_SIZE, 9, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(12, 12, 12)
                .addGroup(jPanel37Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jDateChooser4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jSpinner2, javax.swing.GroupLayout.PREFERRED_SIZE, 129, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel88, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel89, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jSeparator13, javax.swing.GroupLayout.DEFAULT_SIZE, 11, Short.MAX_VALUE))
        );
        jPanel37Layout.setVerticalGroup(
            jPanel37Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel37Layout.createSequentialGroup()
                .addGroup(jPanel37Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel37Layout.createSequentialGroup()
                        .addContainerGap()
                        .addGroup(jPanel37Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel37Layout.createSequentialGroup()
                                .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 29, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jLabel38, javax.swing.GroupLayout.PREFERRED_SIZE, 29, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(jPanel37Layout.createSequentialGroup()
                                .addComponent(jLabel88, javax.swing.GroupLayout.PREFERRED_SIZE, 29, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jLabel89, javax.swing.GroupLayout.PREFERRED_SIZE, 29, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 17, Short.MAX_VALUE)
                        .addGroup(jPanel37Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addGroup(jPanel37Layout.createSequentialGroup()
                                .addComponent(jDateChooser4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addComponent(jSpinner2))
                            .addGroup(jPanel37Layout.createSequentialGroup()
                                .addComponent(jDateChooser3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addComponent(jSpinner1, javax.swing.GroupLayout.PREFERRED_SIZE, 43, javax.swing.GroupLayout.PREFERRED_SIZE))))
                    .addComponent(jSeparator12)
                    .addComponent(jSeparator13)
                    .addComponent(jSeparator11, javax.swing.GroupLayout.Alignment.TRAILING))
                .addContainerGap())
        );

        jPanel5.add(jPanel37, new org.netbeans.lib.awtextra.AbsoluteConstraints(736, 308, 322, -1));

        jTabbedPane1.addTab("Workflow", jPanel5);

        jPanel6.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jTable5.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "plantation id", "inventory id", "quality checking id", "fruit name", "weight (kg)", "yield location"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jTable5.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jTable5MouseClicked(evt);
            }
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                jTable5MouseReleased(evt);
            }
        });
        jScrollPane5.setViewportView(jTable5);

        jPanel6.add(jScrollPane5, new org.netbeans.lib.awtextra.AbsoluteConstraints(330, 308, 728, 242));

        jLabel43.setFont(new java.awt.Font("Serif", 1, 36)); // NOI18N
        jLabel43.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel43.setText("Progress Tracking");
        jPanel6.add(jLabel43, new org.netbeans.lib.awtextra.AbsoluteConstraints(555, 14, 318, 62));

        jButton7.setFont(new java.awt.Font("Segoe UI Black", 0, 14)); // NOI18N
        jButton7.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        jButton7.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton7ActionPerformed(evt);
            }
        });
        jPanel6.add(jButton7, new org.netbeans.lib.awtextra.AbsoluteConstraints(986, 568, -1, 31));

        jPanel30.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0), 3));

        jLabel27.setFont(new java.awt.Font("Serif", 1, 24)); // NOI18N
        jLabel27.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel27.setText("(1)");
        jLabel27.setOpaque(true);

        javax.swing.GroupLayout jPanel30Layout = new javax.swing.GroupLayout(jPanel30);
        jPanel30.setLayout(jPanel30Layout);
        jPanel30Layout.setHorizontalGroup(
            jPanel30Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jLabel27, javax.swing.GroupLayout.DEFAULT_SIZE, 66, Short.MAX_VALUE)
        );
        jPanel30Layout.setVerticalGroup(
            jPanel30Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jLabel27, javax.swing.GroupLayout.DEFAULT_SIZE, 61, Short.MAX_VALUE)
        );

        jPanel6.add(jPanel30, new org.netbeans.lib.awtextra.AbsoluteConstraints(485, 112, -1, -1));

        jPanel32.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0), 3));

        jLabel44.setFont(new java.awt.Font("Serif", 1, 24)); // NOI18N
        jLabel44.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel44.setText("(2)");
        jLabel44.setOpaque(true);

        javax.swing.GroupLayout jPanel32Layout = new javax.swing.GroupLayout(jPanel32);
        jPanel32.setLayout(jPanel32Layout);
        jPanel32Layout.setHorizontalGroup(
            jPanel32Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jLabel44, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        jPanel32Layout.setVerticalGroup(
            jPanel32Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jLabel44, javax.swing.GroupLayout.DEFAULT_SIZE, 61, Short.MAX_VALUE)
        );

        jPanel6.add(jPanel32, new org.netbeans.lib.awtextra.AbsoluteConstraints(677, 112, 70, -1));

        jPanel33.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0), 3));

        jLabel45.setFont(new java.awt.Font("Serif", 1, 24)); // NOI18N
        jLabel45.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel45.setText("(3)");
        jLabel45.setOpaque(true);

        javax.swing.GroupLayout jPanel33Layout = new javax.swing.GroupLayout(jPanel33);
        jPanel33.setLayout(jPanel33Layout);
        jPanel33Layout.setHorizontalGroup(
            jPanel33Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jLabel45, javax.swing.GroupLayout.DEFAULT_SIZE, 64, Short.MAX_VALUE)
        );
        jPanel33Layout.setVerticalGroup(
            jPanel33Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jLabel45, javax.swing.GroupLayout.DEFAULT_SIZE, 61, Short.MAX_VALUE)
        );

        jPanel6.add(jPanel33, new org.netbeans.lib.awtextra.AbsoluteConstraints(867, 112, -1, -1));

        jSeparator3.setOpaque(true);
        jPanel6.add(jSeparator3, new org.netbeans.lib.awtextra.AbsoluteConstraints(563, 141, 108, 10));

        jSeparator4.setOpaque(true);
        jPanel6.add(jSeparator4, new org.netbeans.lib.awtextra.AbsoluteConstraints(753, 141, 108, 10));

        jLabel46.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabel46.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel46.setText("Planting");
        jPanel6.add(jLabel46, new org.netbeans.lib.awtextra.AbsoluteConstraints(491, 197, 66, -1));

        jLabel57.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabel57.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel57.setText("Storing");
        jPanel6.add(jLabel57, new org.netbeans.lib.awtextra.AbsoluteConstraints(677, 197, 70, -1));

        jLabel60.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabel60.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel60.setText("Quality Checking");
        jPanel6.add(jLabel60, new org.netbeans.lib.awtextra.AbsoluteConstraints(851, 197, -1, -1));

        jTextField2.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jTextField2.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                jTextField2KeyReleased(evt);
            }
        });
        jPanel6.add(jTextField2, new org.netbeans.lib.awtextra.AbsoluteConstraints(618, 257, 201, 33));

        jButton31.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        jButton31.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton31ActionPerformed(evt);
            }
        });
        jPanel6.add(jButton31, new org.netbeans.lib.awtextra.AbsoluteConstraints(831, 257, 33, 33));

        jTabbedPane1.addTab("Progress", jPanel6);

        jPanel7.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel47.setFont(new java.awt.Font("Serif", 1, 36)); // NOI18N
        jLabel47.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel47.setText("Worker Performance");
        jPanel7.add(jLabel47, new org.netbeans.lib.awtextra.AbsoluteConstraints(397, 18, 465, 50));

        jTable6.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "task id", "task name", "worker NIC", "assigned date & time", "due date & time", "completion date & time", "duration"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jTable6.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                jTable6MouseReleased(evt);
            }
        });
        jScrollPane6.setViewportView(jTable6);
        if (jTable6.getColumnModel().getColumnCount() > 0) {
            jTable6.getColumnModel().getColumn(0).setPreferredWidth(5);
            jTable6.getColumnModel().getColumn(1).setPreferredWidth(30);
        }

        jPanel7.add(jScrollPane6, new org.netbeans.lib.awtextra.AbsoluteConstraints(57, 153, 1010, 276));

        jComboBox7.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "High Performing Workers", "Low Performing Workers" }));
        jComboBox7.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                jComboBox7ItemStateChanged(evt);
            }
        });
        jPanel7.add(jComboBox7, new org.netbeans.lib.awtextra.AbsoluteConstraints(840, 100, 220, -1));

        jButton11.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        jButton11.setText("Annual");
        jButton11.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        jButton11.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton11ActionPerformed(evt);
            }
        });
        jPanel7.add(jButton11, new org.netbeans.lib.awtextra.AbsoluteConstraints(833, 574, 123, 35));

        jSeparator2.setBackground(new java.awt.Color(255, 153, 51));
        jSeparator2.setOpaque(true);
        jPanel7.add(jSeparator2, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 447, 1113, 9));

        jLabel49.setFont(new java.awt.Font("Serif", 1, 24)); // NOI18N
        jLabel49.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel49.setText("View Productivity Reports");
        jPanel7.add(jLabel49, new org.netbeans.lib.awtextra.AbsoluteConstraints(438, 462, 368, 70));

        jButton12.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        jButton12.setText("Monthly");
        jButton12.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        jButton12.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton12ActionPerformed(evt);
            }
        });
        jPanel7.add(jButton12, new org.netbeans.lib.awtextra.AbsoluteConstraints(552, 574, 121, 35));

        jButton13.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        jButton13.setText("Daily");
        jButton13.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        jButton13.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton13ActionPerformed(evt);
            }
        });
        jPanel7.add(jButton13, new org.netbeans.lib.awtextra.AbsoluteConstraints(282, 574, 108, 35));

        jTextField20.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                jTextField20KeyReleased(evt);
            }
        });
        jPanel7.add(jTextField20, new org.netbeans.lib.awtextra.AbsoluteConstraints(93, 102, 159, -1));

        jButton32.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        jButton32.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton32ActionPerformed(evt);
            }
        });
        jPanel7.add(jButton32, new org.netbeans.lib.awtextra.AbsoluteConstraints(270, 102, 23, 22));

        jSeparator7.setBackground(new java.awt.Color(255, 153, 51));
        jSeparator7.setOpaque(true);
        jPanel7.add(jSeparator7, new org.netbeans.lib.awtextra.AbsoluteConstraints(1105, 479, 8, 163));

        jSeparator8.setBackground(new java.awt.Color(255, 153, 51));
        jSeparator8.setOpaque(true);
        jPanel7.add(jSeparator8, new org.netbeans.lib.awtextra.AbsoluteConstraints(1105, 6, 8, 423));

        jButton39.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        jButton39.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton39ActionPerformed(evt);
            }
        });
        jPanel7.add(jButton39, new org.netbeans.lib.awtextra.AbsoluteConstraints(800, 100, 29, 22));

        jTabbedPane1.addTab("Worker Performance", jPanel7);

        jPanel8.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel50.setFont(new java.awt.Font("Serif", 1, 36)); // NOI18N
        jLabel50.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel50.setText("Inventory Management");
        jPanel8.add(jLabel50, new org.netbeans.lib.awtextra.AbsoluteConstraints(428, 15, 493, 50));

        jComboBox8.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Weight in Ascending Order", "Weight in Descending Order", "Fruit Names A-Z", "Fruit Names Z-A" }));
        jComboBox8.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                jComboBox8ItemStateChanged(evt);
            }
        });
        jPanel8.add(jComboBox8, new org.netbeans.lib.awtextra.AbsoluteConstraints(545, 108, -1, 30));

        jPanel31.setBackground(new java.awt.Color(255, 102, 52));

        jTable7.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "inventory id", "plantation id", "fruit name", "quantity", "weight (kg)", "date"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jTable7.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jTable7MouseClicked(evt);
            }
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                jTable7MouseReleased(evt);
            }
        });
        jScrollPane7.setViewportView(jTable7);

        javax.swing.GroupLayout jPanel31Layout = new javax.swing.GroupLayout(jPanel31);
        jPanel31.setLayout(jPanel31Layout);
        jPanel31Layout.setHorizontalGroup(
            jPanel31Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel31Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane7, javax.swing.GroupLayout.PREFERRED_SIZE, 701, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(14, Short.MAX_VALUE))
        );
        jPanel31Layout.setVerticalGroup(
            jPanel31Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel31Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane7, javax.swing.GroupLayout.DEFAULT_SIZE, 379, Short.MAX_VALUE)
                .addContainerGap())
        );

        jPanel8.add(jPanel31, new org.netbeans.lib.awtextra.AbsoluteConstraints(364, 156, -1, -1));

        jButton17.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jButton17.setText("View inventory report");
        jButton17.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        jButton17.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton17ActionPerformed(evt);
            }
        });
        jPanel8.add(jButton17, new org.netbeans.lib.awtextra.AbsoluteConstraints(906, 107, -1, 30));

        jLabel51.setFont(new java.awt.Font("Serif", 0, 18)); // NOI18N
        jLabel51.setText("Inventory Id");
        jPanel8.add(jLabel51, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 171, 244, 31));

        jLabel52.setFont(new java.awt.Font("Serif", 0, 18)); // NOI18N
        jLabel52.setText("Plantation Id");
        jPanel8.add(jLabel52, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 77, 244, 31));

        jLabel59.setFont(new java.awt.Font("Serif", 0, 18)); // NOI18N
        jLabel59.setText("Quantity");
        jPanel8.add(jLabel59, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 267, 244, 31));

        jTextField13.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jPanel8.add(jTextField13, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 111, 206, -1));

        jLabel61.setFont(new java.awt.Font("Serif", 0, 18)); // NOI18N
        jLabel61.setText("Weight");
        jPanel8.add(jLabel61, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 365, 244, 31));

        jTextField12.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jTextField12.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                jTextField12KeyReleased(evt);
            }
        });
        jPanel8.add(jTextField12, new org.netbeans.lib.awtextra.AbsoluteConstraints(364, 107, 163, 31));

        jButton18.setFont(new java.awt.Font("Segoe UI Black", 0, 14)); // NOI18N
        jButton18.setText("Add");
        jButton18.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        jButton18.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton18ActionPerformed(evt);
            }
        });
        jPanel8.add(jButton18, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 491, 240, -1));

        jButton19.setFont(new java.awt.Font("Segoe UI Black", 0, 14)); // NOI18N
        jButton19.setText("Update");
        jButton19.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        jButton19.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton19ActionPerformed(evt);
            }
        });
        jPanel8.add(jButton19, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 522, 240, -1));

        jTextField15.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jTextField15.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                jTextField15KeyTyped(evt);
            }
        });
        jPanel8.add(jTextField15, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 303, 244, -1));

        jTextField16.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jTextField16.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                jTextField16KeyTyped(evt);
            }
        });
        jPanel8.add(jTextField16, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 402, 198, -1));

        jLabel63.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        jLabel63.setText("kg");
        jPanel8.add(jLabel63, new org.netbeans.lib.awtextra.AbsoluteConstraints(250, 402, 34, 26));

        jTextField19.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jPanel8.add(jTextField19, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 207, 206, -1));

        jButton30.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        jButton30.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton30ActionPerformed(evt);
            }
        });
        jPanel8.add(jButton30, new org.netbeans.lib.awtextra.AbsoluteConstraints(258, 207, 26, 26));

        jButton33.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        jButton33.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton33ActionPerformed(evt);
            }
        });
        jPanel8.add(jButton33, new org.netbeans.lib.awtextra.AbsoluteConstraints(258, 111, 26, 26));

        jButton40.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        jButton40.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton40ActionPerformed(evt);
            }
        });
        jPanel8.add(jButton40, new org.netbeans.lib.awtextra.AbsoluteConstraints(856, 107, 32, 30));

        jTabbedPane1.addTab("Inventory", jPanel8);

        jPanel9.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel54.setFont(new java.awt.Font("Serif", 1, 36)); // NOI18N
        jLabel54.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel54.setText("Raw Materials");
        jPanel9.add(jLabel54, new org.netbeans.lib.awtextra.AbsoluteConstraints(472, 18, 493, 50));

        jTable8.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "raw material id", "material name"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jTable8.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jTable8MouseClicked(evt);
            }
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                jTable8MouseReleased(evt);
            }
        });
        jScrollPane8.setViewportView(jTable8);

        jPanel9.add(jScrollPane8, new org.netbeans.lib.awtextra.AbsoluteConstraints(351, 133, 700, 171));

        jComboBox9.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Quantity in Ascending Order", "Quantity in Descending Order", "Fruit Names A-Z", "Fruit Names Z-A" }));
        jComboBox9.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                jComboBox9ItemStateChanged(evt);
            }
        });
        jPanel9.add(jComboBox9, new org.netbeans.lib.awtextra.AbsoluteConstraints(808, 371, -1, 30));

        jTextField7.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                jTextField7KeyReleased(evt);
            }
        });
        jPanel9.add(jTextField7, new org.netbeans.lib.awtextra.AbsoluteConstraints(351, 371, 183, 30));

        jLabel53.setFont(new java.awt.Font("Serif", 0, 18)); // NOI18N
        jLabel53.setText("Plantation Id");
        jPanel9.add(jLabel53, new org.netbeans.lib.awtextra.AbsoluteConstraints(41, 104, 223, 29));

        jLabel55.setFont(new java.awt.Font("Serif", 0, 18)); // NOI18N
        jLabel55.setText("Material Name");
        jPanel9.add(jLabel55, new org.netbeans.lib.awtextra.AbsoluteConstraints(41, 275, 223, 29));

        jLabel56.setFont(new java.awt.Font("Serif", 0, 18)); // NOI18N
        jLabel56.setText("Fruit Name");
        jPanel9.add(jLabel56, new org.netbeans.lib.awtextra.AbsoluteConstraints(41, 376, 223, 29));

        jTextField10.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jPanel9.add(jTextField10, new org.netbeans.lib.awtextra.AbsoluteConstraints(41, 411, 223, -1));

        jButton14.setFont(new java.awt.Font("Segoe UI Black", 1, 14)); // NOI18N
        jButton14.setText("Add");
        jButton14.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        jButton14.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton14ActionPerformed(evt);
            }
        });
        jPanel9.add(jButton14, new org.netbeans.lib.awtextra.AbsoluteConstraints(41, 501, 223, 30));

        jButton15.setFont(new java.awt.Font("Segoe UI Black", 1, 14)); // NOI18N
        jButton15.setText("Update");
        jButton15.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        jButton15.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton15ActionPerformed(evt);
            }
        });
        jPanel9.add(jButton15, new org.netbeans.lib.awtextra.AbsoluteConstraints(41, 537, 223, 30));

        jLabel62.setFont(new java.awt.Font("Serif", 0, 18)); // NOI18N
        jLabel62.setText("Raw Material Id");
        jPanel9.add(jLabel62, new org.netbeans.lib.awtextra.AbsoluteConstraints(41, 186, 223, 29));

        jTextField14.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jPanel9.add(jTextField14, new org.netbeans.lib.awtextra.AbsoluteConstraints(41, 133, 186, -1));

        jButton23.setFont(new java.awt.Font("Segoe UI Black", 1, 14)); // NOI18N
        jButton23.setText("Delete");
        jButton23.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        jButton23.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton23ActionPerformed(evt);
            }
        });
        jPanel9.add(jButton23, new org.netbeans.lib.awtextra.AbsoluteConstraints(41, 573, 223, 30));

        jTextField17.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jPanel9.add(jTextField17, new org.netbeans.lib.awtextra.AbsoluteConstraints(41, 221, 223, -1));

        jTextField18.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jTextField18.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                jTextField18KeyReleased(evt);
            }
        });
        jPanel9.add(jTextField18, new org.netbeans.lib.awtextra.AbsoluteConstraints(41, 309, 189, -1));

        jTable9.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "plantation id", "fruit name", "quantity", "yield location"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jTable9.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jTable9MouseClicked(evt);
            }
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                jTable9MouseReleased(evt);
            }
        });
        jScrollPane10.setViewportView(jTable9);

        jPanel9.add(jScrollPane10, new org.netbeans.lib.awtextra.AbsoluteConstraints(351, 419, 700, 184));

        jButton34.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        jButton34.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton34ActionPerformed(evt);
            }
        });
        jPanel9.add(jButton34, new org.netbeans.lib.awtextra.AbsoluteConstraints(239, 133, 25, 25));

        jButton41.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        jButton41.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton41ActionPerformed(evt);
            }
        });
        jPanel9.add(jButton41, new org.netbeans.lib.awtextra.AbsoluteConstraints(552, 371, 34, 30));

        jLabel87.setFont(new java.awt.Font("JetBrains Mono", 1, 14)); // NOI18N
        jLabel87.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel87.setText("All Raw Materials");
        jPanel9.add(jLabel87, new org.netbeans.lib.awtextra.AbsoluteConstraints(351, 104, 648, 24));

        jButton51.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        jButton51.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton51ActionPerformed(evt);
            }
        });
        jPanel9.add(jButton51, new org.netbeans.lib.awtextra.AbsoluteConstraints(242, 309, 25, 25));

        jTabbedPane1.addTab("Raw Materials", jPanel9);

        jPanel10.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel68.setFont(new java.awt.Font("Serif", 1, 36)); // NOI18N
        jLabel68.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel68.setText("Production Cost");
        jPanel10.add(jLabel68, new org.netbeans.lib.awtextra.AbsoluteConstraints(467, 36, 323, 55));

        jPanel28.setBackground(new java.awt.Color(255, 0, 0));
        jPanel28.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(255, 0, 255), 3));

        jLabel69.setBackground(new java.awt.Color(255, 102, 102));
        jLabel69.setFont(new java.awt.Font("Segoe UI", 1, 36)); // NOI18N
        jLabel69.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel69.setText("Total Cost");
        jLabel69.setOpaque(true);

        jLabel70.setFont(new java.awt.Font("Segoe UI", 0, 36)); // NOI18N
        jLabel70.setText("LKR");

        jFormattedTextField1.setEditable(false);
        jFormattedTextField1.setBackground(new java.awt.Color(255, 0, 0));
        jFormattedTextField1.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(255, 0, 0)));
        jFormattedTextField1.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.NumberFormatter(new java.text.DecimalFormat("#0.00"))));
        jFormattedTextField1.setHorizontalAlignment(javax.swing.JTextField.LEFT);
        jFormattedTextField1.setFont(new java.awt.Font("Segoe UI", 0, 36)); // NOI18N

        javax.swing.GroupLayout jPanel28Layout = new javax.swing.GroupLayout(jPanel28);
        jPanel28.setLayout(jPanel28Layout);
        jPanel28Layout.setHorizontalGroup(
            jPanel28Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel28Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel28Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel69, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel28Layout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(jLabel70)
                        .addGap(18, 18, 18)
                        .addComponent(jFormattedTextField1, javax.swing.GroupLayout.PREFERRED_SIZE, 172, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
        );
        jPanel28Layout.setVerticalGroup(
            jPanel28Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel28Layout.createSequentialGroup()
                .addGap(14, 14, 14)
                .addComponent(jLabel69, javax.swing.GroupLayout.PREFERRED_SIZE, 42, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(27, 27, 27)
                .addGroup(jPanel28Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jLabel70, javax.swing.GroupLayout.PREFERRED_SIZE, 42, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jFormattedTextField1, javax.swing.GroupLayout.PREFERRED_SIZE, 42, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(30, Short.MAX_VALUE))
        );

        jPanel10.add(jPanel28, new org.netbeans.lib.awtextra.AbsoluteConstraints(467, 161, 323, -1));

        jPanel29.setBackground(new java.awt.Color(0, 255, 51));
        jPanel29.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 255, 51), 3));

        jLabel71.setBackground(new java.awt.Color(102, 255, 102));
        jLabel71.setFont(new java.awt.Font("Segoe UI", 0, 24)); // NOI18N
        jLabel71.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel71.setText("Worker Cost");
        jLabel71.setOpaque(true);

        jLabel72.setFont(new java.awt.Font("Segoe UI", 0, 24)); // NOI18N
        jLabel72.setText("LKR");

        jFormattedTextField2.setBackground(new java.awt.Color(0, 255, 51));
        jFormattedTextField2.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 255, 51)));
        jFormattedTextField2.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.NumberFormatter(new java.text.DecimalFormat("#0.00"))));
        jFormattedTextField2.setFont(new java.awt.Font("Segoe UI", 0, 24)); // NOI18N

        javax.swing.GroupLayout jPanel29Layout = new javax.swing.GroupLayout(jPanel29);
        jPanel29.setLayout(jPanel29Layout);
        jPanel29Layout.setHorizontalGroup(
            jPanel29Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel29Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel29Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel71, javax.swing.GroupLayout.DEFAULT_SIZE, 250, Short.MAX_VALUE)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel29Layout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(jLabel72)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jFormattedTextField2, javax.swing.GroupLayout.PREFERRED_SIZE, 129, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
        );
        jPanel29Layout.setVerticalGroup(
            jPanel29Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel29Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel71, javax.swing.GroupLayout.PREFERRED_SIZE, 29, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addGroup(jPanel29Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel72, javax.swing.GroupLayout.DEFAULT_SIZE, 37, Short.MAX_VALUE)
                    .addComponent(jFormattedTextField2))
                .addGap(16, 16, 16))
        );

        jPanel10.add(jPanel29, new org.netbeans.lib.awtextra.AbsoluteConstraints(324, 422, -1, -1));

        jPanel34.setBackground(new java.awt.Color(0, 255, 255));
        jPanel34.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 255, 255), 3));

        jLabel73.setBackground(new java.awt.Color(102, 255, 255));
        jLabel73.setFont(new java.awt.Font("Segoe UI", 0, 24)); // NOI18N
        jLabel73.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel73.setText("Raw Material Cost");
        jLabel73.setOpaque(true);

        jLabel74.setFont(new java.awt.Font("Segoe UI", 0, 24)); // NOI18N
        jLabel74.setText("LKR");

        jFormattedTextField3.setBackground(new java.awt.Color(0, 255, 255));
        jFormattedTextField3.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 255, 255)));
        jFormattedTextField3.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.NumberFormatter(new java.text.DecimalFormat("#0.00"))));
        jFormattedTextField3.setFont(new java.awt.Font("Segoe UI", 0, 24)); // NOI18N

        javax.swing.GroupLayout jPanel34Layout = new javax.swing.GroupLayout(jPanel34);
        jPanel34.setLayout(jPanel34Layout);
        jPanel34Layout.setHorizontalGroup(
            jPanel34Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel34Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel34Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel73, javax.swing.GroupLayout.DEFAULT_SIZE, 245, Short.MAX_VALUE)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel34Layout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(jLabel74)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jFormattedTextField3, javax.swing.GroupLayout.PREFERRED_SIZE, 129, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
        );
        jPanel34Layout.setVerticalGroup(
            jPanel34Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel34Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel73, javax.swing.GroupLayout.PREFERRED_SIZE, 29, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addGroup(jPanel34Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jLabel74, javax.swing.GroupLayout.DEFAULT_SIZE, 37, Short.MAX_VALUE)
                    .addComponent(jFormattedTextField3))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jPanel10.add(jPanel34, new org.netbeans.lib.awtextra.AbsoluteConstraints(669, 422, -1, 112));

        javax.swing.GroupLayout jPanel35Layout = new javax.swing.GroupLayout(jPanel35);
        jPanel35.setLayout(jPanel35Layout);
        jPanel35Layout.setHorizontalGroup(
            jPanel35Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 312, Short.MAX_VALUE)
        );
        jPanel35Layout.setVerticalGroup(
            jPanel35Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 37, Short.MAX_VALUE)
        );

        jPanel10.add(jPanel35, new org.netbeans.lib.awtextra.AbsoluteConstraints(6, 302, -1, -1));

        jSeparator9.setBackground(new java.awt.Color(255, 153, 51));
        jSeparator9.setOpaque(true);
        jPanel10.add(jSeparator9, new org.netbeans.lib.awtextra.AbsoluteConstraints(708, 328, 11, 88));

        jSeparator10.setBackground(new java.awt.Color(255, 153, 51));
        jSeparator10.setOpaque(true);
        jPanel10.add(jSeparator10, new org.netbeans.lib.awtextra.AbsoluteConstraints(544, 328, 11, 88));

        jTabbedPane1.addTab("Production Cost", jPanel10);

        jPanel11.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel75.setFont(new java.awt.Font("Serif", 1, 36)); // NOI18N
        jLabel75.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel75.setText("Production Issues");
        jPanel11.add(jLabel75, new org.netbeans.lib.awtextra.AbsoluteConstraints(567, 16, 284, 55));

        jTable10.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "issue id", "description", "issue type", "issue date", "employee NIC"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jTable10.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jTable10MouseClicked(evt);
            }
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                jTable10MouseReleased(evt);
            }
        });
        jScrollPane11.setViewportView(jTable10);

        jPanel11.add(jScrollPane11, new org.netbeans.lib.awtextra.AbsoluteConstraints(291, 111, 748, 191));

        jLabel76.setFont(new java.awt.Font("Serif", 0, 18)); // NOI18N
        jLabel76.setText("Issue Id");
        jPanel11.add(jLabel76, new org.netbeans.lib.awtextra.AbsoluteConstraints(316, 346, 100, 25));

        jTextField5.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jPanel11.add(jTextField5, new org.netbeans.lib.awtextra.AbsoluteConstraints(316, 377, 100, -1));

        jLabel77.setFont(new java.awt.Font("Serif", 0, 18)); // NOI18N
        jLabel77.setText("Issue Type");
        jPanel11.add(jLabel77, new org.netbeans.lib.awtextra.AbsoluteConstraints(576, 346, 166, 25));

        jPanel11.add(jComboBox4, new org.netbeans.lib.awtextra.AbsoluteConstraints(576, 377, 132, 26));

        jLabel78.setFont(new java.awt.Font("Serif", 0, 18)); // NOI18N
        jLabel78.setText("Issue Date");
        jPanel11.add(jLabel78, new org.netbeans.lib.awtextra.AbsoluteConstraints(902, 348, 137, 25));

        jTextField8.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jPanel11.add(jTextField8, new org.netbeans.lib.awtextra.AbsoluteConstraints(902, 379, 137, -1));

        jLabel79.setFont(new java.awt.Font("Serif", 0, 18)); // NOI18N
        jLabel79.setText("Description");
        jPanel11.add(jLabel79, new org.netbeans.lib.awtextra.AbsoluteConstraints(316, 469, 492, 25));

        jTextArea2.setColumns(20);
        jTextArea2.setRows(5);
        jScrollPane12.setViewportView(jTextArea2);

        jPanel11.add(jScrollPane12, new org.netbeans.lib.awtextra.AbsoluteConstraints(316, 514, 436, 100));

        jButton25.setBackground(new java.awt.Color(255, 255, 51));
        jButton25.setFont(new java.awt.Font("Segoe UI Black", 1, 24)); // NOI18N
        jButton25.setText("+");
        jButton25.setAlignmentY(0.0F);
        jButton25.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0), 2));
        jButton25.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        jButton25.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton25ActionPerformed(evt);
            }
        });
        jPanel11.add(jButton25, new org.netbeans.lib.awtextra.AbsoluteConstraints(714, 377, -1, 28));

        jButton8.setFont(new java.awt.Font("Segoe UI Black", 0, 14)); // NOI18N
        jButton8.setText("Report issue");
        jButton8.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        jButton8.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton8ActionPerformed(evt);
            }
        });
        jPanel11.add(jButton8, new org.netbeans.lib.awtextra.AbsoluteConstraints(836, 469, 203, 33));

        jButton21.setFont(new java.awt.Font("Segoe UI Black", 0, 14)); // NOI18N
        jButton21.setText("Issue fixed");
        jButton21.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        jButton21.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton21ActionPerformed(evt);
            }
        });
        jPanel11.add(jButton21, new org.netbeans.lib.awtextra.AbsoluteConstraints(836, 528, 203, 33));

        jButton50.setFont(new java.awt.Font("Segoe UI Black", 0, 14)); // NOI18N
        jButton50.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        jButton50.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton50ActionPerformed(evt);
            }
        });
        jPanel11.add(jButton50, new org.netbeans.lib.awtextra.AbsoluteConstraints(836, 587, 203, 33));

        jTabbedPane1.addTab("Issues", jPanel11);

        jPanel12.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel80.setFont(new java.awt.Font("Serif", 1, 36)); // NOI18N
        jLabel80.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel80.setText("Fruit Registration");
        jPanel12.add(jLabel80, new org.netbeans.lib.awtextra.AbsoluteConstraints(385, 17, 284, 55));

        jPanel36.setBackground(new java.awt.Color(255, 102, 51));

        jTable11.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "fruit id", "fruit name"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jTable11.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                jTable11MouseReleased(evt);
            }
        });
        jScrollPane13.setViewportView(jTable11);
        if (jTable11.getColumnModel().getColumnCount() > 0) {
            jTable11.getColumnModel().getColumn(0).setPreferredWidth(15);
        }

        javax.swing.GroupLayout jPanel36Layout = new javax.swing.GroupLayout(jPanel36);
        jPanel36.setLayout(jPanel36Layout);
        jPanel36Layout.setHorizontalGroup(
            jPanel36Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel36Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane13, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel36Layout.setVerticalGroup(
            jPanel36Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel36Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane13, javax.swing.GroupLayout.DEFAULT_SIZE, 348, Short.MAX_VALUE)
                .addContainerGap())
        );

        jPanel12.add(jPanel36, new org.netbeans.lib.awtextra.AbsoluteConstraints(291, 172, -1, -1));

        jLabel81.setFont(new java.awt.Font("Serif", 0, 18)); // NOI18N
        jLabel81.setText("Fruit Id");
        jPanel12.add(jLabel81, new org.netbeans.lib.awtextra.AbsoluteConstraints(41, 211, 163, 25));

        jTextField9.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jPanel12.add(jTextField9, new org.netbeans.lib.awtextra.AbsoluteConstraints(41, 242, 163, -1));

        jLabel82.setFont(new java.awt.Font("Serif", 0, 18)); // NOI18N
        jLabel82.setText("Fruit Name");
        jPanel12.add(jLabel82, new org.netbeans.lib.awtextra.AbsoluteConstraints(41, 313, 163, 25));

        jTextField11.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jTextField11.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                jTextField11KeyTyped(evt);
            }
        });
        jPanel12.add(jTextField11, new org.netbeans.lib.awtextra.AbsoluteConstraints(41, 344, 163, -1));

        jButton26.setFont(new java.awt.Font("Segoe UI Black", 0, 14)); // NOI18N
        jButton26.setText("Add");
        jButton26.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        jButton26.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton26ActionPerformed(evt);
            }
        });
        jPanel12.add(jButton26, new org.netbeans.lib.awtextra.AbsoluteConstraints(41, 460, 163, 33));

        jButton27.setFont(new java.awt.Font("Segoe UI Black", 0, 14)); // NOI18N
        jButton27.setText("Update");
        jButton27.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        jButton27.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton27ActionPerformed(evt);
            }
        });
        jPanel12.add(jButton27, new org.netbeans.lib.awtextra.AbsoluteConstraints(41, 499, 163, 33));

        jButton42.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        jButton42.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton42ActionPerformed(evt);
            }
        });
        jPanel12.add(jButton42, new org.netbeans.lib.awtextra.AbsoluteConstraints(713, 117, 44, 43));

        jTextField3.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jTextField3.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                jTextField3KeyReleased(evt);
            }
        });
        jPanel12.add(jTextField3, new org.netbeans.lib.awtextra.AbsoluteConstraints(428, 127, 154, 33));

        jTabbedPane1.addTab("Fruit Registration", jPanel12);

        jPanel1.add(jTabbedPane1, java.awt.BorderLayout.CENTER);

        add(jPanel1, java.awt.BorderLayout.CENTER);
    }// </editor-fold>//GEN-END:initComponents

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed

        try {
            String plantationId = jTextField6.getText();
            String fruit = String.valueOf(jComboBox1.getSelectedItem());
            String qty = jTextField1.getText();
            String yield = String.valueOf(jComboBox5.getSelectedItem());
            String dateTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());

            Pattern pattern = Pattern.compile("[a-zA-Z\\s]+");
            Pattern pattern1 = Pattern.compile("^[1-9][0-9]*$");

            if (fruit.equals("Select")) {
                JOptionPane.showMessageDialog(this, "Please select a fruit", "Warning", JOptionPane.WARNING_MESSAGE, JOptionIcon.WARNING.setICon());
            } else if (yield.equals("Select")) {
                JOptionPane.showMessageDialog(this, "Please select a yield location", "Warning", JOptionPane.WARNING_MESSAGE, JOptionIcon.WARNING.setICon());
            } else if (!pattern.matcher(yield).matches()) {
                JOptionPane.showMessageDialog(this, "Yield location should contains only letters and spaces.", "Invalid Input", JOptionPane.WARNING_MESSAGE, JOptionIcon.WARNING.setICon());
            } else if (!pattern1.matcher(qty).matches()) {
                JOptionPane.showMessageDialog(this, "Please input a valid integer quantity", "Invalid Input", JOptionPane.WARNING_MESSAGE, JOptionIcon.WARNING.setICon());
                jTextField1.setText("");
                jTextField1.grabFocus();
            } else {

                ResultSet resultSet = MySQL.executeSearch("SELECT * FROM `plantation` "
                        + "INNER JOIN `fruit` ON `plantation`.`fruit_fruit_id` = `fruit`.`fruit_id` "
                        + "INNER JOIN `yield_location` ON `plantation`.`yield_location_id` = `yield_location`.`yield_location_id` "
                        + "WHERE `plantation_id` = '" + plantationId + "' AND `yield_location` = '" + yield + "' AND `fruit_name` = '" + fruit + "' AND `qty` = '" + qty + "'");

                if (resultSet.next()) {
                    JOptionPane.showMessageDialog(this, "This plantation is already registered", "Warning", JOptionPane.WARNING_MESSAGE, JOptionIcon.WARNING.setICon());
                } else {
                    MySQL.executeIUD("UPDATE `plantation` SET `plantation_id` = '" + plantationId + "', "
                            + "`date` = '" + dateTime + "', `qty` = '" + qty + "', `fruit_fruit_id` = '" + fruitMap.get(fruit) + "', `yield_location_id` = '" + yieldMap.get(yield) + "' "
                            + "WHERE `plantation_id` = '" + plantationId + "'");

                    loadPlantationTable("qty", "ASC");
                    reset2();
                    loadProgressTable("");
                    loadRawTable("qty", "ASC", "", "");
                    loadStockTable("total_weight", "ASC", "", "");

                    JOptionPane.showMessageDialog(this, "Plantation updated successfully", "Success", JOptionPane.PLAIN_MESSAGE, JOptionIcon.SUCCESS.setICon());
                }
            }
        } catch (Exception e) {
            SignIn.getLoggerObject().log(Level.WARNING, "plantation Update Exception found!", e);
        }
    }//GEN-LAST:event_jButton2ActionPerformed

    private void jButton14ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton14ActionPerformed
        try {
            String plantationId = jTextField14.getText();
            String materialName = jTextField18.getText();
            Pattern pattern = Pattern.compile("[a-zA-Z\\s]+");

            if (plantationId.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Please enter plantation Id", "Warning", JOptionPane.WARNING_MESSAGE, JOptionIcon.WARNING.setICon());
                return;
            }
            if (materialName.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Please select a raw material", "Warning", JOptionPane.WARNING_MESSAGE, JOptionIcon.WARNING.setICon());
                return;
            }
            if (!pattern.matcher(materialName).matches()) {
                JOptionPane.showMessageDialog(this, "Please enter only letters and spaces for raw material", "Invalid Input", JOptionPane.WARNING_MESSAGE, JOptionIcon.WARNING.setICon());
                return;
            }
            if (jTextField10.getText().isEmpty()) {
                JOptionPane.showMessageDialog(this, "Please select a plantation first", "Warning", JOptionPane.WARNING_MESSAGE, JOptionIcon.WARNING.setICon());
                return;
            }

            ResultSet rawMaterialCheck = MySQL.executeSearch("SELECT `raw_material_id` FROM `raw_material` WHERE `raw_material_name` = '" + materialName + "'");
            if (rawMaterialCheck.next()) {
                String rawMaterialId = rawMaterialCheck.getString("raw_material_id");

                ResultSet plantationCheck = MySQL.executeSearch("SELECT * FROM `plantation` WHERE `plantation_id` = '" + plantationId + "'");
                if (plantationCheck.next()) {

                    ResultSet existingAssignmentCheck = MySQL.executeSearch("SELECT * FROM `plantation_has_raw_material` "
                            + "WHERE `plantation_plantation_id` = '" + plantationId + "' AND `raw_material_raw_material_id` = '" + rawMaterialId + "'");

                    if (existingAssignmentCheck.next()) {
                        JOptionPane.showMessageDialog(this, "This raw material for this plantation is already registered", "Warning", JOptionPane.WARNING_MESSAGE, JOptionIcon.WARNING.setICon());
                    } else {

                        MySQL.executeIUD("INSERT INTO `plantation_has_raw_material`(`plantation_plantation_id`, `raw_material_raw_material_id`) "
                                + "VALUES('" + plantationId + "', '" + rawMaterialId + "')");

                        reset8();
                        JOptionPane.showMessageDialog(this, "Raw material added to relative plantation successfully", "Success", JOptionPane.PLAIN_MESSAGE, JOptionIcon.SUCCESS.setICon());
                    }
                } else {
                    JOptionPane.showMessageDialog(this, "Plantation ID does not exist. Please enter a valid Plantation ID.", "Warning", JOptionPane.WARNING_MESSAGE, JOptionIcon.WARNING.setICon());
                }
            } else {
                JOptionPane.showMessageDialog(this, "Raw material not found. Please register the raw material first.", "Warning", JOptionPane.WARNING_MESSAGE, JOptionIcon.WARNING.setICon());
            }
        } catch (Exception e) {
            SignIn.getLoggerObject().log(Level.WARNING, "raw material setting to plantation Exception found!", e);
        }
    }//GEN-LAST:event_jButton14ActionPerformed

    private void jButton17ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton17ActionPerformed
        String nic = SignIn.getEmployeeNIC();
        String dateTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
        int rowCount = jTable7.getRowCount();

        if (nic.isEmpty()) {
            JOptionPane.showMessageDialog(null, "Employee should logged in to process", "Error", JOptionPane.ERROR_MESSAGE, JOptionIcon.ERROR.setICon());
        } else if (rowCount == 0) {
            JOptionPane.showMessageDialog(null, "The inventory management table is empty!", "No Data", JOptionPane.ERROR_MESSAGE, JOptionIcon.ERROR.setICon());
        } else {
            try {

                //view 
                InputStream s = this.getClass().getResourceAsStream("/lk/corevision/fruitshop/reports/production/fruitShopAppInventory.jasper");

                HashMap<String, Object> params = new HashMap<>();
                params.put("Parameter1", dateTime);
                params.put("Parameter2", nic);
                params.put("Parameter3", String.valueOf(rowCount));

                JRTableModelDataSource dataSource = new JRTableModelDataSource(jTable7.getModel());

                JasperPrint jasperPrint = JasperFillManager.fillReport(s, params, dataSource);

                JasperViewer.viewReport(jasperPrint, false);
            } catch (JRException e) {
                SignIn.getLoggerObject().log(Level.WARNING, "inventory jasper report Exception found!", e);
            }
        }
    }//GEN-LAST:event_jButton17ActionPerformed

    private void jButton21ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton21ActionPerformed
        JOptionPane optionPane = new JOptionPane(
                "Are you sure production issue is fixed?",
                JOptionPane.QUESTION_MESSAGE,
                JOptionPane.YES_NO_OPTION
        );

        JDialog dialog = optionPane.createDialog(this, "Confirmation");
        dialog.setLocation(610, 330);
        dialog.setVisible(true);

        // Capture the result of the user's choice
        Object selectedValue = optionPane.getValue();

        if (selectedValue != null) {
            int result = (int) selectedValue;

            if (result == JOptionPane.YES_OPTION) {
                try {
                    String issueId = jTextField5.getText();
                    MySQL.executeIUD("DELETE FROM `issue` WHERE `issue_id` = '" + issueId + "'");
                    reset9();

                    JOptionPane.showMessageDialog(this, "Production issue fixed.", "Success", JOptionPane.PLAIN_MESSAGE, JOptionIcon.SUCCESS.setICon());

                    String to = "sheshandanuksha19@gmail.com";
                    String subject = "Production Issue Fixed";
                    String messageText = "The previous production issue has been fixed.\n\n"
                            + "Do not reply to this automated message.";

                    try {
                        SendEmail.sendEmail(to, subject, messageText);
                        JOptionPane.showMessageDialog(null, "Email sent successfully.", "Success", JOptionPane.PLAIN_MESSAGE, JOptionIcon.SUCCESS.setICon());
                    } catch (Exception e) {
                        SignIn.getLoggerObject().log(Level.WARNING, "production issue fixed email sending Exception found!", e);
                        JOptionPane.showMessageDialog(null, "Unable to send email.", "Error", JOptionPane.ERROR_MESSAGE, JOptionIcon.ERROR.setICon());
                    }

                } catch (Exception e) {
                    SignIn.getLoggerObject().log(Level.WARNING, "production issue Delete Exception found!", e);
                }

            } else if (result == JOptionPane.NO_OPTION) {
                reset9();
            }
        }
    }//GEN-LAST:event_jButton21ActionPerformed

    private void jButton27ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton27ActionPerformed

        try {
            String fruitId = jTextField9.getText();
            String fruit = jTextField11.getText();
            Pattern pattern = Pattern.compile("[a-zA-Z\\s]+");

            if (fruit.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Please enter a fruit name", "Warning", JOptionPane.WARNING_MESSAGE, JOptionIcon.WARNING.setICon());
            } else if (!pattern.matcher(fruit).matches()) {
                JOptionPane.showMessageDialog(this, "Please enter only letters and spaces.", "Invalid Input", JOptionPane.WARNING_MESSAGE, JOptionIcon.WARNING.setICon());
            } else {

                ResultSet resultSet = MySQL.executeSearch("SELECT * FROM `fruit` WHERE `fruit_name` = '" + fruit + "' AND `fruit_id` = '" + fruitId + "'");

                if (resultSet.next()) {
                    JOptionPane.showMessageDialog(this, "This fruit is already registered.", "Warning", JOptionPane.WARNING_MESSAGE, JOptionIcon.WARNING.setICon());
                } else {
                    MySQL.executeIUD("UPDATE `fruit` SET `fruit_id` = '" + fruitId + "', "
                            + "`fruit_name` = '" + fruit + "' "
                            + "WHERE `fruit_id` = '" + fruitId + "'");

                    loadFruits();
                    reset1();
                    loadPlantationTable("qty", "ASC");
                    loadRawTable("qty", "ASC", "", "");
                    loadYieldAnalyticsLabels();
                    loadYieldAnalyticsTable();
                    loadProgressTable("");
                    loadStockTable("total_weight", "ASC", "", "");
                    loadPieChart();

                    JOptionPane.showMessageDialog(this, "Fruit name updated successfully", "Success", JOptionPane.PLAIN_MESSAGE, JOptionIcon.SUCCESS.setICon());
                }
            }

        } catch (Exception e) {
            SignIn.getLoggerObject().log(Level.WARNING, "fruit name Update Exception found!", e);
        }
    }//GEN-LAST:event_jButton27ActionPerformed

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        Yield yield = new Yield(dashboard, true, this);
        yield.setVisible(true);
    }//GEN-LAST:event_jButton1ActionPerformed

    private void jButton9ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton9ActionPerformed
        Task task = new Task(dashboard, true, this);
        task.setVisible(true);
    }//GEN-LAST:event_jButton9ActionPerformed

    private void jButton24ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton24ActionPerformed
        TaskStatus status = new TaskStatus(dashboard, true, this);
        status.setVisible(true);
    }//GEN-LAST:event_jButton24ActionPerformed

    private void jButton25ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton25ActionPerformed
        IssueType type = new IssueType(dashboard, true, this);
        type.setVisible(true);
    }//GEN-LAST:event_jButton25ActionPerformed

    private void jButton6ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton6ActionPerformed
        jButton6.setEnabled(false);
        String text = jTextField6.getText();

        Toolkit.getDefaultToolkit().getSystemClipboard().setContents(new StringSelection(text), null);
        Notifications.getInstance().show(Notifications.Type.SUCCESS, Notifications.Location.BOTTOM_RIGHT, "Copied");
        jButton6.setEnabled(true);
    }//GEN-LAST:event_jButton6ActionPerformed

    private void jButton29ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton29ActionPerformed
        jButton29.setEnabled(false);
        String text = jTextField4.getText();

        if (text.isEmpty()) {
            Notifications.getInstance().show(Notifications.Type.ERROR, Notifications.Location.BOTTOM_RIGHT, "Worker NIC is empty");

        } else {
            Toolkit.getDefaultToolkit().getSystemClipboard().setContents(new StringSelection(text), null);
            Notifications.getInstance().show(Notifications.Type.SUCCESS, Notifications.Location.BOTTOM_RIGHT, "Copied");
            jButton29.setEnabled(true);
        }
    }//GEN-LAST:event_jButton29ActionPerformed

    private void jButton30ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton30ActionPerformed
        jButton30.setEnabled(false);
        String text = jTextField19.getText();

        Toolkit.getDefaultToolkit().getSystemClipboard().setContents(new StringSelection(text), null);
        Notifications.getInstance().show(Notifications.Type.SUCCESS, Notifications.Location.BOTTOM_RIGHT, "Copied");
        jButton30.setEnabled(true);
    }//GEN-LAST:event_jButton30ActionPerformed

    private void jButton31ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton31ActionPerformed
        try {
            String pastedText = (String) Toolkit.getDefaultToolkit().getSystemClipboard().getData(DataFlavor.stringFlavor);
            jTextField2.setText(pastedText);

            if (!jTextField2.getText().isEmpty()) {
                Notifications.getInstance().show(Notifications.Type.SUCCESS, Notifications.Location.BOTTOM_RIGHT, "Pasted");
                loadProgressTable(jTextField2.getText());
            }
        } catch (Exception e) {
            Notifications.getInstance().show(Notifications.Type.ERROR, Notifications.Location.BOTTOM_RIGHT, "Failed to paste");
        }
    }//GEN-LAST:event_jButton31ActionPerformed

    private void jButton32ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton32ActionPerformed
        try {
            String pastedText = (String) Toolkit.getDefaultToolkit().getSystemClipboard().getData(DataFlavor.stringFlavor);
            jTextField20.setText(pastedText);

            if (!jTextField20.getText().isEmpty()) {
                Notifications.getInstance().show(Notifications.Type.SUCCESS, Notifications.Location.BOTTOM_RIGHT, "Pasted");
                search3();
            }
        } catch (Exception e) {
            Notifications.getInstance().show(Notifications.Type.ERROR, Notifications.Location.BOTTOM_RIGHT, "Failed to paste");
        }
    }//GEN-LAST:event_jButton32ActionPerformed

    private void jButton33ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton33ActionPerformed
        try {
            String pastedText = (String) Toolkit.getDefaultToolkit().getSystemClipboard().getData(DataFlavor.stringFlavor);

            if (pastedText.matches("\\d+")) {
                jTextField13.setText(pastedText);
                search2();
                Notifications.getInstance().show(Notifications.Type.SUCCESS, Notifications.Location.BOTTOM_RIGHT, "Pasted");
            } else {
                JOptionPane.showMessageDialog(this, "Paste digits only.", "Invalid Input", JOptionPane.WARNING_MESSAGE, JOptionIcon.WARNING.setICon());
            }
        } catch (Exception e) {
            Notifications.getInstance().show(Notifications.Type.ERROR, Notifications.Location.BOTTOM_RIGHT, "Failed to paste");
        }
    }//GEN-LAST:event_jButton33ActionPerformed

    private void jButton34ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton34ActionPerformed
        try {
            String pastedText = (String) Toolkit.getDefaultToolkit().getSystemClipboard().getData(DataFlavor.stringFlavor);

            if (pastedText.matches("\\d+")) {
                jTextField14.setText(pastedText);
                Notifications.getInstance().show(Notifications.Type.SUCCESS, Notifications.Location.BOTTOM_RIGHT, "Pasted");
                search4();
                loadMaterialTable(jTextField14.getText(), jTextField18.getText());
            } else {
                JOptionPane.showMessageDialog(this, "Paste digits only.", "Invalid Input", JOptionPane.WARNING_MESSAGE, JOptionIcon.WARNING.setICon());
            }
        } catch (Exception e) {
            Notifications.getInstance().show(Notifications.Type.ERROR, Notifications.Location.BOTTOM_RIGHT, "Failed to paste");
        }
    }//GEN-LAST:event_jButton34ActionPerformed

    private void jButton3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton3ActionPerformed
        try {
            String plantationId = jTextField6.getText();
            String fruit = String.valueOf(jComboBox1.getSelectedItem());
            String qty = jTextField1.getText();
            String yield = String.valueOf(jComboBox5.getSelectedItem());
            String dateTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());

            Pattern pattern = Pattern.compile("[a-zA-Z\\s]+");
            Pattern pattern1 = Pattern.compile("^[1-9][0-9]*$");

            if (fruit.equals("Select")) {
                JOptionPane.showMessageDialog(this, "Please select a fruit", "Invalid Input", JOptionPane.WARNING_MESSAGE, JOptionIcon.WARNING.setICon());
            } else if (yield.equals("Select")) {
                JOptionPane.showMessageDialog(this, "Please select a yield location", "Warning", JOptionPane.WARNING_MESSAGE, JOptionIcon.WARNING.setICon());
            } else if (!pattern.matcher(yield).matches()) {
                JOptionPane.showMessageDialog(this, "Yield location should contains only letters and spaces.", "Invalid Input", JOptionPane.WARNING_MESSAGE, JOptionIcon.WARNING.setICon());
            } else if (!pattern1.matcher(qty).matches()) {
                JOptionPane.showMessageDialog(this, "Please input a valid integer quantity", "Invalid Input", JOptionPane.WARNING_MESSAGE, JOptionIcon.WARNING.setICon());
                jTextField1.setText("");
                jTextField1.grabFocus();
            } else {

                ResultSet resultSet = MySQL.executeSearch("SELECT * FROM `plantation` "
                        + "INNER JOIN `fruit` ON `plantation`.`fruit_fruit_id` = `fruit`.`fruit_id` "
                        + "INNER JOIN `yield_location` ON `plantation`.`yield_location_id` = `yield_location`.`yield_location_id` "
                        + "WHERE `plantation_id` = '" + plantationId + "' AND `yield_location` = '" + yield + "' AND `fruit_name` = '" + fruit + "' AND `qty` = '" + qty + "'");

                if (resultSet.next()) {
                    JOptionPane.showMessageDialog(this, "This plantation is already registered", "Warning", JOptionPane.WARNING_MESSAGE, JOptionIcon.WARNING.setICon());
                } else {

                    MySQL.executeIUD("INSERT INTO `plantation`(`plantation_id`,`date`,`qty`,`fruit_fruit_id`,`yield_location_id`) "
                            + "VALUES('" + plantationId + "','" + dateTime + "','" + qty + "','" + fruitMap.get(fruit) + "','" + yieldMap.get(yield) + "')");

                    loadPlantationTable("qty", "ASC");
                    reset2();
                    loadProgressTable("");
                    loadRawTable("qty", "ASC", "", "");
                    loadStockTable("total_weight", "ASC", "", "");

                    JOptionPane.showMessageDialog(this, "New plantation added successfully", "Success", JOptionPane.PLAIN_MESSAGE, JOptionIcon.SUCCESS.setICon());
                    JOptionPane.showMessageDialog(this, "Please be kind to set relavant raw materials to this plantation.", "Information", JOptionPane.PLAIN_MESSAGE, JOptionIcon.INFO.setICon());
                }
            }
        } catch (Exception e) {
            SignIn.getLoggerObject().log(Level.WARNING, "plantation Add Exception found!", e);
        }
    }//GEN-LAST:event_jButton3ActionPerformed

    private void jButton4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton4ActionPerformed
        try {
            String workerNic = jTextField4.getText();
            String task = String.valueOf(jComboBox3.getSelectedItem());
            String description = jTextArea1.getText();
            String currentDateTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
            String taskStatus = String.valueOf(jComboBox13.getSelectedItem());
            String nic = SignIn.getEmployeeNIC();

            Pattern pattern = Pattern.compile("[a-zA-Z\\s]+");
            Pattern pattern2 = Pattern.compile("[0-9]+([.][0-9]+)?");

            if (workerNic.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Please select an idle worker.", "Warning", JOptionPane.WARNING_MESSAGE, JOptionIcon.WARNING.setICon());
            } else if (description.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Please enter a task description.", "Warning", JOptionPane.WARNING_MESSAGE, JOptionIcon.WARNING.setICon());
            } else if (task.equals("Select")) {
                JOptionPane.showMessageDialog(this, "Please select a task", "Warning", JOptionPane.WARNING_MESSAGE, JOptionIcon.WARNING.setICon());
            } else if (taskStatus.equals("Select")) {
                JOptionPane.showMessageDialog(this, "Please select a task status", "Warning", JOptionPane.WARNING_MESSAGE, JOptionIcon.WARNING.setICon());
            } else if (!pattern.matcher(description).matches()) {
                JOptionPane.showMessageDialog(this, "Description should contains only letters and spaces.", "Invalid Input", JOptionPane.WARNING_MESSAGE, JOptionIcon.WARNING.setICon());
            } else if (!pattern2.matcher(nic).matches()) {
                JOptionPane.showMessageDialog(this, "Employee nic is invalid", "Invalid Input", JOptionPane.WARNING_MESSAGE, JOptionIcon.WARNING.setICon());
            } else if (jDateChooser3.getDate() == null) {
                JOptionPane.showMessageDialog(this, "Please fill due date", "Invalid Input", JOptionPane.WARNING_MESSAGE, JOptionIcon.WARNING.setICon());
            } else if (jDateChooser4.getDate() != null) {
                jDateChooser4.setDate(null);
                JOptionPane.showMessageDialog(this, "Don't fill completion date.", "Invalid Input", JOptionPane.WARNING_MESSAGE, JOptionIcon.WARNING.setICon());
            } else if (tf1.getText().equals("--:--")) {
                tf1.setValue(new Date());
                JOptionPane.showMessageDialog(this, "Please fill due time.", "Invalid Input", JOptionPane.WARNING_MESSAGE, JOptionIcon.WARNING.setICon());
            } else if (!tf.getText().equals("--:--")) {
                tf.setValue(null);
                tf.setText("--:--");
                JOptionPane.showMessageDialog(this, "Don't fill completion time.", "Invalid Input", JOptionPane.WARNING_MESSAGE, JOptionIcon.WARNING.setICon());
            } else {

                ResultSet resultSet = MySQL.executeSearch("SELECT * FROM `task` "
                        + "WHERE `task_name_id` = '" + taskMap.get(task) + "' AND `task_description` = '" + description + "' AND `task_status_id` = '" + taskStatusMap.get(taskStatus) + "'");

                if (resultSet.next()) {
                    JOptionPane.showMessageDialog(this, "This task is already assigned to a worker.", "Warning", JOptionPane.WARNING_MESSAGE, JOptionIcon.WARNING.setICon());
                } else {

                    Timestamp dueTimestamp = getCombinedDateTime(jDateChooser3, jSpinner1);
                    String formattedDueDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(dueTimestamp);

                    MySQL.executeIUD("INSERT INTO `task`(`employee_NIC`,`task_name_id`,`task_description`,`assigned_date`,`due_date`,`task_status_id`,`tasak_assigned_by`) "
                            + "VALUES('" + workerNic + "','" + taskMap.get(task) + "','" + description + "','" + currentDateTime + "','" + formattedDueDate + "','" + taskStatusMap.get(taskStatus) + "','" + nic + "')");

                    reset4();
                    loadIdleWorkerTable();
                    loadTaskAssignedWorkerTable();
                    loadLineChart();

                    JOptionPane.showMessageDialog(this, "Worker assigned to the task successfully", "Success", JOptionPane.PLAIN_MESSAGE, JOptionIcon.SUCCESS.setICon());
                }
            }
        } catch (Exception e) {
            SignIn.getLoggerObject().log(Level.WARNING, "worker task assigning Exception found!", e);
        }
    }//GEN-LAST:event_jButton4ActionPerformed

    private void jButton5ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton5ActionPerformed
        try {
            String taskId = String.valueOf(jTable4.getValueAt(row1, 1));
            String workerNic = jTextField4.getText();
            String task = String.valueOf(jComboBox3.getSelectedItem());
            String description = jTextArea1.getText();
            String currentDateTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
            String taskStatus = String.valueOf(jComboBox13.getSelectedItem());
            String nic = SignIn.getEmployeeNIC();

            Pattern pattern = Pattern.compile("[a-zA-Z\\s]+");
            Pattern pattern2 = Pattern.compile("[0-9]+([.][0-9]+)?");

            if (workerNic.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Please select an idle worker.", "Warning", JOptionPane.WARNING_MESSAGE, JOptionIcon.WARNING.setICon());
            } else if (description.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Please enter a task description.", "Warning", JOptionPane.WARNING_MESSAGE, JOptionIcon.WARNING.setICon());
            } else if (task.equals("Select")) {
                JOptionPane.showMessageDialog(this, "Please select a task", "Warning", JOptionPane.WARNING_MESSAGE, JOptionIcon.WARNING.setICon());
            } else if (taskStatus.equals("Select")) {
                JOptionPane.showMessageDialog(this, "Please select a task status", "Warning", JOptionPane.WARNING_MESSAGE, JOptionIcon.WARNING.setICon());
            } else if (!pattern.matcher(description).matches()) {
                JOptionPane.showMessageDialog(this, "Description should contains only letters and spaces.", "Invalid Input", JOptionPane.WARNING_MESSAGE, JOptionIcon.WARNING.setICon());
            } else if (!pattern2.matcher(nic).matches()) {
                JOptionPane.showMessageDialog(this, "Employee nic is invalid", "Invalid Input", JOptionPane.WARNING_MESSAGE, JOptionIcon.WARNING.setICon());
            } else if (jDateChooser3.getDate() == null) {
                JOptionPane.showMessageDialog(this, "Please fill due date", "Invalid Input", JOptionPane.WARNING_MESSAGE, JOptionIcon.WARNING.setICon());
            } else if (jDateChooser4.getDate() != null) {
                jDateChooser4.setDate(null);
                JOptionPane.showMessageDialog(this, "Don't fill completion date.", "Invalid Input", JOptionPane.WARNING_MESSAGE, JOptionIcon.WARNING.setICon());
            } else if (tf1.getText().equals("--:--")) {
                tf1.setValue(new Date());
                JOptionPane.showMessageDialog(this, "Please fill due time.", "Invalid Input", JOptionPane.WARNING_MESSAGE, JOptionIcon.WARNING.setICon());
            } else if (!tf.getText().equals("--:--")) {
                tf.setValue(null);
                tf.setText("--:--");
                JOptionPane.showMessageDialog(this, "Don't fill completion time.", "Invalid Input", JOptionPane.WARNING_MESSAGE, JOptionIcon.WARNING.setICon());
            } else {

                ResultSet resultSet = MySQL.executeSearch("SELECT * FROM `task` "
                        + "WHERE `task_description` = '" + description + "' AND `task_status_id` = '" + taskStatusMap.get(taskStatus) + "'");

                if (resultSet.next()) {
                    JOptionPane.showMessageDialog(this, "No changes detected.", "Warning", JOptionPane.WARNING_MESSAGE, JOptionIcon.WARNING.setICon());
                } else {

                    Timestamp dueTimestamp = getCombinedDateTime(jDateChooser3, jSpinner1);
                    String formattedDueDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(dueTimestamp);

                    MySQL.executeIUD("UPDATE `task` SET `task_id` = '" + taskId + "', "
                            + "`employee_NIC` = '" + workerNic + "', `task_name_id` = '" + taskMap.get(task) + "', `task_description` = '" + description + "', `assigned_date` = '" + currentDateTime + "', "
                            + "`due_date` = '" + formattedDueDate + "', `task_status_id` = '" + taskStatusMap.get(taskStatus) + "', `tasak_assigned_by` = '" + nic + "' "
                            + "WHERE `task_id` = '" + taskId + "'");

                    reset4();
                    loadIdleWorkerTable();
                    loadTaskAssignedWorkerTable();
                    loadLineChart();

                    JOptionPane.showMessageDialog(this, "Worker's task updated successfully.", "Success", JOptionPane.PLAIN_MESSAGE, JOptionIcon.SUCCESS.setICon());
                }
            }
        } catch (Exception e) {
            SignIn.getLoggerObject().log(Level.WARNING, "worker assigned task Update Exception found!", e);
        }
    }//GEN-LAST:event_jButton5ActionPerformed

    private void jButton18ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton18ActionPerformed
        try {
            String stockId = jTextField19.getText();
            String qty = jTextField15.getText();
            String weight = jTextField16.getText();
            String date = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
            String nic = SignIn.getEmployeeNIC();
            String plantationId = jTextField13.getText();

            Pattern pattern = Pattern.compile("[1-9][0-9]*(\\.\\d+)?");
            Pattern pattern1 = Pattern.compile("^[1-9][0-9]*$");

            if (plantationId.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Please paste a plantation id", "Warning", JOptionPane.WARNING_MESSAGE, JOptionIcon.WARNING.setICon());
            } else if (!pattern1.matcher(qty).matches()) {
                JOptionPane.showMessageDialog(this, "Please input a valid integer quantity", "Invalid Input", JOptionPane.WARNING_MESSAGE, JOptionIcon.WARNING.setICon());
                jTextField15.setText("");
                jTextField15.grabFocus();
            } else if (!pattern.matcher(weight).matches()) {
                JOptionPane.showMessageDialog(this, "Please enter a valid weight (e.g. : 123, 45.67).", "Invalid Input", JOptionPane.WARNING_MESSAGE, JOptionIcon.WARNING.setICon());
            } else {

                ResultSet resultSet = MySQL.executeSearch("SELECT * FROM `inventory` "
                        + "WHERE `plantation_id` = '" + plantationId + "' AND `total_weight` = '" + weight + "' AND `qty` = '" + qty + "' AND `inventory_id` = '" + stockId + "'");

                if (resultSet.next()) {
                    JOptionPane.showMessageDialog(this, "This inventory is already registered", "Warning", JOptionPane.WARNING_MESSAGE, JOptionIcon.WARNING.setICon());
                } else {

                    MySQL.executeIUD("INSERT INTO `inventory`(`inventory_id`,`qty`,`total_weight`,`date`,`employee_NIC`,`plantation_id`) "
                            + "VALUES('" + stockId + "','" + qty + "','" + weight + "','" + date + "','" + nic + "','" + plantationId + "')");

                    reset7();
                    loadProgressTable("");
                    loadYieldAnalyticsLabels();
                    loadYieldAnalyticsTable();
                    loadPieChart();

                    JOptionPane.showMessageDialog(this, "New inventory added successfully", "Success", JOptionPane.PLAIN_MESSAGE, JOptionIcon.SUCCESS.setICon());
                }
            }
        } catch (Exception e) {
            SignIn.getLoggerObject().log(Level.WARNING, "inventory Add Exception found!", e);
        }
    }//GEN-LAST:event_jButton18ActionPerformed

    private void jButton19ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton19ActionPerformed
        try {
            String stockId = jTextField19.getText();
            String qty = jTextField15.getText();
            String weight = jTextField16.getText();
            String date = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
            String nic = SignIn.getEmployeeNIC();
            String plantationId = jTextField13.getText();

            Pattern pattern = Pattern.compile("[1-9][0-9]*(\\.\\d+)?");
            Pattern pattern1 = Pattern.compile("^[1-9][0-9]*$");

            if (plantationId.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Please paste a plantation id", "Warning", JOptionPane.WARNING_MESSAGE, JOptionIcon.WARNING.setICon());
            } else if (!pattern1.matcher(qty).matches()) {
                JOptionPane.showMessageDialog(this, "Please input a valid integer quantity", "Invalid Input", JOptionPane.WARNING_MESSAGE, JOptionIcon.WARNING.setICon());
                jTextField15.setText("");
                jTextField15.grabFocus();
            } else if (!pattern.matcher(weight).matches()) {
                JOptionPane.showMessageDialog(this, "Please enter a valid weight (e.g. : 123, 45.67).", "Invalid Input", JOptionPane.WARNING_MESSAGE, JOptionIcon.WARNING.setICon());
            } else {

                ResultSet resultSet = MySQL.executeSearch("SELECT * FROM `inventory` "
                        + "WHERE `plantation_id` = '" + plantationId + "' AND `total_weight` = '" + weight + "' AND `qty` = '" + qty + "'");

                if (resultSet.next()) {
                    JOptionPane.showMessageDialog(this, "This inventory is already registered", "Warning", JOptionPane.WARNING_MESSAGE, JOptionIcon.WARNING.setICon());
                } else {
                    MySQL.executeIUD("UPDATE `inventory` SET `inventory_id` = '" + stockId + "', "
                            + "`qty` = '" + qty + "', "
                            + "`total_weight` = '" + weight + "', "
                            + "`date` = '" + date + "', "
                            + "`employee_NIC` = '" + nic + "', "
                            + "`plantation_id` = '" + plantationId + "' "
                            + "WHERE `inventory_id` = '" + stockId + "'");

                    reset7();
                    loadProgressTable("");
                    loadYieldAnalyticsLabels();
                    loadYieldAnalyticsTable();
                    loadPieChart();

                    JOptionPane.showMessageDialog(this, "Inventory updated successfully", "Success", JOptionPane.PLAIN_MESSAGE, JOptionIcon.SUCCESS.setICon());

                }
            }
        } catch (Exception e) {
            SignIn.getLoggerObject().log(Level.WARNING, "inventory Update Exception found!", e);
        }
    }//GEN-LAST:event_jButton19ActionPerformed

    private void jButton15ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton15ActionPerformed
        try {
            String plantationId = jTextField14.getText();
            String rawId = jTextField17.getText();
            String materialName = jTextField18.getText();
            Pattern pattern = Pattern.compile("[a-zA-Z\\s]+");

            if (plantationId.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Please enter plantation Id", "Invalid Input", JOptionPane.WARNING_MESSAGE, JOptionIcon.WARNING.setICon());
            } else if (jTextField10.getText().isEmpty()) {
                JOptionPane.showMessageDialog(this, "Please select the plantation Id", "Invalid Input", JOptionPane.WARNING_MESSAGE, JOptionIcon.WARNING.setICon());
            } else if (!pattern.matcher(materialName).matches()) {
                JOptionPane.showMessageDialog(this, "Please enter only letters and spaces for raw material", "Invalid Input", JOptionPane.WARNING_MESSAGE, JOptionIcon.WARNING.setICon());
            } else {

                ResultSet resultSet = MySQL.executeSearch("SELECT * FROM `plantation_has_raw_material` "
                        + "INNER JOIN `raw_material` ON `plantation_has_raw_material`.`raw_material_raw_material_id` = `raw_material`.`raw_material_id` "
                        + "WHERE `plantation_plantation_id` = '" + plantationId + "' AND `raw_material_name` = '" + materialName + "'");

                if (resultSet.next()) {
                    JOptionPane.showMessageDialog(this, "This raw material for this plantation is already registered", "Warning", JOptionPane.WARNING_MESSAGE, JOptionIcon.WARNING.setICon());
                } else {

                    MySQL.executeIUD("UPDATE `raw_material` SET `raw_material_id` = '" + rawId + "', "
                            + "`raw_material_name` = '" + materialName + "' "
                            + "WHERE `raw_material_id` = '" + rawId + "'");

                    MySQL.executeIUD("UPDATE `plantation_has_raw_material` SET `plantation_plantation_id` = '" + plantationId + "', "
                            + "`raw_material_raw_material_id` = '" + rawId + "' "
                            + "WHERE `plantation_plantation_id` = '" + plantationId + "' AND `raw_material_raw_material_id` = '" + rawId + "'");

                    reset8();
                    JOptionPane.showMessageDialog(this, "Raw material updated successfully.", "Success", JOptionPane.PLAIN_MESSAGE, JOptionIcon.SUCCESS.setICon());
                }
            }
        } catch (Exception e) {
            SignIn.getLoggerObject().log(Level.WARNING, "raw material Update Exception found!", e);
        }
    }//GEN-LAST:event_jButton15ActionPerformed

    private void jButton23ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton23ActionPerformed
        String plantationId = jTextField14.getText();

        if (plantationId.isEmpty()) {
            reset8();
            JOptionPane.showMessageDialog(this, "Please paste the plantation Id", "Invalid Input", JOptionPane.WARNING_MESSAGE, JOptionIcon.WARNING.setICon());
        } else {

            JOptionPane optionPane = new JOptionPane(
                    "Do you want to delete this raw material from relavant plantation?",
                    JOptionPane.QUESTION_MESSAGE,
                    JOptionPane.YES_NO_OPTION
            );

            JDialog dialog = optionPane.createDialog(this, "Confirmation");
            dialog.setLocation(610, 330);
            dialog.setVisible(true);

            // Capture the result of the user's choice
            Object selectedValue = optionPane.getValue();

            if (selectedValue != null) {
                int result = (int) selectedValue;

                if (result == JOptionPane.YES_OPTION) {
                    try {
                        String rawId = jTextField17.getText();

                        MySQL.executeIUD("DELETE FROM `plantation_has_raw_material` WHERE `raw_material_raw_material_id` = '" + rawId + "' AND `plantation_plantation_id` = '" + plantationId + "'");

                        reset8();
                        JOptionPane.showMessageDialog(this, "Raw material removed from relative plantation successfully.", "Success", JOptionPane.PLAIN_MESSAGE, JOptionIcon.SUCCESS.setICon());
                    } catch (Exception e) {
                        SignIn.getLoggerObject().log(Level.WARNING, "raw material removing from plantation Exception found!", e);
                    }

                } else if (result == JOptionPane.NO_OPTION) {
                    reset8();
                }
            }
        }
    }//GEN-LAST:event_jButton23ActionPerformed

    private void jButton26ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton26ActionPerformed

        try {
            String fruit = jTextField11.getText();
            Pattern pattern = Pattern.compile("[a-zA-Z\\s]+");

            if (fruit.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Please enter a fruit name", "Warning", JOptionPane.WARNING_MESSAGE, JOptionIcon.WARNING.setICon());
            } else if (!pattern.matcher(fruit).matches()) {
                JOptionPane.showMessageDialog(this, "Please enter only letters and spaces.", "Invalid Input", JOptionPane.WARNING_MESSAGE, JOptionIcon.WARNING.setICon());
            } else {

                ResultSet resultSet = MySQL.executeSearch("SELECT * FROM `fruit` WHERE `fruit_name` = '" + fruit + "'");

                if (resultSet.next()) {
                    JOptionPane.showMessageDialog(this, "This fruit is already registered", "Warning", JOptionPane.WARNING_MESSAGE, JOptionIcon.WARNING.setICon());
                } else {
                    MySQL.executeIUD("INSERT INTO `fruit`(`fruit_name`) VALUES('" + fruit + "')");

                    loadFruits();
                    reset1();

                    JOptionPane.showMessageDialog(this, "New fruit added successfully", "Success", JOptionPane.PLAIN_MESSAGE, JOptionIcon.SUCCESS.setICon());
                }
            }
        } catch (Exception e) {
            SignIn.getLoggerObject().log(Level.WARNING, "fruit name Add Exception found!", e);
        }
    }//GEN-LAST:event_jButton26ActionPerformed

    private void jButton8ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton8ActionPerformed
        try {

            String issueType = String.valueOf(jComboBox4.getSelectedItem());
            String date = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
            String description = jTextArea2.getText();
            String nic = SignIn.getEmployeeNIC();

            Pattern pattern = Pattern.compile("[a-zA-Z\\s]+");

            if (issueType.equals("Select")) {
                JOptionPane.showMessageDialog(this, "Please select an issue type", "Warning", JOptionPane.WARNING_MESSAGE, JOptionIcon.WARNING.setICon());
            } else if (!pattern.matcher(description).matches()) {
                JOptionPane.showMessageDialog(this, "Description should contains only letters and spaces.", "Invalid Input", JOptionPane.WARNING_MESSAGE, JOptionIcon.WARNING.setICon());
            } else if (description.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Please enter a description about the issue.", "Invalid Input", JOptionPane.WARNING_MESSAGE, JOptionIcon.WARNING.setICon());
            } else {

                ResultSet resultSet = MySQL.executeSearch("SELECT * FROM `issue` "
                        + "INNER JOIN `issue_type` ON `issue`.`issue_type_issue_type_id` = `issue_type`.`issue_type_id` "
                        + "WHERE `description_of_issue` = '" + description + "'");

                if (resultSet.next()) {
                    JOptionPane.showMessageDialog(this, "This issue is already added.", "Warning", JOptionPane.WARNING_MESSAGE, JOptionIcon.WARNING.setICon());
                } else {

                    MySQL.executeIUD("INSERT INTO `issue`(`description_of_issue`,`issue_date`,`employee_NIC`,`issue_type_issue_type_id`) "
                            + "VALUES('" + description + "','" + date + "','" + nic + "','" + issueTypeMap.get(issueType) + "')");

                    reset9();
                    JOptionPane.showMessageDialog(this, "Issue recorded successfully", "Success", JOptionPane.PLAIN_MESSAGE, JOptionIcon.SUCCESS.setICon());

                    IssueMailDialog mail = new IssueMailDialog(dashboard, true, this, issueType, date, description);
                    mail.setVisible(true);
                }
            }
        } catch (Exception e) {
            SignIn.getLoggerObject().log(Level.WARNING, "production issue Add Exception found!", e);
        }
    }//GEN-LAST:event_jButton8ActionPerformed

    private void jButton42ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton42ActionPerformed
        reset1();
        Notifications.getInstance().show(Notifications.Type.SUCCESS, Notifications.Location.BOTTOM_RIGHT, "Refreshed");
    }//GEN-LAST:event_jButton42ActionPerformed

    private void jComboBox6ItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_jComboBox6ItemStateChanged
        search();
    }//GEN-LAST:event_jComboBox6ItemStateChanged

    private void jComboBox2ItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_jComboBox2ItemStateChanged
        loadYieldAnalyticsTable();
    }//GEN-LAST:event_jComboBox2ItemStateChanged

    private void jComboBox8ItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_jComboBox8ItemStateChanged
        search2();
    }//GEN-LAST:event_jComboBox8ItemStateChanged

    private void jComboBox7ItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_jComboBox7ItemStateChanged
        search3();
    }//GEN-LAST:event_jComboBox7ItemStateChanged

    private void jComboBox9ItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_jComboBox9ItemStateChanged
        search4();
    }//GEN-LAST:event_jComboBox9ItemStateChanged

    private void jTextField12KeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTextField12KeyReleased
        search2();
    }//GEN-LAST:event_jTextField12KeyReleased

    private void jTextField7KeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTextField7KeyReleased
        search4();
    }//GEN-LAST:event_jTextField7KeyReleased

    private void jTextField20KeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTextField20KeyReleased
        search3();
    }//GEN-LAST:event_jTextField20KeyReleased

    private void jTextField2KeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTextField2KeyReleased
        loadProgressTable(jTextField2.getText());
    }//GEN-LAST:event_jTextField2KeyReleased

    private void jTextField18KeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTextField18KeyReleased

        String name = jTextField18.getText();
        Pattern pattern = Pattern.compile("[a-zA-Z\\s]+");

        if (!pattern.matcher(name).matches()) {
            Notifications.getInstance().show(Notifications.Type.ERROR, Notifications.Location.BOTTOM_RIGHT, "Enter only letters and spaces");
            jTextField18.setText("");
            return;
        } else {
            loadMaterialTable(jTextField14.getText(), name);
        }

        if (jTextField14.getText().matches("\\d+")) {
            loadMaterialTable(jTextField14.getText(), jTextField18.getText());
        }

        try {
            String materialId = jTextField17.getText();
            String query = "SELECT COUNT(*) AS count FROM `raw_material` "
                    + "WHERE `raw_material_id` = '" + materialId + "'";

            ResultSet resultSet = MySQL.executeSearch(query);

            if (resultSet.next()) {
                int count = resultSet.getInt("count");

                if (count > 0) {
                    jButton14.setEnabled(false);
                    jButton15.setEnabled(true);
                    jButton23.setEnabled(true);
                } else {
                    jButton14.setEnabled(true);
                    jButton15.setEnabled(false);
                    jButton23.setEnabled(false);
                }
            }
        } catch (Exception e) {
            SignIn.getLoggerObject().log(Level.WARNING, "raw material table loading Exception found!", e);
        }
    }//GEN-LAST:event_jTextField18KeyReleased

    private void jButton36ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton36ActionPerformed
        reset2();
        Notifications.getInstance().show(Notifications.Type.SUCCESS, Notifications.Location.BOTTOM_RIGHT, "Refreshed");
    }//GEN-LAST:event_jButton36ActionPerformed

    private void jButton37ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton37ActionPerformed
        reset3();
        Notifications.getInstance().show(Notifications.Type.SUCCESS, Notifications.Location.BOTTOM_RIGHT, "Refreshed");
    }//GEN-LAST:event_jButton37ActionPerformed

    private void jButton38ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton38ActionPerformed
        reset4();
        Notifications.getInstance().show(Notifications.Type.SUCCESS, Notifications.Location.BOTTOM_RIGHT, "Refreshed");
    }//GEN-LAST:event_jButton38ActionPerformed

    private void jButton7ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton7ActionPerformed
        reset5();
        Notifications.getInstance().show(Notifications.Type.SUCCESS, Notifications.Location.BOTTOM_RIGHT, "Refreshed");
    }//GEN-LAST:event_jButton7ActionPerformed

    private void jButton39ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton39ActionPerformed
        reset6();
        Notifications.getInstance().show(Notifications.Type.SUCCESS, Notifications.Location.BOTTOM_RIGHT, "Refreshed");
    }//GEN-LAST:event_jButton39ActionPerformed

    private void jButton40ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton40ActionPerformed
        reset7();
        Notifications.getInstance().show(Notifications.Type.SUCCESS, Notifications.Location.BOTTOM_RIGHT, "Refreshed");
    }//GEN-LAST:event_jButton40ActionPerformed

    private void jButton41ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton41ActionPerformed
        reset8();
        Notifications.getInstance().show(Notifications.Type.SUCCESS, Notifications.Location.BOTTOM_RIGHT, "Refreshed");
    }//GEN-LAST:event_jButton41ActionPerformed

    private void jTable1MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jTable1MouseClicked
        int row = jTable1.getSelectedRow();

        if (row == -1) {
            Notifications.getInstance().show(Notifications.Type.WARNING, Notifications.Location.BOTTOM_RIGHT, "Please select a row in the table");
        } else {
            jTextField6.setText(String.valueOf(jTable1.getValueAt(row, 0)));

            if (evt.getClickCount() == 2) {
                String plantationId = String.valueOf(jTable1.getValueAt(row, 0));

                jTextField13.setText(plantationId);
                jTabbedPane1.setSelectedIndex(6);
                generateStockId();
                jTextField15.setText("");
                jTextField16.setText("");
                search2();
                jButton18.setEnabled(true);
                jButton19.setEnabled(false);
            }
        }
    }//GEN-LAST:event_jTable1MouseClicked

    private void jTable5MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jTable5MouseClicked
        int row = jTable5.getSelectedRow();

        if (row == -1) {
            Notifications.getInstance().show(Notifications.Type.WARNING, Notifications.Location.BOTTOM_RIGHT, "Please select a row in the table");
        } else {
            resetLabelColors();

            String plantationId = (String) jTable5.getValueAt(row, 0);
            String inventoryId = (String) jTable5.getValueAt(row, 1);
            String productQualityId = (String) jTable5.getValueAt(row, 2);

            updateProgressColors(plantationId, inventoryId, productQualityId);
        }
    }//GEN-LAST:event_jTable5MouseClicked

    private void jTable1MouseReleased(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jTable1MouseReleased
        if (evt.getButton() == MouseEvent.BUTTON3 && jTable1.getSelectedRow() != -1) {
            jPopupMenu1.show(evt.getComponent(), evt.getX(), evt.getY());
        }
    }//GEN-LAST:event_jTable1MouseReleased

    private void jMenuItem1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem1ActionPerformed
        // foreground
        JColorChooser colorChooser = new JColorChooser();
        Color color = JColorChooser.showDialog(dashboard, "Pick a colour", Color.BLACK);
        Product.foreground = color;
        renderer.setForeground(color);
    }//GEN-LAST:event_jMenuItem1ActionPerformed

    private void jMenuItem2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem2ActionPerformed
        // background
        JColorChooser colorChooser = new JColorChooser();
        Color color = JColorChooser.showDialog(dashboard, "Pick a colour", Color.WHITE);
        Product.background = color;
        renderer.setBackground(color);
    }//GEN-LAST:event_jMenuItem2ActionPerformed

    private void jTable2MouseReleased(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jTable2MouseReleased
        if (evt.getButton() == MouseEvent.BUTTON3 && jTable2.getSelectedRow() != -1) {
            jPopupMenu2.show(evt.getComponent(), evt.getX(), evt.getY());
        }
    }//GEN-LAST:event_jTable2MouseReleased

    private void jTable3MouseReleased(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jTable3MouseReleased
        if (evt.getButton() == MouseEvent.BUTTON3 && jTable3.getSelectedRow() != -1) {
            jPopupMenu2.show(evt.getComponent(), evt.getX(), evt.getY());
        }
    }//GEN-LAST:event_jTable3MouseReleased

    private void jTable4MouseReleased(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jTable4MouseReleased
        if (evt.getButton() == MouseEvent.BUTTON3 && jTable4.getSelectedRow() != -1) {
            jPopupMenu6.show(evt.getComponent(), evt.getX(), evt.getY());
        }
    }//GEN-LAST:event_jTable4MouseReleased

    private void jTable5MouseReleased(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jTable5MouseReleased
        if (evt.getButton() == MouseEvent.BUTTON3 && jTable5.getSelectedRow() != -1) {
            jPopupMenu2.show(evt.getComponent(), evt.getX(), evt.getY());
        }
    }//GEN-LAST:event_jTable5MouseReleased

    private void jTable6MouseReleased(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jTable6MouseReleased
        if (evt.getButton() == MouseEvent.BUTTON3 && jTable6.getSelectedRow() != -1) {
            jPopupMenu2.show(evt.getComponent(), evt.getX(), evt.getY());
        }
    }//GEN-LAST:event_jTable6MouseReleased

    private void jTable7MouseReleased(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jTable7MouseReleased
        if (evt.getButton() == MouseEvent.BUTTON3 && jTable7.getSelectedRow() != -1) {
            jPopupMenu3.show(evt.getComponent(), evt.getX(), evt.getY());
        }
    }//GEN-LAST:event_jTable7MouseReleased

    private void jTable8MouseReleased(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jTable8MouseReleased
        if (evt.getButton() == MouseEvent.BUTTON3 && jTable8.getSelectedRow() != -1) {
            jPopupMenu4.show(evt.getComponent(), evt.getX(), evt.getY());
        }
    }//GEN-LAST:event_jTable8MouseReleased

    private void jTable9MouseReleased(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jTable9MouseReleased
        if (evt.getButton() == MouseEvent.BUTTON3 && jTable9.getSelectedRow() != -1) {
            jPopupMenu2.show(evt.getComponent(), evt.getX(), evt.getY());
        }
    }//GEN-LAST:event_jTable9MouseReleased

    private void jTable10MouseReleased(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jTable10MouseReleased
        if (evt.getButton() == MouseEvent.BUTTON3 && jTable10.getSelectedRow() != -1) {
            jPopupMenu2.show(evt.getComponent(), evt.getX(), evt.getY());
        }
    }//GEN-LAST:event_jTable10MouseReleased

    private void jTable11MouseReleased(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jTable11MouseReleased
        if (evt.getButton() == MouseEvent.BUTTON3 && jTable11.getSelectedRow() != -1) {
            jPopupMenu5.show(evt.getComponent(), evt.getX(), evt.getY());
        }
    }//GEN-LAST:event_jTable11MouseReleased

    private void jTable9MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jTable9MouseClicked
        int row = jTable9.getSelectedRow();

        if (row == -1) {
            Notifications.getInstance().show(Notifications.Type.WARNING, Notifications.Location.BOTTOM_RIGHT, "Please select a row in the table");
        } else {
            jLabel87.setText("Raw Materials for Selected Plantation");
            String paste = String.valueOf(jTable9.getValueAt(row, 0));
            jTextField14.setText(paste);
            if (!jTextField18.getText().equals("")) {
                jButton14.setEnabled(true);
                jButton15.setEnabled(false);
                jButton23.setEnabled(false);
            }
            loadMaterialTable(paste, jTextField18.getText());
            jTextField10.setText(String.valueOf(jTable9.getValueAt(row, 1)));
        }
    }//GEN-LAST:event_jTable9MouseClicked

    private void jTable8MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jTable8MouseClicked
        int row = jTable8.getSelectedRow();

        if (row == -1) {
            Notifications.getInstance().show(Notifications.Type.WARNING, Notifications.Location.BOTTOM_RIGHT, "Please select a row in the table");
        } else {
            jButton14.setEnabled(false);
            jButton15.setEnabled(false);
            jButton23.setEnabled(false);

            jTextField17.setText(String.valueOf(jTable8.getValueAt(row, 0)));
            jTextField18.setText(String.valueOf(jTable8.getValueAt(row, 1)));
        }
    }//GEN-LAST:event_jTable8MouseClicked

    private void jTable10MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jTable10MouseClicked
        int row = jTable10.getSelectedRow();

        if (row == -1) {
            Notifications.getInstance().show(Notifications.Type.WARNING, Notifications.Location.BOTTOM_RIGHT, "Please select a row in the table");
        } else {
            jButton8.setEnabled(false);
            jButton21.setEnabled(true);

            jTextField5.setText(String.valueOf(jTable10.getValueAt(row, 0)));
            jTextArea2.setText(String.valueOf(jTable10.getValueAt(row, 1)));
            jComboBox4.setSelectedItem(String.valueOf(jTable10.getValueAt(row, 2)));
            jTextField8.setText(String.valueOf(jTable10.getValueAt(row, 3)));
        }
    }//GEN-LAST:event_jTable10MouseClicked

    private void jMenuItem3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem3ActionPerformed

        int row = jTable1.getSelectedRow();

        if (row == -1) {
            Notifications.getInstance().show(Notifications.Type.WARNING, Notifications.Location.BOTTOM_RIGHT, "Please select a row in the table");
        } else {
            jButton3.setEnabled(false);
            jButton2.setEnabled(true);

            String plantationId = String.valueOf(jTable1.getValueAt(row, 0));
            jTextField6.setText(plantationId);
            jTextField1.setText(String.valueOf(jTable1.getValueAt(row, 3)));
            jComboBox1.setSelectedItem(String.valueOf(jTable1.getValueAt(row, 1)));
            jComboBox5.setSelectedItem(String.valueOf(jTable1.getValueAt(row, 2)));
        }
    }//GEN-LAST:event_jMenuItem3ActionPerformed

    private void jMenuItem5ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem5ActionPerformed
        // foreground
        JColorChooser colorChooser = new JColorChooser();
        Color color = JColorChooser.showDialog(dashboard, "Pick a colour", Color.BLACK);
        Product.foreground = color;
        renderer.setForeground(color);
    }//GEN-LAST:event_jMenuItem5ActionPerformed

    private void jMenuItem6ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem6ActionPerformed
        // background
        JColorChooser colorChooser = new JColorChooser();
        Color color = JColorChooser.showDialog(dashboard, "Pick a colour", Color.WHITE);
        Product.background = color;
        renderer.setBackground(color);
    }//GEN-LAST:event_jMenuItem6ActionPerformed

    private void jMenuItem7ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem7ActionPerformed
        // foreground
        JColorChooser colorChooser = new JColorChooser();
        Color color = JColorChooser.showDialog(dashboard, "Pick a colour", Color.BLACK);
        Product.foreground = color;
        renderer.setForeground(color);
    }//GEN-LAST:event_jMenuItem7ActionPerformed

    private void jMenuItem8ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem8ActionPerformed
        // background
        JColorChooser colorChooser = new JColorChooser();
        Color color = JColorChooser.showDialog(dashboard, "Pick a colour", Color.WHITE);
        Product.background = color;
        renderer.setBackground(color);
    }//GEN-LAST:event_jMenuItem8ActionPerformed

    private void jMenuItem9ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem9ActionPerformed

        int row = jTable7.getSelectedRow();

        if (row == -1) {
            Notifications.getInstance().show(Notifications.Type.WARNING, Notifications.Location.BOTTOM_RIGHT, "Please select a row in the table");
        } else {

            jTextField13.setText(String.valueOf(jTable7.getValueAt(row, 1)));
            String stockId = String.valueOf(jTable7.getValueAt(row, 0));
            String qty = String.valueOf(jTable7.getValueAt(row, 3));

            if (stockId.equals("null") && qty.equals("null")) {
                generateStockId();
                jTextField15.setText("");
                jTextField16.setText("");
                jButton18.setEnabled(true);
                jButton19.setEnabled(false);

            } else {
                jTextField19.setText(String.valueOf(stockId));
                jTextField15.setText(String.valueOf(qty));
                jTextField16.setText(String.valueOf(jTable7.getValueAt(row, 4)));
                jButton18.setEnabled(false);
                jButton19.setEnabled(true);
            }
        }
    }//GEN-LAST:event_jMenuItem9ActionPerformed

    private void jMenuItem11ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem11ActionPerformed
        // foreground
        JColorChooser colorChooser = new JColorChooser();
        Color color = JColorChooser.showDialog(dashboard, "Pick a colour", Color.BLACK);
        Product.foreground = color;
        renderer.setForeground(color);
    }//GEN-LAST:event_jMenuItem11ActionPerformed

    private void jMenuItem12ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem12ActionPerformed
        // background
        JColorChooser colorChooser = new JColorChooser();
        Color color = JColorChooser.showDialog(dashboard, "Pick a colour", Color.WHITE);
        Product.background = color;
        renderer.setBackground(color);
    }//GEN-LAST:event_jMenuItem12ActionPerformed

    private void jMenuItem13ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem13ActionPerformed
        jButton14.setEnabled(false);
        jButton15.setEnabled(true);
        jButton23.setEnabled(false);
    }//GEN-LAST:event_jMenuItem13ActionPerformed

    private void jMenuItem14ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem14ActionPerformed
        jButton14.setEnabled(false);
        jButton15.setEnabled(false);
        jButton23.setEnabled(true);
    }//GEN-LAST:event_jMenuItem14ActionPerformed

    private void jMenuItem15ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem15ActionPerformed
        // foreground
        JColorChooser colorChooser = new JColorChooser();
        Color color = JColorChooser.showDialog(dashboard, "Pick a colour", Color.BLACK);
        Product.foreground = color;
        renderer.setForeground(color);
    }//GEN-LAST:event_jMenuItem15ActionPerformed

    private void jMenuItem16ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem16ActionPerformed
        // background
        JColorChooser colorChooser = new JColorChooser();
        Color color = JColorChooser.showDialog(dashboard, "Pick a colour", Color.WHITE);
        Product.background = color;
        renderer.setBackground(color);
    }//GEN-LAST:event_jMenuItem16ActionPerformed

    private void jMenuItem17ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem17ActionPerformed

        int row = jTable11.getSelectedRow();

        if (row == -1) {
            Notifications.getInstance().show(Notifications.Type.WARNING, Notifications.Location.BOTTOM_RIGHT, "Please select a row in the table");
        } else {
            jButton26.setEnabled(false);
            jButton27.setEnabled(true);

            jTextField9.setText(String.valueOf(jTable11.getValueAt(row, 0)));
            jTextField11.setText(String.valueOf(jTable11.getValueAt(row, 1)));
        }
    }//GEN-LAST:event_jMenuItem17ActionPerformed

    private void jTextField3KeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTextField3KeyReleased
        loadFruitTable(jTextField3.getText());
    }//GEN-LAST:event_jTextField3KeyReleased

    private void jMenuItem19ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem19ActionPerformed
        // foreground
        JColorChooser colorChooser = new JColorChooser();
        Color color = JColorChooser.showDialog(dashboard, "Pick a colour", Color.BLACK);
        Product.foreground = color;
        renderer.setForeground(color);
    }//GEN-LAST:event_jMenuItem19ActionPerformed

    private void jMenuItem20ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem20ActionPerformed
        // background
        JColorChooser colorChooser = new JColorChooser();
        Color color = JColorChooser.showDialog(dashboard, "Pick a colour", Color.WHITE);
        Product.background = color;
        renderer.setBackground(color);
    }//GEN-LAST:event_jMenuItem20ActionPerformed

    private void jMenuItem21ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem21ActionPerformed
        jDateChooser4.setDate(null);
        tf.setText("--:--");
        jButton4.setEnabled(false);
        jButton5.setEnabled(true);
        jButton35.setEnabled(false);
        jDateChooser4.setEnabled(false);
        jSpinner2.setEnabled(false);
        jDateChooser3.setEnabled(true);
        jSpinner1.setEnabled(true);
        jTextArea1.setEditable(true);
        jComboBox3.setEnabled(true);
        jComboBox13.setEnabled(true);
    }//GEN-LAST:event_jMenuItem21ActionPerformed

    private void jTable4MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jTable4MouseClicked
        row1 = jTable4.getSelectedRow();

        if (row1 == -1) {
            Notifications.getInstance().show(Notifications.Type.WARNING, Notifications.Location.BOTTOM_RIGHT, "Please select a row in the table");
        } else {
            jButton4.setEnabled(false);
            jButton5.setEnabled(false);
            jButton35.setEnabled(true);
            jComboBox3.setEnabled(false);
            jComboBox13.setEnabled(false);
            jTextArea1.setEditable(false);
            jDateChooser3.setEnabled(false);
            jSpinner1.setEnabled(false);
            jDateChooser4.setEnabled(true);
            jSpinner2.setEnabled(true);

            try {
                String taskId = String.valueOf(jTable4.getValueAt(row1, 1));
                String query = "SELECT task.task_description AS description, task.due_date AS due_date FROM `task` "
                        + "WHERE `task_id` = '" + taskId + "'";

                ResultSet resultSet = MySQL.executeSearch(query);

                if (resultSet.next()) {
                    String description = resultSet.getString("description");
                    jTextArea1.setText(description);

                    Timestamp dueDateTimestamp = resultSet.getTimestamp("due_date");
                    if (dueDateTimestamp != null) {
                        Date dueDate = new Date(dueDateTimestamp.getTime());
                        jDateChooser3.setDate(dueDate);

                        Calendar calendar = Calendar.getInstance();
                        calendar.setTime(dueDate);
                        jSpinner1.setValue(calendar.getTime());
                    } else {
                        jDateChooser3.setDate(null);
                        jSpinner1.setValue(new java.util.Date());
                    }
                }
            } catch (Exception e) {
                SignIn.getLoggerObject().log(Level.WARNING, "task assigned worker table Exception found!", e);
            }

            String nic = String.valueOf(jTable4.getValueAt(row1, 0));
            String task = String.valueOf(jTable4.getValueAt(row1, 2));
            String taskStatus = String.valueOf(jTable4.getValueAt(row1, 3));

            jTextField4.setText(nic);
            jComboBox3.setSelectedItem(task);
            jComboBox13.setSelectedItem(taskStatus);
            jDateChooser4.setDate(null);
            tf.setValue(new java.util.Date());
        }
    }//GEN-LAST:event_jTable4MouseClicked

    private void jTable3MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jTable3MouseClicked
        int row = jTable3.getSelectedRow();

        if (row == -1) {
            Notifications.getInstance().show(Notifications.Type.WARNING, Notifications.Location.BOTTOM_RIGHT, "Please select a row in the table");
        } else {
            jButton4.setEnabled(true);
            jButton5.setEnabled(false);
            jButton35.setEnabled(false);

            jTextField4.setText(String.valueOf(jTable3.getValueAt(row, 0)));
            tf.setText("--:--");
            jComboBox3.setSelectedIndex(0);
            jComboBox13.setSelectedIndex(0);
            jTextArea1.setText("");
            jDateChooser3.setDate(null);
            jDateChooser4.setDate(null);
        }
    }//GEN-LAST:event_jTable3MouseClicked

    private void jButton35ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton35ActionPerformed
        try {

            if (jDateChooser4.getDate() == null) {
                JOptionPane.showMessageDialog(null, "Please fill completion date", "Invalid Input", JOptionPane.WARNING_MESSAGE, JOptionIcon.WARNING.setICon());
            } else if (tf.getText().equals("--:--")) {
                tf.setValue(new java.util.Date());
                JOptionPane.showMessageDialog(null, "Please fill completion time.", "Invalid Input", JOptionPane.WARNING_MESSAGE, JOptionIcon.WARNING.setICon());
            } else {

                try {
                    Timestamp completeTimestamp = getCombinedDateTime(jDateChooser4, jSpinner2);
                    String formattedCompleteDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(completeTimestamp);

                    String taskId = String.valueOf(jTable4.getValueAt(row1, 1));

                    MySQL.executeIUD("UPDATE `task` SET `completion_date` = '" + formattedCompleteDate + "' "
                            + "WHERE `task_id` = '" + taskId + "'");

                    reset4();
                    loadIdleWorkerTable();
                    loadTaskAssignedWorkerTable();
                    loadWorkerTable("ASC", "");
                    loadLineChart();

                    JOptionPane.showMessageDialog(this, "Task completion recorded successfully.", "Success", JOptionPane.PLAIN_MESSAGE, JOptionIcon.SUCCESS.setICon());
                } catch (Exception e) {
                    SignIn.getLoggerObject().log(Level.WARNING, "task completion Exception found!", e);
                }
            }
        } catch (Exception e) {
            SignIn.getLoggerObject().log(Level.WARNING, "task assigned worker table Exception found!", e);
        }
    }//GEN-LAST:event_jButton35ActionPerformed

    private void jButton12ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton12ActionPerformed
        generateReport("Monthly");

        String nic = SignIn.getEmployeeNIC();
        String dateTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
        int rowCount = jTable6.getRowCount();

        if (nic.isEmpty()) {
            JOptionPane.showMessageDialog(null, "Employee should logged in to process", "Error", JOptionPane.ERROR_MESSAGE, JOptionIcon.ERROR.setICon());
        } else if (rowCount == 0) {
            JOptionPane.showMessageDialog(null, "The worker performance table is empty!", "No Data", JOptionPane.ERROR_MESSAGE, JOptionIcon.ERROR.setICon());
        } else {
            try {

                int hours = (int) avgDuration / 60;
                int minutes = (int) avgDuration % 60;

                String formattedDuration = String.format("%d hours %d minutes", hours, minutes);

                //view 
                InputStream s = this.getClass().getResourceAsStream("/lk/corevision/fruitshop/reports/production/fruitShopAppWorkerMonthly.jasper");

                HashMap<String, Object> params = new HashMap<>();
                params.put("Parameter1", dateTime);
                params.put("Parameter2", nic);
                params.put("Parameter3", String.valueOf(totalTasksCompleted));
                params.put("Parameter5", formattedDuration);

                totalTasksCompleted = 0;
                avgDuration = 0;

                int sort = jComboBox7.getSelectedIndex();
                String text = null;

                if (sort == 0) {
                    text = "Workers in descending performance order";
                } else if (sort == 1) {
                    text = "Workers in ascending performance order";
                }
                params.put("Parameter4", text);

                JRTableModelDataSource dataSource = new JRTableModelDataSource(jTable6.getModel());

                JasperPrint jasperPrint = JasperFillManager.fillReport(s, params, dataSource);

                JasperViewer.viewReport(jasperPrint, false);

            } catch (JRException e) {
                SignIn.getLoggerObject().log(Level.WARNING, "monthly worker performance jasper report Exception found!", e);
            }
        }
    }//GEN-LAST:event_jButton12ActionPerformed

    private void jDateChooser4PropertyChange(java.beans.PropertyChangeEvent evt) {//GEN-FIRST:event_jDateChooser4PropertyChange
        Date dueDate = jDateChooser3.getDate();
        Date completionDate = jDateChooser4.getDate();

        if (dueDate != null && completionDate != null) {
            Calendar dueCal = Calendar.getInstance();
            dueCal.setTime(dueDate);
            dueCal.set(Calendar.HOUR_OF_DAY, 0);
            dueCal.set(Calendar.MINUTE, 0);
            dueCal.set(Calendar.SECOND, 0);
            dueCal.set(Calendar.MILLISECOND, 0);

            Calendar completionCal = Calendar.getInstance();
            completionCal.setTime(completionDate);
            completionCal.set(Calendar.HOUR_OF_DAY, 0);
            completionCal.set(Calendar.MINUTE, 0);
            completionCal.set(Calendar.SECOND, 0);
            completionCal.set(Calendar.MILLISECOND, 0);

            if (completionCal.before(dueCal)) {
                JOptionPane.showMessageDialog(null, "Completion date must be the same day or after the due date!", "Invalid Date", JOptionPane.WARNING_MESSAGE, JOptionIcon.WARNING.setICon());
                jDateChooser4.setDate(null);
            }
        }
    }//GEN-LAST:event_jDateChooser4PropertyChange

    private void jDateChooser2PropertyChange(java.beans.PropertyChangeEvent evt) {//GEN-FIRST:event_jDateChooser2PropertyChange
        Date startDate = jDateChooser1.getDate();
        Date endDate = jDateChooser2.getDate();

        if (startDate != null && endDate != null) {
            Calendar dueCal = Calendar.getInstance();
            dueCal.setTime(startDate);
            dueCal.set(Calendar.HOUR_OF_DAY, 0);
            dueCal.set(Calendar.MINUTE, 0);
            dueCal.set(Calendar.SECOND, 0);
            dueCal.set(Calendar.MILLISECOND, 0);

            Calendar completionCal = Calendar.getInstance();
            completionCal.setTime(endDate);
            completionCal.set(Calendar.HOUR_OF_DAY, 0);
            completionCal.set(Calendar.MINUTE, 0);
            completionCal.set(Calendar.SECOND, 0);
            completionCal.set(Calendar.MILLISECOND, 0);

            if (completionCal.before(dueCal)) {
                JOptionPane.showMessageDialog(null, "Please select a date after the start date!", "Invalid Date", JOptionPane.WARNING_MESSAGE, JOptionIcon.WARNING.setICon());
                jDateChooser2.setDate(null);
            } else {
                loadYieldAnalyticsTable();
            }
        } else if (startDate == null && endDate != null) {
            loadYieldAnalyticsTable();
        }
    }//GEN-LAST:event_jDateChooser2PropertyChange

    private void jButton16ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton16ActionPerformed

        String nic = SignIn.getEmployeeNIC();
        String dateTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
        int rowCount = jTable1.getRowCount();

        if (nic.isEmpty()) {
            JOptionPane.showMessageDialog(null, "Employee should logged in to process", "Error", JOptionPane.ERROR_MESSAGE, JOptionIcon.ERROR.setICon());
        } else if (rowCount == 0) {
            JOptionPane.showMessageDialog(null, "The plantation table is empty!", "No Data", JOptionPane.ERROR_MESSAGE, JOptionIcon.ERROR.setICon());
        } else {
            try {

                //view 
                InputStream s = this.getClass().getResourceAsStream("/lk/corevision/fruitshop/reports/production/fruitShopAppPlantation.jasper");

                HashMap<String, Object> params = new HashMap<>();
                params.put("Parameter1", dateTime);
                params.put("Parameter2", nic);
                params.put("Parameter3", String.valueOf(rowCount));

                JRTableModelDataSource dataSource = new JRTableModelDataSource(jTable1.getModel());

                JasperPrint jasperPrint = JasperFillManager.fillReport(s, params, dataSource);

                JasperViewer.viewReport(jasperPrint, false);
            } catch (JRException e) {
                SignIn.getLoggerObject().log(Level.WARNING, "plantation jasper report Exception found!", e);
            }
        }
    }//GEN-LAST:event_jButton16ActionPerformed

    private void jButton10ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton10ActionPerformed
        String nic = SignIn.getEmployeeNIC();
        String dateTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
        int rowCount = jTable2.getRowCount();

        if (nic.isEmpty()) {
            JOptionPane.showMessageDialog(null, "Employee should logged in to process", "Error", JOptionPane.ERROR_MESSAGE, JOptionIcon.ERROR.setICon());
        } else if (rowCount == 0) {
            JOptionPane.showMessageDialog(null, "The yield analytics table is empty!", "No Data", JOptionPane.ERROR_MESSAGE, JOptionIcon.ERROR.setICon());
        } else {
            try {

                //view 
                InputStream s = this.getClass().getResourceAsStream("/lk/corevision/fruitshop/reports/production/fruitShopAppAnalytics.jasper");

                HashMap<String, Object> params = new HashMap<>();
                params.put("Parameter1", dateTime);
                params.put("Parameter2", nic);
                params.put("Parameter3", String.valueOf(rowCount));

                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
                String start = (jDateChooser1.getDate() != null) ? dateFormat.format(jDateChooser1.getDate()) : null;
                String end = (jDateChooser2.getDate() != null) ? dateFormat.format(jDateChooser2.getDate()) : null;

                if (start != null && end == null) {
                    params.put("Parameter4", "From");
                    params.put("Parameter5", start);
                    params.put("Parameter6", "upwards");

                } else if (start == null && end != null) {
                    params.put("Parameter4", "Up to");
                    params.put("Parameter5", end);
                    params.put("Parameter6", "");

                } else if (start != null && end != null) {
                    params.put("Parameter4", start);
                    params.put("Parameter5", "to");
                    params.put("Parameter6", end);

                } else {
                    params.put("Parameter4", "");
                    params.put("Parameter5", "");
                    params.put("Parameter6", "");
                }

                JRTableModelDataSource dataSource = new JRTableModelDataSource(jTable2.getModel());

                JasperPrint jasperPrint = JasperFillManager.fillReport(s, params, dataSource);

                JasperViewer.viewReport(jasperPrint, false);
            } catch (JRException e) {
                SignIn.getLoggerObject().log(Level.WARNING, "yield analytics jasper report Exception found!", e);
            }
        }
    }//GEN-LAST:event_jButton10ActionPerformed

    private void jButton13ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton13ActionPerformed
        generateReport("Daily");

        String nic = SignIn.getEmployeeNIC();
        String dateTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
        int rowCount = jTable6.getRowCount();

        if (nic.isEmpty()) {
            JOptionPane.showMessageDialog(null, "Employee should logged in to process", "Error", JOptionPane.ERROR_MESSAGE, JOptionIcon.ERROR.setICon());
        } else if (rowCount == 0) {
            JOptionPane.showMessageDialog(null, "The worker performance table is empty!", "No Data", JOptionPane.ERROR_MESSAGE, JOptionIcon.ERROR.setICon());
        } else {
            try {

                int hours = (int) avgDuration / 60;
                int minutes = (int) avgDuration % 60;

                String formattedDuration = String.format("%d hours %d minutes", hours, minutes);

                //view 
                InputStream s = this.getClass().getResourceAsStream("/lk/corevision/fruitshop/reports/production/fruitShopAppWorkerDaily.jasper");

                HashMap<String, Object> params = new HashMap<>();
                params.put("Parameter1", dateTime);
                params.put("Parameter2", nic);
                params.put("Parameter3", String.valueOf(totalTasksCompleted));
                params.put("Parameter5", formattedDuration);

                totalTasksCompleted = 0;
                avgDuration = 0;

                int sort = jComboBox7.getSelectedIndex();
                String text = null;

                if (sort == 0) {
                    text = "Workers in descending performance order";
                } else if (sort == 1) {
                    text = "Workers in ascending performance order";
                }
                params.put("Parameter4", text);

                JRTableModelDataSource dataSource = new JRTableModelDataSource(jTable6.getModel());

                JasperPrint jasperPrint = JasperFillManager.fillReport(s, params, dataSource);

                JasperViewer.viewReport(jasperPrint, false);
            } catch (JRException e) {
                SignIn.getLoggerObject().log(Level.WARNING, "daily worker performance jasper report Exception found!", e);
            }
        }
    }//GEN-LAST:event_jButton13ActionPerformed

    private void jButton11ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton11ActionPerformed
        generateReport("Annual");

        String nic = SignIn.getEmployeeNIC();
        String dateTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
        int rowCount = jTable6.getRowCount();

        if (nic.isEmpty()) {
            JOptionPane.showMessageDialog(null, "Employee should logged in to process", "Error", JOptionPane.ERROR_MESSAGE, JOptionIcon.ERROR.setICon());
        } else if (rowCount == 0) {
            JOptionPane.showMessageDialog(null, "The worker performance table is empty!", "No Data", JOptionPane.ERROR_MESSAGE, JOptionIcon.ERROR.setICon());
        } else {
            try {

                int hours = (int) avgDuration / 60;
                int minutes = (int) avgDuration % 60;

                String formattedDuration = String.format("%d hours %d minutes", hours, minutes);

                //view 
                InputStream s = this.getClass().getResourceAsStream("/lk/corevision/fruitshop/reports/production/fruitShopAppWorkerAnnual.jasper");

                HashMap<String, Object> params = new HashMap<>();
                params.put("Parameter1", dateTime);
                params.put("Parameter2", nic);
                params.put("Parameter3", String.valueOf(totalTasksCompleted));
                params.put("Parameter5", formattedDuration);

                totalTasksCompleted = 0;
                avgDuration = 0;

                int sort = jComboBox7.getSelectedIndex();
                String text = null;

                if (sort == 0) {
                    text = "Workers in descending performance order";
                } else if (sort == 1) {
                    text = "Workers in ascending performance order";
                }
                params.put("Parameter4", text);

                JRTableModelDataSource dataSource = new JRTableModelDataSource(jTable6.getModel());

                JasperPrint jasperPrint = JasperFillManager.fillReport(s, params, dataSource);

                JasperViewer.viewReport(jasperPrint, false);

            } catch (JRException e) {
                SignIn.getLoggerObject().log(Level.WARNING, "annual worker performance jasper report Exception found!", e);
            }
        }
    }//GEN-LAST:event_jButton11ActionPerformed

    private void jDateChooser1PropertyChange(java.beans.PropertyChangeEvent evt) {//GEN-FIRST:event_jDateChooser1PropertyChange
        Date startDate = jDateChooser1.getDate();
        Date endDate = jDateChooser2.getDate();

        if (startDate != null && endDate != null) {
            Calendar dueCal = Calendar.getInstance();
            dueCal.setTime(startDate);
            dueCal.set(Calendar.HOUR_OF_DAY, 0);
            dueCal.set(Calendar.MINUTE, 0);
            dueCal.set(Calendar.SECOND, 0);
            dueCal.set(Calendar.MILLISECOND, 0);

            Calendar completionCal = Calendar.getInstance();
            completionCal.setTime(endDate);
            completionCal.set(Calendar.HOUR_OF_DAY, 0);
            completionCal.set(Calendar.MINUTE, 0);
            completionCal.set(Calendar.SECOND, 0);
            completionCal.set(Calendar.MILLISECOND, 0);

            if (completionCal.before(dueCal)) {
                JOptionPane.showMessageDialog(null, "Please select a date before the end date!", "Invalid Date", JOptionPane.WARNING_MESSAGE, JOptionIcon.WARNING.setICon());
                jDateChooser1.setDate(null);
            } else {
                loadYieldAnalyticsTable();
            }
        } else if (startDate != null && endDate == null) {
            loadYieldAnalyticsTable();
        }
    }//GEN-LAST:event_jDateChooser1PropertyChange

    private void jButton47ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton47ActionPerformed
        jTabbedPane1.setSelectedIndex(8);
    }//GEN-LAST:event_jButton47ActionPerformed

    private void jButton49ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton49ActionPerformed
        jTabbedPane1.setSelectedIndex(10);
    }//GEN-LAST:event_jButton49ActionPerformed

    private void jButton28ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton28ActionPerformed
        jTabbedPane1.setSelectedIndex(3);
    }//GEN-LAST:event_jButton28ActionPerformed

    private void jButton43ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton43ActionPerformed
        jTabbedPane1.setSelectedIndex(4);
    }//GEN-LAST:event_jButton43ActionPerformed

    private void jButton44ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton44ActionPerformed
        jTabbedPane1.setSelectedIndex(5);
    }//GEN-LAST:event_jButton44ActionPerformed

    private void jButton48ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton48ActionPerformed
        jTabbedPane1.setSelectedIndex(9);
        loadDate();
    }//GEN-LAST:event_jButton48ActionPerformed

    private void jButton46ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton46ActionPerformed
        jTabbedPane1.setSelectedIndex(7);
        generateRawId();
    }//GEN-LAST:event_jButton46ActionPerformed

    private void jButton20ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton20ActionPerformed
        jTabbedPane1.setSelectedIndex(1);
        generatePlantationId();
    }//GEN-LAST:event_jButton20ActionPerformed

    private void jButton45ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton45ActionPerformed
        jTabbedPane1.setSelectedIndex(6);
        generateStockId();
    }//GEN-LAST:event_jButton45ActionPerformed

    private void jButton22ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton22ActionPerformed
        jTabbedPane1.setSelectedIndex(2);
    }//GEN-LAST:event_jButton22ActionPerformed

    private void jButton50ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton50ActionPerformed
        reset9();
        Notifications.getInstance().show(Notifications.Type.SUCCESS, Notifications.Location.BOTTOM_RIGHT, "Refreshed");
    }//GEN-LAST:event_jButton50ActionPerformed

    private void jTable7MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jTable7MouseClicked
        int row = jTable7.getSelectedRow();

        if (row == -1) {
            Notifications.getInstance().show(Notifications.Type.WARNING, Notifications.Location.BOTTOM_RIGHT, "Please select a row in the table");
        } else {
            jTextField13.setText(String.valueOf(jTable7.getValueAt(row, 1)));

            String inventoryId = (String) jTable7.getValueAt(row, 0);
            if ((inventoryId == null || inventoryId.isEmpty())) {
                generateStockId();
            } else {
                jTextField19.setText(inventoryId);
            }
        }
    }//GEN-LAST:event_jTable7MouseClicked


    private void jTextField1KeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTextField1KeyTyped

        if (jTextField1.getText().length() >= 8) {
            evt.consume();
            JOptionPane.showMessageDialog(null, "Maximum length for quantity is 8 characters.");
        }
    }//GEN-LAST:event_jTextField1KeyTyped

    private void jTextField15KeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTextField15KeyTyped
        if (jTextField15.getText().length() >= 8) {
            evt.consume();
            JOptionPane.showMessageDialog(null, "Maximum length for quantity is 8 characters.");
        }
    }//GEN-LAST:event_jTextField15KeyTyped

    private void jTextField16KeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTextField16KeyTyped
        if (jTextField16.getText().length() >= 8) {
            evt.consume();
            JOptionPane.showMessageDialog(null, "Maximum length for weight is 8 characters.");
        }
    }//GEN-LAST:event_jTextField16KeyTyped

    private void jTextField11KeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTextField11KeyTyped
        if (jTextField11.getText().length() >= 8) {
            evt.consume();
            JOptionPane.showMessageDialog(null, "Maximum length for fruit name is 8 characters.");
        }
    }//GEN-LAST:event_jTextField11KeyTyped

    private void jButton51ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton51ActionPerformed
        jButton51.setEnabled(false);
        String text = jTextField18.getText();
        if (text.isEmpty()) {
            Notifications.getInstance().show(Notifications.Type.WARNING, Notifications.Location.BOTTOM_RIGHT, "Raw material name is empty");
            return;
        }
        Toolkit.getDefaultToolkit().getSystemClipboard().setContents(new StringSelection(text), null);
        Notifications.getInstance().show(Notifications.Type.SUCCESS, Notifications.Location.BOTTOM_RIGHT, "Copied");
        jButton51.setEnabled(true);
    }//GEN-LAST:event_jButton51ActionPerformed

    private void inventoryAuditMenuItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_inventoryAuditMenuItemActionPerformed
        InventoryAudits inventoryAudits = new InventoryAudits(dashboard, true, String.valueOf(jTable7.getValueAt(jTable7.getSelectedRow(), 0)));
        inventoryAudits.setVisible(true);
    }//GEN-LAST:event_inventoryAuditMenuItemActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JMenuItem inventoryAuditMenuItem;
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton10;
    private javax.swing.JButton jButton11;
    private javax.swing.JButton jButton12;
    private javax.swing.JButton jButton13;
    private javax.swing.JButton jButton14;
    private javax.swing.JButton jButton15;
    private javax.swing.JButton jButton16;
    private javax.swing.JButton jButton17;
    private javax.swing.JButton jButton18;
    private javax.swing.JButton jButton19;
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jButton20;
    private javax.swing.JButton jButton21;
    private javax.swing.JButton jButton22;
    private javax.swing.JButton jButton23;
    private javax.swing.JButton jButton24;
    private javax.swing.JButton jButton25;
    private javax.swing.JButton jButton26;
    private javax.swing.JButton jButton27;
    private javax.swing.JButton jButton28;
    private javax.swing.JButton jButton29;
    private javax.swing.JButton jButton3;
    private javax.swing.JButton jButton30;
    private javax.swing.JButton jButton31;
    private javax.swing.JButton jButton32;
    private javax.swing.JButton jButton33;
    private javax.swing.JButton jButton34;
    private javax.swing.JButton jButton35;
    private javax.swing.JButton jButton36;
    private javax.swing.JButton jButton37;
    private javax.swing.JButton jButton38;
    private javax.swing.JButton jButton39;
    private javax.swing.JButton jButton4;
    private javax.swing.JButton jButton40;
    private javax.swing.JButton jButton41;
    private javax.swing.JButton jButton42;
    private javax.swing.JButton jButton43;
    private javax.swing.JButton jButton44;
    private javax.swing.JButton jButton45;
    private javax.swing.JButton jButton46;
    private javax.swing.JButton jButton47;
    private javax.swing.JButton jButton48;
    private javax.swing.JButton jButton49;
    private javax.swing.JButton jButton5;
    private javax.swing.JButton jButton50;
    private javax.swing.JButton jButton51;
    private javax.swing.JButton jButton6;
    private javax.swing.JButton jButton7;
    private javax.swing.JButton jButton8;
    private javax.swing.JButton jButton9;
    private javax.swing.JComboBox<String> jComboBox1;
    private javax.swing.JComboBox<String> jComboBox13;
    private javax.swing.JComboBox<String> jComboBox2;
    private javax.swing.JComboBox<String> jComboBox3;
    private javax.swing.JComboBox<String> jComboBox4;
    private javax.swing.JComboBox<String> jComboBox5;
    private javax.swing.JComboBox<String> jComboBox6;
    private javax.swing.JComboBox<String> jComboBox7;
    private javax.swing.JComboBox<String> jComboBox8;
    private javax.swing.JComboBox<String> jComboBox9;
    private com.toedter.calendar.JDateChooser jDateChooser1;
    private com.toedter.calendar.JDateChooser jDateChooser2;
    private com.toedter.calendar.JDateChooser jDateChooser3;
    private com.toedter.calendar.JDateChooser jDateChooser4;
    private javax.swing.JFormattedTextField jFormattedTextField1;
    private javax.swing.JFormattedTextField jFormattedTextField2;
    private javax.swing.JFormattedTextField jFormattedTextField3;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel23;
    private javax.swing.JLabel jLabel24;
    private javax.swing.JLabel jLabel25;
    private javax.swing.JLabel jLabel26;
    private javax.swing.JLabel jLabel27;
    private javax.swing.JLabel jLabel28;
    private javax.swing.JLabel jLabel29;
    private javax.swing.JLabel jLabel30;
    private javax.swing.JLabel jLabel31;
    private javax.swing.JLabel jLabel32;
    private javax.swing.JLabel jLabel33;
    private javax.swing.JLabel jLabel34;
    private javax.swing.JLabel jLabel35;
    private javax.swing.JLabel jLabel36;
    private javax.swing.JLabel jLabel37;
    private javax.swing.JLabel jLabel38;
    private javax.swing.JLabel jLabel39;
    private javax.swing.JLabel jLabel40;
    private javax.swing.JLabel jLabel41;
    private javax.swing.JLabel jLabel42;
    private javax.swing.JLabel jLabel43;
    private javax.swing.JLabel jLabel44;
    private javax.swing.JLabel jLabel45;
    private javax.swing.JLabel jLabel46;
    private javax.swing.JLabel jLabel47;
    private javax.swing.JLabel jLabel48;
    private javax.swing.JLabel jLabel49;
    private javax.swing.JLabel jLabel50;
    private javax.swing.JLabel jLabel51;
    private javax.swing.JLabel jLabel52;
    private javax.swing.JLabel jLabel53;
    private javax.swing.JLabel jLabel54;
    private javax.swing.JLabel jLabel55;
    private javax.swing.JLabel jLabel56;
    private javax.swing.JLabel jLabel57;
    private javax.swing.JLabel jLabel58;
    private javax.swing.JLabel jLabel59;
    private javax.swing.JLabel jLabel60;
    private javax.swing.JLabel jLabel61;
    private javax.swing.JLabel jLabel62;
    private javax.swing.JLabel jLabel63;
    private javax.swing.JLabel jLabel64;
    private javax.swing.JLabel jLabel65;
    private javax.swing.JLabel jLabel66;
    private javax.swing.JLabel jLabel67;
    private javax.swing.JLabel jLabel68;
    private javax.swing.JLabel jLabel69;
    private javax.swing.JLabel jLabel70;
    private javax.swing.JLabel jLabel71;
    private javax.swing.JLabel jLabel72;
    private javax.swing.JLabel jLabel73;
    private javax.swing.JLabel jLabel74;
    private javax.swing.JLabel jLabel75;
    private javax.swing.JLabel jLabel76;
    private javax.swing.JLabel jLabel77;
    private javax.swing.JLabel jLabel78;
    private javax.swing.JLabel jLabel79;
    private javax.swing.JLabel jLabel80;
    private javax.swing.JLabel jLabel81;
    private javax.swing.JLabel jLabel82;
    private javax.swing.JLabel jLabel83;
    private javax.swing.JLabel jLabel84;
    private javax.swing.JLabel jLabel85;
    private javax.swing.JLabel jLabel86;
    private javax.swing.JLabel jLabel87;
    private javax.swing.JLabel jLabel88;
    private javax.swing.JLabel jLabel89;
    private javax.swing.JLabel jLabel90;
    private javax.swing.JLabel jLabel91;
    private javax.swing.JMenuItem jMenuItem1;
    private javax.swing.JMenuItem jMenuItem11;
    private javax.swing.JMenuItem jMenuItem12;
    private javax.swing.JMenuItem jMenuItem13;
    private javax.swing.JMenuItem jMenuItem14;
    private javax.swing.JMenuItem jMenuItem15;
    private javax.swing.JMenuItem jMenuItem16;
    private javax.swing.JMenuItem jMenuItem17;
    private javax.swing.JMenuItem jMenuItem19;
    private javax.swing.JMenuItem jMenuItem2;
    private javax.swing.JMenuItem jMenuItem20;
    private javax.swing.JMenuItem jMenuItem21;
    private javax.swing.JMenuItem jMenuItem3;
    private javax.swing.JMenuItem jMenuItem5;
    private javax.swing.JMenuItem jMenuItem6;
    private javax.swing.JMenuItem jMenuItem7;
    private javax.swing.JMenuItem jMenuItem8;
    private javax.swing.JMenuItem jMenuItem9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel10;
    private javax.swing.JPanel jPanel11;
    private javax.swing.JPanel jPanel12;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel21;
    private javax.swing.JPanel jPanel22;
    private javax.swing.JPanel jPanel25;
    private javax.swing.JPanel jPanel26;
    private javax.swing.JPanel jPanel27;
    private javax.swing.JPanel jPanel28;
    private javax.swing.JPanel jPanel29;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel30;
    private javax.swing.JPanel jPanel31;
    private javax.swing.JPanel jPanel32;
    private javax.swing.JPanel jPanel33;
    private javax.swing.JPanel jPanel34;
    private javax.swing.JPanel jPanel35;
    private javax.swing.JPanel jPanel36;
    private javax.swing.JPanel jPanel37;
    private javax.swing.JPanel jPanel39;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JPanel jPanel7;
    private javax.swing.JPanel jPanel8;
    private javax.swing.JPanel jPanel9;
    private javax.swing.JPopupMenu jPopupMenu1;
    private javax.swing.JPopupMenu jPopupMenu2;
    private javax.swing.JPopupMenu jPopupMenu3;
    private javax.swing.JPopupMenu jPopupMenu4;
    private javax.swing.JPopupMenu jPopupMenu5;
    private javax.swing.JPopupMenu jPopupMenu6;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane10;
    private javax.swing.JScrollPane jScrollPane11;
    private javax.swing.JScrollPane jScrollPane12;
    private javax.swing.JScrollPane jScrollPane13;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JScrollPane jScrollPane4;
    private javax.swing.JScrollPane jScrollPane5;
    private javax.swing.JScrollPane jScrollPane6;
    private javax.swing.JScrollPane jScrollPane7;
    private javax.swing.JScrollPane jScrollPane8;
    private javax.swing.JScrollPane jScrollPane9;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JSeparator jSeparator10;
    private javax.swing.JSeparator jSeparator11;
    private javax.swing.JSeparator jSeparator12;
    private javax.swing.JSeparator jSeparator13;
    private javax.swing.JSeparator jSeparator2;
    private javax.swing.JSeparator jSeparator3;
    private javax.swing.JSeparator jSeparator4;
    private javax.swing.JSeparator jSeparator5;
    private javax.swing.JSeparator jSeparator6;
    private javax.swing.JSeparator jSeparator7;
    private javax.swing.JSeparator jSeparator8;
    private javax.swing.JSeparator jSeparator9;
    private javax.swing.JSpinner jSpinner1;
    private javax.swing.JSpinner jSpinner2;
    private javax.swing.JTabbedPane jTabbedPane1;
    private javax.swing.JTable jTable1;
    private javax.swing.JTable jTable10;
    private javax.swing.JTable jTable11;
    private javax.swing.JTable jTable2;
    private javax.swing.JTable jTable3;
    private javax.swing.JTable jTable4;
    private javax.swing.JTable jTable5;
    private javax.swing.JTable jTable6;
    private javax.swing.JTable jTable7;
    private javax.swing.JTable jTable8;
    private javax.swing.JTable jTable9;
    private javax.swing.JTextArea jTextArea1;
    private javax.swing.JTextArea jTextArea2;
    private javax.swing.JTextField jTextField1;
    private javax.swing.JTextField jTextField10;
    private javax.swing.JTextField jTextField11;
    private javax.swing.JTextField jTextField12;
    private javax.swing.JTextField jTextField13;
    private javax.swing.JTextField jTextField14;
    private javax.swing.JTextField jTextField15;
    private javax.swing.JTextField jTextField16;
    private javax.swing.JTextField jTextField17;
    private javax.swing.JTextField jTextField18;
    private javax.swing.JTextField jTextField19;
    private javax.swing.JTextField jTextField2;
    private javax.swing.JTextField jTextField20;
    private javax.swing.JTextField jTextField3;
    private javax.swing.JTextField jTextField4;
    private javax.swing.JTextField jTextField5;
    private javax.swing.JTextField jTextField6;
    private javax.swing.JTextField jTextField7;
    private javax.swing.JTextField jTextField8;
    private javax.swing.JTextField jTextField9;
    private javax.swing.JPanel lineChartPanel;
    private javax.swing.JPanel pieChart1Panel;
    // End of variables declaration//GEN-END:variables

    // home
    private void reset() {
        jButton20.setBackground(new Color(255, 255, 255));
        jButton22.setBackground(new Color(255, 255, 255));
        jButton28.setBackground(new Color(255, 255, 255));
        jButton43.setBackground(new Color(255, 255, 255));
        jButton44.setBackground(new Color(255, 255, 255));
        jButton45.setBackground(new Color(255, 255, 255));
        jButton46.setBackground(new Color(255, 255, 255));
        jButton47.setBackground(new Color(255, 255, 255));
        jButton48.setBackground(new Color(255, 255, 255));
        jButton49.setBackground(new Color(255, 255, 255));
    }

    // fruit registration
    private void reset1() {
        jTextField3.setText("");
        jTextField11.setText("");
        jTable11.clearSelection();
        jButton26.setEnabled(true);
        jButton27.setEnabled(false);
        loadFruitId();
        loadFruitTable("");
    }

    // plantation
    private void reset2() {
        jTextField1.setText("");
        jTable1.clearSelection();
        jButton3.setEnabled(true);
        jButton2.setEnabled(false);
        jComboBox1.setSelectedIndex(0);
        jComboBox5.setSelectedIndex(0);
        jComboBox6.setSelectedIndex(0);
        generatePlantationId();
    }

    // yield analytics
    private void reset3() {
        jTable2.clearSelection();
        jComboBox2.setSelectedIndex(0);
        jDateChooser1.setDate(null);
        jDateChooser2.setDate(null);
        loadYieldAnalyticsTable();
        loadYieldAnalyticsLabels();
    }

    // workflow
    private void reset4() {
        tf.setText("--:--");
        tf1.setValue(new java.util.Date());
        jTextField4.setText("");
        jTextArea1.setText("");
        jTable3.clearSelection();
        jTable4.clearSelection();
        jButton29.setEnabled(true);
        jButton4.setEnabled(true);
        jButton5.setEnabled(false);
        jButton35.setEnabled(false);
        jComboBox3.setSelectedIndex(0);
        jComboBox13.setSelectedIndex(0);
        jDateChooser3.setDate(null);
        jDateChooser4.setDate(null);
        jDateChooser3.setEnabled(true);
        jSpinner1.setEnabled(true);
        jDateChooser4.setEnabled(false);
        jSpinner2.setEnabled(false);
        jTextArea1.setEditable(true);
        jComboBox3.setEnabled(true);
        jComboBox13.setEnabled(true);
    }

    // progress
    private void reset5() {
        try {
            isResetting = true;
            jTextField2.setText("");
            jTable5.clearSelection();
            resetLabelColors();
            loadProgressTable("");
        } finally {
            isResetting = false;
        }
    }

    // worker performance
    private void reset6() {
        jTextField20.setText("");
        jTable6.clearSelection();
        jComboBox7.setSelectedIndex(0);
        loadWorkerTable("ASC", jTextField20.getText());
    }

    // stock
    private void reset7() {
        jTextField12.setText("");
        jTextField13.setText("");
        jTextField15.setText("");
        jTextField16.setText("");
        jTable7.clearSelection();
        jButton18.setEnabled(true);
        jButton19.setEnabled(false);
        jComboBox8.setSelectedIndex(0);
        generateStockId();
        loadStockTable("total_weight", "ASC", "", "");
    }

    // raw materials
    private void reset8() {
        jTextField7.setText("");
        jTextField10.setText("");
        jTextField14.setText("");
        jTextField18.setText("");
        jTable8.clearSelection();
        jTable9.clearSelection();
        jButton14.setEnabled(true);
        jButton15.setEnabled(false);
        jButton23.setEnabled(false);
        jButton34.setEnabled(true);
        jButton51.setEnabled(true);
        jComboBox9.setSelectedIndex(0);
        jLabel87.setText("All Raw Materials");
        generateRawId();
        loadRawTable("qty", "ASC", "", "");
        loadMaterialTable("", "");
    }

    // issues
    private void reset9() {
        jTextArea2.setText("");
        jComboBox4.setSelectedIndex(0);
        jTable10.clearSelection();
        jButton8.setEnabled(true);
        jButton21.setEnabled(false);
        loadIssueId();
        loadDate();
        loadIssueTable();
    }

    public void setYieldName(String name) {
        loadYield();
        jComboBox5.setSelectedItem(name);
    }

    public void setTaskStatusName(String name) {
        loadTaskStatus();
        jComboBox13.setSelectedItem(name);
    }

    public void setTaskName(String name) {
        loadTasks();
        jComboBox3.setSelectedItem(name);
    }

    public void setIssueType(String name) {
        loadIssueType();
        jComboBox4.setSelectedItem(name);
    }

    private void generatePlantationId() {
        long id = System.currentTimeMillis();
        jTextField6.setText(String.valueOf(id));
    }

    private void generateRawId() {
        long id = System.currentTimeMillis();
        jTextField17.setText(String.valueOf(id));
    }

    private void generateStockId() {
        long id = System.currentTimeMillis();
        jTextField19.setText(String.valueOf(id));
    }

    private void init() {
        // generate plantation id
        generatePlantationId();

        // remove labels
        removeYieldLabels();

        // load tables
        loadPlantationTable("qty", "ASC");
        loadYieldAnalyticsLabels();
        loadYieldAnalyticsTable();
        loadIdleWorkerTable();
        loadTaskAssignedWorkerTable();
        loadProgressTable("");
        loadWorkerTable("ASC", "");
        loadStockTable("total_weight", "ASC", "", "");
        loadRawTable("qty", "ASC", "", "");
        loadMaterialTable("", "");
        loadCost();
        loadIssueTable();
        loadFruitTable("");

        // load ids
        loadIssueId();
        loadFruitId();

        // charts loading
        loadPieChart();
        loadLineChart();

        // adjusting components
        jButton6.setOpaque(false);
        jButton6.setContentAreaFilled(false);
        jButton6.setBorderPainted(false);
        jButton5.setEnabled(false);
        jTextField5.setEditable(false);
        jTextField6.setEditable(false);
        jTextField8.setEditable(false);
        jTextField9.setEditable(false);
        jTextField13.setEditable(false);
        jTextField14.setEditable(false);
        jTextField17.setEditable(false);
        jTextField10.setEditable(false);
        jDateChooser4.setEnabled(false);
        jSpinner2.setEnabled(false);

        jButton29.setOpaque(false);
        jButton29.setContentAreaFilled(false);
        jButton29.setBorderPainted(false);
        jTextField4.setEditable(false);

        jButton30.setOpaque(false);
        jButton30.setContentAreaFilled(false);
        jButton30.setBorderPainted(false);
        jTextField19.setEditable(false);

        jButton31.setOpaque(false);
        jButton31.setContentAreaFilled(false);
        jButton31.setBorderPainted(false);

        jButton7.setOpaque(false);
        jButton7.setContentAreaFilled(false);
        jButton7.setBorderPainted(false);

        jButton32.setOpaque(false);
        jButton32.setContentAreaFilled(false);
        jButton32.setBorderPainted(false);

        jButton33.setOpaque(false);
        jButton33.setContentAreaFilled(false);
        jButton33.setBorderPainted(false);

        jButton34.setOpaque(false);
        jButton34.setContentAreaFilled(false);
        jButton34.setBorderPainted(false);

        jButton36.setOpaque(false);
        jButton36.setContentAreaFilled(false);
        jButton36.setBorderPainted(false);

        jButton37.setOpaque(false);
        jButton37.setContentAreaFilled(false);
        jButton37.setBorderPainted(false);

        jButton38.setOpaque(false);
        jButton38.setContentAreaFilled(false);
        jButton38.setBorderPainted(false);

        jButton39.setOpaque(false);
        jButton39.setContentAreaFilled(false);
        jButton39.setBorderPainted(false);

        jButton40.setOpaque(false);
        jButton40.setContentAreaFilled(false);
        jButton40.setBorderPainted(false);

        jButton41.setOpaque(false);
        jButton41.setContentAreaFilled(false);
        jButton41.setBorderPainted(false);

        jButton42.setOpaque(false);
        jButton42.setContentAreaFilled(false);
        jButton42.setBorderPainted(false);

        jButton50.setOpaque(false);
        jButton50.setContentAreaFilled(false);
        jButton50.setBorderPainted(false);

        jButton51.setOpaque(false);
        jButton51.setContentAreaFilled(false);
        jButton51.setBorderPainted(false);

        jFormattedTextField1.setEditable(false);
        jFormattedTextField2.setEditable(false);
        jFormattedTextField3.setEditable(false);

        jFormattedTextField1.setOpaque(false);
        jFormattedTextField2.setOpaque(false);
        jFormattedTextField3.setOpaque(false);

        // listener adding
        addTableSelectionListener();

        // set time spinner
        jSpinner1.setModel(new SpinnerDateModel());
        JSpinner.DateEditor timeEditor = new JSpinner.DateEditor(jSpinner1, "HH:mm");
        jSpinner1.setEditor(timeEditor);
        tf1 = ((JSpinner.DefaultEditor) jSpinner1.getEditor()).getTextField();
        tf1.setValue(null);
        SimpleDateFormat format = new SimpleDateFormat("HH:mm") {
            @Override
            public Object parseObject(String source, java.text.ParsePosition pos) {
                if ("--:--".equals(source)) {
                    pos.setIndex(5);
                    return null;
                }
                return super.parseObject(source, pos);
            }
        };
        format.setLenient(false);
        timeEditor.getFormat().setLenient(false);
        tf1.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(
                new javax.swing.text.DateFormatter(format)));
        tf1.setValue(new java.util.Date());

        jSpinner2.setModel(new SpinnerDateModel());
        JSpinner.DateEditor timeEditor1 = new JSpinner.DateEditor(jSpinner2, "HH:mm");
        jSpinner2.setEditor(timeEditor1);
        tf = ((JSpinner.DefaultEditor) jSpinner2.getEditor()).getTextField();
        tf.setValue(null);
        SimpleDateFormat format1 = new SimpleDateFormat("HH:mm") {
            @Override
            public Object parseObject(String source, java.text.ParsePosition pos) {
                if ("--:--".equals(source)) {
                    pos.setIndex(5);
                    return null;
                }
                return super.parseObject(source, pos);
            }
        };
        format1.setLenient(false);
        timeEditor1.getFormat().setLenient(false);
        tf.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(
                new javax.swing.text.DateFormatter(format1)));
        tf.setText("--:--");
        // time spinner end

        // Add KeyListeners to JFormattedTextField
        tf1.addKeyListener(new KeyListener() {
            @Override
            public void keyTyped(KeyEvent e) {
                char ch = e.getKeyChar();
                if ((!Character.isDigit(ch) && ch != ':') || tf1.getText().length() >= 5) {
                    e.consume();
                    Notifications.getInstance().show(Notifications.Type.WARNING, Notifications.Location.BOTTOM_RIGHT, "Invalid time. Check the format.");
                    tf1.setValue(new java.util.Date());
                }
            }

            @Override
            public void keyPressed(KeyEvent e) {
            }

            @Override
            public void keyReleased(KeyEvent e) {
            }
        });

        tf.addKeyListener(new KeyListener() {
            @Override
            public void keyTyped(KeyEvent e) {
                char ch = e.getKeyChar();
                if ((!Character.isDigit(ch) && ch != ':') || tf.getText().length() >= 5) {
                    e.consume();
                    Notifications.getInstance().show(Notifications.Type.WARNING, Notifications.Location.BOTTOM_RIGHT, "Invalid time. Check the format.");
                    tf.setValue(new java.util.Date());
                }
            }

            @Override
            public void keyPressed(KeyEvent e) {
            }

            @Override
            public void keyReleased(KeyEvent e) {
            }
        });

        // Add KeyListeners to JFormattedTextField
        jTextField1.putClientProperty("JComponent.roundRect", true);
        jTextField2.putClientProperty("JComponent.roundRect", true);
        jTextField3.putClientProperty("JComponent.roundRect", true);
        jTextField6.putClientProperty("JComponent.roundRect", true);
        jTextField7.putClientProperty("JComponent.roundRect", true);
        jTextField9.putClientProperty("JComponent.roundRect", true);
        jTextField10.putClientProperty("JComponent.roundRect", true);
        jTextField11.putClientProperty("JComponent.roundRect", true);
        jTextField12.putClientProperty("JComponent.roundRect", true);
        jTextField13.putClientProperty("JComponent.roundRect", true);
        jTextField14.putClientProperty("JComponent.roundRect", true);
        jTextField15.putClientProperty("JComponent.roundRect", true);
        jTextField16.putClientProperty("JComponent.roundRect", true);
        jTextField17.putClientProperty("JComponent.roundRect", true);
        jTextField18.putClientProperty("JComponent.roundRect", true);
        jTextField19.putClientProperty("JComponent.roundRect", true);
        jTextField20.putClientProperty("JComponent.roundRect", true);

        // placeholders
        jTextField1.putClientProperty(FlatClientProperties.PLACEHOLDER_TEXT, "Enter quantity");
        jTextField3.putClientProperty(FlatClientProperties.PLACEHOLDER_TEXT, "Search");
        jTextField13.putClientProperty(FlatClientProperties.PLACEHOLDER_TEXT, "Paste plantation id");
        jTextField14.putClientProperty(FlatClientProperties.PLACEHOLDER_TEXT, "Paste plantation id");
        jTextField19.putClientProperty(FlatClientProperties.PLACEHOLDER_TEXT, "Enter inventory id");
        jTextField11.putClientProperty(FlatClientProperties.PLACEHOLDER_TEXT, "Enter fruit name");
        jTextField4.putClientProperty(FlatClientProperties.PLACEHOLDER_TEXT, "Select an idle worker");
        jTextField12.putClientProperty(FlatClientProperties.PLACEHOLDER_TEXT, "Search");
        jTextField15.putClientProperty(FlatClientProperties.PLACEHOLDER_TEXT, "Enter quantity");
        jTextField16.putClientProperty(FlatClientProperties.PLACEHOLDER_TEXT, "Enter weight");
        jTextField18.putClientProperty(FlatClientProperties.PLACEHOLDER_TEXT, "Enter material name");
        jTextField2.putClientProperty(FlatClientProperties.PLACEHOLDER_TEXT, "Search");
        jTextField7.putClientProperty(FlatClientProperties.PLACEHOLDER_TEXT, "Search");
        jTextField20.putClientProperty(FlatClientProperties.PLACEHOLDER_TEXT, "Search");

        // center tables
        renderer = new DefaultTableCellRenderer();
        renderer.setHorizontalAlignment(SwingConstants.CENTER);
        renderer.setForeground(this.foreground);
        renderer.setBackground(this.background);

        jTable1.setDefaultRenderer(Object.class, renderer);
        jTable2.setDefaultRenderer(Object.class, renderer);
        jTable3.setDefaultRenderer(Object.class, renderer);
        jTable4.setDefaultRenderer(Object.class, renderer);
        jTable5.setDefaultRenderer(Object.class, renderer);
        jTable6.setDefaultRenderer(Object.class, renderer);
        jTable7.setDefaultRenderer(Object.class, renderer);
        jTable8.setDefaultRenderer(Object.class, renderer);
        jTable9.setDefaultRenderer(Object.class, renderer);
        jTable10.setDefaultRenderer(Object.class, renderer);
        jTable11.setDefaultRenderer(Object.class, renderer);

        generateStockId();

        // load images
        try {
            InputStream imgStream11 = getClass().getResourceAsStream("/lk/corevision/fruitshop/resources/production/copy.png");
            ImageIcon originalIcon11 = new ImageIcon(ImageIO.read(imgStream11));
            Image scaledImage11 = originalIcon11.getImage().getScaledInstance(26, 26, Image.SCALE_SMOOTH);
            jButton6.setIcon(new ImageIcon(scaledImage11));

            Image scaledImage12 = originalIcon11.getImage().getScaledInstance(26, 26, Image.SCALE_SMOOTH);
            jButton30.setIcon(new ImageIcon(scaledImage12));

            Image scaledImage13 = originalIcon11.getImage().getScaledInstance(23, 23, Image.SCALE_SMOOTH);
            jButton29.setIcon(new ImageIcon(scaledImage13));

            Image scaledImage27 = originalIcon11.getImage().getScaledInstance(23, 23, Image.SCALE_SMOOTH);
            jButton51.setIcon(new ImageIcon(scaledImage27));

            InputStream imgStream14 = getClass().getResourceAsStream("/lk/corevision/fruitshop/resources/production/paste.png");
            ImageIcon originalIcon14 = new ImageIcon(ImageIO.read(imgStream14));
            Image scaledImage14 = originalIcon14.getImage().getScaledInstance(25, 25, Image.SCALE_SMOOTH);
            jButton31.setIcon(new ImageIcon(scaledImage14));

            Image scaledImage15 = originalIcon14.getImage().getScaledInstance(22, 22, Image.SCALE_SMOOTH);
            jButton32.setIcon(new ImageIcon(scaledImage15));

            Image scaledImage16 = originalIcon14.getImage().getScaledInstance(26, 26, Image.SCALE_SMOOTH);
            jButton33.setIcon(new ImageIcon(scaledImage16));

            Image scaledImage17 = originalIcon14.getImage().getScaledInstance(24, 24, Image.SCALE_SMOOTH);
            jButton34.setIcon(new ImageIcon(scaledImage17));

            InputStream imgStream18 = getClass().getResourceAsStream("/lk/corevision/fruitshop/resources/production/reset30.png");
            ImageIcon originalIcon18 = new ImageIcon(ImageIO.read(imgStream18));
            Image scaledImage18 = originalIcon18.getImage().getScaledInstance(30, 30, Image.SCALE_SMOOTH);
            jButton36.setIcon(new ImageIcon(scaledImage18));

            Image scaledImage23 = originalIcon18.getImage().getScaledInstance(30, 30, Image.SCALE_SMOOTH);
            jButton41.setIcon(new ImageIcon(scaledImage23));

            Image scaledImage22 = originalIcon18.getImage().getScaledInstance(30, 30, Image.SCALE_SMOOTH);
            jButton40.setIcon(new ImageIcon(scaledImage22));

            Image scaledImage25 = originalIcon18.getImage().getScaledInstance(30, 30, Image.SCALE_SMOOTH);
            jButton7.setIcon(new ImageIcon(scaledImage25));

            InputStream imgStream20 = getClass().getResourceAsStream("/lk/corevision/fruitshop/resources/production/reset32.png");
            ImageIcon originalIcon20 = new ImageIcon(ImageIO.read(imgStream20));
            Image scaledImage20 = originalIcon20.getImage().getScaledInstance(32, 32, Image.SCALE_SMOOTH);
            jButton38.setIcon(new ImageIcon(scaledImage20));

            Image scaledImage26 = originalIcon20.getImage().getScaledInstance(32, 32, Image.SCALE_SMOOTH);
            jButton50.setIcon(new ImageIcon(scaledImage26));

            InputStream imgStream19 = getClass().getResourceAsStream("/lk/corevision/fruitshop/resources/production/reset24.png");
            ImageIcon originalIcon19 = new ImageIcon(ImageIO.read(imgStream19));
            Image scaledImage19 = originalIcon19.getImage().getScaledInstance(24, 24, Image.SCALE_SMOOTH);
            jButton37.setIcon(new ImageIcon(scaledImage19));

            InputStream imgStream21 = getClass().getResourceAsStream("/lk/corevision/fruitshop/resources/production/reset22.png");
            ImageIcon originalIcon21 = new ImageIcon(ImageIO.read(imgStream21));
            Image scaledImage21 = originalIcon21.getImage().getScaledInstance(22, 22, Image.SCALE_SMOOTH);
            jButton39.setIcon(new ImageIcon(scaledImage21));

            InputStream imgStream24 = getClass().getResourceAsStream("/lk/corevision/fruitshop/resources/production/clear_all.png");
            ImageIcon originalIcon24 = new ImageIcon(ImageIO.read(imgStream24));
            Image scaledImage24 = originalIcon24.getImage().getScaledInstance(42, 42, Image.SCALE_SMOOTH);
            jButton42.setIcon(new ImageIcon(scaledImage24));

        } catch (IOException e) {
            SignIn.getLoggerObject().log(Level.WARNING, "production images loading Exception found!", e);
        }

        jButton20.setCursor(new Cursor(Cursor.HAND_CURSOR)); // Hand cursor
        jButton20.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                jButton20.setBackground(new Color(255, 153, 0));
            }

            @Override
            public void mouseExited(java.awt.event.MouseEvent evt) {
                reset();
            }
        });

        jButton22.setCursor(new Cursor(Cursor.HAND_CURSOR)); // Hand cursor
        jButton22.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                jButton22.setBackground(new Color(255, 153, 0));
            }

            @Override
            public void mouseExited(java.awt.event.MouseEvent evt) {
                reset();
            }
        });

        jButton28.setCursor(new Cursor(Cursor.HAND_CURSOR)); // Hand cursor
        jButton28.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                jButton28.setBackground(new Color(255, 153, 0));
            }

            @Override
            public void mouseExited(java.awt.event.MouseEvent evt) {
                reset();
            }
        });

        jButton43.setCursor(new Cursor(Cursor.HAND_CURSOR)); // Hand cursor
        jButton43.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                jButton43.setBackground(new Color(255, 153, 0));
            }

            @Override
            public void mouseExited(java.awt.event.MouseEvent evt) {
                reset();
            }
        });

        jButton44.setCursor(new Cursor(Cursor.HAND_CURSOR)); // Hand cursor
        jButton44.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                jButton44.setBackground(new Color(255, 153, 0));
            }

            @Override
            public void mouseExited(java.awt.event.MouseEvent evt) {
                reset();
            }
        });

        jButton45.setCursor(new Cursor(Cursor.HAND_CURSOR)); // Hand cursor
        jButton45.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                jButton45.setBackground(new Color(255, 153, 0));
            }

            @Override
            public void mouseExited(java.awt.event.MouseEvent evt) {
                reset();
            }
        });

        jButton46.setCursor(new Cursor(Cursor.HAND_CURSOR)); // Hand cursor
        jButton46.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                jButton46.setBackground(new Color(255, 153, 0));
            }

            @Override
            public void mouseExited(java.awt.event.MouseEvent evt) {
                reset();
            }
        });

        jButton47.setCursor(new Cursor(Cursor.HAND_CURSOR)); // Hand cursor
        jButton47.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                jButton47.setBackground(new Color(255, 153, 0));
            }

            @Override
            public void mouseExited(java.awt.event.MouseEvent evt) {
                reset();
            }
        });

        jButton48.setCursor(new Cursor(Cursor.HAND_CURSOR)); // Hand cursor
        jButton48.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                jButton48.setBackground(new Color(255, 153, 0));
            }

            @Override
            public void mouseExited(java.awt.event.MouseEvent evt) {
                reset();
            }
        });

        jButton49.setCursor(new Cursor(Cursor.HAND_CURSOR)); // Hand cursor
        jButton49.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                jButton49.setBackground(new Color(255, 153, 0));
            }

            @Override
            public void mouseExited(java.awt.event.MouseEvent evt) {
                reset();
            }
        });

        // combo boxes loading
        loadFruits();
        loadYield();
        loadTasks();
        loadTaskStatus();
        loadIssueType();
        loadDate();
        generateRawId();

        // disabling buttons
        jButton2.setEnabled(false);
        jButton35.setEnabled(false);
        jButton19.setEnabled(false);
        jButton15.setEnabled(false);
        jButton23.setEnabled(false);
        jButton21.setEnabled(false);
        jButton27.setEnabled(false);
    }

    private void loadIssueId() {
        try {
            ResultSet resultSet = MySQL.executeSearch("SELECT MAX(issue_id) FROM `issue`");

            if (resultSet.next()) {
                int lastIssueId = resultSet.getInt(1);
                jTextField5.setText(String.valueOf(lastIssueId + 1));
            }
        } catch (Exception e) {
            SignIn.getLoggerObject().log(Level.WARNING, "issue id loading Exception found!", e);
        }
    }

    private void loadFruitId() {
        try {
            ResultSet resultSet = MySQL.executeSearch("SELECT MAX(fruit_id) FROM `fruit`");

            if (resultSet.next()) {
                int lastFruitId = resultSet.getInt(1);
                jTextField9.setText(String.valueOf(lastFruitId + 1));
            }
        } catch (Exception e) {
            SignIn.getLoggerObject().log(Level.WARNING, "fruit id loading Exception found!", e);
        }
    }

    private void loadFruitTable(String search) {
        try {

            ResultSet resultSet = MySQL.executeSearch("SELECT * FROM `fruit` "
                    + "WHERE `fruit_id` LIKE '" + search + "%' OR `fruit_name` LIKE '" + search + "%'");

            DefaultTableModel model = (DefaultTableModel) jTable11.getModel();
            model.setRowCount(0);

            while (resultSet.next()) {
                Vector<String> vector = new Vector<>();
                vector.add(resultSet.getString("fruit_id"));
                vector.add(resultSet.getString("fruit_name"));

                model.addRow(vector);
            }

        } catch (Exception e) {
            SignIn.getLoggerObject().log(Level.WARNING, "fruit table loading Exception found!", e);
        }
    }

    public void loadPlantationTable(String column, String orderby) {

        try {

            ResultSet resultSet = MySQL.executeSearch("SELECT * FROM `plantation` "
                    + "INNER JOIN `fruit` ON `plantation`.`fruit_fruit_id` = `fruit`.`fruit_id` "
                    + "INNER JOIN `yield_location` ON `plantation`.`yield_location_id` = `yield_location`.`yield_location_id` "
                    + "ORDER BY `" + column + "` " + orderby + "");

            DefaultTableModel model = (DefaultTableModel) jTable1.getModel();
            model.setRowCount(0);

            while (resultSet.next()) {
                Vector<String> vector = new Vector<>();
                vector.add(resultSet.getString("plantation.plantation_id"));
                vector.add(resultSet.getString("fruit.fruit_name"));
                vector.add(resultSet.getString("yield_location.yield_location"));
                vector.add(resultSet.getString("qty"));
                vector.add(resultSet.getString("date"));

                model.addRow(vector);
            }
            generatePlantationId();
        } catch (Exception e) {
            SignIn.getLoggerObject().log(Level.WARNING, "plantation table loading Exception found!", e);
        }
    }

    // plantation combo box
    private void search() {
        int sort = jComboBox6.getSelectedIndex();

        if (sort == 0) {
            loadPlantationTable("qty", "ASC");
        } else if (sort == 1) {
            loadPlantationTable("qty", "DESC");
        } else if (sort == 2) {
            loadPlantationTable("fruit_name", "ASC");
        } else if (sort == 3) {
            loadPlantationTable("fruit_name", "DESC");
        }
    }

    private void loadYieldAnalyticsTable() {
        try {
            String query = "SELECT * FROM `inventory` "
                    + "INNER JOIN `plantation` ON `inventory`.`plantation_id` = `plantation`.`plantation_id` "
                    + "INNER JOIN `fruit` ON `plantation`.`fruit_fruit_id` = `fruit`.`fruit_id` "
                    + "INNER JOIN `yield_location` ON `plantation`.`yield_location_id` = `yield_location`.`yield_location_id`";

            Date start = jDateChooser1.getDate();
            Date end = jDateChooser2.getDate();
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
            StringBuilder conditions = new StringBuilder();

            if (start != null) {
                conditions.append(" `inventory`.`date` >= '").append(format.format(start)).append("'");
            }

            if (end != null) {
                if (conditions.length() > 0) {
                    conditions.append(" AND");
                }
                conditions.append(" `inventory`.`date` <= '").append(format.format(end)).append("'");
            }

            if (conditions.length() > 0) {
                query += " WHERE" + conditions;
            }

            // Sorting logic
            int sort = jComboBox2.getSelectedIndex();
            query += " ORDER BY ";

            switch (sort) {
                case 0:
                    query += "`inventory`.`total_weight` ASC";
                    break;
                case 1:
                    query += "`inventory`.`total_weight` DESC";
                    break;
                case 2:
                    query += "`fruit`.`fruit_name` ASC";
                    break;
                case 3:
                    query += "`fruit`.`fruit_name` DESC";
                    break;
                default:
                    break;
            }

            ResultSet resultSet = MySQL.executeSearch(query);

            DefaultTableModel model = (DefaultTableModel) jTable2.getModel();
            model.setRowCount(0);

            while (resultSet.next()) {
                Vector<String> vector = new Vector<>();
                vector.add(resultSet.getString("inventory_id"));
                vector.add(resultSet.getString("fruit.fruit_name"));
                vector.add(resultSet.getString("total_weight"));
                vector.add(resultSet.getString("yield_location.yield_location"));
                vector.add(resultSet.getString("inventory.date"));

                model.addRow(vector);
            }

        } catch (Exception e) {
            SignIn.getLoggerObject().log(Level.WARNING, "yield analytics table loading Exception found!", e);
        }
    }

    // inventory combo box
    private void search2() {
        int sort = jComboBox8.getSelectedIndex();

        switch (sort) {
            case 0:
                loadStockTable("total_weight", "ASC", jTextField12.getText(), jTextField13.getText());
                break;
            case 1:
                loadStockTable("total_weight", "DESC", jTextField12.getText(), jTextField13.getText());
                break;
            case 2:
                loadStockTable("fruit_name", "ASC", jTextField12.getText(), jTextField13.getText());
                break;
            case 3:
                loadStockTable("fruit_name", "DESC", jTextField12.getText(), jTextField13.getText());
                break;
            default:
                break;
        }
    }

    // worker combo box
    private void search3() {
        int sort = jComboBox7.getSelectedIndex();
        String search = jTextField20.getText();

        if (sort == 0) {
            loadWorkerTable("ASC", search);
        } else if (sort == 1) {
            loadWorkerTable("DESC", search);
        }
    }

    private void loadWorkerTable(String sortOrder, String search) {

        String query = "SELECT t.employee_NIC AS NIC, "
                + "tn.task_name AS task_name, "
                + "t.task_id AS task_id, "
                + "t.assigned_date AS assigned_date, "
                + "t.due_date AS due_date, "
                + "t.completion_date AS completion_date "
                + "FROM `task` t "
                + "INNER JOIN `employee` e ON e.NIC = t.employee_NIC "
                + "INNER JOIN `employee_type` et ON e.employee_type_employee_type_id = et.employee_type_id "
                + "INNER JOIN `task_name` tn ON t.task_name_id = tn.task_name_id "
                + "INNER JOIN `task_status` ts ON t.task_status_id = ts.task_status_id "
                + "WHERE t.due_date IS NOT NULL AND t.completion_date IS NOT NULL "
                + "AND et.employee_type = 'Worker' "
                + "AND (t.task_id LIKE '" + search + "%' "
                + "OR tn.task_name LIKE '" + search + "%' "
                + "OR t.employee_NIC LIKE '" + search + "%' "
                + "OR t.assigned_date LIKE '" + search + "%' "
                + "OR t.due_date LIKE '" + search + "%' "
                + "OR t.completion_date LIKE '" + search + "%') "
                + "ORDER BY TIMESTAMPDIFF(MINUTE, t.due_date, t.completion_date) " + sortOrder;

        try {
            ResultSet resultSet = MySQL.executeSearch(query);
            DefaultTableModel model = (DefaultTableModel) jTable6.getModel();
            model.setRowCount(0);

            while (resultSet.next()) {
                Vector<Object> rowData = new Vector<>();
                rowData.add(resultSet.getString("task_id"));
                rowData.add(resultSet.getString("task_name"));
                rowData.add(resultSet.getString("NIC"));
                rowData.add(resultSet.getTimestamp("assigned_date"));
                rowData.add(resultSet.getTimestamp("due_date"));
                rowData.add(resultSet.getTimestamp("completion_date"));
                rowData.add(calculateDuration(resultSet.getTimestamp("due_date"), resultSet.getTimestamp("completion_date")));

                model.addRow(rowData);
            }
        } catch (Exception e) {
            SignIn.getLoggerObject().log(Level.WARNING, "worker performance table loading Exception found!", e);
        }
    }

    private void loadStockTable(String column, String orderby, String search, String paste) {
        try {

            String query = "SELECT * FROM `plantation` "
                    + "INNER JOIN `inventory` ON `plantation`.`plantation_id` = `inventory`.`plantation_id` "
                    + "INNER JOIN `fruit` ON `plantation`.`fruit_fruit_id` = `fruit`.`fruit_id` "
                    + "WHERE (`inventory_id` LIKE '" + search + "%' OR `plantation`.`plantation_id` LIKE '" + search + "%' OR `fruit_name` LIKE '" + search + "%' OR `inventory`.`qty` LIKE '" + search + "%' "
                    + "OR `total_weight` LIKE '" + search + "%') "
                    + (paste.isEmpty() ? "" : "AND `plantation`.`plantation_id` = '" + paste + "' ")
                    + "ORDER BY `" + column + "` " + orderby;

            ResultSet resultSet = MySQL.executeSearch(query);

            DefaultTableModel model = (DefaultTableModel) jTable7.getModel();
            model.setRowCount(0);

            while (resultSet.next()) {
                Vector<String> vector = new Vector<>();
                vector.add(resultSet.getString("inventory_id"));
                vector.add(resultSet.getString("plantation.plantation_id"));
                vector.add(resultSet.getString("fruit.fruit_name"));
                vector.add(resultSet.getString("inventory.qty"));
                vector.add(resultSet.getString("total_weight"));
                vector.add(resultSet.getString("inventory.date"));

                model.addRow(vector);
            }

            generateStockId();
        } catch (Exception e) {
            SignIn.getLoggerObject().log(Level.WARNING, "inventory table loading Exception found!", e);
        }
    }

    // raw material combo box
    private void search4() {
        jLabel87.setText("Raw Materials for Selected Plantation");
        int sort = jComboBox9.getSelectedIndex();
        String search = jTextField7.getText();
        String paste = jTextField14.getText();
        String type = jTextField18.getText();

        switch (sort) {
            case 0:
                loadRawTable("qty", "ASC", search, paste);
                loadMaterialTable(paste, type);
                break;
            case 1:
                loadRawTable("qty", "DESC", search, paste);
                loadMaterialTable(paste, type);
                break;
            case 2:
                loadRawTable("fruit_name", "ASC", search, paste);
                loadMaterialTable(paste, type);
                break;
            case 3:
                loadRawTable("fruit_name", "DESC", search, paste);
                loadMaterialTable(paste, type);
                break;
            default:
                break;
        }
    }

    // load raw material below table
    private void loadRawTable(String column, String orderby, String search, String paste) {
        try {

            String query = "SELECT * FROM `plantation` "
                    + "INNER JOIN `fruit` ON `plantation`.`fruit_fruit_id` = `fruit`.`fruit_id` "
                    + "INNER JOIN `yield_location` ON `plantation`.`yield_location_id` = `yield_location`.`yield_location_id` "
                    + "WHERE (`plantation`.`plantation_id` LIKE '" + search + "%' "
                    + "OR `fruit_name` LIKE '" + search + "%' "
                    + "OR `plantation`.`qty` LIKE '" + search + "%' "
                    + "OR `yield_location` LIKE '" + search + "%') "
                    + (paste.isEmpty() ? "" : "AND `plantation`.`plantation_id` = '" + paste + "' ")
                    + "ORDER BY `" + column + "` " + orderby;

            ResultSet resultSet = MySQL.executeSearch(query);

            DefaultTableModel model = (DefaultTableModel) jTable9.getModel();
            model.setRowCount(0);

            while (resultSet.next()) {
                Vector<String> vector = new Vector<>();
                vector.add(resultSet.getString("plantation.plantation_id"));
                vector.add(resultSet.getString("fruit.fruit_name"));
                vector.add(resultSet.getString("plantation.qty"));
                vector.add(resultSet.getString("yield_location.yield_location"));

                model.addRow(vector);
            }

            generateRawId();
        } catch (Exception e) {
            SignIn.getLoggerObject().log(Level.WARNING, "plantation table loading Exception found!", e);
        }
    }

    private void loadProgressTable(String search) {
        try {
            isResetting = true;

            ResultSet resultSet = MySQL.executeSearch("SELECT * FROM `plantation` "
                    + "LEFT JOIN `inventory` ON `plantation`.`plantation_id` = `inventory`.`plantation_id` "
                    + "LEFT JOIN `product_quality` ON `inventory`.`inventory_id` = `product_quality`.`inventory_inventory_id` "
                    + "INNER JOIN `fruit` ON `plantation`.`fruit_fruit_id` = `fruit`.`fruit_id` "
                    + "INNER JOIN `yield_location` ON `plantation`.`yield_location_id` = `yield_location`.`yield_location_id` "
                    + "WHERE `plantation`.`plantation_id` LIKE '" + search + "%' OR `fruit_name` LIKE '" + search + "%' OR `inventory_id` LIKE '" + search + "%' OR `yield_location` LIKE '" + search + "%' OR `total_weight` LIKE '" + search + "%'");

            DefaultTableModel model = (DefaultTableModel) jTable5.getModel();
            model.setRowCount(0);

            while (resultSet.next()) {
                Vector<String> vector = new Vector<>();
                vector.add(resultSet.getString("plantation.plantation_id"));
                vector.add(resultSet.getString("inventory_id"));
                vector.add(resultSet.getString("product_quality_id"));
                vector.add(resultSet.getString("fruit_name"));
                vector.add(resultSet.getString("total_weight"));
                vector.add(resultSet.getString("yield_location.yield_location"));

                model.addRow(vector);
            }

            if (model.getRowCount() == 1) {
                jTable5.setRowSelectionInterval(0, 0);

                resetLabelColors();

                String plantationId = (String) jTable5.getValueAt(0, 0);
                String inventoryId = (String) jTable5.getValueAt(0, 1);
                String productQualityId = (String) jTable5.getValueAt(0, 2);

                updateProgressColors(plantationId, inventoryId, productQualityId);

            } else {
                resetLabelColors();
            }

        } catch (Exception e) {
            SignIn.getLoggerObject().log(Level.WARNING, "progress table loading Exception found!", e);
        } finally {
            isResetting = false;
        }
    }

    // load raw material upper table
    private void loadMaterialTable(String paste, String search) {
        try {
            String query = "SELECT `raw_material`.`raw_material_id`, `raw_material`.`raw_material_name` "
                    + "FROM `plantation` "
                    + "LEFT JOIN `plantation_has_raw_material` ON `plantation`.`plantation_id` = `plantation_has_raw_material`.`plantation_plantation_id` "
                    + "LEFT JOIN `raw_material` ON `plantation_has_raw_material`.`raw_material_raw_material_id` = `raw_material`.`raw_material_id` "
                    + "WHERE (`raw_material`.`raw_material_name` LIKE '" + search + "%' OR '" + search + "' = '') "
                    + "AND `raw_material`.`raw_material_id` IS NOT NULL "
                    + "AND `raw_material`.`raw_material_name` IS NOT NULL";

            if (!paste.isEmpty()) {
                query += " AND `plantation`.`plantation_id` = '" + paste + "'";
            }

            // Add GROUP BY clause
            query += " GROUP BY `raw_material`.`raw_material_id`, `raw_material`.`raw_material_name`";

            ResultSet resultSet = MySQL.executeSearch(query);

            DefaultTableModel model = (DefaultTableModel) jTable8.getModel();
            model.setRowCount(0);

            while (resultSet.next()) {
                Vector<String> vector = new Vector<>();
                vector.add(resultSet.getString("raw_material_id"));
                vector.add(resultSet.getString("raw_material_name"));
                model.addRow(vector);
            }

            if (model.getRowCount() == 1) {
                String rawId = String.valueOf(jTable8.getValueAt(0, 0));
                String name = String.valueOf(jTable8.getValueAt(0, 1));

                if (rawId.equals("null") && name.equals("null")) {
                    generateRawId();
                    jButton14.setEnabled(true);
                    jButton15.setEnabled(false);
                    jButton23.setEnabled(false);
                } else {
                    jButton14.setEnabled(true);
                    jButton15.setEnabled(false);
                    jButton23.setEnabled(false);
                }
            }

        } catch (Exception e) {
            SignIn.getLoggerObject().log(Level.WARNING, "raw material table loading Exception found!", e);
        }
    }

    // load costs
    private void loadCost() {
        resetCost();
        loadWorkerCost();
        loadRawCost();
        loadTotalCost();
    }

    public void loadIssueTable() {
        try {

            ResultSet resultSet = MySQL.executeSearch("SELECT * FROM `issue` "
                    + "INNER JOIN `issue_type` ON `issue`.`issue_type_issue_type_id` = `issue_type`.`issue_type_id`");

            DefaultTableModel model = (DefaultTableModel) jTable10.getModel();
            model.setRowCount(0);

            while (resultSet.next()) {
                Vector<String> vector = new Vector<>();
                vector.add(resultSet.getString("issue_id"));
                vector.add(resultSet.getString("description_of_issue"));
                vector.add(resultSet.getString("issue_type"));
                vector.add(resultSet.getString("issue_date"));
                vector.add(resultSet.getString("employee_NIC"));

                model.addRow(vector);
            }

        } catch (Exception e) {
            SignIn.getLoggerObject().log(Level.WARNING, "issue table loading Exception found!", e);
        }
    }

    private void loadYieldAnalyticsLabels() {
        removeYieldLabels();

        try {
            ResultSet resultSet = MySQL.executeSearch(
                    "SELECT `fruit`.`fruit_name`, `inventory`.`total_weight` "
                    + "FROM `inventory` "
                    + "INNER JOIN `plantation` ON `inventory`.`plantation_id` = `plantation`.`plantation_id` "
                    + "INNER JOIN `fruit` ON `plantation`.`fruit_fruit_id` = `fruit`.`fruit_id` "
                    + "ORDER BY `inventory`.`total_weight` DESC");

            if (resultSet.next()) {
                jLabel31.setText(resultSet.getString("fruit_name"));
                jLabel85.setText(resultSet.getString("total_weight"));
                jLabel83.setText(resultSet.getString("total_weight"));
                jLabel66.setText("kg");
                jLabel67.setText("kg");
                jLabel86.setText("0.0");
                jLabel33.setText("Other");
            }

            if (resultSet.next()) {
                jLabel32.setText(resultSet.getString("fruit_name"));
                jLabel84.setText(resultSet.getString("total_weight"));
                jLabel65.setText("kg");

                double total = Double.parseDouble(jLabel85.getText()) + Double.parseDouble(jLabel84.getText());
                jLabel83.setText(String.valueOf(total));
            }

            if (resultSet.next()) {
                ResultSet sumResult = MySQL.executeSearch("SELECT SUM(`total_weight`) AS total_sum FROM `inventory`");

                if (sumResult.next()) {
                    jLabel83.setText(sumResult.getString("total_sum"));

                    double sum = Double.parseDouble(jLabel83.getText());
                    double total = Double.parseDouble(jLabel85.getText()) + Double.parseDouble(jLabel84.getText());
                    jLabel86.setText(String.valueOf(sum - total));
                }
            }
        } catch (Exception e) {
            SignIn.getLoggerObject().log(Level.WARNING, "yield analytics labels loading Exception found!", e);
        }
    }

    public void loadIdleWorkerTable() {
        try {
            ResultSet resultSet = MySQL.executeSearch(
                    "SELECT `employee`.`NIC`, `employee`.`first_name`, `employee`.`mobile` FROM `employee` "
                    + "INNER JOIN `employee_type` ON `employee`.`employee_type_employee_type_id` = `employee_type`.`employee_type_id` "
                    + "LEFT JOIN `task` ON `employee`.`NIC` = `task`.`employee_NIC` "
                    + "WHERE `employee_type`.`employee_type` = 'Worker' "
                    + "AND (`task`.`employee_NIC` IS NULL OR `task`.`completion_date` IS NOT NULL) "
                    + "AND NOT EXISTS (SELECT 1 FROM `task` t "
                    + "WHERE t.`employee_NIC` = `employee`.`NIC` AND t.`completion_date` IS NULL) "
                    + "GROUP BY `employee`.`NIC`, `employee`.`first_name`, `employee`.`mobile`");

            DefaultTableModel model = (DefaultTableModel) jTable3.getModel();
            model.setRowCount(0);

            while (resultSet.next()) {
                Vector<String> vector = new Vector<>();
                vector.add(resultSet.getString("NIC"));
                vector.add(resultSet.getString("first_name"));
                vector.add(resultSet.getString("mobile"));

                model.addRow(vector);
            }

        } catch (Exception e) {
            SignIn.getLoggerObject().log(Level.WARNING, "idle worker table loading Exception found!", e);
        }
    }

    public void loadTaskAssignedWorkerTable() {
        try {
            ResultSet resultSet = MySQL.executeSearch(
                    "SELECT employee.NIC, employee.first_name, "
                    + "task_name.task_name AS task_name, "
                    + "task_status.task_status AS task_status, "
                    + "task.task_id AS task_id "
                    + "FROM `employee` "
                    + "INNER JOIN `employee_type` ON `employee`.`employee_type_employee_type_id` = `employee_type`.`employee_type_id` "
                    + "INNER JOIN `task` ON `employee`.`NIC` = `task`.`employee_NIC` "
                    + "INNER JOIN `task_name` ON `task`.`task_name_id` = `task_name`.`task_name_id` "
                    + "INNER JOIN `task_status` ON `task`.`task_status_id` = `task_status`.`task_status_id` "
                    + "WHERE `employee_type`.`employee_type` = 'Worker' "
                    + "AND `task`.`completion_date` IS NULL");

            DefaultTableModel model = (DefaultTableModel) jTable4.getModel();
            model.setRowCount(0);

            while (resultSet.next()) {
                Vector<String> vector = new Vector<>();
                vector.add(resultSet.getString("NIC"));
                vector.add(resultSet.getString("task_id"));
                vector.add(resultSet.getString("task_name"));
                vector.add(resultSet.getString("task_status"));

                model.addRow(vector);
            }
        } catch (Exception e) {
            SignIn.getLoggerObject().log(Level.WARNING, "task assigned worker table loading Exception found!", e);
        }
    }

    private void loadTotalCost() {
        DecimalFormat df = new DecimalFormat("0.00");
        jFormattedTextField1.setText(df.format(
                Double.parseDouble(jFormattedTextField2.getText())
                + Double.parseDouble(jFormattedTextField3.getText())));
    }

    private void loadWorkerCost() {
        try {
            ResultSet resultSet = MySQL.executeSearch(
                    "SELECT SUM(`employee_salary`.`total_salary`) AS total_salary "
                    + "FROM `employee` "
                    + "INNER JOIN `employee_salary` ON `employee`.`NIC` = `employee_salary`.`employee_NIC` "
                    + "INNER JOIN `employee_type` ON `employee`.`employee_type_employee_type_id` = `employee_type`.`employee_type_id` "
                    + "WHERE `employee_type`.`employee_type` = 'Worker'");

            if (resultSet.next()) {
                double totalSalary = resultSet.getDouble("total_salary");

                DecimalFormat df = new DecimalFormat("0.00");
                jFormattedTextField2.setText(df.format(totalSalary));
            }
        } catch (Exception e) {
            SignIn.getLoggerObject().log(Level.WARNING, "worker cost Exception found!", e);
        }
    }

    private void loadRawCost() {
        try {
            ResultSet resultSet = MySQL.executeSearch("SELECT SUM(`unit_price` * `qty`) AS total_cost FROM `raw_material`");

            if (resultSet.next()) {
                double totalCost = resultSet.getDouble("total_cost");

                DecimalFormat df = new DecimalFormat("0.00");
                jFormattedTextField3.setText(df.format(totalCost));
            }
        } catch (Exception e) {
            SignIn.getLoggerObject().log(Level.WARNING, "raw material cost Exception found!", e);
        }
    }

    private void removeYieldLabels() {
        // remove yield labels
        jLabel83.setText("0");
        jLabel31.setText("");
        jLabel85.setText("");
        jLabel66.setText("");
        jLabel32.setText("");
        jLabel65.setText("");
        jLabel84.setText("");
        jLabel33.setText("");
        jLabel67.setText("");
        jLabel86.setText("");
    }

    private void resetCost() {
        jFormattedTextField1.setText("0.00");
        jFormattedTextField2.setText("0.00");
        jFormattedTextField3.setText("0.00");
    }

    public static Timestamp getCombinedDateTime(JDateChooser dateChooser, JSpinner timeSpinner) {
        Date date = dateChooser.getDate();
        Date time = (Date) timeSpinner.getValue();

        if (date == null || time == null) {
            return null;
        }

        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        Calendar timeCalendar = Calendar.getInstance();
        timeCalendar.setTime(time);

        calendar.set(Calendar.HOUR_OF_DAY, timeCalendar.get(Calendar.HOUR_OF_DAY));
        calendar.set(Calendar.MINUTE, timeCalendar.get(Calendar.MINUTE));
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);

        return new Timestamp(calendar.getTimeInMillis());
    }

    private String calculateDuration(Timestamp dueDate, Timestamp completionDate) {
        long milliseconds = completionDate.getTime() - dueDate.getTime();
        long minutes = (milliseconds / (1000 * 60)) % 60;
        long hours = milliseconds / (1000 * 60 * 60);

        return String.format("%02d hours, %02d minutes", hours, minutes);
    }

    private void generateReport(String periodType) {

        int sort = jComboBox7.getSelectedIndex();
        String sortOrder = null;

        if (sort == 0) {
            sortOrder = "ASC";
        } else if (sort == 1) {
            sortOrder = "DESC";
        }

        try {

            String query = "SELECT t.employee_NIC AS NIC, "
                    + "tn.task_name AS task_name, "
                    + "t.task_id AS task_id, "
                    + "t.assigned_date AS assigned_date, "
                    + "t.due_date AS due_date, "
                    + "t.completion_date AS completion_date "
                    + "FROM `task` t "
                    + "INNER JOIN `employee` e ON e.NIC = t.employee_NIC "
                    + "INNER JOIN `employee_type` et ON e.employee_type_employee_type_id = et.employee_type_id "
                    + "INNER JOIN `task_name` tn ON t.task_name_id = tn.task_name_id "
                    + "INNER JOIN `task_status` ts ON t.task_status_id = ts.task_status_id "
                    + "WHERE t.due_date IS NOT NULL AND t.completion_date IS NOT NULL "
                    + "AND et.employee_type = 'Worker' "
                    + "AND ";

            switch (periodType) {
                case "Daily":
                    query += "DATE(t.assigned_date) = CURDATE()";
                    break;
                case "Monthly":
                    query += "MONTH(t.assigned_date) = MONTH(CURDATE()) "
                            + "AND YEAR(t.assigned_date) = YEAR(CURDATE())";
                    break;
                case "Annual":
                    query += "YEAR(t.assigned_date) = YEAR(CURDATE())";
                    break;
                default:
                    throw new IllegalArgumentException("Invalid period type");
            }
            query += " ORDER BY TIMESTAMPDIFF(MINUTE, t.due_date, t.completion_date) " + sortOrder;

            ResultSet resultSet = MySQL.executeSearch(query);
            DefaultTableModel model = (DefaultTableModel) jTable6.getModel();
            model.setRowCount(0);

            int totalDurationMinutes = 0;

            while (resultSet.next()) {
                totalTasksCompleted++;

                String durationString = calculateDuration(resultSet.getTimestamp("due_date"), resultSet.getTimestamp("completion_date"));
                String[] parts = durationString.split(", ");

                int hours = Integer.parseInt(parts[0].replace(" hours", "").trim());
                int minutes = Integer.parseInt(parts[1].replace(" minutes", "").trim());

                int duration = (hours * 60) + minutes;
                totalDurationMinutes += duration;

                avgDuration = totalTasksCompleted > 0 ? (double) totalDurationMinutes / totalTasksCompleted : 0;

                Vector<Object> rowData = new Vector<>();
                rowData.add(resultSet.getString("task_id"));
                rowData.add(resultSet.getString("task_name"));
                rowData.add(resultSet.getString("NIC"));
                rowData.add(resultSet.getTimestamp("assigned_date"));
                rowData.add(resultSet.getTimestamp("due_date"));
                rowData.add(resultSet.getTimestamp("completion_date"));
                rowData.add(calculateDuration(resultSet.getTimestamp("due_date"), resultSet.getTimestamp("completion_date")));

                model.addRow(rowData);
            }
        } catch (SQLException e) {
            SignIn.getLoggerObject().log(Level.WARNING, "worker performance table loading Exception found!", e);
        } catch (Exception ex) {
            SignIn.getLoggerObject().log(Level.WARNING, "generateReport method Exception found!", ex);
        }
    }

    private void loadPieChart() {
        pieChart1 = new PieChart();
        pieChart1.setChartType(PieChart.PeiChartType.DEFAULT);
        pieChart1.clearData();

        String query = "SELECT fruit.fruit_name, SUM(inventory.total_weight) AS total_weight FROM plantation "
                + "LEFT JOIN inventory ON plantation.plantation_id = inventory.plantation_id "
                + "INNER JOIN fruit ON plantation.fruit_fruit_id = fruit.fruit_id "
                + "GROUP BY fruit.fruit_name";

        try {
            ResultSet resultSet = MySQL.executeSearch(query);
            boolean hasData = false;
            int index = 1;
            while (resultSet.next()) {
                hasData = true;
                String fruitName = resultSet.getString("fruit_name");
                double totalWeight = resultSet.getDouble("total_weight");
                pieChart1.addData(new ModelPieChart(fruitName, totalWeight, getColor(index++)));

            }
            if (!hasData) {
                pieChart1.addData(new ModelPieChart("Mango", 30, getColor(index++)));
                pieChart1.addData(new ModelPieChart("Guava", 20, getColor(index++)));
                pieChart1.addData(new ModelPieChart("Pineapple", 5, getColor(index++)));
                pieChart1.addData(new ModelPieChart("Papaya", 10, getColor(index++)));
                pieChart1.addData(new ModelPieChart("Banana", 15, getColor(index++)));
            }

        } catch (Exception e) {
            SignIn.getLoggerObject().log(Level.WARNING, "pie chart loading Exception found!", e);
        }
        pieChart1.setBounds(0, 0, pieChart1Panel.getWidth(), pieChart1Panel.getHeight());
        pieChart1Panel.setLayout(new BorderLayout());
        pieChart1Panel.add(pieChart1, BorderLayout.CENTER);
        pieChart1Panel.revalidate();
        pieChart1Panel.repaint();
    }

    private Color getColor(int index) {
        Color[] color = new Color[]{
            new Color(255, 99, 132),
            new Color(54, 162, 235),
            new Color(75, 192, 192),
            new Color(255, 159, 64),
            new Color(153, 102, 255),
            new Color(128, 0, 128),
            new Color(0, 128, 128),
            new Color(220, 20, 60)
        };
        return color[index % color.length];
    }

    private void loadLineChart() {
        chart = new CurveLineChart();

        chart.addLegend("Tasks Completed", Color.decode("#0099F7"), Color.decode("#F11712"));
        chart.addLegend("Pending Workers", Color.decode("#4682B4"), Color.decode("#32CD32"));
        chart.addLegend("Total Workers Assigned", Color.decode("#FF1493"), Color.decode("#FFD700"));
        chart.addLegend("Average Completion Time", Color.decode("#FFA500"), Color.decode("#00008B"));

        setChartData();
        lineChartPanel.setLayout(new BorderLayout());
        lineChartPanel.add(chart, BorderLayout.CENTER);
        lineChartPanel.revalidate();
        lineChartPanel.repaint();
    }

    private void setChartData() {
        chart.clear();

        try {
            ResultSet resultSet1 = MySQL.executeSearch("SELECT DATE(t.completion_date) AS completion_date, COUNT(DISTINCT t.employee_NIC) AS tasks_completed FROM task t WHERE t.completion_date IS NOT NULL GROUP BY DATE(t.completion_date) ORDER BY DATE(t.completion_date) ASC");
            ResultSet resultSet2 = MySQL.executeSearch("SELECT DATE(t.completion_date) AS completion_date, (SELECT COUNT(DISTINCT t1.employee_NIC) FROM task t1 WHERE t1.completion_date IS NULL AND t1.due_date >= CURDATE()) AS pending_workers FROM task t WHERE t.completion_date IS NOT NULL GROUP BY DATE(t.completion_date) ORDER BY DATE(t.completion_date) ASC");
            ResultSet resultSet3 = MySQL.executeSearch("SELECT DATE(t.completion_date) AS completion_date, (SELECT COUNT(DISTINCT t1.employee_NIC) FROM task t1 WHERE DATE(t1.assigned_date) = DATE(t.assigned_date)) AS total_workers_assigned FROM task t WHERE t.completion_date IS NOT NULL GROUP BY DATE(t.completion_date) ORDER BY DATE(t.completion_date) ASC");
            ResultSet resultSet4 = MySQL.executeSearch("SELECT DATE(t.completion_date) AS completion_date, COALESCE(AVG(TIMESTAMPDIFF(HOUR, t.due_date, t.completion_date)), 0) AS avg_completion_time FROM task t WHERE t.completion_date IS NOT NULL AND t.due_date IS NOT NULL GROUP BY DATE(t.completion_date) ORDER BY DATE(t.completion_date) ASC");

            while (resultSet1.next() && resultSet2.next() && resultSet3.next() && resultSet4.next()) {
                String date = resultSet1.getString("completion_date");

                chart.addData(new ModelChart(date, new double[]{
                    resultSet1.getInt("tasks_completed"),
                    resultSet2.getInt("pending_workers"),
                    resultSet3.getInt("total_workers_assigned"),
                    resultSet4.getDouble("avg_completion_time")
                }));
            }

        } catch (Exception e) {
            LocalDate today = LocalDate.now();
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

            chart.addData(new ModelChart(today.minusDays(3).format(formatter), new double[]{11.1, 1.1, 2.2, 4.4}));
            chart.addData(new ModelChart(today.minusDays(2).format(formatter), new double[]{13.3, 6.7, 3.3, 6.7}));
            chart.addData(new ModelChart(today.minusDays(1).format(formatter), new double[]{4.4, 1.1, 20.0, 0.0}));
            chart.addData(new ModelChart(today.format(formatter), new double[]{10.7, 15.6, 2.2, 1.1}));
        }
        chart.start();
    }

    private void addTableSelectionListener() {
        jTable5.getSelectionModel().addListSelectionListener(e -> {
            if (!isResetting && !e.getValueIsAdjusting()) {
                int row = jTable5.getSelectedRow();

                if (row == -1) {
                    Notifications.getInstance().show(Notifications.Type.WARNING, Notifications.Location.BOTTOM_RIGHT, "Please select a row in the table");
                } else {
                    resetLabelColors();
                    String plantationId = (String) jTable5.getValueAt(row, 0);
                    String inventoryId = (String) jTable5.getValueAt(row, 1);
                    String productQualityId = (String) jTable5.getValueAt(row, 2);
                    updateProgressColors(plantationId, inventoryId, productQualityId);
                }
            }
        });
    }

    private void resetLabelColors() {
        jLabel27.setBackground(new Color(250, 250, 250));
        jLabel44.setBackground(new Color(250, 250, 250));
        jLabel45.setBackground(new Color(250, 250, 250));
        jSeparator3.setBackground(new Color(250, 250, 250));
        jSeparator4.setBackground(new Color(250, 250, 250));
    }

    private void updateProgressColors(String plantationId, String inventoryId, String productQualityId) {
        if (plantationId != null && !plantationId.isEmpty()) {
            if ((inventoryId == null || inventoryId.isEmpty()) && (productQualityId == null || productQualityId.isEmpty())) {
                jLabel27.setBackground(new Color(0, 255, 0));
            } else if (inventoryId != null && !inventoryId.isEmpty() && (productQualityId == null || productQualityId.isEmpty())) {
                jLabel27.setBackground(new Color(0, 255, 0));
                jLabel44.setBackground(new Color(0, 255, 0));
                jSeparator3.setBackground(new Color(0, 255, 0));
            } else if (inventoryId != null && !inventoryId.isEmpty() && productQualityId != null && !productQualityId.isEmpty()) {
                jLabel27.setBackground(new Color(0, 255, 0));
                jLabel44.setBackground(new Color(0, 255, 0));
                jLabel45.setBackground(new Color(0, 255, 0));
                jSeparator3.setBackground(new Color(0, 255, 0));
                jSeparator4.setBackground(new Color(0, 255, 0));
            }
        }
    }
}
