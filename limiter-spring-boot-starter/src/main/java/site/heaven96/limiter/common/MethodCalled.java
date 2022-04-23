package site.heaven96.limiter.common;



import java.io.Serializable;
import java.util.Objects;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.atomic.AtomicReference;

/**
 * 接口调用频率记录专用对象
 *
 * @author Looly
 */
public class MethodCalled implements Serializable {
    private static final long serialVersionUID = 1L;
    /**
     * 默认时间单位
     */
    private static final TimeUnit DEFAULT_TIME_UNIT = TimeUnit.SECONDS;
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
     * 获取对象存活时长，即超时总时长，0表示无限
     *
     * @return 对象存活时长
     * @since 5.7.17
     */
    public long getTtl() {
        return this.ttl;
    }

    public MethodCalled(long ttl, long timesLimit) {
        this.ttl = ttl;
        this.timesLimit = timesLimit;
        this.timeUnit = DEFAULT_TIME_UNIT;
    }

    /**
     * 方法称为
     *
     * @param ttl        ttl
     * @param timesLimit 时间限制
     * @param timeUnit   时间单位
     */
    public MethodCalled(long ttl, long timesLimit, TimeUnit timeUnit) {
        this.ttl = ttl;
        this.timesLimit = timesLimit;
        this.timeUnit = timeUnit;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MethodCalled that = (MethodCalled) o;
        return ttl == that.ttl && timesLimit == that.timesLimit && timeUnit == that.timeUnit && Objects.equals(lastAccess, that.lastAccess) && Objects.equals(accessCount, that.accessCount);
    }

    @Override
    public int hashCode() {
        return Objects.hash(ttl, timesLimit, timeUnit, lastAccess, accessCount);
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
     * 是否过频
     *
     * @return boolean
     */
    protected boolean isOverFrequency() {
        //clear expired data
        if (isExpired()) {
            //out of date , do clear
            accessCount.set(0);
            return false;
        }
        refreshLastAccess();
        if (accessCount.get() > timesLimit) {
            //is over frequency
            return true;
        }
        // accessCount ++
        accessCount.getAndIncrement();
        return false;
    }
}
