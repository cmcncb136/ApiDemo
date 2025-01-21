package com.example.apidemo.db.repository;

import com.example.apidemo.db.Keyword;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface KeywordRepo extends JpaRepository<Keyword, String> {
    List<Keyword> findByLanguageCode(String languageCode);
}
