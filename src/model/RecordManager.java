package model;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public class RecordManager {

    private static RecordManager instance;
    private final StorageService storageService;

    private RecordManager() {
        // 実装に応じてStorageServiceを切り替え
        this.storageService = new StorageService("data/posture.db"); // SQLite
        // 例: JSONに切り替えるなら → new StorageService("data/records.json");
    }

    public static RecordManager getInstance() {
        if (instance == null) {
            instance = new RecordManager();
        }
        return instance;
    }

    public boolean saveRecord(PostureRecord record) {
        return storageService.save(record);
    }

    public Optional<PostureRecord> loadRecord(LocalDate date) {
        return storageService.load(date);
    }

    public List<PostureRecord> loadAllRecords() {
        return storageService.loadAll();
    }

    public boolean deleteRecord(LocalDate date) {
        return storageService.delete(date);
    }

    public boolean updateRecord(PostureRecord updated) {
        return storageService.update(updated);
    }
}
