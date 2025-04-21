package org.game.servlets;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.testcontainers.containers.MySQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.io.BufferedReader;
import java.io.PrintWriter;
import java.io.StringReader;
import java.io.StringWriter;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@Testcontainers
public class MainServletTest {
    @Container
    private static final MySQLContainer<?> mysql = new MySQLContainer<>("mysql:8.0")
            .withDatabaseName("messages")
            .withUsername("user")
            .withPassword("admin");

    private static EntityManagerFactory emf;
    private static MainServlet servlet;
    private static Gson gson = new GsonBuilder().setPrettyPrinting().create();

    @BeforeAll
    public static void setUp() {
        mysql.start();

        emf = Persistence.createEntityManagerFactory("messages-persistence-unit-test", getJpaProperties());

        servlet = new MainServlet();
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
        mysql.stop();
    }

    @Test
    public void testDoPost() throws Exception {

        String json = """
                {
                    "username": "John",
                    "email": "john@example.com",
                    "message": "Hello from test"
                }
                """;

        //mocks JSON request
        HttpServletRequest request = mock(HttpServletRequest.class);
        BufferedReader reader = new BufferedReader(new StringReader(json));
        when(request.getReader()).thenReturn(reader);

        //mocks JSON response
        HttpServletResponse response = mock(HttpServletResponse.class);
        StringWriter responseWriter = new StringWriter();
        when(response.getWriter()).thenReturn(new PrintWriter(responseWriter));


        EntityManager entityManager = emf.createEntityManager();

        MainServlet servlet = new MainServlet() {
            protected EntityManagerFactory getEntityManagerFactory() {
                return emf;
            }
        };


        servlet.doPost(request, response);

        String responseJson = responseWriter.toString();
        assertTrue(responseJson.contains("\"status\":"));
        assertTrue(responseJson.contains("OK"));

        entityManager.close();
        emf.close();
    }

}
