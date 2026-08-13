package lk.corevision.fruitshop.gui;

import java.awt.Color;
import java.awt.event.MouseEvent;
import java.util.HashMap;
import lk.corevision.fruitshop.model.MySQL;
import java.sql.ResultSet;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Vector;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;
import lk.corevision.fruitshop.validate.JOptionIcon;
import raven.toast.Notifications;
import java.sql.ResultSet;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Map;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;
import lk.corevision.fruitshop.email.SendEmail;
import lk.corevision.fruitshop.email.SendFileEmail;
import lk.corevision.fruitshop.model.SendLogo;
import lk.corevision.fruitshop.validate.DataValidator;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.data.JRTableModelDataSource;
import net.sf.jasperreports.view.JasperViewer;
import raven.chart.ModelChart;

/**
 *
 * @author Administrator
 */
public class Transport extends javax.swing.JPanel {

    Dashboard db;

    HashMap<String, String> vehicleStatMap = new HashMap<>();
    HashMap<String, String> vehicleTypeMap = new HashMap<>();
    HashMap<String, String> deliveryStatMap = new HashMap<>();

    private String customerAddressId = "";

    private String delvieryId;

    public Transport(Dashboard db) {
        initComponents();
        this.db = db;
        loadVehicleStats();
        loadVehicleType();
        loadVehicles();
        loadDeliveryStat();
        loadDeliveryManage();
        jTextField3.setText(SignIn.getEmployeeNIC());
        jTextField3.setEditable(false);
        jButton14.setEnabled(false);
        jTextField2.setEditable(false);
        jTextField5.setEditable(false);
        jButton8.setEnabled(false);
        jButton10.setEnabled(false);
        chartSetup();
        chartData();

    }

    private void chartSetup() {

        curveLineChart1.setTitle("Delivery Count By Date");
        curveLineChart1.setTitleColor(Color.BLACK);
        curveLineChart1.addLegend("JAN", Color.decode("#7b4397"), Color.decode("#dc2430")); // Purple to Red
        curveLineChart1.addLegend("FEB", Color.decode("#e65c00"), Color.decode("#F9D423")); // Orange to Yellow
        curveLineChart1.addLegend("MAR", Color.decode("#43cea2"), Color.decode("#185a9d")); // Aqua to Blue
        curveLineChart1.addLegend("APR", Color.decode("#ff512f"), Color.decode("#dd2476")); // Coral to Pink
        curveLineChart1.addLegend("MAY", Color.decode("#00c6ff"), Color.decode("#0072ff")); // Light Blue to Dark Blue
        curveLineChart1.addLegend("JUN", Color.decode("#f7971e"), Color.decode("#ffd200")); // Gold to Yellow
        curveLineChart1.addLegend("JUL", Color.decode("#00b09b"), Color.decode("#96c93d")); // Teal to Lime
        curveLineChart1.addLegend("AUG", Color.decode("#ff6a00"), Color.decode("#ee0979")); // Orange to Fuchsia
        curveLineChart1.addLegend("SEP", Color.decode("#8360c3"), Color.decode("#2ebf91")); // Violet to Sea Green
        curveLineChart1.addLegend("OCT", Color.decode("#ff4b1f"), Color.decode("#1fddff")); // Red-Orange to Light Blue
        curveLineChart1.addLegend("NOV", Color.decode("#fc00ff"), Color.decode("#00dbde")); // Purple to Cyan
        curveLineChart1.addLegend("DEC", Color.decode("#a770ef"), Color.decode("#cf8bf3")); // Soft Violet Gradient

    }

    private void chartData() {

        curveLineChart1.clear();

        try {

            String query = "SELECT "
                    + "DAY(delivery_date) AS day, "
                    + "MONTH(delivery_date) AS month, "
                    + "COUNT(*) AS count "
                    + "FROM corevisi_fruit_shop_app.delivery "
                    + "GROUP BY DAY(delivery_date), MONTH(delivery_date) "
                    + "ORDER BY day, month";

            ResultSet resultSet = MySQL.executeSearch(query);

            Map<Integer, double[]> dailyData = new HashMap<>();
            for (int day = 1; day <= 31; day++) {
                dailyData.put(day, new double[12]); // 12 months for each day
            }

            while (resultSet.next()) {
                int day = resultSet.getInt("day");
                int month = resultSet.getInt("month");
                int count = resultSet.getInt("count");
                dailyData.get(day)[month - 1] = count; // month-1 for array index (0-11)
            }

            for (int day = 1; day <= 31; day++) {
                String dayLabel = getDayWithOrdinal(day);
                double[] monthlyValues = dailyData.get(day);
                curveLineChart1.addData(new ModelChart(dayLabel, monthlyValues));
            }

        } catch (Exception e) {
            SignIn.getLoggerObject().log(Level.WARNING, "Transport Chart Data Load Exception Found", e);
        }

        curveLineChart1.start();
    }

    private String getDayWithOrdinal(int day) {
        if (day >= 11 && day <= 13) {
            return day + "th";
        }
        switch (day % 10) {
            case 1:
                return day + "st";
            case 2:
                return day + "nd";
            case 3:
                return day + "rd";
            default:
                return day + "th";
        }
    }

    @Override
    public void enable() {
        super.enable(); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/OverriddenMethodBody
    }

    public void getLoadVehicleType() {
        loadVehicleType();
    }

    public void setDeliveryLocation(String customerAddressId) {

        if (!customerAddressId.isEmpty()) {
            this.customerAddressId = customerAddressId;
            jButton13.setBackground(new Color(102, 255, 102));
            jButton14.setEnabled(true);
            jButton5.setEnabled(false);
        }

    }

    public void setEmployeeNic(String employeeNIC) {
        jTextField2.setText(employeeNIC);
    }

    public void setOrderId(String orderId) {
        jTextField5.setText(orderId);
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        buttonGroup1 = new javax.swing.ButtonGroup();
        buttonGroup2 = new javax.swing.ButtonGroup();
        jMonthChooser1 = new com.toedter.calendar.JMonthChooser();
        jPopupMenu1 = new javax.swing.JPopupMenu();
        jMenuItem1 = new javax.swing.JMenuItem();
        jPopupMenu2 = new javax.swing.JPopupMenu();
        jMenuItem2 = new javax.swing.JMenuItem();
        jMenuItem3 = new javax.swing.JMenuItem();
        jTabbedPane2 = new javax.swing.JTabbedPane();
        jPanel11 = new javax.swing.JPanel();
        jPanel4 = new javax.swing.JPanel();
        jPanel22 = new javax.swing.JPanel();
        jPanel23 = new javax.swing.JPanel();
        jButton4 = new javax.swing.JButton();
        jButton9 = new javax.swing.JButton();
        jButton15 = new javax.swing.JButton();
        jPanel7 = new javax.swing.JPanel();
        curveLineChart1 = new lk.corevision.fruitshop.charts.CurveLineChart();
        jPanel10 = new javax.swing.JPanel();
        jPanel5 = new javax.swing.JPanel();
        jLabel8 = new javax.swing.JLabel();
        jButton17 = new javax.swing.JButton();
        jPanel6 = new javax.swing.JPanel();
        jPanel2 = new javax.swing.JPanel();
        jPanel12 = new javax.swing.JPanel();
        jLabel2 = new javax.swing.JLabel();
        jTextField2 = new javax.swing.JTextField();
        jButton2 = new javax.swing.JButton();
        jPanel15 = new javax.swing.JPanel();
        jLabel3 = new javax.swing.JLabel();
        jTextField3 = new javax.swing.JTextField();
        jPanel16 = new javax.swing.JPanel();
        jLabel4 = new javax.swing.JLabel();
        jComboBox5 = new javax.swing.JComboBox<>();
        jPanel17 = new javax.swing.JPanel();
        jLabel5 = new javax.swing.JLabel();
        jTextField5 = new javax.swing.JTextField();
        jButton5 = new javax.swing.JButton();
        jButton13 = new javax.swing.JButton();
        jButton14 = new javax.swing.JButton();
        jPanel18 = new javax.swing.JPanel();
        jLabel6 = new javax.swing.JLabel();
        jDateChooser1 = new com.toedter.calendar.JDateChooser();
        jPanel19 = new javax.swing.JPanel();
        jButton6 = new javax.swing.JButton();
        jButton8 = new javax.swing.JButton();
        jButton3 = new javax.swing.JButton();
        jPanel3 = new javax.swing.JPanel();
        jPanel24 = new javax.swing.JPanel();
        jTextField4 = new javax.swing.JTextField();
        jLabel15 = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTable1 = new javax.swing.JTable();
        jPanel13 = new javax.swing.JPanel();
        jPanel8 = new javax.swing.JPanel();
        jLabel9 = new javax.swing.JLabel();
        jPanel9 = new javax.swing.JPanel();
        jPanel27 = new javax.swing.JPanel();
        jLabel21 = new javax.swing.JLabel();
        jTextField1 = new javax.swing.JTextField();
        jPanel26 = new javax.swing.JPanel();
        jLabel20 = new javax.swing.JLabel();
        jComboBox3 = new javax.swing.JComboBox<>();
        jPanel28 = new javax.swing.JPanel();
        jLabel22 = new javax.swing.JLabel();
        jComboBox4 = new javax.swing.JComboBox<>();
        jButton12 = new javax.swing.JButton();
        jPanel29 = new javax.swing.JPanel();
        jLabel23 = new javax.swing.JLabel();
        jLabel13 = new javax.swing.JLabel();
        jFormattedTextField1 = new javax.swing.JFormattedTextField();
        jFormattedTextField2 = new javax.swing.JFormattedTextField();
        jLabel26 = new javax.swing.JLabel();
        jLabel14 = new javax.swing.JLabel();
        jPanel41 = new javax.swing.JPanel();
        jLabel24 = new javax.swing.JLabel();
        jTextField8 = new javax.swing.JTextField();
        jPanel42 = new javax.swing.JPanel();
        jLabel25 = new javax.swing.JLabel();
        jTextField9 = new javax.swing.JTextField();
        jPanel33 = new javax.swing.JPanel();
        jDateChooser7 = new com.toedter.calendar.JDateChooser();
        jLabel10 = new javax.swing.JLabel();
        jPanel34 = new javax.swing.JPanel();
        jLabel7 = new javax.swing.JLabel();
        jDateChooser5 = new com.toedter.calendar.JDateChooser();
        jPanel40 = new javax.swing.JPanel();
        jLabel12 = new javax.swing.JLabel();
        jDateChooser6 = new com.toedter.calendar.JDateChooser();
        jPanel37 = new javax.swing.JPanel();
        jButton7 = new javax.swing.JButton();
        jPanel38 = new javax.swing.JPanel();
        jButton10 = new javax.swing.JButton();
        jPanel39 = new javax.swing.JPanel();
        jButton11 = new javax.swing.JButton();
        jPanel14 = new javax.swing.JPanel();
        jPanel20 = new javax.swing.JPanel();
        jLabel11 = new javax.swing.JLabel();
        jLabel1 = new javax.swing.JLabel();
        jTextField13 = new javax.swing.JTextField();
        jComboBox1 = new javax.swing.JComboBox<>();
        jComboBox2 = new javax.swing.JComboBox<>();
        jButton1 = new javax.swing.JButton();
        jButton16 = new javax.swing.JButton();
        jPanel21 = new javax.swing.JPanel();
        jScrollPane2 = new javax.swing.JScrollPane();
        jTable2 = new javax.swing.JTable();

        jMenuItem1.setText("Update");
        jMenuItem1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem1ActionPerformed(evt);
            }
        });
        jPopupMenu1.add(jMenuItem1);

        jMenuItem2.setText("Update");
        jMenuItem2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem2ActionPerformed(evt);
            }
        });
        jPopupMenu2.add(jMenuItem2);

        jMenuItem3.setText("Remove");
        jMenuItem3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem3ActionPerformed(evt);
            }
        });
        jPopupMenu2.add(jMenuItem3);

        setPreferredSize(new java.awt.Dimension(816, 565));
        setLayout(new java.awt.BorderLayout());

        jPanel11.setPreferredSize(new java.awt.Dimension(816, 534));
        jPanel11.setLayout(new java.awt.BorderLayout());

        jPanel4.setPreferredSize(new java.awt.Dimension(816, 150));
        jPanel4.setLayout(new java.awt.BorderLayout());

        jPanel22.setPreferredSize(new java.awt.Dimension(816, 50));

        javax.swing.GroupLayout jPanel22Layout = new javax.swing.GroupLayout(jPanel22);
        jPanel22.setLayout(jPanel22Layout);
        jPanel22Layout.setHorizontalGroup(
            jPanel22Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 835, Short.MAX_VALUE)
        );
        jPanel22Layout.setVerticalGroup(
            jPanel22Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 50, Short.MAX_VALUE)
        );

        jPanel4.add(jPanel22, java.awt.BorderLayout.PAGE_START);

        jPanel23.setBorder(javax.swing.BorderFactory.createEmptyBorder(5, 5, 5, 5));
        jPanel23.setLayout(new java.awt.GridLayout(1, 3, 1, 1));

        jButton4.setIcon(new javax.swing.ImageIcon(getClass().getResource("/lk/corevision/fruitshop/resources/transport/icons8-delivery-35.png"))); // NOI18N
        jButton4.setText("Delivery Manage");
        jButton4.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                jButton4MouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                jButton4MouseExited(evt);
            }
        });
        jButton4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton4ActionPerformed(evt);
            }
        });
        jPanel23.add(jButton4);

        jButton9.setIcon(new javax.swing.ImageIcon(getClass().getResource("/lk/corevision/fruitshop/resources/transport/editedVehicle.png"))); // NOI18N
        jButton9.setText("Vehicle Adding");
        jButton9.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                jButton9MouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                jButton9MouseExited(evt);
            }
        });
        jButton9.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton9ActionPerformed(evt);
            }
        });
        jPanel23.add(jButton9);

        jButton15.setIcon(new javax.swing.ImageIcon(getClass().getResource("/lk/corevision/fruitshop/resources/transport/vehiclemanage.png"))); // NOI18N
        jButton15.setText("Vehicle Manage");
        jButton15.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                jButton15MouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                jButton15MouseExited(evt);
            }
        });
        jButton15.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton15ActionPerformed(evt);
            }
        });
        jPanel23.add(jButton15);

        jPanel4.add(jPanel23, java.awt.BorderLayout.CENTER);

        jPanel11.add(jPanel4, java.awt.BorderLayout.PAGE_START);

        jPanel7.setBorder(javax.swing.BorderFactory.createEmptyBorder(5, 5, 5, 5));
        jPanel7.setLayout(new java.awt.BorderLayout());
        jPanel7.add(curveLineChart1, java.awt.BorderLayout.CENTER);

        jPanel11.add(jPanel7, java.awt.BorderLayout.CENTER);

        jTabbedPane2.addTab("Dashboard", jPanel11);

        jPanel10.setBackground(new java.awt.Color(51, 255, 51));
        jPanel10.setPreferredSize(new java.awt.Dimension(816, 534));
        jPanel10.setLayout(new java.awt.BorderLayout());

        jPanel5.setBackground(new java.awt.Color(204, 204, 204));

        jLabel8.setForeground(new java.awt.Color(0, 0, 0));
        jLabel8.setText("Delivery Add");

        jButton17.setText("Print");
        jButton17.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton17ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel5Layout = new javax.swing.GroupLayout(jPanel5);
        jPanel5.setLayout(jPanel5Layout);
        jPanel5Layout.setHorizontalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addGap(25, 25, 25)
                .addComponent(jLabel8)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 649, Short.MAX_VALUE)
                .addComponent(jButton17)
                .addGap(18, 18, 18))
        );
        jPanel5Layout.setVerticalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel8)
                    .addComponent(jButton17))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jPanel10.add(jPanel5, java.awt.BorderLayout.PAGE_START);

        jPanel6.setBackground(new java.awt.Color(255, 153, 153));
        jPanel6.setLayout(new java.awt.BorderLayout());

        jPanel2.setBorder(javax.swing.BorderFactory.createEmptyBorder(5, 5, 5, 5));
        jPanel2.setMaximumSize(new java.awt.Dimension(0, 0));
        jPanel2.setLayout(new java.awt.GridLayout(3, 2));

        jLabel2.setText("Driver NIC");

        jButton2.setText("Select");
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel12Layout = new javax.swing.GroupLayout(jPanel12);
        jPanel12.setLayout(jPanel12Layout);
        jPanel12Layout.setHorizontalGroup(
            jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel12Layout.createSequentialGroup()
                .addGap(22, 22, 22)
                .addGroup(jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel2)
                    .addGroup(jPanel12Layout.createSequentialGroup()
                        .addComponent(jTextField2, javax.swing.GroupLayout.DEFAULT_SIZE, 190, Short.MAX_VALUE)
                        .addGap(18, 18, 18)
                        .addComponent(jButton2, javax.swing.GroupLayout.PREFERRED_SIZE, 91, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(91, Short.MAX_VALUE))
        );
        jPanel12Layout.setVerticalGroup(
            jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel12Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel2)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 20, Short.MAX_VALUE)
                .addGroup(jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jTextField2, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton2, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)))
        );

        jPanel2.add(jPanel12);

        jLabel3.setText("Employee NIC");

        javax.swing.GroupLayout jPanel15Layout = new javax.swing.GroupLayout(jPanel15);
        jPanel15.setLayout(jPanel15Layout);
        jPanel15Layout.setHorizontalGroup(
            jPanel15Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel15Layout.createSequentialGroup()
                .addGap(17, 17, 17)
                .addGroup(jPanel15Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel15Layout.createSequentialGroup()
                        .addComponent(jTextField3, javax.swing.GroupLayout.DEFAULT_SIZE, 382, Short.MAX_VALUE)
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(jPanel15Layout.createSequentialGroup()
                        .addComponent(jLabel3)
                        .addContainerGap(320, Short.MAX_VALUE))))
        );
        jPanel15Layout.setVerticalGroup(
            jPanel15Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel15Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel3)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 20, Short.MAX_VALUE)
                .addComponent(jTextField3, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        jPanel2.add(jPanel15);

        jLabel4.setText("Delivery Status");

        jComboBox5.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));

        javax.swing.GroupLayout jPanel16Layout = new javax.swing.GroupLayout(jPanel16);
        jPanel16.setLayout(jPanel16Layout);
        jPanel16Layout.setHorizontalGroup(
            jPanel16Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel16Layout.createSequentialGroup()
                .addGap(25, 25, 25)
                .addGroup(jPanel16Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel4)
                    .addComponent(jComboBox5, javax.swing.GroupLayout.PREFERRED_SIZE, 197, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(190, Short.MAX_VALUE))
        );
        jPanel16Layout.setVerticalGroup(
            jPanel16Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel16Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel4)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jComboBox5, javax.swing.GroupLayout.DEFAULT_SIZE, 43, Short.MAX_VALUE)
                .addContainerGap())
        );

        jPanel2.add(jPanel16);

        jLabel5.setText("Order ID");

        jButton5.setText("Select");
        jButton5.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton5ActionPerformed(evt);
            }
        });

        jButton13.setBackground(new java.awt.Color(255, 153, 153));
        jButton13.setIcon(new javax.swing.ImageIcon(getClass().getResource("/lk/corevision/fruitshop/resources/transport/icons8-add-location-30.png"))); // NOI18N
        jButton13.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton13ActionPerformed(evt);
            }
        });

        jButton14.setIcon(new javax.swing.ImageIcon(getClass().getResource("/lk/corevision/fruitshop/resources/transport/icons8-close-30.png"))); // NOI18N
        jButton14.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton14ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel17Layout = new javax.swing.GroupLayout(jPanel17);
        jPanel17.setLayout(jPanel17Layout);
        jPanel17Layout.setHorizontalGroup(
            jPanel17Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel17Layout.createSequentialGroup()
                .addGap(17, 17, 17)
                .addGroup(jPanel17Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel17Layout.createSequentialGroup()
                        .addComponent(jLabel5)
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(jPanel17Layout.createSequentialGroup()
                        .addComponent(jTextField5, javax.swing.GroupLayout.DEFAULT_SIZE, 180, Short.MAX_VALUE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jButton5)
                        .addGap(18, 18, 18)
                        .addComponent(jButton13)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jButton14)
                        .addGap(29, 29, 29))))
        );
        jPanel17Layout.setVerticalGroup(
            jPanel17Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel17Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel17Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel17Layout.createSequentialGroup()
                        .addComponent(jLabel5)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(jPanel17Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jTextField5, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jButton5, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(0, 8, Short.MAX_VALUE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel17Layout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addGroup(jPanel17Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jButton13, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 48, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jButton14, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 48, javax.swing.GroupLayout.PREFERRED_SIZE)))))
        );

        jPanel2.add(jPanel17);

        jLabel6.setText("Delivery Date");

        jDateChooser1.setDateFormatString("yyyy-MM-dd");

        javax.swing.GroupLayout jPanel18Layout = new javax.swing.GroupLayout(jPanel18);
        jPanel18.setLayout(jPanel18Layout);
        jPanel18Layout.setHorizontalGroup(
            jPanel18Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel18Layout.createSequentialGroup()
                .addGap(20, 20, 20)
                .addGroup(jPanel18Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel6)
                    .addComponent(jDateChooser1, javax.swing.GroupLayout.PREFERRED_SIZE, 201, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(191, Short.MAX_VALUE))
        );
        jPanel18Layout.setVerticalGroup(
            jPanel18Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel18Layout.createSequentialGroup()
                .addGap(10, 10, 10)
                .addComponent(jLabel6)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jDateChooser1, javax.swing.GroupLayout.DEFAULT_SIZE, 39, Short.MAX_VALUE)
                .addContainerGap())
        );

        jPanel2.add(jPanel18);

        jPanel19.setBorder(javax.swing.BorderFactory.createEmptyBorder(15, 15, 15, 15));
        jPanel19.setLayout(new java.awt.GridLayout(1, 3, 5, 0));

        jButton6.setText("Add");
        jButton6.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton6ActionPerformed(evt);
            }
        });
        jPanel19.add(jButton6);

        jButton8.setText("Update");
        jButton8.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton8ActionPerformed(evt);
            }
        });
        jPanel19.add(jButton8);

        jButton3.setText("Clear");
        jButton3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton3ActionPerformed(evt);
            }
        });
        jPanel19.add(jButton3);

        jPanel2.add(jPanel19);

        jPanel6.add(jPanel2, java.awt.BorderLayout.PAGE_START);

        jPanel3.setBorder(javax.swing.BorderFactory.createEmptyBorder(5, 5, 5, 5));
        jPanel3.setLayout(new java.awt.BorderLayout());

        jPanel24.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N

        jTextField4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextField4ActionPerformed(evt);
            }
        });
        jTextField4.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                jTextField4KeyReleased(evt);
            }
        });

        jLabel15.setText("Search By Delivery Id :");

        javax.swing.GroupLayout jPanel24Layout = new javax.swing.GroupLayout(jPanel24);
        jPanel24.setLayout(jPanel24Layout);
        jPanel24Layout.setHorizontalGroup(
            jPanel24Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel24Layout.createSequentialGroup()
                .addContainerGap(494, Short.MAX_VALUE)
                .addComponent(jLabel15)
                .addGap(24, 24, 24)
                .addComponent(jTextField4, javax.swing.GroupLayout.PREFERRED_SIZE, 192, javax.swing.GroupLayout.PREFERRED_SIZE))
        );
        jPanel24Layout.setVerticalGroup(
            jPanel24Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel24Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                .addComponent(jTextField4, javax.swing.GroupLayout.DEFAULT_SIZE, 35, Short.MAX_VALUE)
                .addComponent(jLabel15))
        );

        jPanel3.add(jPanel24, java.awt.BorderLayout.PAGE_START);

        jTable1.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Delivery ID", "Customer NIC", "Location", "Delivery Date", "Order ID", "Driver NIC", "Employee", "Placed Date", "Status"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jTable1.setShowHorizontalLines(true);
        jTable1.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jTable1MouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(jTable1);

        jPanel3.add(jScrollPane1, java.awt.BorderLayout.CENTER);

        jPanel6.add(jPanel3, java.awt.BorderLayout.CENTER);

        jPanel10.add(jPanel6, java.awt.BorderLayout.CENTER);

        jTabbedPane2.addTab("Delivery Manage", jPanel10);

        jPanel13.setBackground(new java.awt.Color(51, 255, 51));
        jPanel13.setPreferredSize(new java.awt.Dimension(816, 534));
        jPanel13.setLayout(new java.awt.BorderLayout());

        jPanel8.setBackground(new java.awt.Color(204, 204, 204));
        jPanel8.setPreferredSize(new java.awt.Dimension(835, 39));

        jLabel9.setForeground(new java.awt.Color(0, 0, 0));
        jLabel9.setText("Vehicle Adding");

        javax.swing.GroupLayout jPanel8Layout = new javax.swing.GroupLayout(jPanel8);
        jPanel8.setLayout(jPanel8Layout);
        jPanel8Layout.setHorizontalGroup(
            jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel8Layout.createSequentialGroup()
                .addGap(25, 25, 25)
                .addComponent(jLabel9)
                .addContainerGap(730, Short.MAX_VALUE))
        );
        jPanel8Layout.setVerticalGroup(
            jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel8Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel9, javax.swing.GroupLayout.DEFAULT_SIZE, 27, Short.MAX_VALUE)
                .addContainerGap())
        );

        jPanel13.add(jPanel8, java.awt.BorderLayout.PAGE_START);

        jPanel9.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 10, 1, 10));
        jPanel9.setLayout(new java.awt.GridLayout(4, 3, 10, 10));

        jLabel21.setText("Vehicle License No");

        javax.swing.GroupLayout jPanel27Layout = new javax.swing.GroupLayout(jPanel27);
        jPanel27.setLayout(jPanel27Layout);
        jPanel27Layout.setHorizontalGroup(
            jPanel27Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel27Layout.createSequentialGroup()
                .addGap(38, 38, 38)
                .addGroup(jPanel27Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel27Layout.createSequentialGroup()
                        .addComponent(jTextField1, javax.swing.GroupLayout.DEFAULT_SIZE, 215, Short.MAX_VALUE)
                        .addContainerGap(12, Short.MAX_VALUE))
                    .addGroup(jPanel27Layout.createSequentialGroup()
                        .addComponent(jLabel21, javax.swing.GroupLayout.PREFERRED_SIZE, 143, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
        );
        jPanel27Layout.setVerticalGroup(
            jPanel27Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel27Layout.createSequentialGroup()
                .addContainerGap(28, Short.MAX_VALUE)
                .addComponent(jLabel21)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jTextField1, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(30, 30, 30))
        );

        jPanel9.add(jPanel27);

        jLabel20.setText("Vehicle Status");

        jComboBox3.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));

        javax.swing.GroupLayout jPanel26Layout = new javax.swing.GroupLayout(jPanel26);
        jPanel26.setLayout(jPanel26Layout);
        jPanel26Layout.setHorizontalGroup(
            jPanel26Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel26Layout.createSequentialGroup()
                .addGap(35, 35, 35)
                .addGroup(jPanel26Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel20, javax.swing.GroupLayout.PREFERRED_SIZE, 103, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jComboBox3, 0, 223, Short.MAX_VALUE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel26Layout.setVerticalGroup(
            jPanel26Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel26Layout.createSequentialGroup()
                .addContainerGap(28, Short.MAX_VALUE)
                .addComponent(jLabel20)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jComboBox3, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(30, 30, 30))
        );

        jPanel9.add(jPanel26);

        jLabel22.setText("Vehicle Type");

        jComboBox4.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));

        jButton12.setText("+");
        jButton12.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton12ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel28Layout = new javax.swing.GroupLayout(jPanel28);
        jPanel28.setLayout(jPanel28Layout);
        jPanel28Layout.setHorizontalGroup(
            jPanel28Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel28Layout.createSequentialGroup()
                .addGap(20, 20, 20)
                .addGroup(jPanel28Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel22, javax.swing.GroupLayout.PREFERRED_SIZE, 103, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanel28Layout.createSequentialGroup()
                        .addComponent(jComboBox4, javax.swing.GroupLayout.PREFERRED_SIZE, 170, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jButton12, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(29, 29, 29))
        );
        jPanel28Layout.setVerticalGroup(
            jPanel28Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel28Layout.createSequentialGroup()
                .addContainerGap(26, Short.MAX_VALUE)
                .addComponent(jLabel22)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel28Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jComboBox4, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton12, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(32, 32, 32))
        );

        jPanel9.add(jPanel28);

        jLabel23.setText("Capacity");

        jLabel13.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel13.setText("Kg");

        jFormattedTextField1.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.NumberFormatter(new java.text.DecimalFormat("#0.00"))));

        jFormattedTextField2.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.NumberFormatter(new java.text.DecimalFormat("#0.00"))));

        jLabel26.setText("Mileage");

        jLabel14.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel14.setText("KM");

        javax.swing.GroupLayout jPanel29Layout = new javax.swing.GroupLayout(jPanel29);
        jPanel29.setLayout(jPanel29Layout);
        jPanel29Layout.setHorizontalGroup(
            jPanel29Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel29Layout.createSequentialGroup()
                .addGap(38, 38, 38)
                .addGroup(jPanel29Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel23)
                    .addGroup(jPanel29Layout.createSequentialGroup()
                        .addComponent(jFormattedTextField1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel13)))
                .addGap(18, 18, 18)
                .addGroup(jPanel29Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel26)
                    .addGroup(jPanel29Layout.createSequentialGroup()
                        .addComponent(jFormattedTextField2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel14)))
                .addContainerGap(21, Short.MAX_VALUE))
        );
        jPanel29Layout.setVerticalGroup(
            jPanel29Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel29Layout.createSequentialGroup()
                .addContainerGap(26, Short.MAX_VALUE)
                .addGroup(jPanel29Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(jPanel29Layout.createSequentialGroup()
                        .addComponent(jLabel26)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel29Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel14, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jFormattedTextField2, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(jPanel29Layout.createSequentialGroup()
                        .addComponent(jLabel23)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel29Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel13, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jFormattedTextField1, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addGap(31, 31, 31))
        );

        jPanel9.add(jPanel29);

        jLabel24.setText("Brand");

        javax.swing.GroupLayout jPanel41Layout = new javax.swing.GroupLayout(jPanel41);
        jPanel41.setLayout(jPanel41Layout);
        jPanel41Layout.setHorizontalGroup(
            jPanel41Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel41Layout.createSequentialGroup()
                .addGap(35, 35, 35)
                .addGroup(jPanel41Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel24, javax.swing.GroupLayout.PREFERRED_SIZE, 103, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jTextField8, javax.swing.GroupLayout.DEFAULT_SIZE, 223, Short.MAX_VALUE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel41Layout.setVerticalGroup(
            jPanel41Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel41Layout.createSequentialGroup()
                .addContainerGap(28, Short.MAX_VALUE)
                .addComponent(jLabel24)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jTextField8, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(30, 30, 30))
        );

        jPanel9.add(jPanel41);

        jLabel25.setText("Model");

        javax.swing.GroupLayout jPanel42Layout = new javax.swing.GroupLayout(jPanel42);
        jPanel42.setLayout(jPanel42Layout);
        jPanel42Layout.setHorizontalGroup(
            jPanel42Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel42Layout.createSequentialGroup()
                .addGap(20, 20, 20)
                .addGroup(jPanel42Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel25, javax.swing.GroupLayout.PREFERRED_SIZE, 103, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jTextField9, javax.swing.GroupLayout.DEFAULT_SIZE, 212, Short.MAX_VALUE))
                .addGap(33, 33, 33))
        );
        jPanel42Layout.setVerticalGroup(
            jPanel42Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel42Layout.createSequentialGroup()
                .addContainerGap(28, Short.MAX_VALUE)
                .addComponent(jLabel25)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jTextField9, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(30, 30, 30))
        );

        jPanel9.add(jPanel42);

        jDateChooser7.setForeground(new java.awt.Color(255, 255, 255));
        jDateChooser7.setDateFormatString("yyyy-MM-dd");

        jLabel10.setText("Insuarance Date");

        javax.swing.GroupLayout jPanel33Layout = new javax.swing.GroupLayout(jPanel33);
        jPanel33.setLayout(jPanel33Layout);
        jPanel33Layout.setHorizontalGroup(
            jPanel33Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel33Layout.createSequentialGroup()
                .addGap(38, 38, 38)
                .addGroup(jPanel33Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel10)
                    .addComponent(jDateChooser7, javax.swing.GroupLayout.PREFERRED_SIZE, 200, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(27, 27, 27))
        );
        jPanel33Layout.setVerticalGroup(
            jPanel33Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel33Layout.createSequentialGroup()
                .addGap(25, 25, 25)
                .addComponent(jLabel10)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jDateChooser7, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(33, Short.MAX_VALUE))
        );

        jPanel9.add(jPanel33);

        jLabel7.setText("Year Of Manufacture");

        jDateChooser5.setDateFormatString("yyyy-MM-dd");

        javax.swing.GroupLayout jPanel34Layout = new javax.swing.GroupLayout(jPanel34);
        jPanel34.setLayout(jPanel34Layout);
        jPanel34Layout.setHorizontalGroup(
            jPanel34Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel34Layout.createSequentialGroup()
                .addGap(35, 35, 35)
                .addGroup(jPanel34Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel7)
                    .addComponent(jDateChooser5, javax.swing.GroupLayout.PREFERRED_SIZE, 200, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(30, Short.MAX_VALUE))
        );
        jPanel34Layout.setVerticalGroup(
            jPanel34Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel34Layout.createSequentialGroup()
                .addGap(30, 30, 30)
                .addComponent(jLabel7)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jDateChooser5, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(30, 30, 30))
        );

        jPanel9.add(jPanel34);

        jLabel12.setText("Purchase Date");

        jDateChooser6.setDateFormatString("yyyy-MM-dd");

        javax.swing.GroupLayout jPanel40Layout = new javax.swing.GroupLayout(jPanel40);
        jPanel40.setLayout(jPanel40Layout);
        jPanel40Layout.setHorizontalGroup(
            jPanel40Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel40Layout.createSequentialGroup()
                .addGap(20, 20, 20)
                .addGroup(jPanel40Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel12)
                    .addComponent(jDateChooser6, javax.swing.GroupLayout.PREFERRED_SIZE, 200, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(45, Short.MAX_VALUE))
        );
        jPanel40Layout.setVerticalGroup(
            jPanel40Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel40Layout.createSequentialGroup()
                .addGap(30, 30, 30)
                .addComponent(jLabel12)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jDateChooser6, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(30, 30, 30))
        );

        jPanel9.add(jPanel40);

        jButton7.setText("Add");
        jButton7.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton7ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel37Layout = new javax.swing.GroupLayout(jPanel37);
        jPanel37.setLayout(jPanel37Layout);
        jPanel37Layout.setHorizontalGroup(
            jPanel37Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel37Layout.createSequentialGroup()
                .addGap(40, 40, 40)
                .addComponent(jButton7, javax.swing.GroupLayout.PREFERRED_SIZE, 219, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        jPanel37Layout.setVerticalGroup(
            jPanel37Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel37Layout.createSequentialGroup()
                .addGap(36, 36, 36)
                .addComponent(jButton7, javax.swing.GroupLayout.PREFERRED_SIZE, 45, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jPanel9.add(jPanel37);

        jButton10.setText("Update");
        jButton10.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton10ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel38Layout = new javax.swing.GroupLayout(jPanel38);
        jPanel38.setLayout(jPanel38Layout);
        jPanel38Layout.setHorizontalGroup(
            jPanel38Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel38Layout.createSequentialGroup()
                .addGap(40, 40, 40)
                .addComponent(jButton10, javax.swing.GroupLayout.DEFAULT_SIZE, 219, Short.MAX_VALUE)
                .addContainerGap())
        );
        jPanel38Layout.setVerticalGroup(
            jPanel38Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel38Layout.createSequentialGroup()
                .addGap(36, 36, 36)
                .addComponent(jButton10, javax.swing.GroupLayout.PREFERRED_SIZE, 45, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(34, Short.MAX_VALUE))
        );

        jPanel9.add(jPanel38);

        jButton11.setText("Clear");
        jButton11.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton11ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel39Layout = new javax.swing.GroupLayout(jPanel39);
        jPanel39.setLayout(jPanel39Layout);
        jPanel39Layout.setHorizontalGroup(
            jPanel39Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel39Layout.createSequentialGroup()
                .addGap(20, 20, 20)
                .addComponent(jButton11, javax.swing.GroupLayout.DEFAULT_SIZE, 239, Short.MAX_VALUE)
                .addContainerGap())
        );
        jPanel39Layout.setVerticalGroup(
            jPanel39Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel39Layout.createSequentialGroup()
                .addGap(36, 36, 36)
                .addComponent(jButton11, javax.swing.GroupLayout.PREFERRED_SIZE, 45, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(34, Short.MAX_VALUE))
        );

        jPanel9.add(jPanel39);

        jPanel13.add(jPanel9, java.awt.BorderLayout.CENTER);

        jTabbedPane2.addTab("Vehicle Adding", jPanel13);

        jPanel14.setBackground(new java.awt.Color(51, 255, 51));
        jPanel14.setPreferredSize(new java.awt.Dimension(816, 534));
        jPanel14.setLayout(new java.awt.BorderLayout());

        jPanel20.setBackground(new java.awt.Color(204, 204, 204));

        jLabel11.setForeground(new java.awt.Color(0, 0, 0));
        jLabel11.setText("Vehicle Manage");

        jLabel1.setForeground(new java.awt.Color(0, 0, 0));
        jLabel1.setText("Search By Licesnse  No : ");

        jTextField13.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                jTextField13KeyReleased(evt);
            }
        });

        jComboBox1.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        jComboBox1.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                jComboBox1ItemStateChanged(evt);
            }
        });
        jComboBox1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jComboBox1ActionPerformed(evt);
            }
        });

        jComboBox2.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        jComboBox2.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                jComboBox2ItemStateChanged(evt);
            }
        });

        jButton1.setText("Clear");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        jButton16.setText("Print");
        jButton16.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton16ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel20Layout = new javax.swing.GroupLayout(jPanel20);
        jPanel20.setLayout(jPanel20Layout);
        jPanel20Layout.setHorizontalGroup(
            jPanel20Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel20Layout.createSequentialGroup()
                .addGap(25, 25, 25)
                .addComponent(jLabel11)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 22, Short.MAX_VALUE)
                .addComponent(jLabel1)
                .addGap(18, 18, 18)
                .addComponent(jTextField13, javax.swing.GroupLayout.PREFERRED_SIZE, 200, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jButton16)
                .addGap(29, 29, 29)
                .addComponent(jComboBox1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jComboBox2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jButton1)
                .addContainerGap())
        );
        jPanel20Layout.setVerticalGroup(
            jPanel20Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel20Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel20Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel11)
                    .addComponent(jLabel1)
                    .addComponent(jTextField13, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jComboBox1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jComboBox2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton1)
                    .addComponent(jButton16))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jPanel14.add(jPanel20, java.awt.BorderLayout.PAGE_START);

        jPanel21.setBorder(javax.swing.BorderFactory.createEmptyBorder(5, 5, 5, 5));
        jPanel21.setLayout(new java.awt.BorderLayout());

        jTable2.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null}
            },
            new String [] {
                "License No", "Year Of Manufacture", "Insurance Date", "Purchase Date", "Capacity", "Mileage", "Type", "Status", "Employee"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jTable2.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jTable2MouseClicked(evt);
            }
        });
        jScrollPane2.setViewportView(jTable2);

        jPanel21.add(jScrollPane2, java.awt.BorderLayout.CENTER);

        jPanel14.add(jPanel21, java.awt.BorderLayout.CENTER);

        jTabbedPane2.addTab("Vehicle Manage", jPanel14);

        add(jTabbedPane2, java.awt.BorderLayout.CENTER);
    }// </editor-fold>//GEN-END:initComponents

    private void clearFields() {
        jButton10.setEnabled(false);
        jButton7.setEnabled(true);
        jTextField1.setEditable(true);
        jTextField1.setText("");
        jComboBox3.setSelectedIndex(0);
        jComboBox4.setSelectedIndex(0);
        jFormattedTextField1.setText("");
        jFormattedTextField2.setText("");
        jTextField8.setText("");
        jTextField9.setText("");
        jDateChooser7.setDate(null);
        jDateChooser5.setDate(null);
        jDateChooser6.setDate(null);
    }

    private void loadVehicles() {

        try {

            String query = "SELECT * FROM `vehicle` INNER JOIN `vehicle_status` ON `vehicle`.`vehicle_status_id` = `vehicle_status`.`vehicle_status_id` INNER JOIN `vehicle_type` ON `vehicle`.`vehicle_type_id`=`vehicle_type`.`vehicle_type_id` INNER JOIN `employee` ON `vehicle`.`employee_NIC`=`employee`.`NIC` ";

            boolean hasWhereClause = false;

            if (!jTextField13.getText().isEmpty()) {
                query += "WHERE `vehicle`.`licens_plate` LIKE '%" + jTextField13.getText() + "%' ";
                hasWhereClause = true;
            }

            if (!jComboBox2.getSelectedItem().equals("Status")) {
                if (hasWhereClause) {
                    query += "AND ";
                } else {
                    query += "WHERE ";
                    hasWhereClause = true;

                }
                query += "`vehicle`.`vehicle_status_id`='" + vehicleStatMap.get(jComboBox2.getSelectedItem()) + "' ";
            }

            if (!jComboBox1.getSelectedItem().equals("Type")) {
                if (hasWhereClause) {
                    query += "AND ";
                } else {
                    query += "WHERE ";
                    hasWhereClause = true;

                }
                query += "`vehicle`.`vehicle_type_id`='" + vehicleTypeMap.get(jComboBox1.getSelectedItem()) + "' ";

            }

            ResultSet resultSet = MySQL.executeSearch(query);

            DefaultTableModel model = (DefaultTableModel) jTable2.getModel();
            model.setRowCount(0);

            while (resultSet.next()) {
                Vector<String> vector = new Vector<>();
                vector.add(resultSet.getString("licens_plate"));
                vector.add(resultSet.getString("year_of_manufacture"));
                vector.add(resultSet.getString("insuarance_date"));
                vector.add(resultSet.getString("purchase_date"));
                vector.add(resultSet.getString("capacity"));
                vector.add(resultSet.getString("mileage"));
                vector.add(resultSet.getString("vehicle_type.vehicle_type"));
                vector.add(resultSet.getString("vehicle_status.vehicle_status"));
                vector.add(resultSet.getString("employee.NIC"));

                model.addRow(vector);
            }

            jTable2.setModel(model);

        } catch (Exception e) {
            SignIn.getLoggerObject().log(Level.WARNING, "Transport Exception Found", e);
        }
    }


    private void jMenuItem1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem1ActionPerformed
        setVehicleDetails(String.valueOf(jTable2.getValueAt(jTable2.getSelectedRow(), 0)));
        jTabbedPane2.setSelectedIndex(2);


    }//GEN-LAST:event_jMenuItem1ActionPerformed

    private void loadDeliveryManage() {

        try {

            String query = "SELECT * FROM `delivery` INNER JOIN `delivery_status` ON `delivery`.`delivery_status_id`=`delivery_status`.`delivery_status_id` INNER JOIN `orders` ON `delivery`.`orders_order_id`=`orders`.`order_id` INNER JOIN `customer` ON `orders`.`customer_nic`=`customer`.`customer_nic`";

            boolean hasWhereClause = false;

            if (!jTextField4.getText().isEmpty()) {
                if (hasWhereClause) {
                    query += "AND ";
                } else {
                    query += "WHERE ";
                    hasWhereClause = true;

                }
                query += "`delivery`.`delivery_id`='" + jTextField4.getText() + "' ";

            }

            ResultSet resultSet = MySQL.executeSearch(query);

            DefaultTableModel model = (DefaultTableModel) jTable1.getModel();
            model.setRowCount(0);
            while (resultSet.next()) {
                Vector<String> vector = new Vector<>();
                vector.add(resultSet.getString("delivery.delivery_id"));
                vector.add(resultSet.getString("customer.customer_nic"));
                vector.add(resultSet.getString("delivery.customer_address_id"));
                vector.add(resultSet.getString("delivery.delivery_date"));
                vector.add(resultSet.getString("delivery.orders_order_id"));
                vector.add(resultSet.getString("delivery.driver_NIC"));
                vector.add(resultSet.getString("delivery.assign_manager_nic"));
                vector.add(resultSet.getString("delivery.placed_date"));
                vector.add(resultSet.getString("delivery_status.delivery_status"));

                model.addRow(vector);
            }
            jTable1.setModel(model);

        } catch (Exception e) {
            SignIn.getLoggerObject().log(Level.WARNING, "Delivery Manage Exception Found", e);
        }

    }

    private void jMenuItem2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem2ActionPerformed

        int row = jTable1.getSelectedRow();

        if (row != -1) {

            try {
                jTextField2.setText(String.valueOf(jTable1.getValueAt(row, 5)));
                jTextField3.setText(String.valueOf(jTable1.getValueAt(row, 6)));
                jComboBox5.setSelectedItem(String.valueOf(jTable1.getValueAt(row, 8)));
                jTextField5.setText(String.valueOf(jTable1.getValueAt(row, 4)));
                jDateChooser1.setDate(new SimpleDateFormat("yyyy-MM-dd").parse(String.valueOf(jTable1.getValueAt(row, 3))));
                this.customerAddressId = String.valueOf(jTable1.getValueAt(row, 2));
                jButton13.setBackground(new Color(102, 255, 102));
                jButton14.setEnabled(true);
                jButton5.setEnabled(false);
                delvieryId = String.valueOf(jTable1.getValueAt(row, 0));

            } catch (Exception e) {
                SignIn.getLoggerObject().log(Level.WARNING, "Transport Value Setting Exception Found", e);
            }

            jButton8.setEnabled(true);
            jButton6.setEnabled(false);
            jButton5.setEnabled(false);
        }


    }//GEN-LAST:event_jMenuItem2ActionPerformed

    private void jMenuItem3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem3ActionPerformed

        int row = jTable1.getSelectedRow();

        if (row != -1) {

            String deliveryId = String.valueOf(jTable1.getValueAt(row, 0));
            try {
                ResultSet resultSet = MySQL.executeSearch("SELECT * FROM `delivery` INNER JOIN `delivery_status` ON `delivery`.`delivery_status_id`=`delivery_status`.`delivery_status_id` WHERE `delivery_id`='" + deliveryId + "'");
                if (resultSet.next()) {

                    int result = JOptionPane.showConfirmDialog(this, "This delivery is '" + resultSet.getString("delivery_status.delivery_status") + "'.  Do Still want to remove it", "Warning", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, JOptionIcon.INFO.setICon());
                    if (result == JOptionPane.YES_OPTION) {
                        //If delivery remove change its order status to on going
                        MySQL.executeIUD("UPDATE `orders` SET `order_status_id`='1' WHERE `order_id`='" + resultSet.getString("orders_order_id") + "'");
                        MySQL.executeIUD("DELETE FROM `delivery` WHERE `delivery_id`='" + deliveryId + "'");
                        Notifications.getInstance().show(Notifications.Type.SUCCESS, Notifications.Location.BOTTOM_RIGHT, "Delivery removed Successfully");
                        loadDeliveryManage();
                    } else {
                        loadDeliveryManage();
                    }
                }

            } catch (Exception e) {
                SignIn.getLoggerObject().log(Level.WARNING, "Delivery Exception Found", e);
            }
        }

    }//GEN-LAST:event_jMenuItem3ActionPerformed

    private void jTable2MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jTable2MouseClicked
        int row = jTable2.getSelectedRow();
        if (row != -1 && MouseEvent.BUTTON3 == evt.getButton()) {
            jPopupMenu1.show(evt.getComponent(), evt.getX(), evt.getY());

        }
    }//GEN-LAST:event_jTable2MouseClicked

    private void jButton16ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton16ActionPerformed
        Date date = new Date();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        String reportDate = dateFormat.format(date);

        try {

            HashMap<String, Object> parameters = new HashMap<>();
            parameters.put("Parameter1", reportDate);
            parameters.put("Parameter2", SignIn.getEmployeeNIC());
            parameters.put("Parameter101", SendLogo.getFilePath());

            JRTableModelDataSource dataSource = new JRTableModelDataSource(jTable2.getModel());
            JasperPrint report = JasperFillManager.fillReport("src/lk/corevision/fruitshop/reports/transport/fruitShopAppVehicle.jasper", parameters, dataSource);

            JasperViewer.viewReport(report, false);

        } catch (Exception e) {
            SignIn.getLoggerObject().log(Level.WARNING, "Transport Exception Found", e);
        }
    }//GEN-LAST:event_jButton16ActionPerformed

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        clearVehicleManageFields();
    }//GEN-LAST:event_jButton1ActionPerformed

    private void jComboBox2ItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_jComboBox2ItemStateChanged
        loadVehicles();
    }//GEN-LAST:event_jComboBox2ItemStateChanged

    private void jComboBox1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jComboBox1ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jComboBox1ActionPerformed

    private void jComboBox1ItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_jComboBox1ItemStateChanged
        loadVehicles();
    }//GEN-LAST:event_jComboBox1ItemStateChanged

    private void jTextField13KeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTextField13KeyReleased
        loadVehicles();
    }//GEN-LAST:event_jTextField13KeyReleased

    private void jButton11ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton11ActionPerformed
        clearFields();
    }//GEN-LAST:event_jButton11ActionPerformed

    private void jButton10ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton10ActionPerformed

        String licenseNo = jTextField1.getText();
        String vehicleStat = String.valueOf(jComboBox3.getSelectedItem());
        String vehicleType = String.valueOf(jComboBox4.getSelectedItem());
        String vehicleCapacity = jFormattedTextField1.getText();
        String vehicleBrand = jTextField8.getText();
        String vehicleModel = jTextField9.getText();
        Date vehicleInsurance = jDateChooser7.getDate();
        Date vehicleManufacture = jDateChooser5.getDate();
        Date vehiclePurchase = jDateChooser6.getDate();

        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");

        if (licenseNo.isEmpty()) {
            JOptionPane.showMessageDialog(this, "License Number cannot be empty. ", "Warning", JOptionPane.OK_OPTION, JOptionIcon.WARNING.setICon());
        } else if (vehicleStat.equals("Select")) {
            JOptionPane.showMessageDialog(this, "Please select a Vehicle Status. ", "Warning", JOptionPane.OK_OPTION, JOptionIcon.WARNING.setICon());
        } else if (vehicleType.equals("Select")) {
            JOptionPane.showMessageDialog(this, "Please select a Vehicle Type. ", "Warning", JOptionPane.OK_OPTION, JOptionIcon.WARNING.setICon());
        } else if (vehicleCapacity.isEmpty() || vehicleCapacity.equals("0")) {
            JOptionPane.showMessageDialog(this, "Vehicle Capacity must be more than 0. ", "Warning", JOptionPane.OK_OPTION, JOptionIcon.WARNING.setICon());
        } else if (jFormattedTextField2.getText().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Vehicle Mileage cannot be more empty. ", "Warning", JOptionPane.OK_OPTION, JOptionIcon.WARNING.setICon());
        } else if (vehicleBrand.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Vehicle Brand cannot be empty. ", "Warning", JOptionPane.OK_OPTION, JOptionIcon.WARNING.setICon());
        } else if (vehicleModel.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Vehicle Model cannot be empty. ", "Warning", JOptionPane.OK_OPTION, JOptionIcon.WARNING.setICon());
        } else if (vehicleInsurance == null) {
            JOptionPane.showMessageDialog(this, "Please select a Vehicle Insurance Date. ", "Warning", JOptionPane.OK_OPTION, JOptionIcon.WARNING.setICon());
        } else if (vehicleManufacture == null) {
            JOptionPane.showMessageDialog(this, "Please select a Vehicle Manufacture Date. ", "Warning", JOptionPane.OK_OPTION, JOptionIcon.WARNING.setICon());
        } else if (vehiclePurchase == null) {
            JOptionPane.showMessageDialog(this, "Please select a Vehicle Purchase Date. ", "Warning", JOptionPane.OK_OPTION, JOptionIcon.WARNING.setICon());
        } else {

            double vehicleMileage = Double.parseDouble(jFormattedTextField2.getText().trim());

            if (vehicleMileage < 0) {
                JOptionPane.showMessageDialog(this, "Vehicle Mileage cannot be negative. ", "Warning", JOptionPane.OK_OPTION, JOptionIcon.WARNING.setICon());

            } else {

                try {

                    ResultSet resultSet = MySQL.executeSearch("SELECT * FROM `vehicle` WHERE `licens_plate`='" + licenseNo + "'");

                    if (resultSet.next()) {
                        MySQL.executeIUD("UPDATE `vehicle` SET  `year_of_manufacture`='" + format.format(vehicleManufacture) + "',`insuarance_date`='" + format.format(vehicleInsurance) + "',`purchase_date`='" + format.format(vehiclePurchase) + "',`mileage`='" + vehicleMileage + "',`capacity`='" + vehicleCapacity + "',`brand`='" + vehicleBrand + "',`model`='" + vehicleModel + "',`vehicle_status_id`='" + vehicleStatMap.get(vehicleStat) + "',`vehicle_type_id`='" + vehicleTypeMap.get(vehicleType) + "',`employee_NIC`='" + SignIn.getEmployeeNIC() + "' WHERE `licens_plate`='" + licenseNo + "'");
                        JOptionPane.showMessageDialog(this, "Vehicle Details Updated Successfully. ", "Success", JOptionPane.OK_OPTION, JOptionIcon.SUCCESS.setICon());
                        clearFields();
                        jTabbedPane2.setSelectedIndex(3);
                        loadVehicles();

                    } else {
                        JOptionPane.showMessageDialog(this, "Something Went Wrong.", "Warning", JOptionPane.OK_OPTION, JOptionIcon.WARNING.setICon());
                    }

                } catch (Exception e) {
                    SignIn.getLoggerObject().log(Level.WARNING, "Transport Exception Found", e);
                }
            }
        }
    }//GEN-LAST:event_jButton10ActionPerformed

    private void jButton7ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton7ActionPerformed
        String licenseNo = jTextField1.getText();
        String vehicleStat = String.valueOf(jComboBox3.getSelectedItem());
        String vehicleType = String.valueOf(jComboBox4.getSelectedItem());
        String vehicleCapacity = jFormattedTextField1.getText();
        String vehicleBrand = jTextField8.getText();
        String vehicleModel = jTextField9.getText();
        Date vehicleInsurance = jDateChooser7.getDate();
        Date vehicleManufacture = jDateChooser5.getDate();
        Date vehiclePurchase = jDateChooser6.getDate();

        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");

        if (licenseNo.isEmpty()) {
            JOptionPane.showMessageDialog(this, "License Number cannot be empty. ", "Warning", JOptionPane.OK_OPTION, JOptionIcon.WARNING.setICon());
        } else if (vehicleStat.equals("Select")) {
            JOptionPane.showMessageDialog(this, "Please select a Vehicle Status. ", "Warning", JOptionPane.OK_OPTION, JOptionIcon.WARNING.setICon());
        } else if (vehicleType.equals("Select")) {
            JOptionPane.showMessageDialog(this, "Please select a Vehicle Type. ", "Warning", JOptionPane.OK_OPTION, JOptionIcon.WARNING.setICon());
        } else if (vehicleCapacity.isEmpty() || vehicleCapacity.equals("0")) {
            JOptionPane.showMessageDialog(this, "Vehicle Capacity must be more than 0. ", "Warning", JOptionPane.OK_OPTION, JOptionIcon.WARNING.setICon());
        } else if (jFormattedTextField2.getText().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Vehicle Mileage cannot be more empty. ", "Warning", JOptionPane.OK_OPTION, JOptionIcon.WARNING.setICon());
        } else if (vehicleBrand.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Vehicle Brand cannot be empty. ", "Warning", JOptionPane.OK_OPTION, JOptionIcon.WARNING.setICon());
        } else if (vehicleModel.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Vehicle Model cannot be empty. ", "Warning", JOptionPane.OK_OPTION, JOptionIcon.WARNING.setICon());
        } else if (vehicleInsurance == null) {
            JOptionPane.showMessageDialog(this, "Please select a Vehicle Insurance Date. ", "Warning", JOptionPane.OK_OPTION, JOptionIcon.WARNING.setICon());
        } else if (vehicleManufacture == null) {
            JOptionPane.showMessageDialog(this, "Please select a Vehicle Manufacture Date. ", "Warning", JOptionPane.OK_OPTION, JOptionIcon.WARNING.setICon());
        } else if (vehiclePurchase == null) {
            JOptionPane.showMessageDialog(this, "Please select a Vehicle Purchase Date. ", "Warning", JOptionPane.OK_OPTION, JOptionIcon.WARNING.setICon());
        } else {

            double vehicleMileage = Double.parseDouble(jFormattedTextField2.getText().trim());

            if (vehicleMileage < 0) {
                JOptionPane.showMessageDialog(this, "Vehicle Mileage cannot be negative. ", "Warning", JOptionPane.OK_OPTION, JOptionIcon.WARNING.setICon());
            } else {

                try {

                    ResultSet resultSet = MySQL.executeSearch("SELECT * FROM `vehicle` WHERE `licens_plate`='" + licenseNo + "'");

                    if (resultSet.next()) {
                        JOptionPane.showMessageDialog(this, "There is already vehicle added from this license plate. ", "Warning", JOptionPane.OK_OPTION, JOptionIcon.WARNING.setICon());
                    } else {
                        MySQL.executeIUD("INSERT INTO `vehicle`(`licens_plate`,`year_of_manufacture`,`insuarance_date`,`purchase_date`,`mileage`,`capacity`,`brand`,`model`,`vehicle_status_id`,`vehicle_type_id`,`employee_NIC`) VALUES('" + licenseNo + "','" + format.format(vehicleManufacture) + "','" + format.format(vehicleInsurance) + "','" + format.format(vehiclePurchase) + "','" + vehicleMileage + "','" + vehicleCapacity + "','" + vehicleBrand + "','" + vehicleModel + "','" + vehicleStatMap.get(vehicleStat) + "','" + vehicleTypeMap.get(vehicleType) + "','" + SignIn.getEmployeeNIC() + "')");
                        JOptionPane.showMessageDialog(this, "Vehicle Added Successfully. ", "Success", JOptionPane.OK_OPTION, JOptionIcon.SUCCESS.setICon());
                        clearFields();
                        loadVehicles();
                    }
                } catch (Exception e) {
                    SignIn.getLoggerObject().log(Level.WARNING, "Transport Vehicle Adding Exception Found", e);
                }
            }
        }

    }//GEN-LAST:event_jButton7ActionPerformed

    private void jButton12ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton12ActionPerformed
        VehicleTypeDialog vehicleType = new VehicleTypeDialog(db, this, true);
        vehicleType.setVisible(true);
    }//GEN-LAST:event_jButton12ActionPerformed

    private void jTable1MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jTable1MouseClicked

        int row = jTable1.getSelectedRow();

        if (row != -1 && MouseEvent.BUTTON3 == evt.getButton()) {
            jPopupMenu2.show(evt.getComponent(), evt.getX(), evt.getY());

        }
    }//GEN-LAST:event_jTable1MouseClicked

    private void jButton3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton3ActionPerformed
        clearDeliveryManageFields();
    }//GEN-LAST:event_jButton3ActionPerformed

    private void jButton8ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton8ActionPerformed
        String driverNIC = jTextField2.getText();
        String orderId = jTextField5.getText();
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        Date deliveryDate = jDateChooser1.getDate();
        String deliveryStat = String.valueOf(jComboBox5.getSelectedItem());

        try {

            ResultSet resultSet = MySQL.executeSearch("SELECT * FROM `delivery` INNER JOIN `orders` ON `delivery`.`orders_order_id`=`orders`.`order_id` INNER JOIN `customer` ON `orders`.`customer_nic`=`customer`.`customer_nic` WHERE `delivery_id`='" + this.delvieryId + "'");

            if (resultSet.next()) {
                if (orderId.equals(resultSet.getString("orders_order_id"))) {

                    if (customerAddressId == "") {
                        Notifications.getInstance().show(Notifications.Type.WARNING, Notifications.Location.BOTTOM_RIGHT, "Select custoemr address");

                    } else {
                        MySQL.executeIUD("UPDATE `delivery` SET `driver_NIC`='" + driverNIC + "',`delivery_date`='" + format.format(deliveryDate) + "',`delivery_status_id`='" + deliveryStatMap.get(deliveryStat) + "',`assign_manager_nic`='" + SignIn.getEmployeeNIC() + "',`placed_date`='" + format.format(new Date()) + "',`customer_address_id`='" + this.customerAddressId + "' WHERE `delivery_id`='" + this.delvieryId + "' AND `orders_order_id`='" + orderId + "'");
                        Notifications.getInstance().show(Notifications.Type.SUCCESS, Notifications.Location.BOTTOM_RIGHT, "Delivery Updated Successfully");
                        clearDeliveryManageFields();

//                        ResultSet resultSet1 = MySQL.executeSearch("SELECT * FROM `employee` WHERE `NIC`='" + jTextField3.getText() + "'");
                        if (deliveryStat.equals("Delivered")) {

                            String content = "Mr/Mis " + resultSet.getString("customer.first_name") + " " + resultSet.getString("customer.last_name") + ", Your order is Successfully Delivered Order Id :'" + orderId + "' If there problem contact us : corevisiosoftware@gmail.com";

                            try {
                                SendEmail.sendEmail(resultSet.getString("customer.email"), "FruitShop Order Delivery Confirm", content);
                                JOptionPane.showMessageDialog(this, "Email send Successfuly", "Success", JOptionPane.INFORMATION_MESSAGE, JOptionIcon.SUCCESS.setICon());

                            } catch (Exception e) {
                                JOptionPane.showMessageDialog(this, "Mail Not Sent Something went wrong", "Error", JOptionPane.INFORMATION_MESSAGE, JOptionIcon.ERROR.setICon());
                                SignIn.getLoggerObject().log(Level.WARNING, "Transport Delivery Main Send Exception Found", e);

                            }
                            //                            System.out.println("mail send");
//                            int result = JOptionPane.showConfirmDialog(this, "Do you want to send the Mail to Customer", "Message", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, JOptionIcon.INFO.setICon());
//                            if (result == JOptionPane.YES_OPTION) {
//                                System.out.println("mail sent");
//                            } else {
//                                System.out.println("not sent");
//
//                            }
                        }
                    }

                } else {
                    Notifications.getInstance().show(Notifications.Type.WARNING, Notifications.Location.BOTTOM_RIGHT, "Something went wrong");
                    clearDeliveryManageFields();
                }

            }

        } catch (Exception e) {
            SignIn.getLoggerObject().log(Level.WARNING, "Delivery Update Exception Found", e);

        }
    }//GEN-LAST:event_jButton8ActionPerformed

    private void jButton6ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton6ActionPerformed
        String driverNic = jTextField2.getText();
        String employeeNic = jTextField3.getText();
        String deliveryStat = String.valueOf(jComboBox5.getSelectedItem());
        String orderId = jTextField5.getText();
        Date deliveryDate = jDateChooser1.getDate();
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");

        if (driverNic.isEmpty()) {
            //            JOptionPane.showMessageDialog(this, "Driver NIC cannot be empty", "Message", JOptionPane.OK_OPTION, JOptionIcon.WARNING.setICon());
            Notifications.getInstance().show(Notifications.Type.WARNING, Notifications.Location.BOTTOM_RIGHT, "Driver NIC cannot be empty");

        } else if (employeeNic.isEmpty()) {
            //            JOptionPane.showMessageDialog(this, "Employee NIC cannot be empty", "Message", JOptionPane.OK_OPTION, JOptionIcon.WARNING.setICon());
            Notifications.getInstance().show(Notifications.Type.WARNING, Notifications.Location.BOTTOM_RIGHT, "Employee NIC cannot be empty");

        } else if (deliveryStat.equals("Select")) {
            //            JOptionPane.showMessageDialog(this, "Please select delivery Status.", "Message", JOptionPane.OK_OPTION, JOptionIcon.WARNING.setICon());
            Notifications.getInstance().show(Notifications.Type.WARNING, Notifications.Location.BOTTOM_RIGHT, "Please select delivery Status.");

        } else if (orderId.isEmpty()) {
            //            JOptionPane.showMessageDialog(this, "Order ID cannot be empty", "Message", JOptionPane.OK_OPTION, JOptionIcon.WARNING.setICon());
            Notifications.getInstance().show(Notifications.Type.WARNING, Notifications.Location.BOTTOM_RIGHT, "Order ID cannot be empty");

        } else if (deliveryDate == null) {
            //            JOptionPane.showMessageDialog(this, "Delivery Date cannot be empty", "Message", JOptionPane.OK_OPTION, JOptionIcon.WARNING.setICon());
            Notifications.getInstance().show(Notifications.Type.WARNING, Notifications.Location.BOTTOM_RIGHT, "Delivery Date cannot be empty");

        } else if (customerAddressId.isEmpty()) {
            //            JOptionPane.showMessageDialog(this, "Please select cutomer address", "Message", JOptionPane.OK_OPTION, JOptionIcon.WARNING.setICon());
            Notifications.getInstance().show(Notifications.Type.WARNING, Notifications.Location.BOTTOM_RIGHT, "Please select cutomer address");

        } else {

            try {

                ResultSet resultSet = MySQL.executeSearch("SELECT * FROM `delivery` WHERE `orders_order_id`='" + orderId + "'");
                if (resultSet.next()) {
                    //                    JOptionPane.showMessageDialog(this, "This order is already added to delivery.", "Message", JOptionPane.OK_OPTION, JOptionIcon.WARNING.setICon());
                    Notifications.getInstance().show(Notifications.Type.WARNING, Notifications.Location.BOTTOM_RIGHT, "This order is already added to delivery");
                    clearDeliveryManageFields();
                } else {
                    MySQL.executeIUD("INSERT INTO `delivery`(`delivery_date`,`driver_NIC`,`assign_manager_nic`,`placed_date`,`delivery_status_id`,`orders_order_id`,`customer_address_id`) VALUES('" + format.format(deliveryDate) + "','" + driverNic + "','" + SignIn.getEmployeeNIC() + "','" + format.format(new Date()) + "','" + deliveryStatMap.get(deliveryStat) + "','" + orderId + "','" + customerAddressId + "')");
                    //Setting order status to on delivery
                    MySQL.executeIUD("UPDATE `orders` SET `order_status_id`='3' WHERE `order_id`='" + orderId + "'");
                    Notifications.getInstance().show(Notifications.Type.SUCCESS, Notifications.Location.BOTTOM_RIGHT, "Delivery Placed Successfully");
                    clearDeliveryManageFields();
                    loadDeliveryManage();

                }

            } catch (Exception e) {
                SignIn.getLoggerObject().log(Level.WARNING, "Delivery Manage Exception Found", e);

            }

        }

    }//GEN-LAST:event_jButton6ActionPerformed

    private void jButton14ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton14ActionPerformed
        this.customerAddressId = "";
        jButton13.setBackground(new Color(255, 153, 153));
        jButton14.setEnabled(false);
        jButton5.setEnabled(true);
    }//GEN-LAST:event_jButton14ActionPerformed

    private void jButton13ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton13ActionPerformed

        String orderId = jTextField5.getText();
        if (!orderId.isEmpty()) {
            try {
                ResultSet resultSet = MySQL.executeSearch("SELECT * FROM `orders` WHERE `order_id`='" + orderId + "'");
                if (resultSet.next()) {

                    String customerNic = resultSet.getString("customer_nic");
                    CustomerAdressView customerAdressView = new CustomerAdressView(db, true, customerNic);
                    customerAdressView.loadDeliveryLocation(customerAddressId);
                    customerAdressView.setVisible(true);

                }
            } catch (Exception e) {
                SignIn.getLoggerObject().log(Level.WARNING, "Location Set View Exception Found", e);
            }

        } else {
            JOptionPane.showMessageDialog(this, "Order Id cannot be empty", "Message", JOptionPane.OK_OPTION, JOptionIcon.INFO.setICon());
        }
    }//GEN-LAST:event_jButton13ActionPerformed

    private void jButton5ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton5ActionPerformed
        ViewOrderDialog viewOrderDialog = new ViewOrderDialog(db, true);
        viewOrderDialog.setVisible(true);
    }//GEN-LAST:event_jButton5ActionPerformed

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        EmployeeInformationDialog employeeInformationDialog = new EmployeeInformationDialog(db, true);
        employeeInformationDialog.setVisible(true);
    }//GEN-LAST:event_jButton2ActionPerformed

    private void jButton17ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton17ActionPerformed
        Date date = new Date();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        String reportDate = dateFormat.format(date);

        try {
            HashMap<String, Object> parameters = new HashMap<>();
            parameters.put("Parameter1", reportDate);
            parameters.put("Parameter2", SignIn.getEmployeeNIC());
            parameters.put("Parameter101", SendLogo.getFilePath());

            JRTableModelDataSource dataSource = new JRTableModelDataSource(jTable1.getModel());
            JasperPrint report = JasperFillManager.fillReport("src/lk/corevision/fruitshop/reports/transport/fruitShopAppDeliveries.jasper", parameters, dataSource);

            JasperViewer.viewReport(report, false);

        } catch (Exception e) {
            SignIn.getLoggerObject().log(Level.WARNING, "Delivery Report Exception Found", e);
        }
    }//GEN-LAST:event_jButton17ActionPerformed

    private void jTextField4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextField4ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jTextField4ActionPerformed

    private void jTextField4KeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTextField4KeyReleased
        loadDeliveryManage();
    }//GEN-LAST:event_jTextField4KeyReleased

    private void jButton4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton4ActionPerformed
        jTabbedPane2.setSelectedIndex(1);
    }//GEN-LAST:event_jButton4ActionPerformed

    private void jButton9ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton9ActionPerformed
        jTabbedPane2.setSelectedIndex(2);

    }//GEN-LAST:event_jButton9ActionPerformed

    private void jButton15ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton15ActionPerformed
        jTabbedPane2.setSelectedIndex(3);

    }//GEN-LAST:event_jButton15ActionPerformed

    private void jButton4MouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jButton4MouseEntered
        jButton4.setBackground(new Color(255, 153, 0));
    }//GEN-LAST:event_jButton4MouseEntered

    private void jButton9MouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jButton9MouseEntered
        jButton9.setBackground(new Color(255, 153, 0));

    }//GEN-LAST:event_jButton9MouseEntered

    private void jButton15MouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jButton15MouseEntered
        jButton15.setBackground(new Color(255, 153, 0));
    }//GEN-LAST:event_jButton15MouseEntered

    private void jButton4MouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jButton4MouseExited
        jButton4.setBackground(new Color(255, 255, 255));
    }//GEN-LAST:event_jButton4MouseExited

    private void jButton9MouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jButton9MouseExited
        jButton9.setBackground(new Color(255, 255, 255));
    }//GEN-LAST:event_jButton9MouseExited

    private void jButton15MouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jButton15MouseExited
        jButton15.setBackground(new Color(255, 255, 255));
    }//GEN-LAST:event_jButton15MouseExited

    private void clearDeliveryManageFields() {
        jTextField4.setText("");
        jTextField2.setText("");
        jComboBox5.setSelectedIndex(0);
        jTextField5.setText("");
        jDateChooser1.setDate(null);
        customerAddressId = "";
        jTextField3.setText(SignIn.getEmployeeNIC());
        jButton5.setEnabled(true);
        jButton13.setBackground(new Color(255, 153, 153));
        jButton14.setEnabled(false);
        jTable1.clearSelection();
        jButton8.setEnabled(false);
        jButton6.setEnabled(true);
        jButton5.setEnabled(true);
        loadDeliveryManage();
    }

    private void clearVehicleManageFields() {
        jTextField13.setText("");
        jComboBox1.setSelectedIndex(0);
        jComboBox2.setSelectedIndex(0);
        jTable2.clearSelection();
        loadVehicles();
    }

    private void setVehicleDetails(String licenseNo) {

//                jTextField1.setText(licenseNo);
        try {

            ResultSet resultSet = MySQL.executeSearch("SELECT * FROM `vehicle` INNER JOIN `vehicle_status` ON `vehicle`.`vehicle_status_id`=`vehicle_status`.`vehicle_status_id` INNER JOIN `vehicle_type` ON `vehicle`.`vehicle_type_id`=`vehicle_type`.`vehicle_type_id` WHERE `licens_plate`='" + licenseNo + "'");

            if (resultSet.next()) {

                jTextField1.setText(licenseNo);
                jComboBox3.setSelectedItem(resultSet.getString("vehicle_status.vehicle_status"));
                jComboBox4.setSelectedItem(resultSet.getString("vehicle_type.vehicle_type"));
                jFormattedTextField1.setText(resultSet.getString("vehicle.capacity"));
                jFormattedTextField2.setText(resultSet.getString("vehicle.mileage"));
                jTextField8.setText(resultSet.getString("vehicle.brand"));
                jTextField9.setText(resultSet.getString("vehicle.model"));
                jDateChooser7.setDate(resultSet.getDate("vehicle.insuarance_date"));
                jDateChooser5.setDate(resultSet.getDate("vehicle.year_of_manufacture"));
                jDateChooser6.setDate(resultSet.getDate("vehicle.purchase_date"));
                jButton7.setEnabled(false);
                jTextField1.setEditable(false);
                jButton10.setEnabled(true);
            }

        } catch (Exception e) {
            SignIn.getLoggerObject().log(Level.WARNING, "Vehicle Details Set Exception Found", e);
        }

    }

    private void loadVehicleStats() {
        try {
            ResultSet resultSet = MySQL.executeSearch("SELECT * FROM `vehicle_status`");
            Vector<String> vector = new Vector<>();
            Vector<String> vector1 = new Vector<>();
            vector.add("Select");
            vector1.add("Status");
            while (resultSet.next()) {
                vector.add(resultSet.getString("vehicle_status"));
                vector1.add(resultSet.getString("vehicle_status"));
                vehicleStatMap.put(resultSet.getString("vehicle_status"), resultSet.getString("vehicle_status_id"));
            }
            DefaultComboBoxModel model = new DefaultComboBoxModel(vector);
            jComboBox3.setModel(model);
            DefaultComboBoxModel model1 = new DefaultComboBoxModel(vector1);
            jComboBox2.setModel(model1);
        } catch (Exception e) {
            SignIn.getLoggerObject().log(Level.WARNING, "Load Vehicle Stat Exception Found", e);

        }
    }

    private void loadDeliveryStat() {
        try {
            ResultSet resultSet = MySQL.executeSearch("SELECT * FROM `delivery_status`");
            Vector<String> vector = new Vector<>();

            vector.add("Select");
            while (resultSet.next()) {
                vector.add(resultSet.getString("delivery_status"));
                deliveryStatMap.put(resultSet.getString("delivery_status"), resultSet.getString("delivery_status_id"));
            }
            DefaultComboBoxModel model = new DefaultComboBoxModel(vector);
            jComboBox5.setModel(model);
        } catch (Exception e) {
            SignIn.getLoggerObject().log(Level.WARNING, "Load Delivery Stat Exception Found", e);

        }
    }

    private void loadVehicleType() {
        try {
            ResultSet resultSet = MySQL.executeSearch("SELECT * FROM `vehicle_type`");
            Vector<String> vector = new Vector<>();
            Vector<String> vector1 = new Vector<>();

            vector.add("Select");
            vector1.add("Type");

            while (resultSet.next()) {
                vector.add(resultSet.getString("vehicle_type"));
                vector1.add(resultSet.getString("vehicle_type"));
                vehicleTypeMap.put(resultSet.getString("vehicle_type"), resultSet.getString("vehicle_type_id"));
            }
            DefaultComboBoxModel model = new DefaultComboBoxModel(vector);
            jComboBox4.setModel(model);
            DefaultComboBoxModel model1 = new DefaultComboBoxModel(vector1);
            jComboBox1.setModel(model1);
        } catch (Exception e) {
            SignIn.getLoggerObject().log(Level.WARNING, "Load Vehicle Type Exception Found", e);

        }
    }


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.ButtonGroup buttonGroup1;
    private javax.swing.ButtonGroup buttonGroup2;
    private lk.corevision.fruitshop.charts.CurveLineChart curveLineChart1;
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton10;
    private javax.swing.JButton jButton11;
    private javax.swing.JButton jButton12;
    private javax.swing.JButton jButton13;
    private javax.swing.JButton jButton14;
    private javax.swing.JButton jButton15;
    private javax.swing.JButton jButton16;
    private javax.swing.JButton jButton17;
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jButton3;
    private javax.swing.JButton jButton4;
    private javax.swing.JButton jButton5;
    private javax.swing.JButton jButton6;
    private javax.swing.JButton jButton7;
    private javax.swing.JButton jButton8;
    private javax.swing.JButton jButton9;
    private javax.swing.JComboBox<String> jComboBox1;
    private javax.swing.JComboBox<String> jComboBox2;
    private javax.swing.JComboBox<String> jComboBox3;
    private javax.swing.JComboBox<String> jComboBox4;
    private javax.swing.JComboBox<String> jComboBox5;
    private com.toedter.calendar.JDateChooser jDateChooser1;
    private com.toedter.calendar.JDateChooser jDateChooser5;
    private com.toedter.calendar.JDateChooser jDateChooser6;
    private com.toedter.calendar.JDateChooser jDateChooser7;
    private javax.swing.JFormattedTextField jFormattedTextField1;
    private javax.swing.JFormattedTextField jFormattedTextField2;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel15;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel20;
    private javax.swing.JLabel jLabel21;
    private javax.swing.JLabel jLabel22;
    private javax.swing.JLabel jLabel23;
    private javax.swing.JLabel jLabel24;
    private javax.swing.JLabel jLabel25;
    private javax.swing.JLabel jLabel26;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JMenuItem jMenuItem1;
    private javax.swing.JMenuItem jMenuItem2;
    private javax.swing.JMenuItem jMenuItem3;
    private com.toedter.calendar.JMonthChooser jMonthChooser1;
    private javax.swing.JPanel jPanel10;
    private javax.swing.JPanel jPanel11;
    private javax.swing.JPanel jPanel12;
    private javax.swing.JPanel jPanel13;
    private javax.swing.JPanel jPanel14;
    private javax.swing.JPanel jPanel15;
    private javax.swing.JPanel jPanel16;
    private javax.swing.JPanel jPanel17;
    private javax.swing.JPanel jPanel18;
    private javax.swing.JPanel jPanel19;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel20;
    private javax.swing.JPanel jPanel21;
    private javax.swing.JPanel jPanel22;
    private javax.swing.JPanel jPanel23;
    private javax.swing.JPanel jPanel24;
    private javax.swing.JPanel jPanel26;
    private javax.swing.JPanel jPanel27;
    private javax.swing.JPanel jPanel28;
    private javax.swing.JPanel jPanel29;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel33;
    private javax.swing.JPanel jPanel34;
    private javax.swing.JPanel jPanel37;
    private javax.swing.JPanel jPanel38;
    private javax.swing.JPanel jPanel39;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel40;
    private javax.swing.JPanel jPanel41;
    private javax.swing.JPanel jPanel42;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JPanel jPanel7;
    private javax.swing.JPanel jPanel8;
    private javax.swing.JPanel jPanel9;
    private javax.swing.JPopupMenu jPopupMenu1;
    private javax.swing.JPopupMenu jPopupMenu2;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JTabbedPane jTabbedPane2;
    private javax.swing.JTable jTable1;
    private javax.swing.JTable jTable2;
    private javax.swing.JTextField jTextField1;
    private javax.swing.JTextField jTextField13;
    private javax.swing.JTextField jTextField2;
    private javax.swing.JTextField jTextField3;
    private javax.swing.JTextField jTextField4;
    private javax.swing.JTextField jTextField5;
    private javax.swing.JTextField jTextField8;
    private javax.swing.JTextField jTextField9;
    // End of variables declaration//GEN-END:variables
}
