package ru.yandex.yandexlavka.error.handler;

import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import ru.yandex.yandexlavka.error.entity.EmptyBodyResponse;
import ru.yandex.yandexlavka.error.exception.*;
import lombok.extern.slf4j.Slf4j;

import java.time.format.DateTimeParseException;

import static org.springframework.http.HttpStatus.*;

/**
 * @author Oleg Khilko
 */

@Slf4j
@RestControllerAdvice
public class ErrorHandler {

    @ExceptionHandler
    @ResponseStatus(NOT_FOUND)
    public EmptyBodyResponse handleCourierDoesNotExistException(CourierDoesNotExistException exception) {
        log.info(exception.getMessage());
        return new EmptyBodyResponse();
    }

    @ExceptionHandler
    @ResponseStatus(NOT_FOUND)
    public EmptyBodyResponse handleOrderDoesNotExistException(OrderDoesNotExistException exception) {
        log.info(exception.getMessage());
        return new EmptyBodyResponse();
    }

    @ExceptionHandler
    @ResponseStatus(BAD_REQUEST)
    public EmptyBodyResponse handlePageDoesNotExistException(PageDoesNotExistException exception) {
        log.info(exception.getMessage());
        return new EmptyBodyResponse();
    }

    @ExceptionHandler
    @ResponseStatus(BAD_REQUEST)
    public EmptyBodyResponse handleMethodArgumentTypeMismatchException(MethodArgumentTypeMismatchException exception) {
        log.info(exception.getMessage());
        return new EmptyBodyResponse();
    }

    @ExceptionHandler
    @ResponseStatus(BAD_REQUEST)
    public EmptyBodyResponse handleJsonException(HttpMessageNotReadableException exception) {
        log.info(exception.getMessage());
        return new EmptyBodyResponse();
    }

    @ExceptionHandler
    @ResponseStatus(BAD_REQUEST)
    public EmptyBodyResponse handleDateTimeParseException(DateTimeParseException exception) {
        log.info(exception.getMessage());
        return new EmptyBodyResponse();
    }

    @ExceptionHandler
    @ResponseStatus(BAD_REQUEST)
    public EmptyBodyResponse handleBadRequestException(BadRequestException exception) {
        log.info(exception.getMessage());
        return new EmptyBodyResponse();
    }

    @ExceptionHandler
    @ResponseStatus(TOO_MANY_REQUESTS)
    public EmptyBodyResponse handleTooManyRequestsException(TooManyRequestsException exception) {
        log.info(exception.getMessage());
        return new EmptyBodyResponse();
    }

    @ExceptionHandler
    @ResponseStatus(NOT_FOUND)
    public EmptyBodyResponse handleCourierHasZeroCompletedOrdersException(CourierHasZeroCompletedOrdersException exception) {
        log.info(exception.getMessage());
        return new EmptyBodyResponse();
    }
}
