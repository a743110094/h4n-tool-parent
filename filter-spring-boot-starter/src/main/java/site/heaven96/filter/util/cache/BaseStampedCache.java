package site.heaven96.filter.util.cache;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.StampedLock;

/**
 * 带戳的高速缓存
 *
 * @author heaven96
 * @date 2022/04/22
 */
public abstract class BaseStampedCache<K, V> {

    private static final long serialVersionUID = 1L;

    /**
     * 写的时候每个key一把锁，降低锁的粒度
     */
    protected final Map<K, Lock> keyLockMap = new ConcurrentHashMap<>();
    /**
     * 乐观锁，此处使用乐观锁解决读多写少的场景
     * get时乐观读，再检查是否修改，修改则转入悲观读重新读一遍，可以有效解决在写时阻塞大量读操作的情况。（锁升级）
     */
    protected final StampedLock lock = new StampedLock();
    protected Map<K, CacheObj<K, V>> cacheMap;
    /**
     * 返回缓存容量，{@code 0}表示无大小限制
     */
    protected int capacity;
    /**
     * 缓存失效时长， {@code 0} 表示无限制，单位毫秒
     */
    protected long timeout;
    /**
     * 每个对象是否有单独的失效时长，用于决定清理过期对象是否有必要。
     */
    protected boolean existCustomTimeout;

    /**
     * 加入元素，无锁
     *
     * @param key     键
     * @param object  值
     * @param timeout 超时时长
     * @since 4.5.16
     */
    protected void putWithoutLock(K key, V object, long timeout) {
        CacheObj<K, V> co = new CacheObj<>(key, object, timeout);
        if (timeout != 0) {
            existCustomTimeout = true;
        }
        if (isFull()) {
            pruneCache();
        }
        cacheMap.put(key, co);
    }

    public boolean isFull() {
        return (capacity > 0) && (cacheMap.size() >= capacity);
    }

    // ---------------------------------------------------------------- prune start

    /**
     * 清理实现<br>
     * 子类实现此方法时无需加锁
     *
     * @return 清理数
     */
    protected abstract int pruneCache();
    // ---------------------------------------------------------------- prune end


    /**
     * 放
     *
     * @param key     钥匙
     * @param object  对象
     * @param timeout 超时
     */
    public void put(K key, V object, long timeout) {
        final long stamp = lock.writeLock();
        try {
            putWithoutLock(key, object, timeout);
        } finally {
            lock.unlockWrite(stamp);
        }
    }


    public boolean containsKey(K key) {
        final long stamp = lock.readLock();
        try {
            // 不存在或已移除
            final CacheObj<K, V> co = cacheMap.get(key);
            if (co == null) {
                return false;
            }

            if (false == co.isExpired()) {
                // 命中
                return true;
            }
        } finally {
            lock.unlockRead(stamp);
        }

        // 过期
        remove(key);
        return false;
    }


    public final int prune() {
        final long stamp = lock.writeLock();
        try {
            return pruneCache();
        } finally {
            lock.unlockWrite(stamp);
        }
    }


    public void clear() {
        final long stamp = lock.writeLock();
        try {
            cacheMap.clear();
        } finally {
            lock.unlockWrite(stamp);
        }
    }

    /**
     * 移除key对应的对象，不加锁
     *
     * @param key 键
     * @return 移除的对象，无返回null
     */
    protected CacheObj<K, V> removeWithoutLock(K key) {
        final CacheObj<K, V> co = cacheMap.remove(key);
        return co;
    }


    /**
     * 移除key对应的对象
     *
     * @param key 键
     */
    public void remove(K key) {
        final long stamp = lock.writeLock();
        CacheObj<K, V> co;
        try {
            co = removeWithoutLock(key);
        } finally {
            lock.unlockWrite(stamp);
        }
    }


    public V get(K key) {
        // 尝试读取缓存，使用乐观读锁
        long stamp = lock.tryOptimisticRead();
        CacheObj<K, V> co = cacheMap.get(key);
        if (false == lock.validate(stamp)) {
            // 有写线程修改了此对象，悲观读
            stamp = lock.readLock();
            try {
                co = cacheMap.get(key);
            } finally {
                lock.unlockRead(stamp);
            }

        }

        // 未命中
        if (null == co) {
            return null;
        } else if (false == co.isExpired()) {
            return co.get(false);
        }

        // 过期，既不算命中也不算非命中
        remove(key);
        return null;
    }
}