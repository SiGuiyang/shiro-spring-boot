package quick.pager.shiro.spring.boot;


import org.apache.shiro.web.mgt.CookieRememberMeManager;
import org.apache.shiro.web.servlet.Cookie;
import org.apache.shiro.web.servlet.ShiroHttpSession;
import org.apache.shiro.web.servlet.SimpleCookie;
import org.springframework.boot.Banner;
import org.springframework.util.ObjectUtils;
import quick.pager.shiro.spring.boot.constants.Constants;

public abstract class AbstractShiroConfiguration {


    protected static Banner.Mode shiro = Banner.Mode.CONSOLE;

    /**
     * 构建 Shiro Banner文本
     */
    protected String buildBannerText() {
        StringBuilder builder = new StringBuilder();

        builder.append(System.lineSeparator()).append(Constants.BANNER_LOGO).append(" :: Shiro ::       (V")
                .append(Version.version).append(")").append(System.lineSeparator());
        return builder.toString();
    }

    /**
     * 构建 Session Cookie 模板
     */
    protected Cookie sessionCookieTemplate(ShiroProperties properties) {
        if (ObjectUtils.isEmpty(properties.getSession()) || ObjectUtils.isEmpty(properties.getSession().getCookie())) {
           return buildCookie(ShiroHttpSession.DEFAULT_SESSION_ID_NAME,SimpleCookie.DEFAULT_MAX_AGE,"","",false);
        }
        return buildCookie(
                properties.getSession().getCookie().getName(),
                properties.getSession().getCookie().getMaxAge(),
                properties.getSession().getCookie().getPath(),
                properties.getSession().getCookie().getDomain(),
                properties.getSession().getCookie().getSecure());
    }

    /**
     * 构建remembeMe Cookie 模板
     */
    protected Cookie rememberMeCookieTemplate(ShiroProperties properties) {
        if (ObjectUtils.isEmpty(properties.getCookie())) {
            return buildCookie(CookieRememberMeManager.DEFAULT_REMEMBER_ME_COOKIE_NAME,60 * 60 * 24 * 365,"","",false);
        }

        return buildCookie(
                properties.getCookie().getName(),
                properties.getCookie().getMaxAge(),
                properties.getCookie().getPath(),
                properties.getCookie().getDomain(),
                properties.getCookie().getSecure());
    }

    protected Cookie buildCookie(String name, int maxAge, String path, String domain, boolean secure) {
        Cookie cookie = new SimpleCookie(name);
        cookie.setHttpOnly(true);
        cookie.setMaxAge(maxAge);
        cookie.setPath(path);
        cookie.setDomain(domain);
        cookie.setSecure(secure);

        return cookie;
    }

}
