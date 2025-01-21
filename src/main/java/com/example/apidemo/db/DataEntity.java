package com.example.apidemo.db;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.*;

@Entity
@NoArgsConstructor
@Getter
@Setter
public class DataEntity {
    @Id
    private String url;

    private String title;

    private String content;

    private String language_code;

    private long count;

    @Builder
    public DataEntity(String url, String title, String content, String language_code, long count) {
        this.url = url;
        this.title = title;
        this.content = content;
        this.language_code = language_code;
        this.count = count;
    }
}
