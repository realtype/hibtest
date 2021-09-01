package ru.lokoproject.summer.common.data.query;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class SpecificQuery extends Query {
    String path;
    List<String> params;
}
