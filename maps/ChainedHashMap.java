package maps;

import java.util.Collections;
import java.util.Iterator;
import java.util.Map;
import java.util.NoSuchElementException;

public class ChainedHashMap<K, V> extends AbstractIterableMap<K, V> {
    private static final double DEFAULT_RESIZING_LOAD_FACTOR_THRESHOLD = .75;
    private static final int DEFAULT_INITIAL_CHAIN_COUNT = 5;
    private static final int DEFAULT_INITIAL_CHAIN_CAPACITY = 3;


    AbstractIterableMap<K, V>[] chains;

    private double resizingLF;
    private int chainCapacity;
    private int size;


    /**
     * Constructs a new ChainedHashMap with default resizing load factor threshold,
     * default initial chain count, and default initial chain capacity.
     */
    public ChainedHashMap() {
        this(DEFAULT_RESIZING_LOAD_FACTOR_THRESHOLD, DEFAULT_INITIAL_CHAIN_COUNT, DEFAULT_INITIAL_CHAIN_CAPACITY);
    }

    /**
     * Constructs a new ChainedHashMap with the given parameters.
     *
     * @param resizingLoadFactorThreshold the load factor threshold for resizing. When the load factor
     *                                    exceeds this value, the hash table resizes. Must be > 0.
     * @param initialChainCount the initial number of chains for your hash table. Must be > 0.
     * @param chainInitialCapacity the initial capacity of each ArrayMap chain created by the map.
     *                             Must be > 0.
     */
    public ChainedHashMap(double resizingLoadFactorThreshold, int initialChainCount, int chainInitialCapacity) {
        this.resizingLF = resizingLoadFactorThreshold;
        this.chainCapacity = chainInitialCapacity;
        this.chains = createArrayOfChains(initialChainCount);

    }

    /**
     * This method will return a new, empty array of the given size that can contain
     * {@code AbstractIterableMap<K, V>} objects.
     *
     * Note that each element in the array will initially be null.
     *
     * Note: You do not need to modify this method.
     * @see ArrayMap createArrayOfEntries method for more background on why we need this method
     */
    @SuppressWarnings("unchecked")
    private AbstractIterableMap<K, V>[] createArrayOfChains(int arraySize) {
        return (AbstractIterableMap<K, V>[]) new AbstractIterableMap[arraySize];
    }

    /**
     * Returns a new chain.
     *
     * This method will be overridden by the grader so that your ChainedHashMap implementation
     * is graded using our solution ArrayMaps.
     *
     * Note: You do not need to modify this method.
     */
    protected AbstractIterableMap<K, V> createChain(int initialSize) {
        return new ArrayMap<>(initialSize);
    }

    @Override
    public V get(Object key) {
        int hashedKey = getChainIndexObj(key);
        AbstractIterableMap<K, V> chain = this.chains[hashedKey];
        if (chain != null && chain.containsKey(key)) {
            return chain.get(key);
        }
        return null;
    }

    @Override
    public V put(K key, V value) {
            int hashedKey = getChainIndexGen(key);
            AbstractIterableMap<K, V> chain = this.chains[hashedKey];
            if (chain == null) {
                this.chains[hashedKey] = createChain(chainCapacity);
            }
            if (this.chains[hashedKey].containsKey(key)) {
                return this.chains[hashedKey].put(key, value);
            }
            size++;
            this.chains[hashedKey].put(key, value);
            if (size /chains.length > resizingLF) {
                resize();
            }
        return null;
    }

    @Override
    public V remove(Object key) {
        int hashedKey = getChainIndexObj(key);
        AbstractIterableMap<K, V> chain = this.chains[hashedKey];
        if (chain != null && chain.containsKey(key)) {
            size--;
            return chain.remove(key);
        }
        return null;
    }

    @Override
    public void clear() {
        this.chains = createArrayOfChains(chains.length);
        size = 0;
    }

    @Override
    public boolean containsKey(Object key) {
        int chainIndex = getChainIndexObj(key);
        if (chains[chainIndex] != null && chains[chainIndex].containsKey(key)) {
            return true;
        }
        return false;
    }

    @Override
    public int size() {
        return size;
    }

    @Override
    public Iterator<Map.Entry<K, V>> iterator() {
        return new ChainedHashMapIterator<>(this.chains);
    }

    private void resize() {
        AbstractIterableMap<K, V>[] chainsCopy = this.chains;
        this.chains = createArrayOfChains(2 * chains.length);
        for (int i = 0; i < chainsCopy.length; i++) {
            AbstractIterableMap<K, V> chain = chainsCopy[i];
            if (chain != null) {
                for (Entry<K, V> copy : chain) {
                    int index = getChainIndexGen(copy.getKey());
                    if (chains[index] == null) {
                        chains[index] = createChain(chainCapacity);
                    }
                    chains[index].put(copy.getKey(), copy.getValue());
                }
            }
        }
    }

    private int getChainIndexGen(K key) {
        if (key != null) {
            return Math.abs(key.hashCode() % chains.length);
        }
        return 0;
    }

    private int getChainIndexObj(Object key) {
        if (key != null) {
            return Math.abs(key.hashCode() % chains.length);
        }
        return 0;
    }


    private static class ChainedHashMapIterator<K, V> implements Iterator<Map.Entry<K, V>> {
        private AbstractIterableMap<K, V>[] chains;
        private int chainIndex = -1;
        private Iterator<Entry<K, V>> itr = Collections.emptyIterator();

        public ChainedHashMapIterator(AbstractIterableMap<K, V>[] chains) {
            this.chains = chains;
        }

        @Override
        public boolean hasNext() {
            if (itr.hasNext()) {
                return true;
            }
            while (chainIndex < chains.length - 1) {
                chainIndex++;
                if (chains[chainIndex] != null) {
                    itr = chains[chainIndex].iterator();
                    if (itr.hasNext()) {
                        return true;
                    }
                }
            }
            return false;
        }

        @Override
        public Map.Entry<K, V> next() {
            if (this.hasNext()) {
                return itr.next();
            }
            throw new NoSuchElementException();
        }

    }
}
