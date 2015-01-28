package com.rwteam.gscrum.view;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.category.CategoryDataset;
import org.jfree.data.category.DefaultCategoryDataset;

import javax.swing.*;
import java.awt.*;

/**
 * @author mateusz.rutski@sagiton.pl
 */
public class TimelinePanel extends JPanel {


    public TimelinePanel() {
        super();

        JFreeChart jFreeChart = ChartFactory.createLineChart(
                "GScrum Timeline",
                "Date",
                "User stories",
                createDataset(),
                PlotOrientation.VERTICAL,
                false, false, false
        );

        ChartPanel chartPanel = new ChartPanel(jFreeChart);
        chartPanel.setPreferredSize(new Dimension(500, 100));
    }

    private CategoryDataset createDataset() {

        DefaultCategoryDataset dataset = new DefaultCategoryDataset();

        dataset.addValue(1, "Milestone 1", "10-10-2014");

        return dataset;
    }


}
