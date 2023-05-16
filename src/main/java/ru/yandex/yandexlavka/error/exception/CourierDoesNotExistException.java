package ru.yandex.yandexlavka.error.exception;

/**
 * @author Oleg Khilko
 */

public class CourierDoesNotExistException extends RuntimeException {
    public CourierDoesNotExistException(String message) {
        super(message);
    }
}
