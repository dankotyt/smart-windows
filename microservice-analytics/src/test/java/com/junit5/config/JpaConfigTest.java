package com.junit5.config;

import jakarta.persistence.EntityManagerFactory;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.pin36bik.config.JpaConfig;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class JpaConfigTest {
    @Test
    void testTransactionManagerCreation() {
        JpaConfig jpaConfig = new JpaConfig();
        var mockEmf = mock(EntityManagerFactory.class);

        var transactionManager = jpaConfig.transactionManager(mockEmf);

        assertNotNull(transactionManager);
    }
}
