package model;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;

public class Epic extends TaskItem {
    private final ArrayList<Integer> subtaskListId = new ArrayList<>();
    private LocalDateTime endTime;

    public Epic(int taskId, String taskName, String taskDescription) {
        super(taskId, taskName, taskDescription);
        super.setDuration(Duration.ofMinutes(0));
    }

    public Epic(String taskName, String taskDescription, Status taskStatus) {
        super(taskName, taskDescription, taskStatus);
        super.setDuration(Duration.ofMinutes(0));
    }

    public Epic(String taskName, String taskDescription) {
        super(taskName, taskDescription);
        super.setDuration(Duration.ofMinutes(0));
    }

    public Epic(int taskId, String taskName, String taskDescription, Status taskStatus) {
        super(taskId, taskName, taskDescription, taskStatus);
        super.setDuration(Duration.ofMinutes(0));
    }

    public ArrayList<Integer> getSubtaskListId() {
        return subtaskListId;
    }

    @Override
    public LocalDateTime getEndTime() {
        return endTime;
    }

    public void setEndTime(LocalDateTime endTime) {
        this.endTime = endTime;
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
                ", Продолжительность='" + super.getDuration().toMinutes() + '\'' +
                ", Дата и веремя, когда приступать к выполнению='" + super.getStartTime() + '\'' +
                ", Дата и время завершения задачи='" + getEndTime() + '\'' +
                '}';
    }

    /**
     * Строка для записи данных в CSV файл
     *
     * @return String
     */
    public String toStringForFileCSV() {
        return String.format("%s,%s,%s,%s,%s\n", super.getTaskId(), TypesOfTasks.EPIC, super.getTaskName(),
                super.getTaskStatus(), super.getTaskDescription());
    }

}
