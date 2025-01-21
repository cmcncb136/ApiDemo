package com.example.apidemo.db;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.*;
import org.checkerframework.checker.units.qual.A;

@Entity
@Getter
@NoArgsConstructor
@Setter
public class Keyword {
    @Id
    @Column(length = 150) //현재 naver server에 mariaDB에 key 키 제한이 255(default) 보다 작아서 지정해 줌
    private String keyword;

    private long count;

    private String languageCode;

    @Builder
    public Keyword(String keyword, long count, String languageCode) {
        this.keyword = keyword;
        this.count = count;
        this.languageCode = languageCode;
    }
}
