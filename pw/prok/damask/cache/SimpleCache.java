package pw.prok.damask.cache;

public class SimpleCache<T> {
    private final ICache<T> mCache;

    private SimpleCache(ICache<T> cache) {
        mCache = cache;
    }

    public static <T> SimpleCache<T> cache(ICache<T> cache) {
        assert cache != null;
        return new SimpleCache<T>(cache);
    }

    public static <T> SimpleCache<T> memoryCache() {
        return cache(new CacheMemory<T>(false));
    }

    public static <T> SimpleCache<T> memoryCacheHashed() {
        return cache(new CacheMemory<T>(true));
    }

    public T get(String key) {
        return mCache.get(key);
    }

    public T put(String key, T value) {
        return mCache.put(key, value);
    }
}
