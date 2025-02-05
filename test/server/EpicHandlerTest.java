package server;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import exceptions.IntersectException;
import model.*;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import service.InMemoryTaskManager;
import util.*;

import java.io.IOException;
import java.lang.reflect.Type;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class EpicHandlerTest {
    TaskManager taskManager = new InMemoryTaskManager();
    HttpTaskServer httpTaskServer = new HttpTaskServer(taskManager);
    Gson gson = Managers.getGson();

    EpicHandlerTest() throws IOException {
    }

    @BeforeEach
    void init() throws IOException {
        taskManager.deleteTask();
        taskManager.deleteSubtask();
        taskManager.deleteEpic();

        httpTaskServer.start();
    }

    @AfterEach
    void tearDown() {
        httpTaskServer.stop();
    }

    /**
     * Добавление эпика POST /epics
     */
    @Test
    void testAddEpic() throws IOException, IntersectException, InterruptedException {
        System.out.println("Добавление эпика POST: /epics");
        // Добавляем эпик
        Epic epic = new Epic("Epic name", "Epic name");
        String epicJson = gson.toJson(epic);

        //HTTP-клиент и зпрос
        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/epics");
        HttpRequest request = HttpRequest.newBuilder()
                .uri(url)
                .POST(HttpRequest.BodyPublishers.ofString(epicJson))
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(201, response.statusCode(), "Код ответа должен быть 201");

        // Проверяем, что создался один эпик
        Collection<Epic> listEpic = taskManager.getListEpic();

        assertNotNull(listEpic, "Задача не создана");
        assertEquals(1, listEpic.size(), "Некорректное количество задач");
        assertEquals("Epic name", taskManager.getEpicById(1).getTaskName(), "Некорректное имя задачи");
    }

    /**
     * Получение эпиков GET: /epics
     */
    @Test
    void testGetEpic() throws IOException, InterruptedException {
        System.out.println("Получение списка эпиков GET: /epics");
        //Добавляем эпики
        Epic epic1 = new Epic("Epic name", "Epic name");
        Epic epic2 = new Epic("Epic name", "Epic name");

        taskManager.addNewEpic(epic1);
        taskManager.addNewEpic(epic2);

        //Запросим задачу через HTTP-клиент
        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/epics");
        HttpRequest request = HttpRequest.newBuilder()
                .uri(url)
                .GET()
                .build();

        // Вызываем рест, отвечающий за получение списка задач
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(200, response.statusCode(), "Код ответа должен быть 200");

        // Десириализуем json в список объетов
        String jsonResponse = response.body();
        Type listType = new TypeToken<List<Epic>>() {
        }.getType();
        List<Epic> epicList = gson.fromJson(jsonResponse, listType);
        assertNotNull(epicList, "Список эпиков пуст, десириализация не выполнена или задачи не добавлены");
        assertEquals(2, epicList.size(), "Неверное количество эпиков в списке");
    }

    /**
     * Получение эпика по id GET: /epics/{id}
     */
    @Test
    void testGetEpicId() throws IOException, InterruptedException {
        System.out.println("Получение эпика по id GET: /epics");
        //Добавляем эпики
        Epic epic1 = new Epic("Epic name", "Epic name");
        int id = taskManager.addNewEpic(epic1).getTaskId();

        //Запросим задачу через HTTP-клиент
        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/epics/" + id);
        HttpRequest request = HttpRequest.newBuilder()
                .uri(url)
                .GET()
                .build();

        // Вызываем рест, отвечающий за получение задачи
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(200, response.statusCode(), "Код ответа должен быть 200");
        // Десиарилизация в объект
        Epic getEpic = gson.fromJson(response.body(), Epic.class);
        assertEquals(epic1.getTaskName(), getEpic.getTaskName(), "Наименование задачи не совпадает");
    }

    /**
     * Получение эпика по несуществующему id GET: /epics/{id}
     * получение сообщение с кодом 404 - no data found
     */
    @Test
    void testGetEpicExistentId() throws IOException, InterruptedException {
        System.out.println("Получение эпика по несуществующему id GET: /epics");
        //Добавляем эпики
        Epic epic1 = new Epic("Epic name", "Epic name");
        taskManager.addNewEpic(epic1);

        //Запросим задачу через HTTP-клиент
        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/epics/" + 777);
        HttpRequest request = HttpRequest.newBuilder()
                .uri(url)
                .GET()
                .build();

        // Вызываем рест, отвечающий за получение задачи
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(404, response.statusCode(), "Код ответа должен быть 404");
    }

    /**
     * Удаление эпика DELETE /epics{id}
     */
    @Test
    void testDeleteEpic() throws IOException, InterruptedException {
        System.out.println("Удаление эпика DELETE: /epics");
        //Добавляем эпики
        Epic epic1 = new Epic("Epic name", "Epic name");
        int id = taskManager.addNewEpic(epic1).getTaskId();
        assertNotNull(taskManager.getEpicById(id), "Задача не добавлена");

        //Удалим задачу через HTTP-клиент
        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/epics/" + id);
        HttpRequest request = HttpRequest.newBuilder()
                .uri(url)
                .DELETE()
                .build();

        // Вызываем рест, отвечающий за удаление задачи
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(200, response.statusCode(), "Код ответа должен быть 200");

        assertNull(taskManager.getEpicById(id), "Эпик не удален");
    }

    /**
     * Получение списка подзадач по id эпика
     * GET: /epics{id}/subtask
     */
    @Test
    void testGetEpicSubtask() throws IOException, InterruptedException {
        // Добавляем эпик и в нем подзадачу
        Epic epic = new Epic("Epic name", "Epic name");
        int epicId = taskManager.addNewEpic(epic).getTaskId();
        // Добавляем подзадачу
        Subtask subtask1 = new Subtask("Subtask name", "Subtask name", Status.NEW, epicId,
                Duration.ofMinutes(10), LocalDateTime.now());
        Subtask subtask2 = new Subtask("Subtask name", "Subtask name", Status.NEW, epicId,
                Duration.ofMinutes(10), LocalDateTime.now().plusMinutes(10));
        taskManager.addNewSubtask(subtask1);
        taskManager.addNewSubtask(subtask2);

        //Запросим задачу через HTTP-клиент
        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/epics/" + epicId + "/subtasks");
        HttpRequest request = HttpRequest.newBuilder()
                .uri(url)
                .GET()
                .build();

        // Вызываем рест, отвечающий за получение списка задач
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(200, response.statusCode(), "Код ответа должен быть 200");

        // Десириализуем json в список объетов
        String jsonResponse = response.body();
        Type listType = new TypeToken<List<Subtask>>() {
        }.getType();
        List<Subtask> epicList = gson.fromJson(jsonResponse, listType);
        assertNotNull(epicList, "Список подзадач эпика пуст, десириализация не выполнена или эпики/подзадачи не добавлены");
        assertEquals(2, epicList.size(), "Неверное количество подзадач в списке");
    }

}
