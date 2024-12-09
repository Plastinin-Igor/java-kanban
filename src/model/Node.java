package model;

/**
 * Узел списка просмотренных задач
 *
 */

public class Node <T>{
    private TaskItem data;
    private Node<T> next;
    private Node<T> prev;

    public Node(TaskItem data, Node<T> prev, Node<T> next) {
        this.data = data;
        this.next = next;
        this.prev = prev;
    }

    public TaskItem getData() {
        return data;
    }

    public void setData(TaskItem data) {
        this.data = data;
    }

    public Node<T> getNext() {
        return next;
    }

    public void setNext(Node<T> next) {
        this.next = next;
    }

    public Node<T> getPrev() {
        return prev;
    }

    public void setPrev(Node<T> prev) {
        this.prev = prev;
    }
}
