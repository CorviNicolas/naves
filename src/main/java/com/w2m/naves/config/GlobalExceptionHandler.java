package com.w2m.naves.config;

import jakarta.validation.ValidationException;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import java.util.NoSuchElementException;

@RestControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger LOG = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    private final boolean printStack;

    public GlobalExceptionHandler(@Value("${server.print-stack:false}") boolean printStack) {
        this.printStack = printStack;
    }

    @ExceptionHandler({Exception.class, RuntimeException.class})
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ResponseEntity<ErrorDTO> handleGeneral(Exception ex) {
        ErrorDTO error = errorDTO("ERROR_GENERICO", "Error generico", ex);
        return new ResponseEntity<>(error, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(ValidationException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<ErrorDTO> handleValidation(ValidationException ex) {
        ErrorDTO error = errorDTO("VALIDACION", ex.getMessage(), ex);
        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
    }


    @ExceptionHandler(NoSuchElementException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<ErrorDTO> handleNoSuchElement(NoSuchElementException ex) {
        ErrorDTO error = errorDTO("ELEMENTO_NO_ENCONTRADO", ex.getMessage(), ex);
        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
    }


    @ExceptionHandler(IllegalStateException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<ErrorDTO> handleIllegalStateException(IllegalStateException e) {
        ErrorDTO error = errorDTO("ILLEGAL_STATE", e.getMessage(), e);
        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<ErrorDTO> handleArgumentNotValid(MethodArgumentNotValidException e) {
        ErrorDTO error = errorDTO("ARGUMENTO_NO_VALIDO", e.getMessage(), e);
        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<ErrorDTO> handleHttpMethodNotSupported(HttpRequestMethodNotSupportedException e) {
        ErrorDTO error = errorDTO("METODO_HTTP_NO_SOPORTADO", e.getMessage(), e);
        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
    }


    /**
     * @param code
     * @param message
     * @param t
     * @return new ErrorDTO
     */
    private ErrorDTO errorDTO(String code, String message, Throwable t) {
        LOG.error("Error", t);
        String stack = printStack ? ExceptionUtils.getStackTrace(t) : null;
        return new ErrorDTO(code, message, stack);
    }

    class ErrorDTO {
        private String code;
        private String message;
        private String stack;

        public ErrorDTO(String code, String message, String stack) {
            this.code = code;
            this.message = message;
            this.stack = stack;
        }

        public String getCode() {
            return code;
        }

        public String getMessage() {
            return message;
        }

        public String getStack() {
            return stack;
        }
    }
}
