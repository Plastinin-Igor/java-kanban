package model;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import service.InMemoryTaskManager;

import java.util.Collection;
import java.util.List;

class TaskTest {

    InMemoryTaskManager manager = new InMemoryTaskManager();
    Task taskObject = new Task("Test addNewTask name", "Test addNewTask description", Status.NEW);


    @Test
    // Экземпляры класса Epic равны друг другу, если равен их id;
    void shouldBeEqualInstancesOfTheTaskClassById() {
        TaskItem task;
        TaskItem savedTask;

        task = manager.addNewTask(taskObject);
        savedTask = manager.getTaskById(task.getTaskId());

        Assertions.assertNotNull(savedTask, "Задача не найдена");
        Assertions.assertEquals(task, savedTask, "Задачи не совпадают");

        Collection<Task> tasks = manager.getListTask();
        Assertions.assertNotNull(tasks, "Задачи не возвращаются");
        Assertions.assertEquals(1, tasks.size(), "Неверное количество задач");

        for (Task taskList : tasks) {
            Assertions.assertEquals(task, taskList, "Задачи не совпадают");
        }

    }

}