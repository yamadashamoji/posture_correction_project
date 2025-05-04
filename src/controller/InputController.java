package controller;

import java.awt.Label;
import java.awt.TextArea;
import java.time.LocalDate;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import model.PostureRecord;
import model.RecordManager;

public class InputController {

    @FXML
    private Slider ratingSlider;

    @FXML
    private TextArea commentArea;

    @FXML
    private Label statusLabel;

    private final RecordManager recordManager = RecordManager.getInstance();

    @FXML
    public void initialize() {
        // 初期化処理（必要であれば）
        statusLabel.setText("");
    }

    @FXML
    private void handleSave() {
        int rating = (int) ratingSlider.getValue();
        String comment = commentArea.getText().trim();
        LocalDate date = LocalDate.now(); // 今日の日付で保存

        PostureRecord record = new PostureRecord(date, rating, comment);

        boolean success = recordManager.saveRecord(record);

        if (success) {
            statusLabel.setText("保存しました！");
            statusLabel.setStyle("-fx-text-fill: green;");
        } else {
            statusLabel.setText("保存に失敗しました。");
            statusLabel.setStyle("-fx-text-fill: red;");
        }
    }

    @FXML
    private void handleReset() {
        ratingSlider.setValue(3);
        commentArea.clear();
        statusLabel.setText("");
    }
}
