package com.example.apidemo.db;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@NoArgsConstructor
@Getter
@Setter
public class KeywordAndData {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private String keyword;
    private String url;


    @Builder
    public KeywordAndData(long id, String keyword, String url) {
        this.id = id;
        this.keyword = keyword;
        this.url = url;
    }
}
