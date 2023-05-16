package ru.yandex.yandexlavka.error.exception;

/**
 * @author Oleg Khilko
 */

public class OrderDoesNotExistException extends RuntimeException {
    public OrderDoesNotExistException(String message) {
        super(message);
    }
}
