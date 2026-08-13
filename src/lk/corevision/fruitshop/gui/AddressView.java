/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JDialog.java to edit this template
 */
package lk.corevision.fruitshop.gui;

import java.util.HashMap;
import java.sql.ResultSet;
import java.util.Vector;
import java.util.logging.Level;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;
import lk.corevision.fruitshop.model.MySQL;
import lk.corevision.fruitshop.validate.JOptionIcon;

/**
 *
 * @author ADMIN
 */
public class AddressView extends javax.swing.JDialog {

    private HashMap<String, String> cityMap = new HashMap<>();
    private HashMap<String, String> districtMap = new HashMap<>();

    private String email;
    private String NIC;

    public AddressView(java.awt.Frame parent, boolean modal, String email, String NIC) {
        super(parent, modal);
        initComponents();
        jLabel1.setText(email);
        this.email = email;
        this.NIC = NIC;
        loadCity();
        loadDistrict();
        loadAddress();
        jButton2.setEnabled(false);
    }

    private void loadCity() {

        try {

            ResultSet resultSet = MySQL.executeSearch("SELECT * FROM `city`");

            Vector<String> vector = new Vector<>();
            vector.add("Select");

            while (resultSet.next()) {
                vector.add(resultSet.getString("city_name"));
                cityMap.put(resultSet.getString("city_name"), resultSet.getString("city_id"));

            }

            DefaultComboBoxModel model = new DefaultComboBoxModel(vector);
            jComboBox1.setModel(model);

        } catch (Exception e) {
            SignIn.getLoggerObject().log(Level.WARNING, "Load City method Exception Found!", e);
        }
    }

    private void loadDistrict() {

        try {

            ResultSet resultSet = MySQL.executeSearch("SELECT * FROM `district`");

            Vector<String> vector = new Vector<>();
            vector.add("Select");

            while (resultSet.next()) {
                vector.add(resultSet.getString("district_name"));
                districtMap.put(resultSet.getString("district_name"), resultSet.getString("district_id"));

            }

            DefaultComboBoxModel model = new DefaultComboBoxModel(vector);
            jComboBox2.setModel(model);

        } catch (Exception e) {
            SignIn.getLoggerObject().log(Level.WARNING, "Load District method Exception Found!", e);
        }
    }

    private void loadAddress() {

        try {

            ResultSet resultSet = MySQL.executeSearch("SELECT * FROM `employee_address` INNER JOIN `city` ON "
                    + "`employee_address`.`city_city_id`=`city`.`city_id` INNER JOIN `district` ON `city`.`district_district_id`=`district`.`district_id` "
                    + "WHERE `employee_NIC` = '" + this.NIC + "'");

            DefaultTableModel model = (DefaultTableModel) jTable1.getModel();
            model.setRowCount(0);

            while (resultSet.next()) {

                Vector<String> vector = new Vector<>();
                vector.add(resultSet.getString("line1"));
                vector.add(resultSet.getString("line2"));
                vector.add(resultSet.getString("postal_code"));
                vector.add(resultSet.getString("city.city_name"));
                vector.add(resultSet.getString("district.district_name"));

                model.addRow(vector);
            }

        } catch (Exception e) {
            SignIn.getLoggerObject().log(Level.WARNING, "Load Employee Address method Exception Found!", e);
        }

    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        jLabel2 = new javax.swing.JLabel();
        jLabel1 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        jTextField1 = new javax.swing.JTextField();
        jTextField2 = new javax.swing.JTextField();
        jComboBox1 = new javax.swing.JComboBox<>();
        jButton1 = new javax.swing.JButton();
        jButton2 = new javax.swing.JButton();
        jButton3 = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTable1 = new javax.swing.JTable();
        jTextField3 = new javax.swing.JTextField();
        jButton4 = new javax.swing.JButton();
        jLabel6 = new javax.swing.JLabel();
        jTextField4 = new javax.swing.JTextField();
        jLabel7 = new javax.swing.JLabel();
        jComboBox2 = new javax.swing.JComboBox<>();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setResizable(false);

        jLabel2.setFont(new java.awt.Font("Times New Roman", 1, 18)); // NOI18N
        jLabel2.setText("Employee Address");

        jLabel1.setFont(new java.awt.Font("Segoe UI Black", 0, 14)); // NOI18N
        jLabel1.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabel1.setText("Employee Email");

        jLabel3.setFont(new java.awt.Font("Times New Roman", 0, 14)); // NOI18N
        jLabel3.setText("Address Line 1 :");

        jLabel4.setFont(new java.awt.Font("Times New Roman", 0, 14)); // NOI18N
        jLabel4.setText("Address Line 2 :");

        jLabel5.setFont(new java.awt.Font("Times New Roman", 0, 14)); // NOI18N
        jLabel5.setText("City :");

        jComboBox1.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                jComboBox1ItemStateChanged(evt);
            }
        });

        jButton1.setFont(new java.awt.Font("Times New Roman", 1, 14)); // NOI18N
        jButton1.setText("Add");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        jButton2.setFont(new java.awt.Font("Times New Roman", 1, 14)); // NOI18N
        jButton2.setText("Update");
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });

        jButton3.setIcon(new javax.swing.ImageIcon(getClass().getResource("/resources/clearAll.png"))); // NOI18N
        jButton3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton3ActionPerformed(evt);
            }
        });

        jTable1.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Line 1", "Line 2", "Postal Code", "City", "District"
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

        jButton4.setIcon(new javax.swing.ImageIcon(getClass().getResource("/resources/plus.png"))); // NOI18N
        jButton4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton4ActionPerformed(evt);
            }
        });

        jLabel6.setFont(new java.awt.Font("Times New Roman", 0, 14)); // NOI18N
        jLabel6.setText("Postal Code :");

        jLabel7.setFont(new java.awt.Font("Times New Roman", 0, 14)); // NOI18N
        jLabel7.setText("District :");

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 621, Short.MAX_VALUE))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(16, 16, 16)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addComponent(jLabel2)
                                .addGap(0, 0, Short.MAX_VALUE))
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addComponent(jLabel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(jLabel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(jLabel6, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                                .addGap(18, 18, 18)
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addComponent(jTextField1)
                                    .addComponent(jTextField2, javax.swing.GroupLayout.DEFAULT_SIZE, 157, Short.MAX_VALUE)
                                    .addComponent(jTextField4))
                                .addGap(56, 56, 56)
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(jPanel1Layout.createSequentialGroup()
                                        .addGap(0, 0, Short.MAX_VALUE)
                                        .addComponent(jButton1)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                        .addComponent(jButton2)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                        .addComponent(jButton3, javax.swing.GroupLayout.PREFERRED_SIZE, 21, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addGroup(jPanel1Layout.createSequentialGroup()
                                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                            .addComponent(jLabel7)
                                            .addComponent(jLabel5))
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                            .addComponent(jComboBox1, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                                                .addComponent(jTextField3)
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                .addComponent(jButton4))
                                            .addComponent(jComboBox2, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))))))
                .addContainerGap())
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 277, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(14, 14, 14)
                .addComponent(jLabel2)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jLabel1)
                .addGap(12, 12, 12)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel3)
                    .addComponent(jTextField1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel5)
                    .addComponent(jComboBox1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabel4)
                        .addComponent(jTextField2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jTextField3)
                    .addComponent(jButton4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(18, 18, 18)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel6)
                    .addComponent(jTextField4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel7)
                    .addComponent(jComboBox2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jButton1)
                        .addComponent(jButton2))
                    .addComponent(jButton3, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 251, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void jTable1MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jTable1MouseClicked
        int row = jTable1.getSelectedRow();

        jButton2.setEnabled(true);
        jButton1.setEnabled(false);

        jTextField1.setText(String.valueOf(jTable1.getValueAt(row, 0)));
        jTextField2.setText(String.valueOf(jTable1.getValueAt(row, 1)));
        jTextField4.setText(String.valueOf(jTable1.getValueAt(row, 2)));
        jComboBox1.setSelectedItem(String.valueOf(jTable1.getValueAt(row, 3)));
        jComboBox2.setSelectedItem(String.valueOf(jTable1.getValueAt(row, 4)));
    }//GEN-LAST:event_jTable1MouseClicked

    private void jButton4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton4ActionPerformed

        if (Dashboard.getEmployeeTypeName().equals("Admin") || Dashboard.getEmployeeTypeName().equals("HR Manager")) {

            String city = jTextField3.getText();
            String district = String.valueOf(jComboBox2.getSelectedItem());

            if (city.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Please Select or Enter city", "Warning", JOptionPane.WARNING_MESSAGE);
            } else if (district.equals("Select")) {
                JOptionPane.showMessageDialog(this, "Please Select District", "Warning", JOptionPane.WARNING_MESSAGE);
            } else {
                try {

                    ResultSet resultSet = MySQL.executeSearch("SELECT * FROM `city` INNER JOIN `district` ON "
                            + "`city`.`district_district_id`=`district`.`district_id` WHERE `city_name`='" + city + "' AND "
                            + "`district_district_id`='" + districtMap.get(district) + "'");

                    if (resultSet.next()) {
                        JOptionPane.showMessageDialog(this, "This City already added", "Warning", JOptionPane.WARNING_MESSAGE);
                        jTextField3.setText("");
                    } else {

                        if (jComboBox1.getSelectedIndex() == 0) {
                            //Select
                            MySQL.executeIUD("INSERT INTO `city`(`city_name`,`district_district_id`) VALUES('" + city + "','" + districtMap.get(district) + "')");
                            JOptionPane.showMessageDialog(this, "City added successfully", "Success", JOptionPane.INFORMATION_MESSAGE);
                        } else {

                            int showConfirm = JOptionPane.showConfirmDialog(this, "Do you want to update City?", "Update", JOptionPane.YES_NO_OPTION, JOptionPane.INFORMATION_MESSAGE);

                            if (showConfirm == JOptionPane.YES_OPTION) {
                                MySQL.executeIUD("UPDATE `city` SET `city_name`='" + city + "',`district_district_id`='" + districtMap.get(district) + "'"
                                        + " WHERE `city_name`='" + String.valueOf(jComboBox1.getSelectedItem()) + "'");
                                JOptionPane.showMessageDialog(this, "City updated successfully", "Success", JOptionPane.INFORMATION_MESSAGE);
                            }
                        }

                        loadCity();
                        jTextField3.setText("");

                    }

                } catch (Exception e) {
                    SignIn.getLoggerObject().log(Level.WARNING, "Employee City Add method Exception Found!", e);
                }
            }
        } else {
            JOptionPane.showMessageDialog(this, "Access blocked for role: " + Dashboard.getEmployeeTypeName(), "Access Denied", JOptionPane.WARNING_MESSAGE, JOptionIcon.WARNING.setICon());
        }
    }//GEN-LAST:event_jButton4ActionPerformed

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed

        if (Dashboard.getEmployeeTypeName().equals("Admin") || Dashboard.getEmployeeTypeName().equals("HR Manager")) {

            try {

                String line1 = jTextField1.getText();
                String line2 = jTextField2.getText();
                String postalCode = jTextField4.getText();
                String city = String.valueOf(jComboBox1.getSelectedItem());
                String district = String.valueOf(jComboBox2.getSelectedItem());

                if (line1.isEmpty()) {
                    JOptionPane.showMessageDialog(this, "Please Enater Address Line 1", "Warning", JOptionPane.WARNING_MESSAGE);
                } else if (line2.isEmpty()) {
                    JOptionPane.showMessageDialog(this, "Please Enater Address Line 2", "Warning", JOptionPane.WARNING_MESSAGE);
                } else if (postalCode.isEmpty()) {
                    JOptionPane.showMessageDialog(this, "Please Enater Postal Code", "Warning", JOptionPane.WARNING_MESSAGE);
                } else if (city.equals("Select")) {
                    JOptionPane.showMessageDialog(this, "Please Select a City", "Warning", JOptionPane.WARNING_MESSAGE);
                } else if (district.equals("Select")) {
                    JOptionPane.showMessageDialog(this, "Please Select a District", "Warning", JOptionPane.WARNING_MESSAGE);
                } else {

                    boolean canInsert = false;

                    for (int i = 0; i < jTable1.getRowCount(); i++) {

                        String getLine1 = String.valueOf(jTable1.getValueAt(i, 0));
                        String getLine2 = String.valueOf(jTable1.getValueAt(i, 1));
                        String getPostalCode = String.valueOf(jTable1.getValueAt(i, 2));
                        String getCity = String.valueOf(jTable1.getValueAt(i, 3));
                        String getDistrict = String.valueOf(jTable1.getValueAt(i, 4));

                        if (getLine1.equals(line1) && getLine2.equals(line2) && getPostalCode.equals(postalCode) && getCity.equals(city) && getDistrict.equals(district)) {
                            JOptionPane.showMessageDialog(this, "This Address already added", "Warning", JOptionPane.WARNING_MESSAGE);
                            canInsert = true;
                            break;
                        }

                    }

                    if (!canInsert) {

                        MySQL.executeIUD("INSERT INTO `employee_address`(`line1`,`line2`,`postal_code`,`employee_NIC`,`city_city_id`) "
                                + "VALUES('" + line1 + "','" + line2 + "','" + postalCode + "','" + this.NIC + "','" + cityMap.get(city) + "')");
                        loadAddress();
                        reset();

                        JOptionPane.showMessageDialog(this, "Address Added Successfully", "Information", JOptionPane.INFORMATION_MESSAGE);

                    }

                }

            } catch (Exception e) {
                SignIn.getLoggerObject().log(Level.WARNING, "Employee Address Add method Exception Found!", e);
            }
        } else {
            JOptionPane.showMessageDialog(this, "Access blocked for role: " + Dashboard.getEmployeeTypeName(), "Access Denied", JOptionPane.WARNING_MESSAGE, JOptionIcon.WARNING.setICon());
        }
    }//GEN-LAST:event_jButton1ActionPerformed

    private void jComboBox1ItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_jComboBox1ItemStateChanged

        try {

            String city = String.valueOf(jComboBox1.getSelectedItem());

            ResultSet resultSet = MySQL.executeSearch("SELECT * FROM `city` INNER JOIN `district` ON `city`.`district_district_id`=`district`.`district_id` "
                    + "WHERE `city_name`='" + city + "'");

            if (resultSet.next()) {
                jComboBox2.setSelectedItem(resultSet.getString("district.district_name"));
            }

        } catch (Exception e) {
           SignIn.getLoggerObject().log(Level.WARNING, "Load Employee City method Exception Found!", e);
        }

    }//GEN-LAST:event_jComboBox1ItemStateChanged

    private void jButton3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton3ActionPerformed
        reset();
    }//GEN-LAST:event_jButton3ActionPerformed

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed

        if (Dashboard.getEmployeeTypeName().equals("Admin") || Dashboard.getEmployeeTypeName().equals("HR Manager")) {

            int row = jTable1.getSelectedRow();

            if (row == -1) {
                JOptionPane.showMessageDialog(this, "Please Select a Address to Update", "Warning", JOptionPane.WARNING_MESSAGE);
            } else {

                String id = String.valueOf(jTable1.getValueAt(row, 0));

                try {

                    String line1 = jTextField1.getText();
                    String line2 = jTextField2.getText();
                    String postalCode = jTextField4.getText();
                    String city = String.valueOf(jComboBox1.getSelectedItem());
                    String district = String.valueOf(jComboBox2.getSelectedItem());

                    if (line1.isEmpty()) {
                        JOptionPane.showMessageDialog(this, "Please Enater Address Line 1", "Warning", JOptionPane.WARNING_MESSAGE);
                    } else if (line2.isEmpty()) {
                        JOptionPane.showMessageDialog(this, "Please Enater Address Line 2", "Warning", JOptionPane.WARNING_MESSAGE);
                    } else if (postalCode.isEmpty()) {
                        JOptionPane.showMessageDialog(this, "Please Enater Postal Code", "Warning", JOptionPane.WARNING_MESSAGE);
                    } else if (city.equals("Select")) {
                        JOptionPane.showMessageDialog(this, "Please Select a City", "Warning", JOptionPane.WARNING_MESSAGE);
                    } else if (district.equals("Select")) {
                        JOptionPane.showMessageDialog(this, "Please Select a District", "Warning", JOptionPane.WARNING_MESSAGE);
                    } else {

                        boolean canInsert = false;

                        for (int i = 0; i < jTable1.getRowCount(); i++) {

                            String getLine1 = String.valueOf(jTable1.getValueAt(i, 0));
                            String getLine2 = String.valueOf(jTable1.getValueAt(i, 1));
                            String getPostalCode = String.valueOf(jTable1.getValueAt(i, 2));
                            String getCity = String.valueOf(jTable1.getValueAt(i, 3));
                            String getDistrict = String.valueOf(jTable1.getValueAt(i, 4));

                            if (getLine1.equals(line1) && getLine2.equals(line2) && getPostalCode.equals(postalCode) && getCity.equals(city) && getDistrict.equals(district)) {
                                JOptionPane.showMessageDialog(this, "This Address already added", "Warning", JOptionPane.WARNING_MESSAGE);
                                canInsert = true;
                                break;
                            }

                        }

                        if (!canInsert) {

                            MySQL.executeIUD("UPDATE `employee_address` SET `line1`='" + line1 + "',`line2`='" + line2 + "',`postal_code`='" + postalCode + "',"
                                    + "`employee_NIC`='" + this.NIC + "',`city_city_id`= '" + cityMap.get(city) + "'");
                            loadAddress();
                            reset();

                            JOptionPane.showMessageDialog(this, "Address Added Successfully", "Information", JOptionPane.INFORMATION_MESSAGE);

                        }

                    }

                } catch (Exception e) {
                    SignIn.getLoggerObject().log(Level.WARNING, "Employee Address Update method Exception Found!", e);
                }
            }
        } else {
            JOptionPane.showMessageDialog(this, "Access blocked for role: " + Dashboard.getEmployeeTypeName(), "Access Denied", JOptionPane.WARNING_MESSAGE, JOptionIcon.WARNING.setICon());
        }

    }//GEN-LAST:event_jButton2ActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jButton3;
    private javax.swing.JButton jButton4;
    private javax.swing.JComboBox<String> jComboBox1;
    private javax.swing.JComboBox<String> jComboBox2;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTable jTable1;
    private javax.swing.JTextField jTextField1;
    private javax.swing.JTextField jTextField2;
    private javax.swing.JTextField jTextField3;
    private javax.swing.JTextField jTextField4;
    // End of variables declaration//GEN-END:variables

    private void reset() {
        jTextField1.setText("");
        jTextField1.grabFocus();
        jTextField2.setText("");
        jTextField3.setText("");
        jTextField4.setText("");
        jComboBox1.setSelectedIndex(0);
        jComboBox2.setSelectedIndex(0);
        jButton1.setEnabled(true);
        jButton2.setEnabled(false);
    }
}
