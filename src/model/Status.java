package model;

public enum Status {
    NEW ("Новая задача"),
    IN_PROGRESS ("Над задачей ведётся работа"),
    DONE ("Задача выполнена");

    private String title;
    Status(String title) {
        this.title = title;
    }

    public String getTitle() {
        return title;
    }

    @Override
    public String toString() {
        return title;
    }
}
