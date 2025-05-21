package ru.pin36bik.service;

import java.time.LocalDateTime;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.pin36bik.repository.PresetAnalyticsRepository;
import ru.pin36bik.repository.UserAnalyticsRepository;

@Service
@RequiredArgsConstructor
public class KafkaConsumerService {

    private final PresetAnalyticsRepository presetRepository;
    private final UserAnalyticsRepository userRepository;

    @KafkaListener(topics = "preset-download-topic",
            groupId = "test-consumer-group")
    @Transactional
    public LocalDateTime recordPresetDownload(
            final String presetName) {
        System.out.println("Received preset name: " + presetName);
        LocalDateTime now = LocalDateTime.now();
        presetRepository.incrementDownloadsNumber(presetName, now);
        return now;
    }

    @KafkaListener(topics = "user-login-topic",
            groupId = "test-consumer-group")
    @Transactional
    public LocalDateTime recordUserLogin(
            final Long userId) {
        System.out.println("Received user ID: " + userId);
        LocalDateTime now = LocalDateTime.now();
        userRepository.incrementLoginCount(userId, now);
        return now;
    }
}
