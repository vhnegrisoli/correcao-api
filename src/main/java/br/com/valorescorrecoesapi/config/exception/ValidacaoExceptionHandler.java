package br.com.valorescorrecoesapi.config.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class ValidacaoExceptionHandler {

    @ExceptionHandler(ValidacaoException.class)
    public ResponseEntity<?> handleResouseNotFoundException(ValidacaoException rfnException) {
        ExceptionDetails resourceNotFoundDetails = new ExceptionDetails();
        resourceNotFoundDetails.setStatus(HttpStatus.BAD_REQUEST.value());
        resourceNotFoundDetails.setMessage(rfnException.getMessage());
        return new ResponseEntity<>(resourceNotFoundDetails, HttpStatus.BAD_REQUEST);
    }
}
