package com.curescode.bridge.utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Cure
 * @date 2024/02/05 18:43
 */
public class ObjectUtils {
    public static HashMap<String,String> objectCoverMap(Object obj){
        HashMap<String,String> result =new HashMap<>();
        if (obj instanceof Map<?,?> objMap){
            for (Map.Entry<?, ?> entry : objMap.entrySet()) {
                String key = (String) entry.getKey();
                String value = entry.getValue().toString();
                result.put(key,value);
            }
            return result;
        }
        return null;
    }


    public static <T> List<T> objConvertList(Object obj, Class<T> clazz) {
        List<T> result = new ArrayList<>();
        if (obj instanceof List<?>) {
            for (Object o : (List<?>) obj) {
                result.add(clazz.cast(o));
            }
            return result;
        }
        return null;
    }

}
