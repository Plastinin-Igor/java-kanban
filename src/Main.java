import model.*;
import service.*;
import util.InMemoryHistoryManager;
import util.Managers;

public class Main {

    public static void main(String[] args) {

        TaskManager taskManager = Managers.getDefault();

        TaskItem task1;
        TaskItem task2;
        TaskItem epic1;
        TaskItem epic2;
        TaskItem subtask1;
        TaskItem subtask2;
        TaskItem subtask3;

        //Задача 1
        Task taskObj1 = new Task("Задача № 1", "Описание задачи №1", Status.NEW);
        task1 = taskManager.addNewTask(taskObj1);
        taskManager.getTaskById(task1.getTaskId());
        taskManager.getTaskById(task1.getTaskId());
        //Задача 2
        Task taskObj2 = new Task("Задача № 2", "Описание задачи №2", Status.NEW);
        task2 = taskManager.addNewTask(taskObj2);
        taskManager.getTaskById(task2.getTaskId());
        //Эпик 1
        Epic epicObj1 = new Epic("Эпик-задача № 1", "Описание эпик-задачи №1");
        epic1 = taskManager.addNewEpic(epicObj1);
        taskManager.getEpicById(epic1.getTaskId());
        taskManager.getEpicById(epic1.getTaskId());
        //Подзадача 1.1
        Subtask subtaskObj1 = new Subtask("Первая подзадача в эпике № 1",
                "Первая подзадача в эпике № 1", Status.NEW, epic1.getTaskId());
        subtask1 = taskManager.addNewSubtask(subtaskObj1);
        taskManager.getSubtaskById(subtask1.getTaskId());
        //Подзадача 1.2
        Subtask subtaskObj2 = new Subtask("Вторая подзадача в эпике № 1",
                "Вторая подзадача в эпике № 1", Status.NEW, epic1.getTaskId());
        subtask2 = taskManager.addNewSubtask(subtaskObj2);
        taskManager.getSubtaskById(subtask2.getTaskId());
        //Эпик 2
        Epic epicObj2 = new Epic("Эпик-задача № 1", "Описание эпик-задачи №1");
        epic2 = taskManager.addNewEpic(epicObj2);
        taskManager.getEpicById(epic2.getTaskId());
        //Подзадача 2.1
        Subtask subtaskObj3 = new Subtask("Первая подзадача в эпике № 2",
                "Первая подзадача в эпике № 2", Status.NEW, epic2.getTaskId());
        subtask3 = taskManager.addNewSubtask(subtaskObj3);
        taskManager.getSubtaskById(subtask3.getTaskId());
        taskManager.getSubtaskById(subtask3.getTaskId());


        //НАПЕЧАТАТЬ ВСЕ ЗАДАЧИ И ИСТОРИЮ ПРОСМОТРОВ
        printAllTasks(taskManager);
    }

    private static void printAllTasks(TaskManager manager) {
        System.out.println("\nЗадачи:");
        for (TaskItem task : manager.getListTask()) {
            System.out.println(task);
        }
        System.out.println("\nЭпики: ");
        for (TaskItem epic : manager.getListEpic()) {
            System.out.println(epic);
            for(TaskItem subtask : manager.getSubtaskByEpicId(epic.getTaskId())) {
                System.out.println("   --> " + subtask);
            }
        }
        System.out.println("\nИстория:");
        for (TaskItem task : Managers.getDefaultHistory().getHistory()) {
            System.out.println(task);
        }

    }
}
