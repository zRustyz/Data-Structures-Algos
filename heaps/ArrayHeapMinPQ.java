package priorityqueues;

import java.util.ArrayList;
import java.util.HashMap;

import java.util.List;
import java.util.NoSuchElementException;

/**
 * @see ExtrinsicMinPQ
 */
public class ArrayHeapMinPQ<T> implements ExtrinsicMinPQ<T> {

    static final int START_INDEX = 0;
    List<PriorityNode<T>> items;

    HashMap<T, Integer> entries;

    public ArrayHeapMinPQ() {
        items = new ArrayList<>();
        entries = new HashMap<>();
    }

    /**
     * A helper method for swapping the items at two indices of the array heap.
     */
    private void swap(int a, int b) {
        PriorityNode<T> aCopy = items.get(a);
        entries.put(items.get(a).getItem(), b);
        entries.put(items.get(b).getItem(), a);
        items.set(a, items.get(b));
        items.set(b, aCopy);
    }

    private void percolateUp(int index) {
        int parentIndex = (index - 1) / 2;
        if (index > 0 && items.get(index).getPriority() < items.get(parentIndex).getPriority()) {
            swap(index, parentIndex);
            percolateUp(parentIndex);
        }
    }

    private void percolateDown(int index) {
        int leftChildIndex = 2 * index + 1;
        int rightChildIndex = 2 * index + 2;
        int smallest = index;
        if (leftChildIndex < items.size()
            && items.get(leftChildIndex).getPriority() < items.get(smallest).getPriority()) {
            smallest = leftChildIndex;
        }
        if (rightChildIndex < items.size()
            && items.get(rightChildIndex).getPriority() < items.get(smallest).getPriority()) {
            smallest = rightChildIndex;
        }
        if (smallest != index) {
            swap(smallest, index);
            percolateDown(smallest);
        }
    }

    @Override
    public void add(T item, double priority) {

        if (contains(item)) {
            throw new IllegalArgumentException();
        }
        items.add(new PriorityNode<>(item, priority));
        entries.put(item, items.size() - 1);
        percolateUp(items.size() - 1);

    }

    @Override
    public boolean contains(T item) {
        return entries.containsKey(item);
    }

    @Override
    public T peekMin() {
        if (items.size() > 0) {
            return items.get(0).getItem();
        }
        throw new NoSuchElementException();
    }

    @Override
    public T removeMin() {
        if (items.size() == 1) {
            entries.remove(items.get(0).getItem());
            return items.remove(0).getItem();
        } else if (items.size() > 1) {
            entries.remove(items.get(0).getItem());
            entries.put(items.get(items.size() - 1).getItem(), 0);
            T val = items.get(0).getItem();
            items.set(0, items.remove(items.size() - 1));
            percolateDown(0);
            return val;
        }
        throw new NoSuchElementException();
    }

    @Override
    public void changePriority(T item, double priority) {
        if (!entries.containsKey(item)) {
            throw new NoSuchElementException();
        }
        int index = entries.get(item);
        items.set(index, new PriorityNode<>(item, priority));
        percolateUp(index);
        percolateDown(index);
    }

    @Override
    public int size() {
        return items.size();
    }
}
