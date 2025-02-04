package server;

import com.sun.net.httpserver.HttpExchange;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

public class BaseHttpHandler {

    /**
     * Отправка общего ответа в случае успеха - 200
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
     * Отправка ответа в случае, если объект не был найден - 404
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
     * Отправка ответа в случае, если при создании или обновлении
     * задача пересекается с уже существующими - 406
     *
     * @param h    HttpExchange
     * @param text String
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
