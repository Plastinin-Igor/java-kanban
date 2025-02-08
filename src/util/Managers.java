package util;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import model.*;
import service.*;

import java.time.Duration;
import java.time.LocalDateTime;

/**
 * Утилитарный класс для подбора нужной реализации TaskManager и HistoryManager
 * возвращает объект правильного типа
 */

public class Managers {

    public static TaskManager getDefault() {
        return new InMemoryTaskManager();
    }

    public static HistoryManager getDefaultHistory() {
        return new InMemoryHistoryManager();
    }

    public static Gson getGson() {
        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.registerTypeAdapter(LocalDateTime.class, new LocalDataTimeTypeAdapter());
        gsonBuilder.registerTypeAdapter(Duration.class, new DurationAdapter());
        gsonBuilder.setPrettyPrinting();
        return gsonBuilder.create();
    }

}
