import model.*;
import service.*;

public class Main {

    public static void main(String[] args) {
        int taskId;
        TaskManager taskManager = new TaskManager();

        System.out.println("Поехали!");

        //СОЗДАЙТЕ ДВЕ ЗАДАЧИ, А ТАКЖЕ ЭПИК С ДВУМЯ ПОДЗАДАЧАМИ И ЭПИК С ОДНОЙ ПОДЗАДАЧЕЙ.
        //Простая задача - добавление
        Task task1 = new Task("Задача № 1","Простая задача № 1. Описание...", Status.NEW);
        taskId = taskManager.addNewTask(task1);
        Task task2 = new Task("Задача № 2","Простая задача № 2. Описание...", Status.NEW);
        taskId = taskManager.addNewTask(task2);
        //Эпик-задача - добавление + 2 подзадачи
        Epic epic1 = new Epic("Эпик-задача № 1","Эпик-задача № 1 Описание...", Status.NEW);
        taskId = taskManager.addNewEpic(epic1);
        //Добавление подзадачи в эпик
        Subtask subtask1 = new Subtask("Подзадача № 1","Подзадача № 1 в эпик-задаче № 1", Status.NEW,taskId);
        Subtask subtask2 = new Subtask("Подзадача № 2","Подзадача № 2 в эпик-задаче № 1", Status.NEW,taskId);
        taskId = taskManager.addNewSubtask(subtask1);
        taskId = taskManager.addNewSubtask(subtask2);
        //Эпик-задача - добавление + 1 подзадача
        Epic epic2 = new Epic("Эпик-задача № 2","Эпик-задача № 2 Описание...", Status.NEW);
        taskId = taskManager.addNewEpic(epic2);
        //Добавление подзадачи в эпик
        Subtask subtask3 = new Subtask("Подзадача № 1","Подзадача № 1 в эпик-задаче № 2", Status.NEW,taskId);
        taskId = taskManager.addNewSubtask(subtask3);

        //РАСПЕЧАТАЙТЕ СПИСКИ ЭПИКОВ, ЗАДАЧ И ПОДЗАДАЧ
        System.out.println("\nСписок эпиков, задач и подзадач:");
        taskManager.printAllTasks();

    }
}
