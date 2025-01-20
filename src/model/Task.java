package model;

import java.time.Duration;
import java.time.LocalDateTime;

public class Task extends TaskItem {
    public Task(int taskId, String taskName, String taskDescription, Status taskStatus) {
        super(taskId, taskName, taskDescription, taskStatus);
    }

    public Task(int taskId, String taskName, String taskDescription, Status taskStatus,
                Duration duration, LocalDateTime startTime) {
        super(taskId, taskName, taskDescription, taskStatus, duration, startTime);
    }

    public Task(String taskName, String taskDescription, Status taskStatus) {
        super(taskName, taskDescription, taskStatus);
    }

    public Task(String taskName, String taskDescription, Status taskStatus,
                Duration duration, LocalDateTime startTime) {
        super(taskName, taskDescription, taskStatus, duration, startTime);
    }

    @Override
    public String toString() {
        return "Простая задача{" +
                "Идентификатор=" + super.getTaskId() +
                ", Наименование='" + super.getTaskName() + '\'' +
                ", Описание='" + super.getTaskDescription() + '\'' +
                ", Статус='" + super.getTaskStatus() + '\'' +
                ", Продолжительность='" + super.getDuration().toMinutes() + " мин." + '\'' +
                ", Дата и веремя, когда приступать к выполнению='" + super.getStartTime() + '\'' +
                ", Дата и время завершения задачи='" + super.getEndTime() + '\'' +
                '}';
    }

    /**
     * Строка для записи данных в CSV файл
     *
     * @return String
     */
    public String toStringForFileCSV() {
        return String.format("%s,%s,%s,%s,%s\n", super.getTaskId(), TypesOfTasks.TASK, super.getTaskName(),
                super.getTaskStatus(), super.getTaskDescription());
    }

}
