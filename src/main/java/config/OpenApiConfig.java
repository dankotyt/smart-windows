//package config;
//
//import io.swagger.v3.parser.OpenAPIParser;
//import org.springdoc.core.models.GroupedOpenApi;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.core.io.ClassPathResource;
//
//import java.io.IOException;
//import java.io.InputStream;
//
//@Configuration
//public class OpenApiConfig {
//    @Bean
//    public GroupedOpenApi customOpenApi() throws IOException {
//        try (InputStream inputStream = new ClassPathResource("openapi.yaml").getInputStream()) {
//            return GroupedOpenApi.builder()
//                    .group("my-api")
//                    .pathsToMatch("/api/v1/**")
//                    .addOpenApiCustomizer(openApi -> {
//                        openApi = new OpenAPIParser()
//                                .readContents(inputStream, null, null)
//                                .getOpenAPI();
//                    })
//                    .build();
//        }
//    }
//}