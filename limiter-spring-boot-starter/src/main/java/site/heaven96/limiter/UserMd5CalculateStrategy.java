package site.heaven96.limiter;

import javax.servlet.http.HttpServletRequest;

/**
 * 用户md5计算策略
 *
 * @author lrx
 * @date 2022/04/23
 */
public interface UserMd5CalculateStrategy {
    /**
     * 错误提示消息
     */
    String TOKEN_NOT_FOUND_ERR_MSG = "未能成功获取到Token";

    /**
     * 计算
     *
     * @param request 请求
     * @return {@link String}
     */
    String calculate(HttpServletRequest request);
}