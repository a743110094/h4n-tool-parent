package common;


import java.io.Serializable;
import java.util.Objects;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.atomic.AtomicReference;

/**
 * 缓存对象
 *
 * @param <K> Key类型
 * @param <V> Value类型
 * @author Looly
 */
public class CacheObj<K, V> implements Serializable {
    private static final long serialVersionUID = 1L;

    protected final K key;
    protected final V obj;
    /**
     * 对象存活时长，0表示永久存活 无单位
     */
    protected final long ttl;

    /**
     * 每个单位时间访问上限次数
     */
    protected final long timesLimit;

    /**
     * 时间单位
     */
    protected final TimeUnit timeUnit;

    /**
     * 最后一次访问时间
     */
    protected AtomicReference<Long> lastAccess = new AtomicReference<>(System.currentTimeMillis());


    /**
     * 访问次数
     */
    protected AtomicLong accessCount = new AtomicLong();

    /**
     * 构造
     *
     * @param key        键
     * @param obj        值
     * @param ttl        超时时长
     * @param timesLimit 每个单位时间访问上限次数
     * @param timeUnit
     * @param lastAccess
     */
    protected CacheObj(K key, V obj, long ttl, long timesLimit, TimeUnit timeUnit, long lastAccess) {
        this.key = key;
        this.obj = obj;
        this.ttl = TimeUnit.MILLISECONDS.convert(ttl,timeUnit);
        this.timesLimit = timesLimit;
        this.timeUnit = timeUnit;
        this.lastAccess.set(lastAccess);
        this.accessCount.set(1);
    }

    /**
     * 获取键
     *
     * @return 键
     * @since 4.0.10
     */
    public K getKey() {
        return this.key;
    }

    /**
     * 获取值
     *
     * @return 值
     * @since 4.0.10
     */
    public V getValue() {
        return this.obj;
    }

    /**
     * 获取对象存活时长，即超时总时长，0表示无限
     *
     * @return 对象存活时长
     * @since 5.7.17
     */
    public long getTtl() {
        return this.ttl;
    }


    @Override
    public String toString() {
        return "CacheObj{" +
                "key=" + key +
                ", obj=" + obj +
                ", ttl=" + ttl +
                ", timesLimit=" + timesLimit +
                ", timeUnit=" + timeUnit +
                ", accessCount=" + accessCount +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CacheObj<?, ?> cacheObj = (CacheObj<?, ?>) o;
        return ttl == cacheObj.ttl && timesLimit == cacheObj.timesLimit && lastAccess == cacheObj.lastAccess && Objects.equals(key, cacheObj.key) && Objects.equals(obj, cacheObj.obj) && timeUnit == cacheObj.timeUnit && Objects.equals(accessCount, cacheObj.accessCount);
    }

    @Override
    public int hashCode() {
        return Objects.hash(key, obj, ttl, timesLimit, timeUnit, lastAccess, accessCount);
    }

    /**
     * 判断是否过期
     *
     * @return 是否过期
     */
    protected boolean isExpired() {
        if (this.ttl > 0) {
            // 此处不考虑时间回拨
            return (System.currentTimeMillis() - lastAccess.get()) > this.ttl;
        }
        return false;
    }

    /**
     * 刷新末次访问时间
     */
    private void refreshLastAccess() {
        lastAccess.set(System.currentTimeMillis());
    }

    /**
     * 获取值
     *
     * @param isUpdateLastAccess 是否更新最后访问时间
     * @return 获得对象
     * @since 4.0.10
     */
    protected V get() {
        return get(true);
    }

    /**
     * 获取值
     *
     * @param isUpdateLastAccess 是否更新最后访问时间
     * @return 获得对象
     * @since 4.0.10
     */
    protected V get(boolean isUpdateLastAccess) {
        //refresh last access time
        if (isUpdateLastAccess) {
            refreshLastAccess();
        }
        accessCount.getAndIncrement();
        return this.obj;
    }

    /**
     * 是否过频
     * @return boolean
     */
    protected boolean isOverFrequency() {
        //clear expired data
        if (isExpired()){
            //out of date , do clear
            accessCount.set(0);
            return false;
        }
        refreshLastAccess();
        if (accessCount.get() > timesLimit){
            //is over frequency
            return true;
        }
        // accessCount ++
        accessCount.getAndIncrement();
        return false;
    }
}
