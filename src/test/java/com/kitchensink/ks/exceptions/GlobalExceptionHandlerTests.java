package com.kitchensink.ks.exceptions;

import com.kitchensink.ks.dto.Response;
import com.kitchensink.ks.exceptions.*;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.security.SignatureException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.AccountStatusException;
import org.springframework.security.authentication.BadCredentialsException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.MockitoAnnotations.openMocks;

public class GlobalExceptionHandlerTests {

    @InjectMocks
    private GlobalExceptionHandler globalExceptionHandler;

    @BeforeEach
    public void setUp() {
        openMocks(this);
    }

    @Test
    public void testHandleBadCredentialsException() {
        BadCredentialsException exception = new BadCredentialsException("Bad credentials");

        ResponseEntity<Response> responseEntity = globalExceptionHandler.handleAuthExceptions(exception);

        assertEquals(HttpStatus.FORBIDDEN, responseEntity.getStatusCode());
        assertEquals("The username or password is incorrect", responseEntity.getBody().getErrorMessage());
    }

    @Test
    public void testHandleAccountStatusException() {
        AccountStatusException exception = new AccountStatusException("Account is locked") {};

        ResponseEntity<Response> responseEntity = globalExceptionHandler.handleAuthExceptions(exception);

        assertEquals(HttpStatus.FORBIDDEN, responseEntity.getStatusCode());
        assertEquals("The account is locked", responseEntity.getBody().getErrorMessage());
    }

    @Test
    public void testHandleAccessDeniedException() {
        AccessDeniedException exception = new AccessDeniedException("Access Denied");

        ResponseEntity<Response> responseEntity = globalExceptionHandler.handleAuthExceptions(exception);

        assertEquals(HttpStatus.FORBIDDEN, responseEntity.getStatusCode());
        assertEquals("You don't have permission to access this resource", responseEntity.getBody().getErrorMessage());
    }

    @Test
    public void testHandleExpiredJwtException() {
        ExpiredJwtException exception = new ExpiredJwtException(null, null, "JWT expired");

        ResponseEntity<Response> responseEntity = globalExceptionHandler.handleAuthExceptions(exception);

        assertEquals(HttpStatus.FORBIDDEN, responseEntity.getStatusCode());
        assertEquals("The token has expired", responseEntity.getBody().getErrorMessage());
    }

    @Test
    public void testHandleSignatureException() {
        SignatureException exception = new SignatureException("Invalid signature");

        ResponseEntity<Response> responseEntity = globalExceptionHandler.handleAuthExceptions(exception);

        assertEquals(HttpStatus.FORBIDDEN, responseEntity.getStatusCode());
        assertEquals("The JWT signature is invalid", responseEntity.getBody().getErrorMessage());
    }

    @Test
    public void testHandleMemberNotFoundException() {
        MemberNotFoundException exception = new MemberNotFoundException();

        ResponseEntity<Response> responseEntity = globalExceptionHandler.handleMemberException(exception);

        assertEquals(HttpStatus.NOT_FOUND, responseEntity.getStatusCode());
        assertEquals("Member not found", responseEntity.getBody().getErrorMessage());
    }

    @Test
    public void testHandleDuplicateMemberException() {
        DuplicateMemberException exception = new DuplicateMemberException();

        ResponseEntity<Response> responseEntity = globalExceptionHandler.handleMemberException(exception);

        assertEquals(HttpStatus.CONFLICT, responseEntity.getStatusCode());
        assertEquals("Another member exists with this email", responseEntity.getBody().getErrorMessage());
    }

    @Test
    public void testHandleMissingIdException() {
        MissingIdException exception = new MissingIdException();

        ResponseEntity<Response> responseEntity = globalExceptionHandler.handleMemberException(exception);

        assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());
        assertEquals("id field is mandatory for update", responseEntity.getBody().getErrorMessage());
    }

    @Test
    public void testHandleGenericException() {
        Exception exception = new Exception("Generic error");

        ResponseEntity<Response> responseEntity = globalExceptionHandler.handleException(exception);

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, responseEntity.getStatusCode());
        assertEquals("Something went wrong", responseEntity.getBody().getErrorMessage());
    }
}

