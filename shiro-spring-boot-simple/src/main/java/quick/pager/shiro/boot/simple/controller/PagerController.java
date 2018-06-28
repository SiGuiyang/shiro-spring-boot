package quick.pager.shiro.boot.simple.controller;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.apache.shiro.authz.annotation.RequiresRoles;
import org.apache.shiro.subject.Subject;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/admin")
public class PagerController {
    @GetMapping("/")
    public String home(){
        return "index";
    }

    @GetMapping(value = "/login")
    public String login(){
        return "login";
    }

    @PostMapping(value = "/login")
    public String login(String username,String password){
        UsernamePasswordToken token = new UsernamePasswordToken(username,password);

        Subject subject = SecurityUtils.getSubject();

        subject.login(token);

        return "success";
    }


    @RequestMapping("/logout")
    public String logout(){
        Subject subject = SecurityUtils.getSubject();
        subject.logout();

        return "redirect: /admin/login";
    }

    @GetMapping("/success")
    @RequiresPermissions("admin:success")
    public String success(){
        return "success";
    }
    @GetMapping("/role/success")
    @RequiresPermissions("admin:role:success")
    public String success1(){
        return "success";
    }

    @RequestMapping("/404")
    public String notFound(){
        return "404";
    }


    @GetMapping("/other")
    @RequiresPermissions("admin:other")
    public String other(){
        return "other";
    }

    @GetMapping("/role/sp")
    @RequiresPermissions("admin:role:sp")
    public String sp(){
        return "sp";
    }

    @GetMapping("/role/sp1")
    @RequiresRoles("sp")
    public String sp1(){
        return "sp";
    }

    @GetMapping("/other1")
    @RequiresRoles("admin")
    public String other1(){
        return "other";
    }

}
