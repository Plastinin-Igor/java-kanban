package server;

import com.sun.net.httpserver.HttpServer;
import model.Status;
import model.Task;
import model.TaskManager;
import util.Managers;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.time.Duration;
import java.time.LocalDateTime;

public class HttpTaskServer {
    public static final int PORT = 8080;
    private static TaskManager taskManager;
    private static final HttpServer httpServer;

    static {
        try {
            httpServer = HttpServer.create();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public HttpTaskServer() throws IOException {
        taskManager = Managers.getDefault();
    }

    public HttpTaskServer(TaskManager taskManager) throws IOException {
        HttpTaskServer.taskManager = taskManager;
    }

    public static void start() throws IOException {
        httpServer.bind(new InetSocketAddress(PORT), 0); // связываем сервер с сетевым портом
        httpServer.createContext("/", new TaskHandler(taskManager)); // связываем путь и обработчик
        httpServer.start();
        System.out.println("HTTP-сервер запущен на " + PORT + " порту!");
        System.out.println("http://localhost:" + PORT + "/");
    }

    public static void stop() {
        httpServer.stop(0);
        System.out.println("HTTP-сервер отстановлен на " + PORT + " порту!");
    }

    public static void main(String[] args) throws IOException {
        Task task0 = new Task("Задача 1", "Задача 1", Status.NEW, Duration.ofMinutes(10), LocalDateTime.of(2025, 1, 1, 10, 0));
        Task task1 = new Task("Задача 2", "Задача 2", Status.NEW, Duration.ofMinutes(10), LocalDateTime.of(2025, 1, 1, 10, 20));
        Task task2 = new Task("Задача 3", "Задача 3", Status.NEW, Duration.ofMinutes(10), LocalDateTime.of(2025, 1, 1, 10, 30));

        taskManager = Managers.getDefault();
        taskManager.addNewTask(task0);
        taskManager.addNewTask(task1);
        taskManager.addNewTask(task2);

        start();
    }

}
