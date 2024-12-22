package service;

import model.*;

import java.util.ArrayList;
import java.util.Collection;

/**
 * Класс менеджера, который после каждой операции автоматически сохраняет все задачи и их состояние в специальный файл
 *
 */

public class FileBackedTaskManager extends InMemoryTaskManager implements TaskManager{
    @Override
    public Collection<Task> getListTask() {
        return super.getListTask();
    }

    @Override
    public Collection<Epic> getListEpic() {
        return super.getListEpic();
    }

    @Override
    public Collection<Subtask> getListSubtask() {
        return super.getListSubtask();
    }

    @Override
    public void deleteTask() {
        super.deleteTask();
    }

    @Override
    public void deleteEpic() {
        super.deleteEpic();
    }

    @Override
    public void deleteSubtask() {
        super.deleteSubtask();
    }

    @Override
    public Task getTaskById(int taskId) {
        return super.getTaskById(taskId);
    }

    @Override
    public Epic getEpicById(int taskId) {
        return super.getEpicById(taskId);
    }

    @Override
    public Subtask getSubtaskById(int taskId) {
        return super.getSubtaskById(taskId);
    }

    @Override
    public Task addNewTask(Task task) {
        return super.addNewTask(task);
    }

    @Override
    public Epic addNewEpic(Epic epic) {
        return super.addNewEpic(epic);
    }

    @Override
    public Subtask addNewSubtask(Subtask subtask) {
        return super.addNewSubtask(subtask);
    }

    @Override
    public Task updateTask(Task task) {
        return super.updateTask(task);
    }

    @Override
    public Epic updateEpic(Epic epic) {
        return super.updateEpic(epic);
    }

    @Override
    public Subtask updateSubtask(Subtask subtask) {
        return super.updateSubtask(subtask);
    }

    @Override
    public void deleteTaskById(int taskId) {
        super.deleteTaskById(taskId);
    }

    @Override
    public void deleteEpicById(int taskId) {
        super.deleteEpicById(taskId);
    }

    @Override
    public void deleteSubtaskById(int taskId) {
        super.deleteSubtaskById(taskId);
    }

    @Override
    public ArrayList<Subtask> getSubtaskByEpicId(int epicId) {
        return super.getSubtaskByEpicId(epicId);
    }

    @Override
    public Collection<TaskItem> getHistory() {
        return super.getHistory();
    }
}
