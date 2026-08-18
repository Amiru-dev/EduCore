package lk.educore.schoolms.charts;

import org.jfree.chart.*;
import org.jfree.chart.plot.*;
import org.jfree.data.general.*;
import org.jfree.data.category.*;
import java.awt.*;
import javax.swing.*;

/**
 * Pie Chart Generator
 * Creates pie charts for data visualization in the school management system
 */
public class PieChart {

    /**
     * Create a pie chart from category data
     * @param title Chart title
     * @param data Category dataset
     * @return JPanel containing the pie chart
     */
    @SuppressWarnings("unchecked")
    public static JPanel createPieChart(String title, CategoryDataset data) {
        try {
            // Create pie chart from category dataset
            JFreeChart chart = ChartFactory.createPieChart(
                title,
                DatasetUtils.createPieDatasetForRow(data, 0),
                true,  // legend
                true,  // tooltips
                false  // URLs
            );

            // Customize chart appearance
            PiePlot<String> plot = (PiePlot<String>) chart.getPlot();
            plot.setLabelFont(new Font("SansSerif", Font.PLAIN, 12));
            plot.setLabelGap(0.02);
            plot.setOutlineStroke(new BasicStroke(1.0f));
            plot.setOutlinePaint(Color.GRAY);

            // Create chart panel
            ChartPanel chartPanel = new ChartPanel(chart);
            chartPanel.setPreferredSize(new Dimension(500, 300));
            chartPanel.setMouseWheelEnabled(true);

            return chartPanel;

        } catch (Exception e) {
            e.printStackTrace();
            return new JPanel();
        }
    }

    /**
     * Create a pie chart from pie dataset
     * @param title Chart title
     * @param data Pie dataset
     * @return JPanel containing the pie chart
     */
    @SuppressWarnings("unchecked")
    public static JPanel createPieChart(String title, PieDataset<String> data) {
        try {
            // Create pie chart
            JFreeChart chart = ChartFactory.createPieChart(
                title,
                data,
                true,  // legend
                true,  // tooltips
                false  // URLs
            );

            // Customize chart appearance
            PiePlot<String> plot = (PiePlot<String>) chart.getPlot();
            plot.setLabelFont(new Font("SansSerif", Font.PLAIN, 12));
            plot.setLabelGap(0.02);
            plot.setOutlineStroke(new BasicStroke(1.0f));
            plot.setOutlinePaint(Color.GRAY);

            // Create chart panel
            ChartPanel chartPanel = new ChartPanel(chart);
            chartPanel.setPreferredSize(new Dimension(500, 300));
            chartPanel.setMouseWheelEnabled(true);

            return chartPanel;

        } catch (Exception e) {
            e.printStackTrace();
            return new JPanel();
        }
    }

    /**
     * Create a pie chart with custom colors
     * @param title Chart title
     * @param data Pie dataset
     * @param colors Array of colors for pie sections
     * @return JPanel containing the pie chart
     */
    @SuppressWarnings("unchecked")
    public static JPanel createPieChartWithColors(String title, PieDataset<String> data, Color[] colors) {
        try {
            // Create pie chart
            JFreeChart chart = ChartFactory.createPieChart(
                title,
                data,
                true,  // legend
                true,  // tooltips
                false  // URLs
            );

            // Customize chart appearance
            PiePlot<String> plot = (PiePlot<String>) chart.getPlot();
            plot.setLabelFont(new Font("SansSerif", Font.PLAIN, 12));
            plot.setLabelGap(0.02);
            plot.setOutlineStroke(new BasicStroke(1.0f));
            plot.setOutlinePaint(Color.GRAY);

            // Set custom colors
            for (int i = 0; i < data.getItemCount() && i < colors.length; i++) {
                plot.setSectionPaint(data.getKey(i), colors[i]);
            }

            // Create chart panel
            ChartPanel chartPanel = new ChartPanel(chart);
            chartPanel.setPreferredSize(new Dimension(500, 300));
            chartPanel.setMouseWheelEnabled(true);

            return chartPanel;

        } catch (Exception e) {
            e.printStackTrace();
            return new JPanel();
        }
    }

    /**
     * Create a 3D pie chart
     * @param title Chart title
     * @param data Pie dataset
     * @return JPanel containing the 3D pie chart
     */
    @SuppressWarnings({"unchecked", "deprecation"})
    public static JPanel create3DPieChart(String title, PieDataset<String> data) {
        try {
            // Create 3D pie chart
            JFreeChart chart = ChartFactory.createPieChart3D(
                title,
                data,
                true,  // legend
                true,  // tooltips
                false  // URLs
            );

            // Customize chart appearance
            PiePlot<String> plot = (PiePlot<String>) chart.getPlot();
            plot.setLabelFont(new Font("SansSerif", Font.PLAIN, 12));
            plot.setLabelGap(0.02);
            plot.setOutlineStroke(new BasicStroke(1.0f));
            plot.setOutlinePaint(Color.GRAY);
            plot.setForegroundAlpha(0.7f);

            // Create chart panel
            ChartPanel chartPanel = new ChartPanel(chart);
            chartPanel.setPreferredSize(new Dimension(500, 300));
            chartPanel.setMouseWheelEnabled(true);

            return chartPanel;

        } catch (Exception e) {
            e.printStackTrace();
            return new JPanel();
        }
    }

    /**
     * Create a donut chart (ring chart)
     * @param title Chart title
     * @param data Pie dataset
     * @param innerRadius Inner radius as percentage (0.0 to 1.0)
     * @return JPanel containing the donut chart
     */
    @SuppressWarnings("unchecked")
    public static JPanel createDonutChart(String title, PieDataset<String> data, double innerRadius) {
        try {
            // Create pie chart
            JFreeChart chart = ChartFactory.createPieChart(
                title,
                data,
                true,  // legend
                true,  // tooltips
                false  // URLs
            );

            // Customize chart appearance as donut
            PiePlot<String> plot = (PiePlot<String>) chart.getPlot();
            plot.setLabelFont(new Font("SansSerif", Font.PLAIN, 12));
            plot.setLabelGap(0.02);
            plot.setOutlineStroke(new BasicStroke(1.0f));
            plot.setOutlinePaint(Color.GRAY);
            plot.setInteriorGap(innerRadius);

            // Create chart panel
            ChartPanel chartPanel = new ChartPanel(chart);
            chartPanel.setPreferredSize(new Dimension(500, 300));
            chartPanel.setMouseWheelEnabled(true);

            return chartPanel;

        } catch (Exception e) {
            e.printStackTrace();
            return new JPanel();
        }
    }

    /**
     * Create a pie chart for fee collection data
     * @param paidAmount Amount paid
     * @param pendingAmount Amount pending
     * @param overdueAmount Amount overdue
     * @return JPanel containing the fee pie chart
     */
    @SuppressWarnings("unchecked")
    public static JPanel createFeePieChart(double paidAmount, double pendingAmount, double overdueAmount) {
        try {
            // Create dataset
            DefaultPieDataset<String> dataset = new DefaultPieDataset<>();
            dataset.setValue("Paid", paidAmount);
            dataset.setValue("Pending", pendingAmount);
            dataset.setValue("Overdue", overdueAmount);

            // Create chart
            JFreeChart chart = ChartFactory.createPieChart(
                "Fee Collection Status",
                dataset,
                true,  // legend
                true,  // tooltips
                false  // URLs
            );

            // Customize chart appearance
            PiePlot<String> plot = (PiePlot<String>) chart.getPlot();
            plot.setLabelFont(new Font("SansSerif", Font.PLAIN, 12));
            plot.setLabelGap(0.02);
            plot.setOutlineStroke(new BasicStroke(1.0f));
            plot.setOutlinePaint(Color.GRAY);

            // Set colors
            plot.setSectionPaint("Paid", new Color(76, 175, 80));  // Green
            plot.setSectionPaint("Pending", new Color(255, 193, 7));  // Yellow
            plot.setSectionPaint("Overdue", new Color(244, 67, 54));  // Red

            // Create chart panel
            ChartPanel chartPanel = new ChartPanel(chart);
            chartPanel.setPreferredSize(new Dimension(500, 300));
            chartPanel.setMouseWheelEnabled(true);

            return chartPanel;

        } catch (Exception e) {
            e.printStackTrace();
            return new JPanel();
        }
    }

    /**
     * Create a pie chart for academic performance
     * @param aCount Number of A grades
     * @param bCount Number of B grades
     * @param cCount Number of C grades
     * @param otherCount Number of other grades
     * @return JPanel containing the performance pie chart
     */
    @SuppressWarnings("unchecked")
    public static JPanel createPerformancePieChart(int aCount, int bCount, int cCount, int otherCount) {
        try {
            // Create dataset
            DefaultPieDataset<String> dataset = new DefaultPieDataset<>();
            dataset.setValue("A Grade", aCount);
            dataset.setValue("B Grade", bCount);
            dataset.setValue("C Grade", cCount);
            dataset.setValue("Other", otherCount);

            // Create chart
            JFreeChart chart = ChartFactory.createPieChart(
                "Grade Distribution",
                dataset,
                true,  // legend
                true,  // tooltips
                false  // URLs
            );

            // Customize chart appearance
            PiePlot<String> plot = (PiePlot<String>) chart.getPlot();
            plot.setLabelFont(new Font("SansSerif", Font.PLAIN, 12));
            plot.setLabelGap(0.02);
            plot.setOutlineStroke(new BasicStroke(1.0f));
            plot.setOutlinePaint(Color.GRAY);

            // Set colors
            plot.setSectionPaint("A Grade", new Color(76, 175, 80));  // Green
            plot.setSectionPaint("B Grade", new Color(33, 150, 243));  // Blue
            plot.setSectionPaint("C Grade", new Color(255, 193, 7));  // Yellow
            plot.setSectionPaint("Other", new Color(158, 158, 158));  // Gray

            // Create chart panel
            ChartPanel chartPanel = new ChartPanel(chart);
            chartPanel.setPreferredSize(new Dimension(500, 300));
            chartPanel.setMouseWheelEnabled(true);

            return chartPanel;

        } catch (Exception e) {
            e.printStackTrace();
            return new JPanel();
        }
    }

    /**
     * Create a pie chart for finance data
     * @param income Total income
     * @param expense Total expense
     * @return JPanel containing the finance pie chart
     */
    @SuppressWarnings("unchecked")
    public static JPanel createFinancePieChart(double income, double expense) {
        try {
            // Create dataset
            DefaultPieDataset<String> dataset = new DefaultPieDataset<>();
            dataset.setValue("Income", income);
            dataset.setValue("Expense", expense);

            // Create chart
            JFreeChart chart = ChartFactory.createPieChart(
                "Financial Overview",
                dataset,
                true,  // legend
                true,  // tooltips
                false  // URLs
            );

            // Customize chart appearance
            PiePlot<String> plot = (PiePlot<String>) chart.getPlot();
            plot.setLabelFont(new Font("SansSerif", Font.PLAIN, 12));
            plot.setLabelGap(0.02);
            plot.setOutlineStroke(new BasicStroke(1.0f));
            plot.setOutlinePaint(Color.GRAY);

            // Set colors
            plot.setSectionPaint("Income", new Color(76, 175, 80));  // Green
            plot.setSectionPaint("Expense", new Color(244, 67, 54));  // Red

            // Create chart panel
            ChartPanel chartPanel = new ChartPanel(chart);
            chartPanel.setPreferredSize(new Dimension(500, 300));
            chartPanel.setMouseWheelEnabled(true);

            return chartPanel;

        } catch (Exception e) {
            e.printStackTrace();
            return new JPanel();
        }
    }
}