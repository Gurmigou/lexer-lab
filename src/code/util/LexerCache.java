package code.util;

public class LexerCache {
    private String cache;

    public void add(String value) {
        if (cache == null || cache.isEmpty())
            this.cache = value;
        else
            this.cache = cache + value;
    }

    public void clear() {
        this.cache = null;
    }

    public String getCache() {
        return this.cache;
    }

    public boolean isNonEmpty() {
        return cache != null && !cache.isEmpty();
    }
}
