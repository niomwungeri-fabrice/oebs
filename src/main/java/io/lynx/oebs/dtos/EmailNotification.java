package io.lynx.oebs.dtos;

import lombok.Builder;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import java.util.Map;

@Slf4j
@Builder
@Data
public class EmailNotification {
    private String[] to;
    private String subject;
    private String template;
    private Map<String, Object> model;
}
