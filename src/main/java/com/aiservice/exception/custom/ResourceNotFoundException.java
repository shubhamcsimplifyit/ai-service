package com.aiservice.exception.custom;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.IntStream;

import org.apache.commons.lang3.StringUtils;

public class ResourceNotFoundException extends RuntimeException {	

    private static final long serialVersionUID = 1L;
    
    public ResourceNotFoundException(String message) {
        super(message);
    }

    @SuppressWarnings("rawtypes")
    public ResourceNotFoundException(Class clazz, String... searchParamsMap) {
        super(ResourceNotFoundException.generateMessage(clazz.getSimpleName(), toMap(String.class, String.class, searchParamsMap)));
    }

    private static String generateMessage(String entity, Map<String, String> searchParams) {
        return "No "+ StringUtils.capitalize(entity) + " data " +
                        "was found for request parameters " +
                        searchParams;
    }

    private static <K, V> Map<K, V> toMap(Class<K> keyType, Class<V> valueType, Object... entries) {
        if (entries.length % 2 == 1)
            throw new IllegalArgumentException("Invalid entries");
        return IntStream.range(0, entries.length / 2).map(i -> i * 2)
                        .collect(HashMap::new,
                                        (m, i) -> m.put(keyType.cast(entries[i]), valueType.cast(entries[i + 1])),
                                        Map::putAll);
    }

}
