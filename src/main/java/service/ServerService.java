package service;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;
import ru.pin36bik.dto.PresetDTO;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

@SpringBootApplication(exclude = {
        org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration.class,
        org.springframework.boot.autoconfigure.orm.jpa.HibernateJpaAutoConfiguration.class,
        org.springframework.boot.autoconfigure.r2dbc.R2dbcAutoConfiguration.class
})
public class ServerService {

    @Configuration
    public static class KafkaConfig {

        @Bean
        public NewTopic presetRequestTopic() {
            return new NewTopic("preset-request-topic", 1, (short) 1);
        }

        @Bean
        public NewTopic presetResponseTopic() {
            return new NewTopic("preset-response-topic", 1, (short) 1);
        }
    }

    public static void main(String[] args) throws InterruptedException {
        ConfigurableApplicationContext context = SpringApplication.run(ServerService.class, args);

        PresetCoordinator coordinator = context.getBean(PresetCoordinator.class);
        coordinator.orchestratePresetTransfer("Home Mode");
    }

    @Component
    public static class PresetCoordinator {
        private final KafkaTemplate<String, String> requestTemplate;
        private final KafkaTemplate<String, PresetDTO> responseTemplate;
        private final CountDownLatch latch = new CountDownLatch(1);
        private PresetDTO receivedPreset;

        public PresetCoordinator(KafkaTemplate<String, String> requestTemplate,
                                 KafkaTemplate<String, PresetDTO> responseTemplate) {
            this.requestTemplate = requestTemplate;
            this.responseTemplate = responseTemplate;
        }

        public void orchestratePresetTransfer(String presetName) throws InterruptedException {
            System.out.println("=== Preset transmission beginned ===");

            // 1. Отправка запроса в user-management
            System.out.println("[Coordinator] Sending request for preset: " + presetName);
            requestTemplate.send("preset-request-topic", presetName);

            // 2. Ожидаем ответа от preset-service
            if (latch.await(10, TimeUnit.SECONDS)) {
                System.out.println("\n[Coordinator] Preset is successfully received!");
                System.out.println("Preset data: " + receivedPreset);
                System.out.println("=== Preset transmission is successfully finished ===");
            } else {
                System.out.println("[Coordinator] Error: preset waiting is timed out");
            }
        }

        @KafkaListener(topics = "preset-response-topic", groupId = "coordinator-group")
        public void handlePresetResponse(PresetDTO preset) {
            System.out.println("[Coordinator] Received a preset from preset-service: " + preset);
            this.receivedPreset = preset;
            latch.countDown();
        }
    }
}