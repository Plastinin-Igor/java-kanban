package server;

import com.google.gson.Gson;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import exceptions.IntersectException;
import model.*;
import util.Managers;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

public class TaskHandler implements HttpHandler {
    private final TaskManager taskManager;
    private final BaseHttpHandler baseHttpHandler = new BaseHttpHandler();

    public TaskHandler(TaskManager taskManager) {
        this.taskManager = taskManager;
    }

    private final Gson gson = Managers.getGson();

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        Endpoint endpoint = getEndpoint(exchange.getRequestURI().getPath(), exchange.getRequestMethod());
        String path = exchange.getRequestURI().getPath();

        try {
            switch (endpoint) {
                case GET_TASKS: {
                    handleGetTask(exchange); // Запрос GET: /tasks
                    break;
                }
                case GET_TASKS_ID: {
                    handleGetTaskId(exchange, path); // Запрос GET: /tasks/{id}
                    break;
                }
                case POST_TASKS: {
                    handlePostTasks(exchange, path); // Запрос POST: /tasks/{id} и /tasks
                    break;
                }
                case DELETE_TASKS: {
                    handleDeleteTask(exchange, path); // Запрос DELETE /tasks/{id}
                    break;
                }
                case GET_SUBTASKS: {
                    handleGetSubtasks(exchange); // Запрос GET: /subtasks
                    break;
                }
                case GET_SUBTASKS_ID: {
                    handleGetSubtaskId(exchange, path); // Запрос GET: /subtasks/{id}
                    break;
                }
                case POST_SUBTASKS: {
                    handlePostSubtasks(exchange, path); // Запрос POST: /subtasks/{id} и /subtasks
                }
                case DELETE_SUBTASKS: {
                    handleDeleteSubtask(exchange, path); // Запрос DELETE /subtasks/{id}
                }
                case GET_EPICS: {
                    handleGetEpics(exchange); // Запрос GET: /epics
                }
                case GET_EPICS_SUBTASKS: {
                    handleGetEpicSubtask(exchange, path); // Запрос GET: epics/{id}/subtask
                }
                case POST_EPICS: {
                    handlePostEpics(exchange); // Запрос POST: /epic
                }
                case GET_EPICS_ID: {
                    handleGetEpicId(exchange, path); // Запрос GET: /epics/{id}
                }
                case DELETE_EPICS: {
                    handleDeleteEpic(exchange, path); // Запрос DELETE: /epics/{id}
                }
                case GET_HISTORY: {
                    handleGetHistory(exchange); // Запрос GET: /history
                }
                case GET_PRIORITIZED: {
                    handleGetPrioritised(exchange); // Запрос GET: /prioritised
                }
                default: {
                    baseHttpHandler.sendError(exchange, "Неизвестный эндпоинт: "
                            + " Метод: " + exchange.getRequestMethod() + " url: "
                            + exchange.getRequestURI().getPath(), 400);
                }
            }
        } catch (Exception e) {
            baseHttpHandler.sendError(exchange, e.getMessage(), 500);
        } finally {
            exchange.close();
        }
    }

    /**
     * Обработчик запроса GET: /tasks
     *
     * @param exchange HttpExchange
     */
    private void handleGetTask(HttpExchange exchange) throws IOException {
        List<Task> taskList = new ArrayList<>(taskManager.getListTask());
        if (!taskList.isEmpty()) {
            String taskListJson = gson.toJson(taskList);
            baseHttpHandler.sendText(exchange, taskListJson);
        } else {
            baseHttpHandler.sendNotFound(exchange, "Данные не найдены");
        }
    }

    /**
     * Обработчик запроса GET: /tasks{id}
     *
     * @param exchange HttpExchange
     */
    private void handleGetTaskId(HttpExchange exchange, String path) throws IOException {
        String[] pathPart = path.split("/");
        int id = parsePathId(pathPart[2]);

        if (id != -1) {
            Task task = taskManager.getTaskById(id);
            if (task != null) {
                String taskJson = gson.toJson(task);
                baseHttpHandler.sendText(exchange, taskJson);
            } else {
                baseHttpHandler.sendNotFound(exchange, "Задача с id: " + id + " не найдена.");
            }
        } else {
            baseHttpHandler.sendError(exchange, "Получен некорректный id: " + pathPart[2], 405);
        }
    }

    /**
     * Обработчик запроса POST: /tasks{id} и /tasks
     * Создаем задачу, если id не указан.
     * Если id указан, то обновляем
     *
     * @param exchange HttpExchange
     */
    private void handlePostTasks(HttpExchange exchange, String path) throws IOException {
        String[] pathPart = path.split("/");
        try {
            // Добавление задачи
            if (pathPart.length == 2) {
                String body = new String(exchange.getRequestBody().readAllBytes(), StandardCharsets.UTF_8);
                Task task = gson.fromJson(body, Task.class);
                taskManager.addNewTask(task);
                baseHttpHandler.sendText(exchange, "Задача id: " + task.getTaskId() + " успешно добавлена.");
            } else {
                // Исправление задачи
                int id = parsePathId(pathPart[2]);
                if (id != -1) {
                    String body = new String(exchange.getRequestBody().readAllBytes(), StandardCharsets.UTF_8);
                    Task task = gson.fromJson(body, Task.class);
                    if (taskManager.getTaskById(task.getTaskId()) != null) {
                        taskManager.updateTask(task);
                        baseHttpHandler.sendText(exchange, "Задача id: " + task.getTaskId() + " успешно обновлена.");
                    } else {
                        baseHttpHandler.sendNotFound(exchange, "Задача с id: " + id + " не найдена.");
                    }
                } else {
                    baseHttpHandler.sendError(exchange, "Получен некорректный id: " + pathPart[2], 405);
                }
            }
        } catch (IntersectException e) {
            baseHttpHandler.sendHasInteractions(exchange, "Задача пересекается с существующими. " +
                    "Добавление/исправление недопустимо");
        }
    }

    /**
     * Обработчик запроса DELETE /tasks/{id}
     *
     * @param exchange HttpExchange
     * @param path     String
     */
    private void handleDeleteTask(HttpExchange exchange, String path) throws IOException {
        String[] pathPart = path.split("/");
        int id = parsePathId(pathPart[2]);

        if (id != -1) {
            Task task = taskManager.getTaskById(id);
            if (task != null) {
                taskManager.deleteTaskById(id);
                baseHttpHandler.sendText(exchange, "Задача с id: " + id + " успешно удалена.");
            } else {
                baseHttpHandler.sendNotFound(exchange, "Задача с id: " + id + " не найдена.");
            }
        } else {
            baseHttpHandler.sendError(exchange, "Получен некорректный id: " + pathPart[2], 405);
        }
    }

    /**
     * Обработчик запроса GET: /subtasks
     *
     * @param exchange HttpExchange
     */
    private void handleGetSubtasks(HttpExchange exchange) throws IOException {
        List<Subtask> subtaskList = new ArrayList<>(taskManager.getListSubtask());
        if (!subtaskList.isEmpty()) {
            String taskListJson = gson.toJson(subtaskList);
            baseHttpHandler.sendText(exchange, taskListJson);
        } else {
            baseHttpHandler.sendNotFound(exchange, "Данные не найдены");
        }
    }

    /**
     * Обработчик запроса GET: /subtasks{id}
     *
     * @param exchange HttpExchange
     */
    private void handleGetSubtaskId(HttpExchange exchange, String path) throws IOException {
        String[] pathPart = path.split("/");
        int id = parsePathId(pathPart[2]);

        if (id != -1) {
            Subtask subtask = taskManager.getSubtaskById(id);
            if (subtask != null) {
                String subtaskJson = gson.toJson(subtask);
                baseHttpHandler.sendText(exchange, subtaskJson);
            } else {
                baseHttpHandler.sendNotFound(exchange, "Задача с id: " + id + " не найдена.");
            }
        } else {
            baseHttpHandler.sendError(exchange, "Получен некорректный id: " + pathPart[2], 405);
        }
    }

    /**
     * Обработчик запроса POST: /subtasks{id} и /subtasks
     * Создаем задачу, если id не указан.
     * Если id указан, то обновляем
     *
     * @param exchange HttpExchange
     */
    private void handlePostSubtasks(HttpExchange exchange, String path) throws IOException {
        String[] pathPart = path.split("/");
        try {
            // Добавление задачи
            if (pathPart.length == 2) {
                String body = new String(exchange.getRequestBody().readAllBytes(), StandardCharsets.UTF_8);
                Subtask subtask = gson.fromJson(body, Subtask.class);
                taskManager.addNewSubtask(subtask);
                baseHttpHandler.sendText(exchange, "Задача id: " + subtask.getTaskId() + " успешно добавлена.");
            } else {
                // Исправление задачи
                int id = parsePathId(pathPart[2]);
                if (id != -1) {
                    String body = new String(exchange.getRequestBody().readAllBytes(), StandardCharsets.UTF_8);
                    Subtask subtask = gson.fromJson(body, Subtask.class);
                    if (taskManager.getSubtaskById(subtask.getTaskId()) != null) {
                        taskManager.updateSubtask(subtask);
                        baseHttpHandler.sendText(exchange, "Задача id: " + subtask.getTaskId() + " успешно обновлена.");
                    } else {
                        baseHttpHandler.sendNotFound(exchange, "Задача с id: " + id + " не найдена.");
                    }
                } else {
                    baseHttpHandler.sendError(exchange, "Получен некорректный id: " + pathPart[2], 405);
                }
            }
        } catch (IntersectException e) {
            baseHttpHandler.sendHasInteractions(exchange, "Задача пересекается с существующими. " +
                    "Добавление/исправление недопустимо");
        }
    }

    /**
     * Обработчик запроса DELETE /subtasks/{id}
     *
     * @param exchange HttpExchange
     * @param path     String
     */
    private void handleDeleteSubtask(HttpExchange exchange, String path) throws IOException {
        String[] pathPart = path.split("/");
        int id = parsePathId(pathPart[2]);

        if (id != -1) {
            Subtask subtask = taskManager.getSubtaskById(id);
            if (subtask != null) {
                taskManager.deleteSubtaskById(id);
                baseHttpHandler.sendText(exchange, "Задача с id: " + id + " успешно удалена.");
            } else {
                baseHttpHandler.sendNotFound(exchange, "Задача с id: " + id + " не найдена.");
            }
        } else {
            baseHttpHandler.sendError(exchange, "Получен некорректный id: " + pathPart[2], 405);
        }
    }

    /**
     * Обработчик запроса GET: /epics
     *
     * @param exchange HttpExchange
     */
    private void handleGetEpics(HttpExchange exchange) throws IOException {
        List<Epic> epicList = new ArrayList<>(taskManager.getListEpic());
        if (!epicList.isEmpty()) {
            String epicListJson = gson.toJson(epicList);
            baseHttpHandler.sendText(exchange, epicListJson);
        } else {
            baseHttpHandler.sendNotFound(exchange, "Данные не найдены");
        }
    }


    /**
     * Обработчик запроса GET: /epics{id}
     *
     * @param exchange HttpExchange
     */
    private void handleGetEpicId(HttpExchange exchange, String path) throws IOException {
        String[] pathPart = path.split("/");
        int id = parsePathId(pathPart[2]);

        if (id != -1) {
            Epic epic = taskManager.getEpicById(id);
            if (epic != null) {
                String epicJson = gson.toJson(epic);
                baseHttpHandler.sendText(exchange, epicJson);
            } else {
                baseHttpHandler.sendNotFound(exchange, "Задача с id: " + id + " не найдена.");
            }
        } else {
            baseHttpHandler.sendError(exchange, "Получен некорректный id: " + pathPart[2], 405);
        }
    }

    /**
     * Обработчик запроса GET: epics/{id}/subtask
     *
     * @param exchange HttpExchange
     */
    private void handleGetEpicSubtask(HttpExchange exchange, String path) throws IOException {
        String[] pathPart = path.split("/");
        int id = parsePathId(pathPart[2]);
        if (id != -1) {
            Epic epic = taskManager.getEpicById(id);
            if (epic != null) {
                List<Subtask> subtaskList = new ArrayList<>(taskManager.getSubtaskByEpicId(epic.getTaskId()));
                if (!subtaskList.isEmpty()) {
                    String subtaskListJson = gson.toJson(subtaskList);
                    baseHttpHandler.sendText(exchange, subtaskListJson);
                } else {
                    baseHttpHandler.sendNotFound(exchange, "Данные не найдены");
                }
            } else {
                baseHttpHandler.sendNotFound(exchange, "Задача с id: " + id + " не найдена.");
            }
        } else {
            baseHttpHandler.sendError(exchange, "Получен некорректный id: " + pathPart[2], 405);
        }
    }

    /**
     * Обработчик запроса POST: /epics
     *
     * @param exchange HttpExchange
     */
    private void handlePostEpics(HttpExchange exchange) throws IOException {
        String body = new String(exchange.getRequestBody().readAllBytes(), StandardCharsets.UTF_8);
        Epic epic = gson.fromJson(body, Epic.class);
        taskManager.addNewEpic(epic);
        baseHttpHandler.sendText(exchange, "Задача id: " + epic.getTaskId() + " успешно добавлена.");
    }

    /**
     * Обработчик запроса DELETE /epics/{id}
     *
     * @param exchange HttpExchange
     * @param path     String
     */
    private void handleDeleteEpic(HttpExchange exchange, String path) throws IOException {
        String[] pathPart = path.split("/");
        int id = parsePathId(pathPart[2]);

        if (id != -1) {
            Epic epic = taskManager.getEpicById(id);
            if (epic != null) {
                taskManager.deleteEpicById(id);
                baseHttpHandler.sendText(exchange, "Задача с id: " + id + " успешно удалена.");
            } else {
                baseHttpHandler.sendNotFound(exchange, "Задача с id: " + id + " не найдена.");
            }
        } else {
            baseHttpHandler.sendError(exchange, "Получен некорректный id: " + pathPart[2], 405);
        }
    }

    /**
     * Обработчик запроса GET: /history
     *
     * @param exchange HttpExchange
     */
    private void handleGetHistory(HttpExchange exchange) throws IOException {
        List<TaskItem> taskItemList = new ArrayList<>(taskManager.getHistory());
        if (!taskItemList.isEmpty()) {
            String taskItemJson = gson.toJson(taskItemList);
            baseHttpHandler.sendText(exchange, taskItemJson);
        } else {
            baseHttpHandler.sendNotFound(exchange, "Данные не найдены");
        }
    }

    /**
     * Обработчик запроса GET: /prioritised
     *
     * @param exchange HttpExchange
     */
    private void handleGetPrioritised(HttpExchange exchange) throws IOException {
        List<TaskItem> taskItemList = new ArrayList<>(taskManager.getPrioritizedTasks());
        if (!taskItemList.isEmpty()) {
            String taskItemJson = gson.toJson(taskItemList);
            baseHttpHandler.sendText(exchange, taskItemJson);
        } else {
            baseHttpHandler.sendNotFound(exchange, "Данные не найдены");
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
        if (requestMethod.equals("POST") && (pathParts.length == 2 || pathParts.length == 3)) {
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

    /**
     * Возвращает id задачаи из строки
     *
     * @param path String
     * @return int
     */
    private int parsePathId(String path) {
        try {
            return Integer.parseInt(path);
        } catch (NumberFormatException e) {
            return -1;
        }
    }

}
