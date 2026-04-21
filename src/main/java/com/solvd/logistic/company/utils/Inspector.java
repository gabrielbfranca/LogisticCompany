package com.solvd.logistic.company.utils;

import com.solvd.logistic.company.annotations.AuditAction;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import java.lang.reflect.Method;

public final class Inspector {
    public record FieldReport(String className, Map<String, String> Fields) {}
    private static final Logger logger = LogManager.getLogger(Inspector.class);
    public static FieldReport inspect(Object target) {
        Class<?> clazz = target.getClass();
        Map<String,String> sensitiveFields = new HashMap<>();

        for (Field field : clazz.getDeclaredFields()) {
            try {
                field.setAccessible(true);
                Object value = field.get(target);
                sensitiveFields.put(field.getName(), "sensitive email");
            } catch (IllegalAccessException e) {
                sensitiveFields.put(field.getName(), "error");
            }
        }

        return new FieldReport(clazz.getSimpleName(), sensitiveFields);

    }
    public static void audit(Object target) {
        Class<?> clazz = target.getClass();
        for (Method method : clazz.getDeclaredMethods()) {
            if (method.isAnnotationPresent(AuditAction.class)) {
                AuditAction audit = method.getAnnotation(AuditAction.class);
                logger.info("auditing method {} in {}: {} (Level {})",
                        method.getName(), clazz.getSimpleName(), audit.value(), audit.level());
            }
        }
    }
}
