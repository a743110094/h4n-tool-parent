package site.heaven96.limiter.common.aspect;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import site.heaven96.limiter.annotation.Limiter;
import site.heaven96.limiter.util.BizAssertUtil;
import site.heaven96.limiter.util.KeyUtil;
import site.heaven96.limiter.util.LimiterUtil;

import javax.servlet.http.HttpServletRequest;


/**
 * 重复请求过滤器特征
 *
 * @author heaven96
 * @date 2022/04/22
 */
@Component
@Aspect
@Order(0)
public class LimiterAspect {
    /**
     * site.heaven96.limiter.site.heaven96.log.annotation
     */
    // private static Limiter lr;

    @Autowired
    private HttpServletRequest request;


    /**
     * 方法调用速度限幅器
     *
     * @Around(value = "@site.heaven96.log.annotation(around)") //around 与 下面参数名around对应
     */
   /* @Pointcut("@site.heaven96.log.annotation(site.heaven96.limiter.site.heaven96.log.annotation.Limiter)")
    public void methodCallRateLimiter() {
    }*/
    @Around("@annotation(lr)")
    public Object around(ProceedingJoinPoint point, Limiter lr) throws Throwable {
        MethodSignature signature = (MethodSignature) point.getSignature();
        //get md5
        String md5 = KeyUtil.generate(signature, request, lr);
        //make sure caller is under limit
        boolean overLimit = LimiterUtil.putAndCheckIfOverLimit(md5, lr.period(), lr.unit(), lr.ceiling());
        BizAssertUtil.isFalse(overLimit, lr.message());
        return point.proceed();
    }

/*    @After("methodCallRateLimiter()")
    public void after() {

    }*/

}
