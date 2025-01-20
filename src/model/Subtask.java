package model;

import java.time.Duration;
import java.time.LocalDateTime;

public class Subtask extends TaskItem {
    private final int epicId;

    public Subtask(int taskId, String taskName, String taskDescription, Status taskStatus, int epicId) {
        super(taskId, taskName, taskDescription, taskStatus);
        this.epicId = epicId;
    }

    public Subtask(int taskId, String taskName, String taskDescription, Status taskStatus, int epicId,
                   Duration duration, LocalDateTime startTime) {
        super(taskId, taskName, taskDescription, taskStatus, duration, startTime);
        this.epicId = epicId;
    }

    public Subtask(String taskName, String taskDescription, Status taskStatus, int epicId) {
        super(taskName, taskDescription, taskStatus);
        this.epicId = epicId;
    }

    public Subtask(String taskName, String taskDescription, Status taskStatus, int epicId,
                   Duration duration, LocalDateTime startTime) {
        super(taskName, taskDescription, taskStatus, duration, startTime);
        this.epicId = epicId;
    }

    public int getEpicId() {
        return epicId;
    }

    @Override
    public String toString() {
        return "Подзадача{" +
                "Идентификатор=" + super.getTaskId() +
                " Идентификатор эпик-задачи=" + getEpicId() +
                ", Наименование='" + super.getTaskName() + '\'' +
                ", Описание='" + super.getTaskDescription() + '\'' +
                ", Статус='" + super.getTaskStatus() + '\'' +
                ", Продолжительность='" + super.getDuration().toMinutes() + '\'' +
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
        return String.format("%s,%s,%s,%s,%s,%s\n", super.getTaskId(), TypesOfTasks.SUBTASK, super.getTaskName(),
                super.getTaskStatus(), super.getTaskDescription(), getEpicId());
    }

}
