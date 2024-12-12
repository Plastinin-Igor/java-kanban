package model;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class SubtaskTest {

    @Test
    void getEpicId() {
        Subtask subtask = new Subtask(0, "Наименование", "Описание", Status.NEW, 1);
        int epicId = subtask.getEpicId();
        Assertions.assertEquals(epicId, 1, "Неверное значение epicId");
    }

    @Test
    void testToString() {
        Subtask subtask = new Subtask(0, "Наименование", "Описание", Status.NEW, 1);
        String subtaskSaved = "Подзадача{Идентификатор=0 Идентификатор эпик-задачи=1, Наименование='Наименование', " +
                "Описание='Описание', Статус='NEW'}";
        Assertions.assertEquals(subtask.toString(), subtaskSaved, "Значения не равны");
    }
}