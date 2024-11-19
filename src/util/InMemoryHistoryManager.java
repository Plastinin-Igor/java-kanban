package util;

import model.HistoryManager;
import model.Task;
import model.TaskItem;

import java.util.ArrayList;
import java.util.Collection;

/**
 * Управление историей просмотров
 * add() - Добавление просмотренной задачи в историю
 * getHistory() - Список просмотренных задач
 */

public class InMemoryHistoryManager implements HistoryManager {

    private ArrayList<TaskItem> historyList;

    public InMemoryHistoryManager() {
        this.historyList = new ArrayList<>();
    }

    /**
     * Добавление просмотренной задачи в историю
     * Список для хранения просмотров не превышает 10 элементов
     *
     * @param task Task
     */
    @Override
    public void add(TaskItem task) {
        if (historyList.size() == 10) {
            historyList.remove(0);
        }
        historyList.add(task);
    }

    /**
     * Список просмотренных задач
     *
     * @return Collection TaskItem
     */
    @Override
    public Collection<TaskItem> getHistory() {
        return new ArrayList<>(historyList);
    }
}
