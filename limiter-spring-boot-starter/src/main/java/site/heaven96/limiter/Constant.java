package site.heaven96.limiter;

import cn.hutool.cache.impl.TimedCache;

public class Constant {

    /**
     * 调用日志缓存
     */
    private static TimedCache<String, MethodCalled> callLogCache = new TimedCache<>(3600 * 1000);






}
