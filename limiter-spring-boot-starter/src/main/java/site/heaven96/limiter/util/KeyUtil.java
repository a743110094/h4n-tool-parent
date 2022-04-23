package site.heaven96.limiter.util;

import cn.hutool.core.lang.func.Func;
import org.aspectj.lang.reflect.MethodSignature;
import site.heaven96.core.util.MD5Utils;
import site.heaven96.core.util.MethodSignatureUtil;
import site.heaven96.limiter.HeaderUserMd5Stg;
import site.heaven96.limiter.RequestParameterUserMd5Stg;
import site.heaven96.limiter.SessionUserMd5Stg;
import site.heaven96.limiter.UserMd5CalculateStrategy;
import site.heaven96.limiter.annotation.Limiter;
import site.heaven96.limiter.enums.IdentifierType;

import javax.servlet.http.HttpServletRequest;
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
    public static final String FORMAT_STR = "%s_%s";

    /**
     * 生成 没有用户标识
     *
     * @param signature 签名
     * @return {@link String}
     */
    public static String generate(MethodSignature signature) {
        try {
            return MD5Utils.md5Hex(MethodSignatureUtil.getMethodName(signature).getBytes(StandardCharsets.UTF_8));
        } catch (NoSuchAlgorithmException e) {
            return EMPTY_STR;
        }
    }


    public static String generate(MethodSignature signature, HttpServletRequest request, Limiter lr) {
        try {
            //用户身份
            assert null != signature;
            String methodNameMd5 = MethodSignatureUtil.getMethodNameMd5(signature);
            //判断是否需要依据用户限流 若需要就拿到user token
            String userMd5 = lr.byUser().equals(IdentifierType.NONE) ? "" : getUserMd5(lr, request);
            //拼接key
            String combineMd5 = String.format(FORMAT_STR, methodNameMd5, userMd5);
            //生成最终的key  方法的md5_用户token的md5
            return MD5Utils.md5Hex(combineMd5.getBytes(StandardCharsets.UTF_8));
        } catch (NoSuchAlgorithmException e) {
            return EMPTY_STR;
        }
    }

    /**
     * 生成 有用户标识
     *
     * @return {@link String}
     */
    @Deprecated
    public static String generate(MethodSignature signature, Func<Object, String> func) {
        String userIdentifier = EMPTY_STR;
        if (null != func) {
            userIdentifier = func.callWithRuntimeException();
        }
        try {
            return MD5Utils.md5Hex(String.format("%s_%s", MethodSignatureUtil.getMethodName(signature), userIdentifier).getBytes(StandardCharsets.UTF_8));
        } catch (NoSuchAlgorithmException e) {
            return EMPTY_STR;
        }
    }

    private static String getUserMd5(Limiter lr, HttpServletRequest request) throws NoSuchAlgorithmException {
        IdentifierType identifierType = lr.byUser();
        UserMd5CalculateStrategy ms = null;
        switch (identifierType) {
            case HEADER: {
                ms = new HeaderUserMd5Stg(lr.headerKey(), null);
                break;
            }
            case PARAMETER: {
                ms = new RequestParameterUserMd5Stg();
                break;
            }
            case SESSION: {
                ms = new SessionUserMd5Stg();
                break;
            }
        }
        if (null == ms) {
            return EMPTY_STR;
        }
        String userMd5 = ms.calculate(request);
        return MD5Utils.md5Hex(userMd5.getBytes(StandardCharsets.UTF_8));
    }

}
