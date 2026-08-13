package lk.corevision.fruitshop.gui;

import java.io.InputStream;
import java.sql.ResultSet;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Vector;
import java.util.logging.Level;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;
import javax.swing.table.DefaultTableModel;
import lk.corevision.fruitshop.email.SendFileEmail;
import lk.corevision.fruitshop.model.MySQL;
import lk.corevision.fruitshop.model.SendLogo;
import lk.corevision.fruitshop.validate.JOptionIcon;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.data.JRTableModelDataSource;
import net.sf.jasperreports.view.JasperViewer;
import raven.toast.Notifications;

public class ViewOrders extends javax.swing.JPanel {

    /**
     * Creates new form ViewOrders
     */
    private Dashboard dashboard;
    private Transport transport;

    public void setDashboard(Dashboard dashboard) {
        this.dashboard = dashboard;
    }

    public void setTransport(Transport transport) {
        this.transport = transport;
    }

    public ViewOrders() {
        initComponents();
        loadOrderStatus();
        loadPaymentStatus();
    }
// view Orders

    private void loadOrderStatus() {

        try {
            ResultSet resultSet = MySQL.executeSearch("SELECT * FROM `order_status`");

            Vector<String> vector = new Vector<>();

            vector.add("Select");
            while (resultSet.next()) {
                vector.add(resultSet.getString("order_status"));

            }
            DefaultComboBoxModel model = new DefaultComboBoxModel(vector);
            viewOrdersStatusComboBox.setModel(model);
        } catch (Exception e) {
            SignIn.getLoggerObject().log(Level.WARNING, "order status loading Exception found!", e);
        }
    }

    private void loadPaymentStatus() {

        try {
            ResultSet resultSet = MySQL.executeSearch("SELECT * FROM `payment_status`");

            Vector<String> vector = new Vector<>();

            vector.add("Select");
            while (resultSet.next()) {
                vector.add(resultSet.getString("payment_status"));

            }
            DefaultComboBoxModel model = new DefaultComboBoxModel(vector);
            viewOrdersPaymentStatusComboBox1.setModel(model);
        } catch (Exception e) {
            SignIn.getLoggerObject().log(Level.WARNING, "payment loading Exception found!", e);
        }
    }

    private void loadOrders() {
        try {
            String query = "SELECT `orders`.`order_id`,`first_name`,`last_name`,`mobile`,`date`,`paid_amount`,"
                    + "`order_status`.`order_status`,`payment_status`.`payment_status`, "
                    + "IFNULL((SELECT SUM(order_item.`qty`*order_item.`price`) FROM order_item WHERE `orders_order_id`=`orders`.`order_id`), 0) AS `total` "
                    + "FROM `orders` INNER JOIN order_status ON "
                    + "order_status.order_status_id=orders.order_status_id INNER JOIN customer ON "
                    + "customer.customer_nic=orders.customer_nic INNER JOIN `payment_status` "
                    + "ON `payment_status`.`payment_status_id`=`orders`.`payment_status_id` "
                    + "WHERE `orders`.`order_id` LIKE '%" + viewOrdersOrderId.getText() + "%' AND"
                    + " `mobile` LIKE '%" + viewOrdersCustomerMobile.getText() + "%' AND ";

            Date startDate = null;
            Date endDate = null;

            SimpleDateFormat format = new SimpleDateFormat("yyy-MM-dd");

            if (viewOrdersDateFrom.getDate() != null) {
                startDate = viewOrdersDateFrom.getDate();
                query += "`orders`.`date` >= '" + format.format(startDate) + "' AND ";
            }
            if (viewOrdersDateTo.getDate() != null) {
                endDate = viewOrdersDateTo.getDate();
                query += "`orders`.`date` <= '" + format.format(endDate) + "' ";
            }

            if (!String.valueOf(viewOrdersStatusComboBox.getSelectedIndex()).equals("0")) {
                query += "AND `order_status`.`order_status`= '" + String.valueOf(viewOrdersStatusComboBox.getSelectedItem()) + "' ";
                query = query.replace("AND AND ", "AND");
            }
            if (!String.valueOf(viewOrdersPaymentStatusComboBox1.getSelectedIndex()).equals("0")) {
                query += "AND `payment_status`.`payment_status`= '" + String.valueOf(viewOrdersPaymentStatusComboBox1.getSelectedItem()) + "' ";
                query = query.replace("AND AND ", "AND");
            }

            query += "ORDER BY  ";
            query = query.replace("AND ORDER BY ", "ORDER BY ");

            if (viewOrdersComboBox.getSelectedIndex() == 0) {
                query += "`orders`.`order_id` DESC";
            } else if (viewOrdersComboBox.getSelectedIndex() == 1) {
                query += "`orders`.`order_id` ASC";
            } else if (viewOrdersComboBox.getSelectedIndex() == 2) {
                query += "`customer`.`first_name` ASC";
            } else if (viewOrdersComboBox.getSelectedIndex() == 3) {
                query += "`customer`.`first_name` DESC";
            } else if (viewOrdersComboBox.getSelectedIndex() == 4) {
                query += "`total` ASC";
            } else if (viewOrdersComboBox.getSelectedIndex() == 5) {
                query += "`total` DESC";
            }

            ResultSet resultSet = MySQL.executeSearch(query);

            DefaultTableModel dtm = (DefaultTableModel) viewOrderTable.getModel();
            dtm.setRowCount(0);

            while (resultSet.next()) {
                Vector<String> vector = new Vector<>();
                String tot = resultSet.getString("total");
                vector.add(resultSet.getString("orders.order_id"));
                vector.add(resultSet.getString("customer.first_name") + " " + resultSet.getString("customer.last_name"));
                vector.add(resultSet.getString("customer.mobile"));
                vector.add(String.valueOf(resultSet.getDouble("orders.paid_amount")));
                vector.add(resultSet.getString("orders.date"));
                vector.add(tot);
                vector.add(resultSet.getString("order_status.order_status"));
                vector.add(resultSet.getString("payment_status.payment_status"));

                dtm.addRow(vector);
            }
        } catch (Exception e) {
            SignIn.getLoggerObject().log(Level.WARNING, "order loading Exception found!", e);
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

        viewOrderPopupMenue = new javax.swing.JPopupMenu();
        viewOrderUpdateStatusMenuItem = new javax.swing.JMenuItem();
        jPanel2 = new javax.swing.JPanel();
        jPanel26 = new javax.swing.JPanel();
        jLabel34 = new javax.swing.JLabel();
        jLabel35 = new javax.swing.JLabel();
        viewOrdersOrderId = new javax.swing.JTextField();
        jLabel36 = new javax.swing.JLabel();
        viewOrdersCustomerMobile = new javax.swing.JTextField();
        jLabel37 = new javax.swing.JLabel();
        jLabel38 = new javax.swing.JLabel();
        jLabel39 = new javax.swing.JLabel();
        viewOrdersComboBox = new javax.swing.JComboBox<>();
        viewOrdersClearAllButton = new javax.swing.JButton();
        viewOrdersDateFrom = new com.toedter.calendar.JDateChooser();
        viewOrdersDateTo = new com.toedter.calendar.JDateChooser();
        jButton1 = new javax.swing.JButton();
        viewOrdersStatusComboBox = new javax.swing.JComboBox<>();
        jLabel40 = new javax.swing.JLabel();
        viewOrdersPaymentStatusComboBox1 = new javax.swing.JComboBox<>();
        jLabel41 = new javax.swing.JLabel();
        jPanel27 = new javax.swing.JPanel();
        jScrollPane2 = new javax.swing.JScrollPane();
        viewOrderTable = new javax.swing.JTable();
        jPanel1 = new javax.swing.JPanel();
        jButton2 = new javax.swing.JButton();
        jButton3 = new javax.swing.JButton();

        viewOrderUpdateStatusMenuItem.setText("Update Order Status");
        viewOrderUpdateStatusMenuItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                viewOrderUpdateStatusMenuItemActionPerformed(evt);
            }
        });
        viewOrderPopupMenue.add(viewOrderUpdateStatusMenuItem);

        setLayout(new java.awt.BorderLayout());

        jPanel2.setLayout(new javax.swing.BoxLayout(jPanel2, javax.swing.BoxLayout.PAGE_AXIS));

        jPanel26.setBackground(new java.awt.Color(255, 255, 255));

        jLabel34.setFont(new java.awt.Font("Serif", 0, 12)); // NOI18N
        jLabel34.setText(" Order ID");

        jLabel35.setFont(new java.awt.Font("Serif", 1, 24)); // NOI18N
        jLabel35.setText("View Orders");

        viewOrdersOrderId.setFont(new java.awt.Font("Serif", 0, 12)); // NOI18N
        viewOrdersOrderId.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                viewOrdersOrderIdKeyReleased(evt);
            }
        });

        jLabel36.setFont(new java.awt.Font("Serif", 0, 12)); // NOI18N
        jLabel36.setText("Date From");

        viewOrdersCustomerMobile.setFont(new java.awt.Font("Serif", 0, 12)); // NOI18N
        viewOrdersCustomerMobile.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                viewOrdersCustomerMobileKeyReleased(evt);
            }
        });

        jLabel37.setFont(new java.awt.Font("Serif", 0, 12)); // NOI18N
        jLabel37.setText("Customer Mobile");

        jLabel38.setFont(new java.awt.Font("Serif", 0, 12)); // NOI18N
        jLabel38.setText("To");

        jLabel39.setFont(new java.awt.Font("Serif", 0, 12)); // NOI18N
        jLabel39.setText("Sort As");

        viewOrdersComboBox.setFont(new java.awt.Font("Serif", 0, 12)); // NOI18N
        viewOrdersComboBox.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Order Id Descending order", "Order Id Ascending order", "Customer Name (A - Z)", "Customer Name (Z - A)", "Total Ascending order", "Total Descending order" }));
        viewOrdersComboBox.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                viewOrdersComboBoxItemStateChanged(evt);
            }
        });

        viewOrdersClearAllButton.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        viewOrdersClearAllButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/resources/clearAll.png"))); // NOI18N
        viewOrdersClearAllButton.setText("Clear All");
        viewOrdersClearAllButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                viewOrdersClearAllButtonActionPerformed(evt);
            }
        });

        viewOrdersDateFrom.addPropertyChangeListener(new java.beans.PropertyChangeListener() {
            public void propertyChange(java.beans.PropertyChangeEvent evt) {
                viewOrdersDateFromPropertyChange(evt);
            }
        });

        viewOrdersDateTo.addPropertyChangeListener(new java.beans.PropertyChangeListener() {
            public void propertyChange(java.beans.PropertyChangeEvent evt) {
                viewOrdersDateToPropertyChange(evt);
            }
        });

        jButton1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/lk/corevision/fruitshop/resources/reset-black-25.png"))); // NOI18N
        jButton1.setBorder(null);
        jButton1.setRolloverIcon(new javax.swing.ImageIcon(getClass().getResource("/lk/corevision/fruitshop/resources/reset-danger-25.png"))); // NOI18N
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        viewOrdersStatusComboBox.setFont(new java.awt.Font("Serif", 0, 12)); // NOI18N
        viewOrdersStatusComboBox.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                viewOrdersStatusComboBoxItemStateChanged(evt);
            }
        });
        viewOrdersStatusComboBox.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                viewOrdersStatusComboBoxActionPerformed(evt);
            }
        });

        jLabel40.setFont(new java.awt.Font("Serif", 0, 12)); // NOI18N
        jLabel40.setText("Order Status");

        viewOrdersPaymentStatusComboBox1.setFont(new java.awt.Font("Serif", 0, 12)); // NOI18N
        viewOrdersPaymentStatusComboBox1.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                viewOrdersPaymentStatusComboBox1ItemStateChanged(evt);
            }
        });
        viewOrdersPaymentStatusComboBox1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                viewOrdersPaymentStatusComboBox1ActionPerformed(evt);
            }
        });

        jLabel41.setFont(new java.awt.Font("Serif", 0, 12)); // NOI18N
        jLabel41.setText("Payment Status");

        javax.swing.GroupLayout jPanel26Layout = new javax.swing.GroupLayout(jPanel26);
        jPanel26.setLayout(jPanel26Layout);
        jPanel26Layout.setHorizontalGroup(
            jPanel26Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel26Layout.createSequentialGroup()
                .addGroup(jPanel26Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel26Layout.createSequentialGroup()
                        .addComponent(jLabel34)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(viewOrdersOrderId, javax.swing.GroupLayout.PREFERRED_SIZE, 169, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jLabel35)
                    .addGroup(jPanel26Layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jLabel41)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(viewOrdersPaymentStatusComboBox1, javax.swing.GroupLayout.PREFERRED_SIZE, 130, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED, 24, Short.MAX_VALUE)
                        .addGroup(jPanel26Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel26Layout.createSequentialGroup()
                                .addComponent(jLabel37)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(viewOrdersCustomerMobile, javax.swing.GroupLayout.PREFERRED_SIZE, 164, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(jPanel26Layout.createSequentialGroup()
                                .addComponent(jLabel40)
                                .addGap(33, 33, 33)
                                .addComponent(viewOrdersStatusComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, 164, javax.swing.GroupLayout.PREFERRED_SIZE)))))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED, 24, Short.MAX_VALUE)
                .addGroup(jPanel26Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel26Layout.createSequentialGroup()
                        .addComponent(jLabel36, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, 0)
                        .addComponent(viewOrdersDateFrom, javax.swing.GroupLayout.PREFERRED_SIZE, 138, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(jLabel38, javax.swing.GroupLayout.PREFERRED_SIZE, 21, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(viewOrdersDateTo, javax.swing.GroupLayout.PREFERRED_SIZE, 132, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel26Layout.createSequentialGroup()
                        .addComponent(jLabel39, javax.swing.GroupLayout.PREFERRED_SIZE, 47, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(viewOrdersComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, 188, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(viewOrdersClearAllButton, javax.swing.GroupLayout.PREFERRED_SIZE, 110, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(30, 30, 30)
                .addComponent(jButton1)
                .addGap(7, 7, 7))
        );
        jPanel26Layout.setVerticalGroup(
            jPanel26Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel26Layout.createSequentialGroup()
                .addGroup(jPanel26Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(jPanel26Layout.createSequentialGroup()
                        .addComponent(jLabel35)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel26Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(viewOrdersStatusComboBox, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(jPanel26Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(jLabel41)
                                .addComponent(viewOrdersPaymentStatusComboBox1, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(jLabel40)
                                .addComponent(jLabel39)
                                .addComponent(viewOrdersComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(viewOrdersClearAllButton, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE))))
                    .addComponent(jButton1))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(jPanel26Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(jPanel26Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                        .addComponent(viewOrdersDateFrom, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(viewOrdersDateTo, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel26Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabel34)
                        .addComponent(viewOrdersOrderId, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jLabel36)
                        .addComponent(viewOrdersCustomerMobile, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jLabel37))
                    .addComponent(jLabel38, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
        );

        jPanel2.add(jPanel26);

        jPanel27.setBackground(new java.awt.Color(255, 255, 255));

        viewOrderTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Order  Id", "Customer Name", "Customer Mobile", "Payment", "Date", "Total", "Order Status", "Payment Status"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        viewOrderTable.getTableHeader().setReorderingAllowed(false);
        viewOrderTable.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                viewOrderTableMouseClicked(evt);
            }
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                viewOrderTableMouseReleased(evt);
            }
        });
        jScrollPane2.setViewportView(viewOrderTable);

        javax.swing.GroupLayout jPanel27Layout = new javax.swing.GroupLayout(jPanel27);
        jPanel27.setLayout(jPanel27Layout);
        jPanel27Layout.setHorizontalGroup(
            jPanel27Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 964, Short.MAX_VALUE)
        );
        jPanel27Layout.setVerticalGroup(
            jPanel27Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel27Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 295, Short.MAX_VALUE)
                .addContainerGap())
        );

        jPanel2.add(jPanel27);

        jPanel1.setBackground(new java.awt.Color(255, 255, 255));

        jButton2.setFont(new java.awt.Font("Serif", 1, 14)); // NOI18N
        jButton2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/lk/corevision/fruitshop/resources/PrintIcon.png"))); // NOI18N
        jButton2.setText("Print");
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });

        jButton3.setFont(new java.awt.Font("Serif", 1, 14)); // NOI18N
        jButton3.setText("Email");
        jButton3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton3ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addContainerGap(794, Short.MAX_VALUE)
                .addComponent(jButton3)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jButton2)
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addContainerGap(15, Short.MAX_VALUE)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jButton2, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton3, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
        );

        jPanel2.add(jPanel1);

        add(jPanel2, java.awt.BorderLayout.CENTER);
    }// </editor-fold>//GEN-END:initComponents

    private void viewOrdersOrderIdKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_viewOrdersOrderIdKeyReleased
        loadOrders();
    }//GEN-LAST:event_viewOrdersOrderIdKeyReleased

    private void viewOrdersCustomerMobileKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_viewOrdersCustomerMobileKeyReleased
        loadOrders();
    }//GEN-LAST:event_viewOrdersCustomerMobileKeyReleased

    private void viewOrdersComboBoxItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_viewOrdersComboBoxItemStateChanged
        loadOrders();
    }//GEN-LAST:event_viewOrdersComboBoxItemStateChanged

    private void viewOrdersClearAllButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_viewOrdersClearAllButtonActionPerformed
        viewOrdersOrderId.setText("");
        viewOrdersCustomerMobile.setText("");
        viewOrdersDateFrom.setDate(null);
        viewOrdersDateTo.setDate(null);
        viewOrderTable.clearSelection();
        viewOrdersComboBox.setSelectedIndex(0);
        viewOrdersStatusComboBox.setSelectedIndex(0);
        viewOrdersPaymentStatusComboBox1.setSelectedIndex(0);

        loadOrders();
    }//GEN-LAST:event_viewOrdersClearAllButtonActionPerformed

    private void viewOrdersDateFromPropertyChange(java.beans.PropertyChangeEvent evt) {//GEN-FIRST:event_viewOrdersDateFromPropertyChange
        loadOrders();
    }//GEN-LAST:event_viewOrdersDateFromPropertyChange

    private void viewOrdersDateToPropertyChange(java.beans.PropertyChangeEvent evt) {//GEN-FIRST:event_viewOrdersDateToPropertyChange
        loadOrders();
    }//GEN-LAST:event_viewOrdersDateToPropertyChange

    private void viewOrderTableMouseReleased(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_viewOrderTableMouseReleased

        if (evt.getButton() == evt.BUTTON3 && viewOrderTable.getSelectedRow() != -1 && dashboard != null) { //Right Click
            viewOrderPopupMenue.show(evt.getComponent(), evt.getX(), evt.getY());

        }
    }//GEN-LAST:event_viewOrderTableMouseReleased

    private void viewOrderUpdateStatusMenuItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_viewOrderUpdateStatusMenuItemActionPerformed
        if (Dashboard.getEmployeeTypeName().equals("Admin") || Dashboard.getEmployeeTypeName().equals("Sales Manager") || Dashboard.getEmployeeTypeName().equals("Cashair")) {
            OrderStatus orderStatus = new OrderStatus(dashboard, true);
            orderStatus.setorderStatusOrderId(String.valueOf(viewOrderTable.getValueAt(viewOrderTable.getSelectedRow(), 0)));
            orderStatus.setVisible(true);
        } else {
            JOptionPane.showMessageDialog(this, "Access blocked for role: " + Dashboard.getEmployeeTypeName(), "Access Denied", JOptionPane.WARNING_MESSAGE, JOptionIcon.WARNING.setICon());
        }
    }//GEN-LAST:event_viewOrderUpdateStatusMenuItemActionPerformed

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        loadOrders();
        Notifications.getInstance().show(Notifications.Type.SUCCESS, Notifications.Location.BOTTOM_RIGHT, "Refresh Successfuly!!");
    }//GEN-LAST:event_jButton1ActionPerformed

    private void viewOrdersStatusComboBoxItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_viewOrdersStatusComboBoxItemStateChanged
        loadOrders();
    }//GEN-LAST:event_viewOrdersStatusComboBoxItemStateChanged

    private void viewOrdersStatusComboBoxActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_viewOrdersStatusComboBoxActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_viewOrdersStatusComboBoxActionPerformed

    private void viewOrdersPaymentStatusComboBox1ItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_viewOrdersPaymentStatusComboBox1ItemStateChanged
        loadOrders();
    }//GEN-LAST:event_viewOrdersPaymentStatusComboBox1ItemStateChanged

    private void viewOrdersPaymentStatusComboBox1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_viewOrdersPaymentStatusComboBox1ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_viewOrdersPaymentStatusComboBox1ActionPerformed

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        loadViewOrdersReport(true);
    }//GEN-LAST:event_jButton2ActionPerformed

    private void loadViewOrdersReport(boolean viewReport) {
        if (Dashboard.getEmployeeTypeName().equals("Admin") || Dashboard.getEmployeeTypeName().equals("Sales Manager") || Dashboard.getEmployeeTypeName().equals("Cashair")) {
            try {

                InputStream path = this.getClass().getResourceAsStream("/lk/corevision/fruitshop/reports/sales/fruitShopAppViewOrder.jasper");
                String date = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
                HashMap<String, Object> params = new HashMap<>();
                params.put("Parameter1", date);
                params.put("Parameter2", SignIn.getEmployeeNIC());
                params.put("Parameter101", SendLogo.getFilePath());

                JRTableModelDataSource dataSource = new JRTableModelDataSource(viewOrderTable.getModel());
                JasperPrint jasperPrint = JasperFillManager.fillReport(path, params, dataSource);
                if (viewReport) {
                    JasperViewer.viewReport(jasperPrint, false);
                } else {
                    String htmlContent
                            = "<html> All Orders Report"
                            + "</html>";

                    boolean status = SendFileEmail.sendEmail(Dashboard.getLoggedEmployeeEmail(), "FruitShop All Orders", htmlContent, jasperPrint, "All orders report " + date);
                    if (status) {
                        JOptionPane.showMessageDialog(this, "Email sent successfuly", "Success", JOptionPane.INFORMATION_MESSAGE, JOptionIcon.SUCCESS.setICon());
                    }
                }
            } catch (Exception e) {
                SignIn.getLoggerObject().log(Level.WARNING, "Jasper file load Exception found!", e);
            }
        } else {
            JOptionPane.showMessageDialog(this, "Access blocked for role: " + Dashboard.getEmployeeTypeName(), "Access Denied", JOptionPane.WARNING_MESSAGE, JOptionIcon.WARNING.setICon());
        }
    }
    private void jButton3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton3ActionPerformed
        loadViewOrdersReport(false);
    }//GEN-LAST:event_jButton3ActionPerformed

    private void viewOrderTableMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_viewOrderTableMouseClicked
        int row = viewOrderTable.getSelectedRow();
        if (evt.getClickCount() == 2 && transport != null) {
            String orderId = String.valueOf(viewOrderTable.getValueAt(row, 0));

            try {
                ResultSet resultSet = MySQL.executeSearch("SELECT * FROM `orders` INNER JOIN `order_status` ON `orders`.`order_status_id`=`order_status`.`order_status_id` WHERE `order_id`='" + orderId + "'");
                if (resultSet.next()) {
                    if (resultSet.getString("order_status.order_status").equals("Process")) {
                        transport.setOrderId(orderId);
                        SwingUtilities.getWindowAncestor(this).dispose();
                    } else {
                        JOptionPane.showMessageDialog(this, "Order is already '" + resultSet.getString("order_status.order_status") + "'. ", "Warning", JOptionPane.OK_OPTION, JOptionIcon.WARNING.setICon());
                    }
                }

            } catch (Exception e) {
                e.printStackTrace();
            }

        }
    }//GEN-LAST:event_viewOrderTableMouseClicked


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jButton3;
    private javax.swing.JLabel jLabel34;
    private javax.swing.JLabel jLabel35;
    private javax.swing.JLabel jLabel36;
    private javax.swing.JLabel jLabel37;
    private javax.swing.JLabel jLabel38;
    private javax.swing.JLabel jLabel39;
    private javax.swing.JLabel jLabel40;
    private javax.swing.JLabel jLabel41;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel26;
    private javax.swing.JPanel jPanel27;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JPopupMenu viewOrderPopupMenue;
    private javax.swing.JTable viewOrderTable;
    private javax.swing.JMenuItem viewOrderUpdateStatusMenuItem;
    private javax.swing.JButton viewOrdersClearAllButton;
    private javax.swing.JComboBox<String> viewOrdersComboBox;
    private javax.swing.JTextField viewOrdersCustomerMobile;
    private com.toedter.calendar.JDateChooser viewOrdersDateFrom;
    private com.toedter.calendar.JDateChooser viewOrdersDateTo;
    private javax.swing.JTextField viewOrdersOrderId;
    private javax.swing.JComboBox<String> viewOrdersPaymentStatusComboBox1;
    private javax.swing.JComboBox<String> viewOrdersStatusComboBox;
    // End of variables declaration//GEN-END:variables
}
