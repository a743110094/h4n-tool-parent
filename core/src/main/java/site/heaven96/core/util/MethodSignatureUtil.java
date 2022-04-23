package site.heaven96.core.util;

import org.aspectj.lang.reflect.MethodSignature;

import java.nio.charset.StandardCharsets;
import java.security.NoSuchAlgorithmException;

public class MethodSignatureUtil {
    /**
     * get方法名字
     *
     * @param signature 签名
     * @return {@link String}
     */
    public static String getMethodName(MethodSignature signature) {
        if (null == signature || null == signature.getMethod()) {
            return "";
        }
        return signature.getMethod().getName();
    }

    /**
     * get方法名字
     *
     * @param signature 签名
     * @return {@link String}
     */
    public static String getMethodNameMd5(MethodSignature signature) throws NoSuchAlgorithmException {
        if (null == signature || null == signature.getMethod()) {
            return "";
        }
        return MD5Utils.md5Hex(signature.getMethod().toString().getBytes(StandardCharsets.UTF_8));
    }
}
