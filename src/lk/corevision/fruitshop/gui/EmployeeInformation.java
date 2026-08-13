package lk.corevision.fruitshop.gui;

import java.awt.Color;
import java.awt.event.MouseEvent;
import java.sql.ResultSet;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Vector;
import java.util.logging.Level;
import javax.swing.ButtonModel;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JDialog;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;
import javax.swing.table.DefaultTableModel;
import lk.corevision.fruitshop.model.MySQL;
import lk.corevision.fruitshop.model.QRCodeGenerator;
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
 * @author ADMIN
 */
public class EmployeeInformation extends javax.swing.JPanel {

    private static HashMap<String, String> employeeTypeMap = new HashMap<>();
    private static HashMap<String, String> employeeStatusMap = new HashMap<>();

    private Dashboard dashboard;

    private Transport transport;

    public EmployeeInformation() {
        initComponents();
        loadType();
        loadType2();
        loadEmployeeStatus();
        loadEmployees();
        jButton5.setEnabled(false);
    }

    public void setTransport(Transport transport) {
        this.transport = transport;
    }

    private void loadType() {

        try {

            ResultSet resultSet = MySQL.executeSearch("SELECT * FROM `employee_type`");

            Vector<String> vector = new Vector<>();
            vector.add("Select");

            while (resultSet.next()) {
                vector.add(resultSet.getString("employee_type"));
                employeeTypeMap.put(resultSet.getString("employee_type"), resultSet.getString("employee_type_id"));
            }

            DefaultComboBoxModel model = new DefaultComboBoxModel(vector);
            jComboBox1.setModel(model);

        } catch (Exception e) {
            SignIn.getLoggerObject().log(Level.WARNING, "loadType method Exception Found!", e);
        }

    }

    private void loadType2() {

        try {

            ResultSet resultSet = MySQL.executeSearch("SELECT * FROM `employee_type`");

            Vector<String> vector = new Vector<>();
            vector.add("Select");

            while (resultSet.next()) {
                vector.add(resultSet.getString("employee_type"));
                employeeTypeMap.put(resultSet.getString("employee_type"), resultSet.getString("employee_type_id"));
            }

            DefaultComboBoxModel model = new DefaultComboBoxModel(vector);
            jComboBox3.setModel(model);

        } catch (Exception e) {
            SignIn.getLoggerObject().log(Level.WARNING, "loadType method Exception Found!", e);
        }

    }

    private void loadEmployees() {

        try {

            String query = "SELECT * FROM `employee` INNER JOIN `gender` ON `employee`.`gender_gender_id` = `gender`.`gender_id` "
                    + "INNER JOIN `employee_type` ON `employee`.`employee_type_employee_type_id` = `employee_type`.`employee_type_id` "
                    + "INNER JOIN `employee_status` ON `employee`.`employee_status_id` = `employee_status`.`employee_status_id`";

            boolean hasWhereClause = false;

            if (!jTextField3.getText().isEmpty()) {
                query += "WHERE `employee`.`mobile` LIKE '%" + jTextField3.getText() + "%' ";
                hasWhereClause = true;
            }

            if (!jComboBox4.getSelectedItem().equals("Select")) {

                if (hasWhereClause) {
                    query += "AND ";
                } else {
                    query += "WHERE ";
                    hasWhereClause = true;

                }
                query += "`employee`.`employee_status_id`='" + employeeStatusMap.get(jComboBox4.getSelectedItem()) + "' ";

            }

            if (!jComboBox3.getSelectedItem().equals("Select")) {

                if (hasWhereClause) {
                    query += "AND ";
                } else {
                    query += "WHERE ";
                    hasWhereClause = true;

                }
                query += "`employee`.`employee_type_employee_type_id`='" + employeeTypeMap.get(jComboBox3.getSelectedItem()) + "' ";

            }

            if (jComboBox2.getSelectedIndex() != -1) {

                if (jComboBox2.getSelectedIndex() == 0) {
                    query += "ORDER BY `employee`.`first_name` ASC";
                } else if (jComboBox2.getSelectedIndex() == 1) {
                    query += "ORDER BY `employee`.`first_name` DESC";
                } else if (jComboBox2.getSelectedIndex() == 2) {
                    query += "ORDER BY `employee`.`email` ASC";
                } else if (jComboBox2.getSelectedIndex() == 3) {
                    query += "ORDER BY `employee`.`email` DESC";
                } else if (jComboBox2.getSelectedIndex() == 4) {
                    query += "ORDER BY `employee`.`NIC` ASC";
                } else if (jComboBox2.getSelectedIndex() == 5) {
                    query += "ORDER BY `employee`.`NIC` DESC";
                }

            }

            ResultSet resultSet = MySQL.executeSearch(query);

            DefaultTableModel model = (DefaultTableModel) jTable1.getModel();
            model.setRowCount(0);

            while (resultSet.next()) {

                Vector<String> vector = new Vector<>();
                vector.add(resultSet.getString("first_name"));
                vector.add(resultSet.getString("last_name"));
                vector.add(resultSet.getString("email"));
                vector.add(resultSet.getString("mobile"));
                vector.add(resultSet.getString("NIC"));
                vector.add(resultSet.getString("employee_type.employee_type"));
                vector.add(resultSet.getString("username"));
                vector.add(resultSet.getString("password"));
                vector.add(resultSet.getString("birthday"));
                vector.add(resultSet.getString("gender.gender_name"));
                vector.add(resultSet.getString("employee_status.employee_status_name"));

                model.addRow(vector);

            }

        } catch (Exception e) {
            SignIn.getLoggerObject().log(Level.WARNING, "load Employee method Exception Found!", e);
        }

    }

    private void loadEmployeeStatus() {

        try {
            ResultSet resultSet = MySQL.executeSearch("SELECT * FROM `employee_status`");
            Vector<String> vector = new Vector<>();

            vector.add("Select");
            while (resultSet.next()) {
                vector.add(resultSet.getString("employee_status_name"));
                employeeStatusMap.put(resultSet.getString("employee_status_name"), resultSet.getString("employee_status_id"));
            }
            DefaultComboBoxModel model = new DefaultComboBoxModel(vector);
            jComboBox4.setModel(model);
        } catch (Exception e) {
            SignIn.getLoggerObject().log(Level.WARNING, "load Employee Status method Exception Found!", e);
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

        buttonGroup1 = new javax.swing.ButtonGroup();
        jPopupMenu1 = new javax.swing.JPopupMenu();
        jMenuItem1 = new javax.swing.JMenuItem();
        jMenuItem2 = new javax.swing.JMenuItem();
        jPanel1 = new javax.swing.JPanel();
        jPanel2 = new javax.swing.JPanel();
        jPanel5 = new javax.swing.JPanel();
        jLabel2 = new javax.swing.JLabel();
        jPanel6 = new javax.swing.JPanel();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        jLabel11 = new javax.swing.JLabel();
        jLabel12 = new javax.swing.JLabel();
        jLabel13 = new javax.swing.JLabel();
        jTextField1 = new javax.swing.JTextField();
        jTextField2 = new javax.swing.JTextField();
        jTextField3 = new javax.swing.JTextField();
        jTextField4 = new javax.swing.JTextField();
        jTextField5 = new javax.swing.JTextField();
        jRadioButton1 = new javax.swing.JRadioButton();
        jRadioButton2 = new javax.swing.JRadioButton();
        jComboBox1 = new javax.swing.JComboBox<>();
        jButton4 = new javax.swing.JButton();
        jButton5 = new javax.swing.JButton();
        jButton6 = new javax.swing.JButton();
        jButton7 = new javax.swing.JButton();
        jTextField17 = new javax.swing.JTextField();
        jLabel14 = new javax.swing.JLabel();
        jPasswordField1 = new javax.swing.JPasswordField();
        jLabel41 = new javax.swing.JLabel();
        jDateChooser1 = new com.toedter.calendar.JDateChooser();
        jPanel7 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTable1 = new javax.swing.JTable();
        jPanel8 = new javax.swing.JPanel();
        jLabel15 = new javax.swing.JLabel();
        jComboBox2 = new javax.swing.JComboBox<>();
        jLabel3 = new javax.swing.JLabel();
        jComboBox4 = new javax.swing.JComboBox<>();
        jLabel9 = new javax.swing.JLabel();
        jComboBox3 = new javax.swing.JComboBox<>();
        jButton1 = new javax.swing.JButton();

        jMenuItem1.setText("Update");
        jMenuItem1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem1ActionPerformed(evt);
            }
        });
        jPopupMenu1.add(jMenuItem1);

        jMenuItem2.setText("Address View");
        jMenuItem2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem2ActionPerformed(evt);
            }
        });
        jPopupMenu1.add(jMenuItem2);

        setLayout(new java.awt.BorderLayout());

        jLabel2.setFont(new java.awt.Font("Times New Roman", 1, 18)); // NOI18N
        jLabel2.setText("Employee Information");

        javax.swing.GroupLayout jPanel5Layout = new javax.swing.GroupLayout(jPanel5);
        jPanel5.setLayout(jPanel5Layout);
        jPanel5Layout.setHorizontalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addGap(16, 16, 16)
                .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 185, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel5Layout.setVerticalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addGap(14, 14, 14)
                .addComponent(jLabel2)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jLabel4.setFont(new java.awt.Font("Times New Roman", 0, 14)); // NOI18N
        jLabel4.setText("First Name :");

        jLabel5.setFont(new java.awt.Font("Times New Roman", 0, 14)); // NOI18N
        jLabel5.setText("Last Name :");

        jLabel6.setFont(new java.awt.Font("Times New Roman", 0, 14)); // NOI18N
        jLabel6.setText("Mobile :");

        jLabel7.setFont(new java.awt.Font("Times New Roman", 0, 14)); // NOI18N
        jLabel7.setText("Email :");

        jLabel8.setFont(new java.awt.Font("Times New Roman", 0, 14)); // NOI18N
        jLabel8.setText("NIC :");

        jLabel11.setFont(new java.awt.Font("Times New Roman", 0, 14)); // NOI18N
        jLabel11.setText("Type :");

        jLabel12.setFont(new java.awt.Font("Times New Roman", 0, 14)); // NOI18N
        jLabel12.setText("Username :");

        jLabel13.setFont(new java.awt.Font("Times New Roman", 0, 14)); // NOI18N
        jLabel13.setText("Gender :");

        jTextField3.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                jTextField3KeyReleased(evt);
            }
        });

        buttonGroup1.add(jRadioButton1);
        jRadioButton1.setText("Male");
        jRadioButton1.setActionCommand("1");

        buttonGroup1.add(jRadioButton2);
        jRadioButton2.setText("Female");
        jRadioButton2.setActionCommand("2");

        jComboBox1.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));

        jButton4.setFont(new java.awt.Font("Times New Roman", 1, 12)); // NOI18N
        jButton4.setText("Create Account");
        jButton4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton4ActionPerformed(evt);
            }
        });

        jButton5.setFont(new java.awt.Font("Times New Roman", 1, 12)); // NOI18N
        jButton5.setText("Deactivate");
        jButton5.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton5ActionPerformed(evt);
            }
        });

        jButton6.setFont(new java.awt.Font("Times New Roman", 1, 12)); // NOI18N
        jButton6.setText("Update");
        jButton6.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton6ActionPerformed(evt);
            }
        });

        jButton7.setIcon(new javax.swing.ImageIcon(getClass().getResource("/resources/clearAll.png"))); // NOI18N
        jButton7.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton7ActionPerformed(evt);
            }
        });

        jLabel14.setFont(new java.awt.Font("Times New Roman", 0, 14)); // NOI18N
        jLabel14.setText("Password :");

        jLabel41.setFont(new java.awt.Font("Times New Roman", 0, 14)); // NOI18N
        jLabel41.setText("Birthday :");

        jDateChooser1.setDateFormatString("yyyy-MM-dd");

        javax.swing.GroupLayout jPanel6Layout = new javax.swing.GroupLayout(jPanel6);
        jPanel6.setLayout(jPanel6Layout);
        jPanel6Layout.setHorizontalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel6Layout.createSequentialGroup()
                        .addGap(17, 17, 17)
                        .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel6Layout.createSequentialGroup()
                                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addComponent(jLabel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(jLabel5, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(jLabel6)
                                    .addComponent(jLabel7)
                                    .addComponent(jLabel8)
                                    .addComponent(jLabel11)
                                    .addComponent(jLabel12, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(jLabel13)
                                    .addComponent(jLabel14)
                                    .addComponent(jLabel41, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jTextField1)
                                    .addComponent(jTextField2)
                                    .addComponent(jTextField3)
                                    .addComponent(jTextField4)
                                    .addComponent(jTextField5)
                                    .addComponent(jComboBox1, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(jTextField17)
                                    .addComponent(jPasswordField1, javax.swing.GroupLayout.Alignment.TRAILING)
                                    .addGroup(jPanel6Layout.createSequentialGroup()
                                        .addGap(9, 9, 9)
                                        .addComponent(jRadioButton1)
                                        .addGap(18, 18, 18)
                                        .addComponent(jRadioButton2)
                                        .addGap(0, 0, Short.MAX_VALUE))
                                    .addComponent(jDateChooser1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                            .addGroup(jPanel6Layout.createSequentialGroup()
                                .addComponent(jButton5, javax.swing.GroupLayout.DEFAULT_SIZE, 91, Short.MAX_VALUE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jButton6, javax.swing.GroupLayout.PREFERRED_SIZE, 101, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jButton7, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE))))
                    .addGroup(jPanel6Layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jButton4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                .addContainerGap())
        );
        jPanel6Layout.setVerticalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addGap(15, 15, 15)
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel4)
                    .addComponent(jTextField1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(20, 20, 20)
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel5)
                    .addComponent(jTextField2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(20, 20, 20)
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel6)
                    .addComponent(jTextField3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(20, 20, 20)
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel7)
                    .addComponent(jTextField4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(20, 20, 20)
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel8)
                    .addComponent(jTextField5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(20, 20, 20)
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel11)
                    .addComponent(jComboBox1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(20, 20, 20)
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel12)
                    .addComponent(jTextField17, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(20, 20, 20)
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel14)
                    .addComponent(jPasswordField1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(20, 20, 20)
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jRadioButton1)
                    .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabel13)
                        .addComponent(jRadioButton2)))
                .addGap(20, 20, 20)
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel41)
                    .addComponent(jDateChooser1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jButton4)
                .addGap(20, 20, 20)
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jButton5)
                        .addComponent(jButton6))
                    .addComponent(jButton7))
                .addGap(20, 20, 20))
        );

        jPanel7.setLayout(new java.awt.BorderLayout());

        jTable1.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "First Name", "Last Name", "Email", "Mobile", "NIC", "Type", "User Name", "Password", "Birthday", "Gender", "Status"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false, false, false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jTable1.getTableHeader().setReorderingAllowed(false);
        jTable1.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jTable1MouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(jTable1);

        jPanel7.add(jScrollPane1, java.awt.BorderLayout.CENTER);

        jLabel15.setFont(new java.awt.Font("Times New Roman", 0, 14)); // NOI18N
        jLabel15.setText("Sort By :");

        jComboBox2.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Name (A - Z)", "Name (Z - A)", "Email (A - Z)", "Email (Z - A)", "NIC (0 - 9)", "NIC (9 - 0)" }));
        jComboBox2.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                jComboBox2ItemStateChanged(evt);
            }
        });

        jLabel3.setFont(new java.awt.Font("Times New Roman", 0, 14)); // NOI18N
        jLabel3.setText("Employee Status :");

        jComboBox4.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1" }));
        jComboBox4.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                jComboBox4ItemStateChanged(evt);
            }
        });

        jLabel9.setFont(new java.awt.Font("Times New Roman", 0, 14)); // NOI18N
        jLabel9.setText("Employee Type :");

        jComboBox3.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        jComboBox3.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                jComboBox3ItemStateChanged(evt);
            }
        });

        javax.swing.GroupLayout jPanel8Layout = new javax.swing.GroupLayout(jPanel8);
        jPanel8.setLayout(jPanel8Layout);
        jPanel8Layout.setHorizontalGroup(
            jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel8Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel3)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jComboBox4, javax.swing.GroupLayout.PREFERRED_SIZE, 114, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(82, 82, 82)
                .addComponent(jLabel9)
                .addGap(18, 18, 18)
                .addComponent(jComboBox3, javax.swing.GroupLayout.PREFERRED_SIZE, 114, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 88, Short.MAX_VALUE)
                .addComponent(jLabel15)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jComboBox2, javax.swing.GroupLayout.PREFERRED_SIZE, 149, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(21, 21, 21))
        );
        jPanel8Layout.setVerticalGroup(
            jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel8Layout.createSequentialGroup()
                .addGap(18, 18, 18)
                .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel15)
                    .addComponent(jComboBox2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel3)
                    .addComponent(jComboBox4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel9)
                    .addComponent(jComboBox3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(19, Short.MAX_VALUE))
        );

        jButton1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/resources/PrintIcon.png"))); // NOI18N
        jButton1.setText("Print");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jPanel6, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel5, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel7, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel8, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jButton1)
                .addContainerGap())
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                    .addComponent(jPanel8, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel5, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jPanel6, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel7, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jButton1)
                .addGap(19, 19, 19))
        );

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 1114, Short.MAX_VALUE)
            .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addComponent(jPanel2, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 671, Short.MAX_VALUE)
            .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        add(jPanel1, java.awt.BorderLayout.CENTER);
    }// </editor-fold>//GEN-END:initComponents

    private void jButton4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton4ActionPerformed

        if (Dashboard.getEmployeeTypeName().equals("Admin") || Dashboard.getEmployeeTypeName().equals("HR Manager")) {

            try {

                String fname = jTextField1.getText();
                String lname = jTextField2.getText();
                String mobile = jTextField3.getText();
                String email = jTextField4.getText();
                String nic = jTextField5.getText();
                String type = String.valueOf(jComboBox1.getSelectedItem());
                String un = jTextField17.getText();
                String pw = String.valueOf(jPasswordField1.getPassword());
                ButtonModel gender = buttonGroup1.getSelection();

                Date bd = jDateChooser1.getDate();

                SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd");

                if (fname.isEmpty()) {
                    Notifications.getInstance().show(Notifications.Type.WARNING, Notifications.Location.BOTTOM_RIGHT, "Please Enter First Name");
                } else if (lname.isEmpty()) {
                    Notifications.getInstance().show(Notifications.Type.WARNING, Notifications.Location.BOTTOM_RIGHT, "Please Enter Last Name");
                } else if (mobile.isEmpty()) {
                    Notifications.getInstance().show(Notifications.Type.WARNING, Notifications.Location.BOTTOM_RIGHT, "Please Enter Mobile Number");
                } else if (DataValidator.isInvalidMobile(mobile)) {
                    Notifications.getInstance().show(Notifications.Type.WARNING, Notifications.Location.BOTTOM_RIGHT, "Please Enter valid Mobile Number");
                } else if (email.isEmpty()) {
                    Notifications.getInstance().show(Notifications.Type.WARNING, Notifications.Location.BOTTOM_RIGHT, "Please Enter Email");
                } else if (DataValidator.isInvalidEmail(email)) {
                    Notifications.getInstance().show(Notifications.Type.WARNING, Notifications.Location.BOTTOM_RIGHT, "Please Enter valid Email");
                } else if (nic.isEmpty()) {
                    Notifications.getInstance().show(Notifications.Type.WARNING, Notifications.Location.BOTTOM_RIGHT, "Please Enter NIC");
                } else if (type.equals("Select")) {
                    Notifications.getInstance().show(Notifications.Type.WARNING, Notifications.Location.BOTTOM_RIGHT, "Please Select Type");
                } else if (un.isEmpty()) {
                    Notifications.getInstance().show(Notifications.Type.WARNING, Notifications.Location.BOTTOM_RIGHT, "Please Enter Username");
                } else if (pw.isEmpty()) {
                    Notifications.getInstance().show(Notifications.Type.WARNING, Notifications.Location.BOTTOM_RIGHT, "Please Enter Password");
                } else if (DataValidator.isInvalidPassword(pw)) {
                    Notifications.getInstance().show(Notifications.Type.WARNING, Notifications.Location.BOTTOM_RIGHT, "Please Enter a valid Password");
                } else if (gender == null) {
                    Notifications.getInstance().show(Notifications.Type.WARNING, Notifications.Location.BOTTOM_RIGHT, "Please Select Gender");
                } else if (bd == null) {
                    Notifications.getInstance().show(Notifications.Type.WARNING, Notifications.Location.BOTTOM_RIGHT, "Please Select Birthday");
                } else {

                    ResultSet resultSet = MySQL.executeSearch("SELECT * FROM `employee` WHERE `email`='" + email + "' OR `nic`='" + nic + "' "
                            + "OR `mobile`='" + mobile + "'");

                    if (resultSet.next()) {
                        Notifications.getInstance().show(Notifications.Type.WARNING, Notifications.Location.BOTTOM_RIGHT, "This User already Registered");
                    } else {

                        Date date = new Date();
                        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

                        String genderId = gender.getActionCommand();

                        MySQL.executeIUD("INSERT INTO `employee`(`first_name`,`last_name`,`mobile`,`email`,`NIC`,`employee_type_employee_type_id`,"
                                + "`username`,`password`,`gender_gender_id`,`birthday`,`registered_date`,`employee_status_id`) "
                                + "VALUES('" + fname + "','" + lname + "','" + mobile + "','" + email + "','" + nic + "','" + employeeTypeMap.get(type) + "',"
                                + "'" + un + "','" + pw + "','" + genderId + "','" + sdf1.format(bd) + "','" + sdf.format(date) + "','1')");

                        loadEmployees();
                        reset();
                        Notifications.getInstance().show(Notifications.Type.SUCCESS, Notifications.Location.BOTTOM_RIGHT, "Employee added successfully");

                        QRCodeGenerator qr = new QRCodeGenerator(nic);

                        if (qr.isGenerated()) {
                            EmployeeMailDialog mail = new EmployeeMailDialog(dashboard, true, fname, lname, nic, email);
                            mail.setVisible(true);
                        } else {
                            JOptionPane.showMessageDialog(this, "Employee saved but QR code generation failed.\nPlease generate the QR code and send the email manually.", "Warning", JOptionPane.WARNING_MESSAGE, JOptionIcon.WARNING.setICon());
                        }
                    }

                }

            } catch (Exception e) {
                SignIn.getLoggerObject().log(Level.WARNING, "Employee onboarding unsuccessful!", e);
            }
        } else {
            JOptionPane.showMessageDialog(this, "Access blocked for role: " + Dashboard.getEmployeeTypeName(), "Access Denied", JOptionPane.WARNING_MESSAGE, JOptionIcon.WARNING.setICon());
        }
    }//GEN-LAST:event_jButton4ActionPerformed

    private void jTable1MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jTable1MouseClicked

        int row = jTable1.getSelectedRow();

        if (evt.getClickCount() == 2 && transport != null) {
            String nic = String.valueOf(jTable1.getValueAt(row, 4));

            try {

                ResultSet resultSet = MySQL.executeSearch("SELECT * FROM `employee` INNER JOIN `employee_type` ON `employee`.`employee_type_employee_type_id`=`employee_type`.`employee_type_id` WHERE `NIC`='" + nic + "'");
                if (resultSet.next()) {
                    if (resultSet.getString("employee_type.employee_type").equals("Driver")) {
                        transport.setEmployeeNic(nic);
                        SwingUtilities.getWindowAncestor(this).dispose();
                    } else {

                        JOptionPane.showMessageDialog(this, "The Selected Employee is not a Driver.", "Erroe", JOptionPane.OK_OPTION, JOptionIcon.ERROR.setICon());

                    }
                }

            } catch (Exception e) {
                e.printStackTrace();
            }

        }

        if (MouseEvent.BUTTON3 == evt.getButton() && row != -1) {
            jPopupMenu1.show(evt.getComponent(), evt.getX(), evt.getY());
        }

    }//GEN-LAST:event_jTable1MouseClicked


    private void jButton6ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton6ActionPerformed

        if (Dashboard.getEmployeeTypeName().equals("Admin") || Dashboard.getEmployeeTypeName().equals("HR Manager")) {

            try {

                String fname = jTextField1.getText();
                String lname = jTextField2.getText();
                String mobile = jTextField3.getText();
                String email = jTextField4.getText();
                String nic = jTextField5.getText();
                String type = String.valueOf(jComboBox1.getSelectedItem());
                String un = jTextField17.getText();
                String pw = String.valueOf(jPasswordField1.getPassword());
                ButtonModel gender = buttonGroup1.getSelection();
                Date bd = jDateChooser1.getDate();

                int genderId;

                SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd");

                if (fname.isEmpty()) {
                    Notifications.getInstance().show(Notifications.Type.WARNING, Notifications.Location.BOTTOM_RIGHT, "Please Enter First Name");
                } else if (lname.isEmpty()) {
                    Notifications.getInstance().show(Notifications.Type.WARNING, Notifications.Location.BOTTOM_RIGHT, "Please Enter Last Name");
                } else if (mobile.isEmpty()) {
                    Notifications.getInstance().show(Notifications.Type.WARNING, Notifications.Location.BOTTOM_RIGHT, "Please Enter Mobile Number");
                } else if (DataValidator.isInvalidMobile(mobile)) {
                    Notifications.getInstance().show(Notifications.Type.WARNING, Notifications.Location.BOTTOM_RIGHT, "Please Enter valid Mobile Number");
                } else if (email.isEmpty()) {
                    Notifications.getInstance().show(Notifications.Type.WARNING, Notifications.Location.BOTTOM_RIGHT, "Please Enter Email");
                } else if (DataValidator.isInvalidEmail(email)) {
                    Notifications.getInstance().show(Notifications.Type.WARNING, Notifications.Location.BOTTOM_RIGHT, "Please Enter valid Email");
                } else if (type.equals("Select")) {
                    Notifications.getInstance().show(Notifications.Type.WARNING, Notifications.Location.BOTTOM_RIGHT, "Please Select Type");
                } else if (un.isEmpty()) {
                    Notifications.getInstance().show(Notifications.Type.WARNING, Notifications.Location.BOTTOM_RIGHT, "Please Enter User Name");
                } else if (pw.isEmpty()) {
                    Notifications.getInstance().show(Notifications.Type.WARNING, Notifications.Location.BOTTOM_RIGHT, "Please Enter Password");
                } else if (DataValidator.isInvalidPassword(pw)) {
                    Notifications.getInstance().show(Notifications.Type.WARNING, Notifications.Location.BOTTOM_RIGHT, "Please Enter valid Password");
                } else if (gender == null) {
                    Notifications.getInstance().show(Notifications.Type.WARNING, Notifications.Location.BOTTOM_RIGHT, "Please Select Gender");
                } else if (bd == null) {
                    Notifications.getInstance().show(Notifications.Type.WARNING, Notifications.Location.BOTTOM_RIGHT, "Please Select Birthday");
                } else {

                    ResultSet resultSet = MySQL.executeSearch("SELECT * FROM `employee` WHERE `email`='" + email + "' OR `mobile`='" + mobile + "'");

                    boolean canUpdate = false;

                    if (resultSet.next()) {

                        if (!resultSet.getString("email").equals(email)) {
                            Notifications.getInstance().show(Notifications.Type.WARNING, Notifications.Location.BOTTOM_RIGHT, "This Mobile Number or Email Address already Registered");
                        } else {
                            canUpdate = true;
                        }
                    } else {
                        canUpdate = true;
                    }
                    if (canUpdate) {

                        genderId = gender == jRadioButton1.getModel() ? 1 : 2;

                        MySQL.executeIUD("UPDATE `employee` SET `first_name`='" + fname + "', `last_name`='" + lname + "', `mobile`='" + mobile + "', "
                                + "`email`='" + email + "', `employee_type_employee_type_id`='" + employeeTypeMap.get(type) + "', "
                                + "`username`='" + un + "', `password`='" + pw + "', `gender_gender_id`='" + genderId + "', "
                                + "`birthday`='" + sdf1.format(bd) + "' WHERE `NIC`='" + nic + "'");

                        loadEmployees();
                        Notifications.getInstance().show(Notifications.Type.SUCCESS, Notifications.Location.BOTTOM_RIGHT, "Employee Updated Successfully");
                        reset();
                    }

                }

            } catch (Exception e) {
                SignIn.getLoggerObject().log(Level.WARNING, "EmployeeUpdated method Exception Found!", e);
            }
        } else {
            JOptionPane.showMessageDialog(this, "Access blocked for role: " + Dashboard.getEmployeeTypeName(), "Access Denied", JOptionPane.WARNING_MESSAGE, JOptionIcon.WARNING.setICon());
        }
    }//GEN-LAST:event_jButton6ActionPerformed

    private void jButton7ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton7ActionPerformed
        reset();
    }//GEN-LAST:event_jButton7ActionPerformed

    private void jComboBox2ItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_jComboBox2ItemStateChanged
        loadEmployees();
    }//GEN-LAST:event_jComboBox2ItemStateChanged

    private void jButton5ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton5ActionPerformed

        if (Dashboard.getEmployeeTypeName().equals("Admin") || Dashboard.getEmployeeTypeName().equals("HR Manager")) {

            try {

                int row = jTable1.getSelectedRow();

                String status = String.valueOf(jTable1.getValueAt(row, 10));
                String nic = String.valueOf(jTable1.getValueAt(row, 4));
                ResultSet resultSet = MySQL.executeSearch("SELECT * FROM `employee` WHERE `NIC`='" + nic + "'");

                if (resultSet.next()) {
                    if (resultSet.getInt("employee_status_id") == 1) {
                        MySQL.executeIUD("UPDATE `employee` SET `employee_status_id`='2' WHERE `NIC`='" + nic + "'");
                        JOptionPane.showMessageDialog(this, "Employee Deactivaed", "Success", JOptionPane.INFORMATION_MESSAGE);
                    } else if (resultSet.getInt("employee_status_id") == 2) {
                        MySQL.executeIUD("UPDATE `employee` SET `employee_status_id`='1' WHERE `NIC`='" + nic + "'");
                        JOptionPane.showMessageDialog(this, "Employee Activaed", "Success", JOptionPane.INFORMATION_MESSAGE);

                    }
                    loadEmployees();
                    reset();
                }

            } catch (Exception e) {
                SignIn.getLoggerObject().log(Level.WARNING, "Employee Status Change method Exception Found!", e);
            }
        } else {
            JOptionPane.showMessageDialog(this, "Access blocked for role: " + Dashboard.getEmployeeTypeName(), "Access Denied", JOptionPane.WARNING_MESSAGE, JOptionIcon.WARNING.setICon());
        }

    }//GEN-LAST:event_jButton5ActionPerformed

    private void jMenuItem1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem1ActionPerformed

        if (Dashboard.getEmployeeTypeName().equals("Admin") || Dashboard.getEmployeeTypeName().equals("HR Manager")) {

            try {

                int row = jTable1.getSelectedRow();

                jButton5.setEnabled(true);

                String firstName = String.valueOf(jTable1.getValueAt(row, 0));
                jTextField1.setText(firstName);

                String lastName = String.valueOf(jTable1.getValueAt(row, 1));
                jTextField2.setText(lastName);

                String mobile = String.valueOf(jTable1.getValueAt(row, 3));
                jTextField3.setText(mobile);

                String email = String.valueOf(jTable1.getValueAt(row, 2));
                jTextField4.setText(email);

                String nic = String.valueOf(jTable1.getValueAt(row, 4));
                jTextField5.setText(nic);
                jTextField5.setEditable(false);

                String type = String.valueOf(jTable1.getValueAt(row, 5));
                jComboBox1.setSelectedItem(type);

                String userName = String.valueOf(jTable1.getValueAt(row, 6));
                jTextField17.setText(userName);
                jTextField17.setEditable(false);

                String password = String.valueOf(jTable1.getValueAt(row, 7));
                jPasswordField1.setText(password);

                String birthday = String.valueOf(jTable1.getValueAt(row, 8));

                Date date2 = new SimpleDateFormat("yyyy-MM-dd").parse(birthday);
                jDateChooser1.setDate(date2);

                String gender = String.valueOf(jTable1.getValueAt(row, 9));

                if (gender.equals("Male")) {
                    jRadioButton1.setSelected(true);
                }

                if (gender.equals("Female")) {
                    jRadioButton2.setSelected(true);
                }

                String status = String.valueOf(jTable1.getValueAt(row, 10));

                if (status.equals("Active")) {
                    jButton5.setText("Deactive");
                    jButton5.setForeground(Color.red);
                } else {
                    jButton5.setText("Active");
                    jButton5.setForeground(Color.GREEN);
                }

            } catch (Exception e) {
                SignIn.getLoggerObject().log(Level.WARNING, "Employee Select method Exception Found!", e);
            }
        } else {
            JOptionPane.showMessageDialog(this, "Access blocked for role: " + Dashboard.getEmployeeTypeName(), "Access Denied", JOptionPane.WARNING_MESSAGE, JOptionIcon.WARNING.setICon());
        }
    }//GEN-LAST:event_jMenuItem1ActionPerformed

    private void jTextField3KeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTextField3KeyReleased
        loadEmployees();
    }//GEN-LAST:event_jTextField3KeyReleased

    private void jComboBox4ItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_jComboBox4ItemStateChanged
        loadEmployees();
    }//GEN-LAST:event_jComboBox4ItemStateChanged

    private void jMenuItem2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem2ActionPerformed

        if (Dashboard.getEmployeeTypeName().equals("Admin") || Dashboard.getEmployeeTypeName().equals("HR Manager")) {

            int row1 = jTable1.getSelectedRow();
            String email1 = String.valueOf(jTable1.getValueAt(row1, 2));
            String NIC = String.valueOf(jTable1.getValueAt(row1, 4));

            AddressView av = new AddressView(dashboard, true, email1, NIC);
            av.setVisible(true);

        } else {
            JOptionPane.showMessageDialog(this, "Access blocked for role: " + Dashboard.getEmployeeTypeName(), "Access Denied", JOptionPane.WARNING_MESSAGE, JOptionIcon.WARNING.setICon());
        }
    }//GEN-LAST:event_jMenuItem2ActionPerformed

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed

        if (Dashboard.getEmployeeTypeName().equals("Admin") || Dashboard.getEmployeeTypeName().equals("HR Manager")) {

            Date date = new Date();
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
            String reportDate = dateFormat.format(date);

            try {
                HashMap<String, Object> parameters = new HashMap<>();
                parameters.put("Parameter1", reportDate);
                parameters.put("Parameter2", SignIn.getEmployeeNIC());
                parameters.put("Parameter101", SendLogo.getFilePath());

                JRTableModelDataSource dataSource = new JRTableModelDataSource(jTable1.getModel());
                JasperPrint report = JasperFillManager.fillReport("src/lk/corevision/fruitshop/reports/employee/fruitShopAppEIReport.jasper", parameters, dataSource);

                JasperViewer.viewReport(report, false);

            } catch (Exception e) {
                SignIn.getLoggerObject().log(Level.WARNING, "Employee Information Print method Exception Found!", e);
            }
        } else {
            JOptionPane.showMessageDialog(this, "Access blocked for role: " + Dashboard.getEmployeeTypeName(), "Access Denied", JOptionPane.WARNING_MESSAGE, JOptionIcon.WARNING.setICon());
        }
    }//GEN-LAST:event_jButton1ActionPerformed

    private void jComboBox3ItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_jComboBox3ItemStateChanged
        loadEmployees();
    }//GEN-LAST:event_jComboBox3ItemStateChanged


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.ButtonGroup buttonGroup1;
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton4;
    private javax.swing.JButton jButton5;
    private javax.swing.JButton jButton6;
    private javax.swing.JButton jButton7;
    private javax.swing.JComboBox<String> jComboBox1;
    private javax.swing.JComboBox<String> jComboBox2;
    private javax.swing.JComboBox<String> jComboBox3;
    private javax.swing.JComboBox<String> jComboBox4;
    private com.toedter.calendar.JDateChooser jDateChooser1;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel15;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel41;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JMenuItem jMenuItem1;
    private javax.swing.JMenuItem jMenuItem2;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JPanel jPanel7;
    private javax.swing.JPanel jPanel8;
    private javax.swing.JPasswordField jPasswordField1;
    private javax.swing.JPopupMenu jPopupMenu1;
    private javax.swing.JRadioButton jRadioButton1;
    private javax.swing.JRadioButton jRadioButton2;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTable jTable1;
    private javax.swing.JTextField jTextField1;
    private javax.swing.JTextField jTextField17;
    private javax.swing.JTextField jTextField2;
    private javax.swing.JTextField jTextField3;
    private javax.swing.JTextField jTextField4;
    private javax.swing.JTextField jTextField5;
    // End of variables declaration//GEN-END:variables

    private void reset() {
        jTextField1.setText("");
        jTextField2.setText("");
        jTextField3.setText("");
        jTextField4.setText("");
        jTextField5.setText("");
        jTextField5.setEditable(true);
        jTextField17.setEditable(true);
        jTextField17.setText("");
        jPasswordField1.setText("");
        jComboBox1.setSelectedIndex(0);
        buttonGroup1.clearSelection();
        jDateChooser1.setDate(null);
        jButton5.setEnabled(false);
        jButton5.setText("Deactive");
        jComboBox2.setSelectedIndex(0);
        jComboBox4.setSelectedIndex(0);
        jTable1.clearSelection();
        jTextField1.grabFocus();
        loadEmployees();
    }

}
