package com.junit5.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.pin36bik.repository.PresetAnalyticsRepository;
import ru.pin36bik.repository.UserAnalyticsRepository;
import ru.pin36bik.service.KafkaConsumerService;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class KafkaConsumerServiceTest {

    @Mock
    private PresetAnalyticsRepository presetRepository;

    @Mock
    private UserAnalyticsRepository userRepository;

    @InjectMocks
    private KafkaConsumerService kafkaConsumerService;

    @Test
    void testRecordPresetDownload() {
        String presetName = "Test Preset";
        LocalDateTime beforeInvocation = LocalDateTime.now();

        LocalDateTime result = kafkaConsumerService.recordPresetDownload(presetName);

        assertNotNull(result, "Returned LocalDateTime should not be null");
        assertFalse(result.isBefore(beforeInvocation), "Returned time should be after or equal to invocation time");

        verify(presetRepository, times(1)).incrementDownloadsNumber(eq(presetName), eq(result));
        verifyNoMoreInteractions(presetRepository);
        verifyNoInteractions(userRepository);
    }

    @Test
    void testRecordUserLogin() {
        Long userId = 123L;
        LocalDateTime beforeInvocation = LocalDateTime.now();

        LocalDateTime result = kafkaConsumerService.recordUserLogin(userId);

        assertNotNull(result, "Returned LocalDateTime should not be null");
        assertFalse(result.isBefore(beforeInvocation), "Returned time should be after or equal to invocation time");

        verify(userRepository, times(1)).incrementLoginCount(eq(userId), eq(result));
        verifyNoMoreInteractions(userRepository);
        verifyNoInteractions(presetRepository);
    }
}