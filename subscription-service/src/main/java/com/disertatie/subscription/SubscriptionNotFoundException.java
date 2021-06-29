package com.disertatie.subscription;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.NOT_FOUND)
public class SubscriptionNotFoundException extends Exception{
    public SubscriptionNotFoundException(String message){
        super(message);
    }
}
