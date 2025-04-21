package org.game.entities;


import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.testcontainers.containers.MySQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertEquals;

@Testcontainers
public class SFormPersistenceTest {

    @Container
    private static final MySQLContainer<?> mysql = new MySQLContainer<>("mysql:8.0")
            .withDatabaseName("messages")
            .withUsername("user")
            .withPassword("admin");

    private static EntityManagerFactory emf;

    @BeforeAll
    public static void setUp() {
        emf = Persistence.createEntityManagerFactory("messages-persistence-unit-test", getJpaProperties());
    }

    private static Map<String, String> getJpaProperties() {
        return Map.of(
                "jakarta.persistence.jdbc.url", mysql.getJdbcUrl(),
                "jakarta.persistence.jdbc.user", mysql.getUsername(),
                "jakarta.persistence.jdbc.password", mysql.getPassword(),
                "jakarta.persistence.jdbc.driver", "com.mysql.cj.jdbc.Driver",
                "hibernate.hbm2ddl.auto", "create-drop",
                "hibernate.dialect", "org.hibernate.dialect.MySQLDialect",
                "hibernate.show_sql", "true"
        );
    }

    @AfterAll
    public static void tearDown() {
        if (emf != null) {
            emf.close();
        }
    }

    @Test
    public void testSaveFormEntity() {
        EntityManager entityManager = emf.createEntityManager();
        entityManager.getTransaction().begin();

        SForm form = new SForm("Test User", "test@example.com", "Hello from test!");
        entityManager.persist(form);

        entityManager.getTransaction().commit();

        SForm loaded = entityManager.find(SForm.class, form.getUuid());
        assertNotNull(loaded);
        assertEquals("Test User", loaded.getUsername());

        entityManager.close();
    }
}
