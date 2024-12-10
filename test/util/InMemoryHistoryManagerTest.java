package util;

import model.*;
import org.junit.jupiter.api.Assertions;

import org.junit.jupiter.api.Test;

import java.util.Collection;

class InMemoryHistoryManagerTest {

    HistoryManager historyManager = Managers.getDefaultHistory();

    Task taskObj1 = new Task("Задача № 1", "Описание задачи №1", Status.NEW);


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