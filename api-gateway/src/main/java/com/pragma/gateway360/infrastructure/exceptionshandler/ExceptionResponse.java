package com.pragma.gateway360.infrastructure.exceptionshandler;

import java.time.LocalDateTime;

public record ExceptionResponse(
        Boolean success,
        String message,
        LocalDateTime timeStamp,
        String details) {
}
