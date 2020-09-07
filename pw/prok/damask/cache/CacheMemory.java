package pw.prok.damask.cache;

import pw.prok.damask.utils.Digest;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class CacheMemory<T> implements ICache<T> {
    private final boolean mHashedKeys;

    public CacheMemory(boolean hashedKeys) {
        mHashedKeys = hashedKeys;
    }

    private final Map<String, T> _0 = new ConcurrentHashMap<String, T>();
    private final Map<String, T> _1 = new ConcurrentHashMap<String, T>();
    private final Map<String, T> _2 = new ConcurrentHashMap<String, T>();
    private final Map<String, T> _3 = new ConcurrentHashMap<String, T>();
    private final Map<String, T> _4 = new ConcurrentHashMap<String, T>();
    private final Map<String, T> _5 = new ConcurrentHashMap<String, T>();
    private final Map<String, T> _6 = new ConcurrentHashMap<String, T>();
    private final Map<String, T> _7 = new ConcurrentHashMap<String, T>();
    private final Map<String, T> _8 = new ConcurrentHashMap<String, T>();
    private final Map<String, T> _9 = new ConcurrentHashMap<String, T>();
    private final Map<String, T> _A = new ConcurrentHashMap<String, T>();
    private final Map<String, T> _B = new ConcurrentHashMap<String, T>();
    private final Map<String, T> _C = new ConcurrentHashMap<String, T>();
    private final Map<String, T> _D = new ConcurrentHashMap<String, T>();
    private final Map<String, T> _E = new ConcurrentHashMap<String, T>();
    private final Map<String, T> _F = new ConcurrentHashMap<String, T>();

    private Map<String, T> map(char c) {
        switch (c) {
            case '0':
                return _0;
            case '1':
                return _1;
            case '2':
                return _2;
            case '3':
                return _3;
            case '4':
                return _4;
            case '5':
                return _5;
            case '6':
                return _6;
            case '7':
                return _7;
            case '8':
                return _8;
            case '9':
                return _9;
            case 'a':
            case 'A':
                return _A;
            case 'b':
            case 'B':
                return _B;
            case 'c':
            case 'C':
                return _C;
            case 'd':
            case 'D':
                return _D;
            case 'e':
            case 'E':
                return _E;
            case 'f':
            case 'F':
                return _F;
        }
        return null;
    }

    private Map<String, T> map(String key) {
        return key.length() > 0 ? map(key.charAt(0)) : null;
    }

    private String key(String key) {
        if (mHashedKeys) return key;
        return Digest.toHex(Digest.sha1(key));
    }

    @Override
    public T get(String key) {
        key = key(key);
        Map<String, T> map = map(key);
        return map != null ? map.get(key) : null;
    }

    @Override
    public T put(String key, T value) {
        key = key(key);
        Map<String, T> map = map(key);
        return map != null ? map.put(key, value) : null;
    }
}
