package ru.pin36bik.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("API микросервиса аналитики")
                        .version("0.0")
                        .description("API для управления сбором и "
                                + "анализом данных об использовании окон "
                                + "в приложении Smart Windows"));
    }
}
