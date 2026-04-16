package org.b.bonusserver.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.OffsetDateTime;

@Getter
@AllArgsConstructor
public class ApiErrorResponse {

    private final String message;
    private final OffsetDateTime timestamp;
}
