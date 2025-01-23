package service;

import model.*;
import org.junit.jupiter.api.Assertions;

import org.junit.jupiter.api.Test;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;

class InMemoryTaskManagerTest {

    InMemoryTaskManager taskManager = new InMemoryTaskManager();


    Task taskObj = new Task("Задача № 1", "Описание задачи №1", Status.NEW,
            Duration.ofMinutes(1), LocalDateTime.of(2025, 1, 1, 10, 0));
    Epic epicObj = new Epic("Эпик-задача № 1", "Описание эпик-задачи №1");
    Epic epic1 = taskManager.addNewEpic(epicObj);
    Subtask subtaskObj = new Subtask("Первая подзадача в эпике № 1", "Первая подзадача в эпике № 1",
            Status.NEW, epic1.getTaskId(), Duration.ofMinutes(10),
            LocalDateTime.of(2025, 1, 1, 10, 30));

    @Test
    void getListTask() {
        taskManager.addNewTask(taskObj);
        Collection<Task> tasks = taskManager.getListTask();

        Assertions.assertNotNull(tasks, "Задачи не возвращаются");
        Assertions.assertEquals(1, tasks.size(), "Неверное количество задач");
        Assertions.assertTrue(tasks.contains(taskObj), "Задачи не совпадают");

    }

    @Test
    void getListEpic() {
        Collection<Epic> epics = taskManager.getListEpic();

        Assertions.assertNotNull(epics, "Эпики не возвращаются");
        Assertions.assertEquals(1, epics.size(), "Неверное количество эпиков");
        Assertions.assertTrue(epics.contains(epicObj), "Эпики не совпадают");
    }

    @Test
    void getListSubtask() {
        taskManager.addNewSubtask(subtaskObj);
        Collection<Subtask> subtasks = taskManager.getListSubtask();

        Assertions.assertNotNull(subtasks, "Сабтаски не возвращаются");
        Assertions.assertEquals(1, subtasks.size(), "Неверное количество сабтасков");
        Assertions.assertTrue(subtasks.contains(subtaskObj), "Сабтаски не совпадают");
    }

    @Test
    void deleteTask() {
        taskManager.addNewTask(taskObj);
        taskManager.deleteTask();
        Collection<Task> tasks = taskManager.getListTask();

        Assertions.assertEquals(0, tasks.size(), "Неверное количество задач");
    }

    @Test
    void deleteEpic() {
        taskManager.addNewEpic(epicObj);
        taskManager.deleteEpic();
        Collection<Epic> epics = taskManager.getListEpic();

        Assertions.assertEquals(0, epics.size(), "Неверное количество эпиков");
    }

    @Test
    void deleteSubtask() {
        taskManager.addNewSubtask(subtaskObj);
        taskManager.deleteSubtask();
        Collection<Subtask> subtasks = taskManager.getListSubtask();

        Assertions.assertEquals(0, subtasks.size(), "Неверное количество сабтасков");
    }

    @Test
    void getTaskById() {
        Task task = taskManager.addNewTask(taskObj);
        Task savedTask = taskManager.getTaskById(task.getTaskId());

        Assertions.assertNotNull(savedTask, "Задача не найдена");
        Assertions.assertEquals(task, savedTask, "Задачи не совпадают");
    }

    @Test
    void getEpicById() {
        Epic epic = taskManager.addNewEpic(epicObj);
        Epic savedEpic = taskManager.getEpicById(epic.getTaskId());

        Assertions.assertNotNull(savedEpic, "Эпик не найден");
        Assertions.assertEquals(epic, savedEpic, "Эпики не совпадают");
    }

    @Test
    void getSubtaskById() {
        Subtask subtask = taskManager.addNewSubtask(subtaskObj);
        Subtask savedSubtask = taskManager.getSubtaskById(subtask.getTaskId());

        Assertions.assertNotNull(savedSubtask, "Сабтаск не найден");
        Assertions.assertEquals(subtask, savedSubtask, "Сабтаски не совпадают");
    }

    @Test
    void addNewTask() {
        Task task = taskManager.addNewTask(taskObj);

        Assertions.assertNotNull(task, "Задача не найдена");
    }

    @Test
    void addNewEpic() {
        Epic epic = taskManager.addNewEpic(epicObj);

        Assertions.assertNotNull(epic, "Задача не найден");
    }

    @Test
    void addNewSubtask() {
        Subtask subtask = taskManager.addNewSubtask(subtaskObj);

        Assertions.assertNotNull(subtask, "Сабтаск не найден");
    }

    @Test
    void updateTask() {
        Task task = taskManager.addNewTask(taskObj);
        int taskId = task.getTaskId();
        Task taskUpdate = new Task(taskId, "Задача № 1", "Описание задачи №1", Status.IN_PROGRESS,
                Duration.ofMinutes(1), LocalDateTime.of(2025, 1, 1, 10, 0));
        taskManager.updateTask(taskUpdate);
        Task taskAfterUpdate = taskManager.getTaskById(taskId);

        Assertions.assertEquals(taskUpdate, taskAfterUpdate, "Задачи не совпадают");
    }

    @Test
    void updateEpic() {
        Epic epic = taskManager.addNewEpic(epicObj);
        int taskId = epic.getTaskId();
        Epic epicUpdate = new Epic(taskId, "Эпик-задача № 1, исправленная",
                "Описание эпик-задачи №1,исправленная");
        taskManager.updateEpic(epicUpdate);
        Epic epicAfterUpdate = taskManager.getEpicById(taskId);

        Assertions.assertEquals(epicUpdate, epicAfterUpdate, "Эпики не совпадают");
    }

    @Test
    void updateSubtask() {
        Subtask subtask = taskManager.addNewSubtask(subtaskObj);

        int taskId = subtask.getTaskId();
        Status statusEpicBeforeUpdate = epicObj.getTaskStatus();

        Subtask subtaskUpdate = new Subtask(taskId, "Первая подзадача в эпике № 1", "Первая подзадача в эпике № 1",
                Status.DONE, epic1.getTaskId(), Duration.ofMinutes(10),
                LocalDateTime.of(2025, 1, 1, 10, 0));

        taskManager.updateSubtask(subtaskUpdate);
        Subtask subtaskAfterUpdate = taskManager.getSubtaskById(taskId);
        Assertions.assertEquals(subtaskUpdate, subtaskAfterUpdate, "Сабтаски не совпадают");
        Status statusEpicAfterUpdate = epicObj.getTaskStatus();
        Assertions.assertNotEquals(statusEpicBeforeUpdate, statusEpicAfterUpdate, "Статус эпика не обновился " +
                "после обновления статуса сабтаска");

    }

    @Test
    void deleteTaskById() {
        Task task = taskManager.addNewTask(taskObj);
        int taskId = task.getTaskId();
        taskManager.deleteTaskById(taskId);
        Task taskAfterDelete = taskManager.getTaskById(taskId);

        Assertions.assertNull(taskAfterDelete, "Задача не удалена");
    }

    @Test
    void deleteEpicById() {
        Epic epic = taskManager.addNewEpic(epicObj);
        int taskId = epic.getTaskId();
        taskManager.deleteEpicById(taskId);
        Epic epicAfterDelete = taskManager.getEpicById(taskId);

        Assertions.assertNull(epicAfterDelete, "Эпик не удален");
    }

    @Test
    void deleteSubtaskById() {
        Subtask subtask = taskManager.addNewSubtask(subtaskObj);
        int taskId = subtask.getTaskId();
        taskManager.deleteSubtaskById(taskId);
        Subtask subtaskAfterDelete = taskManager.getSubtaskById(taskId);

        Assertions.assertNull(subtaskAfterDelete, "Сабтаск не удален");
    }

    @Test
    void getSubtaskByEpicId() {
        Subtask subtask = taskManager.addNewSubtask(subtaskObj);
        int taskEpicId = subtask.getEpicId();
        ArrayList<Subtask> subtaskList = taskManager.getSubtaskByEpicId(taskEpicId);

        Assertions.assertEquals(subtaskList.size(), 1, "Сабтаски не найдены");
    }

    @Test
    void getHistory() {
        Task task = taskManager.addNewTask(taskObj);
        Epic epic = taskManager.addNewEpic(epicObj);
        Subtask subtask = taskManager.addNewSubtask(subtaskObj);

        int taskId = task.getTaskId();
        int epicId = epic.getTaskId();
        int subtaskId = subtask.getTaskId();

        taskManager.getTaskById(taskId);
        taskManager.getEpicById(epicId);
        taskManager.getSubtaskById(subtaskId);

        Collection<TaskItem> historyList = taskManager.getHistory();

        Assertions.assertNotNull(historyList, "История не сохраняется");
    }


    /**
     * ПРОВЕРКА ОБНОВЛЕНИЯ СТАТУСА В ЭПИК ЗАДАЧЕ
     * Алгоритм должен быть следующий:
     * Если у эпика нет подзадач то его статус NEW;
     * Или все подзадачи имеют статус NEW, тогда эпик тоже NEW,
     * если все подзадачи имеют статус DONE, то и у эпика статус тоже DONE
     * во всех остальных случаях статус - IN_PROGRESS
     */

    Epic epicObj1 = new Epic(2, "Эпик-задача № 1", "Описание эпик-задачи №1");
    Subtask subtaskObj1 = new Subtask(3, "Подзадача № 1", "Подзадача № 1", Status.NEW,
            2, Duration.ofMinutes(15), LocalDateTime.of(2025, 1, 1, 11, 15));
    Subtask subtaskObj2 = new Subtask(4, "Подзадача № 2", "Подзадача № 2", Status.NEW,
            2, Duration.ofMinutes(15), LocalDateTime.of(2025, 1, 1, 11, 30));
    Subtask subtaskObj3 = new Subtask(5, "Подзадача № 3", "3", Status.NEW, 2,
            Duration.ofMinutes(15), LocalDateTime.of(2025, 1, 1, 11, 45));

    @Test
    void EpicStatusCalculationTest() {
        // 1. ЕСЛИ У ЭПИКА НЕТ ПОДЗАДАЧ ТО ЕГО СТАТУС NEW
        // Добавляем эпк
        Epic epic = taskManager.addNewEpic(epicObj1);
        // Проверяем статус нового эпика, он должен быть NEW
        Assertions.assertEquals(Status.NEW, epic.getTaskStatus(), "Если у эпика нет подзадач то его статус NEW");

        // 2. ЕСЛИ ВСЕ ПОДЗАДАЧИ ИМЕЮТ СТАТУС NEW, ТОГДА ЭПИК ТОЖЕ NEW
        // Добавляем в эпик подзадачи со статусом NEW
        Subtask subtask1 = taskManager.addNewSubtask(subtaskObj1);
        Subtask subtask2 = taskManager.addNewSubtask(subtaskObj2);
        Subtask subtask3 = taskManager.addNewSubtask(subtaskObj3);

        // Проверяем статусы у новых подзадач и у эпика. У всех должно быть значение NEW
        Assertions.assertEquals(Status.NEW, subtask1.getTaskStatus(), "У новой подзадачи должен быть статус NEW");
        Assertions.assertEquals(Status.NEW, subtask2.getTaskStatus(), "У новой подзадачи должен быть статус NEW");
        Assertions.assertEquals(Status.NEW, subtask3.getTaskStatus(), "У новой подзадачи должен быть статус NEW");
        Assertions.assertEquals(Status.NEW, epic.getTaskStatus(), "Если все подзадачи имеют статус NEW, тогда эпик тоже NEW");

        // 3. ЕСЛИ ВСЕ ПОДЗАДАЧИ ИМЕЮТ СТАТУС DONE, ТО И У ЭПИКА СТАТУС ТОЖЕ DONE
        // В подзадачах устанавливаем статус в значение DONE
        subtask1.setTaskStatus(Status.DONE);
        subtask2.setTaskStatus(Status.DONE);
        subtask3.setTaskStatus(Status.DONE);
        taskManager.updateSubtask(subtask1);
        taskManager.updateSubtask(subtask2);
        taskManager.updateSubtask(subtask3);

        // Все подзадачи должны иметь статус DONE, и у эпика статус тоже DONE
        Assertions.assertEquals(Status.DONE, subtask1.getTaskStatus(), "После изменения подзадачи, статус должне быть DONE");
        Assertions.assertEquals(Status.DONE, subtask2.getTaskStatus(), "После изменения подзадачи, статус должне быть DONE");
        Assertions.assertEquals(Status.DONE, subtask3.getTaskStatus(), "После изменения подзадачи, статус должне быть DONE");

        Assertions.assertEquals(Status.DONE, epic.getTaskStatus(), "Если все подзадачи имеют статус DONE, " +
                "то и у эпика статус тоже DONE");

        // 4. ВО ВСЕХ ОСТАЛЬНЫХ СЛУЧАЯХ СТАТУС - IN_PROGRESS
        // В подзадачах устанавливаем различные статусы
        subtask1.setTaskStatus(Status.NEW);
        subtask2.setTaskStatus(Status.IN_PROGRESS);
        subtask3.setTaskStatus(Status.DONE);
        taskManager.updateSubtask(subtask1);
        taskManager.updateSubtask(subtask2);
        taskManager.updateSubtask(subtask3);

        //В этом случае статус у эпика должен быть IN_PROGRESS
        Assertions.assertEquals(Status.IN_PROGRESS, epic.getTaskStatus(), "В этом случае статус у эпика " +
                "должен быть IN_PROGRESS");
    }

    /**
     * ПРОВЕРКА НА КОРРЕКТНОСТЬ РАСЧЁТА ПЕРЕСЕЧЕНИЯ ИНТЕРВАЛОВ
     * используется математический метод наложения отрезков
     */
    Task taskObj1 = new Task("Задача № 1", "Описание задачи №1", Status.NEW,
            Duration.ofMinutes(15), LocalDateTime.of(2025, 1, 1, 10, 15));
    Task taskObj2 = new Task("Задача № 2", "Описание задачи №2", Status.NEW,
            Duration.ofMinutes(15), LocalDateTime.of(2025, 1, 1, 10, 15));
    Task taskObj3 = new Task("Задача № 3", "Описание задачи №3", Status.NEW,
            Duration.ofMinutes(15), LocalDateTime.of(2025, 1, 1, 10, 15));

    @Test
    void IntersectionOfIntervalsTask() {
        // 1. Попытаемся добавить 3 задачи с одинаковыми интревалами
        taskManager.addNewTask(taskObj1);
        taskManager.addNewTask(taskObj2);
        taskManager.addNewTask(taskObj3);

        //Проверим, что в результате добавлена только одна задача
        Collection<Task> taskList = taskManager.getListTask();
        Assertions.assertEquals(1, taskList.size(), "Добавлена должна была только одна задача");

        //2. Поменяем интервал у второй задачи, сдвинув его на 15 минут. Теперь она должна добавиться.
        //   У задачи 3 поставим дату, которая пересекатся с первой и со второй задачей,
        //   она не должна в этом случае добавляться
        taskObj2.setStartTime(LocalDateTime.of(2025, 1, 1, 10, 30));
        taskObj3.setStartTime(LocalDateTime.of(2025, 1, 1, 10, 18));
        taskObj3.setDuration(Duration.ofMinutes(50)); // меняям продолжительность
        taskManager.addNewTask(taskObj2);
        taskManager.addNewTask(taskObj3);
        taskList = taskManager.getListTask();

        //Проверяем, что добавлены только две задачи
        Assertions.assertEquals(2, taskList.size(), "На этом этапе проверки должны быть добавлены 2 задачи");

        //3. Поменяем интервал у 3 задачи, так, что бы он не пересекался ни с первой, ни со второй.
        taskObj3.setStartTime(LocalDateTime.of(2025, 1, 1, 10, 45));
        taskManager.addNewTask(taskObj3);
        taskList = taskManager.getListTask();
        //Проверяем, что добавлены все три задачи
        Assertions.assertEquals(3, taskList.size(), "На этом этапе проверки должны быть добавлены 3 задачи");

    }

    /**
     * ПРОВЕРКА РАСЧЕТА ПРОДОЛЖИТЕЛЬНОСТИ ЭПИКА
     * Продолжительность эпика — сумма продолжительностей всех его подзадач.
     * Время начала — дата старта самой ранней подзадачи,
     * а время завершения — время окончания самой поздней из задач.
     */

    Epic epicObj12 = new Epic(10, "Эпик-задача № 1", "Описание эпик-задачи №1");

    @Test
    void calculateTimeForEpic() {
        // 1. У эпика без подзадач время начала, окончания не заданы, продолжительность равна нулю
        //Добавим эпик
        Epic epic = taskManager.addNewEpic(epicObj12);

        // Проверим это утверждение
        Assertions.assertEquals(epic.getDuration().toMinutes(), 0, "Продолжительность эпика без подзадач " +
                "должна быть равна нулю");
        Assertions.assertNull(epic.getStartTime(), "Дата начала у нового эпика должна быть не задана");
        Assertions.assertNull(epic.getEndTime(), "Дата завершения у нового эпика должна быть не задана");

        //2. Продолжительность эпика — сумма продолжительностей всех его подзадач.
        // Добавим в эпик подзадачи
        int epicId = epic.getTaskId();
        Subtask subtaskObj11 = new Subtask(3, "Подзадача № 1", "Подзадача № 1", Status.NEW,
                epicId, Duration.ofMinutes(15), LocalDateTime.of(2025, 1, 1, 11, 15));
        Subtask subtaskObj12 = new Subtask(4, "Подзадача № 2", "Подзадача № 2", Status.NEW,
                epicId, Duration.ofMinutes(15), LocalDateTime.of(2025, 1, 1, 11, 30));
        Subtask subtaskObj13 = new Subtask(5, "Подзадача № 3", "Подзадача № 3", Status.NEW,
                epicId, Duration.ofMinutes(15), LocalDateTime.of(2025, 1, 1, 11, 45));

        Subtask subtask1 = taskManager.addNewSubtask(subtaskObj11);
        Subtask subtask2 = taskManager.addNewSubtask(subtaskObj12);
        Subtask subtask3 = taskManager.addNewSubtask(subtaskObj13);

        //Проверим продолжительность, в сумме должно быть 45 минут
        Assertions.assertEquals(epic.getDuration().toMinutes(), 45, "Продолжительность эпика - сумма сабтасков, " +
                "т.е. в данном случае 45 минут");

        // 3. Время начала — дата старта самой ранней подзадачи, а время завершения — время окончания самой поздней из задач
        Assertions.assertEquals(epic.getStartTime(), subtask1.getStartTime(), "Время начала — дата старта " +
                "самой ранней подзадачи");
        Assertions.assertEquals(epic.getEndTime(), subtask3.getEndTime(), "время завершения — время окончания " +
                "самой поздней из задач");
    }

}