 package site.heaven96.limiter;

import javax.servlet.http.HttpServletRequest;

/**
 * 头用户md5 stg
 *
 * @author lrx
 * @date 2022/04/23
 */
public class RequestParameterUserMd5Stg implements UserMd5CalculateStrategy{
    @Override
    public String calculate(HttpServletRequest request) {
        throw new RuntimeException("暂未实现通过Session对用户身份判定的逻辑");
    }
}
