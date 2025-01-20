package com.example.apidemo.db.service;

import com.example.apidemo.db.KeywordAndData;
import com.example.apidemo.db.repository.KeywordAndDataRepo;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class KeywordAndDataService {

    private final KeywordAndDataRepo keywordAndDataRepo;

    // Create or Update (Save)
    public KeywordAndData saveKeywordAndData(KeywordAndData keywordAndData) {
        return keywordAndDataRepo.save(keywordAndData);
    }

    // Read (Find all)
    public List<KeywordAndData> getAllKeywordAndData() {
        return keywordAndDataRepo.findAll();
    }

    // Read (Find by keyword)
    public List<KeywordAndData> getKeywordAndDataByKeyword(String keyword) {
        return keywordAndDataRepo.findByKeyword(keyword);
    }

    // Read (Find by URL)
    public List<KeywordAndData> getKeywordAndDataByUrl(String url) {
        return keywordAndDataRepo.findByUrl(url);
    }

    // Read (Find by ID)
    public KeywordAndData getKeywordAndDataById(Long id) {
        return keywordAndDataRepo.findById(id).orElse(null);
    }

    // Update (Update operation is done by save method)
    public KeywordAndData updateKeywordAndData(KeywordAndData keywordAndData) {
        if (keywordAndDataRepo.existsById(keywordAndData.getId())) {
            return keywordAndDataRepo.save(keywordAndData);
        } else {
            // Handle case when the entity does not exist
            return null;
        }
    }

    // Delete (by ID)
    public void deleteKeywordAndDataById(Long id) {
        keywordAndDataRepo.deleteById(id);
    }

    // Delete (all)
    public void deleteAllKeywordAndData() {
        keywordAndDataRepo.deleteAll();
    }
}
