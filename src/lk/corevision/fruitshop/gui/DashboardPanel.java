package lk.corevision.fruitshop.gui;

import java.awt.Color;
import java.awt.Image;
import java.io.IOException;
import java.io.InputStream;
import java.sql.ResultSet;
import java.util.logging.Level;
import javaswingdev.chart.ModelPieChart;
import javaswingdev.chart.PieChart;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import lk.corevision.fruitshop.model.MySQL;
import raven.chart.ModelChart;

/**
 *
 * @author ASUS™
 */
public class DashboardPanel extends javax.swing.JPanel {

    public DashboardPanel() {
        initComponents();
        init();
        loadDashBoardData();

    }

    private void loadDashBoardData() {

        int orders = 0;
        int customer = 0;
        int users = 0;
        double earning = 0;

        try {

            ResultSet resultSet = MySQL.executeSearch("SELECT * FROM `orders`");
            ResultSet resultSet2 = MySQL.executeSearch("SELECT * FROM `customer`");
            ResultSet resultSet3 = MySQL.executeSearch("SELECT * FROM `employee`");

            while (resultSet.next()) {
                orders++;
            }

            while (resultSet2.next()) {
                customer++;
            }

            while (resultSet3.next()) {
                users++;
            }

            ResultSet resultSet4 = MySQL.executeSearch("SELECT * FROM `orders`");

            while (resultSet4.next()) {

                if (resultSet4.getInt("order_status_id") == 2) {
                    earning += resultSet4.getDouble("paid_amount");
                }

            }

            String formatedEarning = formatNumber(earning);

            jLabel2.setText(String.valueOf(orders));
            jLabel7.setText(String.valueOf(customer));
            jLabel10.setText(String.valueOf(users));
            jLabel13.setText(formatedEarning);

        } catch (Exception e) {

            SignIn.getLoggerObject().log(Level.WARNING, "Dashboard Data Load Exception Found", e);
        }

    }

    public static String formatNumber(double number) {
        if (number < 1000) {
            return String.format("%.0f", number);
        } else if (number < 1000000) {
            return String.format("%.1fk", number / 1000);
        } else {
            return String.format("%.1fM", number / 1000000);
        }
    }

//        Employee Type Chart
    private void chartData() {

        try {

            ResultSet resultSet = MySQL.executeSearch("SELECT employee_type.employee_type, COUNT(employee.NIC) AS count FROM employee_type LEFT JOIN employee ON employee_type.employee_type_id=employee.employee_type_employee_type_id GROUP BY employee_type.employee_type_id,employee_type.employee_type;");

            Color[] colors = {
                new Color(23, 126, 238), // Blue
                new Color(221, 65, 65), // Red
                new Color(47, 157, 64), // Green
                new Color(196, 151, 58), // Gold
                new Color(156, 39, 176), // Purple
                new Color(244, 67, 54), // Bright Red
                new Color(33, 150, 243), // Light Blue
                new Color(76, 175, 80), // Light Green
                new Color(255, 152, 0), // Orange
                new Color(121, 85, 72), // Brown
                new Color(96, 125, 139), // Blue Gray
                new Color(233, 30, 99) // Pink
            };

            int colorIndex = 0;

            pieChart2.clearData();

            pieChart2.setChartType(PieChart.PeiChartType.DONUT_CHART);

            while (resultSet.next()) {
                String employeeType = resultSet.getString("employee_type");
                int count = resultSet.getInt("count");

                Color color = colors[colorIndex % colors.length];
                colorIndex++;

                pieChart2.addData(new ModelPieChart(employeeType, count, color));
            }

        } catch (Exception e) {
            SignIn.getLoggerObject().log(Level.WARNING, "Dashboard Pie Chart Load Exception Found", e);

        }

    }

    private void chartData2() {
        try {

            ResultSet resultSet = MySQL.executeSearch("SELECT order_status.order_status, COUNT(orders.order_id) AS count FROM order_status LEFT JOIN \n"
                    + "orders ON order_status.order_status_id = orders.order_status_id GROUP BY "
                    + "order_status.order_status_id,order_status.order_status");

            Color[] colors = {
                new Color(23, 126, 238), // Blue
                new Color(221, 65, 65), // Red
                new Color(47, 157, 64), // Green
                new Color(196, 151, 58), // Gold
                new Color(156, 39, 176), // Purple
                new Color(244, 67, 54), // Bright Red
                new Color(33, 150, 243), // Light Blue
                new Color(76, 175, 80), // Light Green
                new Color(255, 152, 0), // Orange
                new Color(121, 85, 72), // Brown
                new Color(96, 125, 139), // Blue Gray
                new Color(233, 30, 99) // Pink
            };

            int colorIndex = 0;

            pieChart3.clearData();

            pieChart3.setChartType(PieChart.PeiChartType.DONUT_CHART);

            while (resultSet.next()) {
                String order_status = resultSet.getString("order_status");
                int count = resultSet.getInt("count");

                Color color = colors[colorIndex % colors.length];
                colorIndex++;

                pieChart3.addData(new ModelPieChart(order_status, count, color));
            }

        } catch (Exception e) {
            SignIn.getLoggerObject().log(Level.WARNING, "Dashboard Pie Chart Load Exception Found", e);

        }
    }

    private void init() {
        try {

            roundPanel1.setGradientColors(Color.decode("#183d8a"), Color.decode("#719bf0"));
            roundPanel2.setGradientColors(Color.decode("#0D5917"), Color.decode("#51F815"));
            roundPanel3.setGradientColors(Color.decode("#C55404"), Color.decode("#F8D32A"));
            roundPanel4.setGradientColors(Color.decode("#931835"), Color.decode("#F18352"));
            chartData();
            chartData2();

//            InputStream imgStream = getClass().getResourceAsStream("/lk/corevision/fruitshop/resources/production/employee.png");
//            ImageIcon originalIcon = new ImageIcon(ImageIO.read(imgStream));
//            Image scaledImage = originalIcon.getImage().getScaledInstance(69, 69, Image.SCALE_SMOOTH);
//            jLabel2.setIcon(new ImageIcon(scaledImage));
//
//            InputStream imgStream2 = getClass().getResourceAsStream("/lk/corevision/fruitshop/resources/production/income.png");
//            ImageIcon originalIcon2 = new ImageIcon(ImageIO.read(imgStream2));
//            Image scaledImage2 = originalIcon2.getImage().getScaledInstance(69, 69, Image.SCALE_SMOOTH);
//            jLabel3.setIcon(new ImageIcon(scaledImage2));
//
//            InputStream imgStream3 = getClass().getResourceAsStream("/lk/corevision/fruitshop/resources/production/expense.png");
//            ImageIcon originalIcon3 = new ImageIcon(ImageIO.read(imgStream3));
//            Image scaledImage3 = originalIcon3.getImage().getScaledInstance(69, 69, Image.SCALE_SMOOTH);
//            jLabel4.setIcon(new ImageIcon(scaledImage3));
//
//            InputStream imgStream4 = getClass().getResourceAsStream("/lk/corevision/fruitshop/resources/production/supplier.png");
//            ImageIcon originalIcon4 = new ImageIcon(ImageIO.read(imgStream4));
//            Image scaledImage4 = originalIcon4.getImage().getScaledInstance(69, 69, Image.SCALE_SMOOTH);
//            jLabel5.setIcon(new ImageIcon(scaledImage4));
        } catch (Exception e) {
            SignIn.getLoggerObject().log(Level.WARNING, "Dashboard Colors Loading Exception Found", e);
        }
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jPanel2 = new javax.swing.JPanel();
        jPanel3 = new javax.swing.JPanel();
        roundPanel1 = new lk.corevision.fruitshop.component.RoundPanel();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        roundPanel2 = new lk.corevision.fruitshop.component.RoundPanel();
        jLabel5 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        roundPanel3 = new lk.corevision.fruitshop.component.RoundPanel();
        jLabel8 = new javax.swing.JLabel();
        jLabel9 = new javax.swing.JLabel();
        jLabel10 = new javax.swing.JLabel();
        roundPanel4 = new lk.corevision.fruitshop.component.RoundPanel();
        jLabel11 = new javax.swing.JLabel();
        jLabel12 = new javax.swing.JLabel();
        jLabel13 = new javax.swing.JLabel();
        jPanel4 = new javax.swing.JPanel();
        jPanel5 = new javax.swing.JPanel();
        jPanel6 = new javax.swing.JPanel();
        jLabel14 = new javax.swing.JLabel();
        jPanel7 = new javax.swing.JPanel();
        jPanel8 = new javax.swing.JPanel();
        pieChart2 = new javaswingdev.chart.PieChart();
        jLabel15 = new javax.swing.JLabel();
        jPanel9 = new javax.swing.JPanel();
        jLabel16 = new javax.swing.JLabel();
        pieChart3 = new javaswingdev.chart.PieChart();

        setPreferredSize(new java.awt.Dimension(816, 565));
        setLayout(new java.awt.BorderLayout());

        jPanel1.setBackground(new java.awt.Color(243, 242, 242));

        jLabel1.setFont(new java.awt.Font("Inter 24pt", 0, 18)); // NOI18N
        jLabel1.setText("Dashboard");

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(27, 27, 27)
                .addComponent(jLabel1)
                .addContainerGap(1019, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(17, 17, 17)
                .addComponent(jLabel1)
                .addContainerGap(17, Short.MAX_VALUE))
        );

        add(jPanel1, java.awt.BorderLayout.PAGE_START);

        jPanel2.setLayout(new java.awt.BorderLayout());

        jPanel3.setBorder(javax.swing.BorderFactory.createEmptyBorder(15, 5, 5, 5));
        jPanel3.setLayout(new java.awt.GridLayout(1, 4, 10, 5));

        roundPanel1.setBackground(new java.awt.Color(23, 163, 184));

        jLabel2.setFont(new java.awt.Font("Inter 24pt", 1, 48)); // NOI18N
        jLabel2.setForeground(new java.awt.Color(255, 255, 255));
        jLabel2.setText("120");

        jLabel3.setIcon(new javax.swing.ImageIcon(getClass().getResource("/lk/corevision/fruitshop/resources/other/icons8-maximum-order-1001.png"))); // NOI18N

        jLabel4.setFont(new java.awt.Font("Inter 24pt", 0, 24)); // NOI18N
        jLabel4.setForeground(new java.awt.Color(255, 255, 255));
        jLabel4.setText("Orders");

        javax.swing.GroupLayout roundPanel1Layout = new javax.swing.GroupLayout(roundPanel1);
        roundPanel1.setLayout(roundPanel1Layout);
        roundPanel1Layout.setHorizontalGroup(
            roundPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(roundPanel1Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(roundPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel4)
                    .addComponent(jLabel2))
                .addGap(27, 27, 27)
                .addComponent(jLabel3)
                .addGap(16, 16, 16))
        );
        roundPanel1Layout.setVerticalGroup(
            roundPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(roundPanel1Layout.createSequentialGroup()
                .addGap(30, 30, 30)
                .addGroup(roundPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jLabel3)
                    .addGroup(roundPanel1Layout.createSequentialGroup()
                        .addComponent(jLabel2)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel4)
                        .addGap(6, 6, 6)))
                .addContainerGap(20, Short.MAX_VALUE))
        );

        jPanel3.add(roundPanel1);

        roundPanel2.setBackground(new java.awt.Color(39, 167, 67));

        jLabel5.setIcon(new javax.swing.ImageIcon(getClass().getResource("/lk/corevision/fruitshop/resources/other/icons8-add-user-male-100.png"))); // NOI18N

        jLabel6.setFont(new java.awt.Font("Inter 24pt", 0, 24)); // NOI18N
        jLabel6.setForeground(new java.awt.Color(255, 255, 255));
        jLabel6.setText("Customers");

        jLabel7.setFont(new java.awt.Font("Inter 24pt", 1, 48)); // NOI18N
        jLabel7.setForeground(new java.awt.Color(255, 255, 255));
        jLabel7.setText("180");

        javax.swing.GroupLayout roundPanel2Layout = new javax.swing.GroupLayout(roundPanel2);
        roundPanel2.setLayout(roundPanel2Layout);
        roundPanel2Layout.setHorizontalGroup(
            roundPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(roundPanel2Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(roundPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel6)
                    .addComponent(jLabel7))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jLabel5)
                .addGap(22, 22, 22))
        );
        roundPanel2Layout.setVerticalGroup(
            roundPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(roundPanel2Layout.createSequentialGroup()
                .addGap(30, 30, 30)
                .addGroup(roundPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel5)
                    .addGroup(roundPanel2Layout.createSequentialGroup()
                        .addComponent(jLabel7)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel6)))
                .addContainerGap(21, Short.MAX_VALUE))
        );

        jPanel3.add(roundPanel2);

        roundPanel3.setBackground(new java.awt.Color(255, 194, 9));

        jLabel8.setIcon(new javax.swing.ImageIcon(getClass().getResource("/lk/corevision/fruitshop/resources/other/icons8-fingerprint-accepted-100.png"))); // NOI18N

        jLabel9.setFont(new java.awt.Font("Inter 24pt", 0, 24)); // NOI18N
        jLabel9.setForeground(new java.awt.Color(255, 255, 255));
        jLabel9.setText("Users");

        jLabel10.setFont(new java.awt.Font("Inter 24pt", 1, 48)); // NOI18N
        jLabel10.setForeground(new java.awt.Color(255, 255, 255));
        jLabel10.setText("60");

        javax.swing.GroupLayout roundPanel3Layout = new javax.swing.GroupLayout(roundPanel3);
        roundPanel3.setLayout(roundPanel3Layout);
        roundPanel3Layout.setHorizontalGroup(
            roundPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(roundPanel3Layout.createSequentialGroup()
                .addGap(36, 36, 36)
                .addGroup(roundPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel9)
                    .addComponent(jLabel10))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jLabel8)
                .addGap(19, 19, 19))
        );
        roundPanel3Layout.setVerticalGroup(
            roundPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(roundPanel3Layout.createSequentialGroup()
                .addGap(30, 30, 30)
                .addGroup(roundPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel8)
                    .addGroup(roundPanel3Layout.createSequentialGroup()
                        .addComponent(jLabel10)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel9)))
                .addContainerGap(21, Short.MAX_VALUE))
        );

        jPanel3.add(roundPanel3);

        roundPanel4.setBackground(new java.awt.Color(221, 52, 68));

        jLabel11.setIcon(new javax.swing.ImageIcon(getClass().getResource("/lk/corevision/fruitshop/resources/other/icons8-money-100.png"))); // NOI18N

        jLabel12.setFont(new java.awt.Font("Inter 24pt", 0, 24)); // NOI18N
        jLabel12.setForeground(new java.awt.Color(255, 255, 255));
        jLabel12.setText("Earnings");

        jLabel13.setFont(new java.awt.Font("Inter 24pt", 1, 48)); // NOI18N
        jLabel13.setForeground(new java.awt.Color(255, 255, 255));
        jLabel13.setText("250K");

        javax.swing.GroupLayout roundPanel4Layout = new javax.swing.GroupLayout(roundPanel4);
        roundPanel4.setLayout(roundPanel4Layout);
        roundPanel4Layout.setHorizontalGroup(
            roundPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(roundPanel4Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(roundPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel12)
                    .addComponent(jLabel13))
                .addGap(27, 27, 27)
                .addComponent(jLabel11)
                .addGap(16, 16, 16))
        );
        roundPanel4Layout.setVerticalGroup(
            roundPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(roundPanel4Layout.createSequentialGroup()
                .addGap(30, 30, 30)
                .addGroup(roundPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jLabel11)
                    .addGroup(roundPanel4Layout.createSequentialGroup()
                        .addComponent(jLabel13)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel12)
                        .addGap(6, 6, 6)))
                .addContainerGap(20, Short.MAX_VALUE))
        );

        jPanel3.add(roundPanel4);

        jPanel2.add(jPanel3, java.awt.BorderLayout.PAGE_START);

        jPanel4.setLayout(new java.awt.GridLayout(1, 2));

        javax.swing.GroupLayout jPanel5Layout = new javax.swing.GroupLayout(jPanel5);
        jPanel5.setLayout(jPanel5Layout);
        jPanel5Layout.setHorizontalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 0, Short.MAX_VALUE)
        );
        jPanel5Layout.setVerticalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 0, Short.MAX_VALUE)
        );

        jPanel4.add(jPanel5);

        jPanel6.setBackground(new java.awt.Color(255, 102, 102));

        javax.swing.GroupLayout jPanel6Layout = new javax.swing.GroupLayout(jPanel6);
        jPanel6.setLayout(jPanel6Layout);
        jPanel6Layout.setHorizontalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 0, Short.MAX_VALUE)
        );
        jPanel6Layout.setVerticalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 0, Short.MAX_VALUE)
        );

        jPanel4.add(jPanel6);

        jPanel2.add(jPanel4, java.awt.BorderLayout.CENTER);

        jLabel14.setFont(new java.awt.Font("Serif", 1, 18)); // NOI18N
        jLabel14.setText("Dashboard");
        jPanel2.add(jLabel14);

        jPanel7.setLayout(new java.awt.BorderLayout());

        jLabel15.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        jLabel15.setForeground(new java.awt.Color(51, 51, 51));
        jLabel15.setText("Employee Types");

        javax.swing.GroupLayout jPanel8Layout = new javax.swing.GroupLayout(jPanel8);
        jPanel8.setLayout(jPanel8Layout);
        jPanel8Layout.setHorizontalGroup(
            jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel8Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(pieChart2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
            .addGroup(jPanel8Layout.createSequentialGroup()
                .addGap(28, 28, 28)
                .addComponent(jLabel15)
                .addContainerGap(499, Short.MAX_VALUE))
        );
        jPanel8Layout.setVerticalGroup(
            jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel8Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel15)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(pieChart2, javax.swing.GroupLayout.PREFERRED_SIZE, 294, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        jPanel7.add(jPanel8, java.awt.BorderLayout.LINE_START);

        jLabel16.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        jLabel16.setForeground(new java.awt.Color(51, 51, 51));
        jLabel16.setText("Order Count By Status");

        javax.swing.GroupLayout jPanel9Layout = new javax.swing.GroupLayout(jPanel9);
        jPanel9.setLayout(jPanel9Layout);
        jPanel9Layout.setHorizontalGroup(
            jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel9Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(pieChart3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(jPanel9Layout.createSequentialGroup()
                        .addComponent(jLabel16)
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
        );
        jPanel9Layout.setVerticalGroup(
            jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel9Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel16)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(pieChart3, javax.swing.GroupLayout.PREFERRED_SIZE, 294, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        jPanel7.add(jPanel9, java.awt.BorderLayout.CENTER);

        jPanel2.add(jPanel7);

        add(jPanel2, java.awt.BorderLayout.CENTER);
    }// </editor-fold>//GEN-END:initComponents


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel15;
    private javax.swing.JLabel jLabel16;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JPanel jPanel7;
    private javax.swing.JPanel jPanel8;
    private javax.swing.JPanel jPanel9;
    private javaswingdev.chart.PieChart pieChart2;
    private javaswingdev.chart.PieChart pieChart3;
    private lk.corevision.fruitshop.component.RoundPanel roundPanel1;
    private lk.corevision.fruitshop.component.RoundPanel roundPanel2;
    private lk.corevision.fruitshop.component.RoundPanel roundPanel3;
    private lk.corevision.fruitshop.component.RoundPanel roundPanel4;
    // End of variables declaration//GEN-END:variables

}
