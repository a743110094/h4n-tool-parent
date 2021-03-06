package filter.aspect;


import filter.annotation.DuplicateRequestFilter;
import filter.enums.Scope;
import filter.mapper.BaseMapper;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import site.heaven96.core.util.HttpUtil;
import site.heaven96.core.util.MD5Utils;
import site.heaven96.core.util.MapUtil;
import site.heaven96.core.util.SerializationUtil;

import javax.servlet.http.HttpServletRequest;
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
     * 请求ID
     */
    private static String reqId;

    @Autowired
    private HttpServletRequest request;


/*    @Pointcut("@site.heaven96.log.annotation(site.heaven96.filter.site.heaven96.log.annotation.DuplicateRequestFilter)")
    public void duplicateRequestFilter() {
    }*/

    @Around("@annotation(ft)")
    public Object saveLog(ProceedingJoinPoint point, DuplicateRequestFilter ft) throws Throwable {
        MethodSignature signature = (MethodSignature) point.getSignature();
        Method method = signature.getMethod();
        ft = method.getAnnotation(DuplicateRequestFilter.class);
        ///gene id
        reqId = generateReqId(ft.scope());
        //检查
        BaseMapper.checkAndPutP(reqId, ft.ttl(), ft.timeUnit());
        return point.proceed();
    }

    //----------------------Private Method

    @After("@annotation(ft)")
    public void after(DuplicateRequestFilter ft) {
        BaseMapper.updateF(reqId, ft.ttl(), ft.timeUnit());
    }

    /**
     * To generate the unique request id
     *
     * @return {@code String}
     * @author heaven96
     */
    private String generateReqId(Scope scope) throws NoSuchAlgorithmException {
        Map<String, String[]> parameterMap = request.getParameterMap();
        String urlMd5 = MD5Utils.md5Hex(request.getRequestURI().getBytes(StandardCharsets.UTF_8));
        String paramsMd5 = EMPTY_STR;
        String reqBodyMd5 = EMPTY_STR;
        if (scope.equals(Scope.BOTH) || scope.equals(Scope.URL_PARAMS)) {
            paramsMd5 = MD5Utils.md5Hex(SerializationUtil.serialize(MapUtil.sort(parameterMap)));
        }
        if (scope.equals(Scope.BOTH) || scope.equals(Scope.REQUEST_BODY)) {
            byte[] bytes = HttpUtil.readBodyAsBytes(request);
            if (bytes.length > 0) {
                reqBodyMd5 = MD5Utils.md5Hex(bytes);
            }
        }
        return MD5Utils.md5Hex(String.format(MD5_FORMAT, urlMd5, paramsMd5, reqBodyMd5).getBytes(StandardCharsets.UTF_8));
    }
}
