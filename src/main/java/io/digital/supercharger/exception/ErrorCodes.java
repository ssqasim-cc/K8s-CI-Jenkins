package io.digital.supercharger.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ErrorCodes {

    COM_0001("INVALID_RESPONSE");

    private final String message;
}
