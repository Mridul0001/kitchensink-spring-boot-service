package com.kitchensink.ks.exceptions;

public class MissingIdException extends MemberException{
    public MissingIdException() {
        super("id field is mandatory for update");
    }
}
