package io.digital.supercharger.util;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class LocalDateTimeUtil {
    public static final DateTimeFormatter BACKEND_DATE_FORMATTER = DateTimeFormatter
        .ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSSZ");
    private static final DateTimeFormatter CUSTOM_DATE_FORMATTER = DateTimeFormatter.ISO_LOCAL_DATE_TIME;

    private LocalDateTimeUtil() {
        //Utility class
    }

    public static class Serializer extends StdSerializer<LocalDateTime> {

        public Serializer() {
            super(LocalDateTime.class);
        }

        @Override
        public void serialize(LocalDateTime value, JsonGenerator generator, SerializerProvider provider)
            throws IOException {
            generator.writeString(value.format(CUSTOM_DATE_FORMATTER));
        }
    }

    public static class Deserializer extends StdDeserializer<LocalDateTime> {

        public Deserializer() {
            super(LocalDateTime.class);
        }

        @Override
        public LocalDateTime deserialize(JsonParser parser, DeserializationContext context)
            throws IOException {
            return LocalDateTime.parse(parser.readValueAs(String.class), CUSTOM_DATE_FORMATTER);
        }
    }
}
