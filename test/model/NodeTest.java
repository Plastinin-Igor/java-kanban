package model;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class NodeTest {

    Task taskObject = new Task("0 Test name", "0 Test name", Status.NEW);
    Task taskObject1 = new Task("1 Test name", "1 Test name", Status.NEW);
    Task taskObject2 = new Task("2 Test name", "2 Test name", Status.NEW);

    @Test
    void NodeInitial() {
        Node<TaskItem> node = new Node<TaskItem>(taskObject1,
                new Node<TaskItem>(taskObject, null, null),
                new Node<TaskItem>(taskObject2, null, null));

        Assertions.assertEquals(taskObject, node.getPrev().getData(), "Hе равны объекты в Prev");
        Assertions.assertEquals(taskObject1, node.getData(), "Hе равны объекты в Data");
        Assertions.assertEquals(taskObject2, node.getNext().getData(), "Hе равны объекты в Next");

    }

}
