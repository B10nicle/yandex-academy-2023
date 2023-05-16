package ru.yandex.yandexlavka.error.exception;

/**
 * @author Oleg Khilko
 */

public class TooManyRequestsException extends RuntimeException {
    public TooManyRequestsException(String message) {
        super(message);
    }
}
