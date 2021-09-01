public interface Position <K, V>{
    K getKey() throws IllegalStateException;
    V getValue() throws IllegalStateException;
}
