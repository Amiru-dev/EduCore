/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JPanel.java to edit this template
 */
package lk.corevision.fruitshop.gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.event.MouseEvent;
import javax.swing.table.DefaultTableModel;
import lk.corevision.fruitshop.model.MySQL;
import java.sql.ResultSet;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.SwingUtilities;
import lk.corevision.fruitshop.model.SendLogo;
import lk.corevision.fruitshop.validate.DataValidator;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.data.JRTableModelDataSource;
import net.sf.jasperreports.view.JasperViewer;
import raven.chart.ModelChart;

/**
 *
 * @author kavin
 */
public class productQuality extends javax.swing.JPanel {

    private Dashboard dashboard;
    public static HashMap<String, Integer> temperatureMap = new HashMap<>();
    public static HashMap<String, Integer> humidityMap = new HashMap<>();
    private String q = "SELECT * FROM `storage_section` INNER JOIN `temperature` ON `storage_section`.`temperature_id`=`temperature`.`temperature_id`"
            + " INNER JOIN `humidity` ON `humidity`.`humidity_id`=`storage_section`.`humidity_id`";
    private HashMap<String, Integer> gradeMap = new HashMap<>();

    /**
     * Creates new form productQuality
     */
    private ProductPacking productPacking;

    public productQuality() {
        initComponents();
        loadTemperature();
        loadHumidity();
        storageQuality(q);
        loadTools();
        loadGrade();
        //   loadQuality();
        loadProductQuality();
        jTextField3.setEditable(false);
        jTextField1.setEditable(false);
        productQualityFruitName.setEditable(false);

        jPanel3.removeAll();
        this.productPacking = new ProductPacking();
        jPanel3.add(productPacking, BorderLayout.CENTER);
        SwingUtilities.updateComponentTreeUI(jPanel3);
        //       productPacking.setDashboard(dashboard);
        chartSetup();
        chartData();
    }

    private void chartSetup() {
        curveLineChart2.setTitle("Order Status");
        curveLineChart2.addLegend("Amount", Color.decode("#7b4397"), Color.decode("#dc2430"));
        curveLineChart2.addLegend("Cost", Color.decode("#e65c00"), Color.decode("#F9D423"));
        curveLineChart2.addLegend("Profit", Color.decode("#0099F7"), Color.decode("#F11712"));
        curveLineChart2.addLegend("test", Color.decode("#0099F7"), Color.decode("#F11712"));

    }

    private void chartData() {

        //Line Chart
        curveLineChart2.clear();
        curveLineChart2.addData(new ModelChart("January", new double[]{500, 50, 100, 200}));
        curveLineChart2.addData(new ModelChart("February", new double[]{600, 300, 150, 300}));
        curveLineChart2.addData(new ModelChart("March", new double[]{200, 50, 900, 0}));
        curveLineChart2.addData(new ModelChart("April", new double[]{480, 700, 100, 50}));
        curveLineChart2.addData(new ModelChart("May", new double[]{350, 540, 500, 400}));
        curveLineChart2.addData(new ModelChart("June", new double[]{450, 800, 100, 600}));
        curveLineChart2.start();

    }

    private void loadProductQuality() {
        String query = "SELECT * FROM `product_quality` INNER JOIN `grade` ON `grade`.`grade_id`=`product_quality`.`grade_grade_id` "
                + "INNER JOIN `employee` ON `employee`.`NIC`=`product_quality`.`employee_NIC`"
                + " INNER JOIN `inventory` ON `product_quality`.`inventory_inventory_id`=`inventory`.`inventory_id`"
                + " INNER JOIN `plantation` ON `plantation`.`plantation_id`=`inventory`.`plantation_id` INNER JOIN `fruit` ON `plantation`.`fruit_fruit_id`=`fruit`.`fruit_id` ";

        if (!productQualityFruitNameSearch.getText().isEmpty()) {
            query += "WHERE `fruit`.`fruit_name` LIKE '%" + productQualityFruitNameSearch.getText() + "%' ";

        }

        if (query.contains("WHERE")) {
            query += " AND ";
        } else {
            query += " WHERE ";
        }

        Date sd = null;
        Date ed = null;

        SimpleDateFormat sdc = new SimpleDateFormat("yyyy-MM-dd");

        if (jDateChooser1.getDate() != null && jDateChooser3.getDate() == null) {
            sd = jDateChooser1.getDate();
            query += " DATE( `product_quality`.`date`)  > '" + sdc.format(sd) + "' ";

        } else if (jDateChooser1.getDate() == null && jDateChooser3.getDate() != null) {
            ed = jDateChooser3.getDate();
            query += " DATE(`product_quality`.`date`)  < '" + sdc.format(ed) + "'";
        } else if (jDateChooser1.getDate() != null && jDateChooser3.getDate() != null) {
            sd = jDateChooser1.getDate();
            ed = jDateChooser3.getDate();

            query += " DATE(`product_quality`.`date` ) BETWEEN '" + sdc.format(sd) + "'  AND  '" + sdc.format(ed) + "'";

        }

        int sort = jComboBox2.getSelectedIndex();

        query += "ORDER BY ";

        query = query.replace("WHERE ORDER BY", "ORDER BY");

        query = query.replace("AND ORDER BY", "ORDER BY");

        if (sort == 0) {
            query += "`product_quality`.`product_quality_id` ASC";
        } else if (sort == 1) {
            query += "`product_quality`.`product_quality_id` DESC";
        } else if (sort == 2) {
            query += "`fruit`.`fruit_name` ASC";
        } else if (sort == 3) {
            query += "`fruit`.`fruit_name` DESC";
        }

        try {
            ResultSet rs = MySQL.executeSearch(query);

            DefaultTableModel dtm = (DefaultTableModel) jTable2.getModel();
            dtm.setRowCount(0);

            while (rs.next()) {
                Vector vector = new Vector();
                vector.add(rs.getString("product_quality_id"));
                vector.add(rs.getString("fruit_name"));
                vector.add(rs.getString("inventory_inventory_id"));
                vector.add(rs.getString("date"));
                vector.add(rs.getString("grade_type"));
                vector.add(rs.getString("first_name") + " " + rs.getString("last_name"));
                vector.add(rs.getString("count"));

                dtm.addRow(vector);
            }

        } catch (Exception e) {
            //     e.printStackTrace();
            SignIn.getLoggerObject().log(Level.WARNING, "load product quality exception!", e);
        }

    }

//    private void loadQuality(){
//            DefaultTableModel dtm = (DefaultTableModel) jTable2.getModel();
//            dtm.setRowCount(0);
//            
//            try{
//              ResultSet rs = MySQL.executeSearch("SELECT * FROM `product_quality` INNER JOIN `inventory` ON `product_quality`.`inventory_inventory_id`=`inventory`.`inventory_id`"
//                      + " INNER JOIN `plantation` ON `plantation`.`plantation_id`=`inventory`.`plantation_id` INNER JOIN `fruit` ON `fruit_id`=`plantation`.`fruit_fruit_id` "
//                      + "INNER JOIN `employee` ON `employee`.`NIC`=`product_quality`.`employee_NIC` INNER JOIN `grade` ON `product_quality`.`grade_grade_id`=`grade`.`grade_id`");
//              while(rs.next()){
//                   Vector vector = new Vector();
//                   vector.add(rs.getString("product_quality_id"));
//                   vector.add(rs.getString("fruit_name"));
//                   vector.add(rs.getString("product_quality.date"));
//                   vector.add(rs.getString("grade_type"));
//                   vector.add(rs.getString("employee.first_name") +" "+rs.getString("employee.last_name"));
//                   vector.add(rs.getString("count"));
//                   
//                   dtm.addRow(vector);
//              }
//            }catch(Exception e){
//                e.printStackTrace();
//            }
//    }    
    private void loadGrade() {
        try {
            ResultSet rs = MySQL.executeSearch("SELECT * FROM `grade`");
            Vector<String> vector = new Vector<>();
            vector.add("SELECT");
            while (rs.next()) {
                vector.add(rs.getString("grade_type"));
                gradeMap.put(rs.getString("grade_type"), rs.getInt("grade_id"));
            }
            DefaultComboBoxModel model = new DefaultComboBoxModel(vector);
            jComboBox1.setModel(model);
        } catch (Exception e) {
            //  e.printStackTrace();
            SignIn.getLoggerObject().log(Level.WARNING, "load grade Exception found!", e);
        }
    }

    public void setProductQuality(HashMap qualityDetails) {

        //    System.out.println(qualityDetails.get("id"));
        //    System.out.println(qualityDetails.get("fName"));
        jTextField1.setText((String) qualityDetails.get("id"));
        productQualityFruitName.setText((String) qualityDetails.get("fName"));

    }

    public void getDashboard(Dashboard db) {
        this.dashboard = db;

        try {
            ResultSet rs = MySQL.executeSearch("SELECT * FROM `employee` WHERE `email`='" + dashboard.employeeType().getText() + "'");
            if (rs.next()) {
                jTextField3.setText(rs.getString("NIC"));
            }
        } catch (Exception e) {
            //   e.printStackTrace();
            SignIn.getLoggerObject().log(Level.WARNING, "dashboard Exception found!", e);
        }
    }

    private void storageQuality(String query) {
        DefaultTableModel dtm = (DefaultTableModel) jTable4.getModel();
        dtm.setRowCount(0);

        try {
            ResultSet rs = MySQL.executeSearch(query);
            while (rs.next()) {
                Vector vector = new Vector();
                vector.add(rs.getString("storage_section_id"));
                vector.add(rs.getString("storage_section"));
                vector.add(rs.getString("humidity"));
                vector.add(rs.getString("temperature"));

                dtm.addRow(vector);
            }
        } catch (Exception e) {
            //  e.printStackTrace();
            SignIn.getLoggerObject().log(Level.WARNING, "storage quality Exception found!", e);
        }
    }

    private void loadTemperature() {
        try {
            ResultSet rs = MySQL.executeSearch("SELECT * FROM `temperature`");
            Vector<String> vector = new Vector<>();
            vector.add("SELECT");
            while (rs.next()) {
                vector.add(rs.getString("temperature"));
                temperatureMap.put(rs.getString("temperature"), rs.getInt("temperature_id"));
            }
            DefaultComboBoxModel model = new DefaultComboBoxModel(vector);
            jComboBox5.setModel(model);
        } catch (Exception e) {
            //   e.printStackTrace();
            SignIn.getLoggerObject().log(Level.WARNING, "load temperature Exception found!", e);
        }
    }

    private void loadHumidity() {
        try {
            ResultSet rs = MySQL.executeSearch("SELECT * FROM `humidity`");
            Vector<String> vector = new Vector<>();
            vector.add("SELECT");
            while (rs.next()) {
                vector.add(rs.getString("humidity"));
                humidityMap.put(rs.getString("humidity"), rs.getInt("humidity_id"));
            }
            DefaultComboBoxModel model = new DefaultComboBoxModel(vector);
            jComboBox6.setModel(model);
        } catch (Exception e) {
            //    e.printStackTrace();
            SignIn.getLoggerObject().log(Level.WARNING, "load humidity Exception found!", e);
        }
    }

    private void sectionAdd() {
        String sname = jTextField9.getText();
        String temp = String.valueOf(jComboBox5.getSelectedItem());
        String humi = String.valueOf(jComboBox6.getSelectedItem());

        if (sname.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please enter a Storage Name,", "Warning", JOptionPane.WARNING_MESSAGE);
        } else if (!sname.matches("^[a-zA-Z]+$")) {
            JOptionPane.showMessageDialog(this, "Can't enter numbers or Special Characters,", "Warning", JOptionPane.WARNING_MESSAGE);
        } else if (temp == "SELECT") {
            JOptionPane.showMessageDialog(this, "Select a Temperature,", "Warning", JOptionPane.WARNING_MESSAGE);
        } else if (humi == "SELECT") {
            JOptionPane.showMessageDialog(this, "Select a Humidity,", "Warning", JOptionPane.WARNING_MESSAGE);
        } else {

            int temId = temperatureMap.get(temp);
            int humiId = humidityMap.get(humi);

            try {

                ResultSet rs = MySQL.executeSearch("SELECT * FROM `storage_section` WHERE `storage_section`='" + sname + "'");

                if (rs.next()) {
                    JOptionPane.showMessageDialog(this, "Section name already added,", "Warning", JOptionPane.WARNING_MESSAGE);
                } else {
                    MySQL.executeIUD("INSERT INTO `storage_section` (`storage_section`,`humidity_id`,`temperature_id`) VALUES ('" + sname + "','" + humiId + "','" + temId + "')");
                    JOptionPane.showMessageDialog(this, "Successfully Added,", "Success", JOptionPane.INFORMATION_MESSAGE);
                    storageQuality(q);
                    reset();
                }

            } catch (Exception e) {
                //    e.printStackTrace();
                SignIn.getLoggerObject().log(Level.WARNING, "section add Exception found!", e);
            }
        }
    }

    private void loadTools() {
        DefaultTableModel dtm = (DefaultTableModel) jTable1.getModel();
        dtm.setRowCount(0);
        try {
            ResultSet rs = MySQL.executeSearch("SELECT * FROM `tool`");
            while (rs.next()) {
                Vector vector = new Vector();
                vector.add(rs.getString("tool_id"));
                vector.add(rs.getString("tool_neme"));
                vector.add(rs.getString("purchase_date"));
                vector.add(rs.getString("price"));
                vector.add(rs.getString("work"));

                dtm.addRow(vector);
            }
        } catch (Exception e) {
            //    e.printStackTrace();
            SignIn.getLoggerObject().log(Level.WARNING, "load tools Exception found!", e);
        }
    }

    public JTable getTable1() {
        return jTable1;
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jFrame1 = new javax.swing.JFrame();
        jButton5 = new javax.swing.JButton();
        jPopupMenu1 = new javax.swing.JPopupMenu();
        jMenuItem4 = new javax.swing.JMenuItem();
        jPanel50 = new javax.swing.JPanel();
        jLabel7 = new javax.swing.JLabel();
        jPopupMenu2 = new javax.swing.JPopupMenu();
        jMenuItem2 = new javax.swing.JMenuItem();
        jMenuItem3 = new javax.swing.JMenuItem();
        jMenu1 = new javax.swing.JMenu();
        productQualityMenue = new javax.swing.JPopupMenu();
        updateProductQualityMenueItem = new javax.swing.JMenuItem();
        jPanel1 = new javax.swing.JPanel();
        jTabbedPane1 = new javax.swing.JTabbedPane();
        jPanel2 = new javax.swing.JPanel();
        jPanel7 = new javax.swing.JPanel();
        jPanel8 = new javax.swing.JPanel();
        jLabel18 = new javax.swing.JLabel();
        jPanel9 = new javax.swing.JPanel();
        jButton17 = new javax.swing.JButton();
        jButton19 = new javax.swing.JButton();
        jButton20 = new javax.swing.JButton();
        jButton18 = new javax.swing.JButton();
        jPanel10 = new javax.swing.JPanel();
        curveLineChart2 = new lk.corevision.fruitshop.charts.CurveLineChart();
        jPanel3 = new javax.swing.JPanel();
        jPanel4 = new javax.swing.JPanel();
        jPanel48 = new javax.swing.JPanel();
        jPanel49 = new javax.swing.JPanel();
        jPanel51 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTable1 = new javax.swing.JTable();
        jPanel52 = new javax.swing.JPanel();
        jPanel53 = new javax.swing.JPanel();
        jLabel9 = new javax.swing.JLabel();
        jTextField5 = new javax.swing.JTextField();
        jPanel54 = new javax.swing.JPanel();
        jLabel14 = new javax.swing.JLabel();
        jPanel55 = new javax.swing.JPanel();
        jButton8 = new javax.swing.JButton();
        jTextField2 = new javax.swing.JTextField();
        jPanel56 = new javax.swing.JPanel();
        jLabel12 = new javax.swing.JLabel();
        jScrollPane3 = new javax.swing.JScrollPane();
        jTextArea1 = new javax.swing.JTextArea();
        jPanel57 = new javax.swing.JPanel();
        jLabel10 = new javax.swing.JLabel();
        jDateChooser2 = new com.toedter.calendar.JDateChooser();
        jPanel58 = new javax.swing.JPanel();
        jFormattedTextField1 = new javax.swing.JFormattedTextField();
        jLabel11 = new javax.swing.JLabel();
        jPanel59 = new javax.swing.JPanel();
        jButton6 = new javax.swing.JButton();
        jPanel60 = new javax.swing.JPanel();
        jButton12 = new javax.swing.JButton();
        jPanel61 = new javax.swing.JPanel();
        jButton13 = new javax.swing.JButton();
        jPanel5 = new javax.swing.JPanel();
        jPanel39 = new javax.swing.JPanel();
        jLabel8 = new javax.swing.JLabel();
        jButton7 = new javax.swing.JButton();
        jTextField6 = new javax.swing.JTextField();
        jPanel38 = new javax.swing.JPanel();
        jPanel41 = new javax.swing.JPanel();
        jPanel42 = new javax.swing.JPanel();
        jLabel13 = new javax.swing.JLabel();
        jTextField9 = new javax.swing.JTextField();
        jPanel43 = new javax.swing.JPanel();
        jLabel22 = new javax.swing.JLabel();
        jComboBox5 = new javax.swing.JComboBox<>();
        jPanel44 = new javax.swing.JPanel();
        jLabel23 = new javax.swing.JLabel();
        jComboBox6 = new javax.swing.JComboBox<>();
        jPanel45 = new javax.swing.JPanel();
        jButton10 = new javax.swing.JButton();
        jPanel46 = new javax.swing.JPanel();
        jButton11 = new javax.swing.JButton();
        jPanel47 = new javax.swing.JPanel();
        jButton15 = new javax.swing.JButton();
        jPanel40 = new javax.swing.JPanel();
        jScrollPane5 = new javax.swing.JScrollPane();
        jTable4 = new javax.swing.JTable();
        jPanel6 = new javax.swing.JPanel();
        jPanel21 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jLabel16 = new javax.swing.JLabel();
        jDateChooser1 = new com.toedter.calendar.JDateChooser();
        jLabel17 = new javax.swing.JLabel();
        jDateChooser3 = new com.toedter.calendar.JDateChooser();
        jComboBox2 = new javax.swing.JComboBox<>();
        jButton9 = new javax.swing.JButton();
        jButton14 = new javax.swing.JButton();
        productQualityFruitNameSearch = new javax.swing.JTextField();
        jLabel15 = new javax.swing.JLabel();
        jPanel19 = new javax.swing.JPanel();
        jPanel22 = new javax.swing.JPanel();
        jScrollPane2 = new javax.swing.JScrollPane();
        jTable2 = new javax.swing.JTable();
        jPanel25 = new javax.swing.JPanel();
        jPanel26 = new javax.swing.JPanel();
        jButton2 = new javax.swing.JButton();
        jPanel27 = new javax.swing.JPanel();
        jLabel2 = new javax.swing.JLabel();
        jTextField1 = new javax.swing.JTextField();
        jPanel28 = new javax.swing.JPanel();
        jLabel3 = new javax.swing.JLabel();
        productQualityFruitName = new javax.swing.JTextField();
        jPanel29 = new javax.swing.JPanel();
        jLabel4 = new javax.swing.JLabel();
        jComboBox1 = new javax.swing.JComboBox<>();
        jPanel30 = new javax.swing.JPanel();
        jLabel5 = new javax.swing.JLabel();
        jTextField3 = new javax.swing.JTextField();
        jPanel31 = new javax.swing.JPanel();
        jLabel6 = new javax.swing.JLabel();
        jTextField4 = new javax.swing.JTextField();
        jPanel32 = new javax.swing.JPanel();
        jPanel33 = new javax.swing.JPanel();
        jPanel34 = new javax.swing.JPanel();
        jButton1 = new javax.swing.JButton();
        jPanel35 = new javax.swing.JPanel();
        jButton3 = new javax.swing.JButton();
        jPanel36 = new javax.swing.JPanel();
        jButton16 = new javax.swing.JButton();
        jPanel37 = new javax.swing.JPanel();

        javax.swing.GroupLayout jFrame1Layout = new javax.swing.GroupLayout(jFrame1.getContentPane());
        jFrame1.getContentPane().setLayout(jFrame1Layout);
        jFrame1Layout.setHorizontalGroup(
            jFrame1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 400, Short.MAX_VALUE)
        );
        jFrame1Layout.setVerticalGroup(
            jFrame1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 300, Short.MAX_VALUE)
        );

        jButton5.setText("jButton5");

        jMenuItem4.setText("Update");
        jMenuItem4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem4ActionPerformed(evt);
            }
        });
        jPopupMenu1.add(jMenuItem4);

        jLabel7.setFont(new java.awt.Font("Serif", 1, 18)); // NOI18N
        jLabel7.setText("Tools");

        javax.swing.GroupLayout jPanel50Layout = new javax.swing.GroupLayout(jPanel50);
        jPanel50.setLayout(jPanel50Layout);
        jPanel50Layout.setHorizontalGroup(
            jPanel50Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel50Layout.createSequentialGroup()
                .addComponent(jLabel7)
                .addGap(0, 786, Short.MAX_VALUE))
        );
        jPanel50Layout.setVerticalGroup(
            jPanel50Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel50Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel7)
                .addContainerGap(30, Short.MAX_VALUE))
        );

        jMenuItem2.setForeground(new java.awt.Color(255, 102, 0));
        jMenuItem2.setText("Tool Quality");
        jMenuItem2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem2ActionPerformed(evt);
            }
        });
        jPopupMenu2.add(jMenuItem2);

        jMenuItem3.setText("Update");
        jMenuItem3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem3ActionPerformed(evt);
            }
        });
        jPopupMenu2.add(jMenuItem3);

        jMenu1.setText("jMenu1");

        updateProductQualityMenueItem.setText("Update");
        updateProductQualityMenueItem.setToolTipText("");
        updateProductQualityMenueItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                updateProductQualityMenueItemActionPerformed(evt);
            }
        });
        productQualityMenue.add(updateProductQualityMenueItem);

        setPreferredSize(new java.awt.Dimension(816, 565));
        setLayout(new java.awt.BorderLayout());

        jPanel1.setPreferredSize(new java.awt.Dimension(816, 565));
        jPanel1.setLayout(new java.awt.BorderLayout());

        jTabbedPane1.setPreferredSize(new java.awt.Dimension(816, 565));

        jPanel2.setLayout(new java.awt.BorderLayout());

        jPanel7.setPreferredSize(new java.awt.Dimension(828, 134));
        jPanel7.setLayout(new java.awt.BorderLayout());

        jLabel18.setFont(new java.awt.Font("Serif", 1, 18)); // NOI18N
        jLabel18.setText("Quality Dashboard");

        javax.swing.GroupLayout jPanel8Layout = new javax.swing.GroupLayout(jPanel8);
        jPanel8.setLayout(jPanel8Layout);
        jPanel8Layout.setHorizontalGroup(
            jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel8Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel18)
                .addContainerGap(758, Short.MAX_VALUE))
        );
        jPanel8Layout.setVerticalGroup(
            jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel8Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel18)
                .addContainerGap(25, Short.MAX_VALUE))
        );

        jPanel7.add(jPanel8, java.awt.BorderLayout.PAGE_START);

        jPanel9.setLayout(new java.awt.GridLayout(1, 4));

        jButton17.setFont(new java.awt.Font("Serif", 1, 18)); // NOI18N
        jButton17.setText("Packing Quality");
        jButton17.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                jButton17MouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                jButton17MouseExited(evt);
            }
        });
        jButton17.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton17ActionPerformed(evt);
            }
        });
        jPanel9.add(jButton17);

        jButton19.setFont(new java.awt.Font("Serif", 1, 18)); // NOI18N
        jButton19.setText("Tools");
        jButton19.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                jButton19MouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                jButton19MouseExited(evt);
            }
        });
        jButton19.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton19ActionPerformed(evt);
            }
        });
        jPanel9.add(jButton19);

        jButton20.setFont(new java.awt.Font("Serif", 1, 18)); // NOI18N
        jButton20.setText("Storage");
        jButton20.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                jButton20MouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                jButton20MouseExited(evt);
            }
        });
        jButton20.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton20ActionPerformed(evt);
            }
        });
        jPanel9.add(jButton20);

        jButton18.setFont(new java.awt.Font("Serif", 1, 18)); // NOI18N
        jButton18.setText("Product Quality");
        jButton18.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                jButton18MouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                jButton18MouseExited(evt);
            }
        });
        jButton18.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton18ActionPerformed(evt);
            }
        });
        jPanel9.add(jButton18);

        jPanel7.add(jPanel9, java.awt.BorderLayout.CENTER);

        jPanel2.add(jPanel7, java.awt.BorderLayout.PAGE_START);

        jPanel10.setPreferredSize(new java.awt.Dimension(816, 400));

        javax.swing.GroupLayout jPanel10Layout = new javax.swing.GroupLayout(jPanel10);
        jPanel10.setLayout(jPanel10Layout);
        jPanel10Layout.setHorizontalGroup(
            jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel10Layout.createSequentialGroup()
                .addGap(19, 19, 19)
                .addComponent(curveLineChart2, javax.swing.GroupLayout.PREFERRED_SIZE, 781, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(115, Short.MAX_VALUE))
        );
        jPanel10Layout.setVerticalGroup(
            jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel10Layout.createSequentialGroup()
                .addGap(38, 38, 38)
                .addComponent(curveLineChart2, javax.swing.GroupLayout.PREFERRED_SIZE, 333, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(29, Short.MAX_VALUE))
        );

        jPanel2.add(jPanel10, java.awt.BorderLayout.CENTER);

        jTabbedPane1.addTab("DashBoard", jPanel2);

        jPanel3.setPreferredSize(new java.awt.Dimension(816, 534));
        jPanel3.setLayout(new java.awt.BorderLayout());
        jTabbedPane1.addTab("Packing Quality", jPanel3);

        jPanel4.setLayout(new java.awt.BorderLayout());

        jPanel48.setLayout(new java.awt.BorderLayout());

        jPanel49.setLayout(new java.awt.BorderLayout());

        jPanel51.setPreferredSize(new java.awt.Dimension(816, 282));
        jPanel51.setLayout(new java.awt.BorderLayout());

        jTable1.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null}
            },
            new String [] {
                "Tool Id", "Tool Name", "Purchased Date", "Price", "Work"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jTable1.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jTable1MouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(jTable1);

        jPanel51.add(jScrollPane1, java.awt.BorderLayout.CENTER);

        jPanel49.add(jPanel51, java.awt.BorderLayout.CENTER);

        jPanel52.setPreferredSize(new java.awt.Dimension(816, 200));
        jPanel52.setLayout(new java.awt.GridLayout(3, 3));

        jLabel9.setFont(new java.awt.Font("Serif", 0, 14)); // NOI18N
        jLabel9.setText("Tool Name");

        jTextField5.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                jTextField5KeyPressed(evt);
            }
        });

        javax.swing.GroupLayout jPanel53Layout = new javax.swing.GroupLayout(jPanel53);
        jPanel53.setLayout(jPanel53Layout);
        jPanel53Layout.setHorizontalGroup(
            jPanel53Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel53Layout.createSequentialGroup()
                .addGap(24, 24, 24)
                .addGroup(jPanel53Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jTextField5, javax.swing.GroupLayout.DEFAULT_SIZE, 275, Short.MAX_VALUE)
                    .addGroup(jPanel53Layout.createSequentialGroup()
                        .addComponent(jLabel9, javax.swing.GroupLayout.PREFERRED_SIZE, 69, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
        );
        jPanel53Layout.setVerticalGroup(
            jPanel53Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel53Layout.createSequentialGroup()
                .addContainerGap(13, Short.MAX_VALUE)
                .addComponent(jLabel9)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jTextField5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        jPanel52.add(jPanel53);

        jLabel14.setFont(new java.awt.Font("Serif", 1, 18)); // NOI18N
        jLabel14.setText("Tools");

        javax.swing.GroupLayout jPanel54Layout = new javax.swing.GroupLayout(jPanel54);
        jPanel54.setLayout(jPanel54Layout);
        jPanel54Layout.setHorizontalGroup(
            jPanel54Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel54Layout.createSequentialGroup()
                .addContainerGap(129, Short.MAX_VALUE)
                .addComponent(jLabel14)
                .addContainerGap(134, Short.MAX_VALUE))
        );
        jPanel54Layout.setVerticalGroup(
            jPanel54Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel54Layout.createSequentialGroup()
                .addComponent(jLabel14)
                .addGap(0, 42, Short.MAX_VALUE))
        );

        jPanel52.add(jPanel54);

        jButton8.setText("Reset");
        jButton8.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton8ActionPerformed(evt);
            }
        });

        jTextField2.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                jTextField2KeyReleased(evt);
            }
        });

        javax.swing.GroupLayout jPanel55Layout = new javax.swing.GroupLayout(jPanel55);
        jPanel55.setLayout(jPanel55Layout);
        jPanel55Layout.setHorizontalGroup(
            jPanel55Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel55Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jTextField2, javax.swing.GroupLayout.PREFERRED_SIZE, 182, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jButton8, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(31, 31, 31))
        );
        jPanel55Layout.setVerticalGroup(
            jPanel55Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel55Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel55Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jTextField2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton8))
                .addContainerGap(37, Short.MAX_VALUE))
        );

        jPanel52.add(jPanel55);

        jLabel12.setFont(new java.awt.Font("Serif", 0, 14)); // NOI18N
        jLabel12.setText("Work");

        jTextArea1.setColumns(20);
        jTextArea1.setRows(5);
        jScrollPane3.setViewportView(jTextArea1);

        javax.swing.GroupLayout jPanel56Layout = new javax.swing.GroupLayout(jPanel56);
        jPanel56.setLayout(jPanel56Layout);
        jPanel56Layout.setHorizontalGroup(
            jPanel56Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel56Layout.createSequentialGroup()
                .addGap(22, 22, 22)
                .addGroup(jPanel56Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel12)
                    .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 254, javax.swing.GroupLayout.PREFERRED_SIZE)))
        );
        jPanel56Layout.setVerticalGroup(
            jPanel56Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel56Layout.createSequentialGroup()
                .addComponent(jLabel12)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane3, javax.swing.GroupLayout.DEFAULT_SIZE, 41, Short.MAX_VALUE))
        );

        jPanel52.add(jPanel56);

        jLabel10.setFont(new java.awt.Font("Serif", 0, 14)); // NOI18N
        jLabel10.setText("Purchased Date");

        javax.swing.GroupLayout jPanel57Layout = new javax.swing.GroupLayout(jPanel57);
        jPanel57.setLayout(jPanel57Layout);
        jPanel57Layout.setHorizontalGroup(
            jPanel57Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel57Layout.createSequentialGroup()
                .addGap(24, 24, 24)
                .addComponent(jLabel10)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel57Layout.createSequentialGroup()
                .addGap(25, 25, 25)
                .addComponent(jDateChooser2, javax.swing.GroupLayout.DEFAULT_SIZE, 259, Short.MAX_VALUE)
                .addGap(21, 21, 21))
        );
        jPanel57Layout.setVerticalGroup(
            jPanel57Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel57Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel10)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jDateChooser2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(13, Short.MAX_VALUE))
        );

        jPanel52.add(jPanel57);

        jFormattedTextField1.setText("0");
        jFormattedTextField1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jFormattedTextField1ActionPerformed(evt);
            }
        });

        jLabel11.setFont(new java.awt.Font("Serif", 0, 14)); // NOI18N
        jLabel11.setText("Price");

        javax.swing.GroupLayout jPanel58Layout = new javax.swing.GroupLayout(jPanel58);
        jPanel58.setLayout(jPanel58Layout);
        jPanel58Layout.setHorizontalGroup(
            jPanel58Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel58Layout.createSequentialGroup()
                .addGap(16, 16, 16)
                .addGroup(jPanel58Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel58Layout.createSequentialGroup()
                        .addComponent(jLabel11)
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addComponent(jFormattedTextField1, javax.swing.GroupLayout.DEFAULT_SIZE, 283, Short.MAX_VALUE))
                .addContainerGap())
        );
        jPanel58Layout.setVerticalGroup(
            jPanel58Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel58Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel11)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jFormattedTextField1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(14, 14, 14))
        );

        jPanel52.add(jPanel58);

        jPanel59.setBorder(javax.swing.BorderFactory.createEmptyBorder(20, 20, 20, 20));
        jPanel59.setLayout(new java.awt.BorderLayout());

        jButton6.setFont(new java.awt.Font("Serif", 0, 14)); // NOI18N
        jButton6.setText("Add Tool");
        jButton6.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton6ActionPerformed(evt);
            }
        });
        jPanel59.add(jButton6, java.awt.BorderLayout.CENTER);

        jPanel52.add(jPanel59);

        jPanel60.setBorder(javax.swing.BorderFactory.createEmptyBorder(20, 20, 20, 20));
        jPanel60.setLayout(new java.awt.BorderLayout());

        jButton12.setFont(new java.awt.Font("Serif", 0, 14)); // NOI18N
        jButton12.setText("Update Tool");
        jButton12.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton12ActionPerformed(evt);
            }
        });
        jPanel60.add(jButton12, java.awt.BorderLayout.CENTER);

        jPanel52.add(jPanel60);

        jPanel61.setBorder(javax.swing.BorderFactory.createEmptyBorder(20, 20, 20, 20));
        jPanel61.setLayout(new java.awt.BorderLayout());

        jButton13.setFont(new java.awt.Font("Serif", 0, 14)); // NOI18N
        jButton13.setText("Reports");
        jButton13.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton13ActionPerformed(evt);
            }
        });
        jPanel61.add(jButton13, java.awt.BorderLayout.CENTER);

        jPanel52.add(jPanel61);

        jPanel49.add(jPanel52, java.awt.BorderLayout.PAGE_START);

        jPanel48.add(jPanel49, java.awt.BorderLayout.CENTER);

        jPanel4.add(jPanel48, java.awt.BorderLayout.CENTER);

        jTabbedPane1.addTab("Tools", jPanel4);

        jPanel5.setLayout(new java.awt.BorderLayout());

        jPanel39.setPreferredSize(new java.awt.Dimension(816, 56));

        jLabel8.setFont(new java.awt.Font("Serif", 1, 18)); // NOI18N
        jLabel8.setText("Storage Quality");

        jButton7.setFont(new java.awt.Font("Serif", 0, 14)); // NOI18N
        jButton7.setText("Reset");
        jButton7.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton7ActionPerformed(evt);
            }
        });

        jTextField6.setPreferredSize(new java.awt.Dimension(64, 30));
        jTextField6.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextField6ActionPerformed(evt);
            }
        });
        jTextField6.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                jTextField6KeyReleased(evt);
            }
        });

        javax.swing.GroupLayout jPanel39Layout = new javax.swing.GroupLayout(jPanel39);
        jPanel39.setLayout(jPanel39Layout);
        jPanel39Layout.setHorizontalGroup(
            jPanel39Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel39Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel8)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 473, Short.MAX_VALUE)
                .addComponent(jTextField6, javax.swing.GroupLayout.PREFERRED_SIZE, 207, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jButton7)
                .addGap(20, 20, 20))
        );
        jPanel39Layout.setVerticalGroup(
            jPanel39Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel39Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel39Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel8)
                    .addComponent(jButton7, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jTextField6, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(20, Short.MAX_VALUE))
        );

        jPanel5.add(jPanel39, java.awt.BorderLayout.PAGE_START);

        jPanel38.setLayout(new java.awt.BorderLayout());

        jPanel41.setPreferredSize(new java.awt.Dimension(816, 150));
        jPanel41.setLayout(new java.awt.GridLayout(2, 3));

        jLabel13.setFont(new java.awt.Font("Serif", 0, 14)); // NOI18N
        jLabel13.setText("Storage Section Name");

        jTextField9.setPreferredSize(new java.awt.Dimension(64, 30));
        jTextField9.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                jTextField9KeyReleased(evt);
            }
        });

        javax.swing.GroupLayout jPanel42Layout = new javax.swing.GroupLayout(jPanel42);
        jPanel42.setLayout(jPanel42Layout);
        jPanel42Layout.setHorizontalGroup(
            jPanel42Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel42Layout.createSequentialGroup()
                .addGap(23, 23, 23)
                .addGroup(jPanel42Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel42Layout.createSequentialGroup()
                        .addComponent(jLabel13)
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(jPanel42Layout.createSequentialGroup()
                        .addComponent(jTextField9, javax.swing.GroupLayout.DEFAULT_SIZE, 231, Short.MAX_VALUE)
                        .addContainerGap(51, Short.MAX_VALUE))))
        );
        jPanel42Layout.setVerticalGroup(
            jPanel42Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel42Layout.createSequentialGroup()
                .addContainerGap(9, Short.MAX_VALUE)
                .addComponent(jLabel13)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jTextField9, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        jPanel41.add(jPanel42);

        jLabel22.setFont(new java.awt.Font("Serif", 0, 14)); // NOI18N
        jLabel22.setText("Temperature");

        jComboBox5.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));

        javax.swing.GroupLayout jPanel43Layout = new javax.swing.GroupLayout(jPanel43);
        jPanel43.setLayout(jPanel43Layout);
        jPanel43Layout.setHorizontalGroup(
            jPanel43Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel43Layout.createSequentialGroup()
                .addGap(22, 22, 22)
                .addGroup(jPanel43Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel22, javax.swing.GroupLayout.PREFERRED_SIZE, 79, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jComboBox5, 0, 247, Short.MAX_VALUE))
                .addContainerGap(36, Short.MAX_VALUE))
        );
        jPanel43Layout.setVerticalGroup(
            jPanel43Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel43Layout.createSequentialGroup()
                .addContainerGap(16, Short.MAX_VALUE)
                .addComponent(jLabel22)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jComboBox5, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        jPanel41.add(jPanel43);

        jLabel23.setFont(new java.awt.Font("Serif", 0, 14)); // NOI18N
        jLabel23.setText("Humidity");

        jComboBox6.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));

        javax.swing.GroupLayout jPanel44Layout = new javax.swing.GroupLayout(jPanel44);
        jPanel44.setLayout(jPanel44Layout);
        jPanel44Layout.setHorizontalGroup(
            jPanel44Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel44Layout.createSequentialGroup()
                .addGap(20, 20, 20)
                .addGroup(jPanel44Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel23, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jComboBox6, 0, 250, Short.MAX_VALUE))
                .addContainerGap(35, Short.MAX_VALUE))
        );
        jPanel44Layout.setVerticalGroup(
            jPanel44Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel44Layout.createSequentialGroup()
                .addContainerGap(16, Short.MAX_VALUE)
                .addComponent(jLabel23)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jComboBox6, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        jPanel41.add(jPanel44);

        jPanel45.setBorder(javax.swing.BorderFactory.createEmptyBorder(25, 20, 25, 30));
        jPanel45.setLayout(new java.awt.BorderLayout());

        jButton10.setFont(new java.awt.Font("Serif", 0, 14)); // NOI18N
        jButton10.setText("Add Section");
        jButton10.setPreferredSize(new java.awt.Dimension(99, 30));
        jButton10.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton10ActionPerformed(evt);
            }
        });
        jPanel45.add(jButton10, java.awt.BorderLayout.PAGE_END);

        jPanel41.add(jPanel45);

        jPanel46.setBorder(javax.swing.BorderFactory.createEmptyBorder(25, 20, 25, 20));
        jPanel46.setLayout(new java.awt.BorderLayout());

        jButton11.setFont(new java.awt.Font("Serif", 0, 14)); // NOI18N
        jButton11.setText("Update Section");
        jButton11.setPreferredSize(new java.awt.Dimension(115, 30));
        jButton11.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton11ActionPerformed(evt);
            }
        });
        jPanel46.add(jButton11, java.awt.BorderLayout.CENTER);

        jPanel41.add(jPanel46);

        jPanel47.setBorder(javax.swing.BorderFactory.createEmptyBorder(25, 20, 25, 20));
        jPanel47.setLayout(new java.awt.BorderLayout());

        jButton15.setFont(new java.awt.Font("Serif", 0, 14)); // NOI18N
        jButton15.setText("Report");
        jButton15.setPreferredSize(new java.awt.Dimension(72, 30));
        jButton15.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton15ActionPerformed(evt);
            }
        });
        jPanel47.add(jButton15, java.awt.BorderLayout.CENTER);

        jPanel41.add(jPanel47);

        jPanel38.add(jPanel41, java.awt.BorderLayout.PAGE_START);

        jPanel40.setPreferredSize(new java.awt.Dimension(816, 336));
        jPanel40.setLayout(new java.awt.BorderLayout());

        jTable4.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Id", "Section Name", "Humidity", "Temperature"
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
        });
        jScrollPane5.setViewportView(jTable4);

        jPanel40.add(jScrollPane5, java.awt.BorderLayout.CENTER);

        jPanel38.add(jPanel40, java.awt.BorderLayout.CENTER);

        jPanel5.add(jPanel38, java.awt.BorderLayout.CENTER);

        jTabbedPane1.addTab("Storage", jPanel5);

        jPanel6.setPreferredSize(new java.awt.Dimension(816, 534));
        jPanel6.setLayout(new java.awt.BorderLayout());

        jLabel1.setFont(new java.awt.Font("Serif", 1, 18)); // NOI18N
        jLabel1.setText("Product Quality Check");

        jLabel16.setText("From");

        jLabel17.setText("To");

        jComboBox2.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Quantity Id ASC", "Quantity Id DESC", "Fruit Name ASC", "Fruit Name DESC" }));

        jButton9.setText("Search");
        jButton9.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton9ActionPerformed(evt);
            }
        });

        jButton14.setText("Reset");
        jButton14.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton14ActionPerformed(evt);
            }
        });

        jLabel15.setText("Fruit Name");

        javax.swing.GroupLayout jPanel21Layout = new javax.swing.GroupLayout(jPanel21);
        jPanel21.setLayout(jPanel21Layout);
        jPanel21Layout.setHorizontalGroup(
            jPanel21Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel21Layout.createSequentialGroup()
                .addGap(21, 21, 21)
                .addComponent(jLabel1)
                .addGap(79, 79, 79)
                .addComponent(jLabel15)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel21Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jComboBox2, 0, 160, Short.MAX_VALUE)
                    .addComponent(productQualityFruitNameSearch))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel16)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel21Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jButton9, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jDateChooser1, javax.swing.GroupLayout.DEFAULT_SIZE, 161, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel17)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel21Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jButton14, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jDateChooser3, javax.swing.GroupLayout.DEFAULT_SIZE, 163, Short.MAX_VALUE))
                .addGap(12, 12, 12))
        );
        jPanel21Layout.setVerticalGroup(
            jPanel21Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel21Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel21Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(jPanel21Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabel1)
                        .addComponent(jLabel16)
                        .addComponent(productQualityFruitNameSearch, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jLabel15))
                    .addGroup(jPanel21Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(jLabel17)
                        .addComponent(jDateChooser1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jDateChooser3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel21Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jButton9)
                    .addGroup(jPanel21Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jComboBox2, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jButton14)))
                .addContainerGap(24, Short.MAX_VALUE))
        );

        jPanel6.add(jPanel21, java.awt.BorderLayout.PAGE_START);

        jPanel19.setPreferredSize(new java.awt.Dimension(816, 534));
        jPanel19.setLayout(new java.awt.BorderLayout());

        jPanel22.setPreferredSize(new java.awt.Dimension(816, 300));
        jPanel22.setLayout(new java.awt.BorderLayout());

        jTable2.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null}
            },
            new String [] {
                "Quality Id", "fruit Name", "Inventory Id", "Date", "Grade", "Employee Name", "Count"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jTable2.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jTable2MouseClicked(evt);
            }
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                jTable2MouseReleased(evt);
            }
        });
        jScrollPane2.setViewportView(jTable2);

        jPanel22.add(jScrollPane2, java.awt.BorderLayout.CENTER);

        jPanel19.add(jPanel22, java.awt.BorderLayout.CENTER);

        jPanel25.setPreferredSize(new java.awt.Dimension(816, 234));
        jPanel25.setLayout(new java.awt.GridLayout(3, 4));

        jPanel26.setBorder(javax.swing.BorderFactory.createEmptyBorder(25, 20, 25, 20));
        jPanel26.setLayout(new java.awt.BorderLayout());

        jButton2.setFont(new java.awt.Font("Serif", 0, 14)); // NOI18N
        jButton2.setText("Inventory");
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });
        jPanel26.add(jButton2, java.awt.BorderLayout.PAGE_END);

        jPanel25.add(jPanel26);

        jLabel2.setFont(new java.awt.Font("Serif", 0, 14)); // NOI18N
        jLabel2.setText("Inventory Id");

        jTextField1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextField1ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel27Layout = new javax.swing.GroupLayout(jPanel27);
        jPanel27.setLayout(jPanel27Layout);
        jPanel27Layout.setHorizontalGroup(
            jPanel27Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel27Layout.createSequentialGroup()
                .addGap(21, 21, 21)
                .addGroup(jPanel27Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel27Layout.createSequentialGroup()
                        .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addComponent(jTextField1, javax.swing.GroupLayout.DEFAULT_SIZE, 201, Short.MAX_VALUE))
                .addContainerGap())
        );
        jPanel27Layout.setVerticalGroup(
            jPanel27Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel27Layout.createSequentialGroup()
                .addGap(17, 17, 17)
                .addComponent(jLabel2)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jTextField1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(14, Short.MAX_VALUE))
        );

        jPanel25.add(jPanel27);

        jLabel3.setFont(new java.awt.Font("Serif", 0, 14)); // NOI18N
        jLabel3.setText("Fruit Name");

        javax.swing.GroupLayout jPanel28Layout = new javax.swing.GroupLayout(jPanel28);
        jPanel28.setLayout(jPanel28Layout);
        jPanel28Layout.setHorizontalGroup(
            jPanel28Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel28Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel28Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 68, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(productQualityFruitName, javax.swing.GroupLayout.DEFAULT_SIZE, 190, Short.MAX_VALUE))
                .addContainerGap(32, Short.MAX_VALUE))
        );
        jPanel28Layout.setVerticalGroup(
            jPanel28Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel28Layout.createSequentialGroup()
                .addGap(17, 17, 17)
                .addComponent(jLabel3)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(productQualityFruitName, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(14, Short.MAX_VALUE))
        );

        jPanel25.add(jPanel28);

        jLabel4.setFont(new java.awt.Font("Serif", 0, 14)); // NOI18N
        jLabel4.setText("Grade");

        jComboBox1.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));

        javax.swing.GroupLayout jPanel29Layout = new javax.swing.GroupLayout(jPanel29);
        jPanel29.setLayout(jPanel29Layout);
        jPanel29Layout.setHorizontalGroup(
            jPanel29Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel29Layout.createSequentialGroup()
                .addGap(17, 17, 17)
                .addGroup(jPanel29Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel4)
                    .addComponent(jComboBox1, javax.swing.GroupLayout.PREFERRED_SIZE, 175, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(36, Short.MAX_VALUE))
        );
        jPanel29Layout.setVerticalGroup(
            jPanel29Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel29Layout.createSequentialGroup()
                .addGap(14, 14, 14)
                .addComponent(jLabel4)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jComboBox1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jPanel25.add(jPanel29);

        jLabel5.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabel5.setText("Employee NIC");

        javax.swing.GroupLayout jPanel30Layout = new javax.swing.GroupLayout(jPanel30);
        jPanel30.setLayout(jPanel30Layout);
        jPanel30Layout.setHorizontalGroup(
            jPanel30Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel30Layout.createSequentialGroup()
                .addGap(18, 18, 18)
                .addGroup(jPanel30Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel5, javax.swing.GroupLayout.PREFERRED_SIZE, 99, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jTextField3, javax.swing.GroupLayout.DEFAULT_SIZE, 178, Short.MAX_VALUE))
                .addContainerGap(32, Short.MAX_VALUE))
        );
        jPanel30Layout.setVerticalGroup(
            jPanel30Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel30Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel5)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jTextField3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(24, Short.MAX_VALUE))
        );

        jPanel25.add(jPanel30);

        jLabel6.setFont(new java.awt.Font("Serif", 0, 14)); // NOI18N
        jLabel6.setText("Count");

        javax.swing.GroupLayout jPanel31Layout = new javax.swing.GroupLayout(jPanel31);
        jPanel31.setLayout(jPanel31Layout);
        jPanel31Layout.setHorizontalGroup(
            jPanel31Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel31Layout.createSequentialGroup()
                .addGap(23, 23, 23)
                .addGroup(jPanel31Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel31Layout.createSequentialGroup()
                        .addComponent(jLabel6)
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addComponent(jTextField4, javax.swing.GroupLayout.DEFAULT_SIZE, 199, Short.MAX_VALUE))
                .addContainerGap())
        );
        jPanel31Layout.setVerticalGroup(
            jPanel31Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel31Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel6)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jTextField4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(25, Short.MAX_VALUE))
        );

        jPanel25.add(jPanel31);

        javax.swing.GroupLayout jPanel32Layout = new javax.swing.GroupLayout(jPanel32);
        jPanel32.setLayout(jPanel32Layout);
        jPanel32Layout.setHorizontalGroup(
            jPanel32Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 228, Short.MAX_VALUE)
        );
        jPanel32Layout.setVerticalGroup(
            jPanel32Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 78, Short.MAX_VALUE)
        );

        jPanel25.add(jPanel32);

        javax.swing.GroupLayout jPanel33Layout = new javax.swing.GroupLayout(jPanel33);
        jPanel33.setLayout(jPanel33Layout);
        jPanel33Layout.setHorizontalGroup(
            jPanel33Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 228, Short.MAX_VALUE)
        );
        jPanel33Layout.setVerticalGroup(
            jPanel33Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 78, Short.MAX_VALUE)
        );

        jPanel25.add(jPanel33);

        jPanel34.setBorder(javax.swing.BorderFactory.createEmptyBorder(25, 20, 25, 20));
        jPanel34.setLayout(new java.awt.BorderLayout());

        jButton1.setFont(new java.awt.Font("Serif", 0, 14)); // NOI18N
        jButton1.setText("Add Product Quality");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });
        jPanel34.add(jButton1, java.awt.BorderLayout.CENTER);

        jPanel25.add(jPanel34);

        jPanel35.setBorder(javax.swing.BorderFactory.createEmptyBorder(25, 20, 25, 20));
        jPanel35.setLayout(new java.awt.BorderLayout());

        jButton3.setFont(new java.awt.Font("Serif", 0, 14)); // NOI18N
        jButton3.setText("Update Product Quality");
        jButton3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton3ActionPerformed(evt);
            }
        });
        jPanel35.add(jButton3, java.awt.BorderLayout.CENTER);

        jPanel25.add(jPanel35);

        jPanel36.setBorder(javax.swing.BorderFactory.createEmptyBorder(25, 20, 25, 20));
        jPanel36.setLayout(new java.awt.BorderLayout());

        jButton16.setFont(new java.awt.Font("Serif", 0, 14)); // NOI18N
        jButton16.setText("Report");
        jButton16.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton16ActionPerformed(evt);
            }
        });
        jPanel36.add(jButton16, java.awt.BorderLayout.PAGE_END);

        jPanel25.add(jPanel36);

        jPanel37.setBorder(javax.swing.BorderFactory.createEmptyBorder(25, 20, 25, 20));
        jPanel37.setLayout(new java.awt.BorderLayout());
        jPanel25.add(jPanel37);

        jPanel19.add(jPanel25, java.awt.BorderLayout.PAGE_START);

        jPanel6.add(jPanel19, java.awt.BorderLayout.CENTER);

        jTabbedPane1.addTab("Product Quality", jPanel6);

        jPanel1.add(jTabbedPane1, java.awt.BorderLayout.PAGE_START);

        add(jPanel1, java.awt.BorderLayout.CENTER);
    }// </editor-fold>//GEN-END:initComponents

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        String iid = jTextField1.getText();
        String fname = productQualityFruitName.getText();
        String grade = String.valueOf(jComboBox1.getSelectedItem());
        String nic = jTextField3.getText();
        String count = jTextField4.getText();

        if (iid.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Inventory Id is empty", "Warning", JOptionPane.WARNING_MESSAGE);
        } else if (fname.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Inventory Id is empty", "Warning", JOptionPane.WARNING_MESSAGE);
        } else if (grade == "SELECT") {
            JOptionPane.showMessageDialog(this, "Please select a Grade", "Warning", JOptionPane.WARNING_MESSAGE);
        } else if (count.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please enter a count", "Warning", JOptionPane.WARNING_MESSAGE);
        } else if (!count.matches("^[1-9]\\d*$")) {
            JOptionPane.showMessageDialog(this, "Please enter only Positive number", "Warning", JOptionPane.WARNING_MESSAGE);
        } else {

            String datetime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
            String date = new SimpleDateFormat("yyyy-MM-dd").format(new Date());

            int grade_id = gradeMap.get(grade);

            try {

                ResultSet rs1 = MySQL.executeSearch("SELECT * FROM `product_quality` INNER JOIN `inventory` ON `product_quality`.`inventory_inventory_id`=`inventory`.`inventory_id`"
                        + " INNER JOIN `plantation` ON `inventory`.`plantation_id`=`plantation`.`plantation_id`  INNER JOIN `fruit` ON `plantation`.`fruit_fruit_id`=`fruit`.`fruit_id`"
                        + "WHERE `product_quality`.`inventory_inventory_id`='" + jTextField1.getText() + "' AND DATE(`product_quality`.`date`)='" + date + "' AND `product_quality`.`grade_grade_id`='" + grade_id + "' AND `product_quality`.`employee_NIC`='" + nic + "' AND `fruit`.`fruit_name`='" + fname + "'");
                if (rs1.next()) {
                    String qid = rs1.getString("product_quality_id");
                    int count1 = rs1.getInt("count");
                    int tot_count = count1 + Integer.parseInt(count);
                    MySQL.executeIUD("UPDATE `product_quality` SET `count` = '" + tot_count + "'  WHERE `product_quality_id`='" + qid + "'");
                    JOptionPane.showMessageDialog(this, "Qty Updated", "Success", JOptionPane.INFORMATION_MESSAGE);
                    loadProductQuality();
                } else {

                    MySQL.executeIUD("INSERT INTO `product_quality` (`inventory_inventory_id`,`date`,`grade_grade_id`,`employee_NIC`,`count`) VALUES('" + iid + "','" + datetime + "','" + grade_id + "','" + nic + "','" + count + "')");
                    JOptionPane.showMessageDialog(this, "Success", "Success", JOptionPane.INFORMATION_MESSAGE);
                    loadProductQuality();

                }

                productReset();
            } catch (Exception e) {
                //      e.printStackTrace();
                SignIn.getLoggerObject().log(Level.WARNING, "add product quality Exception found!", e);

            }
        }
    }//GEN-LAST:event_jButton1ActionPerformed

    private void jButton10ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton10ActionPerformed
        // add
        sectionAdd();
    }//GEN-LAST:event_jButton10ActionPerformed

    private void jTextField9KeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTextField9KeyReleased
//        String sname = jTextField9.getText();
//
//        if (sname.matches("ss")) {
//            JOptionPane.showMessageDialog(this, " enter only Letters", "Warning", JOptionPane.WARNING_MESSAGE);
//        } else {
//            try {
//                storageQuality("SELECT * FROM `storage_section` INNER JOIN `temperature` ON `storage_section`.`temperature_id`=`temperature`.`temperature_id`"
//                        + " INNER JOIN `humidity` ON `humidity`.`humidity_id`=`storage_section`.`humidity_id` WHERE `storage_section`LIKE'%" + sname + "%'");
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//        }
    }//GEN-LAST:event_jTextField9KeyReleased

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        StockDialog st = new StockDialog(dashboard, this, true);
        st.setVisible(true);

    }//GEN-LAST:event_jButton2ActionPerformed

    private void jButton11ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton11ActionPerformed
        int row = jTable4.getSelectedRow();
        if (row != -1) {
            String sid = String.valueOf(jTable4.getValueAt(row, 0));

            String sname = jTextField9.getText();
            String temp = String.valueOf(jComboBox5.getSelectedItem());
            String humi = String.valueOf(jComboBox6.getSelectedItem());

            if (sname.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Please enter a Storage Name,", "Warning", JOptionPane.WARNING_MESSAGE);
            } else if (!sname.matches("^[a-zA-Z]+$")) {
                JOptionPane.showMessageDialog(this, "Can't enter numbers or Special Characters,", "Warning", JOptionPane.WARNING_MESSAGE);
            } else if (temp == "SELECT") {
                JOptionPane.showMessageDialog(this, "Select a Temperature,", "Warning", JOptionPane.WARNING_MESSAGE);
            } else if (humi == "SELECT") {
                JOptionPane.showMessageDialog(this, "Select a Humidity,", "Warning", JOptionPane.WARNING_MESSAGE);
            } else {
                int temId = temperatureMap.get(temp);
                int humiId = humidityMap.get(humi);

                try {
                    ResultSet rs = MySQL.executeSearch("SELECT * FROM `storage_section` WHERE `storage_section`='" + sname + "' AND `storage_section_id`!='" + sid + "'");
                    if (rs.next()) {
                        JOptionPane.showMessageDialog(this, "Section already added,", "Warning", JOptionPane.WARNING_MESSAGE);
                    } else {

                        MySQL.executeIUD("UPDATE `storage_section` SET `storage_section`='" + sname + "',`humidity_id`='" + humiId + "',`temperature_id`='" + temId + "' WHERE `storage_section_id`='" + sid + "'");
                        storageQuality("SELECT * FROM `storage_section` INNER JOIN `temperature` ON `storage_section`.`temperature_id`=`temperature`.`temperature_id`"
                                + " INNER JOIN `humidity` ON `humidity`.`humidity_id`=`storage_section`.`humidity_id`");
                        reset();
                    }

                } catch (Exception e) {
                    //     e.printStackTrace();
                    SignIn.getLoggerObject().log(Level.WARNING, "update section Exception found!", e);
                }
            }
        } else {
            JOptionPane.showMessageDialog(this, "Please select a row,", "Warning", JOptionPane.WARNING_MESSAGE);
        }

    }//GEN-LAST:event_jButton11ActionPerformed

    private void jTable4MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jTable4MouseClicked

        if (jTable4.getSelectedRow() != -1) {
            if (evt.getButton() == MouseEvent.BUTTON3) {
                jPopupMenu1.show(evt.getComponent(), getX(), getY());
            }
        }

//        if (evt.getClickCount() == 2) {
//            jButton10.setEnabled(false);
//            jTable4.setEnabled(false);
//            int row = jTable4.getSelectedRow();
//
//            String sname = String.valueOf(jTable4.getValueAt(row, 1));
//            String humidity = String.valueOf(jTable4.getValueAt(row, 2));
//            String temperature = String.valueOf(jTable4.getValueAt(row, 3));
//
//            jTextField9.setText(sname);
//            jComboBox6.setSelectedItem(humidity);
//            jComboBox5.setSelectedItem(temperature);
//        }

    }//GEN-LAST:event_jTable4MouseClicked

    private void jButton7ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton7ActionPerformed
        reset();
    }//GEN-LAST:event_jButton7ActionPerformed

    private void jTextField6ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextField6ActionPerformed
//       String sname = jTextField9.getText();

//      if(sname.matches("ss")){
//              JOptionPane.showMessageDialog(this, " enter only Letters","Warning",JOptionPane.WARNING_MESSAGE);
//      }else{
//          try{
//                 storageQuality("SELECT * FROM `storage_section` INNER JOIN `temperature` ON `storage_section`.`temperature_id`=`temperature`.`temperature_id`"
//                + " INNER JOIN `humidity` ON `humidity`.`humidity_id`=`storage_section`.`humidity_id` WHERE `storage_section`LIKE'%"+sname+"%'");
//          }catch(Exception e){
//              e.printStackTrace();
//          }
//      }
    }//GEN-LAST:event_jTextField6ActionPerformed

    private void jTextField6KeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTextField6KeyReleased
        String sname = jTextField6.getText();

        if (sname.matches("ss")) {
            JOptionPane.showMessageDialog(this, " enter only Letters", "Warning", JOptionPane.WARNING_MESSAGE);
        } else {
            try {
                storageQuality("SELECT * FROM `storage_section` INNER JOIN `temperature` ON `storage_section`.`temperature_id`=`temperature`.`temperature_id`"
                        + " INNER JOIN `humidity` ON `humidity`.`humidity_id`=`storage_section`.`humidity_id` WHERE `storage_section`LIKE'%" + sname + "%'");
            } catch (Exception e) {
                // e.printStackTrace();
                SignIn.getLoggerObject().log(Level.WARNING, "storage search Exception found!", e);
            }
        }
    }//GEN-LAST:event_jTextField6KeyReleased

    private void jButton6ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton6ActionPerformed
        String toolname = jTextField5.getText();
        // String dateFormat = new SimpleDateFormat("yyyy-MM-dd").format(jDateChooser1.getDate());
        Date date = jDateChooser2.getDate();
        String price = jFormattedTextField1.getText();
        String textarea = jTextArea1.getText();

        if (toolname.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please enter tool name", "warning", JOptionPane.WARNING_MESSAGE);
        } else if (date == null) {
            JOptionPane.showMessageDialog(this, "Please enter date", "warning", JOptionPane.WARNING_MESSAGE);
        } else if (price.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please enter price", "warning", JOptionPane.WARNING_MESSAGE);
        } else if (DataValidator.isInvalidPrice(price)) {
            JOptionPane.showMessageDialog(this, "Please enter valid price", "warning", JOptionPane.WARNING_MESSAGE);
        } else if (textarea.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please enter tool work", "warning", JOptionPane.WARNING_MESSAGE);
        } else {
            String dateFormat = null;
            if (date != null) {
                dateFormat = new SimpleDateFormat("yyyy-MM-dd").format(date);
                System.out.println(dateFormat);
            }

            try {
                ResultSet rs = MySQL.executeSearch("SELECT * FROM `tool` WHERE `tool_neme`='" + toolname + "'");
                if (rs.next()) {
                    JOptionPane.showMessageDialog(this, "Tool was already added", "warning", JOptionPane.WARNING_MESSAGE);
                } else {
                    MySQL.executeIUD("INSERT INTO `tool`(`tool_neme`,`purchase_date`,`price`,`work`) VALUES('" + toolname + "','" + dateFormat + "','" + price + "','" + textarea + "')");
                    JOptionPane.showMessageDialog(this, "Success", "Success", JOptionPane.INFORMATION_MESSAGE);
                    loadTools();
                    reset();
                }
            } catch (Exception e) {
                //  e.printStackTrace();
                SignIn.getLoggerObject().log(Level.WARNING, "tool add Exception found!", e);
            }
        }

    }//GEN-LAST:event_jButton6ActionPerformed

    private void jMenuItem2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem2ActionPerformed
        // tool quality jDialog
        ToolQualityCheck tq = new ToolQualityCheck(dashboard, true);
        tq.getTool(this);
        tq.getDashboard(dashboard);
        tq.setVisible(true);
    }//GEN-LAST:event_jMenuItem2ActionPerformed

    private void jTable1MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jTable1MouseClicked
        //tool
        if (jTable1.getSelectedRow() != -1) {
            if (evt.getButton() == MouseEvent.BUTTON3) {
                jPopupMenu2.show(evt.getComponent(), evt.getX(), evt.getY());
            }
        }

//        if (evt.getClickCount() == 2) {
//            jTable1.setEnabled(false);
//            int row = jTable1.getSelectedRow();
//
//            String toolname = String.valueOf(jTable1.getValueAt(row, 1));
//            jTextField5.setText(toolname);
//            String date = String.valueOf(jTable1.getValueAt(row, 2));
//            try {
//                jDateChooser2.setDate(new SimpleDateFormat("yyyy-MM-dd").parse(date));
//            } catch (ParseException ex) {
//                Logger.getLogger(productQuality.class.getName()).log(Level.SEVERE, null, ex);
//            }
//            String price = String.valueOf(jTable1.getValueAt(row, 3));
//            jFormattedTextField1.setText(price);
//            String work = String.valueOf(jTable1.getValueAt(row, 4));
//            jTextArea1.setText(work);
//
//        }
    }//GEN-LAST:event_jTable1MouseClicked

    private void jButton12ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton12ActionPerformed
        int row = jTable1.getSelectedRow();
        if (row != -1) {

// update tool
            String toolname = jTextField5.getText();
            // String dateFormat = new SimpleDateFormat("yyyy-MM-dd").format(jDateChooser1.getDate());
            Date date = jDateChooser2.getDate();
            String price = jFormattedTextField1.getText();
            String textarea = jTextArea1.getText();

            if (toolname.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Please enter tool name", "warning", JOptionPane.WARNING_MESSAGE);
            } else if (date == null) {
                JOptionPane.showMessageDialog(this, "Please enter date", "warning", JOptionPane.WARNING_MESSAGE);
            } else if (price.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Please enter price", "warning", JOptionPane.WARNING_MESSAGE);
            } else if (DataValidator.isInvalidPrice(price)) {
                JOptionPane.showMessageDialog(this, "Please enter valid price", "warning", JOptionPane.WARNING_MESSAGE);
            } else if (textarea.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Please enter tool work", "warning", JOptionPane.WARNING_MESSAGE);
            } else {
                String dateFormat = null;
                if (date != null) {
                    dateFormat = new SimpleDateFormat("yyyy-MM-dd").format(date);
                    //  System.out.println(dateFormat);
                }

                String tool_id = String.valueOf(jTable1.getValueAt(row, 0));
                try {
                    ResultSet rs = MySQL.executeSearch("SELECT * FROM `tool` WHERE `tool_neme`='" + toolname + "' AND `tool_id` != '" + tool_id + "'");
                    if (rs.next()) {
                        JOptionPane.showMessageDialog(this, "Tool was already Added", "warning", JOptionPane.WARNING_MESSAGE);
                    } else {
                        MySQL.executeIUD("UPDATE `tool` SET `tool_neme`='" + toolname + "',`purchase_date`='" + dateFormat + "',`price`='" + price + "',`work`='" + textarea + "' WHERE `tool_id`='" + tool_id + "'");
                        JOptionPane.showMessageDialog(this, "Successfully added", "Success", JOptionPane.INFORMATION_MESSAGE);
                        loadTools();
                        reset();

                    }
                } catch (Exception e) {
                    //  e.printStackTrace();
                    SignIn.getLoggerObject().log(Level.WARNING, "tool update Exception found!", e);
                }

            }
        } else {
            JOptionPane.showMessageDialog(this, "Please select a row", "warning", JOptionPane.WARNING_MESSAGE);
        }

    }//GEN-LAST:event_jButton12ActionPerformed

    private void jFormattedTextField1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jFormattedTextField1ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jFormattedTextField1ActionPerformed

    private void jButton8ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton8ActionPerformed
        reset();
    }//GEN-LAST:event_jButton8ActionPerformed

    private void jTextField1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextField1ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jTextField1ActionPerformed

    private void jButton16ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton16ActionPerformed
        try {

            String nic = SignIn.getEmployeeNIC();

            String date = new SimpleDateFormat("yyyy-MM-dd").format(new Date());

            HashMap<String, Object> parameters = new HashMap();
            parameters.put("Parameter1", date);
            parameters.put("Parameter2", nic);
            parameters.put("Parameter101", SendLogo.getFilePath());

            JRTableModelDataSource dataSource = new JRTableModelDataSource(jTable2.getModel());

            JasperPrint report = JasperFillManager.fillReport("src/lk/corevision/fruitshop/reports/quality/fruitShopAppProductQuality.jasper", parameters, dataSource);

            JasperViewer.viewReport(report, false);
        } catch (Exception e) {
            //  e.printStackTrace();
            SignIn.getLoggerObject().log(Level.WARNING, "product quality reports Exception found!", e);
        }
    }//GEN-LAST:event_jButton16ActionPerformed

    private void jButton9ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton9ActionPerformed
        loadProductQuality();
    }//GEN-LAST:event_jButton9ActionPerformed

    private void jButton3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton3ActionPerformed
        int row = jTable2.getSelectedRow();
        if (row != -1) {
            String iid = jTextField1.getText();
            String fname = productQualityFruitName.getText();
            String grade = String.valueOf(jComboBox1.getSelectedItem());
            String nic = jTextField3.getText();
            String count = jTextField4.getText();

            if (iid.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Inventory Id is empty", "Warning", JOptionPane.WARNING_MESSAGE);
            } else if (fname.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Inventory Id is empty", "Warning", JOptionPane.WARNING_MESSAGE);
            } else if (grade == "SELECT") {
                JOptionPane.showMessageDialog(this, "Please select a Grade", "Warning", JOptionPane.WARNING_MESSAGE);
            } else if (count.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Please enter a count", "Warning", JOptionPane.WARNING_MESSAGE);
            } else if (!count.matches("^[1-9]\\d*$")) {
                JOptionPane.showMessageDialog(this, "Please enter only Positive number", "Warning", JOptionPane.WARNING_MESSAGE);
            } else {

                String datetime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());

                int grade_id = gradeMap.get(grade);

                try {
                    MySQL.executeIUD("UPDATE `product_quality`  SET `inventory_inventory_id`='" + iid + "',`date`='" + datetime + "',`grade_grade_id`='" + grade_id + "',`employee_NIC`='" + nic + "',`count`='" + count + "' "
                            + "WHERE `product_quality_id`='" + String.valueOf(jTable2.getValueAt(row, 0)) + "'");
                    JOptionPane.showMessageDialog(this, "Success", "Success", JOptionPane.INFORMATION_MESSAGE);
                    productReset();
                    loadProductQuality();
                } catch (Exception e) {
                    //   e.printStackTrace();
                    SignIn.getLoggerObject().log(Level.WARNING, "update product quality Exception found!", e);
                }
            }
        } else {
            JOptionPane.showMessageDialog(this, "Please select a row", "Warning", JOptionPane.WARNING_MESSAGE);

        }


    }//GEN-LAST:event_jButton3ActionPerformed

    private void jTable2MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jTable2MouseClicked
        if (evt.getClickCount() == 2) {
            int row = jTable2.getSelectedRow();
            this.productPacking.getjTextField10().setText(String.valueOf(jTable2.getValueAt(row, 0)));
            jTabbedPane1.setSelectedIndex(1);

        }
    }//GEN-LAST:event_jTable2MouseClicked

    private void jButton14ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton14ActionPerformed
        productReset();
    }//GEN-LAST:event_jButton14ActionPerformed

    private void jButton20ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton20ActionPerformed
        jTabbedPane1.setSelectedIndex(2);
    }//GEN-LAST:event_jButton20ActionPerformed

    private void jButton17ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton17ActionPerformed
        jTabbedPane1.setSelectedIndex(0);
    }//GEN-LAST:event_jButton17ActionPerformed

    private void jButton19ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton19ActionPerformed
        jTabbedPane1.setSelectedIndex(1);
    }//GEN-LAST:event_jButton19ActionPerformed

    private void jButton18ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton18ActionPerformed
        jTabbedPane1.setSelectedIndex(3);
    }//GEN-LAST:event_jButton18ActionPerformed

    private void jButton15ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton15ActionPerformed
        try {

            String nic = SignIn.getEmployeeNIC();

            String date = new SimpleDateFormat("yyyy-MM-dd").format(new Date());

            HashMap<String, Object> parameters = new HashMap();
            parameters.put("Parameter1", date);
            parameters.put("Parameter2", nic);
            parameters.put("Parameter101", SendLogo.getFilePath());

            JRTableModelDataSource dataSource = new JRTableModelDataSource(jTable4.getModel());

            JasperPrint report = JasperFillManager.fillReport("src/lk/corevision/fruitshop/reports/quality/fruitShopAppSectionQuality.jasper", parameters, dataSource);

            JasperViewer.viewReport(report, false);
        } catch (Exception e) {
            //  e.printStackTrace();
            SignIn.getLoggerObject().log(Level.WARNING, "section reports Exception found!", e);
        }
    }//GEN-LAST:event_jButton15ActionPerformed

    private void jButton13ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton13ActionPerformed
        try {

            String nic = SignIn.getEmployeeNIC();

            String date = new SimpleDateFormat("yyyy-MM-dd").format(new Date());

            HashMap<String, Object> parameters = new HashMap();
            parameters.put("Parameter1", date);
            parameters.put("Parameter2", nic);
            parameters.put("Parameter101", SendLogo.getFilePath());

            JRTableModelDataSource dataSource = new JRTableModelDataSource(jTable1.getModel());

            JasperPrint report = JasperFillManager.fillReport("src/lk/corevision/fruitshop/reports/quality/fruitShopAppTool.jasper", parameters, dataSource);

            JasperViewer.viewReport(report, false);
        } catch (Exception e) {
            // e.printStackTrace();
            SignIn.getLoggerObject().log(Level.WARNING, "tool reports Exception found!", e);
        }
    }//GEN-LAST:event_jButton13ActionPerformed

    private void jTextField5KeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTextField5KeyPressed

    }//GEN-LAST:event_jTextField5KeyPressed

    private void jTable2MouseReleased(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jTable2MouseReleased
        if (evt.getButton() == evt.BUTTON3 && jTable2.getSelectedRow() != -1 && dashboard != null) { //Right Click
            productQualityMenue.show(evt.getComponent(), evt.getX(), evt.getY());

        }
    }//GEN-LAST:event_jTable2MouseReleased

    private void updateProductQualityMenueItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_updateProductQualityMenueItemActionPerformed
        jButton1.setEnabled(false);
        jButton2.setEnabled(false);
        jTable2.setEnabled(false);
        int row = jTable2.getSelectedRow();
        if (row != -1) {
            jTextField4.setText(String.valueOf(jTable2.getValueAt(row, 6)));
            String grade = String.valueOf(jTable2.getValueAt(row, 4));
            String iid = String.valueOf(jTable2.getValueAt(row, 2));
            String fname = String.valueOf(jTable2.getValueAt(row, 1));
            productQualityFruitName.setText(fname);
            jTextField1.setText(iid);
            jComboBox1.setSelectedItem(grade);
        } else {
            JOptionPane.showMessageDialog(this, "Please select a row", "Warning", JOptionPane.WARNING_MESSAGE);
        }
    }//GEN-LAST:event_updateProductQualityMenueItemActionPerformed

    private void jMenuItem3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem3ActionPerformed
        //  jTable1.setEnabled(false);
        int row = jTable1.getSelectedRow();
        jTable1.setEnabled(false);

        String toolname = String.valueOf(jTable1.getValueAt(row, 1));
        jTextField5.setText(toolname);
        String date = String.valueOf(jTable1.getValueAt(row, 2));
        try {
            jDateChooser2.setDate(new SimpleDateFormat("yyyy-MM-dd").parse(date));
        } catch (ParseException ex) {
            Logger.getLogger(productQuality.class.getName()).log(Level.SEVERE, null, ex);
        }
        String price = String.valueOf(jTable1.getValueAt(row, 3));
        jFormattedTextField1.setText(price);
        String work = String.valueOf(jTable1.getValueAt(row, 4));
        jTextArea1.setText(work);
    }//GEN-LAST:event_jMenuItem3ActionPerformed

    private void jTextField2KeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTextField2KeyReleased
        String search = jTextField2.getText();

        DefaultTableModel dtm = (DefaultTableModel) jTable1.getModel();
        dtm.setRowCount(0);

        try {
            ResultSet rs = MySQL.executeSearch("SELECT * FROM `tool` WHERE `tool_neme`LIKE '%" + search + "%'");
            while (rs.next()) {
                Vector<String> vector = new Vector<>();
                vector.add(rs.getString("tool_id"));
                vector.add(rs.getString("tool_neme"));
                vector.add(rs.getString("purchase_date"));
                vector.add(rs.getString("price"));
                vector.add(rs.getString("work"));

                dtm.addRow(vector);
            }
        } catch (Exception e) {
            SignIn.getLoggerObject().log(Level.WARNING, "tool name Exception found!", e);
        }

    }//GEN-LAST:event_jTextField2KeyReleased

    private void jMenuItem4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem4ActionPerformed

        jButton10.setEnabled(false);
        jTable4.setEnabled(false);
        int row = jTable4.getSelectedRow();

        String sname = String.valueOf(jTable4.getValueAt(row, 1));
        String humidity = String.valueOf(jTable4.getValueAt(row, 2));
        String temperature = String.valueOf(jTable4.getValueAt(row, 3));

        jTextField9.setText(sname);
        jComboBox6.setSelectedItem(humidity);
        jComboBox5.setSelectedItem(temperature);

    }//GEN-LAST:event_jMenuItem4ActionPerformed

    private void jButton17MouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jButton17MouseEntered
        jButton17.setBackground(new Color(255, 153, 0));
    }//GEN-LAST:event_jButton17MouseEntered

    private void jButton17MouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jButton17MouseExited
        jButton17.setBackground(new Color(255, 255, 255));
    }//GEN-LAST:event_jButton17MouseExited

    private void jButton19MouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jButton19MouseEntered
        jButton19.setBackground(new Color(255, 153, 0));
    }//GEN-LAST:event_jButton19MouseEntered

    private void jButton19MouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jButton19MouseExited
        jButton19.setBackground(new Color(255, 255, 255));
    }//GEN-LAST:event_jButton19MouseExited

    private void jButton20MouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jButton20MouseEntered
        jButton20.setBackground(new Color(255, 153, 0));
    }//GEN-LAST:event_jButton20MouseEntered

    private void jButton20MouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jButton20MouseExited
        jButton20.setBackground(new Color(255, 255, 255));
    }//GEN-LAST:event_jButton20MouseExited

    private void jButton18MouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jButton18MouseEntered
        jButton18.setBackground(new Color(255, 153, 0));
    }//GEN-LAST:event_jButton18MouseEntered

    private void jButton18MouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jButton18MouseExited
        jButton18.setBackground(new Color(255, 255, 255));
    }//GEN-LAST:event_jButton18MouseExited


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private lk.corevision.fruitshop.charts.CurveLineChart curveLineChart2;
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
    private javax.swing.JButton jButton3;
    private javax.swing.JButton jButton5;
    private javax.swing.JButton jButton6;
    private javax.swing.JButton jButton7;
    private javax.swing.JButton jButton8;
    private javax.swing.JButton jButton9;
    private javax.swing.JComboBox<String> jComboBox1;
    private javax.swing.JComboBox<String> jComboBox2;
    private javax.swing.JComboBox<String> jComboBox5;
    private javax.swing.JComboBox<String> jComboBox6;
    private com.toedter.calendar.JDateChooser jDateChooser1;
    private com.toedter.calendar.JDateChooser jDateChooser2;
    private com.toedter.calendar.JDateChooser jDateChooser3;
    private javax.swing.JFormattedTextField jFormattedTextField1;
    private javax.swing.JFrame jFrame1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel15;
    private javax.swing.JLabel jLabel16;
    private javax.swing.JLabel jLabel17;
    private javax.swing.JLabel jLabel18;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel22;
    private javax.swing.JLabel jLabel23;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JMenu jMenu1;
    private javax.swing.JMenuItem jMenuItem2;
    private javax.swing.JMenuItem jMenuItem3;
    private javax.swing.JMenuItem jMenuItem4;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel10;
    private javax.swing.JPanel jPanel19;
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
    private javax.swing.JPanel jPanel38;
    private javax.swing.JPanel jPanel39;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel40;
    private javax.swing.JPanel jPanel41;
    private javax.swing.JPanel jPanel42;
    private javax.swing.JPanel jPanel43;
    private javax.swing.JPanel jPanel44;
    private javax.swing.JPanel jPanel45;
    private javax.swing.JPanel jPanel46;
    private javax.swing.JPanel jPanel47;
    private javax.swing.JPanel jPanel48;
    private javax.swing.JPanel jPanel49;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel50;
    private javax.swing.JPanel jPanel51;
    private javax.swing.JPanel jPanel52;
    private javax.swing.JPanel jPanel53;
    private javax.swing.JPanel jPanel54;
    private javax.swing.JPanel jPanel55;
    private javax.swing.JPanel jPanel56;
    private javax.swing.JPanel jPanel57;
    private javax.swing.JPanel jPanel58;
    private javax.swing.JPanel jPanel59;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JPanel jPanel60;
    private javax.swing.JPanel jPanel61;
    private javax.swing.JPanel jPanel7;
    private javax.swing.JPanel jPanel8;
    private javax.swing.JPanel jPanel9;
    private javax.swing.JPopupMenu jPopupMenu1;
    private javax.swing.JPopupMenu jPopupMenu2;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JScrollPane jScrollPane5;
    private javax.swing.JTabbedPane jTabbedPane1;
    private javax.swing.JTable jTable1;
    private javax.swing.JTable jTable2;
    private javax.swing.JTable jTable4;
    private javax.swing.JTextArea jTextArea1;
    private javax.swing.JTextField jTextField1;
    private javax.swing.JTextField jTextField2;
    private javax.swing.JTextField jTextField3;
    private javax.swing.JTextField jTextField4;
    private javax.swing.JTextField jTextField5;
    private javax.swing.JTextField jTextField6;
    private javax.swing.JTextField jTextField9;
    private javax.swing.JTextField productQualityFruitName;
    private javax.swing.JTextField productQualityFruitNameSearch;
    private javax.swing.JPopupMenu productQualityMenue;
    private javax.swing.JMenuItem updateProductQualityMenueItem;
    // End of variables declaration//GEN-END:variables

    private void reset() {
        jButton10.setEnabled(true);
        jTable4.setEnabled(true);
        jTextField9.setText("");
        jTextField6.setText("");
        jComboBox5.setSelectedItem("SELECT");
        jComboBox6.setSelectedItem("SELECT");
        jTable4.clearSelection();

        //tool
        jTable1.setEnabled(true);
        jTextField5.setText("");
        jTextField2.setText("");
        jTextArea1.setText("");
        jDateChooser2.setDate(null);
        jFormattedTextField1.setText("");

        jTable1.clearSelection();

    }

    private void productReset() {
        jTextField1.setText("");
        productQualityFruitName.setText("");
        jComboBox1.setSelectedItem("SELECT");
        jTextField4.setText("");
        jTable2.clearSelection();
        productQualityFruitNameSearch.setText("");
        jDateChooser1.setDate(null);
        jDateChooser3.setDate(null);
        jTable2.setEnabled(true);
        jButton2.setEnabled(true);
        jButton1.setEnabled(true);
        //  jTextField2.setText("");

    }

}
