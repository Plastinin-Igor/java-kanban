package service;

import model.*;

import java.util.HashMap;
import java.util.ArrayList;

public class TaskManager {
    private int id = 0;

    HashMap<Integer, Task> taskList = new HashMap<Integer, Task>();
    HashMap<Integer, Epic> epicList = new HashMap<Integer, Epic>();
    HashMap<Integer, Subtask> subtaskList = new HashMap<Integer, Subtask>();

    // Идентификатор задачи
    void getId() {
        id++;
    }

    //Получение списка всех задач
    public void printAllTasks() {
        int epicId;
        //Список простых задач
        for (int task : taskList.keySet()) {
            System.out.println(taskList.get(task));
        }
        //Список эпик-задач
        for (int epic : epicList.keySet()) {
            System.out.println(epicList.get(epic));
            epicId = epicList.get(epic).getTaskId();
            //Список подзадач в эпике
            for (int subtask : subtaskList.keySet()) {
                if (subtaskList.get(subtask).getEpicId() == epicId) {
                    System.out.println("    " + subtaskList.get(subtask));
                }
            }
        }
    }

    //Удаление всех задач
    public void removeAllTasks() {
        taskList.clear();
        epicList.clear();
        subtaskList.clear();
    }

    //Получение по идентификатору
    public void getTaskById(int taskId) {
        //Поиск простой задачи
        for (int task : taskList.keySet()) {
            int id = taskList.get(task).getTaskId();
            if (id == taskId) {
                System.out.println(taskList.get(task));
                return;
            }
        }
        //Поиск эпик-задачи
        for (int task : epicList.keySet()) {
            int id = epicList.get(task).getTaskId();
            if (id == taskId) {
                System.out.println(epicList.get(task));
                return;
            }
        }
        //Поиск подзадачи
        for (int task : subtaskList.keySet()) {
            int id = subtaskList.get(task).getTaskId();
            if (id == taskId) {
                System.out.println(subtaskList.get(task));
                return;
            }
        }
    }

    //Добавление обычной задачи
    public int addNewTask(Task task) {
        getId();
        if (taskList.containsKey(id)) {
            System.out.println(STR."Задача с id \{id} уже создана в системе.");
        } else {
            task.setTaskId(id);
            taskList.put(id, task);
            System.out.println(STR."Создана задача с id \{id}");
        }
        return id;
    }

    //Добавление эпик-задачи
    public int addNewEpic(Epic epic) {
        getId();
        if (epicList.containsKey(id)) {
            System.out.println(STR."Задача с id \{id} уже создана в системе.");
        } else {
            epic.setTaskId(id);
            epicList.put(id, epic);
            System.out.println(STR."Создана эпик-задача с id \{id}");
        }
        return id;
    }

    //Добавление подзадачи в эпик-задачу
    public int addNewSubtask(Subtask subtask) {
        getId();
        if (subtaskList.containsKey(id)) {
            System.out.println(STR."Задача с id \{id} уже создана в системе.");
        } else {
            subtask.setTaskId(id);
            subtaskList.put(id, subtask);
            System.out.println(STR."Создана подзадача с id \{id}");
        }
        return id;
    }

    //Определение статуса для эпик-задачи
    Status definingStatusForEpic(int taskId) {
        return Status.NEW;
    }

    //Надо ли менять статус в эпик-задаче
    boolean isChangeStatusInEpic(int taskId) {
        return false;
    }


    //Обновление обычной задачи
    public int updateTask(Task task) {
        return 0;
    }
    //Обновление эпик-задачи
    public int updateEpic(Epic epic) {
        return 0;
    }
    //Обновление подзадачи
    public int updateSubtask(Subtask subtask) {
        return 0;
    }


    public void removeTaskById(int taskId) {
        if(taskList.containsKey(taskId)) {
            taskList.remove(taskId);
        } else if(epicList.containsKey(taskId)) {
            epicList.remove(taskId);
        } else if (subtaskList.containsKey(taskId)) {
            subtaskList.remove(taskId);
        } else {
            System.out.println("Задача с id " + taskId + " не найдена");
        }
        System.out.println("Задача с id " + taskId + " удалена");
    }
}
