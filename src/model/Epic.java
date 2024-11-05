package model;

import java.util.ArrayList;

public class Epic extends TaskItem {
    private ArrayList<Integer> subtaskId = new ArrayList<>();

    public Epic(int taskId, String taskName, String taskDescription) {
        super(taskId, taskName, taskDescription);
    }

    public Epic(String taskName, String taskDescription) {
        super(taskName, taskDescription);
    }

    public ArrayList<Integer> getSubtaskId() {
        return subtaskId;
    }

    /**
     *  Удаление единичной подзадачи из хранилища subtaskId
     * @param id идентификатор подзадачи
     */
    public void deleteSubtaskIdById(int id) {
        subtaskId.remove(id);
    }

    /**
     * Удаление всех данные из хранилища subtaskId
     */
    public void deleteAllSubtaskId() {
        subtaskId.clear();
    }

    /**
     * Добавление задачи в хранилище subtaskId
     * @param id идентификатор подзадачи
     */
    public void addNewSubtaskId(int id) {
        subtaskId.add(id);
    }

    @Override
    public String toString() {
        return "Эпик-задача{" +
                "Идентификатор=" + super.getTaskId() +
                ", Наименование='" + super.getTaskName() + '\'' +
                ", Описание='" + super.getTaskDescription() + '\'' +
                ", Статус='" + super.getTaskStatus() + '\'' +
                ", Подзадача='" + subtaskId + '\'' +
                '}';
    }
}
