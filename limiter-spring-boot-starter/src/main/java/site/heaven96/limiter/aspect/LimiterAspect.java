package site.heaven96.limiter.aspect;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import site.heaven96.limiter.annotation.LimitRate;
import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;


/**
 * 重复请求过滤器特征
 *
 * @author heaven96
 * @date 2022/04/22
 */
@Component
@Aspect
@Order(1)
public class LimiterAspect {
    /**
     * 空串
     */
    public static final String EMPTY_STR = "";
    /**
     * 格式
     */
    private static final String MD5_FORMAT = "%s_%s_%s";
    /**
     * site.heaven96.limiter.annotation
     */
    private static LimitRate lr;
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
    }

    //----------------------Private Method

    @After("duplicateRequestFilter()")
    public void after() {

    }

}
