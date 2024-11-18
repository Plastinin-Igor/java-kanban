package util;

import model.*;
import org.junit.jupiter.api.Assertions;
import util.*;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.Collection;

import static org.junit.jupiter.api.Assertions.*;

class InMemoryHistoryManagerTest {

    HistoryManager historyManager = Managers.getDefaultHistory();

    Task taskObj1 = new Task("Задача № 1", "Описание задачи №1", Status.NEW);
    Epic epicObj1 = new Epic("Эпик-задача № 1", "Описание эпик-задачи №1");
    Subtask subtaskObj1 = new Subtask("Первая подзадача в эпике № 1",
            "Первая подзадача в эпике № 1", Status.NEW, 2);

    @Test
    void add() {
        for (int i = 0; i < 5; i++) {
            historyManager.add(taskObj1);
            historyManager.add(epicObj1);
            historyManager.add(subtaskObj1);
        }
        Collection<TaskItem> historyList = historyManager.getHistory();
        Assertions.assertNotNull(historyList,"История не добавлена");
        Assertions.assertEquals(historyList.size(), 10, "В истории сохранено более 10 записей");
    }

    @Test
    void getHistory() {
        for (int i = 0; i < 5; i++) {
            historyManager.add(taskObj1);
            historyManager.add(epicObj1);
            historyManager.add(subtaskObj1);
        }
        Collection<TaskItem> historyList = historyManager.getHistory();
        Assertions.assertNotNull(historyList,"История не добавлена");
        Assertions.assertEquals(historyList.size(), 10, "В истории сохранено более 10 записей");
    }
}