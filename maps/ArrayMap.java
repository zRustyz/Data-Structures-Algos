package maps;

import java.util.Iterator;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Objects;

public class ArrayMap<K, V> extends AbstractIterableMap<K, V> {
    private static final int DEFAULT_INITIAL_CAPACITY = 8;
    private int size = 0;

    SimpleEntry<K, V>[] entries;

    public ArrayMap() {
        this(DEFAULT_INITIAL_CAPACITY);
    }

    public ArrayMap(int initialCapacity) {
        if (initialCapacity > 0) {
            this.entries = this.createArrayOfEntries(initialCapacity);
        } else {
            this.entries = this.createArrayOfEntries(DEFAULT_INITIAL_CAPACITY);
        }
    }


    @SuppressWarnings("unchecked")
    private SimpleEntry<K, V>[] createArrayOfEntries(int arraySize) {
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
