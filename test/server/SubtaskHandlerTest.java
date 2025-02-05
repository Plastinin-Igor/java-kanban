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

public class SubtaskHandlerTest {
    TaskManager taskManager = new InMemoryTaskManager();
    HttpTaskServer httpTaskServer = new HttpTaskServer(taskManager);
    Gson gson = Managers.getGson();

    SubtaskHandlerTest() throws IOException {
    }

    @BeforeEach
    void init() {
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
     * Добавление подзадачи POST /subtask
     */
    @Test
    void testAddSubtask() throws IOException, IntersectException, InterruptedException {
        System.out.println("Добавление подзадачи POST: /subtask");
        // Добавляем эпик и в нем подзадачу
        Epic epic = new Epic("Epic name", "Epic name");
        int epicId = taskManager.addNewEpic(epic).getTaskId();
        // Добавляем подзадачу
        Subtask subtask = new Subtask("Subtask name", "Subtask name", Status.NEW, epicId,
                Duration.ofMinutes(10), LocalDateTime.now());
        String subtaskJson = gson.toJson(subtask);

        //HTTP-клиент и зпрос
        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/subtasks");
        HttpRequest request = HttpRequest.newBuilder()
                .uri(url)
                .POST(HttpRequest.BodyPublishers.ofString(subtaskJson))
                .build();

        // Вызываем рест
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(201, response.statusCode(), "Код ответа должен быть 201");

        // Проверяем, что создалась одна задача
        Collection<Subtask> subtaskList = taskManager.getListSubtask();

        assertNotNull(subtaskList, "Задача не создана");
        assertEquals(1, subtaskList.size(), "Некорректное количество задач");
        assertEquals("Subtask name", taskManager.getSubtaskById(2).getTaskName(), "Некорректное имя задачи");
    }


    /**
     * Изменение подзадачи POST: /subtasks{id}
     */
    @Test
    void testUpdateSubtask() throws IOException, IntersectException, InterruptedException {
        System.out.println("Исправление подзадачи POST: /subtask{id}");
        // Добавляем эпик и в нем подзадачу
        Epic epic = new Epic("Epic name", "Epic name");
        int epicId = taskManager.addNewEpic(epic).getTaskId();
        // Добавляем подзадачу
        Subtask subtask = new Subtask("Subtask name", "Subtask name", Status.NEW, epicId,
                Duration.ofMinutes(10), LocalDateTime.now());
        String subtaskJson = gson.toJson(subtask);

        //HTTP-клиент и зпрос
        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/subtasks");
        HttpRequest request = HttpRequest.newBuilder()
                .uri(url)
                .POST(HttpRequest.BodyPublishers.ofString(subtaskJson))
                .build();

        // Вызываем рест
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(201, response.statusCode(), "Код ответа должен быть 201");

        //Вносим изменение в подзадачу (поменяем статс задачи с NEW на DONE)
        Subtask subtaskUpd = taskManager.getSubtaskById(2);
        subtaskUpd.setTaskStatus(Status.DONE);
        String taskJsonUpd = gson.toJson(subtaskUpd);

        //HTTP-клиент и зпрос
        HttpClient clientUpd = HttpClient.newHttpClient();
        URI urlUpd = URI.create("http://localhost:8080/subtasks/1");
        HttpRequest requestUpd = HttpRequest.newBuilder()
                .uri(urlUpd)
                .POST(HttpRequest.BodyPublishers.ofString(taskJsonUpd))
                .build();
        // Вызываем рест
        HttpResponse<String> responseUpd = clientUpd.send(requestUpd, HttpResponse.BodyHandlers.ofString());
        assertEquals(201, responseUpd.statusCode(), "Код ответа должен быть 200");
        assertEquals(Status.DONE, taskManager.getSubtaskById(2).getTaskStatus(),
                "Обновленный статуст должен быть DONE");
        assertEquals(Status.DONE, taskManager.getEpicById(epicId).getTaskStatus(),
                "После обновления статуса у подзадачи, статус эпика не изменился");
    }

    /**
     * Получение подзадачи GET: /subtasks
     */
    @Test
    void testGetSubtask() throws IOException, InterruptedException {
        System.out.println("Получение списка подзадач GET: /subtask");
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
        URI url = URI.create("http://localhost:8080/subtasks");
        HttpRequest request = HttpRequest.newBuilder()
                .uri(url)
                .GET()
                .build();

        // Вызываем рест
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(200, response.statusCode(), "Код ответа должен быть 200");

        // Десириализуем json в список объетов
        String jsonResponse = response.body();
        Type listType = new TypeToken<List<Subtask>>() {
        }.getType();
        List<Subtask> subtaskList = gson.fromJson(jsonResponse, listType);
        assertNotNull(subtaskList, "Список задач пуст, десириализация не выполнена или задачи не добавлены");
        assertEquals(2, subtaskList.size(), "Неверное количество задач в списке");
    }

    /**
     * Получение подзадачи по id GET: /subtasks/{id}
     */
    @Test
    void testGetTaskId() throws IOException, InterruptedException {
        System.out.println("Получение подзадачи по id GET: /subtasks");
        // Добавляем эпик и в нем подзадачу
        Epic epic = new Epic("Epic name", "Epic name");
        int epicId = taskManager.addNewEpic(epic).getTaskId();
        // Добавляем подзадачу
        Subtask subtask1 = new Subtask("Subtask name", "Subtask name", Status.NEW, epicId,
                Duration.ofMinutes(10), LocalDateTime.now());

        int id = taskManager.addNewSubtask(subtask1).getTaskId();

        //Запросим задачу через HTTP-клиент
        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/subtasks/" + id);
        HttpRequest request = HttpRequest.newBuilder()
                .uri(url)
                .GET()
                .build();

        // Вызываем рест, отвечающий за получение задачи
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(200, response.statusCode(), "Код ответа должен быть 200");
        // Десиарилизация в объект
        Subtask subtask = gson.fromJson(response.body(), Subtask.class);
        assertEquals(subtask1.getTaskName(), subtask.getTaskName(), "Наименование задачи не совпадает");
    }

    /**
     * Получение подзадачи по несуществующему id GET: /subtasks/{id}
     * получение сообщение с кодом 404 - no data found
     */
    @Test
    void testGetSubtaskNonExistentId() throws IOException, InterruptedException {
        System.out.println("Получение подзадачи по несуществующему id GET: /subtasks");
        // Добавляем эпик и в нем подзадачу
        Epic epic = new Epic("Epic name", "Epic name");
        int epicId = taskManager.addNewEpic(epic).getTaskId();
        // Добавляем подзадачу
        Subtask subtask1 = new Subtask("Subtask name", "Subtask name", Status.NEW, epicId,
                Duration.ofMinutes(10), LocalDateTime.now());

        taskManager.addNewSubtask(subtask1);

        //Запросим задачу через HTTP-клиент
        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/subtasks/" + 777);
        HttpRequest request = HttpRequest.newBuilder()
                .uri(url)
                .GET()
                .build();

        // Вызываем рест, отвечающий за получение задачи
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(404, response.statusCode(), "Код ответа должен быть 404");
    }

    /**
     * Удаление подзадачи DELETE /subtask{id}
     */
    @Test
    void testDeleteSubtask() throws IOException, InterruptedException {
        System.out.println("Удаление подзадачи DELETE: /subtasks");
        // Добавляем эпик и в нем подзадачу
        Epic epic = new Epic("Epic name", "Epic name");
        int epicId = taskManager.addNewEpic(epic).getTaskId();
        // Добавляем подзадачу
        Subtask subtask1 = new Subtask("Subtask name", "Subtask name", Status.NEW, epicId,
                Duration.ofMinutes(10), LocalDateTime.now());

        int id = taskManager.addNewSubtask(subtask1).getTaskId();
        assertNotNull(taskManager.getSubtaskById(id), "Подзадача не добавлена");

        //Удалим задачу через HTTP-клиент
        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/subtasks/" + id);
        HttpRequest request = HttpRequest.newBuilder()
                .uri(url)
                .DELETE()
                .build();

        // Вызываем рест, отвечающий за удаление задачи
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(200, response.statusCode(), "Код ответа должен быть 200");

        assertNull(taskManager.getSubtaskById(id), "Подзадача не удалена");
    }

    /**
     * Добвление подзадачи которая пересекатеся с существующими
     * Получение кода сообщения 406
     * POST /subtask
     */
    @Test
    void testAddSubtaskIntersect() throws IOException, IntersectException, InterruptedException {
        System.out.println("Получение код сообщения 406 при добавлении подзадачи");
        // Добавляем эпик и в нем подзадачу
        Epic epic = new Epic("Epic name", "Epic name");
        int epicId = taskManager.addNewEpic(epic).getTaskId();
        // Добавляем подзадачу
        Subtask subtask1 = new Subtask("Subtask name", "Subtask name", Status.NEW, epicId,
                Duration.ofMinutes(10), LocalDateTime.now());

        Subtask subtask2 = new Subtask("Subtask name", "Subtask name", Status.NEW, epicId,
                Duration.ofMinutes(10), LocalDateTime.now());

        taskManager.addNewSubtask(subtask1);
        String subtaskJson = gson.toJson(subtask2);

        //HTTP-клиент и зпрос
        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/subtasks");
        HttpRequest request = HttpRequest.newBuilder()
                .uri(url)
                .POST(HttpRequest.BodyPublishers.ofString(subtaskJson))
                .build();

        // Вызываем рест
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(406, response.statusCode(), "Код ответа должен быть 406");
    }

}
