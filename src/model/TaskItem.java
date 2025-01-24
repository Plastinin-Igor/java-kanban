package model;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Objects;

public abstract class TaskItem implements Comparable<TaskItem> {
    private int taskId; // Уникальный идентификационный номер задачи
    private String taskName; // Название, кратко описывающее суть задачи
    private String taskDescription; // Описание, в котором раскрываются детали
    private Status taskStatus; // Статус, отображающий прогресс задачи
    private Duration duration; // Продолжительность задачи, оценка того, сколько времени она займёт в минутах
    private LocalDateTime startTime; // Дата и время, когда предполагается приступить к выполнению задачи
    public static final DateTimeFormatter DATE_TIME_FORMAT = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm:ss");

    public TaskItem(int taskId, String taskName, String taskDescription, Status taskStatus) {
        this.taskId = taskId;
        this.taskName = taskName;
        this.taskDescription = taskDescription;
        this.taskStatus = taskStatus;
    }

    public TaskItem(int taskId, String taskName, String taskDescription) {
        this.taskId = taskId;
        this.taskName = taskName;
        this.taskDescription = taskDescription;
        this.taskStatus = Status.NEW;
    }

    public TaskItem(String taskName, String taskDescription, Status taskStatus) {
        this.taskName = taskName;
        this.taskDescription = taskDescription;
        this.taskStatus = taskStatus;
    }

    public TaskItem(String taskName, String taskDescription) {
        this.taskName = taskName;
        this.taskDescription = taskDescription;
        this.taskStatus = Status.NEW;
    }

    public TaskItem(String taskName, String taskDescription, Status taskStatus, Duration duration,
                    LocalDateTime startTime) {
        this.taskName = taskName;
        this.taskDescription = taskDescription;
        this.taskStatus = taskStatus;
        this.duration = duration;
        this.startTime = startTime;
    }

    public TaskItem(int taskId, String taskName, String taskDescription, Status taskStatus, Duration duration,
                    LocalDateTime startTime) {
        this.taskId = taskId;
        this.taskName = taskName;
        this.taskDescription = taskDescription;
        this.taskStatus = taskStatus;
        this.duration = duration;
        this.startTime = startTime;
    }

    public int getTaskId() {
        return taskId;
    }

    public void setTaskId(int taskId) {
        this.taskId = taskId;
    }

    public String getTaskName() {
        return taskName;
    }

    public void setTaskName(String taskName) {
        this.taskName = taskName;
    }

    public String getTaskDescription() {
        return taskDescription;
    }

    public void setTaskDescription(String taskDescription) {
        this.taskDescription = taskDescription;
    }

    public Status getTaskStatus() {
        return taskStatus;
    }

    public void setTaskStatus(Status taskStatus) {
        this.taskStatus = taskStatus;
    }

    public Duration getDuration() {
        return duration;
    }

    public void setDuration(Duration duration) {
        this.duration = duration;
    }

    public LocalDateTime getStartTime() {
        return startTime;
    }

    public void setStartTime(LocalDateTime startTime) {
        this.startTime = startTime;
    }

    public LocalDateTime getEndTime() {
        if (startTime != null) {
            return startTime.plusMinutes(duration.toMinutes());
        } else {
            return null;
        }
    }

    @Override
    public boolean equals(Object object) {
        if (this == object) return true;
        if (object == null || getClass() != object.getClass()) return false;
        TaskItem taskItem = (TaskItem) object;
        return taskId == taskItem.taskId && Objects.equals(taskName, taskItem.taskName)
                && Objects.equals(taskDescription, taskItem.taskDescription) && taskStatus == taskItem.taskStatus;
    }

    @Override
    public int hashCode() {
        return Objects.hash(taskId, taskName, taskDescription, taskStatus);
    }

    @Override
    public String toString() {
        return "TaskItem{" +
                "taskId=" + taskId +
                ", taskName='" + taskName + '\'' +
                ", taskDescription='" + taskDescription + '\'' +
                ", taskStatus=" + taskStatus +
                '}';
    }

    @Override
    public int compareTo(TaskItem other) {
        return this.startTime.compareTo(other.startTime);
    }
}
