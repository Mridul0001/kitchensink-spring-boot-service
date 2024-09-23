package com.kitchensink.ks;

import java.lang.reflect.Field;

public class ReflectionHelper {
    public static void setPrivateField(Object obj, String fieldName, Object value) throws NoSuchFieldException, IllegalAccessException {
        Field field = obj.getClass().getDeclaredField(fieldName);
        field.setAccessible(true);
        field.set(obj, value);
    }
}
