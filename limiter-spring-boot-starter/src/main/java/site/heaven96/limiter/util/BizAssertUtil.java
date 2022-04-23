package site.heaven96.limiter.util;

import site.heaven96.limiter.exception.OverLimitException;

/**
 * 商业维护跑龙套
 *
 * @author heaven96
 * @date 2022/04/23
 */
public class BizAssertUtil {

    /**
     * 是真
     *
     * @param bool   保龄球
     * @param msg    味精
     * @param params 参数个数
     */
    public static void isFalse(Boolean bool, String msg, Object... params) {
        isTrue(!bool, msg, params);
    }

    /**
     * 是真
     *
     * @param bool   保龄球
     * @param msg    味精
     * @param params 参数个数
     */
    public static void isTrue(Boolean bool, String msg, Object... params) {
        if (bool) {
            return;
        }
        throw new OverLimitException(String.format(msg, params));
    }

}
