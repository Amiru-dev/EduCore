package lk.corevision.fruitshop.gui;

import java.awt.BorderLayout;
import java.awt.event.MouseEvent;
import javax.swing.JOptionPane;
import lk.corevision.fruitshop.model.MySQL;
import java.sql.ResultSet;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Vector;
import javaswingdev.chart.ModelPieChart;
import javax.swing.DefaultComboBoxModel;
import java.awt.Color;
import javax.swing.table.DefaultTableModel;
import lk.corevision.fruitshop.charts.PieChart;
import lk.corevision.fruitshop.model.GRNItems;
import lk.corevision.fruitshop.model.GRNTransaction;
import lk.corevision.fruitshop.validate.DataValidator;
import lk.corevision.fruitshop.validate.JOptionIcon;
import java.sql.Connection;
import java.sql.DriverManager;
import java.util.logging.Level;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.data.JRTableModelDataSource;
import net.sf.jasperreports.view.JasperViewer;
import raven.toast.Notifications;
import java.util.logging.*;
import lk.corevision.fruitshop.model.SendLogo;

/**
 *
 * @author CoreVision Software Team
 * @version 1.0.0
 */
public class Supplier extends javax.swing.JPanel {

    private static HashMap<String, GRNItems> grnItemsMap = new HashMap<>();
    private static HashMap<String, String> supplierMap = new HashMap<>();
    private static HashMap<String, String> rawmaterialMap = new HashMap<>();
    private HashMap<String, String> paymentMethodMap = new HashMap<>();
    private HashMap<String, String> transactionTypeMap = new HashMap<>();

    private static HashMap<String, GRNTransaction> grnTransactionMap = new HashMap<>();

    public javax.swing.JTextField getjTextField17() {
        return jTextField17;
    }
    private Dashboard dashboard;
    private double totalAmount = 0;
    private PieChart pieChart1;

    public Supplier(Dashboard dashboard) {

        initComponents();
        this.dashboard = dashboard;

        String employeeNIC = SignIn.getEmployeeNIC();

        loadSuppliers();
        loadSupplierProduct();
        generateGRNId();
        supplierDropdown();
        updateSupplierBtn.setEnabled(false);
        updateRawmaterialBtn.setEnabled(false);
//        loadFinance();
//        transactionTypeDropdown();
        paymentMethodDropdown();
        rawmaterialDropdown();

//        transactionId();
        loadGRNFinances();
        generateGRNId();
        loadSupplierPieChart();

    }

    private void loadSupplierPieChart() {
        pieChart1 = new PieChart();
        pieChart1.setChartType(PieChart.PeiChartType.DEFAULT);
        pieChart1.clearData();

        String query = "SELECT CONCAT(supplier.fname, ' ', supplier.lname) AS supplier_name, COUNT(raw_material.raw_material_name) AS "
                + "raw_material_count FROM supplier INNER JOIN raw_material ON raw_material.supplier_supplier_id = supplier.supplier_id "
                + "GROUP BY supplier.supplier_id, supplier.fname, supplier.lname";

        try {
            ResultSet resultSet = MySQL.executeSearch(query);
            boolean hasData = false;
            int index = 0;

            while (resultSet.next()) {
                hasData = true;
                String name = resultSet.getString("supplier_name");
                double count = resultSet.getDouble("raw_material_count");
                pieChart1.addData(new ModelPieChart(name, count, getColor(index++)));
            }

            if (!hasData) {
                pieChart1.addData(new ModelPieChart("Utilities", 500, getColor(index++)));
                pieChart1.addData(new ModelPieChart("Salaries", 3000, getColor(index++)));
                pieChart1.addData(new ModelPieChart("Maintenance", 800, getColor(index++)));
                pieChart1.addData(new ModelPieChart("Miscellaneous", 200, getColor(index++)));
            }

        } catch (Exception e) {
            SignIn.getLoggerObject().log(Level.WARNING, "loadSupplierPieChart method exception found", e);
        }

        pieChart1.setBounds(0, 0, pieChart1supplier.getWidth(), pieChart1supplier.getHeight());
        pieChart1supplier.setLayout(new BorderLayout());
        pieChart1supplier.add(pieChart1, BorderLayout.CENTER);
        pieChart1supplier.revalidate();
        pieChart1supplier.repaint();
    }

    private Color getColor(int index) {
        Color[] colors = new Color[]{
            new Color(106, 27, 154), // Dark Purple
            new Color(230, 81, 0), // Dark Orange
            new Color(93, 64, 55), // Dark Brown
            new Color(0, 150, 136), // Dark Cyan
            new Color(21, 101, 192), // Dark Blue
            new Color(56, 142, 60), // Dark Green
            new Color(204, 192, 0), // Dark Yellow
            new Color(183, 32, 26) // Dark Red
        };
        return colors[index % colors.length];
    }

    private void loadGRNTransactions() {
        DefaultTableModel model = (DefaultTableModel) grnTable.getModel();
        model.setRowCount(0);

        totalAmount = 0;
        for (GRNItems loadgrnitem : grnItemsMap.values()) {
            Vector<String> vector = new Vector<>();

            vector.add(loadgrnitem.getRawmaterialName());
            vector.add(loadgrnitem.getQty());
            vector.add(loadgrnitem.getUnitPrice());

            double itemTotal = Double.parseDouble(loadgrnitem.getQty()) * Double.parseDouble(loadgrnitem.getUnitPrice());
            totalAmount += itemTotal;
            vector.add(String.valueOf(itemTotal));

            model.addRow(vector);
        }
        grnTotalfield.setText(String.valueOf(totalAmount));
        if (totalAmount != 0) {
            saveGRNbtn.setEnabled(true);
        } else {
            saveGRNbtn.setEnabled(false);
        }

    }

//    public void setAddressText(String address) {
//        jTextArea1.setText(address);
//    }
    private void paymentMethodDropdown() {
        try {
            ResultSet resultSet = MySQL.executeSearch("SELECT * FROM `payment_method`");

            Vector<String> vector = new Vector<>();
            vector.add("Select");

            while (resultSet.next()) {
                String paymentMethod = resultSet.getString("payment_method");
                vector.add(paymentMethod);

                paymentMethodMap.put(paymentMethod, resultSet.getString("payment_method_id"));
            }

            DefaultComboBoxModel<String> model = new DefaultComboBoxModel<>(vector);

            jComboBoxGRNPaymentMethod.setModel(model);

        } catch (Exception e) {
            SignIn.getLoggerObject().log(Level.WARNING, "paymentMethodDropdown method exception found", e);
        }
    }

//    private void transactionTypeDropdown() {
//        try {
//            ResultSet resultSet = MySQL.executeSearch("SELECT * FROM `transaction_type`");
//
//            Vector<String> vector = new Vector<>();
//            vector.add("Select");
//
//            while (resultSet.next()) {
//                String transactionType = resultSet.getString("transaction_type_name");
//                vector.add(transactionType);
//                transactionTypeMap.put(transactionType, resultSet.getString("transaction_type_id"));
//            }
//
//            DefaultComboBoxModel<String> model = new DefaultComboBoxModel<>(vector);
//            jComboBoxTransactionType.setModel(model);
//
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }
//    private void loadFinance() {
//        try {
//            String query = "SELECT `finance`.`transaction_type_id`, `transaction_type`.`transaction_type_name`, `finance`.`amount`, `finance`.`description`, `finance`.`date`, `payment_method`.`payment_method` AS `payment_method_name` FROM `finance` INNER JOIN `transaction_type` ON `finance`.`transaction_type_id` = `transaction_type`.`transaction_type_id` INNER JOIN `payment_method` ON `finance`.`payment_method_id` = `payment_method`.`payment_method_id`;";
//
//            ResultSet resultSet = MySQL.executeSearch(query);
//
//            DefaultTableModel model = (DefaultTableModel) jTableFinance.getModel();
//            model.setRowCount(0);
//
//            double total = 0;
//
//            while (resultSet.next()) {
//                Vector<String> vector = new Vector<>();
//                vector.add(resultSet.getString("transaction_type_id"));
//                vector.add(resultSet.getString("description"));
//                vector.add(resultSet.getString("transaction_type_name"));
//                vector.add(resultSet.getString("payment_method_name"));
//                vector.add(resultSet.getString("amount"));
//                vector.add(resultSet.getString("date"));
//
//                model.addRow(vector);
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }
    private void loadGRNFinances() {
        try {
            String query = "SELECT `grn`.`grn_id`, `grn`.`date`, `grn`.`paid_amount`, `payment_status`.`payment_status`, CONCAT(`supplier`.`fname`, ' ', `supplier`.`lname`) AS `supplier_name` FROM `grn` INNER JOIN `supplier` ON `grn`.`supplier_id` = `supplier`.`supplier_id` INNER JOIN `payment_status` ON `grn`.`payment_status_id` = `payment_status`.`payment_status_id` WHERE `grn`.`grn_id` LIKE '%" + ViewGRNId.getText() + "%' AND CONCAT(`supplier`.`fname`, ' ', `supplier`.`lname`) LIKE '%" + viewGRNSuppliername.getText() + "%'";

            Date startDate = null;
            Date endDate = null;
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");

            if (viewGRNDateFrom.getDate() != null) {
                startDate = viewGRNDateFrom.getDate();
                query += "AND `grn`.`date` >= '" + format.format(startDate) + "' ";
            }
            if (viewGRNDateTo.getDate() != null) {
                endDate = viewGRNDateTo.getDate();
                query += "AND `grn`.`date` <= '" + format.format(endDate) + "' ";
            }

            query += "ORDER BY ";
            switch (viewGRNComboBox.getSelectedIndex()) {
                case 0:
                    query += "`grn`.`grn_id` ASC";
                    break;
                case 1:
                    query += "`grn`.`grn_id` DESC";
                    break;
                case 2:
                    query += "`supplier`.`fname` ASC";
                    break;
                case 3:
                    query += "`supplier`.`fname` DESC";
                    break;
                case 4:
                    query += "`grn`.`paid_amount` ASC";
                    break;
                case 5:
                    query += "`grn`.`paid_amount` DESC";
                    break;
                default:
                    query += "`grn`.`grn_id` ASC";
            }

            ResultSet resultSet = MySQL.executeSearch(query);
            DefaultTableModel model = (DefaultTableModel) viewGRNtable.getModel();
            model.setRowCount(0);

            double total = 0;
            while (resultSet.next()) {
                Vector<String> vector = new Vector<>();
                vector.add(resultSet.getString("grn_id"));
                vector.add(resultSet.getString("supplier_name"));
                vector.add(resultSet.getString("paid_amount"));
                vector.add(resultSet.getString("date"));
                vector.add(resultSet.getString("payment_status"));

                model.addRow(vector);
                total += resultSet.getDouble("paid_amount");
            }

        } catch (Exception e) {
            SignIn.getLoggerObject().log(Level.WARNING, "Error loading the GRN Finances method exception found", e);
        }
    }

    private void supplierDropdown() {
        try {
            ResultSet resultSet = MySQL.executeSearch("SELECT * FROM `supplier`");

            Vector<String> vector = new Vector<>();
            vector.add("Select");

            while (resultSet.next()) {
                String fullName = resultSet.getString("fname") + " " + resultSet.getString("lname");
                vector.add(fullName);
                supplierMap.put(fullName, resultSet.getString("supplier_id"));
            }
            DefaultComboBoxModel<String> model = new DefaultComboBoxModel<>(vector);
            jComboBoxSupplier.setModel(model);
            jComboBox1.setModel(model);

        } catch (Exception e) {
            SignIn.getLoggerObject().log(Level.WARNING, "Supplier dropdown method exception found", e);
        }
    }

    private void rawmaterialDropdown() {
        try {
            ResultSet resultSet = MySQL.executeSearch("SELECT * FROM `raw_material`");

            Vector<String> vector = new Vector<>();
            vector.add("Select");

            while (resultSet.next()) {
                String material = resultSet.getString("raw_material_name");
                vector.add(material);

                rawmaterialMap.put(material, resultSet.getString("raw_material_id"));

            }

            DefaultComboBoxModel model = new DefaultComboBoxModel(vector);

            jComboBoxRawmaterial.setModel(model);

        } catch (Exception e) {
            SignIn.getLoggerObject().log(Level.WARNING, "raw material Dropdown method exception found", e);
        }
    }

    private void generateGRNId() {
        long GRNid = System.currentTimeMillis();
        grnNumberTextField.setText(String.valueOf(GRNid));
        grnNumberTextField.setEditable(false);
    }

//    private void transactionId() {
//        long id = System.currentTimeMillis();
//        jTextFieldTransactionID.setText(String.valueOf(id));
//        jTextFieldTransactionID.setEditable(false);
//    }
    private void loadSupplierProduct() {
        try {
            String rawmaterialName = jTextField18.getText();
            String query = "SELECT `raw_material`.`raw_material_name`, `raw_material`.`unit_price`, `raw_material`.`qty`, "
                    + "`brand`.`brand_name`, CONCAT(`supplier`.`fname`, ' ', `supplier`.`lname`) AS `supplier_name` "
                    + "FROM `raw_material` "
                    + "INNER JOIN `brand` ON `raw_material`.`brand_brand_id` = `brand`.`brand_id` "
                    + "INNER JOIN `supplier` ON `raw_material`.`supplier_supplier_id` = `supplier`.`supplier_id` "
                    + "WHERE `raw_material`.`raw_material_name` LIKE '%" + rawmaterialName + "%' AND ";

            query += "ORDER BY  ";
            query = query.replace("AND ORDER BY ", "ORDER BY ");

            if (sortRawmaterialComboBox.getSelectedIndex() == 0) {
                query += "`raw_material`.`raw_material_name` DESC";
            } else if (sortRawmaterialComboBox.getSelectedIndex() == 1) {
                query += "`raw_material`.`raw_material_name` ASC";
            } else if (sortRawmaterialComboBox.getSelectedIndex() == 2) {
                query += "`raw_material`.`qty` ASC";
            } else if (sortRawmaterialComboBox.getSelectedIndex() == 3) {
                query += "`raw_material`.`qty` DESC";
            } else if (sortRawmaterialComboBox.getSelectedIndex() == 4) {
                query += "`raw_material`.`unit_price` ASC";
            } else if (sortRawmaterialComboBox.getSelectedIndex() == 5) {
                query += "`raw_material`.`unit_price` DESC";
            }
            ResultSet resultSet = MySQL.executeSearch(query);

            DefaultTableModel model = (DefaultTableModel) jTable3.getModel();
            model.setRowCount(0);

            while (resultSet.next()) {
                Vector<String> vector = new Vector<>();
                vector.add(resultSet.getString("raw_material_name"));
                vector.add(resultSet.getString("brand_name"));
                vector.add(resultSet.getString("unit_price"));
                vector.add(resultSet.getString("qty"));
                vector.add(resultSet.getString("supplier_name"));

                model.addRow(vector);
            }
        } catch (Exception e) {
            SignIn.getLoggerObject().log(Level.WARNING, "loadSupplierProduct method exception found", e);
        }
    }

    private void loadSuppliers() {

        String suppContact = jTextField11.getText();

        try {
            String query
                    = "SELECT supplier.supplier_id, supplier.email, supplier.fname, supplier.lname, supplier.contact, supplier_status.supplier_status, COALESCE(CONCAT(supplier_address.line1, ', ', supplier_address.line2, ', ', city.city_name), 'No Address') AS full_address FROM "
                    + "`supplier` LEFT JOIN `supplier_address` ON `supplier`.`supplier_id` = `supplier_address`.`supplier_id` LEFT JOIN "
                    + "`city` ON `supplier_address`.`city_city_id` = `city`.`city_id` LEFT JOIN "
                    + "`supplier_status` ON "
                    + "`supplier`.`supplier_status_id` = `supplier_status`.`supplier_status_id` "
                    + "WHERE `supplier`.`contact` LIKE '%" + suppContact + "%' ";

//            query += "ORDER BY ";
//            query = query.replace("AND ORDER BY ", "ORDER BY ");
            if (sortSuppliersComboBox.getSelectedIndex() == 0) {
                query += "ORDER BY `supplier`.`email` DESC";
            } else if (sortSuppliersComboBox.getSelectedIndex() == 1) {
                query += "ORDER BY `supplier`.`email` ASC";
            } else if (sortSuppliersComboBox.getSelectedIndex() == 2) {
                query += "ORDER BY `supplier`.`fname` DESC";
            } else if (sortSuppliersComboBox.getSelectedIndex() == 3) {
                query += "ORDER BY `supplier`.`fname` ASC";
            } else if (sortSuppliersComboBox.getSelectedIndex() == 4) {
                query += "AND `supplier_status`.`supplier_status` = 'Active' ORDER BY `supplier`.`fname` ASC";
            } else if (sortSuppliersComboBox.getSelectedIndex() == 5) {
                query += "AND `supplier_status`.`supplier_status` = 'Inactive' ORDER BY `supplier`.`fname` ASC";
            }

            ResultSet resultSet = MySQL.executeSearch(query);

            DefaultTableModel model = (DefaultTableModel) jTable1.getModel();
            model.setRowCount(0);

            while (resultSet.next()) {
                Vector<String> vector = new Vector<>();
                vector.add(resultSet.getString("supplier_id"));
                vector.add(resultSet.getString("fname"));
                vector.add(resultSet.getString("lname"));
                vector.add(resultSet.getString("email"));
                vector.add(resultSet.getString("contact"));
                vector.add(resultSet.getString("full_address"));
                vector.add(resultSet.getString("supplier_status"));

                model.addRow(vector);
            }
        } catch (Exception e) {
            SignIn.getLoggerObject().log(Level.WARNING, "loadSuppliers method exception found", e);
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

        jRadioButton1 = new javax.swing.JRadioButton();
        jLayeredPane1 = new javax.swing.JLayeredPane();
        supplierPopupMenu = new javax.swing.JPopupMenu();
        supplierAddressMenuItem = new javax.swing.JMenuItem();
        updateSupplierMenuItem = new javax.swing.JMenuItem();
        changeSupplierStatusMenuItem = new javax.swing.JMenuItem();
        rawmaterialPopupMenu = new javax.swing.JPopupMenu();
        updateRawmaterialMenuItem = new javax.swing.JMenuItem();
        removeRawmaterialMenuItem = new javax.swing.JMenuItem();
        financePopupMenu = new javax.swing.JPopupMenu();
        removeFinanceMenuItem = new javax.swing.JMenuItem();
        grnPopupMenu = new javax.swing.JPopupMenu();
        updateGRNMenuItem = new javax.swing.JMenuItem();
        jPanel1 = new javax.swing.JPanel();
        jTabbedPane1 = new javax.swing.JTabbedPane();
        jPanel2 = new javax.swing.JPanel();
        jPanel7 = new javax.swing.JPanel();
        jPanel11 = new javax.swing.JPanel();
        jButton6 = new javax.swing.JButton();
        jButton3 = new javax.swing.JButton();
        jButton4 = new javax.swing.JButton();
        jButton5 = new javax.swing.JButton();
        jPanel14 = new javax.swing.JPanel();
        jPanel15 = new javax.swing.JPanel();
        pieChart1supplier = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jPanel5 = new javax.swing.JPanel();
        jPanel19 = new javax.swing.JPanel();
        jPanel32 = new javax.swing.JPanel();
        jLabel62 = new javax.swing.JLabel();
        jLabel63 = new javax.swing.JLabel();
        jLabel64 = new javax.swing.JLabel();
        jTextField17 = new javax.swing.JTextField();
        jLabel65 = new javax.swing.JLabel();
        addRawmaterialBtn = new javax.swing.JButton();
        updateRawmaterialBtn = new javax.swing.JButton();
        jLabel66 = new javax.swing.JLabel();
        jTextField18 = new javax.swing.JTextField();
        jTextField19 = new javax.swing.JTextField();
        jLabel67 = new javax.swing.JLabel();
        jButton28 = new javax.swing.JButton();
        jButton29 = new javax.swing.JButton();
        jTextField20 = new javax.swing.JTextField();
        jComboBox1 = new javax.swing.JComboBox<>();
        jPanel18 = new javax.swing.JPanel();
        jLabel18 = new javax.swing.JLabel();
        jPanel13 = new javax.swing.JPanel();
        rawmaterialClearbtn = new javax.swing.JButton();
        sortRawmaterialComboBox = new javax.swing.JComboBox<>();
        jLabel49 = new javax.swing.JLabel();
        jScrollPane5 = new javax.swing.JScrollPane();
        jTable3 = new javax.swing.JTable();
        jPanel3 = new javax.swing.JPanel();
        jPanel9 = new javax.swing.JPanel();
        jPanel10 = new javax.swing.JPanel();
        jTextField1 = new javax.swing.JTextField();
        jLabel26 = new javax.swing.JLabel();
        jLabel28 = new javax.swing.JLabel();
        jTextField2 = new javax.swing.JTextField();
        jLabel29 = new javax.swing.JLabel();
        addSupplierBtn = new javax.swing.JButton();
        updateSupplierBtn = new javax.swing.JButton();
        jLabel30 = new javax.swing.JLabel();
        jTextField3 = new javax.swing.JTextField();
        jTextField11 = new javax.swing.JTextField();
        jLabel41 = new javax.swing.JLabel();
        jButton17 = new javax.swing.JButton();
        supplierSortClearbtn = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTable1 = new javax.swing.JTable();
        jLabel59 = new javax.swing.JLabel();
        sortSuppliersComboBox = new javax.swing.JComboBox<>();
        jPanel8 = new javax.swing.JPanel();
        jLabel25 = new javax.swing.JLabel();
        jButton8 = new javax.swing.JButton();
        jPanel25 = new javax.swing.JPanel();
        jPanel29 = new javax.swing.JPanel();
        jLabel43 = new javax.swing.JLabel();
        jLabel44 = new javax.swing.JLabel();
        jLabel45 = new javax.swing.JLabel();
        jLabel46 = new javax.swing.JLabel();
        jButton16 = new javax.swing.JButton();
        jLabel47 = new javax.swing.JLabel();
        grnNumberTextField = new javax.swing.JTextField();
        grnQtyTextField = new javax.swing.JTextField();
        jLabel48 = new javax.swing.JLabel();
        jButton21 = new javax.swing.JButton();
        grnUnitpriceTextField = new javax.swing.JTextField();
        jComboBoxRawmaterial = new javax.swing.JComboBox<>();
        jComboBoxSupplier = new javax.swing.JComboBox<>();
        jScrollPane9 = new javax.swing.JScrollPane();
        grnTable = new javax.swing.JTable();
        jLabel60 = new javax.swing.JLabel();
        jComboBoxGRNPaymentMethod = new javax.swing.JComboBox<>();
        jPanel27 = new javax.swing.JPanel();
        jLabel42 = new javax.swing.JLabel();
        supplierSortClearbtn1 = new javax.swing.JButton();
        jPanel28 = new javax.swing.JPanel();
        jPanel30 = new javax.swing.JPanel();
        jLabel51 = new javax.swing.JLabel();
        jLabel52 = new javax.swing.JLabel();
        grnTotalfield = new javax.swing.JLabel();
        jLabel54 = new javax.swing.JLabel();
        grnbalanceLabel = new javax.swing.JLabel();
        saveGRNbtn = new javax.swing.JButton();
        paymentgrnFormattedTextField = new javax.swing.JFormattedTextField();
        jPanel4 = new javax.swing.JPanel();
        jPanel33 = new javax.swing.JPanel();
        jScrollPane10 = new javax.swing.JScrollPane();
        viewGRNtable = new javax.swing.JTable();
        jPanel31 = new javax.swing.JPanel();
        jLabel53 = new javax.swing.JLabel();
        jPanel12 = new javax.swing.JPanel();
        jLabel58 = new javax.swing.JLabel();
        jLabel56 = new javax.swing.JLabel();
        ViewGRNId = new javax.swing.JTextField();
        viewGRNSuppliername = new javax.swing.JTextField();
        jLabel55 = new javax.swing.JLabel();
        viewGRNComboBox = new javax.swing.JComboBox<>();
        viewGRNDateFrom = new com.toedter.calendar.JDateChooser();
        jButton7 = new javax.swing.JButton();
        jLabel57 = new javax.swing.JLabel();
        viewGRNDateTo = new com.toedter.calendar.JDateChooser();
        jButton9 = new javax.swing.JButton();

        jRadioButton1.setText("jRadioButton1");

        javax.swing.GroupLayout jLayeredPane1Layout = new javax.swing.GroupLayout(jLayeredPane1);
        jLayeredPane1.setLayout(jLayeredPane1Layout);
        jLayeredPane1Layout.setHorizontalGroup(
            jLayeredPane1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 100, Short.MAX_VALUE)
        );
        jLayeredPane1Layout.setVerticalGroup(
            jLayeredPane1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 100, Short.MAX_VALUE)
        );

        supplierPopupMenu.addMenuKeyListener(new javax.swing.event.MenuKeyListener() {
            public void menuKeyPressed(javax.swing.event.MenuKeyEvent evt) {
            }
            public void menuKeyReleased(javax.swing.event.MenuKeyEvent evt) {
            }
            public void menuKeyTyped(javax.swing.event.MenuKeyEvent evt) {
                supplierPopupMenuMenuKeyTyped(evt);
            }
        });

        supplierAddressMenuItem.setText("Add/Update address");
        supplierAddressMenuItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                supplierAddressMenuItemActionPerformed(evt);
            }
        });
        supplierPopupMenu.add(supplierAddressMenuItem);

        updateSupplierMenuItem.setText("Update Supplier");
        updateSupplierMenuItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                updateSupplierMenuItemActionPerformed(evt);
            }
        });
        supplierPopupMenu.add(updateSupplierMenuItem);

        changeSupplierStatusMenuItem.setText("Change status");
        changeSupplierStatusMenuItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                changeSupplierStatusMenuItemActionPerformed(evt);
            }
        });
        supplierPopupMenu.add(changeSupplierStatusMenuItem);

        updateRawmaterialMenuItem.setText("Update rawmaterial");
        updateRawmaterialMenuItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                updateRawmaterialMenuItemActionPerformed(evt);
            }
        });
        rawmaterialPopupMenu.add(updateRawmaterialMenuItem);

        removeRawmaterialMenuItem.setText("Remove raw material");
        removeRawmaterialMenuItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                removeRawmaterialMenuItemActionPerformed(evt);
            }
        });
        rawmaterialPopupMenu.add(removeRawmaterialMenuItem);

        removeFinanceMenuItem.setText("Remove Transaction");
        removeFinanceMenuItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                removeFinanceMenuItemActionPerformed(evt);
            }
        });
        financePopupMenu.add(removeFinanceMenuItem);

        updateGRNMenuItem.setText("Update GRN");
        updateGRNMenuItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                updateGRNMenuItemActionPerformed(evt);
            }
        });
        grnPopupMenu.add(updateGRNMenuItem);

        setLayout(new java.awt.BorderLayout());

        jPanel1.setLayout(new java.awt.BorderLayout());

        jPanel2.setPreferredSize(new java.awt.Dimension(487, 614));
        jPanel2.setLayout(new java.awt.BorderLayout());

        jPanel7.setLayout(new java.awt.BorderLayout());

        jPanel11.setPreferredSize(new java.awt.Dimension(287, 200));
        jPanel11.setLayout(new java.awt.GridLayout(1, 5, 1, 1));

        jButton6.setFont(new java.awt.Font("Serif", 1, 20)); // NOI18N
        jButton6.setIcon(new javax.swing.ImageIcon(getClass().getResource("/lk/corevision/fruitshop/resources/supplier/icons8-product-48 (1).png"))); // NOI18N
        jButton6.setText("  Supplier Raw Materials");
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
        jPanel11.add(jButton6);

        jButton3.setFont(new java.awt.Font("Serif", 1, 20)); // NOI18N
        jButton3.setIcon(new javax.swing.ImageIcon(getClass().getResource("/lk/corevision/fruitshop/resources/supplier/icons8-supplier-64.png"))); // NOI18N
        jButton3.setText("  Supplier Management");
        jButton3.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                jButton3MouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                jButton3MouseExited(evt);
            }
        });
        jButton3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton3ActionPerformed(evt);
            }
        });
        jPanel11.add(jButton3);

        jButton4.setFont(new java.awt.Font("Serif", 1, 20)); // NOI18N
        jButton4.setIcon(new javax.swing.ImageIcon(getClass().getResource("/lk/corevision/fruitshop/resources/supplier/icons8-cash-64.png"))); // NOI18N
        jButton4.setText("  GRN");
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
        jPanel11.add(jButton4);

        jButton5.setFont(new java.awt.Font("Serif", 1, 20)); // NOI18N
        jButton5.setIcon(new javax.swing.ImageIcon(getClass().getResource("/lk/corevision/fruitshop/resources/supplier/icons8-invoice-50.png"))); // NOI18N
        jButton5.setText("  View GRN");
        jButton5.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                jButton5MouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                jButton5MouseExited(evt);
            }
        });
        jButton5.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton5ActionPerformed(evt);
            }
        });
        jPanel11.add(jButton5);

        jPanel7.add(jPanel11, java.awt.BorderLayout.CENTER);

        jPanel2.add(jPanel7, java.awt.BorderLayout.PAGE_START);

        pieChart1supplier.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        pieChart1supplier.setFont(new java.awt.Font("Serif", 0, 10)); // NOI18N
        pieChart1supplier.setPreferredSize(new java.awt.Dimension(400, 400));

        javax.swing.GroupLayout pieChart1supplierLayout = new javax.swing.GroupLayout(pieChart1supplier);
        pieChart1supplier.setLayout(pieChart1supplierLayout);
        pieChart1supplierLayout.setHorizontalGroup(
            pieChart1supplierLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 432, Short.MAX_VALUE)
        );
        pieChart1supplierLayout.setVerticalGroup(
            pieChart1supplierLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 455, Short.MAX_VALUE)
        );

        jLabel1.setFont(new java.awt.Font("Serif", 1, 14)); // NOI18N
        jLabel1.setText("Supplier Raw Materials Distribution");

        javax.swing.GroupLayout jPanel15Layout = new javax.swing.GroupLayout(jPanel15);
        jPanel15.setLayout(jPanel15Layout);
        jPanel15Layout.setHorizontalGroup(
            jPanel15Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel15Layout.createSequentialGroup()
                .addGroup(jPanel15Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel15Layout.createSequentialGroup()
                        .addGap(57, 57, 57)
                        .addComponent(pieChart1supplier, javax.swing.GroupLayout.PREFERRED_SIZE, 434, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel15Layout.createSequentialGroup()
                        .addGap(114, 114, 114)
                        .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 253, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(101, Short.MAX_VALUE))
        );
        jPanel15Layout.setVerticalGroup(
            jPanel15Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel15Layout.createSequentialGroup()
                .addGap(20, 20, 20)
                .addComponent(pieChart1supplier, javax.swing.GroupLayout.PREFERRED_SIZE, 457, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jLabel1)
                .addContainerGap(87, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout jPanel14Layout = new javax.swing.GroupLayout(jPanel14);
        jPanel14.setLayout(jPanel14Layout);
        jPanel14Layout.setHorizontalGroup(
            jPanel14Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel14Layout.createSequentialGroup()
                .addGap(20, 20, 20)
                .addComponent(jPanel15, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(891, Short.MAX_VALUE))
        );
        jPanel14Layout.setVerticalGroup(
            jPanel14Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel14Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel15, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(143, Short.MAX_VALUE))
        );

        jPanel2.add(jPanel14, java.awt.BorderLayout.CENTER);

        jTabbedPane1.addTab("Dashboard", jPanel2);

        jPanel5.setLayout(new java.awt.BorderLayout());

        jPanel19.setPreferredSize(new java.awt.Dimension(1160, 558));

        jLabel62.setFont(new java.awt.Font("Serif", 0, 14)); // NOI18N
        jLabel62.setText("Rawmaterial Name");

        jLabel63.setFont(new java.awt.Font("Serif", 0, 14)); // NOI18N
        jLabel63.setText("Unit price");

        jLabel64.setFont(new java.awt.Font("Serif", 0, 14)); // NOI18N
        jLabel64.setText("Brand");

        jLabel65.setFont(new java.awt.Font("Serif", 1, 24)); // NOI18N
        jLabel65.setText("Product Raw material details");

        addRawmaterialBtn.setFont(new java.awt.Font("Serif", 1, 14)); // NOI18N
        addRawmaterialBtn.setText("Add product");
        addRawmaterialBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                addRawmaterialBtnActionPerformed(evt);
            }
        });

        updateRawmaterialBtn.setFont(new java.awt.Font("Serif", 1, 14)); // NOI18N
        updateRawmaterialBtn.setText("Update");
        updateRawmaterialBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                updateRawmaterialBtnActionPerformed(evt);
            }
        });

        jLabel66.setFont(new java.awt.Font("Serif", 0, 14)); // NOI18N
        jLabel66.setText("Supplier");

        jTextField18.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextField18ActionPerformed(evt);
            }
        });
        jTextField18.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                jTextField18KeyReleased(evt);
            }
        });

        jTextField19.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextField19ActionPerformed(evt);
            }
        });

        jLabel67.setFont(new java.awt.Font("Serif", 0, 14)); // NOI18N
        jLabel67.setText("Quantity");

        jButton28.setFont(new java.awt.Font("Serif", 1, 14)); // NOI18N
        jButton28.setText("Report");
        jButton28.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton28ActionPerformed(evt);
            }
        });

        jButton29.setFont(new java.awt.Font("Serif", 1, 14)); // NOI18N
        jButton29.setText("Select Brand");
        jButton29.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton29ActionPerformed(evt);
            }
        });

        jComboBox1.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));

        javax.swing.GroupLayout jPanel32Layout = new javax.swing.GroupLayout(jPanel32);
        jPanel32.setLayout(jPanel32Layout);
        jPanel32Layout.setHorizontalGroup(
            jPanel32Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel32Layout.createSequentialGroup()
                .addGroup(jPanel32Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(jPanel32Layout.createSequentialGroup()
                        .addGap(41, 41, 41)
                        .addComponent(jLabel65, javax.swing.GroupLayout.PREFERRED_SIZE, 367, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel32Layout.createSequentialGroup()
                        .addGap(206, 206, 206)
                        .addComponent(jButton29, javax.swing.GroupLayout.PREFERRED_SIZE, 231, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel32Layout.createSequentialGroup()
                        .addGap(14, 14, 14)
                        .addComponent(addRawmaterialBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 187, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(updateRawmaterialBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 224, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel32Layout.createSequentialGroup()
                        .addGap(104, 104, 104)
                        .addComponent(jButton28, javax.swing.GroupLayout.PREFERRED_SIZE, 214, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel32Layout.createSequentialGroup()
                        .addGap(14, 14, 14)
                        .addGroup(jPanel32Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel32Layout.createSequentialGroup()
                                .addComponent(jLabel64, javax.swing.GroupLayout.PREFERRED_SIZE, 74, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(jTextField17, javax.swing.GroupLayout.PREFERRED_SIZE, 237, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(jPanel32Layout.createSequentialGroup()
                                .addGroup(jPanel32Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel66)
                                    .addGroup(jPanel32Layout.createSequentialGroup()
                                        .addGap(3, 3, 3)
                                        .addComponent(jLabel62)))
                                .addGap(79, 79, 79)
                                .addGroup(jPanel32Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jTextField18, javax.swing.GroupLayout.PREFERRED_SIZE, 234, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jComboBox1, javax.swing.GroupLayout.PREFERRED_SIZE, 231, javax.swing.GroupLayout.PREFERRED_SIZE)))))
                    .addGroup(jPanel32Layout.createSequentialGroup()
                        .addGap(14, 14, 14)
                        .addGroup(jPanel32Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel63, javax.swing.GroupLayout.PREFERRED_SIZE, 98, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel67, javax.swing.GroupLayout.PREFERRED_SIZE, 98, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(94, 94, 94)
                        .addGroup(jPanel32Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jTextField20, javax.swing.GroupLayout.PREFERRED_SIZE, 231, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jTextField19, javax.swing.GroupLayout.PREFERRED_SIZE, 231, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel32Layout.setVerticalGroup(
            jPanel32Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel32Layout.createSequentialGroup()
                .addGap(25, 25, 25)
                .addComponent(jLabel65)
                .addGap(30, 30, 30)
                .addGroup(jPanel32Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel62)
                    .addGroup(jPanel32Layout.createSequentialGroup()
                        .addGap(2, 2, 2)
                        .addComponent(jTextField18, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel32Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel66)
                    .addComponent(jComboBox1, javax.swing.GroupLayout.PREFERRED_SIZE, 29, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGroup(jPanel32Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel32Layout.createSequentialGroup()
                        .addGap(20, 20, 20)
                        .addComponent(jLabel64))
                    .addGroup(jPanel32Layout.createSequentialGroup()
                        .addGap(18, 18, 18)
                        .addComponent(jTextField17, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jButton29)
                .addGroup(jPanel32Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel32Layout.createSequentialGroup()
                        .addGap(15, 15, 15)
                        .addComponent(jLabel67))
                    .addGroup(jPanel32Layout.createSequentialGroup()
                        .addGap(13, 13, 13)
                        .addComponent(jTextField19, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(7, 7, 7)
                .addGroup(jPanel32Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel32Layout.createSequentialGroup()
                        .addGap(12, 12, 12)
                        .addComponent(jLabel63))
                    .addComponent(jTextField20, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(51, 51, 51)
                .addGroup(jPanel32Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(addRawmaterialBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 38, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(updateRawmaterialBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 38, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addComponent(jButton28, javax.swing.GroupLayout.PREFERRED_SIZE, 38, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(35, Short.MAX_VALUE))
        );

        jPanel18.setBackground(new java.awt.Color(204, 204, 204));

        jLabel18.setFont(new java.awt.Font("Serif", 1, 24)); // NOI18N
        jLabel18.setText("Supplier Product Management");

        javax.swing.GroupLayout jPanel18Layout = new javax.swing.GroupLayout(jPanel18);
        jPanel18.setLayout(jPanel18Layout);
        jPanel18Layout.setHorizontalGroup(
            jPanel18Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel18Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jLabel18, javax.swing.GroupLayout.PREFERRED_SIZE, 368, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(365, 365, 365))
        );
        jPanel18Layout.setVerticalGroup(
            jPanel18Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel18Layout.createSequentialGroup()
                .addComponent(jLabel18, javax.swing.GroupLayout.PREFERRED_SIZE, 52, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
        );

        rawmaterialClearbtn.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        rawmaterialClearbtn.setIcon(new javax.swing.ImageIcon(getClass().getResource("/lk/corevision/fruitshop/resources/supplier/clear_all.png"))); // NOI18N
        rawmaterialClearbtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rawmaterialClearbtnActionPerformed(evt);
            }
        });

        sortRawmaterialComboBox.setFont(new java.awt.Font("Serif", 0, 12)); // NOI18N
        sortRawmaterialComboBox.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Raw material Name Descending order", "Raw material Name Ascending order", "Quantity Ascending order", "Quantity Descending order", "Unit Price Ascending order", "Unit Price Descending order" }));
        sortRawmaterialComboBox.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                sortRawmaterialComboBoxItemStateChanged(evt);
            }
        });

        jLabel49.setFont(new java.awt.Font("Serif", 0, 12)); // NOI18N
        jLabel49.setText("Sort As");

        jTable3.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Name", "Brand", "Unit price", "Quantity", "Supplier"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jTable3.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                jTable3MouseReleased(evt);
            }
        });
        jScrollPane5.setViewportView(jTable3);

        javax.swing.GroupLayout jPanel13Layout = new javax.swing.GroupLayout(jPanel13);
        jPanel13.setLayout(jPanel13Layout);
        jPanel13Layout.setHorizontalGroup(
            jPanel13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel13Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel13Layout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(jLabel49)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(sortRawmaterialComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(54, 54, 54)
                        .addComponent(rawmaterialClearbtn, javax.swing.GroupLayout.PREFERRED_SIZE, 59, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(56, 56, 56))
                    .addComponent(jScrollPane5, javax.swing.GroupLayout.DEFAULT_SIZE, 1010, Short.MAX_VALUE))
                .addContainerGap())
        );
        jPanel13Layout.setVerticalGroup(
            jPanel13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel13Layout.createSequentialGroup()
                .addGap(34, 34, 34)
                .addGroup(jPanel13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(rawmaterialClearbtn, javax.swing.GroupLayout.PREFERRED_SIZE, 41, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanel13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(sortRawmaterialComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jLabel49, javax.swing.GroupLayout.PREFERRED_SIZE, 29, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane5, javax.swing.GroupLayout.DEFAULT_SIZE, 403, Short.MAX_VALUE)
                .addContainerGap())
        );

        javax.swing.GroupLayout jPanel19Layout = new javax.swing.GroupLayout(jPanel19);
        jPanel19.setLayout(jPanel19Layout);
        jPanel19Layout.setHorizontalGroup(
            jPanel19Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel18, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(jPanel19Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel32, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jPanel13, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel19Layout.setVerticalGroup(
            jPanel19Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel19Layout.createSequentialGroup()
                .addComponent(jPanel18, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(jPanel19Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel13, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jPanel32, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
        );

        jPanel5.add(jPanel19, java.awt.BorderLayout.PAGE_START);

        jTabbedPane1.addTab("Supplier Product", jPanel5);

        jPanel3.setLayout(new java.awt.BorderLayout());

        jTextField1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextField1ActionPerformed(evt);
            }
        });
        jTextField1.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                jTextField1KeyReleased(evt);
            }
        });

        jLabel26.setFont(new java.awt.Font("Serif", 0, 14)); // NOI18N
        jLabel26.setText("First Name");

        jLabel28.setFont(new java.awt.Font("Serif", 0, 14)); // NOI18N
        jLabel28.setText("Email");

        jTextField2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextField2ActionPerformed(evt);
            }
        });
        jTextField2.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                jTextField2KeyReleased(evt);
            }
        });

        jLabel29.setFont(new java.awt.Font("Serif", 1, 24)); // NOI18N
        jLabel29.setText("Supplier Details");

        addSupplierBtn.setFont(new java.awt.Font("Serif", 1, 14)); // NOI18N
        addSupplierBtn.setText("Add Supplier ");
        addSupplierBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                addSupplierBtnActionPerformed(evt);
            }
        });

        updateSupplierBtn.setFont(new java.awt.Font("Serif", 1, 14)); // NOI18N
        updateSupplierBtn.setText("Update ");
        updateSupplierBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                updateSupplierBtnActionPerformed(evt);
            }
        });

        jLabel30.setFont(new java.awt.Font("Serif", 0, 14)); // NOI18N
        jLabel30.setText("Last Name");

        jTextField3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextField3ActionPerformed(evt);
            }
        });
        jTextField3.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                jTextField3KeyReleased(evt);
            }
        });

        jTextField11.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                jTextField11KeyReleased(evt);
            }
        });

        jLabel41.setFont(new java.awt.Font("Serif", 0, 14)); // NOI18N
        jLabel41.setText("Contact");

        jButton17.setFont(new java.awt.Font("Serif", 1, 14)); // NOI18N
        jButton17.setText("Report");
        jButton17.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton17ActionPerformed(evt);
            }
        });

        supplierSortClearbtn.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        supplierSortClearbtn.setIcon(new javax.swing.ImageIcon(getClass().getResource("/lk/corevision/fruitshop/resources/supplier/clear_all.png"))); // NOI18N
        supplierSortClearbtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                supplierSortClearbtnActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel10Layout = new javax.swing.GroupLayout(jPanel10);
        jPanel10.setLayout(jPanel10Layout);
        jPanel10Layout.setHorizontalGroup(
            jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel10Layout.createSequentialGroup()
                .addGroup(jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel10Layout.createSequentialGroup()
                        .addGap(32, 32, 32)
                        .addGroup(jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                            .addGroup(jPanel10Layout.createSequentialGroup()
                                .addComponent(addSupplierBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 187, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(updateSupplierBtn, javax.swing.GroupLayout.DEFAULT_SIZE, 214, Short.MAX_VALUE))
                            .addGroup(jPanel10Layout.createSequentialGroup()
                                .addGroup(jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel26)
                                    .addComponent(jLabel30)
                                    .addComponent(jLabel28, javax.swing.GroupLayout.PREFERRED_SIZE, 74, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jLabel41, javax.swing.GroupLayout.PREFERRED_SIZE, 98, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(82, 82, 82)
                                .addGroup(jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addComponent(jTextField2, javax.swing.GroupLayout.DEFAULT_SIZE, 231, Short.MAX_VALUE)
                                    .addComponent(jTextField1)
                                    .addComponent(jTextField11)
                                    .addComponent(jTextField3)))))
                    .addGroup(jPanel10Layout.createSequentialGroup()
                        .addGap(115, 115, 115)
                        .addComponent(jButton17, javax.swing.GroupLayout.PREFERRED_SIZE, 214, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(21, Short.MAX_VALUE))
            .addGroup(jPanel10Layout.createSequentialGroup()
                .addGap(41, 41, 41)
                .addComponent(jLabel29, javax.swing.GroupLayout.PREFERRED_SIZE, 232, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 93, Short.MAX_VALUE)
                .addComponent(supplierSortClearbtn, javax.swing.GroupLayout.PREFERRED_SIZE, 64, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(36, 36, 36))
        );
        jPanel10Layout.setVerticalGroup(
            jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel10Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel29)
                    .addComponent(supplierSortClearbtn, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(27, 27, 27)
                .addGroup(jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel26)
                    .addGroup(jPanel10Layout.createSequentialGroup()
                        .addGap(2, 2, 2)
                        .addComponent(jTextField3)))
                .addGap(18, 18, 18)
                .addGroup(jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel30)
                    .addGroup(jPanel10Layout.createSequentialGroup()
                        .addGap(2, 2, 2)
                        .addComponent(jTextField1)))
                .addGap(18, 18, 18)
                .addGroup(jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel28)
                    .addGroup(jPanel10Layout.createSequentialGroup()
                        .addGap(2, 2, 2)
                        .addComponent(jTextField2, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel41)
                    .addComponent(jTextField11))
                .addGap(18, 18, 18)
                .addGroup(jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(addSupplierBtn, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 38, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(updateSupplierBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 38, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jButton17, javax.swing.GroupLayout.PREFERRED_SIZE, 38, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(87, 87, 87))
        );

        jTable1.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Supplier ID", "First Name", "Last Name", "Email", "Contact", "Address", "Status"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jTable1.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                jTable1MouseReleased(evt);
            }
        });
        jScrollPane1.setViewportView(jTable1);

        jLabel59.setFont(new java.awt.Font("Serif", 0, 12)); // NOI18N
        jLabel59.setText("Sort As");

        sortSuppliersComboBox.setFont(new java.awt.Font("Serif", 0, 12)); // NOI18N
        sortSuppliersComboBox.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Email Descending order", "Email Ascending order", "Supplier first name Descending order", "Supplier first name Ascending order", "Active suppliers", "Inactive suppliers" }));
        sortSuppliersComboBox.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                sortSuppliersComboBoxItemStateChanged(evt);
            }
        });

        jPanel8.setBackground(new java.awt.Color(204, 204, 204));

        jLabel25.setFont(new java.awt.Font("Serif", 1, 24)); // NOI18N
        jLabel25.setText("Supplier Management");

        javax.swing.GroupLayout jPanel8Layout = new javax.swing.GroupLayout(jPanel8);
        jPanel8.setLayout(jPanel8Layout);
        jPanel8Layout.setHorizontalGroup(
            jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel8Layout.createSequentialGroup()
                .addGap(293, 293, 293)
                .addComponent(jLabel25, javax.swing.GroupLayout.PREFERRED_SIZE, 367, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel8Layout.setVerticalGroup(
            jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel8Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jLabel25)
                .addGap(17, 17, 17))
        );

        jButton8.setIcon(new javax.swing.ImageIcon(getClass().getResource("/lk/corevision/fruitshop/resources/supplier/reset-black-25.png"))); // NOI18N
        jButton8.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton8ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel9Layout = new javax.swing.GroupLayout(jPanel9);
        jPanel9.setLayout(jPanel9Layout);
        jPanel9Layout.setHorizontalGroup(
            jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel9Layout.createSequentialGroup()
                .addComponent(jPanel10, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 1023, Short.MAX_VALUE)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel9Layout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(jLabel59, javax.swing.GroupLayout.PREFERRED_SIZE, 47, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(sortSuppliersComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, 175, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(36, 36, 36)
                        .addComponent(jButton8)))
                .addContainerGap())
            .addGroup(jPanel9Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel8, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel9Layout.setVerticalGroup(
            jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel9Layout.createSequentialGroup()
                .addComponent(jPanel8, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(jPanel9Layout.createSequentialGroup()
                        .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(sortSuppliersComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(jLabel59))
                            .addComponent(jButton8))
                        .addGap(23, 23, 23)
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 408, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jPanel10, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap(109, Short.MAX_VALUE))
        );

        jPanel3.add(jPanel9, java.awt.BorderLayout.PAGE_START);

        jTabbedPane1.addTab("Supplier Management", jPanel3);

        jPanel25.setLayout(new java.awt.BorderLayout());

        jLabel43.setFont(new java.awt.Font("Serif", 0, 14)); // NOI18N
        jLabel43.setText("GRN ID");

        jLabel44.setFont(new java.awt.Font("Serif", 0, 14)); // NOI18N
        jLabel44.setText("Unit price");

        jLabel45.setFont(new java.awt.Font("Serif", 0, 14)); // NOI18N
        jLabel45.setText("Raw material Name");

        jLabel46.setFont(new java.awt.Font("Serif", 1, 24)); // NOI18N
        jLabel46.setText("Supplier Details");

        jButton16.setFont(new java.awt.Font("Serif", 1, 14)); // NOI18N
        jButton16.setText("Add to GRN");
        jButton16.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton16ActionPerformed(evt);
            }
        });

        jLabel47.setFont(new java.awt.Font("Serif", 0, 14)); // NOI18N
        jLabel47.setText("Supplier");

        grnNumberTextField.setEditable(false);
        grnNumberTextField.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                grnNumberTextFieldActionPerformed(evt);
            }
        });

        grnQtyTextField.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                grnQtyTextFieldActionPerformed(evt);
            }
        });

        jLabel48.setFont(new java.awt.Font("Serif", 0, 14)); // NOI18N
        jLabel48.setText("Quantity");

        jButton21.setFont(new java.awt.Font("Serif", 1, 14)); // NOI18N
        jButton21.setIcon(new javax.swing.ImageIcon(getClass().getResource("/lk/corevision/fruitshop/resources/supplier/clear_all.png"))); // NOI18N
        jButton21.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton21ActionPerformed(evt);
            }
        });

        grnUnitpriceTextField.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                grnUnitpriceTextFieldActionPerformed(evt);
            }
        });

        jComboBoxRawmaterial.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        jComboBoxRawmaterial.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jComboBoxRawmaterialActionPerformed(evt);
            }
        });

        jComboBoxSupplier.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        jComboBoxSupplier.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jComboBoxSupplierActionPerformed(evt);
            }
        });

        grnTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Product Name", "Quantity", "Unit Price", "Total"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jScrollPane9.setViewportView(grnTable);

        jLabel60.setFont(new java.awt.Font("Serif", 0, 14)); // NOI18N
        jLabel60.setText("Payment method");

        javax.swing.GroupLayout jPanel29Layout = new javax.swing.GroupLayout(jPanel29);
        jPanel29.setLayout(jPanel29Layout);
        jPanel29Layout.setHorizontalGroup(
            jPanel29Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel29Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel29Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel46, javax.swing.GroupLayout.PREFERRED_SIZE, 367, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanel29Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                        .addGroup(jPanel29Layout.createSequentialGroup()
                            .addComponent(jButton16, javax.swing.GroupLayout.PREFERRED_SIZE, 187, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                            .addComponent(jButton21, javax.swing.GroupLayout.PREFERRED_SIZE, 214, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGroup(jPanel29Layout.createSequentialGroup()
                            .addGroup(jPanel29Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel29Layout.createSequentialGroup()
                                    .addGroup(jPanel29Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addComponent(jLabel43)
                                        .addComponent(jLabel47)
                                        .addComponent(jLabel48, javax.swing.GroupLayout.PREFERRED_SIZE, 98, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(jLabel44, javax.swing.GroupLayout.PREFERRED_SIZE, 98, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(jLabel45))
                                    .addGap(18, 18, 18))
                                .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel29Layout.createSequentialGroup()
                                    .addComponent(jLabel60)
                                    .addGap(42, 42, 42)))
                            .addGroup(jPanel29Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addComponent(jComboBoxGRNPaymentMethod, javax.swing.GroupLayout.PREFERRED_SIZE, 242, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGroup(jPanel29Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addComponent(grnUnitpriceTextField)
                                    .addComponent(grnNumberTextField)
                                    .addComponent(grnQtyTextField)
                                    .addComponent(jComboBoxRawmaterial, 0, 242, Short.MAX_VALUE)
                                    .addComponent(jComboBoxSupplier, javax.swing.GroupLayout.Alignment.TRAILING, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))))
                .addGap(27, 27, 27)
                .addComponent(jScrollPane9, javax.swing.GroupLayout.DEFAULT_SIZE, 1055, Short.MAX_VALUE))
        );
        jPanel29Layout.setVerticalGroup(
            jPanel29Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel29Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel29Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel29Layout.createSequentialGroup()
                        .addComponent(jLabel46)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(jPanel29Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel43)
                            .addGroup(jPanel29Layout.createSequentialGroup()
                                .addGap(2, 2, 2)
                                .addComponent(grnNumberTextField, javax.swing.GroupLayout.PREFERRED_SIZE, 27, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel29Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel47)
                            .addComponent(jComboBoxSupplier, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel29Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel29Layout.createSequentialGroup()
                                .addComponent(jLabel45)
                                .addGap(19, 19, 19)
                                .addGroup(jPanel29Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(grnQtyTextField, javax.swing.GroupLayout.PREFERRED_SIZE, 27, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jLabel48))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(jPanel29Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(jLabel44)
                                    .addComponent(grnUnitpriceTextField, javax.swing.GroupLayout.PREFERRED_SIZE, 27, javax.swing.GroupLayout.PREFERRED_SIZE)))
                            .addComponent(jComboBoxRawmaterial, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(11, 11, 11)
                        .addGroup(jPanel29Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jComboBoxGRNPaymentMethod, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel60))
                        .addGap(18, 18, 18)
                        .addGroup(jPanel29Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jButton16, javax.swing.GroupLayout.PREFERRED_SIZE, 38, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jButton21, javax.swing.GroupLayout.PREFERRED_SIZE, 38, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(0, 345, Short.MAX_VALUE))
                    .addComponent(jScrollPane9, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE))
                .addContainerGap())
        );

        jPanel25.add(jPanel29, java.awt.BorderLayout.CENTER);

        jPanel27.setBackground(new java.awt.Color(204, 204, 204));

        jLabel42.setFont(new java.awt.Font("Serif", 1, 24)); // NOI18N
        jLabel42.setText("Supplier Good Received Note");

        supplierSortClearbtn1.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        supplierSortClearbtn1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/lk/corevision/fruitshop/resources/supplier/clear_all.png"))); // NOI18N
        supplierSortClearbtn1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                supplierSortClearbtn1ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel27Layout = new javax.swing.GroupLayout(jPanel27);
        jPanel27.setLayout(jPanel27Layout);
        jPanel27Layout.setHorizontalGroup(
            jPanel27Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel27Layout.createSequentialGroup()
                .addContainerGap(771, Short.MAX_VALUE)
                .addComponent(jLabel42, javax.swing.GroupLayout.PREFERRED_SIZE, 367, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(75, 75, 75)
                .addComponent(supplierSortClearbtn1, javax.swing.GroupLayout.PREFERRED_SIZE, 64, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(226, 226, 226))
        );
        jPanel27Layout.setVerticalGroup(
            jPanel27Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel27Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel27Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(supplierSortClearbtn1, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel42))
                .addContainerGap(11, Short.MAX_VALUE))
        );

        jPanel25.add(jPanel27, java.awt.BorderLayout.PAGE_START);

        jPanel30.setBackground(new java.awt.Color(204, 204, 204));

        jLabel51.setFont(new java.awt.Font("Serif", 1, 14)); // NOI18N
        jLabel51.setText("Total");

        jLabel52.setFont(new java.awt.Font("Serif", 1, 14)); // NOI18N
        jLabel52.setText("Payment");

        grnTotalfield.setFont(new java.awt.Font("Poppins", 1, 16)); // NOI18N
        grnTotalfield.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        grnTotalfield.setText("0.00");

        jLabel54.setFont(new java.awt.Font("Serif", 1, 14)); // NOI18N
        jLabel54.setText("Balance");

        grnbalanceLabel.setFont(new java.awt.Font("Poppins", 1, 16)); // NOI18N
        grnbalanceLabel.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        grnbalanceLabel.setText("0.00");

        saveGRNbtn.setFont(new java.awt.Font("Serif", 1, 14)); // NOI18N
        saveGRNbtn.setText("Save GRN");
        saveGRNbtn.setEnabled(false);
        saveGRNbtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                saveGRNbtnActionPerformed(evt);
            }
        });

        paymentgrnFormattedTextField.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.NumberFormatter(new java.text.DecimalFormat("#0.00"))));
        paymentgrnFormattedTextField.setFont(new java.awt.Font("Poppins", 0, 14)); // NOI18N
        paymentgrnFormattedTextField.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                paymentgrnFormattedTextFieldKeyReleased(evt);
            }
        });

        javax.swing.GroupLayout jPanel30Layout = new javax.swing.GroupLayout(jPanel30);
        jPanel30.setLayout(jPanel30Layout);
        jPanel30Layout.setHorizontalGroup(
            jPanel30Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel30Layout.createSequentialGroup()
                .addGroup(jPanel30Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel30Layout.createSequentialGroup()
                        .addGap(18, 18, 18)
                        .addGroup(jPanel30Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jLabel52)
                            .addComponent(jLabel51, javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel54, javax.swing.GroupLayout.Alignment.LEADING))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGroup(jPanel30Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel30Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                .addComponent(grnbalanceLabel, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 196, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGroup(jPanel30Layout.createSequentialGroup()
                                    .addGap(6, 6, 6)
                                    .addComponent(paymentgrnFormattedTextField)))
                            .addComponent(grnTotalfield, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 196, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel30Layout.createSequentialGroup()
                        .addContainerGap(49, Short.MAX_VALUE)
                        .addComponent(saveGRNbtn, javax.swing.GroupLayout.PREFERRED_SIZE, 308, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
        );
        jPanel30Layout.setVerticalGroup(
            jPanel30Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel30Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel30Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel51)
                    .addComponent(grnTotalfield, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel30Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(paymentgrnFormattedTextField, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel52))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel30Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel54)
                    .addComponent(grnbalanceLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 26, Short.MAX_VALUE)
                .addComponent(saveGRNbtn, javax.swing.GroupLayout.PREFERRED_SIZE, 29, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(21, 21, 21))
        );

        javax.swing.GroupLayout jPanel28Layout = new javax.swing.GroupLayout(jPanel28);
        jPanel28.setLayout(jPanel28Layout);
        jPanel28Layout.setHorizontalGroup(
            jPanel28Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel28Layout.createSequentialGroup()
                .addContainerGap(1126, Short.MAX_VALUE)
                .addComponent(jPanel30, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        jPanel28Layout.setVerticalGroup(
            jPanel28Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel28Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jPanel30, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        jPanel25.add(jPanel28, java.awt.BorderLayout.PAGE_END);

        jTabbedPane1.addTab("GRN", jPanel25);

        jPanel4.setLayout(new java.awt.BorderLayout());

        viewGRNtable.setFont(new java.awt.Font("Serif", 0, 12)); // NOI18N
        viewGRNtable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "GRN ID", "Supplier Name", "Paid Amount", "Payment Date", "Payment status"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        viewGRNtable.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                viewGRNtableMouseReleased(evt);
            }
        });
        jScrollPane10.setViewportView(viewGRNtable);

        jPanel31.setBackground(new java.awt.Color(204, 204, 204));
        jPanel31.setPreferredSize(new java.awt.Dimension(120, 150));

        jLabel53.setFont(new java.awt.Font("Serif", 1, 24)); // NOI18N
        jLabel53.setText("View GRNs");

        javax.swing.GroupLayout jPanel31Layout = new javax.swing.GroupLayout(jPanel31);
        jPanel31.setLayout(jPanel31Layout);
        jPanel31Layout.setHorizontalGroup(
            jPanel31Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel31Layout.createSequentialGroup()
                .addGap(354, 354, 354)
                .addComponent(jLabel53, javax.swing.GroupLayout.PREFERRED_SIZE, 158, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel31Layout.setVerticalGroup(
            jPanel31Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel31Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jLabel53)
                .addGap(419, 419, 419))
        );

        jLabel58.setFont(new java.awt.Font("Serif", 0, 12)); // NOI18N
        jLabel58.setText("GRN ID");

        jLabel56.setFont(new java.awt.Font("Serif", 0, 12)); // NOI18N
        jLabel56.setText("Supplier Name");

        ViewGRNId.setFont(new java.awt.Font("Serif", 0, 12)); // NOI18N
        ViewGRNId.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                ViewGRNIdKeyReleased(evt);
            }
            public void keyTyped(java.awt.event.KeyEvent evt) {
                ViewGRNIdKeyTyped(evt);
            }
        });

        viewGRNSuppliername.setFont(new java.awt.Font("Serif", 0, 12)); // NOI18N
        viewGRNSuppliername.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                viewGRNSuppliernameKeyReleased(evt);
            }
        });

        jLabel55.setFont(new java.awt.Font("Serif", 0, 12)); // NOI18N
        jLabel55.setText("Sort As");

        viewGRNComboBox.setFont(new java.awt.Font("Serif", 0, 12)); // NOI18N
        viewGRNComboBox.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "GRN Id Ascending order", "GRN Id Descending order", "Supplier Name Ascending order", "Supplier Name Descending order", "Total Ascending order", "Total Descending order" }));
        viewGRNComboBox.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                viewGRNComboBoxItemStateChanged(evt);
            }
        });

        viewGRNDateFrom.addPropertyChangeListener(new java.beans.PropertyChangeListener() {
            public void propertyChange(java.beans.PropertyChangeEvent evt) {
                viewGRNDateFromPropertyChange(evt);
            }
        });

        jButton7.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jButton7.setText("Clear All");
        jButton7.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton7ActionPerformed(evt);
            }
        });

        jLabel57.setFont(new java.awt.Font("Serif", 0, 12)); // NOI18N
        jLabel57.setText("To");

        viewGRNDateTo.addPropertyChangeListener(new java.beans.PropertyChangeListener() {
            public void propertyChange(java.beans.PropertyChangeEvent evt) {
                viewGRNDateToPropertyChange(evt);
            }
        });

        jButton9.setIcon(new javax.swing.ImageIcon(getClass().getResource("/lk/corevision/fruitshop/resources/supplier/reset-black-25.png"))); // NOI18N
        jButton9.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton9ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel12Layout = new javax.swing.GroupLayout(jPanel12);
        jPanel12.setLayout(jPanel12Layout);
        jPanel12Layout.setHorizontalGroup(
            jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel12Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel56)
                    .addComponent(jLabel58))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(ViewGRNId)
                    .addComponent(viewGRNSuppliername, javax.swing.GroupLayout.PREFERRED_SIZE, 164, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(478, 478, 478)
                .addGroup(jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel12Layout.createSequentialGroup()
                        .addComponent(jLabel55, javax.swing.GroupLayout.PREFERRED_SIZE, 47, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(viewGRNComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, 175, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(121, 121, 121)
                        .addComponent(jButton7, javax.swing.GroupLayout.PREFERRED_SIZE, 110, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel12Layout.createSequentialGroup()
                        .addComponent(viewGRNDateFrom, javax.swing.GroupLayout.PREFERRED_SIZE, 138, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jLabel57, javax.swing.GroupLayout.PREFERRED_SIZE, 21, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(20, 20, 20)
                        .addComponent(viewGRNDateTo, javax.swing.GroupLayout.PREFERRED_SIZE, 132, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(62, 62, 62)
                        .addComponent(jButton9)))
                .addContainerGap(300, Short.MAX_VALUE))
        );
        jPanel12Layout.setVerticalGroup(
            jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel12Layout.createSequentialGroup()
                .addGap(21, 21, 21)
                .addGroup(jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel12Layout.createSequentialGroup()
                        .addGroup(jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel55)
                            .addComponent(viewGRNComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jButton7))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(viewGRNDateTo, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jButton9)
                            .addGroup(jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addComponent(jLabel57)
                                .addComponent(viewGRNDateFrom, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))))
                    .addGroup(jPanel12Layout.createSequentialGroup()
                        .addGroup(jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel58)
                            .addComponent(ViewGRNId, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel56)
                            .addComponent(viewGRNSuppliername, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout jPanel33Layout = new javax.swing.GroupLayout(jPanel33);
        jPanel33.setLayout(jPanel33Layout);
        jPanel33Layout.setHorizontalGroup(
            jPanel33Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel31, javax.swing.GroupLayout.DEFAULT_SIZE, 1508, Short.MAX_VALUE)
            .addComponent(jPanel12, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(jScrollPane10)
        );
        jPanel33Layout.setVerticalGroup(
            jPanel33Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel33Layout.createSequentialGroup()
                .addComponent(jPanel31, javax.swing.GroupLayout.PREFERRED_SIZE, 47, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel12, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane10, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jPanel4.add(jPanel33, java.awt.BorderLayout.PAGE_START);

        jTabbedPane1.addTab("View GRN", jPanel4);

        jPanel1.add(jTabbedPane1, java.awt.BorderLayout.CENTER);
        jTabbedPane1.getAccessibleContext().setAccessibleName("Supplier");

        add(jPanel1, java.awt.BorderLayout.CENTER);
    }// </editor-fold>//GEN-END:initComponents

    private String getRawMaterialIdByName(String rawMaterialName) {
        try {

            ResultSet resultSet = MySQL.executeSearch("SELECT `raw_material_id` FROM `raw_material` WHERE `raw_material_name` = '" + rawMaterialName + "'");
            if (resultSet.next()) {

                return resultSet.getString("raw_material_id");
            }
        } catch (Exception e) {
            SignIn.getLoggerObject().log(Level.WARNING, "Getting rawmaterial ID by name method exception found", e);
        }

        return null;
    }


    private void supplierAddressMenuItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_supplierAddressMenuItemActionPerformed
        int row1 = jTable1.getSelectedRow();

        if (row1 == -1) {
            JOptionPane.showMessageDialog(this, "Please select a row", "Warning", JOptionPane.WARNING_MESSAGE, JOptionIcon.WARNING.setICon());

        } else {
            String supplierId = String.valueOf(jTable1.getValueAt(row1, 0));
            String email1 = String.valueOf(jTable1.getValueAt(row1, 3));
            String first_name = String.valueOf(jTable1.getValueAt(row1, 1));
            String last_name = String.valueOf(jTable1.getValueAt(row1, 2));
            String name = first_name + " " + last_name;

            SupplierAddress addressView = new SupplierAddress(dashboard, // Get the parent JFrame
                    true,
                    email1,
                    name,
                    supplierId
            );
            addressView.setVisible(true);

//            
        }
    }//GEN-LAST:event_supplierAddressMenuItemActionPerformed

    private void updateSupplierMenuItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_updateSupplierMenuItemActionPerformed

        int row = jTable1.getSelectedRow();

        jTextField3.setText(String.valueOf(jTable1.getValueAt(row, 1)));
        jTextField1.setText(String.valueOf(jTable1.getValueAt(row, 2)));
        jTextField2.setText(String.valueOf(jTable1.getValueAt(row, 3)));
        jTextField11.setText(String.valueOf(jTable1.getValueAt(row, 4)));

        updateSupplierBtn.setEnabled(true);
        addSupplierBtn.setEnabled(false);

    }//GEN-LAST:event_updateSupplierMenuItemActionPerformed

    private void changeSupplierStatusMenuItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_changeSupplierStatusMenuItemActionPerformed
        int row = jTable1.getSelectedRow();

        String suppId = String.valueOf(jTable1.getValueAt(row, 0));
        String currentStatus = String.valueOf(jTable1.getValueAt(row, 6));
        String newStatus;
        int newStatusId;
        if (currentStatus.equalsIgnoreCase("Active")) {
            newStatus = "Inactive";
            newStatusId = 2;
        } else {
            newStatus = "Active";
            newStatusId = 1;
        }

        if (JOptionPane.showConfirmDialog(this, "Are you sure you want to change the status to " + newStatus + "?", "Confirm Status Change", JOptionPane.OK_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE) == JOptionPane.OK_OPTION) {
            try {
                String query = "UPDATE `supplier` SET `supplier_status_id` = '" + newStatusId + "' WHERE `supplier_id` = '" + suppId + "'";
                MySQL.executeIUD(query);
                jTable1.setValueAt(newStatus, row, 6);
                Notifications.getInstance().show(Notifications.Type.SUCCESS, Notifications.Location.BOTTOM_RIGHT, "Supplier status updated Successfuly!!");
            } catch (Exception e) {
                SignIn.getLoggerObject().log(Level.WARNING, "changing supplier status exception found", e);
            }
        }
    }//GEN-LAST:event_changeSupplierStatusMenuItemActionPerformed

    private void supplierPopupMenuMenuKeyTyped(javax.swing.event.MenuKeyEvent evt) {//GEN-FIRST:event_supplierPopupMenuMenuKeyTyped
        // TODO add your handling code here:
    }//GEN-LAST:event_supplierPopupMenuMenuKeyTyped

    private void updateRawmaterialMenuItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_updateRawmaterialMenuItemActionPerformed
        // TODO add your handling code here:

        int row = jTable3.getSelectedRow();

        jTextField18.setText(String.valueOf(jTable3.getValueAt(row, 0)));
        jTextField17.setText(String.valueOf(jTable3.getValueAt(row, 1)));
        jTextField19.setText(String.valueOf(jTable3.getValueAt(row, 3)));
        jTextField20.setText(String.valueOf(jTable3.getValueAt(row, 2)));
        jComboBox1.setSelectedItem(String.valueOf(jTable3.getValueAt(row, 4)));
        updateRawmaterialBtn.setEnabled(true);
        addRawmaterialBtn.setEnabled(false);
        jButton29.setEnabled(false);

        jTextField18.setEditable(false);
        jTextField17.setEditable(false);
        jComboBox1.setEnabled(false);


    }//GEN-LAST:event_updateRawmaterialMenuItemActionPerformed

    private void removeRawmaterialMenuItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_removeRawmaterialMenuItemActionPerformed

        int row = jTable3.getSelectedRow();
        String rawMaterialName = String.valueOf(jTable3.getValueAt(row, 0));

        if (JOptionPane.showConfirmDialog(this, "Are you sure you want to delete the raw material?", "Confirm Deletion", JOptionPane.OK_CANCEL_OPTION, JOptionPane.WARNING_MESSAGE) == JOptionPane.OK_OPTION) {
            try {

                MySQL.executeIUD("DELETE FROM `raw_material` WHERE `raw_material_name` = '" + rawMaterialName + "'");

                loadSupplierProduct();
                Notifications.getInstance().show(Notifications.Type.SUCCESS, Notifications.Location.BOTTOM_RIGHT, "Raw material deleted Successfuly!!");

            } catch (Exception e) {
                SignIn.getLoggerObject().log(Level.WARNING, "Remove rawmaterial method exception found", e);
            }
        }
    }//GEN-LAST:event_removeRawmaterialMenuItemActionPerformed

    private void removeFinanceMenuItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_removeFinanceMenuItemActionPerformed
//        int row = jTableFinance.getSelectedRow();
//
//        String FinanceId = String.valueOf(jTableFinance.getValueAt(row, 0));
//
//        if (JOptionPane.showConfirmDialog(this, "Are you sure you want to delete the transaction?", "Confirm Deletion", JOptionPane.OK_CANCEL_OPTION, JOptionPane.WARNING_MESSAGE) == JOptionPane.OK_OPTION) {
//            try {
//                MySQL.executeIUD("DELETE FROM `finance` WHERE `transaction_id` = '" + FinanceId + "'");
//
//                resetTransactionForm();
//
//                JOptionPane.showMessageDialog(this, "Transaction deleted successfully", "Information", JOptionPane.INFORMATION_MESSAGE);
//                loadFinance();
//            } catch (Exception e) {
//                e.printStackTrace();
//                JOptionPane.showMessageDialog(this, "Error deleting the transaction", "Error", JOptionPane.ERROR_MESSAGE);
//            }
//        }
    }//GEN-LAST:event_removeFinanceMenuItemActionPerformed

    private void updateGRNMenuItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_updateGRNMenuItemActionPerformed
        updateGRN updategrn = new updateGRN(dashboard, true);
        updategrn.setupdateGRNId(String.valueOf(viewGRNtable.getValueAt(viewGRNtable.getSelectedRow(), 0)));
        updategrn.setVisible(true);
    }//GEN-LAST:event_updateGRNMenuItemActionPerformed

    private void jButton9ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton9ActionPerformed
        loadGRNFinances();
        Notifications.getInstance().show(Notifications.Type.SUCCESS, Notifications.Location.BOTTOM_RIGHT, "Refresh Successfuly!!");

    }//GEN-LAST:event_jButton9ActionPerformed

    private void viewGRNDateToPropertyChange(java.beans.PropertyChangeEvent evt) {//GEN-FIRST:event_viewGRNDateToPropertyChange
        loadGRNFinances();
    }//GEN-LAST:event_viewGRNDateToPropertyChange

    private void jButton7ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton7ActionPerformed
        ViewGRNId.setText("");
        viewGRNSuppliername.setText("");
        viewGRNDateFrom.setDate(null);
        viewGRNDateTo.setDate(null);
        viewGRNtable.clearSelection();
        viewGRNComboBox.setSelectedIndex(0);
        loadGRNFinances();
        Notifications.getInstance().show(Notifications.Type.SUCCESS, Notifications.Location.BOTTOM_RIGHT, "Cleared Successfuly!!");
    }//GEN-LAST:event_jButton7ActionPerformed

    private void viewGRNDateFromPropertyChange(java.beans.PropertyChangeEvent evt) {//GEN-FIRST:event_viewGRNDateFromPropertyChange
        loadGRNFinances();
    }//GEN-LAST:event_viewGRNDateFromPropertyChange

    private void viewGRNComboBoxItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_viewGRNComboBoxItemStateChanged
        loadGRNFinances();
    }//GEN-LAST:event_viewGRNComboBoxItemStateChanged

    private void viewGRNSuppliernameKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_viewGRNSuppliernameKeyReleased
        loadGRNFinances();
    }//GEN-LAST:event_viewGRNSuppliernameKeyReleased

    private void ViewGRNIdKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_ViewGRNIdKeyTyped

        loadGRNFinances();
    }//GEN-LAST:event_ViewGRNIdKeyTyped

    private void ViewGRNIdKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_ViewGRNIdKeyReleased

        loadGRNFinances();
    }//GEN-LAST:event_ViewGRNIdKeyReleased

    private void viewGRNtableMouseReleased(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_viewGRNtableMouseReleased
        if (viewGRNtable.getSelectedRow() != -1) {
            if (evt.getButton() == MouseEvent.BUTTON3) { //Right Click
                grnPopupMenu.show(evt.getComponent(), evt.getX(), evt.getY());
            }
        }
    }//GEN-LAST:event_viewGRNtableMouseReleased

    private void paymentgrnFormattedTextFieldKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_paymentgrnFormattedTextFieldKeyReleased
        try {
            double totalAmount;

            double paidAmount = Double.parseDouble(paymentgrnFormattedTextField.getText());
            totalAmount = Double.parseDouble(grnTotalfield.getText());
            double balance = paidAmount - totalAmount;
            grnbalanceLabel.setText("" + balance);
        } catch (NumberFormatException e) {
            grnbalanceLabel.setText("0.0");
        }
    }//GEN-LAST:event_paymentgrnFormattedTextFieldKeyReleased

    private void saveGRNbtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_saveGRNbtnActionPerformed

        try {
            String grnId = grnNumberTextField.getText();
            String dateTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
            String paidAmount = paymentgrnFormattedTextField.getText();

            String supplierName = String.valueOf(jComboBoxSupplier.getSelectedItem());
            String rawmaterialName = String.valueOf(jComboBoxRawmaterial.getSelectedItem());
            String paymentMethod = String.valueOf(jComboBoxGRNPaymentMethod.getSelectedItem());

            double amountPaid = 0;
            if (!paidAmount.isEmpty()) {
                amountPaid = Double.parseDouble(paidAmount);
            }

            double totalAmount = Double.parseDouble(grnTotalfield.getText());

            // Determine payment status
            int paymentStatusId;
            if (amountPaid >= totalAmount) {
                paymentStatusId = 1; // Fully Paid
            } else if (amountPaid > 0 && amountPaid < totalAmount) {
                paymentStatusId = 3; // Partially Paid
            } else {
                paymentStatusId = 2; // Not Paid
            }

            // Save GRN
            MySQL.executeIUD("INSERT INTO `grn`(`grn_id`,`date`,`paid_amount`,`payment_status_id`,`supplier_id`,`employee_NIC`,`grn_status_id`) "
                    + "VALUES ('" + grnId + "','" + dateTime + "','" + amountPaid + "','" + paymentStatusId + "','" + supplierMap.get(supplierName) + "','" + SignIn.getEmployeeNIC() + "','1')");

            // Save GRN Items
            for (GRNItems grnItems : grnItemsMap.values()) {
                MySQL.executeIUD("INSERT INTO `grn_items`(`grn_grn_id`,`qty`,`unit_price`,`raw_material_id`) "
                        + "VALUES ('" + grnId + "','" + grnItems.getQty() + "','" + grnItems.getUnitPrice() + "','" + rawmaterialMap.get(grnItems.getRawmaterialName()) + "')");

                MySQL.executeIUD("UPDATE `raw_material` SET `qty`=`qty`+'" + grnItems.getQty() + "' WHERE `raw_material_id`='" + rawmaterialMap.get(grnItems.getRawmaterialName()) + "'");
            }

            // Save Payment
            String paymentMethodId = paymentMethodMap.get(paymentMethod);
            MySQL.executeIUD("INSERT INTO `payment`(`amount`, `date`, `payment_status_id`, `grn_grn_id`, `payment_method_id`) "
                    + "VALUES ('" + amountPaid + "','" + dateTime + "','" + paymentStatusId + "','" + grnId + "','" + paymentMethodId + "')");

            resetGRNafterSave();
            Notifications.getInstance().show(Notifications.Type.SUCCESS, Notifications.Location.BOTTOM_RIGHT, "GRN saved successfully!");

        } catch (Exception e) {
            SignIn.getLoggerObject().log(Level.WARNING, "Save GRN method exception found", e);
        }
    }//GEN-LAST:event_saveGRNbtnActionPerformed

    private void jButton21ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton21ActionPerformed
        resetGRN();
        Notifications.getInstance().show(Notifications.Type.SUCCESS, Notifications.Location.BOTTOM_RIGHT, "Cleared Successfuly!!");
    }//GEN-LAST:event_jButton21ActionPerformed

    private void jButton16ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton16ActionPerformed

        try {
            String selectedSupplierName = String.valueOf(jComboBoxSupplier.getSelectedItem());
            String selectedRawmaterialName = String.valueOf(jComboBoxRawmaterial.getSelectedItem());
            String selectedPaymentMethod = String.valueOf(jComboBoxGRNPaymentMethod.getSelectedItem());
            String grnId = grnNumberTextField.getText();
            String qty = grnQtyTextField.getText();
            String unitPrice = grnUnitpriceTextField.getText();

            String date = new SimpleDateFormat("yyyy-MM-dd").format(new Date()); // Current Date

            if (jComboBoxSupplier.getSelectedIndex() == 0) {
                JOptionPane.showMessageDialog(this, "Please Select Supplier ", "Warning", JOptionPane.WARNING_MESSAGE, JOptionIcon.WARNING.setICon());
            } else if (jComboBoxRawmaterial.getSelectedIndex() == 0) {
                JOptionPane.showMessageDialog(this, "Please Select a raw material", "Warning", JOptionPane.WARNING_MESSAGE, JOptionIcon.WARNING.setICon());
            } else if (qty.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Please Enter a qty", "Warning", JOptionPane.WARNING_MESSAGE, JOptionIcon.WARNING.setICon());
            } else if (DataValidator.isInvalidPrice(String.valueOf(qty))) {
                JOptionPane.showMessageDialog(this, "Please Enter a valid qty", "Warning", JOptionPane.WARNING_MESSAGE, JOptionIcon.WARNING.setICon());
            } else if (Integer.parseInt(qty) <= 0) {
                JOptionPane.showMessageDialog(this, "Please enter a valid quantity", "Warning", JOptionPane.WARNING_MESSAGE, JOptionIcon.WARNING.setICon());
            } else if (unitPrice.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Please Enter a unit price", "Warning", JOptionPane.WARNING_MESSAGE, JOptionIcon.WARNING.setICon());
            } else if (DataValidator.isInvalidPrice(String.valueOf(unitPrice))) {
                JOptionPane.showMessageDialog(this, "Please Enter a valid unit price", "Warning", JOptionPane.WARNING_MESSAGE, JOptionIcon.WARNING.setICon());
            } else if (Integer.parseInt(unitPrice) <= 0) {
                JOptionPane.showMessageDialog(this, "Please enter a valid unit price", "Warning", JOptionPane.WARNING_MESSAGE, JOptionIcon.WARNING.setICon());
            } else if (jComboBoxGRNPaymentMethod.getSelectedIndex() == 0) {
                JOptionPane.showMessageDialog(this, "Please Select a Payment Method", "Warning", JOptionPane.WARNING_MESSAGE, JOptionIcon.WARNING.setICon());
            } else {

                GRNItems grnItems = new GRNItems();
                grnItems.setGrnId(grnId);
                grnItems.setUnitPrice(unitPrice);
                grnItems.setQty(qty);
                grnItems.setRawmaterialName(selectedRawmaterialName);

                if (grnItemsMap.get(rawmaterialMap.get(String.valueOf(jComboBoxRawmaterial.getSelectedItem()))) == null) {
                    grnItemsMap.put(rawmaterialMap.get(String.valueOf(jComboBoxRawmaterial.getSelectedItem())), grnItems);
                } else {
                    GRNItems found = grnItemsMap.get(rawmaterialMap.get(String.valueOf(jComboBoxRawmaterial.getSelectedItem())));
                    int option = JOptionPane.showConfirmDialog(this, "Do you want to update the Quantity of Product : " + selectedRawmaterialName, "Message", JOptionPane.YES_NO_OPTION,
                            JOptionPane.INFORMATION_MESSAGE);
                    if (option == JOptionPane.YES_OPTION) {
                        found.setQty(String.valueOf(Double.parseDouble(found.getQty()) + Double.parseDouble(qty)));
                    }
                }

                loadGRNTransactions();
                resetGRN();
                jComboBoxSupplier.setEnabled(false);
                Notifications.getInstance().show(Notifications.Type.SUCCESS, Notifications.Location.BOTTOM_RIGHT, "GRN added Successfuly!!");

            }

            //
        } catch (NumberFormatException e) {
            SignIn.getLoggerObject().log(Level.WARNING, "GRN method exception found", e);
        }
    }//GEN-LAST:event_jButton16ActionPerformed

    private void jButton8ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton8ActionPerformed
        loadSuppliers();
        Notifications.getInstance().show(Notifications.Type.SUCCESS, Notifications.Location.BOTTOM_RIGHT, "Refresh Successfuly!!");
    }//GEN-LAST:event_jButton8ActionPerformed

    private void sortSuppliersComboBoxItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_sortSuppliersComboBoxItemStateChanged
        loadSuppliers();
    }//GEN-LAST:event_sortSuppliersComboBoxItemStateChanged

    private void jTable1MouseReleased(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jTable1MouseReleased
        // TODO add your handling code here:
        if (jTable1.getSelectedRow() != -1) {
            if (evt.getButton() == MouseEvent.BUTTON3) { //Right Click
                supplierPopupMenu.show(evt.getComponent(), evt.getX(), evt.getY());
            }
        }
    }//GEN-LAST:event_jTable1MouseReleased

    private void supplierSortClearbtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_supplierSortClearbtnActionPerformed
        loadSuppliers();
        resetSupplier();
        jTable1.clearSelection();
        sortSuppliersComboBox.setSelectedIndex(0);
        Notifications.getInstance().show(Notifications.Type.SUCCESS, Notifications.Location.BOTTOM_RIGHT, "Cleared Successfuly!!");

    }//GEN-LAST:event_supplierSortClearbtnActionPerformed

    private void jButton17ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton17ActionPerformed
        //Generate supplier report
        String path = "src/lk/corevision/fruitshop/reports/supplier/fruitShopSupplier.jasper";
        try {
            ResultSet resultSet = MySQL.executeSearch("SELECT supplier.supplier_id,supplier.contact, supplier.email, COALESCE(CONCAT(supplier.fname, ' ', supplier.lname), supplier.contact) AS contact_name, supplier_status.supplier_status FROM supplier INNER JOIN supplier_status ON supplier.supplier_status_id = supplier_status.supplier_status_id;");

            HashMap<String, Object> params = new HashMap<>();
            params.put("Parameter101", SendLogo.getFilePath());

            JasperPrint report = JasperFillManager.fillReport(path, params, MySQL.getConnection());

            JasperViewer.viewReport(report, false);
        } catch (Exception e) {
            SignIn.getLoggerObject().log(Level.WARNING, "supplier report exception found", e);
        }
    }//GEN-LAST:event_jButton17ActionPerformed

    private void jTextField11KeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTextField11KeyReleased
        loadSuppliers();
    }//GEN-LAST:event_jTextField11KeyReleased

    private void jTextField3KeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTextField3KeyReleased
        loadSuppliers();
    }//GEN-LAST:event_jTextField3KeyReleased

    private void updateSupplierBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_updateSupplierBtnActionPerformed
        //Update supplier data

        String mobile = jTextField11.getText();
        String fname = jTextField3.getText();
        String lname = jTextField1.getText();
        String email = jTextField2.getText();

        if (fname.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please enter first name", "Warning", JOptionPane.WARNING_MESSAGE, JOptionIcon.WARNING.setICon());
        } else if (lname.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please enter last name", "Warning", JOptionPane.WARNING_MESSAGE, JOptionIcon.WARNING.setICon());

        } else if (email.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please enter email", "Warning", JOptionPane.WARNING_MESSAGE, JOptionIcon.WARNING.setICon());

        } else if (DataValidator.isInvalidEmail(String.valueOf(email))) {

            JOptionPane.showMessageDialog(this, "Please enter valid email", "Warning", JOptionPane.WARNING_MESSAGE, JOptionIcon.WARNING.setICon());

        } else if (mobile.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please enter mobile number", "Warning", JOptionPane.WARNING_MESSAGE, JOptionIcon.WARNING.setICon());
        } else if (DataValidator.isInvalidMobile(String.valueOf(mobile))) {
            JOptionPane.showMessageDialog(this, "Please enter valid mobile number", "Warning", JOptionPane.WARNING_MESSAGE, JOptionIcon.WARNING.setICon());
        } else {

            try {

                ResultSet resultSet = MySQL.executeSearch("SELECT * FROM `supplier` WHERE `email` = '" + email + "' AND `contact` !='" + mobile + "'");

                if (resultSet.next()) {
                    JOptionPane.showMessageDialog(this, "Email already used", "Warning", JOptionPane.WARNING_MESSAGE, JOptionIcon.WARNING.setICon());
                } else {

                    MySQL.executeIUD("UPDATE `supplier` SET "
                            + "`fname` = '" + fname + "' , `lname` = '" + lname + "',`email` = '" + email + "' WHERE `contact` = '" + mobile + "'");
                    resetSupplier();
                    loadSuppliers();
                    updateSupplierBtn.setEnabled(false);
                    addSupplierBtn.setEnabled(true);
                    Notifications.getInstance().show(Notifications.Type.SUCCESS, Notifications.Location.BOTTOM_RIGHT, "Supplier updated Successfuly!!");

                }
            } catch (Exception e) {
                SignIn.getLoggerObject().log(Level.WARNING, "update supplier method exception found", e);
            }

        }
    }//GEN-LAST:event_updateSupplierBtnActionPerformed

    private void addSupplierBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_addSupplierBtnActionPerformed
        //Add supplier

        String contact = jTextField11.getText();
        String fname = jTextField3.getText();
        String lname = jTextField1.getText();
        String email = jTextField2.getText();

        if (fname.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please enter first name", "Warning", JOptionPane.WARNING_MESSAGE, JOptionIcon.WARNING.setICon());
        } else if (lname.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please enter last name", "Warning", JOptionPane.WARNING_MESSAGE, JOptionIcon.WARNING.setICon());

        } else if (email.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please enter email", "Warning", JOptionPane.WARNING_MESSAGE, JOptionIcon.WARNING.setICon());

        } else if (DataValidator.isInvalidEmail(String.valueOf(email))) {

            JOptionPane.showMessageDialog(this, "Please enter valid email", "Warning", JOptionPane.WARNING_MESSAGE, JOptionIcon.WARNING.setICon());

        } else if (contact.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please enter mobile number", "Warning", JOptionPane.WARNING_MESSAGE, JOptionIcon.WARNING.setICon());
        } else if (DataValidator.isInvalidMobile(String.valueOf(contact))) {
            JOptionPane.showMessageDialog(this, "Please enter valid mobile number", "Warning", JOptionPane.WARNING_MESSAGE, JOptionIcon.WARNING.setICon());
        } else {
            try {
                MySQL.executeIUD("INSERT INTO `supplier`(`contact`,`fname`,`lname`,`email`,`supplier_status_id`)"
                        + "VALUES ('" + contact + "','" + fname + "','" + lname + "','" + email + "','1')");
                resetSupplier();
                loadSuppliers();
                Notifications.getInstance().show(Notifications.Type.SUCCESS, Notifications.Location.BOTTOM_RIGHT, "Supplier added Successfuly!!");

            } catch (Exception e) {
                SignIn.getLoggerObject().log(Level.WARNING, "add supplier method exception found", e);
            }

        }

    }//GEN-LAST:event_addSupplierBtnActionPerformed

    private void jTextField2KeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTextField2KeyReleased
        loadSuppliers();
    }//GEN-LAST:event_jTextField2KeyReleased

    private void jTextField1KeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTextField1KeyReleased
        loadSuppliers();
    }//GEN-LAST:event_jTextField1KeyReleased

    private void jTable3MouseReleased(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jTable3MouseReleased
        // TODO add your handling code here:
        if (jTable3.getSelectedRow() != -1) {
            if (evt.getButton() == MouseEvent.BUTTON3) { //Right Click
                rawmaterialPopupMenu.show(evt.getComponent(), evt.getX(), evt.getY());
            }
        }
    }//GEN-LAST:event_jTable3MouseReleased

    private void sortRawmaterialComboBoxItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_sortRawmaterialComboBoxItemStateChanged
        loadSupplierProduct();
    }//GEN-LAST:event_sortRawmaterialComboBoxItemStateChanged

    private void rawmaterialClearbtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rawmaterialClearbtnActionPerformed
        loadSupplierProduct();  // Reload the raw materials list
        resetSupplierRawmaterial();
        jTable3.clearSelection();
        sortRawmaterialComboBox.setSelectedIndex(0);

        updateRawmaterialBtn.setEnabled(false);
        addRawmaterialBtn.setEnabled(true);
        jButton29.setEnabled(true);

        jTextField18.setEditable(true);
        jTextField17.setEditable(true);
        jComboBox1.setEnabled(true);

        Notifications.getInstance().show(Notifications.Type.SUCCESS, Notifications.Location.BOTTOM_RIGHT, "Cleared Successfuly!!");
    }//GEN-LAST:event_rawmaterialClearbtnActionPerformed

    private void jButton29ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton29ActionPerformed

        SupplierBrand supplierbrand = new SupplierBrand(dashboard, true);

        supplierbrand.setSupplier(this);

        supplierbrand.setVisible(true);
    }//GEN-LAST:event_jButton29ActionPerformed

    private void jTextField18KeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTextField18KeyReleased
        loadSupplierProduct();
    }//GEN-LAST:event_jTextField18KeyReleased

    private void updateRawmaterialBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_updateRawmaterialBtnActionPerformed
        // update product raw material

        String rawMaterialName = jTextField18.getText();
        String unitPrice = jTextField20.getText();
        String qty = jTextField19.getText();
        String brandName = jTextField17.getText();
        String supplier = String.valueOf(jComboBox1.getSelectedItem());

        if (rawMaterialName.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please enter raw material name", "Warning", JOptionPane.WARNING_MESSAGE, JOptionIcon.WARNING.setICon());
        } else if (DataValidator.isInvalidPrice(String.valueOf(qty))) {
            JOptionPane.showMessageDialog(this, "Please enter valid quantity", "Error", JOptionPane.ERROR_MESSAGE, JOptionIcon.ERROR.setICon());
        } else if (DataValidator.isInvalidPrice(qty)) {
            JOptionPane.showMessageDialog(this, "Please enter valid quantity", "Warning", JOptionPane.WARNING_MESSAGE, JOptionIcon.WARNING.setICon());
        } else if (DataValidator.isInvalidPrice(String.valueOf(unitPrice))) {
            JOptionPane.showMessageDialog(this, "Please enter valid unit price", "Warning", JOptionPane.WARNING_MESSAGE, JOptionIcon.WARNING.setICon());
        } else {
            try {

                String rawMaterialId = getRawMaterialIdByName(rawMaterialName);

                if (rawMaterialId == null) {
                    Notifications.getInstance().show(Notifications.Type.ERROR, Notifications.Location.BOTTOM_RIGHT, "Raw material not found !!");
                    return;
                }

                MySQL.executeIUD(
                        "UPDATE `raw_material` SET `unit_price` = '" + unitPrice + "', `qty` = '" + qty + "' "
                        + "WHERE `raw_material_id` = '" + rawMaterialId + "'");

                Notifications.getInstance().show(Notifications.Type.SUCCESS, Notifications.Location.BOTTOM_RIGHT, "Raw material updated Successfuly!!");

                resetSupplierRawmaterial();

                updateRawmaterialBtn.setEnabled(false);
                addRawmaterialBtn.setEnabled(true);
                jButton29.setEnabled(true);

                jTextField18.setEditable(true);
                jTextField17.setEditable(true);
                jComboBox1.setEnabled(true);
                loadSupplierProduct();

            } catch (Exception e) {
                SignIn.getLoggerObject().log(Level.WARNING, "update raw material method exception found", e);
            }
        }
    }//GEN-LAST:event_updateRawmaterialBtnActionPerformed

    private void addRawmaterialBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_addRawmaterialBtnActionPerformed
        //Add product
        try {
            String rawmaterial = jTextField18.getText();
            String supplier = String.valueOf(jComboBox1.getSelectedItem());
            String brandname = jTextField17.getText();
            String qty = jTextField19.getText();
            String unitprice = jTextField20.getText();

            if (rawmaterial.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Please enter product raw material name", "Warning", JOptionPane.WARNING_MESSAGE, JOptionIcon.WARNING.setICon());
            } else if (jComboBox1.getSelectedIndex() == 0) {
                JOptionPane.showMessageDialog(this, "Please select a supplier", "Warning", JOptionPane.WARNING_MESSAGE, JOptionIcon.WARNING.setICon());
            } else if (brandname.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Please select a brand name", "Warning", JOptionPane.WARNING_MESSAGE, JOptionIcon.WARNING.setICon());
            } else if (qty.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Please enter quantity", "Warning", JOptionPane.WARNING_MESSAGE, JOptionIcon.WARNING.setICon());
            } else if (DataValidator.isInvalidPrice(String.valueOf(qty))) {
                JOptionPane.showMessageDialog(this, "Please enter valid quantity", "Error", JOptionPane.ERROR_MESSAGE, JOptionIcon.ERROR.setICon());
            } else if (Integer.parseInt(qty) <= 0) {
                JOptionPane.showMessageDialog(this, "Please enter valid quantity", "Warning", JOptionPane.WARNING_MESSAGE, JOptionIcon.WARNING.setICon());
            } else if (unitprice.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Please enter unit price", "Warning", JOptionPane.WARNING_MESSAGE, JOptionIcon.WARNING.setICon());
            } else if (DataValidator.isInvalidPrice(String.valueOf(unitprice))) {
                JOptionPane.showMessageDialog(this, "Please enter valid unit price", "Warning", JOptionPane.WARNING_MESSAGE, JOptionIcon.WARNING.setICon());
            } else if (Integer.parseInt(unitprice) <= 0) {
                JOptionPane.showMessageDialog(this, "Please enter valid unit price", "Warning", JOptionPane.WARNING_MESSAGE, JOptionIcon.WARNING.setICon());
            } else {

                String rawMaterialId = "" + System.currentTimeMillis();

                String query = "INSERT INTO `raw_material` (`raw_material_id`, `raw_material_name`, `unit_price`, `qty`, `brand_brand_id`, `supplier_supplier_id`) "
                        + "VALUES ('" + rawMaterialId + "', '" + rawmaterial + "', '" + unitprice + "', '" + qty
                        + "', (SELECT `brand_id` FROM `brand` WHERE `brand_name` = '" + brandname + "'), "
                        + "(SELECT `supplier_id` FROM `supplier` WHERE CONCAT(`fname`, ' ', `lname`) = '" + supplier + "'))";

                int result = MySQL.executeIUD(query);

                if (result > 0) {
                    Notifications.getInstance().show(Notifications.Type.SUCCESS, Notifications.Location.BOTTOM_RIGHT, "Raw material added Successfuly!!");

                    resetSupplierRawmaterial();
                    loadSupplierProduct();

                } else {
                    Notifications.getInstance().show(Notifications.Type.ERROR, Notifications.Location.BOTTOM_RIGHT, "Failed to add raw material !!");
                }
            }

        } catch (Exception e) {
            SignIn.getLoggerObject().log(Level.WARNING, "add raw material method exception found", e);
        }

    }//GEN-LAST:event_addRawmaterialBtnActionPerformed

    private void jButton5ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton5ActionPerformed
        jTabbedPane1.setSelectedIndex(3);
    }//GEN-LAST:event_jButton5ActionPerformed

    private void jButton4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton4ActionPerformed
        jTabbedPane1.setSelectedIndex(4);
    }//GEN-LAST:event_jButton4ActionPerformed

    private void jButton3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton3ActionPerformed
        jTabbedPane1.setSelectedIndex(2);
    }//GEN-LAST:event_jButton3ActionPerformed

    private void jButton6ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton6ActionPerformed
        jTabbedPane1.setSelectedIndex(1);
    }//GEN-LAST:event_jButton6ActionPerformed

    private void jButton28ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton28ActionPerformed
        //Generate raw material report
        String path = "src/lk/corevision/fruitshop/reports/supplier/fruitShopSuppRawmaterial.jasper";
        try {
            ResultSet resultSet = MySQL.executeSearch("SELECT `raw_material`.`raw_material_id`,`raw_material`.`raw_material_name`,`raw_material`.unit_price, "
                    + "`raw_material`.`qty`,`brand`.`brand_name`,`supplier`.`fname` FROM `raw_material` "
                    + "INNER JOIN `brand` ON `brand`.`brand_id`=`raw_material`.`brand_brand_id`"
                    + "INNER JOIN `supplier` ON `supplier`.`supplier_id`=`raw_material`.`supplier_supplier_id` "
                    + "ORDER BY `raw_material_id` DESC");

            HashMap<String, Object> params = new HashMap<>();
            params.put("Parameter101", SendLogo.getFilePath());

            // Compile and fill the Jasper report
            JasperPrint report = JasperFillManager.fillReport(path, params, MySQL.getConnection());

            // View the report
            JasperViewer.viewReport(report, false);
        } catch (Exception e) {
            SignIn.getLoggerObject().log(Level.WARNING, "supplier raw material report exception found", e);
        }
    }//GEN-LAST:event_jButton28ActionPerformed

    private void supplierSortClearbtn1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_supplierSortClearbtn1ActionPerformed
        resetGRNafterSave();
    }//GEN-LAST:event_supplierSortClearbtn1ActionPerformed

    private void jTextField18ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextField18ActionPerformed
        jComboBox1.grabFocus();
    }//GEN-LAST:event_jTextField18ActionPerformed

    private void jTextField19ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextField19ActionPerformed
        jTextField20.grabFocus();
    }//GEN-LAST:event_jTextField19ActionPerformed

    private void jTextField3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextField3ActionPerformed
        jTextField1.grabFocus();
    }//GEN-LAST:event_jTextField3ActionPerformed

    private void jTextField1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextField1ActionPerformed
        jTextField2.grabFocus();
    }//GEN-LAST:event_jTextField1ActionPerformed

    private void jTextField2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextField2ActionPerformed
        jTextField11.grabFocus();
    }//GEN-LAST:event_jTextField2ActionPerformed

    private void grnNumberTextFieldActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_grnNumberTextFieldActionPerformed
        jComboBoxSupplier.grabFocus();
    }//GEN-LAST:event_grnNumberTextFieldActionPerformed

    private void jComboBoxSupplierActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jComboBoxSupplierActionPerformed
        jComboBoxRawmaterial.grabFocus();
    }//GEN-LAST:event_jComboBoxSupplierActionPerformed

    private void jComboBoxRawmaterialActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jComboBoxRawmaterialActionPerformed
        grnQtyTextField.grabFocus();
    }//GEN-LAST:event_jComboBoxRawmaterialActionPerformed

    private void grnQtyTextFieldActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_grnQtyTextFieldActionPerformed
        grnUnitpriceTextField.grabFocus();
    }//GEN-LAST:event_grnQtyTextFieldActionPerformed

    private void grnUnitpriceTextFieldActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_grnUnitpriceTextFieldActionPerformed
        jComboBoxGRNPaymentMethod.grabFocus();
    }//GEN-LAST:event_grnUnitpriceTextFieldActionPerformed

    private void jButton6MouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jButton6MouseEntered
        jButton6.setBackground(new Color(255, 153, 0));

    }//GEN-LAST:event_jButton6MouseEntered

    private void jButton3MouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jButton3MouseEntered
        jButton3.setBackground(new Color(255, 153, 0));

    }//GEN-LAST:event_jButton3MouseEntered

    private void jButton4MouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jButton4MouseEntered
        jButton4.setBackground(new Color(255, 153, 0));

    }//GEN-LAST:event_jButton4MouseEntered

    private void jButton5MouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jButton5MouseEntered
        jButton5.setBackground(new Color(255, 153, 0));

    }//GEN-LAST:event_jButton5MouseEntered

    private void jButton6MouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jButton6MouseExited
        jButton6.setBackground(new Color(255, 255, 255));
    }//GEN-LAST:event_jButton6MouseExited

    private void jButton3MouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jButton3MouseExited
        jButton3.setBackground(new Color(255, 255, 255));
    }//GEN-LAST:event_jButton3MouseExited

    private void jButton4MouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jButton4MouseExited
        jButton4.setBackground(new Color(255, 255, 255));
    }//GEN-LAST:event_jButton4MouseExited

    private void jButton5MouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jButton5MouseExited
        jButton5.setBackground(new Color(255, 255, 255));
    }//GEN-LAST:event_jButton5MouseExited


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JTextField ViewGRNId;
    private javax.swing.JButton addRawmaterialBtn;
    private javax.swing.JButton addSupplierBtn;
    private javax.swing.JMenuItem changeSupplierStatusMenuItem;
    private javax.swing.JPopupMenu financePopupMenu;
    private javax.swing.JTextField grnNumberTextField;
    private javax.swing.JPopupMenu grnPopupMenu;
    private javax.swing.JTextField grnQtyTextField;
    private javax.swing.JTable grnTable;
    private javax.swing.JLabel grnTotalfield;
    private javax.swing.JTextField grnUnitpriceTextField;
    private javax.swing.JLabel grnbalanceLabel;
    private javax.swing.JButton jButton16;
    private javax.swing.JButton jButton17;
    private javax.swing.JButton jButton21;
    private javax.swing.JButton jButton28;
    private javax.swing.JButton jButton29;
    private javax.swing.JButton jButton3;
    private javax.swing.JButton jButton4;
    private javax.swing.JButton jButton5;
    private javax.swing.JButton jButton6;
    private javax.swing.JButton jButton7;
    private javax.swing.JButton jButton8;
    private javax.swing.JButton jButton9;
    private javax.swing.JComboBox<String> jComboBox1;
    private javax.swing.JComboBox<String> jComboBoxGRNPaymentMethod;
    private javax.swing.JComboBox<String> jComboBoxRawmaterial;
    private javax.swing.JComboBox<String> jComboBoxSupplier;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel18;
    private javax.swing.JLabel jLabel25;
    private javax.swing.JLabel jLabel26;
    private javax.swing.JLabel jLabel28;
    private javax.swing.JLabel jLabel29;
    private javax.swing.JLabel jLabel30;
    private javax.swing.JLabel jLabel41;
    private javax.swing.JLabel jLabel42;
    private javax.swing.JLabel jLabel43;
    private javax.swing.JLabel jLabel44;
    private javax.swing.JLabel jLabel45;
    private javax.swing.JLabel jLabel46;
    private javax.swing.JLabel jLabel47;
    private javax.swing.JLabel jLabel48;
    private javax.swing.JLabel jLabel49;
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
    private javax.swing.JLabel jLabel62;
    private javax.swing.JLabel jLabel63;
    private javax.swing.JLabel jLabel64;
    private javax.swing.JLabel jLabel65;
    private javax.swing.JLabel jLabel66;
    private javax.swing.JLabel jLabel67;
    private javax.swing.JLayeredPane jLayeredPane1;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel10;
    private javax.swing.JPanel jPanel11;
    private javax.swing.JPanel jPanel12;
    private javax.swing.JPanel jPanel13;
    private javax.swing.JPanel jPanel14;
    private javax.swing.JPanel jPanel15;
    private javax.swing.JPanel jPanel18;
    private javax.swing.JPanel jPanel19;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel25;
    private javax.swing.JPanel jPanel27;
    private javax.swing.JPanel jPanel28;
    private javax.swing.JPanel jPanel29;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel30;
    private javax.swing.JPanel jPanel31;
    private javax.swing.JPanel jPanel32;
    private javax.swing.JPanel jPanel33;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel7;
    private javax.swing.JPanel jPanel8;
    private javax.swing.JPanel jPanel9;
    private javax.swing.JRadioButton jRadioButton1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane10;
    private javax.swing.JScrollPane jScrollPane5;
    private javax.swing.JScrollPane jScrollPane9;
    private javax.swing.JTabbedPane jTabbedPane1;
    private javax.swing.JTable jTable1;
    private javax.swing.JTable jTable3;
    private javax.swing.JTextField jTextField1;
    private javax.swing.JTextField jTextField11;
    private javax.swing.JTextField jTextField17;
    private javax.swing.JTextField jTextField18;
    private javax.swing.JTextField jTextField19;
    private javax.swing.JTextField jTextField2;
    private javax.swing.JTextField jTextField20;
    private javax.swing.JTextField jTextField3;
    private javax.swing.JFormattedTextField paymentgrnFormattedTextField;
    private javax.swing.JPanel pieChart1supplier;
    private javax.swing.JButton rawmaterialClearbtn;
    private javax.swing.JPopupMenu rawmaterialPopupMenu;
    private javax.swing.JMenuItem removeFinanceMenuItem;
    private javax.swing.JMenuItem removeRawmaterialMenuItem;
    private javax.swing.JButton saveGRNbtn;
    private javax.swing.JComboBox<String> sortRawmaterialComboBox;
    private javax.swing.JComboBox<String> sortSuppliersComboBox;
    private javax.swing.JMenuItem supplierAddressMenuItem;
    private javax.swing.JPopupMenu supplierPopupMenu;
    private javax.swing.JButton supplierSortClearbtn;
    private javax.swing.JButton supplierSortClearbtn1;
    private javax.swing.JMenuItem updateGRNMenuItem;
    private javax.swing.JButton updateRawmaterialBtn;
    private javax.swing.JMenuItem updateRawmaterialMenuItem;
    private javax.swing.JButton updateSupplierBtn;
    private javax.swing.JMenuItem updateSupplierMenuItem;
    private javax.swing.JComboBox<String> viewGRNComboBox;
    private com.toedter.calendar.JDateChooser viewGRNDateFrom;
    private com.toedter.calendar.JDateChooser viewGRNDateTo;
    private javax.swing.JTextField viewGRNSuppliername;
    private javax.swing.JTable viewGRNtable;
    // End of variables declaration//GEN-END:variables

    private void resetSupplier() {
        jTextField1.setText("");
        jTextField2.setText("");
        jTextField11.setText("");
        jTextField3.setText("");
    }

    private void resetSupplierRawmaterial() {
        jTextField18.setText("");
        jComboBox1.setSelectedIndex(0);
        jTextField17.setText("");
        jTextField19.setText("");
        jTextField20.setText("");
    }

    private void resetGRN() {

//        jComboBoxSupplier.setSelectedIndex(0);
        jComboBoxRawmaterial.setSelectedIndex(0);

        grnQtyTextField.setText("");
        grnUnitpriceTextField.setText("");

    }

    private void resetGRNafterSave() {
        generateGRNId();

        resetGRN();
        grnTotalfield.setText("00.00");
        paymentgrnFormattedTextField.setText("");
        grnbalanceLabel.setText("00.00");

        jComboBoxSupplier.setEnabled(true);
        saveGRNbtn.setEnabled(false);

        grnItemsMap = new HashMap<>();
        loadGRNTransactions();

    }

}
