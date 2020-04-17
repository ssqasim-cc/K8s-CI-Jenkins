package io.digital.supercharger.model.enums;

import java.util.Arrays;

public enum Duration {

    TWELVE(12),
    TWENTY_FOUR(24),
    THIRTY_SIX(36),
    FORTY_EIGHT(48),
    SIXTY(60);

    private final Integer value;

    Duration(Integer value) {
        this.value = value;
    }

    public Integer getValue() {
        return value;
    }

    /**
     * From value to {@link Duration}.
     *
     * @param value the value
     * @return the {@link Duration}
     */
    public static Duration fromValue(Integer value) {
        for (Duration duration : values()) {
            if (duration.getValue().equals(value)) {
                return duration;
            }
        }
        throw new IllegalArgumentException(
            "Unknown enum type " + value + ", Allowed values are: " + Arrays.toString(values()));
    }
}
