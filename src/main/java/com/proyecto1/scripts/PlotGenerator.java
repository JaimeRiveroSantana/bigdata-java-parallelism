package com.proyecto1.scripts;

import org.knowm.xchart.BitmapEncoder;
import org.knowm.xchart.BitmapEncoder.BitmapFormat;
import org.knowm.xchart.CategoryChart;
import org.knowm.xchart.CategoryChartBuilder;
import org.knowm.xchart.SwingWrapper;

import javax.swing.*;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public class PlotGenerator {

    private static final String PLOTS_DIR = "src/main/resources/plots";

    public static void main(String[] args) {
        // Create output directory if it doesn't exist
        try {
            Files.createDirectories(Path.of(PLOTS_DIR));
        } catch (IOException e) {
            System.err.println(" Error creating 'plots' directory");
            return;
        }

        // Real benchmark results (in ms)
        String[] strategiesArr = {"Sequential", "ParallelStream", "ExecutorBased", "SemaphoreThrottled"};
        double[] timesArr = {40.0, 321.0, 98.0, 100.0};

        // Convert arrays to lists (required by XChart)
        List<String> strategies = new ArrayList<>();
        for (String s : strategiesArr) strategies.add(s);

        List<Double> times = new ArrayList<>();
        for (double t : timesArr) times.add(t);

        // Generate chart
        CategoryChart chart = new CategoryChartBuilder()
                .width(800).height(500)
                .title("Execution Time Comparison")
                .xAxisTitle("Strategy")
                .yAxisTitle("Time (ms)")
                .build();

        chart.addSeries("Time (ms)", strategies, times);
        chart.getStyler().setLegendVisible(false);
        chart.getStyler().setToolTipsEnabled(true);

        // Display chart in a window
        new SwingWrapper<>(chart).displayChart();

        // Save chart as PNG inside the project
        try {
            String path = PLOTS_DIR + "/execution_time.png";
            BitmapEncoder.saveBitmap(chart, path, BitmapFormat.PNG);
            JOptionPane.showMessageDialog(null,
                    " Chart saved to:\n" + path,
                    "Success", JOptionPane.INFORMATION_MESSAGE);
        } catch (IOException e) {
            JOptionPane.showMessageDialog(null,
                    " Failed to save image",
                    "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}