package service;

import model.*;
import org.junit.jupiter.api.Assertions;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Collection;

import static org.junit.jupiter.api.Assertions.*;

class InMemoryTaskManagerTest {

    InMemoryTaskManager taskManager = new InMemoryTaskManager();
    TaskItem task, epic, subtask;

    Task taskObj = new Task("Задача № 1", "Описание задачи №1", Status.NEW);
    Epic epicObj = new Epic("Эпик-задача № 1", "Описание эпик-задачи №1");
    Epic epic1 = taskManager.addNewEpic(epicObj);
    Subtask subtaskObj = new Subtask("Первая подзадача в эпике № 1", "Первая подзадача в эпике № 1",
            Status.NEW, epic1.getTaskId());

    @Test
    void getListTask() {
        taskManager.addNewTask(taskObj);
        Collection<Task> tasks = taskManager.getListTask();

        Assertions.assertNotNull(tasks, "Задачи не возвращаются");
        Assertions.assertEquals(1, tasks.size(), "Неверное количество задач");
        Assertions.assertTrue(tasks.contains(taskObj), "Задачи не совпадают");

    }

    @Test
    void getListEpic() {
        Collection<Epic> epics = taskManager.getListEpic();

        Assertions.assertNotNull(epics, "Эпики не возвращаются");
        Assertions.assertEquals(1, epics.size(), "Неверное количество эпиков");
        Assertions.assertTrue(epics.contains(epicObj), "Эпики не совпадают");
    }

    @Test
    void getListSubtask() {
        taskManager.addNewSubtask(subtaskObj);
        Collection<Subtask> subtasks = taskManager.getListSubtask();

        Assertions.assertNotNull(subtasks, "Сабтаски не возвращаются");
        Assertions.assertEquals(1, subtasks.size(), "Неверное количество сабтасков");
        Assertions.assertTrue(subtasks.contains(subtaskObj), "Сабтаски не совпадают");
    }

    @Test
    void deleteTask() {
        taskManager.addNewTask(taskObj);
        taskManager.deleteTask();
        Collection<Task> tasks = taskManager.getListTask();

        Assertions.assertEquals(0, tasks.size(), "Неверное количество задач");
    }

    @Test
    void deleteEpic() {
        taskManager.addNewEpic(epicObj);
        taskManager.deleteEpic();
        Collection<Epic> epics = taskManager.getListEpic();

        Assertions.assertEquals(0, epics.size(), "Неверное количество эпиков");
    }

    @Test
    void deleteSubtask() {
        taskManager.addNewSubtask(subtaskObj);
        taskManager.deleteSubtask();
        Collection<Subtask> subtasks = taskManager.getListSubtask();

        Assertions.assertEquals(0, subtasks.size(), "Неверное количество сабтасков");
    }

    @Test
    void getTaskById() {
        Task task = taskManager.addNewTask(taskObj);
        Task savedTask = taskManager.getTaskById(task.getTaskId());

        Assertions.assertNotNull(savedTask, "Задача не найдена");
        Assertions.assertEquals(task, savedTask, "Задачи не совпадают");
    }

    @Test
    void getEpicById() {
        Epic epic = taskManager.addNewEpic(epicObj);
        Epic savedEpic = taskManager.getEpicById(epic.getTaskId());

        Assertions.assertNotNull(savedEpic, "Эпик не найден");
        Assertions.assertEquals(epic, savedEpic, "Эпики не совпадают");
    }

    @Test
    void getSubtaskById() {
        Subtask subtask = taskManager.addNewSubtask(subtaskObj);
        Subtask savedSubtask = taskManager.getSubtaskById(subtask.getTaskId());

        Assertions.assertNotNull(savedSubtask, "Сабтаск не найден");
        Assertions.assertEquals(subtask, savedSubtask, "Сабтаски не совпадают");
    }

    @Test
    void addNewTask() {
        Task task = taskManager.addNewTask(taskObj);

        Assertions.assertNotNull(task, "Задача не найдена");
    }

    @Test
    void addNewEpic() {
        Epic epic = taskManager.addNewEpic(epicObj);

        Assertions.assertNotNull(epic, "Задача не найден");
    }

    @Test
    void addNewSubtask() {
        Subtask subtask = taskManager.addNewSubtask(subtaskObj);

        Assertions.assertNotNull(subtask, "Сабтаск не найден");
    }

    @Test
    void updateTask() {
        Task task = taskManager.addNewTask(taskObj);
        int taskId = task.getTaskId();
        Task taskUpdate = new Task(taskId, "Задача № 1", "Описание задачи №1", Status.IN_PROGRESS);
        taskManager.updateTask(taskUpdate);
        Task taskAfterUpdate = taskManager.getTaskById(taskId);

        Assertions.assertEquals(taskUpdate, taskAfterUpdate, "Задачи не совпадают");
    }

    @Test
    void updateEpic() {
        Epic epic = taskManager.addNewEpic(epicObj);
        int taskId = epic.getTaskId();
        Epic epicUpdate = new Epic(taskId, "Эпик-задача № 1, исправленная",
                "Описание эпик-задачи №1,исправленная");
        taskManager.updateEpic(epicUpdate);
        Epic epicAfterUpdate = taskManager.getEpicById(taskId);

        Assertions.assertEquals(epicUpdate, epicAfterUpdate, "Эпики не совпадают");
    }

    @Test
    void updateSubtask() {
        Subtask subtask = taskManager.addNewSubtask(subtaskObj);
        int taskId = subtask.getTaskId();
        Status statusEpicBeforeUpdate = epicObj.getTaskStatus();
        Subtask subtaskUpdate = new Subtask(taskId, "Первая подзадача в эпике № 1",
                "Первая подзадача в эпике № 1", Status.DONE, epicObj.getTaskId());
        taskManager.updateSubtask(subtaskUpdate);
        Subtask subtaskAfterUpdate = taskManager.getSubtaskById(taskId);

        Assertions.assertEquals(subtaskUpdate, subtaskAfterUpdate, "Сабтаски не совпадают");

        Status statusEpicAfterUpdate = epicObj.getTaskStatus();

        Assertions.assertNotEquals(statusEpicBeforeUpdate, statusEpicAfterUpdate, "Статус эпика не обновился " +
                "после обновления статуса сабтаска");

    }

    @Test
    void deleteTaskById() {
        Task task = taskManager.addNewTask(taskObj);
        int taskId = task.getTaskId();
        taskManager.deleteTaskById(taskId);
        Task taskAfterDelete = taskManager.getTaskById(taskId);

        Assertions.assertNull(taskAfterDelete, "Задача не удалена");
    }

    @Test
    void deleteEpicById() {
        Epic epic = taskManager.addNewEpic(epicObj);
        int taskId = epic.getTaskId();
        taskManager.deleteEpicById(taskId);
        Epic epicAfterDelete = taskManager.getEpicById(taskId);

        Assertions.assertNull(epicAfterDelete, "Эпик не удален");
    }

    @Test
    void deleteSubtaskById() {
        Subtask subtask = taskManager.addNewSubtask(subtaskObj);
        int taskId = subtask.getTaskId();
        taskManager.deleteSubtaskById(taskId);
        Subtask subtaskAfterDelete = taskManager.getSubtaskById(taskId);

        Assertions.assertNull(subtaskAfterDelete, "Сабтаск не удален");
    }

    @Test
    void getSubtaskByEpicId() {
        Subtask subtask = taskManager.addNewSubtask(subtaskObj);
        int taskEpicId = subtask.getEpicId();
        ArrayList<Subtask> subtaskList = taskManager.getSubtaskByEpicId(taskEpicId);

        Assertions.assertEquals(subtaskList.size(), 1, "Сабтаски не найдены");
    }

    @Test
    void getHistory() {
        Task task = taskManager.addNewTask(taskObj);
        Epic epic = taskManager.addNewEpic(epicObj);
        Subtask subtask = taskManager.addNewSubtask(subtaskObj);

        int taskId = task.getTaskId();
        int epicId = epic.getTaskId();
        int subtaskId = subtask.getTaskId();

        taskManager.getTaskById(taskId);
        taskManager.getEpicById(epicId);
        taskManager.getSubtaskById(subtaskId);

        Collection<TaskItem> historyList = taskManager.getHistory();

        Assertions.assertNotNull(historyList, "История не сохраняется");

    }
}