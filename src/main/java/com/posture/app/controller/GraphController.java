package com.posture.app.controller;

import java.time.LocalDate;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import javafx.fxml.FXML;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.DatePicker;
import com.posture.app.model.PostureRecord;
import com.posture.app.model.RecordManager;

public class GraphController {

    @FXML private LineChart<String, Number> ratingChart;
    @FXML private CategoryAxis xAxis;
    @FXML private NumberAxis yAxis;
    @FXML private DatePicker startDatePicker;
    @FXML private DatePicker endDatePicker;

    private RecordManager recordManager;

    @FXML
    public void initialize() {
        recordManager = RecordManager.getInstance();
        
        // 日付範囲の初期設定
        LocalDate today = LocalDate.now();
        startDatePicker.setValue(today.minusDays(30));
        endDatePicker.setValue(today);
        
        // チャートの初期化
        ratingChart.setTitle("姿勢評価の推移");
        ratingChart.getXAxis().setLabel("日付");
        ratingChart.getYAxis().setLabel("評価");
        
        updateChart();
    }

    @FXML
    public void handleUpdateChart() {
        updateChart();
    }

    private void updateChart() {
        LocalDate startDate = startDatePicker.getValue();
        LocalDate endDate = endDatePicker.getValue();
        
        if (startDate == null || endDate == null) return;
        
        List<PostureRecord> records = recordManager.getAllRecords().stream()
            .filter(record -> !record.getDate().isBefore(startDate) && !record.getDate().isAfter(endDate))
            .collect(Collectors.toList());
        
        XYChart.Series<String, Number> series = new XYChart.Series<>();
        series.setName("評価");
        
        for (PostureRecord record : records) {
            series.getData().add(new XYChart.Data<>(
                record.getDate().toString(),
                record.getRating()
            ));
        }
        
        ratingChart.getData().clear();
        ratingChart.getData().add(series);
    }
}
