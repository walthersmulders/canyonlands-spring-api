package com.walthersmulders.exception;

import java.util.Map;

public class EntityNotFoundException extends RuntimeException {
    public EntityNotFoundException(String className, Map<String, String> searchParamsMap) {
        super(EntityNotFoundException.generateMessage(className, searchParamsMap));
    }

    private static String generateMessage(String className, Map<String, String> searchParamsMap) {
        return "Could not find " + className + " for parameter(s) " + searchParamsMap;
    }
}
