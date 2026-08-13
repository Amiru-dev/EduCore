package lk.corevision.fruitshop.gui;

import java.awt.event.MouseEvent;
import java.io.File;
import static java.lang.System.currentTimeMillis;
import java.sql.ResultSet;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Pattern;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;
import lk.corevision.fruitshop.model.MySQL;
import lk.corevision.fruitshop.model.SendLogo;
import lk.corevision.fruitshop.validate.DataValidator;
import lk.corevision.fruitshop.validate.JOptionIcon;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.data.JRTableModelDataSource;
import net.sf.jasperreports.view.JasperViewer;

/**
 *
 * @author CoreVision Software Team
 * @version 1.0.0
 */
public class ProductPacking extends javax.swing.JPanel {

    private Dashboard dashboard;
    private HashMap<String, Integer> packingStatus = new HashMap();
    private HashMap<String, Integer> packingType = new HashMap();
    private HashMap<String, Integer> sectionMap = new HashMap();
    private SalesAndOrders salesAndOrders;
    private ProductPackingDialog productPackingDialog;

    public JTextField getjTextField10() {
        return jTextField10;
    }

    public void setSalesAndOrders(SalesAndOrders salesAndOrders) {
        this.salesAndOrders = salesAndOrders;
    }

    public void setProductPackingDialog(ProductPackingDialog productPackingDialog) {
        this.productPackingDialog = productPackingDialog;
    }

    public ProductPacking() {
        initComponents();
        loadPacking();
        packingType();
        packingStatus();
        loadSection();
        loadPackingId();
        //  packingAdvanceSearch();
        jTextField1.setEditable(false);
        jTextField8.setEditable(false);
        jTextField8.setText(SignIn.getEmployeeNIC());

    }

    private void packingAdvanceSearch() {
        String q = "SELECT * FROM `packing` INNER JOIN `employee` ON `employee`.`NIC`=`packing`.`employee_NIC` "
                + " INNER JOIN `product_quality` ON `packing`.`product_quality_id`=`product_quality`.`product_quality_id` "
                + " INNER JOIN `storage_section` ON `storage_section`.`storage_section_id`=`packing`.`storage_section_id` "
                + "INNER JOIN `packing_type` ON `packing`.`packing_type_id`=`packing_type`.`packing_type_id` "
                + "INNER JOIN `packing_status` ON `packing_status`.`packing_status`=`packing`.`packing_status` "
                + "INNER JOIN `inventory`  ON `product_quality`.`inventory_inventory_id`=`inventory`.`inventory_id` "
                + "INNER JOIN `plantation` ON `plantation`.`plantation_id`=`inventory`.`plantation_id` "
                + "INNER JOIN `fruit` ON `plantation`.`fruit_fruit_id`=`fruit`.`fruit_id` WHERE `packing`.`packing_id` LIKE '%" + jTextField2.getText() + "%'";

        if (!jTextField7.getText().isEmpty()) {
            q += " AND `packing`.`qty`='" + jTextField7.getText() + "'";
        }

        String pack_status = String.valueOf(jComboBox2.getSelectedItem());

        if (pack_status != "SELECT") {
            q += " AND `packing_status`.`packing_status_type`='" + pack_status + "'";
        }

        if (!jTextField8.getText().isEmpty()) {
            q += " AND `employee`.`NIC`='" + jTextField8.getText() + "'";
        }

        if (!jTextField10.getText().isEmpty()) {
            q += " AND `product_quality`.`product_quality_id`='" + jTextField10.getText() + "'";
        }

        String packing_type = String.valueOf(jComboBox3.getSelectedItem());

        if (packing_type != "SELECT") {
            q += " AND `packing_type`.`packing_type`='" + packing_type + "'";
        }

        if (!jTextField1.getText().isEmpty()) {
            q += " AND `fruit`.`fruit_id`='" + jTextField1.getText() + "'";
        }

        if (!jFormattedTextField3.getText().isEmpty()) {
            q += " AND `packing`.`price`='" + jFormattedTextField3.getText() + "'";
        }

        String storage_section = String.valueOf(jComboBox4.getSelectedItem());

        if (storage_section != "SELECT") {
            q += " AND `storage_section`.`storage_section`='" + storage_section + "'";
        }

        DefaultTableModel dtm = (DefaultTableModel) jTable3.getModel();
        dtm.setRowCount(0);
        try {

            ResultSet rs = MySQL.executeSearch(q);
            while (rs.next()) {
                Vector vector = new Vector();
                vector.add(rs.getString("packing.packing_id"));
                vector.add(rs.getString("fruit.fruit_id"));
                vector.add(rs.getString("fruit.fruit_name"));
                vector.add(rs.getString("packing.qty"));
                vector.add(rs.getString("packing.date"));
                vector.add(rs.getString("packing_status.packing_status_type"));
                vector.add(rs.getString("packing.price"));
                vector.add(rs.getString("packing.employee_NIC"));
                vector.add(rs.getString("product_quality.product_quality_id"));
                vector.add(rs.getString("storage_section.storage_section"));
                vector.add(rs.getString("packing_type.packing_type"));

                dtm.addRow(vector);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

//    public void setDashboard(Dashboard dashboard1){
//        System.out.println(dashboard1);
//          this.dashboard = dashboard1;
//          try{
//             ResultSet rs = MySQL.executeSearch("SELECT * FROM `employee` WHERE `email`='"+dashboard.employeeType().getText()+"'");
//             if(rs.next()){
//                     jTextField8.setText(rs.getString("NIC"));
//             }
//          }catch(Exception e){
//             e.printStackTrace();
//          }
//        
//    }
    private void loadPackingId() {
        jTextField6.setEditable(false);
        long id = currentTimeMillis();
        jTextField6.setText(String.valueOf(id));
        //   jTextField8.setEditable(false);
    }

    private void packingType() {
        try {
            ResultSet rs = MySQL.executeSearch("SELECT * FROM `packing_type`");
            Vector vector = new Vector();
            vector.add("SELECT");
            while (rs.next()) {
                vector.add(rs.getString("packing_type"));
                packingType.put(rs.getString("packing_type"), rs.getInt("packing_type_id"));
            }

            DefaultComboBoxModel model = new DefaultComboBoxModel(vector);
            jComboBox3.setModel(model);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void packingStatus() {
        try {
            ResultSet rs = MySQL.executeSearch("SELECT * FROM `packing_status`");
            Vector vector = new Vector();
            vector.add("SELECT");
            while (rs.next()) {
                vector.add(rs.getString("packing_status_type"));
                packingStatus.put(rs.getString("packing_status_type"), rs.getInt("packing_status"));
            }

            DefaultComboBoxModel model = new DefaultComboBoxModel(vector);
            jComboBox2.setModel(model);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void loadSection() {
        try {
            ResultSet rs = MySQL.executeSearch("SELECT * FROM `storage_section`");
            Vector vector = new Vector();
            vector.add("SELECT");
            while (rs.next()) {
                vector.add(rs.getString("storage_section"));
                sectionMap.put(rs.getString("storage_section"), rs.getInt("storage_section_id"));
            }

            DefaultComboBoxModel model = new DefaultComboBoxModel(vector);
            jComboBox4.setModel(model);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void loadPacking() {
        DefaultTableModel dtm = (DefaultTableModel) jTable3.getModel();
        dtm.setRowCount(0);
        try {
            ResultSet rs = MySQL.executeSearch("SELECT * FROM `packing` INNER JOIN `employee` ON `employee`.`NIC`=`packing`.`employee_NIC` "
                    + "INNER JOIN `product_quality` ON `packing`.`product_quality_id`=`product_quality`.`product_quality_id` "
                    + "INNER JOIN `storage_section` ON `storage_section`.`storage_section_id`=`packing`.`storage_section_id` "
                    + "INNER JOIN `packing_type` ON `packing`.`packing_type_id`=`packing_type`.`packing_type_id` "
                    + "INNER JOIN `packing_status` ON `packing_status`.`packing_status`=`packing`.`packing_status` "
                    + "INNER JOIN `inventory`  ON `product_quality`.`inventory_inventory_id`=`inventory`.`inventory_id` "
                    + "INNER JOIN `plantation` ON `plantation`.`plantation_id`=`inventory`.`plantation_id` "
                    + "INNER JOIN `fruit` ON `plantation`.`fruit_fruit_id`=`fruit`.`fruit_id`");

            while (rs.next()) {
                Vector vector = new Vector();
                vector.add(rs.getString("packing.packing_id"));
                vector.add(rs.getString("fruit.fruit_id"));
                vector.add(rs.getString("fruit.fruit_name"));
                vector.add(rs.getString("packing.qty"));
                vector.add(rs.getString("packing.date"));
                vector.add(rs.getString("packing_status.packing_status_type"));
                vector.add(rs.getString("packing.price"));
                vector.add(rs.getString("packing.employee_NIC"));
                vector.add(rs.getString("product_quality.product_quality_id"));
                vector.add(rs.getString("storage_section.storage_section"));
                vector.add(rs.getString("packing_type.packing_type"));

                dtm.addRow(vector);
            }
        } catch (Exception e) {
            e.printStackTrace();
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

        jPopupMenu1 = new javax.swing.JPopupMenu();
        jMenuItem1 = new javax.swing.JMenuItem();
        jPanel3 = new javax.swing.JPanel();
        jPanel2 = new javax.swing.JPanel();
        jLabel26 = new javax.swing.JLabel();
        jButton1 = new javax.swing.JButton();
        jTextField1 = new javax.swing.JTextField();
        jLabel1 = new javax.swing.JLabel();
        jTextField2 = new javax.swing.JTextField();
        jLabel2 = new javax.swing.JLabel();
        jPanel7 = new javax.swing.JPanel();
        jPanel8 = new javax.swing.JPanel();
        jPanel10 = new javax.swing.JPanel();
        jLabel14 = new javax.swing.JLabel();
        jTextField6 = new javax.swing.JTextField();
        jPanel11 = new javax.swing.JPanel();
        jLabel15 = new javax.swing.JLabel();
        jTextField7 = new javax.swing.JTextField();
        jPanel12 = new javax.swing.JPanel();
        jLabel16 = new javax.swing.JLabel();
        jComboBox2 = new javax.swing.JComboBox<>();
        jPanel13 = new javax.swing.JPanel();
        jLabel17 = new javax.swing.JLabel();
        jFormattedTextField3 = new javax.swing.JFormattedTextField();
        jPanel14 = new javax.swing.JPanel();
        jLabel18 = new javax.swing.JLabel();
        jTextField8 = new javax.swing.JTextField();
        jPanel15 = new javax.swing.JPanel();
        jLabel19 = new javax.swing.JLabel();
        jTextField10 = new javax.swing.JTextField();
        jPanel16 = new javax.swing.JPanel();
        jLabel20 = new javax.swing.JLabel();
        jComboBox3 = new javax.swing.JComboBox<>();
        jPanel17 = new javax.swing.JPanel();
        jLabel21 = new javax.swing.JLabel();
        jComboBox4 = new javax.swing.JComboBox<>();
        jPanel18 = new javax.swing.JPanel();
        jPanel20 = new javax.swing.JPanel();
        jButton7 = new javax.swing.JButton();
        jPanel23 = new javax.swing.JPanel();
        jButton2 = new javax.swing.JButton();
        jPanel24 = new javax.swing.JPanel();
        jButton14 = new javax.swing.JButton();
        jPanel9 = new javax.swing.JPanel();
        jScrollPane4 = new javax.swing.JScrollPane();
        jTable3 = new javax.swing.JTable();

        jMenuItem1.setText("Update");
        jMenuItem1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem1ActionPerformed(evt);
            }
        });
        jPopupMenu1.add(jMenuItem1);

        setPreferredSize(new java.awt.Dimension(810, 621));
        setLayout(new java.awt.BorderLayout());

        jPanel3.setPreferredSize(new java.awt.Dimension(816, 534));
        jPanel3.setLayout(new java.awt.BorderLayout());

        jPanel2.setPreferredSize(new java.awt.Dimension(816, 50));

        jLabel26.setFont(new java.awt.Font("Serif", 1, 18)); // NOI18N
        jLabel26.setText("Packing Quality");

        jButton1.setText("Reset");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

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

        jLabel2.setText("Packing ID");

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addComponent(jLabel26)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 118, Short.MAX_VALUE)
                .addComponent(jLabel2)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGap(177, 177, 177)
                        .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 45, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGap(15, 15, 15)
                        .addComponent(jTextField2, javax.swing.GroupLayout.PREFERRED_SIZE, 193, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jTextField1, javax.swing.GroupLayout.PREFERRED_SIZE, 155, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 109, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel26)
                    .addComponent(jButton1)
                    .addComponent(jTextField1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel1)
                    .addComponent(jTextField2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel2))
                .addContainerGap(18, Short.MAX_VALUE))
        );

        jPanel3.add(jPanel2, java.awt.BorderLayout.PAGE_START);

        jPanel7.setPreferredSize(new java.awt.Dimension(816, 440));
        jPanel7.setLayout(new java.awt.BorderLayout());

        jPanel8.setPreferredSize(new java.awt.Dimension(816, 250));
        jPanel8.setLayout(new java.awt.GridLayout(3, 3, 10, 10));

        jLabel14.setFont(new java.awt.Font("Serif", 0, 14)); // NOI18N
        jLabel14.setText("Packing ID");

        javax.swing.GroupLayout jPanel10Layout = new javax.swing.GroupLayout(jPanel10);
        jPanel10.setLayout(jPanel10Layout);
        jPanel10Layout.setHorizontalGroup(
            jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel10Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel14)
                    .addComponent(jTextField6, javax.swing.GroupLayout.PREFERRED_SIZE, 202, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(62, Short.MAX_VALUE))
        );
        jPanel10Layout.setVerticalGroup(
            jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel10Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel14)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jTextField6)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jPanel8.add(jPanel10);

        jLabel15.setFont(new java.awt.Font("Serif", 0, 14)); // NOI18N
        jLabel15.setText("Packing Qty");

        javax.swing.GroupLayout jPanel11Layout = new javax.swing.GroupLayout(jPanel11);
        jPanel11.setLayout(jPanel11Layout);
        jPanel11Layout.setHorizontalGroup(
            jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel11Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel15, javax.swing.GroupLayout.PREFERRED_SIZE, 74, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jTextField7, javax.swing.GroupLayout.DEFAULT_SIZE, 147, Short.MAX_VALUE))
                .addContainerGap(117, Short.MAX_VALUE))
        );
        jPanel11Layout.setVerticalGroup(
            jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel11Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel15)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jTextField7, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jPanel8.add(jPanel11);

        jLabel16.setFont(new java.awt.Font("Serif", 0, 14)); // NOI18N
        jLabel16.setText("Packing Status");

        jComboBox2.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));

        javax.swing.GroupLayout jPanel12Layout = new javax.swing.GroupLayout(jPanel12);
        jPanel12.setLayout(jPanel12Layout);
        jPanel12Layout.setHorizontalGroup(
            jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel12Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel16)
                    .addComponent(jComboBox2, 0, 206, Short.MAX_VALUE))
                .addContainerGap(58, Short.MAX_VALUE))
        );
        jPanel12Layout.setVerticalGroup(
            jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel12Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel16)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jComboBox2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jPanel8.add(jPanel12);

        jLabel17.setFont(new java.awt.Font("Serif", 0, 14)); // NOI18N
        jLabel17.setText("Price");

        javax.swing.GroupLayout jPanel13Layout = new javax.swing.GroupLayout(jPanel13);
        jPanel13.setLayout(jPanel13Layout);
        jPanel13Layout.setHorizontalGroup(
            jPanel13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel13Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel17)
                    .addComponent(jFormattedTextField3, javax.swing.GroupLayout.PREFERRED_SIZE, 200, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(64, Short.MAX_VALUE))
        );
        jPanel13Layout.setVerticalGroup(
            jPanel13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel13Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel17)
                .addGap(5, 5, 5)
                .addComponent(jFormattedTextField3)
                .addContainerGap())
        );

        jPanel8.add(jPanel13);

        jLabel18.setFont(new java.awt.Font("Serif", 0, 14)); // NOI18N
        jLabel18.setText("Employee NIC");

        jTextField8.setEditable(false);

        javax.swing.GroupLayout jPanel14Layout = new javax.swing.GroupLayout(jPanel14);
        jPanel14.setLayout(jPanel14Layout);
        jPanel14Layout.setHorizontalGroup(
            jPanel14Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel14Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel14Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jTextField8, javax.swing.GroupLayout.PREFERRED_SIZE, 199, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel18, javax.swing.GroupLayout.PREFERRED_SIZE, 105, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(65, Short.MAX_VALUE))
        );
        jPanel14Layout.setVerticalGroup(
            jPanel14Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel14Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel18)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jTextField8, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        jPanel8.add(jPanel14);

        jLabel19.setFont(new java.awt.Font("Serif", 0, 14)); // NOI18N
        jLabel19.setText("Product Quality ID");

        jTextField10.setEditable(false);

        javax.swing.GroupLayout jPanel15Layout = new javax.swing.GroupLayout(jPanel15);
        jPanel15.setLayout(jPanel15Layout);
        jPanel15Layout.setHorizontalGroup(
            jPanel15Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel15Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel15Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jTextField10, javax.swing.GroupLayout.PREFERRED_SIZE, 210, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel19, javax.swing.GroupLayout.PREFERRED_SIZE, 138, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(54, Short.MAX_VALUE))
        );
        jPanel15Layout.setVerticalGroup(
            jPanel15Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel15Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel19)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jTextField10, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        jPanel8.add(jPanel15);

        jLabel20.setFont(new java.awt.Font("Serif", 0, 14)); // NOI18N
        jLabel20.setText("Packing Type");

        jComboBox3.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));

        javax.swing.GroupLayout jPanel16Layout = new javax.swing.GroupLayout(jPanel16);
        jPanel16.setLayout(jPanel16Layout);
        jPanel16Layout.setHorizontalGroup(
            jPanel16Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel16Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel16Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel20)
                    .addComponent(jComboBox3, 0, 184, Short.MAX_VALUE))
                .addContainerGap(80, Short.MAX_VALUE))
        );
        jPanel16Layout.setVerticalGroup(
            jPanel16Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel16Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jLabel20)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jComboBox3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(12, 12, 12))
        );

        jPanel8.add(jPanel16);

        jLabel21.setFont(new java.awt.Font("Serif", 0, 14)); // NOI18N
        jLabel21.setText("Storage Section");

        jComboBox4.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));

        javax.swing.GroupLayout jPanel17Layout = new javax.swing.GroupLayout(jPanel17);
        jPanel17.setLayout(jPanel17Layout);
        jPanel17Layout.setHorizontalGroup(
            jPanel17Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel17Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel17Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel21)
                    .addComponent(jComboBox4, 0, 200, Short.MAX_VALUE))
                .addContainerGap(64, Short.MAX_VALUE))
        );
        jPanel17Layout.setVerticalGroup(
            jPanel17Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel17Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel21)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jComboBox4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jPanel8.add(jPanel17);

        jPanel18.setLayout(new java.awt.GridLayout(1, 3));

        jButton7.setFont(new java.awt.Font("Serif", 0, 14)); // NOI18N
        jButton7.setText("Add");
        jButton7.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton7ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel20Layout = new javax.swing.GroupLayout(jPanel20);
        jPanel20.setLayout(jPanel20Layout);
        jPanel20Layout.setHorizontalGroup(
            jPanel20Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel20Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jButton7, javax.swing.GroupLayout.DEFAULT_SIZE, 84, Short.MAX_VALUE))
        );
        jPanel20Layout.setVerticalGroup(
            jPanel20Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel20Layout.createSequentialGroup()
                .addContainerGap(26, Short.MAX_VALUE)
                .addComponent(jButton7)
                .addContainerGap())
        );

        jPanel18.add(jPanel20);

        jButton2.setText("Update");
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel23Layout = new javax.swing.GroupLayout(jPanel23);
        jPanel23.setLayout(jPanel23Layout);
        jPanel23Layout.setHorizontalGroup(
            jPanel23Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel23Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jButton2, javax.swing.GroupLayout.DEFAULT_SIZE, 78, Short.MAX_VALUE)
                .addContainerGap())
        );
        jPanel23Layout.setVerticalGroup(
            jPanel23Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel23Layout.createSequentialGroup()
                .addContainerGap(29, Short.MAX_VALUE)
                .addComponent(jButton2)
                .addContainerGap())
        );

        jPanel18.add(jPanel23);

        jButton14.setFont(new java.awt.Font("Serif", 0, 14)); // NOI18N
        jButton14.setText("Report");
        jButton14.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton14ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel24Layout = new javax.swing.GroupLayout(jPanel24);
        jPanel24.setLayout(jPanel24Layout);
        jPanel24Layout.setHorizontalGroup(
            jPanel24Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jButton14, javax.swing.GroupLayout.DEFAULT_SIZE, 90, Short.MAX_VALUE)
        );
        jPanel24Layout.setVerticalGroup(
            jPanel24Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel24Layout.createSequentialGroup()
                .addContainerGap(26, Short.MAX_VALUE)
                .addComponent(jButton14)
                .addContainerGap())
        );

        jPanel18.add(jPanel24);

        jPanel8.add(jPanel18);

        jPanel7.add(jPanel8, java.awt.BorderLayout.PAGE_START);

        jPanel9.setPreferredSize(new java.awt.Dimension(816, 260));
        jPanel9.setLayout(new java.awt.BorderLayout());

        jTable3.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null}
            },
            new String [] {
                "Packing ID", "Fruit ID", "Fruit Name", "Packing Qty", "Date", "Packing status", "Price", "Employee NIC", "Product Quality", "Storage section", "Packing Type"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false, false, false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jTable3.addAncestorListener(new javax.swing.event.AncestorListener() {
            public void ancestorAdded(javax.swing.event.AncestorEvent evt) {
                jTable3AncestorAdded(evt);
            }
            public void ancestorMoved(javax.swing.event.AncestorEvent evt) {
            }
            public void ancestorRemoved(javax.swing.event.AncestorEvent evt) {
            }
        });
        jTable3.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jTable3MouseClicked(evt);
            }
        });
        jScrollPane4.setViewportView(jTable3);

        jPanel9.add(jScrollPane4, java.awt.BorderLayout.CENTER);

        jPanel7.add(jPanel9, java.awt.BorderLayout.CENTER);

        jPanel3.add(jPanel7, java.awt.BorderLayout.CENTER);

        add(jPanel3, java.awt.BorderLayout.CENTER);
    }// </editor-fold>//GEN-END:initComponents

    private void jButton7ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton7ActionPerformed
        String packingId = jTextField6.getText();
        String packingQty = jTextField7.getText();
        String pstatus = String.valueOf(jComboBox2.getSelectedItem());
        String price = jFormattedTextField3.getText();
        String nic = jTextField8.getText();
        String pqualityId = jTextField10.getText();
        String pType = String.valueOf(jComboBox3.getSelectedItem());
        String section = String.valueOf(jComboBox4.getSelectedItem());

        Pattern pattern = Pattern.compile("^[1-9][0-9]*$");

        if (packingQty.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Plsease enter  packing Qty", "Warning", JOptionPane.WARNING_MESSAGE, JOptionIcon.WARNING.setICon());
        } else if (!pattern.matcher(packingQty).matches()) {
            JOptionPane.showMessageDialog(this, "Plsease enter valid  packing Qty", "Warning", JOptionPane.WARNING_MESSAGE, JOptionIcon.WARNING.setICon());
        } else if (pstatus == "SELECT") {
            JOptionPane.showMessageDialog(this, "Plsease select  packing status", "Warning", JOptionPane.WARNING_MESSAGE, JOptionIcon.WARNING.setICon());
        } else if (price.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Plsease enter price", "Warning", JOptionPane.WARNING_MESSAGE, JOptionIcon.WARNING.setICon());
        } else if (DataValidator.isInvalidPrice(price)) {
            JOptionPane.showMessageDialog(this, "Plsease enter valid price", "Warning", JOptionPane.WARNING_MESSAGE, JOptionIcon.WARNING.setICon());
        } else if (pqualityId.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Plsease enter product quality id", "Warning", JOptionPane.WARNING_MESSAGE, JOptionIcon.WARNING.setICon());
        } else if (DataValidator.isInvalidNumber(pqualityId)) {
            JOptionPane.showMessageDialog(this, "Plsease enter valid  product quality id", "Warning", JOptionPane.WARNING_MESSAGE, JOptionIcon.WARNING.setICon());
        } else if (pType == "SELECT") {
            JOptionPane.showMessageDialog(this, "Plsease enter product quality type", "Warning", JOptionPane.WARNING_MESSAGE, JOptionIcon.WARNING.setICon());
        } else if (section == "SELECT") {
            JOptionPane.showMessageDialog(this, "Plsease enter product quality section", "Warning", JOptionPane.WARNING_MESSAGE, JOptionIcon.WARNING.setICon());
        } else {

            String date = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss").format(new Date());

            int packingTypeId = packingType.get(pType);
            int packingStatusId = packingStatus.get(pstatus);
            int sectionId = sectionMap.get(section);

            try {

                ResultSet rs = MySQL.executeSearch("SELECT * FROM `employee` WHERE `NIC`='" + nic + "'");
                if (rs.next()) {
                    ResultSet rs1 = MySQL.executeSearch("SELECT * FROM `product_quality` WHERE `product_quality_id`='" + pqualityId + "'");
                    if (rs1.next()) {
                        MySQL.executeIUD("INSERT INTO `packing`(`packing_id`,`qty`,`date`,`packing_status`,`price`,`employee_NIC`,`product_quality_id`,`storage_section_id`,`packing_type_id`) "
                                + "VALUES('" + packingId + "','" + packingQty + "','" + date + "','" + packingStatusId + "','" + price + "','" + nic + "','" + pqualityId + "','" + sectionId + "','" + packingTypeId + "')");

                        // Silently generate the barcode PDF for the number of packed items
                        int numBarcodes = 1;
                        try {
                            numBarcodes = Integer.parseInt(packingQty);
                        } catch (NumberFormatException ignored) {
                        }

                        lk.corevision.fruitshop.model.BarcodeGenerator.generateBarcodePDF(packingId, pType + " Pack", price, numBarcodes);

                        // loadPacking();
                        loadPacking();
                        reset();

                        String homePath = System.getProperty("user.home");

                        String folderPath = homePath
                                + File.separator + "Documents"
                                + File.separator + "FruitShopApp"
                                + File.separator + "Barcodes";

                        JOptionPane.showMessageDialog(this, "Success! Packing record saved and barcodes generated to PDF.\nLocation: " + folderPath, "Success", JOptionPane.INFORMATION_MESSAGE, JOptionIcon.SUCCESS.setICon());

                    } else {
                        jTextField7.setText("");
                        JOptionPane.showMessageDialog(this, "Invalid product Quality ID", "Warning", JOptionPane.WARNING_MESSAGE, JOptionIcon.WARNING.setICon());
                    }

                } else {
                    jTextField8.setText("");
                    JOptionPane.showMessageDialog(this, "Invalid User", "Warning", JOptionPane.WARNING_MESSAGE, JOptionIcon.WARNING.setICon());
                }

            } catch (Exception e) {
                SignIn.getLoggerObject().log(Level.WARNING, "add packing Exception found!", e);
            }
        }
    }//GEN-LAST:event_jButton7ActionPerformed

    private void jButton14ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton14ActionPerformed
        try {

            String nic = SignIn.getEmployeeNIC();

            String date = new SimpleDateFormat("yyyy-MM-dd").format(new Date());

            HashMap<String, Object> parameters = new HashMap();
            parameters.put("Parameter1", date);
            parameters.put("Parameter2", nic);
            parameters.put("Parameter101", SendLogo.getFilePath());

            JRTableModelDataSource dataSource = new JRTableModelDataSource(jTable3.getModel());

            JasperPrint report = JasperFillManager.fillReport("src/lk/corevision/fruitshop/reports/quality/fruitShopAppPackingQuality.jasper", parameters, dataSource);

            JasperViewer.viewReport(report, false);
        } catch (Exception e) {
            //  e.printStackTrace();
            SignIn.getLoggerObject().log(Level.WARNING, "report packing Exception found!", e);
        }
    }//GEN-LAST:event_jButton14ActionPerformed

    private void jTable3MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jTable3MouseClicked
        int row = jTable3.getSelectedRow();
        if (salesAndOrders != null && row != -1) {
            if (evt.getClickCount() == 2) {
                salesAndOrders.setOrderFruitId(String.valueOf(jTable3.getValueAt(row, 1)));
                salesAndOrders.setPackingId(String.valueOf(jTable3.getValueAt(row, 0)));
                productPackingDialog.dispose();
            }
        } else {
            if (row != -1) {
                if (evt.getButton() == MouseEvent.BUTTON3) {
                    jPopupMenu1.show(evt.getComponent(), evt.getX(), evt.getY());
                }

            }
        }
    }//GEN-LAST:event_jTable3MouseClicked

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed

        String packingId = jTextField6.getText();
        String packingQty = jTextField7.getText();
        String pstatus = String.valueOf(jComboBox2.getSelectedItem());
        String price = jFormattedTextField3.getText();
        String nic = jTextField8.getText();
        String pqualityId = jTextField10.getText();
        String pType = String.valueOf(jComboBox3.getSelectedItem());
        String section = String.valueOf(jComboBox4.getSelectedItem());

        Pattern pattern = Pattern.compile("^[1-9][0-9]*$");

        if (packingQty.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Plsease enter  packing Qty", "Warning", JOptionPane.WARNING_MESSAGE, JOptionIcon.WARNING.setICon());
        } else if (!pattern.matcher(packingQty).matches()) {
            JOptionPane.showMessageDialog(this, "Plsease enter valid  packing Qty", "Warning", JOptionPane.WARNING_MESSAGE, JOptionIcon.WARNING.setICon());
        } else if (pstatus == "SELECT") {
            JOptionPane.showMessageDialog(this, "Plsease select  packing status", "Warning", JOptionPane.WARNING_MESSAGE, JOptionIcon.WARNING.setICon());
        } else if (price.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Plsease enter price", "Warning", JOptionPane.WARNING_MESSAGE, JOptionIcon.WARNING.setICon());
        } else if (DataValidator.isInvalidPrice(price)) {
            JOptionPane.showMessageDialog(this, "Plsease enter valid price", "Warning", JOptionPane.WARNING_MESSAGE, JOptionIcon.WARNING.setICon());
        } else if (pqualityId.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Plsease enter product quality id", "Warning", JOptionPane.WARNING_MESSAGE, JOptionIcon.WARNING.setICon());
        } else if (DataValidator.isInvalidNumber(pqualityId)) {
            JOptionPane.showMessageDialog(this, "Plsease enter valid  product quality id", "Warning", JOptionPane.WARNING_MESSAGE, JOptionIcon.WARNING.setICon());
        } else if (pType == "SELECT") {
            JOptionPane.showMessageDialog(this, "Plsease enter product quality type", "Warning", JOptionPane.WARNING_MESSAGE, JOptionIcon.WARNING.setICon());
        } else if (section == "SELECT") {
            JOptionPane.showMessageDialog(this, "Plsease enter product quality section", "Warning", JOptionPane.WARNING_MESSAGE, JOptionIcon.WARNING.setICon());
        } else {

            String date = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss").format(new Date());

            int packingTypeId = packingType.get(pType);
            int packingStatusId = packingStatus.get(pstatus);
            int sectionId = sectionMap.get(section);

            try {

                ResultSet rs = MySQL.executeSearch("SELECT * FROM `product_quality` WHERE `product_quality_id`='" + pqualityId + "'");
                if (rs.next()) {
                    ResultSet rs1 = MySQL.executeSearch("SELECT * FROM `employee` WHERE `NIC`='" + nic + "'");
                    if (rs1.next()) {
                        int row = jTable3.getSelectedRow();
                        String packing_id = String.valueOf(jTable3.getValueAt(row, 0));

                        MySQL.executeIUD("UPDATE `packing` SET `qty`='" + packingQty + "', `packing_status`='" + packingStatusId + "',`price`='" + price + "',`employee_NIC`='" + nic + "',`product_quality_id`='" + pqualityId + "',`packing_type_id`='" + packingTypeId + "',`storage_section_id`='" + sectionId + "'"
                                + " WHERE `packing_id`='" + packing_id + "'");

                        loadPacking();
                        reset();

                        // packingAdvanceSearch();
                        jTable3.setEnabled(true);
                        jButton7.setEnabled(true);

                        JOptionPane.showMessageDialog(this, "Success", "Success", JOptionPane.INFORMATION_MESSAGE, JOptionIcon.SUCCESS.setICon());
                    } else {
                        JOptionPane.showMessageDialog(this, "Invalid User NIC", "Warning", JOptionPane.WARNING_MESSAGE, JOptionIcon.WARNING.setICon());
                    }

                } else {
                    JOptionPane.showMessageDialog(this, "Invalid Product Quality ID", "Warning", JOptionPane.WARNING_MESSAGE, JOptionIcon.WARNING.setICon());
                }

            } catch (Exception e) {
                SignIn.getLoggerObject().log(Level.WARNING, "update packing Exception found!", e);
            }
        }
    }//GEN-LAST:event_jButton2ActionPerformed

    private void jTextField2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextField2ActionPerformed


    }//GEN-LAST:event_jTextField2ActionPerformed

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        reset();
    }//GEN-LAST:event_jButton1ActionPerformed

    private void jTextField2KeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTextField2KeyReleased
        DefaultTableModel dtm = (DefaultTableModel) jTable3.getModel();
        dtm.setRowCount(0);

        try {
            ResultSet rs = MySQL.executeSearch("SELECT * FROM `packing` INNER JOIN `employee` ON `employee`.`NIC`=`packing`.`employee_NIC` "
                    + " INNER JOIN `product_quality` ON `packing`.`product_quality_id`=`product_quality`.`product_quality_id` "
                    + " INNER JOIN `storage_section` ON `storage_section`.`storage_section_id`=`packing`.`storage_section_id` "
                    + "INNER JOIN `packing_type` ON `packing`.`packing_type_id`=`packing_type`.`packing_type_id` "
                    + "INNER JOIN `packing_status` ON `packing_status`.`packing_status`=`packing`.`packing_status` "
                    + "INNER JOIN `inventory`  ON `product_quality`.`inventory_inventory_id`=`inventory`.`inventory_id` "
                    + "INNER JOIN `plantation` ON `plantation`.`plantation_id`=`inventory`.`plantation_id` "
                    + "INNER JOIN `fruit` ON `plantation`.`fruit_fruit_id`=`fruit`.`fruit_id` WHERE `packing`.`packing_id` LIKE '%" + jTextField2.getText() + "%'");

            while (rs.next()) {
                Vector vector = new Vector();
                vector.add(rs.getString("packing.packing_id"));
                vector.add(rs.getString("fruit.fruit_id"));
                vector.add(rs.getString("fruit.fruit_name"));
                vector.add(rs.getString("packing.qty"));
                vector.add(rs.getString("packing_status.packing_status_type"));
                vector.add(rs.getString("packing.price"));
                vector.add(rs.getString("packing.employee_NIC"));
                vector.add(rs.getString("packing.product_quality_id"));
                vector.add(rs.getString("storage_section.storage_section"));
                vector.add(rs.getString("packing_type.packing_type"));

                dtm.addRow(vector);

            }
        } catch (Exception ex) {
            Logger.getLogger(ProductPacking.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_jTextField2KeyReleased

    private void jTable3AncestorAdded(javax.swing.event.AncestorEvent evt) {//GEN-FIRST:event_jTable3AncestorAdded
        // TODO add your handling code here:
    }//GEN-LAST:event_jTable3AncestorAdded

    private void jMenuItem1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem1ActionPerformed
        int row = jTable3.getSelectedRow();
        jTable3.setEnabled(false);
        jButton7.setEnabled(false);

        String qty = String.valueOf(jTable3.getValueAt(row, 3));
        String pack_status = String.valueOf(jTable3.getValueAt(row, 5));
        String price = String.valueOf(jTable3.getValueAt(row, 6));
        String nic = String.valueOf(jTable3.getValueAt(row, 7));
        String pquality_id = String.valueOf(jTable3.getValueAt(row, 8));
        String packing_type = String.valueOf(jTable3.getValueAt(row, 10));
        String storage_section = String.valueOf(jTable3.getValueAt(row, 9));

        jTextField7.setText(qty);
        jComboBox2.setSelectedItem(pack_status);
        jFormattedTextField3.setText(price);
        jTextField8.setText(nic);
        jTextField10.setText(pquality_id);
        jComboBox3.setSelectedItem(packing_type);
        jComboBox4.setSelectedItem(storage_section);
    }//GEN-LAST:event_jMenuItem1ActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton14;
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jButton7;
    private javax.swing.JComboBox<String> jComboBox2;
    private javax.swing.JComboBox<String> jComboBox3;
    private javax.swing.JComboBox<String> jComboBox4;
    private javax.swing.JFormattedTextField jFormattedTextField3;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel15;
    private javax.swing.JLabel jLabel16;
    private javax.swing.JLabel jLabel17;
    private javax.swing.JLabel jLabel18;
    private javax.swing.JLabel jLabel19;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel20;
    private javax.swing.JLabel jLabel21;
    private javax.swing.JLabel jLabel26;
    private javax.swing.JMenuItem jMenuItem1;
    private javax.swing.JPanel jPanel10;
    private javax.swing.JPanel jPanel11;
    private javax.swing.JPanel jPanel12;
    private javax.swing.JPanel jPanel13;
    private javax.swing.JPanel jPanel14;
    private javax.swing.JPanel jPanel15;
    private javax.swing.JPanel jPanel16;
    private javax.swing.JPanel jPanel17;
    private javax.swing.JPanel jPanel18;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel20;
    private javax.swing.JPanel jPanel23;
    private javax.swing.JPanel jPanel24;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel7;
    private javax.swing.JPanel jPanel8;
    private javax.swing.JPanel jPanel9;
    private javax.swing.JPopupMenu jPopupMenu1;
    private javax.swing.JScrollPane jScrollPane4;
    private javax.swing.JTable jTable3;
    private javax.swing.JTextField jTextField1;
    private javax.swing.JTextField jTextField10;
    private javax.swing.JTextField jTextField2;
    private javax.swing.JTextField jTextField6;
    private javax.swing.JTextField jTextField7;
    private javax.swing.JTextField jTextField8;
    // End of variables declaration//GEN-END:variables

    private void reset() {
        jFormattedTextField3.setText("");
        // jTextField6.setText("");
        jTextField7.setText("");
        // jTextField8.setText("");
        jTextField10.setText("");
        jTextField2.setText("");
        jTextField1.setText("");
        jComboBox2.setSelectedItem("SELECT");
        jComboBox3.setSelectedItem("SELECT");
        jComboBox4.setSelectedItem("SELECT");
        jTable3.setEnabled(true);
        //  jTextField8.setEditable(true);
        jButton7.setEnabled(true);
        loadPackingId();
    }

}
