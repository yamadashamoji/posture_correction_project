package com.posture.app.test;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.time.LocalDate;
import java.util.List;
import com.posture.app.model.PostureRecord;
import com.posture.app.model.RecordManager;

public class RecordManagerTest {
    private RecordManager recordManager;

    @BeforeEach
    public void setUp() {
        recordManager = RecordManager.getInstance();
    }

    @Test
    public void testSaveAndLoadRecord() {
        LocalDate date = LocalDate.now();
        PostureRecord record = new PostureRecord(date, 4, "Test comment");
        recordManager.saveRecord(record);

        List<PostureRecord> records = recordManager.getAllRecords();
        assertFalse(records.isEmpty());
        assertEquals(record.getRating(), records.get(0).getRating());
        assertEquals(record.getDate(), records.get(0).getDate());
        assertEquals(record.getComment(), records.get(0).getComment());
    }

    @Test
    public void testGetRecordsByDate() {
        LocalDate today = LocalDate.now();
        PostureRecord record = new PostureRecord(today, 4, "Today's record");
        recordManager.saveRecord(record);

        List<PostureRecord> todayRecords = recordManager.getRecordsByDate(today);
        assertFalse(todayRecords.isEmpty());
        assertEquals(4, todayRecords.get(0).getRating());
        assertEquals(today, todayRecords.get(0).getDate());
    }
} 