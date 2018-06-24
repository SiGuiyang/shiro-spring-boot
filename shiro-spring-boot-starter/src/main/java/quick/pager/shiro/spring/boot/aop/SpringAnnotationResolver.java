package quick.pager.shiro.spring.boot.aop;


import org.apache.shiro.aop.AnnotationResolver;
import org.apache.shiro.aop.MethodInvocation;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.util.ClassUtils;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;

/**
 * {@code AnnotationResolver} implementation that uses Spring's more robust
 * {@link AnnotationUtils AnnotationUtils} to find method annotations instead of the JDKs simpler
 * (and rather lacking) {@link Method}.{@link Method#getAnnotation(Class) getAnnotation(class)}
 * implementation.
 *
 * @since 1.1
 */
public class SpringAnnotationResolver implements AnnotationResolver {

    public Annotation getAnnotation(MethodInvocation mi, Class<? extends Annotation> clazz) {
        Method m = mi.getMethod();

        Annotation a = AnnotationUtils.findAnnotation(m, clazz);
        if (a != null) return a;

        //The MethodInvocation's method object could be a method defined in an interface.
        //However, if the annotation existed in the interface's implementation (and not
        //the interface itself), it won't be on the above method object.  Instead, we need to
        //acquire the method representation from the targetClass and check directly on the
        //implementation itself:
        Class<?> targetClass = mi.getThis().getClass();
        m = ClassUtils.getMostSpecificMethod(m, targetClass);
        a = AnnotationUtils.findAnnotation(m, clazz);
        if (a != null) return a;
        // See if the class has the same annotation
        return AnnotationUtils.findAnnotation(mi.getThis().getClass(), clazz);
    }
}