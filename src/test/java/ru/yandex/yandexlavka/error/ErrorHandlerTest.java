package ru.yandex.yandexlavka.error;

import ru.yandex.yandexlavka.error.handler.ErrorHandler;
import ru.yandex.yandexlavka.error.exception.*;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * @author Oleg Khilko
 */

public class ErrorHandlerTest {
    private static final ErrorHandler handler = new ErrorHandler();
    private static final String error = "Error";

    @Test
    public void handleCourierDoesNotExistExceptionTest() {
        var exception = new CourierDoesNotExistException(error);
        handler.handleCourierDoesNotExistException(exception);
        assertEquals(error, exception.getMessage());
    }

    @Test
    public void handleOrderDoesNotExistExceptionTest() {
        var exception = new OrderDoesNotExistException(error);
        handler.handleOrderDoesNotExistException(exception);
        assertEquals(error, exception.getMessage());
    }

    @Test
    public void handlePageDoesNotExistExceptionTest() {
        var exception = new PageDoesNotExistException(error);
        handler.handlePageDoesNotExistException(exception);
        assertEquals(error, exception.getMessage());
    }

    @Test
    public void handleBadRequestExceptionTest() {
        var exception = new BadRequestException(error);
        handler.handleBadRequestException(exception);
        assertEquals(error, exception.getMessage());
    }

    @Test
    public void handleTooManyRequestsExceptionTest() {
        var exception = new TooManyRequestsException(error);
        handler.handleTooManyRequestsException(exception);
        assertEquals(error, exception.getMessage());
    }

    @Test
    public void handleCourierHasZeroCompletedOrdersExceptionTest() {
        var exception = new CourierHasZeroCompletedOrdersException(error);
        handler.handleCourierHasZeroCompletedOrdersException(exception);
        assertEquals(error, exception.getMessage());
    }
}
