package lk.corevision.fruitshop.gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.event.MouseEvent;
import java.sql.ResultSet;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Vector;
import java.util.logging.Level;
import javaswingdev.chart.ModelPieChart;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;
import lk.corevision.fruitshop.charts.PieChart;
import lk.corevision.fruitshop.model.MySQL;
import lk.corevision.fruitshop.model.SendLogo;
import lk.corevision.fruitshop.validate.DataValidator;
import lk.corevision.fruitshop.validate.JOptionIcon;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.data.JRTableModelDataSource;
import net.sf.jasperreports.view.JasperViewer;
import raven.toast.Notifications;

/**
 *
 * @author CoreVision Software Team
 * @version 1.0.0
 */
public class Finance extends javax.swing.JPanel {

    private PieChart pieChart1;
    private HashMap<String, String> transactionTypeMap = new HashMap<>();
    private HashMap<String, String> paymentMethodMap = new HashMap<>();

    public Finance() {
        initComponents();
        loadFinance();
        transactionTypeDropdown();
        paymentMethodDropdown();
        transactionId();
        loadFinancePieChart();
        ((JTextField) jDateChooser2.getDateEditor().getUiComponent()).setEditable(false);

    }

    private void loadFinancePieChart() {
        pieChart1 = new PieChart();
        pieChart1.setChartType(PieChart.PeiChartType.DEFAULT);
        pieChart1.clearData();

        String query = "SELECT `Description`, `amount` FROM finance";

        try {
            ResultSet resultSet = MySQL.executeSearch(query);
            boolean hasData = false;
            int index = 0;

            while (resultSet.next()) {
                hasData = true;
                String description = resultSet.getString("Description");
                double totalAmount = resultSet.getDouble("amount");
                pieChart1.addData(new ModelPieChart(description, totalAmount, getColor(index++)));
            }

            if (!hasData) {
                pieChart1.addData(new ModelPieChart("Utilities", 500, getColor(index++)));
                pieChart1.addData(new ModelPieChart("Salaries", 3000, getColor(index++)));
                pieChart1.addData(new ModelPieChart("Maintenance", 800, getColor(index++)));
                pieChart1.addData(new ModelPieChart("Miscellaneous", 200, getColor(index++)));
            }

        } catch (Exception e) {
            SignIn.getLoggerObject().log(Level.WARNING, "loading finance pie chart method exception found", e);
        }

        pieChart1.setBounds(0, 0, pieChart1finance.getWidth(), pieChart1finance.getHeight());

        pieChart1finance.setLayout(new BorderLayout());
        pieChart1finance.add(pieChart1, BorderLayout.CENTER);
        pieChart1finance.revalidate();
        pieChart1finance.repaint();
    }

    private Color getColor(int index) {
        Color[] colors = new Color[]{
            new Color(21, 101, 192), // Dark Blue
            new Color(56, 142, 60), // Dark Green
            new Color(204, 192, 0), // Dark Yellow
            new Color(183, 32, 26), // Dark Red
            new Color(106, 27, 154), // Dark Purple
            new Color(230, 81, 0), // Dark Orange
            new Color(93, 64, 55), // Dark Brown
            new Color(0, 150, 136) // Dark Cyan
        };
        return colors[index % colors.length];
    }

    private void transactionId() {
        long id = System.currentTimeMillis();
        jTextFieldTransactionID.setText(String.valueOf(id));
        jTextFieldTransactionID.setEditable(false);
    }

    private void paymentMethodDropdown() {
        try {
            ResultSet resultSet = MySQL.executeSearch("SELECT * FROM `payment_method`");

            Vector<String> vector = new Vector<>();
            vector.add("Select");

            while (resultSet.next()) {
                String paymentMethod = resultSet.getString("payment_method");
                vector.add(paymentMethod);

                // Assuming paymentMethodMap is a map to store `payment_method` and `payment_method_id`
                paymentMethodMap.put(paymentMethod, resultSet.getString("payment_method_id"));
            }

            DefaultComboBoxModel<String> model = new DefaultComboBoxModel<>(vector);
            jComboBoxPaymentMethod.setModel(model);

        } catch (Exception e) {
            SignIn.getLoggerObject().log(Level.WARNING, "paymentMethodDropdown method exception found", e);
        }
    }

    private void transactionTypeDropdown() {
        try {
            ResultSet resultSet = MySQL.executeSearch("SELECT * FROM `transaction_type`");

            Vector<String> vector = new Vector<>();
            vector.add("Select");

            while (resultSet.next()) {
                String transactionType = resultSet.getString("transaction_type_name");
                vector.add(transactionType);
                transactionTypeMap.put(transactionType, resultSet.getString("transaction_type_id"));
            }

            DefaultComboBoxModel<String> model = new DefaultComboBoxModel<>(vector);
            jComboBoxTransactionType.setModel(model);

        } catch (Exception e) {
            SignIn.getLoggerObject().log(Level.WARNING, "transaction type dropdown method exception found", e);
        }
    }

    private void loadFinance() {
        transactionId();
        try {
            String query = "SELECT `finance`.`transaction_id`, `transaction_type`.`transaction_type_name`, `finance`.`amount`, `finance`.`description`, `finance`.`date`, `payment_method`.`payment_method` AS `payment_method_name` FROM "
                    + "`finance` INNER JOIN `transaction_type` ON `finance`.`transaction_type_id` = `transaction_type`.`transaction_type_id` INNER JOIN `payment_method` ON `finance`.`payment_method_id` = `payment_method`.`payment_method_id` ";

//            query += "ORDER BY ";
//            query = query.replace("AND ORDER BY ", "ORDER BY ");
            if (sortFinanceComboBox.getSelectedIndex() == 1) {
                query += "WHERE `transaction_type`.`transaction_type_name` = 'Credit'";
            } else if (sortFinanceComboBox.getSelectedIndex() == 2) {
                query += "WHERE `transaction_type`.`transaction_type_name` = 'Debit'";
            } else if (sortFinanceComboBox.getSelectedIndex() == 3) {
                query += "WHERE `payment_method`.`payment_method` = 'Cash'";
            } else if (sortFinanceComboBox.getSelectedIndex() == 4) {
                query += "WHERE `payment_method`.`payment_method` = 'Credit'";
            } else if (sortFinanceComboBox.getSelectedIndex() == 5) {
                query += "WHERE `payment_method`.`payment_method` = 'Cheque'";
            }
            query += "ORDER BY `finance`.`transaction_id` DESC";

            ResultSet resultSet = MySQL.executeSearch(query);

            DefaultTableModel model = (DefaultTableModel) jTableFinance.getModel();
            model.setRowCount(0);

            double total = 0;

            while (resultSet.next()) {
                Vector<String> vector = new Vector<>();
                vector.add(resultSet.getString("transaction_id"));
                vector.add(resultSet.getString("description"));
                vector.add(resultSet.getString("transaction_type_name"));
                vector.add(resultSet.getString("payment_method_name"));
                vector.add(resultSet.getString("amount"));
                vector.add(resultSet.getString("date"));

                model.addRow(vector);
            }
        } catch (Exception e) {
            SignIn.getLoggerObject().log(Level.WARNING, "load finance method exception found", e);
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

        financePopupMenu = new javax.swing.JPopupMenu();
        removeFinanceMenuItem = new javax.swing.JMenuItem();
        jTabbedPane1 = new javax.swing.JTabbedPane();
        jPanel2 = new javax.swing.JPanel();
        jPanel7 = new javax.swing.JPanel();
        jPanel11 = new javax.swing.JPanel();
        jButton4 = new javax.swing.JButton();
        jPanel12 = new javax.swing.JPanel();
        pieChart1finance = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jPanel6 = new javax.swing.JPanel();
        jPanel25 = new javax.swing.JPanel();
        jLabel41 = new javax.swing.JLabel();
        jScrollPane8 = new javax.swing.JScrollPane();
        jTableFinance = new javax.swing.JTable();
        jPanel26 = new javax.swing.JPanel();
        jTextFieldAmount = new javax.swing.JTextField();
        jLabel42 = new javax.swing.JLabel();
        jLabel43 = new javax.swing.JLabel();
        jLabel44 = new javax.swing.JLabel();
        jLabel45 = new javax.swing.JLabel();
        jButton14 = new javax.swing.JButton();
        jLabel46 = new javax.swing.JLabel();
        jComboBoxTransactionType = new javax.swing.JComboBox<>();
        jLabel47 = new javax.swing.JLabel();
        jComboBoxPaymentMethod = new javax.swing.JComboBox<>();
        jDateChooser2 = new com.toedter.calendar.JDateChooser();
        jTextFieldTransactionType = new javax.swing.JTextField();
        jTextFieldTransactionID = new javax.swing.JTextField();
        jButton15 = new javax.swing.JButton();
        financeReport = new javax.swing.JButton();
        jTextFieldTransactionDes1 = new javax.swing.JTextField();
        jLabel50 = new javax.swing.JLabel();
        financeClearbtn = new javax.swing.JButton();
        jLabel49 = new javax.swing.JLabel();
        sortFinanceComboBox = new javax.swing.JComboBox<>();
        jButton8 = new javax.swing.JButton();

        removeFinanceMenuItem.setText("Remove Transaction");
        removeFinanceMenuItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                removeFinanceMenuItemActionPerformed(evt);
            }
        });
        financePopupMenu.add(removeFinanceMenuItem);

        setLayout(new java.awt.BorderLayout());

        jPanel2.setPreferredSize(new java.awt.Dimension(487, 614));
        jPanel2.setLayout(new java.awt.BorderLayout());

        jPanel11.setPreferredSize(new java.awt.Dimension(287, 200));
        jPanel11.setLayout(new java.awt.GridLayout(1, 5, 1, 1));

        jButton4.setFont(new java.awt.Font("Serif", 1, 36)); // NOI18N
        jButton4.setIcon(new javax.swing.ImageIcon(getClass().getResource("/lk/corevision/fruitshop/resources/supplier/icons8-cash-64.png"))); // NOI18N
        jButton4.setText("Finance and Accounts Management");
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

        pieChart1finance.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        pieChart1finance.setFont(new java.awt.Font("Serif", 0, 10)); // NOI18N
        pieChart1finance.setName(""); // NOI18N
        pieChart1finance.setPreferredSize(new java.awt.Dimension(400, 400));

        javax.swing.GroupLayout pieChart1financeLayout = new javax.swing.GroupLayout(pieChart1finance);
        pieChart1finance.setLayout(pieChart1financeLayout);
        pieChart1financeLayout.setHorizontalGroup(
            pieChart1financeLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 382, Short.MAX_VALUE)
        );
        pieChart1financeLayout.setVerticalGroup(
            pieChart1financeLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 377, Short.MAX_VALUE)
        );

        jLabel1.setFont(new java.awt.Font("Serif", 1, 14)); // NOI18N
        jLabel1.setText("Company Finances Distribution");

        javax.swing.GroupLayout jPanel12Layout = new javax.swing.GroupLayout(jPanel12);
        jPanel12.setLayout(jPanel12Layout);
        jPanel12Layout.setHorizontalGroup(
            jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel12Layout.createSequentialGroup()
                .addGroup(jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel12Layout.createSequentialGroup()
                        .addGap(45, 45, 45)
                        .addComponent(pieChart1finance, javax.swing.GroupLayout.PREFERRED_SIZE, 384, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel12Layout.createSequentialGroup()
                        .addGap(123, 123, 123)
                        .addComponent(jLabel1)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel12Layout.setVerticalGroup(
            jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel12Layout.createSequentialGroup()
                .addComponent(pieChart1finance, javax.swing.GroupLayout.PREFERRED_SIZE, 379, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel1)
                .addContainerGap(20, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout jPanel7Layout = new javax.swing.GroupLayout(jPanel7);
        jPanel7.setLayout(jPanel7Layout);
        jPanel7Layout.setHorizontalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel11, javax.swing.GroupLayout.DEFAULT_SIZE, 1186, Short.MAX_VALUE)
            .addComponent(jPanel12, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        jPanel7Layout.setVerticalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel7Layout.createSequentialGroup()
                .addComponent(jPanel11, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel12, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );

        jPanel2.add(jPanel7, java.awt.BorderLayout.PAGE_START);

        jTabbedPane1.addTab("Dashboard", jPanel2);

        jPanel25.setBackground(new java.awt.Color(204, 204, 204));

        jLabel41.setFont(new java.awt.Font("Serif", 1, 24)); // NOI18N
        jLabel41.setText("Finance and Accounting Management");

        javax.swing.GroupLayout jPanel25Layout = new javax.swing.GroupLayout(jPanel25);
        jPanel25.setLayout(jPanel25Layout);
        jPanel25Layout.setHorizontalGroup(
            jPanel25Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel25Layout.createSequentialGroup()
                .addGap(373, 373, 373)
                .addComponent(jLabel41, javax.swing.GroupLayout.PREFERRED_SIZE, 412, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel25Layout.setVerticalGroup(
            jPanel25Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel25Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jLabel41)
                .addGap(436, 436, 436))
        );

        jTableFinance.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "ID", "Transaction", "Type", "Payment method ", "Amount", "Date"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jTableFinance.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                jTableFinanceMouseReleased(evt);
            }
        });
        jScrollPane8.setViewportView(jTableFinance);
        if (jTableFinance.getColumnModel().getColumnCount() > 0) {
            jTableFinance.getColumnModel().getColumn(3).setResizable(false);
        }

        jTextFieldAmount.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextFieldAmountActionPerformed(evt);
            }
        });

        jLabel42.setFont(new java.awt.Font("Serif", 0, 14)); // NOI18N
        jLabel42.setText("Transaction ID");

        jLabel43.setFont(new java.awt.Font("Serif", 0, 14)); // NOI18N
        jLabel43.setText("Amount");

        jLabel44.setFont(new java.awt.Font("Serif", 0, 14)); // NOI18N
        jLabel44.setText("Payment method");

        jLabel45.setFont(new java.awt.Font("Serif", 1, 24)); // NOI18N
        jLabel45.setText("Transaction Details");

        jButton14.setFont(new java.awt.Font("Serif", 1, 14)); // NOI18N
        jButton14.setText("Add Transaction");
        jButton14.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton14ActionPerformed(evt);
            }
        });

        jLabel46.setFont(new java.awt.Font("Serif", 0, 14)); // NOI18N
        jLabel46.setText("Transaction type");

        jComboBoxTransactionType.setToolTipText("");

        jLabel47.setFont(new java.awt.Font("Serif", 0, 14)); // NOI18N
        jLabel47.setText("Payment date");

        jComboBoxPaymentMethod.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jComboBoxPaymentMethodActionPerformed(evt);
            }
        });

        jTextFieldTransactionID.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextFieldTransactionIDActionPerformed(evt);
            }
        });

        jButton15.setFont(new java.awt.Font("Serif", 1, 14)); // NOI18N
        jButton15.setText("Add");
        jButton15.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton15ActionPerformed(evt);
            }
        });

        financeReport.setFont(new java.awt.Font("Serif", 1, 14)); // NOI18N
        financeReport.setText("Report");
        financeReport.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                financeReportActionPerformed(evt);
            }
        });

        jTextFieldTransactionDes1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextFieldTransactionDes1ActionPerformed(evt);
            }
        });

        jLabel50.setFont(new java.awt.Font("Serif", 0, 14)); // NOI18N
        jLabel50.setText("Transaction description ");

        financeClearbtn.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        financeClearbtn.setIcon(new javax.swing.ImageIcon(getClass().getResource("/lk/corevision/fruitshop/resources/supplier/clear_all.png"))); // NOI18N
        financeClearbtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                financeClearbtnActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel26Layout = new javax.swing.GroupLayout(jPanel26);
        jPanel26.setLayout(jPanel26Layout);
        jPanel26Layout.setHorizontalGroup(
            jPanel26Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel26Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel26Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel26Layout.createSequentialGroup()
                        .addComponent(jLabel50)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(jPanel26Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel26Layout.createSequentialGroup()
                                .addComponent(jTextFieldTransactionType)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jButton15, javax.swing.GroupLayout.PREFERRED_SIZE, 77, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(jPanel26Layout.createSequentialGroup()
                                .addGroup(jPanel26Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jTextFieldTransactionDes1)
                                    .addComponent(jComboBoxPaymentMethod, javax.swing.GroupLayout.Alignment.TRAILING, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                                .addContainerGap())
                            .addComponent(jTextFieldAmount)
                            .addComponent(jDateChooser2, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                    .addGroup(jPanel26Layout.createSequentialGroup()
                        .addGroup(jPanel26Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel26Layout.createSequentialGroup()
                                .addComponent(jLabel42)
                                .addGap(61, 61, 61)
                                .addComponent(jTextFieldTransactionID))
                            .addGroup(jPanel26Layout.createSequentialGroup()
                                .addGroup(jPanel26Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel46)
                                    .addComponent(jLabel44)
                                    .addComponent(jLabel43, javax.swing.GroupLayout.PREFERRED_SIZE, 98, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jLabel47, javax.swing.GroupLayout.PREFERRED_SIZE, 98, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(50, 50, 50)
                                .addComponent(jComboBoxTransactionType, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel26Layout.createSequentialGroup()
                                .addGap(0, 0, Short.MAX_VALUE)
                                .addGroup(jPanel26Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel26Layout.createSequentialGroup()
                                        .addComponent(jLabel45, javax.swing.GroupLayout.PREFERRED_SIZE, 367, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(financeClearbtn, javax.swing.GroupLayout.PREFERRED_SIZE, 46, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addComponent(jButton14, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 371, javax.swing.GroupLayout.PREFERRED_SIZE))))
                        .addContainerGap())))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel26Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(financeReport, javax.swing.GroupLayout.PREFERRED_SIZE, 371, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        jPanel26Layout.setVerticalGroup(
            jPanel26Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel26Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel26Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jLabel45)
                    .addComponent(financeClearbtn, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(39, 39, 39)
                .addGroup(jPanel26Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel42)
                    .addComponent(jTextFieldTransactionID, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel26Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel50)
                    .addComponent(jTextFieldTransactionDes1, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(7, 7, 7)
                .addGroup(jPanel26Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jComboBoxTransactionType, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel46))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel26Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jButton15, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jTextFieldTransactionType, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel26Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jComboBoxPaymentMethod, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel44))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 20, Short.MAX_VALUE)
                .addGroup(jPanel26Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel43)
                    .addComponent(jTextFieldAmount, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel26Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel47, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jDateChooser2, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jButton14, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(financeReport, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(22, 22, 22))
        );

        jLabel49.setFont(new java.awt.Font("Serif", 0, 12)); // NOI18N
        jLabel49.setText("Sort As");

        sortFinanceComboBox.setFont(new java.awt.Font("Serif", 0, 12)); // NOI18N
        sortFinanceComboBox.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Select", "Credit transactions", "Debit transactions", "Cash transactions", "Card transactions", "Cheque transactions" }));
        sortFinanceComboBox.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                sortFinanceComboBoxItemStateChanged(evt);
            }
        });
        sortFinanceComboBox.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                sortFinanceComboBoxActionPerformed(evt);
            }
        });

        jButton8.setIcon(new javax.swing.ImageIcon(getClass().getResource("/lk/corevision/fruitshop/resources/supplier/reset-black-25.png"))); // NOI18N
        jButton8.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton8ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel6Layout = new javax.swing.GroupLayout(jPanel6);
        jPanel6.setLayout(jPanel6Layout);
        jPanel6Layout.setHorizontalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addComponent(jPanel26, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel6Layout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jScrollPane8, javax.swing.GroupLayout.DEFAULT_SIZE, 739, Short.MAX_VALUE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel6Layout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jLabel49, javax.swing.GroupLayout.PREFERRED_SIZE, 47, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(sortFinanceComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, 175, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(30, 30, 30)
                        .addComponent(jButton8)
                        .addGap(51, 51, 51))))
            .addComponent(jPanel25, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        jPanel6Layout.setVerticalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel6Layout.createSequentialGroup()
                .addGap(8, 8, 8)
                .addComponent(jPanel25, javax.swing.GroupLayout.PREFERRED_SIZE, 54, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                    .addGroup(jPanel6Layout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jPanel26, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel6Layout.createSequentialGroup()
                        .addGap(21, 21, 21)
                        .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(jLabel49)
                                .addComponent(sortFinanceComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(jButton8))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jScrollPane8)))
                .addContainerGap())
        );

        jTabbedPane1.addTab("Finance Process", jPanel6);

        add(jTabbedPane1, java.awt.BorderLayout.CENTER);
    }// </editor-fold>//GEN-END:initComponents

    private void financeReportActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_financeReportActionPerformed
        try {
            String path = "src/lk/corevision/fruitshop/reports/finance/fruitShopAppFinance.jasper";

            HashMap<String, Object> params = new HashMap<>();
            params.put("Parameter101", SendLogo.getFilePath());

            JRTableModelDataSource dataSource = new JRTableModelDataSource(jTableFinance.getModel());
            JasperPrint jasperPrint = JasperFillManager.fillReport(path, params, dataSource);

            JasperViewer.viewReport(jasperPrint, false);
        } catch (Exception e) {
            SignIn.getLoggerObject().log(Level.WARNING, "finance report method exception found", e);

        }
    }//GEN-LAST:event_financeReportActionPerformed

    private void jButton15ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton15ActionPerformed
        // TODO add your handling code here:

        try {
            String newTransactionType = jTextFieldTransactionType.getText().trim(); // TextField for adding new type

            if (newTransactionType.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Please enter a valid transaction type", "Warning", JOptionPane.WARNING_MESSAGE, JOptionIcon.WARNING.setICon());
                return;
            }

            DefaultComboBoxModel<String> model = (DefaultComboBoxModel<String>) jComboBoxTransactionType.getModel();

            // Check if the type already exists in the dropdown
            boolean exists = false;
            for (int i = 0; i < model.getSize(); i++) {
                if (model.getElementAt(i).equalsIgnoreCase(newTransactionType)) {
                    exists = true;
                    break;
                }
            }

            if (!exists) {
                // Add new transaction type to the combo box
                model.addElement(newTransactionType);
                JOptionPane.showMessageDialog(this, "Transaction type added successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
                jComboBoxTransactionType.setSelectedItem(newTransactionType); // Optionally set it as selected
            } else {
                JOptionPane.showMessageDialog(this, "Transaction type already exists", "Info", JOptionPane.INFORMATION_MESSAGE);
            }

            jTextFieldTransactionType.setText("");

        } catch (Exception e) {
            SignIn.getLoggerObject().log(Level.WARNING, "error adding transaction type exception found", e);
        }
    }//GEN-LAST:event_jButton15ActionPerformed

    private void jTextFieldTransactionIDActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextFieldTransactionIDActionPerformed
        jTextFieldTransactionDes1.grabFocus();
    }//GEN-LAST:event_jTextFieldTransactionIDActionPerformed

    private void jButton14ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton14ActionPerformed
        // add finance transaction
        Date paymentDate = jDateChooser2.getDate();

        try {
            String transactionId = "" + System.currentTimeMillis();
            String description = jTextFieldTransactionDes1.getText();
            String transactionType = String.valueOf(jComboBoxTransactionType.getSelectedItem());
            String amountStr = jTextFieldAmount.getText();
            String paymentMethod = String.valueOf(jComboBoxPaymentMethod.getSelectedItem());
//            Date paymentDate = jDateChooser2.getDate();

//            if (description.isEmpty() || transactionType.isEmpty() || amountStr.isEmpty() || paymentMethod.isEmpty() || paymentDate == null) {
//                JOptionPane.showMessageDialog(this, "Please fill all the fields", "Validation Error", JOptionPane.WARNING_MESSAGE, JOptionIcon.WARNING.setICon());
//                return;
            if (description.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Please enter a description", "Warning", JOptionPane.WARNING_MESSAGE, JOptionIcon.WARNING.setICon());
            } else if (jComboBoxTransactionType.getSelectedIndex() == 0) {
                JOptionPane.showMessageDialog(this, "Please select a transaction type", "Warning", JOptionPane.WARNING_MESSAGE, JOptionIcon.WARNING.setICon());
            } else if (jComboBoxPaymentMethod.getSelectedIndex() == 0) {
                JOptionPane.showMessageDialog(this, "Please select a payment method", "Warning", JOptionPane.WARNING_MESSAGE, JOptionIcon.WARNING.setICon());
            } else if (amountStr.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Please enter an amount", "Warning", JOptionPane.WARNING_MESSAGE, JOptionIcon.WARNING.setICon());
            } else if (DataValidator.isInvalidPrice(String.valueOf(amountStr))) {
                JOptionPane.showMessageDialog(this, "Please enter a valid amount", "Error", JOptionPane.ERROR_MESSAGE, JOptionIcon.ERROR.setICon());
            } else if (Integer.parseInt(amountStr) <= 0) {
                JOptionPane.showMessageDialog(this, "Please enter valid amount", "Warning", JOptionPane.WARNING_MESSAGE, JOptionIcon.WARNING.setICon());
            } else if (paymentDate == null) {
                JOptionPane.showMessageDialog(this, "Please select a payment date", "Warning", JOptionPane.WARNING_MESSAGE, JOptionIcon.WARNING.setICon());
                return;
            }
            if (!amountStr.isEmpty() || paymentDate != null) {

                double amount = Double.parseDouble(amountStr);

                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

                Vector<Object> row = new Vector<>();
                row.add(transactionId);
                row.add(description);
                row.add(transactionType);
                row.add(paymentMethod);
                row.add(amount);

                row.add(paymentDate);

                DefaultTableModel model = (DefaultTableModel) jTableFinance.getModel();
                model.addRow(row);

                //insert to database code
                String query = "INSERT INTO `finance` (`transaction_id`, `transaction_type_id`, `amount`, `description`, `date`, `payment_method_id`) "
                        + "VALUES ('" + transactionId + "', '"
                        + transactionTypeMap.get(transactionType) + "', '"
                        + amount + "', '"
                        + description + "', '"
                        + sdf.format(paymentDate) + "', '"
                        + paymentMethodMap.get(paymentMethod) + "')";

                MySQL.executeIUD(query);

                loadFinance();
                resetTransactionForm();
                loadFinancePieChart();
                Notifications.getInstance().show(Notifications.Type.SUCCESS, Notifications.Location.BOTTOM_RIGHT, "Transaction added Successfuly!!");
            }
        } catch (Exception e) {
            SignIn.getLoggerObject().log(Level.WARNING, "error adding finance transaction found", e);
        }
    }//GEN-LAST:event_jButton14ActionPerformed

    private void jTableFinanceMouseReleased(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jTableFinanceMouseReleased
        // TODO add your handling code here:
        if (jTableFinance.getSelectedRow() != -1) {
            if (evt.getButton() == MouseEvent.BUTTON3) { //Right Click
                financePopupMenu.show(evt.getComponent(), evt.getX(), evt.getY());
            }
        }
    }//GEN-LAST:event_jTableFinanceMouseReleased

    private void removeFinanceMenuItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_removeFinanceMenuItemActionPerformed
        int row = jTableFinance.getSelectedRow();

        String FinanceId = String.valueOf(jTableFinance.getValueAt(row, 0));

        if (JOptionPane.showConfirmDialog(this, "Are you sure you want to delete the transaction?", "Confirm Deletion", JOptionPane.OK_CANCEL_OPTION, JOptionPane.WARNING_MESSAGE) == JOptionPane.OK_OPTION) {
            try {
                MySQL.executeIUD("DELETE FROM `finance` WHERE `transaction_id` = '" + FinanceId + "'");

                resetTransactionForm();
                Notifications.getInstance().show(Notifications.Type.SUCCESS, Notifications.Location.BOTTOM_RIGHT, "Transaction deleted  Successfuly!!");

                loadFinance();
            } catch (Exception e) {
                SignIn.getLoggerObject().log(Level.WARNING, "remove finance method exception found", e);
            }
        }
    }//GEN-LAST:event_removeFinanceMenuItemActionPerformed

    private void jButton4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton4ActionPerformed
        jTabbedPane1.setSelectedIndex(1);
    }//GEN-LAST:event_jButton4ActionPerformed

    private void sortFinanceComboBoxItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_sortFinanceComboBoxItemStateChanged
        loadFinance();
    }//GEN-LAST:event_sortFinanceComboBoxItemStateChanged

    private void financeClearbtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_financeClearbtnActionPerformed
        loadFinance();

        resetTransactionForm();
        jTableFinance.clearSelection();
        sortFinanceComboBox.setSelectedIndex(0);
        jTextFieldTransactionDes1.grabFocus();
        Notifications.getInstance().show(Notifications.Type.SUCCESS, Notifications.Location.BOTTOM_RIGHT, "Cleared Successfuly!!");

    }//GEN-LAST:event_financeClearbtnActionPerformed

    private void sortFinanceComboBoxActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_sortFinanceComboBoxActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_sortFinanceComboBoxActionPerformed

    private void jButton8ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton8ActionPerformed
        loadFinance();
        Notifications.getInstance().show(Notifications.Type.SUCCESS, Notifications.Location.BOTTOM_RIGHT, "Refreshed Successfuly!!");

    }//GEN-LAST:event_jButton8ActionPerformed

    private void jTextFieldTransactionDes1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextFieldTransactionDes1ActionPerformed
        jComboBoxTransactionType.grabFocus();
    }//GEN-LAST:event_jTextFieldTransactionDes1ActionPerformed

    private void jComboBoxPaymentMethodActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jComboBoxPaymentMethodActionPerformed
        jTextFieldAmount.grabFocus();
    }//GEN-LAST:event_jComboBoxPaymentMethodActionPerformed

    private void jTextFieldAmountActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextFieldAmountActionPerformed
        jDateChooser2.grabFocus();
    }//GEN-LAST:event_jTextFieldAmountActionPerformed

    private void jButton4MouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jButton4MouseEntered
        jButton4.setBackground(new Color(255, 153, 0));

    }//GEN-LAST:event_jButton4MouseEntered

    private void jButton4MouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jButton4MouseExited
        jButton4.setBackground(new Color(255, 255, 255));
    }//GEN-LAST:event_jButton4MouseExited


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton financeClearbtn;
    private javax.swing.JPopupMenu financePopupMenu;
    private javax.swing.JButton financeReport;
    private javax.swing.JButton jButton14;
    private javax.swing.JButton jButton15;
    private javax.swing.JButton jButton4;
    private javax.swing.JButton jButton8;
    private javax.swing.JComboBox<String> jComboBoxPaymentMethod;
    private javax.swing.JComboBox<String> jComboBoxTransactionType;
    private com.toedter.calendar.JDateChooser jDateChooser2;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel41;
    private javax.swing.JLabel jLabel42;
    private javax.swing.JLabel jLabel43;
    private javax.swing.JLabel jLabel44;
    private javax.swing.JLabel jLabel45;
    private javax.swing.JLabel jLabel46;
    private javax.swing.JLabel jLabel47;
    private javax.swing.JLabel jLabel49;
    private javax.swing.JLabel jLabel50;
    private javax.swing.JPanel jPanel11;
    private javax.swing.JPanel jPanel12;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel25;
    private javax.swing.JPanel jPanel26;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JPanel jPanel7;
    private javax.swing.JScrollPane jScrollPane8;
    private javax.swing.JTabbedPane jTabbedPane1;
    private javax.swing.JTable jTableFinance;
    private javax.swing.JTextField jTextFieldAmount;
    private javax.swing.JTextField jTextFieldTransactionDes1;
    private javax.swing.JTextField jTextFieldTransactionID;
    private javax.swing.JTextField jTextFieldTransactionType;
    private javax.swing.JPanel pieChart1finance;
    private javax.swing.JMenuItem removeFinanceMenuItem;
    private javax.swing.JComboBox<String> sortFinanceComboBox;
    // End of variables declaration//GEN-END:variables

    private void resetTransactionForm() {
//        jTextFieldTransactionID.setText("");
        jTextFieldTransactionDes1.setText("");
        jComboBoxTransactionType.setSelectedIndex(0);
        jTextFieldAmount.setText("");
        jDateChooser2.setDate(null);
        jTextFieldTransactionType.setText("");
        jComboBoxPaymentMethod.setSelectedIndex(0);

    }

}
