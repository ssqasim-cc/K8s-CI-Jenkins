package io.digital.supercharger.util;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import io.digital.supercharger.model.enums.Duration;
import org.springframework.util.ObjectUtils;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

/**
 * The type Monthly validator.
 *
 */
public class MonthlyValidator {

    /**
     * Utility Class private Constructor
     */
    private MonthlyValidator(){

    }

    private static final List<Integer> validMonths = Arrays.asList(12, 24, 36, 48, 60);

    /**
     * Validate monthly duration.
     *
     * @param duration the duration
     * @return the boolean
     */
    public static boolean validateDuration(Integer duration) {

        return !ObjectUtils.isEmpty(duration) && validMonths.contains(duration);
    }

    public static class Serializer extends StdSerializer<Duration> {

        public Serializer() {
            super(Duration.class);
        }

        @Override
        public void serialize(Duration value, JsonGenerator generator, SerializerProvider provider)
            throws IOException {
            generator.writeNumber(value.getValue());
        }
    }

    public static class Deserializer extends StdDeserializer<Duration> {

        public Deserializer() {
            super(Duration.class);
        }

        @Override
        public Duration deserialize(JsonParser parser, DeserializationContext context)
            throws IOException {
            return Duration.fromValue(parser.readValueAs(Integer.class));
        }
    }
}
