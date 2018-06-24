package quick.pager.shiro.spring.boot.interceptor;

import org.apache.shiro.authz.annotation.RequiresAuthentication;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.apache.shiro.authz.annotation.RequiresRoles;
import org.apache.shiro.authz.annotation.RequiresUser;
import org.apache.shiro.mgt.SecurityManager;
import org.springframework.aop.support.StaticMethodMatcherPointcutAdvisor;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.lang.Nullable;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;


/**
 * @since 0.1
 */
public class AuthorizationAttributeSourceAdvisor extends StaticMethodMatcherPointcutAdvisor {

    private static final List<Class<? extends Annotation>> AUTHZ_ANNOTATION_CLASSES = new ArrayList<>();

    static {
        AUTHZ_ANNOTATION_CLASSES.add(RequiresPermissions.class);
        AUTHZ_ANNOTATION_CLASSES.add(RequiresRoles.class);
        AUTHZ_ANNOTATION_CLASSES.add(RequiresUser.class);
        AUTHZ_ANNOTATION_CLASSES.add(RequiresAuthentication.class);
    }

    private SecurityManager securityManager = null;

    /**
     * Create a new AuthorizationAttributeSourceAdvisor.
     */
    public AuthorizationAttributeSourceAdvisor() {
        setAdvice(new AopAllianceAnnotationsAuthorizingMethodInterceptor());
    }

    public SecurityManager getSecurityManager() {
        return securityManager;
    }

    public void setSecurityManager(SecurityManager securityManager) {
        this.securityManager = securityManager;
    }

    /**
     * Returns <tt>true</tt> if the method or the class has any Shiro annotations, false otherwise.
     * The annotations inspected are:
     * <ul>
     * <li>{@link RequiresAuthentication RequiresAuthentication}</li>
     * <li>{@link RequiresUser RequiresUser}</li>
     * <li>{@link org.apache.shiro.authz.annotation.RequiresGuest RequiresGuest}</li>
     * <li>{@link RequiresRoles RequiresRoles}</li>
     * <li>{@link RequiresPermissions RequiresPermissions}</li>
     * </ul>
     *
     * @param method      the method to check for a Shiro annotation
     * @param targetClass the class potentially declaring Shiro annotations
     * @return <tt>true</tt> if the method has a Shiro annotation, false otherwise.
     * @see org.springframework.aop.MethodMatcher#matches(Method, Class)
     */
    public boolean matches(Method method, @Nullable Class<?> targetClass) {

        if (isAuthzAnnotationPresent(method)) {
            return true;
        }

        //The 'method' parameter could be from an interface that doesn't have the annotation.
        //Check to see if the implementation has it.
        if (targetClass != null) {
            try {
                method = targetClass.getMethod(method.getName(), method.getParameterTypes());
                return isAuthzAnnotationPresent(method) || isAuthzAnnotationPresent(targetClass);
            } catch (NoSuchMethodException ignored) {
                //default return value is false.  If we can't find the method, then obviously
                //there is no annotation, so just use the default return value.
            }
        }

        return false;
    }

    private boolean isAuthzAnnotationPresent(Class<?> targetClazz) {
        for (Class<? extends Annotation> annClass : AUTHZ_ANNOTATION_CLASSES) {
            Annotation a = AnnotationUtils.findAnnotation(targetClazz, annClass);
            if (a != null) {
                return true;
            }
        }
        return false;
    }

    private boolean isAuthzAnnotationPresent(Method method) {
        for (Class<? extends Annotation> annClass : AUTHZ_ANNOTATION_CLASSES) {
            Annotation a = AnnotationUtils.findAnnotation(method, annClass);
            if (a != null) {
                return true;
            }
        }
        return false;
    }

}

