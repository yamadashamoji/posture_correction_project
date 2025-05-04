package controller;

import java.awt.Button;
import java.awt.Color;
import java.awt.Label;
import java.awt.Rectangle;
import java.time.LocalDate;
import java.time.YearMonth;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.w3c.dom.Text;

import javafx.fxml.FXML;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import model.PostureRecord;
import model.RecordManager;

public class CalendarController {

    @FXML private Button prevMonthButton;
    @FXML private Button nextMonthButton;
    @FXML private Label monthLabel;
    @FXML private GridPane calendarGrid;

    private YearMonth currentYearMonth;
    private Map<LocalDate, PostureRecord> recordMap;

    @FXML
    public void initialize() {
        currentYearMonth = YearMonth.now();
        loadRecords();
        updateCalendar();
    }

    private void loadRecords() {
        List<PostureRecord> records = RecordManager.getInstance().loadAllRecords();
        recordMap = records.stream().collect(Collectors.toMap(PostureRecord::getDate, r -> r));
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
            // TODO: 詳細表示や編集画面に遷移させたい場合
            System.out.println("Clicked date: " + date);
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
}
