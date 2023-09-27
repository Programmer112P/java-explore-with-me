package ru.practicum.mainservice.exception.errorhandler;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import ru.practicum.mainservice.exception.BadRequestException;
import ru.practicum.mainservice.exception.ConflictException;
import ru.practicum.mainservice.exception.NotFoundException;

import javax.validation.ConstraintViolationException;

@RestControllerAdvice
@Slf4j
public class ErrorHandler {

    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(NotFoundException.class)
    public ApiError handleNotFoundException(final NotFoundException e) {
        log.info("404 {}", e.getMessage());
        return ApiError.builder()
                .status(HttpStatus.NOT_FOUND.toString())
                .reason("The required object was not found")
                .message(e.getMessage())
                .build();
    }

    @ResponseStatus(HttpStatus.CONFLICT)
    @ExceptionHandler(ConflictException.class)
    public ApiError handleConflictException(final ConflictException e) {
        log.info("409 {}", e.getMessage());
        return ApiError.builder()
                .status(HttpStatus.CONFLICT.toString())
                .reason("Integrity constraint has been violated")
                .message(e.getMessage())
                .build();
    }

    @ExceptionHandler(BadRequestException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ApiError handleBadRequestException(final BadRequestException e) {
        log.info("400 {}", e.getMessage());
        return ApiError.builder()
                .status(HttpStatus.BAD_REQUEST.toString())
                .reason("Incorrectly made request")
                .message(e.getMessage())
                .build();
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ApiError handleMethodArgumentTypeMismatchException(final MethodArgumentTypeMismatchException e) {
        log.info("400 {}", e.getMessage());
        return ApiError.builder()
                .status(HttpStatus.BAD_REQUEST.toString())
                .reason("Incorrectly made request")
                .message(e.getMessage())
                .build();
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ApiError handleMethodArgumentNotValidException(final MethodArgumentNotValidException e) {
        log.info("400 {}", e.getMessage());
        return ApiError.builder()
                .status(HttpStatus.BAD_REQUEST.toString())
                .reason("Incorrectly made request")
                .message(e.getMessage())
                .build();
    }

    @ExceptionHandler(MissingServletRequestParameterException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ApiError handleMissingParams(final MissingServletRequestParameterException e) {
        log.info("400 {}", e.getMessage());
        return ApiError.builder()
                .status(HttpStatus.BAD_REQUEST.toString())
                .reason("Incorrectly made request")
                .message(e.getMessage())
                .build();
    }

    @ExceptionHandler(ConstraintViolationException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ApiError handleConstraintViolationException(final ConstraintViolationException e) {
        log.info("400 {}", e.getMessage());
        return ApiError.builder()
                .status(HttpStatus.BAD_REQUEST.toString())
                .reason("Incorrectly made request")
                .message(e.getMessage())
                .build();
    }
}
