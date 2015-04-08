package demo

import java.util.Map

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController

@RestController
public class PostController {

    @RequestMapping(value="/post", method=RequestMethod.POST)
    public boolean post(
            @RequestParam Map<String,String> allRequestParams) {
        allRequestParams.each { key,value->
            println "$key => $value"
        }
        
        true
    }
}
