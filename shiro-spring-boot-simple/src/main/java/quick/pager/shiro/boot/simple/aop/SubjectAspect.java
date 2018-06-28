package quick.pager.shiro.boot.simple.aop;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

@Component
@Aspect
public class SubjectAspect {

    @Around("execution(* quick.pager.shiro.boot.simple.controller..*(..))")
    public Object doAspect(ProceedingJoinPoint pjp) throws Throwable {
        Subject subject = SecurityUtils.getSubject();

        Object obj = pjp.proceed();

        System.out.println("权限 [admin:other]=======" + subject.isPermitted("admin:other"));
        System.out.println("权限 [admin:success]=======" + subject.isPermitted("admin:success"));
        System.out.println("权限 [admin:role:sp]=======" + subject.isPermitted("admin:role:sp"));
        System.out.println("权限 [admin:role:success]=======" + subject.isPermitted("admin:role:success"));
        System.out.println("权限 [admin:role:sp]=======" + subject.isPermitted("admin:role:sp"));
        System.out.println("角色 [sp]=======" + subject.hasRole("sp"));
        System.out.println("角色 [admin]=======" + subject.hasRole("admin"));

        return obj;
    }
}
