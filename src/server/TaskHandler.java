package server;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import model.Endpoint;
import model.Task;
import model.TaskManager;
import util.LocalDataTimeTypeAdapter;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;

public class TaskHandler implements HttpHandler {
    private static final Charset DEFAULT_CHARSET = StandardCharsets.UTF_8;
    private TaskManager taskManager;


    public TaskHandler(TaskManager taskManager) {
        this.taskManager = taskManager;
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {

        //TODO убрать System.out.println !
        System.out.println("Путь: " + exchange.getRequestURI().getPath() + " Метод: " + exchange.getRequestMethod());

        Endpoint endpoint = getEndpoint(exchange.getRequestURI().getPath(), exchange.getRequestMethod());

        switch (endpoint) {
            case GET_TASKS: {
                writeResponse(exchange, "Это GET_TASKS", 200);
                break;
            }
            case GET_TASKS_ID: {
                writeResponse(exchange, "Это GET_TASKS_ID", 200);
                break;
            }
            case POST_TASKS: {
                //writeResponse(exchange, "Это POST_TASKS", 200);
                handlePostTasks(exchange);
            }
            case DELETE_TASKS: {
                writeResponse(exchange, "Это DELETE_TASKS", 200);
            }
            case GET_SUBTASKS: {
                writeResponse(exchange, "Это GET_SUBTASKS", 200);
            }
            case GET_SUBTASKS_ID: {
                writeResponse(exchange, "Это GET_SUBTASKS_ID", 200);
            }
            case POST_SUBTASKS: {
                writeResponse(exchange, "Это POST_SUBTASKS", 200);
            }
            case DELETE_SUBTASKS: {
                writeResponse(exchange, "Это DELETE_SUBTASKS", 200);
            }
            case GET_EPICS: {
                writeResponse(exchange, "Это GET_EPICS", 200);
            }
            case GET_EPICS_SUBTASKS: {
                writeResponse(exchange, "Это GET_EPICS_SUBTASKS", 200);
            }
            case POST_EPICS: {
                writeResponse(exchange, "Это POST_EPICS", 200);
            }
            case GET_EPICS_ID: {
                writeResponse(exchange, "Это GET_EPICS_ID", 200);
            }
            case DELETE_EPICS: {
                writeResponse(exchange, "Это DELETE_EPICS", 200);
            }
            case GET_HISTORY: {
                writeResponse(exchange, "Это GET_HISTORY", 200);
            }
            case GET_PRIORITIZED: {
                writeResponse(exchange, "Это GET_PRIORITIZED", 200);
            }
            default: {
                writeResponse(exchange, "Неизвестный ендпоинт", 400);
            }
        }
    }

    /**
     * Добавление задачи
     *
     * @param exchange HttpExchange
     * @throws IOException
     */
    private void handlePostTasks(HttpExchange exchange) throws IOException {
        InputStream inputStream = exchange.getRequestBody();
        String body = new String(inputStream.readAllBytes(), StandardCharsets.UTF_8);
        Task task;
        try {
            task = getGson().fromJson(body, Task.class);
            taskManager.addNewTask(task);
            writeResponse(exchange, "Задача с id=" + task.getTaskId() + " успешно добавлена", 201);
        } catch (Exception e) {
            writeResponse(exchange, "Errors..: " + e.getMessage(), 400);
        }

    }

    /**
     * Определяет endpoint по URL и HTTP-методу
     * Базовые пути приложения: /tasks, /subtasks, /epics, /history, /prioritized
     *
     * @param path          String
     * @param requestMethod String
     * @return Endpoint
     */
    private Endpoint getEndpoint(String path, String requestMethod) {
        String[] pathParts = path.split("/");

        // Метод DELETE
        if (requestMethod.equals("DELETE") && pathParts.length == 3) {
            switch (pathParts[1]) {
                case "tasks" -> {
                    return Endpoint.DELETE_TASKS;
                }
                case "subtasks" -> {
                    return Endpoint.DELETE_SUBTASKS;
                }
                case "epics" -> {
                    return Endpoint.DELETE_EPICS;
                }
                default -> {
                    return Endpoint.UNKNOWN;
                }
            }
        }

        // Метод POST
        if (requestMethod.equals("POST") && pathParts.length == 2) {
            switch (pathParts[1]) {
                case "tasks" -> {
                    return Endpoint.POST_TASKS;
                }
                case "subtasks" -> {
                    return Endpoint.POST_SUBTASKS;
                }
                case "epics" -> {
                    return Endpoint.POST_EPICS;
                }
                default -> {
                    return Endpoint.UNKNOWN;
                }
            }
        }

        // Метод GET
        if (requestMethod.equals("GET")) {
            if (pathParts.length == 3) {
                switch (pathParts[1]) {
                    case "tasks" -> {
                        return Endpoint.GET_TASKS_ID;
                    }
                    case "subtasks" -> {
                        return Endpoint.GET_SUBTASKS_ID;
                    }
                    case "epics" -> {
                        return Endpoint.GET_EPICS_ID;
                    }
                }
            } else if (pathParts.length == 2) {
                switch (pathParts[1]) {
                    case "tasks" -> {
                        return Endpoint.GET_TASKS;
                    }
                    case "subtasks" -> {
                        return Endpoint.GET_SUBTASKS;
                    }
                    case "epics" -> {
                        return Endpoint.GET_EPICS;
                    }
                    case "history" -> {
                        return Endpoint.GET_HISTORY;
                    }
                    case "prioritizied" -> {
                        return Endpoint.GET_PRIORITIZED;
                    }
                }
            } else if (pathParts.length == 4 && pathParts[3].equals("subtasks")) {
                return Endpoint.GET_EPICS_SUBTASKS;
            } else {
                return Endpoint.UNKNOWN;
            }
        }
        return Endpoint.UNKNOWN;
    }

    private void writeResponse(HttpExchange exchange,
                               String responseString,
                               int responseCode) throws IOException {
        try (OutputStream os = exchange.getResponseBody()) {
            exchange.sendResponseHeaders(responseCode, 0);
            os.write(responseString.getBytes(DEFAULT_CHARSET));
        }
        exchange.close();
    }


    private Gson getGson() {
        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.registerTypeAdapter(LocalDateTime.class, new LocalDataTimeTypeAdapter());
        gsonBuilder.setPrettyPrinting();
        return gsonBuilder.create();
    }

}
