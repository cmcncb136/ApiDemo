package com.example.apidemo.db.service;

import com.example.apidemo.db.DataEntity;
import com.example.apidemo.db.repository.DataRepo;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class DataService {
    DataRepo dataRepo;

    public DataEntity saveData(DataEntity dataEntity) {
        return dataRepo.save(dataEntity);
    }

    // Read (Find all)
    public List<DataEntity> getAllData() {
        return dataRepo.findAll();
    }

    // Read (Find by ID)
    public DataEntity getDataById(String id) {
        return dataRepo.findById(id).orElse(null);
    }

    // Update (Update operation is done by save method)
    public DataEntity updateData(DataEntity dataEntity) {
        if (dataRepo.existsById(dataEntity.getUrl())) {
            return dataRepo.save(dataEntity);
        } else {
            // Handle case when the entity does not exist
            return null;
        }
    }

    // Delete (by ID)
    public void deleteDataById(String id) {
        dataRepo.deleteById(id);
    }

    // Delete (all)
    public void deleteAllData() {
        dataRepo.deleteAll();
    }

}

