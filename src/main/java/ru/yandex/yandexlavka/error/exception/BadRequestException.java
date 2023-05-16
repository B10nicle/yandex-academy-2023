package ru.yandex.yandexlavka.error.exception;

/**
 * @author Oleg Khilko
 */

public class BadRequestException extends RuntimeException {
    public BadRequestException(String message) {
        super(message);
    }
}
