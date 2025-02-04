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
    private static final TaskManager taskManager = Managers.getDefault();

    public static void main(String[] args) throws IOException {

        Task task = new Task(0, "Задача 1", "Задача 1", Status.NEW, Duration.ofMinutes(10),
                LocalDateTime.of(2025, 1, 1, 10, 0));
        Task task1 = new Task(0, "Задача 2", "Задача 2", Status.NEW, Duration.ofMinutes(10),
                LocalDateTime.of(2025, 1, 1, 10, 20));
        Task task2 = new Task(0, "Задача 2", "Задача 2", Status.NEW, Duration.ofMinutes(10),
                LocalDateTime.of(2025, 1, 1, 12, 20));
        taskManager.addNewTask(task);
        taskManager.addNewTask(task1);
        taskManager.addNewTask(task2);
        System.out.println(taskManager.getListTask());


        HttpServer httpServer = HttpServer.create();

        httpServer.bind(new InetSocketAddress(PORT), 0); // связываем сервер с сетевым портом
        httpServer.createContext("/", new TaskHandler(taskManager)); // связываем путь и обработчик
        httpServer.start();

        System.out.println("HTTP-сервер запущен на " + PORT + " порту!");
    }

}
