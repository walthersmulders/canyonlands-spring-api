package com.walthersmulders.exception;

import java.util.Map;

public class EntityExistsException extends RuntimeException {
    public EntityExistsException(String className, Map<String, String> searchParamsMap) {
        super(EntityExistsException.generateMessage(className, searchParamsMap));
    }

    private static String generateMessage(String className, Map<String, String> searchParamsMap) {
        return className + " already exists. " + className + " found for the following parameter(s) " + searchParamsMap;
    }
}
