package lk.corevision.fruitshop.gui;

import java.awt.BorderLayout;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.logging.Level;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;
import javax.swing.filechooser.FileNameExtensionFilter;
import lk.corevision.fruitshop.model.MySQL;
import lk.corevision.fruitshop.validate.JOptionIcon;
import raven.toast.Notifications;

public class Dashboard extends javax.swing.JFrame {

    private JFrame dashboard;
    private SalesAndOrders sao;
    private Transport transport;
    private static String employeeTypeName;
    private static String loggedEmployeeEmail;

    public static String getLoggedEmployeeEmail() {
        return loggedEmployeeEmail;
    }

    public static String getEmployeeTypeName() {
        return Dashboard.employeeTypeName;
    }

    public SalesAndOrders getSalesAndOrders() {
        return sao;
    }

    public Transport getTransport() {
        return transport;
    }

    public JLabel employeeType() {
        return employeeEmail;
    }

    public Dashboard(String fName, String lName, String email, String employeeTypeName) {
        initComponents();
        this.setExtendedState(JFrame.MAXIMIZED_BOTH);
        dashboard = this;
        fullName.setText(fName + " " + lName);
        employeeEmail.setText(email);
        employeeType.setText(employeeTypeName);
        Dashboard.employeeTypeName = employeeTypeName;
        Dashboard.loggedEmployeeEmail = email;
        Notifications.getInstance().setJFrame(dashboard);

        jPanel2.removeAll();
        DashboardPanel panel = new DashboardPanel();
        jPanel2.add(panel, BorderLayout.CENTER);
        SwingUtilities.updateComponentTreeUI(jPanel2);
//      jButton14.putClientProperty("Button.background", "#fff27a");

        try {
            InputStream imgStream = getClass().getResourceAsStream("/lk/corevision/fruitshop/resources/fruitshop-logo.png");
            if (imgStream == null) {
                SignIn.getLoggerObject().log(Level.WARNING, "Fruit shop logo image resource not found at /lk/corevision/fruitshop/resources/fruitshop-logo.png");
                return;
            }
            ImageIcon originalIcon = new ImageIcon(ImageIO.read(imgStream));
            this.setIconImage(originalIcon.getImage());

        } catch (IOException e) {
            SignIn.getLoggerObject().log(Level.WARNING, "Failed to load fruit shop logo image: " + e.getMessage(), e);
        } catch (Exception e) {
            SignIn.getLoggerObject().log(Level.WARNING, "Unexpected error loading fruit shop logo image: " + e.getMessage(), e);
        }
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        dashboardButtons = new javax.swing.ButtonGroup();
        jPanel1 = new javax.swing.JPanel();
        jPanel6 = new javax.swing.JPanel();
        fullName = new javax.swing.JLabel();
        employeeType = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        employeeEmail = new javax.swing.JLabel();
        jPanel7 = new javax.swing.JPanel();
        jToggleButton1 = new javax.swing.JToggleButton();
        jToggleButton2 = new javax.swing.JToggleButton();
        jToggleButton3 = new javax.swing.JToggleButton();
        jToggleButton4 = new javax.swing.JToggleButton();
        jToggleButton5 = new javax.swing.JToggleButton();
        jToggleButton6 = new javax.swing.JToggleButton();
        jToggleButton7 = new javax.swing.JToggleButton();
        jToggleButton9 = new javax.swing.JToggleButton();
        jToggleButton8 = new javax.swing.JToggleButton();
        jToggleButton10 = new javax.swing.JToggleButton();
        jPanel2 = new javax.swing.JPanel();
        jMenuBar8 = new javax.swing.JMenuBar();
        jMenu9 = new javax.swing.JMenu();
        setupConnectionMenue6 = new javax.swing.JMenuItem();
        setupBackupMenue = new javax.swing.JMenuItem();
        logOutMenue = new javax.swing.JMenuItem();
        exitMenue = new javax.swing.JMenuItem();
        jMenu10 = new javax.swing.JMenu();
        updateLogoMenue = new javax.swing.JMenuItem();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setPreferredSize(new java.awt.Dimension(1200, 700));

        jPanel1.setLayout(new java.awt.BorderLayout());

        jPanel6.setBackground(new java.awt.Color(227, 142, 73));

        fullName.setFont(new java.awt.Font("Inter 24pt", 1, 14)); // NOI18N
        fullName.setForeground(new java.awt.Color(221, 225, 221));
        fullName.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        fullName.setText("Amiru Upek");

        employeeType.setFont(new java.awt.Font("Inter 24pt", 0, 10)); // NOI18N
        employeeType.setForeground(new java.awt.Color(255, 255, 255));
        employeeType.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        employeeType.setText("Admin");

        jLabel5.setIcon(new javax.swing.ImageIcon(getClass().getResource("/lk/corevision/fruitshop/resources/other/profileIcon.png"))); // NOI18N

        employeeEmail.setFont(new java.awt.Font("Inter 24pt", 0, 10)); // NOI18N
        employeeEmail.setForeground(new java.awt.Color(255, 255, 255));
        employeeEmail.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        employeeEmail.setText("abc@gmail.com");

        javax.swing.GroupLayout jPanel6Layout = new javax.swing.GroupLayout(jPanel6);
        jPanel6.setLayout(jPanel6Layout);
        jPanel6Layout.setHorizontalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(employeeType, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(fullName, javax.swing.GroupLayout.DEFAULT_SIZE, 200, Short.MAX_VALUE)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel6Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jLabel5)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addComponent(employeeEmail, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        jPanel6Layout.setVerticalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel6Layout.createSequentialGroup()
                .addGap(18, 18, 18)
                .addComponent(jLabel5)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(fullName)
                .addGap(7, 7, 7)
                .addComponent(employeeEmail)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(employeeType))
        );

        jPanel1.add(jPanel6, java.awt.BorderLayout.PAGE_START);

        jPanel7.setBackground(new java.awt.Color(227, 142, 73));
        jPanel7.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 1, 1, 1));
        jPanel7.setPreferredSize(new java.awt.Dimension(200, 60));
        jPanel7.setLayout(new java.awt.GridLayout(10, 1, 1, 5));

        jToggleButton1.setBackground(new java.awt.Color(227, 142, 73));
        dashboardButtons.add(jToggleButton1);
        jToggleButton1.setFont(new java.awt.Font("Inter 24pt", 0, 13)); // NOI18N
        jToggleButton1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/lk/corevision/fruitshop/resources/other/light-dashboard-layout-20.png"))); // NOI18N
        jToggleButton1.setText("Dashboard");
        jToggleButton1.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 2, 0));
        jToggleButton1.setRolloverIcon(new javax.swing.ImageIcon(getClass().getResource("/lk/corevision/fruitshop/resources/other/icons8-dashboard-layout-20.png"))); // NOI18N
        jToggleButton1.setSelectedIcon(new javax.swing.ImageIcon(getClass().getResource("/lk/corevision/fruitshop/resources/other/icons8-dashboard-layout-20.png"))); // NOI18N
        jToggleButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jToggleButton1ActionPerformed(evt);
            }
        });
        jPanel7.add(jToggleButton1);

        jToggleButton2.setBackground(new java.awt.Color(227, 142, 73));
        dashboardButtons.add(jToggleButton2);
        jToggleButton2.setFont(new java.awt.Font("Inter 24pt", 0, 13)); // NOI18N
        jToggleButton2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/lk/corevision/fruitshop/resources/other/light-total-sales-20.png"))); // NOI18N
        jToggleButton2.setText("Sales & Orders");
        jToggleButton2.setToolTipText("");
        jToggleButton2.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 2, 0));
        jToggleButton2.setRolloverIcon(new javax.swing.ImageIcon(getClass().getResource("/lk/corevision/fruitshop/resources/other/icons8-total-sales-20.png"))); // NOI18N
        jToggleButton2.setSelectedIcon(new javax.swing.ImageIcon(getClass().getResource("/lk/corevision/fruitshop/resources/other/icons8-total-sales-20.png"))); // NOI18N
        jToggleButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jToggleButton2ActionPerformed(evt);
            }
        });
        jPanel7.add(jToggleButton2);

        jToggleButton3.setBackground(new java.awt.Color(227, 142, 73));
        dashboardButtons.add(jToggleButton3);
        jToggleButton3.setFont(new java.awt.Font("Inter 24pt", 0, 13)); // NOI18N
        jToggleButton3.setIcon(new javax.swing.ImageIcon(getClass().getResource("/lk/corevision/fruitshop/resources/other/light-employee-20.png"))); // NOI18N
        jToggleButton3.setText("Employee");
        jToggleButton3.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 2, 0));
        jToggleButton3.setRolloverIcon(new javax.swing.ImageIcon(getClass().getResource("/lk/corevision/fruitshop/resources/other/icons8-employee-20.png"))); // NOI18N
        jToggleButton3.setSelectedIcon(new javax.swing.ImageIcon(getClass().getResource("/lk/corevision/fruitshop/resources/other/icons8-employee-20.png"))); // NOI18N
        jToggleButton3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jToggleButton3ActionPerformed(evt);
            }
        });
        jPanel7.add(jToggleButton3);

        jToggleButton4.setBackground(new java.awt.Color(227, 142, 73));
        dashboardButtons.add(jToggleButton4);
        jToggleButton4.setFont(new java.awt.Font("Inter 24pt", 0, 13)); // NOI18N
        jToggleButton4.setIcon(new javax.swing.ImageIcon(getClass().getResource("/lk/corevision/fruitshop/resources/other/light-production-20 (2).png"))); // NOI18N
        jToggleButton4.setText("Production");
        jToggleButton4.setToolTipText("");
        jToggleButton4.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 2, 0));
        jToggleButton4.setRolloverIcon(new javax.swing.ImageIcon(getClass().getResource("/lk/corevision/fruitshop/resources/other/icons8-production-20 (3).png"))); // NOI18N
        jToggleButton4.setSelectedIcon(new javax.swing.ImageIcon(getClass().getResource("/lk/corevision/fruitshop/resources/other/icons8-production-20 (3).png"))); // NOI18N
        jToggleButton4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jToggleButton4ActionPerformed(evt);
            }
        });
        jPanel7.add(jToggleButton4);

        jToggleButton5.setBackground(new java.awt.Color(227, 142, 73));
        dashboardButtons.add(jToggleButton5);
        jToggleButton5.setFont(new java.awt.Font("Inter 24pt", 0, 13)); // NOI18N
        jToggleButton5.setIcon(new javax.swing.ImageIcon(getClass().getResource("/lk/corevision/fruitshop/resources/other/light-truck-20.png"))); // NOI18N
        jToggleButton5.setText("Transportation");
        jToggleButton5.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 2, 0));
        jToggleButton5.setRolloverIcon(new javax.swing.ImageIcon(getClass().getResource("/lk/corevision/fruitshop/resources/other/icons8-truck-20.png"))); // NOI18N
        jToggleButton5.setSelectedIcon(new javax.swing.ImageIcon(getClass().getResource("/lk/corevision/fruitshop/resources/other/icons8-truck-20.png"))); // NOI18N
        jToggleButton5.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jToggleButton5ActionPerformed(evt);
            }
        });
        jPanel7.add(jToggleButton5);

        jToggleButton6.setBackground(new java.awt.Color(227, 142, 73));
        dashboardButtons.add(jToggleButton6);
        jToggleButton6.setFont(new java.awt.Font("Inter 24pt", 0, 13)); // NOI18N
        jToggleButton6.setIcon(new javax.swing.ImageIcon(getClass().getResource("/lk/corevision/fruitshop/resources/other/light-verified-20.png"))); // NOI18N
        jToggleButton6.setText("Quality");
        jToggleButton6.setToolTipText("");
        jToggleButton6.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 2, 0));
        jToggleButton6.setRolloverIcon(new javax.swing.ImageIcon(getClass().getResource("/lk/corevision/fruitshop/resources/other/icons8-verified-20.png"))); // NOI18N
        jToggleButton6.setSelectedIcon(new javax.swing.ImageIcon(getClass().getResource("/lk/corevision/fruitshop/resources/other/icons8-verified-20.png"))); // NOI18N
        jToggleButton6.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jToggleButton6ActionPerformed(evt);
            }
        });
        jPanel7.add(jToggleButton6);

        jToggleButton7.setBackground(new java.awt.Color(227, 142, 73));
        dashboardButtons.add(jToggleButton7);
        jToggleButton7.setFont(new java.awt.Font("Inter 24pt", 0, 13)); // NOI18N
        jToggleButton7.setIcon(new javax.swing.ImageIcon(getClass().getResource("/lk/corevision/fruitshop/resources/other/light-bills-20.png"))); // NOI18N
        jToggleButton7.setText("Finance");
        jToggleButton7.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 2, 0));
        jToggleButton7.setRolloverIcon(new javax.swing.ImageIcon(getClass().getResource("/lk/corevision/fruitshop/resources/other/icons8-bills-20.png"))); // NOI18N
        jToggleButton7.setSelectedIcon(new javax.swing.ImageIcon(getClass().getResource("/lk/corevision/fruitshop/resources/other/icons8-bills-20.png"))); // NOI18N
        jToggleButton7.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jToggleButton7ActionPerformed(evt);
            }
        });
        jPanel7.add(jToggleButton7);

        jToggleButton9.setBackground(new java.awt.Color(227, 142, 73));
        dashboardButtons.add(jToggleButton9);
        jToggleButton9.setFont(new java.awt.Font("Inter 24pt", 0, 13)); // NOI18N
        jToggleButton9.setIcon(new javax.swing.ImageIcon(getClass().getResource("/lk/corevision/fruitshop/resources/other/light-marketing-20.png"))); // NOI18N
        jToggleButton9.setText("Marketing");
        jToggleButton9.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 2, 0));
        jToggleButton9.setRolloverIcon(new javax.swing.ImageIcon(getClass().getResource("/lk/corevision/fruitshop/resources/other/icons8-marketing-20.png"))); // NOI18N
        jToggleButton9.setSelectedIcon(new javax.swing.ImageIcon(getClass().getResource("/lk/corevision/fruitshop/resources/other/icons8-marketing-20.png"))); // NOI18N
        jToggleButton9.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jToggleButton9ActionPerformed(evt);
            }
        });
        jPanel7.add(jToggleButton9);

        jToggleButton8.setBackground(new java.awt.Color(227, 142, 73));
        dashboardButtons.add(jToggleButton8);
        jToggleButton8.setFont(new java.awt.Font("Inter 24pt", 0, 13)); // NOI18N
        jToggleButton8.setIcon(new javax.swing.ImageIcon(getClass().getResource("/lk/corevision/fruitshop/resources/other/light-customers-20.png"))); // NOI18N
        jToggleButton8.setText("Customers");
        jToggleButton8.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 2, 0));
        jToggleButton8.setRolloverIcon(new javax.swing.ImageIcon(getClass().getResource("/lk/corevision/fruitshop/resources/other/icons8-customers-20.png"))); // NOI18N
        jToggleButton8.setSelectedIcon(new javax.swing.ImageIcon(getClass().getResource("/lk/corevision/fruitshop/resources/other/icons8-customers-20.png"))); // NOI18N
        jToggleButton8.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jToggleButton8ActionPerformed(evt);
            }
        });
        jPanel7.add(jToggleButton8);

        jToggleButton10.setBackground(new java.awt.Color(227, 142, 73));
        dashboardButtons.add(jToggleButton10);
        jToggleButton10.setFont(new java.awt.Font("Inter 24pt", 0, 13)); // NOI18N
        jToggleButton10.setIcon(new javax.swing.ImageIcon(getClass().getResource("/lk/corevision/fruitshop/resources/other/light-supplier-20.png"))); // NOI18N
        jToggleButton10.setText("Suppliers");
        jToggleButton10.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 2, 0));
        jToggleButton10.setRolloverIcon(new javax.swing.ImageIcon(getClass().getResource("/lk/corevision/fruitshop/resources/other/icons8-supplier-20.png"))); // NOI18N
        jToggleButton10.setSelectedIcon(new javax.swing.ImageIcon(getClass().getResource("/lk/corevision/fruitshop/resources/other/icons8-supplier-20.png"))); // NOI18N
        jToggleButton10.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jToggleButton10ActionPerformed(evt);
            }
        });
        jPanel7.add(jToggleButton10);

        jPanel1.add(jPanel7, java.awt.BorderLayout.CENTER);

        getContentPane().add(jPanel1, java.awt.BorderLayout.LINE_START);

        jPanel2.setAutoscrolls(true);
        jPanel2.setLayout(new java.awt.BorderLayout());
        getContentPane().add(jPanel2, java.awt.BorderLayout.CENTER);

        jMenu9.setText("File");

        setupConnectionMenue6.setText("Setup Database");
        setupConnectionMenue6.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                setupConnectionMenue6ActionPerformed(evt);
            }
        });
        jMenu9.add(setupConnectionMenue6);

        setupBackupMenue.setText("Backup");
        setupBackupMenue.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                setupBackupMenueActionPerformed(evt);
            }
        });
        jMenu9.add(setupBackupMenue);

        logOutMenue.setText("Log Out");
        logOutMenue.setToolTipText("");
        logOutMenue.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                logOutMenueActionPerformed(evt);
            }
        });
        jMenu9.add(logOutMenue);

        exitMenue.setText("Exit");
        exitMenue.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                exitMenueActionPerformed(evt);
            }
        });
        jMenu9.add(exitMenue);

        jMenuBar8.add(jMenu9);

        jMenu10.setText("Edit");

        updateLogoMenue.setText("Update Logo");
        updateLogoMenue.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                updateLogoMenueActionPerformed(evt);
            }
        });
        jMenu10.add(updateLogoMenue);

        jMenuBar8.add(jMenu10);

        setJMenuBar(jMenuBar8);

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jToggleButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jToggleButton1ActionPerformed
        jPanel2.removeAll();
        DashboardPanel product = new DashboardPanel();
        jPanel2.add(product, BorderLayout.CENTER);
        SwingUtilities.updateComponentTreeUI(jPanel2);
//      dasboardButtonClick(jButton12);
    }//GEN-LAST:event_jToggleButton1ActionPerformed

    private void jToggleButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jToggleButton2ActionPerformed
        if (Dashboard.getEmployeeTypeName().equals("Admin") || Dashboard.getEmployeeTypeName().equals("Sales Manager") || Dashboard.getEmployeeTypeName().equals("Cashair")) {

            jPanel2.removeAll();
            sao = new SalesAndOrders(this);
            jPanel2.add(sao, BorderLayout.CENTER);
            SwingUtilities.updateComponentTreeUI(jPanel2);
        } else {
            JOptionPane.showMessageDialog(this, "Access blocked for role: " + Dashboard.employeeTypeName, "Access Denied", JOptionPane.WARNING_MESSAGE, JOptionIcon.WARNING.setICon());
        }
//        dasboardButtonClick(jButton13);
    }//GEN-LAST:event_jToggleButton2ActionPerformed

    private void jToggleButton3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jToggleButton3ActionPerformed
        if (Dashboard.getEmployeeTypeName().equals("Admin") || Dashboard.getEmployeeTypeName().equals("HR Manager")) {
            jPanel2.removeAll();
            Employee employee1 = new Employee(this);
            jPanel2.add(employee1, BorderLayout.CENTER);
            SwingUtilities.updateComponentTreeUI(jPanel2);
        } else {
            JOptionPane.showMessageDialog(this, "Access blocked for role: " + Dashboard.employeeTypeName, "Access Denied", JOptionPane.WARNING_MESSAGE, JOptionIcon.WARNING.setICon());
        }

//        dasboardButtonClick(jButton14);
    }//GEN-LAST:event_jToggleButton3ActionPerformed

    private void jToggleButton4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jToggleButton4ActionPerformed

        if (Dashboard.getEmployeeTypeName().equals("Admin") || Dashboard.getEmployeeTypeName().equals("Production Manager")) {
            jPanel2.removeAll();
            Product product = new Product(this);
            jPanel2.add(product, BorderLayout.CENTER);
            SwingUtilities.updateComponentTreeUI(jPanel2);
        } else {
            JOptionPane.showMessageDialog(this, "Access blocked for role: " + Dashboard.getEmployeeTypeName(), "Access Denied", JOptionPane.WARNING_MESSAGE, JOptionIcon.WARNING.setICon());
        }
    }//GEN-LAST:event_jToggleButton4ActionPerformed

    private void jToggleButton5ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jToggleButton5ActionPerformed

        if (Dashboard.getEmployeeTypeName().equals("Admin") || Dashboard.getEmployeeTypeName().equals("Trasport Manager")) {

            jPanel2.removeAll();
            transport = new Transport(this);
            jPanel2.add(transport, BorderLayout.CENTER);
            SwingUtilities.updateComponentTreeUI(jPanel2);
//        dasboardButtonClick(jButton15);
        } else {
            JOptionPane.showMessageDialog(this, "Access blocked for role: " + Dashboard.getEmployeeTypeName(), "Access Denied", JOptionPane.WARNING_MESSAGE, JOptionIcon.WARNING.setICon());
        }

    }//GEN-LAST:event_jToggleButton5ActionPerformed

    private void jToggleButton6ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jToggleButton6ActionPerformed
        if (Dashboard.getEmployeeTypeName().equals("Admin") || Dashboard.getEmployeeTypeName().equals("Quality Manager") || Dashboard.getEmployeeTypeName().equals("Production Manager")) {
            jPanel2.removeAll();
            productQuality pq = new productQuality();
            pq.getDashboard(this);
            jPanel2.add(pq, BorderLayout.CENTER);
            SwingUtilities.updateComponentTreeUI(jPanel2);
        } else {
            JOptionPane.showMessageDialog(this, "Access blocked for role: " + Dashboard.getEmployeeTypeName(), "Access Denied", JOptionPane.WARNING_MESSAGE, JOptionIcon.WARNING.setICon());
        }
//        dasboardButtonClick(jButton16);
    }//GEN-LAST:event_jToggleButton6ActionPerformed

    private void jToggleButton7ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jToggleButton7ActionPerformed

        if (Dashboard.getEmployeeTypeName().equals("Admin") || Dashboard.getEmployeeTypeName().equals("Finance Manager")) {
            jPanel2.removeAll();
            Finance finance = new Finance();
            jPanel2.add(finance, BorderLayout.CENTER);
            SwingUtilities.updateComponentTreeUI(jPanel2);
//        dasboardButtonClick(jButton17);

        } else {
            JOptionPane.showMessageDialog(this, "Access blocked for role: " + Dashboard.getEmployeeTypeName(), "Access Denied", JOptionPane.WARNING_MESSAGE, JOptionIcon.WARNING.setICon());
        }


    }//GEN-LAST:event_jToggleButton7ActionPerformed

    private void jToggleButton9ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jToggleButton9ActionPerformed
        if (Dashboard.getEmployeeTypeName().equals("Admin") || Dashboard.getEmployeeTypeName().equals("Marketing Manager")) {
            jPanel2.removeAll();
            Marketing marketing = new Marketing();
            jPanel2.add(marketing, BorderLayout.CENTER);
            SwingUtilities.updateComponentTreeUI(jPanel2);

        } else {
            JOptionPane.showMessageDialog(this, "Access blocked for role: " + Dashboard.employeeTypeName, "Access Denied", JOptionPane.WARNING_MESSAGE, JOptionIcon.WARNING.setICon());
        }
    }//GEN-LAST:event_jToggleButton9ActionPerformed

    private void jToggleButton8ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jToggleButton8ActionPerformed

        if (Dashboard.getEmployeeTypeName().equals("Admin") || Dashboard.getEmployeeTypeName().equals("HR Manager")) {
            jPanel2.removeAll();
            Customer customer = new Customer();
            jPanel2.add(customer, BorderLayout.CENTER);
            SwingUtilities.updateComponentTreeUI(jPanel2);
//        dasboardButtonClick(jButton19);
        } else {
            JOptionPane.showMessageDialog(this, "Access blocked for role: " + Dashboard.getEmployeeTypeName(), "Access Denied", JOptionPane.WARNING_MESSAGE, JOptionIcon.WARNING.setICon());
        }
    }//GEN-LAST:event_jToggleButton8ActionPerformed

    private void jToggleButton10ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jToggleButton10ActionPerformed

        if (Dashboard.getEmployeeTypeName().equals("Admin") || Dashboard.getEmployeeTypeName().equals("Finance Manager") || Dashboard.getEmployeeTypeName().equals("Supplier Manager")) {
            jPanel2.removeAll();
            Supplier supplier = new Supplier(this);
            jPanel2.add(supplier, BorderLayout.CENTER);
            SwingUtilities.updateComponentTreeUI(jPanel2);
//        dasboardButtonClick(jButton20);
        } else {
            JOptionPane.showMessageDialog(this, "Access blocked for role: " + Dashboard.getEmployeeTypeName(), "Access Denied", JOptionPane.WARNING_MESSAGE, JOptionIcon.WARNING.setICon());
        }
    }//GEN-LAST:event_jToggleButton10ActionPerformed

    private void setupConnectionMenue4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_setupConnectionMenue4ActionPerformed
        if (Dashboard.getEmployeeTypeName().equals("Admin")) {
            SetupConnection setupConnection = new SetupConnection();
            setupConnection.setVisible(true);
        } else {
            JOptionPane.showMessageDialog(this, "Access blocked for role: " + Dashboard.employeeTypeName, "Access Denied", JOptionPane.WARNING_MESSAGE, JOptionIcon.WARNING.setICon());
        }
    }//GEN-LAST:event_setupConnectionMenue4ActionPerformed

    private void setupBackupMenueActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_setupBackupMenueActionPerformed
        if (Dashboard.getEmployeeTypeName().equals("Admin")) {
            Backup_Restore backup_Restore = new Backup_Restore();
            backup_Restore.setVisible(true);
        } else {
            JOptionPane.showMessageDialog(this, "Access blocked for role: " + Dashboard.employeeTypeName, "Access Denied", JOptionPane.WARNING_MESSAGE, JOptionIcon.WARNING.setICon());
        }

    }//GEN-LAST:event_setupBackupMenueActionPerformed

    private void exitMenueActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_exitMenueActionPerformed
        try {
            MySQL.closeConnection();
            System.exit(0);
        } catch (Exception e) {
            SignIn.getLoggerObject().log(Level.WARNING, "Connection close error ", e);
        }
    }//GEN-LAST:event_exitMenueActionPerformed

    private void setupConnectionMenue6ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_setupConnectionMenue6ActionPerformed
        if (Dashboard.getEmployeeTypeName().equals("Admin")) {
            SetupConnection setupConnection = new SetupConnection();
            setupConnection.setVisible(true);
        } else {
            JOptionPane.showMessageDialog(this, "Access blocked for role: " + Dashboard.employeeTypeName, "Access Denied", JOptionPane.WARNING_MESSAGE, JOptionIcon.WARNING.setICon());
        }

    }//GEN-LAST:event_setupConnectionMenue6ActionPerformed

    private void logOutMenueActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_logOutMenueActionPerformed
        this.dispose();
        new SignIn().setVisible(true);
    }//GEN-LAST:event_logOutMenueActionPerformed

    private void updateLogoMenueActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_updateLogoMenueActionPerformed
        if (Dashboard.getEmployeeTypeName().equals("Admin")) {
            try {

                JFileChooser fileChooser = new JFileChooser();
                fileChooser.setDialogTitle("Select a PNG Logo");
                fileChooser.setFileFilter(new FileNameExtensionFilter("PNG Images", "png"));

                int result = fileChooser.showOpenDialog(this);

                if (result == JFileChooser.APPROVE_OPTION) {
                    File selectedFile = fileChooser.getSelectedFile();

                    // Validate .png extension
                    if (!selectedFile.getName().toLowerCase().endsWith(".png")) {
                        JOptionPane.showMessageDialog(null, "Only .png files are allowed!", "Invalid File", JOptionPane.ERROR_MESSAGE);
                        return;
                    }

                    // Target path OUTSIDE of src folder
                    File targetFile = new File("src/lk/corevision/fruitshop/resources/logo.png");

                    // Create parent folders if not exist
                    if (!targetFile.getParentFile().exists()) {
                        targetFile.getParentFile().mkdirs();
                    }

                    // Copy file (Simple and readable version)
                    try (
                            InputStream in = new FileInputStream(selectedFile); OutputStream out = new FileOutputStream(targetFile)) {
                        byte[] buffer = new byte[1024];
                        int length;
                        while ((length = in.read(buffer)) > 0) {
                            out.write(buffer, 0, length);
                        }
                        JOptionPane.showMessageDialog(null, "Logo image replaced successfully!", "Success", JOptionPane.INFORMATION_MESSAGE, JOptionIcon.SUCCESS.setICon());
                    } catch (IOException ex) {
                        JOptionPane.showMessageDialog(null, "Error replacing image: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE, JOptionIcon.ERROR.setICon());
                    }
                }

            } catch (Exception e) {
                JOptionPane.showMessageDialog(null, "Unexpected error: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE, JOptionIcon.ERROR.setICon());
            }
        } else {
            JOptionPane.showMessageDialog(this, "Access blocked for role: " + Dashboard.employeeTypeName, "Access Denied", JOptionPane.WARNING_MESSAGE, JOptionIcon.WARNING.setICon());
        }

    }//GEN-LAST:event_updateLogoMenueActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.ButtonGroup dashboardButtons;
    private javax.swing.JLabel employeeEmail;
    private javax.swing.JLabel employeeType;
    private javax.swing.JMenuItem exitMenue;
    private javax.swing.JLabel fullName;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JMenu jMenu10;
    private javax.swing.JMenu jMenu9;
    private javax.swing.JMenuBar jMenuBar8;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JPanel jPanel7;
    private javax.swing.JToggleButton jToggleButton1;
    private javax.swing.JToggleButton jToggleButton10;
    private javax.swing.JToggleButton jToggleButton2;
    private javax.swing.JToggleButton jToggleButton3;
    private javax.swing.JToggleButton jToggleButton4;
    private javax.swing.JToggleButton jToggleButton5;
    private javax.swing.JToggleButton jToggleButton6;
    private javax.swing.JToggleButton jToggleButton7;
    private javax.swing.JToggleButton jToggleButton8;
    private javax.swing.JToggleButton jToggleButton9;
    private javax.swing.JMenuItem logOutMenue;
    private javax.swing.JMenuItem setupBackupMenue;
    private javax.swing.JMenuItem setupConnectionMenue6;
    private javax.swing.JMenuItem updateLogoMenue;
    // End of variables declaration//GEN-END:variables
}
