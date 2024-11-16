package util;

import model.HistoryManager;
import model.TaskManager;
import service.InMemoryTaskManager;

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

}
