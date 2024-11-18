package model;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import service.InMemoryTaskManager;

import java.util.Collection;

class EpicTest {

    InMemoryTaskManager manager = new InMemoryTaskManager();
    Epic epicObject = new Epic("Test addNewEpic name", "Test addNewEpic description");

    @Test
        // Экземпляры класса Epic равны друг другу, если равен их id;
    void shouldBeEqualInstancesOfTheEpicClassById() {
        TaskItem epic;
        TaskItem savedEpic;

        epic = manager.addNewEpic(epicObject);
        savedEpic = manager.getEpicById(epic.getTaskId());

        Assertions.assertNotNull(savedEpic, "Задача не найдена");
        Assertions.assertEquals(epic, savedEpic, "Задачи не совпадают");

        Collection<Epic> epics = manager.getListEpic();
        Assertions.assertNotNull(epics, "Задачи не возвращаются");
        Assertions.assertEquals(1, epics.size(), "Неверное количество задач");

        for (Epic epicList : epics) {
            Assertions.assertEquals(epic, epicList, "Задачи не совпадают");
        }
    }

    @Test
        //Эпик должен создаваться со статусом по умолчанию New
    void shouldBeNewEpicShouldHaveStatusNew() {
        TaskItem epic = manager.addNewEpic(epicObject);
        Assertions.assertEquals(epic.getTaskStatus(), Status.NEW, "Статус у нового эпика должен быть NEW");
    }
}