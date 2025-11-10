package co.com.bootcamp.api.exception;

import java.util.List;

public class ValidationException extends BaseException {

    public ValidationException(List<String> errors) {
        super(
                "Error de validación",
                "VALIDATION_ERROR",
                "Error de validación",
                400,
                errors
        );
    }
}
