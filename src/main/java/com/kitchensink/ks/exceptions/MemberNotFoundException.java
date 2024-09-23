package com.kitchensink.ks.exceptions;

public class MemberNotFoundException extends MemberException{
    public MemberNotFoundException() {
        super("Member not found");
    }
}
