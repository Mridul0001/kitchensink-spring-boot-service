package com.kitchensink.ks.exceptions;

import com.kitchensink.ks.dto.Response;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.security.SignatureException;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.AccountStatusException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.Arrays;

/**
 * Global Exception handler to handle all exceptions like security, CRUD, Internal server errors etc.
 */
@ControllerAdvice
@Log4j2
public class GlobalExceptionHandler {

    @ExceptionHandler({BadCredentialsException.class, AccountStatusException.class, AccessDeniedException.class, SignatureException.class, ExpiredJwtException.class})
    public ResponseEntity<Response> handleAuthExceptions(Exception e) {
        Response response = new Response();
        if (e instanceof BadCredentialsException) {
            response.setErrorMessage("The username or password is incorrect");
        }
        if (e instanceof AccountStatusException) {
            response.setErrorMessage("The account is locked");
        }
        if (e instanceof AccessDeniedException) {
            response.setErrorMessage("You don't have permission to access this resource");
        }
        if (e instanceof ExpiredJwtException) {
            response.setErrorMessage("The token has expired");
        }
        if(e instanceof SignatureException) {
            response.setErrorMessage("The JWT signature is invalid");
        }
        return new ResponseEntity<>(response, HttpStatus.FORBIDDEN);
    }

    @ExceptionHandler(MemberException.class)
    public ResponseEntity<Response> handleMemberException(MemberException e) {
        Response response = new Response();
        HttpStatus status = HttpStatus.BAD_REQUEST;
        if(e instanceof MemberNotFoundException) {
            response.setErrorMessage(e.getMessage());
            status = HttpStatus.NOT_FOUND;
        }else if(e instanceof DuplicateMemberException){
            response.setErrorMessage(e.getMessage());
            status = HttpStatus.CONFLICT;
        }

        response.setErrorMessage(e.getMessage());
        return new ResponseEntity<>(response, status);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Response> handleException(Exception e) {
        log.error(Arrays.asList(e.getStackTrace()));
        return getDefaultExceptionResponse();
    }

    private ResponseEntity<Response> getDefaultExceptionResponse(){
        Response response = new Response();
        response.setErrorMessage("Something went wrong");
        return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
