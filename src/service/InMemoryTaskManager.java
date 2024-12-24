package service;

import model.*;
import util.*;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class InMemoryTaskManager implements TaskManager {
    private int id = 0;

    private final Map<Integer, Task> taskMap = new HashMap<>();
    private final Map<Integer, Epic> epicMap = new HashMap<>();
    private final Map<Integer, Subtask> subtaskMap = new HashMap<>();

    HistoryManager historyManager = Managers.getDefaultHistory();

    /**
     * Генерация уникального идентификатора задачи/эпика/подзадачи
     */
    private void generateIdentifier() {
        id++;
    }

    /**
     * Актуализация счетчика Id при восстановлении данных из файла
     *
     * @param id int
     */
    protected void setId(int id) {
        this.id = id;
    }

    /**
     * Получение списка задач
     *
     * @return List Task
     */
    @Override
    public Collection<Task> getListTask() {
        return taskMap.values();
    }

    /**
     * Получение списка эпик-задач
     *
     * @return List Epic
     */
    @Override
    public Collection<Epic> getListEpic() {
        return epicMap.values();
    }

    /**
     * Получение подзадач
     *
     * @return List Subtask
     */
    @Override
    public Collection<Subtask> getListSubtask() {
        return subtaskMap.values();
    }

    /**
     * Удаление всех задач
     */
    @Override
    public void deleteTask() {
        // Удаление истории просмотров
        for (int taskId : taskMap.keySet()) {
            historyManager.remove(taskId);
        }
        taskMap.clear();
    }

    /**
     * Удаление всех эпик-задач
     */
    @Override
    public void deleteEpic() {
        // Удаление истории просмотров эпиков
        for (int epicId : epicMap.keySet()) {
            historyManager.remove(epicId);
        }
        // Удаление истории просмотров подзадач
        for (int subtaskId : subtaskMap.keySet()) {
            historyManager.remove(subtaskId);
        }
        subtaskMap.clear(); //Удаление подзадач
        epicMap.clear(); //Удаление эпиков
    }

    /**
     * Удаление всех подзадач
     */
    @Override
    public void deleteSubtask() {
        // Удаление истории просмотров подзадач
        for (int subtaskId : subtaskMap.keySet()) {
            historyManager.remove(subtaskId);
        }
        //Удаление подзадач
        subtaskMap.clear();
        //Удаление подзадач из всех эпиков обновление статуса в эпиках
        for (Integer epicLocal : epicMap.keySet()) {
            epicMap.get(epicLocal).deleteAllSubtaskId();
            updateEpicStatus(epicMap.get(epicLocal));
        }
    }

    /**
     * Получение по идентификатору задачи
     *
     * @param taskId идентификатор задачи
     * @return Task объект
     */
    @Override
    public Task getTaskById(int taskId) {
        historyManager.add(taskMap.get(taskId));
        return taskMap.get(taskId);
    }

    /**
     * Получение по идентификатору эпик-задачи
     *
     * @param taskId идентификатор эпика
     * @return Epic объект
     */
    @Override
    public Epic getEpicById(int taskId) {
        historyManager.add(epicMap.get(taskId));
        return epicMap.get(taskId);
    }

    /**
     * Получение по идентификатору подзадачи
     *
     * @param taskId идентификатор подзадачи
     * @return Subtask объект
     */
    @Override
    public Subtask getSubtaskById(int taskId) {
        historyManager.add(subtaskMap.get(taskId));
        return subtaskMap.get(taskId);
    }

    /**
     * Обновление статуса в эпик-задаче
     * Если у эпика нет подзадач то его статус NEW;
     * Или все подзадачи имеют статус NEW, тогда эпик тоже NEW,
     * если все подзадачи имеют статус DONE, то и у эпика статус тоже DONE
     * во всех остальных случаях статус - IN_PROGRESS
     *
     * @param epic Epic
     */
    private void updateEpicStatus(Epic epic) {
        //Если у эпика нет подзадач - статус NEW
        if (epic.getSubtaskListId().isEmpty()) {
            epic.setTaskStatus(Status.NEW);
            return;
        }
        //Или
        boolean isNew = true;
        boolean isDone = true;
        Status status;
        for (Integer epicsSub : epic.getSubtaskListId()) {
            status = subtaskMap.get(epicsSub).getTaskStatus();

            if (status != Status.NEW) {
                isNew = false;
            }
            if (status != Status.DONE) {
                isDone = false;
            }
        }
        if (isNew) { // все подзадачи имеют статус NEW - эпик тоже NEW
            epic.setTaskStatus(Status.NEW);
        } else if (isDone) { // если все подзадачи имеют статус DONE, то и у эпика статус DONE.
            epic.setTaskStatus(Status.DONE);
        } else { // во всех остальных случаях - IN_PROGRESS
            epic.setTaskStatus(Status.IN_PROGRESS);
        }
    }

    /**
     * Добавление обычной задачи
     *
     * @param task Task
     * @return Task
     */
    @Override
    public Task addNewTask(Task task) {
        generateIdentifier();
        task.setTaskId(id);
        taskMap.put(id, task);
        return task;
    }

    /**
     * Добавление эпик-задачи
     *
     * @param epic Epic
     * @return Epic
     */
    @Override
    public Epic addNewEpic(Epic epic) {
        generateIdentifier();
        epic.setTaskId(id);
        epicMap.put(id, epic);
        return epic;
    }

    /**
     * Добавление подзадачи в эпик-задачу
     *
     * @param subtask Subtask
     * @return Subtask
     */
    @Override
    public Subtask addNewSubtask(Subtask subtask) {
        if (epicMap.containsKey(subtask.getEpicId())) {
            generateIdentifier();
            subtask.setTaskId(id);
            epicMap.get(subtask.getEpicId()).addNewSubtaskId(id); // добавление id подзадачи в эпик
            subtaskMap.put(id, subtask);
            updateEpicStatus(epicMap.get(subtask.getEpicId())); // обновление статуса в эпик-задаче
            return subtask;
        } else {
            System.out.println("Не найден эпик с ID " + subtask.getEpicId());
            return null;
        }
    }

    /**
     * Обновление обычной задачи.
     * Обновляются только те задачи, которые уже есть в хранилище taskMap
     *
     * @param task Task
     * @return Task
     */
    @Override
    public Task updateTask(Task task) {
        if (taskMap.containsKey(task.getTaskId())) {
            taskMap.put(task.getTaskId(), task);
            return task;
        } else {
            return null;
        }
    }

    /**
     * Обновление эпик-задачи
     *
     * @param epic Epic
     * @return Epic
     */
    @Override
    public Epic updateEpic(Epic epic) {
        if (epicMap.containsKey(epic.getTaskId())) {
            Epic epicLocal = epicMap.get(epic.getTaskId());
            epicLocal.setTaskName(epic.getTaskName());
            epicLocal.setTaskDescription(epic.getTaskDescription());
            return epic;
        } else {
            return null;
        }
    }

    /**
     * Обновление подзадачи
     *
     * @param subtask Subtask
     * @return Subtask
     */
    @Override
    public Subtask updateSubtask(Subtask subtask) {
        // проверяем, есть ли такая подзадача
        if (subtaskMap.containsKey(subtask.getTaskId())) {
            //проверяем равен ли epicId в новой подзадаче со значением epicId подзадачи
            if (subtask.getEpicId() == subtaskMap.get(subtask.getTaskId()).getEpicId()) {
                subtaskMap.put(subtask.getTaskId(), subtask);
                updateEpicStatus(epicMap.get(subtask.getEpicId())); // обновление статуса в эпик-задаче
                return subtask;
            } else {
                System.out.println("Подзадачи не переходят между Эпиками");
                return null;
            }
        } else {
            return null;
        }
    }

    /**
     * Удаление задачи по идентификатору
     *
     * @param taskId int
     */
    @Override
    public void deleteTaskById(int taskId) {
        taskMap.remove(taskId);
        historyManager.remove(taskId);
    }

    /**
     * Удаление эпик-задачи по идентификатору
     *
     * @param taskId int
     */
    @Override
    public void deleteEpicById(int taskId) {
        if (epicMap.containsKey(taskId)) {
            // Удаление, связанных с эпиком подзадач
            for (Integer subtask : epicMap.get(taskId).getSubtaskListId()) {
                subtaskMap.remove(subtask);
                historyManager.remove(subtask);
            }
            //Удаляем эпик
            epicMap.remove(taskId);
            historyManager.remove(taskId);
        }
    }

    /**
     * Удаление подзадачи по идентификатору
     *
     * @param taskId int
     */
    @Override
    public void deleteSubtaskById(int taskId) {
        Subtask subtask = subtaskMap.get(taskId);
        if (subtask != null) {
            epicMap.get(subtask.getEpicId()).deleteSubtaskIdById(taskId); // удаляем подзадачу из эпика
            subtaskMap.remove(taskId); // удаляем подзадачу
            updateEpicStatus(epicMap.get(subtask.getEpicId())); // обновление статуса в эпик-задаче
            historyManager.remove(taskId);
        }
    }

    /**
     * Получение списка всех подзадач определенного эпика
     *
     * @param epicId int
     * @return ArrayList Subtask
     */
    @Override
    public ArrayList<Subtask> getSubtaskByEpicId(int epicId) {
        Epic epic = epicMap.get(epicId);
        ArrayList<Subtask> subtasks = new ArrayList<>();
        if (epic != null) {
            for (int i = 0; i < epic.getSubtaskListId().size(); i++) {
                subtasks.add(subtaskMap.get(epic.getSubtaskListId().get(i)));
            }
        }
        return subtasks;
    }

    /**
     * Список просмотренных задач
     *
     * @return Collection TaskItem
     */
    public Collection<TaskItem> getHistory() {
        return historyManager.getHistory();
    }


    /**
     * Добавление обычной задачи;
     * метод используется для восстановления данных из файла
     *
     * @param task Task
     * @return Task
     */

    protected Task addTask(Task task) {
        taskMap.put(task.getTaskId(), task);
        return task;
    }

    /**
     * Добавление эпик-задачи;
     * метод используется для восстановления данных из файла
     *
     * @param epic Epic
     * @return Epic
     */

    protected Epic addEpic(Epic epic) {
        epicMap.put(epic.getTaskId(), epic);
        return epic;
    }

    /**
     * Добавление подзадачи в эпик-задачу;
     * метод используется для восстановления данных из файла
     *
     * @param subtask Subtask
     * @return Subtask
     */
    protected Subtask addSubtask(Subtask subtask) {
        if (epicMap.containsKey(subtask.getEpicId())) {
            epicMap.get(subtask.getEpicId()).addNewSubtaskId(subtask.getTaskId()); // добавление id подзадачи в эпик
            subtaskMap.put(subtask.getTaskId(), subtask);
            updateEpicStatus(epicMap.get(subtask.getEpicId())); // обновление статуса в эпик-задаче
            return subtask;
        } else {
            System.out.println("Не найден эпик с ID " + subtask.getEpicId());
            return null;
        }
    }

}
