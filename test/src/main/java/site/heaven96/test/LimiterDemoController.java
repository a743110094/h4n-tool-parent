package site.heaven96.test;

import cn.hutool.core.map.MapUtil;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import site.heaven96.limiter.annotation.Limiter;
import site.heaven96.limiter.enums.IdentifierType;

import java.util.HashMap;
import java.util.Map;

@RestController
public class LimiterDemoController {

    /**
     * 默认配置 每5s可以访问20次 全局的 不区分用户的
     *
     * @return {@link String}
     */
    @PostMapping("mine")
    @Limiter
    public String index() {
        return "asdasd";
    }

    /**
     * 每个用户 每5s 1次
     * 用户的身份识别来自于Header的Authorization
     *
     * @param sd sd
     * @return {@link Map}
     */
    @PostMapping("mine2")
    @Limiter(independence = IdentifierType.HEADER, ceiling = 1)
    public Map index(String sd) {
        HashMap<Object, Object> objectObjectHashMap = MapUtil.newHashMap();
        objectObjectHashMap.put("123", "1233");
        return objectObjectHashMap;
    }

    /**
     * 每个用户 每5s 1次
     * 用户的身份识别来自于Header的 MyToken
     *
     * @param sd sd
     * @return {@link Map}
     */
    @PostMapping("mine3")
    @Limiter(independence = IdentifierType.HEADER, ceiling = 1, key = "MyToken")
    public Map index2(String sd) {
        HashMap<Object, Object> objectObjectHashMap = MapUtil.newHashMap();
        objectObjectHashMap.put("123", "1233");
        return objectObjectHashMap;
    }

}
