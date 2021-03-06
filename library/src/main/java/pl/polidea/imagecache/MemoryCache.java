package pl.polidea.imagecache;

import android.graphics.Bitmap;
import java.util.Map;
import pl.polidea.utils.Utils;

/**
 * @author Przemysław Jakubczyk <przemyslaw.jakubczyk@pl.polidea.pl>
 */
public class MemoryCache {

    BitmapLRUCache cache;

    public MemoryCache(final int size) {
        cache = new BitmapLRUCache(size);
    }

    public int createCount() {
        return cache.createCount();
    }

    public void evictAll() {
        cache.evictAll();
    }

    public int evictionCount() {
        return cache.evictionCount();
    }

    public Bitmap get(final String key) {
        return cache.get(key);
    }

    public int hitCount() {
        return cache.hitCount();
    }

    public int maxSize() {
        return cache.maxSize();
    }

    public int missCount() {
        return cache.missCount();
    }

    /**
     * Puts bitmap to cache. Both key and value can't be null. Inserting bitmap
     * bigger than MemoryCache size throw IllegalArgumentException.
     *
     * @param key
     * @param bitmap
     */
    public Bitmap put(final String key, final Bitmap bitmap) {
        if (key == null || bitmap == null) {
            throw new IllegalArgumentException("key == null || value == null");
        }
        final int size = bitmap.getRowBytes() * bitmap.getHeight();
        if (size > maxSize()) {
            throw new IllegalArgumentException("Tried to put bitmap of size: " + size / 1024
                    + " KB, while maximum memory cache size is: " + maxSize() / 1024 + " KB.");
        }
        Bitmap put = cache.put(key, bitmap);
        Utils.log("Inserting " + key + " into LRU Cache Bitmap with size: " + size + "B " + " width:"
                + bitmap.getWidth() + "\theight: " + bitmap.getHeight() + " Cache size: " + size() / 1000 + " KB");

        return put;
    }

    public int putCount() {
        return cache.putCount();
    }

    public Bitmap remove(final String key) {
        return cache.remove(key);
    }

    public int size() {
        return cache.size();
    }

    public Map<String, Bitmap> snapshot() {
        return cache.snapshot();
    }

    @Override
    public final String toString() {
        return cache.toString();
    }
}
