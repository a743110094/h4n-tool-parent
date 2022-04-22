package common;

import com.google.common.util.concurrent.RateLimiter;

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
     * 写的时候每个key一个限速器，降低锁的粒度
     */
    protected final Map<K, Lock> keyLockMap = new ConcurrentHashMap<>();
    /**
     * 乐观锁，此处使用乐观锁解决读多写少的场景
     * get时乐观读，再检查是否修改，修改则转入悲观读重新读一遍，可以有效解决在写时阻塞大量读操作的情况。（锁升级）
     */
    protected final StampedLock lock = new StampedLock();

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
     * 是否满
     *
     * @return boolean
     */
    public boolean isFull() {
        return (capacity > 0) && (keyLockMap.size() >= capacity);
    }

    public boolean key() {
        return (capacity > 0) && (keyLockMap.size() >= capacity);
    }


}