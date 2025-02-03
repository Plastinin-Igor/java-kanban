package server;

import com.sun.net.httpserver.HttpServer;
import model.TaskManager;
import util.Managers;

import java.io.IOException;
import java.net.InetSocketAddress;

public class HttpTaskServer {
    public static final int PORT = 8080;
    private static final TaskManager taskManager = Managers.getDefault();

    public static void main(String[] args) throws IOException {
        HttpServer httpServer = HttpServer.create();

        httpServer.bind(new InetSocketAddress(PORT), 0); // связываем сервер с сетевым портом
        httpServer.createContext("/", new TaskHandler(taskManager)); // связываем путь и обработчик
        httpServer.start();

        System.out.println("HTTP-сервер запущен на " + PORT + " порту!");
    }

}
