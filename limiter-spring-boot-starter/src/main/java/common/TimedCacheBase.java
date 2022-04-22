package common;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ScheduledFuture;

public class TimedCacheBase<K, V> extends BaseStampedCache<K, V> {
    private static final long serialVersionUID = 1L;

    /**
     * 正在执行的定时任务
     */
    private ScheduledFuture<?> pruneJobFuture;

    /**
     * 构造
     *
     * @param timeout 超时（过期）时长，单位毫秒
     */
    public TimedCacheBase(long timeout) {
        this(timeout, new HashMap<>());
    }

    /**
     * 构造
     *
     * @param timeout 过期时长
     * @param map     存储缓存对象的map
     */
    public TimedCacheBase(long timeout, Map<K, CacheObj<K, V>> map) {
        this.capacity = 0;
        this.timeout = timeout;
        this.cacheMap = map;
    }


}