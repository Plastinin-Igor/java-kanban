package model;

public class Subtask extends TaskItem {
    private final int epicId;

    public Subtask(int taskId, String taskName, String taskDescription, Status taskStatus, int epicId) {
        super(taskId, taskName, taskDescription, taskStatus);
        this.epicId = epicId;
    }

    public Subtask(String taskName, String taskDescription, Status taskStatus, int epicId) {
        super(taskName, taskDescription, taskStatus);
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
