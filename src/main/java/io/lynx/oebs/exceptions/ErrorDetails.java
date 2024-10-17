package io.lynx.oebs.exceptions;

import io.lynx.oebs.dtos.ErrorResponse;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ErrorDetails {
    private ErrorResponse data;
}
