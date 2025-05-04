package model;

import java.time.LocalDate;

public class PostureRecord {
    private LocalDate date;
    private int rating;         // 1〜5 の姿勢評価
    private String comment;     // コメント（任意）

    public PostureRecord(LocalDate date, int rating, String comment) {
        this.date = date;
        this.rating = rating;
        this.comment = comment;
    }

    // Getter / Setter
    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public int getRating() {
        return rating;
    }

    public void setRating(int rating) {
        this.rating = rating;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    // デバッグやログ用に
    @Override
    public String toString() {
        return "PostureRecord{" +
                "date=" + date +
                ", rating=" + rating +
                ", comment='" + comment + '\'' +
                '}';
    }
}
