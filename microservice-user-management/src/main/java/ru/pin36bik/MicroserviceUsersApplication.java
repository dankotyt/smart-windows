package ru.pin36bik;

import io.github.cdimascio.dotenv.Dotenv;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class MicroserviceUsersApplication {
    public static void main(String[] args) {
        // Загружаем переменные из .env файла
        Dotenv dotenv = Dotenv.configure().load();
        // Устанавливаем переменные окружения
        dotenv.entries().forEach(entry -> System.setProperty(entry.getKey(),
                entry.getValue()));
        SpringApplication.run(MicroserviceUsersApplication.class, args);
    }
}
