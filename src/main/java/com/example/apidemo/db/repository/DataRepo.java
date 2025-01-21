package com.example.apidemo.db.repository;

import com.example.apidemo.db.DataEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DataRepo extends JpaRepository<DataEntity, String > {
}
