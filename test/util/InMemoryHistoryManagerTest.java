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
}