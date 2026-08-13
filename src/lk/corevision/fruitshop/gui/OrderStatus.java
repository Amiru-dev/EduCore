package lk.corevision.fruitshop.gui;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.sql.ResultSet;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.Level;
import java.util.HashMap;
import java.util.Vector;
import java.util.logging.Level;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;
import lk.corevision.fruitshop.email.SendEmail;
import lk.corevision.fruitshop.email.SendFileEmail;
import lk.corevision.fruitshop.model.MySQL;
import lk.corevision.fruitshop.model.SendLogo;
import lk.corevision.fruitshop.validate.DataValidator;
import lk.corevision.fruitshop.validate.JOptionIcon;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.data.JRTableModelDataSource;
import net.sf.jasperreports.export.SimpleExporterInput;
import net.sf.jasperreports.export.SimpleOutputStreamExporterOutput;
import net.sf.jasperreports.pdf.JRPdfExporter;
import net.sf.jasperreports.view.JasperViewer;

public class OrderStatus extends javax.swing.JDialog {

    private HashMap<String, String> orderStatusMap = new HashMap<>();
    private HashMap<String, String> paymentMethodMap = new HashMap<>();
    private String customerEmail;

    public void setorderStatusOrderId(String id) {
        orderStatusOrderId.setText(id);
        loadOrderStatus();
        loadPaymentMethod();
        loadOrder();
    }

    public OrderStatus(java.awt.Frame parent, boolean modal) {
        super(parent, modal);
        initComponents();

    }

    private double total;
    private double balance;

    private void loadOrder() {
        try {
            ResultSet resultSet = MySQL.executeSearch("SELECT `fruit_name`,"
                    + "(order_item.`qty`*(packing.`price`- order_item.`price`)) AS `total_discount`,"
                    + "`payment_method`.`payment_method`,`paid_amount`,orders.`payment_status_id`,`order_status`,`payment_status`,`fruit_id`,`order_id`,employee.`first_name` AS `efname`,"
                    + "customer.`first_name` AS `cfname`,customer.`email` AS `cemail`,`packing_type`,orders.`date` AS `odate`,packing.`date` AS `pdate`,"
                    + "order_item.`price` AS `discount_price`,order_item.`qty`,packing.`price` AS `unit_price`,`packing_id`,`payment_date` "
                    + "FROM `fruit` INNER JOIN  `plantation` ON `fruit`.`fruit_id`=`plantation`.`fruit_fruit_id` "
                    + "INNER JOIN `inventory` ON `inventory`.`plantation_id`=`plantation`.`plantation_id` "
                    + "INNER JOIN `product_quality` ON `product_quality`.`inventory_inventory_id`=`inventory`.`inventory_id` "
                    + "INNER JOIN `packing` ON `packing`.`product_quality_id`=`product_quality`.`product_quality_id` "
                    + "INNER JOIN order_item ON order_item.packing_packing_id=packing.packing_id "
                    + "INNER JOIN orders ON orders.order_id=order_item.orders_order_id "
                    + "INNER JOIN employee ON employee.NIC=orders.employee_NIC "
                    + "INNER JOIN customer ON  customer.customer_nic=orders.customer_nic "
                    + "INNER JOIN packing_type ON packing.packing_type_id=packing_type.packing_type_id "
                    + "INNER JOIN `order_status` ON `order_status`.`order_status_id`=`orders`.`order_status_id` "
                    + "INNER JOIN `payment_status` ON `payment_status`.`payment_status_id`=`orders`.`payment_status_id` "
                    + " LEFT JOIN `payment_method` ON `payment_method`.`payment_method_id`=`orders`.`payment_method_id`"
                    + "WHERE `order_id`='" + orderStatusOrderId.getText() + "'");

            DefaultTableModel model = (DefaultTableModel) orderStatusTable.getModel();
            model.setRowCount(0);

            while (resultSet.next()) {
                Vector<String> vector = new Vector<>();
                vector.add(resultSet.getString("packing_id"));
                vector.add(resultSet.getString("fruit_id"));
                vector.add(resultSet.getString("fruit_name"));
                vector.add(resultSet.getString("packing_type.packing_type"));
                vector.add(resultSet.getString("pdate"));
                vector.add(resultSet.getString("unit_price"));
                vector.add(resultSet.getString("discount_price"));
                vector.add(resultSet.getString("order_item.qty"));
                double itemTotal = Double.parseDouble(resultSet.getString("order_item.qty")) * Double.parseDouble(resultSet.getString("discount_price"));
                total += itemTotal;
                balance = total;
                vector.add(String.valueOf(itemTotal));
                model.addRow(vector);
                orderStatusEmployeeName.setText(resultSet.getString("efname"));
                orderStatusCustomerName.setText(resultSet.getString("cfname"));
                customerEmail = resultSet.getString("cemail");
                orderStatusComboBox.setSelectedItem(resultSet.getString("order_status"));
                orderStatusField.setText(resultSet.getString("order_status"));
                orderStatusPaymentStatus.setText(resultSet.getString("payment_status"));
                orderStatusOrderDate.setText(resultSet.getString("odate"));
                OrderStatusTotalDiscountField.setText(resultSet.getString("total_discount"));
                if (resultSet.getString("payment_status_id").equals("1")) {
                    orderStatusPaymentField.setEditable(false);
                    orderPaymentMethodComboBox.setEnabled(false);
                    orderStatusPaymentField.setText(String.valueOf(resultSet.getDouble("paid_amount")));
                    balance = total - resultSet.getDouble("paid_amount");
                    orderPaymentMethodComboBox.setSelectedItem(resultSet.getString("payment_method"));
                    orderStatusPaymentDate.setText(resultSet.getString("payment_date"));
                }
            }

            OrderStatusTotal.setText(String.valueOf(total));
            orderStatusBalance.setText(String.valueOf(balance));
            total = 0;

        } catch (Exception e) {
            SignIn.getLoggerObject().log(Level.WARNING, "order loading Exception found!", e);
        }
    }

    private void loadOrderStatus() {
        try {
            ResultSet resultSet = MySQL.executeSearch("SELECT * FROM `order_status`");

            Vector<String> vector = new Vector<>();

            while (resultSet.next()) {
                vector.add(resultSet.getString("order_status"));
                orderStatusMap.put(resultSet.getString("order_status"), resultSet.getString("order_status_id"));

            }
            DefaultComboBoxModel model = new DefaultComboBoxModel(vector);
            orderStatusComboBox.setModel(model);
        } catch (Exception e) {
            SignIn.getLoggerObject().log(Level.WARNING, "order status loading Exception found!", e);
        }
    }

    private void loadPaymentMethod() {
        try {
            ResultSet resultSet = MySQL.executeSearch("SELECT * FROM `payment_method`");

            Vector<String> vector = new Vector<>();

            while (resultSet.next()) {
                vector.add(resultSet.getString("payment_method"));
                paymentMethodMap.put(resultSet.getString("payment_method"), resultSet.getString("payment_method_id"));

            }
            DefaultComboBoxModel model = new DefaultComboBoxModel(vector);
            orderPaymentMethodComboBox.setModel(model);
        } catch (Exception e) {
            SignIn.getLoggerObject().log(Level.WARNING, "order status loading Exception found!", e);
        }
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        jLabel40 = new javax.swing.JLabel();
        jLabel41 = new javax.swing.JLabel();
        orderStatusOrderId = new javax.swing.JTextField();
        jLabel42 = new javax.swing.JLabel();
        orderStatusComboBox = new javax.swing.JComboBox<>();
        jButton1 = new javax.swing.JButton();
        orderStatusEmployeeName = new javax.swing.JTextField();
        orderStatusCustomerName = new javax.swing.JTextField();
        jLabel47 = new javax.swing.JLabel();
        jLabel48 = new javax.swing.JLabel();
        jLabel50 = new javax.swing.JLabel();
        orderStatusField = new javax.swing.JTextField();
        orderStatusPaymentStatusComboBox = new javax.swing.JPanel();
        jLabel43 = new javax.swing.JLabel();
        orderPaymentMethodComboBox = new javax.swing.JComboBox<>();
        orderStatusUpdateButton = new javax.swing.JButton();
        orderStatusPaymentField = new javax.swing.JFormattedTextField();
        jLabel44 = new javax.swing.JLabel();
        jLabel49 = new javax.swing.JLabel();
        orderStatusPaymentStatus = new javax.swing.JTextField();
        orderStatusClearButton = new javax.swing.JButton();
        jLabel51 = new javax.swing.JLabel();
        orderStatusPaymentDate = new javax.swing.JTextField();
        orderStatusOrderDate = new javax.swing.JTextField();
        jLabel53 = new javax.swing.JLabel();
        jPanel3 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        orderStatusTable = new javax.swing.JTable();
        jPanel4 = new javax.swing.JPanel();
        jPanel2 = new javax.swing.JPanel();
        OrderStatusTotal = new javax.swing.JFormattedTextField();
        OrderStatusTotalDiscountField = new javax.swing.JFormattedTextField();
        orderStatusBalance = new javax.swing.JFormattedTextField();
        jPanel6 = new javax.swing.JPanel();
        jButton3 = new javax.swing.JButton();
        jButton8 = new javax.swing.JButton();
        jPanel5 = new javax.swing.JPanel();
        jLabel46 = new javax.swing.JLabel();
        jLabel52 = new javax.swing.JLabel();
        jLabel45 = new javax.swing.JLabel();
        jLabel1 = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);

        jPanel1.setBackground(new java.awt.Color(255, 255, 255));

        jLabel40.setFont(new java.awt.Font("Serif", 1, 24)); // NOI18N
        jLabel40.setText("Order And Payment Status");

        jLabel41.setFont(new java.awt.Font("Serif", 0, 12)); // NOI18N
        jLabel41.setText("Order Id");

        orderStatusOrderId.setEditable(false);
        orderStatusOrderId.setFont(new java.awt.Font("Serif", 0, 12)); // NOI18N

        jLabel42.setFont(new java.awt.Font("Serif", 0, 12)); // NOI18N
        jLabel42.setText("Order Status");

        orderStatusComboBox.setFont(new java.awt.Font("Serif", 0, 12)); // NOI18N

        jButton1.setFont(new java.awt.Font("Serif", 1, 14)); // NOI18N
        jButton1.setText("Update");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        orderStatusEmployeeName.setEditable(false);
        orderStatusEmployeeName.setFont(new java.awt.Font("Serif", 0, 12)); // NOI18N
        orderStatusEmployeeName.setText("Employee Name Here");

        orderStatusCustomerName.setEditable(false);
        orderStatusCustomerName.setFont(new java.awt.Font("Serif", 0, 12)); // NOI18N
        orderStatusCustomerName.setText("Customer Name Here");

        jLabel47.setFont(new java.awt.Font("Serif", 0, 12)); // NOI18N
        jLabel47.setText("Employee:");

        jLabel48.setFont(new java.awt.Font("Serif", 0, 12)); // NOI18N
        jLabel48.setText("Customer");

        jLabel50.setFont(new java.awt.Font("Serif", 0, 12)); // NOI18N
        jLabel50.setText("Order Status");

        orderStatusField.setEditable(false);
        orderStatusField.setFont(new java.awt.Font("Serif", 0, 12)); // NOI18N

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jLabel41)
                        .addGap(18, 18, 18)
                        .addComponent(orderStatusOrderId, javax.swing.GroupLayout.PREFERRED_SIZE, 251, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jLabel40)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jLabel47)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(orderStatusEmployeeName, javax.swing.GroupLayout.PREFERRED_SIZE, 251, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jLabel42)
                    .addComponent(jLabel48))
                .addGap(18, 18, 18)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(orderStatusComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, 185, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 194, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(125, 125, 125))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(orderStatusCustomerName, javax.swing.GroupLayout.PREFERRED_SIZE, 269, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(jLabel50)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(orderStatusField)))
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel40)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabel50)
                        .addComponent(orderStatusField, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabel47)
                        .addComponent(orderStatusEmployeeName, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jLabel48)
                        .addComponent(orderStatusCustomerName, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 23, Short.MAX_VALUE)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(orderStatusOrderId, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel41)
                    .addComponent(jLabel42)
                    .addComponent(orderStatusComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(15, 15, 15))
        );

        orderStatusPaymentStatusComboBox.setBackground(new java.awt.Color(255, 255, 255));

        jLabel43.setFont(new java.awt.Font("Serif", 0, 12)); // NOI18N
        jLabel43.setText("Payment");

        orderPaymentMethodComboBox.setFont(new java.awt.Font("Serif", 0, 12)); // NOI18N

        orderStatusUpdateButton.setFont(new java.awt.Font("Serif", 1, 14)); // NOI18N
        orderStatusUpdateButton.setText("Update");
        orderStatusUpdateButton.setEnabled(false);
        orderStatusUpdateButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                orderStatusUpdateButtonActionPerformed(evt);
            }
        });

        orderStatusPaymentField.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.NumberFormatter(new java.text.DecimalFormat("#0.00"))));
        orderStatusPaymentField.setFont(new java.awt.Font("Serif", 0, 12)); // NOI18N
        orderStatusPaymentField.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                orderStatusPaymentFieldKeyReleased(evt);
            }
        });

        jLabel44.setFont(new java.awt.Font("Serif", 0, 12)); // NOI18N
        jLabel44.setText("Payment method");

        jLabel49.setFont(new java.awt.Font("Serif", 0, 12)); // NOI18N
        jLabel49.setText("Payment Status");

        orderStatusPaymentStatus.setEditable(false);
        orderStatusPaymentStatus.setFont(new java.awt.Font("Serif", 0, 12)); // NOI18N
        orderStatusPaymentStatus.setForeground(new java.awt.Color(255, 0, 0));

        orderStatusClearButton.setFont(new java.awt.Font("Serif", 1, 14)); // NOI18N
        orderStatusClearButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/resources/clearAll.png"))); // NOI18N
        orderStatusClearButton.setText("Clear");
        orderStatusClearButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                orderStatusClearButtonActionPerformed(evt);
            }
        });

        jLabel51.setFont(new java.awt.Font("Serif", 0, 12)); // NOI18N
        jLabel51.setText("Payment Date");

        orderStatusPaymentDate.setEditable(false);
        orderStatusPaymentDate.setFont(new java.awt.Font("Serif", 0, 12)); // NOI18N
        orderStatusPaymentDate.setForeground(new java.awt.Color(51, 51, 51));

        orderStatusOrderDate.setEditable(false);
        orderStatusOrderDate.setFont(new java.awt.Font("Serif", 0, 12)); // NOI18N
        orderStatusOrderDate.setForeground(new java.awt.Color(51, 51, 51));

        jLabel53.setFont(new java.awt.Font("Serif", 0, 12)); // NOI18N
        jLabel53.setText("Order Date");

        javax.swing.GroupLayout orderStatusPaymentStatusComboBoxLayout = new javax.swing.GroupLayout(orderStatusPaymentStatusComboBox);
        orderStatusPaymentStatusComboBox.setLayout(orderStatusPaymentStatusComboBoxLayout);
        orderStatusPaymentStatusComboBoxLayout.setHorizontalGroup(
            orderStatusPaymentStatusComboBoxLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(orderStatusPaymentStatusComboBoxLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(orderStatusPaymentStatusComboBoxLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel53)
                    .addComponent(jLabel43))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(orderStatusPaymentStatusComboBoxLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(orderStatusOrderDate, javax.swing.GroupLayout.DEFAULT_SIZE, 216, Short.MAX_VALUE)
                    .addComponent(orderStatusPaymentField))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(orderStatusPaymentStatusComboBoxLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel44, javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jLabel51, javax.swing.GroupLayout.Alignment.TRAILING))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(orderStatusPaymentStatusComboBoxLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(orderPaymentMethodComboBox, 0, 183, Short.MAX_VALUE)
                    .addComponent(orderStatusPaymentDate))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(orderStatusPaymentStatusComboBoxLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(orderStatusClearButton, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(orderStatusPaymentStatusComboBoxLayout.createSequentialGroup()
                        .addComponent(orderStatusUpdateButton)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jLabel49)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(orderStatusPaymentStatus)))
                .addContainerGap())
        );
        orderStatusPaymentStatusComboBoxLayout.setVerticalGroup(
            orderStatusPaymentStatusComboBoxLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(orderStatusPaymentStatusComboBoxLayout.createSequentialGroup()
                .addGap(11, 11, 11)
                .addGroup(orderStatusPaymentStatusComboBoxLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel43)
                    .addComponent(orderPaymentMethodComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(orderStatusUpdateButton, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(orderStatusPaymentField, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel44)
                    .addComponent(jLabel49)
                    .addComponent(orderStatusPaymentStatus, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(orderStatusPaymentStatusComboBoxLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(orderStatusPaymentStatusComboBoxLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(orderStatusOrderDate, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jLabel53))
                    .addGroup(orderStatusPaymentStatusComboBoxLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(orderStatusClearButton, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(orderStatusPaymentDate, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jLabel51)))
                .addContainerGap(16, Short.MAX_VALUE))
        );

        jPanel3.setBackground(new java.awt.Color(255, 255, 255));

        orderStatusTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Packing ID", "Fruit Id", "Fruit Name", "packing type", "MFD", "Selling Price", "Discount Price", "Qty", "Total"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        orderStatusTable.getTableHeader().setReorderingAllowed(false);
        jScrollPane1.setViewportView(orderStatusTable);

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane1)
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel3Layout.createSequentialGroup()
                .addGap(0, 0, Short.MAX_VALUE)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 256, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        jPanel4.setBackground(new java.awt.Color(255, 255, 255));

        jPanel2.setBackground(new java.awt.Color(255, 255, 255));
        jPanel2.setLayout(new java.awt.GridLayout(4, 1, 0, 4));

        OrderStatusTotal.setEditable(false);
        OrderStatusTotal.setFont(new java.awt.Font("Serif", 0, 12)); // NOI18N
        jPanel2.add(OrderStatusTotal);

        OrderStatusTotalDiscountField.setEditable(false);
        OrderStatusTotalDiscountField.setFont(new java.awt.Font("Serif", 0, 12)); // NOI18N
        jPanel2.add(OrderStatusTotalDiscountField);

        orderStatusBalance.setEditable(false);
        orderStatusBalance.setFont(new java.awt.Font("Serif", 0, 12)); // NOI18N
        jPanel2.add(orderStatusBalance);

        jPanel6.setBackground(new java.awt.Color(255, 255, 255));
        jPanel6.setLayout(new java.awt.BorderLayout(2, 0));

        jButton3.setFont(new java.awt.Font("Serif", 1, 14)); // NOI18N
        jButton3.setText("Email");
        jButton3.setFocusable(false);
        jButton3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton3ActionPerformed(evt);
            }
        });
        jPanel6.add(jButton3, java.awt.BorderLayout.EAST);

        jButton8.setFont(new java.awt.Font("Serif", 1, 12)); // NOI18N
        jButton8.setIcon(new javax.swing.ImageIcon(getClass().getResource("/resources/PrintIcon.png"))); // NOI18N
        jButton8.setText("Print");
        jButton8.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton8ActionPerformed(evt);
            }
        });
        jPanel6.add(jButton8, java.awt.BorderLayout.CENTER);

        jPanel2.add(jPanel6);

        jPanel5.setBackground(new java.awt.Color(255, 255, 255));
        jPanel5.setLayout(new java.awt.GridLayout(4, 1, 0, 4));

        jLabel46.setFont(new java.awt.Font("Serif", 0, 12)); // NOI18N
        jLabel46.setText("Total");
        jPanel5.add(jLabel46);

        jLabel52.setFont(new java.awt.Font("Serif", 0, 12)); // NOI18N
        jLabel52.setText("Total Discount");
        jPanel5.add(jLabel52);

        jLabel45.setFont(new java.awt.Font("Serif", 0, 12)); // NOI18N
        jLabel45.setText("Balance");
        jPanel5.add(jLabel45);

        jLabel1.setText(" ");
        jPanel5.add(jLabel1);

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jPanel5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, 212, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, 149, Short.MAX_VALUE)
                    .addComponent(jPanel5, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(jPanel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(orderStatusPaymentStatusComboBox, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(orderStatusPaymentStatusComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void orderStatusPaymentFieldKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_orderStatusPaymentFieldKeyReleased
        String payment = orderStatusPaymentField.getText();
        String total = OrderStatusTotal.getText();

        if (payment.isEmpty()) {
            payment = "0.00";
        }
        if (DataValidator.isInvalidPrice(payment)) {
            JOptionPane.showMessageDialog(this, "Please Enter valid price", "Warning", JOptionPane.WARNING_MESSAGE);
            orderStatusPaymentField.setText("");
        } else {
            double balance = Double.parseDouble(total) - Double.parseDouble(payment);
            orderStatusBalance.setText(String.valueOf(balance));
            if (balance <= 0.00) {
                orderStatusUpdateButton.setEnabled(true);
            } else {
                orderStatusUpdateButton.setEnabled(false);
            }
        }
    }//GEN-LAST:event_orderStatusPaymentFieldKeyReleased

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        try {
            MySQL.executeIUD("UPDATE `orders` SET `order_status_id`='" + orderStatusMap.get(orderStatusComboBox.getSelectedItem()) + "' WHERE `order_id`='" + orderStatusOrderId.getText() + "'");
            JOptionPane.showMessageDialog(this, "Successfully Updated Status in to:" + orderStatusComboBox.getSelectedItem(), "Info", JOptionPane.INFORMATION_MESSAGE);
        } catch (Exception e) {
            SignIn.getLoggerObject().log(Level.WARNING, "order status update Exception found!", e);
        }
    }//GEN-LAST:event_jButton1ActionPerformed

    private void orderStatusUpdateButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_orderStatusUpdateButtonActionPerformed
        try {
            String date = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
            MySQL.executeIUD("UPDATE `orders` SET `payment_method_id`='" + paymentMethodMap.get(orderPaymentMethodComboBox.getSelectedItem()) + "',"
                    + "`payment_status_id`='1',`paid_amount`='" + orderStatusPaymentField.getText() + "',`payment_date`='" + date + "' "
                    + " WHERE `order_id`='" + orderStatusOrderId.getText() + "'");
            JOptionPane.showMessageDialog(this, "Successfully Updated Payment:", "Info", JOptionPane.INFORMATION_MESSAGE);
            loadOrder();
            loadInvoiceReport(false);
        } catch (Exception e) {
            SignIn.getLoggerObject().log(Level.WARNING, "order status update Exception found!", e);
        }

    }//GEN-LAST:event_orderStatusUpdateButtonActionPerformed

    private void orderStatusClearButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_orderStatusClearButtonActionPerformed
        reset();
    }//GEN-LAST:event_orderStatusClearButtonActionPerformed

    private void jButton8ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton8ActionPerformed
        loadInvoiceReport(true);
    }//GEN-LAST:event_jButton8ActionPerformed

    private void jButton3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton3ActionPerformed
        loadInvoiceReport(false);

    }//GEN-LAST:event_jButton3ActionPerformed
    private void loadInvoiceReport(boolean viewReport) {
        try {
            InputStream path = this.getClass().getResourceAsStream("/lk/corevision/fruitshop/reports/sales/fruitShopAppInvoice.jasper");

            HashMap<String, Object> params = new HashMap<>();
            params.put("Parameter1", orderStatusOrderId.getText());
            params.put("Parameter2", orderStatusEmployeeName.getText());
            params.put("Parameter3", orderStatusCustomerName.getText());
            params.put("Parameter4", orderStatusOrderDate.getText());
            params.put("Parameter5", orderStatusPaymentDate.getText());
            params.put("Parameter6", OrderStatusTotal.getText());
            params.put("Parameter101", SendLogo.getFilePath());
            params.put("Parameter102", SendLogo.getFilePath());

            String PaymentMethod = "Not Paid";
            String PaymentStatus = "Not Paid";
            if (orderStatusPaymentStatus.getText().equals("Paid")) {
                PaymentMethod = String.valueOf(orderPaymentMethodComboBox.getSelectedItem());
                PaymentStatus = orderStatusPaymentField.getText();
            }
            params.put("Parameter8", PaymentMethod);
            params.put("Parameter9", PaymentStatus);

            params.put("Parameter7", OrderStatusTotalDiscountField.getText());

            params.put("Parameter10", orderStatusBalance.getText());

            JRTableModelDataSource dataSource = new JRTableModelDataSource(orderStatusTable.getModel());
            JasperPrint jasperPrint = JasperFillManager.fillReport(path, params, dataSource);

            if (viewReport) {
                this.dispose();
                JasperViewer.viewReport(jasperPrint, false);
            } else {
                String htmlContent
                        = "<html>"
                        + "<body style='font-family: Arial, sans-serif; background-color: #f5f5f5; padding: 20px;'>"
                        + "<div style='max-width: 600px; margin: auto; background-color: #ffffff; padding: 25px; border-radius: 10px; box-shadow: 0 2px 6px rgba(0,0,0,0.1);'>"
                        + "<h2 style='color: #4CAF50;'>Dear " + orderStatusCustomerName.getText() + ",</h2>"
                        + "<p style='font-size: 16px;'>Thank you for shopping with <b>Fruit Shop</b>.</p>"
                        + "<p style='font-size: 15px;'>Your requested invoice (<b>" + orderStatusOrderId.getText() + "</b>) is attached below as a PDF document.</p>"
                        + "<p style='font-size: 15px;'>Please review the details and feel free to contact us if you have any concerns or questions.</p>"
                        // Order Summary Table Start
                        + "<h3 style='color: #333;'>Order Summary</h3>"
                        + "<table style='width: 100%; border-collapse: collapse; font-size: 14px;'>"
                        + "<tr><td style='padding: 8px; border-bottom: 1px solid #ddd;'>Order ID</td><td style='padding: 8px; border-bottom: 1px solid #ddd;'>" + orderStatusOrderId.getText() + "</td></tr>"
                        + "<tr><td style='padding: 8px; border-bottom: 1px solid #ddd;'>Order Status</td><td style='padding: 8px; border-bottom: 1px solid #ddd;'>" + String.valueOf(orderStatusComboBox.getSelectedItem()) + "</td></tr>"
                        + "<tr><td style='padding: 8px; border-bottom: 1px solid #ddd;'>Payment Status</td><td style='padding: 8px; border-bottom: 1px solid #ddd;'>" + orderStatusPaymentStatus.getText() + "</td></tr>"
                        + "<tr><td style='padding: 8px; border-bottom: 1px solid #ddd;'>Order Date</td><td style='padding: 8px; border-bottom: 1px solid #ddd;'>" + orderStatusOrderDate.getText() + "</td></tr>"
                        + "<tr><td style='padding: 8px; border-bottom: 1px solid #ddd;'>Sub Total</td><td style='padding: 8px; border-bottom: 1px solid #ddd;'>Rs. " + OrderStatusTotal.getText() + "</td></tr>"
                        + "<tr><td style='padding: 8px; border-bottom: 1px solid #ddd;'>Discount</td><td style='padding: 8px; border-bottom: 1px solid #ddd;'>Rs. " + OrderStatusTotalDiscountField.getText() + "</td></tr>"
                        + "<tr><td style='padding: 8px; border-bottom: 1px solid #ddd;'>Payment</td><td style='padding: 8px; border-bottom: 1px solid #ddd;'>Rs. " + orderStatusPaymentField.getText() + "</td></tr>"
                        + "<tr><td style='padding: 8px;'>Balance</td><td style='padding: 8px;'>Rs. " + orderStatusBalance.getText() + "</td></tr>"
                        + "<tr><td style='padding: 8px;'>Payment Method</td><td style='padding: 8px;'>" + PaymentMethod + "</td></tr>"
                        + "</table>"
                        + "<br><p style='font-size: 15px;'>ආයෙත් ඔබව අපගේ වෙළඳසැලට පිළිගන්න අපි සූදානම්!</p>"
                        + "<hr style='border:none; border-top:1px solid #ddd;'>"
                        + "<p style='font-size:12px; color:gray; text-align: center;'>This is an automated message from <b>Fruit Shop - Order Management System</b>.</p>"
                        + "<p style='font-size:12px; color:gray; text-align: center;'>Powered By <b><a href='https://github.com/CoreVision-Software' style='color: inherit; text-decoration: none;'>CoreVision Software Solution</a></b>.</p>"
                        + "</div>"
                        + "</body>"
                        + "</html>";

                boolean status = SendFileEmail.sendEmail(customerEmail, "FruitShop Order:" + orderStatusOrderId.getText(), htmlContent, jasperPrint, orderStatusOrderId.getText());
                if (status) {
                    JOptionPane.showMessageDialog(this, "Email send Successfuly", "Success", JOptionPane.INFORMATION_MESSAGE, JOptionIcon.SUCCESS.setICon());
                }
            }

        } catch (Exception e) {
            SignIn.getLoggerObject().log(Level.WARNING, "loadInvoice Exception Found!!", e);
        }
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(OrderStatus.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(OrderStatus.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(OrderStatus.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(OrderStatus.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the dialog */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                OrderStatus dialog = new OrderStatus(new javax.swing.JFrame(), true);
                dialog.addWindowListener(new java.awt.event.WindowAdapter() {
                    @Override
                    public void windowClosing(java.awt.event.WindowEvent e) {
                        System.exit(0);
                    }
                });
                dialog.setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JFormattedTextField OrderStatusTotal;
    private javax.swing.JFormattedTextField OrderStatusTotalDiscountField;
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton3;
    private javax.swing.JButton jButton8;
    private javax.swing.JLabel jLabel1;
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
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JComboBox<String> orderPaymentMethodComboBox;
    private javax.swing.JFormattedTextField orderStatusBalance;
    private javax.swing.JButton orderStatusClearButton;
    private javax.swing.JComboBox<String> orderStatusComboBox;
    private javax.swing.JTextField orderStatusCustomerName;
    private javax.swing.JTextField orderStatusEmployeeName;
    private javax.swing.JTextField orderStatusField;
    private javax.swing.JTextField orderStatusOrderDate;
    private javax.swing.JTextField orderStatusOrderId;
    private javax.swing.JTextField orderStatusPaymentDate;
    private javax.swing.JFormattedTextField orderStatusPaymentField;
    private javax.swing.JTextField orderStatusPaymentStatus;
    private javax.swing.JPanel orderStatusPaymentStatusComboBox;
    private javax.swing.JTable orderStatusTable;
    private javax.swing.JButton orderStatusUpdateButton;
    // End of variables declaration//GEN-END:variables

    private void reset() {
        orderStatusPaymentField.setText("");
        orderPaymentMethodComboBox.setSelectedIndex(0);
        orderStatusTable.clearSelection();
        loadOrder();
        orderStatusUpdateButton.setEnabled(false);
        orderStatusPaymentField.grabFocus();
    }
}
