package server;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
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

public class HistoryHandlerTest {

    TaskManager taskManager = new InMemoryTaskManager();
    HttpTaskServer httpTaskServer = new HttpTaskServer(taskManager);

    HistoryHandlerTest() throws IOException {
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
     * Запрос к истории
     * GET: /history
     */
    @Test
    void testGetHistory() throws IOException, InterruptedException {
        System.out.println("Запрос к истории GET: /history");
        //Добавим задачу, подзадачу, эпик
        Task task = new Task("Task name", "Task description", Status.NEW,
                Duration.ofMinutes(5), LocalDateTime.now());
        int taskId = taskManager.addNewTask(task).getTaskId();
        // Добавляем эпик и в нем подзадачу
        Epic epic = new Epic("Epic name", "Epic name");
        int epicId = taskManager.addNewEpic(epic).getTaskId();
        // Добавляем подзадачу
        Subtask subtask1 = new Subtask("Subtask name", "Subtask name", Status.NEW, epicId,
                Duration.ofMinutes(10), LocalDateTime.now().plusMinutes(10));
        int subtaskId = taskManager.addNewSubtask(subtask1).getTaskId();

        //Заполним историю просмотров
        taskManager.getTaskById(taskId);
        taskManager.getEpicById(epicId);
        taskManager.getSubtaskById(subtaskId);

        //Запросим задачу через HTTP-клиент
        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/history");
        HttpRequest request = HttpRequest.newBuilder()
                .uri(url)
                .GET()
                .build();

        // Вызываем рест
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(200, response.statusCode(), "Код ответа должен быть 200");

        // Десириализуем json в список объетов
        String jsonResponse = response.body();

        Gson gsonWithAdapter = new GsonBuilder()
                .registerTypeAdapter(TaskItem.class, new TaskItemTypeAdapter())
                .setPrettyPrinting()
                .create();
        Type listType = new TypeToken<List<TaskItem>>() {
        }.getType();
        List<TaskItem> historyList = gsonWithAdapter.fromJson(jsonResponse, listType);

        assertNotNull(historyList, "Список пустой");
        assertEquals(3, historyList.size(), "В истории должно быть 3 просмотра");

    }

}
