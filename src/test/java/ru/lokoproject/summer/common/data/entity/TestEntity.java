package ru.lokoproject.summer.common.data.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.List;

@Getter
@Setter
@Entity
public class TestEntity {
    @Id
    int    id;
    String strField;
    @ManyToOne
    SecondTestEntity secondTestEntity;
    @OneToMany
    @JoinColumn(name = "testEntityInList")
    List<SecondTestEntity> secondTestEntityList;
}
