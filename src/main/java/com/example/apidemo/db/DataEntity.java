package com.example.apidemo.db;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.*;

@Entity
@NoArgsConstructor
@Getter
@Setter
public class DataEntity {
    @Id
    @Column(length = 150) //현재 naver server에 mariaDB에 key 키 제한이 255(default) 보다 작아서 지정해 줌
    private String url;

    @Column(columnDefinition = "TEXT")
    private String title;

    @Column(columnDefinition = "TEXT")
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
