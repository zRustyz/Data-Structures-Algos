package maps;

import java.util.Iterator;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Objects;

/**
 * @see AbstractIterableMap
 * @see Map
 */
public class ArrayMap<K, V> extends AbstractIterableMap<K, V> {
    private static final int DEFAULT_INITIAL_CAPACITY = 8;
    private int size = 0;
    /*
    Warning:
    You may not rename this field or change its type.
    We will be inspecting it in our secret tests.
     */
    SimpleEntry<K, V>[] entries;

    // You may add extra fields or helper methods though!

    /**
     * Constructs a new ArrayMap with default initial capacity.
     */
    public ArrayMap() {
        this(DEFAULT_INITIAL_CAPACITY);
    }

    /**
     * Constructs a new ArrayMap with the given initial capacity (i.e., the initial
     * size of the internal array).
     *
     * @param initialCapacity the initial capacity of the ArrayMap. Must be > 0.
     */
    public ArrayMap(int initialCapacity) {
        if (initialCapacity > 0) {
            this.entries = this.createArrayOfEntries(initialCapacity);
        } else {
            this.entries = this.createArrayOfEntries(DEFAULT_INITIAL_CAPACITY);
        }
    }

    /**
     * This method will return a new, empty array of the given size that can contain
     * {@code Entry<K, V>} objects.
     *
     * Note that each element in the array will initially be null.
     *
     * Note: You do not need to modify this method.
     */
    @SuppressWarnings("unchecked")
    private SimpleEntry<K, V>[] createArrayOfEntries(int arraySize) {
        /*
        It turns out that creating arrays of generic objects in Java is complicated due to something
        known as "type erasure."

        We've given you this helper method to help simplify this part of your assignment. Use this
        helper method as appropriate when implementing the rest of this class.

        You are not required to understand how this method works, what type erasure is, or how
        arrays and generics interact.
        */
        return (SimpleEntry<K, V>[]) (new SimpleEntry[arraySize]);
    }

    @Override
    public V get(Object key) {
        for (int i = 0; i < size; i++) {
            SimpleEntry<K, V> entry = this.entries[i];
            if (Objects.equals(key, this.entries[i].getKey())) {
                return entry.getValue();
            }
        }
        return null;
    }

    @Override
    public V put(K key, V value) {
        for (int i = 0; i < size; i++) {
            SimpleEntry<K, V> entry = this.entries[i];
            if (Objects.equals(entry.getKey(), key)) {
                return entry.setValue(value);
            }
        }
        if (size == this.entries.length) {
            SimpleEntry<K, V>[] temp = this.createArrayOfEntries(this.entries.length * 2);
            for (int i = 0; i < size; i++) {
                SimpleEntry<K, V> entry = this.entries[i];
                temp[i] = new SimpleEntry<>(entry.getKey(), entry.getValue());
            }
            this.entries = temp;
        }
        this.entries[size] = new SimpleEntry<>(key, value);
        size++;
        return null;
    }

    @Override
    public V remove(Object key) {
        for (int i = 0; i < size; i++) {
            if (Objects.equals(key, this.entries[i].getKey())) {
                SimpleEntry<K, V> lastEntry = this.entries[size - 1];
                V val = this.entries[i].getValue();
                if (i != size - 1) {
                    this.entries[i] = new SimpleEntry<>(lastEntry.getKey(), lastEntry.getValue());
                }
                size--;
                this.entries[size] = null;
                return val;
            }
        }
        return null;
    }

    @Override
    public void clear() {
        for (int i = 0; i < size; i++) {
            this.entries[i] = null;
        }
        size = 0;
    }

    @Override
    public boolean containsKey(Object key) {
        for (int i = 0; i < size; i++) {
            if (Objects.equals(key, this.entries[i].getKey())) {
                return true;
            }
        }
        return false;
    }

    @Override
    public int size() {
        return size;
    }

    @Override
    public Iterator<Map.Entry<K, V>> iterator() {
        // Note: You may or may not need to change this method, depending on whether you
        // add any parameters to the ArrayMapIterator constructor.
        return new ArrayMapIterator<>(this.entries);
    }


    private static class ArrayMapIterator<K, V> implements Iterator<Map.Entry<K, V>> {
        private final SimpleEntry<K, V>[] entries;
        private int index = -1;
        public ArrayMapIterator(SimpleEntry<K, V>[] entries) {
            this.entries = entries;
        }

        @Override
        public boolean hasNext() {
            if (index + 1 >= this.entries.length || this.entries[index + 1] == null) {
                return false;
            }
            return true;
        }

        @Override
        public Map.Entry<K, V> next() {
            index++;
            if (index >= entries.length || this.entries[index] == null) {
                throw new NoSuchElementException();
            }
            return this.entries[index];
        }
    }
}
