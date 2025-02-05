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
    private final HttpServer httpServer;
    private final TaskManager taskManager;


    public HttpTaskServer(TaskManager taskManager) throws IOException {
        this.taskManager = taskManager;
        httpServer = HttpServer.create(new InetSocketAddress(PORT), 0);
        httpServer.createContext("/", new TaskHandler(taskManager));
    }

    public HttpTaskServer() throws IOException {
        this.taskManager = Managers.getDefault();
        httpServer = HttpServer.create(new InetSocketAddress(PORT), 0);
        httpServer.createContext("/", new TaskHandler(taskManager));
    }

    public void start() {
        System.out.println("HTTP-сервер запущен на " + PORT + " порту!");
        System.out.println("http://localhost:" + PORT + "/");
        httpServer.start();
    }

    public void stop() {
        httpServer.stop(0);
        System.out.println("HTTP-сервер отстановлен на " + PORT + " порту!");
    }


    public static void main(String[] args) throws IOException {
        HttpTaskServer httpTaskServer = new HttpTaskServer();

        //Инициализируем тестовыми данными:
        Task task0 = new Task("Задача 1", "Задача 1", Status.NEW, Duration.ofMinutes(10),
                LocalDateTime.of(2025, 1, 1, 10, 0));
        Task task1 = new Task("Задача 2", "Задача 2", Status.NEW, Duration.ofMinutes(10),
                LocalDateTime.of(2025, 1, 1, 10, 20));
        Task task2 = new Task("Задача 3", "Задача 3", Status.NEW, Duration.ofMinutes(10),
                LocalDateTime.of(2025, 1, 1, 10, 30));

        httpTaskServer.taskManager.addNewTask(task0);
        httpTaskServer.taskManager.addNewTask(task1);
        httpTaskServer.taskManager.addNewTask(task2);

        //Запускаем сервер
        httpTaskServer.start();
    }


}
