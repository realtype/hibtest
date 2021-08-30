package ru.lokoproject.summer.common.data.util;

import lombok.experimental.UtilityClass;

@UtilityClass
public class FieldPathUtil {

    private static final String DELIMITER = ".";

    public static boolean isFieldPathFinal(String path){
        return !path.contains(DELIMITER);
    }

    public static String dropLastPartOfPath(String path){
        return path.substring(0, path.lastIndexOf(DELIMITER));
    }

    public static String getLastPartOfPath(String path){
        return path.substring(path.lastIndexOf(DELIMITER)+1);
    }

    public static String[] splitPath(String path){
        return path.split(DELIMITER);
    }
}
