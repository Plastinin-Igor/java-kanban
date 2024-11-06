package service;

import model.*;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;

public class TaskManager {
    private int id = 0;

    private HashMap<Integer, Task> taskMap = new HashMap<>();
    private HashMap<Integer, Epic> epicMap = new HashMap<>();
    private HashMap<Integer, Subtask> subtaskMap = new HashMap<>();

    /**
     * Генерация уникального идентификатора задачи/эпика/подзадачи
     */
    private void generateIdentifier() {
        id++;
    }

    /**
     * Получение списка задач
     *
     * @return List Task
     */
    public Collection<Task> getListTask() {
        return taskMap.values();
    }

    /**
     * Получение списка эпик-задач
     *
     * @return List Epic
     */
    public Collection<Epic> getListEpic() {
        return epicMap.values();
    }

    /**
     * Получение подзадач
     *
     * @return List Subtask
     */
    public Collection<Subtask> getListSubtask() {
        return subtaskMap.values();
    }

    /**
     * Удаление всех задач
     */
    public void deleteTask() {
        taskMap.clear();
    }

    /**
     * Удаление всех эпик-задач
     */
    public void deleteEpic() {
        subtaskMap.clear(); //Удаление подзадач
        epicMap.clear(); //Удаление эпиков
    }

    /**
     * Удаление всех подзадач
     */
    public void deleteSubtask() {
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
    public Task getTaskById(int taskId) {
        return taskMap.get(taskId);
    }

    /**
     * Получение по идентификатору эпик-задачи
     *
     * @param taskId идентификатор эпика
     * @return Epic объект
     */
    public Epic getEpicById(int taskId) {
        return epicMap.get(taskId);
    }

    /**
     * Получение по идентификатору подзадачи
     *
     * @param taskId идентификатор подзадачи
     * @return Subtask объект
     */
    public Subtask getSubtaskById(int taskId) {
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
    public void deleteTaskById(int taskId) {
        taskMap.remove(taskId);
    }

    /**
     * Удаление эпик-задачи по идентификатору
     *
     * @param taskId int
     */
    public void deleteEpicById(int taskId) {
        if (epicMap.containsKey(taskId)) {
            // Удаление, связанных с эпиком подзадач
            for (int i = 0; i < epicMap.get(taskId).getSubtaskListId().size(); i++) {
                epicMap.get(taskId).getSubtaskListId().remove(i);
            }
            //Удаляем эпик
            epicMap.remove(taskId);
        }
    }

    /**
     * Удаление подзадачи по идентификатору
     *
     * @param taskId int
     */
    public void deleteSubtaskById(int taskId) {
        Subtask subtask = subtaskMap.get(taskId);
        if (subtask != null) {
            epicMap.get(subtask.getEpicId()).deleteSubtaskIdById(taskId); // удаляем подзадачу из эпика
            subtaskMap.remove(taskId); // удаляем подзадачу
            updateEpicStatus(epicMap.get(subtask.getEpicId())); // обновление статуса в эпик-задаче
        }
    }

    /**
     * Получение списка всех подзадач определенного эпика
     *
     * @param epicId int
     * @return ArrayList Subtask
     */
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

}
