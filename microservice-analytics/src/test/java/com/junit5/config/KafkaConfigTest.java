package com.junit5.config;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.serialization.LongDeserializer;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.junit.jupiter.api.Test;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import ru.pin36bik.config.KafkaConfig;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class KafkaConfigTest {

    @Test
    void testStringConsumerFactoryConfiguration() {
        KafkaConfig kafkaConfig = new KafkaConfig();

        ConsumerFactory<String, String> factory = kafkaConfig.stringConsumerFactory();
        Map<String, Object> configs = factory.getConfigurationProperties();

        assertEquals("localhost:9092", configs.get(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG));
        assertEquals("test-consumer-group", configs.get(ConsumerConfig.GROUP_ID_CONFIG));
        assertEquals(StringDeserializer.class, configs.get(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG));
        assertEquals(StringDeserializer.class, configs.get(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG));
    }

    @Test
    void testLongConsumerFactoryConfiguration() {
        KafkaConfig kafkaConfig = new KafkaConfig();

        ConsumerFactory<Long, Long> factory = kafkaConfig.longConsumerFactory();
        Map<String, Object> configs = factory.getConfigurationProperties();

        assertEquals("localhost:9092", configs.get(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG));
        assertEquals("test-consumer-group", configs.get(ConsumerConfig.GROUP_ID_CONFIG));
        assertEquals(LongDeserializer.class, configs.get(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG));
        assertEquals(LongDeserializer.class, configs.get(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG));
    }

    @Test
    void testStringListenerContainerFactory() {
        KafkaConfig kafkaConfig = new KafkaConfig();

        ConcurrentKafkaListenerContainerFactory<String, String> factory =
                kafkaConfig.kafkaListenerStringContainerFactory();

        assertNotNull(factory);
        assertNotNull(factory.getConsumerFactory());
        assertEquals(StringDeserializer.class,
                factory.getConsumerFactory().getConfigurationProperties()
                        .get(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG));
    }

    @Test
    void testLongListenerContainerFactory() {
        KafkaConfig kafkaConfig = new KafkaConfig();

        ConcurrentKafkaListenerContainerFactory<Long, Long> factory =
                kafkaConfig.kafkaListenerLongContainerFactory();

        assertNotNull(factory);
        assertNotNull(factory.getConsumerFactory());
        assertEquals(LongDeserializer.class,
                factory.getConsumerFactory().getConfigurationProperties()
                        .get(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG));
    }
}
