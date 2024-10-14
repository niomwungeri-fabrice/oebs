package io.lynx.oebs.helpers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class Helpers {
    private static final ObjectMapper objectMapper = new ObjectMapper(); // No pretty printing

    public static String toJson(Object obj) {
        try {
            return objectMapper.writeValueAsString(obj);
        } catch (JsonProcessingException e) {
            log.error("couldn't convert class to json error: {}", e.getMessage());
            return "{}"; // Returns an empty JSON if there’s an error
        }
    }
}
