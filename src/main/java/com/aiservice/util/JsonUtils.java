package com.aiservice.util;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.io.FileUtils;

import com.aiservice.exception.custom.AiServiceBusinessException;
import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

public class JsonUtils {
	
    public static String parseObjectToJson(Object obj,String objName){
        ObjectMapper mapper = new ObjectMapper();
        try {
        	return mapper.writeValueAsString(obj);
        } catch (JsonProcessingException e) {
        	e.printStackTrace();
        	throw new AiServiceBusinessException("Unable to parse "+objName, e);
        }
    }
	
    public static String encodeFileToBase64Binary(File file) throws IOException {
        byte[] encoded = Base64.getEncoder().encode(FileUtils.readFileToByteArray(file));
        return new String(encoded, StandardCharsets.US_ASCII);
    }
    
    public static <T> T parseJsonToObject(String json, Class<T> type,String objName) {
        ObjectMapper mapper = new ObjectMapper();
        try {
            T data = mapper.readValue(json, type);
            return data;
        } catch (Exception e) {
        	e.printStackTrace();
        	throw new AiServiceBusinessException("Unable to parse "+objName, e);
        }
    }
    
    public static <T> T parseConvertValueJsonToObject(String json, Class<T> type,String objName) {
        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(DeserializationFeature.ACCEPT_SINGLE_VALUE_AS_ARRAY, true);
		mapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
        try {
            T data = mapper.convertValue(json, type);
            return data;
        } catch (Exception e) {
            e.printStackTrace();
            throw new AiServiceBusinessException("Unable to parse "+objName, e);
        }
    }
    
    
    public static Map<String, String> parseReadValueJsonToMapString(String json,String objName){
        JsonFactory jf = new JsonFactory();
        jf.enable(JsonParser.Feature.ALLOW_COMMENTS);

        ObjectMapper mapper = new ObjectMapper(jf);
        Map<String, String> map = new HashMap<String, String>();
        try {
            map = mapper.readValue(json, new TypeReference<Map<String, String>>() {
            });
            return map;
        } catch (Exception e) {
            e.printStackTrace();
            throw new AiServiceBusinessException("Unable to parse "+objName, e);
        }
    }
    
    
    
    
    public static Map<String, String> parseConvertValueJsonToMapString(String json,String objName){
        JsonFactory jf = new JsonFactory();
        jf.enable(JsonParser.Feature.ALLOW_COMMENTS);

        ObjectMapper mapper = new ObjectMapper(jf);
        Map<String, String> map = new HashMap<String, String>();
        try {
            map = mapper.convertValue(json, new TypeReference<Map<String, String>>() {
            });
            return map;
        } catch (Exception e) {
            e.printStackTrace();
            throw new AiServiceBusinessException("Unable to parse "+objName, e);
        }
    }
    
    public static Map<String, Object> parseReadValueJsonToMap(String json,String objName){
        JsonFactory jf = new JsonFactory();
        jf.enable(JsonParser.Feature.ALLOW_COMMENTS);

        ObjectMapper mapper = new ObjectMapper(jf);
        // JsonParser parser=mapper.get
        Map<String, Object> map = new HashMap<String, Object>();
        try {
            map = mapper.readValue(json, new TypeReference<Map<String, Object>>() {
            });
            return map;
        } catch (Exception e) {
            e.printStackTrace();
            throw new AiServiceBusinessException("Unable to parse "+objName, e);
        }
    }
    
    public static HashMap<String,Object> parseConvertObjectToMap(Object data) {
        ObjectMapper mapper = new ObjectMapper();
        mapper.enable(DeserializationFeature.ACCEPT_EMPTY_STRING_AS_NULL_OBJECT);
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        return mapper.convertValue(data, new TypeReference<HashMap<String,Object>>() {
        });
    }
    
    
    
    
    
}
