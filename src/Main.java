import model.*;
import service.*;

public class Main {

    public static void main(String[] args) {

        TaskManager taskManager = new TaskManager();

        TaskItem task1;
        TaskItem task2;
        TaskItem epic1;
        TaskItem epic2;
        TaskItem subtask1;
        TaskItem subtask2;
        TaskItem subtask3;

        System.out.println("Поехали!");
        System.out.println();

        //I. СОЗДАЙТЕ ДВЕ ЗАДАЧИ, А ТАКЖЕ ЭПИК С ДВУМЯ ПОДЗАДАЧАМИ И ЭПИК С ОДНОЙ ПОДЗАДАЧЕЙ.
        //Задача 1
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

        //Эпик 2
        Epic epicObj2 = new Epic("Эпик-задача № 1", "Описание эпик-задачи №1");
        epic2 = taskManager.addNewEpic(epicObj2);

        //Подзадача 2.1
        Subtask subtaskObj3 = new Subtask("Первая подзадача в эпике № 2",
                "Первая подзадача в эпике № 2", Status.NEW, epic2.getTaskId());
        subtask3 = taskManager.addNewSubtask(subtaskObj3);

        //II. РАСПЕЧАТАЙТЕ СПИСКИ ЭПИКОВ, ЗАДАЧ И ПОДЗАДАЧ
        System.out.println(task1);
        System.out.println(task2);
        System.out.println(epic1);
        System.out.println("    " + subtask1);
        System.out.println("    " + subtask2);
        System.out.println(epic2);
        System.out.println("    " + subtask3);
        System.out.println();

        // III. ИЗМЕНИТЕ СТАТУСЫ СОЗДАННЫХ ОБЪЕКТОВ
        taskObj1.setTaskStatus(Status.IN_PROGRESS);
        task1 = taskManager.updateTask(taskObj1);

        taskObj2.setTaskStatus(Status.DONE);
        task2 = taskManager.updateTask(taskObj2);

        subtaskObj1.setTaskStatus(Status.DONE);
        subtask1 = taskManager.updateSubtask(subtaskObj1);

        subtaskObj2.setTaskStatus(Status.IN_PROGRESS);
        subtask2 = taskManager.updateSubtask(subtaskObj2);

        subtask3.setTaskStatus(Status.DONE);
        subtask3 = taskManager.updateSubtask(subtaskObj3);

        // РАСПЕЧАТАЙТЕ ИХ. ПРОВЕРЬТЕ, ЧТО СТАТУС ЗАДАЧИ И ПОДЗАДАЧИ СОХРАНИЛСЯ
        System.out.println(task1);
        System.out.println(task2);

        // СТАТУС ЭПИКА РАССЧИТАЛСЯ ПО СТАТУСАМ ПОДЗАДАЧ
        System.out.println(epic1);
        System.out.println("    " + subtask1);
        System.out.println("    " + subtask2);
        System.out.println(epic2);
        System.out.println("    " + subtask3);
        System.out.println();

    }
}
