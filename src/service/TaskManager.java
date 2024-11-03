package service;

import model.*;

import java.util.ArrayList;
import java.util.HashMap;

public class TaskManager {
    private int id = 0;

    HashMap<Integer, Task> taskList = new HashMap<>();
    HashMap<Integer, Epic> epicList = new HashMap<>();
    HashMap<Integer, Subtask> subtaskList = new HashMap<>();

    // Идентификатор задачи (sequence)
    void getId() {
        id++;
    }

    //Получение списка задач
    public HashMap<Integer, Task> getListTask() {
        return taskList;
    }

    //Получение списка эпик-задач
    public HashMap<Integer, Epic> getListEpic() {
        return epicList;
    }

    //Получение подзадач
    public HashMap<Integer, Subtask> getSubtaskList() {
        return subtaskList;
    }

    //Удаление всех задач
    public void deleteTask() {
        taskList.clear();
    }

    //Удаление всех эпик-задач
    public void deleteEpic() {
        epicList.clear();
    }

    //Удаление всех подзадач
    public void deleteSubtask() {
        subtaskList.clear();
    }

    //Получение по идентификатору задачи
    public Task getTaskById(int taskId) {
        return taskList.get(taskId);
    }

    //Получение по идентификатору эпик-задачи
    public Epic getEpicById(int taskId) {
        return epicList.get(taskId);
    }

    //Получение по идентификатору подзадачи
    public Subtask getSubtaskById(int taskId) {
        return subtaskList.get(taskId);
    }

    //Обновление статуса в эпик-задаче
    public void updateEpicStatus(Epic epic) {
        //Если у эпика нет подзадач - статус NEW
        if (epic.getSubtaskId().isEmpty()) {
            epic.setTaskStatus(Status.NEW);
            return;
        }
        //Или
        boolean isNew = true;
        boolean isDone = true;
        Status status;
        for (Integer epicsSub : epic.getSubtaskId()) {
            status = subtaskList.get(epicsSub).getTaskStatus();

            if (status != Status.NEW) {
                isNew = false;
            }
            if (status != Status.DONE) {
                isDone = false;
            }
        }
        if (isNew) { // все подзадачи имеют статус NEW - эпик тоже NEW
            epic.setTaskStatus(Status.NEW);
        } else if (isDone) { // если все подзадачи имеют статус DONE, то и у эпика статус DONE.
            epic.setTaskStatus(Status.DONE);
        } else { // во всех остальных случаях - IN_PROGRESS
            epic.setTaskStatus(Status.IN_PROGRESS);
        }

    }

    //Добавление обычной задачи
    public Task addNewTask(Task task) {
        getId();
        if (taskList.containsKey(id)) {
            System.out.println("Задача с id " + id + " уже создана в системе.");
        } else {
            task.setTaskId(id);
            taskList.put(id, task);
            System.out.println("Создана задача с id " + id);
        }
        return task;
    }

    //Добавление эпик-задачи
    public Epic addNewEpic(Epic epic) {
        getId();
        if (epicList.containsKey(id)) {
            System.out.println("Эпик-задача с id " + id + " уже создана в системе.");
        } else {
            epic.setTaskId(id);
            epicList.put(id, epic);
            System.out.println("Создана эпик-задача с id " + id);
        }
        return epic;
    }

    //Добавление подзадачи в эпик-задачу
    public Subtask addNewSubtask(Subtask subtask) {
        getId();
        if (subtaskList.containsKey(id)) {
            System.out.println("Подзадача с id " + id + " уже создана в системе.");
        } else {
            subtask.setTaskId(id);
            if (epicList.containsKey(subtask.getEpicId())) { // Запишем в эпик-задачу id подзадачи
                ArrayList<Integer> subtaskId = epicList.get(subtask.getEpicId()).getSubtaskId();
                subtaskId.add(id);
            }
            subtaskList.put(id, subtask);
            updateEpicStatus(epicList.get(subtask.getEpicId())); // обновление статуса в эпик-задаче
            System.out.println("Создана подзадача с id " + id);
        }
        return subtask;
    }

    //Обновление обычной задачи
    public Task updateTask(Task task) {
        taskList.put(task.getTaskId(), task);
        return task;
    }

    //Обновление эпик-задачи
    public Epic updateEpic(Epic epic) {
        //Запретить менять статус!!!
        epicList.put(epic.getTaskId(), epic);
        return epic;
    }

    //Обновление подзадачи
    public Subtask updateSubtask(Subtask subtask) {
        subtaskList.put(subtask.getTaskId(), subtask);
        updateEpicStatus(epicList.get(subtask.getEpicId())); // обновление статуса в эпик-задаче
        return subtask;
    }

    //Удаление задачи по идентификатору
    public void deleteTaskById(int taskId) {
        taskList.remove(taskId);
    }

    //Удаление эпик-задачи по идентификатору
    public void deleteEpicById(int taskId) {
        epicList.remove(taskId);
    }

    //Удаление подзадачи по идентификатору
    public void deleteSubtaskById(int taskId) {
        //Удалим информацию о подзадаче из эпик-задачи
        Subtask subtask;
        subtask = subtaskList.get(taskId);
        if (epicList.containsKey(subtask.getEpicId())) {
            ArrayList<Integer> subtaskId = epicList.get(subtask.getEpicId()).getSubtaskId();
            int indexOf = subtaskId.indexOf(taskId);
            subtaskId.remove(indexOf);
        }
        subtaskList.remove(taskId);
        updateEpicStatus(epicList.get(subtask.getEpicId())); // обновление статуса в эпик-задаче
    }

    //Получение списка всех подзадач определенного эпика
    public ArrayList<Subtask> getSubtaskByEpicId(int epicId) {
        Epic epic = epicList.get(epicId);
        ArrayList<Subtask> subtasks = new ArrayList<>();
        for (int i = 0; i < epic.getSubtaskId().size(); i++) {
            subtasks.add(subtaskList.get(epic.getSubtaskId().get(i)));
        }
        return subtasks;
    }


    //Печать всех задач, эпиков, подзадач
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


    public void removeTaskById(int taskId) {
        if (taskList.containsKey(taskId)) {
            taskList.remove(taskId);
        } else if (epicList.containsKey(taskId)) {
            epicList.remove(taskId);
        } else if (subtaskList.containsKey(taskId)) {
            subtaskList.remove(taskId);
        } else {
            System.out.println("Задача с id " + taskId + " не найдена");
        }
        System.out.println("Задача с id " + taskId + " удалена");
    }
}
