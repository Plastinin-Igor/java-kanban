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

class TaskHandlerTest {
    TaskManager taskManager = new InMemoryTaskManager();
    HttpTaskServer httpTaskServer = new HttpTaskServer(taskManager);
    Gson gson = Managers.getGson();

    TaskHandlerTest() throws IOException {
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
     * Добавление задачи POST /tasks
     */
    @Test
    void testAddTask() throws IOException, IntersectException, InterruptedException {
        System.out.println("Добавление задачи POST: /tasks");
        Task task = new Task("Task name", "Task description", Status.NEW,
                Duration.ofMinutes(5), LocalDateTime.now());
        String taskJson = gson.toJson(task);

        //HTTP-клиент и зпрос
        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/tasks");
        HttpRequest request = HttpRequest.newBuilder()
                .uri(url)
                .POST(HttpRequest.BodyPublishers.ofString(taskJson))
                .build();

        // Вызываем рест, отвечающий за создание задач
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(201, response.statusCode(), "Код ответа должен быть 201");

        // Проверяем, что создалась одна задача
        Collection<Task> taskList = taskManager.getListTask();

        assertNotNull(taskList, "Задача не создана");
        assertEquals(1, taskList.size(), "Некорректное количество задач");
        assertEquals("Task name", taskManager.getTaskById(1).getTaskName(), "Некорректное имя задачи");
    }

    /**
     * Изменение задачи POST: /tasks{id}
     */
    @Test
    void testUpdateTask() throws IOException, IntersectException, InterruptedException {
        System.out.println("Исправление задачи POST: /tasks{id}");

        //Добавляем новую задачу
        Task task = new Task("Task name", "Task description", Status.NEW,
                Duration.ofMinutes(5), LocalDateTime.now());
        String taskJson = gson.toJson(task);

        //HTTP-клиент и зпрос
        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/tasks");
        HttpRequest request = HttpRequest.newBuilder()
                .uri(url)
                .POST(HttpRequest.BodyPublishers.ofString(taskJson))
                .build();

        // Вызываем рест, отвечающий за создание задач
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(201, response.statusCode(), "Код ответа должен быть 201");

        //Вносим изменение в задачу (поменяем статс задачи с NEW на DONE)
        Task taskUpd = taskManager.getTaskById(1);
        taskUpd.setTaskStatus(Status.DONE);
        String taskJsonUpd = gson.toJson(taskUpd);

        //HTTP-клиент и зпрос
        HttpClient clientUpd = HttpClient.newHttpClient();
        URI urlUpd = URI.create("http://localhost:8080/tasks/1");
        HttpRequest requestUpd = HttpRequest.newBuilder()
                .uri(urlUpd)
                .POST(HttpRequest.BodyPublishers.ofString(taskJsonUpd))
                .build();
        // Вызываем рест, отвечающий за обновление задач
        HttpResponse<String> responseUpd = clientUpd.send(requestUpd, HttpResponse.BodyHandlers.ofString());
        assertEquals(201, responseUpd.statusCode(), "Код ответа должен быть 200");
        assertEquals(Status.DONE, taskManager.getTaskById(1).getTaskStatus(),
                "Обновленный статуст должен быть DONE");
    }

    /**
     * Получение задачи GET: /tasks
     */
    @Test
    void testGetTask() throws IOException, InterruptedException {
        System.out.println("Получение списка задач GET: /tasks");
        //Добавляем задачу
        Task task1 = new Task("Task name", "Task description", Status.NEW,
                Duration.ofMinutes(5), LocalDateTime.now());
        Task task2 = new Task("Task name 2", "Task description 2", Status.NEW,
                Duration.ofMinutes(5), LocalDateTime.now().plusMinutes(10));

        taskManager.addNewTask(task1);
        taskManager.addNewTask(task2);

        //Запросим задачу через HTTP-клиент
        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/tasks");
        HttpRequest request = HttpRequest.newBuilder()
                .uri(url)
                .GET()
                .build();

        // Вызываем рест, отвечающий за получение списка задач
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(200, response.statusCode(), "Код ответа должен быть 200");

        // Десириализуем json в список объетов
        String jsonResponse = response.body();
        Type listType = new TypeToken<List<Task>>() {
        }.getType();
        List<Task> taskList = gson.fromJson(jsonResponse, listType);
        assertNotNull(taskList, "Список задач пуст, десириализация не выполнена или задачи не добавлены");
        assertEquals(2, taskList.size(), "Неверное количество задач в списке");
    }

    /**
     * Получение задачи по id GET: /tasks/{id}
     */
    @Test
    void testGetTaskId() throws IOException, InterruptedException {
        System.out.println("Получение задачи по id GET: /tasks");
        //Добавляем задачу
        Task task1 = new Task("Task name", "Task description", Status.NEW,
                Duration.ofMinutes(5), LocalDateTime.now());
        int id1 = taskManager.addNewTask(task1).getTaskId();

        //Запросим задачу через HTTP-клиент
        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/tasks/" + id1);
        HttpRequest request = HttpRequest.newBuilder()
                .uri(url)
                .GET()
                .build();

        // Вызываем рест, отвечающий за получение задачи
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(200, response.statusCode(), "Код ответа должен быть 200");
        // Десиарилизация в объект
        Task getTask = gson.fromJson(response.body(), Task.class);
        assertEquals(task1.getTaskName(), getTask.getTaskName(), "Наименование задачи не совпадает");
    }

    /**
     * Получение задачи по несуществующему id GET: /tasks/{id}
     * получение сообщение с кодом 404 - no data found
     */
    @Test
    void testGetTaskNonExistentId() throws IOException, InterruptedException {
        System.out.println("Получение задачи по несуществующему id GET: /tasks");
        //Добавляем задачу
        Task task1 = new Task("Task name", "Task description", Status.NEW,
                Duration.ofMinutes(5), LocalDateTime.now());
        taskManager.addNewTask(task1);

        //Запросим задачу через HTTP-клиент
        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/tasks/" + 777);
        HttpRequest request = HttpRequest.newBuilder()
                .uri(url)
                .GET()
                .build();

        // Вызываем рест, отвечающий за получение задачи
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(404, response.statusCode(), "Код ответа должен быть 404");
    }

    /**
     * Удаление задачи DELETE /task{id}
     */
    @Test
    void testDeleteTask() throws IOException, InterruptedException {
        System.out.println("Удаление задачи DELETE: /tasks");
        //Добавляем задачу
        Task task1 = new Task("Task name", "Task description", Status.NEW,
                Duration.ofMinutes(5), LocalDateTime.now());
        int id = taskManager.addNewTask(task1).getTaskId();
        assertNotNull(taskManager.getTaskById(id), "Задача не добавлена");

        //Удалим задачу через HTTP-клиент
        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/tasks/" + id);
        HttpRequest request = HttpRequest.newBuilder()
                .uri(url)
                .DELETE()
                .build();

        // Вызываем рест, отвечающий за удаление задачи
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(200, response.statusCode(), "Код ответа должен быть 200");

        assertNull(taskManager.getTaskById(id), "Задача не удалена");
    }

    /**
     * Добвление задачи которая пересекатеся с существующими
     * Получение кода сообщения 406
     * POST: /task
     */
    @Test
    void testAddTaskIntersect() throws IOException, InterruptedException {
        System.out.println("Получение код сообщения 406 при добавлении задачи");
        //Добавляем задачу
        Task task1 = new Task("Task name", "Task description", Status.NEW,
                Duration.ofMinutes(5), LocalDateTime.now());
        Task task2 = new Task("Task name 2", "Task description 2", Status.NEW,
                Duration.ofMinutes(5), LocalDateTime.now());

        taskManager.addNewTask(task1);

        String taskJson = gson.toJson(task2);
        //HTTP-клиент и зпрос - попытаемся добавить задачу, которая пересекается по времени с уже существующей
        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/tasks");
        HttpRequest request = HttpRequest.newBuilder()
                .uri(url)
                .POST(HttpRequest.BodyPublishers.ofString(taskJson))
                .build();

        // Вызываем рест, отвечающий за создание задач
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(406, response.statusCode(), "Код ответа должен быть 406");
    }

}