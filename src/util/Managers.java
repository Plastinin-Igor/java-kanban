package util;

import model.*;
import service.*;

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
