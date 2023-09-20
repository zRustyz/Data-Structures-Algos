package deques;

/**
 * @see Deque
 */
public class LinkedDeque<T> extends AbstractDeque<T> {
    private int size;

    Node<T> front;
    Node<T> back;

    public LinkedDeque() {
        size = 0;
        front = new Node<>(null);
        back = new Node<>(null);
        front.next = back;
        back.prev = front;
    }

    public void addFirst(T item) {
        size += 1;
        Node<T> itm = new Node<>(item, front, front.next);
        front.next = itm;
        itm.next.prev = itm;
    }

    public void addLast(T item) {
        size += 1;
        Node<T> itm = new Node<>(item, back.prev, back);
        back.prev = itm;
        itm.prev.next = itm;
    }

    public T removeFirst() {
        if (size == 0) {
            return null;
        }
        size -= 1;
        T itm = front.next.value;
        front.next = front.next.next;
        front.next.prev = front;
        return itm;
    }

    public T removeLast() {
        if (size == 0) {
            return null;
        }
        size -= 1;
        T itm = back.prev.value;
        back.prev = back.prev.prev;
        back.prev.next = back;
        return itm;
    }

    public T get(int index) {
        if ((index >= size) || (index < 0)) {
            return null;
        }
        Node<T> res = front.next;
        for (int i = 0; i < index; i++) {
            res = res.next;
        }
        return res.value;
    }

    public int size() {
        return size;
    }
}
