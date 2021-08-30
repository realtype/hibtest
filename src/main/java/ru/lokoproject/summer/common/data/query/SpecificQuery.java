package ru.lokoproject.summer.common.data.query;

import lombok.Getter;

import java.util.List;

@Getter
public class SpecificQuery extends Query {
    String path;
    List<String> params;
}
