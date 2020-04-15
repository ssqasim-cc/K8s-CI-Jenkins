package io.digital.supercharger.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import java.util.Map;

//@Component
public class JsonUtil {


    /**
     * Private Default constructor to prevent initialization from this class
     */
    private JsonUtil() {

    }

    /**
     * Object with JSON
     *
     * @param object to serialize
     * @return json string (Not pretty, use toJson(object, pretty)
     * @throws JsonProcessingException if a problem occurs
     */
    public static String toJson(Object object) throws JsonProcessingException {
        return toJson(object, false);
    }

    /**
     * Object with JSON with pretty print feature
     *
     * @param object to serialize
     * @param pretty to format the json
     * @return json string
     * @throws JsonProcessingException if a problem occurs
     */
    public static String toJson(Object object, boolean pretty) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        if (pretty) {
            mapper.enable(SerializationFeature.INDENT_OUTPUT);
        }
        return mapper.writeValueAsString(object);
    }


    /**
     * check if the passed json obj is valid Json
     *
     * @param obj string to validate as json
     * @return true if valid, false otherwise
     */
    public static boolean isValidJsonSring(String obj) {
        if (obj == null) {
            return false;
        }
        ObjectMapper mapper = new ObjectMapper();
        try {
            mapper.readTree(obj);
            return true;
        } catch (IOException e) {
            return false;
        }
    }

    /**
     * convert to json string to specified class type
     *
     * @param json      string
     * @param classType the type of the Pojo to construct
     * @return an instance of the Pojo
     * @throws IOException if a problem occurs
     */
    public static <T> T fromJsonStringToObject(String json,
                                               Class<T> classType) throws IOException {

        ObjectMapper mapper = new ObjectMapper();
        return mapper.readValue(json, classType);

    }

    /**
     * Object from JSON
     *
     * @param json      string
     * @param jsonClass the class of the Pojo to construct
     * @return an instance of the Pojo
     * @throws IOException if a problem occurs
     */
    public static <T> T fromJson(String json, Class<T> jsonClass) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        return mapper.readValue(json, jsonClass);
    }

    /**
     * Object from JSON
     *
     * @param json      stream
     * @param jsonClass the class to deserialize
     * @return the Pojo from json
     * @throws IOException if thrown my mapper
     */
    public static <T> T fromJson(InputStream json, Class<T> jsonClass) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        return mapper.readValue(json, jsonClass);
    }

    /**
     * Object from JSON
     *
     * @param json      string
     * @param jsonClass the class of the Pojo to construct
     * @return an instance of the Pojo
     * @throws IOException if a problem occurs
     */
    public static <T> T fromJson(Reader json, Class<T> jsonClass) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        return mapper.readValue(json, jsonClass);
    }


    /**
     * Object from JSON
     *
     * @param json      string
     * @param jsonClass the class of the Pojo to construct
     * @return an instance of the Pojo
     * @throws IOException if a problem occurs
     */
    public static <T> T fromJson(InputStream json, TypeReference<T> jsonClass) throws IOException {

        ObjectMapper mapper = new ObjectMapper();
        return mapper.readValue(json, jsonClass);
    }

    /**
     * Object from JSON
     *
     * @param json string
     * @return a dictionary of all keys/values
     * @throws IOException if a problem occurs
     */
    public static Map<String, Object> fromJson(String json) throws IOException {

        ObjectMapper mapper = new ObjectMapper();
        return mapper.readValue(json, new TypeReference<Map<String, Object>>() {
        });
    }

    /**
     * Object from JSON
     *
     * @param json input stream
     * @return a dictionary of all keys/values
     * @throws IOException if a problem occurs
     */
    public static Map<String, Object> fromJson(InputStream json) throws IOException {

        ObjectMapper mapper = new ObjectMapper();
        return mapper.readValue(json, new TypeReference<Map<String, Object>>() {
        });
    }


}
