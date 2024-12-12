package model;

public class Task extends TaskItem {
    public Task(int taskId, String taskName, String taskDescription, Status taskStatus) {
        super(taskId, taskName, taskDescription, taskStatus);
    }

    public Task(String taskName, String taskDescription, Status taskStatus) {
        super(taskName, taskDescription, taskStatus);
    }

    @Override
    public String toString() {
        return "Простая задача{" +
                "Идентификатор=" + super.getTaskId() +
                ", Наименование='" + super.getTaskName() + '\'' +
                ", Описание='" + super.getTaskDescription() + '\'' +
                ", Статус='" + super.getTaskStatus() + '\'' +
                '}';
    }
}
