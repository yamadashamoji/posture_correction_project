package com.posture.app.controller;

import java.time.LocalDate;
import java.time.YearMonth;
import java.util.List;
import java.util.Map;
import java.util.HashMap;
import java.util.stream.Collectors;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import javafx.scene.control.DatePicker;
import com.posture.app.model.PostureRecord;
import com.posture.app.model.RecordManager;
import com.posture.app.view.PostureRecordListCell;

public class CalendarController {

    @FXML private Button prevMonthButton;
    @FXML private Button nextMonthButton;
    @FXML private Label monthLabel;
    @FXML private GridPane calendarGrid;
    @FXML private DatePicker datePicker;
    @FXML private ListView<PostureRecord> recordListView;

    private YearMonth currentYearMonth;
    private Map<LocalDate, PostureRecord> recordMap;
    private RecordManager recordManager;

    @FXML
    public void initialize() {
        recordManager = RecordManager.getInstance();
        currentYearMonth = YearMonth.now();
        recordMap = new HashMap<>();
        loadRecords();
        updateCalendar();
        setupDatePicker();
        setupRecordListView();
    }

    private void loadRecords() {
        List<PostureRecord> records = recordManager.getAllRecords();
        recordMap = records.stream()
                .collect(Collectors.toMap(
                    PostureRecord::getDate,
                    record -> record,
                    (existing, replacement) -> existing  // 重複する場合は既存のレコードを保持
                ));
        updateRecordList(LocalDate.now());  // 現在の日付のレコードを表示
    }

    private void updateCalendar() {
        calendarGrid.getChildren().clear();
        monthLabel.setText(currentYearMonth.getYear() + "年 " + currentYearMonth.getMonthValue() + "月");

        LocalDate firstDay = currentYearMonth.atDay(1);
        int dayOfWeek = firstDay.getDayOfWeek().getValue() % 7; // Javaの日曜=7を0に
        int daysInMonth = currentYearMonth.lengthOfMonth();

        for (int day = 1; day <= daysInMonth; day++) {
            LocalDate date = currentYearMonth.atDay(day);
            StackPane cell = createDayCell(date);
            int col = (dayOfWeek + day - 1) % 7;
            int row = (dayOfWeek + day - 1) / 7;
            calendarGrid.add(cell, col, row);
        }
    }

    private StackPane createDayCell(LocalDate date) {
        Rectangle bg = new Rectangle(60, 60);
        bg.setFill(Color.LIGHTGRAY);
        bg.setStroke(Color.GRAY);

        Text dayText = new Text(String.valueOf(date.getDayOfMonth()));

        PostureRecord record = recordMap.get(date);
        if (record != null) {
            switch (record.getRating()) {
                case 5 -> bg.setFill(Color.LIGHTGREEN);
                case 4 -> bg.setFill(Color.LIGHTBLUE);
                case 3 -> bg.setFill(Color.KHAKI);
                case 2 -> bg.setFill(Color.SALMON);
                case 1 -> bg.setFill(Color.TOMATO);
                default -> bg.setFill(Color.LIGHTGRAY);
            }
        }

        StackPane cell = new StackPane(bg, dayText);
        cell.setOnMouseClicked(e -> {
            System.out.println("Clicked date: " + date);
            updateRecordList(date);
        });

        return cell;
    }

    @FXML
    public void handlePrevMonth() {
        currentYearMonth = currentYearMonth.minusMonths(1);
        updateCalendar();
    }

    @FXML
    public void handleNextMonth() {
        currentYearMonth = currentYearMonth.plusMonths(1);
        updateCalendar();
    }

    private void setupDatePicker() {
        datePicker.setOnAction(e -> {
            LocalDate selectedDate = datePicker.getValue();
            if (selectedDate != null) {
                updateRecordList(selectedDate);
            }
        });
    }

    private void setupRecordListView() {
        recordListView.setCellFactory(lv -> new PostureRecordListCell());
    }

    private void updateRecordList(LocalDate date) {
        List<PostureRecord> records = recordManager.getRecordsByDate(date);
        recordListView.getItems().setAll(records);
    }
}
