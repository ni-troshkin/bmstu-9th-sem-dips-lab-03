package com.ratingservice.utils;

import java.io.IOException;
import java.util.Properties;

/**
 * Класс, выполняющий обработку файла со свойствами
 */
public final class PropertiesUtil {
    /**
     * Набор свойств
     */
    private static final Properties PROPERTIES = new Properties();

    static {
        loadProperties();
    }

    /**
     * Получение свойства по названию
     * @param key название свойства (ключ)
     * @return строковое значение свойства
     */
    public static String get(String key) {
        return PROPERTIES.getProperty(key);
    }

    /**
     * Загрузка свойств в атрибут из файла database.properties
     */
    private static void loadProperties() {
        try (var inputStream = PropertiesUtil.class.getClassLoader().getResourceAsStream("database.properties")) {
            PROPERTIES.load(inputStream);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private PropertiesUtil () {}
}
