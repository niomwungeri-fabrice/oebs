package io.lynx.oebs.dtos;

import lombok.Data;

import java.util.List;
import java.util.Map;

@Data
public class CreateBusinessRequest {
    private String name;
    private String description;
    private String address;
    private String phoneNumber;
    private String email;
    private List<String> tags;
    private Map<String, String> hoursOfOperation;
}
