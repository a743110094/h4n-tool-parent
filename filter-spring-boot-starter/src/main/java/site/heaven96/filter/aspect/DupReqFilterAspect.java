package site.heaven96.filter.aspect;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import site.heaven96.filter.annotation.DuplicateRequestFilter;
import site.heaven96.filter.enums.Scope;
import site.heaven96.filter.mapper.BaseMapper;
import site.heaven96.filter.util.MD5Utils;
import site.heaven96.filter.util.MapUtil;
import site.heaven96.filter.util.SerializationUtil;

import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.lang.reflect.Method;
import java.nio.charset.StandardCharsets;
import java.security.NoSuchAlgorithmException;
import java.util.Map;

/**
 * 重复请求过滤器特征
 *
 * @author heaven96
 * @date 2022/04/22
 */
@Component
@Aspect
@Order(1)
public class DupReqFilterAspect {
    /**
     * 空串
     */
    public static final String EMPTY_STR = "";
    /**
     * 格式
     */
    private static final String MD5_FORMAT = "%s_%s_%s";
    /**
     * annotation
     */
    private static DuplicateRequestFilter ft;
    /**
     * 请求ID
     */
    private static String reqId;

    @Autowired
    private HttpServletRequest request;

    /**
     * read request body as byte array
     *
     * @param request 请求
     * @return {@code byte[]}
     */
    private static byte[] readAsBytes(HttpServletRequest request) {
        int len = request.getContentLength();
        byte[] buffer = new byte[len];
        ServletInputStream in = null;

        try {
            in = request.getInputStream();
            in.read(buffer, 0, len);
            in.close();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (null != in) {
                try {
                    in.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return buffer;
    }

    @Pointcut("@annotation(site.heaven96.filter.annotation.DuplicateRequestFilter)")
    public void duplicateRequestFilter() {
    }

    @Around("duplicateRequestFilter()")
    public void saveLog(ProceedingJoinPoint point) throws Throwable {
        MethodSignature signature = (MethodSignature) point.getSignature();
        Method method = signature.getMethod();
        ft = method.getAnnotation(DuplicateRequestFilter.class);
        ///gene id
        reqId = generateReqId();
        //检查
        BaseMapper.checkAndPutP(reqId, ft.ttl(), ft.timeUnit());
        point.proceed();
    }

    //----------------------Private Method

    @After("duplicateRequestFilter()")
    public void after() {
        BaseMapper.updateF(reqId, ft.ttl(), ft.timeUnit());
    }

    /**
     * To generate the unique request id
     *
     * @return {@code String}
     * @author heaven96
     */
    private String generateReqId() throws NoSuchAlgorithmException {
        Map<String, String[]> parameterMap = request.getParameterMap();
        Scope scope = ft.scope();
        String urlMd5 = MD5Utils.md5Hex(request.getRequestURI().getBytes(StandardCharsets.UTF_8));
        String paramsMd5 = EMPTY_STR;
        String reqBodyMd5 = EMPTY_STR;
        if (scope.equals(Scope.BOTH) || scope.equals(Scope.URL_PARAMS)) {
            paramsMd5 = MD5Utils.md5Hex(SerializationUtil.serialize(MapUtil.sort(parameterMap)));
        }
        if (scope.equals(Scope.BOTH) || scope.equals(Scope.REQUEST_BODY)) {
            byte[] bytes = readAsBytes(request);
            if (bytes.length > 0) {
                reqBodyMd5 = MD5Utils.md5Hex(bytes);
            }
        }
        return MD5Utils.md5Hex(String.format(MD5_FORMAT, urlMd5, paramsMd5, reqBodyMd5).getBytes(StandardCharsets.UTF_8));
    }
}