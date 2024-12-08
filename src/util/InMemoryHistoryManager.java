package util;

import model.HistoryManager;
import model.Node;
import model.TaskItem;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;

/**
 * Управление историей просмотров
 * add() - Добавление просмотренной задачи в историю
 * getHistory() - Список просмотренных задач
 */

public class InMemoryHistoryManager implements HistoryManager {

    private ArrayList<TaskItem> historyList;
    private HashMap<Integer, Node<TaskItem>> historyMap;

    //Указатели на первый и последний элемент списка
    private Node<TaskItem> head;
    private Node<TaskItem> tail;




    public InMemoryHistoryManager() {
        this.historyList = new ArrayList<>();
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

    }

    /**
     * Список просмотренных задач
     *
     * @return Collection TaskItem
     */
    @Override
    public Collection<TaskItem> getHistory() {
        return new ArrayList<>(historyList);
    }



    /**
     * Добавление задачи в конец списка
     *
     */
    public void linkLast(TaskItem task){
        final Node<TaskItem> oldTail = tail;
        final Node<TaskItem> newNode = new Node<>(task, tail, null);
        tail = newNode;
        historyMap.put(task.getTaskId(), newNode);

        if (oldTail == null) {
            tail = newNode;
        } else {
            oldTail.prev = newNode;
        }
    }

    /**
     * Удаление узла связанного списка
     *
     */
    public void removeNode(Node node){

    }

    /**
     *
     *
     */
    public List<TaskItem> getTasks() {
        return null;
    }

}
