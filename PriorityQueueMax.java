public interface PriorityQueueMax<K, V> {
    int size();

    boolean isEmpty();

    Position<K,V> insert(K key, V value) throws IllegalArgumentException;

    Position<K,V> max();

    Position<K,V> removeMax();
}
