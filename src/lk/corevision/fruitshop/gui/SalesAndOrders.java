package lk.corevision.fruitshop.gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Image;
import java.awt.event.MouseEvent;
import java.io.InputStream;
import java.sql.ResultSet;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.TextStyle;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Vector;
import java.util.logging.Level;
import javaswingdev.chart.ModelPieChart;
import javax.swing.ImageIcon;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.table.DefaultTableModel;
import lk.corevision.fruitshop.charts.PieChart;
import lk.corevision.fruitshop.model.MySQL;
import lk.corevision.fruitshop.model.OrderItems;
import lk.corevision.fruitshop.model.ReturnItems;
import lk.corevision.fruitshop.model.SendLogo;
import lk.corevision.fruitshop.validate.DataValidator;
import lk.corevision.fruitshop.validate.JOptionIcon;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.data.JRTableModelDataSource;
import net.sf.jasperreports.view.JasperViewer;
import raven.chart.ModelChart;

public class SalesAndOrders extends javax.swing.JPanel {

    private HashMap<String, OrderItems> orderItemMap = new HashMap<>();
    private HashMap<String, ReturnItems> returnItemMap = new HashMap<>();
    private Dashboard dashboard;
    private String customerNIC = null;
    private String packingId;
    private double total = 0;
    private double returnTotal = 0;

    public SalesAndOrders(Dashboard dashboard) {
        initComponents();
        this.dashboard = dashboard;
        genarateOrderId();
        genarateReturnId();
        loadViewOrders();
        loadViewReturns();
        loadPieChart();
        loadLineChart();
    }

    private void loadPieChart() {
        pieChart1.setChartType(PieChart.PeiChartType.DEFAULT);
        try {
            ResultSet resultSet = MySQL.executeSearch("SELECT `order_status`.`order_status`,`order_status`.`order_status_id` FROM `orders` INNER JOIN order_status ON "
                    + "order_status.order_status_id=orders.order_status_id INNER JOIN customer ON "
                    + "customer.customer_nic=orders.customer_nic");

            int processing = 0;
            int complete = 0;
            while (resultSet.next()) {
                if (resultSet.getString("order_status_id").equals("1")) {
                    processing += 1;
                } else {
                    complete += 1;

                }

            }
            pieChart1.addData(new ModelPieChart("processing", processing, new Color(221, 65, 65)));
            pieChart1.addData(new ModelPieChart("complete", complete, new Color(23, 126, 238)));
        } catch (Exception e) {
            SignIn.getLoggerObject().log(Level.WARNING, "pie chart loading Exception found!", e);
        }

    }

    private void loadLineChart() {
        try {
            lineChart.setTitle("Chart Data");
            lineChart.addLegend("Orders", Color.decode("#7b4397"), Color.decode("#dc2430"));
            lineChart.addLegend("Returns", Color.decode("#e65c00"), Color.decode("#F9D423"));
            lineChart.addLegend("Order Pending Payment", Color.decode("#0099F7"), Color.decode("#F11712"));

            Date date = new Date();
            String month = new SimpleDateFormat("MM").format(date);
            String[] months = new String[Integer.parseInt(month)];

            String[] allMonths = {
                "January", "February", "March", "April", "May", "June",
                "July", "August", "September", "October", "November", "December"
            };

            int currentMonthIndex = Integer.parseInt(month) - 1;

            for (int i = 0; i <= currentMonthIndex; i++) {
                months[i] = allMonths[i];
            }

            LocalDate currentDate = LocalDate.now();
            String currentMonth = currentDate.getMonth().getDisplayName(TextStyle.FULL, Locale.ENGLISH);

            String year = new SimpleDateFormat("yyyy").format(date);
            for (int i = 0; i < months.length; i++) {
                ResultSet resultSet1 = MySQL.executeSearch("SELECT COUNT(`order_id`) AS `orders` FROM `orders` WHERE MONTH(`date`)='" + (i + 1) + "' AND Year(`date`)='" + year + "'");
                ResultSet resultSet2 = MySQL.executeSearch("SELECT COUNT(`return_id`) AS `returns` FROM `product_returns` WHERE MONTH(`return_date`)='" + (i + 1) + "' AND Year(`return_date`)='" + year + "'");
                ResultSet resultSet3 = MySQL.executeSearch("SELECT COUNT(`order_id`) AS `pending_payment` FROM `orders` WHERE MONTH(`date`)='" + (i + 1) + "' AND `payment_status_id`='1' AND Year(`date`)='" + year + "'");
                resultSet1.next();
                resultSet2.next();
                resultSet3.next();
                lineChart.addData(new ModelChart(months[i], new double[]{Double.parseDouble(resultSet1.getString("orders")), Double.parseDouble(resultSet2.getString("returns")), Double.parseDouble(resultSet3.getString("pending_payment"))}));
            }
        } catch (Exception e) {
            SignIn.getLoggerObject().log(Level.WARNING, "line chart loading Exception found!", e);
        }
        lineChart.start();

    }

    private void loadViewOrders() {
        ViewOrders viewOrders = new ViewOrders();
        viewOrders.setDashboard(this.dashboard);
        this.jPanel4.add(viewOrders, BorderLayout.CENTER);
        SwingUtilities.updateComponentTreeUI(this.jPanel4);
    }

    public void setCustomerMobile(String customerMobile) {
        orderCustomerMobileField.setText(customerMobile);
        returnCustomerMobile.setText(customerMobile);
    }

    public void setPackingId(String packingId) {
        if (jTabbedPane1.getSelectedIndex() == 1) {
            searchPackedFruit(packingId);
        } else if (jTabbedPane1.getSelectedIndex() == 3) {
            searchReturnPackedFruit(packingId);
        }

    }

    public void getSearch() {
        searchCustomer();
        searchReturnCustomer();
    }

    public void setOrderFruitId(String fruitId) {
        if (jTabbedPane1.getSelectedIndex() == 1) {
            OrderFruitId.setText(fruitId);
        }
    }

    private void genarateOrderId() {
        long id = System.currentTimeMillis();
        orderIdField.setText(String.valueOf(id));
    }

    // Order adding
    private void loadOrderItem() {
        DefaultTableModel model = (DefaultTableModel) orderItemTable.getModel();
        model.setRowCount(0);

        total = 0;

        for (OrderItems InvoiceItem : orderItemMap.values()) {
            Vector<String> vector = new Vector<>();
            vector.add(InvoiceItem.getPackingId());
            vector.add(InvoiceItem.getFruitId());
            vector.add(InvoiceItem.getFruitName());
            vector.add(InvoiceItem.getMfd());
            vector.add(InvoiceItem.getSellingPrice());
            vector.add(InvoiceItem.getDiscountPrice());
            vector.add(InvoiceItem.getQty());

            double itemTotal = Double.parseDouble(InvoiceItem.getQty()) * Double.parseDouble(InvoiceItem.getDiscountPrice());
            total += itemTotal;
            vector.add(String.valueOf(itemTotal));
            model.addRow(vector);

        }
        orderTotalField.setText(String.valueOf(total));
        if (total != 0) {
            orderPlaceButton.setEnabled(true);
        } else {
            orderPlaceButton.setEnabled(false);
        }

    }

    private void searchPacking() {

        try {
            String pId = OrderFruitId.getText();

            ResultSet resultSet1 = MySQL.executeSearch("SELECT COUNT(`fruit_id`) AS `count` FROM `fruit` INNER JOIN `plantation` ON "
                    + "`fruit`.`fruit_id`=`plantation`.`fruit_fruit_id` "
                    + "INNER JOIN `inventory` ON `inventory`.`plantation_id`=`plantation`.`plantation_id` INNER JOIN `product_quality` "
                    + "ON `product_quality`.`inventory_inventory_id`=`inventory`.`inventory_id` INNER JOIN `packing` ON "
                    + "`packing`.`product_quality_id`=`product_quality`.`product_quality_id` "
                    + "WHERE `fruit_id` = '" + pId + "' AND `packing`.`qty`>'0' ");

            while (resultSet1.next()) {
                int count = resultSet1.getInt("count");
                if (count == 0) {
                    reset();
                } else if (count == 1) {
                    searchPackedFruit(null);
                } else {
                    ProductPackingDialog productPackingDialog = new ProductPackingDialog(dashboard, true);
//                    sm.getProductId().setText(pId);
//                    sm.getSortComboBox().setSelectedIndex(15);
                    productPackingDialog.setVisible(true);
                }
            }

        } catch (Exception e) {
            SignIn.getLoggerObject().log(Level.WARNING, "search packing Exception found!", e);
        }
    }

    private void searchPackedFruit(String id) {
        try {
            String query = "SELECT * FROM `fruit` INNER JOIN `plantation` ON "
                    + "`fruit`.`fruit_id`=`plantation`.`fruit_fruit_id` "
                    + "INNER JOIN `inventory` ON `inventory`.`plantation_id`=`plantation`.`plantation_id` INNER JOIN `product_quality` "
                    + "ON `product_quality`.`inventory_inventory_id`=`inventory`.`inventory_id` INNER JOIN `packing` ON "
                    + "`packing`.`product_quality_id`=`product_quality`.`product_quality_id` "
                    + "WHERE `fruit_id` = '" + OrderFruitId.getText() + "' ";
            if (id != null) {
                query += "AND `packing`.`packing_id`='" + id + "'";
            }
            ResultSet resultSet2 = MySQL.executeSearch(query);
            resultSet2.next();
            packingId = resultSet2.getString("packing.packing_id");
            orderFruitName.setText(resultSet2.getString("fruit_name"));
            orderFruitAvailableQuantity.setText(resultSet2.getString("packing.qty"));
            orderFruitmfd.setText(resultSet2.getString("date"));
            orderFruitSellingPrice.setText(resultSet2.getString("price"));
        } catch (Exception e) {
            SignIn.getLoggerObject().log(Level.WARNING, "searchPackedFruit method Exception found!", e);
        }
    }

    private void searchCustomer() {

        try {
            ResultSet resultSet = MySQL.executeSearch("SELECT * FROM  `customer` "
                    + "WHERE `mobile` = '" + orderCustomerMobileField.getText() + "'");

            if (resultSet.next()) {
                customerNIC = resultSet.getString("customer_nic");
                orderCustomerNameLabel.setText(resultSet.getString("first_name") + " " + resultSet.getString("last_name"));
            } else {
                orderCustomerNameLabel.setText("Customer Name Here");
                customerNIC = null;
            }
        } catch (Exception e) {
            SignIn.getLoggerObject().log(Level.WARNING, "searchCustomer method Exception found!", e);
        }
    }

    private void searchReturnPackedFruit(String id) {
        try {
            String query = "SELECT * FROM `fruit` INNER JOIN `plantation` ON "
                    + "`fruit`.`fruit_id`=`plantation`.`fruit_fruit_id` "
                    + "INNER JOIN `inventory` ON `inventory`.`plantation_id`=`plantation`.`plantation_id` INNER JOIN `product_quality` "
                    + "ON `product_quality`.`inventory_inventory_id`=`inventory`.`inventory_id` INNER JOIN `packing` ON "
                    + "`packing`.`product_quality_id`=`product_quality`.`product_quality_id` "
                    + "WHERE ";
            if (id != null) {
                query += "`packing`.`packing_id`='" + id + "'";
            } else {
                query += "`packing_id` = '" + returnPackingId.getText() + "'";
            }
            ResultSet resultSet2 = MySQL.executeSearch(query);
            if (resultSet2.next()) {
                returnPackingId.setText(resultSet2.getString("packing.packing_id"));
                packingId = resultSet2.getString("packing.packing_id");
                returnFruitName.setText(resultSet2.getString("fruit_name"));
                returnFruitId.setText(resultSet2.getString("fruit_id"));
                returnFruitmfd.setText(resultSet2.getString("date"));
                returnFruitSellingPrice.setText(resultSet2.getString("price"));
            } else {
                packingId = null;
                returnFruitName.setText("Fruit Name Here");
                returnFruitId.setText("Fruit Id Here");
                returnFruitmfd.setText("");
                returnFruitSellingPrice.setText("");
            }

        } catch (Exception e) {
            SignIn.getLoggerObject().log(Level.WARNING, "searchReturnPackedFruit method Exception found!", e);

        }
    }

    // Order Returns
    private void genarateReturnId() {
        long id = System.currentTimeMillis();
        returnIdField.setText(String.valueOf(id));
    }

    private void loadReturnItem() {
        DefaultTableModel model = (DefaultTableModel) returnItemTable.getModel();
        model.setRowCount(0);

        returnTotal = 0;

        for (ReturnItems returnItem : returnItemMap.values()) {
            Vector<String> vector = new Vector<>();
            vector.add(returnItem.getPackingId());
            vector.add(returnItem.getFruitId());
            vector.add(returnItem.getFruitName());
            vector.add(returnItem.getMfd());
            vector.add(returnItem.getSellingPrice());
            vector.add(returnItem.getQty());

            double itemTotal = Double.parseDouble(returnItem.getQty()) * Double.parseDouble(returnItem.getSellingPrice());
            returnTotal += itemTotal;
            vector.add(String.valueOf(itemTotal));
            model.addRow(vector);

        }
        returnTotalField.setText(String.valueOf(returnTotal));
        if (returnTotal != 0) {
            returnOrderButton.setEnabled(true);
        } else {
            returnOrderButton.setEnabled(false);
        }

    }

    private void searchReturnCustomer() {

        try {
            ResultSet resultSet = MySQL.executeSearch("SELECT * FROM  `customer` "
                    + "WHERE `mobile` = '" + returnCustomerMobile.getText() + "'");

            if (resultSet.next()) {
                customerNIC = resultSet.getString("customer_nic");
                returnCustomerNameLabel.setText(resultSet.getString("first_name") + " " + resultSet.getString("last_name"));
            } else {
                returnCustomerNameLabel.setText("Customer Name Here");
                customerNIC = null;
            }
        } catch (Exception e) {
            SignIn.getLoggerObject().log(Level.WARNING, "searchReturnCustomer method Exception found!", e);
        }
    }

    private void loadViewReturns() {
        try {
            String query = "SELECT `return_id`,`first_name`,`last_name`,`mobile`,`return_date`,`refund_amount` "
                    + "FROM `product_returns` INNER JOIN  customer ON "
                    + "customer.customer_nic=product_returns.customer_nic "
                    + "WHERE `return_id` LIKE '%" + viewReturnsReturnId.getText() + "%' AND `mobile` LIKE '%" + viewReturnsCustomerMobile.getText() + "%' AND ";

            Date startDate = null;
            Date endDate = null;

            SimpleDateFormat format = new SimpleDateFormat("yyy-MM-dd");

            if (viewReturnsDateFrom.getDate() != null) {
                startDate = viewReturnsDateFrom.getDate();
                query += "`product_returns`.`return_date` >= '" + format.format(startDate) + "' AND ";
            }
            if (viewReturnsDateTo.getDate() != null) {
                endDate = viewReturnsDateTo.getDate();
                query += "`product_returns`.`return_date` <= '" + format.format(endDate) + "' ";
            }
            query += "ORDER BY  ";
            query = query.replace("AND ORDER BY ", "ORDER BY ");

            if (viewReturnsComboBox.getSelectedIndex() == 0) {
                query += "`product_returns`.`return_id` DESC";
            } else if (viewReturnsComboBox.getSelectedIndex() == 1) {
                query += "`product_returns`.`return_id` ASC";
            } else if (viewReturnsComboBox.getSelectedIndex() == 2) {
                query += "`customer`.`first_name` ASC";
            } else if (viewReturnsComboBox.getSelectedIndex() == 3) {
                query += "`customer`.`first_name` DESC";
            } else if (viewReturnsComboBox.getSelectedIndex() == 4) {
                query += "`refund_amount` ASC";
            } else if (viewReturnsComboBox.getSelectedIndex() == 5) {
                query += "`refund_amount` DESC";
            }

            ResultSet resultSet = MySQL.executeSearch(query);

            DefaultTableModel dtm = (DefaultTableModel) viewReturnsTable.getModel();
            dtm.setRowCount(0);

            while (resultSet.next()) {
                Vector<String> vector = new Vector<>();
                ResultSet executeSearch = MySQL.executeSearch("SELECT SUM(return_item.`qty`*packing.`price`) AS `total` "
                        + "FROM `return_item` INNER JOIN `packing` ON "
                        + "packing.`packing_id`=return_item.`packing_packing_id` "
                        + "WHERE `return_id`='" + resultSet.getString("return_id") + "'");
                String tot = "0";
                if (executeSearch.next()) {
                    tot = executeSearch.getString("total");
                }
                vector.add(resultSet.getString("return_id"));
                vector.add(resultSet.getString("customer.first_name") + " " + resultSet.getString("customer.last_name"));
                vector.add(resultSet.getString("customer.mobile"));
                vector.add(String.valueOf(resultSet.getDouble("refund_amount")));
                vector.add(resultSet.getString("return_date"));
                vector.add(tot);

                dtm.addRow(vector);
            }
        } catch (Exception e) {
            SignIn.getLoggerObject().log(Level.WARNING, "loadViewReturns method Exception found!", e);
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

        orderPopupMenu = new javax.swing.JPopupMenu();
        orderUpdateMenuItem = new javax.swing.JMenuItem();
        returnPopupMenu = new javax.swing.JPopupMenu();
        returnDeleteMenuItem = new javax.swing.JMenuItem();
        viewReturnsPopupMenue = new javax.swing.JPopupMenu();
        viewReturnsMenuItem = new javax.swing.JMenuItem();
        jPanel1 = new javax.swing.JPanel();
        jTabbedPane1 = new javax.swing.JTabbedPane();
        jPanel2 = new javax.swing.JPanel();
        jPanel6 = new javax.swing.JPanel();
        jButton6 = new javax.swing.JButton();
        jButton7 = new javax.swing.JButton();
        jButton8 = new javax.swing.JButton();
        jButton14 = new javax.swing.JButton();
        jPanel7 = new javax.swing.JPanel();
        jPanel9 = new javax.swing.JPanel();
        jPanel12 = new javax.swing.JPanel();
        lineChart = new lk.corevision.fruitshop.charts.CurveLineChart();
        jLabel34 = new javax.swing.JLabel();
        jPanel8 = new javax.swing.JPanel();
        pieChart1 = new lk.corevision.fruitshop.charts.PieChart();
        jLabel35 = new javax.swing.JLabel();
        jPanel3 = new javax.swing.JPanel();
        jPanel21 = new javax.swing.JPanel();
        jLabel23 = new javax.swing.JLabel();
        jLabel24 = new javax.swing.JLabel();
        orderIdField = new javax.swing.JTextField();
        jLabel25 = new javax.swing.JLabel();
        orderCustomerMobileField = new javax.swing.JTextField();
        jButton1 = new javax.swing.JButton();
        jLabel26 = new javax.swing.JLabel();
        OrderFruitId = new javax.swing.JTextField();
        jLabel27 = new javax.swing.JLabel();
        orderFruitName = new javax.swing.JTextField();
        jLabel28 = new javax.swing.JLabel();
        orderFruitAvailableQuantity = new javax.swing.JTextField();
        jLabel29 = new javax.swing.JLabel();
        orderFruitmfd = new javax.swing.JTextField();
        jLabel30 = new javax.swing.JLabel();
        orderFruitSellingPrice = new javax.swing.JTextField();
        jLabel31 = new javax.swing.JLabel();
        orderFruitDiscountPrice = new javax.swing.JFormattedTextField();
        orderCustomerNameLabel = new javax.swing.JTextField();
        jLabel32 = new javax.swing.JLabel();
        orderFruitQty = new javax.swing.JFormattedTextField();
        jButton2 = new javax.swing.JButton();
        orderAddToOrderButton = new javax.swing.JButton();
        jButton4 = new javax.swing.JButton();
        jButton5 = new javax.swing.JButton();
        jPanel25 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        orderItemTable = new javax.swing.JTable();
        jPanel22 = new javax.swing.JPanel();
        jLabel33 = new javax.swing.JLabel();
        orderTotalField = new javax.swing.JFormattedTextField();
        orderPlaceButton = new javax.swing.JButton();
        jPanel4 = new javax.swing.JPanel();
        jPanel5 = new javax.swing.JPanel();
        jPanel29 = new javax.swing.JPanel();
        jLabel40 = new javax.swing.JLabel();
        jLabel41 = new javax.swing.JLabel();
        returnIdField = new javax.swing.JTextField();
        jLabel42 = new javax.swing.JLabel();
        returnCustomerMobile = new javax.swing.JTextField();
        jButton9 = new javax.swing.JButton();
        jLabel43 = new javax.swing.JLabel();
        returnPackingId = new javax.swing.JTextField();
        jLabel44 = new javax.swing.JLabel();
        returnFruitName = new javax.swing.JTextField();
        jLabel45 = new javax.swing.JLabel();
        returnFruitmfd = new javax.swing.JTextField();
        jLabel47 = new javax.swing.JLabel();
        returnFruitSellingPrice = new javax.swing.JTextField();
        returnCustomerNameLabel = new javax.swing.JTextField();
        jLabel49 = new javax.swing.JLabel();
        returnQty = new javax.swing.JFormattedTextField();
        jButton10 = new javax.swing.JButton();
        returnAddToReturnButton = new javax.swing.JButton();
        jButton12 = new javax.swing.JButton();
        jButton13 = new javax.swing.JButton();
        returnFruitId = new javax.swing.JTextField();
        jLabel46 = new javax.swing.JLabel();
        jPanel30 = new javax.swing.JPanel();
        jScrollPane3 = new javax.swing.JScrollPane();
        returnItemTable = new javax.swing.JTable();
        jPanel31 = new javax.swing.JPanel();
        jLabel50 = new javax.swing.JLabel();
        returnTotalField = new javax.swing.JFormattedTextField();
        returnOrderButton = new javax.swing.JButton();
        jScrollPane4 = new javax.swing.JScrollPane();
        returnItemsComment = new javax.swing.JTextArea();
        jLabel51 = new javax.swing.JLabel();
        jPanel10 = new javax.swing.JPanel();
        jPanel35 = new javax.swing.JPanel();
        jLabel53 = new javax.swing.JLabel();
        jLabel54 = new javax.swing.JLabel();
        viewReturnsReturnId = new javax.swing.JTextField();
        jLabel55 = new javax.swing.JLabel();
        viewReturnsCustomerMobile = new javax.swing.JTextField();
        jLabel56 = new javax.swing.JLabel();
        jLabel57 = new javax.swing.JLabel();
        jLabel58 = new javax.swing.JLabel();
        viewReturnsComboBox = new javax.swing.JComboBox<>();
        viewReturnsClearAllButton = new javax.swing.JButton();
        viewReturnsDateFrom = new com.toedter.calendar.JDateChooser();
        viewReturnsDateTo = new com.toedter.calendar.JDateChooser();
        jButton17 = new javax.swing.JButton();
        jButton18 = new javax.swing.JButton();
        jPanel34 = new javax.swing.JPanel();
        jScrollPane2 = new javax.swing.JScrollPane();
        viewReturnsTable = new javax.swing.JTable();
        jPanel11 = new javax.swing.JPanel();

        orderUpdateMenuItem.setText("Remove");
        orderUpdateMenuItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                orderUpdateMenuItemActionPerformed(evt);
            }
        });
        orderPopupMenu.add(orderUpdateMenuItem);

        returnDeleteMenuItem.setText("Remove");
        returnDeleteMenuItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                returnDeleteMenuItemActionPerformed(evt);
            }
        });
        returnPopupMenu.add(returnDeleteMenuItem);

        viewReturnsMenuItem.setText("View");
        viewReturnsMenuItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                viewReturnsMenuItemActionPerformed(evt);
            }
        });
        viewReturnsPopupMenue.add(viewReturnsMenuItem);

        setLayout(new java.awt.BorderLayout());

        jTabbedPane1.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                jTabbedPane1StateChanged(evt);
            }
        });

        jPanel2.setLayout(new java.awt.BorderLayout(3, 10));

        jPanel6.setBackground(new java.awt.Color(255, 255, 255));
        jPanel6.setPreferredSize(new java.awt.Dimension(0, 100));
        jPanel6.setLayout(new java.awt.GridLayout(1, 4, 4, 4));

        jButton6.setFont(new java.awt.Font("Serif", 1, 18)); // NOI18N
        jButton6.setIcon(new javax.swing.ImageIcon(getClass().getResource("/lk/corevision/fruitshop/resources/sales/order.png"))); // NOI18N
        jButton6.setText("Orders");
        jButton6.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                jButton6MouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                jButton6MouseExited(evt);
            }
        });
        jButton6.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton6ActionPerformed(evt);
            }
        });
        jPanel6.add(jButton6);

        jButton7.setFont(new java.awt.Font("Serif", 1, 18)); // NOI18N
        jButton7.setIcon(new javax.swing.ImageIcon(getClass().getResource("/lk/corevision/fruitshop/resources/sales/view_order.png"))); // NOI18N
        jButton7.setText("View Orders");
        jButton7.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                jButton7MouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                jButton7MouseExited(evt);
            }
        });
        jButton7.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton7ActionPerformed(evt);
            }
        });
        jPanel6.add(jButton7);

        jButton8.setFont(new java.awt.Font("Serif", 1, 18)); // NOI18N
        jButton8.setIcon(new javax.swing.ImageIcon(getClass().getResource("/lk/corevision/fruitshop/resources/sales/returns.png"))); // NOI18N
        jButton8.setText("Returns");
        jButton8.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                jButton8MouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                jButton8MouseExited(evt);
            }
        });
        jButton8.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton8ActionPerformed(evt);
            }
        });
        jPanel6.add(jButton8);

        jButton14.setFont(new java.awt.Font("Serif", 1, 18)); // NOI18N
        jButton14.setIcon(new javax.swing.ImageIcon(getClass().getResource("/lk/corevision/fruitshop/resources/sales/view_returns.png"))); // NOI18N
        jButton14.setText("View Returns");
        jButton14.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                jButton14MouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                jButton14MouseExited(evt);
            }
        });
        jButton14.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton14ActionPerformed(evt);
            }
        });
        jPanel6.add(jButton14);

        jPanel2.add(jPanel6, java.awt.BorderLayout.NORTH);

        jPanel7.setBackground(new java.awt.Color(255, 255, 255));
        jPanel7.setLayout(new java.awt.GridLayout(1, 2, 4, 1));

        jPanel9.setLayout(new java.awt.GridLayout(1, 2, 4, 0));

        jPanel12.setBorder(javax.swing.BorderFactory.createTitledBorder(""));
        jPanel12.setLayout(new java.awt.BorderLayout());
        jPanel12.add(lineChart, java.awt.BorderLayout.CENTER);

        jLabel34.setFont(new java.awt.Font("Serif", 1, 24)); // NOI18N
        jLabel34.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel34.setText("Analyzation");
        jLabel34.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jPanel12.add(jLabel34, java.awt.BorderLayout.PAGE_START);

        jPanel9.add(jPanel12);

        jPanel8.setBorder(javax.swing.BorderFactory.createTitledBorder(""));
        jPanel8.setLayout(new java.awt.BorderLayout());
        jPanel8.add(pieChart1, java.awt.BorderLayout.CENTER);

        jLabel35.setFont(new java.awt.Font("Serif", 1, 24)); // NOI18N
        jLabel35.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel35.setText(" Orders Status");
        jPanel8.add(jLabel35, java.awt.BorderLayout.PAGE_START);

        jPanel9.add(jPanel8);

        jPanel7.add(jPanel9);

        jPanel2.add(jPanel7, java.awt.BorderLayout.CENTER);

        jTabbedPane1.addTab("Home", jPanel2);

        jPanel21.setBackground(new java.awt.Color(255, 255, 255));

        jLabel23.setFont(new java.awt.Font("Serif", 1, 24)); // NOI18N
        jLabel23.setText(" Order");

        jLabel24.setFont(new java.awt.Font("Serif", 0, 12)); // NOI18N
        jLabel24.setText(" Order ID");

        orderIdField.setEditable(false);
        orderIdField.setFont(new java.awt.Font("Serif", 0, 12)); // NOI18N

        jLabel25.setFont(new java.awt.Font("Serif", 0, 12)); // NOI18N
        jLabel25.setText("Customer");

        orderCustomerMobileField.setFont(new java.awt.Font("Serif", 0, 12)); // NOI18N
        orderCustomerMobileField.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                orderCustomerMobileFieldKeyReleased(evt);
            }
        });

        jButton1.setFont(new java.awt.Font("Serif", 1, 12)); // NOI18N
        jButton1.setText("Add");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        jLabel26.setFont(new java.awt.Font("Serif", 0, 12)); // NOI18N
        jLabel26.setText("Fruit Name");

        OrderFruitId.setFont(new java.awt.Font("Serif", 0, 12)); // NOI18N
        OrderFruitId.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                OrderFruitIdActionPerformed(evt);
            }
        });
        OrderFruitId.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                OrderFruitIdKeyReleased(evt);
            }
        });

        jLabel27.setFont(new java.awt.Font("Serif", 0, 12)); // NOI18N
        jLabel27.setText("Fruit Id");

        orderFruitName.setEditable(false);
        orderFruitName.setFont(new java.awt.Font("Serif", 0, 12)); // NOI18N
        orderFruitName.setText("Fruit Name Here");

        jLabel28.setFont(new java.awt.Font("Serif", 0, 12)); // NOI18N
        jLabel28.setText("Mfd");

        orderFruitAvailableQuantity.setEditable(false);
        orderFruitAvailableQuantity.setFont(new java.awt.Font("Serif", 0, 12)); // NOI18N

        jLabel29.setFont(new java.awt.Font("Serif", 0, 12)); // NOI18N
        jLabel29.setText("Available qty");

        orderFruitmfd.setEditable(false);
        orderFruitmfd.setFont(new java.awt.Font("Serif", 0, 12)); // NOI18N
        orderFruitmfd.setText("Manufacture Date Here");
        orderFruitmfd.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                orderFruitmfdActionPerformed(evt);
            }
        });

        jLabel30.setFont(new java.awt.Font("Serif", 0, 12)); // NOI18N
        jLabel30.setText("Selling Price");

        orderFruitSellingPrice.setEditable(false);
        orderFruitSellingPrice.setText("0.00");

        jLabel31.setFont(new java.awt.Font("Serif", 0, 12)); // NOI18N
        jLabel31.setText("Discount Price");

        orderFruitDiscountPrice.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.NumberFormatter(new java.text.DecimalFormat("#0.00"))));
        orderFruitDiscountPrice.setFont(new java.awt.Font("Serif", 0, 12)); // NOI18N

        orderCustomerNameLabel.setEditable(false);
        orderCustomerNameLabel.setFont(new java.awt.Font("Serif", 0, 12)); // NOI18N
        orderCustomerNameLabel.setText("Customer Name Here");

        jLabel32.setFont(new java.awt.Font("Serif", 0, 12)); // NOI18N
        jLabel32.setText("Qty");

        orderFruitQty.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.NumberFormatter(new java.text.DecimalFormat("#0"))));
        orderFruitQty.setFont(new java.awt.Font("Serif", 0, 12)); // NOI18N
        orderFruitQty.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                orderFruitQtyActionPerformed(evt);
            }
        });
        orderFruitQty.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                orderFruitQtyKeyReleased(evt);
            }
        });

        jButton2.setFont(new java.awt.Font("Serif", 1, 12)); // NOI18N
        jButton2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/resources/clearAll.png"))); // NOI18N
        jButton2.setText("Clear");
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });

        orderAddToOrderButton.setFont(new java.awt.Font("Serif", 1, 12)); // NOI18N
        orderAddToOrderButton.setText("Add to order");
        orderAddToOrderButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                orderAddToOrderButtonActionPerformed(evt);
            }
        });

        jButton4.setFont(new java.awt.Font("Serif", 1, 12)); // NOI18N
        jButton4.setIcon(new javax.swing.ImageIcon(getClass().getResource("/resources/clearAll.png"))); // NOI18N
        jButton4.setText("Clear All");
        jButton4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton4ActionPerformed(evt);
            }
        });

        jButton5.setFont(new java.awt.Font("Serif", 1, 12)); // NOI18N
        jButton5.setText("Add");
        jButton5.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton5ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel21Layout = new javax.swing.GroupLayout(jPanel21);
        jPanel21.setLayout(jPanel21Layout);
        jPanel21Layout.setHorizontalGroup(
            jPanel21Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel21Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel21Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(jPanel21Layout.createSequentialGroup()
                        .addComponent(jLabel23)
                        .addGap(18, 18, 18)
                        .addComponent(jLabel24)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(orderIdField))
                    .addGroup(jPanel21Layout.createSequentialGroup()
                        .addGroup(jPanel21Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel29)
                            .addComponent(jLabel26, javax.swing.GroupLayout.PREFERRED_SIZE, 81, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(jPanel21Layout.createSequentialGroup()
                                .addComponent(jLabel27, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addGap(6, 6, 6)))
                        .addGroup(jPanel21Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel21Layout.createSequentialGroup()
                                .addComponent(OrderFruitId, javax.swing.GroupLayout.PREFERRED_SIZE, 168, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jButton5, javax.swing.GroupLayout.PREFERRED_SIZE, 72, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(0, 0, Short.MAX_VALUE))
                            .addComponent(orderFruitName)
                            .addComponent(orderFruitAvailableQuantity))))
                .addGap(13, 13, 13)
                .addGroup(jPanel21Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                    .addGroup(jPanel21Layout.createSequentialGroup()
                        .addComponent(jLabel25)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 24, Short.MAX_VALUE)
                        .addComponent(orderCustomerMobileField, javax.swing.GroupLayout.PREFERRED_SIZE, 136, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jButton1))
                    .addGroup(jPanel21Layout.createSequentialGroup()
                        .addGroup(jPanel21Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel21Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addComponent(jLabel30)
                                .addComponent(jLabel28))
                            .addComponent(jLabel31))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel21Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                            .addComponent(orderFruitDiscountPrice, javax.swing.GroupLayout.DEFAULT_SIZE, 154, Short.MAX_VALUE)
                            .addComponent(orderFruitmfd, javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(orderFruitSellingPrice, javax.swing.GroupLayout.Alignment.LEADING))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jLabel32)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel21Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(orderCustomerNameLabel)
                    .addGroup(jPanel21Layout.createSequentialGroup()
                        .addComponent(orderFruitQty, javax.swing.GroupLayout.PREFERRED_SIZE, 114, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jButton2, javax.swing.GroupLayout.DEFAULT_SIZE, 233, Short.MAX_VALUE))
                    .addComponent(orderAddToOrderButton, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jButton4, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        jPanel21Layout.setVerticalGroup(
            jPanel21Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel21Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel21Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel23)
                    .addComponent(jLabel24)
                    .addComponent(orderIdField, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel25)
                    .addComponent(orderCustomerMobileField, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(orderCustomerNameLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(jPanel21Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(OrderFruitId, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel27)
                    .addComponent(jLabel28)
                    .addComponent(orderFruitmfd, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel32)
                    .addComponent(orderFruitQty, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton2, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton5, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(jPanel21Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel21Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabel26)
                        .addComponent(orderFruitName, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel21Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabel30)
                        .addComponent(orderFruitSellingPrice, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(orderAddToOrderButton, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(jPanel21Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel21Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel21Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel29)
                            .addComponent(orderFruitAvailableQuantity, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel21Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel31)
                            .addComponent(orderFruitDiscountPrice, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addComponent(jButton4, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jPanel25.setBackground(new java.awt.Color(255, 255, 255));

        orderItemTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Packing ID", "Fruit Id", "Fruit Name", "MFD", "Selling Price", "Discount Price", "Qty", "Total"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        orderItemTable.getTableHeader().setReorderingAllowed(false);
        orderItemTable.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                orderItemTableMouseReleased(evt);
            }
        });
        jScrollPane1.setViewportView(orderItemTable);

        javax.swing.GroupLayout jPanel25Layout = new javax.swing.GroupLayout(jPanel25);
        jPanel25.setLayout(jPanel25Layout);
        jPanel25Layout.setHorizontalGroup(
            jPanel25Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel25Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane1)
                .addContainerGap())
        );
        jPanel25Layout.setVerticalGroup(
            jPanel25Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel25Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 306, Short.MAX_VALUE)
                .addContainerGap())
        );

        jPanel22.setBackground(new java.awt.Color(255, 255, 255));

        jLabel33.setFont(new java.awt.Font("Serif", 0, 12)); // NOI18N
        jLabel33.setText("Total");

        orderTotalField.setEnabled(false);
        orderTotalField.setFont(new java.awt.Font("Serif", 0, 12)); // NOI18N

        orderPlaceButton.setFont(new java.awt.Font("Serif", 1, 12)); // NOI18N
        orderPlaceButton.setText("Place Order");
        orderPlaceButton.setEnabled(false);
        orderPlaceButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                orderPlaceButtonActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel22Layout = new javax.swing.GroupLayout(jPanel22);
        jPanel22.setLayout(jPanel22Layout);
        jPanel22Layout.setHorizontalGroup(
            jPanel22Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel22Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(jPanel22Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel22Layout.createSequentialGroup()
                        .addComponent(jLabel33)
                        .addGap(18, 18, 18)
                        .addComponent(orderTotalField, javax.swing.GroupLayout.PREFERRED_SIZE, 235, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(orderPlaceButton, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 277, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
        );
        jPanel22Layout.setVerticalGroup(
            jPanel22Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel22Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel22Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel33)
                    .addComponent(orderTotalField, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addComponent(orderPlaceButton, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(35, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel21, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(jPanel25, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(jPanel22, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addComponent(jPanel21, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel25, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel22, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        jTabbedPane1.addTab("Order", jPanel3);

        jPanel4.setPreferredSize(new java.awt.Dimension(810, 528));
        jPanel4.setLayout(new java.awt.BorderLayout());
        jTabbedPane1.addTab(" View Orders", jPanel4);

        jPanel29.setBackground(new java.awt.Color(255, 255, 255));

        jLabel40.setFont(new java.awt.Font("Serif", 1, 24)); // NOI18N
        jLabel40.setText("Return");

        jLabel41.setFont(new java.awt.Font("Serif", 0, 12)); // NOI18N
        jLabel41.setText("Return ID");

        returnIdField.setEditable(false);
        returnIdField.setFont(new java.awt.Font("Serif", 0, 12)); // NOI18N

        jLabel42.setFont(new java.awt.Font("Serif", 0, 12)); // NOI18N
        jLabel42.setText("Customer");

        returnCustomerMobile.setFont(new java.awt.Font("Serif", 0, 12)); // NOI18N
        returnCustomerMobile.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                returnCustomerMobileActionPerformed(evt);
            }
        });
        returnCustomerMobile.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                returnCustomerMobileKeyReleased(evt);
            }
        });

        jButton9.setFont(new java.awt.Font("Serif", 1, 12)); // NOI18N
        jButton9.setText("Add");
        jButton9.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton9ActionPerformed(evt);
            }
        });

        jLabel43.setFont(new java.awt.Font("Serif", 0, 12)); // NOI18N
        jLabel43.setText("Fruit Name");

        returnPackingId.setFont(new java.awt.Font("Serif", 0, 12)); // NOI18N
        returnPackingId.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                returnPackingIdActionPerformed(evt);
            }
        });
        returnPackingId.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                returnPackingIdKeyReleased(evt);
            }
        });

        jLabel44.setFont(new java.awt.Font("Serif", 0, 12)); // NOI18N
        jLabel44.setText("Packing Id");

        returnFruitName.setEditable(false);
        returnFruitName.setFont(new java.awt.Font("Serif", 0, 12)); // NOI18N
        returnFruitName.setText("Fruit Name Here");

        jLabel45.setFont(new java.awt.Font("Serif", 0, 12)); // NOI18N
        jLabel45.setText("Mfd");

        returnFruitmfd.setEditable(false);
        returnFruitmfd.setFont(new java.awt.Font("Serif", 0, 12)); // NOI18N
        returnFruitmfd.setText("Manufacture Date Here");
        returnFruitmfd.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                returnFruitmfdActionPerformed(evt);
            }
        });

        jLabel47.setFont(new java.awt.Font("Serif", 0, 12)); // NOI18N
        jLabel47.setText("Selling Price");

        returnFruitSellingPrice.setEditable(false);
        returnFruitSellingPrice.setText("0.00");

        returnCustomerNameLabel.setEditable(false);
        returnCustomerNameLabel.setFont(new java.awt.Font("Serif", 0, 12)); // NOI18N
        returnCustomerNameLabel.setText("Customer Name Here");

        jLabel49.setFont(new java.awt.Font("Serif", 0, 12)); // NOI18N
        jLabel49.setText("Qty");

        returnQty.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.NumberFormatter(new java.text.DecimalFormat("#0.00"))));
        returnQty.setFont(new java.awt.Font("Serif", 0, 12)); // NOI18N
        returnQty.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                returnQtyActionPerformed(evt);
            }
        });

        jButton10.setFont(new java.awt.Font("Serif", 1, 12)); // NOI18N
        jButton10.setIcon(new javax.swing.ImageIcon(getClass().getResource("/resources/clearAll.png"))); // NOI18N
        jButton10.setText("Clear");
        jButton10.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton10ActionPerformed(evt);
            }
        });

        returnAddToReturnButton.setFont(new java.awt.Font("Serif", 1, 12)); // NOI18N
        returnAddToReturnButton.setText("Add to return");
        returnAddToReturnButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                returnAddToReturnButtonActionPerformed(evt);
            }
        });

        jButton12.setFont(new java.awt.Font("Serif", 1, 12)); // NOI18N
        jButton12.setIcon(new javax.swing.ImageIcon(getClass().getResource("/resources/clearAll.png"))); // NOI18N
        jButton12.setText("Clear All");
        jButton12.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton12ActionPerformed(evt);
            }
        });

        jButton13.setFont(new java.awt.Font("Serif", 1, 12)); // NOI18N
        jButton13.setText("Add");
        jButton13.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton13ActionPerformed(evt);
            }
        });

        returnFruitId.setEditable(false);
        returnFruitId.setFont(new java.awt.Font("Serif", 0, 12)); // NOI18N
        returnFruitId.setText("Fruit Id Here");

        jLabel46.setFont(new java.awt.Font("Serif", 0, 12)); // NOI18N
        jLabel46.setText("Fruit ID");

        javax.swing.GroupLayout jPanel29Layout = new javax.swing.GroupLayout(jPanel29);
        jPanel29.setLayout(jPanel29Layout);
        jPanel29Layout.setHorizontalGroup(
            jPanel29Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel29Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel29Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(jPanel29Layout.createSequentialGroup()
                        .addComponent(jLabel40)
                        .addGap(18, 18, 18)
                        .addComponent(jLabel41)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(returnIdField))
                    .addGroup(jPanel29Layout.createSequentialGroup()
                        .addGroup(jPanel29Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel29Layout.createSequentialGroup()
                                .addComponent(jLabel44, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addGap(6, 6, 6))
                            .addGroup(jPanel29Layout.createSequentialGroup()
                                .addGroup(jPanel29Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel43, javax.swing.GroupLayout.PREFERRED_SIZE, 81, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addGroup(jPanel29Layout.createSequentialGroup()
                                        .addGap(6, 6, 6)
                                        .addComponent(jLabel46, javax.swing.GroupLayout.PREFERRED_SIZE, 81, javax.swing.GroupLayout.PREFERRED_SIZE)))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                        .addGroup(jPanel29Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(returnFruitId)
                            .addGroup(jPanel29Layout.createSequentialGroup()
                                .addComponent(returnPackingId, javax.swing.GroupLayout.PREFERRED_SIZE, 168, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jButton13, javax.swing.GroupLayout.PREFERRED_SIZE, 72, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(0, 0, Short.MAX_VALUE))
                            .addComponent(returnFruitName))))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 7, Short.MAX_VALUE)
                .addGroup(jPanel29Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(jPanel29Layout.createSequentialGroup()
                        .addComponent(jLabel42)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(returnCustomerMobile, javax.swing.GroupLayout.PREFERRED_SIZE, 136, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jButton9)
                        .addGap(0, 41, Short.MAX_VALUE))
                    .addGroup(jPanel29Layout.createSequentialGroup()
                        .addGroup(jPanel29Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel47)
                            .addComponent(jLabel45))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel29Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                            .addComponent(returnFruitmfd, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 154, Short.MAX_VALUE)
                            .addComponent(returnFruitSellingPrice, javax.swing.GroupLayout.Alignment.LEADING))
                        .addGap(18, 18, Short.MAX_VALUE)
                        .addComponent(jLabel49)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel29Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(returnCustomerNameLabel)
                    .addGroup(jPanel29Layout.createSequentialGroup()
                        .addComponent(returnQty, javax.swing.GroupLayout.PREFERRED_SIZE, 114, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jButton10, javax.swing.GroupLayout.DEFAULT_SIZE, 198, Short.MAX_VALUE))
                    .addComponent(returnAddToReturnButton, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jButton12, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        jPanel29Layout.setVerticalGroup(
            jPanel29Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel29Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel29Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel40)
                    .addComponent(jLabel41)
                    .addComponent(returnIdField, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel42)
                    .addComponent(returnCustomerMobile, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton9, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(returnCustomerNameLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(jPanel29Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(returnPackingId, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel44)
                    .addComponent(jLabel45)
                    .addComponent(returnFruitmfd, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel49)
                    .addComponent(returnQty, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton10, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton13, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(jPanel29Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel29Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabel43)
                        .addComponent(returnFruitName, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel29Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabel47)
                        .addComponent(returnFruitSellingPrice, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(returnAddToReturnButton, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(jPanel29Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel29Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabel46)
                        .addComponent(returnFruitId, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jButton12, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jPanel30.setBackground(new java.awt.Color(255, 255, 255));

        returnItemTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Packing Id", "Fruit Id", "Fruit Name", "MFD", "Selling Price", "Qty", "Total"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        returnItemTable.getTableHeader().setReorderingAllowed(false);
        returnItemTable.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                returnItemTableMouseReleased(evt);
            }
        });
        jScrollPane3.setViewportView(returnItemTable);

        javax.swing.GroupLayout jPanel30Layout = new javax.swing.GroupLayout(jPanel30);
        jPanel30.setLayout(jPanel30Layout);
        jPanel30Layout.setHorizontalGroup(
            jPanel30Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel30Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane3)
                .addContainerGap())
        );
        jPanel30Layout.setVerticalGroup(
            jPanel30Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel30Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane3, javax.swing.GroupLayout.DEFAULT_SIZE, 333, Short.MAX_VALUE)
                .addContainerGap())
        );

        jPanel31.setBackground(new java.awt.Color(255, 255, 255));

        jLabel50.setFont(new java.awt.Font("Serif", 0, 12)); // NOI18N
        jLabel50.setText("Total");

        returnTotalField.setEditable(false);
        returnTotalField.setFont(new java.awt.Font("Serif", 0, 12)); // NOI18N

        returnOrderButton.setFont(new java.awt.Font("Serif", 1, 12)); // NOI18N
        returnOrderButton.setText("Return and Refound");
        returnOrderButton.setEnabled(false);
        returnOrderButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                returnOrderButtonActionPerformed(evt);
            }
        });

        returnItemsComment.setColumns(20);
        returnItemsComment.setRows(5);
        jScrollPane4.setViewportView(returnItemsComment);

        jLabel51.setFont(new java.awt.Font("Serif", 0, 12)); // NOI18N
        jLabel51.setText("Comment");

        javax.swing.GroupLayout jPanel31Layout = new javax.swing.GroupLayout(jPanel31);
        jPanel31.setLayout(jPanel31Layout);
        jPanel31Layout.setHorizontalGroup(
            jPanel31Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel31Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel51)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane4, javax.swing.GroupLayout.PREFERRED_SIZE, 422, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(jPanel31Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel31Layout.createSequentialGroup()
                        .addComponent(jLabel50)
                        .addGap(18, 18, 18)
                        .addComponent(returnTotalField, javax.swing.GroupLayout.PREFERRED_SIZE, 235, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(returnOrderButton, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 277, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
        );
        jPanel31Layout.setVerticalGroup(
            jPanel31Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel31Layout.createSequentialGroup()
                .addGroup(jPanel31Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel31Layout.createSequentialGroup()
                        .addContainerGap()
                        .addGroup(jPanel31Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                            .addComponent(jScrollPane4, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                            .addGroup(jPanel31Layout.createSequentialGroup()
                                .addGroup(jPanel31Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(jLabel50)
                                    .addComponent(returnTotalField, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(18, 18, 18)
                                .addComponent(returnOrderButton, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE))))
                    .addGroup(jPanel31Layout.createSequentialGroup()
                        .addGap(36, 36, 36)
                        .addComponent(jLabel51)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout jPanel5Layout = new javax.swing.GroupLayout(jPanel5);
        jPanel5.setLayout(jPanel5Layout);
        jPanel5Layout.setHorizontalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel29, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(jPanel30, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(jPanel31, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        jPanel5Layout.setVerticalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addComponent(jPanel29, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel30, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel31, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        jTabbedPane1.addTab("Return Products", jPanel5);

        jPanel10.setLayout(new javax.swing.BoxLayout(jPanel10, javax.swing.BoxLayout.PAGE_AXIS));

        jPanel35.setBackground(new java.awt.Color(255, 255, 255));

        jLabel53.setFont(new java.awt.Font("Serif", 0, 12)); // NOI18N
        jLabel53.setText("Return ID");

        jLabel54.setFont(new java.awt.Font("Serif", 1, 24)); // NOI18N
        jLabel54.setText("View Returns");

        viewReturnsReturnId.setFont(new java.awt.Font("Serif", 0, 12)); // NOI18N
        viewReturnsReturnId.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                viewReturnsReturnIdKeyReleased(evt);
            }
        });

        jLabel55.setFont(new java.awt.Font("Serif", 0, 12)); // NOI18N
        jLabel55.setText("Date From");

        viewReturnsCustomerMobile.setFont(new java.awt.Font("Serif", 0, 12)); // NOI18N
        viewReturnsCustomerMobile.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                viewReturnsCustomerMobileKeyReleased(evt);
            }
        });

        jLabel56.setFont(new java.awt.Font("Serif", 0, 12)); // NOI18N
        jLabel56.setText("Customer Mobile");

        jLabel57.setFont(new java.awt.Font("Serif", 0, 12)); // NOI18N
        jLabel57.setText("To");

        jLabel58.setFont(new java.awt.Font("Serif", 0, 12)); // NOI18N
        jLabel58.setText("Sort As");

        viewReturnsComboBox.setFont(new java.awt.Font("Serif", 0, 12)); // NOI18N
        viewReturnsComboBox.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Return Id Descending order", "Return Id Ascending order", "Customer Name Ascending order", "Customer Name Descending order", "Total Ascending order", "Total Descending order" }));
        viewReturnsComboBox.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                viewReturnsComboBoxItemStateChanged(evt);
            }
        });

        viewReturnsClearAllButton.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        viewReturnsClearAllButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/resources/clearAll.png"))); // NOI18N
        viewReturnsClearAllButton.setText("Clear All");
        viewReturnsClearAllButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                viewReturnsClearAllButtonActionPerformed(evt);
            }
        });

        viewReturnsDateFrom.addPropertyChangeListener(new java.beans.PropertyChangeListener() {
            public void propertyChange(java.beans.PropertyChangeEvent evt) {
                viewReturnsDateFromPropertyChange(evt);
            }
        });

        viewReturnsDateTo.addPropertyChangeListener(new java.beans.PropertyChangeListener() {
            public void propertyChange(java.beans.PropertyChangeEvent evt) {
                viewReturnsDateToPropertyChange(evt);
            }
        });

        jButton17.setIcon(new javax.swing.ImageIcon(getClass().getResource("/lk/corevision/fruitshop/resources/reset-black-25.png"))); // NOI18N
        jButton17.setBorder(null);
        jButton17.setRolloverIcon(new javax.swing.ImageIcon(getClass().getResource("/lk/corevision/fruitshop/resources/reset-danger-25.png"))); // NOI18N
        jButton17.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton17ActionPerformed(evt);
            }
        });

        jButton18.setFont(new java.awt.Font("Serif", 1, 14)); // NOI18N
        jButton18.setIcon(new javax.swing.ImageIcon(getClass().getResource("/lk/corevision/fruitshop/resources/PrintIcon.png"))); // NOI18N
        jButton18.setText("Print");
        jButton18.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton18ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel35Layout = new javax.swing.GroupLayout(jPanel35);
        jPanel35.setLayout(jPanel35Layout);
        jPanel35Layout.setHorizontalGroup(
            jPanel35Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel35Layout.createSequentialGroup()
                .addGroup(jPanel35Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel35Layout.createSequentialGroup()
                        .addComponent(jLabel53)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(viewReturnsReturnId, javax.swing.GroupLayout.PREFERRED_SIZE, 169, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 22, Short.MAX_VALUE)
                        .addComponent(jLabel56)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(viewReturnsCustomerMobile, javax.swing.GroupLayout.PREFERRED_SIZE, 164, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 22, Short.MAX_VALUE)
                        .addComponent(jLabel55, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel35Layout.createSequentialGroup()
                        .addComponent(jLabel54)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jLabel58, javax.swing.GroupLayout.PREFERRED_SIZE, 47, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel35Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel35Layout.createSequentialGroup()
                        .addComponent(viewReturnsDateFrom, javax.swing.GroupLayout.PREFERRED_SIZE, 138, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel57, javax.swing.GroupLayout.PREFERRED_SIZE, 21, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(viewReturnsDateTo, javax.swing.GroupLayout.PREFERRED_SIZE, 132, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(jButton18))
                    .addGroup(jPanel35Layout.createSequentialGroup()
                        .addComponent(viewReturnsComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, 231, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(viewReturnsClearAllButton, javax.swing.GroupLayout.PREFERRED_SIZE, 110, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(19, 19, 19)
                        .addComponent(jButton17)))
                .addContainerGap())
        );
        jPanel35Layout.setVerticalGroup(
            jPanel35Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel35Layout.createSequentialGroup()
                .addGroup(jPanel35Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(jPanel35Layout.createSequentialGroup()
                        .addGroup(jPanel35Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel58)
                            .addComponent(viewReturnsComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(viewReturnsClearAllButton, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jButton17))
                        .addGroup(jPanel35Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel35Layout.createSequentialGroup()
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(jLabel57))
                            .addGroup(jPanel35Layout.createSequentialGroup()
                                .addGap(4, 4, 4)
                                .addGroup(jPanel35Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jButton18, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addGroup(jPanel35Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                        .addComponent(viewReturnsDateFrom, javax.swing.GroupLayout.DEFAULT_SIZE, 32, Short.MAX_VALUE)
                                        .addComponent(viewReturnsDateTo, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))))
                    .addGroup(jPanel35Layout.createSequentialGroup()
                        .addComponent(jLabel54)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(jPanel35Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel53)
                            .addComponent(viewReturnsReturnId, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel55)
                            .addComponent(viewReturnsCustomerMobile, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel56))))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jPanel10.add(jPanel35);

        jPanel34.setBackground(new java.awt.Color(255, 255, 255));

        viewReturnsTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Return  Id", "Customer Name", "Customer Mobile", "Refound Amount", "Date", "Total"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        viewReturnsTable.getTableHeader().setReorderingAllowed(false);
        viewReturnsTable.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                viewReturnsTableMouseReleased(evt);
            }
        });
        jScrollPane2.setViewportView(viewReturnsTable);

        javax.swing.GroupLayout jPanel34Layout = new javax.swing.GroupLayout(jPanel34);
        jPanel34.setLayout(jPanel34Layout);
        jPanel34Layout.setHorizontalGroup(
            jPanel34Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 995, Short.MAX_VALUE)
        );
        jPanel34Layout.setVerticalGroup(
            jPanel34Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel34Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 479, Short.MAX_VALUE)
                .addContainerGap())
        );

        jPanel10.add(jPanel34);

        jPanel11.setBackground(new java.awt.Color(255, 255, 255));

        javax.swing.GroupLayout jPanel11Layout = new javax.swing.GroupLayout(jPanel11);
        jPanel11.setLayout(jPanel11Layout);
        jPanel11Layout.setHorizontalGroup(
            jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 995, Short.MAX_VALUE)
        );
        jPanel11Layout.setVerticalGroup(
            jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 44, Short.MAX_VALUE)
        );

        jPanel10.add(jPanel11);

        jTabbedPane1.addTab("View Returns", jPanel10);

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jTabbedPane1))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addComponent(jTabbedPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 652, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 2, Short.MAX_VALUE))
        );

        add(jPanel1, java.awt.BorderLayout.CENTER);
    }// </editor-fold>//GEN-END:initComponents

    private void jButton4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton4ActionPerformed
        resetAll();
    }//GEN-LAST:event_jButton4ActionPerformed

    private void orderFruitmfdActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_orderFruitmfdActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_orderFruitmfdActionPerformed

    private void returnFruitmfdActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_returnFruitmfdActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_returnFruitmfdActionPerformed

    private void jButton12ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton12ActionPerformed
        resetAllReturns();
    }//GEN-LAST:event_jButton12ActionPerformed

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed

        CustomerDialog customerDialog = new CustomerDialog(dashboard, true);
        customerDialog.setVisible(true);


    }//GEN-LAST:event_jButton1ActionPerformed

    private void jButton5ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton5ActionPerformed
        ProductPackingDialog productPackingDialog = new ProductPackingDialog(dashboard, true);
        productPackingDialog.setVisible(true);
    }//GEN-LAST:event_jButton5ActionPerformed

    private void orderUpdateMenuItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_orderUpdateMenuItemActionPerformed
        int row = orderItemTable.getSelectedRow();

        if (row != -1) {
            try {
                orderItemMap.remove(String.valueOf(orderItemTable.getValueAt(row, 0)));
                loadOrderItem();
            } catch (Exception e) {
                SignIn.getLoggerObject().log(Level.WARNING, "remove order row Exception found!", e);
            }
        }

    }//GEN-LAST:event_orderUpdateMenuItemActionPerformed

    private void orderCustomerMobileFieldKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_orderCustomerMobileFieldKeyReleased
        searchCustomer();
    }//GEN-LAST:event_orderCustomerMobileFieldKeyReleased

    private void jButton9ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton9ActionPerformed
        CustomerDialog customerDialog = new CustomerDialog(dashboard, true);
        customerDialog.setVisible(true);
    }//GEN-LAST:event_jButton9ActionPerformed

    private void OrderFruitIdKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_OrderFruitIdKeyReleased
        searchPacking();
    }//GEN-LAST:event_OrderFruitIdKeyReleased

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        resetFruits();
    }//GEN-LAST:event_jButton2ActionPerformed

    private void orderAddToOrderButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_orderAddToOrderButtonActionPerformed
        try {
            String fruitId = OrderFruitId.getText();
            String fruitName = orderFruitName.getText();
            String availableQty = orderFruitAvailableQuantity.getText();
            String mfd = orderFruitmfd.getText();
            String sellingPrice = orderFruitSellingPrice.getText();
            String discountPrice = orderFruitDiscountPrice.getText();
            String qty = orderFruitQty.getText();

            if (customerNIC == null) {
                JOptionPane.showMessageDialog(this, "Please Select Customer First", "Warning", JOptionPane.WARNING_MESSAGE, JOptionIcon.WARNING.setICon());
            } else if (fruitId.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Please Enter or Select Fruit Id", "Warning", JOptionPane.WARNING_MESSAGE, JOptionIcon.WARNING.setICon());
            } else if (packingId == null) {
                JOptionPane.showMessageDialog(this, "Please Enter valid Fruit Id", "Warning", JOptionPane.WARNING_MESSAGE, JOptionIcon.WARNING.setICon());
            } else if (DataValidator.isInvalidPrice(qty)) {
                JOptionPane.showMessageDialog(this, "Please Enter valid qty", "Warning", JOptionPane.WARNING_MESSAGE, JOptionIcon.WARNING.setICon());
            } else {
                if (discountPrice.isEmpty()) {
                    discountPrice = sellingPrice;
                }
                OrderItems orderItems = new OrderItems();
                orderItems.setFruitId(fruitId);
                orderItems.setFruitName(fruitName);
                orderItems.setMfd(mfd);
                orderItems.setPackingId(packingId);
                orderItems.setDiscountPrice(discountPrice);
                orderItems.setSellingPrice(sellingPrice);
                orderItems.setQty(qty);
                if (orderItemMap.get(packingId) == null) {
                    orderItemMap.put(packingId, orderItems);
                } else {
                    OrderItems found = orderItemMap.get(packingId);
                    int option = JOptionPane.showConfirmDialog(this, "Doyou want to update the Quantity of Product : " + fruitName, "Message", JOptionPane.YES_NO_OPTION,
                            JOptionPane.INFORMATION_MESSAGE, JOptionIcon.INFO.setICon());
                    if (option == JOptionPane.YES_OPTION) {
                        found.setQty(String.valueOf(Double.parseDouble(found.getQty()) + Double.parseDouble(qty)));
                    }
                }
                resetFruits();
                loadOrderItem();
                OrderFruitId.grabFocus();
            }
        } catch (Exception e) {
            SignIn.getLoggerObject().log(Level.WARNING, "add order row Exception found!", e);
        }
    }//GEN-LAST:event_orderAddToOrderButtonActionPerformed

    private void orderFruitQtyKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_orderFruitQtyKeyReleased
        String availableQty = orderFruitAvailableQuantity.getText();
        String qty = orderFruitQty.getText();
        if (!availableQty.isEmpty() && !qty.isEmpty()) {
            if (Double.parseDouble(availableQty) < Double.parseDouble(qty)) {
                JOptionPane.showMessageDialog(this, "Quantity riched available stock quantity", "Warning", JOptionPane.WARNING_MESSAGE);
                orderFruitQty.setText(availableQty);
            }
        }
    }//GEN-LAST:event_orderFruitQtyKeyReleased

    private void orderPlaceButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_orderPlaceButtonActionPerformed
        try {
            String orderId = orderIdField.getText();
            String dateTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());

            MySQL.executeIUD("INSERT INTO `orders`(`order_id`,`customer_nic`,`date`,`order_status_id`,`payment_status_id`,`employee_NIC`) "
                    + "VALUES ('" + orderId + "','" + customerNIC + "','" + dateTime + "','1','2','" + SignIn.getEmployeeNIC() + "')");

            for (OrderItems orderItems : orderItemMap.values()) {
                MySQL.executeIUD("INSERT INTO `order_item`(`orders_order_id`,`qty`,`price`,`packing_packing_id`) "
                        + "VALUES ('" + orderId + "','" + orderItems.getQty() + "','" + orderItems.getDiscountPrice() + "','" + orderItems.getPackingId() + "')");

                MySQL.executeIUD("UPDATE `packing` SET `qty`=`qty`-'" + orderItems.getQty() + "' WHERE `packing_id`='" + orderItems.getPackingId() + "'");
                resetAll();
            }
        } catch (Exception e) {
            SignIn.getLoggerObject().log(Level.WARNING, "placed order Exception found!", e);
        }
    }//GEN-LAST:event_orderPlaceButtonActionPerformed

    private void orderItemTableMouseReleased(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_orderItemTableMouseReleased

        if (evt.getButton() == MouseEvent.BUTTON3 && orderItemTable.getSelectedRow() != -1) { //Right Click
            orderPopupMenu.show(evt.getComponent(), evt.getX(), evt.getY());

        }
    }//GEN-LAST:event_orderItemTableMouseReleased

    private void returnCustomerMobileKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_returnCustomerMobileKeyReleased
        searchReturnCustomer();
    }//GEN-LAST:event_returnCustomerMobileKeyReleased

    private void jButton13ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton13ActionPerformed
        ProductPackingDialog productPackingDialog = new ProductPackingDialog(dashboard, true);
        productPackingDialog.setVisible(true);
    }//GEN-LAST:event_jButton13ActionPerformed

    private void jButton10ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton10ActionPerformed
        resetReturnItem();
    }//GEN-LAST:event_jButton10ActionPerformed

    private void returnPackingIdKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_returnPackingIdKeyReleased
        searchReturnPackedFruit(returnPackingId.getText());
    }//GEN-LAST:event_returnPackingIdKeyReleased

    private void jButton11ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton11ActionPerformed
        try {
            String packingId = returnPackingId.getText();
            String fruitName = orderFruitName.getText();
            String fruitId = returnFruitId.getText();
            String mfd = returnFruitmfd.getText();
            String sellingPrice = returnFruitSellingPrice.getText();
            String qty = returnQty.getText();

            if (customerNIC == null) {
                JOptionPane.showMessageDialog(this, "Please Select Customer First", "Warning", JOptionPane.WARNING_MESSAGE);
            } else if (packingId.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Please Enter or Select Fruit Id", "Warning", JOptionPane.WARNING_MESSAGE);
            } else if (packingId == null) {
                JOptionPane.showMessageDialog(this, "Please Enter valid Fruit Id", "Warning", JOptionPane.WARNING_MESSAGE);
            } else if (DataValidator.isInvalidPrice(qty)) {
                JOptionPane.showMessageDialog(this, "Please Enter valid qty", "Warning", JOptionPane.WARNING_MESSAGE);
            } else {

                ReturnItems returnItems = new ReturnItems();
                returnItems.setFruitName(fruitName);
                returnItems.setFruitId(fruitId);
                returnItems.setMfd(mfd);
                returnItems.setPackingId(packingId);
                returnItems.setSellingPrice(sellingPrice);
                returnItems.setQty(qty);
                if (returnItemMap.get(packingId) == null) {
                    returnItemMap.put(packingId, returnItems);
                } else {
                    ReturnItems found = returnItemMap.get(packingId);
                    int option = JOptionPane.showConfirmDialog(this, "Doyou want to update the Return Quantity of Product : " + fruitName, "Message", JOptionPane.YES_NO_OPTION,
                            JOptionPane.INFORMATION_MESSAGE);
                    if (option == JOptionPane.YES_OPTION) {
                        found.setQty(String.valueOf(Double.parseDouble(found.getQty()) + Double.parseDouble(qty)));
                    }
                }
                resetReturnItem();
            }
        } catch (Exception e) {
            SignIn.getLoggerObject().log(Level.WARNING, "add return row Exception found!", e);
        }
    }//GEN-LAST:event_jButton11ActionPerformed

    private void returnItemTableMouseReleased(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_returnItemTableMouseReleased

        if (evt.getButton() == MouseEvent.BUTTON3 && returnItemTable.getSelectedRow() != -1) { //Right Click
            returnPopupMenu.show(evt.getComponent(), evt.getX(), evt.getY());

        }
    }//GEN-LAST:event_returnItemTableMouseReleased

    private void returnDeleteMenuItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_returnDeleteMenuItemActionPerformed
        int row = returnItemTable.getSelectedRow();

        if (row != -1) {
            try {
                returnItemMap.remove(String.valueOf(returnItemTable.getValueAt(row, 0)));
                loadReturnItem();
            } catch (Exception e) {
                SignIn.getLoggerObject().log(Level.WARNING, "remove return row Exception found!", e);
            }
        }
    }//GEN-LAST:event_returnDeleteMenuItemActionPerformed

    private void jTabbedPane1StateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_jTabbedPane1StateChanged
//
//        resetFruits();
//        resetAllReturns();
    }//GEN-LAST:event_jTabbedPane1StateChanged

    private void returnOrderButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_returnOrderButtonActionPerformed
        try {
            String returnId = returnIdField.getText();
            String dateTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());

            MySQL.executeIUD("INSERT INTO `product_returns`(`return_id`,`customer_nic`,`return_date`,`refund_amount`,`reason`,`employee_NIC`) "
                    + "VALUES ('" + returnId + "','" + customerNIC + "','" + dateTime + "',"
                    + "'" + returnTotalField.getText() + "','" + returnItemsComment.getText() + "','" + SignIn.getEmployeeNIC() + "')");

            for (ReturnItems returnItems : returnItemMap.values()) {
                MySQL.executeIUD("INSERT INTO `return_item`(`return_id`,`qty`,`packing_packing_id`) "
                        + "VALUES ('" + returnId + "','" + returnItems.getQty() + "','" + returnItems.getPackingId() + "')");

                MySQL.executeIUD("UPDATE `packing` SET `qty`=`qty`+'" + returnItems.getQty() + "' WHERE `packing_id`='" + returnItems.getPackingId() + "'");

            }
            resetAllReturns();
            loadReturnItem();
        } catch (Exception e) {
            SignIn.getLoggerObject().log(Level.WARNING, "add return Exception found!", e);
        }
    }//GEN-LAST:event_returnOrderButtonActionPerformed

    private void jButton17ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton17ActionPerformed
        loadViewReturns();
    }//GEN-LAST:event_jButton17ActionPerformed

    private void viewReturnsDateToPropertyChange(java.beans.PropertyChangeEvent evt) {//GEN-FIRST:event_viewReturnsDateToPropertyChange
        loadViewReturns();
    }//GEN-LAST:event_viewReturnsDateToPropertyChange

    private void viewReturnsDateFromPropertyChange(java.beans.PropertyChangeEvent evt) {//GEN-FIRST:event_viewReturnsDateFromPropertyChange
        loadViewReturns();
    }//GEN-LAST:event_viewReturnsDateFromPropertyChange

    private void viewReturnsClearAllButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_viewReturnsClearAllButtonActionPerformed
        viewReturnsReturnId.setText("");
        viewReturnsCustomerMobile.setText("");
        viewReturnsDateFrom.setDate(null);
        viewReturnsDateTo.setDate(null);
        viewReturnsTable.clearSelection();
        viewReturnsComboBox.setSelectedIndex(0);

    }//GEN-LAST:event_viewReturnsClearAllButtonActionPerformed

    private void viewReturnsComboBoxItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_viewReturnsComboBoxItemStateChanged
        loadViewReturns();
    }//GEN-LAST:event_viewReturnsComboBoxItemStateChanged

    private void viewReturnsCustomerMobileKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_viewReturnsCustomerMobileKeyReleased
        loadViewReturns();
    }//GEN-LAST:event_viewReturnsCustomerMobileKeyReleased

    private void viewReturnsReturnIdKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_viewReturnsReturnIdKeyReleased
        loadViewReturns();
    }//GEN-LAST:event_viewReturnsReturnIdKeyReleased

    private void viewReturnsTableMouseReleased(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_viewReturnsTableMouseReleased

        if (evt.getButton() == evt.BUTTON3 && viewReturnsTable.getSelectedRow() != -1 && dashboard != null) { //Right Click
            viewReturnsPopupMenue.show(evt.getComponent(), evt.getX(), evt.getY());

        }
    }//GEN-LAST:event_viewReturnsTableMouseReleased

    private void OrderFruitIdActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_OrderFruitIdActionPerformed
        orderFruitQty.grabFocus();
    }//GEN-LAST:event_OrderFruitIdActionPerformed

    private void viewReturnsMenuItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_viewReturnsMenuItemActionPerformed
        viewReturnsItems viewReturnsItems = new viewReturnsItems(dashboard, true);
        viewReturnsItems.setReturnItemId(String.valueOf(viewReturnsTable.getValueAt(viewReturnsTable.getSelectedRow(), 0)));
        viewReturnsItems.setVisible(true);
    }//GEN-LAST:event_viewReturnsMenuItemActionPerformed

    private void jButton18ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton18ActionPerformed
        try {
            InputStream path = this.getClass().getResourceAsStream("/lk/corevision/fruitshop/reports/sales/fruitShopAppViewReturns.jasper");

            HashMap<String, Object> params = new HashMap<>();
            params.put("Parameter1", new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
            params.put("Parameter2", SignIn.getEmployeeNIC());
            params.put("Parameter101", SendLogo.getFilePath());

            JRTableModelDataSource dataSource = new JRTableModelDataSource(viewReturnsTable.getModel());
            JasperPrint jasperPrint = JasperFillManager.fillReport(path, params, dataSource);
            JasperViewer.viewReport(jasperPrint, false);
        } catch (Exception e) {
            SignIn.getLoggerObject().log(Level.WARNING, "viwe Return jasper report Exception found!", e);
        }
    }//GEN-LAST:event_jButton18ActionPerformed

    private void orderFruitQtyActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_orderFruitQtyActionPerformed
        orderAddToOrderButton.grabFocus();
    }//GEN-LAST:event_orderFruitQtyActionPerformed

    private void returnCustomerMobileActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_returnCustomerMobileActionPerformed
        returnPackingId.grabFocus();
    }//GEN-LAST:event_returnCustomerMobileActionPerformed

    private void returnPackingIdActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_returnPackingIdActionPerformed
        returnQty.grabFocus();
    }//GEN-LAST:event_returnPackingIdActionPerformed

    private void returnAddToReturnButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_returnAddToReturnButtonActionPerformed
        try {
            String packingId = returnPackingId.getText();
            String fruitName = returnFruitName.getText();
            String fruitId = returnFruitId.getText();
            String mfd = returnFruitmfd.getText();
            String sellingPrice = returnFruitSellingPrice.getText();
            String qty = returnQty.getText();

            if (customerNIC == null) {
                JOptionPane.showMessageDialog(this, "Please Select Customer First", "Warning", JOptionPane.WARNING_MESSAGE, JOptionIcon.WARNING.setICon());
            } else if (packingId.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Please Enter or Select Fruit Id", "Warning", JOptionPane.WARNING_MESSAGE, JOptionIcon.WARNING.setICon());
            } else if (packingId == null) {
                JOptionPane.showMessageDialog(this, "Please Enter valid Fruit Id", "Warning", JOptionPane.WARNING_MESSAGE, JOptionIcon.WARNING.setICon());
            } else if (DataValidator.isInvalidPrice(qty)) {
                JOptionPane.showMessageDialog(this, "Please Enter valid qty", "Warning", JOptionPane.WARNING_MESSAGE, JOptionIcon.WARNING.setICon());
            } else {

                ReturnItems returnItems = new ReturnItems();
                returnItems.setFruitName(fruitName);
                returnItems.setFruitId(fruitId);
                returnItems.setMfd(mfd);
                returnItems.setPackingId(packingId);
                returnItems.setSellingPrice(sellingPrice);
                returnItems.setQty(qty);
                if (returnItemMap.get(packingId) == null) {
                    returnItemMap.put(packingId, returnItems);
                } else {
                    ReturnItems found = returnItemMap.get(packingId);
                    int option = JOptionPane.showConfirmDialog(this, "Doyou want to update the Return Quantity of Product : " + fruitName, "Message", JOptionPane.YES_NO_OPTION,
                            JOptionPane.INFORMATION_MESSAGE, JOptionIcon.INFO.setICon());
                    if (option == JOptionPane.YES_OPTION) {
                        found.setQty(String.valueOf(Double.parseDouble(found.getQty()) + Double.parseDouble(qty)));
                    }
                }

                loadReturnItem();
                resetReturnItem();
            }
        } catch (Exception e) {
            SignIn.getLoggerObject().log(Level.WARNING, "add Return item row Exception found!", e);
        }
    }//GEN-LAST:event_returnAddToReturnButtonActionPerformed

    private void returnQtyActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_returnQtyActionPerformed
        returnAddToReturnButton.grabFocus();
    }//GEN-LAST:event_returnQtyActionPerformed

    private void jButton6ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton6ActionPerformed
        jTabbedPane1.setSelectedIndex(1);
    }//GEN-LAST:event_jButton6ActionPerformed

    private void jButton7ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton7ActionPerformed
        jTabbedPane1.setSelectedIndex(2);
    }//GEN-LAST:event_jButton7ActionPerformed

    private void jButton8ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton8ActionPerformed
        jTabbedPane1.setSelectedIndex(3);
    }//GEN-LAST:event_jButton8ActionPerformed

    private void jButton14ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton14ActionPerformed
        jTabbedPane1.setSelectedIndex(4);
    }//GEN-LAST:event_jButton14ActionPerformed

    private void jButton6MouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jButton6MouseEntered
        jButton6.setBackground(new Color(255, 153, 0));
    }//GEN-LAST:event_jButton6MouseEntered

    private void jButton6MouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jButton6MouseExited
        jButton6.setBackground(new Color(255, 255, 255));
    }//GEN-LAST:event_jButton6MouseExited

    private void jButton7MouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jButton7MouseExited
        jButton7.setBackground(new Color(255, 255, 255));
    }//GEN-LAST:event_jButton7MouseExited

    private void jButton8MouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jButton8MouseExited
        jButton8.setBackground(new Color(255, 255, 255));
    }//GEN-LAST:event_jButton8MouseExited

    private void jButton14MouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jButton14MouseExited
        jButton14.setBackground(new Color(255, 255, 255));
    }//GEN-LAST:event_jButton14MouseExited

    private void jButton7MouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jButton7MouseEntered
        jButton7.setBackground(new Color(255, 153, 0));
    }//GEN-LAST:event_jButton7MouseEntered

    private void jButton8MouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jButton8MouseEntered
        jButton8.setBackground(new Color(255, 153, 0));
    }//GEN-LAST:event_jButton8MouseEntered

    private void jButton14MouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jButton14MouseEntered
        jButton14.setBackground(new Color(255, 153, 0));
    }//GEN-LAST:event_jButton14MouseEntered


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JTextField OrderFruitId;
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton10;
    private javax.swing.JButton jButton12;
    private javax.swing.JButton jButton13;
    private javax.swing.JButton jButton14;
    private javax.swing.JButton jButton17;
    private javax.swing.JButton jButton18;
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jButton4;
    private javax.swing.JButton jButton5;
    private javax.swing.JButton jButton6;
    private javax.swing.JButton jButton7;
    private javax.swing.JButton jButton8;
    private javax.swing.JButton jButton9;
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
    private javax.swing.JLabel jLabel40;
    private javax.swing.JLabel jLabel41;
    private javax.swing.JLabel jLabel42;
    private javax.swing.JLabel jLabel43;
    private javax.swing.JLabel jLabel44;
    private javax.swing.JLabel jLabel45;
    private javax.swing.JLabel jLabel46;
    private javax.swing.JLabel jLabel47;
    private javax.swing.JLabel jLabel49;
    private javax.swing.JLabel jLabel50;
    private javax.swing.JLabel jLabel51;
    private javax.swing.JLabel jLabel53;
    private javax.swing.JLabel jLabel54;
    private javax.swing.JLabel jLabel55;
    private javax.swing.JLabel jLabel56;
    private javax.swing.JLabel jLabel57;
    private javax.swing.JLabel jLabel58;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel10;
    private javax.swing.JPanel jPanel11;
    private javax.swing.JPanel jPanel12;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel21;
    private javax.swing.JPanel jPanel22;
    private javax.swing.JPanel jPanel25;
    private javax.swing.JPanel jPanel29;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel30;
    private javax.swing.JPanel jPanel31;
    private javax.swing.JPanel jPanel34;
    private javax.swing.JPanel jPanel35;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JPanel jPanel7;
    private javax.swing.JPanel jPanel8;
    private javax.swing.JPanel jPanel9;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JScrollPane jScrollPane4;
    private javax.swing.JTabbedPane jTabbedPane1;
    private lk.corevision.fruitshop.charts.CurveLineChart lineChart;
    private javax.swing.JButton orderAddToOrderButton;
    private javax.swing.JTextField orderCustomerMobileField;
    private javax.swing.JTextField orderCustomerNameLabel;
    private javax.swing.JTextField orderFruitAvailableQuantity;
    private javax.swing.JFormattedTextField orderFruitDiscountPrice;
    private javax.swing.JTextField orderFruitName;
    private javax.swing.JFormattedTextField orderFruitQty;
    private javax.swing.JTextField orderFruitSellingPrice;
    private javax.swing.JTextField orderFruitmfd;
    private javax.swing.JTextField orderIdField;
    private javax.swing.JTable orderItemTable;
    private javax.swing.JButton orderPlaceButton;
    private javax.swing.JPopupMenu orderPopupMenu;
    private javax.swing.JFormattedTextField orderTotalField;
    private javax.swing.JMenuItem orderUpdateMenuItem;
    private lk.corevision.fruitshop.charts.PieChart pieChart1;
    private javax.swing.JButton returnAddToReturnButton;
    private javax.swing.JTextField returnCustomerMobile;
    private javax.swing.JTextField returnCustomerNameLabel;
    private javax.swing.JMenuItem returnDeleteMenuItem;
    private javax.swing.JTextField returnFruitId;
    private javax.swing.JTextField returnFruitName;
    private javax.swing.JTextField returnFruitSellingPrice;
    private javax.swing.JTextField returnFruitmfd;
    private javax.swing.JTextField returnIdField;
    private javax.swing.JTable returnItemTable;
    private javax.swing.JTextArea returnItemsComment;
    private javax.swing.JButton returnOrderButton;
    private javax.swing.JTextField returnPackingId;
    private javax.swing.JPopupMenu returnPopupMenu;
    private javax.swing.JFormattedTextField returnQty;
    private javax.swing.JFormattedTextField returnTotalField;
    private javax.swing.JButton viewReturnsClearAllButton;
    private javax.swing.JComboBox<String> viewReturnsComboBox;
    private javax.swing.JTextField viewReturnsCustomerMobile;
    private com.toedter.calendar.JDateChooser viewReturnsDateFrom;
    private com.toedter.calendar.JDateChooser viewReturnsDateTo;
    private javax.swing.JMenuItem viewReturnsMenuItem;
    private javax.swing.JPopupMenu viewReturnsPopupMenue;
    private javax.swing.JTextField viewReturnsReturnId;
    private javax.swing.JTable viewReturnsTable;
    // End of variables declaration//GEN-END:variables

    private void reset() {
        orderFruitName.setText("Fruit Name Here");
        orderFruitAvailableQuantity.setText("Available Quantity Here");
        orderFruitmfd.setText("Manufacture Date Here");
        orderFruitSellingPrice.setText("0.00");
        orderFruitDiscountPrice.setText("");
        orderFruitQty.setText("");
        packingId = null;
        OrderFruitId.grabFocus();
    }

    private void resetFruits() {
        OrderFruitId.setText("");
        reset();
    }

    private void resetAll() {
        resetFruits();
        resetCustomer();
        orderTotalField.setText("");
        total = 0;
        genarateOrderId();
        orderItemMap = new HashMap<>();
        loadOrderItem();
        orderCustomerMobileField.grabFocus();
        orderPlaceButton.setEnabled(false);
    }

    private void resetReturnItem() {
        returnFruitName.setText("Fruit Name Here");
        returnFruitId.setText("Fruit Id Here");
        returnPackingId.setText("");
        returnFruitmfd.setText("Manufacture Date Here");
        returnFruitSellingPrice.setText("0.00");
        returnQty.setText("");
        packingId = null;
        loadReturnItem();
        returnPackingId.grabFocus();
    }

    private void resetCustomer() {
        customerNIC = null;
        returnCustomerMobile.setText("");
        orderCustomerMobileField.setText("");
        searchReturnCustomer();
        searchCustomer();
    }

    private void resetAllReturns() {
        resetReturnItem();
        returnItemMap = new HashMap<>();
        loadReturnItem();
        genarateReturnId();
        resetCustomer();
        orderTotalField.setText("");
        returnTotalField.setText("");
        returnItemsComment.setText("");
        returnOrderButton.setEnabled(false);
        returnTotal = 0;
        returnCustomerMobile.grabFocus();
        returnItemTable.clearSelection();
    }
}
