import model.*;
import util.*;

import java.time.Duration;
import java.time.LocalDateTime;

public class Main {

    public static void main(String[] args) {

        TaskManager taskManager = Managers.getDefault();

        Duration duration = Duration.ofMinutes(50);
        LocalDateTime start = LocalDateTime.now();

        TaskItem task1, task2, epic1, epic2, subtask1, subtask2, subtask3;
        taskManager.addNewTask(new Task("Задача № 1", "Описание задачи №1", Status.NEW, duration, start));
        taskManager.addNewTask(new Task("Задача № 2", "Описание задачи №2", Status.NEW, duration, start.plusMinutes(51)));

        printAllTasks(taskManager);


/*        //Задача 1
        Task taskObj1 = new Task("Задача № 1", "Описание задачи №1", Status.NEW);
        task1 = taskManager.addNewTask(taskObj1);

        //Задача 2
        Task taskObj2 = new Task("Задача № 2", "Описание задачи №2", Status.NEW);
        task2 = taskManager.addNewTask(taskObj2);

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
        System.out.println("01. ДОБАВИЛИ ЗАДАЧИ/ЭПИКИ/ПОДЗАДАЧИ. ПРОСМОТРОВ ПОКА НЕТ:");
        System.out.println("---------------------------------------------------------");
        printAllTasks(taskManager);

        //ПРОСМОТР ЗАДЧ И ПЕЧАТЬ ИСТОРИИ
        System.out.println();
        System.out.println("02. ПРОСМОТРЕЛИ ЭПИК № 2:");
        System.out.println("-------------------------");
        taskManager.getEpicById(epic2.getTaskId());
        printHistory(taskManager);

        System.out.println();
        System.out.println("03. ПРОСМОТРЕЛИ ЭПИК № 1:");
        System.out.println("-------------------------");
        taskManager.getEpicById(epic1.getTaskId());
        printHistory(taskManager);

        System.out.println();
        System.out.println("04. ПРОСМОТРЕЛИ ЕЩЁ РАЗ ЭПИК № 2:");
        System.out.println("---------------------------------");
        taskManager.getEpicById(epic2.getTaskId());
        printHistory(taskManager);

        System.out.println();
        System.out.println("05. ПРОСМОТРЕЛИ ЗАДАЧИ №№ 1, 2:");
        System.out.println("-------------------------------");
        taskManager.getTaskById(task1.getTaskId());
        taskManager.getTaskById(task2.getTaskId());
        printHistory(taskManager);

        System.out.println();
        System.out.println("06. ПРОСМОТРЕЛИ ПОДЗАДАЧИ №№ 1, 2, 3:");
        System.out.println("-------------------------------------");
        taskManager.getSubtaskById(subtask1.getTaskId());
        taskManager.getSubtaskById(subtask2.getTaskId());
        taskManager.getSubtaskById(subtask3.getTaskId());
        printHistory(taskManager);
*/

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
        } else {
            System.out.println("\nИстория: Отсутствует");
        }
    }

    public static void printHistory(TaskManager manager) {
        if (!manager.getHistory().isEmpty()) {
            System.out.println("История:");
            for (TaskItem task : manager.getHistory()) {
                System.out.println(task);
            }
        } else {
            System.out.println("История: Отсутствует");
        }
    }
}
