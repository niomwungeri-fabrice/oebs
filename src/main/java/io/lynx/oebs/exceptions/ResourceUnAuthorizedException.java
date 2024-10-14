package io.lynx.oebs.exceptions;


import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.UNAUTHORIZED)
public class ResourceUnAuthorizedException extends RuntimeException{
    public ResourceUnAuthorizedException(String message) {
        super(message);
    }
}

