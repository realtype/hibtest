package ru.lokoproject.summer.common.data.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Id;

@Getter
@Setter
public class TestEntity {
    @Id
    int    id;

    String strField;
}
