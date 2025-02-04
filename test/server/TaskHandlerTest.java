package server;

import com.google.gson.Gson;
import model.*;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import util.*;

import java.io.IOException;
import java.time.Duration;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class TaskHandlerTest {

    private final Gson gson = Managers.getGson();
    private final TaskManager taskManager = Managers.getDefault();
    private TaskHandler taskHandler;
    Task task;
    Subtask subtask;
    Epic epic;

    @BeforeEach
    void init() throws IOException {

        task = new Task("Задача 1", "Задача 1", Status.NEW, Duration.ofMinutes(10), LocalDateTime.of(2025, 1, 1, 10, 0));

        HttpTaskServer.start();
    }

    @AfterEach
    void tearDown() {
        HttpTaskServer.stop();
    }

    @Test
    void handle() {

        System.out.println("Тестирование Http...");

    }
}