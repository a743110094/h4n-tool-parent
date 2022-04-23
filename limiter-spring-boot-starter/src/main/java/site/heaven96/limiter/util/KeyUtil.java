package site.heaven96.limiter.util;

import cn.hutool.core.lang.func.Func;
import org.aspectj.lang.reflect.MethodSignature;

import java.nio.charset.StandardCharsets;
import java.security.NoSuchAlgorithmException;

/**
 * key generate tools
 *
 * @author lenovo
 * @date 2022/04/23
 */
public class KeyUtil {

    public static final String EMPTY_STR = "";

    /**
     * 生成 没有用户标识
     *
     * @param signature 签名
     * @return {@link String}
     */
    public static String generateWithNoUserIdentifier(MethodSignature signature) {
        try {
            return MD5Utils.md5Hex(getMethodName(signature).getBytes(StandardCharsets.UTF_8));
        } catch (NoSuchAlgorithmException e) {
            return EMPTY_STR;
        }
    }

    /**
     * 生成 有用户标识
     *
     * @return {@link String}
     */
    public static String generate(MethodSignature signature, Func<Object, String> func) {
        String userIdentifier = EMPTY_STR;
        if (null != func) {
            userIdentifier = func.callWithRuntimeException();
        }
        try {
            return MD5Utils.md5Hex(String.format("%s_%s", getMethodName(signature), userIdentifier).getBytes(StandardCharsets.UTF_8));
        } catch (NoSuchAlgorithmException e) {
            return EMPTY_STR;
        }
    }

    /**
     * get方法名字
     *
     * @param signature 签名
     * @return {@link String}
     */
    private static String getMethodName(MethodSignature signature) {
        if (null == signature || null == signature.getMethod()) {
            return EMPTY_STR;
        }
        return signature.getMethod().getName();
    }
}
