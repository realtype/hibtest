package ru.lokoproject.summer.common.data.query;

public class QueryExecutionException extends RuntimeException {


    public QueryExecutionException(String message, Query causeQuery) {
        super(message);
    }

    public QueryExecutionException(String message, Throwable cause, Query causeQuery) {
        super(message, cause);
    }


}
