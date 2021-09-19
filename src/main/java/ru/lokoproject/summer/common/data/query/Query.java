package ru.lokoproject.summer.common.data.query;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public abstract class Query {
    boolean isGroup;
    boolean isNot;
    String type;
    String additionalType;
    String className;
}
