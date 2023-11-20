package com.libraryservice.utils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Класс, выполняющий соединение с базой данных
 */
public final class ConnectionManager {
    /**
     * Пароль от БД
     */
    private static final String PASSWORD_KEY = "db.password";

    /**
     * Пользователь, под которым выполняется вход в БД
     */
    private static final String USERNAME_KEY = "db.username";

    /**
     * Адрес, по которому осуществляется подключение к БД
     */
    private static final String URL_KEY = "db.url";

    static {
        loadDriver();
    }

    /**
     * Загрузка драйвера для PostgreSQL
     */
    private static void loadDriver() {
        try {
            Class.forName("org.postgresql.Driver");
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    private ConnectionManager() {
    }

    /**
     * Открытие соединения с базой данных
     * @return объект, описывающий соединение
     */
    public static Connection open() {
        try {
            return DriverManager.getConnection(
                    PropertiesUtil.get(URL_KEY),
                    PropertiesUtil.get(USERNAME_KEY),
                    PropertiesUtil.get(PASSWORD_KEY)
            );
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}