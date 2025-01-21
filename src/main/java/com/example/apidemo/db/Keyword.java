package com.example.apidemo.db;

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
