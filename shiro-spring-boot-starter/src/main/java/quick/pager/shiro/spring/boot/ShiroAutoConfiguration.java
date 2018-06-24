package quick.pager.shiro.spring.boot;

import org.apache.shiro.mgt.RememberMeManager;
import org.apache.shiro.mgt.SecurityManager;
import org.apache.shiro.mgt.SubjectDAO;
import org.apache.shiro.realm.Realm;
import org.apache.shiro.session.mgt.SessionFactory;
import org.apache.shiro.session.mgt.SessionManager;
import org.apache.shiro.session.mgt.eis.EnterpriseCacheSessionDAO;
import org.apache.shiro.session.mgt.eis.SessionDAO;
import org.apache.shiro.session.mgt.eis.SessionIdGenerator;
import org.apache.shiro.web.mgt.CookieRememberMeManager;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.apache.shiro.web.session.mgt.DefaultWebSessionManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.aop.framework.autoproxy.DefaultAdvisorAutoProxyCreator;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.Banner;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;
import org.springframework.context.annotation.Import;
import org.springframework.util.Assert;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;
import quick.pager.shiro.spring.boot.annotation.EnableShiroConfiguration;
import quick.pager.shiro.spring.boot.beans.Session;
import quick.pager.shiro.spring.boot.interceptor.AuthorizationAttributeSourceAdvisor;

import javax.annotation.PostConstruct;

/**
 * Shiro Configuration
 *
 * @author Pager
 * @version 1.0.0
 * @since 1.0.0
 */
@Configuration
@ConditionalOnClass(ShiroFilterFactoryBean.class)
@ConditionalOnBean(annotation = EnableShiroConfiguration.class)
@EnableConfigurationProperties(ShiroProperties.class)
@Import(ShiroConfiguration.class)
public class ShiroAutoConfiguration extends AbstractShiroConfiguration {


    private static final Logger logger = LoggerFactory.getLogger(ShiroAutoConfiguration.class);


    private final ShiroProperties properties;


    @Autowired
    public ShiroAutoConfiguration(ShiroProperties properties) {
        super();
        this.properties = properties;
    }

    @PostConstruct
    public void init() {

        String printText = this.buildBannerText();

        if (shiro == Banner.Mode.CONSOLE) {
            System.out.println(printText);
        } else if (shiro == Banner.Mode.LOG) {
            logger.info(printText);
        }

        boolean exists = !CollectionUtils.isEmpty(this.properties.getFilterChainDefinitionMap());
        Assert.state(exists, "Shiro configuration properties not found");
    }


    @Bean
    @ConditionalOnMissingBean
    public Realm realm() {
        return BeanUtils.instantiateClass(this.properties.getRealmClass());
    }


    /**
     * SessionDAO
     *
     * @return SessionDAO
     */
    @Bean
    @ConditionalOnMissingBean
    public SessionDAO sessionDAO() {
        EnterpriseCacheSessionDAO sessionDAO = new EnterpriseCacheSessionDAO();
        sessionDAO.setActiveSessionsCacheName(this.properties.getActiveSessionCacheName());
        Class<? extends SessionIdGenerator> idGenerator = this.properties.getIdGenerator();

        if (idGenerator != null) {
            SessionIdGenerator sessionIdGenerator = BeanUtils.instantiateClass(idGenerator);

            sessionDAO.setSessionIdGenerator(sessionIdGenerator);
        }
        return sessionDAO;
    }




    /**
     * session 管理
     *
     * @param sessionDAO sessionDao
     * @return SessionManager
     */
    @Bean
    @ConditionalOnMissingBean
    public SessionManager sessionManager(SessionDAO sessionDAO, SessionFactory sessionFactory) {
        DefaultWebSessionManager sessionManager = new DefaultWebSessionManager();
        if (ObjectUtils.isEmpty(this.properties.getSession())) {
            Session session = new Session();
            sessionManager.setSessionIdCookieEnabled(session.getSessionIdCookieEnabled());
            sessionManager.setSessionIdUrlRewritingEnabled(session.getSessionIdUrlRewritingEnabled());
        } else {
            sessionManager.setSessionIdCookieEnabled(this.properties.getSession().getSessionIdCookieEnabled());
            sessionManager.setSessionIdUrlRewritingEnabled(this.properties.getSession().getSessionIdUrlRewritingEnabled());
        }

        sessionManager.setSessionDAO(sessionDAO);
        sessionManager.setSessionIdCookie(this.sessionCookieTemplate(this.properties));

        sessionManager.setSessionFactory(sessionFactory);
        sessionManager.setGlobalSessionTimeout(this.properties.getGlobalSessionTimeout());

        if (ObjectUtils.isEmpty(this.properties.getSession())) {
            sessionManager.setDeleteInvalidSessions(Boolean.TRUE);
        } else {
            sessionManager.setDeleteInvalidSessions(this.properties.getSession().getDeleteInvalidSessions());
        }

        return sessionManager;
    }

    /**
     * rememberMeManager
     *
     * @return rememberMeManager
     */
    @Bean
    @ConditionalOnMissingBean
    public RememberMeManager rememberMeManager() {
        CookieRememberMeManager cookieRememberMeManager = new CookieRememberMeManager();
        cookieRememberMeManager.setCookie(this.rememberMeCookieTemplate(this.properties));
        return cookieRememberMeManager;
    }

    /**
     * 安全管理
     *
     * @param realm             realm
     * @param sessionManager    sessionManager
     * @param subjectDAO        subjectDAO
     * @param rememberMeManager rememberMeManager
     * @return SecurityManager
     */
    @Bean
    @ConditionalOnMissingBean
    public SecurityManager securityManager(Realm realm, SessionManager sessionManager, SubjectDAO subjectDAO, RememberMeManager rememberMeManager) {
        DefaultWebSecurityManager defaultWebSecurityManager = new DefaultWebSecurityManager();
        if (!ObjectUtils.isEmpty(subjectDAO)) {
            defaultWebSecurityManager.setSubjectDAO(subjectDAO);
        }

        if (!ObjectUtils.isEmpty(rememberMeManager)) {
            defaultWebSecurityManager.setRememberMeManager(rememberMeManager);
        }

        if (!ObjectUtils.isEmpty(sessionManager)) {
            defaultWebSecurityManager.setSessionManager(sessionManager);
        }

        defaultWebSecurityManager.setRealm(realm);

        return defaultWebSecurityManager;
    }


    @Bean
    @ConditionalOnMissingBean
    protected AuthorizationAttributeSourceAdvisor authorizationAttributeSourceAdvisor(SecurityManager securityManager) {
        AuthorizationAttributeSourceAdvisor advisor = new AuthorizationAttributeSourceAdvisor();
        advisor.setSecurityManager(securityManager);
        return advisor;
    }

    /**
     * 权限filter配置
     *
     * @param securityManager securityManager
     * @return ShiroFilterFactoryBean
     */
    @Bean
    @ConditionalOnMissingBean
    public ShiroFilterFactoryBean shiroFilter(SecurityManager securityManager) {
        ShiroFilterFactoryBean shiroFilterFactoryBean = new ShiroFilterFactoryBean();
        shiroFilterFactoryBean.setSecurityManager(securityManager);
        if (StringUtils.hasLength(this.properties.getSuccessUrl())) {
            shiroFilterFactoryBean.setSuccessUrl(this.properties.getSuccessUrl());
        }
        if (StringUtils.hasLength(this.properties.getLoginUrl())) {
            shiroFilterFactoryBean.setLoginUrl(this.properties.getLoginUrl());
        }

        if (StringUtils.hasLength(this.properties.getUnauthorizedUrl())) {
            shiroFilterFactoryBean.setUnauthorizedUrl(this.properties.getUnauthorizedUrl());
        }

        if (!CollectionUtils.isEmpty(this.properties.getFilterChainDefinitionMap())) {
            shiroFilterFactoryBean.setFilterChainDefinitionMap(this.properties.getFilterChainDefinitionMap());
        }

        return shiroFilterFactoryBean;
    }

    @Bean
    @ConditionalOnMissingBean
    @DependsOn("lifecycleBeanPostProcessor")
    public DefaultAdvisorAutoProxyCreator defaultAdvisorAutoProxyCreator() {
        DefaultAdvisorAutoProxyCreator defaultAdvisorAutoProxyCreator = new DefaultAdvisorAutoProxyCreator();
        defaultAdvisorAutoProxyCreator.setProxyTargetClass(this.properties.getProxyTargetClass());
        return defaultAdvisorAutoProxyCreator;
    }
}
