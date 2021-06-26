package com.ynz.demo.containerizedapp.exceptions;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

public class DuplicatedClientException extends RuntimeException {
    public DuplicatedClientException(@Email @NotBlank String s) {
        super(s);
    }
}
