package com.example.json_diff.repository.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.Id;

@Getter
@Setter
@NoArgsConstructor
@Entity
public class JsonDiffEntity {

    @Id
    private Long id;
    private String leftJson;
    private String rightJson;

}
