package com.example.apidemo.db.repository;


import com.example.apidemo.db.Keyword;
import com.example.apidemo.db.KeywordAndData;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface KeywordAndDataRepo extends JpaRepository<KeywordAndData, Long> {
    List<KeywordAndData> findByKeyword(String keyword);
    List<KeywordAndData> findByUrl(String url);

}
