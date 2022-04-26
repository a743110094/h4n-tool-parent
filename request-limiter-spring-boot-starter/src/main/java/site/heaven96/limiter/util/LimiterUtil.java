package site.heaven96.limiter.util;

import cn.hutool.cache.impl.TimedCache;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 限速器工具
 *
 * @author lrx
 * @date 2022/04/23
 */
public class LimiterUtil {
    public static final int TIMEOUT = 3600 * 1000;

    /**
     * 调用日志缓存
     */
    private static TimedCache<String, AtomicInteger> cacheMap = new TimedCache<>(TIMEOUT);

    /**
     * 放入并检查
     *
     * @param md5 md5
     */
    public static boolean putAndCheckIfOverLimit(String md5, long ttl, TimeUnit unit, long max) {
        if (cacheMap.containsKey(md5)) {
            //if over limit
            return max < cacheMap.get(md5).incrementAndGet();
        }
        //put counter into the cacheMap
        cacheMap.put(md5, new AtomicInteger(1),TimeUnit.MILLISECONDS.convert(ttl,unit));
        return cacheMap.get(md5).get() > max;
    }
}
