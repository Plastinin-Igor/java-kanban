package service;

import model.*;
import util.*;

import java.time.Duration;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;
import java.util.Comparator;
import java.util.Collection;
import java.util.List;
import java.util.ArrayList;
import java.util.stream.Collectors;

public class InMemoryTaskManager implements TaskManager {
    private int id = 0;

    private final Map<Integer, Task> taskMap = new HashMap<>();
    private final Map<Integer, Epic> epicMap = new HashMap<>();
    private final Map<Integer, Subtask> subtaskMap = new HashMap<>();
    //Отсортированные по полю startTime задачи, подзадачи, эпики
    private final Set<TaskItem> prioritizedTasksTree = new TreeSet<>(Comparator.comparing(TaskItem::getStartTime));

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
     * Расчет продолжительности эпика; определение даты начала и даты окончания.
     * Продолжительность эпика — сумма продолжительностей всех его подзадач.
     * Время начала — дата старта самой ранней подзадачи, а время завершения
     * — время окончания самой поздней из задач.
     *
     * @param epic Epic
     */
    private void calculateTimeForEpic(Epic epic) {
        Duration duration = Duration.ofMinutes(0);
        List<Subtask> listSubtask = new ArrayList<>();

        if (!epic.getSubtaskListId().isEmpty()) {
            for (Integer epicsSub : epic.getSubtaskListId()) {
                duration = duration.plus(subtaskMap.get(epicsSub).getDuration());
                epic.setDuration(duration);
                listSubtask.add(subtaskMap.get(epicsSub));
            }
            listSubtask.sort(Comparator.comparing(TaskItem::getStartTime));
            epic.setStartTime(listSubtask.getFirst().getStartTime());
            epic.setEndTime(listSubtask.getLast().getEndTime());
        }
    }

    /**
     * Список задач в порядке приоритета
     *
     * @return TreeSet TaskItem
     */
    @Override
    public List<TaskItem> getPrioritizedTasks() {
        return new ArrayList<>(prioritizedTasksTree);
    }

    /**
     * Пересекаются ли две задачи по времени выполнения
     *
     * @param newTask      TaskItem
     * @param existingTask TaskItem
     * @return boolean
     */
    private boolean isIntersect(TaskItem newTask, TaskItem existingTask) {
        if (newTask.getStartTime() == null || newTask.getEndTime() == null
                || existingTask.getStartTime() == null || existingTask.getEndTime() == null) {
            return false;
        } else {
            return newTask.getStartTime().isBefore(existingTask.getEndTime())
                    && existingTask.getStartTime().isBefore(newTask.getEndTime());
        }
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
            prioritizedTasksTree.remove(taskMap.get(taskId));
        }
        taskMap.clear();
    }

    /**
     * Удаление всех эпик-задач
     */
    @Override
    public void deleteEpic() {
        // Удаление истории просмотров эпиков
        epicMap.keySet().stream()
                .peek(epicId -> {
                    historyManager.remove(epicId);
                    prioritizedTasksTree.remove(epicMap.get(epicId));
                })
                .count();
        // Удаление истории просмотров подзадач
        subtaskMap.keySet().stream()
                .peek(subtaskId -> {
                    historyManager.remove(subtaskId);
                    prioritizedTasksTree.remove(subtaskMap.get(subtaskId));
                })
                .count();

        subtaskMap.clear(); //Удаление подзадач
        epicMap.clear(); //Удаление эпиков
    }

    /**
     * Удаление всех подзадач
     */
    @Override
    public void deleteSubtask() {
        for (int subtaskId : subtaskMap.keySet()) {
            historyManager.remove(subtaskId); // Удаление истории просмотров подзадач
            prioritizedTasksTree.remove(subtaskMap.get(subtaskId)); // Удаление из отсторитрованного списка
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
     * Получение списка всех подзадач определенного эпика
     *
     * @param epicId int
     * @return ArrayList Subtask
     */
    @Override
    public ArrayList<Subtask> getSubtaskByEpicId(int epicId) {
        Epic epic = epicMap.get(epicId);
        if (epic != null) {
            return (ArrayList<Subtask>) subtaskMap.values().stream()
                    .filter(subtask -> subtask.getEpicId() == epicId)
                    .collect(Collectors.toList());
        }
        return null;
    }

    /**
     * Добавление обычной задачи
     *
     * @param task Task
     * @return Task
     */
    @Override
    public Task addNewTask(Task task) {
        boolean isIntersect = prioritizedTasksTree.stream()
                .anyMatch(existingTask -> isIntersect(task, existingTask));
        if (!isIntersect) {
            generateIdentifier();
            task.setTaskId(id);
            taskMap.put(id, task);
            if (task.getStartTime() != null) {
                prioritizedTasksTree.add(task);
            }
            return task;
        } else {
            return null;
        }
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
        if (epic.getStartTime() != null) {
            prioritizedTasksTree.add(epic);
        }
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
            boolean isIntersect = prioritizedTasksTree.stream()
                    .anyMatch(existingTask -> isIntersect(subtask, existingTask));
            if (!isIntersect) {
                generateIdentifier();
                subtask.setTaskId(id);
                epicMap.get(subtask.getEpicId()).addNewSubtaskId(id); // добавление id подзадачи в эпик
                subtaskMap.put(id, subtask);
                updateEpicStatus(epicMap.get(subtask.getEpicId())); // обновление статуса в эпик-задаче
                calculateTimeForEpic(epicMap.get(subtask.getEpicId())); // обновляем расчет времени в эпике
                if (subtask.getStartTime() != null) {
                    prioritizedTasksTree.add(subtask);
                }
                return subtask;
            } else {
                return null;
            }
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
            boolean isIntersect = prioritizedTasksTree.stream()
                    .anyMatch(taskExisting -> isIntersect(task, taskExisting));
            if (!isIntersect) {
                taskMap.put(task.getTaskId(), task);
                return task;
            } else {
                return null;
            }
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
            boolean isIntersect = prioritizedTasksTree.stream()
                    .anyMatch(existingTask -> isIntersect(epic, existingTask));
            if (!isIntersect) {
                Epic epicLocal = epicMap.get(epic.getTaskId());
                epicLocal.setTaskName(epic.getTaskName());
                epicLocal.setTaskDescription(epic.getTaskDescription());
                return epic;
            } else {
                return null;
            }
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
                calculateTimeForEpic(epicMap.get(subtask.getEpicId())); // обновляем расчет времени в эпике
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
        prioritizedTasksTree.removeIf(task -> task.getTaskId() == taskId);
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
            epicMap.get(taskId).getSubtaskListId().stream()
                    .peek(subtask -> {
                        subtaskMap.remove(subtask);
                        historyManager.remove(subtask);
                    })
                    .forEach(subtask -> prioritizedTasksTree.removeIf(task -> task.getTaskId() == subtask));
            //Удаляем эпик
            epicMap.remove(taskId);
            historyManager.remove(taskId);
            prioritizedTasksTree.removeIf(epic -> epic.getTaskId() == taskId);
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
            calculateTimeForEpic(epicMap.get(subtask.getEpicId()));
            historyManager.remove(taskId);
            prioritizedTasksTree.removeIf(task -> task.getTaskId() == taskId);
        }
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
        if (task.getStartTime() != null) {
            prioritizedTasksTree.add(task);
        }
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
        if (epic.getStartTime() != null) {
            prioritizedTasksTree.add(epic);
        }
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
            if (subtask.getStartTime() != null) {
                prioritizedTasksTree.add(subtask);
            }
            return subtask;
        } else {
            System.out.println("Не найден эпик с ID " + subtask.getEpicId());
            return null;
        }
    }
}