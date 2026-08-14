package lk.educore.schoolms.charts;

import org.jfree.chart.*;
import org.jfree.chart.plot.*;
import org.jfree.data.category.*;
import org.jfree.data.xy.*;
import java.awt.*;
import javax.swing.*;

/**
 * Curve Line Chart Generator
 * Creates line charts for trend visualization in the school management system
 */
public class CurveLineChart {

    /**
     * Create a line chart from category dataset
     * @param title Chart title
     * @param xAxisLabel X-axis label
     * @param yAxisLabel Y-axis label
     * @param data Category dataset
     * @return JPanel containing the line chart
     */
    public static JPanel createLineChart(String title, String xAxisLabel, String yAxisLabel, CategoryDataset data) {
        try {
            // Create line chart
            JFreeChart chart = ChartFactory.createLineChart(
                title,
                xAxisLabel,
                yAxisLabel,
                data,
                PlotOrientation.VERTICAL,
                true,  // legend
                true,  // tooltips
                false  // URLs
            );

            // Customize chart appearance
            CategoryPlot plot = chart.getCategoryPlot();
            plot.setDomainGridlinePaint(Color.LIGHT_GRAY);
            plot.setRangeGridlinePaint(Color.LIGHT_GRAY);
            plot.setDomainGridlinesVisible(true);
            plot.setRangeGridlinesVisible(true);

            LineAndShapeRenderer renderer = (LineAndShapeRenderer) plot.getRenderer();
            renderer.setSeriesShapesVisible(0, true);
            renderer.setSeriesShapesFilled(0, true);
            renderer.setSeriesLinesVisible(0, true);
            renderer.setSeriesStroke(0, new BasicStroke(2.0f));

            // Create chart panel
            ChartPanel chartPanel = new ChartPanel(chart);
            chartPanel.setPreferredSize(new Dimension(600, 400));
            chartPanel.setMouseWheelEnabled(true);

            return chartPanel;

        } catch (Exception e) {
            e.printStackTrace();
            return new JPanel();
        }
    }

    /**
     * Create a line chart from XY dataset
     * @param title Chart title
     * @param xAxisLabel X-axis label
     * @param yAxisLabel Y-axis label
     * @param data XY dataset
     * @return JPanel containing the line chart
     */
    public static JPanel createXYLineChart(String title, String xAxisLabel, String yAxisLabel, XYDataset data) {
        try {
            // Create line chart
            JFreeChart chart = ChartFactory.createXYLineChart(
                title,
                xAxisLabel,
                yAxisLabel,
                data,
                PlotOrientation.VERTICAL,
                true,  // legend
                true,  // tooltips
                false  // URLs
            );

            // Customize chart appearance
            XYPlot plot = chart.getXYPlot();
            plot.setDomainGridlinePaint(Color.LIGHT_GRAY);
            plot.setRangeGridlinePaint(Color.LIGHT_GRAY);
            plot.setDomainGridlinesVisible(true);
            plot.setRangeGridlinesVisible(true);

            XYLineAndShapeRenderer renderer = (XYLineAndShapeRenderer) plot.getRenderer();
            renderer.setSeriesShapesVisible(0, true);
            renderer.setSeriesShapesFilled(0, true);
            renderer.setSeriesLinesVisible(0, true);
            renderer.setSeriesStroke(0, new BasicStroke(2.0f));

            // Create chart panel
            ChartPanel chartPanel = new ChartPanel(chart);
            chartPanel.setPreferredSize(new Dimension(600, 400));
            chartPanel.setMouseWheelEnabled(true);

            return chartPanel;

        } catch (Exception e) {
            e.printStackTrace();
            return new JPanel();
        }
    }

    /**
     * Create a curved line chart (spline)
     * @param title Chart title
     * @param xAxisLabel X-axis label
     * @param yAxisLabel Y-axis label
     * @param data Category dataset
     * @return JPanel containing the curved line chart
     */
    public static JPanel createCurvedLineChart(String title, String xAxisLabel, String yAxisLabel, CategoryDataset data) {
        try {
            // Create line chart
            JFreeChart chart = ChartFactory.createLineChart(
                title,
                xAxisLabel,
                yAxisLabel,
                data,
                PlotOrientation.VERTICAL,
                true,  // legend
                true,  // tooltips
                false  // URLs
            );

            // Customize chart appearance
            CategoryPlot plot = chart.getCategoryPlot();
            plot.setDomainGridlinePaint(Color.LIGHT_GRAY);
            plot.setRangeGridlinePaint(Color.LIGHT_GRAY);
            plot.setDomainGridlinesVisible(true);
            plot.setRangeGridlinesVisible(true);

            LineAndShapeRenderer renderer = (LineAndShapeRenderer) plot.getRenderer();
            renderer.setSeriesShapesVisible(0, true);
            renderer.setSeriesShapesFilled(0, true);
            renderer.setSeriesLinesVisible(0, true);
            renderer.setSeriesStroke(0, new BasicStroke(2.5f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));

            // Create chart panel
            ChartPanel chartPanel = new ChartPanel(chart);
            chartPanel.setPreferredSize(new Dimension(600, 400));
            chartPanel.setMouseWheelEnabled(true);

            return chartPanel;

        } catch (Exception e) {
            e.printStackTrace();
            return new JPanel();
        }
    }

    /**
     * Create a 3D line chart
     * @param title Chart title
     * @param xAxisLabel X-axis label
     * @param yAxisLabel Y-axis label
     * @param data Category dataset
     * @return JPanel containing the 3D line chart
     */
    public static JPanel create3DLineChart(String title, String xAxisLabel, String yAxisLabel, CategoryDataset data) {
        try {
            // Create 3D line chart
            JFreeChart chart = ChartFactory.createLineChart3D(
                title,
                xAxisLabel,
                yAxisLabel,
                data,
                PlotOrientation.VERTICAL,
                true,  // legend
                true,  // tooltips
                false  // URLs
            );

            // Customize chart appearance
            CategoryPlot plot = chart.getCategoryPlot();
            plot.setDomainGridlinePaint(Color.LIGHT_GRAY);
            plot.setRangeGridlinePaint(Color.LIGHT_GRAY);
            plot.setDomainGridlinesVisible(true);
            plot.setRangeGridlinesVisible(true);
            plot.setForegroundAlpha(0.7f);

            // Create chart panel
            ChartPanel chartPanel = new ChartPanel(chart);
            chartPanel.setPreferredSize(new Dimension(600, 400));
            chartPanel.setMouseWheelEnabled(true);

            return chartPanel;

        } catch (Exception e) {
            e.printStackTrace();
            return new JPanel();
        }
    }

    /**
     * Create an area chart
     * @param title Chart title
     * @param xAxisLabel X-axis label
     * @param yAxisLabel Y-axis label
     * @param data Category dataset
     * @return JPanel containing the area chart
     */
    public static JPanel createAreaChart(String title, String xAxisLabel, String yAxisLabel, CategoryDataset data) {
        try {
            // Create area chart
            JFreeChart chart = ChartFactory.createAreaChart(
                title,
                xAxisLabel,
                yAxisLabel,
                data,
                PlotOrientation.VERTICAL,
                true,  // legend
                true,  // tooltips
                false  // URLs
            );

            // Customize chart appearance
            CategoryPlot plot = chart.getCategoryPlot();
            plot.setDomainGridlinePaint(Color.LIGHT_GRAY);
            plot.setRangeGridlinePaint(Color.LIGHT_GRAY);
            plot.setDomainGridlinesVisible(true);
            plot.setRangeGridlinesVisible(true);

            // Create chart panel
            ChartPanel chartPanel = new ChartPanel(chart);
            chartPanel.setPreferredSize(new Dimension(600, 400));
            chartPanel.setMouseWheelEnabled(true);

            return chartPanel;

        } catch (Exception e) {
            e.printStackTrace();
            return new JPanel();
        }
    }

    /**
     * Create a line chart for attendance trends
     * @param monthNames Array of month names
     * @param attendanceRates Array of attendance rates
     * @return JPanel containing the attendance line chart
     */
    public static JPanel createAttendanceLineChart(String[] monthNames, double[] attendanceRates) {
        try {
            // Create dataset
            DefaultCategoryDataset dataset = new DefaultCategoryDataset();
            for (int i = 0; i < monthNames.length && i < attendanceRates.length; i++) {
                dataset.addValue(attendanceRates[i], "Attendance Rate", monthNames[i]);
            }

            // Create chart
            JFreeChart chart = ChartFactory.createLineChart(
                "Attendance Trends",
                "Month",
                "Attendance Rate (%)",
                dataset,
                PlotOrientation.VERTICAL,
                true,  // legend
                true,  // tooltips
                false  // URLs
            );

            // Customize chart appearance
            CategoryPlot plot = chart.getCategoryPlot();
            plot.setDomainGridlinePaint(Color.LIGHT_GRAY);
            plot.setRangeGridlinePaint(Color.LIGHT_GRAY);
            plot.setDomainGridlinesVisible(true);
            plot.setRangeGridlinesVisible(true);

            LineAndShapeRenderer renderer = (LineAndShapeRenderer) plot.getRenderer();
            renderer.setSeriesShapesVisible(0, true);
            renderer.setSeriesShapesFilled(0, true);
            renderer.setSeriesLinesVisible(0, true);
            renderer.setSeriesStroke(0, new BasicStroke(2.5f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
            renderer.setSeriesPaint(0, new Color(33, 150, 243));  // Blue

            // Create chart panel
            ChartPanel chartPanel = new ChartPanel(chart);
            chartPanel.setPreferredSize(new Dimension(600, 400));
            chartPanel.setMouseWheelEnabled(true);

            return chartPanel;

        } catch (Exception e) {
            e.printStackTrace();
            return new JPanel();
        }
    }

    /**
     * Create a line chart for exam trends
     * @param examNames Array of exam names
     * @param averageScores Array of average scores
     * @return JPanel containing the exam line chart
     */
    public static JPanel createExamLineChart(String[] examNames, double[] averageScores) {
        try {
            // Create dataset
            DefaultCategoryDataset dataset = new DefaultCategoryDataset();
            for (int i = 0; i < examNames.length && i < averageScores.length; i++) {
                dataset.addValue(averageScores[i], "Average Score", examNames[i]);
            }

            // Create chart
            JFreeChart chart = ChartFactory.createLineChart(
                "Exam Performance Trends",
                "Exam",
                "Average Score",
                dataset,
                PlotOrientation.VERTICAL,
                true,  // legend
                true,  // tooltips
                false  // URLs
            );

            // Customize chart appearance
            CategoryPlot plot = chart.getCategoryPlot();
            plot.setDomainGridlinePaint(Color.LIGHT_GRAY);
            plot.setRangeGridlinePaint(Color.LIGHT_GRAY);
            plot.setDomainGridlinesVisible(true);
            plot.setRangeGridlinesVisible(true);

            LineAndShapeRenderer renderer = (LineAndShapeRenderer) plot.getRenderer();
            renderer.setSeriesShapesVisible(0, true);
            renderer.setSeriesShapesFilled(0, true);
            renderer.setSeriesLinesVisible(0, true);
            renderer.setSeriesStroke(0, new BasicStroke(2.5f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
            renderer.setSeriesPaint(0, new Color(76, 175, 80));  // Green

            // Create chart panel
            ChartPanel chartPanel = new ChartPanel(chart);
            chartPanel.setPreferredSize(new Dimension(600, 400));
            chartPanel.setMouseWheelEnabled(true);

            return chartPanel;

        } catch (Exception e) {
            e.printStackTrace();
            return new JPanel();
        }
    }

    /**
     * Create a multi-line chart for comparing multiple data series
     * @param title Chart title
     * @param xAxisLabel X-axis label
     * @param yAxisLabel Y-axis label
     * @param data Category dataset with multiple series
     * @return JPanel containing the multi-line chart
     */
    public static JPanel createMultiLineChart(String title, String xAxisLabel, String yAxisLabel, CategoryDataset data) {
        try {
            // Create line chart
            JFreeChart chart = ChartFactory.createLineChart(
                title,
                xAxisLabel,
                yAxisLabel,
                data,
                PlotOrientation.VERTICAL,
                true,  // legend
                true,  // tooltips
                false  // URLs
            );

            // Customize chart appearance
            CategoryPlot plot = chart.getCategoryPlot();
            plot.setDomainGridlinePaint(Color.LIGHT_GRAY);
            plot.setRangeGridlinePaint(Color.LIGHT_GRAY);
            plot.setDomainGridlinesVisible(true);
            plot.setRangeGridlinesVisible(true);

            LineAndShapeRenderer renderer = (LineAndShapeRenderer) plot.getRenderer();
            
            // Customize each series
            for (int i = 0; i < data.getRowCount(); i++) {
                renderer.setSeriesShapesVisible(i, true);
                renderer.setSeriesShapesFilled(i, true);
                renderer.setSeriesLinesVisible(i, true);
                renderer.setSeriesStroke(i, new BasicStroke(2.5f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
                
                // Set different colors for each series
                Color[] colors = {
                    new Color(33, 150, 243),   // Blue
                    new Color(76, 175, 80),    // Green
                    new Color(255, 193, 7),    // Yellow
                    new Color(244, 67, 54),    // Red
                    new Color(156, 39, 176)    // Purple
                };
                if (i < colors.length) {
                    renderer.setSeriesPaint(i, colors[i]);
                }
            }

            // Create chart panel
            ChartPanel chartPanel = new ChartPanel(chart);
            chartPanel.setPreferredSize(new Dimension(600, 400));
            chartPanel.setMouseWheelEnabled(true);

            return chartPanel;

        } catch (Exception e) {
            e.printStackTrace();
            return new JPanel();
        }
    }

    /**
     * Create a line chart with custom colors
     * @param title Chart title
     * @param xAxisLabel X-axis label
     * @param yAxisLabel Y-axis label
     * @param data Category dataset
     * @param colors Array of colors for series
     * @return JPanel containing the line chart
     */
    public static JPanel createLineChartWithColors(String title, String xAxisLabel, String yAxisLabel, 
                                                   CategoryDataset data, Color[] colors) {
        try {
            // Create line chart
            JFreeChart chart = ChartFactory.createLineChart(
                title,
                xAxisLabel,
                yAxisLabel,
                data,
                PlotOrientation.VERTICAL,
                true,  // legend
                true,  // tooltips
                false  // URLs
            );

            // Customize chart appearance
            CategoryPlot plot = chart.getCategoryPlot();
            plot.setDomainGridlinePaint(Color.LIGHT_GRAY);
            plot.setRangeGridlinePaint(Color.LIGHT_GRAY);
            plot.setDomainGridlinesVisible(true);
            plot.setRangeGridlinesVisible(true);

            LineAndShapeRenderer renderer = (LineAndShapeRenderer) plot.getRenderer();
            
            // Set custom colors
            for (int i = 0; i < data.getRowCount() && i < colors.length; i++) {
                renderer.setSeriesShapesVisible(i, true);
                renderer.setSeriesShapesFilled(i, true);
                renderer.setSeriesLinesVisible(i, true);
                renderer.setSeriesStroke(i, new BasicStroke(2.5f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
                renderer.setSeriesPaint(i, colors[i]);
            }

            // Create chart panel
            ChartPanel chartPanel = new ChartPanel(chart);
            chartPanel.setPreferredSize(new Dimension(600, 400));
            chartPanel.setMouseWheelEnabled(true);

            return chartPanel;

        } catch (Exception e) {
            e.printStackTrace();
            return new JPanel();
        }
    }
}