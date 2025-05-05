package com.posture.app.view;

import javafx.scene.control.ListCell;
import com.posture.app.model.PostureRecord;

public class PostureRecordListCell extends ListCell<PostureRecord> {
    @Override
    protected void updateItem(PostureRecord record, boolean empty) {
        super.updateItem(record, empty);

        if (empty || record == null) {
            setText(null);
        } else {
            setText(String.format("評価: %d/5, コメント: %s", record.getRating(), record.getComment()));
        }
    }
} 