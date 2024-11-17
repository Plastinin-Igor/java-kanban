package model;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import service.InMemoryTaskManager;

class TaskTest {

    @Test
    void addNewTask() {
        TaskItem task;
        TaskItem savedTask;
        InMemoryTaskManager manager = new InMemoryTaskManager();

        Task taskObject = new Task("Test addNewTask name", "Test addNewTask description", Status.NEW);
        task = manager.addNewTask(taskObject);
        savedTask = manager.getTaskById(task.getTaskId());

        Assertions.assertNotNull(savedTask, "Задача не найдена");
        Assertions.assertEquals(task, savedTask, "Задачи не совпадают");
    }
}