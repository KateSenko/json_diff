package com.example.json_diff.repository;

import com.example.json_diff.repository.entity.JsonDiffEntity;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface JsonDiffRepository extends CrudRepository<JsonDiffEntity, Long> {

}
