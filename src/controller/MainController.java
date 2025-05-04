package controller;

import java.util.Objects;

import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.TabPane;

public class MainController {

    @FXML
    private TabPane mainTabPane;

    private boolean isDarkTheme = false;

    @FXML
    public void initialize() {
        // 必要に応じて初期化処理
        System.out.println("MainController initialized");
    }

    @FXML
    private void handleToggleTheme() {
        // 現在のウィンドウ（Stage）を取得
        Scene scene = mainTabPane.getScene();
        if (scene == null) return;

        // CSSファイルを切り替え
        scene.getStylesheets().clear();

        if (isDarkTheme) {
            scene.getStylesheets().add(
                Objects.requireNonNull(getClass().getResource("/css/light-theme.css")).toExternalForm()
            );
        } else {
            scene.getStylesheets().add(
                Objects.requireNonNull(getClass().getResource("/css/dark-theme.css")).toExternalForm()
            );
        }

        isDarkTheme = !isDarkTheme;
    }
}
