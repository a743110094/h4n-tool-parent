package site.heaven96.test;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import site.heaven96.limiter.annotation.Limiter;
import site.heaven96.limiter.enums.IdentifierType;

@RestController
public class Controller {

    @PostMapping("mine")
    @Limiter
    public String index(){
        return "asdasd";
    }

    @PostMapping("mine2")
    @Limiter(byUser = IdentifierType.HEADER)
    public String index(String sd){
        return "asdasd";
    }
}
