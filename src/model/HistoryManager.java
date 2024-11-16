package model;

import java.util.Collection;

public interface HistoryManager {

    void add(TaskItem task);

    Collection<TaskItem> getHistory();

}
