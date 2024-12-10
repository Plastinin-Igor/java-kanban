package model;

import java.util.ArrayList;

public class Epic extends TaskItem {
    private final ArrayList<Integer> subtaskListId = new ArrayList<>();

    public Epic(int taskId, String taskName, String taskDescription) {
        super(taskId, taskName, taskDescription);
    }

    public Epic(String taskName, String taskDescription) {
        super(taskName, taskDescription);
    }

    public ArrayList<Integer> getSubtaskListId() {
        return subtaskListId;
    }

    /**
     * Удаление единичной подзадачи из хранилища subtaskListId
     *
     * @param id идентификатор подзадачи
     */
    public void deleteSubtaskIdById(int id) {
        subtaskListId.remove(subtaskListId.indexOf(id));
    }

    /**
     * Удаление всех данные из хранилища subtaskListId
     */
    public void deleteAllSubtaskId() {
        subtaskListId.clear();
    }

    /**
     * Добавление задачи в хранилище subtaskListId
     *
     * @param id идентификатор подзадачи
     */
    public void addNewSubtaskId(int id) {
        subtaskListId.add(id);
    }

    @Override
    public String toString() {
        return "Эпик-задача{" +
                "Идентификатор=" + super.getTaskId() +
                ", Наименование='" + super.getTaskName() + '\'' +
                ", Описание='" + super.getTaskDescription() + '\'' +
                ", Статус='" + super.getTaskStatus() + '\'' +
                ", Подзадача='" + subtaskListId + '\'' +
                '}';
    }
}
