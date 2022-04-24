package site.heaven96.test;

import cn.hutool.core.map.MapUtil;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import site.heaven96.log.annotation.Logs;

import java.util.HashMap;
import java.util.Map;

@RestController("log")
public class LogTestController {
    /**
     * 每个用户 每5s 1次
     * 用户的身份识别来自于Header的 MyToken
     *
     * @param sd sd
     * @return {@link Map}
     */
    @Logs(value = "hello", handle = "@demoLogHandler", type = "demo")
    @PostMapping("log1")
    public Map index2(String sd) {
        HashMap<Object, Object> objectObjectHashMap = MapUtil.newHashMap();
        objectObjectHashMap.put("123", "1233");
        return objectObjectHashMap;
    }
}
