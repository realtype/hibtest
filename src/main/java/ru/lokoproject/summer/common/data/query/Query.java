package ru.lokoproject.summer.common.data.query;

import lombok.Getter;

@Getter
public abstract class Query {
    boolean isGroup;
    boolean isNot;
    String type;
    String className;
}
