package com.kitchensink.ks.exceptions;

public class DuplicateMemberException extends MemberException{
    public DuplicateMemberException() {
        super("Another member exists with this email");
    }
}
