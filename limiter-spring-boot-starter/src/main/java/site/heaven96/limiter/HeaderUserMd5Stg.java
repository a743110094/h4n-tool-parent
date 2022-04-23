package site.heaven96.limiter;

import org.springframework.util.Assert;
import org.springframework.util.StringUtils;

import javax.servlet.http.HttpServletRequest;
import java.util.function.Function;

/**
 * 头用户md5 stg
 *
 * @author lrx
 * @date 2022/04/23
 */
public class HeaderUserMd5Stg implements UserMd5CalculateStrategy {

    /**
     * Default Key Name Of User Token
     */
    private static String DEFAULT_TOKEN_KEY = "Authorization";

    /**
     * Key Name Of User Token
     */
    private String keyName;

    private Function function;

    /**
     * Constructor
     *
     * @param function 函数
     */
    public HeaderUserMd5Stg(Function function) {
        this.keyName = DEFAULT_TOKEN_KEY;
        this.function = function;
    }

    /**
     * Constructor
     *
     * @param keyName  键名
     * @param function 函数
     */
    public HeaderUserMd5Stg(String keyName, Function function) {
        this.keyName = StringUtils.isEmpty(keyName) ? DEFAULT_TOKEN_KEY : keyName;
        this.function = function;
    }

    /**
     * Constructor
     *
     * @param keyName 键名
     */
    public HeaderUserMd5Stg(String keyName) {
        this.function = null;
        this.keyName = StringUtils.isEmpty(keyName) ? DEFAULT_TOKEN_KEY : keyName;
    }

    /**
     * Constructor
     */
    public HeaderUserMd5Stg() {
        this.function = null;
        this.keyName = DEFAULT_TOKEN_KEY;
    }

    /**
     * 计算
     *
     * @param request 请求
     * @return {@link String}
     */
    @Override
    public String calculate(HttpServletRequest request) {
        //TODO Functional
        String token = request.getHeader(this.keyName);
        Assert.isTrue(null != request && StringUtils.hasText(token), TOKEN_NOT_FOUND_ERR_MSG);
        return token;
    }
}
