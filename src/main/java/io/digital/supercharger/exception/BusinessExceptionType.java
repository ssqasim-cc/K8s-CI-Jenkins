package io.digital.supercharger.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum BusinessExceptionType {
    BET_0001("Internal Server Error"),
    BET_0002("Error retrieving data from ESB");
    private final String message;
}
