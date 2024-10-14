package io.lynx.oebs.exceptions;

import lombok.Data;

import java.text.SimpleDateFormat;
import java.util.Date;

@Data
public class ErrorDetails {
    private final String timeStamp;
    private String message;
    private String details;

    public ErrorDetails(String message, String details) {
        this.message = message;
        this.details = details;
        SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("dd-MMM-yyyy HH:mm:SS");
        this.timeStamp = DATE_FORMAT.format(new Date());
    }

}