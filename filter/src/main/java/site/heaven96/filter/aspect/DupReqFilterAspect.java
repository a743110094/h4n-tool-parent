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
import site.heaven96.filter.LocalReqIdCache;
import site.heaven96.filter.annotation.DuplicateRequestFilter;
import site.heaven96.filter.enums.Scope;
import site.heaven96.filter.starter.configuration.DupReqFilterAutoConfiguration;
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

@Component
@Aspect
@Order(1)
public class DupReqFilterAspect {
    public static final String EMPTY_STR = "";
    private String reqId;
    private DuplicateRequestFilter ft ;


    @Autowired
    private HttpServletRequest request;


    @Pointcut("@annotation(site.heaven96.filter.annotation.DuplicateRequestFilter)")
    public void duplicateRequestFilter() {
    }

    @Around("duplicateRequestFilter()")
    public void saveLog(ProceedingJoinPoint point) throws Throwable {
        MethodSignature signature = (MethodSignature) point.getSignature();
        Method method = signature.getMethod();
        ft = method.getAnnotation(DuplicateRequestFilter.class);
        ///gene id
        this.reqId = generateReqId();
        //检查
        LocalReqIdCache.check(reqId);
        LocalReqIdCache.putP(reqId, ft.ttl(), ft.timeUnit());
        point.proceed();
    }

    @After("duplicateRequestFilter()")
    public void after() {
        LocalReqIdCache.updateF(reqId, ft.ttl(), ft.timeUnit());
    }

    /**
     * To generate the unique request id
     *
     * @return {@code String}
     * @author lgw3488
     */
    private String generateReqId() throws NoSuchAlgorithmException {
        Map<String, String[]> parameterMap = request.getParameterMap();
        //计算策略
        Scope scope = ft.scope();
        String urlMd5 = MD5Utils.md5Hex(request.getRequestURI().getBytes(StandardCharsets.UTF_8));
        String paramsMd5 = EMPTY_STR;
        String reqBodyMd5 = EMPTY_STR;
        if (scope.equals(Scope.BOTH)||scope.equals(Scope.URL_PARAMS)){
            paramsMd5 = MD5Utils.md5Hex(SerializationUtil.serialize(MapUtil.sort(parameterMap)));
        }
        if (scope.equals(Scope.BOTH)||scope.equals(Scope.REQUEST_BODY)){
            byte[] bytes = readAsBytes(request);
            if (bytes.length>0){
                reqBodyMd5 = MD5Utils.md5Hex(bytes);
            }
        }
        return MD5Utils.md5Hex(String.format("%s_%s_%s",urlMd5,paramsMd5,reqBodyMd5).getBytes(StandardCharsets.UTF_8));
    }


    public static byte[] readAsBytes(HttpServletRequest request) {
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
}
