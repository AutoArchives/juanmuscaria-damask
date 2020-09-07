package pw.prok.damask.cache;

public interface ICache<T> {
    T get(String key);

    T put(String key, T value);
}
