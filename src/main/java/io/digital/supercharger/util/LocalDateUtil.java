package io.digital.supercharger.util;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class LocalDateUtil {
    private static final String CUSTOM_DATE_FORMAT = "yyyy-MM-dd";
    private static final DateTimeFormatter CUSTOM_DATE_FORMATTER = DateTimeFormatter.ofPattern(CUSTOM_DATE_FORMAT);

    private LocalDateUtil() {
        //Utility class
    }

    public static boolean isDatePastCutoff(LocalDate currentDate, int cutoffDay) {
        return currentDate.getDayOfMonth() > cutoffDay;
    }

    public static class Serializer extends StdSerializer<LocalDate> {

        public Serializer() {
            super(LocalDate.class);
        }

        @Override
        public void serialize(LocalDate value, JsonGenerator generator, SerializerProvider provider)
            throws IOException {
            generator.writeString(value.format(CUSTOM_DATE_FORMATTER));
        }
    }

    public static class Deserializer extends StdDeserializer<LocalDate> {

        public Deserializer() {
            super(LocalDate.class);
        }

        @Override
        public LocalDate deserialize(JsonParser parser, DeserializationContext context)
            throws IOException {
            return LocalDate.parse(parser.readValueAs(String.class), CUSTOM_DATE_FORMATTER);
        }
    }
}
