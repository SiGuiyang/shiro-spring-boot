package quick.pager.shiro.spring.boot.remoting;


import org.apache.shiro.SecurityUtils;
import org.apache.shiro.mgt.SecurityManager;
import org.apache.shiro.subject.ExecutionException;
import org.apache.shiro.subject.Subject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.remoting.support.DefaultRemoteInvocationExecutor;
import org.springframework.remoting.support.RemoteInvocation;

import java.io.Serializable;
import java.lang.reflect.InvocationTargetException;
import java.util.concurrent.Callable;


/**
 * An implementation of the Spring {@link org.springframework.remoting.support.RemoteInvocationExecutor}
 * that binds a {@code sessionId} to the incoming thread to make it available to the {@code SecurityManager}
 * implementation during the thread execution.  The {@code SecurityManager} implementation can use this sessionId
 * to reconstitute the {@code Subject} instance based on persistent state in the corresponding {@code Session}.
 *
 * @since 0.1
 */
public class SecureRemoteInvocationExecutor extends DefaultRemoteInvocationExecutor {

    //TODO - complete JavaDoc

    /*--------------------------------------------
    |             C O N S T A N T S             |
    ============================================*/

    /*--------------------------------------------
    |    I N S T A N C E   V A R I A B L E S    |
    ============================================*/
    private static final Logger log = LoggerFactory.getLogger(SecureRemoteInvocationExecutor.class);

    /**
     * The SecurityManager used to retrieve realms that should be associated with the
     * created <tt>Subject</tt>s upon remote invocation.
     */
    private SecurityManager securityManager;

    /*--------------------------------------------
    |         C O N S T R U C T O R S           |
    ============================================*/

    /*--------------------------------------------
    |  A C C E S S O R S / M O D I F I E R S    |
    ============================================*/

    public void setSecurityManager(SecurityManager securityManager) {
        this.securityManager = securityManager;
    }

    /*--------------------------------------------
    |               M E T H O D S               |
    ============================================*/
    @SuppressWarnings({"unchecked"})
    public Object invoke(final RemoteInvocation invocation, final Object targetObject)
            throws NoSuchMethodException, IllegalAccessException, InvocationTargetException {

        try {
            SecurityManager securityManager =
                    this.securityManager != null ? this.securityManager : SecurityUtils.getSecurityManager();

            Subject.Builder builder = new Subject.Builder(securityManager);

            String host = (String) invocation.getAttribute(SecureRemoteInvocationFactory.HOST_KEY);
            if (host != null) {
                builder.host(host);
            }

            Serializable sessionId = invocation.getAttribute(SecureRemoteInvocationFactory.SESSION_ID_KEY);
            if (sessionId != null) {
                builder.sessionId(sessionId);
            } else {
                if (log.isTraceEnabled()) {
                    log.trace("RemoteInvocation did not contain a Shiro Session id attribute under " +
                            "key [" + SecureRemoteInvocationFactory.SESSION_ID_KEY + "].  A Subject based " +
                            "on an existing Session will not be available during the method invocation.");
                }
            }

            Subject subject = builder.buildSubject();
            return subject.execute(new Callable() {
                public Object call() throws Exception {
                    return SecureRemoteInvocationExecutor.super.invoke(invocation, targetObject);
                }
            });
        } catch (ExecutionException e) {
            Throwable cause = e.getCause();
            if (cause instanceof NoSuchMethodException) {
                throw (NoSuchMethodException) cause;
            } else if (cause instanceof IllegalAccessException) {
                throw (IllegalAccessException) cause;
            } else if (cause instanceof InvocationTargetException) {
                throw (InvocationTargetException) cause;
            } else {
                throw new InvocationTargetException(cause);
            }
        } catch (Throwable t) {
            throw new InvocationTargetException(t);
        }
    }
}
