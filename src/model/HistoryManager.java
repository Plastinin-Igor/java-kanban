package model;

import java.util.Collection;

/**
 * Управление историей просмотров
 */
public interface HistoryManager {

    /**
     * Добавление задачи в историю
     *
     * @param task TaskItem
     */
    void add(TaskItem task);

    /**
     * Удаление задачи из просмотра
     *
     * @param id int
     */
    void remove(int id);

    /**
     * Получение списка просмотренных пользователем задач
     *
     * @return Collection TaskItem
     */
    Collection<TaskItem> getHistory();

}
