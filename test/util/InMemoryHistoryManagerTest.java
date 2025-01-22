package util;

import model.*;
import org.junit.jupiter.api.Assertions;

import org.junit.jupiter.api.Test;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Collection;

class InMemoryHistoryManagerTest {

    HistoryManager historyManager = Managers.getDefaultHistory();

    //Простая задача
    Task taskObj1 = new Task(1, "Задача № 1", "Описание задачи №1", Status.NEW,
            Duration.ofMinutes(15), LocalDateTime.of(2025, 1, 1, 10, 15));
    Task taskObj2 = new Task(2, "Задача № 2", "Описание задачи №2", Status.NEW,
            Duration.ofMinutes(15), LocalDateTime.of(2025, 1, 1, 10, 30));
    Task taskObj3 = new Task(3, "Задача № 3", "Описание задачи №3", Status.NEW,
            Duration.ofMinutes(15), LocalDateTime.of(2025, 1, 1, 10, 45));
    Task taskObj4 = new Task(4, "Задача № 4", "Описание задачи №4", Status.NEW,
            Duration.ofMinutes(15), LocalDateTime.of(2025, 1, 1, 11, 0));
    Task taskObj5 = new Task(5, "Задача № 5", "Описание задачи №5", Status.NEW,
            Duration.ofMinutes(15), LocalDateTime.of(2025, 1, 1, 11, 15));
    Task taskObj6 = new Task(6, "Задача № 6", "Описание задачи №6", Status.NEW,
            Duration.ofMinutes(15), LocalDateTime.of(2025, 1, 1, 11, 30));


    @Test
    void add() {
        historyManager.add(taskObj1);
        Collection<TaskItem> historyList = historyManager.getHistory();
        Assertions.assertNotNull(historyList, "История не добавлена");
    }

    @Test
    void getHistory() {
        historyManager.add(taskObj1);
        Collection<TaskItem> historyList = historyManager.getHistory();
        Assertions.assertNotNull(historyList, "История не добавлена");
    }

    @Test
    void remove() {
        historyManager.add(taskObj1);
        Collection<TaskItem> historyList = historyManager.getHistory();
        Assertions.assertNotNull(historyList, "История не добавлена");
        historyManager.remove(taskObj1.getTaskId());
        historyList = historyManager.getHistory();
        Assertions.assertTrue(historyList.isEmpty(), "История не удалена");
    }

    @Test
        // Пустая история задач
    void emptyHistory() {
        historyManager.add(taskObj1);
        historyManager.add(taskObj2);
        historyManager.add(taskObj3);

        Assertions.assertEquals(3, historyManager.getHistory().size(), "В историю не добавлено 3 просмотра");

        historyManager.remove(1);
        historyManager.remove(2);
        historyManager.remove(3);

        Assertions.assertEquals(0, historyManager.getHistory().size(), "История не очищена");

    }

    @Test
        // Дублирование истории
    void duplicationHistory() {
        historyManager.add(taskObj1);
        Assertions.assertEquals(1, historyManager.getHistory().size(), "В историю не добавлен 1 просмотр");

        historyManager.add(taskObj1);
        historyManager.add(taskObj1);
        historyManager.add(taskObj1);
        historyManager.add(taskObj1);

        Assertions.assertEquals(1, historyManager.getHistory().size(), "В историю добавлены дублирующие записи");
    }

    @Test
    void DeleteFromHistoryBeginningMiddleEnd() {
        historyManager.add(taskObj1);
        historyManager.add(taskObj2);
        historyManager.add(taskObj3);
        historyManager.add(taskObj4);
        historyManager.add(taskObj5);
        historyManager.add(taskObj6);

        Assertions.assertEquals(6, historyManager.getHistory().size(), "В историю не добавлено 6 просмотров");

        historyManager.remove(1);
        Assertions.assertEquals(0, historyManager.getHistory().stream().filter(h -> h.getTaskId() == 1).count(),
                "Из истории не удален первый просмотр");
        historyManager.remove(4);
        Assertions.assertEquals(0, historyManager.getHistory().stream().filter(h -> h.getTaskId() == 4).count(),
                "Из истории не удален просмотр из середины");
        historyManager.remove(6);
        Assertions.assertEquals(0, historyManager.getHistory().stream().filter(h -> h.getTaskId() == 6).count(),
                "Из истории не удален последний просмотр");

        Assertions.assertEquals(3, historyManager.getHistory().size(), "В истории должно остаться 3 просмотра");

    }
}