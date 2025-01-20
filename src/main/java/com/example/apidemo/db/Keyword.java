package com.example.apidemo.db;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.checkerframework.checker.units.qual.A;

@Entity
@Getter
@NoArgsConstructor
public class Keyword {
    @Id
    private String keyword;

    private long count;

    @Builder
    public Keyword(String keyword, long count) {
        this.keyword = keyword;
        this.count = count;
    }
}
