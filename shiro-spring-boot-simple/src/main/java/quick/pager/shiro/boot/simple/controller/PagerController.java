package quick.pager.shiro.boot.simple.controller;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.subject.Subject;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/admin")
public class PagerController {
    @RequestMapping("/")
    public String home(){
        return "index";
    }

    @RequestMapping("/login/page")
    public String login(){
        return "login";
    }

    @PostMapping("/login")
    public String login(String username,String password){
        UsernamePasswordToken token = new UsernamePasswordToken(username,password);

        Subject subject = SecurityUtils.getSubject();

        subject.login(token);

        return "success";
    }

    @RequestMapping("/success")
    public String success(){
        return "success";
    }

    @RequestMapping("/404")
    public String notFound(){
        return "404";
    }


    @RequestMapping("/other")
    public String other(){
        return "other";
    }
}
