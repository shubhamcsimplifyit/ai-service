package com.aiservice.util;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

public final class StringUtil {

    StringUtil(){

    }
    
    public static String join(List<String> namesList) {
        return String.join(",", namesList);
    }

    public static String join(List<String> namesList, String separtor) {
        return String.join(separtor, namesList);
    }

    public static String strJoin(List<String> namesList) {
        return String.join(",", namesList
                        .stream()
                        .map(name -> ("'" + name + "'"))
                        .collect(Collectors.toList()));
    }

    public static String joinString(List<String> string) {
        return org.springframework.util.StringUtils.collectionToDelimitedString(string, ",");
    }

    public static String joinStringWithBase64(List<String> namesList) {
        return String.join(",", namesList
                        .stream()
                        .map(name -> ("TO_BASE64('" + name + "')"))
                        .collect(Collectors.toList()));
    }
    
    public static String strJoinWithOutQueots(List<String> namesList) {
        return String.join(",", namesList
                        .stream()
                        .map(name -> ( name ))
                        .collect(Collectors.toList()));
    }
    
    public static final String replaceString(String tobeReplaced, Map<String,String> inputMap) {
        Set<String> keySet = inputMap.keySet();
        for(String key : keySet) {
            tobeReplaced = tobeReplaced.replaceAll(key, inputMap.get(key));
        }
        return tobeReplaced;
    }


}
