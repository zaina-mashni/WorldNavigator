package com.WorldNavigator.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class JSONDecode {

    public static <T>T decodeJsonString(String jsonString, Class<?> classRep) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        return (T) objectMapper.readValue(jsonString, classRep);
    }
}
