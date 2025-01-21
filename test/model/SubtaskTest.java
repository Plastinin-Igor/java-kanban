package model;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.time.Duration;
import java.time.LocalDateTime;

class SubtaskTest {

    @Test
    void getEpicId() {
        Subtask subtask = new Subtask(0, "Наименование", "Описание", Status.NEW, 1);
        int epicId = subtask.getEpicId();
        Assertions.assertEquals(epicId, 1, "Неверное значение epicId");
    }

    @Test
    void testToString() {
        Subtask subtask = new Subtask(0, "Наименование", "Описание", Status.NEW, 1,
                Duration.ofMinutes(10), LocalDateTime.of(2025, 1, 1, 10, 0));
        String subtaskSaved = "Подзадача{Идентификатор=0 Идентификатор эпик-задачи=1, Наименование='Наименование', " +
                "Описание='Описание', Статус='NEW', Продолжительность='10', Дата и веремя начала задачи='2025-01-01T10:00', " +
                "Дата и время завершения задачи='2025-01-01T10:10'}";
        Assertions.assertEquals(subtask.toString(), subtaskSaved, "Значения не равны");
    }
}