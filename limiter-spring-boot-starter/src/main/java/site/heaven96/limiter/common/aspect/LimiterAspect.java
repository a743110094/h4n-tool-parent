package site.heaven96.limiter.common.aspect;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import site.heaven96.limiter.annotation.Limiter;
import site.heaven96.limiter.util.BizAssertUtil;
import site.heaven96.limiter.util.KeyUtil;
import site.heaven96.limiter.util.LimiterUtil;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;
import java.security.NoSuchAlgorithmException;


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
     * 格式
     */
    private static final String MD5_FORMAT = "%s_%s_%s";
    /**
     * site.heaven96.limiter.annotation
     */
    private static Limiter lr;
    /**
     * 请求ID
     */
    private static String reqId;

    @Autowired
    private HttpServletRequest request;


    /**
     * 方法调用速度限幅器
     */
    @Pointcut("@annotation(site.heaven96.limiter.annotation.Limiter)")
    public void methodCallRateLimiter() {
    }

    @Around("methodCallRateLimiter()")
    public void around(ProceedingJoinPoint point) {
        MethodSignature signature = (MethodSignature) point.getSignature();
        Method method = signature.getMethod();
        lr = method.getAnnotation(Limiter.class);
        //get md5
        String md5 = KeyUtil.generate(signature, request, lr);
        //make sure caller is under limit
        boolean overLimit = LimiterUtil.putAndCheckIfOverLimit(md5, lr.ttl(), lr.timeUnit(), lr.callLimit());
        BizAssertUtil.isFalse(overLimit, lr.message());
    }

    @After("methodCallRateLimiter()")
    public void after() {

    }

}
