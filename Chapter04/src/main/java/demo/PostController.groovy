package demo

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController

@RestController
public class PostController {

    @RequestMapping(value="/post", method=RequestMethod.POST)
    def post(@RequestParam Map<String,String> allRequestParams) {
        
        if (allRequestParams.values().any { it.contains("~") } ) {
            println "Tilde rejection!"
            throw new TildeException("The details contained ~")
        }
        
        if (allRequestParams.values().any { it.contains("@") } ) {
            println "At sign rejection!"
            return [success: false, status: "rejected", message: "The details contained @"]
        }
        
//        if (allRequestParams.values().any { it.contains("\\") } ) {
//            println "Backslash - empty response"
//            return [success: false, data:null]
//        }

        allRequestParams.each { key,value->
            println "$key => $value"
        }
        
        return [success: true, status: "submitted", message: "The details where submitted"]
    }
}
