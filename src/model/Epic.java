package model;

public class Epic extends TaskItem{
    public Epic(int taskId, String taskName, String taskDescription, Status taskStatus) {
        super(taskId, taskName, taskDescription, taskStatus);
    }

    public Epic(String taskName, String taskDescription, Status taskStatus) {
        super(taskName, taskDescription, taskStatus);
    }

    @Override
    public String toString() {
        return "Эпик-задача{" +
                "Идентификатор=" + super.getTaskId() +
                ", Наименование='" + super.getTaskName() + '\'' +
                ", Описание='" + super.getTaskDescription() + '\'' +
                ", Статус='" + super.getTaskStatus() + '\'' +
                '}';
    }
}
