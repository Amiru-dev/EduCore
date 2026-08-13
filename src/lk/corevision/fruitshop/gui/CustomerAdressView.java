package lk.corevision.fruitshop.gui;

import java.awt.Color;
import java.awt.Point;
import java.awt.event.MouseEvent;
import lk.corevision.fruitshop.model.MySQL;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.Vector;
import java.util.logging.Level;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;
import javax.swing.event.MouseInputListener;
import javax.swing.table.DefaultTableModel;
import lk.corevision.fruitshop.resources.transport.mapviewer.RoutingData;
import lk.corevision.fruitshop.resources.transport.mapviewer.RoutingService;
import lk.corevision.fruitshop.resources.transport.waypoint.EventWaypoint;
import lk.corevision.fruitshop.resources.transport.waypoint.MyWaypoint;
import lk.corevision.fruitshop.resources.transport.waypoint.WaypointRender;
import lk.corevision.fruitshop.validate.JOptionIcon;
import org.jxmapviewer.OSMTileFactoryInfo;
import org.jxmapviewer.VirtualEarthTileFactoryInfo;
import org.jxmapviewer.input.PanMouseInputListener;
import org.jxmapviewer.input.ZoomMouseWheelListenerCenter;
import org.jxmapviewer.viewer.DefaultTileFactory;
import org.jxmapviewer.viewer.GeoPosition;
import org.jxmapviewer.viewer.TileFactoryInfo;
import org.jxmapviewer.viewer.WaypointPainter;
import raven.toast.Notifications;

public class CustomerAdressView extends javax.swing.JDialog {

    private final Set<MyWaypoint> waypoints = new HashSet<>();
    private List<RoutingData> routingData = new ArrayList<>();
    private EventWaypoint event;
    private Point mousePosition;
    private double longtitude;
    private double latitude;

    HashMap<String, String> customerCityMap = new HashMap<>();
    private String customerNIC;
    private Dashboard dashboard;
    private Transport transport;

    public CustomerAdressView(java.awt.Frame parent, boolean modal, String nic) {
        super(parent, modal);
        initComponents();
        init();
        this.customerNIC = nic;
        this.dashboard = (Dashboard) parent;
        transport = dashboard.getTransport();
        setCustomerData();
        clearFields();
        loadCustomerAddress();
        loadCity();

    }

    private void init() {
        TileFactoryInfo info = new OSMTileFactoryInfo();
        DefaultTileFactory tileFactory = new DefaultTileFactory(info);
        jXMapViewer.setTileFactory(tileFactory);
        GeoPosition geo = new GeoPosition(7.8731, 80.7718);
        jXMapViewer.setAddressLocation(geo);
        jXMapViewer.setZoom(11);

        //  Create event mouse move
        MouseInputListener mm = new PanMouseInputListener(jXMapViewer);
        jXMapViewer.addMouseListener(mm);
        jXMapViewer.addMouseMotionListener(mm);
        jXMapViewer.addMouseWheelListener(new ZoomMouseWheelListenerCenter(jXMapViewer));
        event = getEvent();
    }

    private void addWaypoint(MyWaypoint waypoint) {
        for (MyWaypoint d : waypoints) {
            jXMapViewer.remove(d.getButton());
        }
        Iterator<MyWaypoint> iter = waypoints.iterator();
        while (iter.hasNext()) {
            if (iter.next().getPointType() == waypoint.getPointType()) {
                iter.remove();
            }
        }
        waypoints.add(waypoint);
        if (waypoints.size() > 1) {
            waypoints.clear();
            waypoints.add(waypoint);
        }

        initWaypoint();

    }

    private void initWaypoint() {
        WaypointPainter<MyWaypoint> wp = new WaypointRender();
        wp.setWaypoints(waypoints);
        jXMapViewer.setOverlayPainter(wp);
        for (MyWaypoint d : waypoints) {
            jXMapViewer.add(d.getButton());
        }

//        //  Routing Data
//        if (waypoints.size() == 2) {
//            GeoPosition start = null;
//            GeoPosition end = null;
//            for (MyWaypoint w : waypoints) {
//                if (w.getPointType() == MyWaypoint.PointType.START) {
//                    start = w.getPosition();
//                } else if (w.getPointType() == MyWaypoint.PointType.END) {
//                    end = w.getPosition();
//                }
//            }
//            if (start != null && end != null) {
//                routingData = RoutingService.getInstance().routing(start.getLatitude(), start.getLongitude(), end.getLatitude(), end.getLongitude());
//
//            } else {
//                routingData.clear();
//            }
//            jXMapViewer.setRoutingData(routingData);
//        }
    }

    private void clearWaypoint() {
        for (MyWaypoint d : waypoints) {
            jXMapViewer.remove(d.getButton());
        }
        this.longtitude = 0;
        this.latitude = 0;
        routingData.clear();
        waypoints.clear();
        initWaypoint();
    }

    private EventWaypoint getEvent() {
        return new EventWaypoint() {
            @Override
            public void selected(MyWaypoint waypoint) {
//                JOptionPane.showMessageDialog(Map.this, waypoint.getName());
            }
        };
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPopupMenu1 = new javax.swing.JPopupMenu();
        jMenuItem1 = new javax.swing.JMenuItem();
        jPopupMenu2 = new javax.swing.JPopupMenu();
        jMenuItem2 = new javax.swing.JMenuItem();
        jPanel1 = new javax.swing.JPanel();
        jPanel3 = new javax.swing.JPanel();
        comboMapType = new javax.swing.JComboBox<>();
        cmdClear = new javax.swing.JButton();
        jPanel4 = new javax.swing.JPanel();
        jXMapViewer = new lk.corevision.fruitshop.resources.transport.mapviewer.JXMapViewerCustom();
        jPanel2 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jTextField1 = new javax.swing.JTextField();
        jTextField2 = new javax.swing.JTextField();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jButton1 = new javax.swing.JButton();
        jButton2 = new javax.swing.JButton();
        jButton3 = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTable2 = new javax.swing.JTable();
        jLabel5 = new javax.swing.JLabel();
        jComboBox1 = new javax.swing.JComboBox<>();

        jMenuItem1.setText("View Location");
        jMenuItem1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem1ActionPerformed(evt);
            }
        });
        jPopupMenu1.add(jMenuItem1);

        jMenuItem2.setText("Set Here");
        jMenuItem2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem2ActionPerformed(evt);
            }
        });
        jPopupMenu2.add(jMenuItem2);

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);

        jPanel1.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        jPanel1.setLayout(new java.awt.BorderLayout());

        jPanel3.setBackground(new java.awt.Color(0, 204, 204));

        comboMapType.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Default", "Map", "Hybrid", "Satellite" }));
        comboMapType.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                comboMapTypeItemStateChanged(evt);
            }
        });

        cmdClear.setText("Clear");
        cmdClear.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmdClearActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 516, Short.MAX_VALUE)
            .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jPanel3Layout.createSequentialGroup()
                    .addContainerGap()
                    .addComponent(cmdClear)
                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 260, Short.MAX_VALUE)
                    .addComponent(comboMapType, javax.swing.GroupLayout.PREFERRED_SIZE, 168, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addContainerGap()))
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 45, Short.MAX_VALUE)
            .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jPanel3Layout.createSequentialGroup()
                    .addGap(9, 9, 9)
                    .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(comboMapType, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(cmdClear))
                    .addContainerGap(9, Short.MAX_VALUE)))
        );

        jPanel1.add(jPanel3, java.awt.BorderLayout.PAGE_START);

        jPanel4.setLayout(new java.awt.BorderLayout());

        jXMapViewer.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                jXMapViewerMouseReleased(evt);
            }
        });

        javax.swing.GroupLayout jXMapViewerLayout = new javax.swing.GroupLayout(jXMapViewer);
        jXMapViewer.setLayout(jXMapViewerLayout);
        jXMapViewerLayout.setHorizontalGroup(
            jXMapViewerLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 516, Short.MAX_VALUE)
        );
        jXMapViewerLayout.setVerticalGroup(
            jXMapViewerLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 403, Short.MAX_VALUE)
        );

        jPanel4.add(jXMapViewer, java.awt.BorderLayout.CENTER);

        jPanel1.add(jPanel4, java.awt.BorderLayout.CENTER);

        jLabel1.setFont(new java.awt.Font("Serif", 1, 18)); // NOI18N
        jLabel1.setText("Address View");

        jLabel2.setText("Address Line 1");

        jLabel3.setText("Address Line 2");

        jLabel4.setText("City");

        jButton1.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jButton1.setText("Add");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        jButton2.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jButton2.setText("Update");
        jButton2.setPreferredSize(new java.awt.Dimension(75, 30));
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });

        jButton3.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jButton3.setText("Remove");
        jButton3.setPreferredSize(new java.awt.Dimension(75, 30));
        jButton3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton3ActionPerformed(evt);
            }
        });

        jTable2.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Id", "Line 1", "Line 2", "City"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jTable2.getTableHeader().setReorderingAllowed(false);
        jTable2.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jTable2MouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(jTable2);

        jLabel5.setFont(new java.awt.Font("Serif", 1, 12)); // NOI18N
        jLabel5.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel5.setText("Customer NIC");

        jComboBox1.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jLabel1)
                        .addGap(101, 101, 101)
                        .addComponent(jLabel5, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGap(30, 30, 30)
                        .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 119, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(30, 30, 30)
                        .addComponent(jButton2, javax.swing.GroupLayout.PREFERRED_SIZE, 119, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(30, 30, 30)
                        .addComponent(jButton3, javax.swing.GroupLayout.PREFERRED_SIZE, 119, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGap(20, 20, 20)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel2Layout.createSequentialGroup()
                                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel4)
                                    .addComponent(jLabel3))
                                .addGap(18, 18, 18)
                                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jComboBox1, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(jTextField2)))
                            .addGroup(jPanel2Layout.createSequentialGroup()
                                .addComponent(jLabel2)
                                .addGap(18, 18, 18)
                                .addComponent(jTextField1)))))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(18, 18, 18)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1)
                    .addComponent(jLabel5, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(20, 20, 20)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel2)
                    .addComponent(jTextField1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(15, 15, 15)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel3)
                    .addComponent(jTextField2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(15, 15, 15)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel4)
                    .addComponent(jComboBox1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(20, 20, 20)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 42, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton2, javax.swing.GroupLayout.PREFERRED_SIZE, 42, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton3, javax.swing.GroupLayout.PREFERRED_SIZE, 42, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 198, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(233, 233, 233))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, 518, javax.swing.GroupLayout.PREFERRED_SIZE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, 444, javax.swing.GroupLayout.PREFERRED_SIZE))
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void menuEndActionPerformed(java.awt.event.ActionEvent evt) {
        GeoPosition geop = jXMapViewer.convertPointToGeoPosition(mousePosition);
        MyWaypoint wayPoint = new MyWaypoint("End Location", MyWaypoint.PointType.END, event, new GeoPosition(geop.getLatitude(), geop.getLongitude()));
        addWaypoint(wayPoint);

//        home.getLabel().setText("Selected");
//        home.getLabel().setForeground(Color.green);
//        home.setLocation(geop.getLatitude(), geop.getLongitude());
//        System.out.println(geop.getLatitude());
//        System.out.println(geop.getLongitude());
    }

    private void setCustomerData() {

        try {

            ResultSet resultSet = MySQL.executeSearch("SELECT * FROM `customer` WHERE `customer_nic`='" + customerNIC + "'");
            if (resultSet.next()) {
                jLabel5.setText(resultSet.getString("first_name") + ' ' + resultSet.getString("last_name"));
            }

        } catch (Exception e) {
            SignIn.getLoggerObject().log(Level.WARNING, "Customer Data Setting Exception Found", e);

        }

    }

    private void jTable2MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jTable2MouseClicked

        int row = jTable2.getSelectedRow();

        if (row != -1) {
            if (MouseEvent.BUTTON3 == evt.getButton()) {
                jPopupMenu1.show(evt.getComponent(), evt.getX(), evt.getY());
            } else if (evt.getClickCount() == 2) {
                transport.setDeliveryLocation(String.valueOf(jTable2.getValueAt(row, 0)));
                this.dispose();
            }
        }
    }//GEN-LAST:event_jTable2MouseClicked

    private void jButton3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton3ActionPerformed

        int row = jTable2.getSelectedRow();

        if (row == -1) {
            JOptionPane.showMessageDialog(this, "You must Select one Location In order to Delete. ", "Warning", JOptionPane.OK_OPTION, JOptionIcon.WARNING.setICon());
        } else {

            try {

                String id = String.valueOf(jTable2.getValueAt(row, 0));

                MySQL.executeIUD("DELETE FROM `customer_address` WHERE `customer_address_id`='" + id + "'");
                JOptionPane.showMessageDialog(this, "Location Successfully Removed.", "Warning", JOptionPane.WARNING_MESSAGE);
                loadCustomerAddress();
                clearFields();

            } catch (Exception e) {
                SignIn.getLoggerObject().log(Level.WARNING, "Customer Address Remove Exception Found", e);

            }

        }
    }//GEN-LAST:event_jButton3ActionPerformed

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed

        int row = jTable2.getSelectedRow();

        if (row == -1) {
            JOptionPane.showMessageDialog(this, "You must Select one Location In order to Update. ", "Warning", JOptionPane.WARNING_MESSAGE);

        } else {

            String line1 = jTextField1.getText();
            String line2 = jTextField2.getText();
            String city = String.valueOf(jComboBox1.getSelectedItem());

            String id = String.valueOf(jTable2.getValueAt(row, 0));

            try {

                if (line1.isEmpty()) {
                    JOptionPane.showMessageDialog(this, "Please Enter Adress Line 1", "Warning", JOptionPane.WARNING_MESSAGE);
                } else if (line2.isEmpty()) {
                    JOptionPane.showMessageDialog(this, "Please Enter Adress Line 2", "Warning", JOptionPane.WARNING_MESSAGE);
                } else if (city.equals("Select")) {
                    JOptionPane.showMessageDialog(this, "Please Select the City ", "Warning", JOptionPane.WARNING_MESSAGE);
                } else if (waypoints.isEmpty()) {
                    Notifications.getInstance().show(Notifications.Type.WARNING, Notifications.Location.BOTTOM_RIGHT, "Please Select the Location.");
                } else {
                    boolean addressStat = true;

                    ResultSet resultSet = MySQL.executeSearch("SELECT * FROM `customer_address` INNER JOIN `city` ON `customer_address`.`city_city_id`=`city`.`city_id` WHERE `customer_nic`='" + customerNIC + "'");

                    while (resultSet.next()) {
                        if (line1.equals(resultSet.getString("line1")) && line2.equals(resultSet.getString("line2")) && city.equals(resultSet.getString("city.city_name")) && this.longtitude == resultSet.getDouble("longtitude") && this.latitude == resultSet.getDouble("latitude")) {

                            JOptionPane.showMessageDialog(this, "This Address is Already Registered.", "Warning", JOptionPane.WARNING_MESSAGE);
                            addressStat = false;
                            break;
                        }
                    }

                    if (addressStat) {
                        MySQL.executeIUD("UPDATE `customer_address` SET `line1`='" + line1 + "',`line2`='" + line2 + "',`longtitude`='" + this.longtitude + "',`latitude`='" + this.latitude + "',`city_city_id`='" + customerCityMap.get(city) + "' WHERE `customer_address_id`='" + id + "'");
                        Notifications.getInstance().show(Notifications.Type.SUCCESS, Notifications.Location.BOTTOM_RIGHT, "This Address is Updated Successfully.");
                        loadCustomerAddress();
                        clearFields();
                    }

                }
            } catch (Exception e) {
                SignIn.getLoggerObject().log(Level.WARNING, "Customer Address Update Exception Found", e);

            }

        }

    }//GEN-LAST:event_jButton2ActionPerformed

    private void loadCity() {

        try {

            ResultSet resultSet = MySQL.executeSearch("SELECT * FROM `city`");
            Vector<String> vector = new Vector<>();
            vector.add("Select");
            while (resultSet.next()) {

                vector.add(resultSet.getString("city_name"));
                customerCityMap.put(resultSet.getString("city_name"), resultSet.getString("city_id"));

            }

            DefaultComboBoxModel model = new DefaultComboBoxModel(vector);
            jComboBox1.setModel(model);

        } catch (Exception e) {
            SignIn.getLoggerObject().log(Level.WARNING, "Load City Exception Found", e);

        }

    }

    private void loadCustomerAddress() {

        if (!customerNIC.isEmpty()) {

            try {

                ResultSet resultSet = MySQL.executeSearch("SELECT * FROM `customer_address` INNER JOIN `city` ON `customer_address`.`city_city_id`=`city`.`city_id` WHERE `customer_nic`='" + customerNIC + "'");

                DefaultTableModel model = (DefaultTableModel) jTable2.getModel();
                model.setRowCount(0);

                while (resultSet.next()) {

                    Vector<String> vector = new Vector<>();
                    vector.add(resultSet.getString("customer_address_id"));
                    vector.add(resultSet.getString("line1"));
                    vector.add(resultSet.getString("line2"));
                    vector.add(resultSet.getString("city.city_name"));

                    model.addRow(vector);

                }
                jTable2.setModel(model);

            } catch (Exception e) {
                SignIn.getLoggerObject().log(Level.WARNING, "Load Customer Address Exception Found", e);

            }
        } else {
            Notifications.getInstance().show(Notifications.Type.WARNING, Notifications.Location.BOTTOM_RIGHT, "Something Went wrong Please Restart.");

        }

    }

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed

        String line1 = jTextField1.getText();
        String line2 = jTextField2.getText();
        String city = String.valueOf(jComboBox1.getSelectedItem());

        try {

            if (line1.isEmpty()) {
                Notifications.getInstance().show(Notifications.Type.WARNING, Notifications.Location.BOTTOM_RIGHT, "Please Enter Address Line 1.");

            } else if (line2.isEmpty()) {
                Notifications.getInstance().show(Notifications.Type.WARNING, Notifications.Location.BOTTOM_RIGHT, "Please Enter Address Line 2.");

            } else if (city.equals("Select")) {
                Notifications.getInstance().show(Notifications.Type.WARNING, Notifications.Location.BOTTOM_RIGHT, "Please Select a City.");

            } else if (waypoints.isEmpty()) {
                Notifications.getInstance().show(Notifications.Type.WARNING, Notifications.Location.BOTTOM_RIGHT, "Please Select the Location.");

            } else {
                boolean addressStat = true;

                for (int i = 0; i < jTable2.getRowCount(); i++) {

                    if (line1.equals(String.valueOf(jTable2.getValueAt(i, 1))) && line2.equals(String.valueOf(jTable2.getValueAt(i, 2))) && city.equals(String.valueOf(jTable2.getValueAt(i, 3)))) {

                        Notifications.getInstance().show(Notifications.Type.WARNING, Notifications.Location.BOTTOM_RIGHT, "This Address is Already Registered.");
                        addressStat = false;
                        break;
                    }
                }

                if (addressStat) {
                    MySQL.executeIUD("INSERT INTO `customer_address`(`line1`,`line2`,`longtitude`,`latitude`,`city_city_id`,`customer_nic`) VALUES('" + line1 + "','" + line2 + "','" + this.longtitude + "','" + this.latitude + "','" + this.customerCityMap.get(city) + "','" + this.customerNIC + "')");

                }

                loadCustomerAddress();
                clearFields();
            }

        } catch (Exception e) {
            SignIn.getLoggerObject().log(Level.WARNING, "Customer Address Adding Exception Found", e);
        }
    }//GEN-LAST:event_jButton1ActionPerformed

    private void jMenuItem1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem1ActionPerformed
        jButton1.setEnabled(false);
        jButton2.setEnabled(true);
        jButton3.setEnabled(true);

        loadLocation();

    }//GEN-LAST:event_jMenuItem1ActionPerformed

    private void comboMapTypeItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_comboMapTypeItemStateChanged
        TileFactoryInfo info;
        int index = comboMapType.getSelectedIndex();
        if (index == 0) {
            OSMTileFactoryInfo tt = new OSMTileFactoryInfo();
            tt.setDefaultZoomLevel(11);

            info = tt;
        } else if (index == 1) {
            info = new VirtualEarthTileFactoryInfo(VirtualEarthTileFactoryInfo.MAP);
            System.out.println(info.getDefaultZoomLevel());

        } else if (index == 2) {
            info = new VirtualEarthTileFactoryInfo(VirtualEarthTileFactoryInfo.HYBRID);
        } else {
            info = new VirtualEarthTileFactoryInfo(VirtualEarthTileFactoryInfo.SATELLITE);
        }
        DefaultTileFactory tileFactory = new DefaultTileFactory(info);
        jXMapViewer.setTileFactory(tileFactory);
    }//GEN-LAST:event_comboMapTypeItemStateChanged

    private void cmdClearActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmdClearActionPerformed
        clearWaypoint();
        clearFields();
    }//GEN-LAST:event_cmdClearActionPerformed

    private void jMenuItem2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem2ActionPerformed
        GeoPosition geop = jXMapViewer.convertPointToGeoPosition(mousePosition);
        MyWaypoint wayPoint = new MyWaypoint("End Location", MyWaypoint.PointType.END, event, new GeoPosition(geop.getLatitude(), geop.getLongitude()));
        addWaypoint(wayPoint);

        this.longtitude = geop.getLongitude();
        this.latitude = geop.getLatitude();

    }//GEN-LAST:event_jMenuItem2ActionPerformed

    private void jXMapViewerMouseReleased(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jXMapViewerMouseReleased
        if (SwingUtilities.isRightMouseButton(evt)) {
            mousePosition = evt.getPoint();
            jPopupMenu2.show(jXMapViewer, evt.getX(), evt.getY());
        }
    }//GEN-LAST:event_jXMapViewerMouseReleased

    public void loadDeliveryLocation(String customerAddressId) {
        try {

            ResultSet resultSet = MySQL.executeSearch("SELECT * FROM `customer_address` INNER JOIN `city` ON `customer_address`.`city_city_id`=`city`.`city_id` WHERE `customer_address_id`='" + customerAddressId + "'");
            if (resultSet.next()) {

                this.longtitude = resultSet.getDouble("longtitude");
                this.latitude = resultSet.getDouble("latitude");

                jTextField1.setText(resultSet.getString("customer_address.line1"));
                jTextField2.setText(resultSet.getString("customer_address.line2"));
                jComboBox1.setSelectedItem(resultSet.getString("city.city_name"));

                MyWaypoint wayPoint = new MyWaypoint("Location", MyWaypoint.PointType.START, event, new GeoPosition(resultSet.getDouble("latitude"), resultSet.getDouble("longtitude")));
                addWaypoint(wayPoint);

            }

        } catch (Exception e) {
            SignIn.getLoggerObject().log(Level.WARNING, "Load Delivery Location Exception Found", e);
        }
    }

    private void loadLocation() {
        int row = jTable2.getSelectedRow();

        if (row != -1) {
            jTextField1.setText(String.valueOf(jTable2.getValueAt(row, 1)));
            jTextField2.setText(String.valueOf(jTable2.getValueAt(row, 2)));
            jComboBox1.setSelectedItem(String.valueOf(jTable2.getValueAt(row, 3)));

            try {

                ResultSet resultSet = MySQL.executeSearch("SELECT * FROM `customer_address` WHERE `customer_address_id`='" + String.valueOf(jTable2.getValueAt(row, 0)) + "'");
                if (resultSet.next()) {

                    this.longtitude = resultSet.getDouble("longtitude");
                    this.latitude = resultSet.getDouble("latitude");

                    MyWaypoint wayPoint = new MyWaypoint("Location", MyWaypoint.PointType.START, event, new GeoPosition(resultSet.getDouble("latitude"), resultSet.getDouble("longtitude")));
                    addWaypoint(wayPoint);

                }

            } catch (Exception e) {
                SignIn.getLoggerObject().log(Level.WARNING, "Location Loading Exception Found", e);
//                e.printStackTrace();

            }
        }

    }

    private void clearFields() {
        jTextField1.setText("");
        jTextField2.setText("");
        jComboBox1.setSelectedIndex(0);

        //Disabling update remove buttons
        jButton2.setEnabled(false);
        jButton3.setEnabled(false);
        jButton1.setEnabled(true);
        jTable2.clearSelection();
        clearWaypoint();

    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton cmdClear;
    private javax.swing.JComboBox<String> comboMapType;
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jButton3;
    private javax.swing.JComboBox<String> jComboBox1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JMenuItem jMenuItem1;
    private javax.swing.JMenuItem jMenuItem2;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPopupMenu jPopupMenu1;
    private javax.swing.JPopupMenu jPopupMenu2;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTable jTable2;
    private javax.swing.JTextField jTextField1;
    private javax.swing.JTextField jTextField2;
    private lk.corevision.fruitshop.resources.transport.mapviewer.JXMapViewerCustom jXMapViewer;
    // End of variables declaration//GEN-END:variables
}
