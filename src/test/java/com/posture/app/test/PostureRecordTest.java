package com.posture.app.test;

import com.posture.app.model.PostureRecord;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import java.time.LocalDate;

public class PostureRecordTest {
    
    @Test
    public void testDefaultConstructor() {
        PostureRecord record = new PostureRecord();
        assertNotNull(record.getDate());
        assertEquals(LocalDate.now(), record.getDate());
    }
    
    @Test
    public void testParameterizedConstructor() {
        LocalDate date = LocalDate.now();
        int rating = 4;
        String comment = "Good posture";
        PostureRecord record = new PostureRecord(date, rating, comment);
        
        assertEquals(date, record.getDate());
        assertEquals(rating, record.getRating());
        assertEquals(comment, record.getComment());
    }
    
    @Test
    public void testSettersAndGetters() {
        PostureRecord record = new PostureRecord();
        LocalDate date = LocalDate.now();
        int rating = 5;
        String comment = "Excellent posture";

        record.setDate(date);
        record.setRating(rating);
        record.setComment(comment);

        assertEquals(date, record.getDate());
        assertEquals(rating, record.getRating());
        assertEquals(comment, record.getComment());
    }
    
    @Test
    public void testSetAndGetTitle() {
        PostureRecord record = new PostureRecord();
        String title = "テスト記録";
        record.setTitle(title);
        assertEquals(title, record.getTitle());
    }
    
    @Test
    public void testSetAndGetDescription() {
        PostureRecord record = new PostureRecord();
        String description = "テストの説明";
        record.setDescription(description);
        assertEquals(description, record.getDescription());
    }
    
    @Test
    public void testSetAndGetDate() {
        PostureRecord record = new PostureRecord();
        LocalDate date = LocalDate.of(2024, 3, 15);
        record.setDate(date);
        assertEquals(date, record.getDate());
    }

    @Test
    public void testRatingBounds() {
        PostureRecord record = new PostureRecord();
        int rating = 5;
        record.setRating(rating);
        assertEquals(rating, record.getRating());
    }
} 