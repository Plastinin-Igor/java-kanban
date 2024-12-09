import model.*;
import util.*;

public class Main {

    public static void main(String[] args) {

        TaskManager taskManager = Managers.getDefault();
        TaskItem task1, task2, epic1, epic2, subtask1, subtask2, subtask3;

        //Задача 1
        Task taskObj1 = new Task("Задача № 1", "Описание задачи №1", Status.NEW);
        task1 = taskManager.addNewTask(taskObj1);

        //Задача 2
        Task taskObj2 = new Task("Задача № 2", "Описание задачи №2", Status.NEW);

        //Эпик 1
        Epic epicObj1 = new Epic("Эпик-задача № 1", "Описание эпик-задачи №1");
        epic1 = taskManager.addNewEpic(epicObj1);

        //Подзадача 1.1
        Subtask subtaskObj1 = new Subtask("Первая подзадача в эпике № 1",
                "Первая подзадача в эпике № 1", Status.NEW, epic1.getTaskId());
        subtask1 = taskManager.addNewSubtask(subtaskObj1);

        //Подзадача 1.2
        Subtask subtaskObj2 = new Subtask("Вторая подзадача в эпике № 1",
                "Вторая подзадача в эпике № 1", Status.NEW, epic1.getTaskId());
        subtask2 = taskManager.addNewSubtask(subtaskObj2);

        //Подзадача 1.3
        Subtask subtaskObj3 = new Subtask("Третья подзадача в эпике № 1",
                "Третья подзадача в эпике № 1", Status.NEW, epic1.getTaskId());
        subtask3 = taskManager.addNewSubtask(subtaskObj3);

        //Эпик 2
        Epic epicObj2 = new Epic("Эпик-задача № 2", "Описание эпик-задачи №2");
        epic2 = taskManager.addNewEpic(epicObj2);

        //НАПЕЧАТАТЬ ВСЕ ЗАДАЧИ
        printAllTasks(taskManager);

        //ПРОСМОТР ЗАДЧ И ПЕЧАТЬ ИСТОРИИ
        taskManager.getEpicById(epic2.getTaskId());
        printHistory(taskManager);

        taskManager.getEpicById(epic1.getTaskId());
        printHistory(taskManager);


        taskManager.getEpicById(epic2.getTaskId());
        printHistory(taskManager);

        taskManager.getTaskById(task1.getTaskId());
        printHistory(taskManager);

        //Просмотр подзадач
        taskManager.getSubtaskById(subtask1.getTaskId());
        taskManager.getSubtaskById(subtask2.getTaskId());
        taskManager.getSubtaskById(subtask3.getTaskId());
        printHistory(taskManager);


        //Удаление задачи
        taskManager.deleteTaskById(task1.getTaskId());
        printHistory(taskManager);

        //Удаление эпика с подзадачами
        taskManager.deleteEpicById(epic1.getTaskId());
        printHistory(taskManager);
    }

    private static void printAllTasks(TaskManager manager) {
        System.out.println("\nЗадачи:");
        for (TaskItem task : manager.getListTask()) {
            System.out.println(task);
        }
        System.out.println("\nЭпики: ");
        for (TaskItem epic : manager.getListEpic()) {
            System.out.println(epic);
            for (TaskItem subtask : manager.getSubtaskByEpicId(epic.getTaskId())) {
                System.out.println("   --> " + subtask);
            }
        }

        if (!manager.getHistory().isEmpty()) {
            System.out.println("\nИстория:");
            for (TaskItem task : manager.getHistory()) {
                System.out.println(task);
            }
        }
    }

    public static void printHistory(TaskManager manager) {
        if (!manager.getHistory().isEmpty()) {
            System.out.println("\nИстория:");
            for (TaskItem task : manager.getHistory()) {
                System.out.println(task);
            }
        }
    }
}
