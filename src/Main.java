import model.*;
import service.*;
import util.*;

public class Main {

    public static void main(String[] args) {

        TaskManager taskManager = Managers.getDefault();
        FileBackedTaskManager fileBackedTaskManager = new FileBackedTaskManager("TaskManagerData.csv");

        System.out.println("Восстановленные из файла данные:");
        printAllTasks(fileBackedTaskManager);

        System.out.println("Добавлена подзадача:");
        Subtask subtaskObj3 = new Subtask("Третья подзадача в эпике № 1",
                "Третья подзадача в эпике № 1", Status.NEW, 4);
        fileBackedTaskManager.addNewSubtask(subtaskObj3);
        printAllTasks(fileBackedTaskManager);

        System.out.println("Изменен статус подзадачи:");
        fileBackedTaskManager.updateSubtask(new Subtask(5, "Первая подзадача в эпике № 1",
                "Первая подзадача в эпике № 1", Status.IN_PROGRESS, 4));
        printAllTasks(fileBackedTaskManager);

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
