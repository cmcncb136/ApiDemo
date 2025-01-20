package com.example.apidemo.db.service;

import com.example.apidemo.db.Keyword;
import com.example.apidemo.db.repository.KeywordRepo;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class KeywordService {
    private final KeywordRepo keywordRepo;


    // Create or Update (Save)
    public Keyword saveKeyword(Keyword keyword) {
        return keywordRepo.save(keyword);
    }

    // Read (Find all)
    public List<Keyword> getAllKeywords() {
        return keywordRepo.findAll();
    }

    // Read (Find by ID)
    public Keyword getKeywordById(String id) {
        return keywordRepo.findById(id).orElse(null);
    }

    // Update (Update operation is done by save method)
    public Keyword updateKeyword(Keyword keyword) {
        if (keywordRepo.existsById(keyword.getKeyword())) {
            return keywordRepo.save(keyword);
        } else {
            // Handle case when the entity does not exist
            return null;
        }
    }

    // Delete (by ID)
    public void deleteKeywordById(String id) {
        keywordRepo.deleteById(id);
    }

    // Delete (all)
    public void deleteAllKeywords() {
        keywordRepo.deleteAll();
    }
}
