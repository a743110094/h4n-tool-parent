package filter.mapper;

import filter.exception.DuplicateRequestException;
import filter.util.cache.TimedCacheBase;

import java.util.concurrent.TimeUnit;

/**
 * 本地请求ID缓存
 *
 * @author heaven96
 * @date 2022/04/21
 */
public abstract class BaseMapper implements RequestIdMapper {

    /**
     * 定时缓存 最长 1h
     */
    private static TimedCacheBase<String, String> reqIdCache = new TimedCacheBase<>(MAX_TTL);

    /**
     * 冲压锁
     * @deprecated 直接下沉加锁到TimeCache操作类
     */
    // private static StampedLock stampedLock = new StampedLock();

    static {
        reqIdCache.schedulePrune(1000);
    }

    /**
     * 放
     *
     * @param requestId 请求ID
     * @param value     价值
     * @param timeout   超时
     * @param timeUnit  时间单位
     */

    public static void put(String requestId, String value, long timeout, TimeUnit timeUnit) {
        long time = TimeUnit.MILLISECONDS.convert(timeout, timeUnit);
        //with stampedLock...
        reqIdCache.put(requestId, value, time);
    }

    /**
     * 初始化P状态
     *
     * @param requestId 请求ID
     * @param timeout   超时
     * @param timeUnit  时间单位
     */
    public static void putP(String requestId, long timeout, TimeUnit timeUnit) {
        put(requestId, P, timeout, timeUnit);
    }

    /**
     * 更新
     *
     * @param requestId 请求ID
     * @param newVal    新值
     * @param timeout   超时
     * @param timeUnit  时间单位
     */
    public static void update(String requestId, String newVal, long timeout, TimeUnit timeUnit) {
        long l = 0L;
        if (reqIdCache.containsKey(requestId)) {
            try {
                reqIdCache.remove(requestId);
                put(requestId, newVal, timeout, timeUnit);
            } finally {
            }
        } else {
            put(requestId, newVal, timeout, timeUnit);
        }
    }

    /**
     * 更新f
     *
     * @param requestId 请求ID
     * @param timeout   超时
     * @param timeUnit  时间单位
     */
    public static void updateF(String requestId, long timeout, TimeUnit timeUnit) {
        update(requestId, F, timeout, timeUnit);
    }

    /**
     * 检查
     * 存在则延期到初始时间并报错
     *
     * @param requestId 请求ID
     */
    public static void check(String requestId) {
        String s = reqIdCache.get(requestId);
        if (null == s) {
            return;
        }
        if (P.equals(s)) {
            throw new DuplicateRequestException(请求重复_该请求正在处理);
        }
        if (F.equals(s)) {
            throw new DuplicateRequestException(请求重复_该请求已处理完毕);
        }
    }

    /**
     * 检查是否缺席
     *
     * @param requestId 请求ID
     */
    public static void checkAndPutP(String requestId, long timeout, TimeUnit timeUnit) {
        String s = reqIdCache.get(requestId);
        if (null == s) {
            putP(requestId, timeout, timeUnit);
            return;
        }
        if (P.equals(s)) {
            throw new DuplicateRequestException(请求重复_该请求正在处理);
        }
        if (F.equals(s)) {
            throw new DuplicateRequestException(请求重复_该请求已处理完毕);
        }
    }

    public static Object get(String key) {
        return reqIdCache.get(key);
    }

}
