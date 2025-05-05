package com.posture.app.controller;

import java.time.LocalDate;

import javafx.fxml.FXML;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Slider;
import javafx.scene.control.TextArea;
import javafx.scene.control.Label;
import com.posture.app.model.PostureRecord;
import com.posture.app.model.RecordManager;

public class InputController {

    @FXML
    private DatePicker datePicker;

    @FXML
    private Slider ratingSlider;

    @FXML
    private TextArea commentArea;

    @FXML
    private Label statusLabel;

    private RecordManager recordManager;

    @FXML
    public void initialize() {
        recordManager = RecordManager.getInstance();
        datePicker.setValue(LocalDate.now());
        ratingSlider.setMin(1);
        ratingSlider.setMax(5);
        ratingSlider.setValue(3);
        ratingSlider.setShowTickLabels(true);
        ratingSlider.setShowTickMarks(true);
        ratingSlider.setMajorTickUnit(1);
        ratingSlider.setMinorTickCount(0);
        ratingSlider.setSnapToTicks(true);
        statusLabel.setText("");
    }

    @FXML
    private void handleSave() {
        LocalDate date = datePicker.getValue();
        int rating = (int) ratingSlider.getValue();
        String comment = commentArea.getText().trim();

        PostureRecord record = new PostureRecord(date, rating, comment);
        recordManager.saveRecord(record);
        statusLabel.setText("保存しました！");
        statusLabel.setStyle("-fx-text-fill: green;");

        // 入力フィールドをクリア
        datePicker.setValue(LocalDate.now());
        ratingSlider.setValue(3);
        commentArea.clear();
    }

    @FXML
    private void handleReset() {
        ratingSlider.setValue(3);
        commentArea.clear();
        statusLabel.setText("");
    }
}
