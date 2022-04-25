package site.heaven96.test;

import cn.hutool.core.map.MapUtil;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
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
     * @return {@link Map}
     */
    @Logs(value = "hello", handle = "@demoLogHandler")
    @PostMapping("log1")
    public Map index2(@RequestBody Map param) throws InterruptedException {
        HashMap<Object, Object> objectObjectHashMap = MapUtil.newHashMap();
        objectObjectHashMap.put("123", "1233");
        Thread.sleep(10);
       // if (true)throw new RuntimeException("asfsa");
        return objectObjectHashMap;
    }
}
