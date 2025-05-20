package ru.pin36bik;

import io.github.cdimascio.dotenv.Dotenv;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import ru.pin36bik.config.JwtConfig;

@SpringBootApplication
@EnableConfigurationProperties(JwtConfig.class)
public class MicroserviceUsersApplication {
    public static void main(String[] args) {
        Dotenv dotenv = Dotenv.configure().load();

        System.out.println("JWT_SECRET: " + dotenv.get("JWT_SECRET"));
        System.out.println("JWT_ACCESS_TTL: " + dotenv.get("JWT_ACCESS_TTL"));
        System.out.println("JWT_REFRESH_TTL: " + dotenv.get("JWT_REFRESH_TTL"));
        System.out.println("ALL_PRIVILEGES_USER_LOGIN: " + dotenv.get("ALL_PRIVILEGES_USER_LOGIN"));
        System.out.println("ALL_PRIVILEGES_USER_PASSWORD: " + dotenv.get("ALL_PRIVILEGES_USER_PASSWORD"));

        dotenv.entries().forEach(entry -> System.setProperty(entry.getKey(), entry.getValue()));
        SpringApplication.run(MicroserviceUsersApplication.class, args);
    }
}
