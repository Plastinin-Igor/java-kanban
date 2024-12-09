package util;

import model.HistoryManager;
import model.Node;
import model.TaskItem;

import java.util.*;

/**
 * Управление историей просмотров
 * add() - Добавление просмотренной задачи в историю
 * getHistory() - Список просмотренных задач
 */

public class InMemoryHistoryManager implements HistoryManager {

    private final HashMap<Integer, Node<TaskItem>> historyMap;

    //Указатели на первый и последний элемент списка
    private Node<TaskItem> head;
    private Node<TaskItem> tail;
    private int size = 0;


    public InMemoryHistoryManager() {
        this.historyMap = new HashMap<>();
    }

    /**
     * Добавление просмотренной задачи в историю
     * Список для хранения просмотров не превышает 10 элементов
     *
     * @param task Task
     */
    @Override
    public void add(TaskItem task) {
        if (task != null) {
            linkLast(task);
        }
    }

    /**
     * Удаление задачи из просмотра
     *
     * @param id int
     */
    @Override
    public void remove(int id) {
        removeNode(historyMap.get(id));
        historyMap.remove(id);
    }

    /**
     * Список просмотренных задач
     *
     * @return Collection TaskItem
     */
    @Override
    public Collection<TaskItem> getHistory() {
        return new ArrayList<>(getTasks());
    }


    /**
     * Добавление задачи в конец списка
     */
    public void linkLast(TaskItem task) {
        if (historyMap.containsKey(task.getTaskId())) {
            removeNode(historyMap.get(task.getTaskId()));
            historyMap.remove(task.getTaskId());
        }
        //Аналог LinkedList linkLast
        final Node<TaskItem> oldTail = tail;
        final Node<TaskItem> newNode = new Node<>(task, tail, null);
        tail = newNode;
        historyMap.put(task.getTaskId(), newNode);

        if (oldTail == null) {
            head = newNode;
        } else {
            oldTail.setNext(newNode);
        }
        size++;
    }

    /**
     * Удаление узла связанного списка
     */
    public void removeNode(Node<TaskItem> node) {
        //Аналог LinkedList unlink
        final Node<TaskItem> next = node.getNext();
        final Node<TaskItem> prev = node.getPrev();

        if (prev == null) {
            head = next;
        } else {
            prev.setNext(next);
            node.setPrev(null);
        }

        if (next == null) {
            tail = prev;
        } else {
            next.setPrev(prev);
            node.setNext(null);
        }

        node.setData(null);
        size--;
    }

    /**
     * Собирает все просмотренные задачи в List, сохраняя последовательность просмотров
     *
     * @return List
     */
    public List<TaskItem> getTasks() {
        List<TaskItem> linkedList = new LinkedList<>();
        //Цикл по связанному списку от Head до Tail
        Node<TaskItem> node = head;
        while (node != null) {
            linkedList.add(node.getData());
            node = node.getNext();
        }
        return linkedList;
    }

    /**
     * Размер связанного списка
     *
     * @return int
     */
    public int sizeHistory() {
        return size;
    }

}
