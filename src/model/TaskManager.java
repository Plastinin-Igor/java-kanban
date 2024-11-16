package model;

import java.util.ArrayList;
import java.util.Collection;
/**
 * Методы, которые должны быть у любого объекта-менеджера
 */
public interface TaskManager {

    Collection<Task> getListTask();

    Collection<Epic> getListEpic();

    Collection<Subtask> getListSubtask();

    void deleteTask();

    void deleteEpic();

    void deleteSubtask();

    Task getTaskById(int taskId);

    Epic getEpicById(int taskId);

    Subtask getSubtaskById(int taskId);

    Task addNewTask(Task task);

    Epic addNewEpic(Epic epic);

    Subtask addNewSubtask(Subtask subtask);

    Task updateTask(Task task);

    Epic updateEpic(Epic epic);

    Subtask updateSubtask(Subtask subtask);

    void deleteTaskById(int taskId);

    void deleteEpicById(int taskId);

    void deleteSubtaskById(int taskId);

    ArrayList<Subtask> getSubtaskByEpicId(int epicId);
}
