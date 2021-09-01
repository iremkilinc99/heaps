public interface PriorityQueue<K,V> {

  int size();

  boolean isEmpty();

  Position<K,V> insert(K key, V value) throws IllegalArgumentException;

  Position<K,V> min();

  Position<K,V> removeMin();
}
