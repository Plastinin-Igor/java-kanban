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
        subtaskMap.clear(); //Удаление подзадач из эпика
        epicMap.clear(); //Удаление самого эпика
    }

    /**
     * Удаление всех подзадач
     */
    public void deleteSubtask() {

        subtaskMap.clear();
    }

    /**
     * Получение по идентификатору задачи
     *
     * @param taskId int
     * @return Task
     */
    public Task getTaskById(int taskId) {
        return taskMap.get(taskId);
    }

    /**
     * Получение по идентификатору эпик-задачи
     *
     * @param taskId int
     * @return Epic
     */
    public Epic getEpicById(int taskId) {
        return epicMap.get(taskId);
    }

    /**
     * Получение по идентификатору подзадачи
     *
     * @param taskId int
     * @return Subtask
     */
    public Subtask getSubtaskById(int taskId) {
        return subtaskMap.get(taskId);
    }

    /**
     * Обновление статуса в эпик-задаче
     *
     * @param epic Epic
     */
    private void updateEpicStatus(Epic epic) {
        //Если у эпика нет подзадач - статус NEW
        if (epic.getSubtaskId().isEmpty()) {
            epic.setTaskStatus(Status.NEW);
            return;
        }
        //Или
        boolean isNew = true;
        boolean isDone = true;
        Status status;
        for (Integer epicsSub : epic.getSubtaskId()) {
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
            ArrayList<Integer> subtaskId = epicMap.get(subtask.getEpicId()).getSubtaskId();
            subtaskId.add(id);
            subtaskMap.put(id, subtask);
            updateEpicStatus(epicMap.get(subtask.getEpicId())); // обновление статуса в эпик-задаче
            return subtask;
        } else {
            System.out.println("Не найден эпик с ID " + subtask.getEpicId());
            return null;
        }
    }

    /**
     * Обновление обычной задачи
     *
     * @param task Task
     * @return Task
     */
    public Task updateTask(Task task) {
        taskMap.put(task.getTaskId(), task);
        return task;
    }

    /**
     * Обновление эпик-задачи
     *
     * @param epic Epic
     * @return Epic
     */
    public Epic updateEpic(Epic epic) {
        if (epicMap.containsKey(epic.getTaskId())) {
            Epic epicLocal = getEpicById(epic.getTaskId());
            epicLocal.setTaskName(epic.getTaskName());
            epicLocal.setTaskDescription(epic.getTaskDescription());
            epicMap.put(epic.getTaskId(), epicLocal);
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
        subtaskMap.put(subtask.getTaskId(), subtask);
        updateEpicStatus(epicMap.get(subtask.getEpicId())); // обновление статуса в эпик-задаче
        return subtask;
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
        //Удаляем подзадачи (если есть) из эпика
        if (!epicMap.get(taskId).getSubtaskId().isEmpty()) {
            epicMap.get(taskId).getSubtaskId().clear();
        }
        //Удаляем эпик
        epicMap.remove(taskId);
    }

    /**
     * Удаление подзадачи по идентификатору
     *
     * @param taskId int
     */
    public void deleteSubtaskById(int taskId) {
        //Удалим информацию о подзадаче из эпик-задачи
        Subtask subtask;
        subtask = subtaskMap.get(taskId);
        if (epicMap.containsKey(subtask.getEpicId())) {
            ArrayList<Integer> subtaskId = epicMap.get(subtask.getEpicId()).getSubtaskId();
            int indexOf = subtaskId.indexOf(taskId);
            subtaskId.remove(indexOf);
        }
        subtaskMap.remove(taskId); // удаляем подзадачу
        updateEpicStatus(epicMap.get(subtask.getEpicId())); // обновление статуса в эпик-задаче
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
        for (int i = 0; i < epic.getSubtaskId().size(); i++) {
            subtasks.add(subtaskMap.get(epic.getSubtaskId().get(i)));
        }
        return subtasks;
    }

}
