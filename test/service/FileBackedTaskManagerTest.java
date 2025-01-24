package service;

import exceptions.ManagerSaveException;
import model.*;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Collection;

import static org.junit.jupiter.api.Assertions.assertThrows;

class FileBackedTaskManagerTest {

    FileBackedTaskManager taskManager = new FileBackedTaskManager("TaskManagerData.csv");

    Task taskObj1 = new Task(1, "Задача 1", "Задача 1", Status.NEW,
            Duration.ofMinutes(10), LocalDateTime.of(2025, 1, 1, 10, 0));
    Epic epicObj1 = new Epic(2, "Эпик 1", "Эпик 1");

    Subtask subtaskObj1 = new Subtask(3, "Подзадача 1", "Подзадача 1", Status.NEW, 2,
            Duration.ofMinutes(15), LocalDateTime.of(2025, 1, 1, 10, 0));

    Subtask subtaskObj2 = new Subtask(4, "Подзадача 2", "Подзадача 2", Status.NEW, 2,
            Duration.ofMinutes(15), LocalDateTime.of(2025, 1, 1, 10, 0));


    @Test
    void addDeleteTask() {
        taskManager.addTask(taskObj1);
        Collection<Task> tasks = taskManager.getListTask();
        Assertions.assertEquals(tasks.size(), 1, "Задача не добавлена");
        taskManager.deleteTask();
        tasks = taskManager.getListTask();
        Assertions.assertEquals(tasks.size(), 0, "Задача не удалена");
    }

    @Test
    void addDeleteEpic() {
        taskManager.addEpic(epicObj1);
        Collection<Epic> epics = taskManager.getListEpic();
        Assertions.assertEquals(epics.size(), 1, "Эпик не добавлен");
        taskManager.deleteEpic();
        epics = taskManager.getListEpic();
        Assertions.assertEquals(epics.size(), 0, "Эпик не удален");
    }

    @Test
    void addDeleteSubtask() {
        taskManager.addEpic(epicObj1);
        taskManager.addSubtask(subtaskObj1);
        Collection<Subtask> subtasks = taskManager.getListSubtask();
        Assertions.assertEquals(subtasks.size(), 1, "Подзадача не добавлена");
        taskManager.deleteSubtask();
        subtasks = taskManager.getListSubtask();
        Assertions.assertEquals(subtasks.size(), 0, "Подзадача не удалена");
        taskManager.deleteEpic();
    }

    @Test
    void updateTask() {
        taskManager.addTask(taskObj1);
        Task taskUpdObj1 = new Task(1, "Задача 1 изменена", "Задача 1 изменена", Status.NEW,
                Duration.ofMinutes(10), LocalDateTime.of(2025, 1, 1, 10, 0));
        Task taskAfterUpdate = taskManager.updateTask(taskUpdObj1);
        Assertions.assertEquals(taskUpdObj1.toString(), taskAfterUpdate.toString(), "Задача не изменилась");
        taskManager.deleteTask();
    }

    @Test
    void testManagerSaveException() {
        assertThrows(ManagerSaveException.class, () -> {
            FileBackedTaskManager manager = new FileBackedTaskManager("/.!@#$%^&*()_<>_-");
            manager.addNewTask(taskObj1);
        }, "Ошибка создания файла");

    }

}