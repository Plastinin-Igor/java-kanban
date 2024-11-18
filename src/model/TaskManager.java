package model;

import java.util.ArrayList;
import java.util.Collection;

/**
 * Методы, которые должны быть у любого объекта-менеджера
 */
public interface TaskManager {

    /**
     * Получение списка задач
     *
     * @return List Task
     */
    Collection<Task> getListTask();

    /**
     * Получение списка эпик-задач
     *
     * @return List Epic
     */
    Collection<Epic> getListEpic();

    /**
     * Получение подзадач
     *
     * @return List Subtask
     */
    Collection<Subtask> getListSubtask();

    /**
     * Удаление всех задач
     */
    void deleteTask();

    /**
     * Удаление всех эпик-задач
     */
    void deleteEpic();

    /**
     * Удаление всех подзадач
     */
    void deleteSubtask();

    /**
     * Получение по идентификатору задачи
     *
     * @param taskId идентификатор задачи
     * @return Task объект
     */
    Task getTaskById(int taskId);

    /**
     * Получение по идентификатору эпик-задачи
     *
     * @param taskId идентификатор эпика
     * @return Epic объект
     */
    Epic getEpicById(int taskId);

    /**
     * Получение по идентификатору подзадачи
     *
     * @param taskId идентификатор подзадачи
     * @return Subtask объект
     */
    Subtask getSubtaskById(int taskId);

    /**
     * Добавление обычной задачи
     *
     * @param task Task
     * @return Task
     */
    Task addNewTask(Task task);

    /**
     * Добавление эпик-задачи
     *
     * @param epic Epic
     * @return Epic
     */
    Epic addNewEpic(Epic epic);

    /**
     * Добавление подзадачи в эпик-задачу
     *
     * @param subtask Subtask
     * @return Subtask
     */
    Subtask addNewSubtask(Subtask subtask);

    /**
     * Обновление обычной задачи.
     * Обновляются только те задачи, которые уже есть в хранилище taskMap
     *
     * @param task Task
     * @return Task
     */
    Task updateTask(Task task);

    /**
     * Обновление эпик-задачи
     *
     * @param epic Epic
     * @return Epic
     */
    Epic updateEpic(Epic epic);

    /**
     * Обновление подзадачи
     *
     * @param subtask Subtask
     * @return Subtask
     */
    Subtask updateSubtask(Subtask subtask);

    /**
     * Удаление задачи по идентификатору
     *
     * @param taskId int
     */
    void deleteTaskById(int taskId);

    /**
     * Удаление эпик-задачи по идентификатору
     *
     * @param taskId int
     */
    void deleteEpicById(int taskId);

    /**
     * Удаление подзадачи по идентификатору
     *
     * @param taskId int
     */
    void deleteSubtaskById(int taskId);

    /**
     * Получение списка всех подзадач определенного эпика
     *
     * @param epicId int
     * @return ArrayList Subtask
     */
    Collection<Subtask> getSubtaskByEpicId(int epicId);

    /**
     * Список просмотренных задач
     *
     * @return Collection TaskItem
     */
    Collection<TaskItem> getHistory();
}
