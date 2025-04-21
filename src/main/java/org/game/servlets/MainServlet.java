package org.game.servlets;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.game.entities.SForm;
import org.game.model.ResponseData;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

@WebServlet("/game")
public class MainServlet extends HttpServlet {

    private final Gson gson = new GsonBuilder().setPrettyPrinting().create();

    private static final EntityManagerFactory entityManagerFactory =
            Persistence.createEntityManagerFactory("messages-persistence-unit");


    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        EntityManager em = entityManagerFactory.createEntityManager();
        List<SForm> formData = em.createQuery("SELECT f FROM SForm f", SForm.class).getResultList();
        em.close();
        String jsonResponse = gson.toJson(formData);
        try (PrintWriter writer = response.getWriter()) {
            writer.print(jsonResponse);
            writer.flush();
        }

    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        //Присвоение типа контента
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        //1) Чтение тела Json из реквеста
        StringBuilder builder = new StringBuilder();
        try (BufferedReader reader = request.getReader()) {
            String line;
            while ((line = reader.readLine()) != null) {
                builder.append(line);
            }
        }
        String requestBody = builder.toString();
        System.out.printf("Полученный Json: %s", requestBody);

        //2) Преобразование Джейсона в java entity
        SForm requestData = gson.fromJson(requestBody, SForm.class);

        System.out.println(requestData.toString());

        //3) Реализация логики сервера
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        entityManager.getTransaction().begin();
        entityManager.merge(requestData);
        entityManager.getTransaction().commit();
        entityManager.close();
        entityManagerFactory.close();

        //4) Отправка ответа клиенту
        ResponseData responseData = new ResponseData("OK", "Ваши данные сохранены");

        //5) Java-Objekt -> JSON
        String jsonResponse = gson.toJson(responseData);

        //6) send response to client as JSON
        try (PrintWriter writer = response.getWriter()) {
            writer.print(jsonResponse);
            writer.flush();
        }
    }

    @Override
    public void destroy() {
        if (entityManagerFactory != null) {
            entityManagerFactory.close();
        }
    }
}
