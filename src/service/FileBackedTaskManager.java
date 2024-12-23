package service;

import exceptions.ManagerSaveException;
import model.*;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Collection;

/**
 * Класс менеджера, который после каждой операции автоматически сохраняет все задачи и их состояние в специальный файл
 */

public class FileBackedTaskManager extends InMemoryTaskManager {

    private final String path;

    public FileBackedTaskManager(String path) {
        this.path = path;
        // Восстановление данных менеджера из файла
        loadFromFile();
    }

    @Override
    public Collection<Task> getListTask() {
        return super.getListTask();
    }

    @Override
    public Collection<Epic> getListEpic() {
        return super.getListEpic();
    }

    @Override
    public Collection<Subtask> getListSubtask() {
        return super.getListSubtask();
    }

    @Override
    public void deleteTask() {
        super.deleteTask();
        save();
    }

    @Override
    public void deleteEpic() {
        super.deleteEpic();
        save();
    }

    @Override
    public void deleteSubtask() {
        super.deleteSubtask();
        save();
    }

    @Override
    public Task getTaskById(int taskId) {
        return super.getTaskById(taskId);
    }

    @Override
    public Epic getEpicById(int taskId) {
        return super.getEpicById(taskId);
    }

    @Override
    public Subtask getSubtaskById(int taskId) {
        return super.getSubtaskById(taskId);
    }

    @Override
    public Task addNewTask(Task task) {
        super.addNewTask(task);
        save();
        return task;
    }

    @Override
    public Epic addNewEpic(Epic epic) {
        super.addNewEpic(epic);
        save();
        return epic;
    }

    @Override
    public Subtask addNewSubtask(Subtask subtask) {
        super.addNewSubtask(subtask);
        save();
        return subtask;
    }

    @Override
    public Task updateTask(Task task) {
        super.updateTask(task);
        save();
        return task;
    }

    @Override
    public Epic updateEpic(Epic epic) {
        super.updateEpic(epic);
        save();
        return epic;
    }

    @Override
    public Subtask updateSubtask(Subtask subtask) {
        super.updateSubtask(subtask);
        save();
        return subtask;
    }

    @Override
    public void deleteTaskById(int taskId) {
        super.deleteTaskById(taskId);
        save();
    }

    @Override
    public void deleteEpicById(int taskId) {
        super.deleteEpicById(taskId);
        save();
    }

    @Override
    public void deleteSubtaskById(int taskId) {
        super.deleteSubtaskById(taskId);
        save();
    }

    @Override
    public ArrayList<Subtask> getSubtaskByEpicId(int epicId) {
        return super.getSubtaskByEpicId(epicId);
    }

    @Override
    public Collection<TaskItem> getHistory() {
        return super.getHistory();
    }

    /**
     * Сохранение текущего состояние менеджера в указанный файл
     */
    private void save() {
        try (FileWriter fileWriter = new FileWriter(path, StandardCharsets.UTF_8)) {
            //Запись в файл заголовка
            fileWriter.write("id,type,name,status,description,epic\n");
            //Запись данных
            for (Task task : super.getListTask()) {
                fileWriter.write(task.toStringForFileCSV());
            }
            for (Epic epic : super.getListEpic()) {
                fileWriter.write(epic.toStringForFileCSV());
            }
            for (Subtask subtask : super.getListSubtask()) {
                fileWriter.write(subtask.toStringForFileCSV());
            }
        } catch (IOException e) {
            throw new ManagerSaveException("Ошибка записи данных в файл: " + e.getMessage());
        }
    }

    /**
     * Восстанавление данных из файла
     */
    private void loadFromFile() {
        File file = new File(path);
        if (file.exists()) {
            try (BufferedReader br = new BufferedReader(new FileReader(file, StandardCharsets.UTF_8))) {
                String line;
                int rowIndex = 0;
                while ((line = br.readLine()) != null) {
                    if (rowIndex != 0) {
                        TaskItem taskItem = fromString(line);
                        if (taskItem instanceof Task) {
                            super.addNewTask((Task) taskItem);
                        } else if (taskItem instanceof Epic) {
                            super.addNewEpic((Epic) taskItem);
                        } else if (taskItem instanceof Subtask) {
                            super.addNewSubtask((Subtask) taskItem);
                        }
                    }
                    rowIndex++;
                }
            } catch (IOException e) {
                throw new ManagerSaveException("Ошибка чтения файла: " + e.getMessage());
            }
        }
    }

    /**
     * Создание объекта (Task/Epic/Subtask) из строки
     *
     * @param value String
     * @return TaskItem
     */
    private TaskItem fromString(String value) {
        String[] line = value.split(",");
        int id;
        String name;
        String description;
        Status status;
        int epicId;

        switch (line[1]) {
            case "TASK" -> {
                id = Integer.parseInt(line[0]);
                name = line[2];
                status = Status.valueOf(line[3]);
                description = line[4];
                return new Task(id, name, description, status);
            }
            case "EPIC" -> {
                id = Integer.parseInt(line[0]);
                name = line[2];
                status = Status.valueOf(line[3]);
                description = line[4];
                return new Epic(id, name, description, status);
            }
            case "SUBTASK" -> {
                id = Integer.parseInt(line[0]);
                name = line[2];
                status = Status.valueOf(line[3]);
                description = line[4];
                epicId = Integer.parseInt(line[5]);
                return new Subtask(id, name, description, status, epicId);
            }
            default -> {
                return null;
            }
        }
    }
}
