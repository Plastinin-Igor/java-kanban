package util;

import com.google.gson.*;
import model.*;

import java.lang.reflect.Type;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;

public class TaskItemTypeAdapter implements JsonDeserializer<TaskItem> {
    @Override
    public TaskItem deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        JsonObject jsonObject = json.getAsJsonObject();

        String taskName = jsonObject.get("taskName").getAsString();
        String taskDescription = jsonObject.get("taskDescription").getAsString();
        Status taskStatus = Status.valueOf(jsonObject.get("taskStatus").getAsString());
        Duration duration = Duration.parse(jsonObject.get("duration").getAsString());
        LocalDateTime startTime = LocalDateTime.parse(jsonObject.get("startTime").getAsString(), TaskItem.DATE_TIME_FORMAT);

        if (jsonObject.has("epicId")) { // Если есть поле epicId, то это Subtask
            int epicId = jsonObject.get("epicId").getAsInt();
            return new Subtask(-1, taskName, taskDescription, taskStatus, epicId);
        } else if (jsonObject.has("subtaskListId")) { // Если есть поле subtaskListId, то это Epic
            JsonArray subtaskListIdArray = jsonObject.get("subtaskListId").getAsJsonArray();
            ArrayList<Integer> subtaskListId = new ArrayList<>();
            for (JsonElement idElement : subtaskListIdArray) {
                subtaskListId.add(idElement.getAsInt());
            }
            return new Epic(-1, taskName, taskDescription, taskStatus);
        } else { // В остальных случаях считаем, что это Task
            return new Task(-1, taskName, taskDescription, taskStatus);
        }
    }
}
