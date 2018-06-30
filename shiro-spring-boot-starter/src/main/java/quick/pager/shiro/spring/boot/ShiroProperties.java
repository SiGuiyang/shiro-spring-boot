package quick.pager.shiro.spring.boot;

import org.apache.shiro.realm.Realm;
import org.apache.shiro.session.mgt.eis.JavaUuidSessionIdGenerator;
import org.apache.shiro.session.mgt.eis.SessionIdGenerator;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.NestedConfigurationProperty;
import quick.pager.shiro.spring.boot.beans.Cookie;
import quick.pager.shiro.spring.boot.beans.Session;

import javax.servlet.Filter;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Shiro properties
 *
 * @author Pager
 * @version 1.0.0
 * @since 1.0.0
 */
@ConfigurationProperties(prefix = "spring.shiro")
public class ShiroProperties {

    /**
     * 授权的Realm
     */
    private Class<? extends Realm> realmClass;
    /**
     * 配置权限路由
     */
    private Map<String, String> filterChainDefinitionMap = new LinkedHashMap<>(); //urlPathExpression_to_comma-delimited-filter-chain-definition
    /**
     * 登录地址
     */
    private String loginUrl;
    /**
     * 登录成功的地址
     */
    private String successUrl;
    /**
     * 未授权的地址
     */
    private String unauthorizedUrl;
    /**
     * 自定义过滤器
     */
    private Map<String,Class<? extends Filter>> filters = new LinkedHashMap<>();
    /**
     * cookie 配置
     */
    @NestedConfigurationProperty
    private Cookie cookie;

    @NestedConfigurationProperty
    private Session session;

    /**
     * 默认sessionId 生成方式
     */
    private Class<? extends SessionIdGenerator> idGenerator = JavaUuidSessionIdGenerator.class;

    /**
     * The default active sessions cache name, equal to {@code shiro-activeSessionCache}.
     */
    private String activeSessionCacheName = "shiro-activeSessionCache";
    /**
     * 全局session 过期时间
     */
    private Long globalSessionTimeout = 30 * 60 * 60 * 1000L;
    /**
     * aop实现方式<br />
     * false: jdk代理
     * true: cglib代理
     */
    private Boolean proxyTargetClass = Boolean.FALSE;

    private Boolean useNativeSessionManager = Boolean.FALSE;

    public Class<? extends Realm> getRealmClass() {
        return realmClass;
    }

    public void setRealmClass(Class<? extends Realm> realmClass) {
        this.realmClass = realmClass;
    }

    public Map<String, String> getFilterChainDefinitionMap() {
        return filterChainDefinitionMap;
    }

    public void setFilterChainDefinitionMap(Map<String, String> filterChainDefinitionMap) {
        this.filterChainDefinitionMap = filterChainDefinitionMap;
    }

    public String getLoginUrl() {
        return loginUrl;
    }

    public void setLoginUrl(String loginUrl) {
        this.loginUrl = loginUrl;
    }

    public String getSuccessUrl() {
        return successUrl;
    }

    public void setSuccessUrl(String successUrl) {
        this.successUrl = successUrl;
    }

    public String getUnauthorizedUrl() {
        return unauthorizedUrl;
    }

    public void setUnauthorizedUrl(String unauthorizedUrl) {
        this.unauthorizedUrl = unauthorizedUrl;
    }

    public Map<String, Class<? extends Filter>> getFilters() {
        return filters;
    }

    public void setFilters(Map<String, Class<? extends Filter>> filters) {
        this.filters = filters;
    }

    public Cookie getCookie() {
        return cookie;
    }

    public void setCookie(Cookie cookie) {
        this.cookie = cookie;
    }

    public Session getSession() {
        return session;
    }

    public void setSession(Session session) {
        this.session = session;
    }

    public Class<? extends SessionIdGenerator> getIdGenerator() {
        return idGenerator;
    }

    public void setIdGenerator(Class<? extends SessionIdGenerator> idGenerator) {
        this.idGenerator = idGenerator;
    }

    public String getActiveSessionCacheName() {
        return activeSessionCacheName;
    }

    public void setActiveSessionCacheName(String activeSessionCacheName) {
        this.activeSessionCacheName = activeSessionCacheName;
    }

    public Long getGlobalSessionTimeout() {
        return globalSessionTimeout;
    }

    public void setGlobalSessionTimeout(Long globalSessionTimeout) {
        this.globalSessionTimeout = globalSessionTimeout;
    }

    public Boolean getProxyTargetClass() {
        return proxyTargetClass;
    }

    public void setProxyTargetClass(Boolean proxyTargetClass) {
        this.proxyTargetClass = proxyTargetClass;
    }

    public Boolean getUseNativeSessionManager() {
        return useNativeSessionManager;
    }

    public void setUseNativeSessionManager(Boolean useNativeSessionManager) {
        this.useNativeSessionManager = useNativeSessionManager;
    }
}
