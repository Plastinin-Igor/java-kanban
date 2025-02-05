package server;

import com.sun.net.httpserver.HttpExchange;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

public class BaseHttpHandler {

    /**
     * Отправка общего ответа в случае успеха - 200
     * 200 OK — успешный запрос.
     *
     * @param h    HttpExchange
     * @param text String
     * @throws IOException
     */
    protected void sendText(HttpExchange h, String text) throws IOException {
        byte[] resp = text.getBytes(StandardCharsets.UTF_8);
        h.getResponseHeaders().add("Content-Type", "application/json;charset=utf-8");
        h.sendResponseHeaders(200, resp.length);
        h.getResponseBody().write(resp);
        h.close();
    }

    /**
     * Отправка ответа для метода POST в случае успеха - 201
     * 201 Created — в результате успешного выполнения запроса был создан новый ресурс.
     *
     * @param h    HttpExchange
     * @param text String
     * @throws IOException
     */
    protected void sendCreated(HttpExchange h, String text) throws IOException {
        byte[] resp = text.getBytes(StandardCharsets.UTF_8);
        h.getResponseHeaders().add("Content-Type", "application/json;charset=utf-8");
        h.sendResponseHeaders(201, resp.length);
        h.getResponseBody().write(resp);
        h.close();
    }

    /**
     * Отправка ответа в случае, если объект не был найден - 404
     * 404 Not Found — Сервер понял запрос, но не нашёл соответствующего ресурса по указанному URL.
     *
     * @param h    HttpExchange
     * @param text String
     * @throws IOException
     */
    protected void sendNotFound(HttpExchange h, String text) throws IOException {
        byte[] resp = text.getBytes(StandardCharsets.UTF_8);
        h.getResponseHeaders().add("Content-Type", "application/json;charset=utf-8");
        h.sendResponseHeaders(404, resp.length);
        h.getResponseBody().write(resp);
        h.close();
    }

    /**
     * Отправка ответа в случае, если при создании или обновлении
     * задача пересекается с уже существующими - 406
     * 406 Not Acceptable — запрошенный URI не может удовлетворить переданным в заголовке характеристикам.
     *
     * @param h    HttpExchange
     * @param text String
     * @throws IOException
     */
    protected void sendHasInteractions(HttpExchange h, String text) throws IOException {
        byte[] resp = text.getBytes(StandardCharsets.UTF_8);
        h.getResponseHeaders().add("Content-Type", "application/json;charset=utf-8");
        h.sendResponseHeaders(406, resp.length);
        h.getResponseBody().write(resp);
        h.close();
    }

    /**
     * Отправка ответа с заданным в параметрах кодом
     * Например 500 Internal Server Error или 400 Bad Request
     * Используется для исключения: Неизвестный эндпоинт
     *
     * @param h    HttpExchange
     * @param text String
     * @param code int
     * @throws IOException
     */
    protected void sendError(HttpExchange h, String text, int code) throws IOException {
        byte[] resp = text.getBytes(StandardCharsets.UTF_8);
        h.getResponseHeaders().add("Content-Type", "application/json;charset=utf-8");
        h.sendResponseHeaders(code, resp.length);
        h.getResponseBody().write(resp);
        h.close();
    }

}
